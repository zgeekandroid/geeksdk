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

package com.geekandroid.sdk.jpushlibrary.push;

import android.content.Context;

import java.util.Set;

/**
 * date        :  2016-01-27  13:52
 * author      :  Mickaecle gizthon
 * description :
 */
public interface IPush {

    void init(Context context);

    void resumePush();

    void stopPush();

    void clearAllNotifications();

    void clearNotificationById(int id);

    void setAliasAndTags(String alias, Set<String> tags, IPushCallBack callback);

    void setAlias(String alias, IPushCallBack callback);

}
