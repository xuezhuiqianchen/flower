package com.ly.flower.base;


import java.io.File;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText; 
import android.widget.Toast;

import com.ly.flower.component.ColoredSnackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseFunction {


	public static boolean verifyResult(String data, View view)
	{
		try {
			JSONObject object = new JSONObject(data);
			String error = object.getString("error");
			if (error.equals(DataStructure.ERROR_OK)) {
				return true;
			}else {
				String description = object.getString("description");
				Snackbar snackbar = Snackbar.make(view, description, Snackbar.LENGTH_SHORT);
				ColoredSnackbar.alert(snackbar).show();
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	 
	
	private final String[] SIZE = new String[]{"B", "KB", "M", "G"};
	

//
//	public void selectImage(Activity instance, int request)
//	{
//		Intent intent = new Intent(instance, MultiImageSelectorActivity.class);
//		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
//		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
//		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
//		instance.startActivityForResult(intent, request);
//	}
//
//
//	public DisplayImageOptions getPortraitOption()
//	{
//		if (portraitOptions == null) {
//			portraitOptions = new DisplayImageOptions.Builder()
//			.showImageOnLoading(R.drawable.portrait_default)
//			.showImageOnFail(R.drawable.portrait_default)
//			.cacheInMemory(true)
//			.cacheOnDisk(true)
//			.bitmapConfig(Bitmap.Config.RGB_565)
//			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
//			.build();
//		}
//		return portraitOptions;
//	}
	

	public double getFileLength(String path)
	{
		double len = 0.00;
		File file = new File(path);
		if (file != null && file.exists()) {
			len = file.length();
		}
		return len;
	}
	
	public String getFileSize(String path)
	{
		DecimalFormat df = new DecimalFormat("###.00"); 
		String size = "";
		double len = 0.00;
		File file = new File(path);
		if (file != null && file.exists()) {
			len = file.length();
			int times = 0;
			while (len > 1024.00) {				
				len /= 1024.00; 
				times++;
			}
			size = String.valueOf(df.format(len)) + SIZE[times];
		} 
		return size;
	}
	
	public String getFileSize(double len)
	{
		DecimalFormat df = new DecimalFormat("###.00"); 
		String size = "";   
		int times = 0;
		while (len > 1024.00) {				
			len /= 1024.00; 
			times++;
		}
		size = String.valueOf(df.format(len)) + SIZE[times];
		return size;
	}

	

	


 
	



	

    

	 

	

	
	/**
	 *双击退出应用
	 */
	private static Boolean isExit = false;

	public void exitBy2Click(Activity instance) {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;  
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;  
				}
			}, 2000);  

		} else {
			instance.finish();
			System.exit(0);
		}
	}
	
	public void backHomeBy2Click(Activity instance) {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;  
			toastString(instance, "再点击一次退出");
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;  
				}
			}, 2000);  

		} else {
			Intent home = new Intent(Intent.ACTION_MAIN);  
			home.addCategory(Intent.CATEGORY_HOME); 
			instance.startActivity(home); 

		}
	}
	
	


    
    public OnFocusChangeListener onFocusAutoClearHintListener = new OnFocusChangeListener() {
    	@Override
    	public void onFocusChange(View v, boolean hasFocus) {
    		EditText textView = (EditText) v;
    		String hint;
    		if (hasFocus) {
    			hint = textView.getHint().toString();
    			textView.setTag(hint);
    			textView.setHint("");
    		} else {
    			hint = textView.getTag().toString();
    			textView.setHint(hint);
    		}
    	}
    }; 
    
    
	public void toastString(Activity instance, String str)
	{
		Toast.makeText(instance, str, Toast.LENGTH_LONG).show();
	}
 

	
	/**
	 * 线程不可用
	 * @param view
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param duration
	 */
	public void addScaleAnimation(View view, float startX, float startY, float endX, float endY, int duration)
	{
		Animation scaleAnimation = new ScaleAnimation(startX, endX, startY, endY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//new ScaleAnimation(0.1f, 1.0f,0.1f,1.0f);
		scaleAnimation.setDuration(duration);
		view.startAnimation(scaleAnimation);
	}
	
	public void addScaleAnimation(View view, float start, float end, int duration)
	{
		Animation scaleAnimation = new ScaleAnimation(start, end, start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//new ScaleAnimation(0.1f, 1.0f,0.1f,1.0f);
		scaleAnimation.setDuration(duration);
		view.startAnimation(scaleAnimation);
	}
	

	

	
	
//	private void showReloginDialog(final Activity act)
//	{
//		if (alertDialog != null && alertDialog.isShowing()) {
//			return;
//		}
//		
//		if (alertDialog == null) {
//			alertDialog = new AlertDialog.Builder(act).create();  
//			alertDialog.setCanceledOnTouchOutside(false); 
//		} 
//		alertDialog.show();  
//		Window window = alertDialog.getWindow();  
//		window.setContentView(R.layout.activity_relogin);
//		window.setGravity(Gravity.CENTER);  
//		
//      WindowManager m = act.getWindowManager();
//      Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//      WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
//      p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
//      p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.65
//      window.setAttributes(p);
//		  
//		TextView tvLoginname = (TextView) window.findViewById(R.id.tv_loginname); 		 
//		final EditText etPasswd = (EditText) window.findViewById(R.id.et_psw); 
//		TextView tvLogin = (TextView) window.findViewById(R.id.tv_login); 
//		TextView tvLogout = (TextView) window.findViewById(R.id.tv_logout); 		
//		
//		tvLoginname.setText(DataStructure.loginname); 
//		tvLogin.setOnClickListener(new OnClickListener() { 
//			@Override
//			public void onClick(View arg0) { 
//				if (etPasswd.getText().toString().equals("")) {
//					BaseFunction.getInstance().showSweetMessageDialog(act, "请输入密码");
//					return;
//				}
//				new ApiHttpUtil().api(act, 
//						SendInfo.getLoginSendInfo(act, DataStructure.loginname, etPasswd.getText().toString()),
//        				DataStructure.mCurrentSessionHandler, 
//        				MessageHander.API_LOGIN, 
//        				true); 
//				alertDialog.dismiss();
//				alertDialog.cancel();
//			}
//		});
//		
//		tvLogout.setOnClickListener(new OnClickListener() { 
//			@Override
//			public void onClick(View arg0) { 
//				logoutForRelogin(act);
//				alertDialog.dismiss();
//				alertDialog.cancel();
//			}
//		});  
//	}  





	public void install(Context context)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		String url = Environment.getExternalStorageDirectory()
				+ "/anyis/3DA482D2A10944AC895C3D3FB1E98A87.apk";
		intent.setDataAndType(Uri.fromFile(new File(url)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	

	

}
