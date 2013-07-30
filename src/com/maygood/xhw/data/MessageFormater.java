package com.maygood.xhw.data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.maygood.xhw.MainActivity;
import com.maygood.xhw.net.HttpsUtils;

public class MessageFormater {

	//6位表示：前三位为起始位置   末三位为结束位置
	public static int locateBq(String originalMsg, int start) {
		int loc = start;
		int length = originalMsg.length();
		while(loc<length) {
			if(originalMsg.charAt(loc)=='[') {
				start = loc;
				while(loc<length) {
					if(originalMsg.charAt(loc)==']') {
						return start*1000 + loc;
					}
					loc++;
				}
				loc = start;
			}
			loc++;
		}
		return 0;
	}
	
	//格式化日期
	public static String getDateString(String d_in) {
		//Tue Jul 30 18:02:50 +0800 2013
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
		String dateString = null;
		ParsePosition pp = new ParsePosition(0);
		Date d = sdf.parse(d_in, pp);
		sdf = new SimpleDateFormat("HH:mm:ss EEE M月d日");
		dateString = sdf.format(d);
		return dateString;
	}
	
	//url 解析
	public static String parseStatus(String status) {
		JSONObject statusJsonObject = null;
		try {
			statusJsonObject = new JSONObject(status);
			int len = statusJsonObject.getJSONArray("statuses").length();
			String text;
			for(int i=0; i<len; i++) {
				text = statusJsonObject.getJSONArray("statuses").getJSONObject(i).getString("text");
				String responseValue = parseURL(text);
				if(responseValue!=null) {
					statusJsonObject.getJSONArray("statuses").getJSONObject(i).put("urls", responseValue);
				}
				
				if(statusJsonObject.getJSONArray("statuses").getJSONObject(i).has("retweeted_status")) {
					text = statusJsonObject.getJSONArray("statuses").getJSONObject(i).getJSONObject("retweeted_status").getString("text");
					responseValue = parseURL(text);
					if(responseValue!=null) {
						statusJsonObject.getJSONArray("statuses").getJSONObject(i).getJSONObject("retweeted_status").put("urls", responseValue);
					}
				}
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statusJsonObject.toString();
	}
	
	public static String parseURL(String status) {
		int start, end, length;
		start = 0;
		end = 0;
		String tempStr = status;
		String urls_short = null;
		length = tempStr.length();
		while(tempStr.contains("http://")) {
			start = tempStr.indexOf("http://");
			end = start + 7;
			while(end<length && isLegal(tempStr.charAt(end))) {
				end++;
			}
			if(urls_short!=null) {
				urls_short += "&url_short="+tempStr.substring(start, end);
			}
			else {
				urls_short = tempStr.substring(start, end);
			}
			tempStr = tempStr.substring(end);
			length = tempStr.length();
		}
		
		if(urls_short!=null) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("url", "https://api.weibo.com/2/short_url/expand.json");
			params.put("source", ConstantS.APP_KEY);
			params.put("access_token", MainActivity.accessToken.getToken());
			params.put("url_short", urls_short);
			
			String responseValue = null;
			try {
				responseValue = HttpsUtils.doGet(params);
				JSONObject jsonObject = new JSONObject(responseValue);
				if(jsonObject.has("error")) {
					return null;
				}
				return responseValue;
			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static boolean isLegal(char ch) {
		if(ch>='a' && ch <='z') {
			return true;
		}
		else if(ch>='A' && ch <='Z') {
			return true;
		}
		else if(ch>='0' && ch <='9') {
			return true;
		}
		else if(ch=='.' || ch=='/') {
			return true;
		}
		return false;
	}
	
}
