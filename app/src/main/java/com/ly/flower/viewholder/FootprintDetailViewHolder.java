package com.ly.flower.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ly.common.utils.DimensionUtils;
import com.ly.common.utils.TimeUtils;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.share.ImageInfo;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 2016/3/24.
 */
public class FootprintDetailViewHolder {
    private RoundedImageView rivPortrait ;
    private TextView tvNickname;
    private TextView tvTime;
    private TextView tvContent;
    private TextView tvPlace;
    private TextView tvCommentNum;
    private TextView tvPraiseNum;
    private LinearLayout llImages;

    private ArrayList<ImageInfo> imgInfoList = new ArrayList<>();

    public FootprintDetailViewHolder(View parentView)
    {
        rivPortrait = (RoundedImageView) parentView.findViewById(R.id.riv_portrait);
        tvNickname = (TextView) parentView.findViewById(R.id.tv_nickname);
        tvTime = (TextView) parentView.findViewById(R.id.tv_time);
        tvContent = (TextView) parentView.findViewById(R.id.tv_content);
        tvPlace = (TextView) parentView.findViewById(R.id.tv_place);
        tvCommentNum = (TextView) parentView.findViewById(R.id.tv_comment_num);
        tvPraiseNum = (TextView) parentView.findViewById(R.id.tv_praise_num);
        llImages = (LinearLayout) parentView.findViewById(R.id.ll_images);
    }

    public void initData(BaseActivity activity, JSONObject object)
    {
        rivPortrait.setCornerRadius((float) DimensionUtils.dip2px(activity, 100));
        try {
            String strPortrait = object.getString("uavatar");
            String strNickname = object.getString("uname");
            String strTime = TimeUtils.parseToIntervalTimeFormat(object.getString("time"));
            String strContent = object.getString("title");

            String strCommentNum = object.getString("ccomment");
            String strPraiseNum = object.getString("cpraise");
            JSONArray imagesArray = object.getJSONArray("img");

            String strPlace = "";
            if (object.has("place")) {
                object.getString("place");
            }else {
                object.getString("sub_title");
            }

            tvNickname.setText(strNickname);
            tvTime.setText(strTime);
            tvContent.setText(strContent);
            tvPlace.setText(strPlace);
            tvCommentNum.setText(strCommentNum);
            tvPraiseNum.setText(strPraiseNum);
            activity.imageLoader.displayImage(strPortrait,
                    rivPortrait, activity.portraitOptions);
            initImages(activity, imagesArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initImages(BaseActivity activity, JSONArray array)
    {
        getBitmaps(activity, array);
    }

    private void showImages(BaseActivity activity, ArrayList<ImageInfo> imgInfoList) {
        llImages.removeAllViews();
        for (int i = 0; i < imgInfoList.size(); i++)
        {
            ImageInfo imageInfo = imgInfoList.get(i);
            if (imageInfo.isbOk()) {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout linearLayout = (LinearLayout)inflater.inflate(R.layout.layout_linear_image, llImages, false);

                if (imageInfo.isSpecial()) {
                    ArrayList<Bitmap> bitmaps = imageInfo.getBitmaps();
                    for (int j = 0; j < bitmaps.size(); j++) {
                        final ImageView imageView = (ImageView) inflater.inflate(R.layout.item_image, linearLayout, false);
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                        lp.width = imageInfo.getDisplayWidth();
                        lp.height = imageInfo.getDisplayHeight();
                        if (j == bitmaps.size() - 1)
                            lp.height = imageInfo.getLastHeight();
                        imageView.setLayoutParams(lp);
                        imageView.setImageBitmap(bitmaps.get(j));
                        linearLayout.addView(imageView);
                    }
                }else {
                    final ImageView imageView = (ImageView) inflater.inflate(R.layout.item_image, linearLayout, false);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    layoutParams.width = imageInfo.getDisplayWidth();
                    layoutParams.height = imageInfo.getDisplayHeight();
                    imageView.setLayoutParams(layoutParams);
                    imageView.setImageBitmap(imageInfo.getBitmaps().get(0));
                    linearLayout.addView(imageView);
                }
                llImages.addView(linearLayout);
            }
        }
        llImages.invalidate();
    }

    private void getBitmaps(final BaseActivity activity, final JSONArray array) {
        imgInfoList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++)
        {
            try {
                JSONObject imageObject = array.getJSONObject(i);
                String strImageUrl = imageObject.getString("url");
                String strWidth = imageObject.getString("width");
                String strLength = imageObject.getString("height");

                int imgWidthPx = Integer.valueOf(strWidth);
                int imgHeightPx = Integer.valueOf(strLength);
                final ImageInfo imageInfo = new ImageInfo(activity, imgWidthPx, imgHeightPx);
                imgInfoList.add(imageInfo);
                activity.imageLoader.loadImage(strImageUrl, activity.imageOptions, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        if (imageInfo.isSpecial()) {
                            imageInfo.getSpecialBitmaps(bitmap);
                        }else {
                            imageInfo.setBitmaps(bitmap);
                        }
                        showImages(activity, imgInfoList);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showImage(final LayoutInflater inflater, BaseActivity activity, final LinearLayout linearLayout, String strWidth,
                           String strLength, String strImageUrl) {
        int imgWidthPx = 0;
        int imgHeightPx = 0;

        try{
            imgWidthPx = Integer.valueOf(strWidth);
            imgHeightPx = Integer.valueOf(strLength);
            final ImageInfo imageInfo = new ImageInfo(activity, imgWidthPx, imgHeightPx);

            final ImageView imageView = (ImageView) inflater.inflate(R.layout.layout_linear_image, linearLayout, false);
            linearLayout.addView(imageView);
            activity.imageLoader.displayImage(strImageUrl, imageView, activity.imageOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if (imageInfo.isSpecial()) {
                        ArrayList<Bitmap> bitmaps = imageInfo.getSpecialBitmaps(bitmap);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                        layoutParams.width = imageInfo.getDisplayWidth();
                        layoutParams.height = imageInfo.getDisplayHeight();
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageBitmap(bitmaps.get(0));
                        for (int i = 1; i < bitmaps.size(); i++) {
                            final ImageView imageView = (ImageView) inflater.inflate(R.layout.layout_linear_image, linearLayout, false);
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                            lp.width = imageInfo.getDisplayWidth();
                            lp.height = imageInfo.getDisplayHeight();
                            imageView.setLayoutParams(lp);
                            imageView.setImageBitmap(bitmaps.get(i));
                            linearLayout.addView(imageView);
                        }
                    }else {
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                        layoutParams.width = imageInfo.getDisplayWidth();
                        layoutParams.height = imageInfo.getDisplayHeight();
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
