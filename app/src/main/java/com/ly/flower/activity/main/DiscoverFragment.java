package com.ly.flower.activity.main;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.ly.flower.R;
import com.ly.flower.activity.club.DetailActivity;
import com.ly.flower.adapter.DiscoveriesListAdapter;
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
public class DiscoverFragment extends BaseFragment implements XListView.IXListViewListener{
    public final static String TAG = "DiscoveryFragment";


    private final int GET_DISCOVERY         = 1;
    private final int GET_DISCOVERY_MORE    = 2;
    private boolean bFirst = true;

    private XListView lvDiscoveries;
    private DiscoveriesListAdapter discoveriesListAdapter;

    public DiscoverFragment(MainActivity instance, LayoutInflater inflater) {
        super(instance, inflater);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (bFirst)
            lvDiscoveries.setPullLoadEnable(true);
        else
            lvDiscoveries.setPullLoadEnable(false);
    }

    @Override
    public void initView() {
        rlFragmentView = (RelativeLayout) inflater.inflate(R.layout.fragment_listview, null);
        lvDiscoveries = (XListView) rlFragmentView.findViewById(R.id.listView);

        discoveriesListAdapter = new DiscoveriesListAdapter(inflater);
        discoveriesListAdapter.setContext(mInstance);
        lvDiscoveries.setAdapter(discoveriesListAdapter);
        lvDiscoveries.setXListViewListener(this);
        lvDiscoveries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject object = (JSONObject) parent.getItemAtPosition(position);
                try {
                    //1:足迹;2:话题
                    if (object.getString("type").equals("1")) {
                        object.put("hid", object.getString("sid"));
                        object.put("place", object.getString("sub_title"));
                    } else {
                        object.put("tid", object.getString("sid"));
                    }
                    mInstance.gotoActivity(DetailActivity.class, object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getData() {
        if(bFirst){
            lvDiscoveries.startRefresh();
            bFirst = false;
        }
    }

    @Override
    public void refreshView() {
    }

    private void getDiscoveriesListData()
    {
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_DISCOVER_GET_LIST);
        String strInfo = SendInfo.getListOfDiscoverSendInfo(mInstance, "20", "0", new JSONObject());
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(GET_DISCOVERY));
    }

    private void getMoreDiscoveriesListData()
    {
        JSONObject object = discoveriesListAdapter.getLastObject();
        if (object == null)
            return;

        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_DISCOVER_GET_LIST);
        String strInfo = SendInfo.getListOfDiscoverSendInfo(mInstance, "10", "0", object);
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(GET_DISCOVERY_MORE));
    }

    private void cbGetDiscoveriesListData(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            JSONArray array = object.getJSONArray("data");
            discoveriesListAdapter.setData(array);
            if (array.length() >= 20) {
                lvDiscoveries.setPullLoadEnable(true);
            } else {
                lvDiscoveries.setPullLoadEnable(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cbGetMoreDiscoveriesListData(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            JSONArray array = object.getJSONArray("data");
            discoveriesListAdapter.addData(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ResponseHandlerInterface getResponseHandler(final int type)
    {
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] headers, byte[] responsebody) {
                lvDiscoveries.stopRefresh();
                lvDiscoveries.stopLoadMore();
                if (BaseFunction.verifyResult(new String(responsebody), mInstance.clSnackContainer)) {
                    switch (type)
                    {
                        case GET_DISCOVERY:
                            cbGetDiscoveriesListData(responsebody);
                            break;

                        case GET_DISCOVERY_MORE:
                            cbGetMoreDiscoveriesListData(responsebody);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] responsebody, Throwable err) {
                lvDiscoveries.stopRefresh();
                lvDiscoveries.stopLoadMore();
            }
        };
    }

    private JSONArray testData()
    {
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        try {
            object.put("cid", "cid");
            object.put("cname", "元子");
            object.put("cavatar", "");
            object.put("type", "0");
            object.put("ctype", "0");
            object.put("url_video", "cid");
            object.put("time", "2016/03/18 16:49:58");
            object.put("sid", "cid");
            object.put("title", "探索新的Android Material Design支持库");
            object.put("sub_title", "我是Material Design的粉丝，它使应用程序更具有)");
            object.put("ccomment", "34");
            object.put("cpraise", "4");
            object.put("uid", "2");
            object.put("uavatar", "http:\\/\\/www.sunflowerslove.cn\\/ImageService\\/ImageLibrary\\/20160219\\/84cf4991-7c04-43d3-a167-2dfc739102cf.jpg");
            object.put("uname", "melody");
            object.put("bbound", "cid");
            object.put("mid", "cid");
            object.put("mname", "cid");
            object.put("mavatar", "cid");
            object.put("bpraise", "0");

            JSONArray imageArray = new JSONArray();
            JSONObject imageObject = new JSONObject();
            imageObject.put("url", "http:\\/\\/www.sunflowerslove.cn\\/ImageService\\/ImageLibrary\\/20160303\\/8ae650d1-f1a5-4176-b549-d256113843b0.jp");
            imageArray.put(imageObject);
            imageArray.put(imageObject);
            object.put("img", imageArray);
            array.put(object);
            array.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    @Override
    public void onRefresh() {
        getDiscoveriesListData();
    }

    @Override
    public void onLoadMore() {
        getMoreDiscoveriesListData();
    }
}
