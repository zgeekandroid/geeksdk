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

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

/**
 * Created by yefeng on 7/1/15.
 * github:yefengfreedom
 */
public interface BtInterface {
    /**
     * start discovery bt device
     *
     * @see BluetoothAdapter.ACTION_DISCOVERY_STARTED
     */
    void btStartDiscovery(Intent intent);

    /**
     * finish discovery bt device
     *
     * @see BluetoothAdapter.ACTION_DISCOVERY_FINISHED
     */
    void btFinishDiscovery(Intent intent);

    /**
     * bluetooth status changed
     *
     * @see BluetoothAdapter.ACTION_STATE_CHANGED
     */
    void btStatusChanged(Intent intent);

    /**
     * found bt device
     *
     * @see BluetoothDevice.ACTION_FOUND
     */
    void btFoundDevice(Intent intent);

    /**
     * device bond status change
     *
     * @see BluetoothDevice.ACTION_BOND_STATE_CHANGED
     */
    void btBondStatusChange(Intent intent);

    /**
     * pairing bluetooth request
     *
     * @see BluetoothDevice.ACTION_PAIRING_REQUEST
     */
    void btPairingRequest(Intent intent);
}
