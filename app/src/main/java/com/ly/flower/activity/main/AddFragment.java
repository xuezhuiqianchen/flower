package com.ly.flower.activity.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.ly.flower.R;
import com.ly.flower.base.DataStructure;
import com.ly.flower.memory.GlobalStatic;

/**
 * Created by admin on 2016/5/27.
 */
public class AddFragment implements View.OnClickListener {
    public final static String TAG = "AddFragment";
    public MainActivity mInstance;
    public LayoutInflater inflater = null;
    public RelativeLayout rlFragmentView = null;

    private ImageView ivBg ;
    private RelativeLayout layoutClub, layoutAddUs, layoutInterestCard;//, layoutContent

    public Animation inBounceAnim, outBounceAnim;

    public AddFragment(MainActivity instance, LayoutInflater inflater) {
        mInstance = instance;
        this.inflater = inflater;
        initView();
    }

    public void initView() {
        rlFragmentView = (RelativeLayout) mInstance.findViewById(R.id.rl_add_fragment);
        ivBg = (ImageView) mInstance.findViewById(R.id.iv_backgound);
        layoutClub = (RelativeLayout) mInstance.findViewById(R.id.rl_club);
        layoutAddUs = (RelativeLayout) mInstance.findViewById(R.id.rl_add_us);
        layoutInterestCard = (RelativeLayout) mInstance.findViewById(R.id.rl_interest_card);

        ivBg.setOnClickListener(this);
        layoutClub.setOnClickListener(this);
        layoutAddUs.setOnClickListener(this);
        layoutInterestCard.setOnClickListener(this);

        inBounceAnim = AnimationUtils.loadAnimation(mInstance, R.anim.in_bounce);
        outBounceAnim = AnimationUtils.loadAnimation(mInstance, R.anim.out_bounce);

        inBounceAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                layoutClub.setVisibility(View.GONE);
//                layoutInterestCard.setVisibility(View.GONE);
//                layoutAddUs.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layoutClub.setVisibility(View.VISIBLE);
                layoutInterestCard.setVisibility(View.VISIBLE);
                layoutAddUs.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        outBounceAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layoutClub.setVisibility(View.VISIBLE);
                layoutInterestCard.setVisibility(View.VISIBLE);
                layoutAddUs.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layoutClub.setVisibility(View.GONE);
                layoutInterestCard.setVisibility(View.GONE);
                layoutAddUs.setVisibility(View.GONE);
                rlFragmentView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void onResume()
    {
        rlFragmentView.setVisibility(View.VISIBLE);
        refreshView();
    }

    @Override
    public void onClick(View v) {
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
                startOutAnim();
                break;
        }
    }

    public void refreshView() {
        if (GlobalStatic.getSharedString(mInstance, GlobalStatic.LOGIN).equals("1")) {
            DataStructure.login = true;
            DataStructure.uid = GlobalStatic.getSharedString(mInstance, GlobalStatic.UID);
            DataStructure.passwd = GlobalStatic.getSharedString(mInstance, GlobalStatic.PASSWORD);
        }

        startInAnim();
    }

    private void doClickClubAction() {
        startInAnim();
    }

    private void doClickAddUsAction() {

    }

    private void doClickInterestAction() {

    }

    private void startInAnim()
    {
        rlFragmentView.startAnimation(inBounceAnim);
    }

    public void startOutAnim()
    {
        rlFragmentView.startAnimation(outBounceAnim);

    }
}
