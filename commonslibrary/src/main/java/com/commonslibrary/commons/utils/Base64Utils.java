package com.commonslibrary.commons.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.ow2.util.base64.Base64;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author :lenovo
 * @date:2016/3/25
 */

public class Base64Utils {
    public static String fileToStr(File imgFile )
    {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        FileInputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try
        {
            in = new FileInputStream(imgFile) ;
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        Base64 encoder=new Base64();
        return String.valueOf(encoder.encode(data));//返回Base64编码过的字节数组字符串
    }

    public static Bitmap strToBitmap(String imgStr)
    {//对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return null;
        Base64 decoder=new Base64();
        try
        {
            //Base64解码
            byte[] b = decoder.decode(imgStr.toCharArray());
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            Bitmap bitmap= BitmapFactory.decodeByteArray(b, 0, b.length);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    
    
    
    
}
