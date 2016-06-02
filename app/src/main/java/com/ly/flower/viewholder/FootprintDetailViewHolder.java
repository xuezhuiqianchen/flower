package com.ly.flower.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ly.common.utils.DimensionUtils;
import com.ly.common.utils.TimeUtils;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
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
            ImageLoader.getInstance().displayImage(strPortrait, rivPortrait);

            initImages(activity, imagesArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initImages(BaseActivity activity, JSONArray array)
    {
        llImages.removeAllViews();
        for (int i = 0; i < array.length(); i++)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView imageView = (ImageView)inflater.inflate(R.layout.item_image, llImages, false);
            try {
                activity.imageLoader.displayImage(array.getJSONObject(i).getString("url"),
                        imageView, activity.imageOptions);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            llImages.addView(imageView);
        }
    }
}
