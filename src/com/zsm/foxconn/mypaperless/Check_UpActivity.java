package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.foxconn.selecttime.Get_Time;
import com.foxconn.selecttime.SelectTime;
import com.foxconn.selecttime.SelectTime.TimeDayListener;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.wheeltime.WheelMain;
import com.zsm.qr.CaptureActivity;

/**
 * 補點檢--->二維碼信息
 * 
 * @author F1330297
 * 
 */
public class Check_UpActivity extends BaseActivity implements OnClickListener {

	private UserBean user;
	HttpStart start = null;
	WheelMain wheelMain;
	private MyAdapter adapter = new MyAdapter();
	private TextView title;
	private Button homebtn;
	private final int SCANER_CODE = 1;
	Context context;
	private String[] valuename = { "floor_name", "line_name", "equipment",
			"report_num", "site_name", "mfg_name", "sbu_name", "report_name",
			"check_section", "is_input_order", "check_yeild", "image_info" };// 數據名
	private String[] valuenameDialog = { "day", "time" };// 數據名
	private Map<String, String> map;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> listDialog = new ArrayList<Map<String, String>>();
	private ListView listView;
	private int pos = 0;
	private String line = "",weihu = "";
	private Dialog dia;
	private Intent intent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_check_up);
		context = Check_UpActivity.this;
		Intent intent = getIntent();
		title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(intent.getStringExtra("TitleName"));
		weihu = intent.getStringExtra("weihu");
		// 獲取列表報表名
		user = (UserBean) getApplicationContext();
		start = new HttpStart(context, handler);
		CheckLogin();
		// initView();
		initData();
		findViewById();
	}

	/**
	 * 检查账号是否登录
	 */
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

	/**
	 * 初始化控件
	 */
	public void initView() {
		// start.getServerData(3, MyConstant.GET_QRCODEID_CHECK_UP,
		// "201611170128298483", user.getSite(), user.getSBU(),
		// user.getMFG());
		// Intent openCameraIntent = new Intent(context, CaptureActivity.class);
		// startActivityForResult(openCameraIntent, SCANER_CODE);
		// UIHelper.ToastMessage(context, "補點檢，掃碼查詢");
	}

	/**
	 * 獲取二維碼掃描數據
	 */
	public void initData() {
		// start.getServerData(3, MyConstant.GET_QRCODEID_CHECK_UP,
		// "2016101111540159330", user.getSite(), user.getSBU(),
		// user.getMFG());
		Toast toast = null;
		if (weihu.equals("1")) {
		    toast = Toast.makeText(getApplicationContext(), "提前維護",
					Toast.LENGTH_SHORT);
		}else {
			toast = Toast.makeText(getApplicationContext(), "補點檢，掃碼查詢",
					Toast.LENGTH_SHORT);
		}
		Intent openCameraIntent = new Intent(context, CaptureActivity.class);
		startActivityForResult(openCameraIntent, SCANER_CODE);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
	}

	@Override
	protected void findViewById() {
		listView = (ListView) findViewById(R.id.add_oldlistView);
		homebtn = (Button) findViewById(R.id.btn_submit);
		homebtn.setBackgroundResource(R.drawable.erweima_click_seletor);
		homebtn.setOnClickListener(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			if (requestCode == SCANER_CODE) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				start.getServerData(3, MyConstant.GET_QRCODEID_CHECK_UP,
						scanResult, user.getSite(), user.getSBU(),
						user.getMFG());
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_submit:
			initData();
			break;

		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.equals(MyConstant.GET_QRCODEID_CHECK_UP)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						list.clear();
						for (int i = 1; i < result.size(); i += 12) {
							map = new HashMap<String, String>();
							for (int k = 0; k < valuename.length; k++) {
								map.put(valuename[k], result.get(i + k));
							}
							list.add(map);
						}
						setListView();
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "二維碼信息錯誤");
						return;
					}
				}
				if (key.equals(MyConstant.GET_QRCODEID_CHECK_UP_TIME)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						listDialog.clear();
						for (int i = 1; i < result.size(); i += 12) {
							map = new HashMap<String, String>();
							for (int k = 0; k < valuenameDialog.length; k++) {
								map.put(valuenameDialog[k], result.get(i + k));
							}
							listDialog.add(map);
						}
						setDialogListView();
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無數據");
						setDialogListView();
					}
				}
			}
		};
	};
	/**
	 * 獲取選擇時間(監聽器)
	 */
	TimeDayListener listen1 = new TimeDayListener() {

		@Override
		public void onGetresult(String result) {
			// TODO Auto-generated method stub
			line = list.get(pos).get(valuename[2]).toString();
			start.getServerData(3, MyConstant.GET_QRCODEID_CHECK_UP_TIME, list
					.get(pos).get(valuename[3]).toString(),
					list.get(pos).get(valuename[5]).toString(), list.get(pos)
							.get(valuename[0]).toString(), line, result);
		}
	};

	/**
	 * 初始化對話框
	 */
	private void setDialogListView() {
		dia = new Dialog(context, R.style.customDialog);
		dia.show();
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_check_up, null);
		((TextView) view.findViewById(R.id.dialog_title_check_up))
				.setText("已點檢時間段");
		final TextView timetv = (TextView) view
				.findViewById(R.id.dialog_time_check_up);
		timetv.setText("點擊,選擇補點檢的時間");
		dia.setContentView(view);
		ListView lvdialog = (ListView) view
				.findViewById(R.id.check_up_dialoglistView);
		DialogAdapter dialogAdapter = new DialogAdapter();
		lvdialog.setAdapter(dialogAdapter);
		timetv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new SelectTime().ShowDateDialog(Check_UpActivity.this, timetv,
						"選擇補點檢的時間");
			}
		});
		((Button) view.findViewById(R.id.dialog_ok_check_up))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Get_Time date = new Get_Time();
						String tvtimestr = timetv.getText().toString();
						String timenowstr = date.GetTimeYMDhms("-");
						if (tvtimestr.split("-").length > 1) {
							if (date.strToDateLong(tvtimestr).before(
									date.strToDateLong(timenowstr))) {

								if (list.get(pos).get(valuename[9]).toString()
										.equalsIgnoreCase("0")) {
									intent = new Intent(Check_UpActivity.this,
											Check_Up_CheckPdActivity.class);
								} else {
									intent = new Intent(Check_UpActivity.this,
											Check_Up_ECheckActivity.class);
								}
								intent.putExtra("reportname", list.get(pos)
										.get(valuename[7]).toString());
								intent.putExtra("reportid",
										list.get(pos).get(valuename[3])
												.toString());
								intent.putExtra("site",
										list.get(pos).get(valuename[4])
												.toString());
								intent.putExtra("mfg",
										list.get(pos).get(valuename[5])
												.toString());
								intent.putExtra("sbu",
										list.get(pos).get(valuename[6])
												.toString());
								intent.putExtra("is_input_order", list.get(pos)
										.get(valuename[9]).toString());
								intent.putExtra("is_usercheck", "0");
								intent.putExtra("floorName",
										list.get(pos).get(valuename[0])
												.toString());
								intent.putExtra("linename", line);
								intent.putExtra("CheckUpTime", tvtimestr);
								intent.putExtra("section", user.getSection());
								startActivity(intent);
							} else {
								Toast.makeText(context, "時間不能大於當前時間",
										Toast.LENGTH_SHORT).show();
							}
							dia.dismiss();
						}
					}
				});

	}

	/**
	 * 初始化listview數據
	 */
	private void setListView() {
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (weihu.equals("1")) {
					pos = position;
					showdia();
					return;
				}
				pos = position;
				new SelectTime(Check_UpActivity.this, "選擇時間，查詢該報表已點檢信息",
						listen1).ShowDateDayDialog();
			}
		});
	}

	/**
	 * 适配器，显示詳細點檢信息
	 */
	class MyAdapter extends BaseAdapter {

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final Hold hold;
			if (convertView == null) {
				hold = new Hold();
				convertView = View.inflate(context,
						R.layout.listview_check_up_d_item, null);
				hold.tv1 = (TextView) convertView
						.findViewById(R.id.add_old_tv1);
				hold.tv4 = (TextView) convertView
						.findViewById(R.id.add_old_tv4);
				hold.tv5 = (TextView) convertView
						.findViewById(R.id.add_old_tv5);
				hold.tv6 = (TextView) convertView
						.findViewById(R.id.add_old_tv6);
				hold.tv7 = (TextView) convertView
						.findViewById(R.id.add_old_tv7);
				hold.tv8 = (TextView) convertView
						.findViewById(R.id.add_old_tv8);
				hold.tv9 = (TextView) convertView
						.findViewById(R.id.add_old_tv9);
				hold.tv10 = (TextView) convertView
						.findViewById(R.id.add_old_tv10);
				convertView.setTag(hold);
			} else {
				hold = (Hold) convertView.getTag();
			}
			hold.tv1.setText(list.get(position).get(valuename[11]).toString());
			hold.tv4.setText(list.get(position).get(valuename[0]).toString());
			hold.tv5.setText(list.get(position).get(valuename[1]).toString());
			hold.tv6.setText(list.get(position).get(valuename[8]).toString());
			hold.tv7.setText(list.get(position).get(valuename[2]).toString());
			hold.tv8.setText(list.get(position).get(valuename[3]).toString());
			hold.tv9.setText(list.get(position).get(valuename[10]).toString());
			hold.tv10.setText(list.get(position).get(valuename[7]).toString());
			return convertView;
		}

	}

	/**
	 * 适配器，显示已點檢的時間信息
	 */
	class DialogAdapter extends BaseAdapter {

		public int getCount() {
			return listDialog.size();
		}

		public Object getItem(int position) {
			return listDialog.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final Hold holddalog;
			if (convertView == null) {
				holddalog = new Hold();
				convertView = View.inflate(context,
						R.layout.listview_check_up_item, null);
				holddalog.tv1 = (TextView) convertView
						.findViewById(R.id.check_up_day_type);
				holddalog.tv2 = (TextView) convertView
						.findViewById(R.id.check_up_time);

				convertView.setTag(holddalog);
			} else {
				holddalog = (Hold) convertView.getTag();
			}
			if (listDialog.get(position).get(valuenameDialog[0]).toString()
					.equalsIgnoreCase("D")) {
				holddalog.tv1.setText("白班");
			} else {
				holddalog.tv1.setText("晚班");
			}
			holddalog.tv2.setText(listDialog.get(position)
					.get(valuenameDialog[1]).toString());
			return convertView;
		}

	}
	
	public void showdia(){

		/**
		 * 初始化對話框
		 */
		 dia = new Dialog(context,
				R.style.customDialog);
		dia.show();
		View view1 = LayoutInflater
				.from(context)
				.inflate(R.layout.dialog_check_up, null);
		final TextView timetv = (TextView) view1
				.findViewById(R.id.dialog_time_check_up);
		((TextView) view1
				.findViewById(R.id.dialog_title_check_up))
				.setText("提前維護");
		timetv.setText("點擊,選擇提前點檢的時間");
		dia.setContentView(view1);
		ListView lvdialog = (ListView) view1
				.findViewById(R.id.check_up_dialoglistView);
		timetv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new SelectTime().ShowDateDialog(
						context, timetv, "選擇點檢的時間");
			}
		});
		((Button) view1
				.findViewById(R.id.dialog_ok_check_up))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method
						// stub
						Get_Time date = new Get_Time();
						String tvtimestr = timetv
								.getText().toString();
						String timenowstr = date
								.GetTimeYMDhms("-");
						if (tvtimestr.split("-").length > 1) {
							if (date.strToDateLong(
									tvtimestr)
									.after(date
											.strToDateLong(timenowstr))) {
								if (list.get(pos).get(valuename[9]).toString()
										.equalsIgnoreCase("0")) {
									intent = new Intent(
											context,
											Check_Up_CheckPdActivity.class);
								} else {
									intent = new Intent(
											context,
											Check_Up_ECheckActivity.class);
								}

								intent.putExtra("reportname", list.get(pos)
										.get(valuename[7]).toString());
								intent.putExtra("reportid",
										list.get(pos).get(valuename[3])
												.toString());
								intent.putExtra("site",
										list.get(pos).get(valuename[4])
												.toString());
								intent.putExtra("mfg",
										list.get(pos).get(valuename[5])
												.toString());
								intent.putExtra("sbu",
										list.get(pos).get(valuename[6])
												.toString());
								intent.putExtra("is_input_order", list.get(pos)
										.get(valuename[9]).toString());
								intent.putExtra("is_usercheck", "0");
								intent.putExtra("floorName",
										list.get(pos).get(valuename[0])
												.toString());
								intent.putExtra("linename", list.get(pos).get(valuename[2])
										.toString());
								intent.putExtra("CheckUpTime", tvtimestr);
								intent.putExtra("section", user.getSection());
								startActivity(intent);
							} else {
								Toast.makeText(
										context,
										"時間不能小於當前時間",
										Toast.LENGTH_SHORT)
										.show();
							}
							dia.dismiss();
						}
					}
				});
	
	}
	
	class Hold {
		TextView tv1, tv2, tv4, tv5, tv6, tv7, tv8, tv9, tv10;
	}

	/**
	 * 返回键按钮
	 * 
	 * @param v
	 */
	public void activity_back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			this.finish();
			overridePendingTransition(R.anim.move_left_in_activity,
					R.anim.move_right_out_activity);
		}
		return super.onKeyDown(keyCode, event);
	}

}
