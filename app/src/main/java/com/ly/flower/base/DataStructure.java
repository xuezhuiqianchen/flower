package com.ly.flower.base;

import java.io.File; 

import org.json.JSONObject;

import android.app.Activity;
import android.os.Environment; 
import android.os.Handler;

public class DataStructure {
	public static String[] strTime = null;
	public static boolean login = false;

	public static String uid = "6";
	public static String passwd = "8ddcff3a80f4189ca1c9d4d902c3c909";
	public static String number = "";
	public static String gender = "";
	public static String hometown = "";
	public static String nickname = "";
	public static String portrait = "";



//	DataStructure.passwd = object.getString("passwd");
//	DataStructure.number = object.getString("number");
//	DataStructure.nickname = object.getString("nickname");
//	DataStructure.gender = object.getString("gender");
//	DataStructure.portrait = object.getString("avatar");
//	DataStructure.hometown = object.getString("hometown");

	//other
	public static final int IMAGE_OK										= 51;

	

	/**
	 * recv error
	 */
	public static String ERROR_OK											= "200";
	
	

 
	
	
	//file dir
	public static File DIRACTOR_SD_CARD = Environment.getExternalStorageDirectory();
	public static String DIRACTOR_APP = DIRACTOR_SD_CARD.getPath() + "/flower/";
	
	public static String DIRACTOR_FILES = DIRACTOR_APP + "files/";
	public static String DIRACTOR_TEMP = DIRACTOR_APP +"temp/";
	public static String DIRACTOR_CACHE = DIRACTOR_APP + "cache/";
	public static String DIRACTOR_IMAGE = DIRACTOR_APP + "cache/images/";	 
}



