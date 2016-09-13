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

package com.bluetoothlibrary.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by yefeng on 6/15/15.
 * github:yefengfreedom
 */
public class ImageUtils {

    /**
     * 按照指定宽度缩放图片
     *
     * @param bitmap 原始图片
     * @param width  指定宽度
     * @return 缩放后的图片
     */
    public static Bitmap zoomBitmapByWidth(Bitmap bitmap, int width) {
        if (width <= 0 || null == bitmap) {
            return bitmap;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scale = (float) width / (float) w;
        if (scale <= 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        try {
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
        return bitmap;
    }

}
