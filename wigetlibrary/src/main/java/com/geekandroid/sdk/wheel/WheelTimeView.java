/*******************************************************************************
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.geekandroid.sdk.wheel;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geekandroid.sdk.R;

import java.util.Calendar;

/**
 * <p>Created by gizthon on 16/9/25. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public class WheelTimeView extends LinearLayout {

    private WheelView hourWheel;
    private WheelView minuteWheel;
    private TextView tvHour;
    private TextView tvMinute;

    private int timeType = 0;

    public WheelTimeView(Context context) {
        super(context);
        init(context);
    }

    public WheelTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Calendar getDate() {
        int hour = Integer.parseInt(hourWheel.getCurrentItemText());
        int minute = Integer.parseInt(minuteWheel.getCurrentItemText());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
//        long setTime = calendar.getTimeInMillis();
        if (onWheelTimeListener != null) {
            onWheelTimeListener.onSelected(hour, minute);
        }
        return calendar;
    }

    public void setShowDate(int hour, int minute) {

        hourWheel.setCurrentItem(hour);
        minuteWheel.setCurrentItem(minute);

    }


    public void setShowDate(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        setShowDate(hour, minute);

    }

    public void setOffset(int offset) {
        hourWheel.setOffset(offset);
        minuteWheel.setOffset(offset);
        setDefault();
    }

    public void setDefault() {
        hourWheel.setWheelStyle(WheelStyle.STYLE_HOUR);
        minuteWheel.setWheelStyle(WheelStyle.STYLE_MINUTE);
    }

    public interface OnWheelTimeListener {
        void onSelected(int hour, int minute);
    }

    private OnWheelTimeListener onWheelTimeListener;

    public void setOnWheelTimeListener(OnWheelTimeListener onWheelTimeListener) {
        this.onWheelTimeListener = onWheelTimeListener;
    }

    public void setShowDate() {
        setShowDate(Calendar.getInstance());
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View dialogView = layoutInflater.inflate(R.layout.wheel_view_time, this, true);
        hourWheel = (WheelView) dialogView.findViewById(R.id.select_time_wheel_hour);
        minuteWheel = (WheelView) dialogView.findViewById(R.id.select_time_wheel_minute);
        tvHour = (TextView) dialogView.findViewById(R.id.tv_hour);
        tvMinute = (TextView) dialogView.findViewById(R.id.tv_minute);


        hourWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                getDate();
            }
        });

        minuteWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                getDate();
            }
        });

    }

    public void setTextPadding(int left, int top, int right, int bottom) {
        hourWheel.setTextPadding(left, top, right, bottom);
        minuteWheel.setTextPadding(left, top, right, bottom);
    }

    public WheelTimeView setHourAndMinute(String hour, String minute) {
        if (!TextUtils.isEmpty(minute)) {
            tvMinute.setText(minute);
        }
        if (!TextUtils.isEmpty(hour)) {
            tvHour.setText(hour);
        }
        return this;
    }

    public WheelTimeView setHourVisibility(int visible) {
        tvMinute.setVisibility(visible);
        return this;
    }

    public WheelTimeView setMinuteVisibility(int visible) {
        tvHour.setVisibility(visible);
        return this;
    }
}
