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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluetoothlibrary.print.PrintUtil;


/**
 * 打印界面
 */
public class PrintDataActivity extends Activity {

    private ImageView iv_back;
    private TextView print_tv_info;
    private Button print_btn_print;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_data);
        initView();
        initListener();
    }

    private void initView() {
//        message =  getIntent().getStringExtra("message");
        message =  PrintUtil.getDefaultBluetoothInfo(PrintDataActivity.this);
        iv_back=(ImageView) findViewById(R.id.iv_back);
        print_tv_info=(TextView) findViewById(R.id.print_tv_info);
        print_tv_info.setText(message);
        print_btn_print=(Button) findViewById(R.id.print_btn_print);
        
    }

    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        print_btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BtService.class);
                
//                intent.putExtra("message",message);
                intent.setAction(PrintUtil.ACTION_PRINT_TEST);
                startService(intent);
            }
        });  
    }
}
