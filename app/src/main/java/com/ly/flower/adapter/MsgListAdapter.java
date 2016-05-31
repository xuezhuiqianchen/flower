package com.ly.flower.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.viewholder.MsgViewHolder;
import org.json.JSONObject;

/**
 * Created by admin on 2016/3/23.
 */
public class MsgListAdapter extends BaseListAdapter {
    private BaseActivity activity;

    public MsgListAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MsgViewHolder msgViewHolder = null;
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_msg, parent, false);
            msgViewHolder = new MsgViewHolder(convertView);
            convertView.setTag(msgViewHolder);

        }else {
            msgViewHolder = (MsgViewHolder) convertView.getTag();
        }

        msgViewHolder.initData(activity, (JSONObject)getItem(position));

        return convertView;
    }

    public void setContext(BaseActivity activity)
    {
        this.activity = activity;
    }
}
