package com.zsm.foxconn.mypaperless;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.qr.CaptureActivity;

public class TorqueDriverCalibrationActivity extends BaseActivity implements
		OnClickListener {

	private Context context = TorqueDriverCalibrationActivity.this;
	private TextView average, shiftTV, RNOTV, customerTV, modelTV;
	private EditText torque1, torque2, torque3, checkPersonET;
	private Button torqueCalculation, selectBtn, submitBtn;
	private AutoCompleteTextView autoTV;
	private String torqueAverage, shift, RNO, mfg, reportName, reportNum,
			minTorque, maxTorque;
	private UserBean user;
	private List<String> CompleteQZ = new ArrayList<String>();// 已經點檢過的起子
	HttpStart start;
	private ImageButton cameraIB;
	private int submitLzfsBS = -1;
	private static final int IMAGE_HALFWIDTH = 20;
	int[] pixels = new int[2 * IMAGE_HALFWIDTH * 2 * IMAGE_HALFWIDTH];
	private final int SCANER_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_torque_diver_calibration);
		user = (UserBean) getApplicationContext();
		CheckLogin();
		Intent intent = getIntent();
		reportName = intent.getStringExtra("reportName");
		findViewById();
		setListener();
		start = new HttpStart(context, handler);
		start.getServerData(3, MyConstant.GET_SHIFT);
		mfg = user.getMFG();

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
		autoTV = (AutoCompleteTextView) findViewById(R.id.auto);
		selectBtn = (Button) findViewById(R.id.select);
		checkPersonET = (EditText) findViewById(R.id.et_check_person);
		submitBtn = (Button) findViewById(R.id.btn_submit);
		cameraIB = (ImageButton) findViewById(R.id.btn_camera);
		customerTV = (TextView) findViewById(R.id.tv_customer);
		modelTV = (TextView) findViewById(R.id.tv_model);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(new Date()).toString();
		String time = "";
		for (int i = 0; i < date.length(); i++) {
			char h = date.charAt(i);
			if (h != '-') {
				time += h;
			}
		}

		RNO = time + "ELEC" + mfg + shift;
		RNOTV.setText(RNO);
		shiftTV.setText(shift);

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
			if (customerTV.getText().toString().equals("")) {
				UIHelper.alertDialogEng(context,
						"Please click on the OK button");
				average.setText("");
			} else {
				start.getServerData(0, MyConstant.GET_MAXMINTORSION, autoTV
						.getText().toString());
			}
			break;
		case R.id.btn_camera:
			// 打开扫描界面扫描条形码或二维码
			Intent openCameraIntent = new Intent(context, CaptureActivity.class);
			startActivityForResult(openCameraIntent, SCANER_CODE);
			break;
		case R.id.select:
			String text1 = autoTV.getText().toString();
			start.getServerData(0, MyConstant.GET_MODEL, text1);
			if (submitLzfsBS == -1) {
				submitLzfsBS = 1;
				selectBtn.setText("Cancel");
				autoTV.setEnabled(false);
			} else {
				submitLzfsBS = -1;
				selectBtn.setText("OK");
				autoTV.setEnabled(true);
			}
			break;
		case R.id.btn_submit:
			try {

				String text = "Are you sure want to submit？";
				String ave = average.getText().toString();
				if ((Float.parseFloat(ave) > Float.parseFloat(minTorque))
						&& (Float.parseFloat(ave) < Float.parseFloat(maxTorque))) {
					text = "The Torque Driver is OK.\n Are you sure want to submit？";
				} else {
					text = "The Torque Driver is fail.\n Are you sure want to submit？";
				}
				AlertDialog alert = new AlertDialog.Builder(context).create();
				alert.setIcon(R.drawable.img8);
				alert.setTitle("System info：");
				alert.setMessage(text);

				alert.setButton(DialogInterface.BUTTON_NEGATIVE, "YES",
						new DialogInterface.OnClickListener() {

							@SuppressWarnings("unchecked")
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (checkInputOk()) {
									if (submitLzfsBS == 1) {
										start.getServerData(3,
												MyConstant.GET_CHECKTOOLNO,
												reportNum, RNO);
									} else {
										UIHelper.alertDialogEng(context,
												"Please click on the OK button");
									}

								}
							}
						});
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Toast.makeText(context, "", Toast.LENGTH_LONG)
										.show();
							}
						});
				alert.show();
			} catch (Exception e) {
				UIHelper.alertDialogEng(context,
						"Please fill in the complete content.");
			}
			break;
		}
	}

	/**
	 * 判斷當前點檢編號是否點檢過
	 */
	private void checkLINE() {
		// TODO Auto-generated method stub
		if (CompleteQZ.indexOf(autoTV.getText().toString()) == -1) {
			start.getServerData(0, MyConstant.SAVE_TOOLCONTENT, RNO, autoTV.getText().toString(),
					reportNum);
		} else {
			UIHelper.alertDialogEng(context, "The number has been checking");
		}

	}

	String Check_Result = "";

	private boolean checkInputOk() {
		if (Check_Result.length() > 0) {
			Check_Result = "";
		}
		if (checkPersonET.getText().toString().length() == 0
				|| average.getText().toString().length() == 0) {
			UIHelper.alertDialogEng(context,
					"Please fill in the complete content.");
			return false;
		} else {
			int flag = 0;
			String str = 1 + "";
			Check_Result += RNO + "~" + flag + "~" + reportNum + "~" + str
					+ "~" + average.getText().toString() + "~" + str + "~"
					+ checkPersonET.getText().toString() + "~" + autoTV.getText().toString() + "~a66abb5684c45962d887564f08346e8d";

			System.out.println(Check_Result);
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
				start.getServerData(0, MyConstant.GET_MODEL, autoTV.getText()
						.toString());
			}
		}
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
					start.getServerData(0, MyConstant.GET_REPORTNUM, reportName);
				} else if (key.equals(MyConstant.GET_REPORTNUM)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						reportNum = result.get(1).toString();
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "网络异常....", 0).show();
					}
				} else if (key.equals(MyConstant.GET_MODEL)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						customerTV.setText(result.get(1).toString());
						modelTV.setText(result.get(2).toString());
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						UIHelper.alertDialogEng(context,
								"Input is incorrect, Please re-enter.");
					}
				} else if (key.equals(MyConstant.GET_CHECKTOOLNO)) {
					result = new ArrayList<String>();
					if (CompleteQZ.size() > 0) {
						CompleteQZ.clear();
					}
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i++) {
							CompleteQZ.add(result.get(i).toString());
						}
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "网络异常....", 0).show();
					}
					checkLINE();
				} else if (key.equals(MyConstant.GET_MAXMINTORSION)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						minTorque = result.get(1).toString();
						maxTorque = result.get(2).toString();
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "网络异常....", 0).show();
					}
				} else if (key.equals(MyConstant.SAVE_TOOLCONTENT)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						start.getServerData(0, MyConstant.SAVE_CHECKREPORT,
								RNO, 0 + "", reportNum, mfg, customerTV
										.getText().toString(), modelTV
										.getText().toString(), shift, user
										.getLogonName());
					}
				} else if (key.equals(MyConstant.SAVE_CHECKREPORT)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						String str = ChangString.change(Check_Result);
						start.getServerData(0, MyConstant.SAVE_CHECKRESULT,
								str);

					}
				} else if (key.equals(MyConstant.SAVE_CHECKRESULT)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {

						UIHelper.alertDialogEng(context, "SUCCESS!");
					} else {
						UIHelper.alertDialogEng(context, "FAILED!");
					}
				}
			}
		}
	};

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
			average.setText(torqueAverage + "0");
		} catch (Exception e) {
			e.printStackTrace();
			average.setText("");
			UIHelper.alertDialogEng(context,
					"The torque value cannot be null and must be numeric.");
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
