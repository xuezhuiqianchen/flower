package com.ly.flower.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ly.common.utils.DimensionUtils;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.activity.MediaPlayerActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public void initData(final BaseActivity context, JSONObject object)
    {
        rivPortrait.setCornerRadius((float) DimensionUtils.dip2px(context, 100));
        try {
            String strPortrait = object.getString("uavatar");
            String strNickname = object.getString("uname");
            String strTime = object.getString("time");
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
            ImageLoader.getInstance().displayImage(strPortrait, rivPortrait);
            initImages(context, imagesArray);

            if (strCtype.equals("0")) {
                ivPlay.setVisibility(View.GONE);
            }else {
                ivPlay.setVisibility(View.VISIBLE);
                ivPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.gotoActivity(MediaPlayerActivity.class, strVideoUrl);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initImages(Context context, JSONArray array)
    {
        llImages.removeAllViews();
        for (int i = 0; i < array.length(); i++)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView imageView = (ImageView)inflater.inflate(R.layout.item_image, llImages, false);
            try {
                ImageLoader.getInstance().displayImage(array.getJSONObject(i).getString("url"), imageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            llImages.addView(imageView);
        }
    }
}
