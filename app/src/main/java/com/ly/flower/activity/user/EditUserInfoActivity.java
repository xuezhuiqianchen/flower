package com.ly.flower.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.network.AscynHttpUtil;
import com.ly.flower.network.SendInfo;
import com.ly.flower.base.BaseFunction;
import com.ly.flower.share.MessageHandler;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by admin on 2016/5/23.
 */
public class EditUserInfoActivity extends BaseActivity {
    private ImageView ivBack, ivBkg;
    private TextView tvSave;
    private RoundedImageView rivPortrait;
    private EditText etNickName, etSignature;

    private JSONObject userObject;
    private String strPortrait = "";
    private String strBkgUrl = "";

    private final int EDIT_USER_INFO    = 0;
    private final int CB_UPLOAD_IMAGE   = 1;
    private final int CB_EDIT_USER_INFO = 2;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case EDIT_USER_INFO:
                    String data = msg.getData().getString("data");
                    if (data.equals(""))
                        editUserInfo("", "");
                    try {
                        JSONArray array = new JSONArray(data);

                        String portrait = "";
                        String bkg = "";
                        int i = array.length() - 1;
                        if (!strBkgUrl.equals("")) {
                            bkg = array.getJSONObject(i).getString("url");
                            i--;
                        }

                        if (!strPortrait.equals("")) {
                            portrait = array.getJSONObject(i).getString("url");
                        }

                        editUserInfo(bkg, portrait);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }

        }

    };

    @ Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            String path = data.getStringArrayListExtra(
                    MultiImageSelectorActivity.EXTRA_RESULT).get(0);
            Bitmap bitmap = BitmapFactory.decodeFile(path);

            if (reqCode == MessageHandler.REQUEST_CODE_SELECT_BKG) {
                strBkgUrl = path;
                ivBkg.setImageBitmap(bitmap);
            }else if (reqCode == MessageHandler.REQUEST_CODE_SELECT_PORTRAIT) {
                strPortrait = path;
                rivPortrait.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void init() {
        setView(R.layout.activity_edit_user_info);
        initView();
        fillView();
    }

    private void initView()
    {
        ivBack = (ImageView) this.findViewById(R.id.iv_back);
        ivBkg = (ImageView) this.findViewById(R.id.iv_bg);
        rivPortrait = (RoundedImageView) this.findViewById(R.id.riv_portrait);
        tvSave = (TextView) this.findViewById(R.id.tv_save);
        etNickName = (EditText) this.findViewById(R.id.et_nickname);
        etSignature = (EditText) this.findViewById(R.id.et_signature);
    }

    private void fillView() {
        hideTitleLayout();
        try {
            userObject = new JSONObject(getIntent().getStringExtra("data"));
            String strBkUrl = userObject.getString("bk_url");
            String strPortrait = userObject.getString("avatar");
            String strNickName = userObject.getString("nickname");
            String strSignature = userObject.getString("signature");

            etNickName.setText(strNickName);
            etSignature.setText(strSignature);
            if(!strBkUrl.equals("")) {
                imageLoader.displayImage(strBkUrl, ivBkg, imageOptions);
            }

            imageLoader.displayImage(strPortrait, rivPortrait, portraitOptions);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tvSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivBkg.setOnClickListener(this);
        rivPortrait.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:


                changeUserInfo();
                break;
            case R.id.iv_bg:
                jumpSelectImageActivity(this, MessageHandler.REQUEST_CODE_SELECT_BKG, strBkgUrl);
                break;
            case R.id.riv_portrait:
                jumpSelectImageActivity(this, MessageHandler.REQUEST_CODE_SELECT_PORTRAIT, strPortrait);
                break;
            default:
                break;
        }
    }

    private void changeUserInfo()
    {
        showProgressBar(R.string.tip_uploading);
        ArrayList<String> pathList = getImageList();
        if (pathList.size() > 0) {
            AscynHttpUtil.uploadMutilImage(this, pathList, getResponseHandler(CB_UPLOAD_IMAGE));
        }
        else {
            MessageHandler.sendMessage(mHandler, EDIT_USER_INFO, "");
        }
    }

    private ArrayList<String> getImageList()
    {
        //将改变后的背景/头像上传
        ArrayList<String> pathList = new ArrayList<>();
        if (!strBkgUrl.equals(""))
        {
            pathList.add(strBkgUrl);
        }

        if (!strPortrait.equals("")) {
            pathList.add(strPortrait);
        }
        return pathList;
    }



    private void editUserInfo(String strBkgUrl, String strPortrait)
    {
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(this, AscynHttpUtil.URL_EDIT_USER_INFO);
        String strInfo = SendInfo.getEditUserInfoSendInfo(this, etNickName.getText().toString(),
                strPortrait, etSignature.getText().toString(), strBkgUrl);
        showProgressBar(R.string.tip_uploading);
        AscynHttpUtil.post(this, strUrl, strInfo, getResponseHandler(CB_EDIT_USER_INFO));
    }

    private void cbGetUserInfo(byte[] responsebody)
    {
        try {
            userObject = new JSONObject(new String(responsebody)).getJSONObject("data");
            Intent data = new Intent();
            data.putExtra("data", userObject.toString());
            setResult(RESULT_OK, data);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cbUploadImage(byte[] responsebody)
    {
        try {
            JSONArray array = new JSONObject(new String(responsebody)).getJSONArray("data");
            MessageHandler.sendMessage(mHandler, EDIT_USER_INFO, array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ResponseHandlerInterface getResponseHandler(final int type)
    {
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] headers, byte[] responsebody) {
                if (BaseFunction.verifyResult(new String(responsebody), clSnackContainer)) {
                    switch (type)
                    {
                        case CB_UPLOAD_IMAGE:
                            cbUploadImage(responsebody);
                            break;

                        case CB_EDIT_USER_INFO:
                            dismissProgressBar();
                            cbGetUserInfo(responsebody);
                            break;
                    }
                }else {
                    dismissProgressBar();
                }

            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] responsebody, Throwable err) {
                dismissProgressBar();
            }
        };
    }

    private void jumpSelectImageActivity(Activity instance, int request, String path)
    {
        ArrayList<String> defaultList = new ArrayList<>();
        defaultList.add(path);
        Intent intent = new Intent(instance, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultList);
        instance.startActivityForResult(intent, request);
    }
}
