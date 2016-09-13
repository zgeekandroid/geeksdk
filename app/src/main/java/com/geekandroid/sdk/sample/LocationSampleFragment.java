
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
import android.widget.Button;

import com.commonslibrary.commons.net.RequestCallBack;
import com.commonslibrary.commons.utils.ToastUtils;
import com.geekandroid.sdk.maplibrary.Location;
import com.geekandroid.sdk.maplibrary.impl.BDLocationImpl;

;


/**
 * date        :  2016-04-20  13:39
 * author      :  Mickaecle gizthon
 * description :
 */

public class LocationSampleFragment extends BaseSampleFragment {
    private Button button;
    @Override
    public int getResLayoutId() {
        return R.layout.location;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        button = (Button) view.findViewById(R.id.location);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location();
            }
        });



        return view;
    }


    private void location() {




        BDLocationImpl.getInstance().start(getActivity(),new RequestCallBack<Location>() {
            @Override
            public void onSuccess(Location result) {
                ToastUtils.show(getActivity(),result.toString());

            }

            @Override
            public void onFailure(String errorMessage, Exception exception) {
                ToastUtils.show(getActivity(),errorMessage);

            }
        });
    }





}
