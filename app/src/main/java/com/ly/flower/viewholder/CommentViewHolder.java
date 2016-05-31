package com.ly.flower.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ly.common.utils.DimensionUtils;
import com.ly.common.utils.TimeUtils;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/3/24.
 */
public class CommentViewHolder {
    private RoundedImageView rivPortrait ;
    private TextView tvNickname;
    private TextView tvContent;
    private TextView tvLayer;
    private TextView tvTime;

    public CommentViewHolder(View parentView)
    {
        rivPortrait = (RoundedImageView) parentView.findViewById(R.id.riv_portrait);
        tvNickname = (TextView) parentView.findViewById(R.id.tv_nickname);
        tvContent = (TextView) parentView.findViewById(R.id.tv_content);
        tvLayer = (TextView) parentView.findViewById(R.id.tv_layer);
        tvTime = (TextView) parentView.findViewById(R.id.tv_time);
    }

    public void initData(BaseActivity activity, JSONObject object, int position)
    {
        rivPortrait.setCornerRadius((float) DimensionUtils.dip2px(activity, 100));
        try {
            String strPortrait = object.getString("avatar");
            String strNickname = object.getString("nickname");
            String strTime = TimeUtils.parseToIntervalTimeFormat(object.getString("time"));
            String strContent = object.getString("content");

            tvNickname.setText(strNickname);
            tvTime.setText(strTime);
            tvLayer.setText(String.valueOf(position) + "楼");
            tvContent.setText(strContent);
            activity.imageLoader.displayImage(strPortrait, rivPortrait, activity.portraitOptions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
