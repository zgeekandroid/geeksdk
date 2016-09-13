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

package com.geekandroid.sdk.jpushlibrary.push.impl;

import android.content.Context;

import com.geekandroid.sdk.jpushlibrary.push.IPush;
import com.geekandroid.sdk.jpushlibrary.push.IPushCallBack;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;


/**
 * date        :  2016-01-27  13:53
 * author      :  Mickaecle gizthon
 * description :
 */
public class JPushImpl implements IPush {
    private static JPushImpl instance;
    private  Context context;
    private boolean isDebug=false;

    private JPushImpl() {
    }



    public static JPushImpl getInstance() {
        if (null == instance) {
            instance = new JPushImpl();
        }
        return instance;
    }

    @Override
    public void init(Context context) {
        this.context = context;
        JPushInterface.init(context);
        JPushInterface.setDebugMode(isDebug);
    }

    public void onResume(Context context){
        JPushInterface.onResume(context);
    }

    public void onPause(Context context){
        JPushInterface.onPause(context);
    }


public void setDebugMode(boolean isDebug){
    this.isDebug=isDebug;
}

    @Override
    public void resumePush() {
        JPushInterface.resumePush(context);
    }


    @Override
    public void stopPush() {
        try {
            JPushInterface.stopPush(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearAllNotifications() {
        JPushInterface.clearAllNotifications(context);
    }

    @Override
    public void clearNotificationById(int id) {
        JPushInterface.clearNotificationById(context, id);
    }

    @Override
    public void setAliasAndTags(String alias, Set<String> tags, IPushCallBack callback) {
        JPushInterface.setAliasAndTags(context, alias, tags, callback);
    }

    @Override
    public void setAlias(String alias, IPushCallBack callback) {
        JPushInterface.setAlias(context, alias, callback);
    }




}
