package com.ly.flower.viewholder;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ly.common.utils.Common;
import com.ly.common.utils.DimensionUtils;
import com.ly.common.utils.TimeUtils;
import com.ly.flower.R;
import com.ly.flower.activity.ShareActivity;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.base.BaseFunction;
import com.ly.flower.base.DataStructure;
import com.ly.flower.network.AscynHttpUtil;
import com.ly.flower.network.SendInfo;
import com.ly.flower.share.ImageInfo;
import com.ly.flower.share.MessageHandler;
import com.ly.flower.share.Player;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2016/3/18.
 */
public class DiscoveryViewHolder {
    private View container;
    private RoundedImageView rivPortrait ;
    private TextView tvNickname;
    private TextView tvTime;
    private TextView tvClub;

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


    public DiscoveryViewHolder(View parentView)
    {
        container = parentView;
        rivPortrait = (RoundedImageView) parentView.findViewById(R.id.riv_portrait);
        tvNickname = (TextView) parentView.findViewById(R.id.tv_nickname);
        tvTime = (TextView) parentView.findViewById(R.id.tv_time);
        tvClub = (TextView) parentView.findViewById(R.id.tv_club);
        tvTitle = (TextView) parentView.findViewById(R.id.tv_title);
        tvSubTitle = (TextView) parentView.findViewById(R.id.tv_sub_title);

        tvImageNum = (TextView) parentView.findViewById(R.id.tv_image_num);
        ivImage = (ImageView) parentView.findViewById(R.id.iv_image);

//        View view = parentView.findViewById(R.id.rl_media_player);
//        tvImageNum = (TextView) view.findViewById(R.id.tv_image_num);
//        ivImage = (ImageView) view.findViewById(R.id.iv_image);
//        ivPlay = (ImageView) view.findViewById(R.id.iv_play);
//        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
//        ivPlayPause = (ImageView) view.findViewById(R.id.iv_play_pause);
//        tvCurrentTime = (TextView) view.findViewById(R.id.tv_time_current);
//        tvTotalTime = (TextView) view.findViewById(R.id.tv_time_total);
//        skbProgress = (SeekBar) view.findViewById(R.id.seekbar);
//        vMediaPlayerController = view.findViewById(R.id.rl_controller);

        LinearLayout llEditBar = (LinearLayout) parentView.findViewById(R.id.edit_bar);
        tvCommentNum = (TextView) llEditBar.findViewById(R.id.tv_comment_num);
        tvPraiseNum = (TextView) llEditBar.findViewById(R.id.tv_praise_num);
        ivPraise = (ImageView) llEditBar.findViewById(R.id.iv_praise);
        rlPraise = (RelativeLayout) llEditBar.findViewById(R.id.rl_praise);
        rlShare = (RelativeLayout) llEditBar.findViewById(R.id.rl_share);
    }

