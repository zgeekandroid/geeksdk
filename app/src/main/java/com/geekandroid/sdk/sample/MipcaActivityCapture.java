
/*******************************************************************************
 *
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.geekandroid.sdk.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.geekandroid.sdk.qrcode.ScannerActivity;
import com.geekandroid.sdk.qrcode.ViewfinderView;
import com.google.zxing.Result;

/**
 * @describle huangshiyang
 */

public class MipcaActivityCapture extends ScannerActivity implements View.OnClickListener {
    private static final String TAG = MipcaActivityCapture.class.getSimpleName();

    // 选择相册二维码
    private LinearLayout llyout_album;
    private ImageView capture_flashlight;



/**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);

        LinearLayout mButtonBack = (LinearLayout) findViewById(R.id.button_back);
        mButtonBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MipcaActivityCapture.this.finish();

            }
        });
        llyout_album = (LinearLayout) findViewById(R.id.llyout_album);
        // 按钮监听事件
        capture_flashlight = (ImageView) findViewById(R.id.capture_flashlight);
        capture_flashlight.setOnClickListener(this);
        llyout_album.setOnClickListener(this);
    }

    @Override
    public SurfaceView getSurfaceView() {
        return (SurfaceView) findViewById(R.id.preview_view);
    }

    @Override
    public ViewfinderView getViewfinderView() {
        return (ViewfinderView) findViewById(R.id.viewfinder_view);
    }



/**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */


    @Override
    public void handleDecodeResult(Result rawResult, Bitmap barcode, float scaleFactor) {
        String resultString = rawResult.getText();
        if (resultString.equals("")) {
            Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", resultString);
            bundle.putParcelable("bitmap", barcode);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
        }
        MipcaActivityCapture.this.finish();
    }

    @Override
    public void handleDecodePickPhotoResult(String rawResult) {
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("result", rawResult);
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.capture_flashlight:

                //赏光灯，点击开启的图片和关闭的图
                if (isFlashlightOpen()) {
                    //关闭图
                    capture_flashlight.setImageResource(R.mipmap.close);
                } else {
                    // 开启图片
                    capture_flashlight.setImageResource(R.mipmap.turn);
                }

                toggleFlashLight();

                break;
            // 从相册选择二维码
            case R.id.llyout_album: {
                pickPhoto();
            }
            break;
            default:
                break;
        }
    }

}

