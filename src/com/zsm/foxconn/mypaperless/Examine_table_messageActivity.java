package com.zsm.foxconn.mypaperless;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.tsz.afinal.FinalDb;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.ExamineAllMessageTableBean;
import com.zsm.foxconn.mypaperless.bean.Picture;
import com.zsm.foxconn.mypaperless.bean.Qr_check_situation;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.database.APP_yindao_page;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.refreshlistview.OnRefreshListener;
import com.zsm.foxconn.mypaperless.refreshlistview.RefreshListView;
import com.zsm.foxconn.mypaperless.util.GuideUtil;
import com.zsm.foxconn.mypaperless.wheeltime.ScreenInfo;
import com.zsm.foxconn.mypaperless.wheeltime.WheelMain;
import com.zsm.qr.CaptureActivity;

public class Examine_table_messageActivity extends BaseActivity {

	RefreshListView examineListView;
	MyAdapter adapter;
	private String checkstr = null, timeDate, logonName, logonMFG, repostId,
			reportName, linearid, floor, linename;
	private int year, month, day, check_type = 0;
	private EditText checkEditText;
	private UserBean user;
	HttpStart start = null;
	WheelMain wheelMain;
	private TextView title, checktext, alertdialog_top_tv, alertdialog_tv;
	private ImageButton checkAllbtn;
	private ImageView btn_select, detele_account;
	private Button homebtn;
	Context context = Examine_table_messageActivity.this;
	List<ExamineAllMessageTableBean> listexaminall = new ArrayList<ExamineAllMessageTableBean>();
	List<ExamineAllMessageTableBean> piliang_listdata = new ArrayList<ExamineAllMessageTableBean>();
	private List<Picture> dataspinn = new ArrayList<Picture>();
	ArrayList<String> listcheck = new ArrayList<String>();
	private Spinner type_spinner;
	private String[] spinnerStr = null, floorstr = null, linestr = null;
	PopupWindow pw;
	private int style = 3, datamore = 50;// datamore每次多刷新加载的数据
	InputMethodManager inputMethodManager;
	private LinearLayout layout_xuanze_sbu, parent;
	private Spinner choosefloor_sp, chooseline_sp;
	private AutoCompleteTextView auto_et;
	private int pwidth; // 下拉框依附组件宽度，也将作为下拉框的宽度
	private PopupWindow selectPopupWindow = null, popuWindow = null;
	private ArrayList<String> datas = new ArrayList<String>(); // 下拉框选项数据源
	// 展示所有下拉选项的ListView
	private ListView listView = null;
	private boolean isfirst = false, isnull = false, isflag = false;
	private View contentView;
	private final int SCANER_CODE = 1;
	private int selectsaoma = 0;
	private String floorName, reportNum, lineName, site, mfg, sbu, section,
			rpt;
	private List<Qr_check_situation> list_qrcheck;
	private FinalDb finalDb = null;
	private GuideUtil guideUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_examine_table_message);
		title = (TextView) findViewById(R.id.bartitle_txt);
		Intent intent = getIntent();
		linearid = intent.getStringExtra("linearid").toString().trim();// 查看的类型区别编号
		if (linearid.equals("1")) {
			title.setText(getResources().getString(R.string.table_all_message));
		} else if (linearid.equals("2")) {
			title.setText(getResources().getString(R.string.table_message_will));
			finalDb = FinalDb.create(context, "child"); // 创建数据库
			List<APP_yindao_page> list_app = finalDb
					.findAllByWhere(APP_yindao_page.class,
							"pagename='Examine_table_messageActivity' and pagename_id='0'");
			if (list_app.size() == 0) {
				guideUtil = GuideUtil.getInstance();
				guideUtil.initGuide(this, R.drawable.yindao_shenhe,
						"Examine_table_messageActivity", 0);
			}
		} else if (linearid.equals("3")) {
			title.setText(getResources().getString(R.string.table_message_ok));
		} else if (linearid.equals("4")) {
			title.setText(getResources().getString(R.string.table_message_no));
		}
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 獲取列表報表名
		user = (UserBean) getApplicationContext();
		CheckLogin();
		start = new HttpStart(context, handler);
		findViewById();
		initView();
		// 获取spinner列表值
		if (linearid.equals("1")) {
			start.getServerData(3, MyConstant.GET_MFG_ALL_REPORT_NAME, user
					.getMFG().toString(), user.getSite().toString(), user
					.getBU(), linearid);
		} else {
			start.getServerData(3, MyConstant.GET_MFG_ALL_REPORT_NAME, user
					.getMFG().toString(), user.getSite().toString(), user
					.getBU(), linearid, user.getTeam(), user.getLogonName());
		}
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
		adapter = new MyAdapter();
		examineListView.setAdapter(adapter);
		checktext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 通过布局注入器，注入布局给View对象
				View myView = getLayoutInflater().inflate(
						R.layout.examine_popupwindowlv, null);

				WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
				int xPos = windowManager.getDefaultDisplay().getWidth() / 3;
				int yPos = windowManager.getDefaultDisplay().getHeight() / 5;
				// 通过view 和宽·高，构造PopopWindow
				pw = new PopupWindow(myView, xPos, yPos, true);
				pw.setBackgroundDrawable(getResources().getDrawable(
				// 此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
						R.color.white));
				// 设置焦点为可点击
				pw.setFocusable(true);// 可以试试设为false的结果
				// 将window视图显示在checktext下面
				pw.showAsDropDown(checktext);

				ListView lv = (ListView) myView
						.findViewById(R.id.checktype_list);
				lv.setAdapter(new ListViewAdapter(
						Examine_table_messageActivity.this, listcheck));
				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						checktext.setText(listcheck.get(position));// 点击后选择框显示数据
						check_type = position;
						switch (check_type) {
						case 0:
							break;
						case 1:
							getTime();
							break;
						case 2:
							break;
						case 3:
							checkline();
							break;
						default:
							break;
						}
						pw.dismiss();
					}
				});
			}

		});

		checkAllbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				datamore = 30;
				checkstr = checkEditText.getText().toString().trim();
				selectsaoma = 0;
				getDataAll();

			}
		});

		// 查询所得点检报表点检后详情
		examineListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Examine_table_messageActivity.this,
						Examine_mes_moreActivity.class);
				intent.putExtra("RepostNO", listexaminall.get(arg2 - 1)
						.getCheck_report_NO());// 点检编号
				intent.putExtra("report_num", repostId);// 报表编号
				intent.putExtra("report_Name", reportName);// 报表名
				intent.putExtra("linearid", linearid);
				intent.putExtra("createby_status", "3");
				startActivity(intent);
			}
		});
		examineListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (linearid.equals("2")) {
							Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
							vibrator.vibrate(new long[] { 0, 500 }, -1);
							if (piliang_listdata.size() > 0) {
								piliang_listdata.clear();
							}
							for (int i = 0; i < listexaminall.size(); i++) {
								if (listexaminall.get(i).getWhether_check()
										.equals("1")) {
									piliang_listdata.add(listexaminall.get(i));
								}
							}
							if (piliang_listdata.size() == 0) {
								UIHelper.ToastMessage(context, "無可批量審核列表");
							} else {
								Intent intent = new Intent(context,
										Examine_table_piliangcheck.class);
								intent.putExtra("piliang_listdata",
										(Serializable) piliang_listdata);
								startActivity(intent);
							}
						}
						return true;
					}
				});
		examineListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// 异步查询数据
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						SystemClock.sleep(500);
						style = 10;
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
						SystemClock.sleep(500);
						style = 10;
						datamore += 30;
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

		detele_account.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				auto_et.setText("");
			}
		});
		homebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openCameraIntent = new Intent(context,
						CaptureActivity.class);
				startActivityForResult(openCameraIntent, SCANER_CODE);
				UIHelper.ToastMessage(context, "掃碼查詢");
			}
		});
	}

	@Override
	protected void findViewById() {
		// 返回主页
		homebtn = (Button) findViewById(R.id.btn_submit);
		homebtn.setBackgroundResource(R.drawable.erweima_click_seletor);
		examineListView = (RefreshListView) findViewById(R.id.table_message_listView);
		auto_et = (AutoCompleteTextView) findViewById(R.id.item_table_typespin);
		checkEditText = (EditText) findViewById(R.id.check_type_str);
		checkAllbtn = (ImageButton) findViewById(R.id.check_type_allbtn);// 查询按钮
		btn_select = (ImageView) findViewById(R.id.btn_select);

		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		// 查询条件
		checktext = (TextView) findViewById(R.id.check_type2);
		listcheck = getList();// 装填数据
		// 设置默认显示的Text
		checktext.setText(listcheck.get(0));
		detele_account = (ImageView) findViewById(R.id.detele_account);

	}

	/**
	 * 得到list集合的方法
	 * 
	 * @return
	 */
	public ArrayList<String> getList() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(getResources().getString(R.string.check_examine_reportNO));
		list.add(getResources().getString(R.string.check_examine_time));
		list.add(getResources().getString(R.string.check_examine_liaohaoNO));
		list.add(getResources().getString(R.string.check_examine_floor));
		return list;

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

	/**
	 * 查询条件下拉菜单适配器
	 * 
	 * @author F1330297
	 * 
	 */
	public class ListViewAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		private ArrayList<String> list;

		public ListViewAdapter(Context context, ArrayList<String> list) {
			super();
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.examine_popupwindowitem, null);
			}
			TextView tv = (TextView) convertView
					.findViewById(R.id.checktypetext);
			tv.setText(list.get(position));// 给下拉处出来的listview显示数据
			return convertView;
		}

	}

	public void getTime() {
		LayoutInflater inflater = LayoutInflater
				.from(Examine_table_messageActivity.this);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(
				Examine_table_messageActivity.this);

		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		wheelMain.initDateTimePicker(year, month, day);

		new AlertDialog.Builder(Examine_table_messageActivity.this)
				.setTitle("选择日期").setView(timepickerview)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						timeDate = wheelMain.getTime().toString().trim();
						checkEditText.setText(timeDate);
					}
				}).setNegativeButton("取消", null).show();
	}

	public void checkline() {
		start.getServerData(0, MyConstant.GET_MFG_FLOOR, user.getMFG());
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.alertdialog_item, null);
		final AlertDialog alert = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT).create();
		alert.setView(layout);
		alertdialog_top_tv = (TextView) layout
				.findViewById(R.id.alertdialog_top_tv);
		alertdialog_top_tv.setText("樓層:");
		layout_xuanze_sbu = (LinearLayout) layout
				.findViewById(R.id.layout_xuanze_sbu);
		layout_xuanze_sbu.setVisibility(View.VISIBLE);
		chooseline_sp = (Spinner) layout.findViewById(R.id.choosesbu_sp);
		choosefloor_sp = (Spinner) layout.findViewById(R.id.choosesbu_spinner);
		alertdialog_tv = (TextView) layout.findViewById(R.id.alertdialog_tv);
		alertdialog_tv.setText("線體(設備):");
		alert.setCanceledOnTouchOutside(true);
		alert.setCancelable(true);
		alert.setIcon(R.drawable.ic_system);
		alert.setTitle("系統提示:");
		alert.setMessage("請選擇對應樓層信息");
		choosefloor_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (isfirst) {
					isfirst = true;
				} else {
					floor = arg0.getItemAtPosition(arg2).toString();
					start.getServerData(0, MyConstant.GET_MFG_LINE,
							user.getMFG(), floor);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		chooseline_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (isfirst) {
					isfirst = true;
				} else {
					linename = arg0.getItemAtPosition(arg2).toString();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		alert.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						checkEditText.setText(linename);
					}
				});
		alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						alert.dismiss();
						return;
					}
				});
		alert.show();
	}

	/**
	 * 获取spinner所有权限报表的列表
	 */
	public void getSpinnerData() {

		// type_spinner = (Spinner) findViewById(R.id.item_table_typespin);
		spinnerStr = new String[dataspinn.size()];
		if (dataspinn.size() == 0) {
			UIHelper.ToastMessage(context, "暫無數據");
			return;
		} else {
			for (int i = 0; i < dataspinn.size(); i++) {
				spinnerStr[i] = dataspinn.get(i).getReportName();
			}

			auto_et.setText(spinnerStr[0].toString());
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, spinnerStr);
			auto_et.setAdapter(arrayAdapter);
			// type_spinner.setAdapter(arrayAdapter);
			// repostId = dataspinn.get(0).getReportId();// 默认第一个报表编号
			// reportName = dataspinn.get(0).getReportName();// 默认第一个报表名
			// type_spinner
			// .setOnItemSelectedListener(new
			// AdapterView.OnItemSelectedListener() {
			// @Override
			// public void onItemSelected(AdapterView<?> parent,
			// View view, int position, long id) {
			// repostId = dataspinn.get(position).getReportId();
			// reportName = dataspinn.get(position).getReportName();// 默认第一个报表名
			// }
			//
			// @Override
			// public void onNothingSelected(AdapterView<?> parent) {
			// Toast.makeText(getApplicationContext(), "没有改变的处理",
			// Toast.LENGTH_LONG).show();
			// }
			//
			// });

		}

	}

	private void getDataAll() {
		logonName = user.getLogonName();
		logonMFG = user.getMFG();
		checkstr = checkEditText.getText().toString().trim();
		Log.i(">>>><<datamore}}}}}", datamore + "---");
		if (selectsaoma == 1) {
			repostId = reportNum;
			reportName = rpt;
			Log.i("tag", "-" + rpt);
			check_type = 3;
		} else {
			for (int i = 0; i < dataspinn.size(); i++) {
				if (auto_et.getText().toString()
						.equals(dataspinn.get(i).getReportName())) {
					repostId = dataspinn.get(i).getReportId();
					reportName = dataspinn.get(i).getReportName();
				}
			}
		}
		// check_type查询的类型，checkstr查询的条件内容
		if (linearid.equals("1")) {
			if (checkstr == null || checkstr.equals("")) {
				// 按报表查询所有信息
				/**
				 * 报表编号 MFG 用户名 从第10条开始查询 第1个审核表(全部信息)
				 */
				start.getServerData(style, MyConstant.GET_ALL_EXAMINE_MESSAGE,
						repostId, logonMFG, logonName, datamore + "", "1");
			} else {
				// 按报表和时间查询所有信息
				Log.i(">>>><<type???", check_type + "---" + checkstr);
				/**
				 * 报表编号 MFG 用户名 查询内容 查询条件 数据库从第10条开始查询 第1个审核表(全部信息)
				 */
				start.getServerData(style,
						MyConstant.GET_ALL_EXAMINE_MESSAGE_TYPE, repostId,
						logonMFG, logonName, checkstr, check_type + "",
						datamore + "", "1");
			}
		} else if (linearid.equals("2")) {
			if (checkstr == null || checkstr.equals("")) {
				// 按报表查询所有待审核的信息
				start.getServerData(style, MyConstant.GET_ALL_EXAMINE_MESSAGE,
						repostId, logonMFG, logonName, datamore + "", "2");
			} else {
				// 按报表和时间查询所有待审核的信息
				Log.i(">>>><<type???", check_type + "---" + checkstr);
				start.getServerData(style,
						MyConstant.GET_ALL_EXAMINE_MESSAGE_TYPE, repostId,
						logonMFG, logonName, checkstr, check_type + "",
						datamore + "", "2");
			}
		} else if (linearid.equals("3")) {
			if (checkstr == null || checkstr.equals("")) {
				// 按报表查询所有已审核的信息
				start.getServerData(style, MyConstant.GET_ALL_EXAMINE_MESSAGE,
						repostId, logonMFG, logonName, datamore + "", "3");
			} else {
				// 按报表和时间查询所有已审核的信息
				Log.i(">>>><<type???", check_type + "---" + checkstr);
				start.getServerData(style,
						MyConstant.GET_ALL_EXAMINE_MESSAGE_TYPE, repostId,
						logonMFG, logonName, checkstr, check_type + "",
						datamore + "", "3");
			}
		} else if (linearid.equals("4")) {
			if (checkstr == null || checkstr.equals("")) {
				// 按报表查询所有拒签核的信息
				start.getServerData(style, MyConstant.GET_ALL_EXAMINE_MESSAGE,
						repostId, logonMFG, logonName, datamore + "", "4");
			} else {
				// 按报表和时间查询所有拒签核的信息
				Log.i(">>>><<type???", check_type + "---" + checkstr);
				start.getServerData(style,
						MyConstant.GET_ALL_EXAMINE_MESSAGE_TYPE, repostId,
						logonMFG, logonName, checkstr, check_type + "",
						datamore + "", "4");
			}
		}
		style = 3;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			int j = 0;// 排的序列
			for (String key : keySet) {
				if (key.equals(MyConstant.GET_ALL_EXAMINE_MESSAGE_TYPE)
						|| key.equals(MyConstant.GET_ALL_EXAMINE_MESSAGE)) {
					// 數據遍歷
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString().equals("NULL")
							|| result.get(0).toString() == null) {
					}
					if (listexaminall.size() > 0) {
						listexaminall.clear();
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)
							|| result.get(1).toString()
									.equals(MyConstant.GET_FLAG_NULL_DETAIL)) {
						UIHelper.ToastMessage(context, "暫無可查詢的報表信息", 5000);
						adapter.notifyDataSetChanged();
						return;
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {

						// int a = Integer.parseInt(result.get(result.size() -
						// 1)
						// .toString()); // 遍历数组,“1”即通过审核,“0”则为部分审核
						// int t = result.size() - a - 1;
						// Log.i(">>>>length>>", a + ">>>>" + t);
						// a = t;
						if (linearid.equals("2")) {
							for (int i = 1; i < result.size() - 1; i += 5) {
								j++;
								ExamineAllMessageTableBean examinbeantype = new ExamineAllMessageTableBean(
										result.get(i + 0).toString(), result
												.get(i + 1).toString(), result
												.get(i + 2).toString(), result
												.get(i + 3).toString(), j + "",
										result.get(i + 4).toString());
								listexaminall.add(examinbeantype);
							}
						} else {
							for (int i = 1; i < result.size() - 1; i += 4) {
								j++;
								ExamineAllMessageTableBean examinbeantype = new ExamineAllMessageTableBean(
										result.get(i + 0).toString(), result
												.get(i + 1).toString(), result
												.get(i + 2).toString(), result
												.get(i + 3).toString(), j + "");
								listexaminall.add(examinbeantype);
							}
						}
						if (j < datamore) {
							UIHelper.ToastMessage(context, "沒有更多了");
						}
					}
					adapter.notifyDataSetChanged();
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "未连接服务器....", 0).show();
					}
					result.clear();
				}
				if (key.equals(MyConstant.GET_MFG_ALL_REPORT_NAME)) {
					// 數據遍歷
					result = msg.getData().getStringArrayList(key);
					if (dataspinn.size() > 0) {
						dataspinn.clear();
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL_DETAIL)
							|| result.get(0).toString()
									.equals(MyConstant.GET_FLAG_NULL)) {
						adapter.notifyDataSetChanged();
						Toast.makeText(context, "暫無可查詢的報表", 5000).show();
						return;
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 3) {
							Picture spin = new Picture(
									result.get(i).toString(), result.get(i + 1)
											.toString(), result.get(i + 2)
											.toString());
							// 如果有权限就添加进去
							if (!(result.get(i + 2).toString().equals("0"))) {
								dataspinn.add(spin);
							}
						}
						adapter.notifyDataSetChanged();
					}
					getSpinnerData();
					// adapter.notifyDataSetChanged();
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "未连接服务器....", 0).show();
						adapter.notifyDataSetChanged();
					}
					result.clear();
					getDataAll();
				}
				if (key.equalsIgnoreCase(MyConstant.GET_MFG_FLOOR)) {
					// 數據遍歷
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						floorstr = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							floorstr[i - 1] = result.get(i).toString();
						}
						isnull = true;
					}
					choosefloor_sp.setAdapter(new ArrayAdapter(
							Examine_table_messageActivity.this,
							android.R.layout.simple_dropdown_item_1line,
							floorstr));
					floor = choosefloor_sp.getSelectedItem().toString();
					start.getServerData(0, MyConstant.GET_MFG_LINE,
							user.getMFG(), floor);

				}
				if (key.equalsIgnoreCase(MyConstant.GET_MFG_LINE)) {
					// 數據遍歷
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						linestr = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							linestr[i - 1] = result.get(i).toString();
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "無線體或設備");
					}
					chooseline_sp.setAdapter(new ArrayAdapter(
							Examine_table_messageActivity.this,
							android.R.layout.simple_dropdown_item_1line,
							linestr));
					linename = chooseline_sp.getSelectedItem().toString();

				}
				if (key.equals(MyConstant.GET_QRCODEID)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						selectsaoma = 1;
						if (result.size() > 14) {
							list_qrcheck = new ArrayList<Qr_check_situation>();
							for (int i = 1; i < result.size(); i += 12) {
								Qr_check_situation check_situation;
								String str = "";
								str = result.get(i + 2).toString();
								if (result.get(i + 11).equals(
										"Has been checked")) {
									check_situation = new Qr_check_situation(
											result.get(i).toString().trim(),
											str, result.get(i + 3).toString()
													.trim(), result.get(i + 4)
													.toString().trim(), result
													.get(i + 5).toString()
													.trim(), result.get(i + 6)
													.toString().trim(), result
													.get(i + 7).toString()
													.trim(), result.get(i + 8)
													.toString().trim(), result
													.get(i + 9).toString()
													.trim(), result.get(i + 11)
													.toString().trim(), result
													.get(i + 12).toString()
													.trim());
									i = i + 1;
								} else {
									check_situation = new Qr_check_situation(
											result.get(i).toString().trim(),
											str, result.get(i + 3).toString()
													.trim(), result.get(i + 4)
													.toString().trim(), result
													.get(i + 5).toString()
													.trim(), result.get(i + 6)
													.toString().trim(), result
													.get(i + 7).toString()
													.trim(), result.get(i + 8)
													.toString().trim(), result
													.get(i + 9).toString()
													.trim(), result.get(i + 11)
													.toString().trim(), "");
								}
								list_qrcheck.add(check_situation);
							}
							initPopuWindow(list_qrcheck);
						} else {
							floorName = result.get(1).toString();
							lineName = result.get(3).toString();
							reportNum = result.get(4).toString();
							site = result.get(5).toString();
							mfg = result.get(6).toString();
							sbu = result.get(7).toString();
							rpt = result.get(8).toString();
							section = result.get(9).toString();
							if (user.getSite().trim().toString()
									.equalsIgnoreCase(site)
									&& user.getMFG().trim().toString()
											.equalsIgnoreCase(mfg)) {
								checkEditText.setText(lineName);
								auto_et.setText(rpt);
								getDataAll();
							} else {
								UIHelper.ToastMessage(context, "您無權限查詢此報表");
							}

						}
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "二維碼信息錯誤");
						return;
					}
				}

				if (key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					Toast.makeText(context, "未连接服务器....", 0).show();
					return;
				}

			}
		};
	};

	// 适配器，显示查询的信息
	class MyAdapter extends BaseAdapter {

		public void getRefreshData() {
			getDataAll();
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
					R.layout.examine_table_message_listview, null);
			ExamineAllMessageTableBean listex = listexaminall.get(position);
			String sataend = null;

			final TextView Check_table_numbertv = (TextView) item
					.findViewById(R.id.Check_table_numbertv);
			final TextView state_table_tv = (TextView) item
					.findViewById(R.id.state_table_tv);
			final TextView produce_table_timetv = (TextView) item
					.findViewById(R.id.produce_table_timetv);
			final TextView examine_persontv = (TextView) item
					.findViewById(R.id.examine_persontv);
			final TextView numberOrder = (TextView) item
					.findViewById(R.id.xulie_nb_tv);
			Check_table_numbertv.setText(listex.getCheck_report_NO());
			produce_table_timetv.setText(listex.getCreate_Date());
			examine_persontv.setText(listex.getExamine_person());
			numberOrder.setText(listex.getNumberorder());

			if (listex.getStatus().equals("1")) {
				sataend = getResources().getString(R.string.table_Status_endok);
				state_table_tv.setTextColor(context.getResources().getColor(
						R.color.back));
			} else if (listex.getStatus().equals("0")) {
				sataend = getResources().getString(R.string.table_Status_endno);
				state_table_tv.setTextColor(context.getResources().getColor(
						R.color.color1));
			}

			state_table_tv.setText(sataend);
			return item;
		}

	}

	/**
	 * 没有在onCreate方法中调用initWedget()，而是在onWindowFocusChanged方法中调用，
	 * 是因为initWedget()中需要获取PopupWindow浮动下拉框依附的组件宽度，在onCreate方法中是无法获取到该宽度的
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		while (!isflag) {
			initWedget();
			isflag = true;
		}

	}

	/**
	 * 初始化界面控件
	 */
	private void initWedget() {

		// 初始化界面组件
		parent = (LinearLayout) findViewById(R.id.parent);

		// 获取下拉框依附的组件宽度
		int width = parent.getWidth();
		pwidth = width;

		// 设置点击下拉箭头图片事件，点击弹出PopupWindow浮动下拉框
		btn_select.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isflag) {
					// 显示PopupWindow窗口
					if (dataspinn.size() != 0) {
						popupWindwShowing();
					}
				}
			}
		});
	}

	private void initPopuWindow(final List<Qr_check_situation> list) {
		String[] arr1 = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr1[i] = list.get(i).getRpt() + "(" + list.get(i).getReportNum()
					+ ")-" + list.get(i).getMfg();
		}
		if (popuWindow == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(this);
			contentView = mLayoutInflater.inflate(
					R.layout.popuwindow_chooserpt, null);
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
		popuWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		popuWindow.update();
		ListView listview_chooserpt = (ListView) contentView
				.findViewById(R.id.listview_chooserpt);
		ArrayAdapter<String> adpter = new ArrayAdapter<String>(this,
				R.layout.popuwindow_chooserpt_item, arr1);
		listview_chooserpt.setAdapter(adpter);
		listview_chooserpt.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				floorName = list.get(arg2).getFloorName();
				lineName = list.get(arg2).getLineName();
				reportNum = list.get(arg2).getReportNum();
				site = list.get(arg2).getSite();
				mfg = list.get(arg2).getMfg();
				sbu = list.get(arg2).getSbu();
				rpt = list.get(arg2).getRpt();
				section = list.get(arg2).getSection();
				if (user.getSite().trim().toString().equalsIgnoreCase(site)
						&& user.getMFG().trim().toString()
								.equalsIgnoreCase(mfg)) {
					checkEditText.setText(lineName);
					getDataAll();
				} else {
					UIHelper.ToastMessage(context, "您無權限查詢此報表");
				}
			}
		});
		popuWindow.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
	}

	/**
	 * 显示PopupWindow窗口
	 * 
	 * @param popupwindow
	 */
	public void popupWindwShowing() {
		// 将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
		// 这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
		// （是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
		// selectPopupWindow.showAsDropDown(parent, 0, -3);
		initDatas();
		if (selectPopupWindow == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(this);
			contentView = mLayoutInflater.inflate(R.layout.options, null);
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			selectPopupWindow = new PopupWindow(contentView,
					(display.getWidth() - (display.getWidth() / 15)),
					WindowManager.LayoutParams.WRAP_CONTENT);
		}

		ColorDrawable cd = new ColorDrawable(0x000000);
		selectPopupWindow.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.4f;
		getWindow().setAttributes(lp);

		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setFocusable(true);
		selectPopupWindow.setAnimationStyle(R.style.AppBaseTheme);
		selectPopupWindow.showAtLocation((View) parent.getParent(),
				Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

		selectPopupWindow.update();
		listView = (ListView) contentView.findViewById(R.id.list);

		// 设置自定义Adapter
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, datas);
		listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				auto_et.setText(datas.get(arg2).toString());
				dismiss();
			}
		});
		selectPopupWindow.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});

	}

	/**
	 * 初始化填充Adapter所用List数据
	 */
	private void initDatas() {

		datas.clear();
		for (int i = 0; i < dataspinn.size(); i++) {
			datas.add(dataspinn.get(i).getReportName());
		}

	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 1f;
		getWindow().setAttributes(lp);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			if (requestCode == SCANER_CODE) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				start.getServerData(3, MyConstant.GET_QRCODEID, scanResult);
			}
		}
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
