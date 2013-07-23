package com.maygood.xhw.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class BqBox extends View {
	
	public Paint painter;
	
	public BqBox(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		painter = new Paint();
	}

	public BqBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		painter = new Paint();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		painter.setColor(Color.GRAY);
		painter.setStrokeWidth(2);
		painter.setStyle(Style.STROKE);
		canvas.drawRect(0, 0, getWidth(), getHeight(), painter);
	}

}
