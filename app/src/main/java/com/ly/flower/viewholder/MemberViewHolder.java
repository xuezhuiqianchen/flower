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
public class MemberViewHolder {
    private RoundedImageView rivPortrait ;
    private TextView tvNickname;
    private TextView tvLevel;
    private TextView tvTime;

    public MemberViewHolder(View parentView)
    {
        rivPortrait = (RoundedImageView) parentView.findViewById(R.id.riv_portrait);
        tvNickname = (TextView) parentView.findViewById(R.id.tv_nickname);
        tvLevel = (TextView) parentView.findViewById(R.id.tv_level);
        tvTime = (TextView) parentView.findViewById(R.id.tv_time);
    }

    public void initData(BaseActivity activity, JSONObject object)
    {
        rivPortrait.setCornerRadius((float) DimensionUtils.dip2px(activity, 100));
        try {
            String strPortrait = object.getString("mavatar");
            String strNickname = object.getString("mname");
            String strLevel = object.getString("mlevel");
            String strTime = TimeUtils.parseToIntervalTimeFormat(object.getString("join_time"));

            tvNickname.setText(strNickname);
            tvLevel.setText(strLevel);
            tvTime.setText(strTime);
            activity.imageLoader.displayImage(strPortrait, rivPortrait, activity.portraitOptions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
