package com.ly.flower.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.viewholder.MyCommentViewHolder;
import org.json.JSONObject;

/**
 * Created by admin on 2016/5/23.
 */
public class MyCommentListAdapter extends BaseListAdapter {
    private BaseActivity activity;

    public MyCommentListAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyCommentViewHolder myCommentViewHolder = null;
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_my_comment, parent, false);
            myCommentViewHolder = new MyCommentViewHolder(convertView);
            convertView.setTag(myCommentViewHolder);

        }else {
            myCommentViewHolder = (MyCommentViewHolder) convertView.getTag();
        }

        myCommentViewHolder.initData(activity, (JSONObject)getItem(position));

        return convertView;
    }

    public void setContext(BaseActivity activity)
    {
        this.activity = activity;
    }
}
