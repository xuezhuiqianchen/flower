package com.ly.flower.base;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ly.common.utils.Common;
import com.ly.common.utils.UMSharePlatformUtil;
import com.ly.flower.R;
import com.ly.flower.component.ColoredSnackbar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.message.PushAgent;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by admin on 2016/3/17.
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener{
    private int baseLayoutId = R.layout.activity_base, contentLayoutId;

    private ImageView ivLeft, ivRight;
    private TextView tvTitle, tvRight, tvLeft;
    private RelativeLayout layoutTitle, layoutRoot;

    private SweetAlertDialog progressBar;

    public CoordinatorLayout clSnackContainer;
    public UMSharePlatformUtil umSharePlatformUtil;
    public ImageLoader imageLoader;
    public DisplayImageOptions imageOptions, portraitOptions;

    public abstract void init();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        imageOptions = new DisplayImageOptions.Builder()
                 .showImageOnLoading(R.drawable.default_image) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_image)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_image) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                 .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                // .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                // .decodingOptions(BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                .delayBeforeLoading(0)// int delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                //.displayer(new RoundedBitmapDisplayer(0))// 不推荐用！！！！是否设置为圆角，弧度为多少
                //.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间，可能会出现闪动
                .build();

        portraitOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_portrait) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_portrait)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_portrait) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                // .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                // .decodingOptions(BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                .delayBeforeLoading(0)// int delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                //.displayer(new RoundedBitmapDisplayer(0))// 不推荐用！！！！是否设置为圆角，弧度为多少
                //.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间，可能会出现闪动
                .build();
        umSharePlatformUtil = new UMSharePlatformUtil(this, Common.UM_APP_KET,
                Common.WX_APP_ID, Common.WX_APP_KEY, Common.QQ_APP_ID, Common.QQ_APP_KEY);
        init();
    }

    public void setLayoutRootId(int layoutId)
    {
        baseLayoutId = layoutId;
    }

    public void setView(int layoutId){
        this.contentLayoutId = layoutId;
        setView();
    }

    private void setView()
    {
        setContentView(baseLayoutId);
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_base_layout);
        View view = LayoutInflater.from(this)
                .inflate(contentLayoutId, null);
        layout.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        initTitleLayout();
        clSnackContainer = (CoordinatorLayout) this.findViewById(R.id.container);
    }

    private void initTitleLayout()
    {
        layoutTitle = (RelativeLayout) findViewById(R.id.layout_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivRight = (ImageView) findViewById(R.id.iv_right);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        layoutRoot = (RelativeLayout) findViewById(R.id.layout_root);

        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                doClickLeftAction();
                break;
            case R.id.iv_right:
                doClickRightAction();
                break;
            case R.id.tv_left:
                doClickLeftTextAction();
                break;
            case R.id.tv_right:
                doClickRightTextAction();
                break;
            default:
                break;
        }
    }

    public void doClickRightAction() {
    }

    public void doClickLeftAction() {
    }

    public void doClickLeftTextAction() {
    }

    public void doClickRightTextAction() {
    }

    public void setBackgroundResource(int rid){
        layoutRoot.setBackgroundResource(rid);
    }

    public void hideTitleLayout() {
        layoutTitle.setVisibility(View.GONE);
    }

    public void displayTitleLayout() {
        layoutTitle.setVisibility(View.VISIBLE);
    }

    public void setTitleLayoutBkColor(int rid){
        layoutTitle.setBackgroundColor(getResources().getColor(rid));
    }

    public void setTitle(int rid)
    {
        tvTitle.setText(getString(rid));
    }

    public void setTitle(String title)
    {
        tvTitle.setText(title);
    }

    public void setLeftActionVisiable()
    {
        ivLeft.setVisibility(View.VISIBLE);
    }

    public void setRightActionVisiable()
    {
        ivRight.setVisibility(View.VISIBLE);
    }

    public void setRightAction(int rid)
    {
        ivRight.setImageResource(rid);
    }

    public void setLeftText(int rid)
    {
        tvLeft.setText(getString(rid));
    }

    public void setRightText(int rid)
    {
        tvRight.setText(getString(rid));
    }

    public void showAlertMessage(String message)
    {
        Snackbar snackbar = Snackbar.make(clSnackContainer, message, Snackbar.LENGTH_SHORT);
        ColoredSnackbar.alert(snackbar).show();
    }

    public void showWarningtMessage(String message)
    {
        Snackbar snackbar = Snackbar.make(clSnackContainer, message, Snackbar.LENGTH_SHORT);
        ColoredSnackbar.warning(snackbar).show();
    }

    public void showInfoMessage(String message)
    {
        Snackbar snackbar = Snackbar.make(clSnackContainer, message, Snackbar.LENGTH_SHORT);
        ColoredSnackbar.info(snackbar).show();
    }

    public void showAlertMessage(int id)
    {
        Snackbar snackbar = Snackbar.make(clSnackContainer, getString(id), Snackbar.LENGTH_SHORT);
        ColoredSnackbar.alert(snackbar).show();
    }

    public void showWarningtMessage(int id)
    {
        Snackbar snackbar = Snackbar.make(clSnackContainer, getString(id), Snackbar.LENGTH_SHORT);
        ColoredSnackbar.warning(snackbar).show();
    }

    public void showInfoMessage(int id)
    {
        Snackbar snackbar = Snackbar.make(clSnackContainer, getString(id), Snackbar.LENGTH_SHORT);
        ColoredSnackbar.info(snackbar).show();
    }

    /**
     * 开启进度条
     *
     * @param message
     */
    public void showProgressBar(CharSequence message) {
        if (message != null) {
        } else {
            message = "";
        }
        if (progressBar == null)
        {
            int color = getResources().getColor(R.color.theme);
            progressBar = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            progressBar.getProgressHelper().setBarColor(color);
            progressBar.setCancelable(true);
        }
        progressBar.setTitleText(message.toString());
        progressBar.show();
    }

    /**
     * 开启进度条
     *
     * @param
     */
    public void showProgressBar(int id) {
        String message = getString(id);
        if (message != null) {
        } else {
            message = "";
        }
        if (progressBar == null)
        {
            int color = getResources().getColor(R.color.theme);
            progressBar = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            progressBar.getProgressHelper().setBarColor(color);
            progressBar.setCancelable(true);
        }
        progressBar.setTitleText(message.toString());
        progressBar.show();
    }

    /**
     * 关闭进度条
     */
    public void dismissProgressBar() {
        if (progressBar != null) {
            progressBar.dismiss();
        }
    }

    public void gotoActivity(Class<?> cls)
    {
        Intent intent = new Intent(this, cls);
        this.startActivity(intent);
    }

    public void gotoActivity(Class<?> cls, String data)
    {
        Intent intent = new Intent(this, cls);
        intent.putExtra("data", data);
        this.startActivity(intent);
    }

    public void gotoActivityForResult(Class<?> cls, int requestCode)
    {
        Intent intent = new Intent(this, cls);
        this.startActivityForResult(intent, requestCode);
    }

    public void gotoActivityForResult(Class<?> cls, String data, int requestCode)
    {
        Intent intent = new Intent(this, cls);
        intent.putExtra("data", data);
        this.startActivityForResult(intent, requestCode);
    }

    public void gotoActivityAndFinish(Class<?> cls)
    {
        Intent intent = new Intent(this, cls);
        this.startActivity(intent);
        this.finish();
    }
}
