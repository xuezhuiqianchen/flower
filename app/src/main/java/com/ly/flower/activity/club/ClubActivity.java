package com.ly.flower.activity.club;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.ly.flower.R;
import com.ly.flower.activity.ShareActivity;
import com.ly.flower.adapter.ClubListAdapter;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.component.XListView;
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
public class ClubActivity extends BaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, XListView.IXListViewListener {
    private ClubActivity mInstance;
    private LayoutInflater inflater = null;

    private ImageView ivBack;
    private ImageView ivEdit;
    private RadioGroup rgMenu;
    private RadioButton rbFootprint;
    private RadioButton rbTopic;
    private RadioButton rbMember;
    private FloatingActionButton fabEditTopic;
    private XListView mListView;
    private ClubListAdapter clubListAdapter;

    private JSONArray footprintArray = new JSONArray();
    private JSONArray topicArray = new JSONArray();
    private JSONArray memberArray = new JSONArray();
    private String cid = "";

    private static final int TYPE_FOOTPRINT_MORE    = 101;
    private static final int TYPE_TOPIC_MORE        = 102;



    @Override
    public void onResume()
    {
        super.onResume();

        if (rgMenu.getCheckedRadioButtonId() == R.id.radio_button3)
        {
            mListView.setPullLoadEnable(false);
        }else {
            mListView.setPullLoadEnable(true);
        }
        mListView.startRefresh();
    }

    @Override
    public void init() {
        setLayoutRootId(R.layout.activity_base_floatingbutton);
        setView(R.layout.activity_club);
        mInstance = this;
        inflater =(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
        getData();
    }

    private void initView() {
        initTitleBar();
        initMainView();
    }

    private void getData() {
        Intent intent = getIntent();
        try {
            JSONObject object = new JSONObject(intent.getStringExtra("data"));
            cid = object.getString("cid");
            footprintArray.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mListView.startRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_edit:
                try {
                    gotoActivity(ShareActivity.class, footprintArray.getJSONObject(0).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.fab_edit:
//                gotoActivity(PostTopicActivity.class, cid);
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId)
        {
            case R.id.radio_button1:
                mListView.setPullLoadEnable(true);
                ivEdit.setVisibility(View.VISIBLE);
                fabEditTopic.setVisibility(View.GONE);
                clubListAdapter.setData(footprintArray, ClubListAdapter.TYPE_FOOTPRINT);
                getFootprintListData(false);
                break;

            case R.id.radio_button2:
                mListView.setPullLoadEnable(true);
                ivEdit.setVisibility(View.INVISIBLE);
                fabEditTopic.setVisibility(View.VISIBLE);
                clubListAdapter.setData(topicArray, ClubListAdapter.TYPE_TOPIC);
                getTopicListData(false);
                break;

            case R.id.radio_button3:
                mListView.setPullLoadEnable(false);
                ivEdit.setVisibility(View.INVISIBLE);
                fabEditTopic.setVisibility(View.GONE);
                clubListAdapter.setData(memberArray, ClubListAdapter.TYPE_MEMBER);
                getMemberListData(false);
                break;
        }

    }

    public void initTitleBar() {
        hideTitleLayout();
        RelativeLayout rlTitleBar = (RelativeLayout) this.findViewById(R.id.titlebar);
        ivBack = (ImageView) rlTitleBar.findViewById(R.id.iv_back);
        ivEdit = (ImageView) rlTitleBar.findViewById(R.id.iv_edit);
        rgMenu = (RadioGroup) rlTitleBar.findViewById(R.id.rg_menu);
        rbFootprint = (RadioButton) rlTitleBar.findViewById(R.id.radio_button1);
        rbTopic = (RadioButton) rlTitleBar.findViewById(R.id.radio_button2);
        rbMember = (RadioButton) rlTitleBar.findViewById(R.id.radio_button3);

        rbFootprint.setText(getResources().getString(R.string.str_footprint));
        rbTopic.setText(getResources().getString(R.string.str_topic));
        rbMember.setText(getResources().getString(R.string.str_member));
        rgMenu.setOnCheckedChangeListener(this);
        ivBack.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
    }

    public void initMainView() {
        fabEditTopic = (FloatingActionButton) this.findViewById(R.id.fab_edit);
        mListView = (XListView) this.findViewById(R.id.listView);

        fabEditTopic.setVisibility(View.GONE);
        clubListAdapter = new ClubListAdapter(inflater);
        clubListAdapter.setContext(mInstance);
        mListView.setAdapter(clubListAdapter);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject object = (JSONObject) parent.getItemAtPosition(position);
                if (object == null)
                    return;

                if (rgMenu.getCheckedRadioButtonId() == R.id.radio_button1 && position == 0)
                    return;

                if (rgMenu.getCheckedRadioButtonId() == R.id.radio_button3) {
                    return;
                }

                gotoActivity(DetailActivity.class, object.toString());
            }
        });

        fabEditTopic.setOnClickListener(this);
    }

