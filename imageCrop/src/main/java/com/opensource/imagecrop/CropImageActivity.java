/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.opensource.imagecrop;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.opensource.imagecrop.utils.Util;
import com.opensource.imagecrop.widget.CropImageView;
import com.opensource.imagecrop.widget.HighlightView;

/**
 * The activity can crop specific region of interest from an image.
 */
public class CropImageActivity extends MonitoredActivity {

    private static final String TAG = "CropImageActivity";

	public static final int CROP_MSG = 10;	
    public static final int CROP_MSG_INTERNAL = 100;    
    

    private final Handler mHandler = new Handler();

    private boolean mCircleCrop = false;
    // These options specifiy the output image size and whether we should
    // scale the output to fit it (or just crop it).
    private int mAspectX;
    private int mAspectY; // CR: two definitions per line == sad panda.
    private int mOutputX;
    private int mOutputY;
    private boolean mScale;
    private boolean mScaleUp = true;
    private Uri mSaveUri = null;
    // These are various options can be specified in the intent.
    private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG; // only
                                                                              // used
                                                                              // with
                                                                              // mSaveUri

    boolean mSaving; // Whether the "save" button is already clicked.

    private CropImageView mImageView;
    private ContentResolver mContentResolver;

    private Bitmap mBitmap;
    private Uri mInputUri;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mContentResolver = getContentResolver();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cropimage);

        mImageView = (CropImageView) findViewById(R.id.image);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            if (extras.getString(CropConfig.EXTRA_CIRCLE_CROP) != null) {
                mCircleCrop = true;
                mAspectX = 1;
                mAspectY = 1;
                //TODO Add some code to make fomat is png when crop a circle image.
            }
            mSaveUri = extras.getParcelable(CropConfig.EXTRA_OUTPUT);
            if (mSaveUri != null) {
                String outputFormatString = extras.getString(CropConfig.EXTRA_OUTPUT_FORMAT);
                if (outputFormatString != null) {
                    mOutputFormat = Bitmap.CompressFormat.valueOf(outputFormatString);
                }
            }
            mBitmap = extras.getParcelable(CropConfig.EXTRA_DATA);
            mAspectX = extras.getInt(CropConfig.EXTRA_ASPECT_X);
            mAspectY = extras.getInt(CropConfig.EXTRA_ASPECT_Y);
            mOutputX = extras.getInt(CropConfig.EXTRA_OUTPUT_X);
            mOutputY = extras.getInt(CropConfig.EXTRA_OUTPUT_Y);
            mScale = extras.getBoolean(CropConfig.EXTRA_SCALE, true);
            mScaleUp = extras.getBoolean(CropConfig.EXTRA_SCALE_UP_IF_NEEDED, true);
        }

        if (mBitmap == null) {
            // Create a MediaItem representing the URI.
            mInputUri = intent.getData();

            File imageFile = Util.parseUriToFile(this, mInputUri);


            if(null != imageFile) {
                String imagePath = imageFile.getPath();
                Log.i(TAG, "Parse Uri to file, file path : " + imagePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imagePath, options);
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                options.inSampleSize = calculateInSampleSize(options,
                        displayMetrics.widthPixels, displayMetrics.heightPixels);
                options.inJustDecodeBounds = false;
                mBitmap = BitmapFactory.decodeFile(imagePath, options);

            }
        }

        if (mBitmap == null) {
            Log.e(TAG, "Cannot load bitmap, exiting.");
            finish();
            return;
        }

        // Make UI fullscreen.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewById(R.id.discard).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveClicked();
            }
        });


        mImageView.setImageBitmapResetBase(mBitmap, true);

        makeCropView();

    }

    /**
     * Calculate an inSampleSize for use in a {@link android.graphics.BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link android.graphics.BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int width = options.outWidth;
        final int height = options.outHeight;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            } else {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further.
            final float totalReqPixelsCap = reqWidth * reqHeight;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * Save button clicked
     */
    private void onSaveClicked() {
        if (mSaving)
            return;

        if (mImageView.getCropView() == null) {
            return;
        }

        mSaving = true;
        mImageView.setSaving(true);

        Rect r = mImageView.getCropRect();

        int width = r.width(); // CR: final == happy panda!
        int height = r.height();

        // If we are circle cropping, we want alpha channel, which is the
        // third param here.
        Bitmap croppedImage = Bitmap.createBitmap(width, height, mCircleCrop ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        {
            Canvas canvas = new Canvas(croppedImage);
            Rect dstRect = new Rect(0, 0, width, height);
            canvas.drawBitmap(mBitmap, r, dstRect, null);
        }

        if (mCircleCrop) {
            // OK, so what's all this about?
            // Bitmaps are inherently rectangular but we want to return
            // something that's basically a circle. So we fill in the
            // area around the circle with alpha. Note the all important
            // PortDuff.Mode.CLEARes.
            Canvas c = new Canvas(croppedImage);
            Path p = new Path();
            p.addCircle(width / 2F, height / 2F, width / 2F, Path.Direction.CW);
            c.clipPath(p, Region.Op.DIFFERENCE);
            c.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
        }

        // If the output is required to a specific size then scale or fill.
        if (mOutputX != 0 && mOutputY != 0) {
            if (mScale) {
                // Scale the image to the required dimensions.
                Bitmap old = croppedImage;
                croppedImage = Util.transform(new Matrix(), croppedImage, mOutputX, mOutputY, mScaleUp);
                if (old != croppedImage) {
                    old.recycle();
                }
            } else {

                /*
                 * Don't scale the image crop it to the size requested. Create
                 * an new image with the cropped image in the center and the
                 * extra space filled.
                 */

                // Don't scale the image but instead fill it so it's the
                // required dimension
                Bitmap b = Bitmap.createBitmap(mOutputX, mOutputY, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(b);

                Rect srcRect = mImageView.getCropRect();
                Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);

                int dx = (srcRect.width() - dstRect.width()) / 2;
                int dy = (srcRect.height() - dstRect.height()) / 2;

                // If the srcRect is too big, use the center part of it.
                srcRect.inset(Math.max(0, dx), Math.max(0, dy));

                // If the dstRect is too big, use the center part of it.
                dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

                // Draw the cropped bitmap in the center.
                canvas.drawBitmap(mBitmap, srcRect, dstRect, null);

                // Set the cropped bitmap as the new bitmap.
                croppedImage.recycle();
                croppedImage = b;
            }
        }

        // Return the cropped image directly or save it to the specified URI.
        Bundle myExtras = getIntent().getExtras();
        if (myExtras != null && (myExtras.getParcelable(CropConfig.EXTRA_DATA) != null || myExtras.getBoolean(CropConfig.EXTRA_RETURN_DATA))) {
            Bundle extras = new Bundle();
            extras.putParcelable(CropConfig.EXTRA_DATA, croppedImage);
            setResult(RESULT_OK, (new Intent()).setAction(CropConfig.ACTION_INLINE_DATA).putExtras(extras));
            finish();
        } else {
            final Bitmap b = croppedImage;
            final Runnable save = new Runnable() {
                public void run() {
                    saveOutput(b);
                }
            };
            Util.startBackgroundJob(this, null, getResources().getString(R.string.saving_image), save, mHandler);
        }
    }

    /**
     * Save the cropped image to file<br/>
     * <br/><p/>If the output file has been set, it won't insert the image message inout ContentProvider<br/>
     * @param croppedImage
     */
    private void saveOutput(Bitmap croppedImage) {
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = mContentResolver.openOutputStream(mSaveUri);
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 75, outputStream);
                }
                // TODO ExifInterface write
            } catch (IOException ex) {
                Log.e(TAG, "Cannot open file: " + mSaveUri, ex);
            } finally {
                Util.closeSilently(outputStream);
            }
            Bundle extras = new Bundle();
            setResult(RESULT_OK, new Intent(mSaveUri.toString()).putExtras(extras));
        } else {
            Bundle extras = new Bundle();
            extras.putString(CropConfig.EXTRA_RECT, mImageView.getCropRect().toString());
            File oldFile = Util.parseUriToFile(this, mInputUri);
            File directory = new File(oldFile.getParent());
            int x = 0;
            String fileName = oldFile.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));

            // Try file-1.jpg, file-2.jpg, ... until we find a filename
            // which
            // does not exist yet.
            while (true) {
                x += 1;
                File candidateFile = new File(directory, fileName + "-" + x + ".jpg");
                if (!candidateFile.exists()) { // CR: inline the expression for exists
                               // here--it's clear enough.
                    break;
                }
            }

            String title = fileName + "-" + x;
            String finalFileName = title + ".jpg";
            int[] degree = new int[1];
            Double latitude = null;
            Double longitude = null;
            Uri newUri = Util.addImage(mContentResolver, title,
                    System.currentTimeMillis() / 1000, System.currentTimeMillis(), latitude,
                    longitude, directory.toString(), finalFileName,
                    croppedImage, null, degree);
            if (newUri != null) {
                setResult(RESULT_OK, new Intent().setAction(newUri.toString()).putExtras(extras));
            } else {
                setResult(RESULT_OK, new Intent().setAction(null));
            }
        }
        croppedImage.recycle();
        finish();
    }

    /**
     * Create a HightlightView to show how to crop the image.
     */
    private void makeCropView() {
        HighlightView hv = new HighlightView(mImageView);

        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        Rect imageRect = new Rect(0, 0, width, height);

        // CR: sentences!
        // make the default size about 4/5 of the width or height
        int cropWidth = Math.min(width, height) * 4 / 5;
        int cropHeight = cropWidth;

        if (mAspectX != 0 && mAspectY != 0) {
            if (mAspectX > mAspectY) {
                cropHeight = cropWidth * mAspectY / mAspectX;
            } else {
                cropWidth = cropHeight * mAspectX / mAspectY;
            }
        }

        int x = (width - cropWidth) / 2;
        int y = (height - cropHeight) / 2;

        RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
        hv.setup(mImageView.getImageMatrix(), imageRect, cropRect, mCircleCrop, mAspectX != 0 && mAspectY != 0);
        hv.setFocus(true);
        mImageView.setCropView(hv);
    }
}