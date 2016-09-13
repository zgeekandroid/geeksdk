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
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.IOException;

/**
 * date        :  2016-04-20  13:39
 * author      :  Mickaecle gizthon
 * description :
 */
public class RxPermissionsSampleFragment extends BaseSampleFragment {
    private static final String TAG = "RxPermissionsSample";

    private Camera camera;
    private SurfaceView surfaceView;
    private RxPermissions rxPermissions;

    @Override
    public int getResLayoutId() {
        return R.layout.rxpermission;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        rxPermissions = RxPermissions.getInstance(getActivity());
        rxPermissions.setLogging(true);
        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
        RxView.clicks(view.findViewById(R.id.enableCamera))
                // Ask for permissions when button is clicked
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA))
                .subscribe(granted -> {
                            Log.i(TAG, " TRIGGER Received result " + granted);
                            if (granted) {
                                releaseCamera();
                                camera = Camera.open(0);
                                try {
                                    camera.setPreviewDisplay(surfaceView.getHolder());
                                    camera.startPreview();
                                } catch (IOException e) {
                                    Log.e(TAG, "Error while trying to display the camera preview", e);
                                }
                            } else {
                                Toast.makeText(getActivity(),
                                        "Permission denied, can't enable the camera",
                                        Toast.LENGTH_SHORT).show();
                            }
                        },
                        t -> Log.e(TAG, "onError", t),
                        () -> Log.i(TAG, "OnComplete")
                );
        return view;
    }
    @Override
    public void onStop() {
        super.onStop();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

}
