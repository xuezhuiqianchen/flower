package com.ly.common.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by admin on 2016/5/20.
 */
public class UMSharePlatformUtil {
    final UMSocialService mController = UMServiceFactory.getUMSocialService(
            "com.umeng.share", RequestType.SOCIAL);
    private Activity mContext;
    private boolean has_wx_share, has_wx_circle_share, has_qq_share,
            has_qq_space_share;
    private String wx_appid, wx_appkey, qq_appid, qq_appkey;

    public UMSharePlatformUtil(Activity context, String umAppKey) {
        SocializeConstants.APPKEY = umAppKey;
        this.mContext = context;
        configPlatforms();
    }

    public UMSharePlatformUtil(Activity context, String umAppKey,
                               String wx_appid, String wx_appkey, String qq_appid, String qq_appkey) {
        this.mContext = context;
        this.has_wx_share = true;
        this.has_wx_circle_share = true;
        this.has_qq_share = true;
        this.has_qq_space_share = false;
        this.wx_appid = wx_appid;
        this.wx_appkey = wx_appkey;
        this.qq_appid = qq_appid;
        this.qq_appkey = qq_appkey;
        SocializeConstants.APPKEY = umAppKey;
        configPlatforms();
    }

    public UMSocialService getController() {
        return mController;
    }


    private void configPlatforms() {
        if (has_qq_share || has_qq_space_share) {
            addQQPlatform();
        }
        if (has_wx_share || has_wx_circle_share) {
            addWXPlatform();
        }
    }

    public void share(UMImage umImage, String title, String content, String url, SHARE_MEDIA var2, final View layout)
    {
        mController.setShareContent(content);

        setShareContent(umImage, title, content, url);
        mController.postShare(mContext,var2,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
//						Toast.makeText(mContext, "开始分享.", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
                        if (eCode == 200) {
                            if (layout != null)
                                layout.setVisibility(View.GONE);
                            Toast.makeText(mContext, "分享成功.", Toast.LENGTH_SHORT).show();
                        } else {
                            String eMsg = "";
                            if (eCode == -101){
                                eMsg = "没有授权";
                            }
                            Toast.makeText(mContext, "分享失败[" + eCode + "] " +
                                    eMsg,Toast.LENGTH_SHORT).show();
                        }
                        if (layout != null)
                            layout.setVisibility(View.GONE);
                    }
                });
    }

    public void share(String contentUrl, String url) {
        mController.setShareContent(contentUrl);
        TencentWBSsoHandler handler = new TencentWBSsoHandler();
        handler.setShareAfterAuthorize(true);
        mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN);
        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN);
        if (has_qq_share) {

        } else {
            mController.getConfig().removePlatform(SHARE_MEDIA.QQ);

        }
        if (has_qq_space_share) {

        } else {
            mController.getConfig().removePlatform(SHARE_MEDIA.QZONE);
        }

        if (has_wx_share) {

        } else {
            mController.getConfig().removePlatform(SHARE_MEDIA.WEIXIN);
        }
        if (has_wx_circle_share) {

        } else {
            mController.getConfig().removePlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
        }

