package com.geekandroid.sdk.sample.qrcodelibrary.qrcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.geekandroid.sdk.commons.utils.ToastUtils;
import com.geekandroid.sdk.sample.qrcodelibrary.R;
import com.geekandroid.sdk.sample.qrcodelibrary.qrcode.camera.AmbientLightManager;
import com.geekandroid.sdk.sample.qrcodelibrary.qrcode.camera.BeepManager;
import com.geekandroid.sdk.sample.qrcodelibrary.qrcode.camera.CameraManager;
import com.geekandroid.sdk.sample.qrcodelibrary.qrcode.camera.ImageSelectorUtils;
import com.geekandroid.sdk.sample.qrcodelibrary.qrcode.camera.IntentSource;
import com.geekandroid.sdk.sample.qrcodelibrary.qrcode.camera.decoding.CaptureActivityHandler;
import com.geekandroid.sdk.sample.qrcodelibrary.qrcode.camera.decoding.FinishListener;
import com.geekandroid.sdk.sample.qrcodelibrary.qrcode.camera.decoding.InactivityTimer;
import com.geekandroid.sdk.sample.qrcodelibrary.qrcode.camera.decoding.RGBLuminanceSource;
import com.geekandroid.sdk.sample.qrcodelibrary.qrcode.camera.decoding.Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

/**
 * date        :  2016-02-01  17:42
 * author      :  Mickaecle gizthon
 * description :
 */
public abstract class ScannerActivity extends Activity implements SurfaceHolder.Callback{
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Map<DecodeHintType, ?> decodeHints;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private boolean isFlashlightOpen;
    @SuppressWarnings("unused")
    private IntentSource source;
    /**
     * 声音震动管理器。如果扫描成功后可以播放一段音频，也可以震动提醒，可以通过配置来决定扫描成功后的行为。
     */
    private BeepManager beepManager;

    /**
     * 闪光灯调节器。自动检测环境光线强弱并决定是否开启闪光灯
     */
    private AmbientLightManager ambientLightManager;
    // 选择相册二维码
    private LinearLayout llyout_album;
    private String photo_path;
    private Bitmap scanBitmap;

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);







        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);
    }




    public boolean isFlashlightOpen() {
        return isFlashlightOpen;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        cameraManager = new CameraManager(getApplication());
        viewfinderView = getViewfinderView();
        viewfinderView.setCameraManager(cameraManager);

        SurfaceView surfaceView = getSurfaceView();
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            // 防止sdk8的设备初始化预览异常
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            surfaceHolder.addCallback(this);
        }
        decodeFormats = null;
        characterSet = null;

        // 加载声音配置，其实在BeemManager的构造器中也会调用该方法，即在onCreate的时候会调用一次
        beepManager.updatePrefs();

        // 启动闪光灯调节器
        ambientLightManager.start(cameraManager);

        // 恢复活动监控器
        inactivityTimer.onResume();

    }



    //********************************************

    //    需要继承这个类

    public abstract SurfaceView getSurfaceView();

    public abstract ViewfinderView getViewfinderView();


    //********************************************
    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();

        // 关闭摄像头
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = getSurfaceView();
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult
     *            The contents of the barcode.
     * @param scaleFactor
     *            amount by which thumbnail was scaled
     * @param barcode
     *            A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
