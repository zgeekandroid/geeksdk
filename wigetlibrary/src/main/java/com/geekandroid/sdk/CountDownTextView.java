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

package com.geekandroid.sdk;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by hsy on 2016/8/3 0003
 */
public class CountDownTextView extends TextView {

    private long millisInFuture = 60000;// 倒计时长度,这里给了默认60秒
    private int interval = 1000;
    private String textafter = "秒后可重发";
    private String textbefore = "获取验证码";

    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getInterval() {
        return interval;
    }

    public CountDownTextView setInterval(int interval) {
        this.interval = interval;
        return this;
    }

    public String getTextafter() {
        return textafter;
    }

    public CountDownTextView setTextafter(String textafter) {
        this.textafter = textafter;
        return this;
    }

    public String getTextbefore() {
        return textbefore;
    }

    public CountDownTextView setTextbefore(String textbefore) {
        this.textbefore = textbefore;
        return this;
    }

    private CountDownTimer timer = new CountDownTimer(millisInFuture, interval) {

        @Override
        public void onTick(long millisUntilFinished) {
            CountDownTextView.this.setText((millisUntilFinished / interval) + textafter);
        }

        @Override
        public void onFinish() {
            CountDownTextView.this.setEnabled(true);
            CountDownTextView.this.setText(textbefore);
        }
    };

    public void start() {
        setEnabled(false);
        timer.start();
    }

}
