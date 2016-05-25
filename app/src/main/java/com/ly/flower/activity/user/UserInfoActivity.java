package com.ly.flower.activity.user;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.network.AscynHttpUtil;
import com.ly.flower.network.SendInfo;
import com.ly.flower.base.BaseFunction;
import com.ly.flower.viewholder.MyClubViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2016/3/25.
 */
public class UserInfoActivity extends BaseActivity {
    private LayoutInflater inflater = null;
    private ImageView ivBkg, ivBack, ivEdit;
    private RoundedImageView rivPortrait;
    private TextView tvNickName;
    private TextView tvSignature;
    private LinearLayout llClubs;

    private JSONObject userObject;

    @ Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);
        if(resultCode == RESULT_OK){
            try {
                userObject = new JSONObject(data.getStringExtra("data"));
                refreshBaseInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void init() {
        setView(R.layout.activity_user_info);
        inflater =(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
        fillView();
    }

    private void initView()
    {
        ivBack = (ImageView) this.findViewById(R.id.iv_back);
        ivEdit = (ImageView) this.findViewById(R.id.iv_edit);
        ivBkg = (ImageView) this.findViewById(R.id.iv_bg);
        rivPortrait = (RoundedImageView) this.findViewById(R.id.riv_portrait);
        tvNickName = (TextView) this.findViewById(R.id.tv_nickname);
        tvSignature = (TextView) this.findViewById(R.id.tv_signature);
        llClubs = (LinearLayout) this.findViewById(R.id.ll_images_view);
    }

    private void fillView() {
        hideTitleLayout();
        getUserInfo();
        ivBack.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_edit:
                gotoActivityForResult(EditUserInfoActivity.class, userObject.toString(), 0);
                break;
            default:
                break;
        }
    }

    private void refreshClubsLayout(JSONArray array)
    {
        final int length = array.length();
        llClubs.removeAllViews();
        try {
            for (int i = 0; i < length; i++) {
                final RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.item_my_club, llClubs, false);
                final MyClubViewHolder viewHolder = new MyClubViewHolder(view);
                viewHolder.initData(this, array.getJSONObject(i));
                llClubs.addView(view);
            }//end for
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserInfo()
    {
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(this, AscynHttpUtil.URL_EDIT_USER_INFO);
        String strInfo = SendInfo.getUserInfoSendInfo(this);
        showProgressBar(R.string.tip_geting);
        AscynHttpUtil.post(this, strUrl, strInfo, getResponseHandler(1));
    }

    private void cbGetUserInfo(byte[] responsebody)
    {
        try {
            userObject = new JSONObject(new String(responsebody)).getJSONObject("data");
            refreshBaseInfo();
            JSONArray clubs = userObject.getJSONArray("clubs");
            refreshClubsLayout(clubs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ResponseHandlerInterface getResponseHandler(final int type)
    {
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] headers, byte[] responsebody) {
                dismissProgressBar();
                if (BaseFunction.verifyResult(new String(responsebody), clSnackContainer)) {
                    switch (type)
                    {
                        case 1:
                            cbGetUserInfo(responsebody);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] responsebody, Throwable err) {
                dismissProgressBar();
            }
        };
    }

    private void refreshBaseInfo()
    {
        try {
            String strBkUrl = userObject.getString("bk_url");
            String strPortrait = userObject.getString("avatar");
            String strNickName = userObject.getString("nickname");
            String strSignature = userObject.getString("signature");

            tvNickName.setText(strNickName);
            tvSignature.setText(strSignature);
            ImageLoader.getInstance().displayImage(strBkUrl, ivBkg);
            ImageLoader.getInstance().displayImage(strPortrait, rivPortrait);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
