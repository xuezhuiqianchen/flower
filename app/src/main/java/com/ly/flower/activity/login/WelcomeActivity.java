package com.ly.flower.activity.login;


import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.activity.main.MainActivity;

/**
 * Created by admin on 2016/3/30.
 */
public class WelcomeActivity extends BaseActivity {

    @Override
    public void init() {
        setView(R.layout.activity_welcome);
        thrJump();
    }

    private void thrJump()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    gotoActivityAndFinish(MainActivity.class);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
