package com.ly.flower.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient; 
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.ly.flower.base.DataStructure;

import cz.msebera.android.httpclient.entity.StringEntity;

public class AscynHttpUtil {
	private static AsyncHttpClient httpclient = new AsyncHttpClient();  
	private static AsyncHttpClient httpsclient = new AsyncHttpClient();

	public static final String VERSION_1 						= "1";
	public static final String VERSION_2 						= "1";
	public static final String SERVER_HOST						= "https://www.sunflowerslove.cn/api";
	public static final String BLS_HEAD							= "/bls/" + VERSION_1;
	public static final String DISCOVER_HEAD1					= "/discover/" + VERSION_1;
	public static final String DISCOVER_HEAD2					= "/discover/" + VERSION_2;
	public static final String MSG_HEAD							= "/msg/" + VERSION_1;
	public static final String MY_HEAD							= "/my/" + VERSION_1;
	public static final String SHARE_HEAD						= "/share";

	public static final String UPLOAD_FILE						= "https://www.sunflowerslove.cn" + "/FileService/api/file/1/uploadfile";
	public static final String UPLOAD_IMAGE						= "https://www.sunflowerslove.cn" + "/ImageService/api/image/1/uploadimage";

	public static final String URL_SHARE_CLUB					= SHARE_HEAD + "/shareclub?";
	public static final String URL_SHARE_HISTORY				= SHARE_HEAD + "/sharehistory?";
	public static final String URL_SHARE_TOPIC					= SHARE_HEAD + "/sharetopic?";

	public static final String URL_DISCOVER_GET_LIST			= DISCOVER_HEAD2 + "/get_list";
	public static final String URL_MSG_GET_NEW_MSG				= MSG_HEAD + "/get_new_msg";

	public static final String URL_GET_MY_TOPIC_LIST			= MY_HEAD + "/get_topic_list";
	public static final String URL_GET_MY_COMMENT_LIST			= MY_HEAD + "/get_comment_list";
	public static final String URL_DEL_MY_COMMENT				= MY_HEAD + "/del_comment";
	public static final String URL_UPLOAD_SUGGESTION			= MY_HEAD + "/upload_suggestion";

	public static final String URL_UPLOAD_TERM					= BLS_HEAD + "/upload_term";
	public static final String URL_GET_TERM						= BLS_HEAD + "/get_terms";
	public static final String URL_LOGIN_BY_EXTERN				= BLS_HEAD + "/login_by_thirdpart";
	public static final String URL_USER_OPERATION				= BLS_HEAD + "/user_operation";
	public static final String URL_CLUB_SELECT					= BLS_HEAD + "/club/select_clubs";
	public static final String URL_CLUB_ADD						= BLS_HEAD + "/club/add_club";
	public static final String URL_CLUB_EDIT					= BLS_HEAD + "/club/edit_club";
	public static final String URL_CLUB_GET_HISTORY				= BLS_HEAD + "/club/get_history";
	public static final String URL_CLUB_ADD_HISTORY				= BLS_HEAD + "/club/add_history";
	public static final String URL_CLUB_GET_HISTORY_COMMENT		= BLS_HEAD + "/club/get_history_comment";
	public static final String URL_CLUB_ADD_HISTORY_COMMENT		= BLS_HEAD + "/club/add_history_comment";
	public static final String URL_CLUB_DEL_HISTORY_COMMENT		= BLS_HEAD + "/club/del_history_comment";
	public static final String URL_CLUB_DEL						= BLS_HEAD + "/club/del_club";
	public static final String URL_CLUB_DEL_HISTORY				= BLS_HEAD + "/club/del_history";
	public static final String URL_CLUB_GET_MEMBER				= BLS_HEAD + "/club/get_members";
	public static final String URL_CLUB_ADD_MEMBER				= BLS_HEAD + "/club/add_members";
	public static final String URL_CLUB_EDIT_MEMBER				= BLS_HEAD + "/club/edit_members";
	public static final String URL_CLUB_DEL_MEMBER				= BLS_HEAD + "/club/del_members";
	public static final String URL_CLUB_GET_TOPIC_LIST			= BLS_HEAD + "/club/get_topic_list";
	public static final String URL_CLUB_ADD_TOPIC				= BLS_HEAD + "/club/add_topic";
	public static final String URL_CLUB_DEL_TOPIC				= BLS_HEAD + "/club/del_topic";
	public static final String URL_CLUB_ADD_TOPIC_COMMENT		= BLS_HEAD + "/club/add_topic_comment";
	public static final String URL_CLUB_DEL_TOPIC_COMMENT		= BLS_HEAD + "/club/del_topic_comment";
	public static final String URL_CLUB_GET_TOPIC_COMMENT		= BLS_HEAD + "/club/get_topic_comments";
	public static final String URL_CLUB_ADD_CONTENT				= BLS_HEAD + "/bilingual/add_contents";
	public static final String URL_CLUB_GET_CONTENT				= BLS_HEAD + "/bilingual/get_contents";
	public static final String URL_CLUB_DEL_CONTENT				= BLS_HEAD + "/bilingual/del_contents";
	public static final String URL_EDIT_USER_INFO				= BLS_HEAD + "/edit_user_info";





