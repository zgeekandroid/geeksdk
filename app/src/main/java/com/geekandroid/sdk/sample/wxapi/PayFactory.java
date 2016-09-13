
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

package com.geekandroid.sdk.sample.wxapi;


import com.geekandroid.sdk.pay.IPay;
import com.geekandroid.sdk.pay.impl.PayTypeEnum;


/**
 * date        :  2016-04-02  17:45
 * author      :  Mickaecle gizthon
 * description :
 */

public class PayFactory {
    public static IPay getPay(PayTypeEnum type) {

        if (type == PayTypeEnum.ALIPAY) {
            return new AliPay();
        } else if (type == PayTypeEnum.WEIXINPAY) {
            return new WeiXinPay();
        } else if (type == PayTypeEnum.YUEPAY) {
            return new YuEPay();
        }else if (type == PayTypeEnum.CASHPAY) {
            return new CashPay();
        }
        return null;
    }
}
