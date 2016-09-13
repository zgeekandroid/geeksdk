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

package com.bluetoothlibrary;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.bluetoothlibrary.print.PrintUtil;


/**
 *查看蓝牙连接状态
 *@author:BingCheng
 *@date:2016/8/3 13:49
 */
public class MainController {

    public static void init(SetBluetoothActivity activity) {
        if (null == activity.mAdapter) {
            activity.mAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (null == activity.mAdapter) {
            activity.txtPrinterTitle.setText("该设备没有蓝牙模块");
            activity.imgPrinter.setImageResource(R.mipmap.printer_uncon);
            activity.mBtEnable = false;
            activity.bt_print.setVisibility(View.GONE);
            activity.bt_help_or_preview.setText("查看蓝牙连接帮助");
            return;
        }
        if (!activity.mAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, SetBluetoothActivity.OPEN_BLUETOOTH_REQUEST);
            activity.showToast("正在打开蓝牙");
            activity.txtPrinterTitle.setText("蓝牙未打开");
            activity.bt_print.setVisibility(View.GONE);
            activity.bt_help_or_preview.setText("查看蓝牙连接帮助");
            activity.imgPrinter.setImageResource(R.mipmap.printer_uncon);
            return;
        }
        String address = PrintUtil.getDefaultBluethoothDeviceAddress(activity.getApplicationContext());
        if (TextUtils.isEmpty(address)) {
            activity.txtPrinterTitle.setText("尚未绑定蓝牙设备");
            activity.imgPrinter.setImageResource(R.mipmap.printer_uncon);
            activity.bt_print.setVisibility(View.GONE);
            activity.bt_help_or_preview.setText("查看蓝牙连接帮助");
            return;
        }
        String name = PrintUtil.getDefaultBluetoothDeviceName(activity.getApplicationContext());
        activity.txtPrinterTitle.setText("已绑定蓝牙：" + name);
        activity.bt_print.setVisibility(View.VISIBLE);
        activity.bt_help_or_preview.setText("打印预览");
        activity.txtPrinterSummary.setText(address);
        activity.imgPrinter.setImageResource(R.mipmap.printer_con);
    }
}
