package com.ly.flower.activity.main;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
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
public class DiscoverFragment extends BaseFragment implements XListView.IXListViewListener, RadioGroup.OnCheckedChangeListener {
    public final static String TAG = "DiscoveryFragment";

    private final int TYPE_RECOMMEND        = 0;
    private final int TYPE_NEWEST           = 1;
    private final int GET_DISCOVERY         = 1;
    private final int GET_DISCOVERY_MORE    = 2;
    private boolean bFirstRecommend = true;
    private boolean bFirstNewest = true;
    private boolean bLoadMoreForRecommend   = false;
    private boolean isbLoadMoreForNewest    = false;

    private RadioGroup rgMenu;
    private XListView lvDiscoveries;
    private DiscoveriesListAdapter discoveriesListAdapter;
    private int recommenPpage = 1;
    private int newestPpage = 1;

    public DiscoverFragment(MainActivity instance, LayoutInflater inflater) {
        super(instance, inflater);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void initView() {
        rlFragmentView = (RelativeLayout) inflater.inflate(R.layout.fragment_discovery, null);
        rgMenu = (RadioGroup) rlFragmentView.findViewById(R.id.rg_menu);
        rgMenu.setOnCheckedChangeListener(this);

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
                    bFirstRecommend = true;
                    bFirstNewest = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getData() {
        if(bFirstRecommend){
            lvDiscoveries.startRefresh();
            bFirstRecommend = false;
        }
    }

    @Override
    public void refreshView() {
    }

    private void getDiscoveriesListData(int type) {
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_DISCOVER_GET_LIST);
        String strInfo = SendInfo.getListOfDiscoverSendInfo(mInstance, "20", "0", String.valueOf(type), new JSONObject());
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(GET_DISCOVERY, type));
    }

    private void getMoreDiscoveriesListData(int type) {
        JSONObject object = discoveriesListAdapter.getLastObject();
        if (object == null)
        {
            lvDiscoveries.stopLoadMore();
            return;
        }
        int page = 1;
        if (TYPE_RECOMMEND == type) {
            page = recommenPpage;
            recommenPpage++;
        } else {
            page = newestPpage;
            newestPpage++;
        }

        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance,
                AscynHttpUtil.URL_DISCOVER_GET_LIST);
        String strInfo = SendInfo.getListOfDiscoverSendInfo(mInstance, "10", String.valueOf(page),
                String.valueOf(type), object);
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(GET_DISCOVERY_MORE, type));
    }

    private void cbGetDiscoveriesListData(byte[] responsebody, int type) {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            JSONArray array = object.getJSONArray("data");
            discoveriesListAdapter.setData(array, type);
            if ((rgMenu.getCheckedRadioButtonId() == R.id.rb_recommend && type == TYPE_RECOMMEND)
                    ||(rgMenu.getCheckedRadioButtonId() == R.id.rb_new && type == TYPE_NEWEST))
                discoveriesListAdapter.setType(type);

            if (array.length() >= 20) {
                if (type == TYPE_RECOMMEND)
                    bLoadMoreForRecommend = true;
                else
                    isbLoadMoreForNewest = true;
                lvDiscoveries.setPullLoadEnable(true);
            } else {
                if (type == TYPE_RECOMMEND)
                    bLoadMoreForRecommend = false;
                else
                    isbLoadMoreForNewest = false;
                lvDiscoveries.setPullLoadEnable(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cbGetMoreDiscoveriesListData(byte[] responsebody, int type)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            JSONArray array = object.getJSONArray("data");
            discoveriesListAdapter.addData(array, type);
            if ((rgMenu.getCheckedRadioButtonId() == R.id.rb_recommend && type == TYPE_RECOMMEND)
                    ||(rgMenu.getCheckedRadioButtonId() == R.id.rb_new && type == TYPE_NEWEST))
                discoveriesListAdapter.setType(type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ResponseHandlerInterface getResponseHandler(final int type, final int discoveryType)
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
                            cbGetDiscoveriesListData(responsebody, discoveryType);
                            break;

                        case GET_DISCOVERY_MORE:
                            cbGetMoreDiscoveriesListData(responsebody, discoveryType);
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

    @Override
    public void onRefresh() {
        if (rgMenu.getCheckedRadioButtonId() == R.id.rb_recommend)
            getDiscoveriesListData(TYPE_RECOMMEND);
        else
            getDiscoveriesListData(TYPE_NEWEST);
    }

    @Override
    public void onLoadMore() {
        if (rgMenu.getCheckedRadioButtonId() == R.id.rb_recommend)
            getMoreDiscoveriesListData(TYPE_RECOMMEND);
        else
            getMoreDiscoveriesListData(TYPE_NEWEST);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (rgMenu.getCheckedRadioButtonId() == R.id.rb_recommend){
            discoveriesListAdapter.setType(TYPE_RECOMMEND);
            lvDiscoveries.setPullLoadEnable(bLoadMoreForRecommend);
        }else {
            if(bFirstNewest){
                lvDiscoveries.startRefresh();
                bFirstNewest = false;
            }
            discoveriesListAdapter.setType(TYPE_NEWEST);
            lvDiscoveries.setPullLoadEnable(isbLoadMoreForNewest);
        }
    }
}
