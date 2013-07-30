package com.maygood.xhw;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.maygood.xhw.bgtask.QuickTools;
import com.maygood.xhw.bgtask.Service820;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class TimerSettingActivity extends Activity {
	
	private TimePicker timePicker;
	private NumberPicker monthPicker;
	private NumberPicker dayPicker;
	private EditText statusView;
	private Button okButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("大约八点二十发哦~");
		setContentView(R.layout.activity_timer_setting);
		
		timePicker = (TimePicker) findViewById(R.id.timer_time_tp);
		monthPicker = (NumberPicker) findViewById(R.id.timer_date_month);
		dayPicker = (NumberPicker) findViewById(R.id.timer_date_day);
		statusView = (EditText) findViewById(R.id.timer_status_et);
		okButton = (Button) findViewById(R.id.timer_ok_b);
		timePicker.setIs24HourView(true);
		
		monthPicker.setMaxValue(12);
		monthPicker.setMinValue(1);
		dayPicker.setMaxValue(31);
		dayPicker.setMinValue(1);
		
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String dateString = "2013-"+formatString(monthPicker.getValue(), 2)+"-"+formatString(dayPicker.getValue(), 2)+" "
						+formatString(timePicker.getCurrentHour(), 2)+":"+formatString(timePicker.getCurrentMinute(), 2);
				Log.d("dateString", dateString);
				Intent is = new Intent(TimerSettingActivity.this, Service820.class);
				is.putExtra("date", dateString);
				is.putExtra("status", statusView.getText().toString());
				startService(is);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timer_setting, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings:
			long time = (new Date()).getTime();
			Date d = new Date(time+900000);
			QuickTools.sendTextWeibo(this, "可以关机了 #"+(new SimpleDateFormat("大约HH点mm")).format(d)+"#", false);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private String formatString(int i, int n) {
		String str = Integer.toString(i);
		while(str.length()<n) {
			str = "0"+str;
		}
		return str;
	}

}
