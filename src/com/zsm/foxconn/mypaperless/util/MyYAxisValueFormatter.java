package com.zsm.foxconn.mypaperless.util;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.text.DecimalFormat;

public class MyYAxisValueFormatter implements YAxisValueFormatter {

	private DecimalFormat mFormat;
	private String direction;

	public MyYAxisValueFormatter(String direction) {
		mFormat = new DecimalFormat("###,###,###,##");
		this.direction = direction;
	}

	@Override
	public String getFormattedValue(float value, YAxis yAxis) {
		if (direction.equals("left")) {
			return mFormat.format(value) + "℃";
		}if (direction.equals("shiyonglv")) {
			return mFormat.format(value) + "%";
		} else {
			return mFormat.format(value) + "%RH";
		}
	}
}
