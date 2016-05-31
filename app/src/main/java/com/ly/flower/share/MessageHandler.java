package com.ly.flower.share;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MessageHandler {
	
	/**
	 * main activity bar
	 */
	public static final int REQUEST_IMAGE									= 11;  
	public static final int REQUEST_FILE									= 12;
	public static final int REQUEST 										= 13;
	public static final int REQUEST_USER_FRAGMENT							= 14;
	public static final int REQUEST_MSG_FRAGMENT							= 15;
	public static final int REQUEST_MSG_FRESH_USERINFO						= 16;

	public static final int RESULT_ERROR									= 21;
	public static final int RESULT_OK										= 22;
	public static final int RESULT_FINISH									= 23;
	public static final int RESULT_GET_MSG									= 24;
	
	public static final int IMAGE_OK										= 101;
	public static final int REFRESH_VIDEO_VIEW								= 111;

	public static final int REQUEST_CODE_SELECT_PORTRAIT					= 30;
	public static final int REQUEST_CODE_SELECT_BKG							= 31;


	public static final int PRISE_OPERATION									= 41;
	
	
	public static final int FILE											= 61;
	public static final int IMAG											= 62;
	
	public static final int LIST_DIALOG_CHECK								= 101;

	public static void sendMessage(final Handler handler, int messageWhat)
	{
		Message msg = new Message();
		msg.what = messageWhat;
		handler.sendMessage(msg);
	}
	
	public static void sendMessage(final Handler handler, int messageWhat, String data)
	{
		Message msg = new Message();
		msg.what = messageWhat;
		Bundle bundle = new Bundle(); 
		bundle.putString("data", data); 
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	public static void sendMessage(final Handler handler, int messageWhat, byte[] data)
	{
		Message msg = new Message();
		msg.what = messageWhat;
		Bundle bundle = new Bundle();  
		bundle.putByteArray("data", data); 
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	public static void sendMessage(final Handler handler, int messageWhat, Bundle bundle)
	{
		Message msg = new Message();
		msg.what = messageWhat; 
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	

}
