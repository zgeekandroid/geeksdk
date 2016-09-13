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

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonslibrary.commons.utils.LogUtils;
import com.commonslibrary.commons.utils.ToastUtils;


/**
 * date        :  2016-02-05  14:51
 * author      :  Mickaecle gizthon
 * description :
 */
public class BaseFragment extends Fragment {
    private ColorMatrixColorFilter colorMatrixColorFilter;
    private ColorMatrix colorMatrix;
    private float saturation = 0.3F;

    /**
     *  set saturation when press button which has background
     * @param saturation
     */
    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        colorMatrix = new ColorMatrix();
        return super.onCreateView(inflater, container, savedInstanceState);
    }





    public void showToast(String message) {
        ToastUtils.show(getActivity(), message);
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }
    public void startActivityClearTask(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void startActivity(Class<?> cls, Bundle extras) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void startActvityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(getActivity(), cls);
        startActivityForResult(intent, requestCode);
    }

    public void startActvityForResult(Class<?> cls, int requestCode, Bundle extras) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtras(extras);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 打印log ，并且自动加上类名
     * @param log 打印的日志
     */
    public void logI(Object log){
        LogUtils.i("[" + getActivity().getLocalClassName() + "] " + String.valueOf(log));
    }
    public void logE(Object log){
        LogUtils.e("[" + getActivity().getLocalClassName() + "] " + String.valueOf(log));
    }

    public void setDrawablePosition(TextView textView, int left, int top, int right, int bottom) {
        Drawable drawableTop = null;
        Drawable drawableLeft = null;
        Drawable drawableRight = null;
        Drawable drawableBottom = null;

        if (top != 0) {
            drawableTop = getResources().getDrawable(top);
        }
        if (left != 0) {
            drawableLeft = getResources().getDrawable(left);
        }
        if (right != 0) {
            drawableRight = getResources().getDrawable(right);
        }
        if (bottom != 0) {
            drawableBottom = getResources().getDrawable(bottom);
        }
        /// 这一步必须要做,否则不会显示.
        if (drawableTop != null) {
            drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
        }

        if (drawableLeft != null) {
            int result = convertDp(60);
            drawableLeft.setBounds(0, 0, result, result);
//            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
        }
        if (drawableRight != null) {
            int result = convertDp(50);
            drawableRight.setBounds(0, 0, result, result);
//            drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        }
        if (drawableBottom != null) {
            drawableBottom.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());
        }
        int result = convertDp(10);
        textView.setCompoundDrawablePadding(result);
        textView.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public int convertDp(int px){
        final float scale = getResources().getDisplayMetrics().density;
        int result =  (int) ((px / 3.0) * scale);
        return result;
    }
}
