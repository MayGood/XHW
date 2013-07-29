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
		 * ���±����
		 */
		//String bqString = "[{\"rid\":\"0\",\"groupname\":\"\",\"code\":\"[������]\"},{\"rid\":\"1\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"2\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"3\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"4\",\"groupname\":\"\",\"code\":\"[Χ��]\"},{\"rid\":\"5\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"6\",\"groupname\":\"\",\"code\":\"[��è]\"},{\"rid\":\"7\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"8\",\"groupname\":\"\",\"code\":\"[������]\"},{\"rid\":\"9\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"10\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"11\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"12\",\"groupname\":\"\",\"code\":\"[�Ǻ�]\"},{\"rid\":\"13\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"14\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"15\",\"groupname\":\"\",\"code\":\"[�ɰ�]\"},{\"rid\":\"16\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"17\",\"groupname\":\"\",\"code\":\"[�ڱ�ʺ]\"},{\"rid\":\"18\",\"groupname\":\"\",\"code\":\"[�Ծ�]\"},{\"rid\":\"19\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"20\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"21\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"22\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"23\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"24\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"25\",\"groupname\":\"\",\"code\":\"[͵Ц]\"},{\"rid\":\"26\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"27\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"28\",\"groupname\":\"\",\"code\":\"[̫����]\"},{\"rid\":\"29\",\"groupname\":\"\",\"code\":\"[��������]\"},{\"rid\":\"30\",\"groupname\":\"\",\"code\":\"[�Һߺ�]\"},{\"rid\":\"31\",\"groupname\":\"\",\"code\":\"[��ߺ�]\"},{\"rid\":\"32\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"33\",\"groupname\":\"\",\"code\":\"[˥]\"},{\"rid\":\"34\",\"groupname\":\"\",\"code\":\"[ί��]\"},{\"rid\":\"35\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"36\",\"groupname\":\"\",\"code\":\"[���Ƿ]\"},{\"rid\":\"37\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"38\",\"groupname\":\"\",\"code\":\"[ŭ]\"},{\"rid\":\"39\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"40\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"41\",\"groupname\":\"\",\"code\":\"[�ݰ�]\"},{\"rid\":\"42\",\"groupname\":\"\",\"code\":\"[˼��]\"},{\"rid\":\"43\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"44\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"45\",\"groupname\":\"\",\"code\":\"[˯��]\"},{\"rid\":\"46\",\"groupname\":\"\",\"code\":\"[Ǯ]\"},{\"rid\":\"47\",\"groupname\":\"\",\"code\":\"[ʧ��]\"},{\"rid\":\"48\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"49\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"50\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"51\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"52\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"53\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"54\",\"groupname\":\"\",\"code\":\"[ץ��]\"},{\"rid\":\"55\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"56\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"57\",\"groupname\":\"\",\"code\":\"[ŭ��]\"},{\"rid\":\"58\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"59\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"60\",\"groupname\":\"\",\"code\":\"[��ͷ]\"},{\"rid\":\"61\",\"groupname\":\"\",\"code\":\"[ok]\"},{\"rid\":\"62\",\"groupname\":\"\",\"code\":\"[Ү]\"},{\"rid\":\"63\",\"groupname\":\"\",\"code\":\"[good]\"},{\"rid\":\"64\",\"groupname\":\"\",\"code\":\"[��Ҫ]\"},{\"rid\":\"65\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"66\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"67\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"68\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"69\",\"groupname\":\"\",\"code\":\"[��]\"},{\"rid\":\"70\",\"groupname\":\"\",\"code\":\"[��Ͳ]\"},{\"rid\":\"71\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"72\",\"groupname\":\"\",\"code\":\"[�����Ц]\"},{\"rid\":\"73\",\"groupname\":\"\",\"code\":\"[Ц����]\"},{\"rid\":\"74\",\"groupname\":\"\",\"code\":\"[ת��]\"},{\"rid\":\"75\",\"groupname\":\"\",\"code\":\"[͵��]\"},{\"rid\":\"76\",\"groupname\":\"\",\"code\":\"[bm�ɰ�]\"},{\"rid\":\"77\",\"groupname\":\"\",\"code\":\"[lt�п���]\"},{\"rid\":\"78\",\"groupname\":\"\",\"code\":\"[xklתȦ]\"},{\"rid\":\"79\",\"groupname\":\"\",\"code\":\"[ali��]\"},{\"rid\":\"80\",\"groupname\":\"\",\"code\":\"[ppb����]\"},{\"rid\":\"81\",\"groupname\":\"\",\"code\":\"[din��ײ]\"},{\"rid\":\"82\",\"groupname\":\"\",\"code\":\"[xbѹ��]\"},{\"rid\":\"83\",\"groupname\":\"\",\"code\":\"[��ɪ]\"},{\"rid\":\"84\",\"groupname\":\"\",\"code\":\"[ali�ʹ�]\"},{\"rid\":\"85\",\"groupname\":\"\",\"code\":\"[zxh��ɪ]\"},{\"rid\":\"86\",\"groupname\":\"\",\"code\":\"[ppb���տ���]\"},{\"rid\":\"87\",\"groupname\":\"\",\"code\":\"[nono���տ���]\"},{\"rid\":\"88\",\"groupname\":\"\",\"code\":\"[nono��ɪ]\"},{\"rid\":\"89\",\"groupname\":\"\",\"code\":\"[moc���տ���]\"},{\"rid\":\"90\",\"groupname\":\"\",\"code\":\"[lxhx���տ���]\"},{\"rid\":\"91\",\"groupname\":\"\",\"code\":\"[gst���տ���]\"},{\"rid\":\"92\",\"groupname\":\"\",\"code\":\"[bh�뺷]\"},{\"rid\":\"93\",\"groupname\":\"\",\"code\":\"[�������Ƥ]\"},{\"rid\":\"94\",\"groupname\":\"\",\"code\":\"[�ż���]\"},{\"rid\":\"95\",\"groupname\":\"\",\"code\":\"[��Ը]\"},{\"rid\":\"96\",\"groupname\":\"\",\"code\":\"[offthewall]\"},{\"rid\":\"97\",\"groupname\":\"\",\"code\":\"[ppb���˽�]\"},{\"rid\":\"98\",\"groupname\":\"\",\"code\":\"[����ɽ��]\"},{\"rid\":\"99\",\"groupname\":\"\",\"code\":\"[�����㶫]\"},{\"rid\":\"100\",\"groupname\":\"\",\"code\":\"[BOBO����]\"},{\"rid\":\"101\",\"groupname\":\"\",\"code\":\"[�׷�]\"},{\"rid\":\"102\",\"groupname\":\"\",\"code\":\"[ppb����Բ]\"},{\"rid\":\"103\",\"groupname\":\"\",\"code\":\"[������]\"},{\"rid\":\"104\",\"groupname\":\"\",\"code\":\"[ˣ����]\"},{\"rid\":\"105\",\"groupname\":\"\",\"code\":\"[����Բ]\"},{\"rid\":\"106\",\"groupname\":\"\",\"code\":\"[Ԫ������]\"},{\"rid\":\"107\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"108\",\"groupname\":\"\",\"code\":\"[holdס]\"},{\"rid\":\"109\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"110\",\"groupname\":\"\",\"code\":\"[xyj����]\"},{\"rid\":\"111\",\"groupname\":\"\",\"code\":\"[toto����]\"},{\"rid\":\"112\",\"groupname\":\"\",\"code\":\"[mtjj����]\"},{\"rid\":\"113\",\"groupname\":\"\",\"code\":\"[mk����]\"},{\"rid\":\"114\",\"groupname\":\"\",\"code\":\"[��������]\"},{\"rid\":\"115\",\"groupname\":\"\",\"code\":\"[gst����]\"},{\"rid\":\"116\",\"groupname\":\"\",\"code\":\"[mocת��]\"},{\"rid\":\"117\",\"groupname\":\"\",\"code\":\"[����style]\"},{\"rid\":\"118\",\"groupname\":\"\",\"code\":\"[����]\"},{\"rid\":\"119\",\"groupname\":\"��С��\",\"code\":\"[ת��]\"},{\"rid\":\"120\",\"groupname\":\"��С��\",\"code\":\"[Ц����]\"},{\"rid\":\"121\",\"groupname\":\"��С��\",\"code\":\"[�����Ц]\"},{\"rid\":\"122\",\"groupname\":\"��С��\",\"code\":\"[��Ү]\"},{\"rid\":\"123\",\"groupname\":\"��С��\",\"code\":\"[͵��]\"},{\"rid\":\"124\",\"groupname\":\"��С��\",\"code\":\"[��������]\"},{\"rid\":\"125\",\"groupname\":\"��С��\",\"code\":\"[�޺�]\"},{\"rid\":\"126\",\"groupname\":\"��С��\",\"code\":\"[�ٱ�ʺ]\"},{\"rid\":\"127\",\"groupname\":\"��С��\",\"code\":\"[���ע]\"},{\"rid\":\"128\",\"groupname\":\"��С��\",\"code\":\"[��V5]\"},{\"rid\":\"129\",\"groupname\":\"��С��\",\"code\":\"[Ⱥ��Χ��]\"},{\"rid\":\"130\",\"groupname\":\"��С��\",\"code\":\"[holdס]\"},{\"rid\":\"131\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"132\",\"groupname\":\"��С��\",\"code\":\"[�ǳ���]\"},{\"rid\":\"133\",\"groupname\":\"��С��\",\"code\":\"[��Ը]\"},{\"rid\":\"134\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"135\",\"groupname\":\"��С��\",\"code\":\"[�Ç�]\"},{\"rid\":\"136\",\"groupname\":\"��С��\",\"code\":\"[��]\"},{\"rid\":\"137\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"138\",\"groupname\":\"��С��\",\"code\":\"[������˼]\"},{\"rid\":\"139\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"140\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"141\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"142\",\"groupname\":\"��С��\",\"code\":\"[��ϲ��]\"},{\"rid\":\"143\",\"groupname\":\"��С��\",\"code\":\"[�ð�Ŷ]\"},{\"rid\":\"144\",\"groupname\":\"��С��\",\"code\":\"[·�����]\"},{\"rid\":\"145\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"146\",\"groupname\":\"��С��\",\"code\":\"[�����ϰ�]\"},{\"rid\":\"147\",\"groupname\":\"��С��\",\"code\":\"[���֢]\"},{\"rid\":\"148\",\"groupname\":\"��С��\",\"code\":\"[˦˦��]\"},{\"rid\":\"149\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"150\",\"groupname\":\"��С��\",\"code\":\"[ͬ��]\"},{\"rid\":\"151\",\"groupname\":\"��С��\",\"code\":\"[�ȶ���]\"},{\"rid\":\"152\",\"groupname\":\"��С��\",\"code\":\"[��������]\"},{\"rid\":\"153\",\"groupname\":\"��С��\",\"code\":\"[�ܿ�ѷ]\"},{\"rid\":\"154\",\"groupname\":\"��С��\",\"code\":\"[�׷�]\"},{\"rid\":\"155\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"156\",\"groupname\":\"��С��\",\"code\":\"[��һ��]\"},{\"rid\":\"157\",\"groupname\":\"��С��\",\"code\":\"[�ɸ���]\"},{\"rid\":\"158\",\"groupname\":\"��С��\",\"code\":\"[���Ͱ�]\"},{\"rid\":\"159\",\"groupname\":\"��С��\",\"code\":\"[��Ϧ]\"},{\"rid\":\"160\",\"groupname\":\"��С��\",\"code\":\"[������]\"},{\"rid\":\"161\",\"groupname\":\"��С��\",\"code\":\"[��Ѽ��]\"},{\"rid\":\"162\",\"groupname\":\"��С��\",\"code\":\"[�ұ�����]\"},{\"rid\":\"163\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"164\",\"groupname\":\"��С��\",\"code\":\"[�ð�]\"},{\"rid\":\"165\",\"groupname\":\"��С��\",\"code\":\"[��һ��]\"},{\"rid\":\"166\",\"groupname\":\"��С��\",\"code\":\"[�°�]\"},{\"rid\":\"167\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"168\",\"groupname\":\"��С��\",\"code\":\"[�����]\"},{\"rid\":\"169\",\"groupname\":\"��С��\",\"code\":\"[�м�]\"},{\"rid\":\"170\",\"groupname\":\"��С��\",\"code\":\"[����Ĥ��]\"},{\"rid\":\"171\",\"groupname\":\"��С��\",\"code\":\"[Ĥ����]\"},{\"rid\":\"172\",\"groupname\":\"��С��\",\"code\":\"[�ŵ�����]\"},{\"rid\":\"173\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"174\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"175\",\"groupname\":\"��С��\",\"code\":\"[��ש]\"},{\"rid\":\"176\",\"groupname\":\"��С��\",\"code\":\"[������ש]\"},{\"rid\":\"177\",\"groupname\":\"��С��\",\"code\":\"[�ɷ�]\"},{\"rid\":\"178\",\"groupname\":\"��С��\",\"code\":\"[��������]\"},{\"rid\":\"179\",\"groupname\":\"��С��\",\"code\":\"[����style]\"},{\"rid\":\"180\",\"groupname\":\"��С��\",\"code\":\"[ţ]\"},{\"rid\":\"181\",\"groupname\":\"��С��\",\"code\":\"[õ��]\"},{\"rid\":\"182\",\"groupname\":\"��С��\",\"code\":\"[�ް�]\"},{\"rid\":\"183\",\"groupname\":\"��С��\",\"code\":\"[�Ƽ�]\"},{\"rid\":\"184\",\"groupname\":\"��С��\",\"code\":\"[�ż���]\"},{\"rid\":\"185\",\"groupname\":\"��С��\",\"code\":\"[�ȷ�]\"},{\"rid\":\"186\",\"groupname\":\"��С��\",\"code\":\"[�Ի�]\"},{\"rid\":\"187\",\"groupname\":\"��С��\",\"code\":\"[���Ϲ�]\"},{\"rid\":\"188\",\"groupname\":\"��С��\",\"code\":\"[�ϻ�]\"},{\"rid\":\"189\",\"groupname\":\"��С��\",\"code\":\"[��־����]\"},{\"rid\":\"190\",\"groupname\":\"��С��\",\"code\":\"[��ɪ]\"},{\"rid\":\"191\",\"groupname\":\"��С��\",\"code\":\"[�¶�Բ]\"},{\"rid\":\"192\",\"groupname\":\"��С��\",\"code\":\"[�в�]\"},{\"rid\":\"193\",\"groupname\":\"��С��\",\"code\":\"[΢��������]\"},{\"rid\":\"194\",\"groupname\":\"��С��\",\"code\":\"[�����]\"},{\"rid\":\"195\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"196\",\"groupname\":\"��С��\",\"code\":\"[���˽�]\"},{\"rid\":\"197\",\"groupname\":\"��С��\",\"code\":\"[�ղ�]\"},{\"rid\":\"198\",\"groupname\":\"��С��\",\"code\":\"[ϲ�ý���]\"},{\"rid\":\"199\",\"groupname\":\"��С��\",\"code\":\"[��ڸж�]\"},{\"rid\":\"200\",\"groupname\":\"��С��\",\"code\":\"[�ھ�����]\"},{\"rid\":\"201\",\"groupname\":\"��С��\",\"code\":\"[�����]\"},{\"rid\":\"202\",\"groupname\":\"��С��\",\"code\":\"[���˽���]\"},{\"rid\":\"203\",\"groupname\":\"��С��\",\"code\":\"[��������]\"},{\"rid\":\"204\",\"groupname\":\"��С��\",\"code\":\"[����ͭ��]\"},{\"rid\":\"205\",\"groupname\":\"��С��\",\"code\":\"[�¹��Ӽ���]\"},{\"rid\":\"206\",\"groupname\":\"��С��\",\"code\":\"[�������Ӽ���]\"},{\"rid\":\"207\",\"groupname\":\"��С��\",\"code\":\"[�������Ӽ���]\"},{\"rid\":\"208\",\"groupname\":\"��С��\",\"code\":\"[������Ӽ���]\"},{\"rid\":\"209\",\"groupname\":\"��С��\",\"code\":\"[ˣ����]\"},{\"rid\":\"210\",\"groupname\":\"��С��\",\"code\":\"[Ԫ������]\"},{\"rid\":\"211\",\"groupname\":\"��С��\",\"code\":\"[����Բ]\"},{\"rid\":\"212\",\"groupname\":\"��С��\",\"code\":\"[��Ԫ��]\"},{\"rid\":\"213\",\"groupname\":\"��С��\",\"code\":\"[�������]\"},{\"rid\":\"214\",\"groupname\":\"��С��\",\"code\":\"[������]\"},{\"rid\":\"215\",\"groupname\":\"��С��\",\"code\":\"[�ű���]\"},{\"rid\":\"216\",\"groupname\":\"��С��\",\"code\":\"[�����]\"},{\"rid\":\"217\",\"groupname\":\"��С��\",\"code\":\"[������]\"},{\"rid\":\"218\",\"groupname\":\"��С��\",\"code\":\"[������]\"},{\"rid\":\"219\",\"groupname\":\"��С��\",\"code\":\"[��Х]\"},{\"rid\":\"220\",\"groupname\":\"��С��\",\"code\":\"[�����]\"},{\"rid\":\"221\",\"groupname\":\"��С��\",\"code\":\"[��Աһ����]\"},{\"rid\":\"222\",\"groupname\":\"��С��\",\"code\":\"[�������]\"},{\"rid\":\"223\",\"groupname\":\"��С��\",\"code\":\"[������]\"},{\"rid\":\"224\",\"groupname\":\"��С��\",\"code\":\"[Բ������]\"},{\"rid\":\"225\",\"groupname\":\"��С��\",\"code\":\"[������]\"},{\"rid\":\"226\",\"groupname\":\"��С��\",\"code\":\"[Ҫ����]\"},{\"rid\":\"227\",\"groupname\":\"��С��\",\"code\":\"[ƽ����]\"},{\"rid\":\"228\",\"groupname\":\"��С��\",\"code\":\"[�ŵ���]\"},{\"rid\":\"229\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"230\",\"groupname\":\"��С��\",\"code\":\"[��Ѫ]\"},{\"rid\":\"231\",\"groupname\":\"��С��\",\"code\":\"[�ü���]\"},{\"rid\":\"232\",\"groupname\":\"��С��\",\"code\":\"[û����]\"},{\"rid\":\"233\",\"groupname\":\"�����\",\"code\":\"[�����գ��]\"},{\"rid\":\"234\",\"groupname\":\"�����\",\"code\":\"[�����΢Ц]\"},{\"rid\":\"235\",\"groupname\":\"�����\",\"code\":\"[�����ί��]\"},{\"rid\":\"236\",\"groupname\":\"�����\",\"code\":\"[�������Ƥ]\"},{\"rid\":\"237\",\"groupname\":\"�����\",\"code\":\"[�����ŭ]\"},{\"rid\":\"238\",\"groupname\":\"�����\",\"code\":\"[����ܿ���]\"},{\"rid\":\"239\",\"groupname\":\"�����\",\"code\":\"[����܇�]\"},{\"rid\":\"240\",\"groupname\":\"�����\",\"code\":\"[����ܻ�Ц]\"},{\"rid\":\"241\",\"groupname\":\"�����\",\"code\":\"[����ܺ�]\"},{\"rid\":\"242\",\"groupname\":\"�����\",\"code\":\"[����ܵ�ͷ]\"},{\"rid\":\"243\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"244\",\"groupname\":\"��������\",\"code\":\"[bmץ��]\"},{\"rid\":\"245\",\"groupname\":\"��������\",\"code\":\"[bm��ǹ]\"},{\"rid\":\"246\",\"groupname\":\"��������\",\"code\":\"[bm��]\"},{\"rid\":\"247\",\"groupname\":\"��������\",\"code\":\"[bm��]\"},{\"rid\":\"248\",\"groupname\":\"��������\",\"code\":\"[bmϲ��]\"},{\"rid\":\"249\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"250\",\"groupname\":\"��������\",\"code\":\"[bm�˷�]\"},{\"rid\":\"251\",\"groupname\":\"��������\",\"code\":\"[bmѪ��]\"},{\"rid\":\"252\",\"groupname\":\"��������\",\"code\":\"[bm�ڱǿ�]\"},{\"rid\":\"253\",\"groupname\":\"��������\",\"code\":\"[bm����ͷ]\"},{\"rid\":\"254\",\"groupname\":\"��������\",\"code\":\"[bm�²�]\"},{\"rid\":\"255\",\"groupname\":\"��������\",\"code\":\"[bmͶ��]\"},{\"rid\":\"256\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"257\",\"groupname\":\"��������\",\"code\":\"[bm��Ƥ]\"},{\"rid\":\"258\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"259\",\"groupname\":\"��������\",\"code\":\"[bm̧��]\"},{\"rid\":\"260\",\"groupname\":\"��������\",\"code\":\"[bm˼��]\"},{\"rid\":\"261\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"262\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"263\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"264\",\"groupname\":\"��������\",\"code\":\"[bm�ں�]\"},{\"rid\":\"265\",\"groupname\":\"��������\",\"code\":\"[bmæµ]\"},{\"rid\":\"266\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"267\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"268\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"269\",\"groupname\":\"��������\",\"code\":\"[bm����ˮ]\"},{\"rid\":\"270\",\"groupname\":\"��������\",\"code\":\"[bm������]\"},{\"rid\":\"271\",\"groupname\":\"��������\",\"code\":\"[bm·��]\"},{\"rid\":\"272\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"273\",\"groupname\":\"��������\",\"code\":\"[bm������]\"},{\"rid\":\"274\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"275\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"276\",\"groupname\":\"��������\",\"code\":\"[bm���]\"},{\"rid\":\"277\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"278\",\"groupname\":\"��������\",\"code\":\"[bm�ɰ�]\"},{\"rid\":\"279\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"280\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"281\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"282\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"283\",\"groupname\":\"��������\",\"code\":\"[bm����Ц]\"},{\"rid\":\"284\",\"groupname\":\"��������\",\"code\":\"[bm��Ц]\"},{\"rid\":\"285\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"286\",\"groupname\":\"��������\",\"code\":\"[bm����Ƥ]\"},{\"rid\":\"287\",\"groupname\":\"��������\",\"code\":\"[bm�ð�]\"},{\"rid\":\"288\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"289\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"290\",\"groupname\":\"��������\",\"code\":\"[bm�¶�]\"},{\"rid\":\"291\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"292\",\"groupname\":\"��������\",\"code\":\"[bm���]\"},{\"rid\":\"293\",\"groupname\":\"��������\",\"code\":\"[bm��Ц]\"},{\"rid\":\"294\",\"groupname\":\"��������\",\"code\":\"[bm�ж�]\"},{\"rid\":\"295\",\"groupname\":\"��������\",\"code\":\"[bm���]\"},{\"rid\":\"296\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"297\",\"groupname\":\"��������\",\"code\":\"[bm�ⲽ]\"},{\"rid\":\"298\",\"groupname\":\"��������\",\"code\":\"[bm��]\"},{\"rid\":\"299\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"300\",\"groupname\":\"��������\",\"code\":\"[bm��ɪ]\"},{\"rid\":\"301\",\"groupname\":\"��������\",\"code\":\"[bm��Ц]\"},{\"rid\":\"302\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"303\",\"groupname\":\"��������\",\"code\":\"[bm���]\"},{\"rid\":\"304\",\"groupname\":\"��������\",\"code\":\"[bm���]\"},{\"rid\":\"305\",\"groupname\":\"��������\",\"code\":\"[bm�Ծ�]\"},{\"rid\":\"306\",\"groupname\":\"��������\",\"code\":\"[bm��]\"},{\"rid\":\"307\",\"groupname\":\"��������\",\"code\":\"[bm��ɫ]\"},{\"rid\":\"308\",\"groupname\":\"��������\",\"code\":\"[bm�ͷ�]\"},{\"rid\":\"309\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"310\",\"groupname\":\"��������\",\"code\":\"[bm����]\"},{\"rid\":\"311\",\"groupname\":\"��������\",\"code\":\"[bm��ŭ]\"},{\"rid\":\"312\",\"groupname\":\"��������\",\"code\":\"[bm��ҹ]\"},{\"rid\":\"313\",\"groupname\":\"��������\",\"code\":\"[bm��ˬ]\"},{\"rid\":\"314\",\"groupname\":\"С����\",\"code\":\"[xklŭ��]\"},{\"rid\":\"315\",\"groupname\":\"С����\",\"code\":\"[xklתȦ]\"},{\"rid\":\"316\",\"groupname\":\"С����\",\"code\":\"[xklϲ]\"},{\"rid\":\"317\",\"groupname\":\"С����\",\"code\":\"[xklί��]\"},{\"rid\":\"318\",\"groupname\":\"С����\",\"code\":\"[xklʯ��]\"},{\"rid\":\"319\",\"groupname\":\"С����\",\"code\":\"[xkl�ڴ�]\"},{\"rid\":\"320\",\"groupname\":\"С����\",\"code\":\"[xkl����]\"},{\"rid\":\"321\",\"groupname\":\"С����\",\"code\":\"[xkl·��]\"},{\"rid\":\"322\",\"groupname\":\"С����\",\"code\":\"[xkl������]\"},{\"rid\":\"323\",\"groupname\":\"С����\",\"code\":\"[xkl��]\"},{\"rid\":\"324\",\"groupname\":\"񲵱\",\"code\":\"[din񲵱ʥ������]\"},{\"rid\":\"325\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"326\",\"groupname\":\"񲵱\",\"code\":\"[din˵��]\"},{\"rid\":\"327\",\"groupname\":\"񲵱\",\"code\":\"[din�����в�è]\"},{\"rid\":\"328\",\"groupname\":\"񲵱\",\"code\":\"[din���޸���]\"},{\"rid\":\"329\",\"groupname\":\"񲵱\",\"code\":\"[din񲵱�в�è]\"},{\"rid\":\"330\",\"groupname\":\"񲵱\",\"code\":\"[din񲵱xmas]\"},{\"rid\":\"331\",\"groupname\":\"񲵱\",\"code\":\"[din񲵱ʥ����]\"},{\"rid\":\"332\",\"groupname\":\"񲵱\",\"code\":\"[din񲵱����ʥ����]\"},{\"rid\":\"333\",\"groupname\":\"񲵱\",\"code\":\"[din񲵱��������]\"},{\"rid\":\"334\",\"groupname\":\"񲵱\",\"code\":\"[din񲵱���������]\"},{\"rid\":\"335\",\"groupname\":\"񲵱\",\"code\":\"[din񲵱��������]\"},{\"rid\":\"336\",\"groupname\":\"񲵱\",\"code\":\"[din񲵱���ޱ���]\"},{\"rid\":\"337\",\"groupname\":\"񲵱\",\"code\":\"[din񲵱���]\"},{\"rid\":\"338\",\"groupname\":\"񲵱\",\"code\":\"[din񲵱����]\"},{\"rid\":\"339\",\"groupname\":\"񲵱\",\"code\":\"[dinתת]\"},{\"rid\":\"340\",\"groupname\":\"񲵱\",\"code\":\"[dinײǽ]\"},{\"rid\":\"341\",\"groupname\":\"񲵱\",\"code\":\"[dinץ��]\"},{\"rid\":\"342\",\"groupname\":\"񲵱\",\"code\":\"[din�޺�]\"},{\"rid\":\"343\",\"groupname\":\"񲵱\",\"code\":\"[din��Ϣ]\"},{\"rid\":\"344\",\"groupname\":\"񲵱\",\"code\":\"[din�˷�]\"},{\"rid\":\"345\",\"groupname\":\"񲵱\",\"code\":\"[din��ײ]\"},{\"rid\":\"346\",\"groupname\":\"񲵱\",\"code\":\"[din��Ŷ]\"},{\"rid\":\"347\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"348\",\"groupname\":\"񲵱\",\"code\":\"[din˯��]\"},{\"rid\":\"349\",\"groupname\":\"񲵱\",\"code\":\"[din˧]\"},{\"rid\":\"350\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"351\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"352\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"353\",\"groupname\":\"񲵱\",\"code\":\"[dinŭ]\"},{\"rid\":\"354\",\"groupname\":\"񲵱\",\"code\":\"[din��ͷ]\"},{\"rid\":\"355\",\"groupname\":\"񲵱\",\"code\":\"[din��Ѫ]\"},{\"rid\":\"356\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"357\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"358\",\"groupname\":\"񲵱\",\"code\":\"[din��]\"},{\"rid\":\"359\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"360\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"361\",\"groupname\":\"񲵱\",\"code\":\"[din��ױ]\"},{\"rid\":\"362\",\"groupname\":\"񲵱\",\"code\":\"[din��]\"},{\"rid\":\"363\",\"groupname\":\"񲵱\",\"code\":\"[din��]\"},{\"rid\":\"364\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"365\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"366\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"367\",\"groupname\":\"񲵱\",\"code\":\"[din����1]\"},{\"rid\":\"368\",\"groupname\":\"񲵱\",\"code\":\"[din����2]\"},{\"rid\":\"369\",\"groupname\":\"񲵱\",\"code\":\"[din񲵱]\"},{\"rid\":\"370\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"371\",\"groupname\":\"񲵱\",\"code\":\"[din��]\"},{\"rid\":\"372\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"373\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"374\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"375\",\"groupname\":\"񲵱\",\"code\":\"[din����]\"},{\"rid\":\"376\",\"groupname\":\"����\",\"code\":\"[ali�ʹ�]\"},{\"rid\":\"377\",\"groupname\":\"����\",\"code\":\"[ali������]\"},{\"rid\":\"378\",\"groupname\":\"����\",\"code\":\"[ali׷]\"},{\"rid\":\"379\",\"groupname\":\"����\",\"code\":\"[aliתȦ��]\"},{\"rid\":\"380\",\"groupname\":\"����\",\"code\":\"[aliת]\"},{\"rid\":\"381\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"382\",\"groupname\":\"����\",\"code\":\"[aliԪ��]\"},{\"rid\":\"383\",\"groupname\":\"����\",\"code\":\"[aliҡ��]\"},{\"rid\":\"384\",\"groupname\":\"����\",\"code\":\"[ali������]\"},{\"rid\":\"385\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"386\",\"groupname\":\"����\",\"code\":\"[aliЦ����]\"},{\"rid\":\"387\",\"groupname\":\"����\",\"code\":\"[aliЦ]\"},{\"rid\":\"388\",\"groupname\":\"����\",\"code\":\"[ali������]\"},{\"rid\":\"389\",\"groupname\":\"����\",\"code\":\"[ali�ʻ�]\"},{\"rid\":\"390\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"391\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"392\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"393\",\"groupname\":\"����\",\"code\":\"[ali��Ѫ]\"},{\"rid\":\"394\",\"groupname\":\"����\",\"code\":\"[ali͵��]\"},{\"rid\":\"395\",\"groupname\":\"����\",\"code\":\"[ali������]\"},{\"rid\":\"396\",\"groupname\":\"����\",\"code\":\"[ali˯]\"},{\"rid\":\"397\",\"groupname\":\"����\",\"code\":\"[ali˦��]\"},{\"rid\":\"398\",\"groupname\":\"����\",\"code\":\"[aliˤ]\"},{\"rid\":\"399\",\"groupname\":\"����\",\"code\":\"[ali��Ǯ]\"},{\"rid\":\"400\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"401\",\"groupname\":\"����\",\"code\":\"[ali��һ��]\"},{\"rid\":\"402\",\"groupname\":\"����\",\"code\":\"[aliǷ��]\"},{\"rid\":\"403\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"404\",\"groupname\":\"����\",\"code\":\"[ali�˵�]\"},{\"rid\":\"405\",\"groupname\":\"����\",\"code\":\"[aliƮ]\"},{\"rid\":\"406\",\"groupname\":\"����\",\"code\":\"[aliƮ��]\"},{\"rid\":\"407\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"408\",\"groupname\":\"����\",\"code\":\"[ali������]\"},{\"rid\":\"409\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"410\",\"groupname\":\"����\",\"code\":\"[ali��ǽ]\"},{\"rid\":\"411\",\"groupname\":\"����\",\"code\":\"[ali����ͷ]\"},{\"rid\":\"412\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"413\",\"groupname\":\"����\",\"code\":\"[ali��Ƥ]\"},{\"rid\":\"414\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"415\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"416\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"417\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"418\",\"groupname\":\"����\",\"code\":\"[ali��ʬ��]\"},{\"rid\":\"419\",\"groupname\":\"����\",\"code\":\"[ali����Ȧ]\"},{\"rid\":\"420\",\"groupname\":\"����\",\"code\":\"[ali��ȦȦ]\"},{\"rid\":\"421\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"422\",\"groupname\":\"����\",\"code\":\"[ali��Ц]\"},{\"rid\":\"423\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"424\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"425\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"426\",\"groupname\":\"����\",\"code\":\"[ali������]\"},{\"rid\":\"427\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"428\",\"groupname\":\"����\",\"code\":\"[ali��ͷ]\"},{\"rid\":\"429\",\"groupname\":\"����\",\"code\":\"[ali��ɪ]\"},{\"rid\":\"430\",\"groupname\":\"����\",\"code\":\"[ali������]\"},{\"rid\":\"431\",\"groupname\":\"����\",\"code\":\"[ali���]\"},{\"rid\":\"432\",\"groupname\":\"����\",\"code\":\"[ali���]\"},{\"rid\":\"433\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"434\",\"groupname\":\"����\",\"code\":\"[ali���ͷ�]\"},{\"rid\":\"435\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"436\",\"groupname\":\"����\",\"code\":\"[alibiechaonew]\"},{\"rid\":\"437\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"438\",\"groupname\":\"����\",\"code\":\"[ali��һ��]\"},{\"rid\":\"439\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"440\",\"groupname\":\"����\",\"code\":\"[ali88]\"},{\"rid\":\"441\",\"groupname\":\"����\",\"code\":\"[ali��Ц]\"},{\"rid\":\"442\",\"groupname\":\"����\",\"code\":\"[aliԩ]\"},{\"rid\":\"443\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"444\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"445\",\"groupname\":\"����\",\"code\":\"[ali�ֺ�]\"},{\"rid\":\"446\",\"groupname\":\"����\",\"code\":\"[ali������]\"},{\"rid\":\"447\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"448\",\"groupname\":\"����\",\"code\":\"[aliigh]\"},{\"rid\":\"449\",\"groupname\":\"����\",\"code\":\"[ali��]\"},{\"rid\":\"450\",\"groupname\":\"����\",\"code\":\"[ali����]\"},{\"rid\":\"451\",\"groupname\":\"����\",\"code\":\"[ali����ƽ]\"},{\"rid\":\"452\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[BOBO����]\"},{\"rid\":\"453\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[BOBO����]\"},{\"rid\":\"454\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[BOBO����]\"},{\"rid\":\"455\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[BOBO����]\"},{\"rid\":\"456\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[bobuyaoa]\"},{\"rid\":\"457\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[BOBO����]\"},{\"rid\":\"458\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[BOBO����]\"},{\"rid\":\"459\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[BOBO��]\"},{\"rid\":\"460\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[BOBO����]\"},{\"rid\":\"461\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[TOTO���Ұ�]\"},{\"rid\":\"462\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[toto����]\"},{\"rid\":\"463\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[bobo����]\"},{\"rid\":\"464\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[toto����]\"},{\"rid\":\"465\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[toto����ҡ��]\"},{\"rid\":\"466\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[toto����]\"},{\"rid\":\"467\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[toto˯��]\"},{\"rid\":\"468\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[toto˦ͷ��]\"},{\"rid\":\"469\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[totoƮ��]\"},{\"rid\":\"470\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[toto��]\"},{\"rid\":\"471\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[toto����]\"},{\"rid\":\"472\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[boboץ��]\"},{\"rid\":\"473\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[bobo����]\"},{\"rid\":\"474\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[bobo������]\"},{\"rid\":\"475\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[boboĤ��]\"},{\"rid\":\"476\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[bobo����]\"},{\"rid\":\"477\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[bobo��Ҫ��]\"},{\"rid\":\"478\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[bobo������]\"},{\"rid\":\"479\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[�а�]\"},{\"rid\":\"480\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[TOTOYES]\"},{\"rid\":\"481\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[�Ұ���]\"},{\"rid\":\"482\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[ŭ��]\"},{\"rid\":\"483\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[�޹�]\"},{\"rid\":\"484\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[��Ц]\"},{\"rid\":\"485\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[��Ǯ]\"},{\"rid\":\"486\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[�仨]\"},{\"rid\":\"487\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[���]\"},{\"rid\":\"488\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[��èè]\"},{\"rid\":\"489\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[����]\"},{\"rid\":\"490\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[������]\"},{\"rid\":\"491\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[��]\"},{\"rid\":\"492\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[������]\"},{\"rid\":\"493\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[BOBO��]\"},{\"rid\":\"494\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[Hi]\"},{\"rid\":\"495\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[BOBOôô��]\"},{\"rid\":\"496\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[�Ұ�����]\"},{\"rid\":\"497\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[��һ��]\"},{\"rid\":\"498\",\"groupname\":\"BOBO��TOTO\",\"code\":\"[�Է�]\"},{\"rid\":\"499\",\"groupname\":\"С����\",\"code\":\"[c˧]\"},{\"rid\":\"500\",\"groupname\":\"С����\",\"code\":\"[c��ϲ]\"},{\"rid\":\"501\",\"groupname\":\"С����\",\"code\":\"[c�Ժ�]\"},{\"rid\":\"502\",\"groupname\":\"С����\",\"code\":\"[c��̱]\"},{\"rid\":\"503\",\"groupname\":\"С����\",\"code\":\"[c��]\"},{\"rid\":\"504\",\"groupname\":\"С����\",\"code\":\"[c��]\"},{\"rid\":\"505\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"506\",\"groupname\":\"С����\",\"code\":\"[c��Ц]\"},{\"rid\":\"507\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"508\",\"groupname\":\"С����\",\"code\":\"[c���ҿ�]\"},{\"rid\":\"509\",\"groupname\":\"С����\",\"code\":\"[c��Ц]\"},{\"rid\":\"510\",\"groupname\":\"С����\",\"code\":\"[c������]\"},{\"rid\":\"511\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"512\",\"groupname\":\"С����\",\"code\":\"[c��ע]\"},{\"rid\":\"513\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"514\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"515\",\"groupname\":\"С����\",\"code\":\"[c�ɻ�]\"},{\"rid\":\"516\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"517\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"518\",\"groupname\":\"С����\",\"code\":\"[c�ڱǿ�]\"},{\"rid\":\"519\",\"groupname\":\"С����\",\"code\":\"[c�ڴ�]\"},{\"rid\":\"520\",\"groupname\":\"С����\",\"code\":\"[cҡͷ��]\"},{\"rid\":\"521\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"522\",\"groupname\":\"С����\",\"code\":\"[c��ɬ]\"},{\"rid\":\"523\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"524\",\"groupname\":\"С����\",\"code\":\"[c��ɪ]\"},{\"rid\":\"525\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"526\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"527\",\"groupname\":\"С����\",\"code\":\"[cί��]\"},{\"rid\":\"528\",\"groupname\":\"С����\",\"code\":\"[c˦��ͷ]\"},{\"rid\":\"529\",\"groupname\":\"С����\",\"code\":\"[cҡͷ��]\"},{\"rid\":\"530\",\"groupname\":\"С����\",\"code\":\"[cץ��]\"},{\"rid\":\"531\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"532\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"533\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"534\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"535\",\"groupname\":\"С����\",\"code\":\"[c�𾪿�]\"},{\"rid\":\"536\",\"groupname\":\"С����\",\"code\":\"[cҡ��]\"},{\"rid\":\"537\",\"groupname\":\"С����\",\"code\":\"[c����Ц]\"},{\"rid\":\"538\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"539\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"540\",\"groupname\":\"С����\",\"code\":\"[c����]\"},{\"rid\":\"541\",\"groupname\":\"��С��\",\"code\":\"[lxhx��]\"},{\"rid\":\"542\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"543\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"544\",\"groupname\":\"��С��\",\"code\":\"[lxhx��]\"},{\"rid\":\"545\",\"groupname\":\"��С��\",\"code\":\"[lxhx���]\"},{\"rid\":\"546\",\"groupname\":\"��С��\",\"code\":\"[lxhx��]\"},{\"rid\":\"547\",\"groupname\":\"��С��\",\"code\":\"[lxhx��]\"},{\"rid\":\"548\",\"groupname\":\"��С��\",\"code\":\"[lxhx�ܶ�]\"},{\"rid\":\"549\",\"groupname\":\"��С��\",\"code\":\"[lxhxЦ]\"},{\"rid\":\"550\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"551\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"552\",\"groupname\":\"��С��\",\"code\":\"[lxhx�ұ�]\"},{\"rid\":\"553\",\"groupname\":\"��С��\",\"code\":\"[lxhxתͷ]\"},{\"rid\":\"554\",\"groupname\":\"��С��\",\"code\":\"[lxhx��Ծ]\"},{\"rid\":\"555\",\"groupname\":\"��С��\",\"code\":\"[lxhxת��]\"},{\"rid\":\"556\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"557\",\"groupname\":\"��С��\",\"code\":\"[lxhx��]\"},{\"rid\":\"558\",\"groupname\":\"��С��\",\"code\":\"[lxhx�ӻ�]\"},{\"rid\":\"559\",\"groupname\":\"��С��\",\"code\":\"[lxhx��תȦ]\"},{\"rid\":\"560\",\"groupname\":\"��С��\",\"code\":\"[lxhx��]\"},{\"rid\":\"561\",\"groupname\":\"��С��\",\"code\":\"[lxhx��ɪ]\"},{\"rid\":\"562\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"563\",\"groupname\":\"��С��\",\"code\":\"[lxhx������]\"},{\"rid\":\"564\",\"groupname\":\"��С��\",\"code\":\"[lxhx��]\"},{\"rid\":\"565\",\"groupname\":\"��С��\",\"code\":\"[lxhxɨ��]\"},{\"rid\":\"566\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"567\",\"groupname\":\"��С��\",\"code\":\"[lxhx���]\"},{\"rid\":\"568\",\"groupname\":\"��С��\",\"code\":\"[lxhx��Ȧ]\"},{\"rid\":\"569\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"570\",\"groupname\":\"��С��\",\"code\":\"[lxhxˢ��]\"},{\"rid\":\"571\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"572\",\"groupname\":\"��С��\",\"code\":\"[lxhx������]\"},{\"rid\":\"573\",\"groupname\":\"��С��\",\"code\":\"[lxhx������]\"},{\"rid\":\"574\",\"groupname\":\"��С��\",\"code\":\"[lxhx�仯]\"},{\"rid\":\"575\",\"groupname\":\"��С��\",\"code\":\"[lxhx�����]\"},{\"rid\":\"576\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"577\",\"groupname\":\"��С��\",\"code\":\"[lxhx��]\"},{\"rid\":\"578\",\"groupname\":\"��С��\",\"code\":\"[lxhx��2]\"},{\"rid\":\"579\",\"groupname\":\"��С��\",\"code\":\"[lxhx��3]\"},{\"rid\":\"580\",\"groupname\":\"��С��\",\"code\":\"[lxhx��4]\"},{\"rid\":\"581\",\"groupname\":\"��С��\",\"code\":\"[lxhx��5]\"},{\"rid\":\"582\",\"groupname\":\"��С��\",\"code\":\"[lxhx��6]\"},{\"rid\":\"583\",\"groupname\":\"��С��\",\"code\":\"[lxhx��7]\"},{\"rid\":\"584\",\"groupname\":\"��С��\",\"code\":\"[lxhx��8]\"},{\"rid\":\"585\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"586\",\"groupname\":\"��С��\",\"code\":\"[lxhx����ǹ]\"},{\"rid\":\"587\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"588\",\"groupname\":\"��С��\",\"code\":\"[lxhx������]\"},{\"rid\":\"589\",\"groupname\":\"��С��\",\"code\":\"[lxhx��Ѫ]\"},{\"rid\":\"590\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"591\",\"groupname\":\"��С��\",\"code\":\"[lxhx��Ŀ]\"},{\"rid\":\"592\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"593\",\"groupname\":\"��С��\",\"code\":\"[lxhx�ʺ�]\"},{\"rid\":\"594\",\"groupname\":\"��С��\",\"code\":\"[lxhx��Ŀ]\"},{\"rid\":\"595\",\"groupname\":\"��С��\",\"code\":\"[lxhx��]\"},{\"rid\":\"596\",\"groupname\":\"��С��\",\"code\":\"[lxhx��]\"},{\"rid\":\"597\",\"groupname\":\"��С��\",\"code\":\"[lxhxʧ��]\"},{\"rid\":\"598\",\"groupname\":\"��С��\",\"code\":\"[lxhx��]\"},{\"rid\":\"599\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"600\",\"groupname\":\"��С��\",\"code\":\"[lxhx��]\"},{\"rid\":\"601\",\"groupname\":\"��С��\",\"code\":\"[lxhxԹ��]\"},{\"rid\":\"602\",\"groupname\":\"��С��\",\"code\":\"[lxhx˯��]\"},{\"rid\":\"603\",\"groupname\":\"��С��\",\"code\":\"[lxhx�����]\"},{\"rid\":\"604\",\"groupname\":\"��С��\",\"code\":\"[lxhx�ĵ�]\"},{\"rid\":\"605\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"606\",\"groupname\":\"��С��\",\"code\":\"[lxhx˳ë]\"},{\"rid\":\"607\",\"groupname\":\"��С��\",\"code\":\"[lxhx����]\"},{\"rid\":\"608\",\"groupname\":\"��С��\",\"code\":\"[lxhx��ˬ]\"},{\"rid\":\"609\",\"groupname\":\"��С��\",\"code\":\"[lxhx�ϴ�]\"},{\"rid\":\"610\",\"groupname\":\"��С��\",\"code\":\"[lxhx���տ���]\"},{\"rid\":\"611\",\"groupname\":\"��С��\",\"code\":\"[mtjj����]\"},{\"rid\":\"612\",\"groupname\":\"��˹��\",\"code\":\"[gst�ڱ�ʺ]\"},{\"rid\":\"613\",\"groupname\":\"��˹��\",\"code\":\"[gst����]\"},{\"rid\":\"614\",\"groupname\":\"��˹��\",\"code\":\"[gst������]\"},{\"rid\":\"615\",\"groupname\":\"��˹��\",\"code\":\"[gst����]\"},{\"rid\":\"616\",\"groupname\":\"��˹��\",\"code\":\"[gst���Ѷ�]\"},{\"rid\":\"617\",\"groupname\":\"��˹��\",\"code\":\"[gst������]\"},{\"rid\":\"618\",\"groupname\":\"��˹��\",\"code\":\"[gstתתת]\"},{\"rid\":\"619\",\"groupname\":\"��˹��\",\"code\":\"[gst��]\"},{\"rid\":\"620\",\"groupname\":\"��˹��\",\"code\":\"[gst������]\"},{\"rid\":\"621\",\"groupname\":\"��˹��\",\"code\":\"[gst���տ���]\"},{\"rid\":\"622\",\"groupname\":\"��˹��\",\"code\":\"[gst�˼Ҳ���]\"},{\"rid\":\"623\",\"groupname\":\"��˹��\",\"code\":\"[gst������]\"},{\"rid\":\"624\",\"groupname\":\"��˹��\",\"code\":\"[gst����]\"},{\"rid\":\"625\",\"groupname\":\"��˹��\",\"code\":\"[gst��]\"},{\"rid\":\"626\",\"groupname\":\"��˹��\",\"code\":\"[gst����ѽ]\"},{\"rid\":\"627\",\"groupname\":\"��˹��\",\"code\":\"[gst��������]\"},{\"rid\":\"628\",\"groupname\":\"��˹��\",\"code\":\"[gst��Ц��]\"},{\"rid\":\"629\",\"groupname\":\"��˹��\",\"code\":\"[gstѽ���]\"},{\"rid\":\"630\",\"groupname\":\"��˹��\",\"code\":\"[gst�°���]\"},{\"rid\":\"631\",\"groupname\":\"��˹��\",\"code\":\"[gst��]\"},{\"rid\":\"632\",\"groupname\":\"��˹��\",\"code\":\"[gst����]\"},{\"rid\":\"633\",\"groupname\":\"��˹��\",\"code\":\"[gst������]\"},{\"rid\":\"634\",\"groupname\":\"��˹��\",\"code\":\"[gst˧����]\"},{\"rid\":\"635\",\"groupname\":\"��˹��\",\"code\":\"[gst������]\"},{\"rid\":\"636\",\"groupname\":\"��˹��\",\"code\":\"[gst�ٺٺ�]\"},{\"rid\":\"637\",\"groupname\":\"��˹��\",\"code\":\"[gst��ɪ]\"},{\"rid\":\"638\",\"groupname\":\"��˹��\",\"code\":\"[gst����]\"},{\"rid\":\"639\",\"groupname\":\"����\",\"code\":\"[lt��һ]\"},{\"rid\":\"640\",\"groupname\":\"����\",\"code\":\"[lt�����]\"},{\"rid\":\"641\",\"groupname\":\"����\",\"code\":\"[lt��]\"},{\"rid\":\"642\",\"groupname\":\"����\",\"code\":\"[lt��Ʊ]\"},{\"rid\":\"643\",\"groupname\":\"����\",\"code\":\"[lt���]\"},{\"rid\":\"644\",\"groupname\":\"����\",\"code\":\"[ltҡ��]\"},{\"rid\":\"645\",\"groupname\":\"����\",\"code\":\"[lt��ʥ��]\"},{\"rid\":\"646\",\"groupname\":\"����\",\"code\":\"[lt��]\"},{\"rid\":\"647\",\"groupname\":\"����\",\"code\":\"[lt����style]\"},{\"rid\":\"648\",\"groupname\":\"����\",\"code\":\"[lt�Զ���]\"},{\"rid\":\"649\",\"groupname\":\"����\",\"code\":\"[lt����]\"},{\"rid\":\"650\",\"groupname\":\"����\",\"code\":\"[lt�п���]\"},{\"rid\":\"651\",\"groupname\":\"����\",\"code\":\"[lt����]\"},{\"rid\":\"652\",\"groupname\":\"����\",\"code\":\"[lt��Ϲ]\"},{\"rid\":\"653\",\"groupname\":\"����\",\"code\":\"[lt��Ѫ]\"},{\"rid\":\"654\",\"groupname\":\"����\",\"code\":\"[lt����]\"},{\"rid\":\"655\",\"groupname\":\"����\",\"code\":\"[ltҡ��]\"},{\"rid\":\"656\",\"groupname\":\"����\",\"code\":\"[lt��]\"},{\"rid\":\"657\",\"groupname\":\"����\",\"code\":\"[lt��Ϲ]\"},{\"rid\":\"658\",\"groupname\":\"����\",\"code\":\"[lt����]\"},{\"rid\":\"659\",\"groupname\":\"����\",\"code\":\"[lt����]\"},{\"rid\":\"660\",\"groupname\":\"����\",\"code\":\"[lt����]\"},{\"rid\":\"661\",\"groupname\":\"����\",\"code\":\"[ltĸ�׽�]\"},{\"rid\":\"662\",\"groupname\":\"����\",\"code\":\"[lt�ڱ�]\"},{\"rid\":\"663\",\"groupname\":\"����\",\"code\":\"[lt��Ƿ]\"},{\"rid\":\"664\",\"groupname\":\"����\",\"code\":\"[lt��Ŀ]\"},{\"rid\":\"665\",\"groupname\":\"����\",\"code\":\"[lt��]\"},{\"rid\":\"666\",\"groupname\":\"����\",\"code\":\"[lt��ǹ]\"},{\"rid\":\"667\",\"groupname\":\"����\",\"code\":\"[lt����]\"},{\"rid\":\"668\",\"groupname\":\"����\",\"code\":\"[lt��]\"},{\"rid\":\"669\",\"groupname\":\"����\",\"code\":\"[ltǱˮ]\"},{\"rid\":\"670\",\"groupname\":\"����\",\"code\":\"[lt������Ц]\"},{\"rid\":\"671\",\"groupname\":\"����\",\"code\":\"[lt����]\"},{\"rid\":\"672\",\"groupname\":\"����\",\"code\":\"[lt��Ѫ]\"},{\"rid\":\"673\",\"groupname\":\"����\",\"code\":\"[lt�޺�]\"},{\"rid\":\"674\",\"groupname\":\"����\",\"code\":\"[lt�ɻ�]\"},{\"rid\":\"675\",\"groupname\":\"����\",\"code\":\"[lt����]\"},{\"rid\":\"676\",\"groupname\":\"����\",\"code\":\"[ltЦ��]\"},{\"rid\":\"677\",\"groupname\":\"����\",\"code\":\"[lt��]\"},{\"rid\":\"678\",\"groupname\":\"����\",\"code\":\"[ltѩ]\"},{\"rid\":\"679\",\"groupname\":\"����\",\"code\":\"[ltת��]\"},{\"rid\":\"680\",\"groupname\":\"����\",\"code\":\"[lt͵��]\"},{\"rid\":\"681\",\"groupname\":\"����\",\"code\":\"[lt����]\"},{\"rid\":\"682\",\"groupname\":\"����\",\"code\":\"[lt��]\"},{\"rid\":\"683\",\"groupname\":\"����\",\"code\":\"[lt�ҷ�����]\"},{\"rid\":\"684\",\"groupname\":\"����\",\"code\":\"[lt����]\"},{\"rid\":\"685\",\"groupname\":\"����\",\"code\":\"[lt��]\"},{\"rid\":\"686\",\"groupname\":\"����\",\"code\":\"[lt������]\"},{\"rid\":\"687\",\"groupname\":\"����\",\"code\":\"[lt��]\"},{\"rid\":\"688\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"689\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xbת]\"},{\"rid\":\"690\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xbתȦ]\"},{\"rid\":\"691\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xbָָ]\"},{\"rid\":\"692\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"693\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb�վ�]\"},{\"rid\":\"694\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��]\"},{\"rid\":\"695\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��Ц]\"},{\"rid\":\"696\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb�ɻ�]\"},{\"rid\":\"697\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xbҡ��]\"},{\"rid\":\"698\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb�۾�]\"},{\"rid\":\"699\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xbѹ��]\"},{\"rid\":\"700\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��]\"},{\"rid\":\"701\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb�˷�]\"},{\"rid\":\"702\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xbϲ��]\"},{\"rid\":\"703\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xbС��]\"},{\"rid\":\"704\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"705\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"706\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��ʹ]\"},{\"rid\":\"707\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb̫��]\"},{\"rid\":\"708\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb˯��]\"},{\"rid\":\"709\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb˦��]\"},{\"rid\":\"710\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"711\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"712\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"713\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��]\"},{\"rid\":\"714\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"715\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"716\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb�ڴ�]\"},{\"rid\":\"717\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"718\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xbŭ]\"},{\"rid\":\"719\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xbŬ��]\"},{\"rid\":\"720\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xbĴָ]\"},{\"rid\":\"721\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��]\"},{\"rid\":\"722\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"723\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��]\"},{\"rid\":\"724\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"725\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"726\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"727\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��Ц]\"},{\"rid\":\"728\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��]\"},{\"rid\":\"729\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"730\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"731\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb�Ȳ�]\"},{\"rid\":\"732\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��]\"},{\"rid\":\"733\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"734\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"735\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb�紵]\"},{\"rid\":\"736\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb�糵]\"},{\"rid\":\"737\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��ħ]\"},{\"rid\":\"738\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��]\"},{\"rid\":\"739\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��Ц]\"},{\"rid\":\"740\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��]\"},{\"rid\":\"741\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"742\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb��]\"},{\"rid\":\"743\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"744\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb�Է�]\"},{\"rid\":\"745\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb�԰�]\"},{\"rid\":\"746\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"747\",\"groupname\":\"�׹Ǿ�\",\"code\":\"[xb����]\"},{\"rid\":\"748\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"749\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"750\",\"groupname\":\"hello�˲�\",\"code\":\"[caiײǽ]\"},{\"rid\":\"751\",\"groupname\":\"hello�˲�\",\"code\":\"[cai��ѽ]\"},{\"rid\":\"752\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"753\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"754\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"755\",\"groupname\":\"hello�˲�\",\"code\":\"[cai�ѹ�]\"},{\"rid\":\"756\",\"groupname\":\"hello�˲�\",\"code\":\"[cai͵��]\"},{\"rid\":\"757\",\"groupname\":\"hello�˲�\",\"code\":\"[cai̫����]\"},{\"rid\":\"758\",\"groupname\":\"hello�˲�\",\"code\":\"[cai��ף]\"},{\"rid\":\"759\",\"groupname\":\"hello�˲�\",\"code\":\"[caiǮ]\"},{\"rid\":\"760\",\"groupname\":\"hello�˲�\",\"code\":\"[caiǱˮ]\"},{\"rid\":\"761\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"762\",\"groupname\":\"hello�˲�\",\"code\":\"[cai��Ҷ]\"},{\"rid\":\"763\",\"groupname\":\"hello�˲�\",\"code\":\"[cai��]\"},{\"rid\":\"764\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"765\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"766\",\"groupname\":\"hello�˲�\",\"code\":\"[cai��Ц]\"},{\"rid\":\"767\",\"groupname\":\"hello�˲�\",\"code\":\"[cai��ͷ]\"},{\"rid\":\"768\",\"groupname\":\"hello�˲�\",\"code\":\"[cai���]\"},{\"rid\":\"769\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"770\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"771\",\"groupname\":\"hello�˲�\",\"code\":\"[cai���]\"},{\"rid\":\"772\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"773\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"774\",\"groupname\":\"hello�˲�\",\"code\":\"[cai��ʺ]\"},{\"rid\":\"775\",\"groupname\":\"hello�˲�\",\"code\":\"[cai����]\"},{\"rid\":\"776\",\"groupname\":\"hello�˲�\",\"code\":\"[cai�ݰ�]\"},{\"rid\":\"777\",\"groupname\":\"hello�˲�\",\"code\":\"[cai��]\"},{\"rid\":\"778\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lbװɵ]\"},{\"rid\":\"779\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb��]\"},{\"rid\":\"780\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb��]\"},{\"rid\":\"781\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb���]\"},{\"rid\":\"782\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb�ٺ�]\"},{\"rid\":\"783\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb����]\"},{\"rid\":\"784\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb��]\"},{\"rid\":\"785\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lbҡͷ]\"},{\"rid\":\"786\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb��]\"},{\"rid\":\"787\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb����]\"},{\"rid\":\"788\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb��]\"},{\"rid\":\"789\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb��]\"},{\"rid\":\"790\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb����]\"},{\"rid\":\"791\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb����]\"},{\"rid\":\"792\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb��]\"},{\"rid\":\"793\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb��]\"},{\"rid\":\"794\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb����]\"},{\"rid\":\"795\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lbˬ]\"},{\"rid\":\"796\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lbζ]\"},{\"rid\":\"797\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb����]\"},{\"rid\":\"798\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb˧]\"},{\"rid\":\"799\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb��]\"},{\"rid\":\"800\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb��]\"},{\"rid\":\"801\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb��]\"},{\"rid\":\"802\",\"groupname\":\"��̱�ܲ�\",\"code\":\"[lb����]\"},{\"rid\":\"803\",\"groupname\":\"������\",\"code\":\"[ala��]\"},{\"rid\":\"804\",\"groupname\":\"������\",\"code\":\"[ala����]\"},{\"rid\":\"805\",\"groupname\":\"������\",\"code\":\"[alaҮ]\"},{\"rid\":\"806\",\"groupname\":\"������\",\"code\":\"[ala��]\"},{\"rid\":\"807\",\"groupname\":\"������\",\"code\":\"[ala����]\"},{\"rid\":\"808\",\"groupname\":\"������\",\"code\":\"[ala������]\"},{\"rid\":\"809\",\"groupname\":\"������\",\"code\":\"[ala����]\"},{\"rid\":\"810\",\"groupname\":\"������\",\"code\":\"[ala����]\"},{\"rid\":\"811\",\"groupname\":\"������\",\"code\":\"[ala����]\"},{\"rid\":\"812\",\"groupname\":\"������\",\"code\":\"[ala���]\"},{\"rid\":\"813\",\"groupname\":\"������\",\"code\":\"[ala��]\"},{\"rid\":\"814\",\"groupname\":\"������\",\"code\":\"[ala����]\"},{\"rid\":\"815\",\"groupname\":\"������\",\"code\":\"[ala����]\"},{\"rid\":\"816\",\"groupname\":\"������\",\"code\":\"[alaŤ��Ť]\"},{\"rid\":\"817\",\"groupname\":\"������\",\"code\":\"[ala����ͷ]\"},{\"rid\":\"818\",\"groupname\":\"������\",\"code\":\"[alaôô]\"},{\"rid\":\"819\",\"groupname\":\"������\",\"code\":\"[ala�ٺٺ�]\"},{\"rid\":\"820\",\"groupname\":\"������\",\"code\":\"[ala��]\"},{\"rid\":\"821\",\"groupname\":\"������\",\"code\":\"[ala��]\"},{\"rid\":\"822\",\"groupname\":\"������\",\"code\":\"[ala�ϻ�]\"},{\"rid\":\"823\",\"groupname\":\"������\",\"code\":\"[ala��������]\"},{\"rid\":\"824\",\"groupname\":\"������\",\"code\":\"[alaƮ��]\"},{\"rid\":\"825\",\"groupname\":\"������\",\"code\":\"[ala�Ի�]\"},{\"rid\":\"826\",\"groupname\":\"������\",\"code\":\"[ala����]\"},{\"rid\":\"827\",\"groupname\":\"������\",\"code\":\"[ala����]\"},{\"rid\":\"828\",\"groupname\":\"������\",\"code\":\"[ala˥]\"},{\"rid\":\"829\",\"groupname\":\"������\",\"code\":\"[alt����]\"},{\"rid\":\"830\",\"groupname\":\"С�ۼ�\",\"code\":\"[j����]\"},{\"rid\":\"831\",\"groupname\":\"С�ۼ�\",\"code\":\"[j����]\"},{\"rid\":\"832\",\"groupname\":\"С�ۼ�\",\"code\":\"[j��Ѫ]\"},{\"rid\":\"833\",\"groupname\":\"С�ۼ�\",\"code\":\"[j��Ц]\"},{\"rid\":\"834\",\"groupname\":\"С�ۼ�\",\"code\":\"[j��Ҿ]\"},{\"rid\":\"835\",\"groupname\":\"С�ۼ�\",\"code\":\"[j��ѽ]\"},{\"rid\":\"836\",\"groupname\":\"С�ۼ�\",\"code\":\"[j����]\"},{\"rid\":\"837\",\"groupname\":\"С�ۼ�\",\"code\":\"[jŤ���]\"},{\"rid\":\"838\",\"groupname\":\"С�ۼ�\",\"code\":\"[j����]\"},{\"rid\":\"839\",\"groupname\":\"С�ۼ�\",\"code\":\"[j����]\"},{\"rid\":\"840\",\"groupname\":\"С�ۼ�\",\"code\":\"[j��]\"},{\"rid\":\"841\",\"groupname\":\"С�ۼ�\",\"code\":\"[xyj��������]\"},{\"rid\":\"842\",\"groupname\":\"С�ۼ�\",\"code\":\"[xyj���]\"},{\"rid\":\"843\",\"groupname\":\"С�ۼ�\",\"code\":\"[xyj����]\"},{\"rid\":\"844\",\"groupname\":\"С�ۼ�\",\"code\":\"[ץɳ��]\"},{\"rid\":\"845\",\"groupname\":\"С�ۼ�\",\"code\":\"[��]\"},{\"rid\":\"846\",\"groupname\":\"С�ۼ�\",\"code\":\"[����]\"},{\"rid\":\"847\",\"groupname\":\"С�ۼ�\",\"code\":\"[Ϲ��]\"},{\"rid\":\"848\",\"groupname\":\"С�ۼ�\",\"code\":\"[Ϊ��]\"},{\"rid\":\"849\",\"groupname\":\"С�ۼ�\",\"code\":\"[��]\"},{\"rid\":\"850\",\"groupname\":\"С�ۼ�\",\"code\":\"[����]\"},{\"rid\":\"851\",\"groupname\":\"С�ۼ�\",\"code\":\"[��]\"},{\"rid\":\"852\",\"groupname\":\"С�ۼ�\",\"code\":\"[�ϴ�]\"},{\"rid\":\"853\",\"groupname\":\"С�ۼ�\",\"code\":\"[�˯]\"},{\"rid\":\"854\",\"groupname\":\"С�ۼ�\",\"code\":\"[������]\"},{\"rid\":\"855\",\"groupname\":\"С�ۼ�\",\"code\":\"[���ȿ���]\"},{\"rid\":\"856\",\"groupname\":\"С�ۼ�\",\"code\":\"[��Ц]\"},{\"rid\":\"857\",\"groupname\":\"С�ۼ�\",\"code\":\"[����]\"},{\"rid\":\"858\",\"groupname\":\"С�ۼ�\",\"code\":\"[�õ���]\"},{\"rid\":\"859\",\"groupname\":\"С�ۼ�\",\"code\":\"[�尡]\"},{\"rid\":\"860\",\"groupname\":\"С�ۼ�\",\"code\":\"[������]\"},{\"rid\":\"861\",\"groupname\":\"С�ۼ�\",\"code\":\"[��Ҫ��]\"},{\"rid\":\"862\",\"groupname\":\"С�ۼ�\",\"code\":\"[�����]\"},{\"rid\":\"863\",\"groupname\":\"С�ۼ�\",\"code\":\"[����Ŷ]\"},{\"rid\":\"864\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"865\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"866\",\"groupname\":\"����\",\"code\":\"[ŭ��]\"},{\"rid\":\"867\",\"groupname\":\"����\",\"code\":\"[̫����]\"},{\"rid\":\"868\",\"groupname\":\"����\",\"code\":\"[��������]\"},{\"rid\":\"869\",\"groupname\":\"����\",\"code\":\"[���Ƿ]\"},{\"rid\":\"870\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"871\",\"groupname\":\"����\",\"code\":\"[�����]\"},{\"rid\":\"872\",\"groupname\":\"����\",\"code\":\"[ʧ��]\"},{\"rid\":\"873\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"874\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"875\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"876\",\"groupname\":\"����\",\"code\":\"[ί��]\"},{\"rid\":\"877\",\"groupname\":\"����\",\"code\":\"[˼��]\"},{\"rid\":\"878\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"879\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"880\",\"groupname\":\"����\",\"code\":\"[�Һߺ�]\"},{\"rid\":\"881\",\"groupname\":\"����\",\"code\":\"[��ߺ�]\"},{\"rid\":\"882\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"883\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"884\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"885\",\"groupname\":\"����\",\"code\":\"[Ǯ]\"},{\"rid\":\"886\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"887\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"888\",\"groupname\":\"����\",\"code\":\"[�ݰ�]\"},{\"rid\":\"889\",\"groupname\":\"����\",\"code\":\"[�Ծ�]\"},{\"rid\":\"890\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"891\",\"groupname\":\"����\",\"code\":\"[˥]\"},{\"rid\":\"892\",\"groupname\":\"����\",\"code\":\"[��ŭ]\"},{\"rid\":\"893\",\"groupname\":\"����\",\"code\":\"[��ð]\"},{\"rid\":\"894\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"895\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"896\",\"groupname\":\"����\",\"code\":\"[good]\"},{\"rid\":\"897\",\"groupname\":\"����\",\"code\":\"[haha]\"},{\"rid\":\"898\",\"groupname\":\"����\",\"code\":\"[��Ҫ]\"},{\"rid\":\"899\",\"groupname\":\"����\",\"code\":\"[ok]\"},{\"rid\":\"900\",\"groupname\":\"����\",\"code\":\"[ȭͷ]\"},{\"rid\":\"901\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"902\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"903\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"904\",\"groupname\":\"����\",\"code\":\"[Ү]\"},{\"rid\":\"905\",\"groupname\":\"����\",\"code\":\"[���]\"},{\"rid\":\"906\",\"groupname\":\"����\",\"code\":\"[�����]\"},{\"rid\":\"907\",\"groupname\":\"����\",\"code\":\"[�ɰ�]\"},{\"rid\":\"908\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"909\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"910\",\"groupname\":\"����\",\"code\":\"[�Ǻ�]\"},{\"rid\":\"911\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"912\",\"groupname\":\"����\",\"code\":\"[˯��]\"},{\"rid\":\"913\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"914\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"915\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"916\",\"groupname\":\"����\",\"code\":\"[�ڱ�ʺ]\"},{\"rid\":\"917\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"918\",\"groupname\":\"����\",\"code\":\"[͵Ц]\"},{\"rid\":\"919\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"920\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"921\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"922\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"923\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"924\",\"groupname\":\"����\",\"code\":\"[ץ��]\"},{\"rid\":\"925\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"926\",\"groupname\":\"����\",\"code\":\"[ŭ]\"},{\"rid\":\"927\",\"groupname\":\"����\",\"code\":\"[�ұ���]\"},{\"rid\":\"928\",\"groupname\":\"����\",\"code\":\"[�󱧱�]\"},{\"rid\":\"929\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc���տ���]\"},{\"rid\":\"930\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc����]\"},{\"rid\":\"931\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[mocתͷ]\"},{\"rid\":\"932\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[mocװ��]\"},{\"rid\":\"933\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[mocת��]\"},{\"rid\":\"934\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc�м�]\"},{\"rid\":\"935\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc��]\"},{\"rid\":\"936\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc��]\"},{\"rid\":\"937\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[mocΧ��]\"},{\"rid\":\"938\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc��]\"},{\"rid\":\"939\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc����]\"},{\"rid\":\"940\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[mocʯ��]\"},{\"rid\":\"941\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc����]\"},{\"rid\":\"942\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc����Ů]\"},{\"rid\":\"943\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc������]\"},{\"rid\":\"944\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc����]\"},{\"rid\":\"945\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[mocǿ��]\"},{\"rid\":\"946\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc����]\"},{\"rid\":\"947\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[mocŻ��]\"},{\"rid\":\"948\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[mocð��]\"},{\"rid\":\"949\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc·��]\"},{\"rid\":\"950\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc�����]\"},{\"rid\":\"951\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc���]\"},{\"rid\":\"952\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc��]\"},{\"rid\":\"953\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc����]\"},{\"rid\":\"954\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc����]\"},{\"rid\":\"955\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc����]\"},{\"rid\":\"956\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc��]\"},{\"rid\":\"957\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc���]\"},{\"rid\":\"958\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc��ڳ�]\"},{\"rid\":\"959\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc���]\"},{\"rid\":\"960\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc����Ц]\"},{\"rid\":\"961\",\"groupname\":\"Ħ˿Ħ˿\",\"code\":\"[moc����]\"},{\"rid\":\"962\",\"groupname\":\"��\",\"code\":\"[g˼��]\"},{\"rid\":\"963\",\"groupname\":\"��\",\"code\":\"[g��]\"},{\"rid\":\"964\",\"groupname\":\"��\",\"code\":\"[g��Ц]\"},{\"rid\":\"965\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"966\",\"groupname\":\"��\",\"code\":\"[g���]\"},{\"rid\":\"967\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"968\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"969\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"970\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"971\",\"groupname\":\"��\",\"code\":\"[g��ɪ]\"},{\"rid\":\"972\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"973\",\"groupname\":\"��\",\"code\":\"[g�޹�]\"},{\"rid\":\"974\",\"groupname\":\"��\",\"code\":\"[g��ü]\"},{\"rid\":\"975\",\"groupname\":\"��\",\"code\":\"[gī��1]\"},{\"rid\":\"976\",\"groupname\":\"��\",\"code\":\"[gī��2]\"},{\"rid\":\"977\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"978\",\"groupname\":\"��\",\"code\":\"[g��Ц]\"},{\"rid\":\"979\",\"groupname\":\"��\",\"code\":\"[g�۱�]\"},{\"rid\":\"980\",\"groupname\":\"��\",\"code\":\"[g�۱�2]\"},{\"rid\":\"981\",\"groupname\":\"��\",\"code\":\"[g�ٺ�]\"},{\"rid\":\"982\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"983\",\"groupname\":\"��\",\"code\":\"[g�׽�]\"},{\"rid\":\"984\",\"groupname\":\"��\",\"code\":\"[g��Ӱ]\"},{\"rid\":\"985\",\"groupname\":\"��\",\"code\":\"[g��Ѫ]\"},{\"rid\":\"986\",\"groupname\":\"��\",\"code\":\"[gŻ��]\"},{\"rid\":\"987\",\"groupname\":\"��\",\"code\":\"[g��Ѫ]\"},{\"rid\":\"988\",\"groupname\":\"��\",\"code\":\"[g���]\"},{\"rid\":\"989\",\"groupname\":\"��\",\"code\":\"[g����1]\"},{\"rid\":\"990\",\"groupname\":\"��\",\"code\":\"[gͷ��]\"},{\"rid\":\"991\",\"groupname\":\"��\",\"code\":\"[g����1]\"},{\"rid\":\"992\",\"groupname\":\"��\",\"code\":\"[g����2]\"},{\"rid\":\"993\",\"groupname\":\"��\",\"code\":\"[g�޺�]\"},{\"rid\":\"994\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"995\",\"groupname\":\"��\",\"code\":\"[g�к�]\"},{\"rid\":\"996\",\"groupname\":\"��\",\"code\":\"[g����2]\"},{\"rid\":\"997\",\"groupname\":\"��\",\"code\":\"[g����ν]\"},{\"rid\":\"998\",\"groupname\":\"��\",\"code\":\"[g�׻�]\"},{\"rid\":\"999\",\"groupname\":\"��\",\"code\":\"[gаЦ]\"},{\"rid\":\"1000\",\"groupname\":\"��\",\"code\":\"[g�㱼1]\"},{\"rid\":\"1001\",\"groupname\":\"��\",\"code\":\"[g�㱼2]\"},{\"rid\":\"1002\",\"groupname\":\"��\",\"code\":\"[g�㱼3]\"},{\"rid\":\"1003\",\"groupname\":\"��\",\"code\":\"[g�ٵ�]\"},{\"rid\":\"1004\",\"groupname\":\"��\",\"code\":\"[g�Ȳ�]\"},{\"rid\":\"1005\",\"groupname\":\"��\",\"code\":\"[gҡ��]\"},{\"rid\":\"1006\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"1007\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"1008\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"1009\",\"groupname\":\"��\",\"code\":\"[gί��]\"},{\"rid\":\"1010\",\"groupname\":\"��\",\"code\":\"[g��Ʈ]\"},{\"rid\":\"1011\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"1012\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"1013\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"1014\",\"groupname\":\"��\",\"code\":\"[g��]\"},{\"rid\":\"1015\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"1016\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"1017\",\"groupname\":\"��\",\"code\":\"[g��ͷ]\"},{\"rid\":\"1018\",\"groupname\":\"��\",\"code\":\"[g��ˮ]\"},{\"rid\":\"1019\",\"groupname\":\"��\",\"code\":\"[g����2]\"},{\"rid\":\"1020\",\"groupname\":\"��\",\"code\":\"[g����2]\"},{\"rid\":\"1021\",\"groupname\":\"��\",\"code\":\"[g����]\"},{\"rid\":\"1022\",\"groupname\":\"��èè\",\"code\":\"[lm�в�è]\"},{\"rid\":\"1023\",\"groupname\":\"��èè\",\"code\":\"[lm��Ц]\"},{\"rid\":\"1024\",\"groupname\":\"��èè\",\"code\":\"[lm����]\"},{\"rid\":\"1025\",\"groupname\":\"��èè\",\"code\":\"[lmС����]\"},{\"rid\":\"1026\",\"groupname\":\"��èè\",\"code\":\"[lm����]\"},{\"rid\":\"1027\",\"groupname\":\"��èè\",\"code\":\"[lm�ڱ�ʺ]\"},{\"rid\":\"1028\",\"groupname\":\"��èè\",\"code\":\"[lm��Ȼ��]\"},{\"rid\":\"1029\",\"groupname\":\"��èè\",\"code\":\"[lm������]\"},{\"rid\":\"1030\",\"groupname\":\"��èè\",\"code\":\"[lm�˿���]\"},{\"rid\":\"1031\",\"groupname\":\"��èè\",\"code\":\"[lm�ٲ���]\"},{\"rid\":\"1032\",\"groupname\":\"��èè\",\"code\":\"[lmĥ��]\"},{\"rid\":\"1033\",\"groupname\":\"��èè\",\"code\":\"[lmû����]\"},{\"rid\":\"1034\",\"groupname\":\"��èè\",\"code\":\"[lmû�°�]\"},{\"rid\":\"1035\",\"groupname\":\"��èè\",\"code\":\"[lmãȻ]\"},{\"rid\":\"1036\",\"groupname\":\"��èè\",\"code\":\"[lm��������]\"},{\"rid\":\"1037\",\"groupname\":\"��èè\",\"code\":\"[lm�庹]\"},{\"rid\":\"1038\",\"groupname\":\"��èè\",\"code\":\"[lm����]\"},{\"rid\":\"1039\",\"groupname\":\"��èè\",\"code\":\"[lm����]\"},{\"rid\":\"1040\",\"groupname\":\"��èè\",\"code\":\"[lm����]\"},{\"rid\":\"1041\",\"groupname\":\"��èè\",\"code\":\"[lm������]\"},{\"rid\":\"1042\",\"groupname\":\"��èè\",\"code\":\"[lm����]\"},{\"rid\":\"1043\",\"groupname\":\"��èè\",\"code\":\"[lm��ˮ]\"},{\"rid\":\"1044\",\"groupname\":\"��èè\",\"code\":\"[lm�ٺ�]\"},{\"rid\":\"1045\",\"groupname\":\"��èè\",\"code\":\"[lm������]\"},{\"rid\":\"1046\",\"groupname\":\"��èè\",\"code\":\"[lm��Ц]\"},{\"rid\":\"1047\",\"groupname\":\"��èè\",\"code\":\"[lm����]\"},{\"rid\":\"1048\",\"groupname\":\"��èè\",\"code\":\"[lm����]\"},{\"rid\":\"1049\",\"groupname\":\"��èè\",\"code\":\"[lm����]\"},{\"rid\":\"1050\",\"groupname\":\"��èè\",\"code\":\"[lm����]\"},{\"rid\":\"1051\",\"groupname\":\"��èè\",\"code\":\"[lm��ͷ]\"},{\"rid\":\"1052\",\"groupname\":\"��èè\",\"code\":\"[lm��ŭ]\"},{\"rid\":\"1053\",\"groupname\":\"��èè\",\"code\":\"[lm��ʧɫ]\"},{\"rid\":\"1054\",\"groupname\":\"��èè\",\"code\":\"[lm��Ц]\"},{\"rid\":\"1055\",\"groupname\":\"��èè\",\"code\":\"[lm�����]\"},{\"rid\":\"1056\",\"groupname\":\"��èè\",\"code\":\"[lm���]\"},{\"rid\":\"1057\",\"groupname\":\"��èè\",\"code\":\"[lm��]\"},{\"rid\":\"1058\",\"groupname\":\"��èè\",\"code\":\"[lm�����]\"},{\"rid\":\"1059\",\"groupname\":\"��èè\",\"code\":\"[lm���꺹]\"},{\"rid\":\"1060\",\"groupname\":\"��èè\",\"code\":\"[lm���ذ���]\"},{\"rid\":\"1061\",\"groupname\":\"��èè\",\"code\":\"[lm������]\"},{\"rid\":\"1062\",\"groupname\":\"��èè\",\"code\":\"[mk����]\"},{\"rid\":\"1063\",\"groupname\":\"��èè\",\"code\":\"[�浭��]\"},{\"rid\":\"1064\",\"groupname\":\"��èè\",\"code\":\"[������]\"},{\"rid\":\"1065\",\"groupname\":\"��èè\",\"code\":\"[��]\"},{\"rid\":\"1066\",\"groupname\":\"��èè\",\"code\":\"[һͷ����]\"},{\"rid\":\"1067\",\"groupname\":\"��èè\",\"code\":\"[�����۶�]\"},{\"rid\":\"1068\",\"groupname\":\"��èè\",\"code\":\"[Ц����]\"},{\"rid\":\"1069\",\"groupname\":\"��èè\",\"code\":\"[С����]\"},{\"rid\":\"1070\",\"groupname\":\"��èè\",\"code\":\"[�Ҵ���]\"},{\"rid\":\"1071\",\"groupname\":\"��èè\",\"code\":\"[ι]\"},{\"rid\":\"1072\",\"groupname\":\"��èè\",\"code\":\"[����ͷ]\"},{\"rid\":\"1073\",\"groupname\":\"��èè\",\"code\":\"[��Ȼ��]\"},{\"rid\":\"1074\",\"groupname\":\"��èè\",\"code\":\"[������]\"},{\"rid\":\"1075\",\"groupname\":\"��èè\",\"code\":\"[������]\"},{\"rid\":\"1076\",\"groupname\":\"��èè\",\"code\":\"[������]\"},{\"rid\":\"1077\",\"groupname\":\"��èè\",\"code\":\"[�̲���]\"},{\"rid\":\"1078\",\"groupname\":\"��èè\",\"code\":\"[�˿���]\"},{\"rid\":\"1079\",\"groupname\":\"��èè\",\"code\":\"[�ٲ���]\"},{\"rid\":\"1080\",\"groupname\":\"��èè\",\"code\":\"[��û�°�]\"},{\"rid\":\"1081\",\"groupname\":\"��èè\",\"code\":\"[��ţ����]\"},{\"rid\":\"1082\",\"groupname\":\"��èè\",\"code\":\"[û����]\"},{\"rid\":\"1083\",\"groupname\":\"��èè\",\"code\":\"[������]\"},{\"rid\":\"1084\",\"groupname\":\"��èè\",\"code\":\"[�庹]\"},{\"rid\":\"1085\",\"groupname\":\"��èè\",\"code\":\"[������]\"},{\"rid\":\"1086\",\"groupname\":\"��èè\",\"code\":\"[������]\"},{\"rid\":\"1087\",\"groupname\":\"��èè\",\"code\":\"[��������]\"},{\"rid\":\"1088\",\"groupname\":\"��èè\",\"code\":\"[�ٺٺ�]\"},{\"rid\":\"1089\",\"groupname\":\"��èè\",\"code\":\"[��������]\"},{\"rid\":\"1090\",\"groupname\":\"��èè\",\"code\":\"[��Ц��]\"},{\"rid\":\"1091\",\"groupname\":\"��èè\",\"code\":\"[������]\"},{\"rid\":\"1092\",\"groupname\":\"��èè\",\"code\":\"[����]\"},{\"rid\":\"1093\",\"groupname\":\"��èè\",\"code\":\"[��ŭ]\"},{\"rid\":\"1094\",\"groupname\":\"��èè\",\"code\":\"[��ʧɫ]\"},{\"rid\":\"1095\",\"groupname\":\"��èè\",\"code\":\"[����]\"},{\"rid\":\"1096\",\"groupname\":\"��èè\",\"code\":\"[�����]\"},{\"rid\":\"1097\",\"groupname\":\"��èè\",\"code\":\"[�����]\"},{\"rid\":\"1098\",\"groupname\":\"��èè\",\"code\":\"[���꺹]\"},{\"rid\":\"1099\",\"groupname\":\"��èè\",\"code\":\"[���ذ���]\"},{\"rid\":\"1100\",\"groupname\":\"��èè\",\"code\":\"[��]\"},{\"rid\":\"1101\",\"groupname\":\"��èè\",\"code\":\"[������]\"},{\"rid\":\"1102\",\"groupname\":\"�˶���\",\"code\":\"[bed����]\"},{\"rid\":\"1103\",\"groupname\":\"�˶���\",\"code\":\"[bed����]\"},{\"rid\":\"1104\",\"groupname\":\"�˶���\",\"code\":\"[bed��]\"},{\"rid\":\"1105\",\"groupname\":\"�˶���\",\"code\":\"[bed����]\"},{\"rid\":\"1106\",\"groupname\":\"�˶���\",\"code\":\"[bed����]\"},{\"rid\":\"1107\",\"groupname\":\"�˶���\",\"code\":\"[bed��������]\"},{\"rid\":\"1108\",\"groupname\":\"�˶���\",\"code\":\"[bed��ԡ]\"},{\"rid\":\"1109\",\"groupname\":\"�˶���\",\"code\":\"[bed����]\"},{\"rid\":\"1110\",\"groupname\":\"�˶���\",\"code\":\"[bedƤ]\"},{\"rid\":\"1111\",\"groupname\":\"�˶���\",\"code\":\"[bed����]\"},{\"rid\":\"1112\",\"groupname\":\"�˶���\",\"code\":\"[bed������]\"},{\"rid\":\"1113\",\"groupname\":\"�˶���\",\"code\":\"[bed������]\"},{\"rid\":\"1114\",\"groupname\":\"�˶���\",\"code\":\"[bedƮ��]\"},{\"rid\":\"1115\",\"groupname\":\"�˶���\",\"code\":\"[bed����]\"},{\"rid\":\"1116\",\"groupname\":\"�˶���\",\"code\":\"[bed�ٹ�]\"},{\"rid\":\"1117\",\"groupname\":\"�˶���\",\"code\":\"[bed̤��]\"},{\"rid\":\"1118\",\"groupname\":\"�˶���\",\"code\":\"[bed����]\"},{\"rid\":\"1119\",\"groupname\":\"�˶���\",\"code\":\"[bedתȦ]\"},{\"rid\":\"1120\",\"groupname\":\"�˶���\",\"code\":\"[bed����]\"},{\"rid\":\"1121\",\"groupname\":\"�˶���\",\"code\":\"[bed��]\"},{\"rid\":\"1122\",\"groupname\":\"�˶���\",\"code\":\"[bed����]\"},{\"rid\":\"1123\",\"groupname\":\"�˶���\",\"code\":\"[bed����]\"},{\"rid\":\"1124\",\"groupname\":\"�˶���\",\"code\":\"[bed����]\"},{\"rid\":\"1125\",\"groupname\":\"�˶���\",\"code\":\"[bed�ñ�]\"},{\"rid\":\"1126\",\"groupname\":\"�˶���\",\"code\":\"[bed��]\"},{\"rid\":\"1127\",\"groupname\":\"�˶���\",\"code\":\"[bed�˷�]\"},{\"rid\":\"1128\",\"groupname\":\"�˶���\",\"code\":\"[brd��]\"},{\"rid\":\"1129\",\"groupname\":\"�˶���\",\"code\":\"[brd��]\"},{\"rid\":\"1130\",\"groupname\":\"�˶���\",\"code\":\"[brd����]\"},{\"rid\":\"1131\",\"groupname\":\"�˶���\",\"code\":\"[brd��]\"},{\"rid\":\"1132\",\"groupname\":\"�˶���\",\"code\":\"[brd��]\"},{\"rid\":\"1133\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"1134\",\"groupname\":\"����\",\"code\":\"[̨��]\"},{\"rid\":\"1135\",\"groupname\":\"����\",\"code\":\"[ɳ����]\"},{\"rid\":\"1136\",\"groupname\":\"����\",\"code\":\"[��ת����]\"},{\"rid\":\"1137\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1138\",\"groupname\":\"����\",\"code\":\"[�����]\"},{\"rid\":\"1139\",\"groupname\":\"����\",\"code\":\"[��ˮ]\"},{\"rid\":\"1140\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"1141\",\"groupname\":\"����\",\"code\":\"[����ת��]\"},{\"rid\":\"1142\",\"groupname\":\"����\",\"code\":\"[�ʺ�]\"},{\"rid\":\"1143\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1144\",\"groupname\":\"����\",\"code\":\"[΢��]\"},{\"rid\":\"1145\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1146\",\"groupname\":\"����\",\"code\":\"[ѩ]\"},{\"rid\":\"1147\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1148\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1149\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1150\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1151\",\"groupname\":\"����\",\"code\":\"[�ú����]\"},{\"rid\":\"1152\",\"groupname\":\"����\",\"code\":\"[Χ��]\"},{\"rid\":\"1153\",\"groupname\":\"����\",\"code\":\"[��ůñ��]\"},{\"rid\":\"1154\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1155\",\"groupname\":\"����\",\"code\":\"[���]\"},{\"rid\":\"1156\",\"groupname\":\"����\",\"code\":\"[ϲ]\"},{\"rid\":\"1157\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1158\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1159\",\"groupname\":\"����\",\"code\":\"[���]\"},{\"rid\":\"1160\",\"groupname\":\"����\",\"code\":\"[��ʯ]\"},{\"rid\":\"1161\",\"groupname\":\"����\",\"code\":\"[���]\"},{\"rid\":\"1162\",\"groupname\":\"����\",\"code\":\"[�ɻ�]\"},{\"rid\":\"1163\",\"groupname\":\"����\",\"code\":\"[���г�]\"},{\"rid\":\"1164\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1165\",\"groupname\":\"����\",\"code\":\"[�ֻ�]\"},{\"rid\":\"1166\",\"groupname\":\"����\",\"code\":\"[�����]\"},{\"rid\":\"1167\",\"groupname\":\"����\",\"code\":\"[ҩ]\"},{\"rid\":\"1168\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1169\",\"groupname\":\"����\",\"code\":\"[��ֽ]\"},{\"rid\":\"1170\",\"groupname\":\"����\",\"code\":\"[��Ҷ]\"},{\"rid\":\"1171\",\"groupname\":\"����\",\"code\":\"[ʥ����]\"},{\"rid\":\"1172\",\"groupname\":\"����\",\"code\":\"[ʥ��ñ]\"},{\"rid\":\"1173\",\"groupname\":\"����\",\"code\":\"[ʥ������]\"},{\"rid\":\"1174\",\"groupname\":\"����\",\"code\":\"[ʥ������]\"},{\"rid\":\"1175\",\"groupname\":\"����\",\"code\":\"[ʥ����]\"},{\"rid\":\"1176\",\"groupname\":\"����\",\"code\":\"[bh�뺷]\"},{\"rid\":\"1177\",\"groupname\":\"����\",\"code\":\"[offthewall]\"},{\"rid\":\"1178\",\"groupname\":\"����\",\"code\":\"[����ɽ��]\"},{\"rid\":\"1179\",\"groupname\":\"����\",\"code\":\"[�����㶫]\"},{\"rid\":\"1180\",\"groupname\":\"����\",\"code\":\"[K������]\"},{\"rid\":\"1181\",\"groupname\":\"����\",\"code\":\"[�����ɶ�]\"},{\"rid\":\"1182\",\"groupname\":\"����\",\"code\":\"[������]\"},{\"rid\":\"1183\",\"groupname\":\"����\",\"code\":\"[΢��������]\"},{\"rid\":\"1184\",\"groupname\":\"����\",\"code\":\"[��С��]\"},{\"rid\":\"1185\",\"groupname\":\"����\",\"code\":\"[����һ����]\"},{\"rid\":\"1186\",\"groupname\":\"����\",\"code\":\"[�׶ذ»�]\"},{\"rid\":\"1187\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1188\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1189\",\"groupname\":\"����\",\"code\":\"[ѱ¹]\"},{\"rid\":\"1190\",\"groupname\":\"����\",\"code\":\"[�Ϻ�־Ը��]\"},{\"rid\":\"1191\",\"groupname\":\"����\",\"code\":\"[���ֺ�]\"},{\"rid\":\"1192\",\"groupname\":\"����\",\"code\":\"[�׷�]\"},{\"rid\":\"1193\",\"groupname\":\"����\",\"code\":\"[�����ǲ�˹]\"},{\"rid\":\"1194\",\"groupname\":\"����\",\"code\":\"[iPhone]\"},{\"rid\":\"1195\",\"groupname\":\"����\",\"code\":\"[΢������]\"},{\"rid\":\"1196\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1197\",\"groupname\":\"����\",\"code\":\"[����ܰ]\"},{\"rid\":\"1198\",\"groupname\":\"����\",\"code\":\"[ͼƬ]\"},{\"rid\":\"1199\",\"groupname\":\"����\",\"code\":\"[ֲ����]\"},{\"rid\":\"1200\",\"groupname\":\"����\",\"code\":\"[�۵���]\"},{\"rid\":\"1201\",\"groupname\":\"����\",\"code\":\"[�ǹ�]\"},{\"rid\":\"1202\",\"groupname\":\"����\",\"code\":\"[��ʥ��]\"},{\"rid\":\"1203\",\"groupname\":\"����\",\"code\":\"[���]\"},{\"rid\":\"1204\",\"groupname\":\"����\",\"code\":\"[�ƺ�]\"},{\"rid\":\"1205\",\"groupname\":\"����\",\"code\":\"[�±�]\"},{\"rid\":\"1206\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1207\",\"groupname\":\"����\",\"code\":\"[�ڰ�]\"},{\"rid\":\"1208\",\"groupname\":\"����\",\"code\":\"[�ɿ���]\"},{\"rid\":\"1209\",\"groupname\":\"����\",\"code\":\"[��ӡ]\"},{\"rid\":\"1210\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"1211\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"1212\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1213\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1214\",\"groupname\":\"����\",\"code\":\"[Ҷ��]\"},{\"rid\":\"1215\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1216\",\"groupname\":\"����\",\"code\":\"[ӡ��]\"},{\"rid\":\"1217\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"1218\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"1219\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1220\",\"groupname\":\"����\",\"code\":\"[��ɡ]\"},{\"rid\":\"1221\",\"groupname\":\"����\",\"code\":\"[���ӻ�]\"},{\"rid\":\"1222\",\"groupname\":\"����\",\"code\":\"[�绰]\"},{\"rid\":\"1223\",\"groupname\":\"����\",\"code\":\"[̫��]\"},{\"rid\":\"1224\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"1225\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1226\",\"groupname\":\"����\",\"code\":\"[��Ͳ]\"},{\"rid\":\"1227\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1228\",\"groupname\":\"����\",\"code\":\"[��Ӱ]\"},{\"rid\":\"1229\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1230\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1231\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1232\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1233\",\"groupname\":\"����\",\"code\":\"[ñ��]\"},{\"rid\":\"1234\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1235\",\"groupname\":\"����\",\"code\":\"[�ʻ�]\"},{\"rid\":\"1236\",\"groupname\":\"����\",\"code\":\"[��]\"},{\"rid\":\"1237\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1238\",\"groupname\":\"����\",\"code\":\"[�ɱ�]\"},{\"rid\":\"1239\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1240\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb���տ���]\"},{\"rid\":\"1241\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb���˽�]\"},{\"rid\":\"1242\",\"groupname\":\"���ڱ�\",\"code\":\"[ppbʥ��ʥ��]\"},{\"rid\":\"1243\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb������]\"},{\"rid\":\"1244\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb�簲]\"},{\"rid\":\"1245\",\"groupname\":\"���ڱ�\",\"code\":\"[ppbϴ��]\"},{\"rid\":\"1246\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb�ѹ�]\"},{\"rid\":\"1247\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb��Ѫ]\"},{\"rid\":\"1248\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb����]\"},{\"rid\":\"1249\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb����]\"},{\"rid\":\"1250\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb����]\"},{\"rid\":\"1251\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb·��]\"},{\"rid\":\"1252\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb����]\"},{\"rid\":\"1253\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb��Ц]\"},{\"rid\":\"1254\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb����]\"},{\"rid\":\"1255\",\"groupname\":\"���ڱ�\",\"code\":\"[ppbbibi]\"},{\"rid\":\"1256\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb��]\"},{\"rid\":\"1257\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb����]\"},{\"rid\":\"1258\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb��]\"},{\"rid\":\"1259\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb������]\"},{\"rid\":\"1260\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb��ʬ]\"},{\"rid\":\"1261\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb˦��]\"},{\"rid\":\"1262\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb��]\"},{\"rid\":\"1263\",\"groupname\":\"���ڱ�\",\"code\":\"[ppbȥ��]\"},{\"rid\":\"1264\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb��������]\"},{\"rid\":\"1265\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb��]\"},{\"rid\":\"1266\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb���]\"},{\"rid\":\"1267\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb�ҿ�]\"},{\"rid\":\"1268\",\"groupname\":\"���ڱ�\",\"code\":\"[ppbɨ��]\"},{\"rid\":\"1269\",\"groupname\":\"���ڱ�\",\"code\":\"[ppbɱ��]\"},{\"rid\":\"1270\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb����]\"},{\"rid\":\"1271\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb������]\"},{\"rid\":\"1272\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb��]\"},{\"rid\":\"1273\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb��ӭ��ӭ]\"},{\"rid\":\"1274\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb���]\"},{\"rid\":\"1275\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb����]\"},{\"rid\":\"1276\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb����Ӵ]\"},{\"rid\":\"1277\",\"groupname\":\"���ڱ�\",\"code\":\"[ppb����]\"},{\"rid\":\"1278\",\"groupname\":\"������\",\"code\":\"[����������Ĥ]\"},{\"rid\":\"1279\",\"groupname\":\"������\",\"code\":\"[����������]\"},{\"rid\":\"1280\",\"groupname\":\"������\",\"code\":\"[��������]\"},{\"rid\":\"1281\",\"groupname\":\"������\",\"code\":\"[������yes]\"},{\"rid\":\"1282\",\"groupname\":\"������\",\"code\":\"[����������]\"},{\"rid\":\"1283\",\"groupname\":\"������\",\"code\":\"[��������ѻ]\"},{\"rid\":\"1284\",\"groupname\":\"������\",\"code\":\"[����������]\"},{\"rid\":\"1285\",\"groupname\":\"������\",\"code\":\"[��������]\"},{\"rid\":\"1286\",\"groupname\":\"������\",\"code\":\"[���������տ���]\"},{\"rid\":\"1287\",\"groupname\":\"������\",\"code\":\"[����������]\"},{\"rid\":\"1288\",\"groupname\":\"������\",\"code\":\"[������no]\"},{\"rid\":\"1289\",\"groupname\":\"������\",\"code\":\"[������·��]\"},{\"rid\":\"1290\",\"groupname\":\"������\",\"code\":\"[����������]\"},{\"rid\":\"1291\",\"groupname\":\"������\",\"code\":\"[����������Ѫ]\"},{\"rid\":\"1292\",\"groupname\":\"������\",\"code\":\"[����������]\"},{\"rid\":\"1293\",\"groupname\":\"������\",\"code\":\"[�������ᱼ]\"},{\"rid\":\"1294\",\"groupname\":\"������\",\"code\":\"[�����ܿ���]\"},{\"rid\":\"1295\",\"groupname\":\"������\",\"code\":\"[�����ܿ���]\"},{\"rid\":\"1296\",\"groupname\":\"������\",\"code\":\"[�����ܿ�����]\"},{\"rid\":\"1297\",\"groupname\":\"������\",\"code\":\"[�����ܾ���]\"},{\"rid\":\"1298\",\"groupname\":\"������\",\"code\":\"[�����ܺ���]\"},{\"rid\":\"1299\",\"groupname\":\"������\",\"code\":\"[�����ܹ���]\"},{\"rid\":\"1300\",\"groupname\":\"������\",\"code\":\"[�����ܸж�]\"},{\"rid\":\"1301\",\"groupname\":\"������\",\"code\":\"[�����ܸ���]\"},{\"rid\":\"1302\",\"groupname\":\"������\",\"code\":\"[�����ܷ���]\"},{\"rid\":\"1303\",\"groupname\":\"������\",\"code\":\"[�����ܴ��к�]\"},{\"rid\":\"1304\",\"groupname\":\"������\",\"code\":\"[�����ܲ�ױ]\"},{\"rid\":\"1305\",\"groupname\":\"������\",\"code\":\"[�����ܱ���]\"},{\"rid\":\"1306\",\"groupname\":\"���\",\"code\":\"[֯]\"},{\"rid\":\"1307\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1308\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1309\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1310\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1311\",\"groupname\":\"���\",\"code\":\"[��]\"},{\"rid\":\"1312\",\"groupname\":\"���\",\"code\":\"[��è]\"},{\"rid\":\"1313\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1314\",\"groupname\":\"���\",\"code\":\"[Χ��]\"},{\"rid\":\"1315\",\"groupname\":\"���\",\"code\":\"[�Ӽ���]\"},{\"rid\":\"1316\",\"groupname\":\"���\",\"code\":\"[������]\"},{\"rid\":\"1317\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1318\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1319\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1320\",\"groupname\":\"���\",\"code\":\"[��]\"},{\"rid\":\"1321\",\"groupname\":\"���\",\"code\":\"[orz]\"},{\"rid\":\"1322\",\"groupname\":\"���\",\"code\":\"[լ]\"},{\"rid\":\"1323\",\"groupname\":\"���\",\"code\":\"[˧]\"},{\"rid\":\"1324\",\"groupname\":\"���\",\"code\":\"[��ͷ]\"},{\"rid\":\"1325\",\"groupname\":\"���\",\"code\":\"[ʵϰ]\"},{\"rid\":\"1326\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1327\",\"groupname\":\"���\",\"code\":\"[���]\"},{\"rid\":\"1328\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1329\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1330\",\"groupname\":\"���\",\"code\":\"[���軨]\"},{\"rid\":\"1331\",\"groupname\":\"���\",\"code\":\"[��]\"},{\"rid\":\"1332\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1333\",\"groupname\":\"���\",\"code\":\"[̾��]\"},{\"rid\":\"1334\",\"groupname\":\"���\",\"code\":\"[�ʺ�]\"},{\"rid\":\"1335\",\"groupname\":\"���\",\"code\":\"[���]\"},{\"rid\":\"1336\",\"groupname\":\"���\",\"code\":\"[��]\"},{\"rid\":\"1337\",\"groupname\":\"���\",\"code\":\"[����]\"},{\"rid\":\"1338\",\"groupname\":\"���\",\"code\":\"[���]\"},{\"rid\":\"1339\",\"groupname\":\"���\",\"code\":\"[��ף]\"},{\"rid\":\"1340\",\"groupname\":\"���\",\"code\":\"[��]\"},{\"rid\":\"1341\",\"groupname\":\"���\",\"code\":\"[1]\"},{\"rid\":\"1342\",\"groupname\":\"���\",\"code\":\"[2]\"},{\"rid\":\"1343\",\"groupname\":\"���\",\"code\":\"[3]\"},{\"rid\":\"1344\",\"groupname\":\"���\",\"code\":\"[4]\"},{\"rid\":\"1345\",\"groupname\":\"���\",\"code\":\"[5]\"},{\"rid\":\"1346\",\"groupname\":\"���\",\"code\":\"[6]\"},{\"rid\":\"1347\",\"groupname\":\"���\",\"code\":\"[7]\"},{\"rid\":\"1348\",\"groupname\":\"���\",\"code\":\"[8]\"},{\"rid\":\"1349\",\"groupname\":\"���\",\"code\":\"[9]\"},{\"rid\":\"1350\",\"groupname\":\"���\",\"code\":\"[a]\"},{\"rid\":\"1351\",\"groupname\":\"���\",\"code\":\"[b]\"},{\"rid\":\"1352\",\"groupname\":\"���\",\"code\":\"[c]\"},{\"rid\":\"1353\",\"groupname\":\"���\",\"code\":\"[d]\"},{\"rid\":\"1354\",\"groupname\":\"���\",\"code\":\"[e]\"},{\"rid\":\"1355\",\"groupname\":\"���\",\"code\":\"[f]\"},{\"rid\":\"1356\",\"groupname\":\"���\",\"code\":\"[g]\"},{\"rid\":\"1357\",\"groupname\":\"���\",\"code\":\"[h]\"},{\"rid\":\"1358\",\"groupname\":\"���\",\"code\":\"[i]\"},{\"rid\":\"1359\",\"groupname\":\"���\",\"code\":\"[j]\"},{\"rid\":\"1360\",\"groupname\":\"���\",\"code\":\"[k]\"},{\"rid\":\"1361\",\"groupname\":\"���\",\"code\":\"[l]\"},{\"rid\":\"1362\",\"groupname\":\"���\",\"code\":\"[m]\"},{\"rid\":\"1363\",\"groupname\":\"���\",\"code\":\"[n]\"},{\"rid\":\"1364\",\"groupname\":\"���\",\"code\":\"[o]\"},{\"rid\":\"1365\",\"groupname\":\"���\",\"code\":\"[p]\"},{\"rid\":\"1366\",\"groupname\":\"���\",\"code\":\"[q]\"},{\"rid\":\"1367\",\"groupname\":\"���\",\"code\":\"[r]\"},{\"rid\":\"1368\",\"groupname\":\"���\",\"code\":\"[s]\"},{\"rid\":\"1369\",\"groupname\":\"���\",\"code\":\"[t]\"},{\"rid\":\"1370\",\"groupname\":\"���\",\"code\":\"[u]\"},{\"rid\":\"1371\",\"groupname\":\"���\",\"code\":\"[v]\"},{\"rid\":\"1372\",\"groupname\":\"���\",\"code\":\"[w]\"},{\"rid\":\"1373\",\"groupname\":\"���\",\"code\":\"[x]\"},{\"rid\":\"1374\",\"groupname\":\"���\",\"code\":\"[y]\"},{\"rid\":\"1375\",\"groupname\":\"���\",\"code\":\"[z]\"},{\"rid\":\"1376\",\"groupname\":\"���\",\"code\":\"[��]\"},{\"rid\":\"1377\",\"groupname\":\"���\",\"code\":\"[Բ]\"},{\"rid\":\"1378\",\"groupname\":\"���\",\"code\":\"[�к���]\"},{\"rid\":\"1379\",\"groupname\":\"���\",\"code\":\"[Ů����]\"},{\"rid\":\"1380\",\"groupname\":\"���\",\"code\":\"[������]\"},{\"rid\":\"1381\",\"groupname\":\"���\",\"code\":\"[��]\"},{\"rid\":\"1382\",\"groupname\":\"���\",\"code\":\"[Ѽ��]\"},{\"rid\":\"1383\",\"groupname\":\"���\",\"code\":\"[ʡ�Ժ�]\"},{\"rid\":\"1384\",\"groupname\":\"���\",\"code\":\"[kiss]\"},{\"rid\":\"1385\",\"groupname\":\"���\",\"code\":\"[ѩ��]\"},{\"rid\":\"1386\",\"groupname\":\"���\",\"code\":\"[С��]\"},{\"rid\":\"1387\",\"groupname\":\"��è\",\"code\":\"[km�ʺ�]\"},{\"rid\":\"1388\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1389\",\"groupname\":\"��è\",\"code\":\"[km�׿���ת]\"},{\"rid\":\"1390\",\"groupname\":\"��è\",\"code\":\"[km�ڿ���ת]\"},{\"rid\":\"1391\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1392\",\"groupname\":\"��è\",\"code\":\"[km�ɰ�]\"},{\"rid\":\"1393\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1394\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1395\",\"groupname\":\"��è\",\"code\":\"[km���װ׿�]\"},{\"rid\":\"1396\",\"groupname\":\"��è\",\"code\":\"[km���׺ڿ�]\"},{\"rid\":\"1397\",\"groupname\":\"��è\",\"code\":\"[km�ڱ�ʺ]\"},{\"rid\":\"1398\",\"groupname\":\"��è\",\"code\":\"[km���ۿ�]\"},{\"rid\":\"1399\",\"groupname\":\"��è\",\"code\":\"[kmΧ��]\"},{\"rid\":\"1400\",\"groupname\":\"��è\",\"code\":\"[kmί��]\"},{\"rid\":\"1401\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1402\",\"groupname\":\"��è\",\"code\":\"[kmFL]\"},{\"rid\":\"1403\",\"groupname\":\"��è\",\"code\":\"[km��̽]\"},{\"rid\":\"1404\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1405\",\"groupname\":\"��è\",\"code\":\"[km����1]\"},{\"rid\":\"1406\",\"groupname\":\"��è\",\"code\":\"[km��Ц]\"},{\"rid\":\"1407\",\"groupname\":\"��è\",\"code\":\"[km�ʼ�]\"},{\"rid\":\"1408\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1409\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1410\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1411\",\"groupname\":\"��è\",\"code\":\"[km�ڿ鲻����]\"},{\"rid\":\"1412\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1413\",\"groupname\":\"��è\",\"code\":\"[km����88]\"},{\"rid\":\"1414\",\"groupname\":\"��è\",\"code\":\"[km͸��]\"},{\"rid\":\"1415\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1416\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1417\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1418\",\"groupname\":\"��è\",\"code\":\"[km��ɯ2]\"},{\"rid\":\"1419\",\"groupname\":\"��è\",\"code\":\"[km��2]\"},{\"rid\":\"1420\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1421\",\"groupname\":\"��è\",\"code\":\"[km�ж�]\"},{\"rid\":\"1422\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1423\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1424\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1425\",\"groupname\":\"��è\",\"code\":\"[km�ۼ�è]\"},{\"rid\":\"1426\",\"groupname\":\"��è\",\"code\":\"[km�ڻ�Ц]\"},{\"rid\":\"1427\",\"groupname\":\"��è\",\"code\":\"[km��è]\"},{\"rid\":\"1428\",\"groupname\":\"��è\",\"code\":\"[km�ó�]\"},{\"rid\":\"1429\",\"groupname\":\"��è\",\"code\":\"[kmAI]\"},{\"rid\":\"1430\",\"groupname\":\"��è\",\"code\":\"[km�ڻ���߶]\"},{\"rid\":\"1431\",\"groupname\":\"��è\",\"code\":\"[km�óԾ�]\"},{\"rid\":\"1432\",\"groupname\":\"��è\",\"code\":\"[km��߶]\"},{\"rid\":\"1433\",\"groupname\":\"��è\",\"code\":\"[km�۾�]\"},{\"rid\":\"1434\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1435\",\"groupname\":\"��è\",\"code\":\"[kmV]\"},{\"rid\":\"1436\",\"groupname\":\"��è\",\"code\":\"[km������]\"},{\"rid\":\"1437\",\"groupname\":\"��è\",\"code\":\"[km��Ѫ1]\"},{\"rid\":\"1438\",\"groupname\":\"��è\",\"code\":\"[km�ö�]\"},{\"rid\":\"1439\",\"groupname\":\"��è\",\"code\":\"[km�ϴ�]\"},{\"rid\":\"1440\",\"groupname\":\"��è\",\"code\":\"[km�ڻ�]\"},{\"rid\":\"1441\",\"groupname\":\"��è\",\"code\":\"[km��Ѫ]\"},{\"rid\":\"1442\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1443\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1444\",\"groupname\":\"��è\",\"code\":\"[km���]\"},{\"rid\":\"1445\",\"groupname\":\"��è\",\"code\":\"[kmϲ]\"},{\"rid\":\"1446\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1447\",\"groupname\":\"��è\",\"code\":\"[kmŭ]\"},{\"rid\":\"1448\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1449\",\"groupname\":\"��è\",\"code\":\"[kmDW]\"},{\"rid\":\"1450\",\"groupname\":\"��è\",\"code\":\"[km��Ѫ��]\"},{\"rid\":\"1451\",\"groupname\":\"��è\",\"code\":\"[kmPS]\"},{\"rid\":\"1452\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1453\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1454\",\"groupname\":\"��è\",\"code\":\"[kmX]\"},{\"rid\":\"1455\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1456\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1457\",\"groupname\":\"��è\",\"code\":\"[km��ɯ]\"},{\"rid\":\"1458\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1459\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1460\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1461\",\"groupname\":\"��è\",\"code\":\"[km��]\"},{\"rid\":\"1462\",\"groupname\":\"��è\",\"code\":\"[kmè]\"},{\"rid\":\"1463\",\"groupname\":\"��è\",\"code\":\"[km����]\"},{\"rid\":\"1464\",\"groupname\":\"�ط�\",\"code\":\"[bofu����ͷ]\"},{\"rid\":\"1465\",\"groupname\":\"�ط�\",\"code\":\"[bofu����]\"},{\"rid\":\"1466\",\"groupname\":\"�ط�\",\"code\":\"[bofu��Ц]\"},{\"rid\":\"1467\",\"groupname\":\"�ط�\",\"code\":\"[bofuѹ��ɽ��]\"},{\"rid\":\"1468\",\"groupname\":\"�ط�\",\"code\":\"[bofu�Ļ�����]\"},{\"rid\":\"1469\",\"groupname\":\"�ط�\",\"code\":\"[bofu�Ķ�]\"},{\"rid\":\"1470\",\"groupname\":\"�ط�\",\"code\":\"[bofu�̵�����]\"},{\"rid\":\"1471\",\"groupname\":\"�ط�\",\"code\":\"[bofuʳ��]\"},{\"rid\":\"1472\",\"groupname\":\"�ط�\",\"code\":\"[bofuƱ�ӿ���]\"},{\"rid\":\"1473\",\"groupname\":\"�ط�\",\"code\":\"[bofuŭ]\"},{\"rid\":\"1474\",\"groupname\":\"�ط�\",\"code\":\"[bofuŤ]\"},{\"rid\":\"1475\",\"groupname\":\"�ط�\",\"code\":\"[bofu����]\"},{\"rid\":\"1476\",\"groupname\":\"�ط�\",\"code\":\"[bofu��]\"},{\"rid\":\"1477\",\"groupname\":\"�ط�\",\"code\":\"[bofu������]\"},{\"rid\":\"1478\",\"groupname\":\"�ط�\",\"code\":\"[bofu����]\"},{\"rid\":\"1479\",\"groupname\":\"�ط�\",\"code\":\"[bofu����ͼǿ]\"},{\"rid\":\"1480\",\"groupname\":\"�ط�\",\"code\":\"[bofu��ɧ]\"},{\"rid\":\"1481\",\"groupname\":\"�ط�\",\"code\":\"[bofu��ɪ]\"},{\"rid\":\"1482\",\"groupname\":\"�ط�\",\"code\":\"[bofu��ɻ�]\"},{\"rid\":\"1483\",\"groupname\":\"�ط�\",\"code\":\"[bofu����]\"},{\"rid\":\"1484\",\"groupname\":\"�ط�\",\"code\":\"[bofu�ļ�]\"},{\"rid\":\"1485\",\"groupname\":\"�ط�\",\"code\":\"[bofu����]\"},{\"rid\":\"1486\",\"groupname\":\"����\",\"code\":\"[����������]\"},{\"rid\":\"1487\",\"groupname\":\"����\",\"code\":\"[���ȴ��]\"},{\"rid\":\"1488\",\"groupname\":\"����\",\"code\":\"[����˦ñ]\"},{\"rid\":\"1489\",\"groupname\":\"����\",\"code\":\"[����ˤƿ]\"},{\"rid\":\"1490\",\"groupname\":\"����\",\"code\":\"[����Ťƨ��]\"},{\"rid\":\"1491\",\"groupname\":\"����\",\"code\":\"[���Ⱦ���]\"},{\"rid\":\"1492\",\"groupname\":\"����\",\"code\":\"[����������]\"},{\"rid\":\"1493\",\"groupname\":\"����\",\"code\":\"[����͵��]\"},{\"rid\":\"1494\",\"groupname\":\"����\",\"code\":\"[���ȱ���]\"},{\"rid\":\"1495\",\"groupname\":\"����\",\"code\":\"[���ȹ�Ƿ]\"},{\"rid\":\"1496\",\"groupname\":\"����\",\"code\":\"[����ʯ��]\"},{\"rid\":\"1497\",\"groupname\":\"����\",\"code\":\"[�����ù�]\"},{\"rid\":\"1498\",\"groupname\":\"����\",\"code\":\"[����̾��]\"},{\"rid\":\"1499\",\"groupname\":\"����\",\"code\":\"[���ȴ���Ц]\"},{\"rid\":\"1500\",\"groupname\":\"����\",\"code\":\"[��������]\"},{\"rid\":\"1501\",\"groupname\":\"����\",\"code\":\"[��������]\"},{\"rid\":\"1502\",\"groupname\":\"����\",\"code\":\"[���ȿٱ�]\"},{\"rid\":\"1503\",\"groupname\":\"����\",\"code\":\"[�����ᱼ]\"},{\"rid\":\"1504\",\"groupname\":\"����\",\"code\":\"[�����׻�]\"},{\"rid\":\"1505\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1506\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1507\",\"groupname\":\"�ܲ���\",\"code\":\"[�ܲ��Ű�]\"},{\"rid\":\"1508\",\"groupname\":\"�ܲ���\",\"code\":\"[��]\"},{\"rid\":\"1509\",\"groupname\":\"�ܲ���\",\"code\":\"[�˲���]\"},{\"rid\":\"1510\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz�洩Խ]\"},{\"rid\":\"1511\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz��˯��]\"},{\"rid\":\"1512\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz����]\"},{\"rid\":\"1513\",\"groupname\":\"�ܲ���\",\"code\":\"[gbzί��]\"},{\"rid\":\"1514\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz����]\"},{\"rid\":\"1515\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz��]\"},{\"rid\":\"1516\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz����]\"},{\"rid\":\"1517\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz��Ц]\"},{\"rid\":\"1518\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz����]\"},{\"rid\":\"1519\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz��]\"},{\"rid\":\"1520\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz�Ӱ�]\"},{\"rid\":\"1521\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz�Ӱ���]\"},{\"rid\":\"1522\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz��]\"},{\"rid\":\"1523\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz����]\"},{\"rid\":\"1524\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz����]\"},{\"rid\":\"1525\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz��Ц]\"},{\"rid\":\"1526\",\"groupname\":\"�ܲ���\",\"code\":\"[gbz��Խ��]\"},{\"rid\":\"1527\",\"groupname\":\"�ܲ���\",\"code\":\"[�е���]\"},{\"rid\":\"1528\",\"groupname\":\"�ܲ���\",\"code\":\"[yes]\"},{\"rid\":\"1529\",\"groupname\":\"�ܲ���\",\"code\":\"[�ʻ�ȥ��]\"},{\"rid\":\"1530\",\"groupname\":\"�ܲ���\",\"code\":\"[Ѽ��ܴ�]\"},{\"rid\":\"1531\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1532\",\"groupname\":\"�ܲ���\",\"code\":\"[ϲ����]\"},{\"rid\":\"1533\",\"groupname\":\"�ܲ���\",\"code\":\"[С��ƨ]\"},{\"rid\":\"1534\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1535\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1536\",\"groupname\":\"�ܲ���\",\"code\":\"[����ͷ]\"},{\"rid\":\"1537\",\"groupname\":\"�ܲ���\",\"code\":\"[ͷ��]\"},{\"rid\":\"1538\",\"groupname\":\"�ܲ���\",\"code\":\"[������]\"},{\"rid\":\"1539\",\"groupname\":\"�ܲ���\",\"code\":\"[˯���]\"},{\"rid\":\"1540\",\"groupname\":\"�ܲ���\",\"code\":\"[������]\"},{\"rid\":\"1541\",\"groupname\":\"�ܲ���\",\"code\":\"[������]\"},{\"rid\":\"1542\",\"groupname\":\"�ܲ���\",\"code\":\"[������]\"},{\"rid\":\"1543\",\"groupname\":\"�ܲ���\",\"code\":\"[������]\"},{\"rid\":\"1544\",\"groupname\":\"�ܲ���\",\"code\":\"[������]\"},{\"rid\":\"1545\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1546\",\"groupname\":\"�ܲ���\",\"code\":\"[��ͷ]\"},{\"rid\":\"1547\",\"groupname\":\"�ܲ���\",\"code\":\"[������]\"},{\"rid\":\"1548\",\"groupname\":\"�ܲ���\",\"code\":\"[�����Ա�]\"},{\"rid\":\"1549\",\"groupname\":\"�ܲ���\",\"code\":\"[����Ů]\"},{\"rid\":\"1550\",\"groupname\":\"�ܲ���\",\"code\":\"[������]\"},{\"rid\":\"1551\",\"groupname\":\"�ܲ���\",\"code\":\"[��]\"},{\"rid\":\"1552\",\"groupname\":\"�ܲ���\",\"code\":\"[������]\"},{\"rid\":\"1553\",\"groupname\":\"�ܲ���\",\"code\":\"[��ϲ]\"},{\"rid\":\"1554\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1555\",\"groupname\":\"�ܲ���\",\"code\":\"[���ܲ�]\"},{\"rid\":\"1556\",\"groupname\":\"�ܲ���\",\"code\":\"[����ȥ��]\"},{\"rid\":\"1557\",\"groupname\":\"�ܲ���\",\"code\":\"[��ð��]\"},{\"rid\":\"1558\",\"groupname\":\"�ܲ���\",\"code\":\"[ŭ��]\"},{\"rid\":\"1559\",\"groupname\":\"�ܲ���\",\"code\":\"[��Ҫ�ܶ�]\"},{\"rid\":\"1560\",\"groupname\":\"�ܲ���\",\"code\":\"[��ѿ]\"},{\"rid\":\"1561\",\"groupname\":\"�ܲ���\",\"code\":\"[��ů����]\"},{\"rid\":\"1562\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1563\",\"groupname\":\"�ܲ���\",\"code\":\"[��]\"},{\"rid\":\"1564\",\"groupname\":\"�ܲ���\",\"code\":\"[��]\"},{\"rid\":\"1565\",\"groupname\":\"�ܲ���\",\"code\":\"[�Բ�˫Ŀ]\"},{\"rid\":\"1566\",\"groupname\":\"�ܲ���\",\"code\":\"[��]\"},{\"rid\":\"1567\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1568\",\"groupname\":\"�ܲ���\",\"code\":\"[�ҳ�]\"},{\"rid\":\"1569\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1570\",\"groupname\":\"�ܲ���\",\"code\":\"[v5]\"},{\"rid\":\"1571\",\"groupname\":\"�ܲ���\",\"code\":\"[��Ϸ]\"},{\"rid\":\"1572\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1573\",\"groupname\":\"�ܲ���\",\"code\":\"[�ּ�]\"},{\"rid\":\"1574\",\"groupname\":\"�ܲ���\",\"code\":\"[ɫ]\"},{\"rid\":\"1575\",\"groupname\":\"�ܲ���\",\"code\":\"[��]\"},{\"rid\":\"1576\",\"groupname\":\"�ܲ���\",\"code\":\"[�㶮��]\"},{\"rid\":\"1577\",\"groupname\":\"�ܲ���\",\"code\":\"[��]\"},{\"rid\":\"1578\",\"groupname\":\"�ܲ���\",\"code\":\"[��ζ]\"},{\"rid\":\"1579\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1580\",\"groupname\":\"�ܲ���\",\"code\":\"[�ж�]\"},{\"rid\":\"1581\",\"groupname\":\"�ܲ���\",\"code\":\"[�ſ�]\"},{\"rid\":\"1582\",\"groupname\":\"�ܲ���\",\"code\":\"[�մ�]\"},{\"rid\":\"1583\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1584\",\"groupname\":\"�ܲ���\",\"code\":\"[��֪����]\"},{\"rid\":\"1585\",\"groupname\":\"�ܲ���\",\"code\":\"[����]\"},{\"rid\":\"1586\",\"groupname\":\"����\",\"code\":\"[cc���]\"},{\"rid\":\"1587\",\"groupname\":\"����\",\"code\":\"[cc�Ի�]\"},{\"rid\":\"1588\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1589\",\"groupname\":\"����\",\"code\":\"[cc��ү]\"},{\"rid\":\"1590\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1591\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1592\",\"groupname\":\"����\",\"code\":\"[cc����ι]\"},{\"rid\":\"1593\",\"groupname\":\"����\",\"code\":\"[cc��Ѫ]\"},{\"rid\":\"1594\",\"groupname\":\"����\",\"code\":\"[ccû��]\"},{\"rid\":\"1595\",\"groupname\":\"����\",\"code\":\"[cc�β�]\"},{\"rid\":\"1596\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1597\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1598\",\"groupname\":\"����\",\"code\":\"[ccί��]\"},{\"rid\":\"1599\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1600\",\"groupname\":\"����\",\"code\":\"[ccײǽ]\"},{\"rid\":\"1601\",\"groupname\":\"����\",\"code\":\"[cc��Խ]\"},{\"rid\":\"1602\",\"groupname\":\"����\",\"code\":\"[cc�ٺ�]\"},{\"rid\":\"1603\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1604\",\"groupname\":\"����\",\"code\":\"[cc���]\"},{\"rid\":\"1605\",\"groupname\":\"����\",\"code\":\"[ccˣ��]\"},{\"rid\":\"1606\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1607\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1608\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1609\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1610\",\"groupname\":\"����\",\"code\":\"[cc�趯]\"},{\"rid\":\"1611\",\"groupname\":\"����\",\"code\":\"[cc��Ǯ]\"},{\"rid\":\"1612\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1613\",\"groupname\":\"����\",\"code\":\"[cc˯��]\"},{\"rid\":\"1614\",\"groupname\":\"����\",\"code\":\"[cc��ʬ]\"},{\"rid\":\"1615\",\"groupname\":\"����\",\"code\":\"[cc�Ҳ�]\"},{\"rid\":\"1616\",\"groupname\":\"����\",\"code\":\"[cc�˶�]\"},{\"rid\":\"1617\",\"groupname\":\"����\",\"code\":\"[cc��ϲ]\"},{\"rid\":\"1618\",\"groupname\":\"����\",\"code\":\"[cc�質]\"},{\"rid\":\"1619\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1620\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1621\",\"groupname\":\"����\",\"code\":\"[cc��]\"},{\"rid\":\"1622\",\"groupname\":\"����\",\"code\":\"[cc˼��]\"},{\"rid\":\"1623\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1624\",\"groupname\":\"����\",\"code\":\"[cc��ɪ]\"},{\"rid\":\"1625\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1626\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1627\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1628\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1629\",\"groupname\":\"����\",\"code\":\"[cc͵��]\"},{\"rid\":\"1630\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1631\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1632\",\"groupname\":\"����\",\"code\":\"[ccɫɫ]\"},{\"rid\":\"1633\",\"groupname\":\"����\",\"code\":\"[cc����]\"},{\"rid\":\"1634\",\"groupname\":\"nonopanda\",\"code\":\"[nono���տ���]\"},{\"rid\":\"1635\",\"groupname\":\"nonopanda\",\"code\":\"[nono��ɪ]\"},{\"rid\":\"1636\",\"groupname\":\"nonopanda\",\"code\":\"[nono��˧]\"},{\"rid\":\"1637\",\"groupname\":\"nonopanda\",\"code\":\"[nonoҡ��ָ]\"},{\"rid\":\"1638\",\"groupname\":\"nonopanda\",\"code\":\"[nono��ѽ��ѽ]\"},{\"rid\":\"1639\",\"groupname\":\"nonopanda\",\"code\":\"[nono��]\"},{\"rid\":\"1640\",\"groupname\":\"nonopanda\",\"code\":\"[nono����]\"},{\"rid\":\"1641\",\"groupname\":\"nonopanda\",\"code\":\"[nono����]\"},{\"rid\":\"1642\",\"groupname\":\"nonopanda\",\"code\":\"[nono����]\"},{\"rid\":\"1643\",\"groupname\":\"nonopanda\",\"code\":\"[nono����]\"},{\"rid\":\"1644\",\"groupname\":\"nonopanda\",\"code\":\"[nono��������]\"},{\"rid\":\"1645\",\"groupname\":\"nonopanda\",\"code\":\"[nonoգ��]\"},{\"rid\":\"1646\",\"groupname\":\"nonopanda\",\"code\":\"[nono�����]\"},{\"rid\":\"1647\",\"groupname\":\"nonopanda\",\"code\":\"[nonoˮ����]\"},{\"rid\":\"1648\",\"groupname\":\"nonopanda\",\"code\":\"[nonokiss]\"},{\"rid\":\"1649\",\"groupname\":\"nonopanda\",\"code\":\"[nonoʥ����]\"},{\"rid\":\"1650\",\"groupname\":\"nonopanda\",\"code\":\"[nono����]\"},{\"rid\":\"1651\",\"groupname\":\"nonopanda\",\"code\":\"[nono����]\"},{\"rid\":\"1652\",\"groupname\":\"nonopanda\",\"code\":\"[nono����]\"},{\"rid\":\"1653\",\"groupname\":\"nonopanda\",\"code\":\"[nono��ƨ]\"},{\"rid\":\"1654\",\"groupname\":\"nonopanda\",\"code\":\"[nono��]\"},{\"rid\":\"1655\",\"groupname\":\"nonopanda\",\"code\":\"[nono������]\"},{\"rid\":\"1656\",\"groupname\":\"nonopanda\",\"code\":\"[nono���Ƿ]\"},{\"rid\":\"1657\",\"groupname\":\"nonopanda\",\"code\":\"[nonoŤ]\"},{\"rid\":\"1658\",\"groupname\":\"nonopanda\",\"code\":\"[nonomua]\"},{\"rid\":\"1659\",\"groupname\":\"nonopanda\",\"code\":\"[nono����]\"},{\"rid\":\"1660\",\"groupname\":\"nonopanda\",\"code\":\"[nono�ܲ�]\"},{\"rid\":\"1661\",\"groupname\":\"nonopanda\",\"code\":\"[nonoתȦȦ]\"},{\"rid\":\"1662\",\"groupname\":\"nonopanda\",\"code\":\"[nono������]\"},{\"rid\":\"1663\",\"groupname\":\"nonopanda\",\"code\":\"[nono˯��]\"},{\"rid\":\"1664\",\"groupname\":\"nonopanda\",\"code\":\"[nono������]\"},{\"rid\":\"1665\",\"groupname\":\"nonopanda\",\"code\":\"[nono��С��]\"},{\"rid\":\"1666\",\"groupname\":\"nonopanda\",\"code\":\"[nono����]\"},{\"rid\":\"1667\",\"groupname\":\"��������\",\"code\":\"[dino����]\"},{\"rid\":\"1668\",\"groupname\":\"��������\",\"code\":\"[dino�ᱼ]\"},{\"rid\":\"1669\",\"groupname\":\"��������\",\"code\":\"[dino����]\"},{\"rid\":\"1670\",\"groupname\":\"��������\",\"code\":\"[dino����]\"},{\"rid\":\"1671\",\"groupname\":\"��������\",\"code\":\"[dino��]\"},{\"rid\":\"1672\",\"groupname\":\"��������\",\"code\":\"[dino�ٱ�]\"},{\"rid\":\"1673\",\"groupname\":\"��������\",\"code\":\"[dino����]\"},{\"rid\":\"1674\",\"groupname\":\"��������\",\"code\":\"[dino����]\"},{\"rid\":\"1675\",\"groupname\":\"��������\",\"code\":\"[dino��Ͳ]\"},{\"rid\":\"1676\",\"groupname\":\"��������\",\"code\":\"[dino��]\"},{\"rid\":\"1677\",\"groupname\":\"��������\",\"code\":\"[dino��Ц]\"},{\"rid\":\"1678\",\"groupname\":\"��������\",\"code\":\"[dino����]\"},{\"rid\":\"1679\",\"groupname\":\"��������\",\"code\":\"[dino�ε�]\"},{\"rid\":\"1680\",\"groupname\":\"��������\",\"code\":\"[dino����]\"},{\"rid\":\"1681\",\"groupname\":\"��������\",\"code\":\"[dino�˯]\"},{\"rid\":\"1682\",\"groupname\":\"��������\",\"code\":\"[dino��ο]\"},{\"rid\":\"1683\",\"groupname\":\"��������\",\"code\":\"[dino�ټ�]\"},{\"rid\":\"1684\",\"groupname\":\"��������\",\"code\":\"[dino��Ͳ]\"},{\"rid\":\"1685\",\"groupname\":\"��������\",\"code\":\"[dino��м]\"},{\"rid\":\"1686\",\"groupname\":\"��������\",\"code\":\"[dino�簲]\"},{\"rid\":\"1687\",\"groupname\":\"��������\",\"code\":\"[dino����]\"},{\"rid\":\"1688\",\"groupname\":\"��������\",\"code\":\"[dinoͶ��]\"},{\"rid\":\"1689\",\"groupname\":\"��������\",\"code\":\"[dino����]\"},{\"rid\":\"1690\",\"groupname\":\"��������\",\"code\":\"[dino�Է�]\"},{\"rid\":\"1691\",\"groupname\":\"��������\",\"code\":\"[dinoʧ��]\"},{\"rid\":\"1692\",\"groupname\":\"��������\",\"code\":\"[dino��Ǯ]\"},{\"rid\":\"1693\",\"groupname\":\"��������\",\"code\":\"[dino����]\"},{\"rid\":\"1694\",\"groupname\":\"��������\",\"code\":\"[dino���]\"},{\"rid\":\"1695\",\"groupname\":\"��������\",\"code\":\"[dino��Ѫ]\"},{\"rid\":\"1696\",\"groupname\":\"��������\",\"code\":\"[dinoί��]\"},{\"rid\":\"1697\",\"groupname\":\"��������\",\"code\":\"[dino��Ȧ]\"},{\"rid\":\"1698\",\"groupname\":\"��������\",\"code\":\"[dino��ŭ]\"},{\"rid\":\"1699\",\"groupname\":\"��������\",\"code\":\"[dino�Ծ�]\"},{\"rid\":\"1700\",\"groupname\":\"��������\",\"code\":\"[dino�Ⱦ�]\"},{\"rid\":\"1701\",\"groupname\":\"��������\",\"code\":\"[dinoҧ����]\"},{\"rid\":\"1702\",\"groupname\":\"��������\",\"code\":\"[dino����]\"},{\"rid\":\"1703\",\"groupname\":\"��������\",\"code\":\"[dino����]\"},{\"rid\":\"1704\",\"groupname\":\"��������\",\"code\":\"[dino��Ը]\"},{\"rid\":\"1705\",\"groupname\":\"��������\",\"code\":\"[dino���]\"},{\"rid\":\"1706\",\"groupname\":\"Ӱ��\",\"code\":\"[yz�ҵ�]\"},{\"rid\":\"1707\",\"groupname\":\"Ӱ��\",\"code\":\"[yzײ����]\"},{\"rid\":\"1708\",\"groupname\":\"Ӱ��\",\"code\":\"[yz��ԡ]\"},{\"rid\":\"1709\",\"groupname\":\"Ӱ��\",\"code\":\"[yz����]\"},{\"rid\":\"1710\",\"groupname\":\"Ӱ��\",\"code\":\"[yz����]\"},{\"rid\":\"1711\",\"groupname\":\"Ӱ��\",\"code\":\"[yz������]\"},{\"rid\":\"1712\",\"groupname\":\"Ӱ��\",\"code\":\"[yz���]\"},{\"rid\":\"1713\",\"groupname\":\"Ӱ��\",\"code\":\"[yz���۽�]\"},{\"rid\":\"1714\",\"groupname\":\"Ӱ��\",\"code\":\"[yz������]\"},{\"rid\":\"1715\",\"groupname\":\"Ӱ��\",\"code\":\"[yz����]\"},{\"rid\":\"1716\",\"groupname\":\"Ӱ��\",\"code\":\"[yz͵��Ц]\"},{\"rid\":\"1717\",\"groupname\":\"Ӱ��\",\"code\":\"[yzye]\"},{\"rid\":\"1718\",\"groupname\":\"Ӱ��\",\"code\":\"[yzͶ��]\"},{\"rid\":\"1719\",\"groupname\":\"Ӱ��\",\"code\":\"[yz���]\"},{\"rid\":\"1720\",\"groupname\":\"Ӱ��\",\"code\":\"[yzoye]\"},{\"rid\":\"1721\",\"groupname\":\"Ӱ��\",\"code\":\"[yz����]\"},{\"rid\":\"1722\",\"groupname\":\"Ӱ��\",\"code\":\"[yz����ͷ]\"},{\"rid\":\"1723\",\"groupname\":\"Ӱ��\",\"code\":\"[yz˦�־�]\"},{\"rid\":\"1724\",\"groupname\":\"Ӱ��\",\"code\":\"[yz�ұ�����]\"},{\"rid\":\"1725\",\"groupname\":\"Ӱ��\",\"code\":\"[yz����]\"},{\"rid\":\"1726\",\"groupname\":\"Ӱ��\",\"code\":\"[yzɵ����]\"},{\"rid\":\"1727\",\"groupname\":\"Ӱ��\",\"code\":\"[yz��]\"},{\"rid\":\"1728\",\"groupname\":\"Ӱ��\",\"code\":\"[yz�в�è]\"},{\"rid\":\"1729\",\"groupname\":\"Ӱ��\",\"code\":\"[yz������]\"},{\"rid\":\"1730\",\"groupname\":\"Ӱ��\",\"code\":\"[yz����]\"},{\"rid\":\"1731\",\"groupname\":\"Ӱ��\",\"code\":\"[yz��ƨ��]\"},{\"rid\":\"1732\",\"groupname\":\"Ӱ��\",\"code\":\"[yzί����]\"},{\"rid\":\"1733\",\"groupname\":\"Ӱ��\",\"code\":\"[yz����]\"},{\"rid\":\"1734\",\"groupname\":\"Ӱ��\",\"code\":\"[yz�Թ�]\"},{\"rid\":\"1735\",\"groupname\":\"Ӱ��\",\"code\":\"[yz����]\"},{\"rid\":\"1736\",\"groupname\":\"Ӱ��\",\"code\":\"[yz������]\"},{\"rid\":\"1737\",\"groupname\":\"Ӱ��\",\"code\":\"[yz������]\"},{\"rid\":\"1738\",\"groupname\":\"Ӱ��\",\"code\":\"[yz��ƨ]\"},{\"rid\":\"1739\",\"groupname\":\"Ӱ��\",\"code\":\"[yz��ƻ��]\"},{\"rid\":\"1740\",\"groupname\":\"Ӱ��\",\"code\":\"[yz̫����]\"},{\"rid\":\"1741\",\"groupname\":\"Ӱ��\",\"code\":\"[yz�ý���]\"},{\"rid\":\"1742\",\"groupname\":\"�����\",\"code\":\"[���]\"},{\"rid\":\"1743\",\"groupname\":\"�����\",\"code\":\"[��ü]\"},{\"rid\":\"1744\",\"groupname\":\"�����\",\"code\":\"[����]\"},{\"rid\":\"1745\",\"groupname\":\"�����\",\"code\":\"[�׶���]\"},{\"rid\":\"1746\",\"groupname\":\"�����\",\"code\":\"[����]\"},{\"rid\":\"1747\",\"groupname\":\"�����\",\"code\":\"[ð����]\"},{\"rid\":\"1748\",\"groupname\":\"�����\",\"code\":\"[�����]\"},{\"rid\":\"1749\",\"groupname\":\"�����\",\"code\":\"[����]\"},{\"rid\":\"1750\",\"groupname\":\"�����\",\"code\":\"[�ŵ�]\"},{\"rid\":\"1751\",\"groupname\":\"�����\",\"code\":\"[����]\"},{\"rid\":\"1752\",\"groupname\":\"��Ƥ��\",\"code\":\"[����]\"},{\"rid\":\"1753\",\"groupname\":\"��Ƥ��\",\"code\":\"[��]\"},{\"rid\":\"1754\",\"groupname\":\"��Ƥ��\",\"code\":\"[��]\"},{\"rid\":\"1755\",\"groupname\":\"��Ƥ��\",\"code\":\"[����]\"},{\"rid\":\"1756\",\"groupname\":\"��Ƥ��\",\"code\":\"[������]\"},{\"rid\":\"1757\",\"groupname\":\"��Ƥ��\",\"code\":\"[����]\"},{\"rid\":\"1758\",\"groupname\":\"��Ƥ��\",\"code\":\"[Ż��]\"},{\"rid\":\"1759\",\"groupname\":\"��Ƥ��\",\"code\":\"[����һ��]\"},{\"rid\":\"1760\",\"groupname\":\"��Ƥ��\",\"code\":\"[ƦƦ��Ү]\"},{\"rid\":\"1761\",\"groupname\":\"��Ƥ��\",\"code\":\"[mua]\"},{\"rid\":\"1762\",\"groupname\":\"��Ƥ��\",\"code\":\"[���]\"},{\"rid\":\"1763\",\"groupname\":\"��Ƥ��\",\"code\":\"[��Ц]\"},{\"rid\":\"1764\",\"groupname\":\"��Ƥ��\",\"code\":\"[��]\"},{\"rid\":\"1765\",\"groupname\":\"��Ƥ��\",\"code\":\"[ƦƦ�Ç�]\"},{\"rid\":\"1766\",\"groupname\":\"��Ƥ��\",\"code\":\"[������Ү]\"},{\"rid\":\"1767\",\"groupname\":\"��Ƥ��\",\"code\":\"[����]\"},{\"rid\":\"1768\",\"groupname\":\"��Ƥ��\",\"code\":\"[ҧ����]\"},{\"rid\":\"1769\",\"groupname\":\"��Ƥ��\",\"code\":\"[ȥ]\"},{\"rid\":\"1770\",\"groupname\":\"��Ƥ��\",\"code\":\"[������]\"},{\"rid\":\"1771\",\"groupname\":\"��Ƥ��\",\"code\":\"[���]\"},{\"rid\":\"1772\",\"groupname\":\"��Ƥ��\",\"code\":\"[��������]\"},{\"rid\":\"1773\",\"groupname\":\"��Ƥ��\",\"code\":\"[ŭ��]\"},{\"rid\":\"1774\",\"groupname\":\"��Ƥ��\",\"code\":\"[886]\"},{\"rid\":\"1775\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1776\",\"groupname\":\"����\",\"code\":\"[����]\"},{\"rid\":\"1777\",\"groupname\":\"����\",\"code\":\"[˫��]\"},{\"rid\":\"1778\",\"groupname\":\"����\",\"code\":\"[˫��]\"},{\"rid\":\"1779\",\"groupname\":\"����\",\"code\":\"[���]\"},{\"rid\":\"1780\",\"groupname\":\"����\",\"code\":\"[��Ы]\"},{\"rid\":\"1781\",\"groupname\":\"����\",\"code\":\"[ˮƿ]\"},{\"rid\":\"1782\",\"groupname\":\"����\",\"code\":\"[��Ů]\"},{\"rid\":\"1783\",\"groupname\":\"����\",\"code\":\"[��ţ]\"},{\"rid\":\"1784\",\"groupname\":\"����\",\"code\":\"[��з]\"},{\"rid\":\"1785\",\"groupname\":\"����\",\"code\":\"[ʨ��]\"},{\"rid\":\"1786\",\"groupname\":\"����\",\"code\":\"[Ħ��]\"},{\"rid\":\"1787\",\"groupname\":\"����\",\"code\":\"[��Ы��]\"},{\"rid\":\"1788\",\"groupname\":\"����\",\"code\":\"[�����]\"},{\"rid\":\"1789\",\"groupname\":\"����\",\"code\":\"[˫����]\"},{\"rid\":\"1790\",\"groupname\":\"����\",\"code\":\"[˫����]\"},{\"rid\":\"1791\",\"groupname\":\"����\",\"code\":\"[������]\"},{\"rid\":\"1792\",\"groupname\":\"����\",\"code\":\"[ˮƿ��]\"},{\"rid\":\"1793\",\"groupname\":\"����\",\"code\":\"[Ħ����]\"},{\"rid\":\"1794\",\"groupname\":\"����\",\"code\":\"[ʨ����]\"},{\"rid\":\"1795\",\"groupname\":\"����\",\"code\":\"[��з��]\"},{\"rid\":\"1796\",\"groupname\":\"����\",\"code\":\"[��ţ��]\"},{\"rid\":\"1797\",\"groupname\":\"����\",\"code\":\"[��Ů��]\"},{\"rid\":\"1798\",\"groupname\":\"����\",\"code\":\"[������]\"},{\"rid\":\"1799\",\"groupname\":\"����\",\"code\":\"[���Ĵ���]\"},{\"rid\":\"1800\",\"groupname\":\"����\",\"code\":\"[��˿��]\"},{\"rid\":\"1801\",\"groupname\":\"����\",\"code\":\"[�ۺ�˿��]\"},{\"rid\":\"1802\",\"groupname\":\"����\",\"code\":\"[��˿��]\"},{\"rid\":\"1803\",\"groupname\":\"���˻�\",\"code\":\"[����]\"},{\"rid\":\"1804\",\"groupname\":\"���˻�\",\"code\":\"[����]\"},{\"rid\":\"1805\",\"groupname\":\"���˻�\",\"code\":\"[����]\"},{\"rid\":\"1806\",\"groupname\":\"���˻�\",\"code\":\"[����]\"},{\"rid\":\"1807\",\"groupname\":\"���˻�\",\"code\":\"[ͭ��]\"},{\"rid\":\"1808\",\"groupname\":\"���˻�\",\"code\":\"[����]\"},{\"rid\":\"1809\",\"groupname\":\"���˻�\",\"code\":\"[����]\"},{\"rid\":\"1810\",\"groupname\":\"���˻�\",\"code\":\"[����]\"},{\"rid\":\"1811\",\"groupname\":\"���˻�\",\"code\":\"[����]\"},{\"rid\":\"1812\",\"groupname\":\"���˻�\",\"code\":\"[����]\"},{\"rid\":\"1813\",\"groupname\":\"���˻�\",\"code\":\"[��8]\"},{\"rid\":\"1814\",\"groupname\":\"���˻�\",\"code\":\"[����]\"},{\"rid\":\"1815\",\"groupname\":\"���˻�\",\"code\":\"[��Ӿ]\"},{\"rid\":\"1816\",\"groupname\":\"���˻�\",\"code\":\"[ƹ����]\"},{\"rid\":\"1817\",\"groupname\":\"���˻�\",\"code\":\"[Ͷ��]\"},{\"rid\":\"1818\",\"groupname\":\"���˻�\",\"code\":\"[��ë��]\"},{\"rid\":\"1819\",\"groupname\":\"���˻�\",\"code\":\"[����]\"},{\"rid\":\"1820\",\"groupname\":\"���˻�\",\"code\":\"[���]\"},{\"rid\":\"1821\",\"groupname\":\"���˻�\",\"code\":\"[����]\"},{\"rid\":\"1822\",\"groupname\":\"��С��\",\"code\":\"[zxh��ɪ]\"},{\"rid\":\"1823\",\"groupname\":\"��С��\",\"code\":\"[΢΢Ц]\"},{\"rid\":\"1824\",\"groupname\":\"��С��\",\"code\":\"[��ί��]\"},{\"rid\":\"1825\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"1826\",\"groupname\":\"��С��\",\"code\":\"[������]\"},{\"rid\":\"1827\",\"groupname\":\"��С��\",\"code\":\"[������]\"},{\"rid\":\"1828\",\"groupname\":\"��С��\",\"code\":\"[ĬĬ����]\"},{\"rid\":\"1829\",\"groupname\":\"��С��\",\"code\":\"[С�к�]\"},{\"rid\":\"1830\",\"groupname\":\"��С��\",\"code\":\"[������]\"},{\"rid\":\"1831\",\"groupname\":\"��С��\",\"code\":\"[������]\"},{\"rid\":\"1832\",\"groupname\":\"��С��\",\"code\":\"[ǿ�ұ���]\"},{\"rid\":\"1833\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"1834\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"1835\",\"groupname\":\"��С��\",\"code\":\"[��Ǯ]\"},{\"rid\":\"1836\",\"groupname\":\"��С��\",\"code\":\"[΢Ц]\"},{\"rid\":\"1837\",\"groupname\":\"��С��\",\"code\":\"[˧��]\"},{\"rid\":\"1838\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"1839\",\"groupname\":\"��С��\",\"code\":\"[������]\"},{\"rid\":\"1840\",\"groupname\":\"��С��\",\"code\":\"[ɫ����]\"},{\"rid\":\"1841\",\"groupname\":\"��С��\",\"code\":\"[ƣ��]\"},{\"rid\":\"1842\",\"groupname\":\"��С��\",\"code\":\"[��]\"},{\"rid\":\"1843\",\"groupname\":\"��С��\",\"code\":\"[��]\"},{\"rid\":\"1844\",\"groupname\":\"��С��\",\"code\":\"[�ÿ���]\"},{\"rid\":\"1845\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"1846\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"1847\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"1848\",\"groupname\":\"��С��\",\"code\":\"[��Ǯ]\"},{\"rid\":\"1849\",\"groupname\":\"��С��\",\"code\":\"[����]\"},{\"rid\":\"1850\",\"groupname\":\"��С��\",\"code\":\"[�ܶ�]\"},{\"rid\":\"1851\",\"groupname\":\"������\",\"code\":\"[С�˵�־]\"},{\"rid\":\"1852\",\"groupname\":\"������\",\"code\":\"[�۹���]\"},{\"rid\":\"1853\",\"groupname\":\"������\",\"code\":\"[̾��]\"},{\"rid\":\"1854\",\"groupname\":\"������\",\"code\":\"[����]\"},{\"rid\":\"1855\",\"groupname\":\"������\",\"code\":\"[��]\"},{\"rid\":\"1856\",\"groupname\":\"������\",\"code\":\"[����]\"},{\"rid\":\"1857\",\"groupname\":\"������\",\"code\":\"[����]\"},{\"rid\":\"1858\",\"groupname\":\"������\",\"code\":\"[ŭ��]\"},{\"rid\":\"1859\",\"groupname\":\"������\",\"code\":\"[Ĥ��]\"},{\"rid\":\"1860\",\"groupname\":\"������\",\"code\":\"[·��]\"},{\"rid\":\"1861\",\"groupname\":\"������\",\"code\":\"[�ᱼ]\"},{\"rid\":\"1862\",\"groupname\":\"������\",\"code\":\"[����ɫ]\"},{\"rid\":\"1863\",\"groupname\":\"������\",\"code\":\"[��]\"},{\"rid\":\"1864\",\"groupname\":\"������\",\"code\":\"[�ֲ�]\"},{\"rid\":\"1865\",\"groupname\":\"������\",\"code\":\"[�����Ұ�]\"},{\"rid\":\"1866\",\"groupname\":\"������\",\"code\":\"[��������]\"},{\"rid\":\"1867\",\"groupname\":\"������\",\"code\":\"[����]\"},{\"rid\":\"1868\",\"groupname\":\"������\",\"code\":\"[����]\"},{\"rid\":\"1869\",\"groupname\":\"������\",\"code\":\"[����]\"},{\"rid\":\"1870\",\"groupname\":\"������\",\"code\":\"[����]\"},{\"rid\":\"1871\",\"groupname\":\"������\",\"code\":\"[����]\"},{\"rid\":\"1872\",\"groupname\":\"������\",\"code\":\"[����]\"},{\"rid\":\"1873\",\"groupname\":\"������\",\"code\":\"[���]\"},{\"rid\":\"1874\",\"groupname\":\"������\",\"code\":\"[�����]\"},{\"rid\":\"1875\",\"groupname\":\"������\",\"code\":\"[���Զ��]\"},{\"rid\":\"1876\",\"groupname\":\"������\",\"code\":\"[����]\"},{\"rid\":\"1877\",\"groupname\":\"������\",\"code\":\"[����]\"},{\"rid\":\"1878\",\"groupname\":\"������\",\"code\":\"[��ʤ]\"},{\"rid\":\"1879\",\"groupname\":\"������\",\"code\":\"[��������]\"},{\"rid\":\"1880\",\"groupname\":\"������\",\"code\":\"[�ϻ�]\"},{\"rid\":\"1881\",\"groupname\":\"������\",\"code\":\"[������]\"},{\"rid\":\"1882\",\"groupname\":\"������\",\"code\":\"[����Ŷ]\"},{\"rid\":\"1883\",\"groupname\":\"С��С��\",\"code\":\"[yeah]\"},{\"rid\":\"1884\",\"groupname\":\"С��С��\",\"code\":\"[ϲ��]\"},{\"rid\":\"1885\",\"groupname\":\"С��С��\",\"code\":\"[�Ķ�]\"},{\"rid\":\"1886\",\"groupname\":\"С��С��\",\"code\":\"[����]\"},{\"rid\":\"1887\",\"groupname\":\"С��С��\",\"code\":\"[�����㵸]\"},{\"rid\":\"1888\",\"groupname\":\"С��С��\",\"code\":\"[��Ц]\"},{\"rid\":\"1889\",\"groupname\":\"С��С��\",\"code\":\"[ʹ��]\"},{\"rid\":\"1890\",\"groupname\":\"С��С��\",\"code\":\"[����]\"},{\"rid\":\"1891\",\"groupname\":\"С��С��\",\"code\":\"[����]\"},{\"rid\":\"1892\",\"groupname\":\"С��С��\",\"code\":\"[��м]\"},{\"rid\":\"1893\",\"groupname\":\"����\",\"code\":\"[dx����]\"},{\"rid\":\"1894\",\"groupname\":\"����\",\"code\":\"[dxը��]\"},{\"rid\":\"1895\",\"groupname\":\"����\",\"code\":\"[dxϴ��]\"},{\"rid\":\"1896\",\"groupname\":\"����\",\"code\":\"[dx��צ]\"},{\"rid\":\"1897\",\"groupname\":\"����\",\"code\":\"[dx����]\"},{\"rid\":\"1898\",\"groupname\":\"����\",\"code\":\"[dxˢ��]\"},{\"rid\":\"1899\",\"groupname\":\"����\",\"code\":\"[dxɵ]\"},{\"rid\":\"1900\",\"groupname\":\"����\",\"code\":\"[dxɹ]\"},{\"rid\":\"1901\",\"groupname\":\"����\",\"code\":\"[dx������]\"},{\"rid\":\"1902\",\"groupname\":\"����\",\"code\":\"[dx������]\"},{\"rid\":\"1903\",\"groupname\":\"����\",\"code\":\"[dxҮ]\"},{\"rid\":\"1904\",\"groupname\":\"����\",\"code\":\"[dxŤ]\"},{\"rid\":\"1905\",\"groupname\":\"����\",\"code\":\"[dxû��]\"},{\"rid\":\"1906\",\"groupname\":\"����\",\"code\":\"[dx����]\"},{\"rid\":\"1907\",\"groupname\":\"����\",\"code\":\"[dx����]\"},{\"rid\":\"1908\",\"groupname\":\"����\",\"code\":\"[dx�ᱼ]\"},{\"rid\":\"1909\",\"groupname\":\"����\",\"code\":\"[dx����]\"},{\"rid\":\"1910\",\"groupname\":\"����\",\"code\":\"[dx��̤��]\"},{\"rid\":\"1911\",\"groupname\":\"����\",\"code\":\"[dx����]\"},{\"rid\":\"1912\",\"groupname\":\"����\",\"code\":\"[dx����]\"},{\"rid\":\"1913\",\"groupname\":\"����\",\"code\":\"[dx����]\"},{\"rid\":\"1914\",\"groupname\":\"����\",\"code\":\"[dx��]\"},{\"rid\":\"1915\",\"groupname\":\"����\",\"code\":\"[dx����]\"},{\"rid\":\"1916\",\"groupname\":\"����\",\"code\":\"[dx��]\"},{\"rid\":\"1917\",\"groupname\":\"����\",\"code\":\"[dx��]\"},{\"rid\":\"1918\",\"groupname\":\"Ģ�����\",\"code\":\"[գգ��]\"},{\"rid\":\"1919\",\"groupname\":\"Ģ�����\",\"code\":\"[�Ӽ�]\"},{\"rid\":\"1920\",\"groupname\":\"Ģ�����\",\"code\":\"[���ʺ�]\"},{\"rid\":\"1921\",\"groupname\":\"Ģ�����\",\"code\":\"[����]\"},{\"rid\":\"1922\",\"groupname\":\"Ģ�����\",\"code\":\"[ǿ��]\"},{\"rid\":\"1923\",\"groupname\":\"Ģ�����\",\"code\":\"[������]\"},{\"rid\":\"1924\",\"groupname\":\"Ģ�����\",\"code\":\"[��ͷ]\"},{\"rid\":\"1925\",\"groupname\":\"Ģ�����\",\"code\":\"[����]\"},{\"rid\":\"1926\",\"groupname\":\"Ģ�����\",\"code\":\"[��]\"}]";
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
			//Log.d("ת��", id_to_repost);
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
			new AlertDialog.Builder(Saloon.this).setTitle("˵��ʲô�ɣ�ү~").setView(repostView)
			//.setMultiChoiceItems(new String[] {"���۸���ǰ΢��", "���۸�ԭ΢��"}, null, null)
			.setPositiveButton("ȷ��", repostListener)
			.setNegativeButton("�༭", null)
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
					menu.setHeaderTitle("�ٻ���������");
					menu.add(0, 0, 0, "�������~");
					menu.add(0, 1, 1, "ת����");
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
				listItemView.counts.setText("����("+ori_json_obj.getString("comments_count")+")    ת��("
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
							Toast.makeText(Saloon.this, "û��ˢ����...", Toast.LENGTH_SHORT).show();
						}
						else{
							//db op
							Toast.makeText(Saloon.this, "ˢ������������", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(Saloon.this, "��֪��Ϊʲô������ʧ���ˡ�", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(Saloon.this, "��֪��Ϊʲô������ʧ���ˡ�", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(Saloon.this, "ת���ɹ��� ������~", Toast.LENGTH_SHORT).show();
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