    private void getFootprintListData(boolean refresh)
    {
        if (footprintArray.length() > 0 && !refresh )
        {
            return;
        }
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_CLUB_GET_HISTORY);
        String strInfo = SendInfo.getHistoryOfClubSendInfo(mInstance, cid, "0", "20");
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(ClubListAdapter.TYPE_FOOTPRINT));
    }

    private void getTopicListData(boolean refresh)
    {
        if (topicArray.length() > 0 && !refresh )
        {
            return;
        }
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_CLUB_GET_TOPIC_LIST);
        String strInfo = SendInfo.getTopicListClubSendInfo(mInstance, cid, "0", "0", "20");
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(ClubListAdapter.TYPE_TOPIC));
    }

    private void getMemberListData(boolean refresh)
    {
        if (memberArray.length() > 0 && !refresh )
        {
            return;
        }
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_CLUB_GET_MEMBER);
        String strInfo = SendInfo.getMemberOfClubSendInfo(mInstance, cid);
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(ClubListAdapter.TYPE_MEMBER));
    }

    private void getMoreTopicListData()
    {
        String tid = "";
        try {
            tid = topicArray.getJSONObject(topicArray.length() - 1).getString("tid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (tid.equals("")) {
            mListView.stopLoadMore();
            return;
        }

        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_CLUB_GET_TOPIC_LIST);
        String strInfo = SendInfo.getTopicListClubSendInfo(mInstance, cid, "0", tid, "10");
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(TYPE_TOPIC_MORE));
    }

    private void getMoreFootprintListData()
    {
        String hid = "";
        try {
            hid = footprintArray.getJSONObject(footprintArray.length() - 1).getString("hid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (hid.equals("")) {
            mListView.stopLoadMore();
            return;
        }
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_CLUB_GET_TOPIC_LIST);
        String strInfo = SendInfo.getTopicListClubSendInfo(mInstance, cid, "0", hid, "10");
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(TYPE_FOOTPRINT_MORE));
    }

    private void cbGetFootprintListData(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            JSONArray array = object.getJSONArray("data");
            array.put(0,footprintArray.getJSONObject(0));
            footprintArray = array;
            if (rgMenu.getCheckedRadioButtonId() == R.id.radio_button1)
            {
                clubListAdapter.setData(footprintArray, ClubListAdapter.TYPE_FOOTPRINT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cbGetTopicListData(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            topicArray = object.getJSONArray("data");
            if (rgMenu.getCheckedRadioButtonId() == R.id.radio_button2)
            {
                clubListAdapter.setData(topicArray, ClubListAdapter.TYPE_TOPIC);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cbGetMemberListData(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            memberArray = object.getJSONArray("data");
            if (rgMenu.getCheckedRadioButtonId() == R.id.radio_button3)
            {
                clubListAdapter.setData(memberArray, ClubListAdapter.TYPE_MEMBER);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cbGetMoreFootprintListData(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            JSONArray array = object.getJSONArray("data");
            if (array.length() == 0)
                return;
            try {
                for (int i = 0; i < array.length(); i++)
                {
                    footprintArray.put(array.getJSONArray(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (rgMenu.getCheckedRadioButtonId() == R.id.radio_button1)
            {
                clubListAdapter.addData(footprintArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cbGetMoreTopicListData(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            JSONArray array = object.getJSONArray("data");
            try {
                for (int i = 0; i < array.length(); i++)
                {
                    topicArray.put(array.getJSONArray(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (rgMenu.getCheckedRadioButtonId() == R.id.radio_button1)
            {
                clubListAdapter.addData(topicArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ResponseHandlerInterface getResponseHandler(final int type)
    {
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] headers, byte[] responsebody) {
                mListView.stopRefresh();
                mListView.stopLoadMore();
                if (BaseFunction.verifyResult(new String(responsebody), clSnackContainer)) {
                    switch (type)
                    {
                        case ClubListAdapter.TYPE_FOOTPRINT:
                            cbGetFootprintListData(responsebody);
                            break;

                        case ClubListAdapter.TYPE_TOPIC:
                            cbGetTopicListData(responsebody);
                            break;

                        case ClubListAdapter.TYPE_MEMBER:
                            cbGetMemberListData(responsebody);
                            break;

                        case TYPE_FOOTPRINT_MORE:
                            cbGetMoreFootprintListData(responsebody);
                            break;

                        case TYPE_TOPIC_MORE:
                            cbGetMoreTopicListData(responsebody);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] responsebody, Throwable err) {
                mListView.stopRefresh();
                mListView.stopLoadMore();
            }
        };
    }

    @Override
    public void onRefresh() {
        switch (rgMenu.getCheckedRadioButtonId()) {
            case R.id.radio_button1:
                getFootprintListData(true);
                break;

            case R.id.radio_button2:
                getTopicListData(true);
                break;

            case R.id.radio_button3:
                getMemberListData(true);
                break;
        }
    }

    @Override
    public void onLoadMore() {
        switch (rgMenu.getCheckedRadioButtonId()) {
            case R.id.radio_button1:
                getMoreFootprintListData();
                break;

            case R.id.radio_button2:
                getMoreTopicListData();
                break;
        }
    }
}
