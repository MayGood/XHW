package com.maygood.xhw.bgtask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.maygood.xhw.MainActivity;
import com.maygood.xhw.data.ConstantS;
import com.maygood.xhw.net.HttpsUtils;

public class QuickTools {

	public static void sendTextWeibo(Context context, String status, boolean visible) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("url", "https://api.weibo.com/2/statuses/update.json");
		params.put("source", ConstantS.APP_KEY);
		params.put("access_token", MainActivity.accessToken.getToken());
		params.put("status", status);
		//if(!visible) {
		//	params.put("visible", "1");
		//}
		SendWeiboTask textTask = new SendWeiboTask(context);
		textTask.execute(params);
	}
	
	public static void sendPictureWeibo(Context context, String status, int imageId) {
		
	}
	
}

class SendWeiboTask extends AsyncTask<Map, Integer, String> {
	
	public Context context;
	public SendWeiboTask(Context context) {
		this.context = context;
	}
	
	@Override
	protected String doInBackground(Map... params) {
		// TODO Auto-generated method stub
		String responseValue = "";
		try {
			responseValue = HttpsUtils.doPost(null, params[0]);
			//Log.d("responseValue", responseValue);
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
		}
		return responseValue;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		try {
			if (result.isEmpty()) {
				Toast.makeText(context, "不知道为什么，就是失败了…", Toast.LENGTH_SHORT).show();
			}
			else {
				JSONObject jsonObj = new JSONObject(result);
				if (jsonObj.has("error")) {
					Toast.makeText(context, jsonObj.getString("error"), Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(context, "你得到了它！", Toast.LENGTH_SHORT).show();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "不知道为什么，就是失败了…", Toast.LENGTH_SHORT).show();
		}
	}
	
}