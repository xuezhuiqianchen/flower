package com.ly.flower.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.viewholder.CommentViewHolder;
import com.ly.flower.viewholder.FootprintDetailViewHolder;
import com.ly.flower.viewholder.TopicDetailViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/3/24.
 */
public class DetailListAdapter extends BaseListAdapter {
    private BaseActivity context;

    public static final int TYPE_FOOTPRINT_DETAIL  = 0;
    public static final int TYPE_TOPIC_DETAIL      = 1;
    public static final int TYPE_COMMENT           = 2;

    private static final int TYPE_COUNT = 3;
    private int currentType = TYPE_FOOTPRINT_DETAIL;

    public DetailListAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
        {
            return currentType;
        }else {
            return TYPE_COMMENT;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FootprintDetailViewHolder footprintDetailViewHolder = null;
        TopicDetailViewHolder topicDetailViewHolder = null;
        CommentViewHolder commentViewHolder = null;
        int type = getItemViewType(position);
        if(convertView == null) {
            switch (type) {
                case TYPE_FOOTPRINT_DETAIL:
                    convertView = inflater.inflate(R.layout.item_footprint_detail, parent, false);
                    footprintDetailViewHolder = new FootprintDetailViewHolder(convertView);
                    convertView.setTag(footprintDetailViewHolder);
                    break;

                case TYPE_TOPIC_DETAIL:
                    convertView = inflater.inflate(R.layout.item_topic_detail, parent, false);
                    topicDetailViewHolder = new TopicDetailViewHolder(convertView);
                    convertView.setTag(topicDetailViewHolder);
                    break;

                case TYPE_COMMENT:
                    convertView = inflater.inflate(R.layout.item_comment, parent, false);
                    commentViewHolder = new CommentViewHolder(convertView);
                    convertView.setTag(commentViewHolder);
                    break;
            }
        }else {
            switch (type) {
                case TYPE_FOOTPRINT_DETAIL:
                    footprintDetailViewHolder = (FootprintDetailViewHolder) convertView.getTag();
                    break;

                case TYPE_TOPIC_DETAIL:
                    topicDetailViewHolder = (TopicDetailViewHolder) convertView.getTag();
                    break;

                case TYPE_COMMENT:
                    commentViewHolder = (CommentViewHolder) convertView.getTag();
                    break;
            }
        }

        switch (type) {
            case TYPE_FOOTPRINT_DETAIL:
                footprintDetailViewHolder.initData(context, (JSONObject)getItem(position));
                break;

            case TYPE_TOPIC_DETAIL:
                topicDetailViewHolder.initData(context, (JSONObject)getItem(position));
                break;

            case TYPE_COMMENT:
                commentViewHolder.initData(context, (JSONObject)getItem(position), position);
                break;
        }
        return convertView;
    }

    public void setContext(BaseActivity context)
    {
        this.context = context;
    }

    public void addComment(JSONObject object)
    {
        this.array.put(object);
        notifyDataSetChanged();
    }

    public void setType(int type)
    {
        this.currentType = type;
    }

    public void refreshPraise(String bpraise)
    {
        try {

            JSONObject object = array.getJSONObject(0);
            int cPraise = Integer.valueOf(object.getString("cpraise"));
            object.put("bpraise", bpraise);
            if (bpraise.equals("1"))
            {
                cPraise++;
            }else {
                cPraise--;
            }
            object.put("cpraise", String.valueOf(cPraise));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }
}
