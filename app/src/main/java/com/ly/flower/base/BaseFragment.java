package com.ly.flower.base;

import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.ly.flower.activity.main.MainActivity;

/**
 * Created by admin on 2016/3/16.
 */
public abstract class BaseFragment {
    public MainActivity mInstance;
    public LayoutInflater inflater = null;
    public RelativeLayout rlFragmentView = null;

    public BaseFragment(MainActivity instance, LayoutInflater inflater)
    {
        mInstance = instance;
        this.inflater = inflater;
        init();
    }

    public void init()
    {
        initView();
    }

    public void onResume()
    {
        getData();
        refreshView();
    }

    public RelativeLayout getView()
    {
        return rlFragmentView;
    }

    public abstract void initView();
    public abstract void getData();
    public abstract void refreshView();
}
