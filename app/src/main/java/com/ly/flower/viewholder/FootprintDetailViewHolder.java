package com.ly.flower.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ly.common.utils.DimensionUtils;
import com.ly.flower.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public void initData(Context context, JSONObject object)
    {
        rivPortrait.setCornerRadius((float) DimensionUtils.dip2px(context, 100));
        try {
            String strPortrait = object.getString("uavatar");
            String strNickname = object.getString("uname");
            String strTime = object.getString("time");
            String strContent = object.getString("title");
            String strPlace = object.getString("place");
            String strCommentNum = object.getString("ccomment");
            String strPraiseNum = object.getString("cpraise");
            JSONArray imagesArray = object.getJSONArray("img");

            tvNickname.setText(strNickname);
            tvTime.setText(strTime);
            tvContent.setText(strContent);
            tvPlace.setText(strPlace);
            tvCommentNum.setText(strCommentNum);
            tvPraiseNum.setText(strPraiseNum);
            ImageLoader.getInstance().displayImage(strPortrait, rivPortrait);

            initImages(context, imagesArray);
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
