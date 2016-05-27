package com.ly.flower.activity.login;


import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.memory.GlobalStatic;
import com.ly.flower.network.AscynHttpUtil;
import com.ly.flower.network.SendInfo;
import com.ly.flower.base.BaseFunction;
import com.ly.flower.base.DataStructure;
import com.ly.flower.share.MessageHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2016/3/17.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private LoginActivity mInstance;
    private ImageView ivWeiXin;
    private ImageView ivQQ;

    private UMSocialService mController ;

    @Override
    public void init() {
        setView(R.layout.activity_login);
        mInstance = this;
        mController = umSharePlatformUtil.getController();

        initView();
        fillView();
    }

    private void initView()
    {
        ivWeiXin = (ImageView) this.findViewById(R.id.iv_weixin);
        ivQQ = (ImageView) this.findViewById(R.id.iv_qq);

        ivWeiXin.setOnClickListener(this);
        ivQQ.setOnClickListener(this);
    }

    private void fillView()
    {
        setTitle(R.string.login_page);
        setLeftActionVisiable();
    }

    @Override
    public void doClickLeftAction() {
        setResult(MessageHandler.RESULT_ERROR);
        finish();
    }

    private void doOauthVerify(final SHARE_MEDIA share_media)//SHARE_MEDIA.WEIXIN
    {
        mController.doOauthVerify(mInstance, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                showProgressBar(R.string.tip_authorizing);
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                showAlertMessage(R.string.tip_authorize_fail);
                dismissProgressBar();
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                showProgressBar(R.string.tip_authorize_success);
                //获取相关授权信息
                mController.getPlatformInfo(mInstance, platform, new UMDataListener() {
                    @Override
                    public void onStart() {
                        showProgressBar(mInstance.getResources().getString(R.string.tip_geting));
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == 200 && info != null) {
                            login(info, share_media);
                        } else {
                            showAlertMessage(R.string.tip_error);
                            dismissProgressBar();
                        }
                    }
                });
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                showAlertMessage(R.string.tip_authorize_cancel);
                dismissProgressBar();
            }
        });
    }

    private void login(Map<String, Object> info, SHARE_MEDIA share_media)
    {
        showProgressBar(R.string.tip_logining);

        String strNickname = "";
        String strGender = "1";
        String strAvatar = "";
        String strThirdUid = "";
        String strLtype = "";
        String strHometown = "";
        String strBkUrl = "";

        if (share_media.equals(SHARE_MEDIA.WEIXIN))
        {
            strLtype = "0";
            strNickname = info.get("nickname").toString();
            strAvatar = info.get("headimgurl").toString();
            strThirdUid = info.get("unionid").toString();//openid
            strHometown = info.get("country").toString() + info.get("province").toString()
                    + info.get("city").toString();
            if (info.get("sex").toString().equals("2"))
            {
                strGender = "0";
            }
        }else if (share_media.equals(SHARE_MEDIA.QQ)){
            strLtype = "0";
            strNickname = info.get("screen_name").toString();
            strAvatar = info.get("profile_image_url").toString();
            strThirdUid = info.get("openid").toString();//openid
            strHometown = info.get("province").toString() + info.get("city").toString();
            if (info.get("gender").toString().equals("女"))
            {
                strGender = "0";
            }
        }

        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_LOGIN_BY_EXTERN);
        String strInfo = SendInfo.getLoginByExternalSendInfo(strNickname, strGender, strAvatar,
                strThirdUid, strLtype, strHometown, strBkUrl);
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler());
    }

    private void cbLogin(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody)).getJSONObject("data");

            DataStructure.uid = object.getString("uid");
            DataStructure.passwd = object.getString("passwd");
            GlobalStatic.saveSharedString(mInstance, GlobalStatic.LOGIN, "1");
            GlobalStatic.saveSharedString(mInstance, GlobalStatic.UID, DataStructure.uid);
            GlobalStatic.saveSharedString(mInstance, GlobalStatic.PASSWORD, DataStructure.passwd);

            showInfoMessage(R.string.tip_login_success);
            setResult(MessageHandler.RESULT_OK);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ResponseHandlerInterface getResponseHandler()
    {
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] headers, byte[] responsebody) {
                dismissProgressBar();
                if (BaseFunction.verifyResult(new String(responsebody), clSnackContainer)) {
                    cbLogin(responsebody);
                }
            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] responsebody, Throwable err) {
                dismissProgressBar();
                showAlertMessage(err.getMessage());
            }
        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_weixin:
                doOauthVerify(SHARE_MEDIA.WEIXIN);
                break;

            case R.id.iv_qq:
                doOauthVerify(SHARE_MEDIA.QQ);
                break;
        }
    }
}