//		setShareContent(contentUrl, url);
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.SINA,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN,
                SHARE_MEDIA.EMAIL, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.SMS);
        mController.openShare(mContext, false);

    }

    public void share(String contentUrl, String url, Bitmap bitmap) {
        mController.setShareContent(contentUrl);
        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
        mController.getConfig().removePlatform(SHARE_MEDIA.SINA);
        mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN);
        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN);
        if (has_qq_share) {

        } else {
            mController.getConfig().removePlatform(SHARE_MEDIA.QQ);

        }
        if (has_qq_space_share) {

        } else {
            mController.getConfig().removePlatform(SHARE_MEDIA.QZONE);
        }

        if (has_wx_share) {

        } else {
            mController.getConfig().removePlatform(SHARE_MEDIA.WEIXIN);
        }
        if (has_wx_circle_share) {

        } else {
            mController.getConfig().removePlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
        }

        setShareContent(contentUrl, url, bitmap);
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.SINA,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN,
                SHARE_MEDIA.EMAIL, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.SMS);
        mController.openShare(mContext, false);

    }

    private void setShareContent(String contentUrl, String url, Bitmap bitmap) {
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(contentUrl);
        weixinContent.setTargetUrl(url);
        weixinContent.setShareImage(new UMImage(mContext, bitmap));
        mController.setShareMedia(weixinContent);

        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(contentUrl);
        circleMedia.setTargetUrl(url);
        circleMedia.setTitle(contentUrl);
        circleMedia.setShareImage(new UMImage(mContext, bitmap));
        mController.setShareMedia(circleMedia);

        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(contentUrl);
        qzone.setTargetUrl(url);
        qzone.setShareImage(new UMImage(mContext, bitmap));
        mController.setShareMedia(qzone);

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(contentUrl);
        qqShareContent.setShareImage(new UMImage(mContext, bitmap));
        qqShareContent.setTargetUrl(url);
        mController.setShareMedia(qqShareContent);

        MailShareContent mail = new MailShareContent(contentUrl);
        mController.setShareMedia(mail);

        SmsShareContent sms = new SmsShareContent();
        sms.setShareContent(contentUrl);
        // sms.setShareImage(urlImage);
        mController.setShareMedia(sms);

        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(contentUrl);
        sinaContent.setShareImage(new UMImage(mContext, bitmap));
        mController.setShareMedia(sinaContent);

        TencentWbShareContent tencent = new TencentWbShareContent();
        tencent.setTargetUrl(url);
        tencent.setShareContent(contentUrl);
        tencent.setShareImage(new UMImage(mContext, bitmap));
        mController.setShareMedia(tencent);

    }

    private void setShareContent(UMImage umImage, String title, String contentUrl, String url) {
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setTitle(title);
        weixinContent.setShareContent(contentUrl);
        weixinContent.setTargetUrl(url);
        weixinContent.setShareImage(umImage);
        mController.setShareMedia(weixinContent);

        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(contentUrl);
        circleMedia.setTargetUrl(url);
        circleMedia.setTitle(contentUrl + "—" + title);
        circleMedia.setShareImage(umImage);
        mController.setShareMedia(circleMedia);

        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(contentUrl);
        qzone.setTargetUrl(url);
        qzone.setTitle(contentUrl + "—" + title);
        qzone.setShareImage(umImage);
        mController.setShareMedia(qzone);

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(contentUrl);
        qqShareContent.setTargetUrl(url);
        qqShareContent.setTitle(contentUrl + "—" + title);
        qqShareContent.setShareImage(umImage);
        mController.setShareMedia(qqShareContent);
//
//		MailShareContent mail = new MailShareContent(contentUrl);
//		mController.setShareMedia(mail);
//
//		SmsShareContent sms = new SmsShareContent();
//		sms.setShareContent(contentUrl);
//		// sms.setShareImage(urlImage);
//		mController.setShareMedia(sms);
//
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(contentUrl);
        sinaContent.setTargetUrl(url);
        sinaContent.setTitle(contentUrl + "—" + title);
        sinaContent.setShareImage(umImage);
        mController.setShareMedia(sinaContent);
//
//		TencentWbShareContent tencent = new TencentWbShareContent();
//		tencent.setTargetUrl(url);
//		tencent.setShareContent(contentUrl);
//		mController.setShareMedia(tencent);

    }


    private void addWXPlatform() {
        String WX_APPID = wx_appid;
        String WX_KEY = wx_appkey;

        UMWXHandler wxHandler = new UMWXHandler(mContext, WX_APPID, WX_KEY);
        wxHandler.addToSocialSDK();

        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, WX_APPID,
                WX_KEY);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

    }

    private void addQQPlatform() {
        String APPID = qq_appid;
        String APPKEY = qq_appkey;
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mContext, APPID,
                APPKEY);
        qqSsoHandler.addToSocialSDK();

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mContext, APPID,
                APPKEY);
        qZoneSsoHandler.addToSocialSDK();
    }
}
