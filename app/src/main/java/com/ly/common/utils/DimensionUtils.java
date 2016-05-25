package com.ly.common.utils;

import android.content.Context;

/**
 * Created by admin on 2016/4/5.
 */
public class DimensionUtils {

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
}
