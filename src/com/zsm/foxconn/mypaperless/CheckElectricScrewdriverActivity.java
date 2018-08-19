package com.zsm.foxconn.mypaperless;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.adapter.CheckeAdapter2;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.CheckHolder;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.qr.CaptureActivity;

public class CheckElectricScrewdriverActivity extends BaseActivity implements
		OnClickListener {

	private Context context = CheckElectricScrewdriverActivity.this;
	private TextView average, shiftTV, RNOTV, codeTV;
	private EditText torque1, torque2, torque3, resistanceET, checkPersonET;
	private Button torqueCalculation, selectBtn, submitBtn;
	private Spinner lineSP, voltageSP;
	private AutoCompleteTextView autoTV;
	private String torqueAverage, shift, RNO, mfg, reportName, reportNum,
			qzcode, mline;
	private RadioGroup radio;
	private UserBean user;
	private List<String> AllLine = new ArrayList<String>();
	private List<String> ToolNos = new ArrayList<String>();
	private List<String> CompleteQZ = new ArrayList<String>();// 已經點檢過的起子
	HttpStart start;
	private int submitLzfsBS = -1;
	private ImageButton cameraIB;
	private final int SCANER_CODE = 1;
	private CheckeAdapter2 adapterPerson = null;
	List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
	private LinearLayout layout_check_aoi;
	private CheckHolder holder = new CheckHolder();
	List<String> liststr2 = new ArrayList<String>();
	List<String> list3 = new ArrayList<String>();
	String personNameString = null;
	int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_check_electric_screwdriver);
		user = (UserBean) getApplicationContext();
		CheckLogin();
		Intent intent = getIntent();
		reportName = intent.getStringExtra("reportName");
		findViewById();
		setListener();
		start = new HttpStart(context, handler);
		start.getServerData(3, MyConstant.GET_SHIFT);
		mfg = user.getMFG();

		radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton r = (RadioButton) findViewById(checkedId);
				r.getText();
			}
		});

		lineSP.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				mline = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		checkPersonET.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					checkPersonET
							.setText(user.getLogonName().toString().trim());
				}
			}
		});
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		TextView textView = (TextView) findViewById(R.id.bartitle_txt);
		textView.setText(reportName);
		torque1 = (EditText) findViewById(R.id.torque1);
		torque2 = (EditText) findViewById(R.id.torque2);
		torque3 = (EditText) findViewById(R.id.torque3);
		average = (TextView) findViewById(R.id.torque_sum);
		shiftTV = (TextView) findViewById(R.id.tv_shift);
		RNOTV = (TextView) findViewById(R.id.tv_RNO);
		torqueCalculation = (Button) findViewById(R.id.torque_calculation);
		lineSP = (Spinner) findViewById(R.id.sp_line);
		autoTV = (AutoCompleteTextView) findViewById(R.id.auto);
		selectBtn = (Button) findViewById(R.id.select);
		codeTV = (TextView) findViewById(R.id.tv_code);
		resistanceET = (EditText) findViewById(R.id.et_resistance);
		checkPersonET = (EditText) findViewById(R.id.et_check_person);
		submitBtn = (Button) findViewById(R.id.btn_submit);
		voltageSP = (Spinner) findViewById(R.id.sp_voltage);
		radio = (RadioGroup) findViewById(R.id.group);
		cameraIB = (ImageButton) findViewById(R.id.btn_camera);
		layout_check_aoi = (LinearLayout) findViewById(R.id.activity_check_electric_screwdriver);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		SimpleDateFormat dat = new SimpleDateFormat("yyyyMMddHHmmss");
		String iDate = dat.format(new Date()).toString();
		String time = iDate.substring(0, 12);

		RNO = time + "ELEC" + mfg + shift;
		RNOTV.setText(RNO);
		shiftTV.setText(shift);
		start.getServerData(3, MyConstant.GET_NOTSMTLINE, mfg);

	}

	protected void setListener() {
		// TODO Auto-generated method stub
		torqueCalculation.setOnClickListener(this);
		selectBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		cameraIB.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.torque_calculation:
			calculatedTorque();
			break;
		case R.id.btn_camera:
			// 打开扫描界面扫描条形码或二维码
			Intent openCameraIntent = new Intent(context, CaptureActivity.class);
			startActivityForResult(openCameraIntent, SCANER_CODE);
			break;
		case R.id.select:
			if (submitLzfsBS == -1) {
				String text1 = autoTV.getText().toString();
				qzcode = text1;
				searchQZ(0, ToolNos.size() - 1, ToolNos);
				if (bl) {
					codeTV.setText(text1);
					submitLzfsBS = 1;
					selectBtn.setText("取消");
					autoTV.setEnabled(false);
				} else {
					UIHelper.alertDialog(context, "起子編號錯誤");
				}

			} else {
				submitLzfsBS = -1;
				selectBtn.setText("确定");
				autoTV.setEnabled(true);
			}
			break;
		case R.id.btn_submit:
			AlertDialog alert = new AlertDialog.Builder(context).create();
			alert.setIcon(R.drawable.img8);
			alert.setTitle("系統提示：");
			alert.setMessage("提交內容前,是否已經交由當前線長審核過？");

			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "是的,已審核,繼續提交！",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							if (checkInputOk()) {
								if (mline.length() == 0) {
									UIHelper.alertDialog(context, "線別不能為空");
								} else {
									if (submitLzfsBS == 1) {
										start.getServerData(3,
												MyConstant.GET_CHECKTOOLNO,
												reportNum, RNO);
									} else {
										UIHelper.alertDialog(context,
												"请点击确定按钮!");
									}
								}
							}
						}
					});
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "否，立即交由當前線長審核！",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Toast.makeText(context, "立即交由當前線長審核",
									Toast.LENGTH_LONG).show();
						}
					});
			alert.show();
			break;
		}
	}

	/**
	 * 判斷當前點檢編號是否點檢過
	 */
	private void checkLINE() {
		// TODO Auto-generated method stub
		if (CompleteQZ.indexOf(autoTV.getText().toString()) == -1) {
			int u = 0;
			try {
				Float.valueOf(average.getText().toString());
				if (Integer.valueOf(resistanceET.getText().toString()) >= 200) {
					u = -1;
					UIHelper.alertDialog(context, "接地電阻應小於200");
				}
			} catch (Exception e) {
				UIHelper.alertDialog(context, "請輸入正確數字");
				u = -1;
			}
			if (u == 0) {
				if (i == 0) {
					popsindows();
				} else {
					UIHelper.alertDialog(context, "已經點檢過了...");
					return;
				}
			}
		} else {
			UIHelper.alertDialog(context, "该起子已经点检过");
		}

	}

	String Check_Result = "";

	private boolean checkInputOk() {
		if (Check_Result.length() > 0) {
			Check_Result = "";
		}
		List list = new ArrayList();
		list.add(average.getText().toString());
		list.add(voltageSP.getSelectedItem());
		list.add(resistanceET.getText().toString());
		int result = -1; // 選擇框
		for (int i = 0; i < radio.getChildCount(); i++) {
			RadioButton r = (RadioButton) radio.getChildAt(i);
			if (r.isChecked()) {
				result = i;
				break;
			}
		}
		String text1 = autoTV.getText().toString();
		String connPerson = checkPersonET.getText().toString();// 聯繫人
		if (list.size() == 3 && result != -1 && connPerson != null
				&& connPerson.length() > 0 && qzcode != null
				&& qzcode.length() > 0 && bl) {
			for (int i = 0; i < list.size(); i++) {
				int flag = 0;
				Check_Result += RNO + "~" + flag + "~" + reportNum + "~"
						+ ((i + 1) + "") + "~" + list.get(i).toString() + "~"
						+ (result + "") + "~" + connPerson + "~" + text1 + "~a66abb5684c45962d887564f08346e8d";

			}
		} else {
			UIHelper.alertDialog(context, "请填写完整内容");
			return false;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			if (requestCode == SCANER_CODE) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				autoTV.setText(scanResult);
			}
		}
	}

	public void Adapter() {
		ArrayAdapter adapter1 = new ArrayAdapter(this,
				android.R.layout.simple_dropdown_item_1line, AllLine);
		lineSP.setAdapter(adapter1);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> result = null;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) // 键值对
			{
				if (key.equals(MyConstant.GET_SHIFT)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						shift = result.get(1).toString();
						Log.i("shift", ">>>>>>>>>>>" + shift);
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "网络异常....", 0).show();
					}
					initView();

				} else if (key.equals(MyConstant.GET_NOTSMTLINE)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						AllLine.add("");
						for (int i = 1; i < result.size(); i++) {
							AllLine.add(result.get(i).toString());
						}
						Adapter();
						start.getServerData(3, MyConstant.GET_REPORTNUM,
								reportName);
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "网络异常....", 0).show();
					}
				} else if (key.equals(MyConstant.GET_REPORTNUM)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						reportNum = result.get(1).toString();
						start.getServerData(3, MyConstant.GET_REPORTTOOLNO,
								mfg, reportNum);
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "网络异常....", 0).show();
					}
				} else if (key.equals(MyConstant.GET_REPORTTOOLNO)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						ToolNos = result;
						String Tool = "";
						for (int i = 0; i < result.get(1).length(); i++) {
							char T = result.get(1).charAt(i);
							if (T != '-') {
								Tool += T;
							} else {
								break;
							}
						}
						autoTV.setText(Tool);
						ArrayAdapter<String> ad = new ArrayAdapter<String>(
								context,
								android.R.layout.simple_dropdown_item_1line,
								result);
						autoTV.setAdapter(ad);
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "网络异常....", 0).show();
					}
				} else if (key.equals(MyConstant.GET_CHECKTOOLNO)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						CompleteQZ = result;
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "网络异常....", 0).show();
					}
					checkLINE();
				} else if (key.equals(MyConstant.SAVE_TOOLCONTENT)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						start.getServerData(0, MyConstant.SAVE_CHECKREPORT,
								RNO, 0 + "", reportNum, mfg, "N/A", mline,
								shift, user.getLogonName());
					}
				} else if (key.equals(MyConstant.SAVE_CHECK_PERSON)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						start.getServerData(0, MyConstant.SAVE_TOOLCONTENT,
								RNO, autoTV.getText().toString(), reportNum);
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "提交審核人失敗", 0).show();
					}
				} else if (key.equals(MyConstant.SAVE_CHECKREPORT)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						String str = ChangString.change(Check_Result);
						start.getServerData(0, MyConstant.SAVE_CHECKRESULT, str);

					}
				} else if (key.equals(MyConstant.SAVE_CHECKRESULT)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					String totalDate = "";
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						String a[] = { RNO, "NULL", "NULL", "0", "NULL",
								"NULL", "0", "NULL", "NULL", "0",
								user.getLogonName() };
						for (int i = 0; i < a.length; i++) {
							totalDate += a[i] + MyConstant.GET_FLAG3;
						}
						start.getServerData(0, MyConstant.SAVE_CHECK_BASE_INFO,
								totalDate);
					} else {
						UIHelper.alertDialog(context, "点检失败");
					}
				} else if (key.equals(MyConstant.SAVE_CHECK_BASE_INFO)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						// Toast.makeText(context, "提交PD点检基本信息成功", 0).show();
						UIHelper.alertDialog(context, "點檢成功");
						i = 1;
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "提交PD点检基本信息失败", 0).show();
					}
				} else if (key.equals(MyConstant.GET_PD_CHECK_NAME)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						if (list2.size() > 0) {
							list2.clear();
						}
						if (list3.size() > 0) {
							list3.clear();
						}
						for (int i = 1; i < result.size(); i++) {
							list3.add(result.get(i));
						}
						for (int i = 0; i < list3.size(); i += 2) {
							Map<String, String> presonHashMap = new HashMap<String, String>();
							presonHashMap.put("LogonName", list3.get(i));
							presonHashMap.put("ChineseName", list3.get(i + 1));
							list2.add(presonHashMap);
						}
						adapterPerson.notifyDataSetChanged();
						adapterPerson.initDate();
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "获取审核人失败...", 0).show();
					}
				}
			}
		}
	};

	boolean bl = false;

	public void searchQZ(int x, int y, List<String> ToolNos) {// x,y標記位置
		if (bl == false) {
			if (y - x != 1 && y - x != 2) {
				searchQZ(x, (y - x) / 2 + x, ToolNos);
				searchQZ((y - x) / 2 + x + 1, y, ToolNos);
			}
			if (y - x == 1
					&& (ToolNos.get(x).equals(qzcode) || ToolNos.get(y).equals(
							qzcode))) {
				bl = true;
			}
			if (y - x == 2
					&& (ToolNos.get(x).equals(qzcode)
							|| ToolNos.get(x + 1).equals(qzcode) || ToolNos
							.get(y).equals(qzcode))) {
				bl = true;
				return;
			}
		}
		return;
	}

	// 计算扭力
	private void calculatedTorque() {
		try {
			float js = ((Float) (Float.valueOf(torque1.getText().toString())
					+ Float.valueOf(torque2.getText().toString()) + Float
					.valueOf(torque3.getText().toString())) / 3);
			torqueAverage = String.valueOf(js);

			if (torqueAverage.length() > 4) {
				torqueAverage = torqueAverage.substring(0, 4);
			} else {
				torqueAverage = torqueAverage.substring(0, 3);
			}
			average.setText(torqueAverage);
		} catch (Exception e) {
			e.printStackTrace();
			average.setText("");
			UIHelper.alertDialog(context, "扭力值不能为空且必须为数字");
		}
	}

	// 选择点检人;
	private void popsindows() {

		View view = LayoutInflater.from(context).inflate(
				R.layout.activity_check_pd_popwindow, null);
		ImageButton imageButton_add = (ImageButton) view
				.findViewById(R.id.imageButton_add);
		ImageButton imageButton1_detele = (ImageButton) findViewById(R.id.imageButton1_detele);
		ImageButton OK = (ImageButton) view.findViewById(R.id.OK);
		ImageButton closePopwindow = (ImageButton) view
				.findViewById(R.id.closePopwindow);
		ListView personListView = (ListView) view
				.findViewById(R.id.listViewperson);
		Spinner spinner_check_name = (Spinner) view
				.findViewById(R.id.spinner_check_name);
		spinner_check_name
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String PDteam = arg0.getItemAtPosition(arg2).toString();
						start.getServerData(0, MyConstant.GET_PD_CHECK_NAME,
								PDteam, user.getMFG());
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		adapterPerson = new CheckeAdapter2(list2, context);
		personListView.setAdapter(adapterPerson);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		final PopupWindow window = new PopupWindow(view,
				(display.getWidth() - (display.getWidth() / 15)),
				(display.getHeight() - (display.getHeight() / 5)));
		window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		window.setFocusable(true);
		backgroundAlpha(0.3f);
		window.setAnimationStyle(R.style.POP_Animation_trans);
		window.showAtLocation(layout_check_aoi, Gravity.CENTER, 0, 0);
		personListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				holder = (CheckHolder) arg1.getTag();
				holder.flagCheckBox.toggle();
				if (holder.flagCheckBox.isChecked()) {
					adapterPerson.getIsSelected().put(arg2,
							holder.flagCheckBox.isChecked());
					liststr2.add(list2.get(arg2).get("LogonName").toString());
				} else {
					adapterPerson.getIsSelected().remove(arg2);
					liststr2.remove(list2.get(arg2).get("LogonName").toString());
				}

			}
		});
		closePopwindow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(context)
						.setIcon(R.drawable.nt_warn)
						.setTitle("确认关闭?")
						.setPositiveButton(
								getResources().getString(R.string.back_yes),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										window.setOnDismissListener(new poponDismissListener());
										window.dismiss();
									}
								})
						.setNeutralButton(
								getResources().getString(R.string.back_no),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		});

		OK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String allLines = "";
				if (liststr2.size() > 0) {

					for (int i = 0; i < liststr2.size(); i++) {
						allLines += liststr2.get(i).toString().trim()
								+ MyConstant.GET_FLAG2;
					}
					personNameString = allLines;

					if (i == 0) {

						start.getServerData(3, MyConstant.SAVE_CHECK_PERSON,
								RNO, personNameString, user.getLogonName()
										.toString().trim());
					} else {
						UIHelper.alertDialog(context, "已經點檢過了...");
					}

				} else {
					personNameString = allLines;
					Toast.makeText(context, "您还未选择审核人，请选择...", 1000).show();
				}
			}
		});
	}

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha;
		getWindow().setAttributes(lp);
	}

	class poponDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
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

	// 返回键按钮
	public void activity_back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}

	// 回退
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
