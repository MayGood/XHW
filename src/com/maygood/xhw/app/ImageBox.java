package com.maygood.xhw.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ImageBox extends ImageView {
	
	int block_w = 500, block_h = 2000;
	float scale_x = 1, scale_y = 1;
	private XHWBitmap bitmap[];
	
	private MODE mode;

	public ImageBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mode = MODE.NORMAL;
	}
	
	public void setBlock(int w, int h) {
		block_w = w;
		block_h = h;
	}
	
	public void setBitmap(Bitmap bm, MODE m) {
		mode = m;
		if(mode == MODE.TRIM) {
			setImageBitmap(Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), 4096));
		}
		else {
			setImageBitmap(bm);
		}
	}
	
	public void setScale(float scale) {
		mode = MODE.SCALE;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(mode == MODE.EXTRA_LARGE) {
			//Log.d("imageview", "onDraw:EXTRA_LARGE");
			int left, top;
			for(int i=0; i<bitmap.length; i++) {
				left = bitmap[i].getX();
				top = bitmap[i].getY();
				canvas.drawBitmap(bitmap[i].getBitmap(), left, top, null);
				bitmap[i].getBitmap().recycle();
			}
		}
		else if(mode == MODE.TRIM) {
			//Log.d("imageview", "onDraw:TRIM");
			int left = bitmap[0].getX();
			int	top = bitmap[0].getY();
			canvas.drawBitmap(bitmap[0].getBitmap(), left, top, null);
			bitmap[0].getBitmap().recycle();
		}
		else if(mode == MODE.SCALE) {
			//Log.d("imageview", "onDraw:TRIM");
			int left = bitmap[0].getX();
			int	top = bitmap[0].getY();
			Matrix matrix = new Matrix();
			matrix.postScale((float)0.5, (float)0.5);
			canvas.drawBitmap(bitmap[0].getBitmap(), matrix, null);
			bitmap[0].getBitmap().recycle();
		}
		else {
			//Log.d("imageview", "onDraw:NORMAL");
			super.onDraw(canvas);
		}
		//super.onDraw(canvas);
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		//Log.d("imageview", "setImageBitmap");
		if(bm.getHeight()>4096) {
			mode = MODE.EXTRA_LARGE;
			int n = (bm.getHeight()+4095) / 4096;
			bitmap = new XHWBitmap[n];
			int i;
			for(i=0; i<n-1; i++) {
				bitmap[i] = new XHWBitmap(0, i*4096, Bitmap.createBitmap(bm, 0, i*4096, bm.getWidth(), 4096));
			}
			bitmap[i] = new XHWBitmap(0, i*4096, Bitmap.createBitmap(bm, 0, i*4096, bm.getWidth(), bm.getHeight()-i*4096));
		}
		else {
			mode = MODE.NORMAL;
		}
		super.setImageBitmap(bm);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	
	class XHWBitmap {
		private int x;
		private int y;
		private Bitmap bitmap;
		public XHWBitmap(int x, int y, Bitmap bm) {
			// TODO Auto-generated constructor stub
			this.x = x;
			this.y = y;
			this.bitmap = bm;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public Bitmap getBitmap() {
			return bitmap;
		}
	}
	
}
