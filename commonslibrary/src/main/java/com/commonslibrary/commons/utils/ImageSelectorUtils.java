package com.commonslibrary.commons.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.commonslibrary.commons.R;
import com.commonslibrary.commons.config.SystemConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * date        :  2015-12-02  11:26
 * author      :  Mickaecle gizthon
 * description :
 */
public class ImageSelectorUtils {
    /* 请求码 */
    public static final int PICK_FROM_FILE = 3;
    public static final int PICK_FROM_CAMERA = 1;
    public static final int CROP_FROM_CAMERA = 2;
    public static final int REQUEST_CODE = 234;

    private   Activity context;


    public ImageSelectorUtils(Activity ctx) {
        context = ctx;
    }



    public void selectImage(final SelectorCallBack callBack) {
        final Dialog builder = new Dialog(context, R.style.dialog);
        sex = "男";
        View payWayDialogView = View.inflate(context, R.layout.dialog_select_head, null);
        TextView tv_photo = (TextView) payWayDialogView.findViewById(R.id.tv_photo);
        RadioGroup radioGroup = (RadioGroup) payWayDialogView.findViewById(R.id.rg_sex);
        final RadioButton man = (RadioButton) payWayDialogView.findViewById(R.id.rb_man);
        final RadioButton woman = (RadioButton) payWayDialogView.findViewById(R.id.rb_women);
        TextView tv_camera = (TextView) payWayDialogView.findViewById(R.id.tv_camera);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == man.getId()) {
                    sex = "男";
                } else if (checkedId == woman.getId()) {
                    sex = "女";
                }
            }
        });
        tv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               
                builder.dismiss();
            }
        });
       
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sex.equals("男")){
                    takePickture(callBack);
                }else{
                    pickPickture();
                }
                builder.dismiss();
            }
        });
        builder.setContentView(payWayDialogView);

        WindowManager.LayoutParams lp = builder.getWindow().getAttributes();
        lp.width = (int) (DeviceUtils.getScreenWidth(context) * 0.8); //设置宽度
        builder.getWindow().setAttributes(lp);
        builder.show();
    }
    /**
     * 如果想要重写对话框就不用调用这个方法
     *
     * @param callBack 回调接口
     */
    String sex="男";

    public void takePickture(final SelectorCallBack callBack) {
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imgUri = Uri.fromFile(generateImagePath());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        callBack.callBack(imgUri);
        context.startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void pickPickture() {
        // 方式1，直接打开图库，只能选择图库的图片
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 方式2，会先让用户选择接收到该请求的APP，可以从文件系统直接选取图片
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        context.startActivityForResult(i, PICK_FROM_FILE);
    }


    public static void pickPickture(Activity context, String title) {
        Intent innerIntent = new Intent(); // "android.intent.action.GET_CONTENT"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            innerIntent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        // innerIntent.setAction(Intent.ACTION_GET_CONTENT);

        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, title);
        context.startActivityForResult(wrapperIntent, REQUEST_CODE);
    }


    public File generateImagePath() {
        return new File(SystemConfig.getSystemImageDir(), "img_" + String.valueOf(DateUtils.timeStampToFormatString(System.currentTimeMillis(), "yyyyMMddhhmmss")) + ".png");
    }


    public String getAbsoluteImagePath(Uri uri) {
        String path = "";
        try {
            // can post image
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(context, uri, proj, null, null, null);
            Cursor cursor = loader.loadInBackground();

            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path = cursor.getString(column_index);
            cursor.close();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return path;
    }


    public interface SelectorCallBack {
        void callBack(Uri uri);
    }

    public void cropImage(final Uri imgUri, DialogInterface.OnCancelListener onCancelListener) {

        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(
                intent, 0);
        int size = list.size();

        if (size == 0) {
            ToastUtils.show(context, "没有找到合适的裁剪工具!");
            return;
        }

        intent.setData(imgUri);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        // only one
        if (size == 1) {
            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.setComponent(new ComponentName(res.activityInfo.packageName,
                    res.activityInfo.name));
            context.startActivityForResult(i, ImageSelectorUtils.CROP_FROM_CAMERA);
        } else {
            // many crop app
            for (ResolveInfo res : list) {
                final CropOption co = new CropOption();
                co.title = context.getPackageManager().getApplicationLabel(
                        res.activityInfo.applicationInfo);
                co.icon = context.getPackageManager().getApplicationIcon(
                        res.activityInfo.applicationInfo);
                co.appIntent = new Intent(intent);
                co.appIntent
                        .setComponent(new ComponentName(
                                res.activityInfo.packageName,
                                res.activityInfo.name));
                cropOptions.add(co);
            }

            CropOptionAdapter adapter = new CropOptionAdapter(
                    context.getApplicationContext(), cropOptions);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("选择一个裁剪工具");
            builder.setAdapter(adapter,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            context.startActivityForResult(
                                    cropOptions.get(item).appIntent,
                                    ImageSelectorUtils.CROP_FROM_CAMERA);
                        }
                    });
            builder.setOnCancelListener(onCancelListener);
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    /**
     * 调整图片的角度
     *
     * @param imagePath 需要调整的图片的路径
     * @return 调整之后的uri
     * @throws IOException
     */
    public Uri adjustDegree(String imagePath) {
        int degree = getBitmapDegree(imagePath);
        Uri beforeUri = Uri.fromFile(new File(imagePath));
        try {
            Bitmap beforBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), beforeUri);
            Bitmap afterBitmap = rotateBitmapByDegree(beforBitmap, degree);
            Uri afterUri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), afterBitmap, null, null));
            return afterUri;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beforeUri;
    }

    public File saveBitmap(final Bitmap mBitmap) {
        File file = generateImagePath();
        FileOutputStream fOut = null;
        try {
            if (file.exists()) {
                boolean deleteSuccess = file.delete();
                LogUtils.i("文件已经存在,删除" + (deleteSuccess ? "成功" : "失败"));
            }
            fOut = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();

            LogUtils.i("保存文件成功!文件大小:" + StringUtils.formatKB(file.length()) + "，文件路径：" + file.getAbsolutePath());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = bm;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


}
