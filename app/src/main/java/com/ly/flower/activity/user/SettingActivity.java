package com.ly.flower.activity.user;


import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ly.flower.R;
import com.ly.flower.activity.login.WelcomeActivity;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.base.DataStructure;
import com.ly.flower.memory.GlobalStatic;

/**
 * Created by admin on 2016/3/25.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rlClearCache ;
    private TextView tvCacheSum ;
    private TextView tvLogout ;


    @Override
    public void init() {
        setView(R.layout.activity_setting);
        initView();
        fillView();
    }

    private void initView()
    {
        rlClearCache = (RelativeLayout) this.findViewById(R.id.rl_clear_cache);
        tvCacheSum = (TextView) this.findViewById(R.id.tv_sum);
        tvLogout = (TextView) this.findViewById(R.id.tv_logout);

        rlClearCache.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    private void fillView() {
        setTitle(R.string.str_setting);
        setLeftActionVisiable();
    }

    @Override
    public void doClickLeftAction() {
        finish();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.rl_clear_cache:
                clearCache();
                break;

            case R.id.tv_logout:
                logout();
                break;
        }
    }

    private void clearCache()
    {
        imageLoader.clearDiskCache();
        imageLoader.clearMemoryCache();
        tvCacheSum.setText("");
    }

    private void logout() {
        DataStructure.login = false;
        GlobalStatic.saveSharedString(this, GlobalStatic.LOGIN, "0");
        gotoActivityAndFinish(WelcomeActivity.class);
    }
}
