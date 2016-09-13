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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.commonslibrary.commons.config.SystemConfig;
import com.geekandroid.sdk.update.NotificationService;
import com.geekandroid.sdk.update.UpdateService;

/**
 * Created by lenovo on 2016/4/25.
 */
public class AppUpdateSampleFragment extends BaseSampleFragment implements View.OnClickListener {
    private static final String TYPE_DIALOG = "type_dialog";
    private static final String TYPE_NOTIFICATION = "type_notification";

    Button showDialog;
    Button showNotification;
    EditText et_url;
    EditText et_name;
    EditText et_filePath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getResLayoutId() {
        return R.layout.app_update;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showDialog = (Button) view.findViewById(R.id.showDialog);
        showNotification = (Button) view.findViewById(R.id.showNotification);
        et_url = (EditText) view.findViewById(R.id.et_url);
        et_name = (EditText) view.findViewById(R.id.et_name);
        et_filePath = (EditText) view.findViewById(R.id.et_filePath);
        et_filePath.setText(SystemConfig.getSystemFileDir());

        showDialog.setOnClickListener(this);
        showNotification.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showDialog: {
                toUpdateService(TYPE_DIALOG);
                break;
            }
            case R.id.showNotification: {
                toUpdateService(TYPE_NOTIFICATION);
                break;
            }
        }
    }

    private void toUpdateService(String type) {
        String url = et_url.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        String filePath = et_filePath.getText().toString().trim();

        if (type == TYPE_DIALOG) {
            UpdateService.startService(getActivity(), url, name, filePath);
        } else {
            NotificationService.startService(getActivity(), url, name, filePath);
        }

    }


}
