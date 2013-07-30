package com.maygood.xhw;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import com.maygood.xhw.app.ImageBox;
import com.maygood.xhw.app.MODE;
import com.maygood.xhw.app.NETWORK_TASK;
import com.maygood.xhw.data.ConstantS;
import com.maygood.xhw.data.DataBaseHandler;
import com.maygood.xhw.data.MessageFormater;
import com.maygood.xhw.net.HttpsUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class WeiboShow extends Activity {
	
	private String mid = "";
	private String cid = null;
	
	private String thumbnail_pic;	//缩略图片地址
	private String bmiddle_pic;		//中等尺寸图片地址
	private String original_pic;	//原始图片地址
	
	private ScrollView weiboView;
	private TextView weiboContent;
	private ImageBox weiboPicture;
	private DialogBox dialogBox;
	private DataBaseHandler dbHandler;
	private ListView commentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_show);
		
		weiboView = (ScrollView) findViewById(R.id.weibo_view);
		weiboView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menu.setHeaderTitle("召唤我作甚吗？");
				menu.add(0, 0, 0, "不干嘛…玩~");
				menu.add(0, 1, 1, "复制一下内容文本");
				menu.add(0, 2, 2, "查看原微博");
				menu.add(0, 3, 3, "评论");
			}
		});
		
		commentList = (ListView)findViewById(R.id.comment_list);
		weiboContent = (TextView)findViewById(R.id.weibo_content);
		dbHandler = new DataBaseHandler(this);
		
		Intent i = getIntent();
		String ori = i.getStringExtra("ori");
		try {
			JSONObject jsonObj = new JSONObject(ori);
			mid = jsonObj.getString("mid");
			if(jsonObj.has("uid")) {
				if(jsonObj.getString("uid").equals(ConstantS.XX_id)) {
					setTitle("某熊");
				}
				else if(jsonObj.getString("uid").equals(ConstantS.XXH_id)) {
					setTitle("某猫");
				}
				else {
					setTitle("路(luan)人(ru)");
				}
			}
			else {
				if(jsonObj.getJSONObject("user").getString("id").equals(ConstantS.XX_id)) {
					setTitle("某熊");
				}
				else if(jsonObj.getJSONObject("user").getString("id").equals(ConstantS.XXH_id)) {
					setTitle("某猫");
				}
				else {
					String name = jsonObj.getJSONObject("user").getString("name");
					setTitle("路(luan)人(ru)-"+name);
				}
			}
			
			//get mid
			//mid = jsonObj.getString("mid");
			
			((TextView)findViewById(R.id.weibo_time)).setText(jsonObj.getString("created_at"));
			if(jsonObj.has("thumbnail_pic")) {
				weiboPicture = (ImageBox) findViewById(R.id.weibo_pic);
				weiboPicture.setImageResource(R.drawable.ic_loading);
				LoadPicTask getPic = new LoadPicTask();
				if(HttpsUtils.getNetType(this.getSystemService(Context.CONNECTIVITY_SERVICE))==0) {
					//WIFI
					bmiddle_pic = jsonObj.getString("bmiddle_pic");
					getPic.execute(bmiddle_pic);
				}
				else if(HttpsUtils.getNetType(this.getSystemService(Context.CONNECTIVITY_SERVICE))==1) {
					thumbnail_pic = jsonObj.getString("thumbnail_pic");
					getPic.execute(thumbnail_pic);
				}
				
				thumbnail_pic = jsonObj.getString("thumbnail_pic");
				bmiddle_pic = jsonObj.getString("bmiddle_pic");
				original_pic = jsonObj.getString("original_pic");
				
				weiboPicture.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i_image = new Intent(WeiboShow.this, FullscreenImageActivity.class);
						i_image.putExtra("thumbnail_pic", thumbnail_pic);
						i_image.putExtra("bmiddle_pic", bmiddle_pic);
						i_image.putExtra("original_pic", original_pic);
						startActivity(i_image);
					}
				});
				
			}
			String temptext = jsonObj.getString("text");
			setWeiboContent(temptext);
			if (jsonObj.has("retweeted_status")) {
				dialogBox = (DialogBox) findViewById(R.id.retweeted_content);
				String name;
				if(jsonObj.getJSONObject("retweeted_status").has("user")) {
					name = jsonObj.getJSONObject("retweeted_status").getJSONObject("user").getString("screen_name");
				}
				else {
					name = "用户"+jsonObj.getJSONObject("retweeted_status").getString("uid");
				}
				temptext = jsonObj.getJSONObject("retweeted_status").getString("text");
				temptext += "\n"+jsonObj.getJSONObject("retweeted_status").getString("created_at");
				//retweetedContent.setText(temptext);
				//dialogBox.setText(temptext);
				Map<String, String> briefInfo = new HashMap<String, String>();
				
				if(jsonObj.getJSONObject("retweeted_status").has("urls")) {
					String urlsJsonString = jsonObj.getJSONObject("retweeted_status").getString("urls");
					JSONObject urlsJsonObject = new JSONObject(urlsJsonString);
					int length = urlsJsonObject.getJSONArray("urls").length();
					for(int index=0; index<length; index++) {
						if(urlsJsonObject.getJSONArray("urls").getJSONObject(index).getInt("type")==1) {
							if(briefInfo.containsKey("video"))
								continue;
							briefInfo.put("video", urlsJsonObject.getJSONArray("urls").getJSONObject(index).getString("url_long"));
						}
						else if(urlsJsonObject.getJSONArray("urls").getJSONObject(index).getInt("type")==2) {
							if(briefInfo.containsKey("music"))
								continue;
							briefInfo.put("music", urlsJsonObject.getJSONArray("urls").getJSONObject(index).getString("url_long"));
						}
					}
				}
				
				if(jsonObj.getJSONObject("retweeted_status").has("thumbnail_pic")) {
					briefInfo.put("picture", "true");
					dialogBox.setContent(WeiboShow.this, name, briefInfo, temptext, true, true,
							jsonObj.getJSONObject("retweeted_status").getString("thumbnail_pic"),
							jsonObj.getJSONObject("retweeted_status").getString("bmiddle_pic"),
							jsonObj.getJSONObject("retweeted_status").getString("original_pic"));
				}
				else {
					dialogBox.setContent(WeiboShow.this, name, briefInfo, temptext, true, false);
				}
				dialogBox.setVisibility(View.VISIBLE);
				//((DialogBox) findViewById(R.id.dialogBox1)).setSize(120, 120);
			}
			//weiboContent.setText(temptext);
			
			String comments = dbHandler.getCommentBySeq(mid);
			if(comments!=null) {
				initCommentList(comments);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weibo_show, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings:
			readComments();
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
		if(item.getGroupId() == 0) {
			switch (item.getItemId()) {
			case 0:
				break;
			case 1:
				//Log.d("longClick", weiboContent.getText().toString());
				ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("小黑屋", weiboContent.getText().toString());
				cbm.setPrimaryClip(clip);
				break;
			case 3:
				final EditText et = new EditText(this);
				android.content.DialogInterface.OnClickListener onPosListener = new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Map<String, String> params = new HashMap<String, String>();
						params.put("url", "https://api.weibo.com/2/comments/create.json");
						params.put("source", ConstantS.APP_KEY);
						params.put("access_token", MainActivity.accessToken.getToken());
						params.put("id", mid);
						params.put("comment", et.getText().toString());
						NetworkTask commentsCreate = new NetworkTask(NETWORK_TASK.CommentsCreate, true);
						commentsCreate.execute(params);
					}
				};
				new AlertDialog.Builder(this).setTitle("想说啥呢？").setView(et)
					.setPositiveButton("确定", onPosListener)
					.setNegativeButton("取消", null)
					.show();
				break;
	
			default:
				Toast.makeText(WeiboShow.this, "还没做呢", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		else if(item.getGroupId() == 1) {
			switch (item.getItemId()) {
			case 0:
				break;
			case 1:
				String uname = null;
				try {
					JSONObject jsonObj = new JSONObject(info.targetView.getTag(R.id.tag_ori).toString());
					uname = jsonObj.getJSONObject("user").getString("name");
					cid = jsonObj.getString("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final EditText et = new EditText(this);
				android.content.DialogInterface.OnClickListener onPosListener = new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Map<String, String> params = new HashMap<String, String>();
						params.put("url", "https://api.weibo.com/2/comments/reply.json");
						params.put("source", ConstantS.APP_KEY);
						params.put("access_token", MainActivity.accessToken.getToken());
						params.put("cid", cid);
						params.put("id", mid);
						params.put("comment", et.getText().toString());
						NetworkTask commentsReply = new NetworkTask(NETWORK_TASK.CommentsReply, true);
						commentsReply.execute(params);
					}
				};
				new AlertDialog.Builder(this).setTitle("回复@"+uname+":").setView(et)
					.setPositiveButton("确定", onPosListener)
					.setNegativeButton("取消", null)
					.show();
				break;
			case 2:
				try {
					JSONObject jsonObj = new JSONObject(info.targetView.getTag(R.id.tag_ori).toString());
					cid = jsonObj.getString("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				android.content.DialogInterface.OnClickListener onPosListener_destroy = new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Map<String, String> params = new HashMap<String, String>();
						params.put("url", "https://api.weibo.com/2/comments/destroy.json");
						params.put("source", ConstantS.APP_KEY);
						params.put("access_token", MainActivity.accessToken.getToken());
						params.put("cid", cid);
						NetworkTask commentsDestroy = new NetworkTask(NETWORK_TASK.CommentsDestroy, true);
						commentsDestroy.execute(params);
					}
				};
				new AlertDialog.Builder(this).setTitle("确定删除此评论？")
					.setPositiveButton("确定", onPosListener_destroy)
					.setNegativeButton("取消", null)
					.show();
				break;
	
			default:
				Toast.makeText(WeiboShow.this, "还没做呢", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		return super.onContextItemSelected(item);
	}
	
	public void setWeiboContent(String content) {
		int loc = 0;
		int length = content.length();
		int start, end = -1;
		int temp;
		
		ImageGetter imgGetter = new ImageGetter() {
			
			@Override
			public Drawable getDrawable(String source) {
				// TODO Auto-generated method stub
				int id = Integer.parseInt(source);
				Drawable d = getResources().getDrawable(id);
				d.setBounds(0, 0, 25, 25);
				return d;
			}
		};
		weiboContent.setText("");
		weiboContent.setMovementMethod(LinkMovementMethod.getInstance());
		while((temp=MessageFormater.locateBq(content, loc))!=0) {
			start = temp/1000;
			end = temp%1000;
			String preContent;
			String code = content.substring(start, end+1);
			int rid= dbHandler.getRidByCode(code);
			if(rid==-1) {
				preContent = content.substring(loc, start+1);
				//weiboContent.append(preContent);
				formatContent(WeiboShow.this, preContent);
				loc = start+1;
			}
			else {
				preContent = content.substring(loc, start);
				//weiboContent.append(preContent);
				formatContent(WeiboShow.this, preContent);
				rid += R.drawable.bq_0000;
				weiboContent.append(Html.fromHtml("<img src=\'"+rid+"\'/>", imgGetter, null));
				loc = end+1;
			}
		}
		if(loc<length) {
			//weiboContent.append(content.substring(loc));
			formatContent(WeiboShow.this, content.substring(loc));
		}
	}
	
	private void formatContent(Context context, String content) {
		SpannableString sps;
		int start, end, length;
		length = content.length();
		start = 0;
		end = 0;
		while(start < length) {
			if(content.charAt(start) == '@') {
				//weiboContent.append(content, end, start);
				markURL(context, weiboContent, content.substring(end, start));
				end = start;
				int tmp = start+1;
				while(tmp < length && isLegal(content.charAt(tmp), false)) {
					tmp++;
				}
				if(tmp > start+1) {
					sps = new SpannableString(content.subSequence(end, tmp));
					sps.setSpan(new ClickResponse(context, sps, 0), 0, sps.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					weiboContent.append(sps);
				}
				else {
					//weiboContent.append(content, end, tmp);
					markURL(context, weiboContent, content.substring(end, tmp));
				}
				end = tmp;
				start = end;
			}
			else if(content.charAt(start) == '#') {
				//weiboContent.append(content, end, start);
				markURL(context, weiboContent, content.substring(end, start));
				int tmp = start;
				end = start+1;
				while(end < length && content.charAt(end) != '#') {
					end++;
				}
				if(end<length) {
					sps = new SpannableString(content.subSequence(start, end+1));
					sps.setSpan(new ClickResponse(context, sps, 1), 0, sps.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					weiboContent.append(sps);
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
		//weiboContent.append(content.substring(end));
		markURL(context, weiboContent, content.substring(end));
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
	
	public void readComments() {
		if(mid.length()>0) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("url", "https://api.weibo.com/2/comments/show.json");
			params.put("source", ConstantS.APP_KEY);
			params.put("access_token", MainActivity.accessToken.getToken());
			params.put("id", mid);
			if(HttpsUtils.getNetType(this.getSystemService(Context.CONNECTIVITY_SERVICE)) == 1) {
			}
			NetworkTask commentsShow = new NetworkTask(NETWORK_TASK.CommentsShow, false);
			commentsShow.execute(params);
		}
	}
	
	public void initCommentList(String commmentStr) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(commmentStr);
			int length = jsonObj.getJSONArray("comments").length();
			
			commentList.setAdapter(null);
			
			ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
			for (int i=0; i<length; i++) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("ori_data", jsonObj.getJSONArray("comments").getJSONObject(i).toString());
				
				listItem.add(item);
			}
			
			CommentAdapter listItemAdapter = new CommentAdapter(WeiboShow.this, listItem, R.layout.comment_showbox,
					new String[]{"ori_data"},
					new int[]{R.id.comment_usr_name, R.id.comment_text, R.id.comment_time});
			
			commentList.setAdapter(listItemAdapter);
			
			commentList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
				
				@Override
				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					// TODO Auto-generated method stub
					menu.setHeaderTitle("召唤我作甚吗？");
					menu.add(1, 0, 0, "不干嘛…玩~");
					menu.add(1, 1, 1, "对话？");
					menu.add(1, 2, 2, "删除评论");
				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class CommentAdapter extends BaseAdapter {
		
		private Context context;
		private ArrayList<HashMap<String, Object>> listItem;
		private int resource;
		private String[] from;
		private int[] to;
		private LayoutInflater listContainer;
		
		public final class ListItemView {
			public TextView user;
			public TextView text;
			public TextView time;
		}
		
		public CommentAdapter(Context context, List<? extends Map<String, ?>>data, int resource, String[] from, int[] to) {
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
				
				listItemView.user = (TextView) convertView.findViewById(to[0]);
				listItemView.text = (TextView) convertView.findViewById(to[1]);
				listItemView.time = (TextView) convertView.findViewById(to[2]);
				
				convertView.setTag(listItemView);
			}
			else {
				listItemView = (ListItemView) convertView.getTag();
			}
			
			try {
				JSONObject ori_json_obj = new JSONObject(listItem.get(position).get(from[0]).toString());
				listItemView.user.setText(ori_json_obj.getJSONObject("user").getString("name"));
				setWeiboContent(listItemView.text, ori_json_obj.getString("text"));
				listItemView.time.setText(ori_json_obj.getString("created_at"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			convertView.setTag(R.id.tag_ori, listItem.get(position).get(from[0]).toString());
			
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
			contentView.setMovementMethod(LinkMovementMethod.getInstance());
			while((temp=MessageFormater.locateBq(status, loc))!=0) {
				start = temp/1000;
				end = temp%1000;
				String preContent;
				String code = status.substring(start, end+1);
				int rid= dbHandler.getRidByCode(code);
				if(rid==-1) {
					preContent = status.substring(loc, start+1);
					//contentView.append(preContent);
					formatContent(WeiboShow.this, contentView, preContent);
					loc = start+1;
				}
				else {
					preContent = status.substring(loc, start);
					//contentView.append(preContent);
					formatContent(WeiboShow.this, contentView, preContent);
					rid += R.drawable.bq_0000;
					contentView.append(Html.fromHtml("<img src=\'"+rid+"\'/>", imgGetter, null));
					loc = end+1;
				}
			}
			if(loc<length) {
				//contentView.append(status.substring(loc));
				formatContent(WeiboShow.this, contentView, status.substring(loc));
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
	
	class NetworkTask extends AsyncTask<Map, Integer, String> {
		private NETWORK_TASK taskType;
		private boolean isPOST;
		public NetworkTask(NETWORK_TASK taskType, boolean isPOST) {
			this.taskType = taskType;
			this.isPOST = isPOST;
		}

		@Override
		protected String doInBackground(Map... params) {
			// TODO Auto-generated method stub
			String result = "";
			try {
				if(isPOST) {
					result = HttpsUtils.doPost(WeiboShow.this, params[0]);
				}
				else {
					result = HttpsUtils.doGet(params[0]);
				}
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
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(taskType == NETWORK_TASK.CommentsShow) {
				initCommentList(result);
				dbHandler.updateComment(mid, result);
			}
			else if((taskType == NETWORK_TASK.CommentsCreate) || (taskType == NETWORK_TASK.CommentsReply)) {
				if(result!=null) {
					try {
						JSONObject resultJsonObject = new JSONObject(result);
						if(resultJsonObject.has("error")) {
							Toast.makeText(WeiboShow.this, resultJsonObject.getString("error"), Toast.LENGTH_SHORT).show();
						}
						else if(resultJsonObject.has("id")) {
							Toast.makeText(WeiboShow.this, "评论成功！ 哇咔咔~", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else if(taskType == NETWORK_TASK.CommentsDestroy) {
				if(result!=null) {
					try {
						JSONObject resultJsonObject = new JSONObject(result);
						if(resultJsonObject.has("error")) {
							Toast.makeText(WeiboShow.this, resultJsonObject.getString("error"), Toast.LENGTH_SHORT).show();
						}
						else if(resultJsonObject.has("id")) {
							Toast.makeText(WeiboShow.this, "删除成功！ 哇咔咔~", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	

	class LoadPicTask extends AsyncTask<String, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			String imgUrl = params[0];
			return getImage(imgUrl);
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.getHeight()>2048) {
				weiboPicture.setBitmap(result, MODE.TRIM);
			}
			else {
				weiboPicture.setImageBitmap(result);
			}
		}
		
		public Bitmap getImage(String imgUrl) {
			URL url;
			Bitmap b = null;
			try {
				url = new URL(imgUrl);
				HttpURLConnection cnx = (HttpURLConnection) url.openConnection();
				cnx.setDoInput(true);
				cnx.connect();
				InputStream is = cnx.getInputStream();
				b = BitmapFactory.decodeStream(is);
				is.close();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return b;
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
