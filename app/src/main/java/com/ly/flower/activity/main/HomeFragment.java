package com.ly.flower.activity.main;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.ly.flower.R;
import com.ly.flower.activity.club.ClubActivity;
import com.ly.flower.adapter.HomeListAdapter;
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
public class HomeFragment extends BaseFragment implements XListView.IXListViewListener{
    public final static String TAG = "HomeFragment";

    private final int GET_CLUBS         = 1;
    private final int GET_CLUBS_MORE    = 2;

    private XListView lvClubs;
    private HomeListAdapter homeListAdapter;

    public HomeFragment(MainActivity instance, LayoutInflater inflater) {
        super(instance, inflater);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        lvClubs.setPullLoadEnable(true);
    }

    @Override
    public void initView() {
        rlFragmentView = (RelativeLayout) inflater.inflate(R.layout.fragment_listview, null);

        lvClubs = (XListView) rlFragmentView.findViewById(R.id.listView);

        homeListAdapter = new HomeListAdapter(inflater);
        homeListAdapter.setContext(mInstance);
        lvClubs.setAdapter(homeListAdapter);
        lvClubs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject object = (JSONObject) parent.getItemAtPosition(position);
                mInstance.gotoActivity(ClubActivity.class, object.toString());
            }
        });

        lvClubs.setXListViewListener(this);
    }

    @Override
    public void getData() {
        lvClubs.startRefresh();
    }

    @Override
    public void refreshView() {
    }

    private void getClubListData()
    {
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_CLUB_SELECT);
        String strInfo = SendInfo.getSelectClubSendInfo(mInstance, "0", "0", "20");
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(GET_CLUBS));
    }

    private void getMoreClubListData()
    {
        String cid = "";
        try {
            cid = homeListAdapter.getLastObject().getString("cid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance, AscynHttpUtil.URL_CLUB_SELECT);
        String strInfo = SendInfo.getSelectClubSendInfo(mInstance, "0", cid, "10");
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(GET_CLUBS_MORE));
    }

    private void cbGetClubListData(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            JSONArray array = object.getJSONArray("data");
            homeListAdapter.setData(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cbGetMoreClubListData(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            JSONArray array = object.getJSONArray("data");
            homeListAdapter.addData(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ResponseHandlerInterface getResponseHandler(final int type)
    {
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] headers, byte[] responsebody) {
                lvClubs.stopLoadMore();
                lvClubs.stopRefresh();
                if (BaseFunction.verifyResult(new String(responsebody), mInstance.clSnackContainer)) {
                    switch (type)
                    {
                        case GET_CLUBS:
                            cbGetClubListData(responsebody);
                            break;

                        case GET_CLUBS_MORE:
                            cbGetMoreClubListData(responsebody);
                            break;
                    }

                }
            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] responsebody, Throwable err) {
                lvClubs.stopLoadMore();
                lvClubs.stopRefresh();
            }
        };
    }

    @Override
    public void onRefresh() {
        getClubListData();
    }

    @Override
    public void onLoadMore() {
        getMoreClubListData();
    }
}
