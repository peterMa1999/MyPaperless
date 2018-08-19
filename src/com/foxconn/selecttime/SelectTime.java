package com.foxconn.selecttime;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.wheeltime.ScreenInfo;
import com.zsm.foxconn.mypaperless.wheeltime.WheelMain;

public class SelectTime {
	private static WheelView yearWheel;
	private static WheelView monthWheel;
	private static WheelView dayWheel;
	private static WheelView hourWheel;
	private static WheelView minuteWheel;
	private static WheelView secondWheel;
	public static String[] yearContent = null;
	public static String[] monthContent = null;
	public static String[] dayContent = null;
	public static String[] hourContent = null;
	public static String[] minuteContent = null;
	public static String[] secondContent = null;
	private static TimeDayListener listen;
	private static Activity activity;
	private static String title = "";

	public void setTimeListen(TimeDayListener dateListen) {
		listen = dateListen;
	}

	public interface TimeDayListener {
		void onGetresult(String result);
	}

	public SelectTime() {

	}

	public SelectTime(Activity activity, String title, TimeDayListener listen1) {
		this.activity = activity;
		this.title = title;
		this.listen = listen1;
	}

	public static void initContent() {
		yearContent = new String[20];
		for (int i = 0; i < 20; i++)
			yearContent[i] = String.valueOf(i + 2013);

		monthContent = new String[12];
		for (int i = 0; i < 12; i++) {
			monthContent[i] = String.valueOf(i + 1);
			if (monthContent[i].length() < 2) {
				monthContent[i] = "0" + monthContent[i];
			}
		}

		dayContent = new String[31];
		for (int i = 0; i < 31; i++) {
			dayContent[i] = String.valueOf(i + 1);
			if (dayContent[i].length() < 2) {
				dayContent[i] = "0" + dayContent[i];
			}
		}
		hourContent = new String[24];
		for (int i = 0; i < 24; i++) {
			hourContent[i] = String.valueOf(i);
			if (hourContent[i].length() < 2) {
				hourContent[i] = "0" + hourContent[i];
			}
		}

		minuteContent = new String[60];
		for (int i = 0; i < 60; i++) {
			minuteContent[i] = String.valueOf(i);
			if (minuteContent[i].length() < 2) {
				minuteContent[i] = "0" + minuteContent[i];
			}
		}
		secondContent = new String[60];
		for (int i = 0; i < 60; i++) {
			secondContent[i] = String.valueOf(i);
			if (secondContent[i].length() < 2) {
				secondContent[i] = "0" + secondContent[i];
			}
		}

	}

	/**
	 * 選擇時間 年，月，日，時，分，秒
	 */
	public static void ShowDateDialog(Context activity, final TextView time_tv,
			String title) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(R.layout.time_picker, null);
		initContent();
		Calendar calendar = Calendar.getInstance();
		int curYear = calendar.get(Calendar.YEAR);
		int curMonth = calendar.get(Calendar.MONTH) + 1;
		int curDay = calendar.get(Calendar.DAY_OF_MONTH);
		int curHour = calendar.get(Calendar.HOUR_OF_DAY);
		int curMinute = calendar.get(Calendar.MINUTE);
		int curSecond = calendar.get(Calendar.SECOND);

		yearWheel = (WheelView) view.findViewById(R.id.yearwheel_s);
		monthWheel = (WheelView) view.findViewById(R.id.monthwheel_s);
		dayWheel = (WheelView) view.findViewById(R.id.daywheel_s);
		hourWheel = (WheelView) view.findViewById(R.id.hourwheel_s);
		minuteWheel = (WheelView) view.findViewById(R.id.minutewheel_s);
		secondWheel = (WheelView) view.findViewById(R.id.secondwheel_s);

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setView(view);

		yearWheel.setAdapter(new StrericWheelAdapter(yearContent));
		yearWheel.setCurrentItem(curYear - 2013);
		yearWheel.setCyclic(true);
		yearWheel.setInterpolator(new AnticipateOvershootInterpolator());

		monthWheel.setAdapter(new StrericWheelAdapter(monthContent));

		monthWheel.setCurrentItem(curMonth - 1);

		monthWheel.setCyclic(true);
		monthWheel.setInterpolator(new AnticipateOvershootInterpolator());

		dayWheel.setAdapter(new StrericWheelAdapter(dayContent));
		dayWheel.setCurrentItem(curDay - 1);
		dayWheel.setCyclic(true);
		dayWheel.setInterpolator(new AnticipateOvershootInterpolator());

		hourWheel.setAdapter(new StrericWheelAdapter(hourContent));
		hourWheel.setCurrentItem(curHour);
		hourWheel.setCyclic(true);
		hourWheel.setInterpolator(new AnticipateOvershootInterpolator());

		minuteWheel.setAdapter(new StrericWheelAdapter(minuteContent));
		minuteWheel.setCurrentItem(curMinute);
		minuteWheel.setCyclic(true);
		minuteWheel.setInterpolator(new AnticipateOvershootInterpolator());

		secondWheel.setAdapter(new StrericWheelAdapter(secondContent));
		secondWheel.setCurrentItem(curSecond);
		secondWheel.setCyclic(true);
		secondWheel.setInterpolator(new AnticipateOvershootInterpolator());
		if (title.length() > 0) {
			builder.setTitle(title);
		} else {
			builder.setTitle("請選擇時間");
		}
		builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				StringBuffer sb = new StringBuffer();
				sb.append(yearWheel.getCurrentItemValue()).append("-")
						.append(monthWheel.getCurrentItemValue()).append("-")
						.append(dayWheel.getCurrentItemValue());

