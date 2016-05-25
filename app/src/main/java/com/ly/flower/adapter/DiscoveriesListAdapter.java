package com.ly.flower.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ly.flower.R;
import com.ly.flower.viewholder.DiscoveryViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/3/18.
 */
public class DiscoveriesListAdapter extends BaseListAdapter {
    private Context context;

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

        discoveryViewHolder.initData(context, (JSONObject) getItem(position));
        return convertView;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public JSONObject getLastObject()
    {
        try {
            return array.getJSONObject(array.length() - 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
