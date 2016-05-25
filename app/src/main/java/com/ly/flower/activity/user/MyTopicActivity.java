package com.ly.flower.activity.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ly.flower.R;
import com.ly.flower.adapter.MyTopciListAdapter;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.network.AscynHttpUtil;
import com.ly.flower.network.SendInfo;
import com.ly.flower.base.BaseFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2016/5/23.
 */
public class MyTopicActivity extends BaseActivity {
    private MyTopicActivity mInstance;
    private LayoutInflater inflater = null;

    private ListView mListView;
    private MyTopciListAdapter mAdapter;

    @Override
    public void init() {
        setView(R.layout.activity_listview1);
        mInstance = this;
        inflater =(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
        fillView();
    }

    private void initView()
    {
        mListView = (ListView) this.findViewById(R.id.listView);
        mAdapter = new MyTopciListAdapter(inflater);
        mAdapter.setContext(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void fillView() {
        setTitle(R.string.str_my_topic);
        setLeftActionVisiable();
        getMyTopics();
    }

    @Override
    public void doClickLeftAction() {
        finish();
    }

    private void getMyTopics()
    {
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_GET_MY_TOPIC_LIST);
        String strInfo = SendInfo.getMyTopicsSendInfo(this, "0");
        showProgressBar(R.string.tip_geting);
        AscynHttpUtil.post(mInstance, strUrl, strInfo, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] headers, byte[] responsebody) {
                if (BaseFunction.verifyResult(new String(responsebody), clSnackContainer)) {
                    cbGetMyTopics(responsebody);
                }
                dismissProgressBar();
            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] responsebody, Throwable err) {
                dismissProgressBar();
                showAlertMessage(err.getMessage());
            }
        });
    }

    private void cbGetMyTopics(byte[] responsebody)
    {
        try {
            JSONArray array = new JSONObject(new String(responsebody)).getJSONArray("data");
            mAdapter.setData(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
