package com.maygood.xhw.data;

import com.weibo.sdk.android.Oauth2AccessToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * �������ڱ���Oauth2AccessToken��sharepreference�����ṩ��ȡ����
 * @author xiaowei6@staff.sina.com.cn
 *
 */
public class AccessTokenKeeper {
	/**
	 * ����accesstoken��SharedPreferences
	 * @param context Activity �����Ļ���
	 * @param token Oauth2AccessToken
	 */
	public static void keepAccessToken(Context context, Oauth2AccessToken token, String uid) {
		SharedPreferences pref = context.getSharedPreferences(uid, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("token", token.getToken());
		editor.putLong("expiresTime", token.getExpiresTime());
		editor.commit();
	}
	/**
	 * ���sharepreference
	 * @param context
	 */
	public static void clear(Context context, String uid){
	    SharedPreferences pref = context.getSharedPreferences(uid, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}

	/**
	 * ��SharedPreferences��ȡaccessstoken
	 * @param context
	 * @return Oauth2AccessToken
	 */
	public static Oauth2AccessToken readAccessToken(Context context, String uid){
		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(uid, Context.MODE_APPEND);
		token.setToken(pref.getString("token", ""));
		token.setExpiresTime(pref.getLong("expiresTime", 0));
		return token;
	}
}
