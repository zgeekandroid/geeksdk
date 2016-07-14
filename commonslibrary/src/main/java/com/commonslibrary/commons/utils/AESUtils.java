package com.commonslibrary.commons.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * date        :  2016-02-16  10:59
 * author      :  Mickaecle gizthon
 * description :
 */
public class AESUtils {
    private static SecretKeySpec mSecretSpec = null;

    /**
     * RSA加密后的密钥
     */
    private static byte[] mEncodedRawKey = null;

    public static byte[] encrypt(byte[] rawkey, byte[] data) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, mSecretSpec);
        byte[] encrypted = cipher.doFinal(data);
        if(null == mEncodedRawKey){

            /**
             * 密钥进行RSA加密
             */
            mEncodedRawKey = RSAUtils.encodeData(rawkey);
        }

        byte[] encodedData = new byte[encrypted.length + mEncodedRawKey.length];
        for(int i = 0; i < encrypted.length; i++){
            encodedData[i] = encrypted[i];
        }
        for(int i = encrypted.length , j = 0; j < mEncodedRawKey.length; i++, j++){
            encodedData[i] = mEncodedRawKey[j];
        }
        return encodedData;

    }

}
