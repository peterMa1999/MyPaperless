package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.Detailed_update_add.MyAdapter.ViewHolder;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

/**
 * 
 * @author mgp 修改、添加參數
 */
public class Detailed_update_add extends BaseActivity {

	private Context context = Detailed_update_add.this;
	private LinearLayout layout_huihanlu, layout_yinshuji;
	private Spinner sp_de_side, sp_de_floor, sp_de_line;
	private TextView tv_update_person;
	private EditText ed_de_jizhong, ed_yali, ed_yinshua_sudu, ed_tuomo_sudu,
			ed_tuomo_jianju, ed_auto_pinlv, ed_guadao_changdu, ed_zone1,
			ed_zone2, ed_zone3, ed_zone4, ed_zone5, ed_zone6, ed_zone7,
			ed_zone8, ed_zone9, ed_zone10, ed_zone11, ed_zone12, ed_zone13,
			ed_speed, ed_fanspeed, ed_shuntvalue;
	private EditText ed_wentimiaoshu;
	private Intent intent;
	private String parameternum, operating, report_num, report_Name,
			jizhong_num, floo_name, line_name, Side, section, th_floor,
			th_line, teamid, wentimiaoshu;
	private String yali, yinshua_sudu, tuomo_sudu, tuomo_jianju, auto_pinlv,
			guadao_changdu, zone1, zone2, zone3, zone4, zone5, zone6, zone7,
			zone8, zone9, zone10, zone11, zone12, zone13, speed, fanspeed,
			shuntvalue;
	private HttpStart httpStart;
	private UserBean userBean;
	private String[] linestr = null, floorstr = null;
	private Button bt_detailed_up_or_de;
	private boolean isFrist = false;
	private View textEntryView;
	private ListView lvPerson;
	private static Spinner spPartment;
	private MyAdapter myAdapter;
	private String team[] = null, checknameid[] = null, checknamenum[] = null;
	private List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	String checkposition[][] = null; // 選擇的審核人存在此處
	private String namestr = ""; // 審核人拼接成字符串
	private int m;
	ArrayAdapter adapter1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_update_add);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		httpStart = new HttpStart(context, handler);
		intent = getIntent();
		operating = intent.getStringExtra("operating");
		report_num = intent.getStringExtra("report_num");
		report_Name = intent.getStringExtra("report_Name");
		section = intent.getStringExtra("section");
		findViewById();
		httpStart.getServerData(0, MyConstant.GET_FLOOR_IPQC,
				userBean.getLogonName(), section, userBean.getMFG(),userBean.getBU());

		sp_de_floor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				th_floor = arg0.getItemAtPosition(arg2).toString();
				httpStart.getServerData(3, MyConstant.GET_CHECK_LINE,
						userBean.getMFG(), section, th_floor,userBean.getSBU());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		sp_de_line.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				th_line = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		String[] item = getResources().getStringArray(R.array.canshu_mianbie);
		for (int j = 0; j < item.length; j++) {
			if (item[j].equals(Side)) {
				sp_de_side.setSelection(j, true);
			}
		}
		sp_de_side.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Side = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		ed_de_jizhong.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (operating.equals("2")) {
					UIHelper.ToastMessage(context, "機種型號修改無效");
				}
				if (s.length() > 5) {
//					UIHelper.ToastMessage(context, "得到上一個版本號參數");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = new ArrayList<String>();
				result = msg.getData().getStringArrayList(key);
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
					sp_de_floor.setAdapter(new ArrayAdapter(context,
							android.R.layout.simple_dropdown_item_1line,
							floorstr));
					for (int i = 0; i < floorstr.length; i++) {
						if (floorstr[i].equals(floo_name)) {
							sp_de_floor.setSelection(i, true);
						}
					}
					th_floor = sp_de_floor.getSelectedItem().toString();
					httpStart.getServerData(3, MyConstant.GET_CHECK_LINE,
							userBean.getMFG(), section, th_floor,userBean.getSBU().toString());
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
						linestr = new String[result.size()];
						linestr[0] = "無";
						UIHelper.ToastMessage(context, "暫無線別信息");
						return;
					}
					sp_de_line.setAdapter(new ArrayAdapter(context,
							android.R.layout.simple_dropdown_item_1line,
							linestr));
					for (int i = 0; i < linestr.length; i++) {
						if (linestr[i].equals(line_name)) {
							sp_de_line.setSelection(i, true);
						}
					}
					th_line = sp_de_line.getSelectedItem().toString();
				}
				if (key.equalsIgnoreCase(MyConstant.GET_SIGNOFF_NAME)) {

					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						checknameid = new String[(result.size() - 1) / 2];
						checknamenum = new String[(result.size() - 1) / 2];
						for (int i = 0, j = 1; j < result.size(); i++, j += 2) {
							checknameid[i] = result.get(j).toString() + ","
									+ result.get(j + 1);
							checknamenum[i] = result.get(j).toString();
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						checknameid = new String[result.size()];
						checknamenum = new String[result.size()];
						checknameid[1] = "null";
						checknamenum[1] = "null";
					}

					adapter1.notifyDataSetChanged();
					Adapter2();
				}
				if (key.equalsIgnoreCase(MyConstant.ADD_CANSHU_INFO)) {

					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						m = 1;
						UIHelper.ToastMessage(context, "提交成功，等待主管審核中");
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "提交失敗");
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_EXCEPTION)) {
						UIHelper.ToastMessage(context, "參數已存在");
					}
				}
				if (key.equalsIgnoreCase(MyConstant.UPDATE_CANSHU_INFO)) {

					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						m = 1;
						UIHelper.ToastMessage(context, "提交成功，等待主管審核中");
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "提交異常");
					}
				}
				if (key.equalsIgnoreCase(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(context, "未連接服務器");
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

		layout_yinshuji = (LinearLayout) findViewById(R.id.layout_yinshuji);
		layout_huihanlu = (LinearLayout) findViewById(R.id.layout_huihanlu);
		sp_de_floor = (Spinner) findViewById(R.id.sp_de_floor);
		sp_de_line = (Spinner) findViewById(R.id.sp_de_line);
		ed_de_jizhong = (EditText) findViewById(R.id.ed_de_jizhong);
		sp_de_side = (Spinner) findViewById(R.id.sp_de_side);
		tv_update_person = (TextView) findViewById(R.id.tv_update_person);
		tv_update_person.setText(userBean.getChineseName());
		bt_detailed_up_or_de = (Button) findViewById(R.id.bt_detailed_up_or_de);
		if (report_num.equalsIgnoreCase("FM3NCD034003A")) {
			ed_yali = (EditText) findViewById(R.id.ed_yali);
			ed_yinshua_sudu = (EditText) findViewById(R.id.ed_yinshua_sudu);
			ed_tuomo_sudu = (EditText) findViewById(R.id.ed_tuomo_sudu);
			ed_tuomo_jianju = (EditText) findViewById(R.id.ed_tuomo_jianju);
			ed_auto_pinlv = (EditText) findViewById(R.id.ed_auto_pinlv);
			ed_guadao_changdu = (EditText) findViewById(R.id.ed_guadao_changdu);
			layout_yinshuji.setVisibility(View.VISIBLE); // 顯示印刷機參數

		} else if (report_num.equalsIgnoreCase("FM3NCD034003B")) {
			ed_zone1 = (EditText) findViewById(R.id.ed_zone1);
			ed_zone2 = (EditText) findViewById(R.id.ed_zone2);
			ed_zone3 = (EditText) findViewById(R.id.ed_zone3);
			ed_zone4 = (EditText) findViewById(R.id.ed_zone4);
			ed_zone5 = (EditText) findViewById(R.id.ed_zone5);
			ed_zone6 = (EditText) findViewById(R.id.ed_zone6);
			ed_zone7 = (EditText) findViewById(R.id.ed_zone7);
			ed_zone8 = (EditText) findViewById(R.id.ed_zone8);
			ed_zone9 = (EditText) findViewById(R.id.ed_zone9);
			ed_zone10 = (EditText) findViewById(R.id.ed_zone10);
			ed_zone11 = (EditText) findViewById(R.id.ed_zone11);
			ed_zone12 = (EditText) findViewById(R.id.ed_zone12);
			ed_zone13 = (EditText) findViewById(R.id.ed_zone13);
			ed_speed = (EditText) findViewById(R.id.ed_speed);
			ed_fanspeed = (EditText) findViewById(R.id.ed_fanspeed);
			ed_shuntvalue = (EditText) findViewById(R.id.ed_shuntvalue);
			layout_huihanlu.setVisibility(View.VISIBLE); // 顯示回焊爐參數
		}
		if (operating.equals("2")) {
			parameternum = intent.getStringExtra("parameternum");
			jizhong_num = intent.getStringExtra("jizhong_num");
			floo_name = intent.getStringExtra("floo_name");
			line_name = intent.getStringExtra("line_name");
			Side = intent.getStringExtra("Side");
			if (report_num.equalsIgnoreCase("FM3NCD034003A")) {
				yali = intent.getStringExtra("ed_yali");
				yinshua_sudu = intent.getStringExtra("ed_yinshua_sudu");
				tuomo_sudu = intent.getStringExtra("ed_tuomo_sudu");
				tuomo_jianju = intent.getStringExtra("ed_tuomo_jianju");
				auto_pinlv = intent.getStringExtra("ed_auto_pinlv");
				guadao_changdu = intent.getStringExtra("ed_guadao_changdu");
				ed_yali.setText(yali);
				ed_yinshua_sudu.setText(yinshua_sudu);
				ed_tuomo_sudu.setText(tuomo_sudu);
				ed_tuomo_jianju.setText(tuomo_jianju);
				ed_auto_pinlv.setText(auto_pinlv);
				ed_guadao_changdu.setText(guadao_changdu);
			} else if (report_num.equalsIgnoreCase("FM3NCD034003B")) {
				zone1 = intent.getStringExtra("ed_zone1");
				zone2 = intent.getStringExtra("ed_zone2");
				zone3 = intent.getStringExtra("ed_zone3");
				zone4 = intent.getStringExtra("ed_zone4");
				zone5 = intent.getStringExtra("ed_zone5");
				zone6 = intent.getStringExtra("ed_zone6");
				zone7 = intent.getStringExtra("ed_zone7");
				zone8 = intent.getStringExtra("ed_zone8");
				zone9 = intent.getStringExtra("ed_zone9");
				zone10 = intent.getStringExtra("ed_zone10");
				zone11 = intent.getStringExtra("ed_zone11");
				zone12 = intent.getStringExtra("ed_zone12");
				zone13 = intent.getStringExtra("ed_zone13");
				speed = intent.getStringExtra("ed_speed");
				fanspeed = intent.getStringExtra("ed_fanspeed");
				shuntvalue = intent.getStringExtra("ed_shuntvalue");
				ed_zone1.setText(zone1);
				ed_zone2.setText(zone2);
				ed_zone3.setText(zone3);
				ed_zone4.setText(zone4);
				ed_zone5.setText(zone5);
				ed_zone6.setText(zone6);
				ed_zone7.setText(zone7);
				ed_zone8.setText(zone8);
				ed_zone9.setText(zone9);
				ed_zone10.setText(zone10);
				ed_zone11.setText(zone11);
				ed_zone12.setText(zone12);
				ed_zone13.setText(zone13);
				ed_speed.setText(speed);
				ed_fanspeed.setText(fanspeed);
				ed_shuntvalue.setText(shuntvalue);

			}
			ed_de_jizhong.setText(jizhong_num);
		}

	}

	public void add(View v) {
		if (report_num.equalsIgnoreCase("FM3NCD034003A")) {
			if (ed_de_jizhong.getText().length() == 0
					|| ed_yali.getText().length() == 0
					|| ed_yinshua_sudu.getText().length() == 0
					|| ed_tuomo_sudu.getText().length() == 0
					|| ed_tuomo_jianju.getText().length() == 0
					|| ed_auto_pinlv.getText().length() == 0
					|| ed_guadao_changdu.getText().length() == 0) {
				UIHelper.ToastMessage(context, "不能為空");
			} else {
				adiog();
			}
		} else if (report_num.equalsIgnoreCase("FM3NCD034003B")) {
			if (ed_de_jizhong.getText().length() == 0
					|| ed_zone1.getText().length() == 0
					|| ed_zone2.getText().length() == 0
					|| ed_zone3.getText().length() == 0
					|| ed_zone4.getText().length() == 0
					|| ed_zone5.getText().length() == 0
					|| ed_zone6.getText().length() == 0
					|| ed_zone7.getText().length() == 0
					|| ed_zone8.getText().length() == 0
					|| ed_zone9.getText().length() == 0
					|| ed_zone10.getText().length() == 0
					|| ed_zone11.getText().length() == 0
					|| ed_zone12.getText().length() == 0
					|| ed_zone13.getText().length() == 0
					|| ed_speed.getText().length() == 0
					|| ed_fanspeed.getText().length() == 0
					|| ed_shuntvalue.getText().length() == 0) {
				UIHelper.ToastMessage(context, "不能為空");
			} else {
				adiog();
			}

		}
	}

	// 返回键按钮
	public void activity_back(View v) {
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
		this.finish();
	}

	public void adiog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.alert_item, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT).create();
		alertDialog.setView(layout);
		ed_wentimiaoshu = (EditText) layout.findViewById(R.id.ed_wentimiaoshu);
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.setCancelable(true);
		alertDialog.setIcon(R.drawable.ic_system);
		alertDialog.setTitle("系統提示:");
		alertDialog.setMessage("是否提交至主管審核?");
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						team = new String[1];
						team[0] = userBean.getTeam().toString();
						wentimiaoshu = ed_wentimiaoshu.getText().toString();
						if (wentimiaoshu.length() == 0) {
							wentimiaoshu = "NULL";
						}
						wentimiaoshu = ChangString.change(wentimiaoshu);
						LayoutInflater factory;
						httpStart.getServerData(0, MyConstant.GET_SIGNOFF_NAME,
								userBean.getTeam(), userBean.getMFG());
						factory = LayoutInflater.from(context);

						// 把activity_login中的控件定義在View中
						textEntryView = factory.inflate(
								R.layout.adialog_addsignoffuser, null);
						spPartment = (Spinner) textEntryView
								.findViewById(R.id.partment);
						lvPerson = (ListView) textEntryView
								.findViewById(R.id.person);
						adapter1 = new ArrayAdapter(context,
								android.R.layout.simple_spinner_dropdown_item,
								team);
						spPartment.setAdapter(adapter1);
						spPartment
								.setOnItemSelectedListener(new OnItemSelectedListener() {
									@Override
									public void onItemSelected(
											AdapterView<?> parent, View arg1,
											int position, long arg3) {
										if (!isFrist) {
											isFrist = true;
										} else {
											teamid = parent.getItemAtPosition(
													position).toString();
											httpStart
													.getServerData(
															0,
															MyConstant.GET_SIGNOFF_NAME,
															teamid,
															userBean.getMFG());
										}
									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {
									}

								});

						showWaiterAuthorizationDialog();
					}
				});
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						alertDialog.dismiss();
						return;
					}
				});
		alertDialog.show();
	}

	private void Adapter2() {
		checkposition = new String[1][];

		// 当第二次执行之后就清空list值
		if (list.size() > 0) {
			list.clear();
		}
		if (checknameid[0].toString().equalsIgnoreCase("null")
				|| checknameid.length == 0) {
			UIHelper.ToastMessage(context, "暫無數據");
			return;
		} else {
			list = new ArrayList<HashMap<String, Object>>();
			for (int j = 0; j < checknameid.length; j++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("checkusername", checknameid[j].toString());
				map.put("checkstatus", false);
				list.add(map);
			}
			myAdapter = new MyAdapter(context, list,
					R.layout.listview_floor_item, new String[] {
							"checkusername", "checkstatus" }, new int[] {
							R.id.textViewline, R.id.checkline }, checknamenum);
			lvPerson.setAdapter(myAdapter);
			lvPerson.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					ViewHolder holder = (ViewHolder) arg1.getTag();
					holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态
					myAdapter.isSelected.put(arg2, holder.cb.isChecked()); // 同时修改map的值保存状态
					if (holder.cb.isChecked() == true) {
						checkposition[(int) spPartment.getSelectedItemId()][arg2] = checknamenum[arg2]; // 加入數組

					} else {
						checkposition[(int) spPartment.getSelectedItemId()][arg2] = null; // 選擇狀態為false時為null
					}
				}
			});

		}

	}

	// listview的适配器
	public class MyAdapter extends BaseAdapter {

		public HashMap<Integer, Boolean> isSelected;
		private Context context = null;
		private List<HashMap<String, Object>> list = null;
		private String keyString[] = null;
		private String itemString = null; // 记录每个item中textview的值
		private int idValue[] = null;// id值

		public class ViewHolder {
			public TextView tv = null;
			public CheckBox cb = null;
		}

		public MyAdapter(Context context, List<HashMap<String, Object>> list2,
				int resource, String[] from, int[] to, String[] checknamenum) {
			this.context = context;
			this.list = list2;
			init();
		}

		// 初始化 设置所有checkbox都为未选择
		public void init() {
			isSelected = new HashMap<Integer, Boolean>();
			if (checkposition[(int) spPartment.getSelectedItemId()] == null) {
				for (int i = 0; i < list.size(); i++) {
					isSelected.put(i, false);
				}
				checkposition[(int) spPartment.getSelectedItemId()] = new String[list
						.size()];
			} else {
				for (int j = 0; j < checkposition[(int) spPartment
						.getSelectedItemId()].length; j++) {
					if (checkposition[(int) spPartment.getSelectedItemId()][j] != null) {
						isSelected.put(j, true);
					} else {
						isSelected.put(j, false);
					}
				}
			}

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(context).inflate(
						R.layout.listview_floor_item, null);
				holder.tv = (TextView) view.findViewById(R.id.textViewline);
				holder.cb = (CheckBox) view.findViewById(R.id.checkline);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.tv.setText(list.get(position).get("checkusername")
					.toString());
			holder.cb.setChecked((Boolean) list.get(position)
					.get("checkstatus"));
			return view;
		}
	}

	public void showWaiterAuthorizationDialog() {
		Builder builder, builder2;

		m = 0;
		builder = new AlertDialog.Builder(context);

		AlertDialog alert = builder
				// 對話框的標題
				.setTitle("请选择提交审核人：")
				.setView(textEntryView)
				.setPositiveButton("确认提交",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (namestr.length()>0) {
									namestr = "";
								}
								for (int i = 0; i < checkposition.length; i++) {
									if (checkposition[i] != null) {
										for (int j = 0; j < checkposition[i].length; j++) {
											if (checkposition[i][j] != null) {
												// listline.add(checkposition[i][j].toString());
												namestr += checkposition[i][j]
														.toString() + MyConstant.GET_FLAG1;
												continue;
											} else {
												continue;
											}
										}
									}
								}

								if (m == 0) {
									if (namestr.length() != 0) {
										if (operating.equals("1")) {
											if (report_num
													.equalsIgnoreCase("FM3NCD034003A")) {
												httpStart
														.getServerData(
																3,
																MyConstant.ADD_CANSHU_INFO,
																report_num,
																th_floor,
																th_line,
																ed_de_jizhong
																		.getText()
																		.toString(),
																Side,
																ed_yali.getText()
																		.toString(),
																ed_yinshua_sudu
																		.getText()
																		.toString(),
																ed_tuomo_sudu
																		.getText()
																		.toString(),
																ed_tuomo_jianju
																		.getText()
																		.toString(),
																ed_auto_pinlv
																		.getText()
																		.toString(),
																ed_guadao_changdu
																		.getText()
																		.toString(),
																wentimiaoshu,
																userBean.getLogonName(),
																namestr);

											} else if (report_num
													.equalsIgnoreCase("FM3NCD034003B")) {
												httpStart
														.getServerData(
																3,
																MyConstant.ADD_CANSHU_INFO,
																report_num,
																th_floor,
																th_line,
																ed_de_jizhong
																		.getText()
																		.toString(),
																Side,
																ed_zone1.getText()
																		.toString(),
																ed_zone2.getText()
																		.toString(),
																ed_zone3.getText()
																		.toString(),
																ed_zone4.getText()
																		.toString(),
																ed_zone5.getText()
																		.toString(),
																ed_zone6.getText()
																		.toString(),
																ed_zone7.getText()
																		.toString(),
																ed_zone8.getText()
																		.toString(),
																ed_zone9.getText()
																		.toString(),
																ed_zone10
																		.getText()
																		.toString(),
																ed_zone11
																		.getText()
																		.toString(),
																ed_zone12
																		.getText()
																		.toString(),
																ed_zone13
																		.getText()
																		.toString(),
																ed_speed.getText()
																		.toString(),
																ed_fanspeed
																		.getText()
																		.toString(),
																ed_shuntvalue
																		.getText()
																		.toString(),
																wentimiaoshu,
																userBean.getLogonName(),
																namestr);
											}
										} else if (operating.equals("2")) {
											if (report_num
													.equalsIgnoreCase("FM3NCD034003A")) {
												httpStart
														.getServerData(
																3,
																MyConstant.UPDATE_CANSHU_INFO,
																report_num,
																parameternum,
																th_floor,
																th_line,
																ed_de_jizhong
																		.getText()
																		.toString(),
																Side,
																ed_yali.getText()
																		.toString(),
																ed_yinshua_sudu
																		.getText()
																		.toString(),
																ed_tuomo_sudu
																		.getText()
																		.toString(),
																ed_tuomo_jianju
																		.getText()
																		.toString(),
																ed_auto_pinlv
																		.getText()
																		.toString(),
																ed_guadao_changdu
																		.getText()
																		.toString(),
																wentimiaoshu,
																userBean.getLogonName(),
																namestr);

											} else if (report_num
													.equalsIgnoreCase("FM3NCD034003B")) {
												httpStart
														.getServerData(
																3,
																MyConstant.UPDATE_CANSHU_INFO,
																report_num,
																parameternum,
																th_floor,
																th_line,
																jizhong_num,
																Side,
																ed_zone1.getText()
																		.toString(),
																ed_zone2.getText()
																		.toString(),
																ed_zone3.getText()
																		.toString(),
																ed_zone4.getText()
																		.toString(),
																ed_zone5.getText()
																		.toString(),
																ed_zone6.getText()
																		.toString(),
																ed_zone7.getText()
																		.toString(),
																ed_zone8.getText()
																		.toString(),
																ed_zone9.getText()
																		.toString(),
																ed_zone10
																		.getText()
																		.toString(),
																ed_zone11
																		.getText()
																		.toString(),
																ed_zone12
																		.getText()
																		.toString(),
																ed_zone13
																		.getText()
																		.toString(),
																ed_speed.getText()
																		.toString(),
																ed_fanspeed
																		.getText()
																		.toString(),
																ed_shuntvalue
																		.getText()
																		.toString(),
																wentimiaoshu,
																userBean.getLogonName(),
																namestr);
											}
										}
									} else {
										UIHelper.ToastMessage(context, "請選擇審核人");
										m = 0;
										return;
									}
								} else {
									UIHelper.ToastMessage(context, "已經提交過了~",
											5000);
									m = 0;
								}
							}
						})

				// 对话框的“退出”单击事件
				.setNegativeButton("退出", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// AdialogActivity.this.finish();
					}
				})
				// 设置dialog是否为模态，false表示模态，true表示非模态
				.setCancelable(false).create();

		alert.show();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

}