				sb.append(" ");
				sb.append(hourWheel.getCurrentItemValue()).append(":")
						.append(minuteWheel.getCurrentItemValue()).append(":")
						.append(secondWheel.getCurrentItemValue());
				time_tv.setText(sb);
				dialog.cancel();
			}
		}).setNegativeButton("取消", null);
		builder.show();
	}

	/**
	 * 選擇時間 年，月，日，時，分，秒(含監聽)
	 */
	public void ShowDateDialog() {
		LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(R.layout.time_picker, null);
		initContent();
		Calendar calendar = Calendar.getInstance();
		int curYear = calendar.get(Calendar.YEAR);
		int curMonth = calendar.get(Calendar.MONTH) + 1;
		int curDay = calendar.get(Calendar.DAY_OF_MONTH);
		int curHour = calendar.get(Calendar.HOUR_OF_DAY);
		int curMinute = calendar.get(Calendar.MINUTE);
		int curSecond = calendar.get(Calendar.SECOND);

		yearWheel = (WheelView) view.findViewById(R.id.yearwheel_s);
		monthWheel = (WheelView) view.findViewById(R.id.monthwheel_s);
		dayWheel = (WheelView) view.findViewById(R.id.daywheel_s);
		hourWheel = (WheelView) view.findViewById(R.id.hourwheel_s);
		minuteWheel = (WheelView) view.findViewById(R.id.minutewheel_s);
		secondWheel = (WheelView) view.findViewById(R.id.secondwheel_s);

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setView(view);

		yearWheel.setAdapter(new StrericWheelAdapter(yearContent));
		yearWheel.setCurrentItem(curYear - 2013);
		yearWheel.setCyclic(true);
		yearWheel.setInterpolator(new AnticipateOvershootInterpolator());

		monthWheel.setAdapter(new StrericWheelAdapter(monthContent));

		monthWheel.setCurrentItem(curMonth - 1);

		monthWheel.setCyclic(true);
		monthWheel.setInterpolator(new AnticipateOvershootInterpolator());

		dayWheel.setAdapter(new StrericWheelAdapter(dayContent));
		dayWheel.setCurrentItem(curDay - 1);
		dayWheel.setCyclic(true);
		dayWheel.setInterpolator(new AnticipateOvershootInterpolator());

		hourWheel.setAdapter(new StrericWheelAdapter(hourContent));
		hourWheel.setCurrentItem(curHour);
		hourWheel.setCyclic(true);
		hourWheel.setInterpolator(new AnticipateOvershootInterpolator());

		minuteWheel.setAdapter(new StrericWheelAdapter(minuteContent));
		minuteWheel.setCurrentItem(curMinute);
		minuteWheel.setCyclic(true);
		minuteWheel.setInterpolator(new AnticipateOvershootInterpolator());

		secondWheel.setAdapter(new StrericWheelAdapter(secondContent));
		secondWheel.setCurrentItem(curSecond);
		secondWheel.setCyclic(true);
		secondWheel.setInterpolator(new AnticipateOvershootInterpolator());
		if (title.length() > 0) {
			builder.setTitle(title);
		} else {
			builder.setTitle("請選擇時間");
		}

		builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				StringBuffer sb = new StringBuffer();
				sb.append(yearWheel.getCurrentItemValue()).append("-")
						.append(monthWheel.getCurrentItemValue()).append("-")
						.append(dayWheel.getCurrentItemValue());

				sb.append(" ");
				sb.append(hourWheel.getCurrentItemValue()).append(":")
						.append(minuteWheel.getCurrentItemValue()).append(":")
						.append(secondWheel.getCurrentItemValue());
				listen.onGetresult(sb.toString());
				dialog.cancel();
			}
		}).setNegativeButton("取消", null);
		builder.show();
	}

	/**
	 * 弹出日期选择框---- 年，月，日
	 * 
	 * @param activity
	 * @param textView
	 * @author F1330303
	 */
	public static void ShowDateDayDialog(Activity activity,
			final TextView textView) {
		int year, month, day;
		final WheelMain wheelMain;
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		LayoutInflater inflater = LayoutInflater.from(activity);
		View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(activity);

		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		wheelMain.initDateTimePicker(year, month, day);

		new AlertDialog.Builder(activity).setTitle("选择日期")
				.setView(timepickerview)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						textView.setText(wheelMain.getTime());
					}
				}).setNegativeButton("取消", null).show();
	}

	/**
	 * 弹出日期选择框---- 年，月，日(含監聽)
	 * 
	 * @param activity
	 * @param textView
	 * @author F1330303
	 */
	public static void ShowDateDayDialog() {
		int year, month, day;
		final WheelMain wheelMain;
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		LayoutInflater inflater = LayoutInflater.from(activity);
		View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(activity);

		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		wheelMain.initDateTimePicker(year, month, day);

		new AlertDialog.Builder(activity).setTitle(title)
				.setView(timepickerview)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						listen.onGetresult(wheelMain.getTime());
					}
				}).setNegativeButton("取消", null).show();
	}

}