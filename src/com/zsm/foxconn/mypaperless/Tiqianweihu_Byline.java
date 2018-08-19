package com.zsm.foxconn.mypaperless;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.adapter.TeamIpqcAdapter;
import com.zsm.foxconn.mypaperless.adapter.TeamIpqcAdapter.ViewHolder;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.TeamIpqcbean;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.wheeltime.ScreenInfo;
import com.zsm.foxconn.mypaperless.wheeltime.WheelMain;

/**
 * @author f1330296
 * 
 *         提前维护线体（楼层线体维护）
 */
public class Tiqianweihu_Byline extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	HttpStart start;
	Context context = Tiqianweihu_Byline.this;
	UserBean userBean;
	WheelMain wheelMain; // 弹出日期时间选择器

	ListView listView;
	TeamIpqcAdapter adpAdapter; // 适配器
	LayoutInflater layoutInflater;
	PopupWindow popupWindow;
	LinearLayout linearLayout;// 底部按钮布局

	List<String> Floors_list = new ArrayList<String>();// 得到楼层
	List<TeamIpqcbean> Line_style_list = new ArrayList<TeamIpqcbean>();// 得到线体

	TextView head_title;
	ImageButton imgSwitch;// 切换楼层
	Button btnMaintenance;// 选择时间段
	// Button btnSelectAll;// 全选

	/* 获取时间 */
	// SimpleDateFormat formatter;
	// Date curDate;

	/** 变量 **/
	String Floors;// 楼层
	String Line_style;// 线体
	String SITE_NAME, BU, MFG_NAME;// 厂区+BU+事业处
	private boolean flag;
	String BeanLine_style;
	int i = 0; // 選擇整天維護或班別維護標識
	private AlertDialog alert = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_ipqc);
		head_title = (TextView) findViewById(R.id.head_title);
		head_title.setText("提前維護線體");
		userBean = (UserBean) getApplicationContext();

		initView();
		findViewById();
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

		// 切换楼层
		imgSwitch = (ImageButton) findViewById(R.id.imgSwitch);
		imgSwitch.setOnClickListener(this);

		// 选择时间段
		btnMaintenance = (Button) findViewById(R.id.btnMaintenance);
		btnMaintenance.setOnClickListener(this);

		// 全选
		// btnSelectAll = (Button) findViewById(R.id.btnSelectAll);
		// btnSelectAll.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.lvListView);
		listView.setOnItemClickListener(this);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub.
		String[] ipqcitem = new String[] { "楼层线体维护", "二维码编号维护" };
		alert = new AlertDialog.Builder(Tiqianweihu_Byline.this)
				.setTitle("选择楼层")
				.setItems(ipqcitem, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							head_title.setText("楼层线体维护");
							if (userBean.getUserLevel().equals("0")
									|| userBean.getUserLevel().equals("1")) {
								btnMaintenance.setVisibility(View.GONE);
								UIHelper.ToastMessage(context, "您無權限");
								return;
							}
							linearLayout = (LinearLayout) findViewById(R.id.ipqc_lin);
							linearLayout.setVisibility(View.VISIBLE);
							CheckLogin();
							dialog.dismiss();
						} else {
							startActivity(new Intent(context,
									Tiqianweihu_ByQRcode.class));
							finish();
							dialog.dismiss();
						}
					}
				}).show();
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		start = new HttpStart(context, handler);
		start.getServerData(0, MyConstant.Access_floors, userBean.getBU(),
				userBean.getMFG());
	}

	/**
	 * 按钮点击事件
	 */
	@Override
	public void onClick(View v) {

		/*
		 * 切换楼层
		 */
		if (v == imgSwitch) {
			dialog_ipqc();
		}

		/*
		 * 选择时间段
		 */
		if (v == btnMaintenance) {

			if (Line_style_list.size() > 0) {
				// 拿到checkBox选择寄存map
				Map<Integer, Boolean> map = adpAdapter.getCheckMap();
				// 获取当前的数据数量
				int count = adpAdapter.getCount();
				// 进行遍历
				for (int i = 0; i < count; i++) {

					// 所以这里要进行这样的换算,才能拿到真正的position
					int position = i - (count - adpAdapter.getCount());
					if (map.get(i) != null && map.get(i)) {

						TeamIpqcbean bean = (TeamIpqcbean) adpAdapter
								.getItem(position);

						if (bean.isCanRemove()) {
							BeanLine_style = bean.getLine_style();
							// 防止多次弹框
							if (!flag) {
								dialog_time();
							}
						} else {
							map.put(position, false);
						}
					}
				}
				adpAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(getApplicationContext(), "请先在右上角选择楼层",
						Toast.LENGTH_SHORT).show();
			}
		}

		// /**
		// * 全选
		// */
		// if (v == btnSelectAll) {
		// if (btnSelectAll.getText().toString().trim().equals("全选")) {
		// // 所有项目全部选中
		// adpAdapter.configCheckMap(true);
		// btnSelectAll.setText("全不选");
		// } else {
		// // 所有项目全部不选中
		// adpAdapter.configCheckMap(false);
		// btnSelectAll.setText("全选");
		// }
		// adpAdapter.notifyDataSetChanged();
		// }
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) // 键值对
			{
				// 获取楼层
				if (key.equalsIgnoreCase(MyConstant.Access_floors)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 1) {
							Floors_list.add(result.get(i).toString().trim());
						}
						imgSwitch.setVisibility(View.VISIBLE);
						dialog_ipqc();
					} else {
						System.out.println("没有数据");
					}
				}

				// 获取线体
				if (key.equalsIgnoreCase(MyConstant.Gets_the_line_style)) {
					result = msg.getData().getStringArrayList(key);
					if (Line_style_list.size() > 0) {
						Line_style_list.clear();
					}
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 4) {
							Line_style = result.get(i).toString().trim();
							SITE_NAME = result.get(i + 1).toString().trim();
							BU = result.get(i + 2).toString().trim();
							MFG_NAME = result.get(i + 3).toString().trim();

							TeamIpqcbean teamIpqcbean = new TeamIpqcbean();
							teamIpqcbean.setFloors(Floors);
							teamIpqcbean.setLine_style(Line_style);
							teamIpqcbean.setSITE_NAME(SITE_NAME);
							teamIpqcbean.setBU(BU);
							teamIpqcbean.setMFG_NAME(MFG_NAME);
							teamIpqcbean.setCanRemove(true);
							Line_style_list.add(teamIpqcbean);
						}
					} else {
						Toast.makeText(getApplicationContext(), "没有数据",
								Toast.LENGTH_SHORT).show();
					}
					adpAdapter = new TeamIpqcAdapter(context, Line_style_list);
					listView.setAdapter(adpAdapter);
					adpAdapter.notifyDataSetChanged();
				}

				// 线体维护
				if (key.equalsIgnoreCase(MyConstant.Line_maintenance)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						Toast.makeText(getApplicationContext(), "点检成功",
								Toast.LENGTH_SHORT).show();
					} else if (result.get(0).equals("CHOOSETIME FNAILED")) {
						UIHelper.ToastMessage(context, "選擇時間必須大於當前時間");
						return;
					} else if (result.get(0).equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "維護失敗");
						return;
					} else {
						Toast.makeText(getApplicationContext(), "该线别无可点检报表",
								Toast.LENGTH_SHORT).show();
					}
				}

				if (key.equalsIgnoreCase(MyConstant.Line_twohours_maintenance)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						Toast.makeText(getApplicationContext(), "点检成功",
								Toast.LENGTH_SHORT).show();
					} else if (result.get(0).equals("CHOOSETIME FNAILED")) {
						UIHelper.ToastMessage(context, "選擇時間必須大於當前時間");
						return;
					} else if (result.get(0).equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "維護失敗");
						return;
					} else {
						Toast.makeText(getApplicationContext(), "该线别无可点检报表",
								Toast.LENGTH_SHORT).show();
					}
				}

				if (key.equalsIgnoreCase(MyConstant.Line_fourhours_maintenance)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						Toast.makeText(getApplicationContext(), "点检成功",
								Toast.LENGTH_SHORT).show();
					} else if (result.get(0).equals("CHOOSETIME FNAILED")) {
						UIHelper.ToastMessage(context, "選擇時間必須大於當前時間");
						return;
					} else if (result.get(0).equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "維護失敗");
						return;
					} else {
						Toast.makeText(getApplicationContext(), "该线别无可点检报表",
								Toast.LENGTH_SHORT).show();
					}
				}
				if (key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					Toast.makeText(context, "未连接服务器....", 0).show();
					return;
				}
			}
		}
	};

	// 楼层弹框
	public void dialog_ipqc() {
		// list转换字符串数组
		final String[] items = new String[Floors_list.size()];
		for (int i = 0; i < items.length; i++) {
			items[i] = Floors_list.get(i).toString();
		}
		alert = new AlertDialog.Builder(Tiqianweihu_Byline.this)
				.setTitle("选择楼层")
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();

						Floors = Floors_list.get(which);
						start.getServerData(3, MyConstant.Gets_the_line_style,
								Floors, userBean.getMFG().toString());
					}
				}).show();
	}

	// 选择时间
	private void dialog_time() {
		String[] ipqcitem = new String[] { "整天", "白/晚班", "時間段(2小時)", "時間段(4小時)" };
		alert = new AlertDialog.Builder(Tiqianweihu_Byline.this)
				.setTitle("选择维护的线体：" + BeanLine_style)
				.setItems(ipqcitem, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							// 选择整天
							i = 0;
						} else if (which == 1) {
							// 选择白/晚班
							i = 1;
						} else if (which == 2) {
							// 2小時 時間段
							i = 2;
						} else if (which == 3) {
							// 4小時 時間段
							i = 3;
						}
						DateDialog();
						dialog.dismiss();
					}
				}).show();
	}

	// 日期弹框
	@SuppressLint({ "SimpleDateFormat", "InflateParams" })
	private void DateDialog() {
		LayoutInflater inflater = LayoutInflater.from(context);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(Tiqianweihu_Byline.this);

		Calendar calendar = Calendar.getInstance();

		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		wheelMain.initDateTimePicker(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));

		alert = new AlertDialog.Builder(context).setTitle("选择日期")
				.setView(timepickerview)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (i == 0) {
							// 維護
							start.getServerData(3, MyConstant.Line_maintenance,
									"ALLDAY", SITE_NAME, BU, MFG_NAME, Floors,
									BeanLine_style, "NA", wheelMain.getTime()
											.toString().trim(),
									userBean.getLogonName());
							dialog.dismiss();
						} else if (i == 2) {
							two_hours_Dalog();
						} else if (i == 3) {
							four_hours_Dalog();
						} else {
							ClassDalog();
						}
					}
				}).show();
	}

	// 班别弹框
	public void ClassDalog() {
		String[] ipqcitem = new String[] { "白班", "晚班" };
		alert = new AlertDialog.Builder(Tiqianweihu_Byline.this)
				.setTitle("选择班别")
				.setItems(ipqcitem, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							dialog.dismiss();
							start.getServerData(3, MyConstant.Line_maintenance,
									"Day", SITE_NAME, BU, MFG_NAME, Floors,
									BeanLine_style, "NA", wheelMain.getTime()
											.toString().trim(),
									userBean.getLogonName());
						} else {
							dialog.dismiss();
							start.getServerData(3, MyConstant.Line_maintenance,
									"Night", SITE_NAME, BU, MFG_NAME, Floors,
									BeanLine_style, "NA", wheelMain.getTime()
											.toString().trim(),
									userBean.getLogonName());
						}
					}
				}).show();
	}

	// 2小時段弹框
	public void two_hours_Dalog() {
		String[] ipqcitem = new String[] { "08:00~10:00", "10:00~12:00",
				"12:00~14:00", "14:00~16:00", "16:00~18:00", "18:00~20:00",
				"20:00~22:00", "22:00~24:00", "00:00~02:00", "02:00~04:00",
				"04:00~06:00", "06:00~08:00" };
		alert = new AlertDialog.Builder(Tiqianweihu_Byline.this)
				.setTitle("选择班别")
				.setItems(ipqcitem, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String twohours_type = null;
						if (which == 0) {
							twohours_type = "Two_A";
						} else if (which == 1) {
							twohours_type = "Two_B";
						} else if (which == 2) {
							twohours_type = "Two_C";
						} else if (which == 3) {
							twohours_type = "Two_D";
						} else if (which == 4) {
							twohours_type = "Two_E";
						} else if (which == 5) {
							twohours_type = "Two_F";
						} else if (which == 6) {
							twohours_type = "Two_G";
						} else if (which == 7) {
							twohours_type = "Two_H";
						} else if (which == 8) {
							twohours_type = "Two_I";
						} else if (which == 9) {
							twohours_type = "Two_J";
						} else if (which == 10) {
							twohours_type = "Two_K";
						} else if (which == 11) {
							twohours_type = "Two_L";
						}
						dialog.dismiss();
						start.getServerData(3,
								MyConstant.Line_twohours_maintenance,
								twohours_type, SITE_NAME, BU, MFG_NAME, Floors,
								BeanLine_style, "NA", wheelMain.getTime()
										.toString().trim(),
								userBean.getLogonName());
					}
				}).show();
	}

	// 4小時段弹框
	public void four_hours_Dalog() {
		String[] ipqcitem = new String[] { "08:00~12:00", "12:00~16:00",
				"16:00~20:00", "20:00~24:00", "00:00~04:00", "04:00~08:00" };
		alert = new AlertDialog.Builder(Tiqianweihu_Byline.this)
				.setTitle("选择班别")
				.setItems(ipqcitem, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String fourhours_type = null;
						if (which == 0) {
							fourhours_type = "Four_A";
						} else if (which == 1) {
							fourhours_type = "Four_B";
						} else if (which == 2) {
							fourhours_type = "Four_C";
						} else if (which == 3) {
							fourhours_type = "Four_D";
						} else if (which == 4) {
							fourhours_type = "Four_E";
						} else if (which == 5) {
							fourhours_type = "Four_F";
						}
						dialog.dismiss();
						start.getServerData(3,
								MyConstant.Line_fourhours_maintenance,
								fourhours_type, SITE_NAME, BU, MFG_NAME,
								Floors, BeanLine_style, "NA", wheelMain
										.getTime().toString().trim(),
								userBean.getLogonName());
					}
				}).show();
	}

	/**
	 * 当ListView 子项点击的时候
	 */
	@Override
	public void onItemClick(AdapterView<?> listView, View itemLayout,
			int position, long id) {

		if (itemLayout.getTag() instanceof ViewHolder) {

			ViewHolder holder = (ViewHolder) itemLayout.getTag();

			// 会自动触发CheckBox的checked事件
			holder.cbCheck.toggle();
		}

	}

	// 返回键按钮
	public void back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}
}
