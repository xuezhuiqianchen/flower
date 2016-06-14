package com.ly.flower.share;

import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;

import com.ly.common.utils.Common;
import com.ly.common.utils.DimensionUtils;
import com.ly.flower.base.BaseActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 2016/3/16.
 */
public class ImageInfo implements Serializable {

    private int width = 0;
    private int height = 0;
    private int dealWidth = 0;
    private int dealHeight = 0;
    private int displayWidth = 0;
    private int displayHeight = 0;
    private int relHeight = 0;
    private int maxHeight = ImageUtils.getMaxHeight();
    private int lastHeight = 0;

    private boolean special = false;
    private boolean bOk = false;
    private ArrayList<Bitmap> bitmaps;


    public ImageInfo(BaseActivity activity, int width, int height) {
        this.width = width;
        this.height = height;
        this.dealWidth = DimensionUtils.dip2px(activity, width);
        this.dealHeight = DimensionUtils.dip2px(activity, height);

        displayWidth = Common.DEVICE_SCREEN_WIDTH - DimensionUtils.dip2px(activity, 20);
        displayHeight = dealHeight * displayWidth / dealWidth;

        if (displayWidth > dealWidth) {
            displayWidth = dealWidth;
            displayHeight = dealHeight;
        }

        relHeight = displayHeight;

        if (displayHeight > Common.DEVICE_SCREEN_HEIGHT * 1.5) {
            displayHeight = Common.DEVICE_SCREEN_HEIGHT;
            special = true;
        }
    }

    public boolean isbOk() {
        return bOk;
    }

    public boolean isSpecial() {
        return special;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public ArrayList<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public int getLastHeight() {
        return lastHeight;
    }

    public void setBitmaps(Bitmap bitmap) {
        this.bitmaps = new ArrayList<>();
        bitmaps.add(bitmap);
        bOk = true;
    }

    public Bitmap clipBitmap(Bitmap bitmap) {
        if (!special)
            return bitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapRegionDecoder mDecoder = null;
        Rect bsrc = new Rect();
        try {
            mDecoder = BitmapRegionDecoder.newInstance(isBm, true);

            bsrc.left = 0;
            bsrc.top = 0;
            bsrc.right = width;
            bsrc.bottom = width * displayHeight / displayWidth;
            Bitmap bmp = mDecoder.decodeRegion(bsrc, null);
            return bmp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Bitmap> getSpecialBitmaps(Bitmap bitmap) {
        bitmaps = new ArrayList<>();
        try{

            int imageCount = relHeight / maxHeight + 1;
            int heightUnit = width * maxHeight / displayWidth;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            BitmapRegionDecoder mDecoder = BitmapRegionDecoder.newInstance(isBm, true);
            Rect bsrc = new Rect();
            for (int i = 0; i < imageCount; i++) {
                bsrc.left = 0;
                bsrc.top = i * heightUnit;
                bsrc.right = width;
                bsrc.bottom = (i + 1) * heightUnit;
                if (i == imageCount - 1) {
                    bsrc.bottom = height;
                    lastHeight = bsrc.bottom - bsrc.top;
                }
                Bitmap bmp = mDecoder.decodeRegion(bsrc,null);
                bitmaps.add(bmp);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        bOk = true;
        return bitmaps;
    }
}
