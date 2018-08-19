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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.zsm.foxconn.mypaperless.Detailed_canshu.MyAdapter.ViewHolder;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

public class Detailed_canshu extends BaseActivity {
	private ImageView imageview_qiehuan;
	private LinearLayout layout_huihanlu, layout_yinshuji,layout_canchu_beizhu,layout_ischeck;
	private TextView title, textview_update, tv_ischeck;
	private Context context = Detailed_canshu.this;
	private Intent intent;
	private String report_num, report_Name, jizhong_num, floo_name, line_name,
			Side = "BOT", parameternum, teamid, section, wentimiaoshu, ischeck,
			ischeck_ss = "", check_st = "", canshu_check = "", create_date;
	private UserBean userBean;
	private HttpStart httpStart;
	private TextView tv_de_floor, tv_de_line, tv_de_jizhong, tv_de_side,
			tv_last_update, tv_update_person;
	private TextView ed_yali, ed_yinshua_sudu, ed_tuomo_sudu, ed_tuomo_jianju,
			ed_auto_pinlv, ed_guadao_changdu, ed_zone1, ed_zone2, ed_zone3,
			ed_zone4, ed_zone5, ed_zone6, ed_zone7, ed_zone8, ed_zone9,
			ed_zone10, ed_zone11, ed_zone12, ed_zone13, ed_speed, ed_fanspeed,
			ed_shuntvalue,tv_beizhu;
	private EditText ed_wentimiaoshu;
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
	private Button bt_detailed_update, bt_detailed_delete;
	private String check_type;
	ArrayAdapter adapter1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_canshu);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		httpStart = new HttpStart(context, handler);
		intent = getIntent();
		ischeck = intent.getStringExtra("ischeck");
		report_Name = intent.getStringExtra("report_Name");
		report_num = intent.getStringExtra("report_num");
		section = intent.getStringExtra("section");
		if (ischeck.equals("0")) {
			jizhong_num = intent.getStringExtra("jizhong_num");
			floo_name = intent.getStringExtra("floo_name");
			line_name = intent.getStringExtra("line_name");
			parameternum = floo_name + line_name + jizhong_num + Side; // 參數表編號
		} else if (ischeck.equals("1")) {
			parameternum = intent.getStringExtra("parameternum");
			ischeck_ss = intent.getStringExtra("ischeck_ss");
			create_date = intent.getStringExtra("create_date");
			if (ischeck_ss.equals("1")) {
				check_st = intent.getStringExtra("check_st");
			}
			if (ischeck_ss.equals("2")) {
				check_st = intent.getStringExtra("check_st");
				canshu_check = intent.getStringExtra("canshu_check");
			}
		}
		findViewById();
		if (ischeck.equals("0")) {        
			httpStart.getServerData(0, MyConstant.GET_CANSHU_INFO, report_num,
					"0", parameternum);
		} else if (ischeck.equals("1")) {
			httpStart.getServerData(0, MyConstant.GET_CANSHU_INFO, report_num,
					"1", parameternum, create_date);
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = new ArrayList<String>();
				result = msg.getData().getStringArrayList(key);
				if (key.equalsIgnoreCase(MyConstant.GET_CANSHU_INFO)) {
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						if (report_num.equalsIgnoreCase("FM3NCD034003A")) {
							ed_yali.setText(result.get(1).toString());
							ed_yinshua_sudu.setText(result.get(2).toString());
							ed_tuomo_sudu.setText(result.get(3).toString());
							ed_tuomo_jianju.setText(result.get(4).toString());
							ed_auto_pinlv.setText(result.get(5).toString());
							ed_guadao_changdu.setText(result.get(6).toString());
							tv_last_update.setText(result.get(7).toString());
							tv_update_person.setText(result.get(8).toString());
							tv_de_floor.setText(result.get(9).toString());
							tv_de_line.setText(result.get(10).toString());
							tv_de_jizhong.setText(result.get(11).toString());
							tv_de_side.setText(result.get(12).toString());
							if (ischeck.equals("1")) {
								layout_canchu_beizhu.setVisibility(View.VISIBLE);
								tv_beizhu.setText("問題描述:"+result.get(13).toString());
							}
						}
						if (report_num.equalsIgnoreCase("FM3NCD034003B")) {
							ed_zone1.setText(result.get(1).toString());
							ed_zone2.setText(result.get(2).toString());
							ed_zone3.setText(result.get(3).toString());
							ed_zone4.setText(result.get(4).toString());
							ed_zone5.setText(result.get(5).toString());
							ed_zone6.setText(result.get(6).toString());
							ed_zone7.setText(result.get(7).toString());
							ed_zone8.setText(result.get(8).toString());
							ed_zone9.setText(result.get(9).toString());
							ed_zone10.setText(result.get(10).toString());
							ed_zone11.setText(result.get(11).toString());
							ed_zone12.setText(result.get(12).toString());
							ed_zone13.setText(result.get(13).toString());
							ed_speed.setText(result.get(14).toString());
							ed_fanspeed.setText(result.get(15).toString());
							ed_shuntvalue.setText(result.get(16).toString());
							tv_last_update.setText(result.get(17).toString());
							tv_update_person.setText(result.get(18).toString());
							tv_de_floor.setText(result.get(19).toString());
							tv_de_line.setText(result.get(20).toString());
							tv_de_jizhong.setText(result.get(21).toString());
							tv_de_side.setText(result.get(22).toString());
							if (ischeck.equals("1")) {
								layout_canchu_beizhu.setVisibility(View.VISIBLE);
								tv_beizhu.setText("問題描述:"+result.get(23).toString());
							}
						}
						if (ischeck_ss.equals("1")||check_st.equals("通過")||check_st.equals("拒簽")||ischeck_ss.equals("0")) {
							bt_detailed_update.setVisibility(View.GONE);
							bt_detailed_delete.setVisibility(View.GONE);
						}else {
							bt_detailed_update.setVisibility(View.VISIBLE);
							bt_detailed_delete.setVisibility(View.VISIBLE);
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無參數");
						if (report_num.equalsIgnoreCase("FM3NCD034003A")) {
							ed_yali.setText("");
							ed_yinshua_sudu.setText("");
							ed_tuomo_sudu.setText("");
							ed_tuomo_jianju.setText("");
							ed_auto_pinlv.setText("");
							ed_guadao_changdu.setText("");
							tv_last_update.setText("");
							tv_update_person.setText("");
						}
						if (report_num.equalsIgnoreCase("FM3NCD034003B")) {
							ed_zone1.setText("");
							ed_zone2.setText("");
							ed_zone3.setText("");
							ed_zone4.setText("");
							ed_zone5.setText("");
							ed_zone6.setText("");
							ed_zone7.setText("");
							ed_zone8.setText("");
							ed_zone9.setText("");
							ed_zone10.setText("");
							ed_zone11.setText("");
							ed_zone12.setText("");
							ed_zone13.setText("");
							ed_speed.setText("");
							ed_fanspeed.setText("");
							ed_shuntvalue.setText("");
							tv_last_update.setText("");
							tv_update_person.setText("");
						}
						bt_detailed_update.setVisibility(View.GONE);
						bt_detailed_delete.setVisibility(View.GONE);
					}
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
				if (key.equalsIgnoreCase(MyConstant.DELETE_CANSHU_INFO)) {

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
				if (key.equalsIgnoreCase(MyConstant.AGREEN_CHECK)) {

					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "同意簽核成功");
						bt_detailed_update.setVisibility(View.GONE);
						bt_detailed_delete.setVisibility(View.GONE);
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "簽核異常");
					}
				}
				if (key.equalsIgnoreCase(MyConstant.Refuse_CHECK)) {

					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "駁回簽核成功");
						bt_detailed_update.setVisibility(View.GONE);
						bt_detailed_delete.setVisibility(View.GONE);
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "簽核異常");
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

	public void add(View v) {
		switch (v.getId()) {
		case R.id.textview_update: // 修改記錄
			intent = new Intent(context, Update_recording.class);
			intent.putExtra("parameternum", parameternum);
			UIHelper.ToastMessage(context, "待開發中...");
			// startActivity(intent);
			break;
		case R.id.imageview_qiehuan: // 切換面別
			if (Side.equalsIgnoreCase("BOT")) {
				Side = "TOP";
				tv_de_side.setText(Side);
				title.setText("參數詳情-" + Side + "面");
				parameternum = floo_name + line_name + jizhong_num + Side;
				httpStart.getServerData(3, MyConstant.GET_CANSHU_INFO,
						report_num, "0", parameternum);
			} else {
				Side = "BOT";
				tv_de_side.setText(Side);
				title.setText("參數詳情-" + Side + "面");
				parameternum = floo_name + line_name + jizhong_num + Side;
				httpStart.getServerData(3, MyConstant.GET_CANSHU_INFO,
						report_num, "0", parameternum);
			}
			break;
		case R.id.bt_detailed_update: // 修改參數
			if (ischeck_ss.equals("2")) {
				if (check_type.equals("0")) {
					UIHelper.ToastMessage(context, "同意添加");
				} else if (check_type.equals("1")) {
					UIHelper.ToastMessage(context, "同意修改");
				} else if (check_type.equals("2")) {
					UIHelper.ToastMessage(context, "同意刪除");
				}
				if (report_num.equalsIgnoreCase("FM3NCD034003A")) {
					httpStart.getServerData(0, MyConstant.AGREEN_CHECK,
							report_num, parameternum, tv_last_update.getText()
									.toString(), userBean.getLogonName());
				} else if (report_num.equalsIgnoreCase("FM3NCD034003B")) {
					httpStart.getServerData(0, MyConstant.AGREEN_CHECK,
							report_num, parameternum, tv_last_update.getText()
									.toString(), userBean.getLogonName());
				}
			} else {
				intent = new Intent(context, Detailed_update_add.class);
				intent.putExtra("parameternum", parameternum);
				intent.putExtra("operating", "2");
				intent.putExtra("report_num", report_num);
				intent.putExtra("report_Name", report_Name);
				intent.putExtra("jizhong_num", jizhong_num);
				intent.putExtra("floo_name", floo_name);
				intent.putExtra("line_name", line_name);
				intent.putExtra("Side", Side);
				intent.putExtra("section", section);
				if (report_num.equalsIgnoreCase("FM3NCD034003A")) {
					intent.putExtra("ed_yali", ed_yali.getText().toString());
					intent.putExtra("ed_yinshua_sudu", ed_yinshua_sudu
							.getText().toString());
					intent.putExtra("ed_tuomo_sudu", ed_tuomo_sudu.getText()
							.toString());
					intent.putExtra("ed_tuomo_jianju", ed_tuomo_jianju
							.getText().toString());
					intent.putExtra("ed_auto_pinlv", ed_auto_pinlv.getText()
							.toString());
					intent.putExtra("ed_guadao_changdu", ed_guadao_changdu
							.getText().toString());
					intent.putExtra("tv_last_update", tv_last_update.getText()
							.toString());
					intent.putExtra("tv_update_person", tv_update_person
							.getText().toString());
				} else if (report_num.equalsIgnoreCase("FM3NCD034003B")) {
					intent.putExtra("ed_zone1", ed_zone1.getText().toString());
					intent.putExtra("ed_zone2", ed_zone2.getText().toString());
					intent.putExtra("ed_zone3", ed_zone3.getText().toString());
					intent.putExtra("ed_zone4", ed_zone4.getText().toString());
					intent.putExtra("ed_zone5", ed_zone5.getText().toString());
					intent.putExtra("ed_zone6", ed_zone6.getText().toString());
					intent.putExtra("ed_zone7", ed_zone7.getText().toString());
					intent.putExtra("ed_zone8", ed_zone8.getText().toString());
					intent.putExtra("ed_zone9", ed_zone9.getText().toString());
					intent.putExtra("ed_zone10", ed_zone10.getText().toString());
					intent.putExtra("ed_zone11", ed_zone11.getText().toString());
					intent.putExtra("ed_zone12", ed_zone12.getText().toString());
					intent.putExtra("ed_zone13", ed_zone13.getText().toString());
					intent.putExtra("ed_speed", ed_speed.getText().toString());
					intent.putExtra("ed_fanspeed", ed_fanspeed.getText()
							.toString());
					intent.putExtra("ed_shuntvalue", ed_shuntvalue.getText()
							.toString());
				}
				startActivity(intent);
			}

			break;
		case R.id.bt_detailed_delete: // 刪除參數
			if (ischeck_ss.equals("2")) {
				if (check_type.equals("0")) {
					UIHelper.ToastMessage(context, "駁回添加");
				} else if (check_type.equals("1")) {
					UIHelper.ToastMessage(context, "駁回修改");
				} else if (check_type.equals("2")) {
					UIHelper.ToastMessage(context, "駁回刪除");
				}
				if (report_num.equalsIgnoreCase("FM3NCD034003A")) {
					httpStart.getServerData(0, MyConstant.Refuse_CHECK,
							report_num, parameternum, tv_last_update.getText()
									.toString(), userBean.getLogonName());
				} else if (report_num.equalsIgnoreCase("FM3NCD034003B")) {
					httpStart.getServerData(0, MyConstant.Refuse_CHECK,
							report_num, parameternum, tv_last_update.getText()
									.toString(), userBean.getLogonName());
				}
			} else {
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.alert_item, null);
				final AlertDialog alertDialog = new AlertDialog.Builder(this,
						AlertDialog.THEME_HOLO_LIGHT).create();
				alertDialog.setView(layout);
				ed_wentimiaoshu = (EditText) layout
						.findViewById(R.id.ed_wentimiaoshu);
				alertDialog.setCanceledOnTouchOutside(true);
				alertDialog.setCancelable(true);
				alertDialog.setIcon(R.drawable.ic_system);
				alertDialog.setTitle("系統提示:");
				alertDialog.setMessage("是否提交至主管審核刪除?");
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								team = new String[1];
								team[0] = userBean.getTeam().toString();
								wentimiaoshu = ed_wentimiaoshu.getText()
										.toString();
								if (wentimiaoshu.length() == 0) {
									wentimiaoshu = "NULL";
								}
								wentimiaoshu = ChangString.change(wentimiaoshu);
								LayoutInflater factory;
								httpStart.getServerData(0,
										MyConstant.GET_SIGNOFF_NAME,
										userBean.getTeam(), userBean.getMFG());
								factory = LayoutInflater.from(context);

								textEntryView = factory.inflate(
										R.layout.adialog_addsignoffuser, null);
								spPartment = (Spinner) textEntryView
										.findViewById(R.id.partment);
								lvPerson = (ListView) textEntryView
										.findViewById(R.id.person);
								adapter1 = new ArrayAdapter(
										context,
										android.R.layout.simple_spinner_dropdown_item,
										team);
								spPartment.setAdapter(adapter1);
								spPartment
										.setOnItemSelectedListener(new OnItemSelectedListener() {
											@Override
											public void onItemSelected(
													AdapterView<?> parent,
													View arg1, int position,
													long arg3) {
												if (!isFrist) {
													isFrist = true;
												} else {
													teamid = parent
															.getItemAtPosition(
																	position)
															.toString();
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
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								alertDialog.dismiss();
								return;
							}
						});
				alertDialog.show();
			}

			break;
		default:
			break;
		}
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

	private void Adapter1() {
		try {
			ArrayAdapter adapter1 = new ArrayAdapter(context,
					android.R.layout.simple_spinner_dropdown_item, team);
			spPartment.setAdapter(adapter1);

		} catch (Exception e) {
			// TODO: handle exception
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
								if (namestr.length() > 0) {
									namestr = "";
								}
								for (int i = 0; i < checkposition.length; i++) {
									if (checkposition[i] != null) {
										for (int j = 0; j < checkposition[i].length; j++) {
											if (checkposition[i][j] != null) {
												Log.i("tag",
														"msg"
																+ ""
																+ checkposition[i][j]
																		.toString());
												// listline.add(checkposition[i][j].toString());
												namestr += checkposition[i][j]
														.toString() + "#";
												continue;
											} else {
												continue;
											}
										}
									}
								}

								if (m == 0) {
									if (namestr.length() != 0) {
										if (report_num
												.equalsIgnoreCase("FM3NCD034003A")) {
											httpStart
													.getServerData(
															3,
															MyConstant.DELETE_CANSHU_INFO,
															report_num,
															parameternum,
															floo_name,
															line_name,
															jizhong_num,
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
															MyConstant.DELETE_CANSHU_INFO,
															report_num,
															parameternum,
															floo_name,
															line_name,
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
															ed_zone10.getText()
																	.toString(),
															ed_zone11.getText()
																	.toString(),
															ed_zone12.getText()
																	.toString(),
															ed_zone13.getText()
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
	protected void findViewById() {
		// TODO Auto-generated method stub
		imageview_qiehuan = (ImageView) findViewById(R.id.imageview_qiehuan);
		bt_detailed_update = (Button) findViewById(R.id.bt_detailed_update);
		bt_detailed_delete = (Button) findViewById(R.id.bt_detailed_delete);
		title = (TextView) findViewById(R.id.bartitle_txt);
		if (ischeck.equals("0")) {
			imageview_qiehuan.setVisibility(View.VISIBLE);
		} else {
			layout_ischeck = (LinearLayout) findViewById(R.id.layout_ischeck);
			layout_ischeck.setVisibility(View.VISIBLE);
			title.setText("參數簽核");
			imageview_qiehuan.setVisibility(View.GONE);
			tv_ischeck = (TextView) findViewById(R.id.tv_ischeck);
			if (ischeck_ss.equals("0")) {
				bt_detailed_update.setVisibility(View.GONE);
				bt_detailed_delete.setVisibility(View.GONE);
				tv_ischeck.setText("待審核中");
			}
			if (ischeck_ss.equals("1")) {
				bt_detailed_update.setVisibility(View.GONE);
				bt_detailed_delete.setVisibility(View.GONE);
				if (check_st.equals("通過")) {
					tv_ischeck.setText("審核通過");
				}
				if (check_st.equals("拒簽")) {
					tv_ischeck.setText("審核駁回");
				}
			}
			if (ischeck_ss.equals("2")) {
				bt_detailed_update.setVisibility(View.GONE);
				bt_detailed_delete.setVisibility(View.GONE);
				tv_ischeck.setText("創建人操作：" + canshu_check);
				if (check_st.equals("待簽核")) {
					bt_detailed_update.setVisibility(View.VISIBLE);
					bt_detailed_delete.setVisibility(View.VISIBLE);
					bt_detailed_update.setText("通過");
					bt_detailed_delete.setText("駁回");
					if (canshu_check.equals("添加")) {
						check_type = "0";
					}
					if (canshu_check.equals("修改")) {
						check_type = "1";
					}
					if (canshu_check.equals("刪除")) {
						check_type = "2";
					}
				}
				if (check_st.equals("通過")) {
					tv_ischeck.setText("審核通過");
				}
				if (check_st.equals("拒簽")) {
					tv_ischeck.setText("審核駁回");
				}
			}
		}
		textview_update = (TextView) findViewById(R.id.textview_update);
		title.setText("參數詳情-" + Side + "面");
		layout_yinshuji = (LinearLayout) findViewById(R.id.layout_yinshuji);
		layout_huihanlu = (LinearLayout) findViewById(R.id.layout_huihanlu);
		layout_canchu_beizhu = (LinearLayout) findViewById(R.id.layout_canchu_beizhu);
		tv_beizhu = (TextView)findViewById(R.id.tv_beizhu);
		tv_de_floor = (TextView) findViewById(R.id.tv_de_floor);
		tv_de_line = (TextView) findViewById(R.id.tv_de_line);
		tv_de_jizhong = (TextView) findViewById(R.id.tv_de_jizhong);
		tv_de_side = (TextView) findViewById(R.id.tv_de_side);
		tv_last_update = (TextView) findViewById(R.id.tv_last_update);
		tv_update_person = (TextView) findViewById(R.id.tv_update_person);
		tv_de_floor.setText(floo_name);
		tv_de_line.setText(line_name);
		tv_de_jizhong.setText(jizhong_num);
		tv_de_side.setText(Side);
		if (report_num.equalsIgnoreCase("FM3NCD034003A")) {
			ed_yali = (TextView) findViewById(R.id.ed_yali);
			ed_yinshua_sudu = (TextView) findViewById(R.id.ed_yinshua_sudu);
			ed_tuomo_sudu = (TextView) findViewById(R.id.ed_tuomo_sudu);
			ed_tuomo_jianju = (TextView) findViewById(R.id.ed_tuomo_jianju);
			ed_auto_pinlv = (TextView) findViewById(R.id.ed_auto_pinlv);
			ed_guadao_changdu = (TextView) findViewById(R.id.ed_guadao_changdu);
			layout_yinshuji.setVisibility(View.VISIBLE); // 顯示印刷機參數

		} else if (report_num.equalsIgnoreCase("FM3NCD034003B")) {
			ed_zone1 = (TextView) findViewById(R.id.ed_zone1);
			ed_zone2 = (TextView) findViewById(R.id.ed_zone2);
			ed_zone3 = (TextView) findViewById(R.id.ed_zone3);
			ed_zone4 = (TextView) findViewById(R.id.ed_zone4);
			ed_zone5 = (TextView) findViewById(R.id.ed_zone5);
			ed_zone6 = (TextView) findViewById(R.id.ed_zone6);
			ed_zone7 = (TextView) findViewById(R.id.ed_zone7);
			ed_zone8 = (TextView) findViewById(R.id.ed_zone8);
			ed_zone9 = (TextView) findViewById(R.id.ed_zone9);
			ed_zone10 = (TextView) findViewById(R.id.ed_zone10);
			ed_zone11 = (TextView) findViewById(R.id.ed_zone11);
			ed_zone12 = (TextView) findViewById(R.id.ed_zone12);
			ed_zone13 = (TextView) findViewById(R.id.ed_zone13);
			ed_speed = (TextView) findViewById(R.id.ed_speed);
			ed_fanspeed = (TextView) findViewById(R.id.ed_fanspeed);
			ed_shuntvalue = (TextView) findViewById(R.id.ed_shuntvalue);
			layout_huihanlu.setVisibility(View.VISIBLE); // 顯示回焊爐參數
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

}
