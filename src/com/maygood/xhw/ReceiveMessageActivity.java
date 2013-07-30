package com.maygood.xhw;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.maygood.xhw.Saloon.ClickResponse;
import com.maygood.xhw.app.DialogBox;
import com.maygood.xhw.data.ConstantS;
import com.maygood.xhw.data.DataBaseHandler;
import com.maygood.xhw.data.MessageFormater;
import com.maygood.xhw.data.SinceIdKeeper;
import com.maygood.xhw.net.HttpsUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.Html.ImageGetter;
import android.text.style.ClickableSpan;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiveMessageActivity extends Activity {

	private ListView weiboList;
	private ArrayList<HashMap<String, Object>> listItem;
	
	private DataBaseHandler dbHandler;
	private String currentUid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("抽个风~咻… //熊单间");
		setContentView(R.layout.activity_receive_message);
		//SinceIdKeeper.clear(ReceiveMessageActivity.this, ConstantS.XXH_id);
		currentUid = ConstantS.XX_id;
		
		weiboList = (ListView)findViewById(R.id.weiboList);
		
		RelativeLayout footer = (RelativeLayout) LayoutInflater.from(ReceiveMessageActivity.this).inflate(R.layout.list_add_more, null);
		weiboList.addFooterView(footer);
		listItem = new ArrayList<HashMap<String,Object>>();
		
		weiboList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				//String test = ((TextView)view.findViewById(R.id.time_show)).getText().toString();
				//Toast.makeText(ReceiveMessageActivity.this, test, Toast.LENGTH_SHORT).show();
				if(pos == parent.getCount()-1) {
					String maxId = null;
					if(pos>0) {
						maxId = ((HashMap<String, Object>)parent.getItemAtPosition(pos-1)).get("mid").toString();
						getWeiboMessage(maxId);
					}
				}
				else {
					String ori = view.getTag(R.id.tag_ori).toString();
					Intent i_weibo = new Intent(ReceiveMessageActivity.this, WeiboShow.class);
					i_weibo.putExtra("ori", ori);
					startActivity(i_weibo);
				}
			}
		});
		
		dbHandler = new DataBaseHandler(this);
		
		//dbHandler.customized();
		//dbHandler.clearDB();
		
		//db op
		String jsonStr = dbHandler.queryDB(currentUid, 20, null);
		initList(jsonStr);
		
		//getWeiboMessage();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receive_message, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		String jsonStr;
		switch (item.getItemId()) {
		case R.id.action_settings:
			getWeiboMessage();
			break;
		case R.id.xx:
			setTitle("抽个风~咻… //熊单间");
			currentUid = ConstantS.XX_id;
			jsonStr = dbHandler.queryDB(currentUid, 20, null);
			initList(jsonStr);
			break;
		case R.id.xxh:
			setTitle("抽个风~咻… //小黑屋");
			currentUid = ConstantS.XXH_id;
			jsonStr = dbHandler.queryDB(currentUid, 20, null);
			initList(jsonStr);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case 0:
			break;
		case 1:
			final String id_to_repost = info.targetView.getTag(R.id.tag_mid).toString();
			//Log.d("转发", id_to_repost);
			final EditText repostView = new EditText(ReceiveMessageActivity.this);
			OnClickListener repostListener = new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Map<String, String> params = new HashMap<String, String>();
					params.put("url", "https://api.weibo.com/2/statuses/repost.json");
					params.put("source", ConstantS.APP_KEY);
					params.put("access_token", MainActivity.accessToken.getToken());
					params.put("id", id_to_repost);
					params.put("status", repostView.getText().toString());
					RepostWeiboTask repostTask = new RepostWeiboTask();
					repostTask.execute(params);
				}
			};
			repostView.setText(info.targetView.getTag(R.id.tag_retweeted).toString());
			new AlertDialog.Builder(ReceiveMessageActivity.this).setTitle("说点什么吧，爷~").setView(repostView)
			//.setMultiChoiceItems(new String[] {"评论给当前微博", "评论给原微博"}, null, null)
			.setPositiveButton("确定", repostListener)
			.setNegativeButton("取消", null)
			.show();
			break;
		case 2:
			final String id_to_del = info.targetView.getTag(R.id.tag_mid).toString();
			OnClickListener delListener = new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Map<String, String> params = new HashMap<String, String>();
					params.put("url", "https://api.weibo.com/2/statuses/destroy.json");
					params.put("source", ConstantS.APP_KEY);
					params.put("access_token", MainActivity.accessToken.getToken());
					params.put("id", id_to_del);
					DelWeiboTask delTask = new DelWeiboTask();
					delTask.execute(params);
					dbHandler.deleteWeibo(id_to_del);
					String jsonStr = dbHandler.queryDB(currentUid, listItem.size(), null);
					initList(jsonStr);
				}
			};
			new AlertDialog.Builder(ReceiveMessageActivity.this).setTitle("确定删除？")
			.setPositiveButton("确定", delListener)
			.setNegativeButton("取消", null)
			.show();
			break;

		default:
			Toast.makeText(ReceiveMessageActivity.this, "还没做呢", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	public void initList(String json) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(json);
			int length = jsonObj.getJSONArray("statuses").length();
			
			//weiboList.setAdapter(null);
			listItem.clear();
			for (int i=0; i<length; i++) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("ori_data", jsonObj.getJSONArray("statuses").getJSONObject(i).toString());
				
				item.put("mid", jsonObj.getJSONArray("statuses").getJSONObject(i).getString("mid"));
				
				listItem.add(item);
			}
			WeiboAdapter listItemAdapter = new WeiboAdapter(ReceiveMessageActivity.this, listItem, R.layout.weibo_showbox,
					new String[]{"ori_data", "mid"},
					new int[]{R.id.time_show, R.id.content_show, R.id.retweeted_show, R.id.counts_show});
			
			weiboList.setAdapter(listItemAdapter);
			
			weiboList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
				
				@Override
				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					// TODO Auto-generated method stub
					menu.setHeaderTitle("召唤我作甚吗？");
					menu.add(0, 0, 0, "不干嘛…玩~");
					menu.add(0, 1, 1, "转发？");
					if(currentUid == ConstantS.XXH_id) {
						menu.add(0, 2, 2, "删除？");
					}
				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fillList(String json) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(json);
			int length = jsonObj.getJSONArray("statuses").length();
			
			for (int i=0; i<length; i++) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("ori_data", jsonObj.getJSONArray("statuses").getJSONObject(i).toString());
				
				item.put("mid", jsonObj.getJSONArray("statuses").getJSONObject(i).getString("mid"));
				
				listItem.add(item);
			}
			WeiboAdapter listItemAdapter = new WeiboAdapter(ReceiveMessageActivity.this, listItem, R.layout.weibo_showbox,
					new String[]{"ori_data", "mid"},
					new int[]{R.id.time_show, R.id.content_show, R.id.retweeted_show, R.id.counts_show});
			
			weiboList.setAdapter(listItemAdapter);
			weiboList.setSelection(listItem.size()-length-1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getWeiboMessage() {
		String since_id = SinceIdKeeper.read(ReceiveMessageActivity.this, currentUid);
		
		Map<String, String> params = new HashMap<String, String>();
		if(currentUid == ConstantS.XX_id) {
			params.put("url", "https://api.weibo.com/2/statuses/friends_timeline.json");
			params.put("source", ConstantS.X_APP_KEY);
			params.put("access_token", MainActivity.x_accessToken.getToken());
		}
		else {
			params.put("url", "https://api.weibo.com/2/statuses/user_timeline.json");
			params.put("source", ConstantS.APP_KEY);
			params.put("access_token", MainActivity.accessToken.getToken());
			params.put("uid", currentUid);
		}
		//params.put("trim_user", "1");
		params.put("count", "20");
		if(HttpsUtils.getNetType(this.getSystemService(Context.CONNECTIVITY_SERVICE)) == 1) {
			params.put("since_id", since_id);
		}
		GetWeioboTask getAsync = new GetWeioboTask(true, null);
		getAsync.execute(params);
	}
	
	public void getWeiboMessage(String max_id) {
		if(HttpsUtils.getNetType(this.getSystemService(Context.CONNECTIVITY_SERVICE)) == 0) {
			Map<String, String> params = new HashMap<String, String>();
			if(currentUid == ConstantS.XX_id) {
				params.put("url", "https://api.weibo.com/2/statuses/friends_timeline.json");
				params.put("source", ConstantS.X_APP_KEY);
				params.put("access_token", MainActivity.x_accessToken.getToken());
			}
			else {
				params.put("url", "https://api.weibo.com/2/statuses/user_timeline.json");
				params.put("source", ConstantS.APP_KEY);
				params.put("access_token", MainActivity.accessToken.getToken());
				params.put("uid", currentUid);
			}
			//params.put("trim_user", "1");
			params.put("count", "20");
			params.put("max_id", max_id);
			GetWeioboTask getAsync = new GetWeioboTask(false, max_id);
			getAsync.execute(params);
		}
		else {
			String jsonStr = dbHandler.queryDB(currentUid, 20, max_id);
			fillList(jsonStr);
		}
	}
	
	public void changeUser(int i) {
		if (i==0) {
			currentUid = ConstantS.XXH_id;
		}
		else {
			currentUid = ConstantS.XX_id;
		}
	}
	
	class WeiboAdapter extends BaseAdapter {
		
		private Context context;
		private ArrayList<HashMap<String, Object>> listItem;
		private int resource;
		private String[] from;
		private int[] to;
		private LayoutInflater listContainer;
		
		public final class ListItemView {
			public TextView time;
			public TextView content;
			public DialogBox retweeted;
			public TextView counts;
		}
		
		public WeiboAdapter(Context context, List<? extends Map<String, ?>>data, int resource, String[] from, int[] to) {
			// TODO Auto-generated constructor stub
			super();
			this.context = context;
			listItem = (ArrayList<HashMap<String, Object>>) data;
			this.resource = resource;
			this.from = from;
			this.to = to;
			listContainer = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItem.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ListItemView listItemView = null;
			if(convertView == null) {
				listItemView = new ListItemView();
				convertView = listContainer.inflate(resource, null);
				
				listItemView.time = (TextView) convertView.findViewById(to[0]);
				listItemView.content = (TextView) convertView.findViewById(to[1]);
				listItemView.retweeted = (DialogBox) convertView.findViewById(to[2]);
				listItemView.counts = (TextView) convertView.findViewById(to[3]);
				
				convertView.setTag(R.id.tag_layout, listItemView);
			}
			else {
				listItemView = (ListItemView) convertView.getTag(R.id.tag_layout);
			}
			
			try {
				JSONObject ori_json_obj = new JSONObject(listItem.get(position).get(from[0]).toString());
				listItemView.time.setText(MessageFormater.getDateString(ori_json_obj.getString("created_at")));
				setWeiboContent(listItemView.content, ori_json_obj.getString("text"));
				if(ori_json_obj.has("retweeted_status")) {
					String retweeted_name = ori_json_obj.getJSONObject("retweeted_status").getJSONObject("user").getString("name");
					Map<String, String> briefInfo = new HashMap<String, String>();
					if(ori_json_obj.getJSONObject("retweeted_status").has("thumbnail_pic")) {
						briefInfo.put("picture", "true");
					}
					if(ori_json_obj.getJSONObject("retweeted_status").has("urls")) {
						String urlsJsonString = ori_json_obj.getJSONObject("retweeted_status").getString("urls");
						JSONObject urlsJsonObject = new JSONObject(urlsJsonString);
						int length = urlsJsonObject.getJSONArray("urls").length();
						for(int i=0; i<length; i++) {
							if(urlsJsonObject.getJSONArray("urls").getJSONObject(i).getInt("type")==1) {
								if(briefInfo.containsKey("video"))
									continue;
								briefInfo.put("video", urlsJsonObject.getJSONArray("urls").getJSONObject(i).getString("url_long"));
							}
							else if(urlsJsonObject.getJSONArray("urls").getJSONObject(i).getInt("type")==2) {
								if(briefInfo.containsKey("music"))
									continue;
								briefInfo.put("music", urlsJsonObject.getJSONArray("urls").getJSONObject(i).getString("url_long"));
							}
						}
					}
					String retweeted_status = ori_json_obj.getJSONObject("retweeted_status").getString("text");
					retweeted_status += "\n"+MessageFormater.getDateString(ori_json_obj.getJSONObject("retweeted_status").getString("created_at"));
					
					listItemView.retweeted.setContent(ReceiveMessageActivity.this, retweeted_name, briefInfo, retweeted_status, false, false);
					listItemView.retweeted.setVisibility(View.VISIBLE);
					
					String screen_name = null;
					if(currentUid == ConstantS.XX_id) {
						screen_name = "艾米西斯特";
					}
					else if(currentUid == ConstantS.XXH_id) {
						screen_name = "某猫徐小黑";
					}
					
					convertView.setTag(R.id.tag_retweeted, "//@"+screen_name+":"+ori_json_obj.getString("text"));
				}
				else {
					listItemView.retweeted.setVisibility(View.GONE);
					
					convertView.setTag(R.id.tag_retweeted, "");
				}
				listItemView.counts.setText("评论("+ori_json_obj.getString("comments_count")+")    转发("
						+ori_json_obj.getString("reposts_count")+")");
				
				convertView.setTag(R.id.tag_ori, listItem.get(position).get(from[0]).toString());
				convertView.setTag(R.id.tag_mid, listItem.get(position).get(from[1]).toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return convertView;
		}
		
		public void setWeiboContent(TextView contentView, String status) {
			int loc = 0;
			int length = status.length();
			int start, end = -1;
			int temp;
			
			ImageGetter imgGetter = new ImageGetter() {
				
				@Override
				public Drawable getDrawable(String source) {
					// TODO Auto-generated method stub
					int id = Integer.parseInt(source);
					Drawable d = getResources().getDrawable(id);
					d.setBounds(0, 0, 22, 22);
					return d;
				}
			};
			
			contentView.setText("");
			while((temp=MessageFormater.locateBq(status, loc))!=0) {
				start = temp/1000;
				end = temp%1000;
				String preContent;
				String code = status.substring(start, end+1);
				int rid= dbHandler.getRidByCode(code);
				if(rid==-1) {
					preContent = status.substring(loc, start+1);
					//contentView.append(preContent);
					formatContent(ReceiveMessageActivity.this, contentView, preContent);
					loc = start+1;
				}
				else {
					preContent = status.substring(loc, start);
					//contentView.append(preContent);
					formatContent(ReceiveMessageActivity.this, contentView, preContent);
					rid += R.drawable.bq_0000;
					contentView.append(Html.fromHtml("<img src=\'"+rid+"\'/>", imgGetter, null));
					loc = end+1;
				}
			}
			if(loc<length) {
				//contentView.append(status.substring(loc));
				formatContent(ReceiveMessageActivity.this, contentView, status.substring(loc));
			}
		}
		
		private void formatContent(Context context, TextView contentView, String content) {
			SpannableString sps;
			int start, end, length;
			length = content.length();
			start = 0;
			end = 0;
			while(start < length) {
				if(content.charAt(start) == '@') {
					//contentView.append(content, end, start);
					markURL(context, contentView, content.substring(end, start));
					end = start;
					int tmp = start+1;
					while(tmp < length && isLegal(content.charAt(tmp), false)) {
						tmp++;
					}
					if(tmp > start+1) {
						sps = new SpannableString(content.subSequence(end, tmp));
						sps.setSpan(new ClickResponse(context, sps, 0), 0, sps.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						contentView.append(sps);
					}
					else {
						//contentView.append(content, end, tmp);
						markURL(context, contentView, content.substring(end, tmp));
					}
					end = tmp;
					start = end;
				}
				else if(content.charAt(start) == '#') {
					//contentView.append(content, end, start);
					markURL(context, contentView, content.substring(end, start));
					int tmp = start;
					end = start+1;
					while(end < length && content.charAt(end) != '#') {
						end++;
					}
					if(end<length) {
						sps = new SpannableString(content.subSequence(start, end+1));
						sps.setSpan(new ClickResponse(context, sps, 1), 0, sps.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						contentView.append(sps);
						start = end + 1;
						end = start;
					}
					else {
						end = tmp;
						start++;
					}
				}
				else {
					start++;
				}
			}
			//contentView.append(content.substring(end));
			markURL(context, contentView, content.substring(end));
		}
		
		private void markURL(Context context, TextView contentView, String content) {
			SpannableString sps;
			int start, end, length;
			start = 0;
			end = 0;
			String tempStr = content;
			length = tempStr.length();
			while(tempStr.contains("http://")) {
				start = tempStr.indexOf("http://");
				contentView.append(tempStr, 0, start);
				end = start + 7;
				while(end<length && isLegal(tempStr.charAt(end), true)) {
					end++;
				}
				sps = new SpannableString(tempStr.substring(start, end));
				sps.setSpan(new ClickResponse(context, sps, 2), 0, sps.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				contentView.append(sps);
				tempStr = tempStr.substring(end);
				length = tempStr.length();
			}
			contentView.append(tempStr);
		}
		
		private boolean isLegal(char ch, boolean isURL) {
			int i = ch;
			
			if(isURL) {
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
			
			if(i>=19968 && i<=40895) {
				return true;
			}
			else if(ch>='a' && ch <='z') {
				return true;
			}
			else if(ch>='A' && ch <='Z') {
				return true;
			}
			else if(ch>='0' && ch <='9') {
				return true;
			}
			else if(ch=='_' || ch=='-') {
				return true;
			}
			return false;
		}
		
	}
	
	class GetWeioboTask extends AsyncTask<Map, Integer, String> {
		
		public boolean isRefresh;
		public String maxId;
		
		public GetWeioboTask(boolean isRefresh, String maxId) {
			// TODO Auto-generated constructor stub
			this.isRefresh = isRefresh;
			this.maxId = maxId;
		}
		
		@Override
		protected String doInBackground(Map... params) {
			// TODO Auto-generated method stub
			String responseValue = "";
			try {
				responseValue = HttpsUtils.doGet(params[0]);
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
			responseValue = MessageFormater.parseStatus(responseValue);
			return responseValue;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//Log.d("result", result);
			try {
				if(result!=null&&result.length()>0) {
					JSONObject jsonObj = new JSONObject(result);
					if (jsonObj.has("error")) {
						Toast.makeText(ReceiveMessageActivity.this, jsonObj.getString("error"), Toast.LENGTH_SHORT).show();
					}
					else {
						if (jsonObj.getJSONArray("statuses").length()==0) {
							Toast.makeText(ReceiveMessageActivity.this, "喵猜还没有新的微博…嗯", Toast.LENGTH_SHORT).show();
						}
						else{
							//db op
							String since_id = jsonObj.getJSONArray("statuses").getJSONObject(0).getString("idstr");
							if(!since_id.equals(SinceIdKeeper.read(ReceiveMessageActivity.this, currentUid))) {
								SinceIdKeeper.keep(ReceiveMessageActivity.this, currentUid, since_id);
								Toast.makeText(ReceiveMessageActivity.this, "刷出来啦！！！", Toast.LENGTH_SHORT).show();
							}
							else {
								Toast.makeText(ReceiveMessageActivity.this, "喵猜还没有新的微博…嗯", Toast.LENGTH_SHORT).show();
							}
							dbHandler.updateDB(result);
							String jsonStr;
							if(isRefresh) {
								jsonStr = dbHandler.queryDB(currentUid, 20, null);
								initList(jsonStr);
							}
							else {
								jsonStr = dbHandler.queryDB(currentUid, 20, maxId);
								fillList(jsonStr);
							}
						}
					}
				}
				else {
					Toast.makeText(ReceiveMessageActivity.this, "不知道为什么，就是失败了…", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(ReceiveMessageActivity.this, "不知道为什么，就是失败了…", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	class RepostWeiboTask extends AsyncTask<Map, Integer, String> {

		@Override
		protected String doInBackground(Map... params) {
			// TODO Auto-generated method stub
			String responseValue = "";
			try {
				responseValue = HttpsUtils.doPost(ReceiveMessageActivity.this, params[0]);
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
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//Log.d("del weibo", result);
			if(result!=null) {
				try {
					JSONObject resultJsonObject = new JSONObject(result);
					if(resultJsonObject.has("error")) {
						Toast.makeText(ReceiveMessageActivity.this, resultJsonObject.getString("error"), Toast.LENGTH_SHORT).show();
					}
					else if(resultJsonObject.has("created_at")) {
						Toast.makeText(ReceiveMessageActivity.this, "转发成功！ 哇咔咔~", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	class DelWeiboTask extends AsyncTask<Map, Integer, String> {

		@Override
		protected String doInBackground(Map... params) {
			// TODO Auto-generated method stub
			String responseValue = "";
			try {
				responseValue = HttpsUtils.doPost(ReceiveMessageActivity.this, params[0]);
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
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//Log.d("del weibo", result);
			if(result!=null) {
				try {
					JSONObject resultJsonObject = new JSONObject(result);
					if(resultJsonObject.has("error")) {
						Toast.makeText(ReceiveMessageActivity.this, resultJsonObject.getString("error"), Toast.LENGTH_SHORT).show();
					}
					else if(resultJsonObject.has("created_at")) {
						Toast.makeText(ReceiveMessageActivity.this, "删除成功！ 哇咔咔~", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	class ClickResponse extends ClickableSpan {
		Context context;
		CharSequence text;
		int mode;
		
		public ClickResponse(Context context, CharSequence charSequence, int mode) {
			this.context = context;
			text = charSequence;
			this.mode = mode;
		}
		@Override
		public void onClick(View widget) {
			// TODO Auto-generated method stub
			/*
			 * mode :
			 * 	0 - @
			 *  1 - #
			 *  2 - url
			 */
			if(mode == 2) {
				Intent i= new Intent();
			    i.setAction("android.intent.action.VIEW");
			    Uri content_url = Uri.parse(text.toString());
			    i.setData(content_url);
			    startActivity(i);
			}
			else {
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
				
			}
		}
		
		@Override
		public void updateDrawState(TextPaint ds) {
			// TODO Auto-generated method stub
			super.updateDrawState(ds);
			if(mode == 0) {
				ds.setColor(Color.rgb(65, 105, 225));
			}
			else if(mode == 1) {
				ds.setColor(Color.rgb(50, 105, 150));
			}
			else if(mode == 2) {
				ds.setColor(Color.rgb(50, 105, 150));
			}
			ds.setUnderlineText(false);
		}
	}
}
