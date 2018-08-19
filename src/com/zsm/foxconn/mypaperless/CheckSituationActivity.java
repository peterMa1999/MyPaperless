package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.CheckSituation;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.refreshlistview.OnRefreshListener;
import com.zsm.foxconn.mypaperless.refreshlistview.RefreshListView;
import com.zsm.foxconn.mypaperless.wheeltime.ScreenInfo;
import com.zsm.foxconn.mypaperless.wheeltime.WheelMain;


/**
 * 點檢基本狀況-次數統計
 */
public class CheckSituationActivity extends BaseActivity {

	private UserBean user;
	private HttpStart start;
	private Context context = CheckSituationActivity.this;
	RefreshListView examineListView;
	private Button homebtn, situation_bt;
	MyAdapter adapter;
	private int year, day, month;
	private String timeDate, shift = "DN";
	private EditText startET;
	private WheelMain wheelMain;
	private List<CheckSituation> listexaminall = new ArrayList<CheckSituation>();
	private int datamore = 100;// datamore每次多刷新加载的数据
	private Spinner sp_situation_type;
	private boolean isfirst = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.checksituationactivity);
		user = (UserBean) getApplicationContext();
		CheckLogin();
		start = new HttpStart(context, handler);
		findViewById();
		initView();
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		timeDate = year + "/" + (month + 1) + "/" + day;
		startET.setText(timeDate);
		start.getServerData(0, MyConstant.GET_DETAILED_CHECK_LIST, datamore
				+ "", user.getMFG(), timeDate, shift);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = msg.getData().getStringArrayList(key);
				if (key.equals(MyConstant.GET_DETAILED_CHECK_LIST)) {
					if (listexaminall.size() > 0) {
						listexaminall.clear();
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 9) {
							CheckSituation checkSituation = new CheckSituation(
									result.get(i).toString().trim(), result
											.get(i + 1).toString().trim(),
									result.get(i + 2).toString().trim(), result
											.get(i + 3).toString().trim(),
									result.get(i + 4).toString().trim(), result
											.get(i + 5).toString().trim(),
									result.get(i + 6).toString().trim(), result
											.get(i + 7).toString().trim(),
									result.get(i + 8).toString().trim());
							listexaminall.add(checkSituation);
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無點檢信息", 5000);
					}
					adapter.notifyDataSetChanged();
				}
				if (key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(context, "未連接服務器");
				}
			}
		}
	};

	// 适配器，显示查询的信息
	class MyAdapter extends BaseAdapter {

		public void getRefreshData() {
			start.getServerData(0, MyConstant.GET_DETAILED_CHECK_LIST, datamore
					+ "", user.getMFG(), timeDate, shift);
		}

		public int getCount() {
			return listexaminall.size();
		}

		public Object getItem(int position) {
			return listexaminall.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View item = convertView != null ? convertView : View.inflate(
					getApplicationContext(),
					R.layout.refreshlv_checksituation_item, null);
			CheckSituation listex = listexaminall.get(position);

			final TextView refreshlv_no = (TextView) item
					.findViewById(R.id.refreshlv_no);
			final TextView refreshlv_report_name = (TextView) item
					.findViewById(R.id.refreshlv_report_name);
			final TextView refreshlv_floor = (TextView) item
					.findViewById(R.id.refreshlv_floor);
			final TextView refreshlv_create_by = (TextView) item
					.findViewById(R.id.refreshlv_create_by);
			final TextView refreshlv_checknum = (TextView) item
					.findViewById(R.id.refreshlv_checknum);
			final TextView refreshlv_checkshift = (TextView) item.findViewById(R.id.refreshlv_checkshift);
			refreshlv_no.setText(position + 1 + "");
			refreshlv_report_name.setText(listex.getReport_name());
			refreshlv_floor.setText(listex.getFloor_name() + "  "
					+ listex.getLine_name());
			refreshlv_create_by.setText(listex.getCreateby_name());
			refreshlv_checknum.setText(listex.getCheck_num());
			refreshlv_checkshift.setText(listex.getShift_name());
			if (listex.getShift_name().equalsIgnoreCase("N")) {
				refreshlv_checkshift.setBackgroundDrawable(getResources().getDrawable(R.drawable.jbshape));
			}else {
				refreshlv_checkshift.setBackgroundDrawable(null);
			}
			
			return item;
		}

	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		// 检查账号是否登入
		if (user.getLogonName() == null || user.getLogonName().length() == 0) {
			android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
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
		homebtn = (Button) findViewById(R.id.btn_submit);
		homebtn.setVisibility(View.GONE);
		examineListView = (RefreshListView) findViewById(R.id.refreshlv_checksituation);
		startET = (EditText) findViewById(R.id.et_situation_time);
		situation_bt = (Button) findViewById(R.id.situation_bt);
		sp_situation_type = (Spinner) findViewById(R.id.sp_situation_type);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		adapter = new MyAdapter();
		examineListView.setAdapter(adapter);
		sp_situation_type
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (isfirst) {
							if (arg2 == 0) {
								shift = "DN";
							} else if (arg2 == 1) {
								shift = "D";
							} else if (arg2 == 2) {
								shift = "N";
							}
						} else {
							isfirst = true;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		startET.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getTime();
			}
		});
		situation_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (startET.getText().length() != 0) {
					start.getServerData(0, MyConstant.GET_DETAILED_CHECK_LIST,
							datamore + "", user.getMFG(), timeDate, shift);
				} else {
					UIHelper.ToastMessage(context, "日期為空");
				}
			}
		});
		// 查询所得点检报表点检后详情
		examineListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				UIHelper.ToastMessage(context, listexaminall.get(arg2-1)
						.getFloor_name()+"-"+listexaminall.get(arg2-1).getLine_name());
			}
		});
		examineListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// 异步查询数据
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// SystemClock.sleep(500);
						datamore = 90;
						adapter.getRefreshData();
						return null;
					}

					protected void onPostExecute(Void result) {
						adapter.notifyDataSetChanged();
						// 隐藏头布局
						examineListView.onRefreshFinish();
					}
				}.execute(new Void[] {});
			}

			@Override
			public void onLoadMoring() {
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// SystemClock.sleep(500);
						datamore += 100;
						adapter.getRefreshData();
						// listViewData.add(new
						// message("加载刷新出来BaseAnimation1"));
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						examineListView.onRefreshFinish();
					}

				}.execute(new Void[] {});
			}
		});
	}

	public void getTime() {
		LayoutInflater inflater = LayoutInflater.from(context);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(CheckSituationActivity.this);

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

	// 返回键按钮
	public void activity_back(View v) {
		this.finish();
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
	}
}
