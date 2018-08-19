package com.zsm.foxconn.mypaperless;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class DefaultDailog extends Dialog {
	public DefaultDailog(Context context) {
		super(context);
	}

	public DefaultDailog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 当窗口焦点改变时调用
	 */
	public void onWindowFocusChanged(boolean hasFocus) {
		ImageView imageView = (ImageView) findViewById(R.id.dialogo_imageView);
		// 获取ImageView上的动画背景
		final Animation scale = AnimationUtils.loadAnimation(getContext(),
				R.anim.anim_dailog);
		imageView.startAnimation(scale);
	}

	/**
	 * 给Dialog设置提示信息
	 * 
	 * @param message
	 */
	public void setMessage(CharSequence message) {
		if (message != null && message.length() > 0) {
			findViewById(R.id.dialogo_textView).setVisibility(View.VISIBLE);
			TextView txt = (TextView) findViewById(R.id.dialogo_textView);
			txt.setText(message);
			txt.invalidate();
		}
	}

	/**
	 * 弹出自定义ProgressDialog
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            提示
	 * @param cancelable
	 *            是否按返回键取消
	 * @param cancelListener
	 *            按下返回键监听
	 * @return
	 */
	public static DefaultDailog show(Context context, CharSequence message,
			boolean cancelable, OnCancelListener cancelListener) {
		DefaultDailog dialog = new DefaultDailog(context,
				R.style.loading_dialog);
		dialog.setContentView(R.layout.default_dailog);
		if (message == null || message.length() == 0) {
			dialog.findViewById(R.id.dialogo_textView).setVisibility(View.GONE);
		} else {
			TextView txt = (TextView) dialog
					.findViewById(R.id.dialogo_textView);
			txt.setText(message);
		}
		// 按返回键是否取消
		dialog.setCancelable(cancelable);
		// 监听返回键处理
		dialog.setOnCancelListener(cancelListener);
		// 设置居中
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		// 设置背景层透明度
		lp.dimAmount = 0.6f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
		return dialog;
	}

	public static DefaultDailog show(Context context, CharSequence message,
			int drawble, boolean cancelable, OnCancelListener cancelListener) {
		DefaultDailog dialog = new DefaultDailog(context,
				R.style.loading_dialog);
		dialog.setContentView(R.layout.default_dailog);
		if (message == null || message.length() == 0) {
			dialog.findViewById(R.id.dialogo_textView).setVisibility(View.GONE);
		} else {
			TextView txt = (TextView) dialog
					.findViewById(R.id.dialogo_textView);
			txt.setText(message);
			ImageView imageView = (ImageView) dialog
					.findViewById(R.id.dialogo_imageView);
			imageView.setImageResource(drawble);
			final Animation scale = AnimationUtils.loadAnimation(context,
					R.anim.anim_dailog);
			imageView.startAnimation(scale);
		}
		// 按返回键是否取消
		dialog.setCancelable(cancelable);
		// 监听返回键处理
		dialog.setOnCancelListener(cancelListener);
		// 设置居中
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		// 设置背景层透明度
		lp.dimAmount = 0.6f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
		return dialog;
	}
}
