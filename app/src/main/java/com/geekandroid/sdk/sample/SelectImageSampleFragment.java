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

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.commonslibrary.commons.utils.ImageSelectorUtils;
import com.commonslibrary.commons.utils.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * date        :  2016-04-20  13:39
 * author      :  Mickaecle gizthon
 * description :
 */
public class SelectImageSampleFragment extends BaseSampleFragment {
    private static final String TAG = "CommonSampleFragment";
    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};

    @Override
    public int getResLayoutId() {
        return R.layout.fragment_select_photo;
    }


    ImageView show_img;
    private ImageSelectorUtils imageSelector;
    private Uri imgUri;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


       Button button = (Button) view.findViewById(R.id.show_select);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RxPermissions.getInstance(getActivity())
                            .request(android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(granted -> { // will emit 2 Permission objects
                                if (granted) {
                                    // `permission.name` is granted !
                                    showDialog();
                                } else {
                                    ToastUtils.show(getActivity(), "请在设置中打开相机,本地存储权限");
                                }
                            });
                }
            });

          show_img = (ImageView) view.findViewById(R.id.show_img);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        try {
            switch (requestCode) {
                case ImageSelectorUtils.PICK_FROM_CAMERA:
                    if (imgUri != null && imageSelector != null) {
                        imgUri = imageSelector.adjustDegree(imgUri.getPath());
                        cropImage();
                    }
                    break;
                case ImageSelectorUtils.PICK_FROM_FILE:
                    if (data != null && data.getData() != null) {
                        imgUri = data.getData();
                        cropImage();
                    }
                    break;
                case ImageSelectorUtils.CROP_FROM_CAMERA:
                    if (null != data) {
                        setCropImg(data);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void cropImage() {
        //        直接显示
        //        showImage.setUrl("file://"+imageSelector.getAbsoluteImagePath(imageUri));
        //裁剪
        imageSelector.cropImage(imgUri, new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().getContentResolver().delete(imgUri, null, null);
                imgUri = null;
            }
        });
    }

    private void setCropImg(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (null != bundle) {
            Bitmap mBitmap = bundle.getParcelable("data");

            File file = imageSelector.saveBitmap(mBitmap);

            //文件上传
            //            DefaultOkHttpIml.getInstance().doUpload("",null,file,null);

            show_img.setImageBitmap(mBitmap);


        }
    }

    private void showDialog() {
        if (imageSelector == null) {
            imageSelector = new ImageSelectorUtils(getActivity());
        }
        imageSelector.selectImage(new ImageSelectorUtils.SelectorCallBack() {
            @Override
            public void callBack(Uri uri) {
                imgUri = uri;
            }
        });
    }


}