package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.http.HttpStart;

public class Person_View_information extends BaseActivity {
	Intent intent;
	Context context = Person_View_information.this;
	private String string = null;
	HttpStart start = null;
	private UserBean userBean;

	private EditText job_number, chinese_name, english_name, duties, telephone,
			eMail, executive_director, industry_title, section, division_BU,
			business_name, bu, bu_again, keeping_personnel, recording_time;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_view_information);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		TextView title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(R.string.Person_detailed_information);

		job_number = (EditText) findViewById(R.id.e01);
		chinese_name = (EditText) findViewById(R.id.e02);
		english_name = (EditText) findViewById(R.id.e03);
		duties = (EditText) findViewById(R.id.e04);
		telephone = (EditText) findViewById(R.id.e05);
		eMail = (EditText) findViewById(R.id.e06);
		executive_director = (EditText) findViewById(R.id.e07);
		industry_title = (EditText) findViewById(R.id.e09);
		division_BU = (EditText) findViewById(R.id.e11);
		business_name = (EditText) findViewById(R.id.e10);
		bu = (EditText) findViewById(R.id.e08);
		section = (EditText) findViewById(R.id.e12);
		bu_again = (EditText) findViewById(R.id.e13);
		keeping_personnel = (EditText) findViewById(R.id.e14);
		recording_time = (EditText) findViewById(R.id.e15);

		// 得到传递的数据
		intent = getIntent();
		string = intent.getStringExtra("name");
		getDataMore();
	}

	private void getDataMore() {
		start = new HttpStart(context, handler);
		start.getServerData(3, MyConstant.GET_EMPLOYEE_INFOMATION, string);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.equals(MyConstant.GET_EMPLOYEE_INFOMATION)) {
					result = msg.getData().getStringArrayList(key);
					for (int i = 1; i < result.size(); i += 16) {
						// 显示数据
						job_number.setText(result.get(i + 0));
						chinese_name.setText(result.get(i + 1));
						english_name.setText(result.get(i + 2));
						duties.setText(result.get(i + 3));
						telephone.setText(result.get(i + 4));
						eMail.setText(result.get(i + 5));
						executive_director.setText(result.get(i + 6));
						bu.setText(result.get(i + 10));
						industry_title.setText(result.get(i + 7));
						business_name.setText(result.get(i + 9));
						division_BU.setText(result.get(i + 8));
						section.setText(result.get(i + 11));
						bu_again.setText(result.get(i + 12));
						keeping_personnel.setText(result.get(i + 13));
						recording_time.setText(result.get(i + 14));
					}
				}
			}
		};
	};

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
}
