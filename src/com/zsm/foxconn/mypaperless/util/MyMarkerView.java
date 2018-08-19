package com.zsm.foxconn.mypaperless.util;

import java.util.ArrayList;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;
import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.bean.ChartBean;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

	private TextView tvContent;
	private ArrayList<String> yVals = new ArrayList<String>();
	private ArrayList<String> yVals1 = new ArrayList<String>();
	private ArrayList<String> dateList = new ArrayList<String>();

	public MyMarkerView(Context context, int layoutResource) {
		super(context, layoutResource);

		tvContent = (TextView) findViewById(R.id.tvContent);
	}

	public MyMarkerView(Context context, int layoutResource,
			ArrayList<String> yVals, ArrayList<String> yVals1,
			ArrayList<String> dateList) {
		// TODO Auto-generated constructor stub
		super(context, layoutResource);
		this.yVals = yVals;
		this.yVals1 = yVals1;
		this.dateList = dateList;
		tvContent = (TextView) findViewById(R.id.tvContent);
	}
	
	// callbacks everytime the MarkerView is redrawn, can be used to update the
	// content (user-interface)
	@Override
	public void refreshContent(Entry e, Highlight highlight) {
		if (e instanceof CandleEntry) {

			CandleEntry ce = (CandleEntry) e;

			tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
		} else {
			if (e.getXIndex() > yVals.size() - 1) {
				tvContent.setText(dateList.get(e.getXIndex()).substring(0, 10)
						+ "\n濕度：" + yVals1.get(e.getXIndex()) + "%RH");
			} else if (e.getXIndex() > yVals1.size() - 1) {
				tvContent.setText(dateList.get(e.getXIndex()).substring(0, 10)
						+ "\n溫度：" + yVals.get(e.getXIndex()) + "℃");
			} else {
				tvContent.setText(dateList.get(e.getXIndex()).substring(0, 10)
						+ "\n溫度：" + yVals.get(e.getXIndex()) + "℃\n濕度："
						+ yVals1.get(e.getXIndex()) + "%RH");
			}
		}
	}

	@Override
	public int getXOffset(float xpos) {
		// this will center the marker-view horizontally
		return -(getWidth() / 2);
	}

	@Override
	public int getYOffset(float ypos) {
		// this will cause the marker-view to be above the selected value
		return -getHeight();
	}
}
