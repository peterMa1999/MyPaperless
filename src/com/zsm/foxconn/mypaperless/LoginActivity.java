package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.circularprogressbutton.CircularProgressButton;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.help.ImageShower;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.ConnSkyWebService;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.http.HttpThread;
import com.zsm.foxconn.mypaperless.service.TimeServer;
import com.zsm.foxconn.mypaperless.util.Loading_dialogo;
import com.zsm.foxconn.mypaperless.util.PhoneInfo;
import com.zsm.foxconn.mypaperless.util.RemberCode;
import com.zsm.foxconn.mypaperless.util.UpdateManger;

public class LoginActivity extends Activity {

	Context context = LoginActivity.this;
	private ImageView mypaperlessView, detele_account, detele_pwd;
	private AutoCompleteTextView account_ed;
	private EditText pwd_ed;
	private TextView forgetpwd;
	private CircularProgressButton login;
	private CheckBox box;
	HttpThread http = null;
	private String name = null;
	private LinearLayout linearLayout;
	private String pwd = null;
	HttpStart start;
	UserBean userBean;
	RemberCode code = new RemberCode();
	Loading_dialogo dialogo = null;
	private long ExitTime;
	private String str[] = null;
	private Button restart_upload, saoma_download, btn_yuzhanghao;
	private Intent intent;
	private String userstr = "";
	ConnSkyWebService service = new ConnSkyWebService();

