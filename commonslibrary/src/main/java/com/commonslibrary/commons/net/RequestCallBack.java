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

import java.io.Reader;

/**
 * date        :  2016-02-02  15:24
 * author      :  Mickaecle gizthon
 * description :
 */
public abstract class RequestCallBack<T> {

    public void onStart() {
    }

    public abstract void onSuccess(T result);

    public void onSuccess(Reader reader) {
    }

    public abstract void onFailure(String errorMessage, Exception exception);

    public void onCancel() {
    }

    public void onProgress(long byteWrite, long contentLength, boolean isDone) {
    }

}
