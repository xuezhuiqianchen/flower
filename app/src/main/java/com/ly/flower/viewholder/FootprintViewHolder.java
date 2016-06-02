package com.ly.flower.viewholder;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ly.common.utils.TimeUtils;
import com.ly.flower.R;
import com.ly.flower.activity.ShareActivity;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.base.BaseFunction;
import com.ly.flower.base.DataStructure;
import com.ly.flower.network.AscynHttpUtil;
import com.ly.flower.network.SendInfo;
import com.ly.flower.share.MessageHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2016/3/24.
 */
public class FootprintViewHolder {
    private TextView tvTime;
    private TextView tvTitle;
    private TextView tvPlace;
    private TextView tvImageNum;
    private TextView tvCommentNum;
    private TextView tvPraiseNum;
    private ImageView ivImage;
    private ImageView ivPlay;
    private ImageView ivPraise;
    private RelativeLayout rlPraise;
    private RelativeLayout rlShare;

    private final String[] WEEKDAY = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五",
            "星期六", "星期日"};

    public FootprintViewHolder(View parentView)
    {
        tvTime = (TextView) parentView.findViewById(R.id.tv_time);
        tvTitle = (TextView) parentView.findViewById(R.id.tv_title);
        tvPlace = (TextView) parentView.findViewById(R.id.tv_place);
        tvImageNum = (TextView) parentView.findViewById(R.id.tv_image_num);
        ivImage = (ImageView) parentView.findViewById(R.id.iv_image);
        ivPlay = (ImageView) parentView.findViewById(R.id.iv_play);

        LinearLayout llEditBar = (LinearLayout) parentView.findViewById(R.id.edit_bar);
        tvCommentNum = (TextView) llEditBar.findViewById(R.id.tv_comment_num);
        tvPraiseNum = (TextView) llEditBar.findViewById(R.id.tv_praise_num);
        ivPraise = (ImageView) llEditBar.findViewById(R.id.iv_praise);
        rlPraise = (RelativeLayout) llEditBar.findViewById(R.id.rl_praise);
        rlShare = (RelativeLayout) llEditBar.findViewById(R.id.rl_share);
    }

    public void initData(final BaseActivity activity, final JSONObject object, final Handler handler)
    {
        try {
            String strTime = object.getString("chinesetime");
            String strTitle = object.getString("title");
            String strPlace = object.getString("place");
            String strCommentNum = object.getString("ccomment");
            String strPraiseNum = object.getString("cpraise");
            JSONArray imageArray = object.getJSONArray("img");
            String strCtype = object.getString("ctype");
            final String strIsPraise = object.getString("bpraise");
            String strImageUrl = imageArray.getJSONObject(0).getString("url");

            tvTime.setText(strTime);
            tvTitle.setText(strTitle);
            tvPlace.setText(strPlace);
            tvImageNum.setText("共" + String.valueOf(imageArray.length()) + "张");
            tvCommentNum.setText(strCommentNum);
            tvPraiseNum.setText(strPraiseNum);

            if (strIsPraise.equals("0")) {
                ivPraise.setImageResource(R.drawable.praise_icon);
            }else {
                ivPraise.setImageResource(R.drawable.praise_press_icon);
            }
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
            data.put("type", ShareActivity.FOOTPRINT);
            data.put("id", object.getString("hid"));
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
        String ctype = ShareActivity.FOOTPRINT;//足迹
        String sid = "";
        try {
            osubtype = object.getString("bpraise");
            sid = object.getString("hid");
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

    private void setImageViewMode()
    {
        tvImageNum.setVisibility(View.VISIBLE);
        ivImage.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.GONE);
    }

    private void setVidioViewMode()
    {
        ivPlay.setVisibility(View.GONE);
        ivImage.setVisibility(View.VISIBLE);
        tvImageNum.setVisibility(View.VISIBLE);
    }


    private String getTimeString(String time)
    {
        String strTime = TimeUtils.getDateFromTime(time);
        int weekDay = TimeUtils.getWeekDayByDate(strTime);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy年MM月dd日");
        strTime = sfd.format(date);
        return strTime + " " + WEEKDAY[weekDay];
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
}