	public static void post(Context context, String url, 
			String content, ResponseHandlerInterface responseHandler)
	{
		AsyncHttpClient httpClient = getHttpClientByUrl(url);
		try { 
			httpClient.post(context, 
					 url,
					new StringEntity(content, "UTF-8"),
					 "application/x-www-form-urlencoded", 
					 responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	
	public static void asyncUploadFile(Context context, String url, String path, ResponseHandlerInterface responseHandler)
	{
//		File file = new File(path);
//		AsyncHttpClient httpClient = getHttpClientByUrl(url);
//		httpClient.addHeader("FileName", path);
//		httpClient.addHeader("Md5", BaseFunction.getInstance().getFileMD5(new File(path)));
//		RequestParams params = new RequestParams();
//		if (file.exists() && file.length() > 0) {
//			try {
//				params.put("profile_picture", file);
//				params.put("Module", "anyis");
//
//				httpClient.post(url, params, responseHandler);
//
//			} catch(FileNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	/**
	 * 
	 * @param context
	 * @param path
	 * @param responseHandler 
	 */
	public static void uploadImage(Context context, String path, ResponseHandlerInterface responseHandler)
	{
		try {
			String url = UPLOAD_IMAGE;
			File file = new File(path); 
			AsyncHttpClient httpClient = getHttpClientByUrl(url);
//			httpClient.addHeader("uid", DataStructure.uid);
//			httpClient.addHeader("passwd", DataStructure.passwd);
			RequestParams params = new RequestParams();
			if (file.exists() && file.length() > 0) {
				params.put("uid", "6");
				params.put("passwd", "8ddcff3a80f4189ca1c9d4d902c3c909");
				params.put("profile_picture", file);
				httpClient.post(url, params, responseHandler);
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}   
	}

	/**
	 *
	 * @param context
	 * @param responseHandler
	 */
	public static void uploadMutilImage(Context context, ArrayList<String> pathList,
										ResponseHandlerInterface responseHandler)
	{
		try {
			String url = UPLOAD_IMAGE;
			AsyncHttpClient httpClient = getHttpClientByUrl(url);
//			httpClient.addHeader("uid", DataStructure.uid);
//			httpClient.addHeader("passwd", DataStructure.passwd);

			for (int i = 0; i < pathList.size(); i++)
			{
				File file = new File(pathList.get(i));
				RequestParams params = new RequestParams();
				if (file.exists() && file.length() > 0) {
					params.put("uid", DataStructure.uid);
					params.put("passwd", DataStructure.passwd);
					params.put("profile_picture", file);
					httpClient.post(url, params, responseHandler);
				}
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void asyncDownFile(final Context context, final String url, 
			final String path, final String id, final BinaryHttpResponseHandler handler)
	{    
		// 获取二进制数据如图片和其他文件
		AsyncHttpClient httpClient = getHttpClientByUrl(url);
		httpClient.get(url, handler);
	}
	
	public static void asyncUploadFiles(Context context, String url, String[] path, ResponseHandlerInterface responseHandler)
	{
		//TODO
		for (int i = 0; i < path.length; i++) {
			
		}
	}
	

	private static AsyncHttpClient getHttpClientByUrl(String url)
	{
		if (url.toLowerCase().startsWith("https://")) {
			cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory sslSocketFactory = MySSLSocketFactory.getFixedSocketFactory();
			httpsclient.setSSLSocketFactory(sslSocketFactory);
			return httpsclient;
		}else{
			return httpclient;
		} 	
	}
	 
	
	public static String getAbsoluteUrlString(Context context, String type)
	{
		return getBaseUrlString(context) + type;
	}
	
	public static String getBaseUrlString(Context context)
	{
		return SERVER_HOST ;
	}
}
