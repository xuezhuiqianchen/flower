package com.ly.flower.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Handler;

import com.ly.common.utils.StringUtils;
import com.ly.flower.base.DataStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import io.vov.vitamio.provider.MediaStore;

/**
 * Created by admin on 2016/3/16.
 */
public class ImageLoaderUtil {


    public static Bitmap getVideoThumbnailByUrl(final String url, final Handler handler)
    {
        final String path = DataStructure.DIRACTOR_IMAGE + StringUtils.stringToMD5(url) + ".png";
        File file = new File(path);
        if (!file.exists())
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = createVideoThumbnail(url, 320, 300);
                    try {
                        File file = new File(DataStructure.DIRACTOR_IMAGE);
                        if (!file.exists())
                            file.mkdirs();

                        file = new File(path);
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MessageHandler.sendMessage(handler, MessageHandler.IMAGE_OK, url);
                }
            }).start();
        }else {
            return BitmapFactory.decodeFile(path);
        }
        return null;
    }

    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == android.provider.MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
}