//        Log.d(TAG, "handleDecode");
        inactivityTimer.onActivity();

        beepManager.playBeepSoundAndVibrate();


        handleDecodeResult(rawResult, barcode, scaleFactor);

    }

    /**
     * 处理结果
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    public abstract void handleDecodeResult(Result rawResult, Bitmap barcode, float scaleFactor);
    public abstract void handleDecodePickPhotoResult(String rawResult);



    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
//            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
            }

        } catch (IOException ioe) {
//            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
//            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.notice));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(android.R.string.yes, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
//            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }



    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    public void toggleFlashLight(){
        if (isFlashlightOpen) {
            cameraManager.setTorch(false); // 关闭闪光灯
            isFlashlightOpen = false;
        } else {
            cameraManager.setTorch(true); // 打开闪光灯
            isFlashlightOpen = true;
        }
    }




    public void pickPhoto(){
        ImageSelectorUtils.pickPickture(this, "选择二维码图片");
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case ImageSelectorUtils.REQUEST_CODE:

                    try {
                        String[] proj = { MediaStore.Images.Media.DATA };
                        // 获取选中图片的路径
                        Cursor cursor = getContentResolver().query(data.getData(), proj, null, null, null);
                        if (cursor != null){
                            if (cursor.moveToFirst()) {

                                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                photo_path = cursor.getString(column_index);
                                if (photo_path == null) {
                                    photo_path = Utils.getPath(getApplicationContext(), data.getData());
                                    Log.i("123path  ", photo_path);
                                }
                                Log.i("123path", photo_path);

                            }

                            cursor.close();
                        }

                        new Thread(new Runnable() {

                            @Override
                            public void run() {

                                Result result = scanningImage(photo_path);
                                // String result = decode(photo_path);


                                if (result == null) {
                                    Log.i("123", "   -----------");
                                    Looper.prepare();
                                    ToastUtils.show(ScannerActivity.this,"图片无法识别，请重新选择！");
                                    Looper.loop();
                                } else {
                                    Log.i("123result", result.toString());
                                    // Log.i("123result", result.getText());
                                    // 数据返回
                                    String recode = recode(result.toString());
                                    handleDecodePickPhotoResult(recode);

                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }

        }

    }

    /**
     * 中文乱码
     *
     * @author WangZhuang
     * @date 2015-6-26 上午9:36:50
     * @param str
     * @return
     */
    private String recode(String str) {
        String formart = "";

        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
                Log.i("1234      ISO8859-1", formart);
            } else {
                formart = str;
                Log.i("1234      stringExtra", str);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formart;
    }

    // TODO: 解析部分图片
    protected Result scanningImage(String path) {

        try {
            if (TextUtils.isEmpty(path)) {

                return null;

            }
            // DecodeHintType 和EncodeHintType
            Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; // 先获取原大小
            scanBitmap = BitmapFactory.decodeFile(path, options);
            options.inJustDecodeBounds = false; // 获取新的大小

            int sampleSize = (int) (options.outHeight / (float) 200);

            if (sampleSize <= 0)
                sampleSize = 1;
            options.inSampleSize = sampleSize;
            scanBitmap = BitmapFactory.decodeFile(path, options);

            // --------------测试的解析方法---PlanarYUVLuminanceSource-这几行代码对project没作功----------

            LuminanceSource source1 = new PlanarYUVLuminanceSource(rgb2YUV(scanBitmap), scanBitmap.getWidth(), scanBitmap.getHeight(), 0, 0, scanBitmap.getWidth(), scanBitmap.getHeight(), false);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source1));
            MultiFormatReader reader1 = new MultiFormatReader();
            Result result1;
            try {
                result1 = reader1.decode(binaryBitmap);
                String content = result1.getText();
                Log.i("123content", content);
            } catch (NotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            // ----------------------------

            RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
            QRCodeReader reader = new QRCodeReader();
            try {

                return reader.decode(bitmap1, hints);

            } catch (NotFoundException e) {

                e.printStackTrace();

            } catch (ChecksumException e) {

                e.printStackTrace();

            } catch (FormatException e) {

                e.printStackTrace();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * //TODO: TAOTAO 将bitmap由RGB转换为YUV //TOOD: 研究中
     *
     * @param bitmap
     *            转换的图形
     * @return YUV数据
     */
    public byte[] rgb2YUV(Bitmap bitmap) {
        // 该方法来自QQ空间
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int len = width * height;
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = pixels[i * width + j] & 0x00FFFFFF;

                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;

                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);

                yuv[i * width + j] = (byte) y;
                // yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
                // yuv[len + (i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }
}
