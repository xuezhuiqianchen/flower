package com.ly.flower.activity.user;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.network.AscynHttpUtil;
import com.ly.flower.network.SendInfo;
import com.ly.flower.base.BaseFunction;
import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2016/3/25.
 */
public class MySuggestActivity extends BaseActivity {
    private MySuggestActivity mInstance;
    private LayoutInflater inflater = null;
    private TextView tvTip;
    private EditText etContent;

    @Override
    public void init() {
        setView(R.layout.activity_my_suggest);
        mInstance = this;
        inflater =(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
        fillView();
    }

    private void initView()
    {
        etContent = (EditText) this.findViewById(R.id.et_content);
        tvTip = (TextView) this.findViewById(R.id.tv_tip);

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = s.length();
                if (len < 400) {
                    tvTip.setVisibility(View.VISIBLE);
                    tvTip.setText("还可以输入" + (400 - len) + "个字");
                } else {
                    tvTip.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void fillView() {
        setTitle(R.string.str_suggest_feedback);
        setLeftActionVisiable();
        setRightText(R.string.str_submit);
    }

    @Override
    public void doClickLeftAction() {
        finish();
    }

    @Override
    public void doClickRightTextAction() {
        uploadSuggestion();
    }

    private void uploadSuggestion()
    {
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_UPLOAD_SUGGESTION);
        String strInfo = SendInfo.getUploadSuggestionSendInfo(this, etContent.getText().toString());
        AscynHttpUtil.post(mInstance, strUrl, strInfo, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] headers, byte[] responsebody) {
                if (BaseFunction.verifyResult(new String(responsebody), clSnackContainer)) {
                    cbUploadSuggestion();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] responsebody, Throwable err) {
                showAlertMessage(err.getMessage());
            }
        });
    }

    private void cbUploadSuggestion()
    {
        showInfoMessage(R.string.str_submit_ok);
        finish();
    }
}
