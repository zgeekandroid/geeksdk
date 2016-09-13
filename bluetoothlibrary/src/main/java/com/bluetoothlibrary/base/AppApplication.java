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

package com.bluetoothlibrary.base;

import android.app.Application;

/**
 * Created by yefeng on 6/2/15.
 * github:yefengfreedom
 */
public class AppApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        AppInfo.init(getApplicationContext());
    }

}
