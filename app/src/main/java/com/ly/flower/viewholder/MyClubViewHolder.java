package com.ly.flower.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ly.common.utils.DimensionUtils;
import com.ly.flower.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/5/23.
 */
public class MyClubViewHolder {
    private RoundedImageView rivImage;
    private TextView tvName;


    public MyClubViewHolder(View parentView)
    {
        rivImage = (RoundedImageView) parentView.findViewById(R.id.riv_image);
        tvName = (TextView) parentView.findViewById(R.id.tv_name);
    }

    public void initData(Context context, JSONObject object)
    {
        rivImage.setCornerRadius((float) DimensionUtils.dip2px(context, 10));
        try {
            String strUrl = object.getString("url_bk");
            String strName = object.getString("cname");

            tvName.setText(strName);
            ImageLoader.getInstance().displayImage(strUrl, rivImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
