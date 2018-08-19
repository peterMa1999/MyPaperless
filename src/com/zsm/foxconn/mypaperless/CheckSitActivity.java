package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import net.tsz.afinal.FinalDb;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.CheckSituationActivity.MyAdapter;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.CheckSit;
import com.zsm.foxconn.mypaperless.bean.ChildModel;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.database.APP_yindao_page;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.refreshlistview.OnRefreshListener;
import com.zsm.foxconn.mypaperless.refreshlistview.RefreshListView;
import com.zsm.foxconn.mypaperless.util.GuideUtil;
import com.zsm.foxconn.mypaperless.wheeltime.ScreenInfo;
import com.zsm.foxconn.mypaperless.wheeltime.WheelMain;

/**
 * 
 * @author MPG
 * 
 *         2016-12-22 下午1:47:01 CheckSitActivity 點檢狀況-已點檢、未點檢、責任主管
 */

public class CheckSitActivity extends BaseActivity {

	private UserBean user;
	private HttpStart start;
	private Context context = CheckSitActivity.this;
	RefreshListView examineListView;
	private Button homebtn;
	MyAdapter adapter;
	private int year, day, month;
	private String timeDate, shift = "DN", floor_name, line_name="NA",
			sp_sitselect_yeild = "other", sp_sitbanbie = "NA";
	private EditText startET;
	private WheelMain wheelMain;
	private List<CheckSit> listexaminall = new ArrayList<CheckSit>();
	private int datamore = 100;// datamore每次多刷新加载的数据
	private Spinner sp_sitselect_type, sp_sitbanbie_type, sp_sitfloor,
			sp_sitline;
	private String[] floorstr = null, linestr = null;
	private PopupWindow popuWindow;
	private View contentView;
	private boolean isfirst = true,isfirst2 = true,isfirst3 = true,isfirst4 = true;
	private FinalDb finalDb = null;
	private GuideUtil guideUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checksitactivity);
		user = (UserBean) getApplicationContext();
		CheckLogin();
		findViewById();
		finalDb = FinalDb.create(context, "child"); //创建数据库
		List<APP_yindao_page> list_app = finalDb.findAllByWhere(APP_yindao_page.class, "pagename='CheckSitActivity' and pagename_id='0'");
		if (list_app.size()==0){
			guideUtil = GuideUtil.getInstance();
			guideUtil.initGuide(this, R.drawable.checksitactivity_1,"CheckSitActivity", 0);
		}
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		timeDate = year + "/" + (month + 1) + "/" + day;
		startET.setText(timeDate);
		start = new HttpStart(context, handler);
		start.getServerData(0, MyConstant.Access_floors, user.getBU(),
				user.getMFG());
		initView();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = msg.getData().getStringArrayList(key);
				if (key.equals(MyConstant.Access_floors)) {
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						floorstr = new String[result.size()-1];
						for (int i = 1, j = 0; i < result.size(); i += 1, j++) {
							floorstr[j] = result.get(i).toString().trim();
						}
						sp_sitfloor.setAdapter(new ArrayAdapter(
								CheckSitActivity.this,
								android.R.layout.simple_dropdown_item_1line,
								floorstr));
						floor_name = sp_sitfloor.getSelectedItem().toString();
						start.getServerData(3, MyConstant.Gets_the_line_style,
								floor_name, user.getMFG().toString());
					} else {
						UIHelper.ToastMessage(context, "没有数据");
					}
				}

				// 获取线体
				if (key.equalsIgnoreCase(MyConstant.Gets_the_line_style)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						linestr = new String[(result.size()-1)/4];
						for (int i = 1, j = 0; i < result.size(); i += 4, j++) {
							linestr[j] = result.get(i).toString().trim();
						}

						sp_sitline.setAdapter(new ArrayAdapter(
								CheckSitActivity.this,
								android.R.layout.simple_dropdown_item_1line,
								linestr));
						line_name = sp_sitline.getSelectedItem().toString();
					} else {
						Toast.makeText(getApplicationContext(), "没有数据",
								Toast.LENGTH_SHORT).show();
					}
				}
				if (key.equals(MyConstant.GET_CHECK_SITUATION)) {
					if (listexaminall.size() > 0) {
						listexaminall.clear();
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 13) {
							CheckSit checkSituation = new CheckSit(
									result.get(i).toString().trim(), result
											.get(i + 1).toString().trim(),
									result.get(i + 2).toString().trim(), result
											.get(i + 3).toString().trim(),
									result.get(i + 4).toString().trim(), result
											.get(i + 5).toString().trim(),
									result.get(i + 6).toString().trim(), result
											.get(i + 7).toString().trim(),
									result.get(i + 8).toString().trim(),
									result.get(i + 9).toString().trim(),
									result.get(i + 10).toString().trim(),
									result.get(i + 11).toString().trim(),
									result.get(i + 12).toString().trim());
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
			start.getServerData(0, MyConstant.GET_CHECK_SITUATION,sp_sitselect_yeild,user.getBU(),
					user.getMFG(),startET.getText().toString().trim(),sp_sitbanbie,line_name);
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
					getApplicationContext(), R.layout.refreshlv_checksit_item,
					null);
			CheckSit listex = listexaminall.get(position);

			final LinearLayout refreshlv_linear = (LinearLayout) item
					.findViewById(R.id.refreshlv_linear);
			final TextView sit_refreshlv_no = (TextView) item
					.findViewById(R.id.sit_refreshlv_no);
			final TextView sit_refreshlv_report_name = (TextView) item
					.findViewById(R.id.sit_refreshlv_report_name);
			final TextView sit_refreshlv_equipment = (TextView) item
					.findViewById(R.id.sit_refreshlv_equipment);
			final TextView sit_refreshlv_duty = (TextView) item
					.findViewById(R.id.sit_refreshlv_duty);
			final TextView sit_refreshlv_yeild = (TextView) item
					.findViewById(R.id.sit_refreshlv_yeild);
			final TextView sit_refreshlv_checkline = (TextView) item
					.findViewById(R.id.sit_refreshlv_checkline);
			sit_refreshlv_no.setText(position + 1 + "");
			sit_refreshlv_report_name.setText(listex.getReport_name());
			sit_refreshlv_checkline.setText(listex.getLine_name());
			sit_refreshlv_equipment.setText(listex.getEquipment());
			sit_refreshlv_yeild.setText(listex.getYeild_name());
			sit_refreshlv_duty.setText(listex.getDuty_chinesename());
			if (listex.getCheck_flag().equalsIgnoreCase("0")) {
				refreshlv_linear.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.jbshape));
			} else {
				refreshlv_linear.setBackgroundDrawable(null);
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
		examineListView = (RefreshListView) findViewById(R.id.refreshlv_checksit);
		startET = (EditText) findViewById(R.id.et_sit_time);
		sp_sitfloor = (Spinner) findViewById(R.id.sp_sitfloor);
		sp_sitselect_type = (Spinner) findViewById(R.id.sp_sitselect_type);
		sp_sitbanbie_type = (Spinner) findViewById(R.id.sp_sitbanbie_type);
		sp_sitline = (Spinner) findViewById(R.id.sp_sitline);

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		adapter = new MyAdapter();
		examineListView.setAdapter(adapter);
		sp_sitfloor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (isfirst2) {
					isfirst2 = false;
				}else {
					floor_name = arg0.getItemAtPosition(arg2).toString();
					start.getServerData(3, MyConstant.Gets_the_line_style,
							floor_name, user.getMFG().toString());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		sp_sitline.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (isfirst) {
					isfirst=false;
				}else {
					line_name = arg0.getItemAtPosition(arg2).toString();
					start.getServerData(0, MyConstant.GET_CHECK_SITUATION,
							sp_sitselect_yeild, user.getBU(), user.getMFG(),
							startET.getText().toString().trim(), sp_sitbanbie,
							line_name);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		sp_sitselect_type
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (arg2 == 0) {
							sp_sitselect_yeild = "other";
							sp_sitbanbie_type.setEnabled(true);
						} else if (arg2 == 1) {
							sp_sitselect_yeild = "day";
							sp_sitbanbie_type.setEnabled(false);
							sp_sitbanbie_type.setSelection(0);
						} else if (arg2 == 2) {
							sp_sitselect_yeild = "week";
							sp_sitbanbie_type.setEnabled(false);
							sp_sitbanbie_type.setSelection(0);
						} else if (arg2 == 3) {
							sp_sitselect_yeild = "month";
							sp_sitbanbie_type.setEnabled(false);
							sp_sitbanbie_type.setSelection(0);
						} else if (arg2 == 4) {
							sp_sitselect_yeild = "quarter";
							sp_sitbanbie_type.setEnabled(false);
							sp_sitbanbie_type.setSelection(0);
						}
						if (isfirst3) {
							isfirst3 = false;
						}else {
							start.getServerData(0, MyConstant.GET_CHECK_SITUATION,
									sp_sitselect_yeild, user.getBU(),
									user.getMFG(), startET.getText().toString()
									.trim(), sp_sitbanbie, line_name);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		sp_sitbanbie_type
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (arg2 == 0) {
							sp_sitbanbie = "NA";
						} else if (arg2 == 1) {
							sp_sitbanbie = "D";
						} else if (arg2 == 2) {
							sp_sitbanbie = "N";
						}
						
						if (isfirst4) {
							isfirst4 = false;
						}else {
							start.getServerData(0, MyConstant.GET_CHECK_SITUATION,
									sp_sitselect_yeild, user.getBU(),
									user.getMFG(), startET.getText().toString()
									.trim(), sp_sitbanbie, line_name);
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
		
		examineListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// 震动
				Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(new long[] { 0, 100 }, -1);// -1不重复
				String str = "報表編號："+listexaminall.get(arg2-1).getReport_num()+" \t"+
						"二維碼編號："+listexaminall.get(arg2-1).getQr_image_info();
				initPopuWindow(arg1, str);
				return false;
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
		ScreenInfo screenInfo = new ScreenInfo(CheckSitActivity.this);

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
	
	private void initPopuWindow(View parent, String str) {
		if (popuWindow == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(this);
			contentView = mLayoutInflater.inflate(R.layout.popuwindow, null);
			popuWindow = new PopupWindow(contentView,
					WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.WRAP_CONTENT);
		}

		ColorDrawable cd = new ColorDrawable(0x000000);
		popuWindow.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.4f;
		getWindow().setAttributes(lp);

		popuWindow.setOutsideTouchable(true);
		popuWindow.setFocusable(true);
		popuWindow.setAnimationStyle(R.style.AppBaseTheme);
		popuWindow.showAtLocation((View) parent.getParent(), Gravity.CENTER
				| Gravity.CENTER_HORIZONTAL, 0, 0);

		popuWindow.update();
		TextView brokemate = (TextView) contentView
				.findViewById(R.id.brokemate);
		brokemate.setText(str);
		popuWindow.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
	}
	
	// 返回键按钮
		public void activity_back(View v) {
			this.finish();
			overridePendingTransition(R.anim.move_left_in_activity,
					R.anim.move_right_out_activity);
		}	
	
}
