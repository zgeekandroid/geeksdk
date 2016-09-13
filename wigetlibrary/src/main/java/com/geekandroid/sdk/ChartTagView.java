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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


import lecho.lib.hellocharts.model.Line;

/**
 * date        :  2016-03-07  18:30
 * author      :  Mickaecle gizthon
 * description :
 */
public class ChartTagView extends View {
    private Line line;
    private Paint paint = new Paint();
    private Paint paintLine = new Paint();
    private Paint paintLabel = new Paint();
    private Paint paintValue = new Paint();
    private Rect labelBound = new Rect();
    private Rect valueBound = new Rect();

    private String labelText = "标签";
    private String valueText = "0.0";
    private float labelSize = 50;
    private float valueSize = 50;
    private float valuePadding = 30F;
    private float labelPadding = 10F;
    private float linePadding = 20F;
    private float strokeWidth = 3F;

    private int labelColor = Color.parseColor("#818181");
    private int valueColor  = Color.parseColor("#FF5F57");
    private int valueColorBack  = Color.parseColor("#FF5F57");
    private int backGroundColor = Color.parseColor("#FFFFFF");

    private int lineColor = Color.GRAY;

    private int lines = 1;//默认行数
    private int maxLength = 6;//默认行数的字符长度



    public ChartTagView(Context context) {
       this(context,null);
    }

