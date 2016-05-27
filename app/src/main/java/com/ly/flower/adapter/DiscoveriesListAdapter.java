package com.ly.flower.adapter;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ly.flower.R;
import com.ly.flower.activity.main.MainActivity;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.share.MessageHandler;
import com.ly.flower.viewholder.DiscoveryViewHolder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/3/18.
 */
public class DiscoveriesListAdapter extends BaseListAdapter {
    private MainActivity activity;
    private final int TYPE_RECOMMENT    = 0;
    private final int TYPE_NEWEST       = 1;
    private int type = TYPE_RECOMMENT;

    private JSONArray recommendArray = new JSONArray();
    private JSONArray newestArray = new JSONArray();

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            switch (msg.what) {
                case MessageHandler.PRISE_OPERATION:
                    praiseOperation(data.getString("cid"), data.getString("osubtype"));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public DiscoveriesListAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DiscoveryViewHolder discoveryViewHolder = null;
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_discovery, parent, false);
            discoveryViewHolder = new DiscoveryViewHolder(convertView);
            convertView.setTag(discoveryViewHolder);

        }else {
            discoveryViewHolder = (DiscoveryViewHolder) convertView.getTag();
        }

        discoveryViewHolder.initData(activity, (JSONObject) getItem(position), mHandler);
        return convertView;
    }

    public void setContext(MainActivity activity)
    {
        this.activity = activity;
    }

    public void praiseOperation(String cid, String osubtype) {
        recommendArray = changeData(recommendArray, cid, osubtype);
        newestArray = changeData(newestArray, cid, osubtype);
        array = changeData(array, cid, osubtype);
        notifyDataSetChanged();
    }

    public void setData(JSONArray array, int type) {
        if (TYPE_RECOMMENT == type)
            this.recommendArray = array;
        else
            this.newestArray = array;
    }

    public void setType(int type) {
        if (TYPE_RECOMMENT == type)
            array = recommendArray;
        else
            array = newestArray;
        this.type = type;
        notifyDataSetChanged();
    }

    public void addData(JSONArray array, int type) {
        try {
            for (int i = 0; i < array.length(); i++)
            {
                if (TYPE_RECOMMENT == type)
                    recommendArray.put(array.getJSONObject(i));
                else
                    newestArray.put(array.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray changeData(JSONArray array, String cid, String osubtype)
    {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (object.getString("sid").equals(cid)) {
                    int cpraise = Integer.valueOf(object.getString("cpraise"));
                    if (osubtype.equals("0")) {
                        cpraise--;
                    }else {
                        cpraise++;
                    }
                    object.put("cpraise", String.valueOf(cpraise));
                    object.put("bpraise", osubtype);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }
}
