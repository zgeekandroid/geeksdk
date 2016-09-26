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

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.geekandroid.sdk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by gizthon on 16/9/26. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public class WheelView extends ScrollView {
    public static final String TAG = WheelView.class.getSimpleName();

    public interface OnWheelViewListener {
        void onSelected(int selectedIndex, String item);
    }


    private int selectedColor;
    private int unSelectedColor;
    private float selectedSize;
    //    private float unSelectedSize;
    private int centerLineColor;
    private float centerLineHeight;

    private Context context;

    private LinearLayout views;

    public WheelView(Context context) {
        super(context);
        init(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    //    String[] items;
    List<String> items;

    private List<String> getItems() {
        return items;
    }

    public void setWheelItemList(List<String> list) {
        if (null == items) {
            items = new ArrayList<String>();
        }
        views.removeAllViews();
        items.clear();
        items.addAll(list);

        // 前面和后面补全
        for (int i = 0; i < offset; i++) {
            items.add(0, "");
            items.add("");
        }

        selectedIndex = adjustSelectedIndex();
        initData();
    }


    public void setWheelStyle(int style) {
        List<String> itemList = WheelStyle.getItemList(context, style);
        if (itemList != null) {
            setWheelItemList(itemList);
        } else {
            Log.i(TAG, "item is null");
        }
    }


    public static final int OFF_SET_DEFAULT = 1;
    int offset = OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全）

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    int displayItemCount; // 每页显示的数量

    int selectedIndex = 1;


    private void init(Context context, AttributeSet attrs) {
        this.context = context;

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
            selectedColor = typedArray.getColor(R.styleable.WheelView_selectedColor, Color.parseColor("#333333"));
            selectedSize = typedArray.getDimension(R.styleable.WheelView_selectedSize, 17);
//            unSelectedSize = typedArray.getDimension(R.styleable.WheelView_unSelectedSize, 18);
            unSelectedColor = typedArray.getColor(R.styleable.WheelView_unSelectedColor, Color.parseColor("#999999"));
            centerLineColor = typedArray.getColor(R.styleable.WheelView_centerLineColor, Color.parseColor("#d7d7d7"));
            centerLineHeight = typedArray.getDimension(R.styleable.WheelView_centerLineHeight, 0.5F);
            typedArray.recycle();
        } else {
            selectedSize = 17;
//            unSelectedSize = 18;
            selectedColor = Color.parseColor("#333333");
            unSelectedColor = Color.parseColor("#999999");
            centerLineColor = Color.parseColor("#d7d7d7");
            centerLineHeight = 0.5F;
        }

        Log.d(TAG, "parent: " + this.getParent());
        this.setVerticalScrollBarEnabled(false);

        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        this.addView(views);

        scrollerTask = new Runnable() {

            public void run() {

                int newY = getScrollY();
                if (initialY - newY == 0) { // stopped
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;
//                    Log.d(TAG, "initialY: " + initialY);
//                    Log.d(TAG, "remainder: " + remainder + ", divided: " + divided);
                    if (remainder == 0) {
                        selectedIndex = divided + offset;

                        onSeletedCallBack();
                    } else {
                        if (remainder > itemHeight / 2) {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder + itemHeight);
                                    selectedIndex = divided + offset + 1;
                                    onSeletedCallBack();
                                }
                            });
                        } else {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder);
                                    selectedIndex = divided + offset;
                                    onSeletedCallBack();
                                }
                            });
                        }


                    }


                } else {
                    initialY = getScrollY();
                    WheelView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };


    }

    int initialY;

    Runnable scrollerTask;
    int newCheck = 50;

    public void startScrollerTask() {

        initialY = getScrollY();
        this.postDelayed(scrollerTask, newCheck);
    }

    private void initData() {

        displayItemCount = offset * 2 + 1;


        for (String item : items) {
            views.addView(createView(item));
        }

        refreshItemView((selectedIndex - offset) * itemHeight);
    }

    int itemHeight = 0;

    int left = 10, top = 10, right = 10, bottom = 10;

    public void setTextPadding(int left, int top, int right, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    private TextView createView(String item) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setSingleLine(true);


        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(dip2px(left), dip2px(top), dip2px(right), dip2px(bottom));

        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedSize);


        if (items.get(selectedIndex).equals(item)) {
            tv.setTextColor(selectedColor);
        } else {
            tv.setTextColor(unSelectedColor);
        }
        if (0 == itemHeight) {
            itemHeight = getViewMeasuredHeight(tv);
//                Log.d(TAG, "itemHeight: " + itemHeight);
            views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
            this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));
        }
        return tv;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

