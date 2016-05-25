package com.ly.flower.activity.club;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.ly.flower.R;
import com.ly.flower.activity.ShareActivity;
import com.ly.flower.adapter.DetailListAdapter;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.network.AscynHttpUtil;
import com.ly.flower.network.SendInfo;
import com.ly.flower.base.BaseFunction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2016/3/24.
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener{
    private DetailActivity mInstance;
    private LayoutInflater inflater = null;
    private InputMethodManager imm;

    private ImageView ivBack;
    private ImageView ivPraise;
    private ImageView ivShare;
    private TextView tvTitle;
    private EditText etReplyContent;
    private TextView tvReply;
    private ListView mListview;
    private DetailListAdapter detailListAdapter;

    private JSONObject object;

    private final int TYPE_GET_COMMENT      = 0;
    private final int TYPE_PRAISE           = 1;
    private final int TYPE_REPLY            = 2;


    public static final int TYPE_FOOTPRINT  = 0;
    public static final int TYPE_TOPIC      = 1;


    @Override
    public void init() {
        setView(R.layout.activity_detail);
        mInstance = this;
        inflater =(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        getData();
        fillView();

    }

    private void initView() {
        initTitleBar();
        initReplyBar();
        initMainView();
    }

    private void getData() {
        Intent intent = getIntent();
        try {
            object = new JSONObject(intent.getStringExtra("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getCommentListData(true);
    }

    private void fillView() {
        try {
            tvTitle.setText(object.getString("title"));
            if (object.getString("bpraise").equals("1"))
                ivPraise.setImageResource(R.drawable.like_select_icon);

            JSONArray array = new JSONArray();
            array.put(object);
            if (object.has("hid"))
            {
                detailListAdapter.setType(DetailListAdapter.TYPE_FOOTPRINT_DETAIL);
            }  else {
                detailListAdapter.setType(DetailListAdapter.TYPE_TOPIC_DETAIL);
            }
            detailListAdapter.setData(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_praise:
                praiseAction();
                break;

            case R.id.iv_share:
                gotoActivity(ShareActivity.class, object.toString());
                break;

            case R.id.tv_reply:
                replyAction();
                break;
        }
    }

    private void initTitleBar() {
        hideTitleLayout();
        ivBack = (ImageView) this.findViewById(R.id.iv_back);
        ivPraise = (ImageView) this.findViewById(R.id.iv_praise);
        ivShare = (ImageView) this.findViewById(R.id.iv_share);
        tvTitle = (TextView) this.findViewById(R.id.tv_title);

        ivPraise.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
    }

    private void initReplyBar() {
        etReplyContent = (EditText) this.findViewById(R.id.et_reply_content);
        tvReply = (TextView) this.findViewById(R.id.tv_reply);

        etReplyContent.clearFocus();
        tvReply.setOnClickListener(this);
    }

    private void initMainView() {
        mListview = (ListView) this.findViewById(R.id.listView);

        detailListAdapter = new DetailListAdapter(inflater);
        detailListAdapter.setContext(mInstance);
        mListview.setAdapter(detailListAdapter);
    }

    private void getCommentListData(boolean bProgress)
    {
        String strUrl = "";
        String strInfo = "";
        try {
            if (object.has("hid"))
            {
                strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance,
                        AscynHttpUtil.URL_CLUB_GET_HISTORY_COMMENT);
                strInfo = SendInfo.getHistoryCommentOfClubSendInfo(mInstance,
                        object.getString("hid"));
            }else if (object.has("tid")){
                strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance,
                        AscynHttpUtil.URL_CLUB_GET_TOPIC_COMMENT);
                strInfo = SendInfo.getTopicCommentOfClubSendInfo(mInstance,
                        object.getString("tid"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(TYPE_GET_COMMENT));
        if (bProgress)
            showProgressBar(R.string.tip_geting);
    }

    private void praiseAction()
    {
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance,
                AscynHttpUtil.URL_USER_OPERATION);
        String cid = "";
        String ctype = "";
        String osubtype = "0";
        try {
            if (object.has("hid"))
            {
                cid = object.getString("hid");
                ctype = "1";
            }else if (object.has("tid")){
                cid = object.getString("tid");
                ctype = "2";
            }

            if (object.getString("bpraise").equals("0"))
            {
                osubtype = "1";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String strInfo = SendInfo.getUserOperationSendInfo(mInstance, "0", osubtype, ctype, cid);
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(TYPE_PRAISE));
        showProgressBar(R.string.tip_submiting);
    }

    private void replyAction()
    {
        String strUrl = "";
        String strInfo = "";
        String strContent = etReplyContent.getText().toString();
        if (strContent.trim().equals(""))
        {
            showAlertMessage(R.string.tip_msg_empty);
            return;
        }
        try {
            if (object.has("hid"))
            {
                strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance,
                        AscynHttpUtil.URL_CLUB_ADD_HISTORY_COMMENT);
                strInfo = SendInfo.getAddHistoryCommentOfClubSendInfo(mInstance,
                        object.getString("hid"), strContent);
            }else if (object.has("tid")){
                strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance,
                        AscynHttpUtil.URL_CLUB_ADD_TOPIC_COMMENT);
                strInfo = SendInfo.getAddTopicCommentOfClubSendInfo(mInstance,
                        object.getString("tid"), strContent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(TYPE_REPLY));
        showProgressBar(R.string.tip_submiting);
    }

    private void cbGetCommentListData(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            JSONArray array = object.getJSONArray("data");
            JSONArray dataArray = new JSONArray();
            dataArray.put(this.object);
            for (int i = 0; i < array.length(); i++) {
                dataArray.put(array.getJSONObject(i));
            }
            detailListAdapter.setData(dataArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cbPraiseAction()
    {
        try {
            if (object.getString("bpraise").equals("0"))
            {
                ivPraise.setImageResource(R.drawable.like_select_icon);
                object.put("bpraise", "1");
                detailListAdapter.refreshPraise("1");
            }else {
                ivPraise.setImageResource(R.drawable.like_icon);
                object.put("bpraise", "0");
                detailListAdapter.refreshPraise("0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cbReplyAction(byte[] responsebody)
    {
//        try {
//            String cid = new JSONObject(new String(responsebody)).getJSONObject("data").getString("cid");
//            JSONObject userObject = new JSONObject(GlobalStatic.getSharedString(mInstance, DataStructure.uid));
//            JSONObject commentObject = new JSONObject();
//            commentObject.put("cid", cid);
//            commentObject.put("uid", DataStructure.uid);
//            commentObject.put("avatar", userObject.getString("avatar"));
//            commentObject.put("nickname", userObject.getString("nickname"));
//            commentObject.put("time", BaseFunction.getCurrentTime());
//            commentObject.put("content", etReplyContent.getText().toString());
//            commentObject.put("level", "" );
//            detailListAdapter.addComment(commentObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        etReplyContent.setText("");
        getCommentListData(true);
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
                        case TYPE_GET_COMMENT:
                            cbGetCommentListData(responsebody);
                            break;

                        case TYPE_PRAISE:
                            cbPraiseAction();
                            break;

                        case TYPE_REPLY:
                            cbReplyAction(responsebody);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] responsebody, Throwable err) {
                dismissProgressBar();
                showAlertMessage(err.getMessage());
            }
        };
    }
}
