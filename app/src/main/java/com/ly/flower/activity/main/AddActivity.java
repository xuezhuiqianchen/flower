package com.ly.flower.activity.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.base.DataStructure;
import com.ly.flower.memory.GlobalStatic;

/**
 * Created by admin on 2016/5/27.
 */
public class AddActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBg ;
    private RelativeLayout layoutClub, layoutAddUs, layoutInterestCard;

    @Override
    public void init() {
        setView(R.layout.activity_add);
        initView();
        fillView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.rl_club:
                doClickClubAction();
                break;
            case R.id.rl_add_us:
                doClickAddUsAction();
                break;
            case R.id.rl_interest_card:
                doClickInterestAction();
            default:
                break;
        }
    }

    private void initView() {
        ivBg = (ImageView) this.findViewById(R.id.iv_bg);
        layoutClub = (RelativeLayout) this.findViewById(R.id.rl_club);
        layoutAddUs = (RelativeLayout) this.findViewById(R.id.rl_add_us);
        layoutInterestCard = (RelativeLayout) this.findViewById(R.id.rl_interest_card);

        layoutClub.setOnClickListener(this);
        layoutAddUs.setOnClickListener(this);
        layoutInterestCard.setOnClickListener(this);
    }

    private void fillView() {
        hideTitleLayout();
        if (GlobalStatic.getSharedString(this, GlobalStatic.LOGIN).equals("1")) {
            DataStructure.login = true;
            DataStructure.uid = GlobalStatic.getSharedString(this, GlobalStatic.UID);
            DataStructure.passwd = GlobalStatic.getSharedString(this, GlobalStatic.PASSWORD);
        }
    }

    private void doClickClubAction() {

    }

    private void doClickAddUsAction() {

    }

    private void doClickInterestAction() {

    }
}
