package com.zsm.foxconn.mypaperless;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.NoCheckBean;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.wheeltime.ScreenInfo;
import com.zsm.foxconn.mypaperless.wheeltime.WheelMain;
import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Examine_no_check_inquiryActivity extends BaseActivity {
	WheelMain wheelMain;
	TextView title;
	EditText timeedt, et_time6, valueText;
	ExpandableListView Expandlistview;
	ImageButton img_bu6;
	Spinner spi6;
	private String timeDate;
	private int year, month, day;
	private RadioGroup radioGroup;
	int checkId;
	long currentDate;
	RadioButton radioButton, radioButton1;
	String token, reportName, reportNum, workStation, val6, checksId, userTime,
			nowTime, dbUserTime, valueTexts, checkReportNO,floorID, lineID,
			xuLie;
	String[] arrD = { "1", "2", "3", "4", "5" };
	String[] arrN = { "6", "7", "8", "9", "10" };
	Context context = Examine_no_check_inquiryActivity.this;
	HttpStart start = null;
	List<NoCheckBean> childlistbean = new ArrayList<NoCheckBean>();
	List<NoCheckBean> parentlistbean = new ArrayList<NoCheckBean>();
	NoCheckBean childbean, parentbean;
	MyAdapter adapter = null;
	UserBean userBean;// =(UserBean)getApplicationContext();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.examine_no_check_inquiry);
		start = new HttpStart(context, handler);
		// 通过ID找到到所需的实例
		radioButton = (RadioButton) findViewById(R.id.radio0);
		radioButton1 = (RadioButton) findViewById(R.id.radio1);
		radioGroup = (RadioGroup) findViewById(R.id.check_type_DayOrNight);
		img_bu6 = (ImageButton) findViewById(R.id.imageButton6);
		spi6 = (Spinner) findViewById(R.id.check_type_Session);
		et_time6 = (EditText) findViewById(R.id.check_type_time);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		valueText = (EditText) findViewById(R.id.do_not_wit_edt);
		// 拿到上一个页面所传输过来的数据
		Intent intent = getIntent();
		reportName = intent.getStringExtra("reportName");
		reportNum = intent.getStringExtra("reportNum");
		workStation = intent.getStringExtra("workStation");
		final ArrayAdapter<String> adapterD = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_multiple_choice, arrD);
		final ArrayAdapter<String> adapterN = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_multiple_choice, arrN);
		// 一些执行的方法集
		init();
		initadapter();
		// 监听radioGroup内radioButton的点击
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// 得到用户输入的时间
						val6 = et_time6.getText().toString().trim();
						// TODO Auto-generated method stub
						if (radioButton1.isChecked()) {
							spi6.setAdapter(adapterN);
							token = "N";
							if (val6.length() == 0) {
								Toast.makeText(context, "請輸入查詢時間!!!",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							spi6.setAdapter(adapterD);
							token = "D";
							if (val6.length() == 0) {
								Toast.makeText(context, "請輸入查詢時間!!!",
										Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
		// 通过监听得到下拉菜单点击的值
		spi6.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String sp6 = spi6.getSelectedItem().toString();
				checkId = 0;
				checksId = "";
				try {
					checkId = Integer.parseInt(sp6);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				// TODO Auto-generated method stub
				if (token == "N") {
					Toast.makeText(context, "您所选的节次为晚班第:" + checkId + "節",
							Toast.LENGTH_SHORT).show();
					checksId = String.valueOf(checkId);
				} else if (token == "D") {
					Toast.makeText(context, "您所选的节次为白班第:" + checkId + "節",
							Toast.LENGTH_SHORT).show();
					checksId = String.valueOf(checkId);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		/**
		 * 點擊搜索圖標，拿到時間、點擊按鈕、下拉菜單
		 */
		img_bu6.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 得到当前日期
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				nowTime = df.format(new Date()).toString();
				currentDate = Integer.parseInt(nowTime);

				// 得到用户选择的日期
				et_time6 = (EditText) findViewById(R.id.check_type_time);
				long str2 = 0;
				try {
					val6 = et_time6.getText().toString().trim();
					userTime = val6.replaceAll("/", "");
					str2 = Integer.parseInt(userTime);
				} catch (NumberFormatException e) {
					// TODO: handle exception
				}
				if (str2 >= currentDate) {
					Toast.makeText(context, "日期必须小于当前日期!!!", Toast.LENGTH_SHORT)
							.show();
				} else {
					getDataMore();
				}
			}
		});
		title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(workStation + "未及時點檢報表");
	}

	/**
	 * 点击输入框，弹出选择时间控件
	 * 
	 * @author f1330304
	 */
	public void init() {
		// 返回主页
		Button homebtn = (Button) findViewById(R.id.btn_submit);
		homebtn.setBackgroundResource(R.drawable.tabbar_home1);
		homebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		timeedt = (EditText) findViewById(R.id.check_type_time);
		// 得到系統時間
		String yyyy = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		yyyy = formatter.format(curDate);
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		// 点击listview的item后响应点击事件
		timeedt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater
						.from(Examine_no_check_inquiryActivity.this);
				final View timepickerview = inflater.inflate(
						R.layout.timepicker, null);
				ScreenInfo screenInfo = new ScreenInfo(
						Examine_no_check_inquiryActivity.this);
				wheelMain = new WheelMain(timepickerview);
				wheelMain.screenheight = screenInfo.getHeight();
				wheelMain.initDateTimePicker(year, month, day);

				new AlertDialog.Builder(Examine_no_check_inquiryActivity.this)
						.setTitle("选择日期")
						.setView(timepickerview)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										timeDate = wheelMain.getTime()
												.toString().trim();
										timeedt.setText(timeDate);
									}
								}).setNegativeButton("取消", null).show();

			}

		});
	}

	// 给ExpandableListView添加适配器
	public void initadapter() {
		Expandlistview = (ExpandableListView) this
				.findViewById(R.id.examine_more_listView6);
		// initData();// 整理数据
		adapter = new MyAdapter();
		Expandlistview.setAdapter(adapter);
		Expandlistview.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub

				return false;
			}
		});
		Expandlistview.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					final int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub

				return false;
			}
		});
	}

	// userBean.getMFG()
	public void getDataMore() {
		// 按点检编号查询所属信息
		start.getServerData(3, MyConstant.GET_NO_CHECK_LINE,reportName,
				checksId, userBean.getMFG(), userTime, token, workStation);
	}

	public void insertDate() {
		start.getServerData(0, MyConstant.GET_INSERT_BASEINFO, checkReportNO,
				checksId, valueTexts, nowTime, userBean.getLogonName());
	}

	// 帶出數據
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.equals(MyConstant.GET_NO_CHECK_LINE)) {
					// 數據遍歷
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (childlistbean.size() > 0) {
						childlistbean.clear();
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)
							|| result.get(1).toString()
									.equals(MyConstant.GET_FLAG_NULL_DETAIL)) {
						adapter.notifyDataSetChanged();
						UIHelper.ToastMessage(context, "暫無可查詢的報表信息", 5000);
						return;
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						int j = 0;
						for (int i = 1; i < result.size(); i += 4) {
							j++;
							parentbean = new NoCheckBean(result.get(i + 0)
									.toString(), result.get(i + 1).toString(),
									result.get(i + 2).toString(), result.get(
											i + 3).toString(), token, val6
											+ "第" + checksId + "節", j + "");
							parentlistbean.add(parentbean);
							childlistbean = parentlistbean;
							
						}
						UIHelper.ToastMessage(context, "暫無可查詢的報表信息", 5000);
						Log.i(">>parentlistbean>>>", parentlistbean.size() + "");

					}
					adapter.notifyDataSetChanged();
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						adapter.notifyDataSetChanged();
						Toast.makeText(context, "未连接服务器....", 0).show();
					}
					result.clear();
				} else if (key.equals(MyConstant.GET_INSERT_BASEINFO)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						start.getServerData(0, MyConstant.GET_INSERT_REPORT,
								checkReportNO, checksId, reportNum,
								userBean.getMFG(), floorID, lineID, nowTime,
								token, userBean.getLogonName());

					}
				} else if (key.equals(MyConstant.GET_INSERT_REPORT)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						start.getServerData(0,
								MyConstant.GET_INSERT_REPORTCHECK,
								checkReportNO, checksId,
								userBean.getLogonName(), nowTime);
					}
				} else if (key.equals(MyConstant.GET_INSERT_REPORTCHECK)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						getDataMore();
					}
				}
			}
		};
	};

	class MyAdapter extends BaseExpandableListAdapter {

		// 得到子item需要关联的数据
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// int key = parentlistbean.get(groupPosition);
			// return (map.get(key).get(childPosition));获取当前父容器item的子item的id位置
			return childlistbean.get(groupPosition);// 获取当前childd
													// item的位置,child只有一个item,故是groupPosition
		}

		// 得到子item的ID
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// return childPosition;
			return groupPosition;
		}

		// 设置子item的组件
		@Override
		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) Examine_no_check_inquiryActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.examine_no_check_inquirychild, null);
			}
			final EditText teEditText = (EditText) convertView
					.findViewById(R.id.do_not_wit_edt);
			Button tButton = (Button) convertView.findViewById(R.id.toup_btn);
			String teString = teEditText.getText().toString();
			tButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View convertView) {
					// TODO Auto-generated method stub
					valueTexts = ChangString.change(teEditText.getText().toString().trim());
					if (valueTexts.length() == 0) {
						Toast.makeText(context, "請填寫原因!!!", Toast.LENGTH_SHORT)
								.show();
					} else {
						// 獲得時間,并截取長度,去除/
						dbUserTime = parentlistbean.get(groupPosition)
								.getDates().toString();
						dbUserTime = dbUserTime.substring(0, 10);
						dbUserTime = dbUserTime.replaceAll("/", "");
						workStation = parentlistbean.get(groupPosition)
								.getSection_name().toString();
						lineID = parentlistbean.get(groupPosition)
								.getLine_name().toString();
						token = parentlistbean.get(groupPosition).getDaynight()
								.toString();
						checkReportNO = dbUserTime + workStation + lineID
								+ token;
						floorID = parentlistbean.get(groupPosition)
								.getFloor_name().toString();
						insertDate();
					}
				}
			});
			return convertView;
		}

		// 获取当前父item下的子item的个数
		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		// 获取当前父item的数据
		@Override
		public Object getGroup(int groupPosition) {
			return parentlistbean.get(groupPosition);
		}

		// 获取父item要显示的行数
		@Override
		public int getGroupCount() {
			return parentlistbean.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		// 设置父item组件
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) Examine_no_check_inquiryActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.examine_no_check_inquiryparent, null);
			}

			NoCheckBean parentlist = parentlistbean.get(groupPosition);
			TextView node_number = (TextView) convertView
					.findViewById(R.id.xulies_nb_tv);
			TextView mfgnaem_tv = (TextView) convertView
					.findViewById(R.id.mfgnaem_tv);
			TextView gongzhan_tv = (TextView) convertView
					.findViewById(R.id.gongzhan_tv);
			TextView floor_tv2 = (TextView) convertView
					.findViewById(R.id.floor_tv2);
			TextView lines_nb_tv = (TextView) convertView
					.findViewById(R.id.lines_nb_tv);
			TextView class_diff_tv = (TextView) convertView
					.findViewById(R.id.class_diff_tv);
			TextView datejiechi_tv = (TextView) convertView
					.findViewById(R.id.datejiechi_tv);

			node_number.setText(parentlist.getNumbers());
			mfgnaem_tv.setText(parentlist.getMfg_name());
			gongzhan_tv.setText(parentlist.getSection_name());
			floor_tv2.setText(parentlist.getFloor_name());
			lines_nb_tv.setText(parentlist.getLine_name());
			class_diff_tv.setText(parentlist.getDaynight());
			datejiechi_tv.setText(parentlist.getDates());
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	// 返回键按钮
	public void activity_back(View v) {
		this.finish();
		// overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		if (userBean.getLogonName() == null
				|| userBean.getLogonName().length() == 0) {
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
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}
}
