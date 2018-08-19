package com.zsm.foxconn.mypaperless;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.foxconn.selecttime.Get_Time;
import com.foxconn.selecttime.SelectTime;
import com.zsm.foxconn.mypaperless.adapter.TeamIpqc_codeAdapter;
import com.zsm.foxconn.mypaperless.adapter.TeamIpqc_codeAdapter.ViewHolder2;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.TeamIpqc_codebean;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.wheeltime.ScreenInfo;
import com.zsm.foxconn.mypaperless.wheeltime.WheelMain;

/**
 * @author f1330296
 * 
 *         提前维护线体（二维码维护）
 */
public class Tiqianweihu_ByQRcode extends BaseActivity implements
		OnItemClickListener, OnClickListener, OnItemLongClickListener,
		OnScrollListener {
	Context context = Tiqianweihu_ByQRcode.this;
	UserBean userBean;
	HttpStart start;
	Intent intent;
	WheelMain wheelMain;// 弹出日期时间选择器

	List<TeamIpqc_codebean> listAll = new ArrayList<TeamIpqc_codebean>();// 所有集合数据
	List<TeamIpqc_codebean> list = new ArrayList<TeamIpqc_codebean>();// 分段显示集合
	List<String> Report_nameData = new ArrayList<String>();// 单独编号查询集合
	Map<String, String> map = new HashMap<String, String>();

	ImageButton DetailsimageButton,tiqian_qr;
	ListView listView;// 列表
	// Button code_all;// 全选
	Button code_Maintenance;// 选择时间段
	TeamIpqc_codeAdapter ipqc_codeAdapter;
	private boolean flag;// 防止多次弹框
	private AlertDialog alert = null;
	/* 分段加载 */
	private final int PAGE_SIZE = 40;
	private int minSize = 0;
	private int maxSize = 40;

	int visibleItemCount; // 当前窗口可见项总数
	int visibleLastIndex = 0; // 最后的可视项索引
	Button loadMoreButton;// 加载..
	View loadMoreView;

	/* 获取时间 */
	SimpleDateFormat formatter, formatter1, formatter0;
	Date curDate;

	/** 变量 **/
	String Report_name;// 报表名称
	String Report_number;// 报表编号
	String Line_level;// 线别
	String Control_no;// 设备编号
	String Floors;// 楼层
	String SITE_NAME, BU, MFG_NAME;// 厂区+BU+事业处
	String QR_IMAGE_INFO;// 二維碼编号
	String is_input_order; // 報表類型

	String BeanControl_no;// 获取选择的设备编号
	boolean j;// 判断选择时间
	int i = 0; // 選擇整天維護或班別維護標識

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teamipqc_code);
		TextView head_title = (TextView) findViewById(R.id.head_title);
		head_title.setText("二维码编号维护");
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		findViewById();
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		start = new HttpStart(context, handler);
		start.getServerData(3,
				MyConstant.Two_dimensional_code_number_maintenance,
				userBean.getMFG());
	}

	@SuppressLint({ "InflateParams", "SimpleDateFormat" })
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		DetailsimageButton = (ImageButton) findViewById(R.id.Details);
		DetailsimageButton.setOnClickListener(this);
		tiqian_qr = (ImageButton) findViewById(R.id.tiqian_qr);
		tiqian_qr.setOnClickListener(this);
		
		// 显示的列表
		listView = (ListView) findViewById(R.id.list_code);
		listView.setOnItemClickListener(this);// 点击监听
		listView.setOnItemLongClickListener(this);// 长按监听
		listView.setOnScrollListener(this); // 滑动监听

		// 全选
		// code_all = (Button) findViewById(R.id.code_all);
		// code_all.setOnClickListener(this);

		// 选择时间段
		code_Maintenance = (Button) findViewById(R.id.code_Maintenance);
		code_Maintenance.setOnClickListener(this);

		// 底部加载
		loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
		if (list.size() <= 0) {
			loadMoreView.setVisibility(View.GONE);
		}
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.loadMoreButton);
		listView.addFooterView(loadMoreView); // 设置列表底部视图

		ipqc_codeAdapter = new TeamIpqc_codeAdapter(context, list);
		listView.setAdapter(ipqc_codeAdapter);

		// 获取当前时间
		formatter = new SimpleDateFormat("yyyy/MM/dd");
		curDate = new Date(System.currentTimeMillis());
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
	}

	/**
	 * 滑动时被调用
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	/**
	 * 滑动状态改变时被调用
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = ipqc_codeAdapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1; // 加上底部的loadMoreView项
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// 如果是自动加载,可以在这里放置异步加载数据的代码
			// Log.i("LOADMORE", "loading...");
		}
	}

	/**
	 * 点击按钮事件
	 * 
	 * @param view
	 */
	public void loadMore(View view) {
		if (maxSize + PAGE_SIZE >= listAll.size()) {
			minSize = maxSize;
			maxSize = listAll.size();
			loadMoreButton.setText("没有更多了");
			return;
		} else {
			minSize = maxSize;
			maxSize += PAGE_SIZE;
			loadMoreButton.setText("加载更多..."); // 恢复按钮文字
		}
		for (int i = minSize; i < maxSize; i++) {
			list.add(listAll.get(i));
		}

		ipqc_codeAdapter.notifyDataSetChanged();
		listView.setSelection(visibleLastIndex - visibleItemCount + 1); // 设置选中项
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String Key : keySet) {

				// 二维码编号维护
				if (Key.equalsIgnoreCase(MyConstant.Two_dimensional_code_number_maintenance)) {
					result = msg.getData().getStringArrayList(Key);
					if (listAll.size() > 0) {
						listAll.clear();
					}
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 10) {

							Report_name = result.get(i).toString().trim();
							Report_number = result.get(i + 1).toString().trim();
							Line_level = result.get(i + 2).toString().trim();
							Control_no = result.get(i + 3).toString().trim();
							SITE_NAME = result.get(i + 4).toString().trim();
							BU = result.get(i + 5).toString().trim();
							MFG_NAME = result.get(i + 6).toString().trim();
							Floors = result.get(i + 7).toString().trim();
							QR_IMAGE_INFO = result.get(i + 8).toString().trim();
							is_input_order = result.get(i + 9).toString()
									.trim();

							TeamIpqc_codebean codebean = new TeamIpqc_codebean();
							codebean.setReport_name(Report_name);
							codebean.setReport_number(Report_number);
							codebean.setLine_level(Line_level);
							codebean.setControl_no(Control_no);
							codebean.setSITE_NAME(SITE_NAME);
							codebean.setBU(BU);
							codebean.setMFG_NAME(MFG_NAME);
							codebean.setFlooR_NAME(Floors);
							codebean.setQR_IMAGE_INFO(QR_IMAGE_INFO);
							codebean.setCanRemove(true);
							codebean.setIs_input_order(is_input_order);
							// 存储所有数据
							listAll.add(codebean);

							// 单独存储报表名称
							Report_nameData
									.add(result.get(i).toString().trim());
							map.put(result.get(i).toString().trim(), result
									.get(i + 1).toString().trim());
						}
						if (maxSize >= listAll.size()) {
							loadMoreButton.setText("没有更多了");
							maxSize = listAll.size();
						}
						for (int i = minSize; i < maxSize; i++) {
							list.add(listAll.get(i));
							loadMoreView.setVisibility(View.VISIBLE);
						}
						ipqc_codeAdapter.notifyDataSetChanged();
					} else {
						Toast.makeText(getApplicationContext(), "没有数据",
								Toast.LENGTH_SHORT).show();
					}
				}

				// 二维码详细查询
				if (Key.equalsIgnoreCase(MyConstant.QR_details_query)) {
					result = msg.getData().getStringArrayList(Key);
					minSize = 0;
					maxSize = 40;
					if (listAll.size() > 0) {
						listAll.clear();
					}
					if (list.size() > 0) {
						list.clear();
					}
					loadMoreButton.setText("加载更多...");
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 10) {
							Report_name = result.get(i).toString().trim();
							Report_number = result.get(i + 1).toString().trim();
							Line_level = result.get(i + 2).toString().trim();
							Control_no = result.get(i + 3).toString().trim();
							SITE_NAME = result.get(i + 4).toString().trim();
							BU = result.get(i + 5).toString().trim();
							MFG_NAME = result.get(i + 6).toString().trim();
							Floors = result.get(i + 7).toString().trim();
							QR_IMAGE_INFO = result.get(i + 8).toString().trim();
							is_input_order = result.get(i + 9).toString()
									.trim();
							
							TeamIpqc_codebean codebean = new TeamIpqc_codebean();
							codebean.setReport_name(Report_name);
							codebean.setReport_number(Report_number);
							codebean.setLine_level(Line_level);
							codebean.setControl_no(Control_no);
							codebean.setSITE_NAME(SITE_NAME);
							codebean.setBU(BU);
							codebean.setMFG_NAME(MFG_NAME);
							codebean.setFlooR_NAME(Floors);
							codebean.setQR_IMAGE_INFO(QR_IMAGE_INFO);
							codebean.setCanRemove(true);
							codebean.setIs_input_order(is_input_order);
							// 存储所有数据
							listAll.add(codebean);
						}
						if (maxSize >= listAll.size()) {
							loadMoreButton.setText("没有更多了");
							maxSize = listAll.size();
						}
						for (int i = minSize; i < maxSize; i++) {
							list.add(listAll.get(i));
						}
						// code_all.setVisibility(View.VISIBLE);
						ipqc_codeAdapter = new TeamIpqc_codeAdapter(context,
								list);
						listView.setAdapter(ipqc_codeAdapter);
						ipqc_codeAdapter.notifyDataSetChanged();
					} else {
						Toast.makeText(getApplicationContext(), "没有数据",
								Toast.LENGTH_SHORT).show();
						loadMoreView.setVisibility(View.GONE);
					}
				}

				// 线体维护
				if (Key.equalsIgnoreCase(MyConstant.Line_maintenance)) {
					result = msg.getData().getStringArrayList(Key);
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						Toast.makeText(getApplicationContext(), "点检成功",
								Toast.LENGTH_SHORT).show();
					} else if (result.get(0).equals("CHOOSETIME FNAILED")) {
						UIHelper.ToastMessage(context, "選擇時間必須大於當前時間");
						return;
					} else {
						Toast.makeText(getApplicationContext(), "该线别无可点检报表",
								Toast.LENGTH_SHORT).show();
					}
				}
				if (Key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					Toast.makeText(context, "未连接服务器....", 0).show();
					return;
				}

			}
		}
	};

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// 右上角点击
		if (v == DetailsimageButton) {
			Report_nameData = new ArrayList(new HashSet(Report_nameData));// list去重复

			// list转换字符串数组
			final String[] items = new String[Report_nameData.size()];
			for (int i = 0; i < items.length; i++) {
				items[i] = Report_nameData.get(i).toString();
			}
			alert = new AlertDialog.Builder(context).setTitle("选择報表")
					.setItems(items, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							// 点击item获取详细编号查询
							start.getServerData(3, MyConstant.QR_details_query,
									map.get(Report_nameData.get(which)),
									userBean.getMFG());
						}
					}).show();
		}

		// 选择时间段
		if (v == code_Maintenance) {
			if (listAll.size() > 0) {
				// 拿到checkBox选择寄存map
				Map<Integer, Boolean> map = ipqc_codeAdapter.getCheckMap();

				// 获取当前的数据数量
				int count = ipqc_codeAdapter.getCount();

				// 进行遍历
				for (int i = 0; i < count; i++) {

					// 所以这里要进行这样的换算,才能拿到真正的position
					int position = i - (count - ipqc_codeAdapter.getCount());

					if (map.get(i) != null && map.get(i)) {

						TeamIpqc_codebean bean = (TeamIpqc_codebean) ipqc_codeAdapter
								.getItem(position);

						if (bean.isCanRemove()) {
							BeanControl_no = bean.getQR_IMAGE_INFO();
							// 防止多次弹框
							if (!flag) {
								dialog_time();
							}
						} else {
							map.put(position, false);
						}
					}
				}
				ipqc_codeAdapter.notifyDataSetChanged();
			}
		}
		
		if (v == tiqian_qr) {
			intent = new Intent(context,Check_UpActivity.class);
			intent.putExtra("TitleName", "提前維護");
			intent.putExtra("weihu", "1");
			startActivity(intent);
		}
	}

	// 选择时间
	public void dialog_time() {
		String cons = "选择时间:";
		String[] ipqcitem = new String[] { "整天", "白/晚班" };
		alert = new AlertDialog.Builder(context)
				.setTitle("选择的设备编号:" + BeanControl_no).setTitle(cons)
				.setItems(ipqcitem, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							// 选择整天
							i = 0;
							DateDialog();
							dialog.dismiss();
						} else {
							// 选择白/晚班
							j = false;
							i = 1;
							DateDialog();
							dialog.dismiss();
						}
					}
				}).show();
	}

	// 日期弹框
	@SuppressLint({ "SimpleDateFormat", "InflateParams" })
	private void DateDialog() {
		LayoutInflater inflater = LayoutInflater.from(context);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(Tiqianweihu_ByQRcode.this);

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
							dialog.dismiss();
							start.getServerData(3, MyConstant.Line_maintenance,
									"ALLDAY", SITE_NAME, BU, MFG_NAME, Floors,
									"NA", BeanControl_no, wheelMain.getTime()
											.toString().trim(),
									userBean.getLogonName());
						} else {
							ClassDalog();
						}

						dialog.dismiss();
					}
				}).setNegativeButton("取消", null).show();
	}

	// 班别弹框
	@SuppressLint("SimpleDateFormat")
	public void ClassDalog() {
		String[] ipqcitem = new String[] { "白班", "晚班" };
		alert = new AlertDialog.Builder(context).setTitle("选择班别")
				.setItems(ipqcitem, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (which == 0) {
							dialog.dismiss();
							start.getServerData(3, MyConstant.Line_maintenance,
									"Day", SITE_NAME, BU, MFG_NAME, Floors,
									"NA", BeanControl_no, wheelMain.getTime()
											.toString().trim(),
									userBean.getLogonName());
						} else {
							dialog.dismiss();
							start.getServerData(3, MyConstant.Line_maintenance,
									"Night", SITE_NAME, BU, MFG_NAME, Floors,
									"NA", BeanControl_no, wheelMain.getTime()
											.toString().trim(),
									userBean.getLogonName());
						}
						dialog.dismiss();
					}
				}).show();
	}

	/**
	 * 当ListView 子项点击的时候
	 */
	@Override
	public void onItemClick(AdapterView<?> listView, View itemLayout,
			int position, long id) {

		if (itemLayout.getTag() instanceof ViewHolder2) {

			ViewHolder2 holder = (ViewHolder2) itemLayout.getTag();

			// 会自动触发CheckBox的checked事件
			holder.cbCheck.toggle();
		}
	}

	/**
	 * 当ListView 子项长按的时候
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

		// 震动
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 0, 300 }, -1);// -1不重复
		Log.i("tag", ">>>"+listAll.get(arg2).getIs_input_order().toString());
		final String[] items = new String[2];// 获取一个值
		items[0] = "报表编号：" + listAll.get(arg2).getReport_number();// 获取报表编号字段
		items[1] = "二维码编号：" + listAll.get(arg2).getQR_IMAGE_INFO();// 获取二维码编号字段
		final String str_isorder = listAll.get(arg2).getIs_input_order(); // 是否輸入工單信息
		final String reportname = listAll.get(arg2).getReport_name();
		final String reportid = listAll.get(arg2).getReport_number(); 
		final String floorName = listAll.get(arg2).getFlooR_NAME();
		final String line = listAll.get(arg2).getControl_no();
		alert = new AlertDialog.Builder(context)
				.setTitle(
						"选择的设备编号:"
								+ listAll.get(arg2).getControl_no().toString())
				.setItems(items, null)
				.setPositiveButton("去详细点检",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

								/**
								 * 初始化對話框
								 */
								final Dialog dia = new Dialog(context,
										R.style.customDialog);
								dia.show();
								View view = LayoutInflater
										.from(context)
										.inflate(R.layout.dialog_check_up, null);
								final TextView timetv = (TextView) view
										.findViewById(R.id.dialog_time_check_up);
								((TextView) view
										.findViewById(R.id.dialog_title_check_up))
										.setText("提前維護");
								timetv.setText("點擊,選擇提前點檢的時間");
								dia.setContentView(view);
								ListView lvdialog = (ListView) view
										.findViewById(R.id.check_up_dialoglistView);
								timetv.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										new SelectTime().ShowDateDialog(
												context, timetv, "選擇點檢的時間");
									}
								});
								((Button) view
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
														if (str_isorder
																.equalsIgnoreCase("0")) {
															intent = new Intent(
																	context,
																	Check_Up_CheckPdActivity.class);
														} else {
															intent = new Intent(
																	context,
																	Check_Up_ECheckActivity.class);
														}

														intent.putExtra(
																"reportname",
																reportname);
														intent.putExtra(
																"reportid",
																reportid);
														intent.putExtra(
																"site",
																userBean.getSite());
														intent.putExtra(
																"mfg",
																userBean.getMFG());
														intent.putExtra(
																"sbu",
																userBean.getSBU());
														intent.putExtra(
																"is_input_order",
																is_input_order);
														intent.putExtra(
																"is_usercheck",
																"0");
														intent.putExtra(
																"floorName",
																floorName);
														intent.putExtra(
																"linename",
																line);
														intent.putExtra(
																"CheckUpTime",
																tvtimestr);
														intent.putExtra(
																"section",
																userBean.getSection());
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
						}).setNegativeButton("取消", null).show();
		return false;
	}

	// 返回键按钮
	public void back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}
}
