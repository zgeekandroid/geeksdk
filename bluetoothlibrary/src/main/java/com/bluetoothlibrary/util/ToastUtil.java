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

package com.bluetoothlibrary.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by yefeng on 6/2/15.
 * github:yefengfreedom
 */
public class ToastUtil {

    public static void showToast(Context context, String message) {
        if (null != context && !TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context context, int message) {
        if (null != context && message != 0) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

}
