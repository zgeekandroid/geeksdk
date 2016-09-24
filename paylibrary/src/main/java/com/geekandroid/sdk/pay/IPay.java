/*******************************************************************************
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.geekandroid.sdk.pay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Map;

/**
 * @author duanhui
 * @desc
 * @time 2016/2/24$ 11:22$
 */
public abstract class IPay {

    protected Activity activity;

    public static final int ALI_SDK_PAY_FLAG = 1;//ali 支付成功 handler 标识
    public static final int ALI_SDK_CHECK_FLAG = 2;//ali 支付成功，检查标识
    public static final int WX_SDK_PAY_FLAG = 3;//微信支付成功 handler 标识


    public ProgressDialog progressDialog;


    public void init(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在加载...");
        this.activity = activity;
    }

    public void showProgress() {
        progressDialog.show();//显示进度条
    }

    protected void hideProgress(String errorMessage) {
        if (!TextUtils.isEmpty(errorMessage)) {
            show(activity, errorMessage);
        }
        progressDialog.dismiss();
    }

    public static void show(Context context, String message) {

        if (null == context || TextUtils.isEmpty(message)) {
            return;
        }

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 10);
        toast.show();
    }

    public boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof Integer) {
            return (int) object == 0;
        }

        if (object instanceof String && TextUtils.isEmpty(object.toString())) {
            return true;
        }

        return false;
    }

    protected void hideProgress() {
        hideProgress("");
    }


    /**
     * 支付结果内部自己实现
     */
    public abstract void pay(Map<String, Object> params, RequestCallBack<String> callBack);

    /**
     * 请求订单号
     */
    public abstract void requestOrder(Map<String, Object> params, RequestCallBack<String> callBack);

    /**
     * 请求支付参数
     */
    public abstract void getPayParam(Map<String, Object> params, RequestCallBack<String> callBack);


    /**
     * 请求后台服务器得到支付结果
     */
    public abstract void getPayResult(Map<String, Object> params, RequestCallBack<String> callBack);

    public interface RequestCallBack<T> {
        void onSuccess(T result);

        void onFailure(String errorMessage, Exception exception);
    }
}
