package com.ly.flower.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ly.flower.R;
import com.ly.flower.viewholder.ClubViewHolder;
import org.json.JSONObject;

/**
 * Created by admin on 2016/3/16.
 */
public class HomeListAdapter extends BaseListAdapter {
    private Context context;

    public HomeListAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClubViewHolder clubViewHolder = null;
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_club, parent, false);
            clubViewHolder = new ClubViewHolder(convertView);
            convertView.setTag(clubViewHolder);
        }else {
            clubViewHolder = (ClubViewHolder) convertView.getTag();
        }
        clubViewHolder.initData(context, (JSONObject)getItem(position));
        return convertView;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }
}
