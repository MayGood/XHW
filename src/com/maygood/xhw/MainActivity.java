package com.maygood.xhw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


import org.json.JSONException;
import org.json.JSONObject;

import com.maygood.xhw.bgtask.Service820;
import com.maygood.xhw.data.AccessTokenKeeper;
import com.maygood.xhw.data.ConstantS;
import com.maygood.xhw.data.DataBaseHandler;
import com.maygood.xhw.data.SettingsKeeper;
import com.maygood.xhw.net.HttpsUtils;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private DataBaseHandler dbHandler;

	private Weibo		xxhWeibo;
	private Weibo		xWeibo;
	private TextView	codeText;
	private String		tokenString;
	
	private IWeiboAPI weiboAPI;
	
	public static Oauth2AccessToken accessToken;
	public static Oauth2AccessToken x_accessToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("某猫的小黑屋~嘿…");
		setContentView(R.layout.activity_main);
		
		codeText = (TextView)findViewById(R.id.code);
		//关于 ConstantS.XXH_id 这个参数以后还得修改！！！
		MainActivity.accessToken = AccessTokenKeeper.readAccessToken(this, ConstantS.XXH_id);
		MainActivity.x_accessToken = AccessTokenKeeper.readAccessToken(this, ConstantS.X_id);
		
		//login button
		/*
		findViewById(R.id.loginButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "U got me :)", Toast.LENGTH_LONG).show();
				xxhWeibo.anthorize(MainActivity.this, new AuthDialogListener());
			}
		});*/
		findViewById(R.id.loginButton).setOnClickListener(this);
		findViewById(R.id.x_Button).setOnClickListener(this);
		
		//发风啦~
		findViewById(R.id.test_send).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//测试发微博
				Intent is=new Intent(MainActivity.this, SendMessageActivity.class);
				is.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(is);
			}
		});
		
		//抽风啦~
		findViewById(R.id.test_rcv).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//测试获取微薄
				Intent ir=new Intent(MainActivity.this, ReceiveMessageActivity.class);
				ir.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(ir);
			}
		});
		
		//进屋啦~
		findViewById(R.id.test_saloon).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//测试获取微薄
				Intent i=new Intent(MainActivity.this, Saloon.class);
				i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(i);
			}
		});
		
		if(SettingsKeeper.read(this, "isNewInstalled")) {
			DataBaseHandler.customized(this);
			String dbFilePath = "/data/data/com.maygood.xhw/databases/weibo.db";
			try {
				//FileInputStream fis = new FileInputStream(android.os.Environment.getExternalStorageDirectory()+"/weibo.db");
				InputStream is = this.getAssets().open("weibo.db");
				File f = new File(dbFilePath);
				FileOutputStream fos = new FileOutputStream(f);
				byte[] buffer = new byte[4096];
				int count;
				while((count=is.read(buffer))>0) {
					fos.write(buffer, 0, count);
				}
				fos.flush();
				fos.close();
				is.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SettingsKeeper.keep(this, "isNewInstalled", false);
		}
		/*
		else {
			dbHandler = new DataBaseHandler(this);
			dbHandler.initBqlib();
			
			String dbFilePath = "/data/data/com.maygood.xhw/databases/weibo.db";
			try {
				FileInputStream fis = new FileInputStream(dbFilePath);
				//InputStream is = this.getAssets().open("weibo.db");
				File f = new File(android.os.Environment.getExternalStorageDirectory()+"/weibo.db");
				FileOutputStream fos = new FileOutputStream(f);
				byte[] buffer = new byte[4096];
				int count;
				while((count=fis.read(buffer))>0) {
					fos.write(buffer, 0, count);
				}
				fos.flush();
				fos.close();
				fis.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		if (MainActivity.accessToken.isSessionValid()) {
			String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
			.format(new java.util.Date(MainActivity.accessToken.getExpiresTime()));
			codeText.setText("有效期：" + date);
			
			if (MainActivity.x_accessToken.isSessionValid()) {
				date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(new java.util.Date(MainActivity.x_accessToken.getExpiresTime()));
				codeText.append("\n单间有效期：" + date);
			}
			else {
				Toast.makeText(MainActivity.this, "单间需要重新认证", Toast.LENGTH_SHORT).show();
			}
			
			//测试获取微薄
			//Intent ir=new Intent(MainActivity.this, ReceiveMessageActivity.class);
			//ir.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			//startActivity(ir);
			
			//测试获取微薄
			Intent i=new Intent(MainActivity.this, Saloon.class);
			i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(i);
			
		}
		else {
			Toast.makeText(MainActivity.this, "需要重新认证", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	class AuthDialogListener implements WeiboAuthListener {
		private boolean isX;
		
		public AuthDialogListener(boolean isX) {
			this.isX = isX;
		}
		
        @Override
        public void onComplete(Bundle values) {
        	final String code = values.getString("code");
        	if(code != null){
	        	codeText.setText("取得认证code: \r\n Code: " + code);
	        	Toast.makeText(MainActivity.this, "认证code成功", Toast.LENGTH_SHORT).show();
	        	//return;
        	}
        	
        	//https POST
        	Runnable httpsPost = new Runnable() {
    			
    			@Override
    			public void run() {
    				// TODO Auto-generated method stub
    				try {
    					Map<String, String> params = new HashMap<String, String>();
    					params.put("url", "https://api.weibo.com/oauth2/access_token");
    					if(isX) {
    						params.put("client_id", ConstantS.X_APP_KEY);
    						params.put("client_secret", ConstantS.X_APP_SECRET);
    					}
    					else {
    						params.put("client_id", ConstantS.APP_KEY);
    						params.put("client_secret", ConstantS.APP_SECRET);
    					}
    					params.put("grant_type", ConstantS.GRANT_TYPE);
    					params.put("redirect_uri", ConstantS.REDIRECT_URL);
    					params.put("code", code);
    					tokenString = HttpsUtils.doPost(MainActivity.this, params);
    					//Log.d("认证", tokenString);
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
    			}
    		};
    		Thread postThread = new Thread(httpsPost);
    		postThread.start();
    		
    		while(postThread.isAlive());
        	
    		try {
				JSONObject jsonObj = new JSONObject(tokenString);
				//String token = values.getString("access_token");
				//String expires_in = values.getString("expires_in");
				String token = jsonObj.getString("access_token");
				String expires_in = jsonObj.getString("expires_in");
				String uid = jsonObj.getString("uid");
				if(uid == ConstantS.X_id) {
					MainActivity.x_accessToken = new Oauth2AccessToken(token, expires_in);
					if (MainActivity.x_accessToken.isSessionValid()) {
						String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(MainActivity.x_accessToken.getExpiresTime()));
						codeText.setText("认证成功: \r\n access_token: " + token + "\r\n" + "expires_in: " + expires_in + "\r\n有效期：" + date);
						AccessTokenKeeper.keepAccessToken(MainActivity.this, x_accessToken, uid);
						Toast.makeText(MainActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					MainActivity.accessToken = new Oauth2AccessToken(token, expires_in);
					if (MainActivity.accessToken.isSessionValid()) {
						String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(MainActivity.accessToken.getExpiresTime()));
						codeText.setText("认证成功: \r\n access_token: " + token + "\r\n" + "expires_in: " + expires_in + "\r\n有效期：" + date);
						AccessTokenKeeper.keepAccessToken(MainActivity.this, accessToken, uid);
						Toast.makeText(MainActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }

        @Override
        public void onError(WeiboDialogError e) {
            Toast.makeText(getApplicationContext(),
                    "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "Auth cancel",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getApplicationContext(),
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent is = new Intent(MainActivity.this, TimerSettingActivity.class);
			startActivity(is);
			break;
		case R.id.action_test:
			Intent it = new Intent(MainActivity.this, Test.class);
			startActivity(it);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.loginButton:
			xxhWeibo = Weibo.getInstance(ConstantS.APP_KEY, ConstantS.REDIRECT_URL, ConstantS.SCOPE);
			xxhWeibo.anthorize(MainActivity.this, new AuthDialogListener(false));
			break;
		case R.id.x_Button:
			xWeibo = Weibo.getInstance(ConstantS.X_APP_KEY, ConstantS.REDIRECT_URL, ConstantS.SCOPE);
			xWeibo.anthorize(MainActivity.this, new AuthDialogListener(true));
			break;

		default:
			break;
		}
	}
	
}
