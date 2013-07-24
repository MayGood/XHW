package com.maygood.xhw.app;

import com.maygood.xhw.R;
import com.maygood.xhw.SendMessageActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

public class BqBox extends FrameLayout {
	
	private Paint painter;
	private View view;
	public GridView gv;
	private int location = 90;
	
	public BqBox(Context context, int location) {
		super(context);
		// TODO Auto-generated constructor stub
		setWillNotDraw(false);
		painter = new Paint();
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.popup_bq, null);
		gv = (GridView) view.findViewById(R.id.bq_grid);
		this.location = location;
		this.addView(view);
	}

	public BqBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setWillNotDraw(false);
		painter = new Paint();
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.popup_bq, null);
		gv = (GridView) view.findViewById(R.id.bq_grid);
		this.addView(view);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		//canvas.drawARGB(100, 30, 144, 255);
		
		painter.setARGB(100, 30, 144, 255);
		//painter.setColor(Color.GRAY);
		painter.setStrokeWidth(2);
		painter.setStyle(Style.FILL_AND_STROKE);
		
		Path path = new Path();
		path.moveTo(0, 0);
		path.lineTo(getWidth(), 0);
		path.lineTo(getWidth(), getHeight()-10);
		path.lineTo(140, getHeight()-10);
		path.lineTo(130, getHeight());
		path.lineTo(120-10, getHeight()-10);
		path.lineTo(0, getHeight()-10);
		path.lineTo(0, 0);
		canvas.drawPath(path, painter);
		/*
		canvas.drawLine(0, 0, getWidth(), 0, painter);
		canvas.drawLine(getWidth(), 0, getWidth(), getHeight()-10, painter);
		
		//canvas.drawLine(getWidth(), getHeight(), 0, getHeight(), painter);
		canvas.drawLine(getWidth(), getHeight()-10, 100, getHeight()-10, painter);
		canvas.drawLine(100, getHeight()-10, 90, getHeight(), painter);
		canvas.drawLine(90, getHeight(), 80, getHeight()-10, painter);
		canvas.drawLine(80, getHeight()-10, 0, getHeight()-10, painter);
		
		canvas.drawLine(0, getHeight()-10, 0, 0, painter);*/
		//canvas.drawRect(0, 0, getWidth(), getHeight(), painter);
	}
	
}
