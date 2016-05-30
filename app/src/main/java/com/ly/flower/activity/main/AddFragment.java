package com.ly.flower.activity.main;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ly.common.utils.Common;
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

    private RelativeLayout layoutClub, layoutAddUs, layoutInterestCard;//, layoutContent

    public Animation inBounceAnim, outBounceAnim;

    public AddFragment(MainActivity instance, LayoutInflater inflater) {
        mInstance = instance;
        this.inflater = inflater;
        initView();
    }

    public void initView() {
        rlFragmentView = (RelativeLayout) mInstance.findViewById(R.id.rl_add_fragment);
        layoutClub = (RelativeLayout) mInstance.findViewById(R.id.rl_club);
        layoutAddUs = (RelativeLayout) mInstance.findViewById(R.id.rl_add_us);
        layoutInterestCard = (RelativeLayout) mInstance.findViewById(R.id.rl_interest_card);

        layoutClub.setOnClickListener(this);
        layoutAddUs.setOnClickListener(this);
        layoutInterestCard.setOnClickListener(this);
        rlFragmentView.setOnClickListener(this);

//        inBounceAnim = new TranslateAnimation(0, 0, Common.DEVICE_SCREEN_HEIGHT, 0);
//        inBounceAnim.setDuration(800);
//        inBounceAnim.setInterpolator(new BounceInterpolator());

        inBounceAnim = AnimationUtils.loadAnimation(mInstance, R.anim.in_bounce);
        outBounceAnim = AnimationUtils.loadAnimation(mInstance, R.anim.out_bounce);

        inBounceAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                layoutClub.setVisibility(View.VISIBLE);
//                layoutInterestCard.setVisibility(View.VISIBLE);
//                layoutAddUs.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                layoutClub.setVisibility(View.VISIBLE);
//                layoutInterestCard.setVisibility(View.VISIBLE);
//                layoutAddUs.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        outBounceAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBackground(Bitmap bitmap)
    {
        rlFragmentView.setBackground(new BitmapDrawable(bitmap));
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
        layoutClub.startAnimation(inBounceAnim);
        layoutAddUs.startAnimation(inBounceAnim);
        layoutInterestCard.startAnimation(inBounceAnim);
//        rlFragmentView.startAnimation(inBounceAnim);
    }

    public void startOutAnim()
    {
        layoutClub.startAnimation(outBounceAnim);
        layoutAddUs.startAnimation(outBounceAnim);
        layoutInterestCard.startAnimation(outBounceAnim);
//        rlFragmentView.startAnimation(outBounceAnim);

    }
}
