package com.ly.flower.viewholder;

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
import com.ly.flower.share.MessageHandler;
import com.ly.flower.share.Player;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2016/6/2.
 */
public class TopicViewHolder {

    private View container;
    private RoundedImageView rivPortrait ;
    private TextView tvNickname;
    private TextView tvTime;
    private TextView tvClub;

    private TextView tvTitle;
    private TextView tvSubTitle;
    private TextView tvImageNum;
    private ImageView ivImage;

    private TextView tvCommentNum;
    private TextView tvPraiseNum;
    private ImageView ivPraise;
    private RelativeLayout rlPraise;
    private RelativeLayout rlShare;


    public TopicViewHolder(View parentView)
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
                int imgWidth = 0;
                int imgHeight = 0;

                try{
                    imgWidth = Integer.valueOf(strWidth);
                    imgHeight = Integer.valueOf(strLength);

                    int relWidth = Common.DEVICE_SCREEN_WIDTH ;
                    int relHeight = imgHeight * relWidth / imgWidth;

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
                    layoutParams.width = relWidth;
                    layoutParams.height = relHeight;
                    ivImage.setLayoutParams(layoutParams);
                    ivImage.setImageResource(R.drawable.default_image);
                    ivImage.setScaleType(ImageView.ScaleType.FIT_XY);
                }catch (Exception e) {
                    e.printStackTrace();
                }
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
            activity.imageLoader.displayImage(strImageUrl, ivImage, activity.imageOptions);

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
        } catch (JSONException e) {
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
            data.put("type", ShareActivity.TOPIC);
            data.put("id", object.getString("tid"));
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
        String ctype = ShareActivity.TOPIC;
        String sid = "";
        try {
            osubtype = object.getString("bpraise");
            sid = object.getString("tid");
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

}
