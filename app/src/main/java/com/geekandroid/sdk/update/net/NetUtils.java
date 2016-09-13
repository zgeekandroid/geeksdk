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

package com.geekandroid.sdk.update.net;

import com.commonslibrary.commons.net.DefaultOkHttpIml;
import com.commonslibrary.commons.net.IRequestRemote;
import com.commonslibrary.commons.net.RequestCallBack;

import java.util.Map;

/**
 * Created by lenovo on 2016/4/25.
 */
public class NetUtils<T> {
    static IRequestRemote mIRequestRemote = DefaultOkHttpIml.getInstance();

    public static void doGet(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {
        mIRequestRemote.doGet(url, parameters, callBack);
    }

    public static void doPost(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {
        mIRequestRemote.doPost(url, parameters, callBack);
    }
    public static void doDownLoad(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {
        mIRequestRemote.doDownLoad(url, parameters, callBack);
    }

}
