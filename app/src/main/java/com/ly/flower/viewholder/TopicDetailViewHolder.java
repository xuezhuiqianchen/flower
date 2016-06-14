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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2016/3/24.
 */
public class TopicDetailViewHolder {
    private RoundedImageView rivPortrait ;
    private TextView tvNickname;
    private TextView tvTime;
    private TextView tvTitle;
    private TextView tvSubTitle;
    private TextView tvCommentNum;
    private TextView tvPraiseNum;
    private LinearLayout llImages;
    private ImageView ivPlay;

    private ArrayList<ImageInfo> imgInfoList = new ArrayList<>();

    public TopicDetailViewHolder(View parentView)
    {
        rivPortrait = (RoundedImageView) parentView.findViewById(R.id.riv_portrait);
        tvNickname = (TextView) parentView.findViewById(R.id.tv_nickname);
        tvTime = (TextView) parentView.findViewById(R.id.tv_time);
        tvTitle = (TextView) parentView.findViewById(R.id.tv_title);
        tvSubTitle = (TextView) parentView.findViewById(R.id.tv_sub_title);
        tvCommentNum = (TextView) parentView.findViewById(R.id.tv_comment_num);
        tvPraiseNum = (TextView) parentView.findViewById(R.id.tv_praise_num);
        llImages = (LinearLayout) parentView.findViewById(R.id.ll_images);
        ivPlay = (ImageView) parentView.findViewById(R.id.iv_play);
    }

    public void initData(final BaseActivity activity, JSONObject object)
    {
        rivPortrait.setCornerRadius((float) DimensionUtils.dip2px(activity, 100));
        try {
            String strPortrait = object.getString("uavatar");
            String strNickname = object.getString("uname");
            String strTime = TimeUtils.parseToIntervalTimeFormat(object.getString("time"));
            String strTitle = object.getString("title");
            String strSubTitle = object.getString("sub_title");
            String strCommentNum = object.getString("ccomment");
            String strPraiseNum = object.getString("cpraise");
            String strCtype = object.getString("ctype");
            final String strVideoUrl = object.getString("url_video");
            JSONArray imagesArray = object.getJSONArray("img");

            tvNickname.setText(strNickname);
            tvTime.setText(strTime);
            tvTitle.setText(strTitle);
            tvSubTitle.setText(strSubTitle);
            tvCommentNum.setText(strCommentNum);
            tvPraiseNum.setText(strPraiseNum);

            activity.imageLoader.displayImage(strPortrait, rivPortrait, activity.portraitOptions);

            initImages(activity, imagesArray);

            if (strCtype.equals("0")) {
                ivPlay.setVisibility(View.GONE);
            }else {
//                ivPlay.setVisibility(View.VISIBLE);
//                ivPlay.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        activity.gotoActivity(MediaPlayerActivity.class, strVideoUrl);
//                    }
//                });
            }
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
        llImages.postInvalidate();
    }

    private void getBitmaps(final BaseActivity activity, JSONArray array) {
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
}
