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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RouteActivity extends FragmentActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private RadioButton rb_drive;
    private RadioButton rb_bus;
    private RadioButton rb_walk;
    private RadioGroup rg_type;

    /** 店纬度 */
    public double storeLatitude;
    /** 店经度 */
    public double storeLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
    }

    /**
     * 初始数据：搜索线路
     */
    private void initData() {
        Intent intent = getIntent();
        storeLatitude = intent.getDoubleExtra("storeLatitude", -1);
        storeLongitude = intent.getDoubleExtra("storeLongitude", -1);
        storeName = intent.getStringExtra("storeName");

    }

    /**
     * 初始控件
     */
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.map_viewpager);
        rb_drive = (RadioButton) findViewById(R.id.rb_drive);
        rb_bus = (RadioButton) findViewById(R.id.rb_bus);
        rb_walk = (RadioButton) findViewById(R.id.rb_walk);
        rg_type = (RadioGroup) findViewById(R.id.rg_type);

    }

    /**
     * 设置监听
     */
    private void setListener() {
        mViewPager.setOnPageChangeListener(onPageChangeListener);
        rg_type.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    /**
     * RadioGroup选择改变状态发生改变
     */
    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            switch (id) {
                case R.id.rb_drive:
                    mViewPager.setCurrentItem(0, true);
                    break;
                case R.id.rb_bus:
                    mViewPager.setCurrentItem(1, true);
                    break;
                case R.id.rb_walk:
                    mViewPager.setCurrentItem(2, true);
                    break;

                default:
                    break;
            }
        }
    };


    /**
     * ViewPager滑动监听
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    rb_drive.setChecked(true);
                    break;
                case 1:
                    rb_bus.setChecked(true);
                    break;
                case 2:
                    rb_walk.setChecked(true);
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private String storeName;

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
