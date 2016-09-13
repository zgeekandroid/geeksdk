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

package com.bluetoothlibrary.bt;

/**
 * Created by yefeng on 7/1/15.
 * github:yefengfreedom
 * <p/>
 * read bytes from bluetooth
 */
public class BtMsgReadEvent {

    public int bytes;
    public byte[] buffer;
    public String message;

    public BtMsgReadEvent(int bytes, byte[] buffer) {
        this.buffer = buffer;
        this.bytes = bytes;
        this.message = new String(buffer, 0, bytes);
    }
}