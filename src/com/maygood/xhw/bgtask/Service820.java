package com.maygood.xhw.bgtask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Service820 extends Service {
	public Service820() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d("service820", "onCreate");
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("service820", "onStartCommand");
		String dateString = intent.getStringExtra("date")+":10"; 
		String status = intent.getStringExtra("status");
		
		Timer timer = new Timer();
		XHWTimerTask task = new XHWTimerTask(Service820.this, status);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;// = new Date(System.currentTimeMillis()+10000);
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Log.d("service820", date.getTime()+"");
		//timer.schedule(task, 10000+System.currentTimeMillis());
		timer.schedule(task, date);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("service820", "onDestroy");
		super.onDestroy();
	}
	
	private long dateInMillisecs(int year, int month, int day) {
		return dateInMillisecs(year, month, day, 0, 0, 0);
	}
	
	private long dateInMillisecs(int year, int month, int day, int hour, int minute, int second) {
		return System.currentTimeMillis();
	}
}

class XHWTimerTask extends TimerTask {
	
	public Context context;
	public String status;
	public XHWTimerTask(Context context, String status) {
		this.context = context;
		this.status = status;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Log.d("service820", "time's up!");
		QuickTools.sendTextWeibo(context, status);
	}
	
}