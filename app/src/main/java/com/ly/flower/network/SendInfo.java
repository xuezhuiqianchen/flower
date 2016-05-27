package com.ly.flower.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;

public class SendInfo {
	/**
	 * 获取发现内容列表
	 {
	 "type"=""  0推荐 1最新
	 "uid": "用户id",
	 "passwd": "密码",
	 "count": "取的数量"
	 "page": ""
	 "topic": "内容字典，最后一个结果的json对象，分页时用，page > 0时才会传"
	 }
	 * @return
	 */
	public static String getListOfDiscoverSendInfo(Context context, String count, String page,
												   String type, JSONObject topic)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("type", type);
			object.put("page", page);
			object.put("topic", topic);
			object.put("count", count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 * 用户id&用户密码
	 * @param context
	 	{
		"uid": "用户id",
		"passwd": "密码",
		}
	 * @return
	 */
	public static String getUserUnitSendInfo(Context context)
	{
		return getUserUnit(context).toString();
	}

	/**
	 *提交课程接口
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "lid": "语言id：0 英，1日，2韩",
	 "content": "标题",
	 "url": "图片地址",
	 "url_h": "图片高",
	 "url_w": "图片宽",
	 "open": "用户是否允许展示",
	 "cards": [
		 {
		 "content": "标题",
		 "ori": "释义",
		 "url": "图片地址",
		 "url_h": "图片高",
		 "url_w": "图片宽"
		 }
	 	]
	 }
	 * @return
	 */
	public static String getUploadTermSendInfo(Context context, String lid, String content,
											   String url, String url_h, String url_w, String open,
											   JSONArray cards)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("lid", lid);
			object.put("content", content);
			object.put("url", url);
			object.put("url_h", url_h);
			object.put("url_w", url_w);
			object.put("open", open);
			object.put("cards", cards);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *获取课程
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "tid": "拉历史数据的节点ID； 如果为 0，则从最新的当中取",
	 "count": "取的数量"
	 }
	 * @return
	 */
	public static String getTermSendInfo(Context context, String tid, String count)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("tid", tid);
			object.put("count", count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *使用第三方登录
	 {
	 "nickname": "昵称",
	 "gender": "1男,0女",
	 "avatar": "头像",
	 "third_uid": "第三方ID",
	 "ltype": "0 we chat  1 qq",
	 "hometown": "故乡”",
	 "bk_url": "个人背景url"
	 }
	 * @return
	 */
	public static String getLoginByExternalSendInfo(String nickname, String gender, String avatar,
													String third_uid, String ltype, String hometown,
													String bk_url)
	{
		JSONObject object = new JSONObject();
		try {
			object.put("nickname", nickname);
			object.put("gender", gender);
			object.put("avatar", avatar);
			object.put("third_uid", third_uid);
			object.put("ltype", ltype);
			object.put("hometown", hometown);
			object.put("bk_url", bk_url);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 * 用户操作
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "otype": "操作类型: 0赞",
	 "osubtype": "操作子类型 1 赞，0取消赞",
	 "ctype": "内容类型 0term,1俱乐部足迹，2话题",
	 "cid": "内容ID"
	 }
	 * @return
	 */
	public static String getUserOperationSendInfo(Context context, String otype, String osubtype,
												  String ctype, String cid)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("otype", otype);
			object.put("osubtype", osubtype);
			object.put("ctype", ctype);
			object.put("cid", cid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 * 查询俱乐部信息
	 {
	 "stype": "0全搜索，根据关注人数;1用户关注的club",
	 "uid": "",
	 "passwd": "",
	 "cid": "拉历史数据的节点ID； 如果为 0，则从最新的当中取",
	 "count": "取的数量"
	 }
	 * @return
	 */
	public static String getSelectClubSendInfo(Context context, String stype, String cid,
											   String count)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("stype", stype);
			object.put("cid", cid);
			object.put("count", count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *新建/编辑俱乐部
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "cname": "俱乐部名称",
	 "url_logo": "俱乐部logo",
	 "url_bk": "俱乐部背景图",
	 "url_bk_h":"背景图高",
	 "url_bk_w":"背景图宽"
	 }
	 * @return
	 */
	public static String getAddorEditClubSendInfo(Context context, String cname, String url_logo,
											String url_bk, String url_bk_h, String url_bk_w)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("cname", cname);
			object.put("url_logo", url_logo);
			object.put("url_bk", url_bk);
			object.put("url_bk_h", url_bk_h);
			object.put("url_bk_w", url_bk_w);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 * 获取足迹内容
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "cid": "俱乐部id",
	 "hid": "拉历史数据的节点ID； 如果为 0，则从最新的当中取",
	 "count": "取的数量"
	 }
	 * @return
	 */
	public static String getHistoryOfClubSendInfo(Context context, String cid, String hid,
												  String count)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("hid", hid);
			object.put("cid", cid);
			object.put("count", count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *新增足迹内容
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "cid":"俱乐部id",
	 "time": "足迹时间",
	 "title": "标题",
	 "place": "地点",
	 "img": [
		 {
		 "url": "",
		 "width": "",
		 "height": ""
		 }
		 ]
	 }
	 * @return
	 */
	public static String getAddHistoryOfClubSendInfo(Context context, String time, String cid,
													 String title, String place, JSONArray img)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("time", time);
			object.put("cid", cid);
			object.put("title", title);
			object.put("place", place);
			object.put("img", img);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 * 获取足迹评论
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "hid": "足迹id"
	 }
	 * @return
	 */
	public static String getHistoryCommentOfClubSendInfo(Context context, String hid)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("hid", hid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 * 新增足迹评论
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "hid": "足迹id",
	 "content": "内容"
	 }
	 * @return
	 */
	public static String getAddHistoryCommentOfClubSendInfo(Context context, String hid,
															String content)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("hid", hid);
			object.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *删除足迹评论/俱乐部/足迹
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "items":
		 [
		 {
		 	"id":"id"
		 }
		 ]
	 }
	 * @return
	 */
	public static String getDelOfClubSendInfo(Context context, JSONArray items)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("items", items);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *获取会员信息
	 *{
	 "uid": "用户id",
	 "passwd": "密码",
	 "cid":"俱乐部id"
	 }
	 * @return
	 */
	public static String getMemberOfClubSendInfo(Context context, String cid)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("cid", cid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *添加会员
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "cid": "俱乐部id",
	 "members": [
		 {
		 "uid": "花朵系统uid，0为未绑定",
		 "mname": "会员名称",
		 "mavatar  ": "会员头像",
		 "mlevel": "会员身份，字符串类型，例如：队长",
		 "join_time": "加入时间"
		 }
		 ]
	 }
	 * @return
	 */
	public static String getAddMemberClubSendInfo(Context context, JSONArray members, String cid)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("members", members);
			object.put("cid", cid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *编辑会员信息
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "members": [
		 {
		 "uid": "花朵系统uid，0为未绑定",
		 "mid":"会员id",
		 "mname": "会员名称",
		 "mavatar  ": "会员头像",
		 "mlevel": "会员身份，字符串类型，例如：队长",
		 "join_time": "加入时间"
		 }
	 ]
	 }
	 * @return
	 */
	public static String getEditMemberOfClubSendInfo(Context context, JSONArray members)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("members", members);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *删除会员
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "members": [
		 {
		 "mid": "会员id"
		 }
		 ]
	 }
	 * @return
	 */
	public static String getDelMemberOfClubSendInfo(Context context, JSONArray members)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("members", members);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *获取足迹内容
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "cid": "俱乐部id",
	 "ttype":"话题类型  0普通  1置顶"
	 "tid": "拉历史数据的节点ID； 如果为 0，则从最新的当中取",
	 "count": "取的数量"
	 }
	 * @return
	 */
	public static String getTopicListClubSendInfo(Context context, String cid, String ttype,
												  String tid, String count)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("cid", cid);
			object.put("ttype", ttype);
			object.put("tid", tid);
			object.put("count", count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *新增话题
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "type"：0,1 置顶
	 "cid":"俱乐部id",
	 "title": "标题",
	 "sub_title": "子标题",
	 "img": [
		 {
		 "url": "",
		 "width": "",
		 "height": ""
		 }
	 	]
	 }
	 * @return
	 */
	public static String getAddTopicClubSendInfo(Context context, String type, String cid,
												 String title, String sub_title, JSONArray img)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("type", type);
			object.put("cid", cid);
			object.put("title", title);
			object.put("sub_title", sub_title);
			object.put("img", img);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *删除话题
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "items":
		 [
		 {
		 "tid":"id"
		 }
		 ]
	 }
	 * @return
	 */
	public static String getDelTopicOfClubSendInfo(Context context, JSONArray items)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("items", items);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 * 获取话题评论
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "tid": "id"
	 }
	 * @return
	 */
	public static String getTopicCommentOfClubSendInfo(Context context, String tid)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("tid", tid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 * 新增话题评论
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "tid": "足迹id",
	 "content": "内容"
	 }
	 * @return
	 */
	public static String getAddTopicCommentOfClubSendInfo(Context context, String tid,
															String content)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("tid", tid);
			object.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}


	/**
	 * 新增双语汇内容
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "content": [
		 {
		 "url_img": "图片url",
		 "title": "原文标题",
		 "title_trans": "翻译后标题",
		 "url": "内容url"
		 }
		 ]
	 }
	 * @return
	 */
	public static String getAddContentOfBbilingualSendInfo(Context context, JSONArray content)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *获取双语汇内容
	 {
	 "uid": "用户id",
	 "passwd": "密码",

	 }
	 * @return
	 */
	public static String getContentOfBbilingualSendInfo(Context context)
	{
		JSONObject object = getUserUnit(context);
		return object.toString();
	}

	/**
	 * 删除双语汇
	 {
	 "uid": "用户id",
	 "passwd": "密码",
	 "data":
	 [
	 {
	 "bid":"1"
	 }
	 ]
	 }
	 * @return
	 */
	public static String getDelContentOfBbilingualSendInfo(Context context, JSONArray data)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("data", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	private static JSONObject getUserUnit(Context context)
	{
		JSONObject object = new JSONObject();
		try {
			String passwd = "888888";
//			object.put("uid", DataStructure.uid);
//			object.put("passwd", DataStructure.passwd);
			object.put("uid", "6");
			object.put("passwd", "8ddcff3a80f4189ca1c9d4d902c3c909");//BaseFunction.stringToMD5(passwd)
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	/**
	 *
	 * @param context
	 * @param suggestion
	 * @return
	 */
	public static String getUploadSuggestionSendInfo(Context context, String suggestion)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("suggestion", suggestion);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 *
	 * @param context
	 * @param page
	 * @return
	 */
	public static String getMyCommentsSendInfo(Context context, String page)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("page", page);
			object.put("count", 20);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	public static String getMyTopicsSendInfo(Context context, String tid)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("tid", tid);
			object.put("count", 20);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	public static String getUserInfoSendInfo(Context context)
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("nickname", "");
			object.put("avatar", "");
			object.put("signature", "");
			object.put("bk_url", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	public static String getEditUserInfoSendInfo(Context context, String nickname, String avatar,
												 String signature, String bk_url )
	{
		JSONObject object = getUserUnit(context);
		try {
			object.put("nickname", nickname);
			object.put("avatar", avatar);
			object.put("signature", signature);
			object.put("bk_url", bk_url);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}
}
