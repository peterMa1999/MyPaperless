package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.Canshu_bean;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.refreshlistview.OnRefreshListener;
import com.zsm.foxconn.mypaperless.refreshlistview.RefreshListView;
import com.zsm.qr.CaptureActivity;

public class Canshu_Manage extends BaseActivity {
	private Context context = Canshu_Manage.this;
	private ImageView imageview_add, imageview_xiaoxi;
	private TextView title, tv_canshu_mynews, canshu_pop_jizhong,
			canshu_pop_wo;
	private Intent intent;
	private String bUname, isaccess, reportid, reportName, section, issection;
	private UserBean user;
	private RefreshListView examineListView;
	private List<Canshu_bean> listexaminall = new ArrayList<Canshu_bean>();
	private MyAdapter adapter;
	private int style = 3, datamore = 50;// datamore每次多刷新加载的数据
	private HttpStart httpStart;
	private String[] linestr = null, floorstr = null;
	private String floor, line, selecttype = "1";
	private Spinner sp_line, sp_floor;
	private EditText canshu_jizhong;
	private Button canshu_all_imagebt, canshu_scanQR;
	private final int SCANER_CODE = 1, SCANER_CODE_TWO = 2;
	LinearLayout layout_canshu_seach;
	PopupWindow window;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.canshu_manage);
		user = (UserBean) getApplicationContext();
		CheckLogin();
		intent = getIntent();
		bUname = intent.getStringExtra("bUname");
		isaccess = intent.getStringExtra("isaccess");
		reportid = intent.getStringExtra("reportid");
		reportName = intent.getStringExtra("reportName");
		section = intent.getStringExtra("section");
		issection = intent.getStringExtra("issection");
		findViewById();
		httpStart = new HttpStart(context, handler);
		httpStart.getServerData(3, MyConstant.GET_ALL_CANSHU, reportid,
				datamore + "", selecttype, user.getMFG());

		examineListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				intent = new Intent(context, Detailed_canshu.class);
				intent.putExtra("ischeck", "0");
				intent.putExtra("jizhong_num", listexaminall.get(arg2 - 1)
						.getJizhong_num());
				intent.putExtra("floo_name", listexaminall.get(arg2 - 1)
						.getFloor_name());
				intent.putExtra("line_name", listexaminall.get(arg2 - 1)
						.getLine_name());
				intent.putExtra("report_num", reportid);// 报表编号
				intent.putExtra("report_Name", reportName);// 报表名
				intent.putExtra("section", section);
				startActivity(intent);
			}
		});
		examineListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// 异步查询数据
				// new AsyncTask<Void, Void, Void>() {
				//
				// @Override
				// protected Void doInBackground(Void... params) {
				SystemClock.sleep(500);
				// style = 10;
				// datamore = 90;
				// adapter.getRefreshData();
				// return null;
				// }
				//
				// protected void onPostExecute(Void result) {
				// adapter.notifyDataSetChanged();
				// // 隐藏头布局
				// examineListView.onRefreshFinish();
				// }
				// }.execute();
				style = 10;
				datamore = 90;
				adapter.getRefreshData();
			}

			@Override
			public void onLoadMoring() {
				// new AsyncTask<Void, Void, Void>() {
				//
				// @Override
				// protected Void doInBackground(Void... params) {
				SystemClock.sleep(500);

				// adapter.getRefreshData();
				// // listViewData.add(new
				// // message("加载刷新出来BaseAnimation1"));
				// return null;
				// }
				//
				// @Override
				// protected void onPostExecute(Void result) {
				// super.onPostExecute(result);
				// examineListView.onRefreshFinish();
				// }
				//
				// }.execute(new Void[] {});
				style = 10;
				datamore += 30;
				adapter.getRefreshData();
			}
		});

		sp_floor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				floor = arg0.getItemAtPosition(arg2).toString();
				httpStart.getServerData(3, MyConstant.GET_CHECK_LINE,
						user.getMFG(), section, floor, user.getSBU().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		sp_line.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				line = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		canshu_all_imagebt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selecttype = "4";
				httpStart.getServerData(3, MyConstant.GET_ALL_CANSHU, reportid,
						datamore + "", selecttype, floor, line);
			}
		});
		canshu_pop_wo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selecttype = "3";
				httpStart.getServerData(3, MyConstant.GET_ALL_CANSHU, reportid,
						datamore + "", selecttype, floor, line, canshu_jizhong
								.getText().toString());
			}
		});
		SystemClock.sleep(500);
		httpStart.getServerData(0, MyConstant.GET_FLOOR_IPQC,
				user.getLogonName(), section, user.getMFG(), user.getBU());
	}

	public void getdata(String str) {
		if (selecttype.equals("2") || selecttype.equals("3")) { // 按樓層查詢參數信息
			httpStart.getServerData(3, MyConstant.GET_ALL_CANSHU, reportid,
					datamore + "", selecttype, floor, line, canshu_jizhong
							.getText().toString());
		} else { // 查詢全部參數信息
			httpStart.getServerData(3, MyConstant.GET_ALL_CANSHU, reportid,
					datamore + "", selecttype, user.getMFG());
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			int j = 0;// 排的序列
			for (String key : keySet) {
				result = new ArrayList<String>();
				result = msg.getData().getStringArrayList(key);
				if (key.equalsIgnoreCase(MyConstant.GET_ALL_CANSHU)) {
					if (listexaminall.size() > 0) {
						listexaminall.clear();
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size() - 1; i += 4) {
							++j;
							Canshu_bean cs_bean = new Canshu_bean(j + "",
									result.get(i + 2).toString(), result.get(i)
											.toString(), result.get(i + 1)
											.toString(), result.get(i + 3)
											.toString());
							listexaminall.add(cs_bean);
						}
						if (j < datamore) {
							UIHelper.ToastMessage(context, "沒有更多了");
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無數據");
						adapter.notifyDataSetChanged();
						examineListView.onRefreshFinish();
						return;
					}
					adapter.notifyDataSetChanged();
					examineListView.onRefreshFinish();
					httpStart.getServerData(0, MyConstant.GET_CHECK_NEWS,
							"ALONE", reportid, user.getLogonName());
				}
				if (key.equalsIgnoreCase(MyConstant.GET_FLOOR_IPQC)) {
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						floorstr = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							floorstr[i - 1] = result.get(i).toString();
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無樓層信息");
						return;
					}
					sp_floor.setAdapter(new ArrayAdapter(Canshu_Manage.this,
							android.R.layout.simple_dropdown_item_1line,
							floorstr));
					floor = sp_floor.getSelectedItem().toString();
					httpStart
							.getServerData(3, MyConstant.GET_CHECK_LINE, user
									.getMFG(), section, floor, user.getSBU()
									.toString());
				}
				if (key.equalsIgnoreCase(MyConstant.GET_CHECK_LINE)) {
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						linestr = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							linestr[i - 1] = result.get(i).toString();
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						// linestr = new String[result.size()];
						// linestr[0] = "無";
						UIHelper.ToastMessage(context, "暫無線別信息");
						return;
					}
					sp_line.setAdapter(new ArrayAdapter(Canshu_Manage.this,
							android.R.layout.simple_dropdown_item_1line,
							linestr));
					line = sp_line.getSelectedItem().toString();
				}
				if (key.equalsIgnoreCase(MyConstant.GET_CHECK_NEWS)) {
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						int m = 0;
						for (int i = 1; i < result.size(); i++) {
							m += Integer.parseInt(result.get(i).toString());
						}
						if (m == 0) {
							tv_canshu_mynews.setVisibility(View.GONE);
						} else {
							tv_canshu_mynews.setVisibility(View.VISIBLE);
							tv_canshu_mynews.setText("" + m);
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						tv_canshu_mynews.setVisibility(View.GONE);
						return;
					}
				}
				if (key.equalsIgnoreCase(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(context, "未連接服務器");
				}
			}
		}
	};

	// 适配器，显示查询的信息
	class MyAdapter extends BaseAdapter {

		public void getRefreshData() {
			getdata(selecttype);
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
					R.layout.examine_info_listview_item, null);
			Canshu_bean listex = listexaminall.get(position);
			String sataend = null;

			final TextView info_nb_tv = (TextView) item
					.findViewById(R.id.info_nb_tv);
			final TextView info_jizhong_numbertv = (TextView) item
					.findViewById(R.id.info_jizhong_numbertv);
			final TextView info_floor_tv = (TextView) item
					.findViewById(R.id.info_floor_tv);
			final TextView info_datatime_tv = (TextView) item
					.findViewById(R.id.info_datatime_tv);
			final TextView info_line_tv = (TextView) item
					.findViewById(R.id.info_line_tv);
			info_nb_tv.setText(listex.getNumberorder());
			info_jizhong_numbertv.setText(listex.getJizhong_num());
			info_floor_tv.setText(listex.getFloor_name());
			info_datatime_tv.setText(listex.getCreate_date());
			info_line_tv.setText(listex.getLine_name());

			return item;
		}

	}

	/**
	 * 获取当前点击位置是否为et
	 * 
	 * @param view
	 *            焦点所在View
	 * @param event
	 *            触摸事件
	 * @return
	 */
	public boolean isClickEt(View view, MotionEvent event) {
		if (view != null && (view instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			view.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			// 此处根据输入框左上位置和宽高获得右下位置
			int bottom = top + view.getHeight();
			int right = left + view.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 點擊EditText以外的區域后鍵盤隱藏
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			// 获取当前获得当前焦点所在View
			View view = getCurrentFocus();
			if (isClickEt(view, event)) {

				// 如果不是edittext，则隐藏键盘

				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (inputMethodManager != null) {
					// 隐藏键盘
					inputMethodManager.hideSoftInputFromWindow(
							view.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(event);
		}
		/**
		 * 看源码可知superDispatchTouchEvent 是个抽象方法，用于自定义的Window
		 * 此处目的是为了继续将事件由dispatchTouchEvent(MotionEvent
		 * event)传递到onTouchEvent(MotionEvent event) 必不可少，否则所有组件都不能触发
		 * onTouchEvent(MotionEvent event)
		 */
		if (getWindow().superDispatchTouchEvent(event)) {
			return true;
		}
		return onTouchEvent(event);
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		if (user.getLogonName() == null || user.getLogonName().length() == 0) {
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

		canshu_pop_jizhong = (TextView) findViewById(R.id.canshu_pop_jizhong);
		canshu_pop_wo = (TextView) findViewById(R.id.canshu_pop_wo);
		canshu_pop_jizhong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selecttype = "2";
				httpStart.getServerData(3, MyConstant.GET_ALL_CANSHU, reportid,
						datamore + "", selecttype, floor, line, canshu_jizhong
								.getText().toString());
			}
		});
		imageview_add = (ImageView) findViewById(R.id.imageview_add);
		imageview_xiaoxi = (ImageView) findViewById(R.id.imageview_xiaoxi);
		tv_canshu_mynews = (TextView) findViewById(R.id.tv_canshu_mynews);
		sp_floor = (Spinner) findViewById(R.id.sp_floor);
		sp_line = (Spinner) findViewById(R.id.sp_line);
		canshu_all_imagebt = (Button) findViewById(R.id.canshu_all_imagebt);
		canshu_scanQR = (Button) findViewById(R.id.canshu_scanQR);
		canshu_jizhong = (EditText) findViewById(R.id.canshu_jizhong);
		layout_canshu_seach = (LinearLayout) findViewById(R.id.layout_canshu_seach);
		title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(reportName);
		canshu_pop_wo = (TextView) findViewById(R.id.canshu_pop_wo);
		canshu_pop_jizhong = (TextView) findViewById(R.id.canshu_pop_jizhong);
		examineListView = (RefreshListView) findViewById(R.id.table_info_listView);
		adapter = new MyAdapter();
		examineListView.setAdapter(adapter);
		canshu_jizhong.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.toString().length() > 0) {
					layout_canshu_seach.setVisibility(View.VISIBLE);
					canshu_pop_jizhong.setText("搜機種" + "“" + s.toString() + "”");
					canshu_pop_wo.setText("搜工單" + "“" + s.toString() + "”");
				} else {
					layout_canshu_seach.setVisibility(View.GONE);
				}
			}
		});
	}

	public void add(View v) {
		switch (v.getId()) {
		case R.id.imageview_add:
			intent = new Intent(context, Detailed_update_add.class);
			intent.putExtra("operating", "1");
			intent.putExtra("report_num", reportid);
			intent.putExtra("report_Name", reportName);
			intent.putExtra("section", section);
			startActivity(intent);

			break;
		case R.id.imageview_xiaoxi:
			intent = new Intent(context, MyCanshu_news.class);
			intent.putExtra("report_num", reportid);
			intent.putExtra("report_Name", reportName);
			intent.putExtra("section", section);
			startActivity(intent);
			break;

		case R.id.canshu_scanQR:
			// 打开扫描界面扫描条形码或二维码
			Intent openCameraIntent = new Intent(context, CaptureActivity.class);
			startActivityForResult(openCameraIntent, SCANER_CODE);
			break;

		// 搜樓層+機種
		case R.id.canshu_pop_jizhong:
			// selecttype = "2";
			// httpStart.getServerData(3, MyConstant.GET_ALL_CANSHU, reportid,
			// datamore + "", selecttype, floor,
			// line,canshu_jizhong.getText().toString());
			// UIHelper.ToastMessage(context, "dsads");
			break;

		// 按在線工單查詢
		case R.id.canshu_pop_wo:
			// selecttype = "3";
			// httpStart.getServerData(3, MyConstant.GET_ALL_CANSHU, reportid,
			// datamore + "",selecttype ,canshu_jizhong.getText().toString());
			// UIHelper.ToastMessage(context, "dsadsfhfdhfdg");
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			if (requestCode == SCANER_CODE) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				canshu_jizhong.setText(scanResult);
			}
		}
	}

	// 返回键按钮
	public void activity_back(View v) {
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
		this.finish();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

}
