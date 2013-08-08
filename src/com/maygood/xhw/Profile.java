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

import com.maygood.xhw.ReceiveMessageActivity.ClickResponse;
import com.maygood.xhw.ReceiveMessageActivity.WeiboAdapter.ListItemView;
import com.maygood.xhw.Saloon.WeiboAdapter;
import com.maygood.xhw.app.DialogBox;
import com.maygood.xhw.app.NETWORK_TASK;
import com.maygood.xhw.data.ConstantS;
import com.maygood.xhw.data.DataBaseHandler;
import com.maygood.xhw.data.MessageFormater;
import com.maygood.xhw.net.HttpsUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.Html.ImageGetter;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends Activity {
	
	private TextView nameTextV;
	private TextView locationTextV;
	
	private ListView weiboListV;
	private ArrayList<HashMap<String, Object>> listItem;
	
	private DataBaseHandler dbHandler;
	
	public String screen_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		nameTextV = (TextView) findViewById(R.id.profile_name);
		locationTextV = (TextView) findViewById(R.id.profile_info_location);
		weiboListV = (ListView) findViewById(R.id.weibo_list);
		listItem = new ArrayList<HashMap<String,Object>>();
		
		dbHandler = new DataBaseHandler(this);
		
		//初始化profile信息
		Intent i = getIntent();
		screen_name = i.getStringExtra("screen_name");
		init(screen_name);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}
	
	private void init(String screen_name) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("url", "https://api.weibo.com/2/users/show.json");
		params.put("source", ConstantS.APP_KEY);
		params.put("access_token", MainActivity.accessToken.getToken());
		params.put("screen_name", screen_name);
		NetworkTask getProfileInfoAsync = new NetworkTask(NETWORK_TASK.UsersShow, false);
		getProfileInfoAsync.execute(params);
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
					result = HttpsUtils.doPost(Profile.this, params[0]);
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
			if(taskType == NETWORK_TASK.UsersShow) {
				if(result!=null) {
					try {
						JSONObject resultJsonObject = new JSONObject(result);
						if(resultJsonObject.has("error")) {
							Toast.makeText(Profile.this, resultJsonObject.getString("error"), Toast.LENGTH_SHORT).show();
						}
						else if(resultJsonObject.has("id")) {
							nameTextV.setText(resultJsonObject.getString("screen_name"));
							locationTextV.setText(resultJsonObject.getString("location"));
							
							listItem.clear();
							HashMap<String, Object> item = new HashMap<String, Object>();
							item.put("ori_data", resultJsonObject.getJSONObject("status").toString());
							item.put("mid", resultJsonObject.getJSONObject("status").getString("mid"));
							item.put("screen_name", screen_name);
							listItem.add(item);
							
							WeiboAdapter listItemAdapter = new WeiboAdapter(Profile.this, listItem, R.layout.weibo_showbox,
									new String[]{"ori_data", "mid", "screen_name"},
									new int[]{R.id.time_show, R.id.content_show, R.id.retweeted_show, R.id.counts_show});
							
							weiboListV.setAdapter(listItemAdapter);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else if((taskType == NETWORK_TASK.CommentsCreate) || (taskType == NETWORK_TASK.CommentsReply)) {
				if(result!=null) {
					try {
						JSONObject resultJsonObject = new JSONObject(result);
						if(resultJsonObject.has("error")) {
							Toast.makeText(Profile.this, resultJsonObject.getString("error"), Toast.LENGTH_SHORT).show();
						}
						else if(resultJsonObject.has("id")) {
							Toast.makeText(Profile.this, "评论成功！ 哇咔咔~", Toast.LENGTH_SHORT).show();
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
							Toast.makeText(Profile.this, resultJsonObject.getString("error"), Toast.LENGTH_SHORT).show();
						}
						else if(resultJsonObject.has("id")) {
							Toast.makeText(Profile.this, "删除成功！ 哇咔咔~", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
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
					String retweeted_name = ori_json_obj.getJSONObject("retweeted_status").getJSONObject("user").getString("screen_name");
					String retweeted_time = MessageFormater.getDateString(ori_json_obj.getJSONObject("retweeted_status").getString("created_at"));
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
					//retweeted_status += "\n"+MessageFormater.getDateString(ori_json_obj.getJSONObject("retweeted_status").getString("created_at"));
					
					listItemView.retweeted.setContent(Profile.this, retweeted_name, briefInfo, retweeted_status, retweeted_time, false, false);
					listItemView.retweeted.setVisibility(View.VISIBLE);
					
					String screen_name = listItem.get(position).get(from[2]).toString();
					
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
					formatContent(Profile.this, contentView, preContent);
					loc = start+1;
				}
				else {
					preContent = status.substring(loc, start);
					//contentView.append(preContent);
					formatContent(Profile.this, contentView, preContent);
					rid += R.drawable.bq_0000;
					contentView.append(Html.fromHtml("<img src=\'"+rid+"\'/>", imgGetter, null));
					loc = end+1;
				}
			}
			if(loc<length) {
				//contentView.append(status.substring(loc));
				formatContent(Profile.this, contentView, status.substring(loc));
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
