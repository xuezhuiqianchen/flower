package com.ly.flower.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import com.ly.flower.R;
import com.ly.flower.share.Player;

/**
 * Created by admin on 2016/3/29.
 */
public class MediaPlayerActivity extends Activity {
    private SurfaceView surfaceView;
    private Button btnPause, btnPlayUrl, btnStop;
    private SeekBar skbProgress;
    private Player player;




    private AudioManager mAudioManager;
    /** 最大声音 */
    private int mMaxVolume;
    /** 当前声音 */
    private int mVolume = -1;
    /** 当前亮度 */
    private float mBrightness = -1f;
    /** 当前缩放模式 */
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;
    private MediaController mMediaController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //定义全屏参数
//        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        //获得当前窗体对象
//        Window window = MediaPlayerActivity.this.getWindow();
//        //设置当前窗体为全屏显示
//        window.setFlags(flag, flag);
//        //完全全屏
//        toggleHideyBar();

        setContentView(R.layout.activity_media_player);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);

        btnPlayUrl = (Button) this.findViewById(R.id.btnPlayUrl);
        btnPlayUrl.setOnClickListener(new ClickEvent());

        btnPause = (Button) this.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new ClickEvent());

        btnStop = (Button) this.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new ClickEvent());

        skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        player = new Player(surfaceView, skbProgress);



    }

    class ClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (arg0 == btnPause) {
                player.pause();
            } else if (arg0 == btnPlayUrl) {
                String url="http://www.sunflowerslove.cn/FileService/FileLibrary/20160401/018f79f2-29b8-4ece-a79b-d0122fdbfaed.mp4";
                player.playUrl(url);
            } else if (arg0 == btnStop) {
                player.stop();
            }

        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            player.mediaPlayer.seekTo(progress);
        }
    }


    private void init()
    {
        String strUrl = "http://www.sunflowerslove.cn/FileService/FileLibrary/20160329/c2233150-40e7-4791-b2ae-78942f582f0d.mp4";
        Uri uri = Uri.parse(strUrl);
        VideoView videoView = (VideoView)this.findViewById(R.id.video_view);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.requestFocus();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (mGestureDetector.onTouchEvent(event))
//            return true;
//
//        // 处理手势结束
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_UP:
//                endGesture();
//                break;
//        }
//
//        return super.onTouchEvent(event);
//    }

    /** 手势结束 */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

//        /** 双击 */
//        @Override
//        public boolean onDoubleTap(MotionEvent e) {
//            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
//                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
//            else
//                mLayout++;
//            if (mVideoView != null)
//                mVideoView.setVideoLayout(mLayout, 0);
//            return true;
//        }

//        /** 滑动 */
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2,
//                                float distanceX, float distanceY) {
//            float mOldX = e1.getX(), mOldY = e1.getY();
//            int y = (int) e2.getRawY();
//            Display disp = getWindowManager().getDefaultDisplay();
//            int windowWidth = disp.getWidth();
//            int windowHeight = disp.getHeight();
//
//            if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
//                onVolumeSlide((mOldY - y) / windowHeight);
//            else if (mOldX < windowWidth / 5.0)// 左边滑动
//                onBrightnessSlide((mOldY - y) / windowHeight);
//
//            return super.onScroll(e1, e2, distanceX, distanceY);
//        }
    }

    /** 定时隐藏 */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

//    /**
//     * 滑动改变声音大小
//     *
//     * @param percent
//     */
//    private void onVolumeSlide(float percent) {
//        if (mVolume == -1) {
//            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//            if (mVolume < 0)
//                mVolume = 0;
//
//            // 显示
//            mOperationBg.setImageResource(R.drawable.volume_sel);
//            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
//        }
//
//        int index = (int) (percent * mMaxVolume) + mVolume;
//        if (index > mMaxVolume)
//            index = mMaxVolume;
//        else if (index < 0)
//            index = 0;
//
//        // 变更声音
//        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
//
//        // 变更进度条
//        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
//        lp.width = findViewById(R.id.operation_full).getLayoutParams().width
//                * index / mMaxVolume;
//        mOperationPercent.setLayoutParams(lp);
//    }

//    /**
//     * 滑动改变亮度
//     *
//     * @param percent
//     */
//    private void onBrightnessSlide(float percent) {
//        if (mBrightness < 0) {
//            mBrightness = getWindow().getAttributes().screenBrightness;
//            if (mBrightness <= 0.00f)
//                mBrightness = 0.50f;
//            if (mBrightness < 0.01f)
//                mBrightness = 0.01f;
//
//            // 显示
//            mOperationBg.setImageResource(R.drawable.temp);
//            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
//        }
//        WindowManager.LayoutParams lpa = getWindow().getAttributes();
//        lpa.screenBrightness = mBrightness + percent;
//        if (lpa.screenBrightness > 1.0f)
//            lpa.screenBrightness = 1.0f;
//        else if (lpa.screenBrightness < 0.01f)
//            lpa.screenBrightness = 0.01f;
//        getWindow().setAttributes(lpa);
//
//        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
//        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
//        mOperationPercent.setLayoutParams(lp);
//    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        if (mVideoView != null)
//            mVideoView.setVideoLayout(mLayout, 0);
//        super.onConfigurationChanged(newConfig);
//    }



    public void toggleHideyBar() {
        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
//            Log.i(TAG, "Turning immersive mode mode off. ");
        } else {
//            Log.i(TAG, "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)
    }
}
