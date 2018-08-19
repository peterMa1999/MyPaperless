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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

public class Person_Modify_password extends BaseActivity {
	HttpStart start = null;
	Context context = Person_Modify_password.this;

	private String Old_password, New_password, New_password_confirmation;
	private EditText old_password, new_password, new_password_confirmation;
	Button button;
	private Intent intent = null;

	private String Job_number, Chinese_name, English_name, Duties, Telephone,
			EMail, UserLevel, Executive_director, Industry_title, Section,
			Division_BU, Business_name, BU, BU_again;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_modify_password);
		TextView title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(R.string.Person_Modify_password);
		intent = getIntent();
		Job_number = intent.getStringExtra("usernum");
		old_password = (EditText) findViewById(R.id.cl1);
		new_password = (EditText) findViewById(R.id.cl2);
		new_password_confirmation = (EditText) findViewById(R.id.cl3);

		// 提交按钮
		button = (Button) findViewById(R.id.go);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String password_style = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\!\\@\\#\\$\\%\\^\\&\\*\\.])[0-9a-zA-Z\\!\\@\\#\\$\\%\\^\\&\\*\\.]{8,16}$";
				Old_password = old_password.getText().toString().trim();
				New_password = new_password.getText().toString().trim();
				New_password_confirmation = new_password_confirmation.getText()
						.toString().trim();
				if (Old_password.isEmpty() || New_password.isEmpty()
							|| New_password_confirmation.isEmpty()) {
						Toast.makeText(getApplicationContext(), "密码不能空",
								Toast.LENGTH_SHORT).show();
					}else {
						if (New_password.matches(password_style)&&New_password_confirmation.matches(password_style)) {

							if (!New_password.equals(New_password_confirmation)) {
								Toast.makeText(getApplicationContext(), "新密码輸入不一致",
										Toast.LENGTH_SHORT).show();
							} else {
								getDataAll();
							}
						}else {
							UIHelper.ToastMessage(context, "輸入密碼不符合規則");
						}
					}
			}
		});
	}

	// 修改密码
	private void getDataAll() {

		start = new HttpStart(context, handler);
		start.getServerData(3, MyConstant.MOEDIFY_PASSWORD, Job_number,
				New_password, Chinese_name, English_name, Duties, Telephone,
				EMail, UserLevel, Executive_director, Industry_title, Section,
				Division_BU, Business_name, BU, BU_again,
				Job_number, "CNSBG",Old_password);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = msg.getData().getStringArrayList(key);
				if (key.equals(MyConstant.MOEDIFY_PASSWORD)) {
					if (result.get(0).toString().equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "修改失败");
						return;
					}else if (result.get(0).toString().equals(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "修改成功");
					}else if (result.get(0).toString().equals("OLD PASSWORD ERROR")) {
						UIHelper.ToastMessage(context, "舊密碼錯誤");
					}else {
						UIHelper.ToastMessage(context, "修改異常");
					}
				}
				
				if (key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(context, "網絡異常");
				}
			}
		}
	};

	// 返回键按钮
	public void activity_back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
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
