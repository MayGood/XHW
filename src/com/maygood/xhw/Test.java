package com.maygood.xhw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.ImageView;

public class Test extends Activity {
	
	private ImageView iv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		iv = (ImageView) findViewById(R.id.test_imageview);
		Bitmap b = BitmapFactory.decodeFile(android.os.Environment.getExternalStorageDirectory()+"/test.jpg");
		iv.setImageBitmap(b);
		//gv.setGifImage(R.drawable.bq_0000);
		//LoadPicTask loadPicTask = new LoadPicTask();
		//loadPicTask.execute("http://ww2.sinaimg.cn/bmiddle/4d08297djw1e6u6qj7ylsg2028028t8y.gif");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}
	
	class LoadPicTask extends AsyncTask<String, Integer, byte[]> {

		@Override
		protected byte[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			String imgUrl = params[0];
			return getImage(imgUrl);
			//return getImage("http://ww3.sinaimg.cn/large/ad27b1f7jw1e5uxmqgq9jj20i80i842n.jpg");
			//http://ww4.sinaimg.cn/bmiddle/a716fd45jw1e5uvrmjp6sj20c84wy4qp.jpg
		}
		
		@Override
		protected void onPostExecute(byte[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//gv.setGifImage(result);
		}
		
		public byte[] getImage(String imgUrl) {
			URL url;
			byte[] b = null;
			try {
				url = new URL(imgUrl);
				HttpURLConnection cnx = (HttpURLConnection) url.openConnection();
				cnx.setDoInput(true);
				cnx.connect();
				if(cnx.getResponseCode()==200) {
					InputStream is = cnx.getInputStream();
					
					ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
					byte[] buff = new byte[100];
					int rc = 0;
					while((rc = is.read(buff, 0, 100)) > 0) {
						swapStream.write(buff, 0, rc);
					}
					b = swapStream.toByteArray();
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