    public ChartTagView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public ChartTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
        postInvalidate();
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
        postInvalidate();
    }

    public void setChartLine(Line line) {
        this.line = line;
        lineColor = line.getColor();
//        strokeWidth = line.getStrokeWidth();
    }



    public Line getLine() {
        return line;
    }

    public void setLabelPadding(int labelPadding) {
        this.labelPadding = labelPadding;
    }

    public void setValuePadding(int valuePadding) {
        this.valuePadding = valuePadding;
    }

    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public void setValueColor(int valueColor) {
        this.valueColorBack = valueColor;
        this.valueColor = valueColor;
    }

    public void setLabelSize(float labelSize) {
        this.labelSize = labelSize;
//        postInvalidate();
    }

    public void setValueSize(float valueSize) {
        this.valueSize = valueSize;
    }

    public void init(Context context,AttributeSet attributeSet){


        if (attributeSet != null){
            TypedArray  array = context.obtainStyledAttributes(attributeSet, R.styleable.ChartTagView);
            labelColor = array.getColor(R.styleable.ChartTagView_chartLabelColor,Color.parseColor("#818181"));
            valueColorBack = valueColor = array.getColor(R.styleable.ChartTagView_chartValueColor,Color.parseColor("#FF5F57"));
            backGroundColor = array.getColor(R.styleable.ChartTagView_chartBackGroundColor, Color.parseColor("#FFFFFF"));


            labelText =  array.getString(R.styleable.ChartTagView_chartLabelText);
            valueText =  array.getString(R.styleable.ChartTagView_chartValueText);
            labelSize =  array.getDimension(R.styleable.ChartTagView_chartLabelSize, 50.0F);
            valueSize =  array.getDimension(R.styleable.ChartTagView_chartValueSize,50.0F);
            labelPadding = array.getDimension(R.styleable.ChartTagView_chartLabelPadding,10.0F);
            valuePadding = array.getDimension(R.styleable.ChartTagView_chartValuePadding,30.0F);
            linePadding = array.getDimension(R.styleable.ChartTagView_chartLinePadding,20.0F);

            valueText =   valueText == null ? "" : valueText;
            labelText =   labelText == null ? "" : labelText;

         }





        paintLine.setAntiAlias(true);
        paintLine.setTextAlign(Paint.Align.CENTER);


        paintLabel.setAntiAlias(true);
        paintLabel.setTextAlign(Paint.Align.CENTER);


        paintValue.setAntiAlias(true);

        paintValue.setTextAlign(Paint.Align.CENTER);




        paintLabel.setTextSize(labelSize);
        paintValue.setTextSize(valueSize);
        paintLabel.getTextBounds(labelText, 0, labelText.length(), labelBound);
        paintValue.getTextBounds(valueText, 0, valueText.length(), valueBound);



        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (line == null) {
                    return;
                }


                boolean toggle = valueColor == paintLabel.getColor();
                if (toggle) {
                    lineColor = line.getColor();
                    valueColor = valueColorBack;
                } else {
                    lineColor = paintLabel.getColor();
                    valueColor = paintLabel.getColor();
                }
                listener.onToggle(toggle);
                postInvalidate();
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(backGroundColor);

        paintLabel.setColor(labelColor);
        paintValue.setColor(valueColor);

        paintLine.setColor(lineColor);

        paintLabel.setTextSize(labelSize);
        paintValue.setTextSize(valueSize);




            String formatString  = labelText;
            if (formatString.length() > maxLength){
                if (formatString.length() % maxLength == 0){
                    lines = formatString.length() / maxLength;
                }else{
                    lines =  formatString.length() / maxLength + 1;
                }
                paintLabel.getTextBounds(labelText, 0,  maxLength, labelBound);
            }else{
             paintLabel.getTextBounds(labelText, 0, labelText.length(), labelBound);
            }

        paintValue.getTextBounds(valueText, 0, valueText.length(), valueBound);


        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

        float valueHeight = valueBound.height()+  valuePadding+getPaddingTop();
        canvas.drawText(valueText, getMeasuredWidth() / 2 - valueText.length() / 2, valueHeight, paintValue);

        if (lines > 1){//如果多行
            float lineHeight = labelPadding + labelBound.height();
            float labelHeight = valueBound.height() + 2 * valuePadding+getPaddingTop()   + lineHeight;


            String localText = labelText;
            for (int i = 0; i < lines ; i++) {
                int left = localText.length();
                String lineText ;
                if (left < maxLength){
                    lineText=   localText.substring(0,left);
                    localText =  localText.substring(left);
                }else{
                    lineText = localText.substring(0,maxLength);
                    localText =  localText.substring(maxLength );
                }

                canvas.drawText(lineText, getMeasuredWidth() / 2 - lineText.length() / 2, labelHeight, paintLabel);
                labelHeight += lineHeight;
            }
        }else{
            float labelHeight = valueBound.height() + 2 * valuePadding  + labelPadding + labelBound.height()+getPaddingTop() ;
            canvas.drawText(labelText, getMeasuredWidth() / 2 - labelText.length() / 2, labelHeight, paintLabel);
        }




        if (line != null){
            paintLine.setStrokeWidth(strokeWidth * 2);
            float eachLine = lines * labelBound.height() + 2 * labelPadding;
            float lineHeight =  eachLine+ valueBound.height() + 2 * valuePadding + linePadding+ strokeWidth * 4+getPaddingTop();
            canvas.drawLine(getMeasuredWidth() / 3, lineHeight, getMeasuredWidth() * 2 / 3, lineHeight, paintLine);
            canvas.drawCircle(getMeasuredWidth() / 2, lineHeight, strokeWidth * 4, paintLine);
        }



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int width = 0;
        int height = 0;


        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode)
        {
            case MeasureSpec.EXACTLY:
                width = getPaddingLeft() + getPaddingRight() + specSize;
                break;
            case MeasureSpec.AT_MOST:
                float labelWidth = getPaddingLeft() + getPaddingRight() + labelBound.width() + 2* labelPadding + 20;
                float valueWidth = getPaddingLeft() + getPaddingRight() + valueBound.width()+ 2*valuePadding + 20;
                width = (int) (labelWidth > valueWidth ? labelWidth : valueWidth);
                break;
            default:
                break;
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode)
        {
            case MeasureSpec.EXACTLY:
                height = getPaddingTop() + getPaddingBottom() + specSize ;
                break;
            case MeasureSpec.AT_MOST:
                float lineHeight = labelBound.height() + 2 * labelPadding ;

                float totalHeight = (lines + 1)* lineHeight + valueBound.height() + 2 * valuePadding + 2 * linePadding+ strokeWidth * 8;
//                float totalHeight = (lines + 1)* lineHeight;
                height = (int) (getPaddingTop() + getPaddingBottom() + totalHeight);
                break;
        }
        setMeasuredDimension(width, height);

    }




    public void setOnToggleListener(onToggleListener listener){
        this.listener = listener;
    }
    private onToggleListener listener;
    public interface  onToggleListener{
        void onToggle(boolean toggle);
    }

}
