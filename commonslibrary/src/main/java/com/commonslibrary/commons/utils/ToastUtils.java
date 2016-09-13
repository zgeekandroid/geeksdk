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

package com.commonslibrary.commons.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * date        :  2016-01-27  13:27
 * author      :  Mickaecle gizthon
 * description :
 */
public class ToastUtils {
    private ToastUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void show(Context context, String message) {

        if (null == context || TextUtils.isEmpty(message)) {
            return;
        }
        show(context, message, Gravity.CENTER);
    }

    public static void showDefault(Context context, String message) {

        if (null == context || TextUtils.isEmpty(message)) {
            return;
        }
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(Context context, String message, int gravity) {

        if (null == context || TextUtils.isEmpty(message)) {
            return;
        }

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 10);
        toast.show();
    }
}
