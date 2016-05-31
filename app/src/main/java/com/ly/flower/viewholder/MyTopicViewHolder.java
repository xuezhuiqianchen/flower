package com.ly.flower.viewholder;

import android.content.Context;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ly.common.utils.Common;
import com.ly.common.utils.DimensionUtils;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.share.Player;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/5/23.
 */
public class MyTopicViewHolder {
    private RoundedImageView rivPortrait ;
    private TextView tvNickname;
    private TextView tvTime;

    private TextView tvTitle;
    private TextView tvSubTitle;
    private TextView tvImageNum;
    private ImageView ivImage;
    private ImageView ivPlay;

    private SurfaceView surfaceView;
    private SeekBar skbProgress;
    private Player player;
    private TextView tvTotalTime;
    private TextView tvCurrentTime;
    private ImageView ivPlayPause;
    private View vMediaPlayerController;

    private TextView tvCommentNum;
    private TextView tvPraiseNum;
    private ImageView ivPraise;
    private RelativeLayout rlPraise;
    private RelativeLayout rlShare;

    public MyTopicViewHolder(View parentView)
    {
        rivPortrait = (RoundedImageView) parentView.findViewById(R.id.riv_portrait);
        tvNickname = (TextView) parentView.findViewById(R.id.tv_nickname);
        tvTime = (TextView) parentView.findViewById(R.id.tv_time);
        tvTitle = (TextView) parentView.findViewById(R.id.tv_title);
        tvSubTitle = (TextView) parentView.findViewById(R.id.tv_sub_title);

        View view = parentView.findViewById(R.id.rl_media_player);
        tvImageNum = (TextView) view.findViewById(R.id.tv_image_num);
        ivImage = (ImageView) view.findViewById(R.id.iv_image);
        ivPlay = (ImageView) view.findViewById(R.id.iv_play);
        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
        ivPlayPause = (ImageView) view.findViewById(R.id.iv_play_pause);
        tvCurrentTime = (TextView) view.findViewById(R.id.tv_time_current);
        tvTotalTime = (TextView) view.findViewById(R.id.tv_time_total);
        skbProgress = (SeekBar) view.findViewById(R.id.seekbar);
        vMediaPlayerController = view.findViewById(R.id.rl_controller);

        LinearLayout llEditBar = (LinearLayout) parentView.findViewById(R.id.edit_bar);
        tvCommentNum = (TextView) llEditBar.findViewById(R.id.tv_comment_num);
        tvPraiseNum = (TextView) llEditBar.findViewById(R.id.tv_praise_num);
        ivPraise = (ImageView) llEditBar.findViewById(R.id.iv_praise);
        rlPraise = (RelativeLayout) llEditBar.findViewById(R.id.rl_praise);
        rlShare = (RelativeLayout) llEditBar.findViewById(R.id.rl_share);
    }

    public void initData(BaseActivity activity, JSONObject object)
    {
        rivPortrait.setCornerRadius((float) DimensionUtils.dip2px(activity, 100));
        try {
            String strPortrait = object.getString("uavatar");
            String strNickname = object.getString("uname");
            String strTime = object.getString("time");
            JSONArray imageArray = object.getJSONArray("img");
            JSONObject imageObject = imageArray.getJSONObject(0);
            String strImageUrl = imageObject.getString("url");
            String strTitle = object.getString("title");
            String strSubTitle = object.getString("sub_title");
            String strCommentNum = object.getString("ccomment");
            String strPraiseNum = object.getString("cpraise");
            String strCtype = object.getString("ctype");

            tvNickname.setText(strNickname);
            tvTime.setText(strTime);
            tvTitle.setText(strTitle);
            tvSubTitle.setText(strSubTitle);
            tvCommentNum.setText(strCommentNum);
            tvPraiseNum.setText(strPraiseNum);

            activity.imageLoader.displayImage(strPortrait, rivPortrait, activity.portraitOptions);
            activity.imageLoader.displayImage(strImageUrl, ivImage, activity.imageOptions);

            rlShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            if (strCtype.equals("0")) {
                setImageViewMode();
                if (imageArray.length() <= 1) {
                    tvImageNum.setVisibility(View.GONE);
                }else {
                    tvImageNum.setVisibility(View.VISIBLE);
                }
                tvImageNum.setText("共" + String.valueOf(imageArray.length()) + "张");
            }else {
                String strVideoUrl = object.getString("url_video");
                setVidioViewMode(Integer.valueOf(imageObject.getString("width")),
                        Integer.valueOf(imageObject.getString("height")));
                initMediaVideo(strVideoUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initMediaVideo(final String url)
    {
        player = new Player(surfaceView, skbProgress);
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player == null)
                {
                    player = new Player(surfaceView, skbProgress);
                }
                player.playUrl(url);
                surfaceView.setVisibility(View.VISIBLE);
                ivImage.setVisibility(View.GONE);
                ivPlay.setVisibility(View.GONE);
            }
        });

        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.playPause();
            }
        });
    }

    private void setImageViewMode()
    {
        surfaceView.setVisibility(View.GONE);
        ivImage.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.GONE);
        tvImageNum.setVisibility(View.VISIBLE);
        vMediaPlayerController.setVisibility(View.GONE);
    }

    private void setVidioViewMode(int width, int height)
    {
        int rHeight = Common.DEVICE_SCREEN_WIDTH * height / width;
        changeSurfaceViewSize(surfaceView, Common.DEVICE_SCREEN_WIDTH, rHeight);
        surfaceView.setVisibility(View.GONE);
        ivPlay.setVisibility(View.VISIBLE);
        ivImage.setVisibility(View.VISIBLE);
        tvImageNum.setVisibility(View.GONE);
        vMediaPlayerController.setVisibility(View.VISIBLE);
    }

    public void changeSurfaceViewSize(SurfaceView view, int width, int height){

        RelativeLayout.LayoutParams params  =
                new RelativeLayout.LayoutParams(width, height);

        params.leftMargin = 0;
        params.topMargin  = 0;

        view.setLayoutParams(params);
//      view.getHolder().setFixedSize(width, height);
    }

}