    public void initData(final BaseActivity activity, final JSONObject object, final Handler handler)
    {
        rivPortrait.setCornerRadius((float) DimensionUtils.dip2px(activity, 100));
        try {
            String strPortrait = object.getString("uavatar");
            String strNickname = object.getString("uname");
            String strTime = TimeUtils.parseToIntervalTimeFormat(object.getString("time"));
            String strClub = "";
            JSONArray imageArray = object.getJSONArray("img");
            String strImageUrl = "";
            if (imageArray.length() > 0)
            {
                tvImageNum.setVisibility(View.VISIBLE);
                ivImage.setVisibility(View.VISIBLE);
                JSONObject imageObject = imageArray.getJSONObject(0);
                strImageUrl = imageObject.getString("url");
                String strWidth = imageObject.getString("width");
                String strLength = imageObject.getString("height");
                showImage(activity, strWidth, strLength, strImageUrl);
            } else {
                tvImageNum.setVisibility(View.GONE);
                ivImage.setVisibility(View.GONE);
            }


            if (object.has("cname"))
            {
                strClub = object.getString("cname");
            }
            String strTitle = object.getString("title");
            String strSubTitle = object.getString("sub_title");
            String strCommentNum = object.getString("ccomment");
            String strPraiseNum = object.getString("cpraise");
            String strCtype = object.getString("ctype");
            final String strIsPraise = object.getString("bpraise");

            tvNickname.setText(strNickname);
            tvTime.setText(strTime);
            tvClub.setText(strClub);
            tvTitle.setText(strTitle);
            tvSubTitle.setText(strSubTitle);
            tvCommentNum.setText(strCommentNum);
            tvPraiseNum.setText(strPraiseNum);

            if (strIsPraise.equals("0")) {
                ivPraise.setImageResource(R.drawable.praise_icon);
            }else {
                ivPraise.setImageResource(R.drawable.praise_press_icon);
            }
            activity.imageLoader.displayImage(strPortrait, rivPortrait, activity.portraitOptions);

            rlPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    praiseAction(activity, handler, object);
                }
            });

            rlShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareAction(activity, object);
                }
            });

            if (strCtype.equals("0")) {
                if (imageArray.length() <= 1) {
                    tvImageNum.setVisibility(View.GONE);
                }else {
                    tvImageNum.setVisibility(View.VISIBLE);
                }
                tvImageNum.setText("共" + String.valueOf(imageArray.length()) + "张");
            }
            /**
             * 暂时不做视频这块
             * time：2016.05.25
             else {
             String strVideoUrl = object.getString("url_video");
             setVidioViewMode(Integer.valueOf(imageObject.getString("width")),
             Integer.valueOf(imageObject.getString("height")));
             initMediaVideo(strVideoUrl);
             }
             */
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setImageViewMode()
    {
        surfaceView.setVisibility(View.GONE);
        ivImage.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.GONE);
        tvImageNum.setVisibility(View.VISIBLE);
        vMediaPlayerController.setVisibility(View.GONE);
    }

    private void showImage(BaseActivity activity, String strWidth, String strLength, String strImageUrl) {
        int imgWidthPx = 0;
        int imgHeightPx = 0;
        boolean bBig = false;

        try{
            imgWidthPx = Integer.valueOf(strWidth);
            imgHeightPx = Integer.valueOf(strLength);
            final ImageInfo imageInfo = new ImageInfo(activity, imgWidthPx, imgHeightPx);

            activity.imageLoader.displayImage(strImageUrl, ivImage, activity.imageOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if (imageInfo.isSpecial()) {
                        bitmap = imageInfo.clipBitmap(bitmap);
                    }

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
                    layoutParams.width = imageInfo.getDisplayWidth();
                    layoutParams.height = imageInfo.getDisplayHeight();
                    ivImage.setLayoutParams(layoutParams);
                    ivImage.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareAction(BaseActivity activity, JSONObject object) {
        if (!DataStructure.login)
            return;
        try {
            String url = "";
            JSONArray array = object.getJSONArray("img");
            if (array.length() > 0) {
                url = array.getJSONObject(0).getString("url");
            }
            JSONObject data = new JSONObject();
            data.put("type", object.getString("type"));
            data.put("id", object.getString("sid"));
            data.put("title", object.getString("title"));
            data.put("img_url", url);
            activity.gotoActivity(ShareActivity.class, data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void praiseAction(BaseActivity activity, Handler handler, JSONObject object){
        if (!DataStructure.login)
            return;
        String osubtype = "";
        String ctype = "";
        String sid = "";
        try {
            osubtype = object.getString("bpraise");
            ctype = object.getString("type");
            sid = object.getString("sid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (osubtype.equals("0")) {
            osubtype = "1";
        } else {
            osubtype = "0";
        }
        praiseNetOperation(activity, handler, osubtype, ctype, sid);
    }


    private void praiseNetOperation(final BaseActivity activity, final Handler handler,
                               final String osubtype, final String ctype, final String sid)
    {
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(activity, AscynHttpUtil.URL_USER_OPERATION);
        String strInfo = SendInfo.getUserOperationSendInfo(activity, "0", osubtype, ctype, sid);
        activity.showProgressBar(R.string.tip_submiting);
        AscynHttpUtil.post(activity, strUrl, strInfo, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] headers, byte[] responsebody) {
                activity.dismissProgressBar();
                if (BaseFunction.verifyResult(new String(responsebody), activity.clSnackContainer)) {
                    Bundle data = new Bundle();
                    data.putString("cid", sid);
                    data.putString("osubtype", osubtype);
                    data.putString("ctype", ctype);
                    MessageHandler.sendMessage(handler, MessageHandler.PRISE_OPERATION, data);
                }
            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] responsebody, Throwable err) {
                activity.dismissProgressBar();
            }
        });
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
