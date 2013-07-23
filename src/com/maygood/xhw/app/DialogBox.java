package com.maygood.xhw.app;

import org.xml.sax.XMLReader;

import com.maygood.xhw.FullscreenImageActivity;
import com.maygood.xhw.R;
import com.maygood.xhw.WeiboShow;
import com.maygood.xhw.data.DataBaseHandler;
import com.maygood.xhw.data.MessageFormater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Html.TagHandler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DialogBox extends TextView {

	public Paint painter;

	public DialogBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		painter = new Paint();
		setWillNotDraw(false);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		painter.setColor(Color.GRAY);
		painter.setStrokeWidth(2);
		painter.setStyle(Style.STROKE);
		//canvas.drawRect(0, 0, getWidth(), getHeight(), painter);
		/*
		Path path = new Path();
		path.moveTo(0, 7);
		path.lineTo(14, 7);
		path.lineTo(24, 0);
		path.lineTo(34, 7);
		path.lineTo(getWidth(), 7);
		path.lineTo(getWidth(), getHeight());
		path.lineTo(0, getHeight());
		path.lineTo(0, 7);
		canvas.drawPath(path, painter);
		/*/
		painter.setStrokeWidth(3);
		canvas.drawLine(0, 7, 14, 7, painter);
		painter.setStrokeWidth(2);
		canvas.drawLine(14, 7, 24, 0, painter);
		painter.setStrokeWidth(3);
		canvas.drawLine(24, 0, 34, 7, painter);
		canvas.drawLine(34, 7, getWidth(), 7, painter);
		painter.setStrokeWidth(2);
		canvas.drawLine(getWidth(), 7, getWidth(), getHeight(), painter);
		canvas.drawLine(getWidth(), getHeight(), 0, getHeight(), painter);
		canvas.drawLine(0, getHeight(), 0, 7, painter);
		//*/
		super.onDraw(canvas);
	}
	
	public void setContent(Context context, String name, String text, boolean clickable, boolean hasPicture) {
		setContent(context, name, text, clickable, hasPicture, null, null, null);
	}
	
	public void setContent(Context context, String name, String text, boolean clickable, 
			boolean hasPicture, String thumbnail_pic, String bmiddle_pic, String original_pic) {
		int length = name.length();
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
		
		setText("");
		if(clickable) {
			setMovementMethod(LinkMovementMethod.getInstance());
		}
		if(name.charAt(length-1)=='@') {
			append(Html.fromHtml("<font color=\'#808080\'>"+name.substring(0, length-1)+"</font>"));
			append(" ");
			SpannableString sps = new SpannableString(" ");
			Drawable d = getResources().getDrawable(R.drawable.placeholder);
			d.setBounds(0, 0, 24, 24);
			ImageSpan isp = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
			sps.setSpan(isp, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			sps.setSpan(new ImageClickResponse(context, thumbnail_pic, bmiddle_pic, original_pic),
					0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			append(sps);
			//append(Html.fromHtml("<img src=\'"+R.drawable.placeholder+"\'/>", imgGetter, null));
		}
		else {
			append(Html.fromHtml("<font color=\'#808080\'>"+name+"</font>"));
			
			if(hasPicture) {
				append(" ");
				SpannableString sps = new SpannableString(" ");
				Drawable d = getResources().getDrawable(R.drawable.placeholder);
				d.setBounds(0, 0, 24, 24);
				ImageSpan isp = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
				sps.setSpan(isp, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				sps.setSpan(new ImageClickResponse(context, thumbnail_pic, bmiddle_pic, original_pic),
						0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				append(sps);
				//append(Html.fromHtml("<img src=\'"+R.drawable.placeholder+"\'/>", imgGetter, null));
			}
		}
		
		DataBaseHandler dbHandler = new DataBaseHandler(context);
		
		int loc = 0;
		length = text.length();
		int start, end = -1;
		int temp;
		
		append("\n");
		
		//setText("");
		while((temp=MessageFormater.locateBq(text, loc))!=0) {
			start = temp/1000;
			end = temp%1000;
			String preContent;
			String code = text.substring(start, end+1);
			int rid= dbHandler.getRidByCode(code);
			if(rid==-1) {
				preContent = text.substring(loc, start+1);
				//append(preContent);
				formatContent(context, preContent);
				
				//sps = new SpannableString(preContent);
				//sps.setSpan(new ClickResponse(context, sps), 0, sps.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				//append(sps);
				
				loc = start+1;
			}
			else {
				preContent = text.substring(loc, start);
				//append(preContent);
				formatContent(context, preContent);
				
				//sps = new SpannableString(preContent);
				//sps.setSpan(new ClickResponse(context, sps), 0, sps.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				//append(sps);
				
				rid += R.drawable.bq_0000;
				append(Html.fromHtml("<img src=\'"+rid+"\'/>", imgGetter, null));
				loc = end+1;
			}
		}
		if(loc<length) {
			//append(text.substring(loc));
			formatContent(context, text.substring(loc));
			
			//sps = new SpannableString(text.substring(loc));
			//sps.setSpan(new ClickResponse(context, sps), 0, sps.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//append(sps);
		}
		
	}
	
	private void formatContent(Context context, String content) {
		SpannableString sps;
		int start, end, length;
		length = content.length();
		start = 0;
		end = 0;
		while(start < length) {
			if(content.charAt(start) == '@') {
				//append(content, end, start);
				markURL(context, this, content.substring(end, start));
				end = start;
				int tmp = start+1;
				while(tmp < length && isLegal(content.charAt(tmp), false)) {
					tmp++;
				}
				if(tmp > start+1) {
					sps = new SpannableString(content.subSequence(end, tmp));
					sps.setSpan(new ClickResponse(context, sps, 0), 0, sps.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					append(sps);
				}
				else {
					//append(content, end, tmp);
					markURL(context, this, content.substring(end, tmp));
				}
				end = tmp;
				start = end;
			}
			else if(content.charAt(start) == '#') {
				int tmp = end;
				//append(content, end, start);
				markURL(context, this, content.substring(end, start));
				end = start+1;
				while(end < length && content.charAt(end) != '#') {
					end++;
				}
				if(end<length) {
					sps = new SpannableString(content.subSequence(start, end+1));
					sps.setSpan(new ClickResponse(context, sps, 1), 0, sps.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					append(sps);
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
		//append(content.substring(end));
		markURL(context, this, content.substring(end));
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
			    context.startActivity(i);
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
	
	class ImageClickResponse extends ClickableSpan {
		Context context;
		String thumbnail_pic;	//ËõÂÔÍ¼Æ¬µØÖ·
		String bmiddle_pic;		//ÖÐµÈ³ß´çÍ¼Æ¬µØÖ·
		String original_pic;	//Ô­Ê¼Í¼Æ¬µØÖ·
		
		public ImageClickResponse(Context context, String thumbnail_pic, String bmiddle_pic, String original_pic) {
			this.context = context;
			this.thumbnail_pic = thumbnail_pic;
			this.bmiddle_pic = bmiddle_pic;
			this.original_pic = original_pic;
		}

		@Override
		public void onClick(View widget) {
			// TODO Auto-generated method stub
			//Toast.makeText(context, "image", Toast.LENGTH_SHORT).show();
			Intent i_image = new Intent(context, FullscreenImageActivity.class);
			i_image.putExtra("thumbnail_pic", thumbnail_pic);
			i_image.putExtra("bmiddle_pic", bmiddle_pic);
			i_image.putExtra("original_pic", original_pic);
			context.startActivity(i_image);
		}
		
	}

}
