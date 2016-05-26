package com.ly.flower.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/3/16.
 */
public class BaseListAdapter extends android.widget.BaseAdapter{
    public LayoutInflater inflater = null;
    public JSONArray array = new JSONArray();


    public BaseListAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


    @Override
    public int getCount() {
        return array.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return array.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return convertView;
    }

    public void setData(JSONArray array)
    {
        this.array = array;
        notifyDataSetChanged();
    }

    public void addData(JSONArray array)
    {
        try {
            for (int i = 0; i < array.length(); i++)
            {
                this.array.put(array.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
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
