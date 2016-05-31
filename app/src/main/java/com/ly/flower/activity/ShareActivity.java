package com.ly.flower.activity;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.network.AscynHttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;


/**
 * Created by admin on 2016/3/17.
 */
public class ShareActivity extends BaseActivity implements View.OnClickListener {
    private ShareActivity mInstance;
    private UMSocialService mController ;

    private ImageView ivIcon;
    private TextView tvTitle;

    private JSONObject object;
    private String strTitle = "" ;
    private String strIconUrl = "";


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
        tvTitle = (TextView) this.findViewById(R.id.tv_share_title);
    }

    private void getData() {
        Intent intent = getIntent();
        try {
            object = new JSONObject(intent.getStringExtra("data"));
            if (object.has("title"))
            {
                strTitle = object.getString("title");
                strIconUrl = object.getJSONArray("img").getJSONObject(0).getString("url");
            }else {
                strTitle = object.getString("cname");
                strIconUrl = object.getString("url_bk");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillView() {
        tvTitle.setText(strTitle);
        imageLoader.displayImage(strIconUrl, ivIcon, imageOptions);
        mController = umSharePlatformUtil.getController();

        setTitle(R.string.str_share_to);
        setLeftActionVisiable();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
//        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE);
//        mController.openShare(mInstance, false);
    }

    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setShareContent() {
        String strTargetUrl = "";
        String strId;

        String strUrlDecodeTitle = URLEncoder.encode(strTitle);

        try {

            if (object.has("cid")) {
                strId = object.getString("cid");
                strTargetUrl = AscynHttpUtil.URL_SHARE_CLUB + "cid=" + strId + "&title=" +
                        strUrlDecodeTitle;
            }else if (object.has("tid")) {
                strId = object.getString("tid");
                strTargetUrl = AscynHttpUtil.URL_SHARE_TOPIC + "tid=" + strId + "&title=" +
                        strUrlDecodeTitle;
            }else if (object.has("hid")) {
                strId = object.getString("hid");
                strTargetUrl = AscynHttpUtil.URL_SHARE_HISTORY + "hid=" + strId + "&title=" +
                        strUrlDecodeTitle;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // 配置SSO

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mInstance,
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();
        mController.setShareContent("花朵分享");

        UMImage image = new UMImage(mInstance, R.drawable.temp1);


        //QQ空间
        QZoneShareContent qZoneShareContent = new QZoneShareContent();
        qZoneShareContent.setTitle(strTitle);
        qZoneShareContent.setTargetUrl(strTargetUrl);
        qZoneShareContent.setShareMedia(image);
        mController.setShareMedia(qZoneShareContent);

        //微信
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setTitle(strTitle);
        weixinContent.setTargetUrl(strTargetUrl);
        weixinContent.setShareMedia(image);
        mController.setShareMedia(weixinContent);

        //微信朋友圈
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setTitle(strTitle);
        circleMedia.setTargetUrl(strTargetUrl);
        circleMedia.setShareMedia(image);
        mController.setShareMedia(circleMedia);

        //QQ
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setTitle(strTitle);
        qqShareContent.setShareMedia(image);
        qqShareContent.setTargetUrl(strTargetUrl);
        mController.setShareMedia(qqShareContent);
    }
}
