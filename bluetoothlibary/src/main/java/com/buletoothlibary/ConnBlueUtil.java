package com.buletoothlibary;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.TscCommand;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;
import com.gprinter.service.GpPrintService;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author :lenovo
 * @date:2016/7/21
 */

public  class ConnBlueUtil {
    private GpService mGpService = null;
    private PrinterServiceConnection conn = null;
    private Context mContext;
    private Context bContext;
    static ConnBlueUtil INSTANCE;
    private final static String DEBUG_TAG = "SamleApp";
    private PortParameters mPortParam[]= new PortParameters[GpPrintService.MAX_PRINTER_CNT];
    private int mPrinterId=0;
    private PortParamDataBase database;
    private TextView status;

    public static ConnBlueUtil getInstance() {
        if(null == INSTANCE) {
            INSTANCE = new ConnBlueUtil();
        }
        return INSTANCE;
    }

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    }

    public void  init(Context context){
        this.mContext=context;
        initPortParam();
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(context,GpPrintService.class);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    private void initPortParam() {
        String blueAd=null;
        boolean[] state =getConnectState();
        database = new PortParamDataBase(mContext);
        PortParameters portParameters = database.queryPortParamDataBase("" + 0);
        if(portParameters!=null){
            blueAd = portParameters.getBluetoothAddr().toString().trim();
        }
        if(blueAd==null||blueAd.length()==0) {
            for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
                mPortParam[i] = new PortParameters();
                mPortParam[i] = database.queryPortParamDataBase("" + i);
                mPortParam[i].setPortOpenState(state[i]);
            }
        }
    }
    /**
     *检查连接状态
     *@author:BingCheng
     *@date:2016/7/23 11:46
     */
    public boolean[] getConnectState() {
        boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            state[i] = false;
        }
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            try {
                if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
                    state[i] = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return state;
    }
    /**
     *检查连接打印机状态
     *@author:BingCheng
     *@date:2016/7/23 11:45
     */
    public void getPrinterStatusClicked() {
        try {
            int status = mGpService.queryPrinterStatus(mPrinterId, 500);
            String str = new String();
            if (status == GpCom.STATE_NO_ERR) {
                str = "打印机正常";
            } else {
                str = "打印机 ";
                if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                    str += "脱机";
                }
                if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                    str += "缺纸";
                }
                if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                    str += "打印机开盖";
                }
                if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                    str += "打印机出错";
                }
                if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                    str += "查询超时";
                }
            }
            Toast.makeText(mContext,
                    "打印机：" + mPrinterId + " 状态：" + str, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }
    /**
     *关闭蓝牙
     *@author:BingCheng
     *@date:2016/7/26 10:08
     */
    public void closeBlue(){
        disconnectDevice();
        bContext.unregisterReceiver(PrinterStatusBroadcastReceiver);
        bContext.unregisterReceiver(mBluetoothReceiver);
    }
    /**
     *关闭时清空数据
     *@author:BingCheng
     *@date:2016/7/23 11:45
     */
    public void  close(){
        disconnectDevice();
        bContext.unregisterReceiver(PrinterStatusBroadcastReceiver);
        bContext.unregisterReceiver(mBluetoothReceiver);
        if (conn != null) {
            mContext.unbindService(conn);
        }
        database.close();
    }
    /**
     *蓝牙 usb 网络 连接检查端口
     *@author:BingCheng
     *@date:2016/7/23 11:43
     */
    public Boolean CheckPortParameters(PortParameters param) {
        boolean rel = false;
        int type = param.getPortType();
        if (type == PortParameters.BLUETOOTH) {
            if (!param.getBluetoothAddr().equals("")) {
                rel = true;
            }
        } else if (type == PortParameters.ETHERNET) {
            if ((!param.getIpAddr().equals("")) && (param.getPortNumber() != 0)) {
                rel = true;
            }
        } else if (type == PortParameters.USB) {
            if (!param.getUsbDeviceName().equals("")) {
                rel = true;
            }
        }
        return rel;
    }
    /**
     *关闭连接
     *@author:BingCheng
     *@date:2016/7/25 15:33
     */
    public void disconnectDevice(){
        try {
            mGpService.closePort(mPrinterId);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     *   蓝牙 usb 网络 连接（默认蓝牙）
     *@author:BingCheng
     *@date:2016/7/23 11:40
     */
    public boolean connectDevice(int PrinterId) {
        mPortParam[PrinterId]=database.queryPortParamDataBase(PrinterId+"");
        mPrinterId = PrinterId;
        boolean conn=false;
        int rel = 0;
        if (CheckPortParameters(mPortParam[PrinterId])) {
            try {
                mGpService.closePort(mPrinterId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            switch (mPortParam[PrinterId].getPortType()) {
                case PortParameters.USB:
                    try {
                        rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getUsbDeviceName(), 0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case PortParameters.ETHERNET:
                    try {
                        rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getIpAddr(), mPortParam[PrinterId].getPortNumber());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case PortParameters.BLUETOOTH:
                    try {
                        rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getBluetoothAddr(), 0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            Log.e(DEBUG_TAG, "result :" + String.valueOf(r));
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                messageBox(GpCom.getErrorText(r));
                conn=false;
            } else {
                mPortParam[PrinterId].setPortOpenState(true);
                database.modifyPortParam(PrinterId+"",   mPortParam[PrinterId]);
                conn=true;
            }
        } else {
            messageBox(mContext.getString(R.string.port_parameters_wrong));
            conn=false;
        }
        return conn;
        
    }
    /**
     *蓝牙 usb 网络 连接或者关闭（默认蓝牙）
     *@author:BingCheng
     *@date:2016/7/23 11:40
     */
    public void connectDeviceOrOff(int PrinterId) {
        mPortParam[PrinterId]=database.queryPortParamDataBase(PrinterId+"");
        mPrinterId = PrinterId;
        int rel = 0;
        if (mPortParam[PrinterId].getPortOpenState() == false) {
            if (CheckPortParameters(mPortParam[PrinterId])) {
                try {
                    mGpService.closePort(mPrinterId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                switch (mPortParam[PrinterId].getPortType()) {
                    case PortParameters.USB:
                        try {
                            rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getUsbDeviceName(), 0);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case PortParameters.ETHERNET:
                        try {
                            rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getIpAddr(), mPortParam[PrinterId].getPortNumber());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case PortParameters.BLUETOOTH:
                        try {
                            rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getBluetoothAddr(), 0);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                Log.e(DEBUG_TAG, "result :" + String.valueOf(r));
                if (r != GpCom.ERROR_CODE.SUCCESS) {
                    messageBox(GpCom.getErrorText(r));
                } else {
                    mPortParam[PrinterId].setPortOpenState(true);
                    database.modifyPortParam(PrinterId+"",   mPortParam[PrinterId]);
                }
            } else {
                messageBox(mContext.getString(R.string.port_parameters_wrong));
            }
        } else {
            try {
                mGpService.closePort(PrinterId);
                mPortParam[PrinterId].setPortOpenState(false);
                database.modifyPortParam(PrinterId+"",   mPortParam[PrinterId]);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     *测试打印
     *@author:BingCheng
     *@date:2016/7/23 11:51
     */
    public void printTestPageClicked() {
        try {
            int rel = mGpService.printeTestPage(mPrinterId); //
            Log.i("ServiceConnection", "rel " + rel);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(mContext, GpCom.getErrorText(r),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    /**
     *打印string
     *@author:BingCheng
     *@date:2016/7/23 14:41
     */
    public void printString(String string){
        try {
            int type = mGpService.getPrinterCommandType(mPrinterId);
            if (type == GpCom.ESC_COMMAND) {
                int status = mGpService.queryPrinterStatus(mPrinterId,500);
                if (status == GpCom.STATE_NO_ERR) {
                    EscCommand esc = new EscCommand();
                    esc.addPrintAndFeedLines((byte) 2);
                    esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF,EscCommand.ENABLE.OFF);//取消倍高倍宽
                    esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐
                    esc.addText(string, "utf-8");
                    esc.addPrintAndFeedLines((byte) 2);
                    esc.addPrintAndLineFeed();
                    Vector<Byte> data = esc.getCommand(); //发送数据
                    Byte[] Bytes = data.toArray(new Byte[data.size()]);
                    byte[] bytes = ArrayUtils.toPrimitive(Bytes);
                    String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
                    int rs;
                    try {
                        rs = mGpService.sendEscCommand(mPrinterId, sss);
                        GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
                        if (r != GpCom.ERROR_CODE.SUCCESS) {
                            Toast.makeText(mContext, GpCom.getErrorText(r),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(mContext,
                            "打印机错误！", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
       
    }
    
    
    public void getPrinterCommandTypeClicked() {
        try {
            int type = mGpService.getPrinterCommandType(mPrinterId);
            if (type == GpCom.ESC_COMMAND) {
                Toast.makeText(mContext, "打印机使用ESC命令",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "打印机使用TSC命令",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    void sendReceipt() {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte) 3);
        esc.addSelectCodePage(EscCommand.CODEPAGE.UYGUR);
        esc.addCancelKanjiMode();
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);//设置为倍高倍宽
        esc.addText("Sample\n");   //  打印文字
        esc.addPrintAndLineFeed();
        List<Byte> by = new ArrayList<>();

        for (byte i = (byte) 0x80; i <= (byte) 0xff; i++) {
            by.add(i);
        }

        byte[] bs = new byte[by.size()];
        int i = 0;
        for (byte b : by) {
            bs[i++] = b;
        }
        String str = Base64.encodeToString(bs, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendEscCommand(mPrinterId, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(mContext, GpCom.getErrorText(r),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //*打印文字*//*
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF,EscCommand.ENABLE.OFF);//取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐
        esc.addText("Print text\n");   //  打印文字
        esc.addText("Welcome to use Gprinter!\n");   //  打印文字
        esc.addText("你好吗", "utf-8");   //  打印文字

        //*打印繁体中文  需要打印机支持繁体字库*//*
        String message = Util.SimToTra("佳博票据打印机\n");
        //	esc.addText(message,"BIG5");
        esc.addText(message, "GB2312");
        esc.addPrintAndLineFeed();

        //*打印图片*//*
        esc.addText("Print bitmap!\n");   //  打印文字
        Bitmap b = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.gprinter);
        esc.addRastBitImage(b, b.getWidth(), 0);   //打印图片

        //*打印一维条码*//*
        esc.addText("Print code128\n");   //  打印文字
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);//设置条码可识别字符位置在条码下方
        esc.addSetBarcodeHeight((byte) 60); //设置条码高度为60点
        esc.addCODE128("Gprinter");  //打印Code128码
        esc.addPrintAndLineFeed();

        /**QRCode命令打印
         此命令只在支持QRCode命令打印的机型才能使用。
         在不支持二维码指令打印的机型上，则需要发送二维条码图片
         *
         esc.addText("Print QRcode\n");   //  打印文字	
         esc.addSelectErrorCorrectionLevelForQRCode((byte)0x31); //设置纠错等级
         esc.addSelectSizeOfModuleForQRCode((byte)3);//设置qrcode模块大小
         esc.addStoreQRCodeData("www.gprinter.com.cn");//设置qrcode内容
         esc.addPrintQRCode();//打印QRCode
         esc.addPrintAndLineFeed();*/

        //*打印文字*//*
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印左对齐
        esc.addText("Completed!\r\n");   //  打印结束
        esc.addPrintAndFeedLines((byte) 8);

        Vector<Byte> datas = esc.getCommand(); //发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(mPrinterId, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(mContext, GpCom.getErrorText(r),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void sendReceipt3() {
        EscCommand esc = new EscCommand();
        esc.addText("1234567890\n");   //  打印文字
        Vector<Byte> datas = esc.getCommand(); //发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendEscCommand(mPrinterId, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(mContext, GpCom.getErrorText(r),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void sendReceipt(int i) {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte) 3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);//设置为倍高倍宽
        esc.addText("Sample\n");   //  打印文字
        esc.addPrintAndLineFeed();

        //*打印文字*//*
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF,EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);//取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐
        esc.addText("Print text\n");   //  打印文字
        esc.addText("Welcome to use Gprinter!\n");   //  打印文字

        //*打印繁体中文  需要打印机支持繁体字库*//*
        String message = Util.SimToTra("佳博票据打印机\n");
        //	esc.addText(message,"BIG5");
        esc.addText(message, "GB2312");
        esc.addPrintAndLineFeed();

        //*打印图片*//*
        esc.addText("Print bitmap!\n");   //  打印文字
        Bitmap b = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.gprinter);
        esc.addRastBitImage(b, b.getWidth(), 0);   //打印图片

        //*打印一维条码*//*
        esc.addText("Print code128\n");   //  打印文字
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);//设置条码可识别字符位置在条码下方
        esc.addSetBarcodeHeight((byte) 60); //设置条码高度为60点
        esc.addCODE128("Gprinter");  //打印Code128码
        esc.addPrintAndLineFeed();

        //*QRCode命令打印
        //			此命令只在支持QRCode命令打印的机型才能使用。
        //			在不支持二维码指令打印的机型上，则需要发送二维条码图片
        //*
        esc.addText("Print QRcode\n");   //  打印文字
        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); //设置纠错等级
        esc.addSelectSizeOfModuleForQRCode((byte) 3);//设置qrcode模块大小
        esc.addStoreQRCodeData("www.gprinter.com.cn");//设置qrcode内容
        esc.addPrintQRCode();//打印QRCode
        esc.addPrintAndLineFeed();

        esc.addText("第 " + i + " 份\n");   //  打印文字
        //*打印文字*//*
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印左对齐
        esc.addText("Completed!\r\n");   //  打印结束
        esc.addPrintAndFeedLines((byte) 8);

        Vector<Byte> datas = esc.getCommand(); //发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendEscCommand(mPrinterId, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(mContext, GpCom.getErrorText(r),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void sendReceiptBmp(int i) {
        EscCommand esc = new EscCommand();
        //*打印图片*//*
        esc.addText("Print bitmap!\n");   //  打印文字
        Bitmap b = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.test);
        esc.addRastBitImage(b, 384, 0);   //打印图片
        esc.addText("第 " + i + " 份\n");   //  打印文字

        Vector<Byte> datas = esc.getCommand(); //发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendEscCommand(mPrinterId, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(mContext, GpCom.getErrorText(r),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void sendLabel() {
        TscCommand tsc = new TscCommand();
        tsc.addSize(60, 60); //设置标签尺寸，按照实际尺寸设置
        tsc.addGap(0);           //设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(TscCommand.DIRECTION.BACKWARD, TscCommand.MIRROR.NORMAL);//设置打印方向
        tsc.addReference(0, 0);//设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); //撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        //绘制简体中文
        tsc.addText(20, 20, TscCommand.FONTTYPE.SIMPLIFIED_CHINESE, TscCommand.ROTATION.ROTATION_0, TscCommand.FONTMUL.MUL_1, TscCommand.FONTMUL.MUL_1, "Welcome to use Gprinter!");
        //绘制图片
        Bitmap b = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.gprinter);
        tsc.addBitmap(20, 50, TscCommand.BITMAP_MODE.OVERWRITE, b.getWidth() * 2, b);

        tsc.addQRCode(250, 80, TscCommand.EEC.LEVEL_L, 5, TscCommand.ROTATION.ROTATION_0, " www.gprinter.com.cn");
        //绘制一维条码
        tsc.add1DBarcode(20, 250, TscCommand.BARCODETYPE.CODE128, 100, TscCommand.READABEL.EANBEL, TscCommand.ROTATION.ROTATION_0, "Gprinter");
        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); //打印标签后 蜂鸣器响
        Vector<Byte> datas = tsc.getCommand(); //发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendTscCommand(mPrinterId, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(mContext, GpCom.getErrorText(r),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void printReceiptClicked(View view) {
        try {
            int type = mGpService.getPrinterCommandType(mPrinterId);
            if (type == GpCom.ESC_COMMAND) {
                int status = mGpService.queryPrinterStatus(mPrinterId, 500);
                if (status == GpCom.STATE_NO_ERR) {
                    sendReceipt();
                } else {
                    Toast.makeText(mContext,
                            "打印机错误！", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void printLabelClicked(View view) {
        try {
            int type = mGpService.getPrinterCommandType(mPrinterId);
            if (type == GpCom.TSC_COMMAND) {
                int status = mGpService.queryPrinterStatus(mPrinterId, 500);
                if (status == GpCom.STATE_NO_ERR) {
                    sendLabel();
                } else {
                    Toast.makeText(mContext,
                            "打印机错误！", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    /**
     *测试打印多份
     *@author:BingCheng
     *@date:2016/7/25 15:26
     */
    public void printTestClicked(int copies) {
        try {
            int type = mGpService.getPrinterCommandType(mPrinterId);
            if (type == GpCom.ESC_COMMAND) {
                for (int i = 0; i < copies; i++) {
                    sendReceipt();
                }
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public void messageBox(String err) {
        Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
    }
    /**
     *判断是否有已配对接蓝牙并且连接
     *@author:BingCheng
     *@date:2016/7/23 11:15
     */
    public boolean getDeviceAndConn() {
        if(database.queryPortParamDataBase(0+"").getPortType()==5){
            return false;
        }
        //打开蓝牙
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null){
            messageBox("没有蓝牙模块");
            return false;
        }
        if(!bluetoothAdapter.isEnabled()){
            messageBox("请打开蓝牙");
            return false;
        }
        //默认蓝牙数据库地址id是0
        return connectDevice(0);
    }
    /**
     *保存到数据库中
     *@author:BingCheng
     *@date:2016/7/23 11:54
     */
    public void savedState(PortParameters portParam){
        mPortParam[mPrinterId]=portParam;
        if (CheckPortParameters(portParam)){
            database.deleteDataBase("" + 0);
            database.insertPortParam(0, portParam);
            //  PortParameters a = database.queryPortParamDataBase("" + 0);
        } else {
            messageBox(mContext.getString(R.string.port_parameters_wrong));
        }
    }
    /**
     *注册连接状态广播
     *@author:BingCheng
     *@date:2016/7/25 15:23
     */
    public void registerBroadcast(Context context, TextView status) {
        this.status=status;
        this.bContext=context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(GpCom.ACTION_CONNECT_STATUS);
        context.registerReceiver(PrinterStatusBroadcastReceiver, filter);
    }
    /**
     *连接状态广播
     *@author:BingCheng
     *@date:2016/7/25 15:22
     */
    private BroadcastReceiver PrinterStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (GpCom.ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                Log.d(DEBUG_TAG, "connect status " + type);
                if (type == GpDevice.STATE_CONNECTING) {
                    status.setText("正在连接中！");
                    messageBox("正在连接中！");
                } else if (type == GpDevice.STATE_NONE) {
                    status.setText("连接失败！");
                    messageBox("连接失败！");
                } else if (type == GpDevice.STATE_VALID_PRINTER) {
                    status.setText("连接成功！");
                    messageBox("连接成功！");
                } else if (type == GpDevice.STATE_INVALID_PRINTER) {
                    status.setText("连接失败，请使用佳博打印机！");
                    messageBox("请使用佳博打印机");
                }
            }
            
        }
    };
    /**
     *重新搜索蓝牙框
     *@author:BingCheng
     *@date:2016/7/26 11:00
     */
    public void searchBlueDialog() {
        int REQUEST_CONNECT_DEVICE=3;
        Intent intent = new Intent(bContext, BluetoothListActivity.class);
        ((Activity)bContext).startActivityForResult(intent,REQUEST_CONNECT_DEVICE);
    }
    
    /**
     *注册关闭广播
     *@author:BingCheng
     *@date:2016/7/25 15:24
     */
    public void registerBoothCloseBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);//�����Ͽ�
         bContext.registerReceiver(mBluetoothReceiver, filter);
    }
    /**
     *关闭广播
     *@author:BingCheng
     *@date:2016/7/25 15:25
     */
    private final BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                try {
                    mGpService.closePort(mPrinterId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                try {
                    mGpService.closePort(mPrinterId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    
}
