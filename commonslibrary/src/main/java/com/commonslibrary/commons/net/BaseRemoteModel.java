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

package com.commonslibrary.commons.net;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.commonslibrary.commons.utils.AppUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * date        :  2016-02-22  17:31
 * author      :  Mickaecle gizthon
 * description :
 */
public class BaseRemoteModel implements IRequestRemote {

    private Context mContext;

    public BaseRemoteModel() {

    }

    public BaseRemoteModel(Object object) {

        if (object instanceof android.app.Fragment) {
            android.app.Fragment fragment = (android.app.Fragment) object;
            mContext = fragment.getActivity();

        } else if (object instanceof android.support.v4.app.Fragment) {
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) object;
            mContext = fragment.getActivity();
        } else if (object instanceof Activity) {
            mContext = (Activity) object;
        } else if (object instanceof Application) {
            mContext = ((Application) object).getApplicationContext();
        }

    }
    public void setTag(Object tag){
        DefaultOkHttpIml.getInstance().setTag(tag);
    }

    public void cancelRquest(Object tag){
        DefaultOkHttpIml.getInstance().cancelTag(tag);
    }

    public void cancelAllRequest(){
        DefaultOkHttpIml.getInstance().cancelAllTag();
    }

    public Map<String, Object> getExtraParameter() {
        Map<String, Object> parameters = new HashMap<>();

        if (mContext != null) {
            AppUtils.AppInfo appInfo = AppUtils.getAppInfo(mContext);
            if (appInfo != null){
                parameters.put("app_version", appInfo.getVersionName());
                parameters.put("app_code", appInfo.getVersionCode());
            }
        }
        return parameters;
    }




    @Override
    public <T> void doGet(String url, Map<String, Object> parameters, RequestCallBack<T> callBack) {
        parameters.putAll(getExtraParameter());
        DefaultOkHttpIml.getInstance().doGet(url, parameters, callBack);
    }

    @Override
    public <T> void doPost(String url, Map<String, Object> parameters, RequestCallBack<T> callBack) {
        parameters.putAll(getExtraParameter());
        DefaultOkHttpIml.getInstance().doPost(url, parameters, callBack);
    }

    @Override
    public <T> void doUpload(String url, Map<String, Object> parameters, Map<String, File> map, RequestCallBack<T> callBack) {
        parameters.putAll(getExtraParameter());
        DefaultOkHttpIml.getInstance().doUpload(url, parameters, map, callBack);
    }

    @Override
    public <T> void doDownLoad(String url, Map<String, Object> parameters, RequestCallBack<T> callBack) {
        parameters.putAll(getExtraParameter());
        DefaultOkHttpIml.getInstance().doDownLoad(url, parameters, callBack);
    }
}
