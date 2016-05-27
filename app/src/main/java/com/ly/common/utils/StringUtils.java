package com.ly.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by admin on 2016/4/5.
 *
 */
public class StringUtils {

    /**
     *  string to MD5
     *
     * @param string
     * @return
     */
    public static String stringToMD5(String string) {
        byte[]  hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    /**
     *  InputStream to String
     *
     * @param
     * @return String
     */
    public String inputStream2String (InputStream in) throws IOException {

        StringBuffer out = new StringBuffer();
        byte[]  b = new byte[4096];
        int n;
        while ((n = in.read(b))!= -1){
            out.append(new String(b,0,n));
        }

        return  out.toString();
    }
}
