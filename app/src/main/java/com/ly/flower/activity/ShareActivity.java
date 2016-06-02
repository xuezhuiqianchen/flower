package com.ly.flower.activity;


import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.network.AscynHttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.HashMap;


/**
 * Created by admin on 2016/3/17.
 */
public class ShareActivity extends BaseActivity{
    private ShareActivity mInstance;
    private UMSocialService mController ;

    private ImageView ivIcon;
    private EditText etTitle;

    private String strTitle = "" ;
    private String strIconUrl = "";
    private String strType = "";
    private String strId = "";

    public UMImage img = null;

    public static final String CLUB        = "0";
    public static final String FOOTPRINT   = "1";
    public static final String TOPIC       = "2";


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void init() {
        setView(R.layout.activity_share);
        mInstance = this;
        initView();
        getData();
        fillView();
    }

    private void initView() {
        ivIcon = (ImageView) this.findViewById(R.id.iv_icon);
        etTitle = (EditText) this.findViewById(R.id.et_share_title);
    }

    private void getData() {
        Intent intent = getIntent();
        try {
            JSONObject object = new JSONObject(intent.getStringExtra("data"));
            strTitle = object.getString("title");
            strIconUrl = object.getString("img_url");
            strType = object.getString("type");
            strId = object.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillView() {
        img = new UMImage(this, strIconUrl);
        etTitle.setText(strTitle);
        imageLoader.displayImage(strIconUrl, ivIcon, imageOptions);
        mController = umSharePlatformUtil.getController();

        setTitle(R.string.str_share_to);
        setLeftActionVisiable();
        setRightText(R.string.str_sure);
    }

    @Override
    public void doClickLeftAction() {
        finish();
    }

    @Override
    public void doClickRightTextAction() {
        strTitle = etTitle.getText().toString();
        share(getShareUrl(), SHARE_MEDIA.WEIXIN_CIRCLE, null);
    }

    public void share(String url, SHARE_MEDIA type, View view)
    {
        umSharePlatformUtil.share(img, strTitle, strTitle, url, type, view);
    }

    public String getShareUrl() {
        String url = "";

        switch (strType)
        {
            case CLUB:
                url = AscynHttpUtil.URL_SHARE_CLUB + "cid=";
                break;

            case FOOTPRINT:
                url = AscynHttpUtil.URL_SHARE_TOPIC + "tid=";
                break;

            case TOPIC:
                url = AscynHttpUtil.URL_SHARE_HISTORY + "hid=";
                break;

            default:
                return url;
        }

        url += strId + "&title=" + strTitle;

        return url;
    }
}
