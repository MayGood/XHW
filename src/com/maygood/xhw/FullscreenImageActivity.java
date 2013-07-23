package com.maygood.xhw;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.maygood.xhw.app.ImageBox;
import com.maygood.xhw.net.HttpsUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class FullscreenImageActivity extends Activity {
	private ImageBox contentView;
	private String thumbnail_pic;	//Àı¬‘Õº∆¨µÿ÷∑
	private String bmiddle_pic;		//÷–µ»≥ﬂ¥ÁÕº∆¨µÿ÷∑
	private String original_pic;	//‘≠ ºÕº∆¨µÿ÷∑

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fullscreen_image);
		
		Intent i = getIntent();
		thumbnail_pic = i.getStringExtra("thumbnail_pic");
		bmiddle_pic = i.getStringExtra("bmiddle_pic");
		original_pic = i.getStringExtra("original_pic");
		

		contentView = (ImageBox) findViewById(R.id.fullscreen_image);

		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});

		if(HttpsUtils.getNetType(this.getSystemService(Context.CONNECTIVITY_SERVICE))==0) {
			//WIFI
			loadPicture(bmiddle_pic);
		}
		else if(HttpsUtils.getNetType(this.getSystemService(Context.CONNECTIVITY_SERVICE))==1) {
			loadPicture(thumbnail_pic);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.fullscreen_image, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//contentView.setVisibility(View.INVISIBLE);
		switch (item.getItemId()) {
		case R.id.action_thumbnail:
			loadPicture(thumbnail_pic);
			break;
		case R.id.action_bmiddle:
			loadPicture(bmiddle_pic);
			break;
		case R.id.action_original:
			loadPicture(original_pic);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		//Log.d("FullscreenImageActivity", "onStop");
		super.onStop();
	}
	
	private void loadPicture(String imgurl) {
		LoadPicTask loadPicTask = new LoadPicTask();
		if((HttpsUtils.getNetType(this.getSystemService(Context.CONNECTIVITY_SERVICE))==0) ||
				(HttpsUtils.getNetType(this.getSystemService(Context.CONNECTIVITY_SERVICE))==1)) {
			loadPicTask.execute(imgurl);
		}
	}

	class LoadPicTask extends AsyncTask<String, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			String imgUrl = params[0];
			return getImage(imgUrl);
			//return getImage("http://ww3.sinaimg.cn/large/ad27b1f7jw1e5uxmqgq9jj20i80i842n.jpg");
			//http://ww4.sinaimg.cn/bmiddle/a716fd45jw1e5uvrmjp6sj20c84wy4qp.jpg
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			contentView.setImageBitmap(result);
			//contentView.setVisibility(View.VISIBLE);
			//result.recycle();
		}
		
		public Bitmap getImage(String imgUrl) {
			URL url;
			Bitmap b = null;
			try {
				url = new URL(imgUrl);
				HttpURLConnection cnx = (HttpURLConnection) url.openConnection();
				cnx.setDoInput(true);
				cnx.connect();
				if(cnx.getResponseCode()==200) {
					InputStream is = cnx.getInputStream();
					b = BitmapFactory.decodeStream(is);
					is.close();
				}
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

}
