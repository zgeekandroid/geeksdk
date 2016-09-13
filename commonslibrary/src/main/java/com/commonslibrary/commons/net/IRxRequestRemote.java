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

package com.commonslibrary.commons.net;


import com.commonslibrary.commons.net.RequestCallBack;

import java.io.File;
import java.util.Map;

/**
 * model request interface , the interface request from server
 */
public interface IRxRequestRemote {
    /**
     * net work request
     *
     * @param url        request url
     * @param parameters request parameters
     */
    <T> rx.Observable<T> doRxGet(String url, Map<String, Object> parameters,Class<T> cls);

    <T> rx.Observable<T> doRxPost(String url, Map<String, Object> parameters,Class<T> cls);

    <T> rx.Observable<T> doRxUpload(String url, Map<String, Object> parameters, Map<String, File> files, RequestCallBack<T> callBack);


}
