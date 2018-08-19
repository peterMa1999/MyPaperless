package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.ChartBean;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.MyMarkerView;
import com.zsm.foxconn.mypaperless.util.MyYAxisValueFormatter;

public class MoistureproofChartActivity extends BaseActivity implements
		OnChartValueSelectedListener, OnChartGestureListener {

	private LineChart mChart;
	private UserBean user;
	HttpStart start = null;
	private Context context = MoistureproofChartActivity.this;
	private String floorName, lineName;
	private String[] floorStr = null, lineStr = null;
	private Spinner floorSP, lineSP, yearSP, monthSP;
	private String reportName;
	private ArrayList<String> temperatureList = new ArrayList<String>();
	private ArrayList<String> humidityList = new ArrayList<String>();
	private ArrayList<String> dateList = new ArrayList<String>();
	private ArrayList<ChartBean> dataList = new ArrayList<ChartBean>();
	private ChartBean chartBean;
	private final String NO_DATA = "NO_DATA";
	private String minDate, maxDate;
	private int year, day, month;
	private int[] months = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
	private Button temperatureBtn, humidityBtn, refreshBtn;
	private String[] years=new String[3];

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moistureproof_chart);
		Intent intent = getIntent();
		reportName = intent.getStringExtra("reportName");

		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		for (int i = year - 2, j = 0; i <= year; i++, j++) {
			years[j] = i + "";
		}
		month = c.get(Calendar.MONTH) + 1;
		initDays();
		if (month < 10) {
			minDate = year + "-0" + month + "-01";
			maxDate = year + "-0" + month + "-" + day;
		} else {
			minDate = year + "-" + month + "-01";
			maxDate = year + "-" + month + "-" + day;
		}

		findViewById();
		initView();
		user = (UserBean) getApplicationContext();
		start = new HttpStart(context, handler);
		start.getServerData(0, MyConstant.GET_MFG_FLOOR_TUBIAO, user.getMFG(),reportName);

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		TextView textView = (TextView) findViewById(R.id.bartitle_txt);
		textView.setText(reportName);
		Button button = (Button) findViewById(R.id.btn_submit);
		button.setVisibility(View.GONE);

		mChart = (LineChart) findViewById(R.id.chart);

		floorSP = (Spinner) findViewById(R.id.sp_floor);
		lineSP = (Spinner) findViewById(R.id.sp_line);
		yearSP = (Spinner) findViewById(R.id.sp_year);
		monthSP = (Spinner) findViewById(R.id.sp_month);

		temperatureBtn = (Button) findViewById(R.id.btn_temperature);
		humidityBtn = (Button) findViewById(R.id.btn_humiditry);
		refreshBtn = (Button) findViewById(R.id.btn_refresh);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mChart.setOnChartGestureListener(this);
		mChart.setOnChartValueSelectedListener(this);
		mChart.setDrawGridBackground(false);
		// no description text
		// mChart.setDescription(floorName + " " + lineName + "溫濕度統計圖");
		mChart.setDescriptionColor(Color.RED);
		mChart.setNoDataTextDescription("You need to provide data for the chart.");
		// enable touch gestures
		mChart.setTouchEnabled(true);
		// enable scaling and dragging
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		mChart.setPinchZoom(true);

		XAxis xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.resetLabelsToSkip();
		// xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
		// xAxis.addLimitLine(llXAxis); // add x-axis limit line

		Typeface tf = Typeface.createFromAsset(getAssets(),
				"OpenSans-Regular.ttf");

		LimitLine ll1 = new LimitLine(28f, "USL");
		ll1.setLineWidth(4f);
		ll1.enableDashedLine(10f, 10f, 0f);
		ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
		ll1.setTextSize(10f);
		ll1.setTypeface(tf);

		LimitLine ll2 = new LimitLine(18f, "LSL");
		ll2.setLineWidth(4f);
		ll2.enableDashedLine(10f, 10f, 0f);
		ll2.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
		ll2.setTextSize(10f);
		ll2.setTypeface(tf);

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.removeAllLimitLines(); // reset all limit lines to avoid
										// overlapping lines
		leftAxis.addLimitLine(ll1);
		leftAxis.addLimitLine(ll2);
		leftAxis.setAxisMaxValue(32f);
		leftAxis.setAxisMinValue(16f);
		leftAxis.setStartAtZero(false);
		// leftAxis.setYOffset(20f);
		leftAxis.enableGridDashedLine(10f, 10f, 0f);
		leftAxis.setValueFormatter(new MyYAxisValueFormatter("left"));
		// limit lines are drawn behind data (and not on top)
		leftAxis.setDrawLimitLinesBehindData(true);

		YAxis rightAxis = mChart.getAxisRight();
		rightAxis.removeAllLimitLines(); // reset all limit lines to avoid
		rightAxis.setAxisMaxValue(14f);
		rightAxis.setAxisMinValue(-2f);
		rightAxis.setStartAtZero(false);
		rightAxis.setDrawGridLines(false);
		rightAxis.setValueFormatter(new MyYAxisValueFormatter("right"));

		mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

		Legend l = mChart.getLegend();
		// modify the legend ...
		// l.setPosition(LegendPosition.LEFT_OF_CHART);
		l.setForm(LegendForm.LINE);

		floorSP.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				floorName = floorStr[arg2];
				start.getServerData(0, MyConstant.GET_MFG_LINE_TUBIAO, user.getMFG(),
						floorName,reportName);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		lineSP.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				lineName = lineStr[arg2];
				mChart.setDescription(floorName + " " + lineName + "溫濕度統計圖");
				start.getServerData(0, MyConstant.GET_CHART_DATA, reportName,
						user.getMFG(), floorName, lineName, minDate, maxDate);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, years);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定 Adapter到控件
		yearSP.setAdapter(adapter);
		yearSP.setSelection(2);
		yearSP.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.i("year", years[arg2]);
				year=Integer.parseInt(years[arg2]);
				initDays();
				if (month < 10) {
					minDate = year + "-0" + month + "-01";
					maxDate = year + "-0" + month + "-" + day;
				} else {
					minDate = year + "-" + month + "-01";
					maxDate = year + "-" + month + "-" + day;
				}
				start.getServerData(0, MyConstant.GET_CHART_DATA, reportName,
						user.getMFG(), floorName, lineName, minDate, maxDate);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		monthSP.setSelection(month - 1);
		monthSP.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				month = months[arg2];
				initDays();
				if (month < 10) {
					minDate = year + "-0" + month + "-01";
					maxDate = year + "-0" + month + "-" + day;
				} else {
					minDate = year + "-" + month + "-01";
					maxDate = year + "-" + month + "-" + day;
				}
				start.getServerData(0, MyConstant.GET_CHART_DATA, reportName,
						user.getMFG(), floorName, lineName, minDate, maxDate);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		temperatureBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTemperatureData();
				mChart.invalidate();
			}
		});
		humidityBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setHumidityData();
				mChart.invalidate();
			}
		});
		refreshBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDoubleData();
				mChart.invalidate();
			}
		});
	}

	private void setTemperatureData() {
		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < dateList.size(); i++) {
			if (i == 0) {
				xVals.add(dateList.get(i).substring(0, 10));
			} else {
				xVals.add(dateList.get(i).substring(8, 10));
			}
		}

		ArrayList<Entry> yVals = new ArrayList<Entry>();

		for (int i = 0; i < temperatureList.size(); i++) {
			try {
				yVals.add(new Entry(Float.parseFloat(temperatureList.get(i)), i));
			} catch (Exception e) {
				yVals.add(new Entry(23, i));
			}
		}

		MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view,
				temperatureList, humidityList, dateList);
		// set the marker to the chart
		mChart.setMarkerView(mv);

		// create a dataset and give it a type
		LineDataSet set1 = new LineDataSet(yVals, "溫度");
		set1.setDrawValues(false);
		set1.setColor(Color.BLUE);
		set1.setCircleColor(Color.BLUE);
		set1.setLineWidth(1f);
		set1.setCircleSize(3f);
		set1.setDrawCircleHole(false);
		set1.setValueTextSize(9f);
		set1.setFillAlpha(65);
		set1.setFillColor(Color.BLUE);
		set1.setAxisDependency(YAxis.AxisDependency.LEFT); // 以左边坐标轴为准

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set1); // add the datasets

		// create a data object with the datasets
		LineData data = new LineData(xVals, dataSets);
		// set data
		mChart.setData(data);
		mChart.animateXY(2000, 1000);
	}

	private void setHumidityData() {

		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < dateList.size(); i++) {
			if (i == 0) {
				xVals.add(dateList.get(i).substring(0, 10));
			} else {
				xVals.add(dateList.get(i).substring(8, 10));
			}
		}

		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		for (int i = 0; i < humidityList.size(); i++) {
			try {
				yVals1.add(new Entry(Float.parseFloat(humidityList.get(i)), i));
			} catch (Exception e) {
				yVals1.add(new Entry(5, i));
			}
		}

		MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view,
				temperatureList, humidityList, dateList);
		// set the marker to the chart
		mChart.setMarkerView(mv);

		LineDataSet set2 = new LineDataSet(yVals1, "濕度");
		set2.setDrawValues(false);
		set2.setColor(Color.BLACK);
		set2.setCircleColor(Color.BLACK);
		set2.setLineWidth(1f);
		set2.setCircleSize(3f);
		set2.setDrawCircleHole(false);
		set2.setValueTextSize(9f);
		set2.setFillAlpha(65);
		set2.setFillColor(Color.BLACK);
		set2.setAxisDependency(YAxis.AxisDependency.RIGHT);

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set2);

		// create a data object with the datasets
		LineData data = new LineData(xVals, dataSets);
		// set data
		mChart.setData(data);
		mChart.animateXY(3000, 2000);
	}

	private void setDoubleData() {

		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < dateList.size(); i++) {
			if (i == 0) {
				xVals.add(dateList.get(i).substring(0, 10));
			} else {
				xVals.add(dateList.get(i).substring(8, 10));
			}
		}

		ArrayList<Entry> yVals = new ArrayList<Entry>();
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		for (int i = 0; i < temperatureList.size(); i++) {
			try {
				yVals.add(new Entry(Float.parseFloat(temperatureList.get(i)), i));
			} catch (Exception e) {
				yVals.add(new Entry(23, i));
			}
		}
		for (int i = 0; i < humidityList.size(); i++) {
			try {
				yVals1.add(new Entry(Float.parseFloat(humidityList.get(i)), i));
			} catch (Exception e) {
				yVals1.add(new Entry(5, i));
			}
		}

		MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view,
				temperatureList, humidityList, dateList);
		// set the marker to the chart
		mChart.setMarkerView(mv);

		// create a dataset and give it a type
		LineDataSet set1 = new LineDataSet(yVals, "溫度");
		set1.setDrawValues(false);
		set1.setColor(Color.BLUE);
		set1.setCircleColor(Color.BLUE);
		set1.setLineWidth(1f);
		set1.setCircleSize(3f);
		set1.setDrawCircleHole(false);
		set1.setValueTextSize(9f);
		set1.setFillAlpha(65);
		set1.setFillColor(Color.BLUE);
		set1.setAxisDependency(YAxis.AxisDependency.LEFT); // 以左边坐标轴为准
															// 还是以右边坐标轴为基准

		LineDataSet set2 = new LineDataSet(yVals1, "濕度");
		set2.setDrawValues(false);
		set2.setColor(Color.BLACK);
		set2.setCircleColor(Color.BLACK);
		set2.setLineWidth(1f);
		set2.setCircleSize(3f);
		set2.setDrawCircleHole(false);
		set2.setValueTextSize(9f);
		set2.setFillAlpha(65);
		set2.setFillColor(Color.BLACK);
		set2.setAxisDependency(YAxis.AxisDependency.RIGHT);

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set1); // add the datasets
		dataSets.add(set2);

		// create a data object with the datasets
		LineData data = new LineData(xVals, dataSets);
		// set data
		mChart.setData(data);
		mChart.animateXY(3000, 2000);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			int j = 0;// 排的序列
			for (String key : keySet) {
				if (key.equalsIgnoreCase(MyConstant.GET_MFG_FLOOR_TUBIAO)) {
					// 數據遍歷
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						floorStr = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							floorStr[i - 1] = result.get(i).toString();
						}
						floorSP.setAdapter(new ArrayAdapter(context,
								android.R.layout.simple_dropdown_item_1line,
								floorStr));
						floorName = floorSP.getSelectedItem().toString();
						start.getServerData(0, MyConstant.GET_MFG_LINE_TUBIAO,
								user.getMFG(), floorName ,reportName);
					}

				} else if (key.equalsIgnoreCase(MyConstant.GET_MFG_LINE_TUBIAO)) {
					// 數據遍歷
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						lineStr = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							lineStr[i - 1] = result.get(i).toString();
						}
						lineSP.setAdapter(new ArrayAdapter(context,
								android.R.layout.simple_dropdown_item_1line,
								lineStr));
						lineName = lineSP.getSelectedItem().toString();
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "無線體或設備");
					}
				} else if (key.equalsIgnoreCase(MyConstant.GET_CHART_DATA)) {
					// 數據遍歷
					result = msg.getData().getStringArrayList(key);
					if (temperatureList.size() > 0 || humidityList.size() > 0) {
						temperatureList.clear();
						humidityList.clear();
						dataList.clear();
						dateList.clear();
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 5) {
							chartBean = new ChartBean();
							chartBean.setProID(result.get(i + 2).toString());
							chartBean.setCheckContent(result.get(i + 3)
									.toString());
							chartBean
									.setCheckDate(result.get(i + 4).toString());
							dataList.add(chartBean);
						}
						chartBean = new ChartBean();
						chartBean.setProID(NO_DATA);
						dataList.add(chartBean);
						for (int i = 0; i < dataList.size() - 1; i++) {
							if (dataList.get(i).getProID().equals("1")
									&& !(dataList.get(i + 1).getProID()
											.equals("1"))) {
								temperatureList.add(dataList.get(i)
										.getCheckContent());
								dateList.add(dataList.get(i).getCheckDate());
							} else if (dataList.get(i).getProID().equals("1")
									&& (dataList.get(i + 1).getProID()
											.equals("1"))) {
								temperatureList.add(dataList.get(i)
										.getCheckContent());
								humidityList.add("0");
								dateList.add(dataList.get(i).getCheckDate());
							} else if (dataList.get(i).getProID().equals("2")
									&& (dataList.get(i + 1).getProID()
											.equals("2"))) {
								humidityList.add(dataList.get(i)
										.getCheckContent());
								temperatureList.add("0");
								dateList.add(dataList.get(i).getCheckDate());
							} else if (dataList.get(i).getProID().equals("2")
									&& !(dataList.get(i + 1).getProID()
											.equals("2"))) {
								humidityList.add(dataList.get(i)
										.getCheckContent());
							}
						}
					}
					setDoubleData();
					mChart.invalidate();
				}
			}
		};
	};
	
	private void initDays() {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			day = 31;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			day = 30;
		} else if ((month == 2 && year % 4 == 0 && year % 100 != 0)
				|| (month == 2 && year % 400 == 0)) {
			day = 29;
		} else {
			day = 28;
		}
	}


	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub

	}

	// 返回键按钮
	public void activity_back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onChartGestureStart(MotionEvent me,
			ChartGesture lastPerformedGesture) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChartGestureEnd(MotionEvent me,
			ChartGesture lastPerformedGesture) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChartLongPressed(MotionEvent me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChartDoubleTapped(MotionEvent me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChartSingleTapped(MotionEvent me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChartTranslate(MotionEvent me, float dX, float dY) {
		// TODO Auto-generated method stub

	}

}
