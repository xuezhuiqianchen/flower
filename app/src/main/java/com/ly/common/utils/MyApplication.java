package com.ly.common.utils;
 
import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.message.ALIAS_TYPE;
import com.umeng.message.PushAgent;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		getDeviceSrceen();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());

		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		mPushAgent.setExclusiveAlias("flower", ALIAS_TYPE.SINA_WEIBO);


		 //创建默认的ImageLoader配置参数 		 
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
        		.Builder(this)
        		.memoryCache(new UsingFreqLimitedMemoryCache(50))
		        .diskCacheSize(50 * 1024 * 1024)
		        .diskCacheFileCount(200)
                .writeDebugLogs()
                .build();

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
	}

	private void getDeviceSrceen() {
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		Common.DEVICE_SCREEN_WIDTH = wm.getDefaultDisplay().getWidth();
		Common.DEVICE_SCREEN_HEIGHT = wm.getDefaultDisplay().getHeight();
	}
	/**
	 * 
	 * String imageUri = "http://site.com/image.png"; // 网络图片  
		String imageUri = "file://mnt/sdcard/image.png"; //SD卡图片  
		String imageUri = "content://media/external/audio/albumart/13"; // 媒体文件夹  
		String imageUri = "assets://image.png"; // assets  
		String imageUri = "drawable://" + R.drawable.image; //  drawable文件  
	 */
}
