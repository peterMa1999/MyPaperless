package com.zsm.foxconn.mypaperless.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.R.id;
import com.zsm.foxconn.mypaperless.R.layout;
import com.zsm.foxconn.mypaperless.R.style;

/**
 * 自定义對話框配置
 */
public class Dialog_Examine extends Dialog {
	private Context mContext;
	private String title; // dialog的标题
	private LinearLayout buttonLayout;
	View view;

	public Dialog_Examine(Context context, String title) {
		// TODO Auto-generated constructor stub
		super(context, R.style.customDialog);
		this.mContext = context;
		this.title = title;

		initComponent();
	}

	private void initComponent() {

		view = LayoutInflater.from(mContext).inflate(R.layout.dialog_examine,
				null);
		((TextView) view.findViewById(R.id.edit_title_kpi)).setText(title);

		// buttonLayout = (LinearLayout)
		// view.findViewById(R.id.buttonLayout_fqc);
		this.setContentView(view);
	}

	/**
	 * 设置按钮 ok
	 * 
	 * @param text
	 * @param listener
	 */
	public void setOKButton(String text, final View.OnClickListener listener) {
		Button button = (Button) view.findViewById(R.id.btn_dialog_ok_kpi);
		button.setText(text);
		button.setTextColor(Color.WHITE);
		button.setTextSize(20);
		button.setOnClickListener(listener);
	}

	/**
	 * 设置按钮 no
	 * 
	 * @param text
	 * @param listener
	 */
	public void setNOButton(String text, final View.OnClickListener listener) {
		Button button = (Button) view.findViewById(R.id.btn_dialog_no_kpi);
		button.setText(text);
		button.setTextColor(Color.WHITE);
		button.setTextSize(20);
		button.setOnClickListener(listener);
	}

}
