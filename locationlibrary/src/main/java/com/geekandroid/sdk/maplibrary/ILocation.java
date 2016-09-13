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

package com.geekandroid.sdk.maplibrary;

import android.app.Activity;
import android.content.Context;

/**
 * date        :  2016-02-16  12:50
 * author      :  Mickaecle gizthon
 * description :  通用接口
 */
public interface ILocation {
    void init(Context context);
    void start(Activity activity);
    void stop();
}
