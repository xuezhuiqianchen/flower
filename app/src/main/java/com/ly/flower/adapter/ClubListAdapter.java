package com.ly.flower.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.share.MessageHandler;
import com.ly.flower.viewholder.DiscoveryViewHolder;
import com.ly.flower.viewholder.FootprintTitleViewHolder;
import com.ly.flower.viewholder.FootprintViewHolder;
import com.ly.flower.viewholder.MemberViewHolder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by admin on 2016/3/24.
 */
public class ClubListAdapter extends BaseListAdapter{
    private BaseActivity activity;

    public static final int TYPE_FOOTPRINT_TITLE   = 0;
    public static final int TYPE_FOOTPRINT         = 1;
    public static final int TYPE_TOPIC             = 2;
    public static final int TYPE_MEMBER            = 3;


    private static final int TYPE_COUNT = 4;
    private int type = TYPE_FOOTPRINT;

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            switch (msg.what) {
                case MessageHandler.PRISE_OPERATION:
//                    praiseOperation(data.getString("cid"), data.getString("osubtype"));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public ClubListAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (type == TYPE_FOOTPRINT && position == 0)
        {
            return TYPE_FOOTPRINT_TITLE;
        }else {
            return type;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FootprintTitleViewHolder footprintTitleViewHolder = null;
        FootprintViewHolder footprintViewHolder = null;
        DiscoveryViewHolder discoveryViewHolder = null;
        MemberViewHolder memberViewHolder = null;

        int type = getItemViewType(position);
        if(convertView == null)
        {
            switch (type)
            {
                case TYPE_FOOTPRINT_TITLE:
                    convertView = inflater.inflate(R.layout.item_footprint_title, parent, false);
                    footprintTitleViewHolder = new FootprintTitleViewHolder(convertView);
                    convertView.setTag(footprintTitleViewHolder);
                    break;

                case TYPE_FOOTPRINT:
                    convertView = inflater.inflate(R.layout.item_footprint, parent, false);
                    footprintViewHolder = new FootprintViewHolder(convertView);
                    convertView.setTag(footprintViewHolder);
                    break;

                case TYPE_TOPIC:
                    convertView = inflater.inflate(R.layout.item_discovery, parent, false);
                    discoveryViewHolder = new DiscoveryViewHolder(convertView);
                    convertView.setTag(discoveryViewHolder);
                    break;

                case TYPE_MEMBER:
                    convertView = inflater.inflate(R.layout.item_member, parent, false);
                    memberViewHolder = new MemberViewHolder(convertView);
                    convertView.setTag(memberViewHolder);
                    break;
            }
        }else {
            switch (type)
            {
                case TYPE_FOOTPRINT_TITLE:
                    footprintTitleViewHolder = (FootprintTitleViewHolder) convertView.getTag();
                    break;

                case TYPE_FOOTPRINT:
                    footprintViewHolder = (FootprintViewHolder) convertView.getTag();
                    break;

                case TYPE_TOPIC:
                    discoveryViewHolder = (DiscoveryViewHolder) convertView.getTag();
                    break;

                case TYPE_MEMBER:
                    memberViewHolder = (MemberViewHolder) convertView.getTag();
                    break;
            }

        }

        switch (type)
        {
            case TYPE_FOOTPRINT_TITLE:
                footprintTitleViewHolder.initData(activity, (JSONObject)getItem(position));
                break;

            case TYPE_FOOTPRINT:
                footprintViewHolder.initData(activity, (JSONObject)getItem(position));
                break;

            case TYPE_TOPIC:
                discoveryViewHolder.initData(activity, (JSONObject)getItem(position), mHandler);
                break;

            case TYPE_MEMBER:
                memberViewHolder.initData(activity, (JSONObject)getItem(position));
                break;
        }

        return convertView;
    }

    public void setData(JSONArray array, int type)
    {
        this.type = type;
        this.array = array;
        notifyDataSetChanged();
    }

    public void setContext(BaseActivity activity)
    {
        this.activity = activity;
    }
}
