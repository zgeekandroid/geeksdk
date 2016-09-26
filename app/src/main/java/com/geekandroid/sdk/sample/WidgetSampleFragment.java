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
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.commonslibrary.commons.utils.LogUtils;
import com.geekandroid.sdk.wheel.WheelDateView;
import com.geekandroid.sdk.wheel.WheelTimeView;
import com.geekandroid.sdk.wheel.WheelView;

import java.util.Arrays;
import java.util.Calendar;

/**
 * date        :  2016-04-20  13:39
 * author      :  Mickaecle gizthon
 * description :
 */
public class WidgetSampleFragment extends BaseSampleFragment {
    private static final String TAG = "CommonSampleFragment";
    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};

    @Override
    public int getResLayoutId() {
        return R.layout.widget;
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WheelView wheelView = (WheelView) view.findViewById(R.id.wheel_view);
        wheelView.setOffset(1);
        wheelView.setWheelItemList(Arrays.asList(PLANETS));
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                LogUtils.i("wheel  "+item);
            }
        });


        WheelDateView wheelDateView = (WheelDateView) view.findViewById(R.id.wheel_date_view);
        wheelDateView.setOffset(1);
//        wheelDateView.setTextPadding(20,10,20,10);
        wheelDateView.setShowDate();
//        wheelDateView.setShowDate(2016,9,26);
        wheelDateView.setOnWheelDateViewListener(new WheelDateView.OnWheelDateViewListener() {
            @Override
            public void onSelected(int year, int month, int day, Calendar calendar) {
//                ToastUtils.show(getActivity(),year+"-"+month+"-"+day);
                LogUtils.i("onSelected  "+year+"-"+month+"-"+day);
            }
        });
        WheelTimeView wheelTimeView = (WheelTimeView) view.findViewById(R.id.wheel_time_view);
        wheelTimeView.setOffset(1);
//        wheelTimeView.setTextPadding(25,10,25,10);
        wheelTimeView.setShowDate();
        wheelTimeView.setOnWheelTimeListener(new WheelTimeView.OnWheelTimeListener() {
            @Override
            public void onSelected(int hour, int minute) {
                LogUtils.i("onSelected  "+hour+"-"+minute);
                }
        });

       Button button = (Button) view.findViewById(R.id.show_select);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WheelDateDialog dateDialog =  new WheelDateDialog(getActivity());
                    dateDialog.setPositive(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LogUtils.i(dateDialog.getSelectDateString());
                            button.setText(dateDialog.getSelectDateString());
                        }
                    });
                    dateDialog.show();
                }
            });
    }

}