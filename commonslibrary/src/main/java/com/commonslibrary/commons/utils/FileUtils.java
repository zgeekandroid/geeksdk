package com.commonslibrary.commons.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * date        :  2015-12-02  10:22
 * author      :  Mickaecle gizthon
 * description :
 */
public class FileUtils {

    public static File bitmapTransferToFile(String filePath, Bitmap bitmap) {
        File localFile = new File(filePath);
        FileOutputStream fOut = null;
        try {
            localFile.createNewFile();
            fOut = new FileOutputStream(localFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fOut) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return localFile;
    }


    public static byte[] readFromFile(String fileName, int offset, int len) {
        String TAG = "";
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            Log.i(TAG, "readFromFile: file not found");
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

        if (offset < 0) {
            Log.e(TAG, "readFromFile invalid offset:" + offset);
            return null;
        }
        if (len <= 0) {
            Log.e(TAG, "readFromFile invalid len:" + len);
            return null;
        }
        if (offset + len > (int) file.length()) {
            Log.e(TAG, "readFromFile invalid file len:" + file.length());
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len];
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
        }
        return b;
    }

    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            LogUtils.i("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;

            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                LogUtils.i("line " + line + ": " + tempString);
                builder.append(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

}
