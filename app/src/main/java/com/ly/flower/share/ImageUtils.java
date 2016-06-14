package com.ly.flower.share;

import android.opengl.GLES10;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by admin on 2016/6/3.
 */
public class ImageUtils {
    private static final int DEFAULT_MAX_BITMAP_DIMENSION = 2048;
    private static int maxHeight = 0;

    public static int getMaxHeight(){
        int[] maxTextureSize = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
        maxHeight = Math.max(maxTextureSize[0], DEFAULT_MAX_BITMAP_DIMENSION);
        return maxHeight;
    }
}
