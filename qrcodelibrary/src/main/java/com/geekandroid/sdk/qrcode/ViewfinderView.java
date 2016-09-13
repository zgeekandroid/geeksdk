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

package com.geekandroid.sdk.qrcode;/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.geekandroid.sdk.qrcode.camera.CameraManager;
import com.geekandroid.sdk.qrcodelibrary.R;
import com.google.zxing.ResultPoint;

import java.util.Collection;
import java.util.HashSet;


/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 */
public abstract class ViewfinderView extends View {
	@SuppressWarnings("unused")
	private static final String TAG = "ViewfinderView";
	/**
	 * 刷新界面的时间
	 */
	private static final long ANIMATION_DELAY = 60L;
	private static final int OPAQUE = 0xFF;

	/**
	 * 四个白色边角对应的长度
	 */
	private int ScreenRate;
	/**
	 * 四个白色边角对应的宽度
	 */
	private static final int CORNER_WIDTH = 6;
	/**
	 * 扫描框中的中间线的宽度
	 */
	// private static final int MIDDLE_LINE_WIDTH = 6;
	@SuppressWarnings("unused")
	private static final int MIDDLE_LINE_WIDTH = 10;

	/**
	 * 扫描框中的中间线的与扫描框左右的间隙
	 */
	// private static final int MIDDLE_LINE_PADDING = 5;
	@SuppressWarnings("unused")
	private static final int MIDDLE_LINE_PADDING = 5;

	/**
	 * 中间那条线每次刷新移动的距离
	 */
	private static final int SPEEN_DISTANCE = 10;

	/**
	 * 手机的屏幕密度
	 */
	private static float density;
	/**
	 * 字体大小
	 */
	private static final int TEXT_SIZE = 16;
	/**
	 * 字体距离扫描框下面的距离
	 */
	private static final int TEXT_PADDING_TOP = 30;

	/**
	 * 画笔对象的引用
	 */
	private Paint paint;

	/**
	 * 中间滑动线的最顶端位置
	 */
	private int slideTop;

	/**
	 * 中间滑动线的最底端位置
	 */
	// private int slideBottom;
	@SuppressWarnings("unused")
	private int slideBottom;
	/**
	 * 将扫描的二维码拍下来，这里没有这个功能，暂时不考虑
	 */
	private Bitmap resultBitmap;
	private   int maskColor;
	private   int resultColor;

	private   int resultPointColor;
	private   int viewFinderFrameColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	boolean isFirst;
	private CameraManager cameraManager;

	private Drawable lineDrawable;// 扫描线样式
	private Rect mRect;

	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		density = context.getResources().getDisplayMetrics().density;
		// 将像素转换成dp
		ScreenRate = (int) (20 * density);

		paint = new Paint();
		Resources resources = getResources();
//		maskColor = resources.getColor(R.color.viewfinder_mask);
//		resultColor = resources.getColor(R.color.result_view);

		// 获取扫描图片 qr_scan_line.png
//		lineDrawable = getResources().getDrawable(R.drawable.qr_scan_line);
		mRect = new Rect();

//		resultPointColor = resources.getColor(R.color.possible_result_points);


