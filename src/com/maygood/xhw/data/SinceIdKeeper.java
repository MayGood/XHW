package com.maygood.xhw.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SinceIdKeeper {
	
	public static void keep(Context context, String uid, String sinceId) {
		SharedPreferences pref = context.getSharedPreferences(uid, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("uid", uid);
		editor.putString("sinceId", sinceId);
		editor.commit();
	}
	
	public static void clear(Context context, String uid){
	    SharedPreferences pref = context.getSharedPreferences(uid, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}

	public static String read(Context context, String uid){
		String sinceId;
		SharedPreferences pref = context.getSharedPreferences(uid, Context.MODE_APPEND);
		sinceId = pref.getString("sinceId", "0");
		return sinceId;
	}
}
