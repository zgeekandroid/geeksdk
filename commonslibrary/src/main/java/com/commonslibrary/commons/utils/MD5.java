package com.commonslibrary.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static MessageDigest messagedigest = null;
    public static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private MD5() {
    }

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            nsaex.printStackTrace();
        }
    }

    public final static String getMessageDigest(byte[] buffer) {

        try {
            if (messagedigest == null){
                return "";
            }

            messagedigest.update(buffer);
            byte[] md = messagedigest.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }


    public static String getFileMD5(File file) throws IOException {
        if (MD5.messagedigest == null) {
            return "";
        }
        if (file == null){
            return "";
        }
        if (!file.exists()){
            return "";
        }

        InputStream fis;
        fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numRead = 0;
        while ((numRead = fis.read(buffer)) > 0) {
            MD5.messagedigest.update(buffer, 0, numRead);
        }
        fis.close();
        return bufferToHex(MD5.messagedigest.digest());
    }

    private static String getMD5String(byte[] bytes) {
        if (MD5.messagedigest == null) {
            return "";
        }
        MD5.messagedigest.update(bytes);
        return bufferToHex(MD5.messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = MD5.hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c1 = MD5.hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);

    }

    public static boolean checkPassword(String md5, String md5PwdStr) {
        if (md5 == null){
            return md5PwdStr == null;
        }
        return md5.equals(md5PwdStr);
    }
}
