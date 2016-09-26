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

package com.geekandroid.sdk.wheel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.geekandroid.sdk.R;

import java.util.Calendar;

/**
 * <p>Created by gizthon on 16/9/25. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public class WheelDateView extends LinearLayout {

    private WheelView yearWheel;
    private WheelView monthWheel;
    private WheelView dayWheel;

    int selectYear;
    int selectMonth;
    int selectDay;

    public WheelDateView(Context context) {
        super(context);
        init(context);
    }

    public WheelDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelDateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public interface  OnWheelDateViewListener{
        void onSelected(int year, int month, int day , Calendar calendar);
    }
    private  OnWheelDateViewListener onWheelDateViewListener;

    public void setOnWheelDateViewListener(OnWheelDateViewListener onWheelDateViewListener) {
        this.onWheelDateViewListener = onWheelDateViewListener;
    }

    public Calendar getDate() {
        int  year = Integer.parseInt(yearWheel.getCurrentItemText()) ;
        int month = Integer.parseInt(monthWheel.getCurrentItemText());
        int day = Integer.parseInt(dayWheel.getCurrentItemText())  ;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, day);
//        long setTime = calendar.getTimeInMillis();
        if (onWheelDateViewListener != null){
            onWheelDateViewListener.onSelected(year,month,day,calendar);
        }
        return calendar;
    }


    public void setShowDate(int year, int month, int day) {

        yearWheel.setCurrentItem(year - WheelStyle.minYear);
        monthWheel.setCurrentItem(month - 1);
        dayWheel.setWheelItemList(WheelStyle.createDayString(year - WheelStyle.minYear, month ));
        dayWheel.setCurrentItem(day - 1);

    }

    public void setShowDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        setShowDate(year, month + 1, day);
    }

    public void setOffset(int offset){
        yearWheel.setOffset(offset);
        monthWheel.setOffset(offset);
        dayWheel.setOffset(offset);

        setDefault();
    }

    public void setDefault(){
        yearWheel.setWheelStyle(WheelStyle.STYLE_YEAR);
        monthWheel.setWheelStyle(WheelStyle.STYLE_MONTH);
    }

    public void setShowDate() {
        setShowDate(Calendar.getInstance());
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View dialogView = layoutInflater.inflate(R.layout.wheel_view_date, this,true);
        yearWheel = (WheelView) dialogView.findViewById(R.id.select_date_wheel_year_wheel);
        monthWheel = (WheelView) dialogView.findViewById(R.id.select_date_month_wheel);
        dayWheel = (WheelView) dialogView.findViewById(R.id.select_date_day_wheel);





        yearWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int index, String item) {
                selectYear = index + WheelStyle.minYear;
                dayWheel.setWheelItemList(WheelStyle.createDayString(selectYear, selectMonth));
                getDate();
            }
        });



        monthWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int index, String item) {
                selectMonth = index;
                dayWheel.setWheelItemList(WheelStyle.createDayString(selectYear, selectMonth));
                getDate();
            }
        });


        dayWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                getDate();
            }
        });



    }

    public void setTextPadding(int left, int top, int right, int bottom) {
        yearWheel.setTextPadding(left, top, right, bottom);
        monthWheel.setTextPadding(left, top, right, bottom);
        dayWheel.setTextPadding(left, top, right, bottom);
    }
}
