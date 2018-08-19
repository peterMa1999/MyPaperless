package com.zsm.foxconn.mypaperless.help;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * 滚动文字
 */
public class MarqueeText extends TextView implements Runnable {
	private int currentScrollX;
	private boolean isStop = false;
	private int textWidth;
	private boolean isMeasure = false;

	public MarqueeText(Context context) {
		super(context);
	}

	public MarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isMeasure) {
			getTextWidth();
			isMeasure = true;
		}
	}

	private void getTextWidth() {
		Paint paint = this.getPaint();
		String str = this.getText().toString();
		textWidth = (int) paint.measureText(str);
	}

	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		this.isMeasure = false;
	}

	public void run() {
		currentScrollX += 1;
		scrollTo(currentScrollX, 0);
		if (isStop) {
			return;
		}
		if (getScrollX() >= textWidth) {
			scrollTo(-this.getWidth(), 0);
			currentScrollX = -this.getWidth();
		}
		//时间
		postDelayed(this, 5);
	}

	//开始
	public void startScroll() {
		isStop = false;
		this.removeCallbacks(this);
		post(this);
	}

	//停止
	public void stopScroll() {
		currentScrollX = 0;
		isStop = true;
	}

	//从头开始
	public void startFor0() {
		currentScrollX = 0;
		startScroll();
	}
}

