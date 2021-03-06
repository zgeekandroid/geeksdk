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

package com.geekandroid.sdk.sample.map_navigationlibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;



/**
 * date        :  2016-02-05  14:51
 * author      :  Mickaecle gizthon
 * description :
 */
public class BaseActivity extends Activity {


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }



    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void startActivity(Class<?> cls, Bundle extras) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void startActvityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, requestCode);
    }

    public void startActvityForResult(Class<?> cls, int requestCode, Bundle extras) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(extras);
        startActivityForResult(intent, requestCode);
    }




}
