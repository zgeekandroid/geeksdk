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

import java.util.Map;

/**
 * date        :  2015-11-30  11:11
 * author      :  Mickaecle gizthon
 * description :  local request interface
 */
public interface IRequestLocal    {
    /**
     * request local cache data
     * @param parameters  result back extends BaseDomain
     */
   <T> T doQueryLocal(Map<String, String> parameters, Class<T> cls);
}
