package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.MyMarkerView_AnotherBar;
import com.zsm.foxconn.mypaperless.util.MyYAxisValueFormatter;
import com.zsm.foxconn.mypaperless.wheeltime.ScreenInfo;
import com.zsm.foxconn.mypaperless.wheeltime.WheelMain;

public class AnotherBarActivity extends BaseActivity
		 {

	private TextView bartitle_txt;
	private ImageView imageview_add;
	private Context context = AnotherBarActivity.this;
	private UserBean userBean;
	private HttpStart httpStart;
	private BarChart mChart;
	private WheelMain wheelMain;
	private String timeDate;
	private EditText startET;
	private Spinner sp_cycletype;
	private int year, day, month;
	private String Select_Type = "0";
	private Button shiyonglv_bt;
	private List<String> x_listdata = null, y_listdata = null,zongshu_list=null,cycletype_list = null;
	private List<String> x_list = new ArrayList<String>();		//使用報表的MFG
	private List<String> y_list = new ArrayList<String>();		//MFG已使用的報表數目
	private boolean isfirst = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_barchart);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		findViewById();
		initView();
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		timeDate = year + "/" + (month+1) + "/" + day;
		startET.setText(timeDate);
		mChart.setPinchZoom(false);

		mChart.setDrawBarShadow(false);
		mChart.setDrawGridBackground(false);

		XAxis xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setSpaceBetweenLabels(0);
		xAxis.setDrawGridLines(false);
		
		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.removeAllLimitLines(); // reset all limit lines to avoid
		// overlapping lines
		leftAxis.setStartAtZero(false);
		// leftAxis.setYOffset(20f);
		leftAxis.enableGridDashedLine(10f, 10f, 0f);
		leftAxis.setValueFormatter(new MyYAxisValueFormatter("shiyonglv"));
		// limit lines are drawn behind data (and not on top)
		leftAxis.setDrawLimitLinesBehindData(true);
		
		YAxis rightAxis = mChart.getAxisRight();
		rightAxis.setEnabled(false);
		
		mChart.getAxisLeft().setDrawGridLines(false);

		// add a nice and smooth animation
		mChart.animateY(2500);

		mChart.getLegend().setEnabled(false);
		httpStart = new HttpStart(context, handler);
		httpStart.getServerData(3, MyConstant.GET_REPORT_USAGE, Select_Type,
				timeDate,userBean.getLogonName());
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = msg.getData().getStringArrayList(key);
				if (key.equals(MyConstant.GET_REPORT_USAGE)) {
					x_listdata = new ArrayList<String>();
					y_listdata = new ArrayList<String>();
					if (y_list.size()>0||y_listdata.size()>0||x_listdata.size()>0||x_list.size()>0) {
						y_list.clear();
						x_list.clear();
						y_listdata.clear();
						x_listdata.clear();
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 4) {
							x_list.add(result.get(i + 3).toString().trim());
						}
						Set set = new HashSet();
						List newList = new ArrayList();
						for (String cd : x_list) {
							if (set.add(cd)) {
								x_listdata.add(cd);
							}
						}
						String str = "";
						for (int j = 0; j < x_listdata.size(); j++) {
							int i = 0;
							for (int j2 = 0; j2 < x_list.size(); j2++) {
								if (x_list.get(j2).equalsIgnoreCase(
										x_listdata.get(j))) {
									i++;
								}
							}
							str += x_listdata.get(j).toString() + MyConstant.GET_FLAG1;
							y_list.add(i + "");
						}
						httpStart.getServerData(3,
								MyConstant.GET_BU_REPORT_NUM,
								userBean.getSite(), userBean.getBU(), str);
					}

					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						y_listdata.clear();
						x_listdata.clear();
						onProgressChanged();
						mChart.invalidate();
						UIHelper.ToastMessage(context, "無使用數據");
					}
				}
				if (key.equals(MyConstant.GET_BU_REPORT_NUM)) {
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						zongshu_list = new ArrayList<String>();
						cycletype_list = new ArrayList<String>();
						for (int i = 1; i < result.size(); i++) {
							zongshu_list.add(result.get(i).toString());
						}
						if (Select_Type.equals("0")) {
							for (int j = 0; j < zongshu_list.size(); j+=5) {
								cycletype_list.add(zongshu_list.get(j+1).toString());
							}
						}else if (Select_Type.equals("1")) {
							for (int j = 0; j < zongshu_list.size(); j+=5) {
								cycletype_list.add(zongshu_list.get(j+2).toString());
							}
						}else if (Select_Type.equals("2")) {
							for (int j = 0; j < zongshu_list.size(); j+=5) {
								cycletype_list.add(zongshu_list.get(j+3).toString());
							}
						}else if (Select_Type.equals("3")) {
							for (int j = 0; j < zongshu_list.size(); j+=5) {
								cycletype_list.add(zongshu_list.get(j+4).toString());
							}
						}
						
						for (int j = 0; j < x_listdata.size(); j++) {
							y_listdata.add((Float.parseFloat(y_list.get(j).toString())/Float.parseFloat(cycletype_list.get(j).toString()))+"");
						}
						onProgressChanged();
						mChart.invalidate();
						mChart.animateY(2000);
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "無報表總數");
					}
				}
			}
		}
	};

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		if (userBean.getLogonName() == null
				|| userBean.getLogonName().length() == 0) {
			android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// tiemTimelistenresiger.stopthread();
					AppManager.getInstance().killAllActivity();
					startActivity(new Intent(context, LoginActivity.class));
				}
			};
			Builder builder = new Builder(context);
			builder.setTitle("您还未登陆，请先登录");
			builder.setPositiveButton("确定", listener);
			builder.create().show();
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		bartitle_txt = (TextView) findViewById(R.id.bartitle_txt);
		bartitle_txt.setText(userBean.getBU() + "報表使用率");
		imageview_add = (ImageView) findViewById(R.id.imageview_add);
		imageview_add.setBackgroundResource(R.drawable.xiangxi_click_seletor);
		shiyonglv_bt = (Button) findViewById(R.id.shiyonglv_bt);
		mChart = (BarChart) findViewById(R.id.chart1);
		mChart.setDescription("");
		mChart.setMaxVisibleValueCount(60);
		startET = (EditText) findViewById(R.id.et_barchart_time);
		sp_cycletype = (Spinner) findViewById(R.id.sp_cycletype);
		// scaling can now only be done on x- and y-axis separately

	}

	public void getTime() {
		LayoutInflater inflater = LayoutInflater.from(context);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(AnotherBarActivity.this);

		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		wheelMain.initDateTimePicker(year, month, day);

		new AlertDialog.Builder(context).setTitle("选择日期")
				.setView(timepickerview)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						timeDate = wheelMain.getTime().toString().trim();
						startET.setText(timeDate);
					}
				}).setNegativeButton("取消", null).show();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
		startET.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getTime();
			}
		});
		sp_cycletype.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				if (isfirst) {
					if (arg2==0) {
						Select_Type="0";
					}else if (arg2==1) {
						Select_Type="1";
					}else if (arg2==2) {
						Select_Type="2";
					}else if (arg2==3) {
						Select_Type="3";
					}
				}else {
					isfirst = true;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		imageview_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AnotherBarActivity.this,CheckSituationActivity.class);
				startActivity(intent);
			}
		});
		shiyonglv_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (startET.getText().length()!=0) {
					httpStart.getServerData(3, MyConstant.GET_REPORT_USAGE, Select_Type,
							startET.getText().toString(),userBean.getLogonName());
				}else {
					UIHelper.ToastMessage(context, "日期為空");
				}
			}
		});
	}

	// 返回键按钮
	public void activity_back(View v) {
		this.finish();
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
	}

	public void onProgressChanged() {
		// TODO Auto-generated method stub

		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
			try {
				for (int i = 0; i < y_listdata.size(); i++) {
					String str = "";
					if (y_listdata.get(i)
							.toString().length()>5) {
						str = (y_listdata.get(i)
								.toString()).substring(0, 5);
						
					}else {
						str = y_listdata.get(i)
								.toString();
					}
					
					yVals1.add(new BarEntry(Float.parseFloat(str)*100, i));
				}

				ArrayList<String> xVals = new ArrayList<String>();
				for (int i = 0; i < x_listdata.size(); i++) {
					xVals.add(x_listdata.get(i).toString() + "");
				}
				
				MyMarkerView_AnotherBar mv = new MyMarkerView_AnotherBar(this, R.layout.custom_marker_view,
						y_list, cycletype_list);
				// set the marker to the chart
				mChart.setMarkerView(mv);
				
				BarDataSet set1 = new BarDataSet(yVals1, "Data Set");
				set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
				set1.setDrawValues(true);

				ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
				dataSets.add(set1);

				BarData data = new BarData(xVals, dataSets);

				mChart.setData(data);
			} catch (Exception e) {
				// TODO: handle exception
			}

	}

//	@Override
//	protected void onRestart() {
//		// TODO Auto-generated method stub
//		super.onRestart();
//		httpStart.getServerData(3, MyConstant.GET_REPORT_USAGE, Select_Type,
//				timeDate,userBean.getLogonName());
//		
//	}

}
