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

package com.geekandroid.sdk.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * date        :  2016-04-20  13:46
 * author      :  Mickaecle gizthon
 * description :
 */
public class ContentActivity extends AppCompatActivity {
    public static Fragment showFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        if (showFragment != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, showFragment).commit();
        }
    }

    Fragment current;

    public void switchContent(Fragment from, Fragment to) {
        if (current != to) {
            current = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()/*.setCustomAnimations(
                    android.R.anim.fade_in, R.anim.fade_out)*/;
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.container, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
