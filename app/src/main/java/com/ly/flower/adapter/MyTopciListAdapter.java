package com.ly.flower.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ly.flower.R;
import com.ly.flower.viewholder.MyTopicViewHolder;

import org.json.JSONObject;

/**
 * Created by admin on 2016/5/23.
 */
public class MyTopciListAdapter extends BaseListAdapter {
    private Context context;

    public MyTopciListAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyTopicViewHolder myTopicViewHolder = null;
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_discovery, parent, false);
            myTopicViewHolder = new MyTopicViewHolder(convertView);
            convertView.setTag(myTopicViewHolder);

        }else {
            myTopicViewHolder = (MyTopicViewHolder) convertView.getTag();
        }

        myTopicViewHolder.initData(context, (JSONObject)getItem(position));

        return convertView;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }
}
