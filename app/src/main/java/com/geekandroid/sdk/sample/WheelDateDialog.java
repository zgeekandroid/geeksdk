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

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.commonslibrary.commons.utils.DeviceUtils;
import com.geekandroid.sdk.wheel.WheelDateView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * <p>Created by gizthon on 16/9/22. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public class WheelDateDialog extends Dialog {
    public TextView tv_cancel;
    public TextView tv_confirm;
    public WheelDateView wheel_date;

    private View confirmView;
    private Context context;

    private boolean isCancel = true;


    public WheelDateDialog(Context context) {
        super(context);
        init(context);
    }

    public WheelDateDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected WheelDateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }


    private void init(Context activity) {
        context = activity;
        confirmView = View.inflate(activity, R.layout.dialog_select_date, null);
        tv_cancel = (TextView) confirmView.findViewById(R.id.tv_cancel);
        tv_confirm = (TextView) confirmView.findViewById(R.id.tv_confirm);
        wheel_date = (WheelDateView) confirmView.findViewById(R.id.wheel_date);
        wheel_date.setOffset(1);
        wheel_date.setShowDate();
        setPositive(null);
        setNagetive(null);

    }
    private  boolean isInit  =false;
    @Override
    public void show() {
        if (!isInit){
            setContentView(confirmView);
            setCanceledOnTouchOutside(isCancel);
            if (getWindow() != null) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.width =   (DeviceUtils.getScreenWidth(context)); //设置宽度
                lp.height = DeviceUtils.getScreenWidth(context) * 5/ 6;
//            lp.height = DeviceUtils.getScreenHeight(context);
                getWindow().setAttributes(lp);
                getWindow().setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
            }
            confirmView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setValue();
                }
            },800);
            isInit = true;
        }

        super.show();



    }


    public WheelDateDialog setNagetive(View.OnClickListener onclick) {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onclick != null) {
                    onclick.onClick(view);
                }

                dismiss();
            }
        });
        return this;
    }


    public WheelDateDialog setPositive(View.OnClickListener onclick) {
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onclick != null) {
                    onclick.onClick(view);
                }
                dismiss();
            }
        });
        return this;
    }

    /**
     * 如果是dialog中需要将dialog 创建之后再设置值,否则会出现第一次滚动不到指定位置,第二次则没有这个问题
     */
    public void setValue(){
        wheel_date.setShowDate();
    }

    public Calendar getSelectDate(){
        return wheel_date.getDate();
    }

    public String getSelectDateString(){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(wheel_date.getDate().getTime());
        return date;
    }



    public WheelDateDialog setCancel(boolean cancel) {
        isCancel = cancel;
        return this;
    }
}