package com.maygood.xhw;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.maygood.xhw.R;
import com.maygood.xhw.app.BqBox;
import com.maygood.xhw.data.ConstantS;
import com.maygood.xhw.data.DataBaseHandler;
import com.maygood.xhw.net.HttpsUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

public class SendMessageActivity extends Activity {
	private final static int POST_IMAGE = 1;
	
	private EditText textInput;
	private Button picButton;
	private Button bqButton;
	int[] location;	//location of bqButton
	
	private TabHost tabHost;
	private GridView bqGrid;
	private GridView bqGridUndef;
	private DataBaseHandler dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("发个风~呼…");
		setContentView(R.layout.activity_send_message);
		
		textInput = (EditText)findViewById(R.id.textInput);
		
		picButton = (Button)findViewById(R.id.command_pic);
		picButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.setType("image/*");
				startActivityForResult(i, 0);
			}
		});
		
		dbHandler = new DataBaseHandler(this);
		LayoutInflater inflater = LayoutInflater.from(SendMessageActivity.this);
		//View view = inflater.inflate(R.layout.popup_bq, null);
		
		/*
		bqGroup = (Spinner) view.findViewById(R.id.bq_groupname);
		String[] groupname = {"罗小黑"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, groupname);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bqGroup.setAdapter(adapter);
		bqGroup.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});*/
		//bqGrid = (GridView) view.findViewById(R.id.bq_grid);
		
		
		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();
		bqGrid = (GridView) findViewById(R.id.grid_bq0);
		initBqGrid(bqGrid, "罗小黑");
		bqGrid = (GridView) findViewById(R.id.grid_bq1);
		initBqGrid(bqGrid, "心情");
		bqGrid = (GridView) findViewById(R.id.grid_bq2);
		initBqGrid(bqGrid, "阿狸");
		bqGrid = (GridView) findViewById(R.id.grid_bq3);
		initBqGrid(bqGrid, "大熊");
		tabHost.addTab(tabHost.newTabSpec("lxhx").setContent(R.id.grid_bq0).setIndicator("罗小黑"));
		tabHost.addTab(tabHost.newTabSpec("xq").setContent(R.id.grid_bq1).setIndicator("心情"));
		tabHost.addTab(tabHost.newTabSpec("ali").setContent(R.id.grid_bq2).setIndicator("阿狸"));
		tabHost.addTab(tabHost.newTabSpec("dx").setContent(R.id.grid_bq3).setIndicator("大熊"));
		bqGridUndef = (GridView) findViewById(R.id.grid_bq4);
		final Spinner spinner = (Spinner) findViewById(R.id.spinner_undef);
		ArrayList<String> groupnames = dbHandler.getBqGroup();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupnames);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				initBqGrid(bqGridUndef, spinner.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		spinner.setSelection(34);
		tabHost.addTab(tabHost.newTabSpec("undef").setContent(R.id.tab_undef).setIndicator("自定义"));
		
		bqButton = (Button)findViewById(R.id.command_bq);
		location = new int[2];
		bqButton.getLocationOnScreen(location);
		
		BqBox view = new BqBox(this, location[0]+(bqButton.getWidth()/2)-10);
		bqGrid = view.gv;
		initBqGrid(bqGrid, "罗小黑");
		final PopupWindow pop = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		
		bqButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(pop.isShowing()) {
					pop.dismiss();
				}
				else {
					//pop.getContentView().measure(0,0);
					v.getLocationOnScreen(location);
					pop.showAtLocation(v, Gravity.NO_GRAVITY, 10, location[1]-1-pop.getContentView().getHeight());
					//pop.update();
				}
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_message, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings:
			sendTextMessage();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			Uri uri = data.getData();
			if(uri != null) {
				//b = MediaStore.Images.Media.getBitmap(cr, uri);
				sendPicMessage(uri);
				//((ImageView)findViewById(R.id.imageView)).setImageBitmap(b);
			}
		}
	}
	
	public void sendTextMessage() {
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("url", "https://api.weibo.com/2/statuses/update.json");
		params.put("source", ConstantS.APP_KEY);
		params.put("access_token", MainActivity.accessToken.getToken());
		params.put("status", textInput.getText().toString());
		SendWeioboTask textAsync = new SendWeioboTask();
		textAsync.execute(params);
	}
	
	public void sendPicMessage(Uri obj) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("url", "https://upload.api.weibo.com/2/statuses/upload.json");
		params.put("source", ConstantS.APP_KEY);
		params.put("access_token", MainActivity.accessToken.getToken());
		params.put("status", textInput.getText().toString());
		params.put("pic", obj);
		SendWeioboTask picAsync = new SendWeioboTask();
		picAsync.execute(params);
	}
	
	public void initBqGrid(GridView gv, String groupname) {
		//bqGrid.setNumColumns(10);
		//bqGrid.setColumnWidth(22);
		
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
		Map<Integer, String> bq_map = dbHandler.getBqByGroup(groupname);
		for (int i=0; i<(bq_map.size()/2); i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("bq_text", bq_map.get(2*i+1));
			item.put("bq_ico", R.drawable.bq_0000+Integer.parseInt(bq_map.get(2*i)));
			
			listItem.add(item);
		}
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(SendMessageActivity.this, listItem, R.layout.bq_showbox,
				new String[]{"bq_ico"},
				new int[]{R.id.bq_icon});
		
		gv.setAdapter(listItemAdapter);
		
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				String bqStr = ((HashMap<String, Object>)arg0.getItemAtPosition(pos)).get("bq_text").toString();
				int start = textInput.getSelectionStart();
				int end = textInput.getSelectionEnd();
				String old_text = textInput.getText().toString();
				String new_text = old_text.substring(0, start)+bqStr+old_text.substring(start);
				if(new_text.length()>140)
					return;
				textInput.setText(new_text);
				textInput.setSelection(start+bqStr.length());
				//bqGrid.setVisibility(View.INVISIBLE);
			}
		});
		
	}
	
	class SendWeioboTask extends AsyncTask<Map, Integer, String> {
		
		@Override
		protected String doInBackground(Map... params) {
			// TODO Auto-generated method stub
			String responseValue = "";
			try {
				responseValue = HttpsUtils.doPost(SendMessageActivity.this, params[0]);
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
					Toast.makeText(SendMessageActivity.this, "不知道为什么，就是失败了…", Toast.LENGTH_SHORT).show();
				}
				else {
					JSONObject jsonObj = new JSONObject(result);
					if (jsonObj.has("error")) {
						Toast.makeText(SendMessageActivity.this, jsonObj.getString("error"), Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(SendMessageActivity.this, "你得到了它！", Toast.LENGTH_SHORT).show();
						dbHandler.insertDB(result);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(SendMessageActivity.this, "不知道为什么，就是失败了…", Toast.LENGTH_SHORT).show();
			}
		}

	}
}
