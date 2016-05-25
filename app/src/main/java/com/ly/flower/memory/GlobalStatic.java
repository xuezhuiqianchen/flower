package com.ly.flower.memory;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 2016/3/16.
 */
public class GlobalStatic {
    private static final String SHARE_USER_INFO = "flowers";
    public static final String UID          = "uid";
    public static final String NICKNAME     = "nickname_";
    public static final String PORTRAIT     = "portrait_";


    public static boolean saveSharedString(Context context, String key, String value)
    {
        try {
            SharedPreferences perference = context.getSharedPreferences(SHARE_USER_INFO, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = perference.edit();
            editor.putString(key, value);
            editor.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getSharedString(Context context, String key)
    {
        try {
            SharedPreferences perference = context.getSharedPreferences(SHARE_USER_INFO, Context.MODE_PRIVATE);
            return perference.getString(key, "");
        } catch (Exception e) {
            return "";
        }
    }
}
