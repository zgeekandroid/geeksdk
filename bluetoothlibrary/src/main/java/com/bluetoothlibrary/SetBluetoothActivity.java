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

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluetoothlibrary.bt.BluetoothActivity;
import com.bluetoothlibrary.print.PrintMsgEvent;
import com.bluetoothlibrary.print.PrintUtil;
import com.bluetoothlibrary.print.PrinterMsgType;



public class SetBluetoothActivity extends BluetoothActivity  {
    static final int OPEN_BLUETOOTH_REQUEST = 100;

    ImageView imgPrinter,iv_back;
    TextView txtPrinterTitle;
    TextView txtPrinterSummary;
    Button bt_print;
    Button bt_help_or_preview,btn_printer_setting_scan;
    boolean mBtEnable = true;
    BluetoothAdapter mAdapter;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bluetooth);
        initView();
        initListener();
    }

    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_printer_setting_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), BondBtActivity.class));
                    finish();
            }
        });
        bt_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), BtService.class);
                    intent.setAction(PrintUtil.ACTION_PRINT_TEST);
                    startService(intent);
            }
        });
        bt_help_or_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String text=bt_help_or_preview.getText().toString().trim();
                if(text.equals("打印预览")){
                    Intent intent = new Intent(getApplicationContext(), PrintDataActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), BluetoothHelpActivity.class);
                    startActivity(intent);
                }
              
            }
        });
    }

    private void initView() {
        
        address = PrintUtil.getDefaultBluethoothDeviceAddress(getApplicationContext()).trim();
        if(address!=null&&address.length()>0){
            Intent intent = new Intent(getApplicationContext(), BtService.class);
            intent.setAction(PrintUtil.ACTION_PRINT_CONN);
            startService(intent);
        }
         imgPrinter=(ImageView) findViewById(R.id.img_printer_setting_icon);
         iv_back=(ImageView) findViewById(R.id.iv_back);
         txtPrinterTitle=(TextView) findViewById(R.id.txt_printer_setting_title);
         txtPrinterSummary=(TextView) findViewById(R.id.txt_printer_setting_summary);
         bt_print =(Button) findViewById(R.id.btn_printer_setting_test);
         bt_help_or_preview=(Button) findViewById(R.id.btn_printer_setting_test_bitmap);
         btn_printer_setting_scan=(Button) findViewById(R.id.btn_printer_setting_scan);
    }

    
    
    
    @Override
    protected void onStart() {
        super.onStart();
        MainController.init(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_BLUETOOTH_REQUEST && resultCode == Activity.RESULT_OK) {
            MainController.init(this);
        } else if (requestCode == OPEN_BLUETOOTH_REQUEST && resultCode == Activity.RESULT_CANCELED) {
            showToast("您已拒绝使用蓝牙");
            finish();
        }
    }

    @Override
    public void btStatusChanged(Intent intent) {
        super.btStatusChanged(intent);
        MainController.init(this);
    }

    /**
     * handle printer message
     *
     * @param event print msg event
     */
    public void onEventMainThread(PrintMsgEvent event) {
        if (event.type == PrinterMsgType.MESSAGE_TOAST) {
            showToast(event.msg);
        }
    }

}
