package com.ly.flower.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.ly.common.utils.DimensionUtils;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/5/23.
 */
public class MyCommentViewHolder {
    private RoundedImageView rivPortrait ;
    private TextView tvNickname;
    private TextView tvContent;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvClub;

    public MyCommentViewHolder(View parentView)
    {
        rivPortrait = (RoundedImageView) parentView.findViewById(R.id.riv_portrait);
        tvNickname = (TextView) parentView.findViewById(R.id.tv_nickname);
        tvContent = (TextView) parentView.findViewById(R.id.tv_content);
        tvTitle = (TextView) parentView.findViewById(R.id.tv_title);
        tvTime = (TextView) parentView.findViewById(R.id.tv_time);
        tvClub = (TextView) parentView.findViewById(R.id.tv_club);
    }

    /**
     * {
     "id": "5",
     "type": "1足迹评论，2话题评论",
     "icontent":"内容来源的id",
     "avatar": "头像",
     "nickname": "昵称",
     "time": "2016-03-24 11:46:05",
     "content": "评论内容",
     "title": "大标题",
     "cname": "俱乐部名称"
     }
     * @param activity
     * @param object
     */
    public void initData(BaseActivity activity, JSONObject object)
    {
        rivPortrait.setCornerRadius((float) DimensionUtils.dip2px(activity, 100));
        try {
            String strPortrait = object.getString("avatar");
            String strNickname = object.getString("nickname");
            String strTime = object.getString("time");
            String strClub = object.getString("cname");
            String strContent = object.getString("content");
            String strTitle = object.getString("title");

            tvNickname.setText(strNickname);
            tvTime.setText(strTime);
            tvClub.setText(strClub);
            tvTitle.setText(strTitle);
            tvContent.setText(strContent);
            activity.imageLoader.displayImage(strPortrait, rivPortrait, activity.portraitOptions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
