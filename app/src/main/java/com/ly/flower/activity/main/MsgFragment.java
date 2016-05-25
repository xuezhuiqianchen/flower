package com.ly.flower.activity.main;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.ly.flower.R;
import com.ly.flower.adapter.MsgListAdapter;
import com.ly.flower.base.BaseFragment;
import com.ly.flower.component.XListView;
import com.ly.flower.network.AscynHttpUtil;
import com.ly.flower.network.SendInfo;
import com.ly.flower.base.BaseFunction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2016/3/15.
 */
public class MsgFragment extends BaseFragment implements XListView.IXListViewListener{
    public final static String TAG = "MsgFragment";

    private XListView lvMsgList;
    private MsgListAdapter msgListAdapter;
    private boolean bFirst = true;


    public MsgFragment(MainActivity instance, LayoutInflater inflater) {
        super(instance, inflater);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        lvMsgList.setPullLoadEnable(false);
    }

    @Override
    public void initView() {
        rlFragmentView = (RelativeLayout) inflater.inflate(R.layout.fragment_listview, null);
        lvMsgList = (XListView) rlFragmentView.findViewById(R.id.listView);

        msgListAdapter = new MsgListAdapter(inflater);
        msgListAdapter.setContext(mInstance);
        lvMsgList.setAdapter(msgListAdapter);
        lvMsgList.setXListViewListener(this);
        lvMsgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject object = (JSONObject) parent.getItemAtPosition(position);
            }
        });

    }

    @Override
    public void getData() {
        if(bFirst){
            lvMsgList.startRefresh();
            bFirst = false;
        }
    }

    @Override
    public void refreshView() {
    }


    private void getMsgListData()
    {
//        msgListAdapter.setData(testData());
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_MSG_GET_NEW_MSG);
        String strInfo = SendInfo.getUserUnitSendInfo(mInstance);
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler());
    }

    private void cbGetMsgListData(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            JSONArray array = object.getJSONArray("data");
            msgListAdapter.setData(array);
            if (array.length() >= 20) {
                lvMsgList.setPullLoadEnable(true);
            } else {
                lvMsgList.setPullLoadEnable(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ResponseHandlerInterface getResponseHandler()
    {
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] headers, byte[] responsebody) {
                lvMsgList.stopRefresh();
                if (BaseFunction.verifyResult(new String(responsebody), mInstance.clSnackContainer)) {
                    cbGetMsgListData(responsebody);
                }
            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] responsebody, Throwable err) {
                lvMsgList.stopRefresh();
                mInstance.showAlertMessage(err.getMessage());
            }
        };
    }

    private JSONArray testData()
    {
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        try {
            object.put("type", "0");
            object.put("icontent", "i1");
            object.put("imsg", "");
            object.put("sid", "0");
            object.put("sname", "melody");
            object.put("content", "zhu内容");
            object.put("sub_content", "探索新的Android Material Design支持库");
            object.put("cname", "唱吧");
            object.put("time", "2016/03/18 16:49:58");
            object.put("icon", "http:\\/\\/www.sunflowerslove.cn\\/ImageService\\/ImageLibrary\\/20160219\\/84cf4991-7c04-43d3-a167-2dfc739102cf.jpg");

            array.put(object);
            object = new JSONObject();
            object.put("type", "1");
            object.put("icontent", "i1");
            object.put("imsg", "");
            object.put("sid", "0");
            object.put("sname", "melody");
            object.put("content", "这是话题了ID住电话发来的减肥了");
            object.put("sub_content", "副标题");
            object.put("cname", "工艺小铺");
            object.put("time", "2016/03/23 16:49:58");
            object.put("icon", "http:\\/\\/www.sunflowerslove.cn\\/ImageService\\/ImageLibrary\\/20160219\\/84cf4991-7c04-43d3-a167-2dfc739102cf.jpg");

            array.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    @Override
    public void onRefresh() {
        getMsgListData();
    }

    @Override
    public void onLoadMore() {

    }
}
