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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imagerloaderlibrary.imagerloader.ImageLoaderView;

/**
 * @author:BingCheng
 * @date:2016/4/28 17:01
 */
public class ImageloaderFragment extends BaseSampleFragment {
    private static final String TAG = "PaySampleFragment";


    @Override
    public int getResLayoutId() {
        return R.layout.fragment_sample_imager_loader;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ImageLoaderView normal = (ImageLoaderView) view.findViewById(R.id.normal);

        ImageLoaderView circle = (ImageLoaderView) view.findViewById(R.id.circle);
        ImageLoaderView round = (ImageLoaderView) view.findViewById(R.id.round);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        String url = "http://img4q.duitang.com/uploads/item/201411/04/20141104225919_ZR3h5.thumb.224_0.jpeg";
//        normal.setUrl(url,null, false);
        circle.setCircleUrl(url);
        round.setRoundUrl(url);


        return view;
    }


}