//        Log.d(TAG, "l: " + l + ", t: " + t + ", oldl: " + oldl + ", oldt: " + oldt);

        refreshItemView(t);

        if (t > oldt) {
//            Log.d(TAG, "向下滚动");
            scrollDirection = SCROLL_DIRECTION_DOWN;
        } else {
//            Log.d(TAG, "向上滚动");
            scrollDirection = SCROLL_DIRECTION_UP;

        }


    }

    private void refreshItemView(int y) {
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if (remainder == 0) {
            position = divided + offset;
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1;
            }

        }

        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) views.getChildAt(i);
            if (null == itemView) {
                return;
            }
            itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedSize);
            if (position == i) {
                itemView.setTextColor(selectedColor);
            } else {
                itemView.setTextColor(unSelectedColor);
            }
        }
    }

    /**
     * 获取选中区域的边界
     */
    int[] selectedAreaBorder;

    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offset;
            selectedAreaBorder[1] = itemHeight * (offset + 1);
        }
        return selectedAreaBorder;
    }


    private int scrollDirection = -1;
    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;

    Paint paint;
    int viewWidth;


    @Override
    public void setBackground(Drawable background) {

        if (viewWidth == 0) {
            viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
            Log.d(TAG, "viewWidth: " + viewWidth);
        }

        if (null == paint) {
            paint = new Paint();
            paint.setColor(centerLineColor);
            paint.setStrokeWidth(dip2px(centerLineHeight));
        }

        background = new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {
                canvas.drawLine(viewWidth / 6, obtainSelectedAreaBorder()[0], viewWidth * 5 / 6, obtainSelectedAreaBorder()[0], paint);
                canvas.drawLine(viewWidth / 6, obtainSelectedAreaBorder()[1], viewWidth * 5 / 6, obtainSelectedAreaBorder()[1], paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };


        super.setBackground(background);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        Log.d(TAG, "w: " + w + ", h: " + h + ", oldw: " + oldw + ", oldh: " + oldh);
        viewWidth = w;
        setBackground(null);
    }

    /**
     * 选中回调
     */
    private void onSeletedCallBack() {
        if (null != onWheelViewListener) {
            onWheelViewListener.onSelected(selectedIndex, items.get(selectedIndex));
        }

    }


    public int getItemCount() {
        return items == null ? 0 : items.size() - 2 * offset;
    }

    public void setCurrentItem(int position) {
        final int p = position;

            selectedIndex = p + offset;
            this.post(new Runnable() {
                @Override
                public void run() {
                    WheelView.this.smoothScrollTo(0, p * itemHeight);
                }
            });

    }

    public int getCurrentItem() {
        return selectedIndex - offset;
    }

    public String getCurrentItemText() {
        return items.get(adjustSelectedIndex());
    }

    public int adjustSelectedIndex() {
        if (selectedIndex < offset) {//如果是获取到比 offset 小 的index ,获取值的时候,用第一条数据
            return offset;
        }

        if (selectedIndex > items.size() - 1 - offset) {//如果是获取到比 offset 大 的index ,获取值的时候,用最后一条数据
            return items.size() - 1 - offset;
        }

        return selectedIndex;
    }



    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {

            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }

    private OnWheelViewListener onWheelViewListener;

    public OnWheelViewListener getOnWheelViewListener() {
        return onWheelViewListener;
    }

    public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getViewMeasuredHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }
}