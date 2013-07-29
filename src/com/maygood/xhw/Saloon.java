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

import com.maygood.xhw.app.DialogBox;
import com.maygood.xhw.data.ConstantS;
import com.maygood.xhw.data.DataBaseHandler;
import com.maygood.xhw.data.MessageFormater;
import com.maygood.xhw.net.HttpsUtils;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.Html.ImageGetter;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class Saloon extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	private ActionBar actionBar;
	
	private ListView weiboList;
	private ArrayList<HashMap<String, Object>> listItem;
	
	private DataBaseHandler dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saloon);

		// Set up the action bar to show a dropdown list.
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3), }), this);
		
		weiboList = (ListView)findViewById(R.id.weiboList_saloon);
		
		RelativeLayout footer = (RelativeLayout) LayoutInflater.from(Saloon.this).inflate(R.layout.list_add_more, null);
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
					String maxId = "0";
					if(pos>0) {
						long _id = Long.parseLong(view.getTag(R.id.tag_mid).toString());
						maxId = Long.toString(_id-1);
					}
					getWeiboMessage(maxId);
				}
				else {
					String ori = view.getTag(R.id.tag_ori).toString();
					Intent i_weibo = new Intent(Saloon.this, WeiboShow.class);
					i_weibo.putExtra("ori", ori);
					startActivity(i_weibo);
				}
			}
		});
		
		dbHandler = new DataBaseHandler(this);
		
		/*
		 * 更新表情库
		 */
		//String bqString = "[{\"rid\":\"0\",\"groupname\":\"\",\"code\":\"[草泥马]\"},{\"rid\":\"1\",\"groupname\":\"\",\"code\":\"[神马]\"},{\"rid\":\"2\",\"groupname\":\"\",\"code\":\"[浮云]\"},{\"rid\":\"3\",\"groupname\":\"\",\"code\":\"[给力]\"},{\"rid\":\"4\",\"groupname\":\"\",\"code\":\"[围观]\"},{\"rid\":\"5\",\"groupname\":\"\",\"code\":\"[威武]\"},{\"rid\":\"6\",\"groupname\":\"\",\"code\":\"[熊猫]\"},{\"rid\":\"7\",\"groupname\":\"\",\"code\":\"[兔子]\"},{\"rid\":\"8\",\"groupname\":\"\",\"code\":\"[奥特曼]\"},{\"rid\":\"9\",\"groupname\":\"\",\"code\":\"[囧]\"},{\"rid\":\"10\",\"groupname\":\"\",\"code\":\"[互粉]\"},{\"rid\":\"11\",\"groupname\":\"\",\"code\":\"[礼物]\"},{\"rid\":\"12\",\"groupname\":\"\",\"code\":\"[呵呵]\"},{\"rid\":\"13\",\"groupname\":\"\",\"code\":\"[嘻嘻]\"},{\"rid\":\"14\",\"groupname\":\"\",\"code\":\"[哈哈]\"},{\"rid\":\"15\",\"groupname\":\"\",\"code\":\"[可爱]\"},{\"rid\":\"16\",\"groupname\":\"\",\"code\":\"[可怜]\"},{\"rid\":\"17\",\"groupname\":\"\",\"code\":\"[挖鼻屎]\"},{\"rid\":\"18\",\"groupname\":\"\",\"code\":\"[吃惊]\"},{\"rid\":\"19\",\"groupname\":\"\",\"code\":\"[害羞]\"},{\"rid\":\"20\",\"groupname\":\"\",\"code\":\"[挤眼]\"},{\"rid\":\"21\",\"groupname\":\"\",\"code\":\"[闭嘴]\"},{\"rid\":\"22\",\"groupname\":\"\",\"code\":\"[鄙视]\"},{\"rid\":\"23\",\"groupname\":\"\",\"code\":\"[爱你]\"},{\"rid\":\"24\",\"groupname\":\"\",\"code\":\"[泪]\"},{\"rid\":\"25\",\"groupname\":\"\",\"code\":\"[偷笑]\"},{\"rid\":\"26\",\"groupname\":\"\",\"code\":\"[亲亲]\"},{\"rid\":\"27\",\"groupname\":\"\",\"code\":\"[生病]\"},{\"rid\":\"28\",\"groupname\":\"\",\"code\":\"[太开心]\"},{\"rid\":\"29\",\"groupname\":\"\",\"code\":\"[懒得理你]\"},{\"rid\":\"30\",\"groupname\":\"\",\"code\":\"[右哼哼]\"},{\"rid\":\"31\",\"groupname\":\"\",\"code\":\"[左哼哼]\"},{\"rid\":\"32\",\"groupname\":\"\",\"code\":\"[嘘]\"},{\"rid\":\"33\",\"groupname\":\"\",\"code\":\"[衰]\"},{\"rid\":\"34\",\"groupname\":\"\",\"code\":\"[委屈]\"},{\"rid\":\"35\",\"groupname\":\"\",\"code\":\"[吐]\"},{\"rid\":\"36\",\"groupname\":\"\",\"code\":\"[打哈欠]\"},{\"rid\":\"37\",\"groupname\":\"\",\"code\":\"[抱抱]\"},{\"rid\":\"38\",\"groupname\":\"\",\"code\":\"[怒]\"},{\"rid\":\"39\",\"groupname\":\"\",\"code\":\"[疑问]\"},{\"rid\":\"40\",\"groupname\":\"\",\"code\":\"[馋嘴]\"},{\"rid\":\"41\",\"groupname\":\"\",\"code\":\"[拜拜]\"},{\"rid\":\"42\",\"groupname\":\"\",\"code\":\"[思考]\"},{\"rid\":\"43\",\"groupname\":\"\",\"code\":\"[汗]\"},{\"rid\":\"44\",\"groupname\":\"\",\"code\":\"[困]\"},{\"rid\":\"45\",\"groupname\":\"\",\"code\":\"[睡觉]\"},{\"rid\":\"46\",\"groupname\":\"\",\"code\":\"[钱]\"},{\"rid\":\"47\",\"groupname\":\"\",\"code\":\"[失望]\"},{\"rid\":\"48\",\"groupname\":\"\",\"code\":\"[酷]\"},{\"rid\":\"49\",\"groupname\":\"\",\"code\":\"[花心]\"},{\"rid\":\"50\",\"groupname\":\"\",\"code\":\"[哼]\"},{\"rid\":\"51\",\"groupname\":\"\",\"code\":\"[鼓掌]\"},{\"rid\":\"52\",\"groupname\":\"\",\"code\":\"[晕]\"},{\"rid\":\"53\",\"groupname\":\"\",\"code\":\"[悲伤]\"},{\"rid\":\"54\",\"groupname\":\"\",\"code\":\"[抓狂]\"},{\"rid\":\"55\",\"groupname\":\"\",\"code\":\"[黑线]\"},{\"rid\":\"56\",\"groupname\":\"\",\"code\":\"[阴险]\"},{\"rid\":\"57\",\"groupname\":\"\",\"code\":\"[怒骂]\"},{\"rid\":\"58\",\"groupname\":\"\",\"code\":\"[心]\"},{\"rid\":\"59\",\"groupname\":\"\",\"code\":\"[伤心]\"},{\"rid\":\"60\",\"groupname\":\"\",\"code\":\"[猪头]\"},{\"rid\":\"61\",\"groupname\":\"\",\"code\":\"[ok]\"},{\"rid\":\"62\",\"groupname\":\"\",\"code\":\"[耶]\"},{\"rid\":\"63\",\"groupname\":\"\",\"code\":\"[good]\"},{\"rid\":\"64\",\"groupname\":\"\",\"code\":\"[不要]\"},{\"rid\":\"65\",\"groupname\":\"\",\"code\":\"[赞]\"},{\"rid\":\"66\",\"groupname\":\"\",\"code\":\"[来]\"},{\"rid\":\"67\",\"groupname\":\"\",\"code\":\"[弱]\"},{\"rid\":\"68\",\"groupname\":\"\",\"code\":\"[蜡烛]\"},{\"rid\":\"69\",\"groupname\":\"\",\"code\":\"[钟]\"},{\"rid\":\"70\",\"groupname\":\"\",\"code\":\"[话筒]\"},{\"rid\":\"71\",\"groupname\":\"\",\"code\":\"[蛋糕]\"},{\"rid\":\"72\",\"groupname\":\"\",\"code\":\"[得意地笑]\"},{\"rid\":\"73\",\"groupname\":\"\",\"code\":\"[笑哈哈]\"},{\"rid\":\"74\",\"groupname\":\"\",\"code\":\"[转发]\"},{\"rid\":\"75\",\"groupname\":\"\",\"code\":\"[偷乐]\"},{\"rid\":\"76\",\"groupname\":\"\",\"code\":\"[bm可爱]\"},{\"rid\":\"77\",\"groupname\":\"\",\"code\":\"[lt切克闹]\"},{\"rid\":\"78\",\"groupname\":\"\",\"code\":\"[xkl转圈]\"},{\"rid\":\"79\",\"groupname\":\"\",\"code\":\"[ali哇]\"},{\"rid\":\"80\",\"groupname\":\"\",\"code\":\"[ppb鼓掌]\"},{\"rid\":\"81\",\"groupname\":\"\",\"code\":\"[din推撞]\"},{\"rid\":\"82\",\"groupname\":\"\",\"code\":\"[xb压力]\"},{\"rid\":\"83\",\"groupname\":\"\",\"code\":\"[得瑟]\"},{\"rid\":\"84\",\"groupname\":\"\",\"code\":\"[ali皇冠]\"},{\"rid\":\"85\",\"groupname\":\"\",\"code\":\"[zxh得瑟]\"},{\"rid\":\"86\",\"groupname\":\"\",\"code\":\"[ppb生日快乐]\"},{\"rid\":\"87\",\"groupname\":\"\",\"code\":\"[nono生日快乐]\"},{\"rid\":\"88\",\"groupname\":\"\",\"code\":\"[nono得瑟]\"},{\"rid\":\"89\",\"groupname\":\"\",\"code\":\"[moc生日快乐]\"},{\"rid\":\"90\",\"groupname\":\"\",\"code\":\"[lxhx生日快乐]\"},{\"rid\":\"91\",\"groupname\":\"\",\"code\":\"[gst生日快乐]\"},{\"rid\":\"92\",\"groupname\":\"\",\"code\":\"[bh彪悍]\"},{\"rid\":\"93\",\"groupname\":\"\",\"code\":\"[酷库熊顽皮]\"},{\"rid\":\"94\",\"groupname\":\"\",\"code\":\"[放假啦]\"},{\"rid\":\"95\",\"groupname\":\"\",\"code\":\"[许愿]\"},{\"rid\":\"96\",\"groupname\":\"\",\"code\":\"[offthewall]\"},{\"rid\":\"97\",\"groupname\":\"\",\"code\":\"[ppb愚人节]\"},{\"rid\":\"98\",\"groupname\":\"\",\"code\":\"[助力山东]\"},{\"rid\":\"99\",\"groupname\":\"\",\"code\":\"[助力广东]\"},{\"rid\":\"100\",\"groupname\":\"\",\"code\":\"[BOBO爱你]\"},{\"rid\":\"101\",\"groupname\":\"\",\"code\":\"[雷锋]\"},{\"rid\":\"102\",\"groupname\":\"\",\"code\":\"[ppb吃汤圆]\"},{\"rid\":\"103\",\"groupname\":\"\",\"code\":\"[过年啦]\"},{\"rid\":\"104\",\"groupname\":\"\",\"code\":\"[耍花灯]\"},{\"rid\":\"105\",\"groupname\":\"\",\"code\":\"[吃汤圆]\"},{\"rid\":\"106\",\"groupname\":\"\",\"code\":\"[元宵快乐]\"},{\"rid\":\"107\",\"groupname\":\"\",\"code\":\"[崩溃]\"},{\"rid\":\"108\",\"groupname\":\"\",\"code\":\"[hold住]\"},{\"rid\":\"109\",\"groupname\":\"\",\"code\":\"[带感]\"},{\"rid\":\"110\",\"groupname\":\"\",\"code\":\"[xyj拜年]\"},{\"rid\":\"111\",\"groupname\":\"\",\"code\":\"[toto拜年]\"},{\"rid\":\"112\",\"groupname\":\"\",\"code\":\"[mtjj拜年]\"},{\"rid\":\"113\",\"groupname\":\"\",\"code\":\"[mk拜年]\"},{\"rid\":\"114\",\"groupname\":\"\",\"code\":\"[泪流满面]\"},{\"rid\":\"115\",\"groupname\":\"\",\"code\":\"[gst耐你]\"},{\"rid\":\"116\",\"groupname\":\"\",\"code\":\"[moc转发]\"},{\"rid\":\"117\",\"groupname\":\"\",\"code\":\"[江南style]\"},{\"rid\":\"118\",\"groupname\":\"\",\"code\":\"[国旗]\"},{\"rid\":\"119\",\"groupname\":\"浪小花\",\"code\":\"[转发]\"},{\"rid\":\"120\",\"groupname\":\"浪小花\",\"code\":\"[笑哈哈]\"},{\"rid\":\"121\",\"groupname\":\"浪小花\",\"code\":\"[得意地笑]\"},{\"rid\":\"122\",\"groupname\":\"浪小花\",\"code\":\"[噢耶]\"},{\"rid\":\"123\",\"groupname\":\"浪小花\",\"code\":\"[偷乐]\"},{\"rid\":\"124\",\"groupname\":\"浪小花\",\"code\":\"[泪流满面]\"},{\"rid\":\"125\",\"groupname\":\"浪小花\",\"code\":\"[巨汗]\"},{\"rid\":\"126\",\"groupname\":\"浪小花\",\"code\":\"[抠鼻屎]\"},{\"rid\":\"127\",\"groupname\":\"浪小花\",\"code\":\"[求关注]\"},{\"rid\":\"128\",\"groupname\":\"浪小花\",\"code\":\"[真V5]\"},{\"rid\":\"129\",\"groupname\":\"浪小花\",\"code\":\"[群体围观]\"},{\"rid\":\"130\",\"groupname\":\"浪小花\",\"code\":\"[hold住]\"},{\"rid\":\"131\",\"groupname\":\"浪小花\",\"code\":\"[羞嗒嗒]\"},{\"rid\":\"132\",\"groupname\":\"浪小花\",\"code\":\"[非常汗]\"},{\"rid\":\"133\",\"groupname\":\"浪小花\",\"code\":\"[许愿]\"},{\"rid\":\"134\",\"groupname\":\"浪小花\",\"code\":\"[崩溃]\"},{\"rid\":\"135\",\"groupname\":\"浪小花\",\"code\":\"[好囧]\"},{\"rid\":\"136\",\"groupname\":\"浪小花\",\"code\":\"[震惊]\"},{\"rid\":\"137\",\"groupname\":\"浪小花\",\"code\":\"[别烦我]\"},{\"rid\":\"138\",\"groupname\":\"浪小花\",\"code\":\"[不好意思]\"},{\"rid\":\"139\",\"groupname\":\"浪小花\",\"code\":\"[纠结]\"},{\"rid\":\"140\",\"groupname\":\"浪小花\",\"code\":\"[拍手]\"},{\"rid\":\"141\",\"groupname\":\"浪小花\",\"code\":\"[给劲]\"},{\"rid\":\"142\",\"groupname\":\"浪小花\",\"code\":\"[好喜欢]\"},{\"rid\":\"143\",\"groupname\":\"浪小花\",\"code\":\"[好爱哦]\"},{\"rid\":\"144\",\"groupname\":\"浪小花\",\"code\":\"[路过这儿]\"},{\"rid\":\"145\",\"groupname\":\"浪小花\",\"code\":\"[悲催]\"},{\"rid\":\"146\",\"groupname\":\"浪小花\",\"code\":\"[不想上班]\"},{\"rid\":\"147\",\"groupname\":\"浪小花\",\"code\":\"[躁狂症]\"},{\"rid\":\"148\",\"groupname\":\"浪小花\",\"code\":\"[甩甩手]\"},{\"rid\":\"149\",\"groupname\":\"浪小花\",\"code\":\"[瞧瞧]\"},{\"rid\":\"150\",\"groupname\":\"浪小花\",\"code\":\"[同意]\"},{\"rid\":\"151\",\"groupname\":\"浪小花\",\"code\":\"[喝多了]\"},{\"rid\":\"152\",\"groupname\":\"浪小花\",\"code\":\"[啦啦啦啦]\"},{\"rid\":\"153\",\"groupname\":\"浪小花\",\"code\":\"[杰克逊]\"},{\"rid\":\"154\",\"groupname\":\"浪小花\",\"code\":\"[雷锋]\"},{\"rid\":\"155\",\"groupname\":\"浪小花\",\"code\":\"[带感]\"},{\"rid\":\"156\",\"groupname\":\"浪小花\",\"code\":\"[亲一口]\"},{\"rid\":\"157\",\"groupname\":\"浪小花\",\"code\":\"[飞个吻]\"},{\"rid\":\"158\",\"groupname\":\"浪小花\",\"code\":\"[加油啊]\"},{\"rid\":\"159\",\"groupname\":\"浪小花\",\"code\":\"[七夕]\"},{\"rid\":\"160\",\"groupname\":\"浪小花\",\"code\":\"[困死了]\"},{\"rid\":\"161\",\"groupname\":\"浪小花\",\"code\":\"[有鸭梨]\"},{\"rid\":\"162\",\"groupname\":\"浪小花\",\"code\":\"[右边亮了]\"},{\"rid\":\"163\",\"groupname\":\"浪小花\",\"code\":\"[撒花]\"},{\"rid\":\"164\",\"groupname\":\"浪小花\",\"code\":\"[好棒]\"},{\"rid\":\"165\",\"groupname\":\"浪小花\",\"code\":\"[想一想]\"},{\"rid\":\"166\",\"groupname\":\"浪小花\",\"code\":\"[下班]\"},{\"rid\":\"167\",\"groupname\":\"浪小花\",\"code\":\"[最右]\"},{\"rid\":\"168\",\"groupname\":\"浪小花\",\"code\":\"[丘比特]\"},{\"rid\":\"169\",\"groupname\":\"浪小花\",\"code\":\"[中箭]\"},{\"rid\":\"170\",\"groupname\":\"浪小花\",\"code\":\"[互相膜拜]\"},{\"rid\":\"171\",\"groupname\":\"浪小花\",\"code\":\"[膜拜了]\"},{\"rid\":\"172\",\"groupname\":\"浪小花\",\"code\":\"[放电抛媚]\"},{\"rid\":\"173\",\"groupname\":\"浪小花\",\"code\":\"[霹雳]\"},{\"rid\":\"174\",\"groupname\":\"浪小花\",\"code\":\"[被电]\"},{\"rid\":\"175\",\"groupname\":\"浪小花\",\"code\":\"[拍砖]\"},{\"rid\":\"176\",\"groupname\":\"浪小花\",\"code\":\"[互相拍砖]\"},{\"rid\":\"177\",\"groupname\":\"浪小花\",\"code\":\"[采访]\"},{\"rid\":\"178\",\"groupname\":\"浪小花\",\"code\":\"[发表言论]\"},{\"rid\":\"179\",\"groupname\":\"浪小花\",\"code\":\"[江南style]\"},{\"rid\":\"180\",\"groupname\":\"浪小花\",\"code\":\"[牛]\"},{\"rid\":\"181\",\"groupname\":\"浪小花\",\"code\":\"[玫瑰]\"},{\"rid\":\"182\",\"groupname\":\"浪小花\",\"code\":\"[赞啊]\"},{\"rid\":\"183\",\"groupname\":\"浪小花\",\"code\":\"[推荐]\"},{\"rid\":\"184\",\"groupname\":\"浪小花\",\"code\":\"[放假啦]\"},{\"rid\":\"185\",\"groupname\":\"浪小花\",\"code\":\"[萌翻]\"},{\"rid\":\"186\",\"groupname\":\"浪小花\",\"code\":\"[吃货]\"},{\"rid\":\"187\",\"groupname\":\"浪小花\",\"code\":\"[大南瓜]\"},{\"rid\":\"188\",\"groupname\":\"浪小花\",\"code\":\"[赶火车]\"},{\"rid\":\"189\",\"groupname\":\"浪小花\",\"code\":\"[立志青年]\"},{\"rid\":\"190\",\"groupname\":\"浪小花\",\"code\":\"[得瑟]\"},{\"rid\":\"191\",\"groupname\":\"浪小花\",\"code\":\"[月儿圆]\"},{\"rid\":\"192\",\"groupname\":\"浪小花\",\"code\":\"[招财]\"},{\"rid\":\"193\",\"groupname\":\"浪小花\",\"code\":\"[微博三岁啦]\"},{\"rid\":\"194\",\"groupname\":\"浪小花\",\"code\":\"[复活节]\"},{\"rid\":\"195\",\"groupname\":\"浪小花\",\"code\":\"[挤火车]\"},{\"rid\":\"196\",\"groupname\":\"浪小花\",\"code\":\"[愚人节]\"},{\"rid\":\"197\",\"groupname\":\"浪小花\",\"code\":\"[收藏]\"},{\"rid\":\"198\",\"groupname\":\"浪小花\",\"code\":\"[喜得金牌]\"},{\"rid\":\"199\",\"groupname\":\"浪小花\",\"code\":\"[夺冠感动]\"},{\"rid\":\"200\",\"groupname\":\"浪小花\",\"code\":\"[冠军诞生]\"},{\"rid\":\"201\",\"groupname\":\"浪小花\",\"code\":\"[传火炬]\"},{\"rid\":\"202\",\"groupname\":\"浪小花\",\"code\":\"[奥运金牌]\"},{\"rid\":\"203\",\"groupname\":\"浪小花\",\"code\":\"[奥运银牌]\"},{\"rid\":\"204\",\"groupname\":\"浪小花\",\"code\":\"[奥运铜牌]\"},{\"rid\":\"205\",\"groupname\":\"浪小花\",\"code\":\"[德国队加油]\"},{\"rid\":\"206\",\"groupname\":\"浪小花\",\"code\":\"[西班牙队加油]\"},{\"rid\":\"207\",\"groupname\":\"浪小花\",\"code\":\"[葡萄牙队加油]\"},{\"rid\":\"208\",\"groupname\":\"浪小花\",\"code\":\"[意大利队加油]\"},{\"rid\":\"209\",\"groupname\":\"浪小花\",\"code\":\"[耍花灯]\"},{\"rid\":\"210\",\"groupname\":\"浪小花\",\"code\":\"[元宵快乐]\"},{\"rid\":\"211\",\"groupname\":\"浪小花\",\"code\":\"[吃汤圆]\"},{\"rid\":\"212\",\"groupname\":\"浪小花\",\"code\":\"[金元宝]\"},{\"rid\":\"213\",\"groupname\":\"浪小花\",\"code\":\"[红包拿来]\"},{\"rid\":\"214\",\"groupname\":\"浪小花\",\"code\":\"[福到啦]\"},{\"rid\":\"215\",\"groupname\":\"浪小花\",\"code\":\"[放鞭炮]\"},{\"rid\":\"216\",\"groupname\":\"浪小花\",\"code\":\"[发红包]\"},{\"rid\":\"217\",\"groupname\":\"浪小花\",\"code\":\"[大红灯笼]\"},{\"rid\":\"218\",\"groupname\":\"浪小花\",\"code\":\"[拜年了]\"},{\"rid\":\"219\",\"groupname\":\"浪小花\",\"code\":\"[龙啸]\"},{\"rid\":\"220\",\"groupname\":\"浪小花\",\"code\":\"[光棍节]\"},{\"rid\":\"221\",\"groupname\":\"浪小花\",\"code\":\"[会员一周年]\"},{\"rid\":\"222\",\"groupname\":\"浪小花\",\"code\":\"[蛇年快乐]\"},{\"rid\":\"223\",\"groupname\":\"浪小花\",\"code\":\"[过年啦]\"},{\"rid\":\"224\",\"groupname\":\"浪小花\",\"code\":\"[圆蛋快乐]\"},{\"rid\":\"225\",\"groupname\":\"浪小花\",\"code\":\"[发礼物]\"},{\"rid\":\"226\",\"groupname\":\"浪小花\",\"code\":\"[要礼物]\"},{\"rid\":\"227\",\"groupname\":\"浪小花\",\"code\":\"[平安果]\"},{\"rid\":\"228\",\"groupname\":\"浪小花\",\"code\":\"[吓到了]\"},{\"rid\":\"229\",\"groupname\":\"浪小花\",\"code\":\"[走你]\"},{\"rid\":\"230\",\"groupname\":\"浪小花\",\"code\":\"[吐血]\"},{\"rid\":\"231\",\"groupname\":\"浪小花\",\"code\":\"[好激动]\"},{\"rid\":\"232\",\"groupname\":\"浪小花\",\"code\":\"[没人疼]\"},{\"rid\":\"233\",\"groupname\":\"酷库熊\",\"code\":\"[酷库熊眨眼]\"},{\"rid\":\"234\",\"groupname\":\"酷库熊\",\"code\":\"[酷库熊微笑]\"},{\"rid\":\"235\",\"groupname\":\"酷库熊\",\"code\":\"[酷库熊委屈]\"},{\"rid\":\"236\",\"groupname\":\"酷库熊\",\"code\":\"[酷库熊顽皮]\"},{\"rid\":\"237\",\"groupname\":\"酷库熊\",\"code\":\"[酷库熊怒]\"},{\"rid\":\"238\",\"groupname\":\"酷库熊\",\"code\":\"[酷库熊哭泣]\"},{\"rid\":\"239\",\"groupname\":\"酷库熊\",\"code\":\"[酷库熊囧]\"},{\"rid\":\"240\",\"groupname\":\"酷库熊\",\"code\":\"[酷库熊坏笑]\"},{\"rid\":\"241\",\"groupname\":\"酷库熊\",\"code\":\"[酷库熊汗]\"},{\"rid\":\"242\",\"groupname\":\"酷库熊\",\"code\":\"[酷库熊点头]\"},{\"rid\":\"243\",\"groupname\":\"暴走漫画\",\"code\":\"[bm做操]\"},{\"rid\":\"244\",\"groupname\":\"暴走漫画\",\"code\":\"[bm抓狂]\"},{\"rid\":\"245\",\"groupname\":\"暴走漫画\",\"code\":\"[bm中枪]\"},{\"rid\":\"246\",\"groupname\":\"暴走漫画\",\"code\":\"[bm震惊]\"},{\"rid\":\"247\",\"groupname\":\"暴走漫画\",\"code\":\"[bm赞]\"},{\"rid\":\"248\",\"groupname\":\"暴走漫画\",\"code\":\"[bm喜悦]\"},{\"rid\":\"249\",\"groupname\":\"暴走漫画\",\"code\":\"[bm醒悟]\"},{\"rid\":\"250\",\"groupname\":\"暴走漫画\",\"code\":\"[bm兴奋]\"},{\"rid\":\"251\",\"groupname\":\"暴走漫画\",\"code\":\"[bm血泪]\"},{\"rid\":\"252\",\"groupname\":\"暴走漫画\",\"code\":\"[bm挖鼻孔]\"},{\"rid\":\"253\",\"groupname\":\"暴走漫画\",\"code\":\"[bm吐舌头]\"},{\"rid\":\"254\",\"groupname\":\"暴走漫画\",\"code\":\"[bm吐槽]\"},{\"rid\":\"255\",\"groupname\":\"暴走漫画\",\"code\":\"[bm投诉]\"},{\"rid\":\"256\",\"groupname\":\"暴走漫画\",\"code\":\"[bm跳绳]\"},{\"rid\":\"257\",\"groupname\":\"暴走漫画\",\"code\":\"[bm调皮]\"},{\"rid\":\"258\",\"groupname\":\"暴走漫画\",\"code\":\"[bm讨论]\"},{\"rid\":\"259\",\"groupname\":\"暴走漫画\",\"code\":\"[bm抬腿]\"},{\"rid\":\"260\",\"groupname\":\"暴走漫画\",\"code\":\"[bm思考]\"},{\"rid\":\"261\",\"groupname\":\"暴走漫画\",\"code\":\"[bm生气]\"},{\"rid\":\"262\",\"groupname\":\"暴走漫画\",\"code\":\"[bm亲吻]\"},{\"rid\":\"263\",\"groupname\":\"暴走漫画\",\"code\":\"[bm庆幸]\"},{\"rid\":\"264\",\"groupname\":\"暴走漫画\",\"code\":\"[bm内涵]\"},{\"rid\":\"265\",\"groupname\":\"暴走漫画\",\"code\":\"[bm忙碌]\"},{\"rid\":\"266\",\"groupname\":\"暴走漫画\",\"code\":\"[bm乱入]\"},{\"rid\":\"267\",\"groupname\":\"暴走漫画\",\"code\":\"[bm卖萌]\"},{\"rid\":\"268\",\"groupname\":\"暴走漫画\",\"code\":\"[bm流泪]\"},{\"rid\":\"269\",\"groupname\":\"暴走漫画\",\"code\":\"[bm流口水]\"},{\"rid\":\"270\",\"groupname\":\"暴走漫画\",\"code\":\"[bm流鼻涕]\"},{\"rid\":\"271\",\"groupname\":\"暴走漫画\",\"code\":\"[bm路过]\"},{\"rid\":\"272\",\"groupname\":\"暴走漫画\",\"code\":\"[bm咧嘴]\"},{\"rid\":\"273\",\"groupname\":\"暴走漫画\",\"code\":\"[bm啦啦队]\"},{\"rid\":\"274\",\"groupname\":\"暴走漫画\",\"code\":\"[bm哭诉]\"},{\"rid\":\"275\",\"groupname\":\"暴走漫画\",\"code\":\"[bm哭泣]\"},{\"rid\":\"276\",\"groupname\":\"暴走漫画\",\"code\":\"[bm苦逼]\"},{\"rid\":\"277\",\"groupname\":\"暴走漫画\",\"code\":\"[bm口哨]\"},{\"rid\":\"278\",\"groupname\":\"暴走漫画\",\"code\":\"[bm可爱]\"},{\"rid\":\"279\",\"groupname\":\"暴走漫画\",\"code\":\"[bm紧张]\"},{\"rid\":\"280\",\"groupname\":\"暴走漫画\",\"code\":\"[bm惊讶]\"},{\"rid\":\"281\",\"groupname\":\"暴走漫画\",\"code\":\"[bm惊吓]\"},{\"rid\":\"282\",\"groupname\":\"暴走漫画\",\"code\":\"[bm焦虑]\"},{\"rid\":\"283\",\"groupname\":\"暴走漫画\",\"code\":\"[bm会心笑]\"},{\"rid\":\"284\",\"groupname\":\"暴走漫画\",\"code\":\"[bm坏笑]\"},{\"rid\":\"285\",\"groupname\":\"暴走漫画\",\"code\":\"[bm花痴]\"},{\"rid\":\"286\",\"groupname\":\"暴走漫画\",\"code\":\"[bm厚脸皮]\"},{\"rid\":\"287\",\"groupname\":\"暴走漫画\",\"code\":\"[bm好吧]\"},{\"rid\":\"288\",\"groupname\":\"暴走漫画\",\"code\":\"[bm害怕]\"},{\"rid\":\"289\",\"groupname\":\"暴走漫画\",\"code\":\"[bm鬼脸]\"},{\"rid\":\"290\",\"groupname\":\"暴走漫画\",\"code\":\"[bm孤独]\"},{\"rid\":\"291\",\"groupname\":\"暴走漫画\",\"code\":\"[bm高兴]\"},{\"rid\":\"292\",\"groupname\":\"暴走漫画\",\"code\":\"[bm搞怪]\"},{\"rid\":\"293\",\"groupname\":\"暴走漫画\",\"code\":\"[bm干笑]\"},{\"rid\":\"294\",\"groupname\":\"暴走漫画\",\"code\":\"[bm感动]\"},{\"rid\":\"295\",\"groupname\":\"暴走漫画\",\"code\":\"[bm愤懑]\"},{\"rid\":\"296\",\"groupname\":\"暴走漫画\",\"code\":\"[bm反对]\"},{\"rid\":\"297\",\"groupname\":\"暴走漫画\",\"code\":\"[bm踱步]\"},{\"rid\":\"298\",\"groupname\":\"暴走漫画\",\"code\":\"[bm顶]\"},{\"rid\":\"299\",\"groupname\":\"暴走漫画\",\"code\":\"[bm得意]\"},{\"rid\":\"300\",\"groupname\":\"暴走漫画\",\"code\":\"[bm得瑟]\"},{\"rid\":\"301\",\"groupname\":\"暴走漫画\",\"code\":\"[bm大笑]\"},{\"rid\":\"302\",\"groupname\":\"暴走漫画\",\"code\":\"[bm蛋糕]\"},{\"rid\":\"303\",\"groupname\":\"暴走漫画\",\"code\":\"[bm大哭]\"},{\"rid\":\"304\",\"groupname\":\"暴走漫画\",\"code\":\"[bm大叫]\"},{\"rid\":\"305\",\"groupname\":\"暴走漫画\",\"code\":\"[bm吃惊]\"},{\"rid\":\"306\",\"groupname\":\"暴走漫画\",\"code\":\"[bm馋]\"},{\"rid\":\"307\",\"groupname\":\"暴走漫画\",\"code\":\"[bm彩色]\"},{\"rid\":\"308\",\"groupname\":\"暴走漫画\",\"code\":\"[bm缤纷]\"},{\"rid\":\"309\",\"groupname\":\"暴走漫画\",\"code\":\"[bm变身]\"},{\"rid\":\"310\",\"groupname\":\"暴走漫画\",\"code\":\"[bm悲催]\"},{\"rid\":\"311\",\"groupname\":\"暴走漫画\",\"code\":\"[bm暴怒]\"},{\"rid\":\"312\",\"groupname\":\"暴走漫画\",\"code\":\"[bm熬夜]\"},{\"rid\":\"313\",\"groupname\":\"暴走漫画\",\"code\":\"[bm暗爽]\"},{\"rid\":\"314\",\"groupname\":\"小恐龙\",\"code\":\"[xkl怒火]\"},{\"rid\":\"315\",\"groupname\":\"小恐龙\",\"code\":\"[xkl转圈]\"},{\"rid\":\"316\",\"groupname\":\"小恐龙\",\"code\":\"[xkl喜]\"},{\"rid\":\"317\",\"groupname\":\"小恐龙\",\"code\":\"[xkl委屈]\"},{\"rid\":\"318\",\"groupname\":\"小恐龙\",\"code\":\"[xkl石化]\"},{\"rid\":\"319\",\"groupname\":\"小恐龙\",\"code\":\"[xkl期待]\"},{\"rid\":\"320\",\"groupname\":\"小恐龙\",\"code\":\"[xkl捏脸]\"},{\"rid\":\"321\",\"groupname\":\"小恐龙\",\"code\":\"[xkl路过]\"},{\"rid\":\"322\",\"groupname\":\"小恐龙\",\"code\":\"[xkl哈哈哈]\"},{\"rid\":\"323\",\"groupname\":\"小恐龙\",\"code\":\"[xkl顶]\"},{\"rid\":\"324\",\"groupname\":\"癫当\",\"code\":\"[din癫当圣诞礼物]\"},{\"rid\":\"325\",\"groupname\":\"癫当\",\"code\":\"[din看看]\"},{\"rid\":\"326\",\"groupname\":\"癫当\",\"code\":\"[din说话]\"},{\"rid\":\"327\",\"groupname\":\"癫当\",\"code\":\"[din阿赞招财猫]\"},{\"rid\":\"328\",\"groupname\":\"癫当\",\"code\":\"[din阿赞福到]\"},{\"rid\":\"329\",\"groupname\":\"癫当\",\"code\":\"[din癫当招财猫]\"},{\"rid\":\"330\",\"groupname\":\"癫当\",\"code\":\"[din癫当xmas]\"},{\"rid\":\"331\",\"groupname\":\"癫当\",\"code\":\"[din癫当圣诞树]\"},{\"rid\":\"332\",\"groupname\":\"癫当\",\"code\":\"[din癫当阿赞圣诞奔]\"},{\"rid\":\"333\",\"groupname\":\"癫当\",\"code\":\"[din癫当阿赞炮竹]\"},{\"rid\":\"334\",\"groupname\":\"癫当\",\"code\":\"[din癫当阿赞礼物盒]\"},{\"rid\":\"335\",\"groupname\":\"癫当\",\"code\":\"[din癫当阿赞礼物]\"},{\"rid\":\"336\",\"groupname\":\"癫当\",\"code\":\"[din癫当阿赞变身]\"},{\"rid\":\"337\",\"groupname\":\"癫当\",\"code\":\"[din癫当红包]\"},{\"rid\":\"338\",\"groupname\":\"癫当\",\"code\":\"[din癫当财神]\"},{\"rid\":\"339\",\"groupname\":\"癫当\",\"code\":\"[din转转]\"},{\"rid\":\"340\",\"groupname\":\"癫当\",\"code\":\"[din撞墙]\"},{\"rid\":\"341\",\"groupname\":\"癫当\",\"code\":\"[din抓狂]\"},{\"rid\":\"342\",\"groupname\":\"癫当\",\"code\":\"[din赞好]\"},{\"rid\":\"343\",\"groupname\":\"癫当\",\"code\":\"[din信息]\"},{\"rid\":\"344\",\"groupname\":\"癫当\",\"code\":\"[din兴奋]\"},{\"rid\":\"345\",\"groupname\":\"癫当\",\"code\":\"[din推撞]\"},{\"rid\":\"346\",\"groupname\":\"癫当\",\"code\":\"[din天哦]\"},{\"rid\":\"347\",\"groupname\":\"癫当\",\"code\":\"[din弹弹]\"},{\"rid\":\"348\",\"groupname\":\"癫当\",\"code\":\"[din睡觉]\"},{\"rid\":\"349\",\"groupname\":\"癫当\",\"code\":\"[din帅]\"},{\"rid\":\"350\",\"groupname\":\"癫当\",\"code\":\"[din闪避]\"},{\"rid\":\"351\",\"groupname\":\"癫当\",\"code\":\"[din亲亲]\"},{\"rid\":\"352\",\"groupname\":\"癫当\",\"code\":\"[din拍手]\"},{\"rid\":\"353\",\"groupname\":\"癫当\",\"code\":\"[din怒]\"},{\"rid\":\"354\",\"groupname\":\"癫当\",\"code\":\"[din摸头]\"},{\"rid\":\"355\",\"groupname\":\"癫当\",\"code\":\"[din流血]\"},{\"rid\":\"356\",\"groupname\":\"癫当\",\"code\":\"[din厉害]\"},{\"rid\":\"357\",\"groupname\":\"癫当\",\"code\":\"[din脸红]\"},{\"rid\":\"358\",\"groupname\":\"癫当\",\"code\":\"[din泪]\"},{\"rid\":\"359\",\"groupname\":\"癫当\",\"code\":\"[din贱香]\"},{\"rid\":\"360\",\"groupname\":\"癫当\",\"code\":\"[din挥手]\"},{\"rid\":\"361\",\"groupname\":\"癫当\",\"code\":\"[din化妆]\"},{\"rid\":\"362\",\"groupname\":\"癫当\",\"code\":\"[din喝]\"},{\"rid\":\"363\",\"groupname\":\"癫当\",\"code\":\"[din汗]\"},{\"rid\":\"364\",\"groupname\":\"癫当\",\"code\":\"[din害羞]\"},{\"rid\":\"365\",\"groupname\":\"癫当\",\"code\":\"[din鬼脸]\"},{\"rid\":\"366\",\"groupname\":\"癫当\",\"code\":\"[din挂了]\"},{\"rid\":\"367\",\"groupname\":\"癫当\",\"code\":\"[din分身1]\"},{\"rid\":\"368\",\"groupname\":\"癫当\",\"code\":\"[din分身2]\"},{\"rid\":\"369\",\"groupname\":\"癫当\",\"code\":\"[din癫当]\"},{\"rid\":\"370\",\"groupname\":\"癫当\",\"code\":\"[din戴熊]\"},{\"rid\":\"371\",\"groupname\":\"癫当\",\"code\":\"[din吃]\"},{\"rid\":\"372\",\"groupname\":\"癫当\",\"code\":\"[din变身]\"},{\"rid\":\"373\",\"groupname\":\"癫当\",\"code\":\"[din变脸]\"},{\"rid\":\"374\",\"groupname\":\"癫当\",\"code\":\"[din白旗]\"},{\"rid\":\"375\",\"groupname\":\"癫当\",\"code\":\"[din爱你]\"},{\"rid\":\"376\",\"groupname\":\"阿狸\",\"code\":\"[ali皇冠]\"},{\"rid\":\"377\",\"groupname\":\"阿狸\",\"code\":\"[ali做鬼脸]\"},{\"rid\":\"378\",\"groupname\":\"阿狸\",\"code\":\"[ali追]\"},{\"rid\":\"379\",\"groupname\":\"阿狸\",\"code\":\"[ali转圈哭]\"},{\"rid\":\"380\",\"groupname\":\"阿狸\",\"code\":\"[ali转]\"},{\"rid\":\"381\",\"groupname\":\"阿狸\",\"code\":\"[ali郁闷]\"},{\"rid\":\"382\",\"groupname\":\"阿狸\",\"code\":\"[ali元宝]\"},{\"rid\":\"383\",\"groupname\":\"阿狸\",\"code\":\"[ali摇晃]\"},{\"rid\":\"384\",\"groupname\":\"阿狸\",\"code\":\"[ali嘘嘘嘘]\"},{\"rid\":\"385\",\"groupname\":\"阿狸\",\"code\":\"[ali羞]\"},{\"rid\":\"386\",\"groupname\":\"阿狸\",\"code\":\"[ali笑死了]\"},{\"rid\":\"387\",\"groupname\":\"阿狸\",\"code\":\"[ali笑]\"},{\"rid\":\"388\",\"groupname\":\"阿狸\",\"code\":\"[ali掀桌子]\"},{\"rid\":\"389\",\"groupname\":\"阿狸\",\"code\":\"[ali鲜花]\"},{\"rid\":\"390\",\"groupname\":\"阿狸\",\"code\":\"[ali想]\"},{\"rid\":\"391\",\"groupname\":\"阿狸\",\"code\":\"[ali吓]\"},{\"rid\":\"392\",\"groupname\":\"阿狸\",\"code\":\"[ali哇]\"},{\"rid\":\"393\",\"groupname\":\"阿狸\",\"code\":\"[ali吐血]\"},{\"rid\":\"394\",\"groupname\":\"阿狸\",\"code\":\"[ali偷看]\"},{\"rid\":\"395\",\"groupname\":\"阿狸\",\"code\":\"[ali送礼物]\"},{\"rid\":\"396\",\"groupname\":\"阿狸\",\"code\":\"[ali睡]\"},{\"rid\":\"397\",\"groupname\":\"阿狸\",\"code\":\"[ali甩手]\"},{\"rid\":\"398\",\"groupname\":\"阿狸\",\"code\":\"[ali摔]\"},{\"rid\":\"399\",\"groupname\":\"阿狸\",\"code\":\"[ali撒钱]\"},{\"rid\":\"400\",\"groupname\":\"阿狸\",\"code\":\"[ali揪]\"},{\"rid\":\"401\",\"groupname\":\"阿狸\",\"code\":\"[ali亲一个]\"},{\"rid\":\"402\",\"groupname\":\"阿狸\",\"code\":\"[ali欠揍]\"},{\"rid\":\"403\",\"groupname\":\"阿狸\",\"code\":\"[ali扑]\"},{\"rid\":\"404\",\"groupname\":\"阿狸\",\"code\":\"[ali扑倒]\"},{\"rid\":\"405\",\"groupname\":\"阿狸\",\"code\":\"[ali飘]\"},{\"rid\":\"406\",\"groupname\":\"阿狸\",\"code\":\"[ali飘过]\"},{\"rid\":\"407\",\"groupname\":\"阿狸\",\"code\":\"[ali喷嚏]\"},{\"rid\":\"408\",\"groupname\":\"阿狸\",\"code\":\"[ali拍拍手]\"},{\"rid\":\"409\",\"groupname\":\"阿狸\",\"code\":\"[ali你]\"},{\"rid\":\"410\",\"groupname\":\"阿狸\",\"code\":\"[ali挠墙]\"},{\"rid\":\"411\",\"groupname\":\"阿狸\",\"code\":\"[ali摸摸头]\"},{\"rid\":\"412\",\"groupname\":\"阿狸\",\"code\":\"[ali溜]\"},{\"rid\":\"413\",\"groupname\":\"阿狸\",\"code\":\"[ali赖皮]\"},{\"rid\":\"414\",\"groupname\":\"阿狸\",\"code\":\"[ali来吧]\"},{\"rid\":\"415\",\"groupname\":\"阿狸\",\"code\":\"[ali囧]\"},{\"rid\":\"416\",\"groupname\":\"阿狸\",\"code\":\"[ali惊]\"},{\"rid\":\"417\",\"groupname\":\"阿狸\",\"code\":\"[ali加油]\"},{\"rid\":\"418\",\"groupname\":\"阿狸\",\"code\":\"[ali僵尸跳]\"},{\"rid\":\"419\",\"groupname\":\"阿狸\",\"code\":\"[ali呼啦圈]\"},{\"rid\":\"420\",\"groupname\":\"阿狸\",\"code\":\"[ali画圈圈]\"},{\"rid\":\"421\",\"groupname\":\"阿狸\",\"code\":\"[ali欢呼]\"},{\"rid\":\"422\",\"groupname\":\"阿狸\",\"code\":\"[ali坏笑]\"},{\"rid\":\"423\",\"groupname\":\"阿狸\",\"code\":\"[ali跪求]\"},{\"rid\":\"424\",\"groupname\":\"阿狸\",\"code\":\"[ali风筝]\"},{\"rid\":\"425\",\"groupname\":\"阿狸\",\"code\":\"[ali飞]\"},{\"rid\":\"426\",\"groupname\":\"阿狸\",\"code\":\"[ali翻白眼]\"},{\"rid\":\"427\",\"groupname\":\"阿狸\",\"code\":\"[ali顶起]\"},{\"rid\":\"428\",\"groupname\":\"阿狸\",\"code\":\"[ali点头]\"},{\"rid\":\"429\",\"groupname\":\"阿狸\",\"code\":\"[ali得瑟]\"},{\"rid\":\"430\",\"groupname\":\"阿狸\",\"code\":\"[ali打篮球]\"},{\"rid\":\"431\",\"groupname\":\"阿狸\",\"code\":\"[ali打滚]\"},{\"rid\":\"432\",\"groupname\":\"阿狸\",\"code\":\"[ali大吃]\"},{\"rid\":\"433\",\"groupname\":\"阿狸\",\"code\":\"[ali踩]\"},{\"rid\":\"434\",\"groupname\":\"阿狸\",\"code\":\"[ali不耐烦]\"},{\"rid\":\"435\",\"groupname\":\"阿狸\",\"code\":\"[ali不吗]\"},{\"rid\":\"436\",\"groupname\":\"阿狸\",\"code\":\"[alibiechaonew]\"},{\"rid\":\"437\",\"groupname\":\"阿狸\",\"code\":\"[ali鞭炮]\"},{\"rid\":\"438\",\"groupname\":\"阿狸\",\"code\":\"[ali抱一抱]\"},{\"rid\":\"439\",\"groupname\":\"阿狸\",\"code\":\"[ali拜年]\"},{\"rid\":\"440\",\"groupname\":\"阿狸\",\"code\":\"[ali88]\"},{\"rid\":\"441\",\"groupname\":\"阿狸\",\"code\":\"[ali狂笑]\"},{\"rid\":\"442\",\"groupname\":\"阿狸\",\"code\":\"[ali冤]\"},{\"rid\":\"443\",\"groupname\":\"阿狸\",\"code\":\"[ali蜷]\"},{\"rid\":\"444\",\"groupname\":\"阿狸\",\"code\":\"[ali美好]\"},{\"rid\":\"445\",\"groupname\":\"阿狸\",\"code\":\"[ali乐和]\"},{\"rid\":\"446\",\"groupname\":\"阿狸\",\"code\":\"[ali揪耳朵]\"},{\"rid\":\"447\",\"groupname\":\"阿狸\",\"code\":\"[ali晃]\"},{\"rid\":\"448\",\"groupname\":\"阿狸\",\"code\":\"[aliigh]\"},{\"rid\":\"449\",\"groupname\":\"阿狸\",\"code\":\"[ali蹭]\"},{\"rid\":\"450\",\"groupname\":\"阿狸\",\"code\":\"[ali抱枕]\"},{\"rid\":\"451\",\"groupname\":\"阿狸\",\"code\":\"[ali不公平]\"},{\"rid\":\"452\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[BOBO害羞]\"},{\"rid\":\"453\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[BOBO哈哈]\"},{\"rid\":\"454\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[BOBO吃面]\"},{\"rid\":\"455\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[BOBO擦泪]\"},{\"rid\":\"456\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[bobuyaoa]\"},{\"rid\":\"457\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[BOBO变身]\"},{\"rid\":\"458\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[BOBO崩溃]\"},{\"rid\":\"459\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[BOBO拜]\"},{\"rid\":\"460\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[BOBO爱你]\"},{\"rid\":\"461\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[TOTO打我啊]\"},{\"rid\":\"462\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[toto拜年]\"},{\"rid\":\"463\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[bobo拜年]\"},{\"rid\":\"464\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[toto无聊]\"},{\"rid\":\"465\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[toto我最摇滚]\"},{\"rid\":\"466\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[toto数落]\"},{\"rid\":\"467\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[toto睡觉]\"},{\"rid\":\"468\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[toto甩头发]\"},{\"rid\":\"469\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[toto飘过]\"},{\"rid\":\"470\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[toto狂汗]\"},{\"rid\":\"471\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[toto好累]\"},{\"rid\":\"472\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[bobo抓狂]\"},{\"rid\":\"473\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[bobo疑问]\"},{\"rid\":\"474\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[bobo抛媚眼]\"},{\"rid\":\"475\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[bobo膜拜]\"},{\"rid\":\"476\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[bobo纠结]\"},{\"rid\":\"477\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[bobo不要啊]\"},{\"rid\":\"478\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[bobo不理你]\"},{\"rid\":\"479\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[有爱]\"},{\"rid\":\"480\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[TOTOYES]\"},{\"rid\":\"481\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[我爱听]\"},{\"rid\":\"482\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[怒火]\"},{\"rid\":\"483\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[擂鼓]\"},{\"rid\":\"484\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[讥笑]\"},{\"rid\":\"485\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[抛钱]\"},{\"rid\":\"486\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[变花]\"},{\"rid\":\"487\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[飙泪]\"},{\"rid\":\"488\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[藏猫猫]\"},{\"rid\":\"489\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[淘气]\"},{\"rid\":\"490\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[生闷气]\"},{\"rid\":\"491\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[忍]\"},{\"rid\":\"492\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[泡泡糖]\"},{\"rid\":\"493\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[BOBO赞]\"},{\"rid\":\"494\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[Hi]\"},{\"rid\":\"495\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[BOBO么么哒]\"},{\"rid\":\"496\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[我爱西瓜]\"},{\"rid\":\"497\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[吓一跳]\"},{\"rid\":\"498\",\"groupname\":\"BOBO和TOTO\",\"code\":\"[吃饭]\"},{\"rid\":\"499\",\"groupname\":\"小纯洁\",\"code\":\"[c帅]\"},{\"rid\":\"500\",\"groupname\":\"小纯洁\",\"code\":\"[c窃喜]\"},{\"rid\":\"501\",\"groupname\":\"小纯洁\",\"code\":\"[c迷糊]\"},{\"rid\":\"502\",\"groupname\":\"小纯洁\",\"code\":\"[c面瘫]\"},{\"rid\":\"503\",\"groupname\":\"小纯洁\",\"code\":\"[c囧]\"},{\"rid\":\"504\",\"groupname\":\"小纯洁\",\"code\":\"[c汗]\"},{\"rid\":\"505\",\"groupname\":\"小纯洁\",\"code\":\"[c高明]\"},{\"rid\":\"506\",\"groupname\":\"小纯洁\",\"code\":\"[c大笑]\"},{\"rid\":\"507\",\"groupname\":\"小纯洁\",\"code\":\"[c变脸]\"},{\"rid\":\"508\",\"groupname\":\"小纯洁\",\"code\":\"[c左右看]\"},{\"rid\":\"509\",\"groupname\":\"小纯洁\",\"code\":\"[c坏笑]\"},{\"rid\":\"510\",\"groupname\":\"小纯洁\",\"code\":\"[c看热闹]\"},{\"rid\":\"511\",\"groupname\":\"小纯洁\",\"code\":\"[c开心]\"},{\"rid\":\"512\",\"groupname\":\"小纯洁\",\"code\":\"[c关注]\"},{\"rid\":\"513\",\"groupname\":\"小纯洁\",\"code\":\"[c娇羞]\"},{\"rid\":\"514\",\"groupname\":\"小纯洁\",\"code\":\"[c无语]\"},{\"rid\":\"515\",\"groupname\":\"小纯洁\",\"code\":\"[c疑惑]\"},{\"rid\":\"516\",\"groupname\":\"小纯洁\",\"code\":\"[c正经]\"},{\"rid\":\"517\",\"groupname\":\"小纯洁\",\"code\":\"[c无聊]\"},{\"rid\":\"518\",\"groupname\":\"小纯洁\",\"code\":\"[c挖鼻孔]\"},{\"rid\":\"519\",\"groupname\":\"小纯洁\",\"code\":\"[c期待]\"},{\"rid\":\"520\",\"groupname\":\"小纯洁\",\"code\":\"[c摇头看]\"},{\"rid\":\"521\",\"groupname\":\"小纯洁\",\"code\":\"[c亲亲]\"},{\"rid\":\"522\",\"groupname\":\"小纯洁\",\"code\":\"[c羞涩]\"},{\"rid\":\"523\",\"groupname\":\"小纯洁\",\"code\":\"[c悲催]\"},{\"rid\":\"524\",\"groupname\":\"小纯洁\",\"code\":\"[c得瑟]\"},{\"rid\":\"525\",\"groupname\":\"小纯洁\",\"code\":\"[c冷眼]\"},{\"rid\":\"526\",\"groupname\":\"小纯洁\",\"code\":\"[c惊讶]\"},{\"rid\":\"527\",\"groupname\":\"小纯洁\",\"code\":\"[c委屈]\"},{\"rid\":\"528\",\"groupname\":\"小纯洁\",\"code\":\"[c甩舌头]\"},{\"rid\":\"529\",\"groupname\":\"小纯洁\",\"code\":\"[c摇头萌]\"},{\"rid\":\"530\",\"groupname\":\"小纯洁\",\"code\":\"[c抓狂]\"},{\"rid\":\"531\",\"groupname\":\"小纯洁\",\"code\":\"[c发火]\"},{\"rid\":\"532\",\"groupname\":\"小纯洁\",\"code\":\"[c卖萌]\"},{\"rid\":\"533\",\"groupname\":\"小纯洁\",\"code\":\"[c伤心]\"},{\"rid\":\"534\",\"groupname\":\"小纯洁\",\"code\":\"[c捂脸]\"},{\"rid\":\"535\",\"groupname\":\"小纯洁\",\"code\":\"[c震惊哭]\"},{\"rid\":\"536\",\"groupname\":\"小纯洁\",\"code\":\"[c摇摆]\"},{\"rid\":\"537\",\"groupname\":\"小纯洁\",\"code\":\"[c得意笑]\"},{\"rid\":\"538\",\"groupname\":\"小纯洁\",\"code\":\"[c烦躁]\"},{\"rid\":\"539\",\"groupname\":\"小纯洁\",\"code\":\"[c得意]\"},{\"rid\":\"540\",\"groupname\":\"小纯洁\",\"code\":\"[c脸红]\"},{\"rid\":\"541\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx喵]\"},{\"rid\":\"542\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx喵喵]\"},{\"rid\":\"543\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx奔跑]\"},{\"rid\":\"544\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx走]\"},{\"rid\":\"545\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx蠕过]\"},{\"rid\":\"546\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx蹭]\"},{\"rid\":\"547\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx狂欢]\"},{\"rid\":\"548\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx奋斗]\"},{\"rid\":\"549\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx笑]\"},{\"rid\":\"550\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx懒腰]\"},{\"rid\":\"551\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx得意]\"},{\"rid\":\"552\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx右边]\"},{\"rid\":\"553\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx转头]\"},{\"rid\":\"554\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx跳跃]\"},{\"rid\":\"555\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx转体]\"},{\"rid\":\"556\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx撒欢]\"},{\"rid\":\"557\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx挠]\"},{\"rid\":\"558\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx挠皇]\"},{\"rid\":\"559\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx逗转圈]\"},{\"rid\":\"560\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx划]\"},{\"rid\":\"561\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx得瑟]\"},{\"rid\":\"562\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx喷嚏]\"},{\"rid\":\"563\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx打喷嚏]\"},{\"rid\":\"564\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx哭]\"},{\"rid\":\"565\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx扫灰]\"},{\"rid\":\"566\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx听歌]\"},{\"rid\":\"567\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx狂吃]\"},{\"rid\":\"568\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx画圈]\"},{\"rid\":\"569\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx掀桌]\"},{\"rid\":\"570\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx刷牙]\"},{\"rid\":\"571\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx抱枕]\"},{\"rid\":\"572\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx都不给]\"},{\"rid\":\"573\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx逗左右]\"},{\"rid\":\"574\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx变化]\"},{\"rid\":\"575\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx打地鼠]\"},{\"rid\":\"576\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx西瓜]\"},{\"rid\":\"577\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx咻]\"},{\"rid\":\"578\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx咻2]\"},{\"rid\":\"579\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx咻3]\"},{\"rid\":\"580\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx咻4]\"},{\"rid\":\"581\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx咻5]\"},{\"rid\":\"582\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx咻6]\"},{\"rid\":\"583\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx咻7]\"},{\"rid\":\"584\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx咻8]\"},{\"rid\":\"585\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx滚过]\"},{\"rid\":\"586\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx躺中枪]\"},{\"rid\":\"587\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx讨厌]\"},{\"rid\":\"588\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx逗上下]\"},{\"rid\":\"589\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx吐血]\"},{\"rid\":\"590\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx病了]\"},{\"rid\":\"591\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx泪目]\"},{\"rid\":\"592\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx无语]\"},{\"rid\":\"593\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx问号]\"},{\"rid\":\"594\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx侧目]\"},{\"rid\":\"595\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx惊]\"},{\"rid\":\"596\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx吐]\"},{\"rid\":\"597\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx失落]\"},{\"rid\":\"598\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx汗]\"},{\"rid\":\"599\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx暴汗]\"},{\"rid\":\"600\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx狠]\"},{\"rid\":\"601\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx怨念]\"},{\"rid\":\"602\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx睡觉]\"},{\"rid\":\"603\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx求表扬]\"},{\"rid\":\"604\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx啄地]\"},{\"rid\":\"605\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx无聊]\"},{\"rid\":\"606\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx顺毛]\"},{\"rid\":\"607\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx喝奶]\"},{\"rid\":\"608\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx不爽]\"},{\"rid\":\"609\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx老大]\"},{\"rid\":\"610\",\"groupname\":\"罗小黑\",\"code\":\"[lxhx生日快乐]\"},{\"rid\":\"611\",\"groupname\":\"罗小黑\",\"code\":\"[mtjj拜年]\"},{\"rid\":\"612\",\"groupname\":\"郭斯特\",\"code\":\"[gst挖鼻屎]\"},{\"rid\":\"613\",\"groupname\":\"郭斯特\",\"code\":\"[gst舔舔]\"},{\"rid\":\"614\",\"groupname\":\"郭斯特\",\"code\":\"[gst好羞射]\"},{\"rid\":\"615\",\"groupname\":\"郭斯特\",\"code\":\"[gst抽你]\"},{\"rid\":\"616\",\"groupname\":\"郭斯特\",\"code\":\"[gst好难懂]\"},{\"rid\":\"617\",\"groupname\":\"郭斯特\",\"code\":\"[gst不活了]\"},{\"rid\":\"618\",\"groupname\":\"郭斯特\",\"code\":\"[gst转转转]\"},{\"rid\":\"619\",\"groupname\":\"郭斯特\",\"code\":\"[gst汗]\"},{\"rid\":\"620\",\"groupname\":\"郭斯特\",\"code\":\"[gst干嘛噜]\"},{\"rid\":\"621\",\"groupname\":\"郭斯特\",\"code\":\"[gst生日快乐]\"},{\"rid\":\"622\",\"groupname\":\"郭斯特\",\"code\":\"[gst人家不依]\"},{\"rid\":\"623\",\"groupname\":\"郭斯特\",\"code\":\"[gst热热热]\"},{\"rid\":\"624\",\"groupname\":\"郭斯特\",\"code\":\"[gst耐你]\"},{\"rid\":\"625\",\"groupname\":\"郭斯特\",\"code\":\"[gst困]\"},{\"rid\":\"626\",\"groupname\":\"郭斯特\",\"code\":\"[gst好怕呀]\"},{\"rid\":\"627\",\"groupname\":\"郭斯特\",\"code\":\"[gst发工资啦]\"},{\"rid\":\"628\",\"groupname\":\"郭斯特\",\"code\":\"[gst嘲笑你]\"},{\"rid\":\"629\",\"groupname\":\"郭斯特\",\"code\":\"[gst呀咩爹]\"},{\"rid\":\"630\",\"groupname\":\"郭斯特\",\"code\":\"[gst下班啦]\"},{\"rid\":\"631\",\"groupname\":\"郭斯特\",\"code\":\"[gst晚安]\"},{\"rid\":\"632\",\"groupname\":\"郭斯特\",\"code\":\"[gst败了]\"},{\"rid\":\"633\",\"groupname\":\"郭斯特\",\"code\":\"[gst死蚊子]\"},{\"rid\":\"634\",\"groupname\":\"郭斯特\",\"code\":\"[gst帅毙了]\"},{\"rid\":\"635\",\"groupname\":\"郭斯特\",\"code\":\"[gst揉揉脸]\"},{\"rid\":\"636\",\"groupname\":\"郭斯特\",\"code\":\"[gst嘿嘿嘿]\"},{\"rid\":\"637\",\"groupname\":\"郭斯特\",\"code\":\"[gst得瑟]\"},{\"rid\":\"638\",\"groupname\":\"郭斯特\",\"code\":\"[gst艾玛]\"},{\"rid\":\"639\",\"groupname\":\"冷兔\",\"code\":\"[lt五一]\"},{\"rid\":\"640\",\"groupname\":\"冷兔\",\"code\":\"[lt新年好]\"},{\"rid\":\"641\",\"groupname\":\"冷兔\",\"code\":\"[lt火车]\"},{\"rid\":\"642\",\"groupname\":\"冷兔\",\"code\":\"[lt火车票]\"},{\"rid\":\"643\",\"groupname\":\"冷兔\",\"code\":\"[lt红包]\"},{\"rid\":\"644\",\"groupname\":\"冷兔\",\"code\":\"[lt摇滚]\"},{\"rid\":\"645\",\"groupname\":\"冷兔\",\"code\":\"[lt万圣节]\"},{\"rid\":\"646\",\"groupname\":\"冷兔\",\"code\":\"[lt赞]\"},{\"rid\":\"647\",\"groupname\":\"冷兔\",\"code\":\"[lt江南style]\"},{\"rid\":\"648\",\"groupname\":\"冷兔\",\"code\":\"[lt吃东西]\"},{\"rid\":\"649\",\"groupname\":\"冷兔\",\"code\":\"[lt最右]\"},{\"rid\":\"650\",\"groupname\":\"冷兔\",\"code\":\"[lt切克闹]\"},{\"rid\":\"651\",\"groupname\":\"冷兔\",\"code\":\"[lt犯困]\"},{\"rid\":\"652\",\"groupname\":\"冷兔\",\"code\":\"[lt戳瞎]\"},{\"rid\":\"653\",\"groupname\":\"冷兔\",\"code\":\"[lt鼻血]\"},{\"rid\":\"654\",\"groupname\":\"冷兔\",\"code\":\"[lt阴险]\"},{\"rid\":\"655\",\"groupname\":\"冷兔\",\"code\":\"[lt摇摆]\"},{\"rid\":\"656\",\"groupname\":\"冷兔\",\"code\":\"[lt羞]\"},{\"rid\":\"657\",\"groupname\":\"冷兔\",\"code\":\"[lt闪瞎]\"},{\"rid\":\"658\",\"groupname\":\"冷兔\",\"code\":\"[lt拍手]\"},{\"rid\":\"659\",\"groupname\":\"冷兔\",\"code\":\"[lt蛋疼]\"},{\"rid\":\"660\",\"groupname\":\"冷兔\",\"code\":\"[lt撒花]\"},{\"rid\":\"661\",\"groupname\":\"冷兔\",\"code\":\"[lt母亲节]\"},{\"rid\":\"662\",\"groupname\":\"冷兔\",\"code\":\"[lt挖鼻]\"},{\"rid\":\"663\",\"groupname\":\"冷兔\",\"code\":\"[lt哈欠]\"},{\"rid\":\"664\",\"groupname\":\"冷兔\",\"code\":\"[lt泪目]\"},{\"rid\":\"665\",\"groupname\":\"冷兔\",\"code\":\"[lt雷]\"},{\"rid\":\"666\",\"groupname\":\"冷兔\",\"code\":\"[lt中枪]\"},{\"rid\":\"667\",\"groupname\":\"冷兔\",\"code\":\"[lt耳朵]\"},{\"rid\":\"668\",\"groupname\":\"冷兔\",\"code\":\"[lt顶]\"},{\"rid\":\"669\",\"groupname\":\"冷兔\",\"code\":\"[lt潜水]\"},{\"rid\":\"670\",\"groupname\":\"冷兔\",\"code\":\"[lt拍桌大笑]\"},{\"rid\":\"671\",\"groupname\":\"冷兔\",\"code\":\"[lt黑线]\"},{\"rid\":\"672\",\"groupname\":\"冷兔\",\"code\":\"[lt喷血]\"},{\"rid\":\"673\",\"groupname\":\"冷兔\",\"code\":\"[lt巨汗]\"},{\"rid\":\"674\",\"groupname\":\"冷兔\",\"code\":\"[lt疑惑]\"},{\"rid\":\"675\",\"groupname\":\"冷兔\",\"code\":\"[lt浮云]\"},{\"rid\":\"676\",\"groupname\":\"冷兔\",\"code\":\"[lt笑话]\"},{\"rid\":\"677\",\"groupname\":\"冷兔\",\"code\":\"[lt喷]\"},{\"rid\":\"678\",\"groupname\":\"冷兔\",\"code\":\"[lt雪]\"},{\"rid\":\"679\",\"groupname\":\"冷兔\",\"code\":\"[lt转发]\"},{\"rid\":\"680\",\"groupname\":\"冷兔\",\"code\":\"[lt偷窥]\"},{\"rid\":\"681\",\"groupname\":\"冷兔\",\"code\":\"[lt惊吓]\"},{\"rid\":\"682\",\"groupname\":\"冷兔\",\"code\":\"[lt囧]\"},{\"rid\":\"683\",\"groupname\":\"冷兔\",\"code\":\"[lt灰飞烟灭]\"},{\"rid\":\"684\",\"groupname\":\"冷兔\",\"code\":\"[lt冰封]\"},{\"rid\":\"685\",\"groupname\":\"冷兔\",\"code\":\"[lt吐]\"},{\"rid\":\"686\",\"groupname\":\"冷兔\",\"code\":\"[lt吹泡泡]\"},{\"rid\":\"687\",\"groupname\":\"冷兔\",\"code\":\"[lt吓]\"},{\"rid\":\"688\",\"groupname\":\"白骨精\",\"code\":\"[xb自信]\"},{\"rid\":\"689\",\"groupname\":\"白骨精\",\"code\":\"[xb转]\"},{\"rid\":\"690\",\"groupname\":\"白骨精\",\"code\":\"[xb转圈]\"},{\"rid\":\"691\",\"groupname\":\"白骨精\",\"code\":\"[xb指指]\"},{\"rid\":\"692\",\"groupname\":\"白骨精\",\"code\":\"[xb招手]\"},{\"rid\":\"693\",\"groupname\":\"白骨精\",\"code\":\"[xb照镜]\"},{\"rid\":\"694\",\"groupname\":\"白骨精\",\"code\":\"[xb雨]\"},{\"rid\":\"695\",\"groupname\":\"白骨精\",\"code\":\"[xb坏笑]\"},{\"rid\":\"696\",\"groupname\":\"白骨精\",\"code\":\"[xb疑惑]\"},{\"rid\":\"697\",\"groupname\":\"白骨精\",\"code\":\"[xb摇摆]\"},{\"rid\":\"698\",\"groupname\":\"白骨精\",\"code\":\"[xb眼镜]\"},{\"rid\":\"699\",\"groupname\":\"白骨精\",\"code\":\"[xb压力]\"},{\"rid\":\"700\",\"groupname\":\"白骨精\",\"code\":\"[xb星]\"},{\"rid\":\"701\",\"groupname\":\"白骨精\",\"code\":\"[xb兴奋]\"},{\"rid\":\"702\",\"groupname\":\"白骨精\",\"code\":\"[xb喜欢]\"},{\"rid\":\"703\",\"groupname\":\"白骨精\",\"code\":\"[xb小花]\"},{\"rid\":\"704\",\"groupname\":\"白骨精\",\"code\":\"[xb无奈]\"},{\"rid\":\"705\",\"groupname\":\"白骨精\",\"code\":\"[xb捂脸]\"},{\"rid\":\"706\",\"groupname\":\"白骨精\",\"code\":\"[xb天使]\"},{\"rid\":\"707\",\"groupname\":\"白骨精\",\"code\":\"[xb太阳]\"},{\"rid\":\"708\",\"groupname\":\"白骨精\",\"code\":\"[xb睡觉]\"},{\"rid\":\"709\",\"groupname\":\"白骨精\",\"code\":\"[xb甩葱]\"},{\"rid\":\"710\",\"groupname\":\"白骨精\",\"code\":\"[xb生日]\"},{\"rid\":\"711\",\"groupname\":\"白骨精\",\"code\":\"[xb扇子]\"},{\"rid\":\"712\",\"groupname\":\"白骨精\",\"code\":\"[xb伤心]\"},{\"rid\":\"713\",\"groupname\":\"白骨精\",\"code\":\"[xb揉]\"},{\"rid\":\"714\",\"groupname\":\"白骨精\",\"code\":\"[xb求神]\"},{\"rid\":\"715\",\"groupname\":\"白骨精\",\"code\":\"[xb青蛙]\"},{\"rid\":\"716\",\"groupname\":\"白骨精\",\"code\":\"[xb期待]\"},{\"rid\":\"717\",\"groupname\":\"白骨精\",\"code\":\"[xb泡澡]\"},{\"rid\":\"718\",\"groupname\":\"白骨精\",\"code\":\"[xb怒]\"},{\"rid\":\"719\",\"groupname\":\"白骨精\",\"code\":\"[xb努力]\"},{\"rid\":\"720\",\"groupname\":\"白骨精\",\"code\":\"[xb拇指]\"},{\"rid\":\"721\",\"groupname\":\"白骨精\",\"code\":\"[xb喵]\"},{\"rid\":\"722\",\"groupname\":\"白骨精\",\"code\":\"[xb喇叭]\"},{\"rid\":\"723\",\"groupname\":\"白骨精\",\"code\":\"[xb哭]\"},{\"rid\":\"724\",\"groupname\":\"白骨精\",\"code\":\"[xb看书]\"},{\"rid\":\"725\",\"groupname\":\"白骨精\",\"code\":\"[xb开餐]\"},{\"rid\":\"726\",\"groupname\":\"白骨精\",\"code\":\"[xb举手]\"},{\"rid\":\"727\",\"groupname\":\"白骨精\",\"code\":\"[xb奸笑]\"},{\"rid\":\"728\",\"groupname\":\"白骨精\",\"code\":\"[xb昏]\"},{\"rid\":\"729\",\"groupname\":\"白骨精\",\"code\":\"[xb挥手]\"},{\"rid\":\"730\",\"groupname\":\"白骨精\",\"code\":\"[xb欢乐]\"},{\"rid\":\"731\",\"groupname\":\"白骨精\",\"code\":\"[xb喝茶]\"},{\"rid\":\"732\",\"groupname\":\"白骨精\",\"code\":\"[xb汗]\"},{\"rid\":\"733\",\"groupname\":\"白骨精\",\"code\":\"[xb害羞]\"},{\"rid\":\"734\",\"groupname\":\"白骨精\",\"code\":\"[xb害怕]\"},{\"rid\":\"735\",\"groupname\":\"白骨精\",\"code\":\"[xb风吹]\"},{\"rid\":\"736\",\"groupname\":\"白骨精\",\"code\":\"[xb风车]\"},{\"rid\":\"737\",\"groupname\":\"白骨精\",\"code\":\"[xb恶魔]\"},{\"rid\":\"738\",\"groupname\":\"白骨精\",\"code\":\"[xb打]\"},{\"rid\":\"739\",\"groupname\":\"白骨精\",\"code\":\"[xb大笑]\"},{\"rid\":\"740\",\"groupname\":\"白骨精\",\"code\":\"[xb呆]\"},{\"rid\":\"741\",\"groupname\":\"白骨精\",\"code\":\"[xb触手]\"},{\"rid\":\"742\",\"groupname\":\"白骨精\",\"code\":\"[xb吹]\"},{\"rid\":\"743\",\"groupname\":\"白骨精\",\"code\":\"[xb吃糖]\"},{\"rid\":\"744\",\"groupname\":\"白骨精\",\"code\":\"[xb吃饭]\"},{\"rid\":\"745\",\"groupname\":\"白骨精\",\"code\":\"[xb吃包]\"},{\"rid\":\"746\",\"groupname\":\"白骨精\",\"code\":\"[xb唱歌]\"},{\"rid\":\"747\",\"groupname\":\"白骨精\",\"code\":\"[xb摆手]\"},{\"rid\":\"748\",\"groupname\":\"hello菜菜\",\"code\":\"[cai走走]\"},{\"rid\":\"749\",\"groupname\":\"hello菜菜\",\"code\":\"[cai揍人]\"},{\"rid\":\"750\",\"groupname\":\"hello菜菜\",\"code\":\"[cai撞墙]\"},{\"rid\":\"751\",\"groupname\":\"hello菜菜\",\"code\":\"[cai正呀]\"},{\"rid\":\"752\",\"groupname\":\"hello菜菜\",\"code\":\"[cai嘻嘻]\"},{\"rid\":\"753\",\"groupname\":\"hello菜菜\",\"code\":\"[cai羞羞]\"},{\"rid\":\"754\",\"groupname\":\"hello菜菜\",\"code\":\"[cai无语]\"},{\"rid\":\"755\",\"groupname\":\"hello菜菜\",\"code\":\"[cai脱光]\"},{\"rid\":\"756\",\"groupname\":\"hello菜菜\",\"code\":\"[cai偷摸]\"},{\"rid\":\"757\",\"groupname\":\"hello菜菜\",\"code\":\"[cai太好了]\"},{\"rid\":\"758\",\"groupname\":\"hello菜菜\",\"code\":\"[cai庆祝]\"},{\"rid\":\"759\",\"groupname\":\"hello菜菜\",\"code\":\"[cai钱]\"},{\"rid\":\"760\",\"groupname\":\"hello菜菜\",\"code\":\"[cai潜水]\"},{\"rid\":\"761\",\"groupname\":\"hello菜菜\",\"code\":\"[cai怕羞]\"},{\"rid\":\"762\",\"groupname\":\"hello菜菜\",\"code\":\"[cai落叶]\"},{\"rid\":\"763\",\"groupname\":\"hello菜菜\",\"code\":\"[cai哭]\"},{\"rid\":\"764\",\"groupname\":\"hello菜菜\",\"code\":\"[cai开心]\"},{\"rid\":\"765\",\"groupname\":\"hello菜菜\",\"code\":\"[cai惊吓]\"},{\"rid\":\"766\",\"groupname\":\"hello菜菜\",\"code\":\"[cai奸笑]\"},{\"rid\":\"767\",\"groupname\":\"hello菜菜\",\"code\":\"[cai晃头]\"},{\"rid\":\"768\",\"groupname\":\"hello菜菜\",\"code\":\"[cai哈喽]\"},{\"rid\":\"769\",\"groupname\":\"hello菜菜\",\"code\":\"[cai飞吻]\"},{\"rid\":\"770\",\"groupname\":\"hello菜菜\",\"code\":\"[cai肚腩]\"},{\"rid\":\"771\",\"groupname\":\"hello菜菜\",\"code\":\"[cai打打]\"},{\"rid\":\"772\",\"groupname\":\"hello菜菜\",\"code\":\"[cai扯脸]\"},{\"rid\":\"773\",\"groupname\":\"hello菜菜\",\"code\":\"[cai插眼]\"},{\"rid\":\"774\",\"groupname\":\"hello菜菜\",\"code\":\"[cai鼻屎]\"},{\"rid\":\"775\",\"groupname\":\"hello菜菜\",\"code\":\"[cai崩溃]\"},{\"rid\":\"776\",\"groupname\":\"hello菜菜\",\"code\":\"[cai拜拜]\"},{\"rid\":\"777\",\"groupname\":\"hello菜菜\",\"code\":\"[cai啊]\"},{\"rid\":\"778\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb装傻]\"},{\"rid\":\"779\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb咦]\"},{\"rid\":\"780\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb嗯]\"},{\"rid\":\"781\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb糟糕]\"},{\"rid\":\"782\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb嘿嘿]\"},{\"rid\":\"783\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb鄙视]\"},{\"rid\":\"784\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb戳]\"},{\"rid\":\"785\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb摇头]\"},{\"rid\":\"786\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb惊]\"},{\"rid\":\"787\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb欢乐]\"},{\"rid\":\"788\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb雷]\"},{\"rid\":\"789\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb呃]\"},{\"rid\":\"790\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb蹭右]\"},{\"rid\":\"791\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb蹭左]\"},{\"rid\":\"792\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb啊]\"},{\"rid\":\"793\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb哼]\"},{\"rid\":\"794\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb撒欢]\"},{\"rid\":\"795\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb爽]\"},{\"rid\":\"796\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb味]\"},{\"rid\":\"797\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb厉害]\"},{\"rid\":\"798\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb帅]\"},{\"rid\":\"799\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb哭]\"},{\"rid\":\"800\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb呵]\"},{\"rid\":\"801\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb嘻]\"},{\"rid\":\"802\",\"groupname\":\"面瘫萝卜\",\"code\":\"[lb讨厌]\"},{\"rid\":\"803\",\"groupname\":\"阿拉兔\",\"code\":\"[ala晕]\"},{\"rid\":\"804\",\"groupname\":\"阿拉兔\",\"code\":\"[ala郁闷]\"},{\"rid\":\"805\",\"groupname\":\"阿拉兔\",\"code\":\"[ala耶]\"},{\"rid\":\"806\",\"groupname\":\"阿拉兔\",\"code\":\"[ala羞]\"},{\"rid\":\"807\",\"groupname\":\"阿拉兔\",\"code\":\"[ala舔舔]\"},{\"rid\":\"808\",\"groupname\":\"阿拉兔\",\"code\":\"[ala泪汪汪]\"},{\"rid\":\"809\",\"groupname\":\"阿拉兔\",\"code\":\"[ala加油]\"},{\"rid\":\"810\",\"groupname\":\"阿拉兔\",\"code\":\"[ala飞吻]\"},{\"rid\":\"811\",\"groupname\":\"阿拉兔\",\"code\":\"[ala得意]\"},{\"rid\":\"812\",\"groupname\":\"阿拉兔\",\"code\":\"[ala搓搓]\"},{\"rid\":\"813\",\"groupname\":\"阿拉兔\",\"code\":\"[ala蹦]\"},{\"rid\":\"814\",\"groupname\":\"阿拉兔\",\"code\":\"[ala杯具]\"},{\"rid\":\"815\",\"groupname\":\"阿拉兔\",\"code\":\"[ala爱国]\"},{\"rid\":\"816\",\"groupname\":\"阿拉兔\",\"code\":\"[ala扭啊扭]\"},{\"rid\":\"817\",\"groupname\":\"阿拉兔\",\"code\":\"[ala吐舌头]\"},{\"rid\":\"818\",\"groupname\":\"阿拉兔\",\"code\":\"[ala么么]\"},{\"rid\":\"819\",\"groupname\":\"阿拉兔\",\"code\":\"[ala嘿嘿嘿]\"},{\"rid\":\"820\",\"groupname\":\"阿拉兔\",\"code\":\"[ala哼]\"},{\"rid\":\"821\",\"groupname\":\"阿拉兔\",\"code\":\"[ala囧]\"},{\"rid\":\"822\",\"groupname\":\"阿拉兔\",\"code\":\"[ala上火]\"},{\"rid\":\"823\",\"groupname\":\"阿拉兔\",\"code\":\"[ala啊哈哈哈]\"},{\"rid\":\"824\",\"groupname\":\"阿拉兔\",\"code\":\"[ala飘走]\"},{\"rid\":\"825\",\"groupname\":\"阿拉兔\",\"code\":\"[ala吃货]\"},{\"rid\":\"826\",\"groupname\":\"阿拉兔\",\"code\":\"[ala悲催]\"},{\"rid\":\"827\",\"groupname\":\"阿拉兔\",\"code\":\"[ala讨厌]\"},{\"rid\":\"828\",\"groupname\":\"阿拉兔\",\"code\":\"[ala衰]\"},{\"rid\":\"829\",\"groupname\":\"阿拉兔\",\"code\":\"[alt拜年]\"},{\"rid\":\"830\",\"groupname\":\"小幺鸡\",\"code\":\"[j疯了]\"},{\"rid\":\"831\",\"groupname\":\"小幺鸡\",\"code\":\"[j撒娇]\"},{\"rid\":\"832\",\"groupname\":\"小幺鸡\",\"code\":\"[j吐血]\"},{\"rid\":\"833\",\"groupname\":\"小幺鸡\",\"code\":\"[j浪笑]\"},{\"rid\":\"834\",\"groupname\":\"小幺鸡\",\"code\":\"[j作揖]\"},{\"rid\":\"835\",\"groupname\":\"小幺鸡\",\"code\":\"[j哎呀]\"},{\"rid\":\"836\",\"groupname\":\"小幺鸡\",\"code\":\"[j挂了]\"},{\"rid\":\"837\",\"groupname\":\"小幺鸡\",\"code\":\"[j扭秧歌]\"},{\"rid\":\"838\",\"groupname\":\"小幺鸡\",\"code\":\"[j媚眼]\"},{\"rid\":\"839\",\"groupname\":\"小幺鸡\",\"code\":\"[j来嘛]\"},{\"rid\":\"840\",\"groupname\":\"小幺鸡\",\"code\":\"[j蹭]\"},{\"rid\":\"841\",\"groupname\":\"小幺鸡\",\"code\":\"[xyj年年有鱼]\"},{\"rid\":\"842\",\"groupname\":\"小幺鸡\",\"code\":\"[xyj红包]\"},{\"rid\":\"843\",\"groupname\":\"小幺鸡\",\"code\":\"[xyj拜年]\"},{\"rid\":\"844\",\"groupname\":\"小幺鸡\",\"code\":\"[抓沙发]\"},{\"rid\":\"845\",\"groupname\":\"小幺鸡\",\"code\":\"[震撼]\"},{\"rid\":\"846\",\"groupname\":\"小幺鸡\",\"code\":\"[晕晕]\"},{\"rid\":\"847\",\"groupname\":\"小幺鸡\",\"code\":\"[瞎眼]\"},{\"rid\":\"848\",\"groupname\":\"小幺鸡\",\"code\":\"[为难]\"},{\"rid\":\"849\",\"groupname\":\"小幺鸡\",\"code\":\"[舔]\"},{\"rid\":\"850\",\"groupname\":\"小幺鸡\",\"code\":\"[流汗]\"},{\"rid\":\"851\",\"groupname\":\"小幺鸡\",\"code\":\"[冷]\"},{\"rid\":\"852\",\"groupname\":\"小幺鸡\",\"code\":\"[老大]\"},{\"rid\":\"853\",\"groupname\":\"小幺鸡\",\"code\":\"[瞌睡]\"},{\"rid\":\"854\",\"groupname\":\"小幺鸡\",\"code\":\"[可怜的]\"},{\"rid\":\"855\",\"groupname\":\"小幺鸡\",\"code\":\"[咖啡咖啡]\"},{\"rid\":\"856\",\"groupname\":\"小幺鸡\",\"code\":\"[坏笑]\"},{\"rid\":\"857\",\"groupname\":\"小幺鸡\",\"code\":\"[顶啊]\"},{\"rid\":\"858\",\"groupname\":\"小幺鸡\",\"code\":\"[好得意]\"},{\"rid\":\"859\",\"groupname\":\"小幺鸡\",\"code\":\"[冲啊]\"},{\"rid\":\"860\",\"groupname\":\"小幺鸡\",\"code\":\"[吃西瓜]\"},{\"rid\":\"861\",\"groupname\":\"小幺鸡\",\"code\":\"[不要啊]\"},{\"rid\":\"862\",\"groupname\":\"小幺鸡\",\"code\":\"[飙泪中]\"},{\"rid\":\"863\",\"groupname\":\"小幺鸡\",\"code\":\"[爱你哦]\"},{\"rid\":\"864\",\"groupname\":\"心情\",\"code\":\"[挤眼]\"},{\"rid\":\"865\",\"groupname\":\"心情\",\"code\":\"[亲亲]\"},{\"rid\":\"866\",\"groupname\":\"心情\",\"code\":\"[怒骂]\"},{\"rid\":\"867\",\"groupname\":\"心情\",\"code\":\"[太开心]\"},{\"rid\":\"868\",\"groupname\":\"心情\",\"code\":\"[懒得理你]\"},{\"rid\":\"869\",\"groupname\":\"心情\",\"code\":\"[打哈欠]\"},{\"rid\":\"870\",\"groupname\":\"心情\",\"code\":\"[生病]\"},{\"rid\":\"871\",\"groupname\":\"心情\",\"code\":\"[书呆子]\"},{\"rid\":\"872\",\"groupname\":\"心情\",\"code\":\"[失望]\"},{\"rid\":\"873\",\"groupname\":\"心情\",\"code\":\"[可怜]\"},{\"rid\":\"874\",\"groupname\":\"心情\",\"code\":\"[黑线]\"},{\"rid\":\"875\",\"groupname\":\"心情\",\"code\":\"[吐]\"},{\"rid\":\"876\",\"groupname\":\"心情\",\"code\":\"[委屈]\"},{\"rid\":\"877\",\"groupname\":\"心情\",\"code\":\"[思考]\"},{\"rid\":\"878\",\"groupname\":\"心情\",\"code\":\"[哈哈]\"},{\"rid\":\"879\",\"groupname\":\"心情\",\"code\":\"[嘘]\"},{\"rid\":\"880\",\"groupname\":\"心情\",\"code\":\"[右哼哼]\"},{\"rid\":\"881\",\"groupname\":\"心情\",\"code\":\"[左哼哼]\"},{\"rid\":\"882\",\"groupname\":\"心情\",\"code\":\"[疑问]\"},{\"rid\":\"883\",\"groupname\":\"心情\",\"code\":\"[阴险]\"},{\"rid\":\"884\",\"groupname\":\"心情\",\"code\":\"[顶]\"},{\"rid\":\"885\",\"groupname\":\"心情\",\"code\":\"[钱]\"},{\"rid\":\"886\",\"groupname\":\"心情\",\"code\":\"[悲伤]\"},{\"rid\":\"887\",\"groupname\":\"心情\",\"code\":\"[鄙视]\"},{\"rid\":\"888\",\"groupname\":\"心情\",\"code\":\"[拜拜]\"},{\"rid\":\"889\",\"groupname\":\"心情\",\"code\":\"[吃惊]\"},{\"rid\":\"890\",\"groupname\":\"心情\",\"code\":\"[闭嘴]\"},{\"rid\":\"891\",\"groupname\":\"心情\",\"code\":\"[衰]\"},{\"rid\":\"892\",\"groupname\":\"心情\",\"code\":\"[愤怒]\"},{\"rid\":\"893\",\"groupname\":\"心情\",\"code\":\"[感冒]\"},{\"rid\":\"894\",\"groupname\":\"心情\",\"code\":\"[酷]\"},{\"rid\":\"895\",\"groupname\":\"心情\",\"code\":\"[来]\"},{\"rid\":\"896\",\"groupname\":\"心情\",\"code\":\"[good]\"},{\"rid\":\"897\",\"groupname\":\"心情\",\"code\":\"[haha]\"},{\"rid\":\"898\",\"groupname\":\"心情\",\"code\":\"[不要]\"},{\"rid\":\"899\",\"groupname\":\"心情\",\"code\":\"[ok]\"},{\"rid\":\"900\",\"groupname\":\"心情\",\"code\":\"[拳头]\"},{\"rid\":\"901\",\"groupname\":\"心情\",\"code\":\"[弱]\"},{\"rid\":\"902\",\"groupname\":\"心情\",\"code\":\"[握手]\"},{\"rid\":\"903\",\"groupname\":\"心情\",\"code\":\"[赞]\"},{\"rid\":\"904\",\"groupname\":\"心情\",\"code\":\"[耶]\"},{\"rid\":\"905\",\"groupname\":\"心情\",\"code\":\"[最差]\"},{\"rid\":\"906\",\"groupname\":\"心情\",\"code\":\"[打哈气]\"},{\"rid\":\"907\",\"groupname\":\"心情\",\"code\":\"[可爱]\"},{\"rid\":\"908\",\"groupname\":\"心情\",\"code\":\"[嘻嘻]\"},{\"rid\":\"909\",\"groupname\":\"心情\",\"code\":\"[汗]\"},{\"rid\":\"910\",\"groupname\":\"心情\",\"code\":\"[呵呵]\"},{\"rid\":\"911\",\"groupname\":\"心情\",\"code\":\"[困]\"},{\"rid\":\"912\",\"groupname\":\"心情\",\"code\":\"[睡觉]\"},{\"rid\":\"913\",\"groupname\":\"心情\",\"code\":\"[害羞]\"},{\"rid\":\"914\",\"groupname\":\"心情\",\"code\":\"[泪]\"},{\"rid\":\"915\",\"groupname\":\"心情\",\"code\":\"[爱你]\"},{\"rid\":\"916\",\"groupname\":\"心情\",\"code\":\"[挖鼻屎]\"},{\"rid\":\"917\",\"groupname\":\"心情\",\"code\":\"[花心]\"},{\"rid\":\"918\",\"groupname\":\"心情\",\"code\":\"[偷笑]\"},{\"rid\":\"919\",\"groupname\":\"心情\",\"code\":\"[心]\"},{\"rid\":\"920\",\"groupname\":\"心情\",\"code\":\"[哼]\"},{\"rid\":\"921\",\"groupname\":\"心情\",\"code\":\"[鼓掌]\"},{\"rid\":\"922\",\"groupname\":\"心情\",\"code\":\"[晕]\"},{\"rid\":\"923\",\"groupname\":\"心情\",\"code\":\"[馋嘴]\"},{\"rid\":\"924\",\"groupname\":\"心情\",\"code\":\"[抓狂]\"},{\"rid\":\"925\",\"groupname\":\"心情\",\"code\":\"[抱抱]\"},{\"rid\":\"926\",\"groupname\":\"心情\",\"code\":\"[怒]\"},{\"rid\":\"927\",\"groupname\":\"心情\",\"code\":\"[右抱抱]\"},{\"rid\":\"928\",\"groupname\":\"心情\",\"code\":\"[左抱抱]\"},{\"rid\":\"929\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc生日快乐]\"},{\"rid\":\"930\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc自重]\"},{\"rid\":\"931\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc转头]\"},{\"rid\":\"932\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc装酷]\"},{\"rid\":\"933\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc转发]\"},{\"rid\":\"934\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc中箭]\"},{\"rid\":\"935\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc晕]\"},{\"rid\":\"936\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc羞]\"},{\"rid\":\"937\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc围观]\"},{\"rid\":\"938\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc晚安]\"},{\"rid\":\"939\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc弹跳]\"},{\"rid\":\"940\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc石化]\"},{\"rid\":\"941\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc生气]\"},{\"rid\":\"942\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc亲亲女]\"},{\"rid\":\"943\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc亲亲男]\"},{\"rid\":\"944\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc亲吻]\"},{\"rid\":\"945\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc强吻]\"},{\"rid\":\"946\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc拍照]\"},{\"rid\":\"947\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc呕吐]\"},{\"rid\":\"948\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc冒出]\"},{\"rid\":\"949\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc路过]\"},{\"rid\":\"950\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc看清楚]\"},{\"rid\":\"951\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc结冰]\"},{\"rid\":\"952\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc挤]\"},{\"rid\":\"953\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc鬼脸]\"},{\"rid\":\"954\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc尴尬]\"},{\"rid\":\"955\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc浮云]\"},{\"rid\":\"956\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc顶]\"},{\"rid\":\"957\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc大哭]\"},{\"rid\":\"958\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc大口吃]\"},{\"rid\":\"959\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc打击]\"},{\"rid\":\"960\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc呲牙笑]\"},{\"rid\":\"961\",\"groupname\":\"摩丝摩丝\",\"code\":\"[moc扯脸]\"},{\"rid\":\"962\",\"groupname\":\"桂宝\",\"code\":\"[g思考]\"},{\"rid\":\"963\",\"groupname\":\"桂宝\",\"code\":\"[g震惊]\"},{\"rid\":\"964\",\"groupname\":\"桂宝\",\"code\":\"[g狂笑]\"},{\"rid\":\"965\",\"groupname\":\"桂宝\",\"code\":\"[g脸红]\"},{\"rid\":\"966\",\"groupname\":\"桂宝\",\"code\":\"[g发愣]\"},{\"rid\":\"967\",\"groupname\":\"桂宝\",\"code\":\"[g话痨]\"},{\"rid\":\"968\",\"groupname\":\"桂宝\",\"code\":\"[g吹发]\"},{\"rid\":\"969\",\"groupname\":\"桂宝\",\"code\":\"[g爆哭]\"},{\"rid\":\"970\",\"groupname\":\"桂宝\",\"code\":\"[g伤心]\"},{\"rid\":\"971\",\"groupname\":\"桂宝\",\"code\":\"[g得瑟]\"},{\"rid\":\"972\",\"groupname\":\"桂宝\",\"code\":\"[g魅眼]\"},{\"rid\":\"973\",\"groupname\":\"桂宝\",\"code\":\"[g无辜]\"},{\"rid\":\"974\",\"groupname\":\"桂宝\",\"code\":\"[g挑眉]\"},{\"rid\":\"975\",\"groupname\":\"桂宝\",\"code\":\"[g墨镜1]\"},{\"rid\":\"976\",\"groupname\":\"桂宝\",\"code\":\"[g墨镜2]\"},{\"rid\":\"977\",\"groupname\":\"桂宝\",\"code\":\"[g变脸]\"},{\"rid\":\"978\",\"groupname\":\"桂宝\",\"code\":\"[g扇笑]\"},{\"rid\":\"979\",\"groupname\":\"桂宝\",\"code\":\"[g扣鼻]\"},{\"rid\":\"980\",\"groupname\":\"桂宝\",\"code\":\"[g扣鼻2]\"},{\"rid\":\"981\",\"groupname\":\"桂宝\",\"code\":\"[g瀑汗]\"},{\"rid\":\"982\",\"groupname\":\"桂宝\",\"code\":\"[g汗滴]\"},{\"rid\":\"983\",\"groupname\":\"桂宝\",\"code\":\"[g咀嚼]\"},{\"rid\":\"984\",\"groupname\":\"桂宝\",\"code\":\"[g阴影]\"},{\"rid\":\"985\",\"groupname\":\"桂宝\",\"code\":\"[g鼻血]\"},{\"rid\":\"986\",\"groupname\":\"桂宝\",\"code\":\"[g呕吐]\"},{\"rid\":\"987\",\"groupname\":\"桂宝\",\"code\":\"[g噴血]\"},{\"rid\":\"988\",\"groupname\":\"桂宝\",\"code\":\"[g泪滴]\"},{\"rid\":\"989\",\"groupname\":\"桂宝\",\"code\":\"[g惊讶1]\"},{\"rid\":\"990\",\"groupname\":\"桂宝\",\"code\":\"[g头晕]\"},{\"rid\":\"991\",\"groupname\":\"桂宝\",\"code\":\"[g闪牙1]\"},{\"rid\":\"992\",\"groupname\":\"桂宝\",\"code\":\"[g闪牙2]\"},{\"rid\":\"993\",\"groupname\":\"桂宝\",\"code\":\"[g巨汗]\"},{\"rid\":\"994\",\"groupname\":\"桂宝\",\"code\":\"[g鼓掌]\"},{\"rid\":\"995\",\"groupname\":\"桂宝\",\"code\":\"[g招呼]\"},{\"rid\":\"996\",\"groupname\":\"桂宝\",\"code\":\"[g鼓掌2]\"},{\"rid\":\"997\",\"groupname\":\"桂宝\",\"code\":\"[g无所谓]\"},{\"rid\":\"998\",\"groupname\":\"桂宝\",\"code\":\"[g雷击]\"},{\"rid\":\"999\",\"groupname\":\"桂宝\",\"code\":\"[g邪笑]\"},{\"rid\":\"1000\",\"groupname\":\"桂宝\",\"code\":\"[g裸奔1]\"},{\"rid\":\"1001\",\"groupname\":\"桂宝\",\"code\":\"[g裸奔2]\"},{\"rid\":\"1002\",\"groupname\":\"桂宝\",\"code\":\"[g裸奔3]\"},{\"rid\":\"1003\",\"groupname\":\"桂宝\",\"code\":\"[g举刀]\"},{\"rid\":\"1004\",\"groupname\":\"桂宝\",\"code\":\"[g喝茶]\"},{\"rid\":\"1005\",\"groupname\":\"桂宝\",\"code\":\"[g摇手]\"},{\"rid\":\"1006\",\"groupname\":\"桂宝\",\"code\":\"[g病了]\"},{\"rid\":\"1007\",\"groupname\":\"桂宝\",\"code\":\"[g冻上]\"},{\"rid\":\"1008\",\"groupname\":\"桂宝\",\"code\":\"[g好冷]\"},{\"rid\":\"1009\",\"groupname\":\"桂宝\",\"code\":\"[g委屈]\"},{\"rid\":\"1010\",\"groupname\":\"桂宝\",\"code\":\"[g发飘]\"},{\"rid\":\"1011\",\"groupname\":\"桂宝\",\"code\":\"[g卖萌]\"},{\"rid\":\"1012\",\"groupname\":\"桂宝\",\"code\":\"[g唱歌]\"},{\"rid\":\"1013\",\"groupname\":\"桂宝\",\"code\":\"[g吃糖]\"},{\"rid\":\"1014\",\"groupname\":\"桂宝\",\"code\":\"[g桂宝]\"},{\"rid\":\"1015\",\"groupname\":\"桂宝\",\"code\":\"[g汪汪]\"},{\"rid\":\"1016\",\"groupname\":\"桂宝\",\"code\":\"[g吐舌]\"},{\"rid\":\"1017\",\"groupname\":\"桂宝\",\"code\":\"[g骨头]\"},{\"rid\":\"1018\",\"groupname\":\"桂宝\",\"code\":\"[g口水]\"},{\"rid\":\"1019\",\"groupname\":\"桂宝\",\"code\":\"[g惊讶2]\"},{\"rid\":\"1020\",\"groupname\":\"桂宝\",\"code\":\"[g爆哭2]\"},{\"rid\":\"1021\",\"groupname\":\"桂宝\",\"code\":\"[g激动]\"},{\"rid\":\"1022\",\"groupname\":\"懒猫猫\",\"code\":\"[lm招财猫]\"},{\"rid\":\"1023\",\"groupname\":\"懒猫猫\",\"code\":\"[lm贼笑]\"},{\"rid\":\"1024\",\"groupname\":\"懒猫猫\",\"code\":\"[lm严肃]\"},{\"rid\":\"1025\",\"groupname\":\"懒猫猫\",\"code\":\"[lm小地主]\"},{\"rid\":\"1026\",\"groupname\":\"懒猫猫\",\"code\":\"[lm无奈]\"},{\"rid\":\"1027\",\"groupname\":\"懒猫猫\",\"code\":\"[lm挖鼻屎]\"},{\"rid\":\"1028\",\"groupname\":\"懒猫猫\",\"code\":\"[lm天然呆]\"},{\"rid\":\"1029\",\"groupname\":\"懒猫猫\",\"code\":\"[lm生病了]\"},{\"rid\":\"1030\",\"groupname\":\"懒猫猫\",\"code\":\"[lm扑克脸]\"},{\"rid\":\"1031\",\"groupname\":\"懒猫猫\",\"code\":\"[lm瀑布汗]\"},{\"rid\":\"1032\",\"groupname\":\"懒猫猫\",\"code\":\"[lm磨牙]\"},{\"rid\":\"1033\",\"groupname\":\"懒猫猫\",\"code\":\"[lm没听见]\"},{\"rid\":\"1034\",\"groupname\":\"懒猫猫\",\"code\":\"[lm没事吧]\"},{\"rid\":\"1035\",\"groupname\":\"懒猫猫\",\"code\":\"[lm茫然]\"},{\"rid\":\"1036\",\"groupname\":\"懒猫猫\",\"code\":\"[lm泪流满面]\"},{\"rid\":\"1037\",\"groupname\":\"懒猫猫\",\"code\":\"[lm囧汗]\"},{\"rid\":\"1038\",\"groupname\":\"懒猫猫\",\"code\":\"[lm惊恐]\"},{\"rid\":\"1039\",\"groupname\":\"懒猫猫\",\"code\":\"[lm惊呆]\"},{\"rid\":\"1040\",\"groupname\":\"懒猫猫\",\"code\":\"[lm警察]\"},{\"rid\":\"1041\",\"groupname\":\"懒猫猫\",\"code\":\"[lm混乱中]\"},{\"rid\":\"1042\",\"groupname\":\"懒猫猫\",\"code\":\"[lm花痴]\"},{\"rid\":\"1043\",\"groupname\":\"懒猫猫\",\"code\":\"[lm喝水]\"},{\"rid\":\"1044\",\"groupname\":\"懒猫猫\",\"code\":\"[lm嘿嘿]\"},{\"rid\":\"1045\",\"groupname\":\"懒猫猫\",\"code\":\"[lm哈哈哈]\"},{\"rid\":\"1046\",\"groupname\":\"懒猫猫\",\"code\":\"[lm干笑]\"},{\"rid\":\"1047\",\"groupname\":\"懒猫猫\",\"code\":\"[lm疯了]\"},{\"rid\":\"1048\",\"groupname\":\"懒猫猫\",\"code\":\"[lm恶心]\"},{\"rid\":\"1049\",\"groupname\":\"懒猫猫\",\"code\":\"[lm嘟嘟嘴]\"},{\"rid\":\"1050\",\"groupname\":\"懒猫猫\",\"code\":\"[lm滴蜡]\"},{\"rid\":\"1051\",\"groupname\":\"懒猫猫\",\"code\":\"[lm点头]\"},{\"rid\":\"1052\",\"groupname\":\"懒猫猫\",\"code\":\"[lm大怒]\"},{\"rid\":\"1053\",\"groupname\":\"懒猫猫\",\"code\":\"[lm大惊失色]\"},{\"rid\":\"1054\",\"groupname\":\"懒猫猫\",\"code\":\"[lm呆笑]\"},{\"rid\":\"1055\",\"groupname\":\"懒猫猫\",\"code\":\"[lm搭错线]\"},{\"rid\":\"1056\",\"groupname\":\"懒猫猫\",\"code\":\"[lm大便]\"},{\"rid\":\"1057\",\"groupname\":\"懒猫猫\",\"code\":\"[lm不]\"},{\"rid\":\"1058\",\"groupname\":\"懒猫猫\",\"code\":\"[lm鼻涕虫]\"},{\"rid\":\"1059\",\"groupname\":\"懒猫猫\",\"code\":\"[lm暴雨汗]\"},{\"rid\":\"1060\",\"groupname\":\"懒猫猫\",\"code\":\"[lm啊呜啊呜]\"},{\"rid\":\"1061\",\"groupname\":\"懒猫猫\",\"code\":\"[lm爱爱爱]\"},{\"rid\":\"1062\",\"groupname\":\"懒猫猫\",\"code\":\"[mk拜年]\"},{\"rid\":\"1063\",\"groupname\":\"懒猫猫\",\"code\":\"[真淡定]\"},{\"rid\":\"1064\",\"groupname\":\"懒猫猫\",\"code\":\"[运气中]\"},{\"rid\":\"1065\",\"groupname\":\"懒猫猫\",\"code\":\"[嗯]\"},{\"rid\":\"1066\",\"groupname\":\"懒猫猫\",\"code\":\"[一头竖线]\"},{\"rid\":\"1067\",\"groupname\":\"懒猫猫\",\"code\":\"[星星眼儿]\"},{\"rid\":\"1068\",\"groupname\":\"懒猫猫\",\"code\":\"[笑眯眯]\"},{\"rid\":\"1069\",\"groupname\":\"懒猫猫\",\"code\":\"[小地主]\"},{\"rid\":\"1070\",\"groupname\":\"懒猫猫\",\"code\":\"[我错了]\"},{\"rid\":\"1071\",\"groupname\":\"懒猫猫\",\"code\":\"[喂]\"},{\"rid\":\"1072\",\"groupname\":\"懒猫猫\",\"code\":\"[伸舌头]\"},{\"rid\":\"1073\",\"groupname\":\"懒猫猫\",\"code\":\"[天然呆]\"},{\"rid\":\"1074\",\"groupname\":\"懒猫猫\",\"code\":\"[陶醉了]\"},{\"rid\":\"1075\",\"groupname\":\"懒猫猫\",\"code\":\"[生气了]\"},{\"rid\":\"1076\",\"groupname\":\"懒猫猫\",\"code\":\"[生病鸟]\"},{\"rid\":\"1077\",\"groupname\":\"懒猫猫\",\"code\":\"[忍不了]\"},{\"rid\":\"1078\",\"groupname\":\"懒猫猫\",\"code\":\"[扑克脸]\"},{\"rid\":\"1079\",\"groupname\":\"懒猫猫\",\"code\":\"[瀑布汗]\"},{\"rid\":\"1080\",\"groupname\":\"懒猫猫\",\"code\":\"[你没事吧]\"},{\"rid\":\"1081\",\"groupname\":\"懒猫猫\",\"code\":\"[内牛满面]\"},{\"rid\":\"1082\",\"groupname\":\"懒猫猫\",\"code\":\"[没听见]\"},{\"rid\":\"1083\",\"groupname\":\"懒猫猫\",\"code\":\"[哭死啦]\"},{\"rid\":\"1084\",\"groupname\":\"懒猫猫\",\"code\":\"[囧汗]\"},{\"rid\":\"1085\",\"groupname\":\"懒猫猫\",\"code\":\"[惊恐中]\"},{\"rid\":\"1086\",\"groupname\":\"懒猫猫\",\"code\":\"[混乱中]\"},{\"rid\":\"1087\",\"groupname\":\"懒猫猫\",\"code\":\"[花痴闪闪]\"},{\"rid\":\"1088\",\"groupname\":\"懒猫猫\",\"code\":\"[嘿嘿嘿]\"},{\"rid\":\"1089\",\"groupname\":\"懒猫猫\",\"code\":\"[哈哈哈哈]\"},{\"rid\":\"1090\",\"groupname\":\"懒猫猫\",\"code\":\"[干笑中]\"},{\"rid\":\"1091\",\"groupname\":\"懒猫猫\",\"code\":\"[恶心死]\"},{\"rid\":\"1092\",\"groupname\":\"懒猫猫\",\"code\":\"[嘟嘟嘴]\"},{\"rid\":\"1093\",\"groupname\":\"懒猫猫\",\"code\":\"[大怒]\"},{\"rid\":\"1094\",\"groupname\":\"懒猫猫\",\"code\":\"[大惊失色]\"},{\"rid\":\"1095\",\"groupname\":\"懒猫猫\",\"code\":\"[呆呆]\"},{\"rid\":\"1096\",\"groupname\":\"懒猫猫\",\"code\":\"[搭错线]\"},{\"rid\":\"1097\",\"groupname\":\"懒猫猫\",\"code\":\"[鼻涕虫]\"},{\"rid\":\"1098\",\"groupname\":\"懒猫猫\",\"code\":\"[暴雨汗]\"},{\"rid\":\"1099\",\"groupname\":\"懒猫猫\",\"code\":\"[啊呜啊呜]\"},{\"rid\":\"1100\",\"groupname\":\"懒猫猫\",\"code\":\"[哇]\"},{\"rid\":\"1101\",\"groupname\":\"懒猫猫\",\"code\":\"[爱爱爱]\"},{\"rid\":\"1102\",\"groupname\":\"彼尔德\",\"code\":\"[bed蹬腿]\"},{\"rid\":\"1103\",\"groupname\":\"彼尔德\",\"code\":\"[bed弹跳]\"},{\"rid\":\"1104\",\"groupname\":\"彼尔德\",\"code\":\"[bed扯]\"},{\"rid\":\"1105\",\"groupname\":\"彼尔德\",\"code\":\"[bed凌乱]\"},{\"rid\":\"1106\",\"groupname\":\"彼尔德\",\"code\":\"[bed奔跑]\"},{\"rid\":\"1107\",\"groupname\":\"彼尔德\",\"code\":\"[bed仰卧起坐]\"},{\"rid\":\"1108\",\"groupname\":\"彼尔德\",\"code\":\"[bed出浴]\"},{\"rid\":\"1109\",\"groupname\":\"彼尔德\",\"code\":\"[bed练腰]\"},{\"rid\":\"1110\",\"groupname\":\"彼尔德\",\"code\":\"[bed皮]\"},{\"rid\":\"1111\",\"groupname\":\"彼尔德\",\"code\":\"[bed挠痒]\"},{\"rid\":\"1112\",\"groupname\":\"彼尔德\",\"code\":\"[bed啦啦啦]\"},{\"rid\":\"1113\",\"groupname\":\"彼尔德\",\"code\":\"[bed举哑铃]\"},{\"rid\":\"1114\",\"groupname\":\"彼尔德\",\"code\":\"[bed飘忽]\"},{\"rid\":\"1115\",\"groupname\":\"彼尔德\",\"code\":\"[bed拍手]\"},{\"rid\":\"1116\",\"groupname\":\"彼尔德\",\"code\":\"[bed嘿哈]\"},{\"rid\":\"1117\",\"groupname\":\"彼尔德\",\"code\":\"[bed踏步]\"},{\"rid\":\"1118\",\"groupname\":\"彼尔德\",\"code\":\"[bed揉眼]\"},{\"rid\":\"1119\",\"groupname\":\"彼尔德\",\"code\":\"[bed转圈]\"},{\"rid\":\"1120\",\"groupname\":\"彼尔德\",\"code\":\"[bed飞吻]\"},{\"rid\":\"1121\",\"groupname\":\"彼尔德\",\"code\":\"[bed跳]\"},{\"rid\":\"1122\",\"groupname\":\"彼尔德\",\"code\":\"[bed巴掌]\"},{\"rid\":\"1123\",\"groupname\":\"彼尔德\",\"code\":\"[bed撒娇]\"},{\"rid\":\"1124\",\"groupname\":\"彼尔德\",\"code\":\"[bed拍脸]\"},{\"rid\":\"1125\",\"groupname\":\"彼尔德\",\"code\":\"[bed好饱]\"},{\"rid\":\"1126\",\"groupname\":\"彼尔德\",\"code\":\"[bed跑]\"},{\"rid\":\"1127\",\"groupname\":\"彼尔德\",\"code\":\"[bed兴奋]\"},{\"rid\":\"1128\",\"groupname\":\"彼尔德\",\"code\":\"[brd新]\"},{\"rid\":\"1129\",\"groupname\":\"彼尔德\",\"code\":\"[brd年]\"},{\"rid\":\"1130\",\"groupname\":\"彼尔德\",\"code\":\"[brd拜年]\"},{\"rid\":\"1131\",\"groupname\":\"彼尔德\",\"code\":\"[brd谨]\"},{\"rid\":\"1132\",\"groupname\":\"彼尔德\",\"code\":\"[brd贺]\"},{\"rid\":\"1133\",\"groupname\":\"天气\",\"code\":\"[雾]\"},{\"rid\":\"1134\",\"groupname\":\"天气\",\"code\":\"[台风]\"},{\"rid\":\"1135\",\"groupname\":\"天气\",\"code\":\"[沙尘暴]\"},{\"rid\":\"1136\",\"groupname\":\"天气\",\"code\":\"[晴转多云]\"},{\"rid\":\"1137\",\"groupname\":\"天气\",\"code\":\"[流星]\"},{\"rid\":\"1138\",\"groupname\":\"天气\",\"code\":\"[龙卷风]\"},{\"rid\":\"1139\",\"groupname\":\"天气\",\"code\":\"[洪水]\"},{\"rid\":\"1140\",\"groupname\":\"天气\",\"code\":\"[风]\"},{\"rid\":\"1141\",\"groupname\":\"天气\",\"code\":\"[多云转晴]\"},{\"rid\":\"1142\",\"groupname\":\"天气\",\"code\":\"[彩虹]\"},{\"rid\":\"1143\",\"groupname\":\"天气\",\"code\":\"[冰雹]\"},{\"rid\":\"1144\",\"groupname\":\"天气\",\"code\":\"[微风]\"},{\"rid\":\"1145\",\"groupname\":\"天气\",\"code\":\"[阳光]\"},{\"rid\":\"1146\",\"groupname\":\"天气\",\"code\":\"[雪]\"},{\"rid\":\"1147\",\"groupname\":\"天气\",\"code\":\"[闪电]\"},{\"rid\":\"1148\",\"groupname\":\"天气\",\"code\":\"[下雨]\"},{\"rid\":\"1149\",\"groupname\":\"天气\",\"code\":\"[阴天]\"},{\"rid\":\"1150\",\"groupname\":\"休闲\",\"code\":\"[鞭炮]\"},{\"rid\":\"1151\",\"groupname\":\"休闲\",\"code\":\"[让红包飞]\"},{\"rid\":\"1152\",\"groupname\":\"休闲\",\"code\":\"[围脖]\"},{\"rid\":\"1153\",\"groupname\":\"休闲\",\"code\":\"[温暖帽子]\"},{\"rid\":\"1154\",\"groupname\":\"休闲\",\"code\":\"[手套]\"},{\"rid\":\"1155\",\"groupname\":\"休闲\",\"code\":\"[红包]\"},{\"rid\":\"1156\",\"groupname\":\"休闲\",\"code\":\"[喜]\"},{\"rid\":\"1157\",\"groupname\":\"休闲\",\"code\":\"[礼物]\"},{\"rid\":\"1158\",\"groupname\":\"休闲\",\"code\":\"[蛋糕]\"},{\"rid\":\"1159\",\"groupname\":\"休闲\",\"code\":\"[钻戒]\"},{\"rid\":\"1160\",\"groupname\":\"休闲\",\"code\":\"[钻石]\"},{\"rid\":\"1161\",\"groupname\":\"休闲\",\"code\":\"[大巴]\"},{\"rid\":\"1162\",\"groupname\":\"休闲\",\"code\":\"[飞机]\"},{\"rid\":\"1163\",\"groupname\":\"休闲\",\"code\":\"[自行车]\"},{\"rid\":\"1164\",\"groupname\":\"休闲\",\"code\":\"[汽车]\"},{\"rid\":\"1165\",\"groupname\":\"休闲\",\"code\":\"[手机]\"},{\"rid\":\"1166\",\"groupname\":\"休闲\",\"code\":\"[照相机]\"},{\"rid\":\"1167\",\"groupname\":\"休闲\",\"code\":\"[药]\"},{\"rid\":\"1168\",\"groupname\":\"休闲\",\"code\":\"[电脑]\"},{\"rid\":\"1169\",\"groupname\":\"休闲\",\"code\":\"[手纸]\"},{\"rid\":\"1170\",\"groupname\":\"休闲\",\"code\":\"[落叶]\"},{\"rid\":\"1171\",\"groupname\":\"休闲\",\"code\":\"[圣诞树]\"},{\"rid\":\"1172\",\"groupname\":\"休闲\",\"code\":\"[圣诞帽]\"},{\"rid\":\"1173\",\"groupname\":\"休闲\",\"code\":\"[圣诞老人]\"},{\"rid\":\"1174\",\"groupname\":\"休闲\",\"code\":\"[圣诞铃铛]\"},{\"rid\":\"1175\",\"groupname\":\"休闲\",\"code\":\"[圣诞袜]\"},{\"rid\":\"1176\",\"groupname\":\"休闲\",\"code\":\"[bh彪悍]\"},{\"rid\":\"1177\",\"groupname\":\"休闲\",\"code\":\"[offthewall]\"},{\"rid\":\"1178\",\"groupname\":\"休闲\",\"code\":\"[助力山东]\"},{\"rid\":\"1179\",\"groupname\":\"休闲\",\"code\":\"[助力广东]\"},{\"rid\":\"1180\",\"groupname\":\"休闲\",\"code\":\"[K兵加油]\"},{\"rid\":\"1181\",\"groupname\":\"休闲\",\"code\":\"[起亚律动]\"},{\"rid\":\"1182\",\"groupname\":\"休闲\",\"code\":\"[草泥马]\"},{\"rid\":\"1183\",\"groupname\":\"休闲\",\"code\":\"[微博三周年]\"},{\"rid\":\"1184\",\"groupname\":\"休闲\",\"code\":\"[皇小冠]\"},{\"rid\":\"1185\",\"groupname\":\"休闲\",\"code\":\"[达人一周年]\"},{\"rid\":\"1186\",\"groupname\":\"休闲\",\"code\":\"[伦敦奥火]\"},{\"rid\":\"1187\",\"groupname\":\"休闲\",\"code\":\"[神龙]\"},{\"rid\":\"1188\",\"groupname\":\"休闲\",\"code\":\"[龙蛋]\"},{\"rid\":\"1189\",\"groupname\":\"休闲\",\"code\":\"[驯鹿]\"},{\"rid\":\"1190\",\"groupname\":\"休闲\",\"code\":\"[上海志愿者]\"},{\"rid\":\"1191\",\"groupname\":\"休闲\",\"code\":\"[音乐盒]\"},{\"rid\":\"1192\",\"groupname\":\"休闲\",\"code\":\"[首发]\"},{\"rid\":\"1193\",\"groupname\":\"休闲\",\"code\":\"[悼念乔布斯]\"},{\"rid\":\"1194\",\"groupname\":\"休闲\",\"code\":\"[iPhone]\"},{\"rid\":\"1195\",\"groupname\":\"休闲\",\"code\":\"[微博蛋糕]\"},{\"rid\":\"1196\",\"groupname\":\"休闲\",\"code\":\"[蜡烛]\"},{\"rid\":\"1197\",\"groupname\":\"休闲\",\"code\":\"[康乃馨]\"},{\"rid\":\"1198\",\"groupname\":\"休闲\",\"code\":\"[图片]\"},{\"rid\":\"1199\",\"groupname\":\"休闲\",\"code\":\"[植树节]\"},{\"rid\":\"1200\",\"groupname\":\"休闲\",\"code\":\"[粉蛋糕]\"},{\"rid\":\"1201\",\"groupname\":\"休闲\",\"code\":\"[糖果]\"},{\"rid\":\"1202\",\"groupname\":\"休闲\",\"code\":\"[万圣节]\"},{\"rid\":\"1203\",\"groupname\":\"休闲\",\"code\":\"[火炬]\"},{\"rid\":\"1204\",\"groupname\":\"休闲\",\"code\":\"[酒壶]\"},{\"rid\":\"1205\",\"groupname\":\"休闲\",\"code\":\"[月饼]\"},{\"rid\":\"1206\",\"groupname\":\"休闲\",\"code\":\"[满月]\"},{\"rid\":\"1207\",\"groupname\":\"休闲\",\"code\":\"[黑板]\"},{\"rid\":\"1208\",\"groupname\":\"休闲\",\"code\":\"[巧克力]\"},{\"rid\":\"1209\",\"groupname\":\"休闲\",\"code\":\"[脚印]\"},{\"rid\":\"1210\",\"groupname\":\"休闲\",\"code\":\"[酒]\"},{\"rid\":\"1211\",\"groupname\":\"休闲\",\"code\":\"[狗]\"},{\"rid\":\"1212\",\"groupname\":\"休闲\",\"code\":\"[工作]\"},{\"rid\":\"1213\",\"groupname\":\"休闲\",\"code\":\"[档案]\"},{\"rid\":\"1214\",\"groupname\":\"休闲\",\"code\":\"[叶子]\"},{\"rid\":\"1215\",\"groupname\":\"休闲\",\"code\":\"[钢琴]\"},{\"rid\":\"1216\",\"groupname\":\"休闲\",\"code\":\"[印迹]\"},{\"rid\":\"1217\",\"groupname\":\"休闲\",\"code\":\"[钟]\"},{\"rid\":\"1218\",\"groupname\":\"休闲\",\"code\":\"[茶]\"},{\"rid\":\"1219\",\"groupname\":\"休闲\",\"code\":\"[西瓜]\"},{\"rid\":\"1220\",\"groupname\":\"休闲\",\"code\":\"[雨伞]\"},{\"rid\":\"1221\",\"groupname\":\"休闲\",\"code\":\"[电视机]\"},{\"rid\":\"1222\",\"groupname\":\"休闲\",\"code\":\"[电话]\"},{\"rid\":\"1223\",\"groupname\":\"休闲\",\"code\":\"[太阳]\"},{\"rid\":\"1224\",\"groupname\":\"休闲\",\"code\":\"[星]\"},{\"rid\":\"1225\",\"groupname\":\"休闲\",\"code\":\"[哨子]\"},{\"rid\":\"1226\",\"groupname\":\"休闲\",\"code\":\"[话筒]\"},{\"rid\":\"1227\",\"groupname\":\"休闲\",\"code\":\"[音乐]\"},{\"rid\":\"1228\",\"groupname\":\"休闲\",\"code\":\"[电影]\"},{\"rid\":\"1229\",\"groupname\":\"休闲\",\"code\":\"[月亮]\"},{\"rid\":\"1230\",\"groupname\":\"休闲\",\"code\":\"[唱歌]\"},{\"rid\":\"1231\",\"groupname\":\"休闲\",\"code\":\"[冰棍]\"},{\"rid\":\"1232\",\"groupname\":\"休闲\",\"code\":\"[房子]\"},{\"rid\":\"1233\",\"groupname\":\"休闲\",\"code\":\"[帽子]\"},{\"rid\":\"1234\",\"groupname\":\"休闲\",\"code\":\"[足球]\"},{\"rid\":\"1235\",\"groupname\":\"休闲\",\"code\":\"[鲜花]\"},{\"rid\":\"1236\",\"groupname\":\"休闲\",\"code\":\"[花]\"},{\"rid\":\"1237\",\"groupname\":\"休闲\",\"code\":\"[风扇]\"},{\"rid\":\"1238\",\"groupname\":\"休闲\",\"code\":\"[干杯]\"},{\"rid\":\"1239\",\"groupname\":\"休闲\",\"code\":\"[咖啡]\"},{\"rid\":\"1240\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb生日快乐]\"},{\"rid\":\"1241\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb愚人节]\"},{\"rid\":\"1242\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb圣诞圣衣]\"},{\"rid\":\"1243\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb叮叮当]\"},{\"rid\":\"1244\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb早安]\"},{\"rid\":\"1245\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb洗澡]\"},{\"rid\":\"1246\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb脱光]\"},{\"rid\":\"1247\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb喷血]\"},{\"rid\":\"1248\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb捏捏]\"},{\"rid\":\"1249\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb裸走]\"},{\"rid\":\"1250\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb裸舞]\"},{\"rid\":\"1251\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb路过]\"},{\"rid\":\"1252\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb激动]\"},{\"rid\":\"1253\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb奸笑]\"},{\"rid\":\"1254\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb鼓掌]\"},{\"rid\":\"1255\",\"groupname\":\"炮炮兵\",\"code\":\"[ppbbibi]\"},{\"rid\":\"1256\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb靠]\"},{\"rid\":\"1257\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb发狂]\"},{\"rid\":\"1258\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb困]\"},{\"rid\":\"1259\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb啊哈哈]\"},{\"rid\":\"1260\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb僵尸]\"},{\"rid\":\"1261\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb甩嘴]\"},{\"rid\":\"1262\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb囧]\"},{\"rid\":\"1263\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb去死]\"},{\"rid\":\"1264\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb晴天霹雳]\"},{\"rid\":\"1265\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb啊]\"},{\"rid\":\"1266\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb大哭]\"},{\"rid\":\"1267\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb我砍]\"},{\"rid\":\"1268\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb扫射]\"},{\"rid\":\"1269\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb杀啊]\"},{\"rid\":\"1270\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb啊呜]\"},{\"rid\":\"1271\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb蝙蝠侠]\"},{\"rid\":\"1272\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb滚]\"},{\"rid\":\"1273\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb欢迎欢迎]\"},{\"rid\":\"1274\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb狂吃]\"},{\"rid\":\"1275\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb讨厌]\"},{\"rid\":\"1276\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb爱你哟]\"},{\"rid\":\"1277\",\"groupname\":\"炮炮兵\",\"code\":\"[ppb卖萌]\"},{\"rid\":\"1278\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊做面膜]\"},{\"rid\":\"1279\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊咒骂]\"},{\"rid\":\"1280\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊震惊]\"},{\"rid\":\"1281\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊yes]\"},{\"rid\":\"1282\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊掩面]\"},{\"rid\":\"1283\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊乌鸦]\"},{\"rid\":\"1284\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊无奈]\"},{\"rid\":\"1285\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊晚安]\"},{\"rid\":\"1286\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊生日快乐]\"},{\"rid\":\"1287\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊撒欢]\"},{\"rid\":\"1288\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊no]\"},{\"rid\":\"1289\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊路过]\"},{\"rid\":\"1290\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊流汗]\"},{\"rid\":\"1291\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊流鼻血]\"},{\"rid\":\"1292\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊雷死]\"},{\"rid\":\"1293\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊泪奔]\"},{\"rid\":\"1294\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊哭泣]\"},{\"rid\":\"1295\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊开心]\"},{\"rid\":\"1296\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊开饭咯]\"},{\"rid\":\"1297\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊纠结]\"},{\"rid\":\"1298\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊害羞]\"},{\"rid\":\"1299\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊鼓掌]\"},{\"rid\":\"1300\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊感动]\"},{\"rid\":\"1301\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊浮云]\"},{\"rid\":\"1302\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊飞吻]\"},{\"rid\":\"1303\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊打招呼]\"},{\"rid\":\"1304\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊补妆]\"},{\"rid\":\"1305\",\"groupname\":\"哎呦熊\",\"code\":\"[哎呦熊崩溃]\"},{\"rid\":\"1306\",\"groupname\":\"搞怪\",\"code\":\"[织]\"},{\"rid\":\"1307\",\"groupname\":\"搞怪\",\"code\":\"[兔子]\"},{\"rid\":\"1308\",\"groupname\":\"搞怪\",\"code\":\"[神马]\"},{\"rid\":\"1309\",\"groupname\":\"搞怪\",\"code\":\"[浮云]\"},{\"rid\":\"1310\",\"groupname\":\"搞怪\",\"code\":\"[给力]\"},{\"rid\":\"1311\",\"groupname\":\"搞怪\",\"code\":\"[萌]\"},{\"rid\":\"1312\",\"groupname\":\"搞怪\",\"code\":\"[熊猫]\"},{\"rid\":\"1313\",\"groupname\":\"搞怪\",\"code\":\"[互粉]\"},{\"rid\":\"1314\",\"groupname\":\"搞怪\",\"code\":\"[围观]\"},{\"rid\":\"1315\",\"groupname\":\"搞怪\",\"code\":\"[扔鸡蛋]\"},{\"rid\":\"1316\",\"groupname\":\"搞怪\",\"code\":\"[奥特曼]\"},{\"rid\":\"1317\",\"groupname\":\"搞怪\",\"code\":\"[威武]\"},{\"rid\":\"1318\",\"groupname\":\"搞怪\",\"code\":\"[伤心]\"},{\"rid\":\"1319\",\"groupname\":\"搞怪\",\"code\":\"[热吻]\"},{\"rid\":\"1320\",\"groupname\":\"搞怪\",\"code\":\"[囧]\"},{\"rid\":\"1321\",\"groupname\":\"搞怪\",\"code\":\"[orz]\"},{\"rid\":\"1322\",\"groupname\":\"搞怪\",\"code\":\"[宅]\"},{\"rid\":\"1323\",\"groupname\":\"搞怪\",\"code\":\"[帅]\"},{\"rid\":\"1324\",\"groupname\":\"搞怪\",\"code\":\"[猪头]\"},{\"rid\":\"1325\",\"groupname\":\"搞怪\",\"code\":\"[实习]\"},{\"rid\":\"1326\",\"groupname\":\"搞怪\",\"code\":\"[骷髅]\"},{\"rid\":\"1327\",\"groupname\":\"搞怪\",\"code\":\"[便便]\"},{\"rid\":\"1328\",\"groupname\":\"搞怪\",\"code\":\"[黄牌]\"},{\"rid\":\"1329\",\"groupname\":\"搞怪\",\"code\":\"[红牌]\"},{\"rid\":\"1330\",\"groupname\":\"搞怪\",\"code\":\"[跳舞花]\"},{\"rid\":\"1331\",\"groupname\":\"搞怪\",\"code\":\"[礼花]\"},{\"rid\":\"1332\",\"groupname\":\"搞怪\",\"code\":\"[打针]\"},{\"rid\":\"1333\",\"groupname\":\"搞怪\",\"code\":\"[叹号]\"},{\"rid\":\"1334\",\"groupname\":\"搞怪\",\"code\":\"[问号]\"},{\"rid\":\"1335\",\"groupname\":\"搞怪\",\"code\":\"[句号]\"},{\"rid\":\"1336\",\"groupname\":\"搞怪\",\"code\":\"[闪]\"},{\"rid\":\"1337\",\"groupname\":\"搞怪\",\"code\":\"[啦啦]\"},{\"rid\":\"1338\",\"groupname\":\"搞怪\",\"code\":\"[吼吼]\"},{\"rid\":\"1339\",\"groupname\":\"搞怪\",\"code\":\"[庆祝]\"},{\"rid\":\"1340\",\"groupname\":\"搞怪\",\"code\":\"[嘿]\"},{\"rid\":\"1341\",\"groupname\":\"搞怪\",\"code\":\"[1]\"},{\"rid\":\"1342\",\"groupname\":\"搞怪\",\"code\":\"[2]\"},{\"rid\":\"1343\",\"groupname\":\"搞怪\",\"code\":\"[3]\"},{\"rid\":\"1344\",\"groupname\":\"搞怪\",\"code\":\"[4]\"},{\"rid\":\"1345\",\"groupname\":\"搞怪\",\"code\":\"[5]\"},{\"rid\":\"1346\",\"groupname\":\"搞怪\",\"code\":\"[6]\"},{\"rid\":\"1347\",\"groupname\":\"搞怪\",\"code\":\"[7]\"},{\"rid\":\"1348\",\"groupname\":\"搞怪\",\"code\":\"[8]\"},{\"rid\":\"1349\",\"groupname\":\"搞怪\",\"code\":\"[9]\"},{\"rid\":\"1350\",\"groupname\":\"搞怪\",\"code\":\"[a]\"},{\"rid\":\"1351\",\"groupname\":\"搞怪\",\"code\":\"[b]\"},{\"rid\":\"1352\",\"groupname\":\"搞怪\",\"code\":\"[c]\"},{\"rid\":\"1353\",\"groupname\":\"搞怪\",\"code\":\"[d]\"},{\"rid\":\"1354\",\"groupname\":\"搞怪\",\"code\":\"[e]\"},{\"rid\":\"1355\",\"groupname\":\"搞怪\",\"code\":\"[f]\"},{\"rid\":\"1356\",\"groupname\":\"搞怪\",\"code\":\"[g]\"},{\"rid\":\"1357\",\"groupname\":\"搞怪\",\"code\":\"[h]\"},{\"rid\":\"1358\",\"groupname\":\"搞怪\",\"code\":\"[i]\"},{\"rid\":\"1359\",\"groupname\":\"搞怪\",\"code\":\"[j]\"},{\"rid\":\"1360\",\"groupname\":\"搞怪\",\"code\":\"[k]\"},{\"rid\":\"1361\",\"groupname\":\"搞怪\",\"code\":\"[l]\"},{\"rid\":\"1362\",\"groupname\":\"搞怪\",\"code\":\"[m]\"},{\"rid\":\"1363\",\"groupname\":\"搞怪\",\"code\":\"[n]\"},{\"rid\":\"1364\",\"groupname\":\"搞怪\",\"code\":\"[o]\"},{\"rid\":\"1365\",\"groupname\":\"搞怪\",\"code\":\"[p]\"},{\"rid\":\"1366\",\"groupname\":\"搞怪\",\"code\":\"[q]\"},{\"rid\":\"1367\",\"groupname\":\"搞怪\",\"code\":\"[r]\"},{\"rid\":\"1368\",\"groupname\":\"搞怪\",\"code\":\"[s]\"},{\"rid\":\"1369\",\"groupname\":\"搞怪\",\"code\":\"[t]\"},{\"rid\":\"1370\",\"groupname\":\"搞怪\",\"code\":\"[u]\"},{\"rid\":\"1371\",\"groupname\":\"搞怪\",\"code\":\"[v]\"},{\"rid\":\"1372\",\"groupname\":\"搞怪\",\"code\":\"[w]\"},{\"rid\":\"1373\",\"groupname\":\"搞怪\",\"code\":\"[x]\"},{\"rid\":\"1374\",\"groupname\":\"搞怪\",\"code\":\"[y]\"},{\"rid\":\"1375\",\"groupname\":\"搞怪\",\"code\":\"[z]\"},{\"rid\":\"1376\",\"groupname\":\"搞怪\",\"code\":\"[团]\"},{\"rid\":\"1377\",\"groupname\":\"搞怪\",\"code\":\"[圆]\"},{\"rid\":\"1378\",\"groupname\":\"搞怪\",\"code\":\"[男孩儿]\"},{\"rid\":\"1379\",\"groupname\":\"搞怪\",\"code\":\"[女孩儿]\"},{\"rid\":\"1380\",\"groupname\":\"搞怪\",\"code\":\"[做鬼脸]\"},{\"rid\":\"1381\",\"groupname\":\"搞怪\",\"code\":\"[点]\"},{\"rid\":\"1382\",\"groupname\":\"搞怪\",\"code\":\"[鸭梨]\"},{\"rid\":\"1383\",\"groupname\":\"搞怪\",\"code\":\"[省略号]\"},{\"rid\":\"1384\",\"groupname\":\"搞怪\",\"code\":\"[kiss]\"},{\"rid\":\"1385\",\"groupname\":\"搞怪\",\"code\":\"[雪人]\"},{\"rid\":\"1386\",\"groupname\":\"搞怪\",\"code\":\"[小丑]\"},{\"rid\":\"1387\",\"groupname\":\"块猫\",\"code\":\"[km问号]\"},{\"rid\":\"1388\",\"groupname\":\"块猫\",\"code\":\"[km爱你]\"},{\"rid\":\"1389\",\"groupname\":\"块猫\",\"code\":\"[km白块旋转]\"},{\"rid\":\"1390\",\"groupname\":\"块猫\",\"code\":\"[km黑块旋转]\"},{\"rid\":\"1391\",\"groupname\":\"块猫\",\"code\":\"[km花痴]\"},{\"rid\":\"1392\",\"groupname\":\"块猫\",\"code\":\"[km可爱]\"},{\"rid\":\"1393\",\"groupname\":\"块猫\",\"code\":\"[km切]\"},{\"rid\":\"1394\",\"groupname\":\"块猫\",\"code\":\"[km亲亲]\"},{\"rid\":\"1395\",\"groupname\":\"块猫\",\"code\":\"[km亲亲白块]\"},{\"rid\":\"1396\",\"groupname\":\"块猫\",\"code\":\"[km亲亲黑块]\"},{\"rid\":\"1397\",\"groupname\":\"块猫\",\"code\":\"[km挖鼻屎]\"},{\"rid\":\"1398\",\"groupname\":\"块猫\",\"code\":\"[km哇哇哭]\"},{\"rid\":\"1399\",\"groupname\":\"块猫\",\"code\":\"[km围观]\"},{\"rid\":\"1400\",\"groupname\":\"块猫\",\"code\":\"[km委屈]\"},{\"rid\":\"1401\",\"groupname\":\"块猫\",\"code\":\"[km羞]\"},{\"rid\":\"1402\",\"groupname\":\"块猫\",\"code\":\"[kmFL]\"},{\"rid\":\"1403\",\"groupname\":\"块猫\",\"code\":\"[km侦探]\"},{\"rid\":\"1404\",\"groupname\":\"块猫\",\"code\":\"[km嘻嘻]\"},{\"rid\":\"1405\",\"groupname\":\"块猫\",\"code\":\"[km呜呜1]\"},{\"rid\":\"1406\",\"groupname\":\"块猫\",\"code\":\"[km冷笑]\"},{\"rid\":\"1407\",\"groupname\":\"块猫\",\"code\":\"[km邮件]\"},{\"rid\":\"1408\",\"groupname\":\"块猫\",\"code\":\"[km闹钟]\"},{\"rid\":\"1409\",\"groupname\":\"块猫\",\"code\":\"[km哼]\"},{\"rid\":\"1410\",\"groupname\":\"块猫\",\"code\":\"[km无语]\"},{\"rid\":\"1411\",\"groupname\":\"块猫\",\"code\":\"[km黑块不淡定]\"},{\"rid\":\"1412\",\"groupname\":\"块猫\",\"code\":\"[km害怕]\"},{\"rid\":\"1413\",\"groupname\":\"块猫\",\"code\":\"[km呜呜88]\"},{\"rid\":\"1414\",\"groupname\":\"块猫\",\"code\":\"[km透亮]\"},{\"rid\":\"1415\",\"groupname\":\"块猫\",\"code\":\"[km唔]\"},{\"rid\":\"1416\",\"groupname\":\"块猫\",\"code\":\"[km侠盗]\"},{\"rid\":\"1417\",\"groupname\":\"块猫\",\"code\":\"[km醉]\"},{\"rid\":\"1418\",\"groupname\":\"块猫\",\"code\":\"[km丽莎2]\"},{\"rid\":\"1419\",\"groupname\":\"块猫\",\"code\":\"[km酷2]\"},{\"rid\":\"1420\",\"groupname\":\"块猫\",\"code\":\"[km憨]\"},{\"rid\":\"1421\",\"groupname\":\"块猫\",\"code\":\"[km中毒]\"},{\"rid\":\"1422\",\"groupname\":\"块猫\",\"code\":\"[km电视]\"},{\"rid\":\"1423\",\"groupname\":\"块猫\",\"code\":\"[km困]\"},{\"rid\":\"1424\",\"groupname\":\"块猫\",\"code\":\"[km高兴]\"},{\"rid\":\"1425\",\"groupname\":\"块猫\",\"code\":\"[km幺鸡猫]\"},{\"rid\":\"1426\",\"groupname\":\"块猫\",\"code\":\"[km黑化笑]\"},{\"rid\":\"1427\",\"groupname\":\"块猫\",\"code\":\"[km花猫]\"},{\"rid\":\"1428\",\"groupname\":\"块猫\",\"code\":\"[km好吃]\"},{\"rid\":\"1429\",\"groupname\":\"块猫\",\"code\":\"[kmAI]\"},{\"rid\":\"1430\",\"groupname\":\"块猫\",\"code\":\"[km黑化唠叨]\"},{\"rid\":\"1431\",\"groupname\":\"块猫\",\"code\":\"[km好吃惊]\"},{\"rid\":\"1432\",\"groupname\":\"块猫\",\"code\":\"[km唠叨]\"},{\"rid\":\"1433\",\"groupname\":\"块猫\",\"code\":\"[km眼镜]\"},{\"rid\":\"1434\",\"groupname\":\"块猫\",\"code\":\"[km闪]\"},{\"rid\":\"1435\",\"groupname\":\"块猫\",\"code\":\"[kmV]\"},{\"rid\":\"1436\",\"groupname\":\"块猫\",\"code\":\"[km不淡定]\"},{\"rid\":\"1437\",\"groupname\":\"块猫\",\"code\":\"[km鼻血1]\"},{\"rid\":\"1438\",\"groupname\":\"块猫\",\"code\":\"[km好饿]\"},{\"rid\":\"1439\",\"groupname\":\"块猫\",\"code\":\"[km上传]\"},{\"rid\":\"1440\",\"groupname\":\"块猫\",\"code\":\"[km黑化]\"},{\"rid\":\"1441\",\"groupname\":\"块猫\",\"code\":\"[km鼻血]\"},{\"rid\":\"1442\",\"groupname\":\"块猫\",\"code\":\"[km酷]\"},{\"rid\":\"1443\",\"groupname\":\"块猫\",\"code\":\"[km愁]\"},{\"rid\":\"1444\",\"groupname\":\"块猫\",\"code\":\"[km相机]\"},{\"rid\":\"1445\",\"groupname\":\"块猫\",\"code\":\"[km喜]\"},{\"rid\":\"1446\",\"groupname\":\"块猫\",\"code\":\"[km得意]\"},{\"rid\":\"1447\",\"groupname\":\"块猫\",\"code\":\"[km怒]\"},{\"rid\":\"1448\",\"groupname\":\"块猫\",\"code\":\"[km生气]\"},{\"rid\":\"1449\",\"groupname\":\"块猫\",\"code\":\"[kmDW]\"},{\"rid\":\"1450\",\"groupname\":\"块猫\",\"code\":\"[km呜血泪]\"},{\"rid\":\"1451\",\"groupname\":\"块猫\",\"code\":\"[kmPS]\"},{\"rid\":\"1452\",\"groupname\":\"块猫\",\"code\":\"[km馋]\"},{\"rid\":\"1453\",\"groupname\":\"块猫\",\"code\":\"[km下载]\"},{\"rid\":\"1454\",\"groupname\":\"块猫\",\"code\":\"[kmX]\"},{\"rid\":\"1455\",\"groupname\":\"块猫\",\"code\":\"[km情书]\"},{\"rid\":\"1456\",\"groupname\":\"块猫\",\"code\":\"[km骷髅]\"},{\"rid\":\"1457\",\"groupname\":\"块猫\",\"code\":\"[km丽莎]\"},{\"rid\":\"1458\",\"groupname\":\"块猫\",\"code\":\"[km禁]\"},{\"rid\":\"1459\",\"groupname\":\"块猫\",\"code\":\"[km晕]\"},{\"rid\":\"1460\",\"groupname\":\"块猫\",\"code\":\"[km热]\"},{\"rid\":\"1461\",\"groupname\":\"块猫\",\"code\":\"[km冷]\"},{\"rid\":\"1462\",\"groupname\":\"块猫\",\"code\":\"[km猫]\"},{\"rid\":\"1463\",\"groupname\":\"块猫\",\"code\":\"[km拜年]\"},{\"rid\":\"1464\",\"groupname\":\"柏夫\",\"code\":\"[bofu吐舌头]\"},{\"rid\":\"1465\",\"groupname\":\"柏夫\",\"code\":\"[bofu拜年]\"},{\"rid\":\"1466\",\"groupname\":\"柏夫\",\"code\":\"[bofu淫笑]\"},{\"rid\":\"1467\",\"groupname\":\"柏夫\",\"code\":\"[bofu压力山大]\"},{\"rid\":\"1468\",\"groupname\":\"柏夫\",\"code\":\"[bofu心灰意冷]\"},{\"rid\":\"1469\",\"groupname\":\"柏夫\",\"code\":\"[bofu心动]\"},{\"rid\":\"1470\",\"groupname\":\"柏夫\",\"code\":\"[bofu咸蛋超人]\"},{\"rid\":\"1471\",\"groupname\":\"柏夫\",\"code\":\"[bofu食神]\"},{\"rid\":\"1472\",\"groupname\":\"柏夫\",\"code\":\"[bofu票子快来]\"},{\"rid\":\"1473\",\"groupname\":\"柏夫\",\"code\":\"[bofu怒]\"},{\"rid\":\"1474\",\"groupname\":\"柏夫\",\"code\":\"[bofu扭]\"},{\"rid\":\"1475\",\"groupname\":\"柏夫\",\"code\":\"[bofu梦遗]\"},{\"rid\":\"1476\",\"groupname\":\"柏夫\",\"code\":\"[bofu累]\"},{\"rid\":\"1477\",\"groupname\":\"柏夫\",\"code\":\"[bofu啃西瓜]\"},{\"rid\":\"1478\",\"groupname\":\"柏夫\",\"code\":\"[bofu给力]\"},{\"rid\":\"1479\",\"groupname\":\"柏夫\",\"code\":\"[bofu发愤图强]\"},{\"rid\":\"1480\",\"groupname\":\"柏夫\",\"code\":\"[bofu抖骚]\"},{\"rid\":\"1481\",\"groupname\":\"柏夫\",\"code\":\"[bofu得瑟]\"},{\"rid\":\"1482\",\"groupname\":\"柏夫\",\"code\":\"[bofu打飞机]\"},{\"rid\":\"1483\",\"groupname\":\"柏夫\",\"code\":\"[bofu变脸]\"},{\"rid\":\"1484\",\"groupname\":\"柏夫\",\"code\":\"[bofu蹦极]\"},{\"rid\":\"1485\",\"groupname\":\"柏夫\",\"code\":\"[bofu暴躁]\"},{\"rid\":\"1486\",\"groupname\":\"萌萌\",\"code\":\"[萌萌星星眼]\"},{\"rid\":\"1487\",\"groupname\":\"萌萌\",\"code\":\"[萌萌打滚]\"},{\"rid\":\"1488\",\"groupname\":\"萌萌\",\"code\":\"[萌萌甩帽]\"},{\"rid\":\"1489\",\"groupname\":\"萌萌\",\"code\":\"[萌萌摔瓶]\"},{\"rid\":\"1490\",\"groupname\":\"萌萌\",\"code\":\"[萌萌扭屁股]\"},{\"rid\":\"1491\",\"groupname\":\"萌萌\",\"code\":\"[萌萌惊讶]\"},{\"rid\":\"1492\",\"groupname\":\"萌萌\",\"code\":\"[萌萌懒得理]\"},{\"rid\":\"1493\",\"groupname\":\"萌萌\",\"code\":\"[萌萌偷乐]\"},{\"rid\":\"1494\",\"groupname\":\"萌萌\",\"code\":\"[萌萌鄙视]\"},{\"rid\":\"1495\",\"groupname\":\"萌萌\",\"code\":\"[萌萌哈欠]\"},{\"rid\":\"1496\",\"groupname\":\"萌萌\",\"code\":\"[萌萌石化]\"},{\"rid\":\"1497\",\"groupname\":\"萌萌\",\"code\":\"[萌萌敲鼓]\"},{\"rid\":\"1498\",\"groupname\":\"萌萌\",\"code\":\"[萌萌叹气]\"},{\"rid\":\"1499\",\"groupname\":\"萌萌\",\"code\":\"[萌萌捶地笑]\"},{\"rid\":\"1500\",\"groupname\":\"萌萌\",\"code\":\"[萌萌捂脸]\"},{\"rid\":\"1501\",\"groupname\":\"萌萌\",\"code\":\"[萌萌流汗]\"},{\"rid\":\"1502\",\"groupname\":\"萌萌\",\"code\":\"[萌萌抠鼻]\"},{\"rid\":\"1503\",\"groupname\":\"萌萌\",\"code\":\"[萌萌泪奔]\"},{\"rid\":\"1504\",\"groupname\":\"萌萌\",\"code\":\"[萌萌献花]\"},{\"rid\":\"1505\",\"groupname\":\"管不着\",\"code\":\"[欢欢]\"},{\"rid\":\"1506\",\"groupname\":\"管不着\",\"code\":\"[乐乐]\"},{\"rid\":\"1507\",\"groupname\":\"管不着\",\"code\":\"[管不着爱]\"},{\"rid\":\"1508\",\"groupname\":\"管不着\",\"code\":\"[爱]\"},{\"rid\":\"1509\",\"groupname\":\"管不着\",\"code\":\"[了不起爱]\"},{\"rid\":\"1510\",\"groupname\":\"管不着\",\"code\":\"[gbz真穿越]\"},{\"rid\":\"1511\",\"groupname\":\"管不着\",\"code\":\"[gbz再睡会]\"},{\"rid\":\"1512\",\"groupname\":\"管不着\",\"code\":\"[gbz呜呜]\"},{\"rid\":\"1513\",\"groupname\":\"管不着\",\"code\":\"[gbz委屈]\"},{\"rid\":\"1514\",\"groupname\":\"管不着\",\"code\":\"[gbz晚安了]\"},{\"rid\":\"1515\",\"groupname\":\"管不着\",\"code\":\"[gbz祈福]\"},{\"rid\":\"1516\",\"groupname\":\"管不着\",\"code\":\"[gbz祈福了]\"},{\"rid\":\"1517\",\"groupname\":\"管不着\",\"code\":\"[gbz窃笑]\"},{\"rid\":\"1518\",\"groupname\":\"管不着\",\"code\":\"[gbz起床啦]\"},{\"rid\":\"1519\",\"groupname\":\"管不着\",\"code\":\"[gbz困]\"},{\"rid\":\"1520\",\"groupname\":\"管不着\",\"code\":\"[gbz加班]\"},{\"rid\":\"1521\",\"groupname\":\"管不着\",\"code\":\"[gbz加班中]\"},{\"rid\":\"1522\",\"groupname\":\"管不着\",\"code\":\"[gbz饿]\"},{\"rid\":\"1523\",\"groupname\":\"管不着\",\"code\":\"[gbz饿晕]\"},{\"rid\":\"1524\",\"groupname\":\"管不着\",\"code\":\"[gbz得意]\"},{\"rid\":\"1525\",\"groupname\":\"管不着\",\"code\":\"[gbz大笑]\"},{\"rid\":\"1526\",\"groupname\":\"管不着\",\"code\":\"[gbz穿越了]\"},{\"rid\":\"1527\",\"groupname\":\"管不着\",\"code\":\"[有点困]\"},{\"rid\":\"1528\",\"groupname\":\"管不着\",\"code\":\"[yes]\"},{\"rid\":\"1529\",\"groupname\":\"管不着\",\"code\":\"[咽回去了]\"},{\"rid\":\"1530\",\"groupname\":\"管不着\",\"code\":\"[鸭梨很大]\"},{\"rid\":\"1531\",\"groupname\":\"管不着\",\"code\":\"[羞羞]\"},{\"rid\":\"1532\",\"groupname\":\"管不着\",\"code\":\"[喜欢你]\"},{\"rid\":\"1533\",\"groupname\":\"管不着\",\"code\":\"[小便屁]\"},{\"rid\":\"1534\",\"groupname\":\"管不着\",\"code\":\"[无奈]\"},{\"rid\":\"1535\",\"groupname\":\"管不着\",\"code\":\"[兔兔]\"},{\"rid\":\"1536\",\"groupname\":\"管不着\",\"code\":\"[吐舌头]\"},{\"rid\":\"1537\",\"groupname\":\"管不着\",\"code\":\"[头晕]\"},{\"rid\":\"1538\",\"groupname\":\"管不着\",\"code\":\"[听音乐]\"},{\"rid\":\"1539\",\"groupname\":\"管不着\",\"code\":\"[睡大觉]\"},{\"rid\":\"1540\",\"groupname\":\"管不着\",\"code\":\"[闪闪紫]\"},{\"rid\":\"1541\",\"groupname\":\"管不着\",\"code\":\"[闪闪绿]\"},{\"rid\":\"1542\",\"groupname\":\"管不着\",\"code\":\"[闪闪灰]\"},{\"rid\":\"1543\",\"groupname\":\"管不着\",\"code\":\"[闪闪红]\"},{\"rid\":\"1544\",\"groupname\":\"管不着\",\"code\":\"[闪闪粉]\"},{\"rid\":\"1545\",\"groupname\":\"管不着\",\"code\":\"[咆哮]\"},{\"rid\":\"1546\",\"groupname\":\"管不着\",\"code\":\"[摸头]\"},{\"rid\":\"1547\",\"groupname\":\"管不着\",\"code\":\"[真美好]\"},{\"rid\":\"1548\",\"groupname\":\"管不着\",\"code\":\"[脸红自爆]\"},{\"rid\":\"1549\",\"groupname\":\"管不着\",\"code\":\"[哭泣女]\"},{\"rid\":\"1550\",\"groupname\":\"管不着\",\"code\":\"[哭泣男]\"},{\"rid\":\"1551\",\"groupname\":\"管不着\",\"code\":\"[空]\"},{\"rid\":\"1552\",\"groupname\":\"管不着\",\"code\":\"[尽情玩]\"},{\"rid\":\"1553\",\"groupname\":\"管不着\",\"code\":\"[惊喜]\"},{\"rid\":\"1554\",\"groupname\":\"管不着\",\"code\":\"[惊呆]\"},{\"rid\":\"1555\",\"groupname\":\"管不着\",\"code\":\"[胡萝卜]\"},{\"rid\":\"1556\",\"groupname\":\"管不着\",\"code\":\"[欢腾去爱]\"},{\"rid\":\"1557\",\"groupname\":\"管不着\",\"code\":\"[感冒了]\"},{\"rid\":\"1558\",\"groupname\":\"管不着\",\"code\":\"[怒了]\"},{\"rid\":\"1559\",\"groupname\":\"管不着\",\"code\":\"[我要奋斗]\"},{\"rid\":\"1560\",\"groupname\":\"管不着\",\"code\":\"[发芽]\"},{\"rid\":\"1561\",\"groupname\":\"管不着\",\"code\":\"[春暖花开]\"},{\"rid\":\"1562\",\"groupname\":\"管不着\",\"code\":\"[抽烟]\"},{\"rid\":\"1563\",\"groupname\":\"管不着\",\"code\":\"[昂]\"},{\"rid\":\"1564\",\"groupname\":\"管不着\",\"code\":\"[啊]\"},{\"rid\":\"1565\",\"groupname\":\"管不着\",\"code\":\"[自插双目]\"},{\"rid\":\"1566\",\"groupname\":\"管不着\",\"code\":\"[咦]\"},{\"rid\":\"1567\",\"groupname\":\"管不着\",\"code\":\"[嘘嘘]\"},{\"rid\":\"1568\",\"groupname\":\"管不着\",\"code\":\"[我吃]\"},{\"rid\":\"1569\",\"groupname\":\"管不着\",\"code\":\"[喵呜]\"},{\"rid\":\"1570\",\"groupname\":\"管不着\",\"code\":\"[v5]\"},{\"rid\":\"1571\",\"groupname\":\"管不着\",\"code\":\"[调戏]\"},{\"rid\":\"1572\",\"groupname\":\"管不着\",\"code\":\"[打牙]\"},{\"rid\":\"1573\",\"groupname\":\"管不着\",\"code\":\"[手贱]\"},{\"rid\":\"1574\",\"groupname\":\"管不着\",\"code\":\"[色]\"},{\"rid\":\"1575\",\"groupname\":\"管不着\",\"code\":\"[喷]\"},{\"rid\":\"1576\",\"groupname\":\"管不着\",\"code\":\"[你懂的]\"},{\"rid\":\"1577\",\"groupname\":\"管不着\",\"code\":\"[喵]\"},{\"rid\":\"1578\",\"groupname\":\"管不着\",\"code\":\"[美味]\"},{\"rid\":\"1579\",\"groupname\":\"管不着\",\"code\":\"[惊恐]\"},{\"rid\":\"1580\",\"groupname\":\"管不着\",\"code\":\"[感动]\"},{\"rid\":\"1581\",\"groupname\":\"管不着\",\"code\":\"[放开]\"},{\"rid\":\"1582\",\"groupname\":\"管不着\",\"code\":\"[痴呆]\"},{\"rid\":\"1583\",\"groupname\":\"管不着\",\"code\":\"[扯脸]\"},{\"rid\":\"1584\",\"groupname\":\"管不着\",\"code\":\"[不知所措]\"},{\"rid\":\"1585\",\"groupname\":\"管不着\",\"code\":\"[白眼]\"},{\"rid\":\"1586\",\"groupname\":\"臭臭\",\"code\":\"[cc疯掉]\"},{\"rid\":\"1587\",\"groupname\":\"臭臭\",\"code\":\"[cc吃货]\"},{\"rid\":\"1588\",\"groupname\":\"臭臭\",\"code\":\"[cc疑问]\"},{\"rid\":\"1589\",\"groupname\":\"臭臭\",\"code\":\"[cc老爷]\"},{\"rid\":\"1590\",\"groupname\":\"臭臭\",\"code\":\"[cc开心]\"},{\"rid\":\"1591\",\"groupname\":\"臭臭\",\"code\":\"[cc怕怕]\"},{\"rid\":\"1592\",\"groupname\":\"臭臭\",\"code\":\"[cc哎呦喂]\"},{\"rid\":\"1593\",\"groupname\":\"臭臭\",\"code\":\"[cc鼻血]\"},{\"rid\":\"1594\",\"groupname\":\"臭臭\",\"code\":\"[cc没有]\"},{\"rid\":\"1595\",\"groupname\":\"臭臭\",\"code\":\"[cc晕菜]\"},{\"rid\":\"1596\",\"groupname\":\"臭臭\",\"code\":\"[cc媚眼]\"},{\"rid\":\"1597\",\"groupname\":\"臭臭\",\"code\":\"[cc鄙视]\"},{\"rid\":\"1598\",\"groupname\":\"臭臭\",\"code\":\"[cc委屈]\"},{\"rid\":\"1599\",\"groupname\":\"臭臭\",\"code\":\"[cc革命]\"},{\"rid\":\"1600\",\"groupname\":\"臭臭\",\"code\":\"[cc撞墙]\"},{\"rid\":\"1601\",\"groupname\":\"臭臭\",\"code\":\"[cc穿越]\"},{\"rid\":\"1602\",\"groupname\":\"臭臭\",\"code\":\"[cc嘿嘿]\"},{\"rid\":\"1603\",\"groupname\":\"臭臭\",\"code\":\"[cc不行]\"},{\"rid\":\"1604\",\"groupname\":\"臭臭\",\"code\":\"[cc大哭]\"},{\"rid\":\"1605\",\"groupname\":\"臭臭\",\"code\":\"[cc耍赖]\"},{\"rid\":\"1606\",\"groupname\":\"臭臭\",\"code\":\"[cc激动]\"},{\"rid\":\"1607\",\"groupname\":\"臭臭\",\"code\":\"[cc哭泣]\"},{\"rid\":\"1608\",\"groupname\":\"臭臭\",\"code\":\"[cc亲亲]\"},{\"rid\":\"1609\",\"groupname\":\"臭臭\",\"code\":\"[cc心虚]\"},{\"rid\":\"1610\",\"groupname\":\"臭臭\",\"code\":\"[cc舞动]\"},{\"rid\":\"1611\",\"groupname\":\"臭臭\",\"code\":\"[cc数钱]\"},{\"rid\":\"1612\",\"groupname\":\"臭臭\",\"code\":\"[cc抱抱]\"},{\"rid\":\"1613\",\"groupname\":\"臭臭\",\"code\":\"[cc睡觉]\"},{\"rid\":\"1614\",\"groupname\":\"臭臭\",\"code\":\"[cc僵尸]\"},{\"rid\":\"1615\",\"groupname\":\"臭臭\",\"code\":\"[cc我踩]\"},{\"rid\":\"1616\",\"groupname\":\"臭臭\",\"code\":\"[cc运动]\"},{\"rid\":\"1617\",\"groupname\":\"臭臭\",\"code\":\"[cc恭喜]\"},{\"rid\":\"1618\",\"groupname\":\"臭臭\",\"code\":\"[cc歌唱]\"},{\"rid\":\"1619\",\"groupname\":\"臭臭\",\"code\":\"[cc无语]\"},{\"rid\":\"1620\",\"groupname\":\"臭臭\",\"code\":\"[cc郁闷]\"},{\"rid\":\"1621\",\"groupname\":\"臭臭\",\"code\":\"[cc祈祷]\"},{\"rid\":\"1622\",\"groupname\":\"臭臭\",\"code\":\"[cc思考]\"},{\"rid\":\"1623\",\"groupname\":\"臭臭\",\"code\":\"[cc惊讶]\"},{\"rid\":\"1624\",\"groupname\":\"臭臭\",\"code\":\"[cc得瑟]\"},{\"rid\":\"1625\",\"groupname\":\"臭臭\",\"code\":\"[cc不嘛]\"},{\"rid\":\"1626\",\"groupname\":\"臭臭\",\"code\":\"[cc生气]\"},{\"rid\":\"1627\",\"groupname\":\"臭臭\",\"code\":\"[cc乞讨]\"},{\"rid\":\"1628\",\"groupname\":\"臭臭\",\"code\":\"[cc呼啦]\"},{\"rid\":\"1629\",\"groupname\":\"臭臭\",\"code\":\"[cc偷乐]\"},{\"rid\":\"1630\",\"groupname\":\"臭臭\",\"code\":\"[cc无奈]\"},{\"rid\":\"1631\",\"groupname\":\"臭臭\",\"code\":\"[cc蒙面]\"},{\"rid\":\"1632\",\"groupname\":\"臭臭\",\"code\":\"[cc色色]\"},{\"rid\":\"1633\",\"groupname\":\"臭臭\",\"code\":\"[cc哈哈]\"},{\"rid\":\"1634\",\"groupname\":\"nonopanda\",\"code\":\"[nono生日快乐]\"},{\"rid\":\"1635\",\"groupname\":\"nonopanda\",\"code\":\"[nono得瑟]\"},{\"rid\":\"1636\",\"groupname\":\"nonopanda\",\"code\":\"[nono卖帅]\"},{\"rid\":\"1637\",\"groupname\":\"nonopanda\",\"code\":\"[nono摇手指]\"},{\"rid\":\"1638\",\"groupname\":\"nonopanda\",\"code\":\"[nono来呀来呀]\"},{\"rid\":\"1639\",\"groupname\":\"nonopanda\",\"code\":\"[nono哭]\"},{\"rid\":\"1640\",\"groupname\":\"nonopanda\",\"code\":\"[nono挑逗]\"},{\"rid\":\"1641\",\"groupname\":\"nonopanda\",\"code\":\"[nono娇羞]\"},{\"rid\":\"1642\",\"groupname\":\"nonopanda\",\"code\":\"[nono生病]\"},{\"rid\":\"1643\",\"groupname\":\"nonopanda\",\"code\":\"[nono开心]\"},{\"rid\":\"1644\",\"groupname\":\"nonopanda\",\"code\":\"[nono看不见我]\"},{\"rid\":\"1645\",\"groupname\":\"nonopanda\",\"code\":\"[nono眨眼]\"},{\"rid\":\"1646\",\"groupname\":\"nonopanda\",\"code\":\"[nono大礼包]\"},{\"rid\":\"1647\",\"groupname\":\"nonopanda\",\"code\":\"[nono水汪汪]\"},{\"rid\":\"1648\",\"groupname\":\"nonopanda\",\"code\":\"[nonokiss]\"},{\"rid\":\"1649\",\"groupname\":\"nonopanda\",\"code\":\"[nono圣诞节]\"},{\"rid\":\"1650\",\"groupname\":\"nonopanda\",\"code\":\"[nono跳舞]\"},{\"rid\":\"1651\",\"groupname\":\"nonopanda\",\"code\":\"[nono害羞]\"},{\"rid\":\"1652\",\"groupname\":\"nonopanda\",\"code\":\"[nono无语]\"},{\"rid\":\"1653\",\"groupname\":\"nonopanda\",\"code\":\"[nono放屁]\"},{\"rid\":\"1654\",\"groupname\":\"nonopanda\",\"code\":\"[nono晕]\"},{\"rid\":\"1655\",\"groupname\":\"nonopanda\",\"code\":\"[nono悠哉跑]\"},{\"rid\":\"1656\",\"groupname\":\"nonopanda\",\"code\":\"[nono打哈欠]\"},{\"rid\":\"1657\",\"groupname\":\"nonopanda\",\"code\":\"[nono扭]\"},{\"rid\":\"1658\",\"groupname\":\"nonopanda\",\"code\":\"[nonomua]\"},{\"rid\":\"1659\",\"groupname\":\"nonopanda\",\"code\":\"[nono尴尬]\"},{\"rid\":\"1660\",\"groupname\":\"nonopanda\",\"code\":\"[nono跑步]\"},{\"rid\":\"1661\",\"groupname\":\"nonopanda\",\"code\":\"[nono转圈圈]\"},{\"rid\":\"1662\",\"groupname\":\"nonopanda\",\"code\":\"[nono心心眼]\"},{\"rid\":\"1663\",\"groupname\":\"nonopanda\",\"code\":\"[nono睡觉]\"},{\"rid\":\"1664\",\"groupname\":\"nonopanda\",\"code\":\"[nono星星眼]\"},{\"rid\":\"1665\",\"groupname\":\"nonopanda\",\"code\":\"[nono抛小球]\"},{\"rid\":\"1666\",\"groupname\":\"nonopanda\",\"code\":\"[nono拜年]\"},{\"rid\":\"1667\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino求人]\"},{\"rid\":\"1668\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino泪奔]\"},{\"rid\":\"1669\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino害羞]\"},{\"rid\":\"1670\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino等人]\"},{\"rid\":\"1671\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino囧]\"},{\"rid\":\"1672\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino抠鼻]\"},{\"rid\":\"1673\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino心碎]\"},{\"rid\":\"1674\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino撒花]\"},{\"rid\":\"1675\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino电筒]\"},{\"rid\":\"1676\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino热]\"},{\"rid\":\"1677\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino坏笑]\"},{\"rid\":\"1678\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino礼物]\"},{\"rid\":\"1679\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino晕倒]\"},{\"rid\":\"1680\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino诡异]\"},{\"rid\":\"1681\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino瞌睡]\"},{\"rid\":\"1682\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino安慰]\"},{\"rid\":\"1683\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino再见]\"},{\"rid\":\"1684\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino甜筒]\"},{\"rid\":\"1685\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino不屑]\"},{\"rid\":\"1686\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino早安]\"},{\"rid\":\"1687\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino高兴]\"},{\"rid\":\"1688\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino投降]\"},{\"rid\":\"1689\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino鬼脸]\"},{\"rid\":\"1690\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino吃饭]\"},{\"rid\":\"1691\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino失望]\"},{\"rid\":\"1692\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino数钱]\"},{\"rid\":\"1693\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino打你]\"},{\"rid\":\"1694\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino狂叫]\"},{\"rid\":\"1695\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino吐血]\"},{\"rid\":\"1696\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino委屈]\"},{\"rid\":\"1697\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino划圈]\"},{\"rid\":\"1698\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino发怒]\"},{\"rid\":\"1699\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino吃惊]\"},{\"rid\":\"1700\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino喝酒]\"},{\"rid\":\"1701\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino咬手帕]\"},{\"rid\":\"1702\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino臭美]\"},{\"rid\":\"1703\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino困惑]\"},{\"rid\":\"1704\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino许愿]\"},{\"rid\":\"1705\",\"groupname\":\"恐龙宝贝\",\"code\":\"[dino打滚]\"},{\"rid\":\"1706\",\"groupname\":\"影子\",\"code\":\"[yz我倒]\"},{\"rid\":\"1707\",\"groupname\":\"影子\",\"code\":\"[yz撞玻璃]\"},{\"rid\":\"1708\",\"groupname\":\"影子\",\"code\":\"[yz淋浴]\"},{\"rid\":\"1709\",\"groupname\":\"影子\",\"code\":\"[yz纳尼]\"},{\"rid\":\"1710\",\"groupname\":\"影子\",\"code\":\"[yz欢呼]\"},{\"rid\":\"1711\",\"groupname\":\"影子\",\"code\":\"[yz拍桌子]\"},{\"rid\":\"1712\",\"groupname\":\"影子\",\"code\":\"[yz光棍]\"},{\"rid\":\"1713\",\"groupname\":\"影子\",\"code\":\"[yz哇哇叫]\"},{\"rid\":\"1714\",\"groupname\":\"影子\",\"code\":\"[yz求你了]\"},{\"rid\":\"1715\",\"groupname\":\"影子\",\"code\":\"[yz翻滚]\"},{\"rid\":\"1716\",\"groupname\":\"影子\",\"code\":\"[yz偷着笑]\"},{\"rid\":\"1717\",\"groupname\":\"影子\",\"code\":\"[yzye]\"},{\"rid\":\"1718\",\"groupname\":\"影子\",\"code\":\"[yz投降]\"},{\"rid\":\"1719\",\"groupname\":\"影子\",\"code\":\"[yz抽风]\"},{\"rid\":\"1720\",\"groupname\":\"影子\",\"code\":\"[yzoye]\"},{\"rid\":\"1721\",\"groupname\":\"影子\",\"code\":\"[yz撒花]\"},{\"rid\":\"1722\",\"groupname\":\"影子\",\"code\":\"[yz抱枕头]\"},{\"rid\":\"1723\",\"groupname\":\"影子\",\"code\":\"[yz甩手绢]\"},{\"rid\":\"1724\",\"groupname\":\"影子\",\"code\":\"[yz右边亮了]\"},{\"rid\":\"1725\",\"groupname\":\"影子\",\"code\":\"[yz人呢]\"},{\"rid\":\"1726\",\"groupname\":\"影子\",\"code\":\"[yz傻兮兮]\"},{\"rid\":\"1727\",\"groupname\":\"影子\",\"code\":\"[yz砸]\"},{\"rid\":\"1728\",\"groupname\":\"影子\",\"code\":\"[yz招财猫]\"},{\"rid\":\"1729\",\"groupname\":\"影子\",\"code\":\"[yz扇扇子]\"},{\"rid\":\"1730\",\"groupname\":\"影子\",\"code\":\"[yz不呢]\"},{\"rid\":\"1731\",\"groupname\":\"影子\",\"code\":\"[yz拍屁股]\"},{\"rid\":\"1732\",\"groupname\":\"影子\",\"code\":\"[yz委屈哭]\"},{\"rid\":\"1733\",\"groupname\":\"影子\",\"code\":\"[yz听歌]\"},{\"rid\":\"1734\",\"groupname\":\"影子\",\"code\":\"[yz吃瓜]\"},{\"rid\":\"1735\",\"groupname\":\"影子\",\"code\":\"[yz好哇]\"},{\"rid\":\"1736\",\"groupname\":\"影子\",\"code\":\"[yz来看看]\"},{\"rid\":\"1737\",\"groupname\":\"影子\",\"code\":\"[yz焦糖舞]\"},{\"rid\":\"1738\",\"groupname\":\"影子\",\"code\":\"[yz放屁]\"},{\"rid\":\"1739\",\"groupname\":\"影子\",\"code\":\"[yz吃苹果]\"},{\"rid\":\"1740\",\"groupname\":\"影子\",\"code\":\"[yz太好了]\"},{\"rid\":\"1741\",\"groupname\":\"影子\",\"code\":\"[yz好紧张]\"},{\"rid\":\"1742\",\"groupname\":\"大耳兔\",\"code\":\"[猥琐]\"},{\"rid\":\"1743\",\"groupname\":\"大耳兔\",\"code\":\"[挑眉]\"},{\"rid\":\"1744\",\"groupname\":\"大耳兔\",\"code\":\"[挑逗]\"},{\"rid\":\"1745\",\"groupname\":\"大耳兔\",\"code\":\"[亲耳朵]\"},{\"rid\":\"1746\",\"groupname\":\"大耳兔\",\"code\":\"[媚眼]\"},{\"rid\":\"1747\",\"groupname\":\"大耳兔\",\"code\":\"[冒个泡]\"},{\"rid\":\"1748\",\"groupname\":\"大耳兔\",\"code\":\"[囧耳朵]\"},{\"rid\":\"1749\",\"groupname\":\"大耳兔\",\"code\":\"[鬼脸]\"},{\"rid\":\"1750\",\"groupname\":\"大耳兔\",\"code\":\"[放电]\"},{\"rid\":\"1751\",\"groupname\":\"大耳兔\",\"code\":\"[悲剧]\"},{\"rid\":\"1752\",\"groupname\":\"哈皮兔\",\"code\":\"[抚摸]\"},{\"rid\":\"1753\",\"groupname\":\"哈皮兔\",\"code\":\"[大汗]\"},{\"rid\":\"1754\",\"groupname\":\"哈皮兔\",\"code\":\"[大惊]\"},{\"rid\":\"1755\",\"groupname\":\"哈皮兔\",\"code\":\"[惊哭]\"},{\"rid\":\"1756\",\"groupname\":\"哈皮兔\",\"code\":\"[星星眼]\"},{\"rid\":\"1757\",\"groupname\":\"哈皮兔\",\"code\":\"[好困]\"},{\"rid\":\"1758\",\"groupname\":\"哈皮兔\",\"code\":\"[呕吐]\"},{\"rid\":\"1759\",\"groupname\":\"哈皮兔\",\"code\":\"[加我一个]\"},{\"rid\":\"1760\",\"groupname\":\"哈皮兔\",\"code\":\"[痞痞兔耶]\"},{\"rid\":\"1761\",\"groupname\":\"哈皮兔\",\"code\":\"[mua]\"},{\"rid\":\"1762\",\"groupname\":\"哈皮兔\",\"code\":\"[面抽]\"},{\"rid\":\"1763\",\"groupname\":\"哈皮兔\",\"code\":\"[大笑]\"},{\"rid\":\"1764\",\"groupname\":\"哈皮兔\",\"code\":\"[揉]\"},{\"rid\":\"1765\",\"groupname\":\"哈皮兔\",\"code\":\"[痞痞兔囧]\"},{\"rid\":\"1766\",\"groupname\":\"哈皮兔\",\"code\":\"[哈尼兔耶]\"},{\"rid\":\"1767\",\"groupname\":\"哈皮兔\",\"code\":\"[开心]\"},{\"rid\":\"1768\",\"groupname\":\"哈皮兔\",\"code\":\"[咬手帕]\"},{\"rid\":\"1769\",\"groupname\":\"哈皮兔\",\"code\":\"[去]\"},{\"rid\":\"1770\",\"groupname\":\"哈皮兔\",\"code\":\"[晕死了]\"},{\"rid\":\"1771\",\"groupname\":\"哈皮兔\",\"code\":\"[大哭]\"},{\"rid\":\"1772\",\"groupname\":\"哈皮兔\",\"code\":\"[扇子遮面]\"},{\"rid\":\"1773\",\"groupname\":\"哈皮兔\",\"code\":\"[怒气]\"},{\"rid\":\"1774\",\"groupname\":\"哈皮兔\",\"code\":\"[886]\"},{\"rid\":\"1775\",\"groupname\":\"星座\",\"code\":\"[白羊]\"},{\"rid\":\"1776\",\"groupname\":\"星座\",\"code\":\"[射手]\"},{\"rid\":\"1777\",\"groupname\":\"星座\",\"code\":\"[双鱼]\"},{\"rid\":\"1778\",\"groupname\":\"星座\",\"code\":\"[双子]\"},{\"rid\":\"1779\",\"groupname\":\"星座\",\"code\":\"[天秤]\"},{\"rid\":\"1780\",\"groupname\":\"星座\",\"code\":\"[天蝎]\"},{\"rid\":\"1781\",\"groupname\":\"星座\",\"code\":\"[水瓶]\"},{\"rid\":\"1782\",\"groupname\":\"星座\",\"code\":\"[处女]\"},{\"rid\":\"1783\",\"groupname\":\"星座\",\"code\":\"[金牛]\"},{\"rid\":\"1784\",\"groupname\":\"星座\",\"code\":\"[巨蟹]\"},{\"rid\":\"1785\",\"groupname\":\"星座\",\"code\":\"[狮子]\"},{\"rid\":\"1786\",\"groupname\":\"星座\",\"code\":\"[摩羯]\"},{\"rid\":\"1787\",\"groupname\":\"星座\",\"code\":\"[天蝎座]\"},{\"rid\":\"1788\",\"groupname\":\"星座\",\"code\":\"[天秤座]\"},{\"rid\":\"1789\",\"groupname\":\"星座\",\"code\":\"[双子座]\"},{\"rid\":\"1790\",\"groupname\":\"星座\",\"code\":\"[双鱼座]\"},{\"rid\":\"1791\",\"groupname\":\"星座\",\"code\":\"[射手座]\"},{\"rid\":\"1792\",\"groupname\":\"星座\",\"code\":\"[水瓶座]\"},{\"rid\":\"1793\",\"groupname\":\"星座\",\"code\":\"[摩羯座]\"},{\"rid\":\"1794\",\"groupname\":\"星座\",\"code\":\"[狮子座]\"},{\"rid\":\"1795\",\"groupname\":\"星座\",\"code\":\"[巨蟹座]\"},{\"rid\":\"1796\",\"groupname\":\"星座\",\"code\":\"[金牛座]\"},{\"rid\":\"1797\",\"groupname\":\"星座\",\"code\":\"[处女座]\"},{\"rid\":\"1798\",\"groupname\":\"星座\",\"code\":\"[白羊座]\"},{\"rid\":\"1799\",\"groupname\":\"爱心\",\"code\":\"[爱心传递]\"},{\"rid\":\"1800\",\"groupname\":\"爱心\",\"code\":\"[绿丝带]\"},{\"rid\":\"1801\",\"groupname\":\"爱心\",\"code\":\"[粉红丝带]\"},{\"rid\":\"1802\",\"groupname\":\"爱心\",\"code\":\"[红丝带]\"},{\"rid\":\"1803\",\"groupname\":\"亚运会\",\"code\":\"[加油]\"},{\"rid\":\"1804\",\"groupname\":\"亚运会\",\"code\":\"[国旗]\"},{\"rid\":\"1805\",\"groupname\":\"亚运会\",\"code\":\"[金牌]\"},{\"rid\":\"1806\",\"groupname\":\"亚运会\",\"code\":\"[银牌]\"},{\"rid\":\"1807\",\"groupname\":\"亚运会\",\"code\":\"[铜牌]\"},{\"rid\":\"1808\",\"groupname\":\"亚运会\",\"code\":\"[哨子]\"},{\"rid\":\"1809\",\"groupname\":\"亚运会\",\"code\":\"[黄牌]\"},{\"rid\":\"1810\",\"groupname\":\"亚运会\",\"code\":\"[红牌]\"},{\"rid\":\"1811\",\"groupname\":\"亚运会\",\"code\":\"[足球]\"},{\"rid\":\"1812\",\"groupname\":\"亚运会\",\"code\":\"[篮球]\"},{\"rid\":\"1813\",\"groupname\":\"亚运会\",\"code\":\"[黑8]\"},{\"rid\":\"1814\",\"groupname\":\"亚运会\",\"code\":\"[排球]\"},{\"rid\":\"1815\",\"groupname\":\"亚运会\",\"code\":\"[游泳]\"},{\"rid\":\"1816\",\"groupname\":\"亚运会\",\"code\":\"[乒乓球]\"},{\"rid\":\"1817\",\"groupname\":\"亚运会\",\"code\":\"[投篮]\"},{\"rid\":\"1818\",\"groupname\":\"亚运会\",\"code\":\"[羽毛球]\"},{\"rid\":\"1819\",\"groupname\":\"亚运会\",\"code\":\"[射门]\"},{\"rid\":\"1820\",\"groupname\":\"亚运会\",\"code\":\"[射箭]\"},{\"rid\":\"1821\",\"groupname\":\"亚运会\",\"code\":\"[举重]\"},{\"rid\":\"1822\",\"groupname\":\"张小盒\",\"code\":\"[zxh得瑟]\"},{\"rid\":\"1823\",\"groupname\":\"张小盒\",\"code\":\"[微微笑]\"},{\"rid\":\"1824\",\"groupname\":\"张小盒\",\"code\":\"[特委屈]\"},{\"rid\":\"1825\",\"groupname\":\"张小盒\",\"code\":\"[我吐]\"},{\"rid\":\"1826\",\"groupname\":\"张小盒\",\"code\":\"[很生气]\"},{\"rid\":\"1827\",\"groupname\":\"张小盒\",\"code\":\"[流鼻涕]\"},{\"rid\":\"1828\",\"groupname\":\"张小盒\",\"code\":\"[默默哭泣]\"},{\"rid\":\"1829\",\"groupname\":\"张小盒\",\"code\":\"[小盒汗]\"},{\"rid\":\"1830\",\"groupname\":\"张小盒\",\"code\":\"[发呆中]\"},{\"rid\":\"1831\",\"groupname\":\"张小盒\",\"code\":\"[不理你]\"},{\"rid\":\"1832\",\"groupname\":\"张小盒\",\"code\":\"[强烈鄙视]\"},{\"rid\":\"1833\",\"groupname\":\"张小盒\",\"code\":\"[烦躁]\"},{\"rid\":\"1834\",\"groupname\":\"张小盒\",\"code\":\"[呲牙]\"},{\"rid\":\"1835\",\"groupname\":\"张小盒\",\"code\":\"[有钱]\"},{\"rid\":\"1836\",\"groupname\":\"张小盒\",\"code\":\"[微笑]\"},{\"rid\":\"1837\",\"groupname\":\"张小盒\",\"code\":\"[帅爆]\"},{\"rid\":\"1838\",\"groupname\":\"张小盒\",\"code\":\"[生气]\"},{\"rid\":\"1839\",\"groupname\":\"张小盒\",\"code\":\"[生病了]\"},{\"rid\":\"1840\",\"groupname\":\"张小盒\",\"code\":\"[色眯眯]\"},{\"rid\":\"1841\",\"groupname\":\"张小盒\",\"code\":\"[疲劳]\"},{\"rid\":\"1842\",\"groupname\":\"张小盒\",\"code\":\"[瞄]\"},{\"rid\":\"1843\",\"groupname\":\"张小盒\",\"code\":\"[哭]\"},{\"rid\":\"1844\",\"groupname\":\"张小盒\",\"code\":\"[好可怜]\"},{\"rid\":\"1845\",\"groupname\":\"张小盒\",\"code\":\"[紧张]\"},{\"rid\":\"1846\",\"groupname\":\"张小盒\",\"code\":\"[惊讶]\"},{\"rid\":\"1847\",\"groupname\":\"张小盒\",\"code\":\"[激动]\"},{\"rid\":\"1848\",\"groupname\":\"张小盒\",\"code\":\"[见钱]\"},{\"rid\":\"1849\",\"groupname\":\"张小盒\",\"code\":\"[汗了]\"},{\"rid\":\"1850\",\"groupname\":\"张小盒\",\"code\":\"[奋斗]\"},{\"rid\":\"1851\",\"groupname\":\"悠嘻猴\",\"code\":\"[小人得志]\"},{\"rid\":\"1852\",\"groupname\":\"悠嘻猴\",\"code\":\"[哇哈哈]\"},{\"rid\":\"1853\",\"groupname\":\"悠嘻猴\",\"code\":\"[叹气]\"},{\"rid\":\"1854\",\"groupname\":\"悠嘻猴\",\"code\":\"[冻结]\"},{\"rid\":\"1855\",\"groupname\":\"悠嘻猴\",\"code\":\"[切]\"},{\"rid\":\"1856\",\"groupname\":\"悠嘻猴\",\"code\":\"[拍照]\"},{\"rid\":\"1857\",\"groupname\":\"悠嘻猴\",\"code\":\"[怕怕]\"},{\"rid\":\"1858\",\"groupname\":\"悠嘻猴\",\"code\":\"[怒吼]\"},{\"rid\":\"1859\",\"groupname\":\"悠嘻猴\",\"code\":\"[膜拜]\"},{\"rid\":\"1860\",\"groupname\":\"悠嘻猴\",\"code\":\"[路过]\"},{\"rid\":\"1861\",\"groupname\":\"悠嘻猴\",\"code\":\"[泪奔]\"},{\"rid\":\"1862\",\"groupname\":\"悠嘻猴\",\"code\":\"[脸变色]\"},{\"rid\":\"1863\",\"groupname\":\"悠嘻猴\",\"code\":\"[亲]\"},{\"rid\":\"1864\",\"groupname\":\"悠嘻猴\",\"code\":\"[恐怖]\"},{\"rid\":\"1865\",\"groupname\":\"悠嘻猴\",\"code\":\"[交给我吧]\"},{\"rid\":\"1866\",\"groupname\":\"悠嘻猴\",\"code\":\"[欢欣鼓舞]\"},{\"rid\":\"1867\",\"groupname\":\"悠嘻猴\",\"code\":\"[高兴]\"},{\"rid\":\"1868\",\"groupname\":\"悠嘻猴\",\"code\":\"[尴尬]\"},{\"rid\":\"1869\",\"groupname\":\"悠嘻猴\",\"code\":\"[发嗲]\"},{\"rid\":\"1870\",\"groupname\":\"悠嘻猴\",\"code\":\"[犯错]\"},{\"rid\":\"1871\",\"groupname\":\"悠嘻猴\",\"code\":\"[得意]\"},{\"rid\":\"1872\",\"groupname\":\"悠嘻猴\",\"code\":\"[吵闹]\"},{\"rid\":\"1873\",\"groupname\":\"悠嘻猴\",\"code\":\"[冲锋]\"},{\"rid\":\"1874\",\"groupname\":\"悠嘻猴\",\"code\":\"[抽耳光]\"},{\"rid\":\"1875\",\"groupname\":\"悠嘻猴\",\"code\":\"[差得远呢]\"},{\"rid\":\"1876\",\"groupname\":\"悠嘻猴\",\"code\":\"[被砸]\"},{\"rid\":\"1877\",\"groupname\":\"悠嘻猴\",\"code\":\"[拜托]\"},{\"rid\":\"1878\",\"groupname\":\"悠嘻猴\",\"code\":\"[必胜]\"},{\"rid\":\"1879\",\"groupname\":\"悠嘻猴\",\"code\":\"[不关我事]\"},{\"rid\":\"1880\",\"groupname\":\"悠嘻猴\",\"code\":\"[上火]\"},{\"rid\":\"1881\",\"groupname\":\"悠嘻猴\",\"code\":\"[不倒翁]\"},{\"rid\":\"1882\",\"groupname\":\"悠嘻猴\",\"code\":\"[不错哦]\"},{\"rid\":\"1883\",\"groupname\":\"小新小浪\",\"code\":\"[yeah]\"},{\"rid\":\"1884\",\"groupname\":\"小新小浪\",\"code\":\"[喜欢]\"},{\"rid\":\"1885\",\"groupname\":\"小新小浪\",\"code\":\"[心动]\"},{\"rid\":\"1886\",\"groupname\":\"小新小浪\",\"code\":\"[无聊]\"},{\"rid\":\"1887\",\"groupname\":\"小新小浪\",\"code\":\"[手舞足蹈]\"},{\"rid\":\"1888\",\"groupname\":\"小新小浪\",\"code\":\"[搞笑]\"},{\"rid\":\"1889\",\"groupname\":\"小新小浪\",\"code\":\"[痛哭]\"},{\"rid\":\"1890\",\"groupname\":\"小新小浪\",\"code\":\"[爆发]\"},{\"rid\":\"1891\",\"groupname\":\"小新小浪\",\"code\":\"[发奋]\"},{\"rid\":\"1892\",\"groupname\":\"小新小浪\",\"code\":\"[不屑]\"},{\"rid\":\"1893\",\"groupname\":\"大熊\",\"code\":\"[dx拜年]\"},{\"rid\":\"1894\",\"groupname\":\"大熊\",\"code\":\"[dx炸弹]\"},{\"rid\":\"1895\",\"groupname\":\"大熊\",\"code\":\"[dx洗澡]\"},{\"rid\":\"1896\",\"groupname\":\"大熊\",\"code\":\"[dx握爪]\"},{\"rid\":\"1897\",\"groupname\":\"大熊\",\"code\":\"[dx数落]\"},{\"rid\":\"1898\",\"groupname\":\"大熊\",\"code\":\"[dx刷牙]\"},{\"rid\":\"1899\",\"groupname\":\"大熊\",\"code\":\"[dx傻]\"},{\"rid\":\"1900\",\"groupname\":\"大熊\",\"code\":\"[dx晒]\"},{\"rid\":\"1901\",\"groupname\":\"大熊\",\"code\":\"[dx抛媚眼]\"},{\"rid\":\"1902\",\"groupname\":\"大熊\",\"code\":\"[dx拍拍手]\"},{\"rid\":\"1903\",\"groupname\":\"大熊\",\"code\":\"[dx耶]\"},{\"rid\":\"1904\",\"groupname\":\"大熊\",\"code\":\"[dx扭]\"},{\"rid\":\"1905\",\"groupname\":\"大熊\",\"code\":\"[dx没有]\"},{\"rid\":\"1906\",\"groupname\":\"大熊\",\"code\":\"[dx卖萌]\"},{\"rid\":\"1907\",\"groupname\":\"大熊\",\"code\":\"[dx脸红]\"},{\"rid\":\"1908\",\"groupname\":\"大熊\",\"code\":\"[dx泪奔]\"},{\"rid\":\"1909\",\"groupname\":\"大熊\",\"code\":\"[dx加油]\"},{\"rid\":\"1910\",\"groupname\":\"大熊\",\"code\":\"[dx脚踏车]\"},{\"rid\":\"1911\",\"groupname\":\"大熊\",\"code\":\"[dx花心]\"},{\"rid\":\"1912\",\"groupname\":\"大熊\",\"code\":\"[dx欢乐]\"},{\"rid\":\"1913\",\"groupname\":\"大熊\",\"code\":\"[dx滑板]\"},{\"rid\":\"1914\",\"groupname\":\"大熊\",\"code\":\"[dx倒]\"},{\"rid\":\"1915\",\"groupname\":\"大熊\",\"code\":\"[dx超人]\"},{\"rid\":\"1916\",\"groupname\":\"大熊\",\"code\":\"[dx饱]\"},{\"rid\":\"1917\",\"groupname\":\"大熊\",\"code\":\"[dx哎]\"},{\"rid\":\"1918\",\"groupname\":\"蘑菇点点\",\"code\":\"[眨眨眼]\"},{\"rid\":\"1919\",\"groupname\":\"蘑菇点点\",\"code\":\"[杂技]\"},{\"rid\":\"1920\",\"groupname\":\"蘑菇点点\",\"code\":\"[多问号]\"},{\"rid\":\"1921\",\"groupname\":\"蘑菇点点\",\"code\":\"[跳绳]\"},{\"rid\":\"1922\",\"groupname\":\"蘑菇点点\",\"code\":\"[强吻]\"},{\"rid\":\"1923\",\"groupname\":\"蘑菇点点\",\"code\":\"[不活了]\"},{\"rid\":\"1924\",\"groupname\":\"蘑菇点点\",\"code\":\"[磕头]\"},{\"rid\":\"1925\",\"groupname\":\"蘑菇点点\",\"code\":\"[呜呜]\"},{\"rid\":\"1926\",\"groupname\":\"蘑菇点点\",\"code\":\"[不]\"}]";
		//dbHandler.emptyBqlib();
		//dbHandler.addToBqlib(bqString);
		//dbHandler.updateBqlib(bqString);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saloon, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings:
			getWeiboMessage();
			//Toast.makeText(Saloon.this, "test...", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		String jsonStr = null;
		switch (position) {
		case 0:
			jsonStr = dbHandler.querySaloonDB("saloon");
			initList(jsonStr);
			break;
		case 1:
			jsonStr = dbHandler.querySaloonDB("saloon_s");
			initList(jsonStr);
			break;

		default:
			weiboList.setAdapter(null);
			break;
		}
		
		return true;
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
			final EditText repostView = new EditText(Saloon.this);
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
			new AlertDialog.Builder(Saloon.this).setTitle("说点什么吧，爷~").setView(repostView)
			//.setMultiChoiceItems(new String[] {"评论给当前微博", "评论给原微博"}, null, null)
			.setPositiveButton("确定", repostListener)
			.setNegativeButton("编辑", null)
			.show();
			/*
			Map<String, String> params = new HashMap<String, String>();
			params.put("url", "https://api.weibo.com/2/statuses/repost.json");
			params.put("source", ConstantS.APP_KEY);
			params.put("access_token", MainActivity.accessToken.getToken());
			params.put("id", id_to_repost);
			DelWeiboTask delTask = new DelWeiboTask();
			delTask.execute(params);
			dbHandler.deleteWeibo(id_to_del);
			String jsonStr = dbHandler.queryDB(currentUid, listItem.size(), null);
			initList(jsonStr);
			*/
			break;

		default:
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
			WeiboAdapter listItemAdapter = new WeiboAdapter(Saloon.this, listItem, R.layout.weibo_showbox_saloon,
					new String[]{"ori_data", "mid"},
					new int[]{R.id.name_show_saloon, R.id.time_show_saloon, R.id.content_show_saloon, R.id.retweeted_show_saloon, R.id.counts_show_saloon});
			
			weiboList.setAdapter(listItemAdapter);
			
			weiboList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
				
				@Override
				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					// TODO Auto-generated method stub
					menu.setHeaderTitle("召唤我作甚吗？");
					menu.add(0, 0, 0, "不干嘛…玩~");
					menu.add(0, 1, 1, "转发？");
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
			WeiboAdapter listItemAdapter = new WeiboAdapter(Saloon.this, listItem, R.layout.weibo_showbox_saloon,
					new String[]{"ori_data", "mid"},
					new int[]{R.id.name_show_saloon, R.id.time_show_saloon, R.id.content_show_saloon, R.id.retweeted_show_saloon, R.id.counts_show_saloon});
			
			weiboList.setAdapter(listItemAdapter);
			weiboList.setSelection(listItem.size()-length-1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getWeiboMessage() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("url", "https://api.weibo.com/2/statuses/friends_timeline.json");
		params.put("source", ConstantS.APP_KEY);
		if(actionBar.getSelectedNavigationIndex() == 0) {
			params.put("access_token", MainActivity.accessToken.getToken());
		}
		else {
			params.put("access_token", ConstantS.SH_token);
		}
		params.put("count", "20");
		if(HttpsUtils.getNetType(this.getSystemService(Context.CONNECTIVITY_SERVICE))==1) {
			if(listItem.size()>0) {
				String sinceId = listItem.get(0).get("mid").toString();
				params.put("since_id", sinceId);
			}
		}
		SendWeioboTask getAsync = new SendWeioboTask(true, null);
		getAsync.execute(params);
	}
	
	public void getWeiboMessage(String max_id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("url", "https://api.weibo.com/2/statuses/friends_timeline.json");
		params.put("source", ConstantS.APP_KEY);
		if(actionBar.getSelectedNavigationIndex() == 0) {
			params.put("access_token", MainActivity.accessToken.getToken());
		}
		else {
			params.put("access_token", ConstantS.SH_token);
		}
		params.put("count", "10");
		params.put("max_id", max_id);
		SendWeioboTask getAsync = new SendWeioboTask(false, max_id);
		getAsync.execute(params);
	}
	
	class WeiboAdapter extends BaseAdapter {
		
		private Context context;
		private ArrayList<HashMap<String, Object>> listItem;
		private int resource;
		private String[] from;
		private int[] to;
		private LayoutInflater listContainer;
		
		public final class ListItemView {
			public TextView name;
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
				
				listItemView.name = (TextView) convertView.findViewById(to[0]);
				listItemView.time = (TextView) convertView.findViewById(to[1]);
				listItemView.content = (TextView) convertView.findViewById(to[2]);
				listItemView.retweeted = (DialogBox) convertView.findViewById(to[3]);
				listItemView.counts = (TextView) convertView.findViewById(to[4]);
				
				convertView.setTag(R.id.tag_layout, listItemView);
			}
			else {
				listItemView = (ListItemView) convertView.getTag(R.id.tag_layout);
			}
			
			try {
				JSONObject ori_json_obj = new JSONObject(listItem.get(position).get(from[0]).toString());
				listItemView.name.setText(ori_json_obj.getJSONObject("user").getString("name"));
				if(ori_json_obj.has("thumbnail_pic")) {
					SpannableString sps = new SpannableString(" ");
					Drawable d = getResources().getDrawable(R.drawable.imageholder);
					d.setBounds(0, 0, 22, 22);
					ImageSpan isp = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
					sps.setSpan(isp, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					listItemView.name.append(" ");
					listItemView.name.append(sps);
				}
				listItemView.time.setText(ori_json_obj.getString("created_at"));
				setWeiboContent(listItemView.content, ori_json_obj.getString("text"));
				if(ori_json_obj.has("retweeted_status")) {
					String retweeted_name = ori_json_obj.getJSONObject("retweeted_status").getJSONObject("user").getString("name");
					if(ori_json_obj.getJSONObject("retweeted_status").has("thumbnail_pic")) {
						retweeted_name += "@";
					}
					String retweeted_status = ori_json_obj.getJSONObject("retweeted_status").getString("text");
					retweeted_status += "\n"+ori_json_obj.getJSONObject("retweeted_status").getString("created_at");
					
					listItemView.retweeted.setContent(Saloon.this, retweeted_name, retweeted_status, false, false);
					listItemView.retweeted.setVisibility(View.VISIBLE);
					
					convertView.setTag(R.id.tag_retweeted, "//@"+ori_json_obj.getJSONObject("user").getString("name")+":"+ori_json_obj.getString("text"));
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
					formatContent(Saloon.this, contentView, preContent);
					loc = start+1;
				}
				else {
					preContent = status.substring(loc, start);
					//contentView.append(preContent);
					formatContent(Saloon.this, contentView, preContent);
					rid += R.drawable.bq_0000;
					contentView.append(Html.fromHtml("<img src=\'"+rid+"\'/>", imgGetter, null));
					loc = end+1;
				}
			}
			if(loc<length) {
				//contentView.append(status.substring(loc));
				formatContent(Saloon.this, contentView, status.substring(loc));
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
	
	class SendWeioboTask extends AsyncTask<Map, Integer, String> {
		
		public boolean isRefresh;
		public String maxId;
		
		public SendWeioboTask(boolean isRefresh, String maxId) {
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
						Toast.makeText(Saloon.this, jsonObj.getString("error"), Toast.LENGTH_SHORT).show();
					}
					else {
						if (jsonObj.getJSONArray("statuses").length()==0) {
							Toast.makeText(Saloon.this, "没有刷到哎...", Toast.LENGTH_SHORT).show();
						}
						else{
							//db op
							Toast.makeText(Saloon.this, "刷出来啦！！！", Toast.LENGTH_SHORT).show();
							String jsonStr;
							if(isRefresh) {
								if(HttpsUtils.getNetType(Saloon.this.getSystemService(Context.CONNECTIVITY_SERVICE))==0) {
									if(actionBar.getSelectedNavigationIndex() == 0) {
										dbHandler.updateSaloonDB("saloon", result);
									}
									else {
										dbHandler.updateSaloonDB("saloon_s", result);
									}
								}
								else {
									if(listItem.size()>50) {
										if(actionBar.getSelectedNavigationIndex() == 0) {
											dbHandler.updateSaloonDB("saloon", result);
										}
										else {
											dbHandler.updateSaloonDB("saloon_s", result);
										}
									}
									else {
										if(actionBar.getSelectedNavigationIndex() == 0) {
											dbHandler.insertSaloonDB("saloon", result);
										}
										else {
											dbHandler.insertSaloonDB("saloon_s", result);
										}
									}
								}
								if(actionBar.getSelectedNavigationIndex() == 0) {
									jsonStr = dbHandler.querySaloonDB("saloon");
								}
								else {
									jsonStr = dbHandler.querySaloonDB("saloon_s");
								}
								initList(jsonStr);
							}
							else {
								if(actionBar.getSelectedNavigationIndex() == 0) {
									dbHandler.insertSaloonDB("saloon", result);
								}
								else {
									dbHandler.insertSaloonDB("saloon_s", result);
								}
								fillList(result);
							}
						}
					}
				}
				else {
					Toast.makeText(Saloon.this, "不知道为什么，就是失败了…", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(Saloon.this, "不知道为什么，就是失败了…", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	class RepostWeiboTask extends AsyncTask<Map, Integer, String> {

		@Override
		protected String doInBackground(Map... params) {
			// TODO Auto-generated method stub
			String responseValue = "";
			try {
				responseValue = HttpsUtils.doPost(Saloon.this, params[0]);
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
						Toast.makeText(Saloon.this, resultJsonObject.getString("error"), Toast.LENGTH_SHORT).show();
					}
					else if(resultJsonObject.has("created_at")) {
						Toast.makeText(Saloon.this, "转发成功！ 哇咔咔~", Toast.LENGTH_SHORT).show();
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
