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

package com.geekandroid.sdk.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.commonslibrary.commons.utils.ToastUtils;
import com.geekandroid.sdk.sample.R;


/**
 * date        :  2016-02-05  14:51
 * author      :  Mickaecle gizthon
 * description :
 */
public class BaseActivity extends FragmentActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private ColorMatrixColorFilter colorMatrixColorFilter;
    private ColorMatrix colorMatrix;
    private float saturation = 0.3F;
    private boolean isCanFinish = true;
    private GestureDetector mGestureDetector;
    private int verticalMinDistance = 50;
    private int minVelocity = 0;

    /**
     * set saturation when press button which has background
     *
     * @param saturation
     */
    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }


     /* StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork() // or
                        // .detectAll()
                        // for
                        // all
                        // detectable
                        // problems
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
                .penaltyLog() // 打印logcat
                .penaltyDeath().build());*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colorMatrix = new ColorMatrix();
        mGestureDetector = new GestureDetector(this, this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://start to touch the view
                if (v.getBackground() != null) {
                    /**
                     * set background is color
                     */
                    if ((v.getBackground() instanceof ColorDrawable)) {
                        v.setTag(((ColorDrawable) v.getBackground()).getColor());
                        v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    } else {
                        /**
                         *  set background   is drawable ，if you want change saturation，by use variable
                         */
                        v.setTag("");
                        colorMatrix.setSaturation(saturation);
                        colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
                        v.getBackground().setColorFilter(colorMatrixColorFilter);
                    }
                }
                /**
                 * have not set background
                 */
                else {
                    v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    v.setTag(null);
                }

                break;
            case MotionEvent.ACTION_UP://when the finger leave the screen
            case MotionEvent.ACTION_CANCEL://cancel the action
                /**
                 * retrieve background is color
                 */
                if (v.getTag() instanceof Integer) {
                    v.setBackgroundColor((Integer) v.getTag());
                } else if (v.getTag() instanceof String) {
                    colorMatrix.setSaturation(1);
                    colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
                    v.getBackground().setColorFilter(colorMatrixColorFilter);
                } else {
                    v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                v.setTag(null);

                break;
            default:
                break;

        }
        mGestureDetector.onTouchEvent(event);
        return false;
    }

    public View findViewAndSetTouchListener(int resId) {
        View view = findViewById(resId);
        view.setOnTouchListener(this);
        return view;
    }

//    @Override
//    protected void onResume() {
//        JPushInterface.onResume(this);
//        super.onResume();
//    }

//    @Override
//    protected void onPause() {
//        JPushInterface.onPause(this);
//        super.onPause();
//    }


    public void showToast(String message) {
        ToastUtils.show(this, message);
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void startActivityClearTask(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        // scroll.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            //向左手势
        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            //向右手势

            final float scale = getResources().getDisplayMetrics().density;
            if (Math.abs(e2.getY() - e1.getY()) < Math.abs(e1.getX() - e2.getX()) && Math.abs(e1.getX() - e2.getX()) > 60 * scale) {
                if (subActivity != null && isCanFinish) {
                    this.subActivity.finish();
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }
            }


        }
        return false;
    }


    private Activity subActivity;//sub activity when need to finish

    /**
     * if can finish or not
     *
     * @param isCanFinish v
     * @param activity    v
     */

    public void setIsCanFinish(Activity activity, boolean isCanFinish) {
        subActivity = activity;
        this.isCanFinish = isCanFinish;
    }


    public void setDrawablePosition(TextView textView, int left, int top, int right, int bottom) {

        int padding = 10;
        if (top != 0) {
            setDrawableTop(textView, top, padding, 0);
        }
        if (left != 0) {
            int bounds = 60;
            setDrawableLeft(textView, left, padding, bounds);
        }
        if (right != 0) {
            int bounds = 50;
            setDrawableRight(textView, right, padding, bounds);
        }
        if (bottom != 0) {
            setDrawableBottom(textView, bottom, padding, 0);
        }
    }

    public void setDrawableLeft(TextView textView, int resId, int padding, int bounds) {
        Drawable drawable = getDrawable(textView, resId, padding, bounds);
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    private Drawable getDrawable(TextView textView, int resId, int p, int b) {

        int padding = convertDp(p);
        int bounds = convertDp(b);

        Drawable drawable = getResources().getDrawable(resId);

        if (drawable != null) {
            if (bounds <= 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), resId);
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                drawable.setBounds(0, 0, width, height);
            } else {
                drawable.setBounds(0, 0, bounds, bounds);
            }
        }
        if (padding > 0) {
            textView.setCompoundDrawablePadding(padding);
        }
        return drawable;
    }

    public void setDrawableRight(TextView textView, int resId, int padding, int bounds) {
        Drawable drawable = getDrawable(textView, resId, padding, bounds);
        textView.setCompoundDrawables(null, null, drawable, null);
    }

    public void setDrawableTop(TextView textView, int resId, int padding, int bounds) {
        Drawable drawable = getDrawable(textView, resId, padding, bounds);
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    public void setDrawableBottom(TextView textView, int resId, int padding, int bounds) {
        Drawable drawable = getDrawable(textView, resId, padding, bounds);
        textView.setCompoundDrawables(null, null, null, drawable);
    }

    public int convertDp(int px) {
        final float scale = getResources().getDisplayMetrics().density;
        int result = (int) ((px / 3.0) * scale);
        return result;
    }




    //**************************实现handler的另外一种方式****************************************
  /*  protected Handler handler;
    private BaseActivity recordActivity;

    public Handler getDefaultHandler(final BaseActivity activity) {
        if (handler == null || recordActivity != activity) {
            handler = new BaseHandler(activity);
            recordActivity = activity;
        }
        return handler;
    }

    private static class BaseHandler extends WeakHandler<BaseActivity> {

        public BaseHandler(BaseActivity reference) {
            super(reference);
        }

        @Override
        public void handleMessage(BaseActivity reference, Message msg) {
            reference.handleMessage(reference, msg);
        }
    }

    public void handleMessage(BaseActivity reference, Message msg) {
    }*/
    //****************************实现handler的另外一种方式**************************************
}
