package com.zsm.foxconn.mypaperless.util;

import java.util.ArrayList;
import java.util.List;

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
public class MyMarkerView_AnotherBar extends MarkerView {

	private TextView tvContent;
	private List<String> yVals = new ArrayList<String>();
	private List<String> yVals1 = new ArrayList<String>();

	public MyMarkerView_AnotherBar(Context context, int layoutResource) {
		super(context, layoutResource);

		tvContent = (TextView) findViewById(R.id.tvContent);
	}

	public MyMarkerView_AnotherBar(Context context, int layoutResource,
			List<String> y_list, List<String> cycletype_list
			) {
		// TODO Auto-generated constructor stub
		super(context, layoutResource);
		this.yVals = y_list;
		this.yVals1 = cycletype_list;
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
				tvContent.setText("已使用："+yVals.get(e.getXIndex())+"份"
						+ "\n報表總數：" + yVals1.get(e.getXIndex()) + "份");
			
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
