package com.maygood.xhw.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsKeeper {
	
	public static void keep(Context context, String key, Boolean value) {
		SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("key", key);
		editor.putBoolean("value", value);
		editor.commit();
	}
	
	public static void clear(Context context, String key){
	    SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}

	public static Boolean read(Context context, String key){
		Boolean value;
		SharedPreferences pref = context.getSharedPreferences(key, Context.MODE_APPEND);
		value = pref.getBoolean("value", true);
		return value;
	}
}
