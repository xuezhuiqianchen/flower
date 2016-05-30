package com.ly.flower.activity.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ly.flower.R;
import com.ly.flower.activity.login.LoginActivity;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.base.DataStructure;
import com.ly.flower.memory.GlobalStatic;
import com.ly.flower.share.Blur;
import com.ly.flower.share.FastBlur;
import com.ly.flower.share.MessageHandler;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    public static MainActivity mInstance;
    private LayoutInflater inflater = null;

    private RelativeLayout rlHome, rlDiscovery, rlAdd, rlMsg, rlUser;
    private ImageView ivHome, ivDiscovery, ivAdd, ivMsg, ivUser;

    private int checkId = 0;

    private RelativeLayout rlContent = null;
    private AddFragment addFragment = null;
    private HomeFragment homeFragment = null;
    private DiscoverFragment discoverFragment = null;
    private MsgFragment msgFragment = null;
    private UserFragment userFragment = null;

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case MessageHandler.REQUEST_MSG_FRAGMENT:
                if (resultCode == MessageHandler.RESULT_OK){
                    DataStructure.login = true;
                    checkId = R.id.rl_msg;
                    doCheckMsgAction();
                }
                break;

            case MessageHandler.REQUEST_USER_FRAGMENT:
                if (resultCode == MessageHandler.RESULT_OK) {
                    DataStructure.login = true;
                    checkId = R.id.rl_user;
                    doCheckUserAction();
                }
                break;
        }
    }

    @Override
    public void init() {
        setView(R.layout.activity_main);
        mInstance = this;
        inflater =(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
        fillView();
    }

    @Override
    public void onClick(View v) {
        setChecked(v.getId());
    }

    private void initView() {
        initMenuView();
        rlContent = (RelativeLayout) this.findViewById(R.id.rl_content);
        initFragments();
        showFragment(discoverFragment.TAG);
    }

    private void fillView() {
        setTitle(R.string.str_discover);
        hideTitleLayout();
        if (GlobalStatic.getSharedString(this, GlobalStatic.LOGIN).equals("1")) {
            DataStructure.login = true;
            DataStructure.uid = GlobalStatic.getSharedString(this, GlobalStatic.UID);
            DataStructure.passwd = GlobalStatic.getSharedString(this, GlobalStatic.PASSWORD);
        }

    }

    private void initFragments() {
        addFragment = new AddFragment(mInstance, inflater);
        homeFragment = new HomeFragment(mInstance, inflater);
        discoverFragment = new DiscoverFragment(mInstance, inflater);
        msgFragment = new MsgFragment(mInstance, inflater);
        userFragment = new UserFragment(mInstance, inflater);
    }

    private void initMenuView() {
        rlHome = (RelativeLayout) this.findViewById(R.id.rl_home);
        rlDiscovery = (RelativeLayout) this.findViewById(R.id.rl_discover);
        rlAdd = (RelativeLayout) this.findViewById(R.id.rl_add);
        rlMsg = (RelativeLayout) this.findViewById(R.id.rl_msg);
        rlUser = (RelativeLayout) this.findViewById(R.id.rl_user);

        ivHome = (ImageView) this.findViewById(R.id.iv_home);
        ivDiscovery = (ImageView) this.findViewById(R.id.iv_discover);
        ivAdd = (ImageView) this.findViewById(R.id.iv_add);
        ivMsg = (ImageView) this.findViewById(R.id.iv_msg);
        ivUser = (ImageView) this.findViewById(R.id.iv_user);

        rlHome.setOnClickListener(this);
        rlDiscovery.setOnClickListener(this);
        rlAdd.setOnClickListener(this);
        rlMsg.setOnClickListener(this);
        rlUser.setOnClickListener(this);

        checkId = R.id.rl_discover;
    }

    private void showFragment(String tag) {
        if (AddFragment.TAG == tag)
        {
            addFragment.onResume();
            return;
        }

        if (rlContent.getTag() == tag) {
            return;
        }

        View view = null;
        switch (tag){
            case HomeFragment.TAG:
                view = homeFragment.getView();
                homeFragment.onResume();
                break;

            case DiscoverFragment.TAG:
                view = discoverFragment.getView();
                discoverFragment.onResume();
                break;

            case MsgFragment.TAG:
                view = msgFragment.getView();
                msgFragment.onResume();
                break;

            case UserFragment.TAG:
                view = userFragment.getView();
                userFragment.onResume();
                break;
        }

        if (view == null)
        {
            return;
        }

        if (AddFragment.TAG != tag) {
            rlContent.removeAllViews();
        }

        rlContent.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        rlContent.setTag(tag);
    }

    private void resetCheckedView() {
        ivHome.setImageResource(R.drawable.home_icon);
        ivDiscovery.setImageResource(R.drawable.discovery_icon);
        ivMsg.setImageResource(R.drawable.msg_icon);
        ivUser.setImageResource(R.drawable.user_icon);
    }

    public void setChecked(int id) {
        if (checkId == id)
            return;

        if (id != R.id.rl_add)
        {
            resetCheckedView();
        }

        switch (id)
        {
            case R.id.rl_home:
                checkId = id;
                doCheckHomeAction();
                break;

            case R.id.rl_discover:
                checkId = id;
                doCheckDiscoveryAction();
                break;

            case R.id.rl_add:
                doCheckAddAction();
                break;

            case R.id.rl_msg:
                if (doCheckMsgAction())
                    checkId = id;
                break;

            case R.id.rl_user:
                if (doCheckUserAction())
                    checkId = id;
                break;
        }
    }

    public void doCheckAddAction(){
        /**
         * 取截图；模糊处理；动画
         */
        View view = getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        bitmap = Blur.fastblur(mInstance, bitmap, 15);
        addFragment.setBackground(bitmap);
        showFragment(AddFragment.TAG);
    }

    public void doCheckHomeAction(){
        ivHome.setImageResource(R.drawable.home_select_icon);
        showFragment(HomeFragment.TAG);
        setTitle(R.string.str_club);
        displayTitleLayout();
    }

    public void doCheckDiscoveryAction(){
        ivDiscovery.setImageResource(R.drawable.discovery_select_icon);
        showFragment(DiscoverFragment.TAG);
        setTitle(R.string.str_discover);
        hideTitleLayout();
    }

    public boolean doCheckMsgAction(){
        if (!DataStructure.login)
        {
            gotoActivityForResult(LoginActivity.class, MessageHandler.REQUEST_MSG_FRAGMENT);
            return false;
        }
        ivMsg.setImageResource(R.drawable.msg_select_icon);
        showFragment(MsgFragment.TAG);
        setTitle(R.string.str_msg);
        displayTitleLayout();
        return true;
    }

    public boolean doCheckUserAction(){
        if (!DataStructure.login)
        {
            gotoActivityForResult(LoginActivity.class, MessageHandler.REQUEST_USER_FRAGMENT);
            return false;
        }
        ivUser.setImageResource(R.drawable.user_select_icon);
        showFragment(UserFragment.TAG);
        setTitle(R.string.str_me);
        displayTitleLayout();
        return true;
    }
}