		possibleResultPoints = new HashSet<ResultPoint>(5);
		maskColor = getMaskColor();
		resultColor = getResultColor();
		lineDrawable = getLineDrawable();
		resultPointColor = getResultPointColor();
		viewFinderFrameColor  = getViewFinderFrameColor();
	}


	//需要自定义下面的几个参数，getResources().getColor(R.color.possible_result_points); 得到的color
	public abstract int getMaskColor();

	public abstract int  getResultColor() ;

	public abstract Drawable getLineDrawable();

	public abstract int  getResultPointColor() ;

	public abstract int  getViewFinderFrameColor();

	//************************************

	public void setCameraManager(CameraManager cameraManager) {
		this.cameraManager = cameraManager;
	}

	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) {
		if (cameraManager == null) {
			return; // not ready yet, early draw before done configuring
		}
		// 中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
		Rect frame = cameraManager.getFramingRect();
		if (frame == null) {
			return;
		}

		// 初始化中间线滑动的最上边和最下边
		if (!isFirst) {
			isFirst = true;
			slideTop = frame.top;
			// slideBottom = frame.bottom;
		}

		// 获取屏幕的宽和高
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		paint.setColor(resultBitmap != null ? resultColor : maskColor);

		// 画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
		// 扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {

			// 画扫描框边上的角，总共8个部分
			paint.setColor(Color.WHITE);
			// 左上角
			canvas.drawRect(frame.left - 10, frame.top - 10, frame.left + ScreenRate - 10, frame.top - 10 + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left - 10, frame.top - 10, frame.left - 10 + CORNER_WIDTH, frame.top + ScreenRate - 10, paint);
			canvas.drawRect(frame.right - ScreenRate + 10, frame.top - 10, frame.right + 10, frame.top + CORNER_WIDTH - 10, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH + 10, frame.top - 10, frame.right + 10, frame.top + ScreenRate - 10, paint);
			canvas.drawRect(frame.left - 10, frame.bottom - CORNER_WIDTH + 10, frame.left + ScreenRate - 10, frame.bottom + 10, paint);
			canvas.drawRect(frame.left - 10, frame.bottom - ScreenRate + 10, frame.left + CORNER_WIDTH - 10, frame.bottom + 10, paint);
			canvas.drawRect(frame.right - ScreenRate + 10, frame.bottom - CORNER_WIDTH + 10, frame.right + 10, frame.bottom + 10, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH + 10, frame.bottom - ScreenRate + 10, frame.right + 10, frame.bottom + 10, paint);

			// Draw a two pixel solid black border inside the framing rect
			// 画出扫描区域边框

			paint.setColor(viewFinderFrameColor);
			canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
			canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
			canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
			canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);

			// 绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE
			/*
			 * slideTop += SPEEN_DISTANCE; if (slideTop >= frame.bottom) {
			 * slideTop = frame.top; }
			 */
			/*
			 * canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop -
			 * MIDDLE_LINE_WIDTH/2, frame.right - MIDDLE_LINE_PADDING,slideTop +
			 * MIDDLE_LINE_WIDTH/2, paint);
			 */

			// 从图片资源画扫描线
			/*
			 * Rect lineRect = new Rect(); lineRect.left = frame.left +
			 * MIDDLE_LINE_PADDING; lineRect.right = frame.right -
			 * MIDDLE_LINE_PADDING; lineRect.top = slideTop; lineRect.bottom =
			 * (slideTop + MIDDLE_LINE_WIDTH);
			 * canvas.drawBitmap(((BitmapDrawable) (BitmapDrawable)
			 * getResources()
			 * .getDrawable(R.drawable.qr_scan_line)).getBitmap(), null,
			 * lineRect, paint);
			 */

			// Draw a red "laser scanner" line through the middle to show
			// decoding is active
			if ((slideTop += SPEEN_DISTANCE) < (frame.bottom - frame.top)) {

				/* 以下为图片作为扫描线 */
				mRect.set(frame.left + 6, frame.top + slideTop, frame.right - 6, frame.top + 10 + slideTop);
				lineDrawable.setBounds(mRect);
				lineDrawable.draw(canvas);

			} else {
				slideTop = 0;
			}

			// 画扫描框下面的字
			paint.setColor(Color.WHITE);
			paint.setTextSize(TEXT_SIZE * density);
			// paint.setAlpha(0x40);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			String text = getResources().getString(R.string.scan_text);
			float textWidth = paint.measureText(text);
			canvas.drawText(text, (width - textWidth) / 2, (float) (frame.bottom + (float) TEXT_PADDING_TOP * density), paint);

			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
				}
			}

			// 只刷新扫描框的内容，其他地方不刷新
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);

		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

	/**
	 * dp转px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
}
