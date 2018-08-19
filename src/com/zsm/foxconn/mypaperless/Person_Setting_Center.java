package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.service.TimeServer;
import com.zsm.foxconn.mypaperless.util.RemberCode;
import com.zsm.foxconn.mypaperless.util.UpdateManger;

public class Person_Setting_Center extends BaseActivity implements OnClickListener{

	Context context = Person_Setting_Center.this;
	Intent intent;
	private TextView version_tv;
	private LinearLayout password, delete, upgrade_version,shuoming_version,lianxi_women;
	private HttpStart start;
	PackageInfo packageInfo;
	RemberCode code = new RemberCode();
	private UserBean userBean;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_setting_center);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		TextView title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(R.string.Person_Setting_Center);
		start = new HttpStart(context, handler);
		// 修改密碼
		password = (LinearLayout) findViewById(R.id.password);
		password.setOnClickListener(this);
		// 版本更新
		try {
			packageInfo = getApplicationContext().getPackageManager()
					.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String versionstr = packageInfo.versionName.toString();
		version_tv = (TextView) findViewById(R.id.version_tv);
		upgrade_version = (LinearLayout) findViewById(R.id.upgrade_version);
		version_tv.setText("版本更新 V" + versionstr);
		upgrade_version.setOnClickListener(this);
		
		//版本說明
		shuoming_version = (LinearLayout) findViewById(R.id.shuoming_version);
		shuoming_version.setOnClickListener(this);
		
		//聯繫我們
		lianxi_women = (LinearLayout) findViewById(R.id.lianxi_women);
		lianxi_women.setOnClickListener(this);
		
		// 退出程序
		delete = (LinearLayout) findViewById(R.id.delete);
		delete.setOnClickListener(this);
		
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
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
			}
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
				UIHelper.ToastMessage(context, "已是最新版本");
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.i(">>>", "get version error");
			e.printStackTrace();
		}

	}

	// 返回键按钮
	public void activity_back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		if (userBean.getLogonName() == null || userBean.getLogonName().length() == 0) {
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.password:
			intent = new Intent(Person_Setting_Center.this,
					Person_Modify_password.class);
			intent.putExtra("usernum", userBean.getLogonName());
			startActivity(intent);
			break;
		case R.id.upgrade_version:	
			start.getServerDataUpdate(3, MyConstant.GET_CHECK_VERSION,
					MyConstant.GET_localAPK);
			break;
		case R.id.shuoming_version:
			intent = new Intent(context,Shuoming_version.class);
			context.startActivity(intent);
			break;
		case R.id.lianxi_women:
			intent = new Intent(context,Lianxi_women.class);
			context.startActivity(intent);
			break;
			
		case R.id.delete:
			new AlertDialog.Builder(Person_Setting_Center.this)
			.setIcon(R.drawable.nt_warn)
			.setTitle(
					getResources().getString(
							R.string.Person_Determine_exit))
			.setPositiveButton(
					getResources().getString(
							R.string.Person_Determine),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int which) {
							code.clean_userinfo(context);
							Intent intent = new Intent(context,LoginActivity.class);
							startActivity(intent);
							stopService(new Intent(context,TimeServer.class));
						}
					})
			.setNeutralButton(
					getResources()
							.getString(R.string.Person_Cancel),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int which) {
						}
					}).show();
			break;
		default:
			break;
		}
	}
}
