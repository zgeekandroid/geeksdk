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

package com.geekandroid.sdk.pay.impl;

import com.commonslibrary.commons.net.RequestCallBack;
import com.geekandroid.sdk.pay.IPay;

import java.util.Map;


public abstract class CHCashPay extends IPay {
    @Override
    public void getPayParam(Map<String, Object> params, RequestCallBack<String> callBack) {
        throw  new IllegalStateException("不需要调用此方法");
    }
    @Override
    public void getPayResult(Map<String, Object> params, RequestCallBack<String> callBack) {
        throw  new IllegalStateException("不需要调用此方法");
    }

    @Override
    public void requestOrder(Map<String, Object> params, RequestCallBack<String> callBack) {

    }
}