	private ProgressDialog progressDialog;
	private MyTask myTask;
	int isOK = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN); //全屏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		setContentView(R.layout.activity_login);
		AppManager.getInstance().addActivity(LoginActivity.this);
		userBean = (UserBean) getApplicationContext();
		start = new HttpStart(context, handler);
		start.getServerDataUpdate(0, MyConstant.GET_CHECK_VERSION,
				MyConstant.GET_localAPK);
		init();
		login.setOnClickListener(new OnClickListener() // 登陆
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = account_ed.getText().toString().trim();
				pwd = pwd_ed.getText().toString().trim();
				if (name.length() == 0 || pwd.length() == 0) {
					Toast.makeText(context, "输入的密码或用户名为空", 0).show();
				} else {
					start.getServerData(0, MyConstant.GET_LOGIN, name, pwd,
							"no_yu_login");
				}
			}
		});
		restart_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UpdateManger update = new UpdateManger(context,
						MyConstant.GET_localUrl);
				update.showDownloadDialog();
				PhoneInfo phoneinfo = new PhoneInfo();
				TelephonyManager tm = phoneinfo.getTelephonyManager(context);
				//唯一的设备ID
				String deviceid = tm.getDeviceId();
				//手機運營商
				String simoperatorname = ChangString.change(tm.getSimOperatorName());
				//设备厂商
				String phonebrand = phoneinfo.getPhoneBrand();
				//设备名称
				String phonemodel = phoneinfo.getPhoneModel();
				int appvercode = phoneinfo.getVerCode(context);
				int[] metrics = phoneinfo.getMetrics(context);
				String metric = metrics[0]+"*"+metrics[1];
				if (simoperatorname.length()==0) {
					simoperatorname = "NA";
				}
				start.getServerData(0, MyConstant.SAVE_DOWNLOAD_PHONE,deviceid,phonebrand,phonemodel,appvercode+"",metric,simoperatorname);
			}
		});
		saoma_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(context, ImageShower.class));
			}
		});
		btn_yuzhanghao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = account_ed.getText().toString().trim();
				pwd = pwd_ed.getText().toString().trim();
				if (name.length() == 0 || pwd.length() == 0) {
					Toast.makeText(context, "输入的密码或用户名为空", 0).show();
				} else {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				}
			}
		});
		detele_account.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				account_ed.setText("");
			}
		});
		detele_pwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pwd_ed.setText("");
			}
		});
		// 忘记密码
		forgetpwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context,Forgetpwd.class);
				startActivity(intent);
			}
		});
	}

	private void init() {
		forgetpwd = (TextView) findViewById(R.id.forgetpwd);
		linearLayout = (LinearLayout) findViewById(R.id.login_put_layout);
		account_ed = (AutoCompleteTextView) findViewById(R.id.account);
		pwd_ed = (EditText) findViewById(R.id.password);
		login = (CircularProgressButton) findViewById(R.id.login);
		mypaperlessView = (ImageView) findViewById(R.id.mypaperless_bar);
		detele_account = (ImageView) findViewById(R.id.detele_account);
		detele_pwd = (ImageView) findViewById(R.id.detele_pwd);
		restart_upload = (Button) findViewById(R.id.restart_upload);
		saoma_download = (Button) findViewById(R.id.saoma_download);
		btn_yuzhanghao = (Button) findViewById(R.id.btn_yuzhanghao);
		account_ed.addTextChangedListener(textWatcher);
		pwd_ed.addTextChangedListener(textWatcher2);
		final Animation mypaperview = AnimationUtils.loadAnimation(context,
				R.anim.register_bt_anima);
		mypaperlessView.startAnimation(mypaperview);
		final Animation aphlAnimation = AnimationUtils.loadAnimation(context,
				R.anim.login_show);
		linearLayout.startAnimation(aphlAnimation);
		final Animation loginAnimation = AnimationUtils.loadAnimation(context,
				R.anim.login_bt_anima);
		login.startAnimation(loginAnimation);
		str = code.getusername(context);
		if (str.length != 0) {
			ArrayAdapter<String> account_ed_adapter = new ArrayAdapter<String>(
					context, android.R.layout.simple_dropdown_item_1line, str);
			account_ed.setAdapter(account_ed_adapter);
		}
		box = (CheckBox) findViewById(R.id.remember);
		show_pwd();
	}

	private TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			String acount = account_ed.getText().toString();
			if (acount.length() > 0) {
				detele_account.setVisibility(View.VISIBLE);
			} else {
				detele_account.setVisibility(View.INVISIBLE);
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
	};
	private TextWatcher textWatcher2 = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			String pwd = pwd_ed.getText().toString();
			if (pwd.length() > 0) {
				detele_pwd.setVisibility(View.VISIBLE);
			} else {
				detele_pwd.setVisibility(View.INVISIBLE);
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
	};

	/**
	 * 版本更新
	 * 
	 * @param result
	 */
	public void CheckVersion(List<String> result) {
		for (int i = 0; i < result.size(); i++) {
			Log.i(">>>", "get version error" + result.get(i).toString());
		}
		try {
			PackageInfo packageInfo = getApplicationContext()
					.getPackageManager().getPackageInfo(getPackageName(), 0);
			MyConstant.GET_localVersion = packageInfo.versionCode;

			MyConstant.GET_serverVersion = Integer.valueOf(result.get(2)
					.toString());
			MyConstant.GET_localUrl = result.get(8).toString();
			int serverCode = MyConstant.GET_serverVersion;
			Log.i(">>>", "The current version:" + MyConstant.GET_localVersion
					+ "now" + MyConstant.GET_serverVersion);

			if (MyConstant.GET_localVersion < MyConstant.GET_serverVersion) {
				UpdateManger update = new UpdateManger(context,
						MyConstant.GET_localUrl);
				update.checkUpdateInfo(result.get(3).toString());
			} else {
				// UIHelper.ToastMessage(context, "已是最新版本");
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.i(">>>", "get version error");
			e.printStackTrace();
		}
	}

	private void simulateSuccessProgress(final CircularProgressButton button) {
		ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
		widthAnimation.setDuration(600);
		widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		widthAnimation
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						Integer value = (Integer) animation.getAnimatedValue();
						button.setProgress(value);
					}
				});
		widthAnimation.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 2) {
				myTask = new MyTask();
				myTask.execute();
			}
			ArrayList<String> result = null;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) // 键值对
			{

				if (key.equals(MyConstant.GET_CHECK_VERSION)) // 更新
				{
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						CheckVersion(result);
					} else {
						Log.i(">>>", "get version error--------------->");
					}
				}
				if (key.equals(MyConstant.GET_LOGIN)) // 判断登入
				{
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						if (isOK == 2) {
							progressDialog.dismiss();
						}
						simulateSuccessProgress(login);
						login.setClickable(false);
						login.setEnabled(false);
						RemberPwd();
						code.saveusername(context, name);
						userBean.insert(result.get(1).toString().trim(), result
								.get(2).toString().trim(), result.get(3)
								.toString().trim(), result.get(4).toString()
								.trim(), result.get(5).toString().trim(),
								result.get(6).toString().trim(), result.get(7)
										.toString().trim(), result.get(8)
										.toString().trim(), result.get(9)
										.toString().trim(), result.get(10)
										.toString().trim(), result.get(11)
										.toString().trim(), result.get(12)
										.toString().trim(), result.get(13)
										.toString().trim(), result.get(14)
										.toString().trim(), result.get(15)
										.toString().trim(), result.get(16)
										.toString().trim());
						intent = new Intent(context, TimeServer.class);
						intent.putExtra("userBean", userBean);
						startService(intent);
						// new Handler().postDelayed(new Runnable() {
						// @Override
						// public void run() {
						intent = new Intent(context, MainActivity.class);
						startActivity(intent);
						service.getInfo(
								"AppUserCount",
								userBean.getLogonName(),
								MyConstant.APP_NAME,
								"login",
								UIHelper.getLocalMacAddressFromWifiInfo(context));
						// }
						//
						// }, 1000);

					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						if (isOK == 2) {
							progressDialog.dismiss();
						}
						Toast.makeText(context, "您好，此用户不存在...", 0).show();
						login.invalidate();
					}
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_PASSWORD_EXCEPTION)) {
						if (isOK == 2) {
							progressDialog.dismiss();
						}
						login.invalidate();
						AlertDialog alertDialog = new AlertDialog.Builder(
								context).create();
						alertDialog.setIcon(R.drawable.ic_system);
						alertDialog.setTitle("系統提示:");
						alertDialog
								.setMessage("密碼規則：長度為8-16位，必須含有大小寫字母、特殊字符、數字");
						alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
								"去修改>>>",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										intent = new Intent(context,Person_Modify_password.class);
										intent.putExtra("usernum", name);
										startActivity(intent);
									}
								});
						alertDialog.show();
					}

					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_FALSE)) {
						if (isOK == 2) {
							progressDialog.dismiss();
						}
						final Animation trAnimation = AnimationUtils
								.loadAnimation(context, R.anim.login_pwd_false);
						pwd_ed.startAnimation(trAnimation);
						login.invalidate();
					}
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						if (isOK == 2) {
							progressDialog.dismiss();
						}
						Toast.makeText(context, "网络异常....", 0).show();
						login.invalidate();
					}
				}

			}

		}
	};

	public class MyTask extends AsyncTask<String, Integer, String> {

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle("登錄中...");
			progressDialog.setCancelable(false);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				userstr = service.SingelConnection(name, pwd).toUpperCase();
				Log.i("result>>>", userstr);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (userstr.equals(name)) {
				start.getServerData(0, MyConstant.GET_LOGIN, name, pwd,
						"yu_login");
				isOK = 2;
			} else {
				isOK = 1;
			}
			return null;
		}

		protected void onPostExecute(String result) {
			if (isOK == 1) {
				UIHelper.ToastMessage(context, "域登錄賬戶或者密碼錯誤");
				progressDialog.dismiss();
			}
		}

	}

	protected void show_pwd() // 提取密码
	{
		Map<String, String> user = code.getUserInfo(context);
		if (user != null) {
			account_ed.setText(user.get("number"));
			pwd_ed.setText(user.get("password"));
		}

	}

	// 记住密码
	public void RemberPwd() {
		if (box.isChecked()) {
			code.saveUserInfor(context, name, pwd);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - ExitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出",
						Toast.LENGTH_SHORT).show();
				ExitTime = System.currentTimeMillis();
			} else {
				AppManager.getInstance().AppExit(context);
				stopService(new Intent(context, TimeServer.class));
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
