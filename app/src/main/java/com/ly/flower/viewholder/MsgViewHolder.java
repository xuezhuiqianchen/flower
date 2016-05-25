package com.ly.flower.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ly.common.utils.DimensionUtils;
import com.ly.flower.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/3/23.
 */
public class MsgViewHolder {
    private RoundedImageView rivPortrait ;
    private TextView tvNickname;
    private TextView tvContent;
    private TextView tvSubContent;
    private TextView tvTime;
    private TextView tvClub;

    private ImageView ivReadFlag;
    private ImageView ivPraiseFlag;

    public MsgViewHolder(View parentView)
    {
        rivPortrait = (RoundedImageView) parentView.findViewById(R.id.riv_portrait);
        tvNickname = (TextView) parentView.findViewById(R.id.tv_nickname);
        tvContent = (TextView) parentView.findViewById(R.id.tv_content);
        tvSubContent = (TextView) parentView.findViewById(R.id.tv_sub_content);
        tvTime = (TextView) parentView.findViewById(R.id.tv_time);
        tvClub = (TextView) parentView.findViewById(R.id.tv_club);

        ivReadFlag = (ImageView) parentView.findViewById(R.id.iv_read_flag);
        ivPraiseFlag = (ImageView) parentView.findViewById(R.id.iv_praise);
    }

    public void initData(Context context, JSONObject object)
    {
        rivPortrait.setCornerRadius((float) DimensionUtils.dip2px(context, 100));
        try {
            String strPortrait = object.getString("icon");
            String strNickname = object.getString("sname");

            String strTime = object.getString("time");
            String strClub = object.getString("cname");
            String strType = object.getString("type");
            String strContent = "";
            String strSubContent = object.getString("sub_content");

            tvNickname.setText(strNickname);
            tvTime.setText(strTime);
            tvClub.setText(strClub);
            tvSubContent.setText(strSubContent);

            if (object.has("read") && object.getString("read").equals("1"))
            {
                ivReadFlag.setVisibility(View.VISIBLE);
            }else {
                ivReadFlag.setVisibility(View.GONE);
            }

            if (strType.equals("0"))
            {
                ivPraiseFlag.setVisibility(View.VISIBLE);
                tvContent.setVisibility(View.GONE);
            }else {
                ivPraiseFlag.setVisibility(View.GONE);
                tvContent.setVisibility(View.VISIBLE);
                strContent = object.getString("content");
                tvContent.setText(strContent);
            }
            ImageLoader.getInstance().displayImage(strPortrait, rivPortrait);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
