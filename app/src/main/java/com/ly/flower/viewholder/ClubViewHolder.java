package com.ly.flower.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ly.common.utils.Common;
import com.ly.common.utils.DimensionUtils;
import com.ly.flower.R;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * Created by admin on 2016/3/15.
 */;import org.json.JSONException;
import org.json.JSONObject;

public class ClubViewHolder {
    private RoundedImageView rivPortrait ;
    private TextView tvTitle;
    private TextView tvNum;
    private RoundedImageView rivImage;

    public ClubViewHolder(View parentView)
    {
        rivPortrait = (RoundedImageView) parentView.findViewById(R.id.riv_portrait);
        tvTitle = (TextView) parentView.findViewById(R.id.tv_title);
        tvNum = (TextView) parentView.findViewById(R.id.tv_num);
        rivImage = (RoundedImageView) parentView.findViewById(R.id.riv_image);
    }

    public void initData(Context context, JSONObject object)
    {
        rivPortrait.setCornerRadius((float) DimensionUtils.dip2px(context, 100));
        try {
            String strPortrait = object.getString("url_logo");
            String strBkUrl = object.getString("url_bk");
            String strTitle = object.getString("cname");
            String strNum = object.getString("cmember");

            rivImage.setImageResource(R.drawable.default_image);
            try{
                String strWidth = object.getString("url_bk_w");
                String strLength = object.getString("url_bk_h");
                int imgWidth = Integer.valueOf(strWidth);
                int imgHeight = Integer.valueOf(strLength);

                int relWidth = Common.DEVICE_SCREEN_WIDTH - DimensionUtils.dip2px(context, 20);
                int relHeight = imgHeight * relWidth / imgWidth;
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(relWidth, relHeight);
                layoutParams.addRule(RelativeLayout.BELOW, R.id.tv_member);
                rivImage.setLayoutParams(layoutParams);
                rivImage.setScaleType(ImageView.ScaleType.FIT_XY);
            }catch (Exception e) {
               e.printStackTrace();
            }

            tvTitle.setText(strTitle);
            tvNum.setText(strNum);
            ImageLoader.getInstance().displayImage(strPortrait, rivPortrait);
            ImageLoader.getInstance().displayImage(strBkUrl, rivImage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
