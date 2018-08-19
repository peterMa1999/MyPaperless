package com.zsm.foxconn.mypaperless;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.database.Forgetpwd_table;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

/**
 * 
 * @author MPG
 * 
 *         2016-11-22 下午5:09:28 Forgetpwd 找回密碼
 */
public class Forgetpwd extends BaseActivity {

	private EditText ed_forgetpwd, ed_forgetpwd_email;
	private Button send_email;
	HttpStart start = null;
	private FinalDb finalDb = null;
	private Context context = Forgetpwd.this;
	private String newtime;
	private List<Forgetpwd_table> forget_list =  null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forgetpwd);
		findViewById();
		start = new HttpStart(context, handler);
		send_email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ed_forgetpwd.getText().length() != 0
						&& ed_forgetpwd_email.getText().length() != 0) {
					start.getServerData(0, MyConstant.GET_INTERNET_TIME);
				}
			}
		});

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = msg.getData().getStringArrayList(key);
				if (key.equals(MyConstant.USER_FORGETPWD)) {
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						finalDb = FinalDb.create(context, "child"); // 创建数据库
						forget_list = finalDb
								.findAllByWhere(Forgetpwd_table.class, "usernum='"
										+ ed_forgetpwd.getText().toString().trim()
										+ "' and is_sucessful='0'"
										+ " and forget_time='"
										+ newtime + "'");
						for (int i = 0; i < forget_list.size(); i++) {
							System.out.println(forget_list.get(i).getSendnum()+"---");
						}
						Forgetpwd_table forgetpwd = new Forgetpwd_table();
						forgetpwd.setUsernum(ed_forgetpwd.getText().toString()
								.trim());
						forgetpwd.setIs_sucessful(0);
						forgetpwd.setForget_time(newtime);
						if (forget_list.size()>0) {
							if (5-forget_list.get(0).getSendnum()!=0) {
								UIHelper.ToastMessage(context, "賬戶和郵箱不匹配,你還有"+(5-forget_list.get(0).getSendnum())+"次機會");
							}
							forgetpwd.setSendnum(forget_list.get(0).getSendnum()+1);
							finalDb.update(forgetpwd, "usernum='"
										+ ed_forgetpwd.getText().toString().trim()
										+ "' and is_sucessful='0'"
										+ " and forget_time='"
										+ newtime + "'");
						}else {
							UIHelper.ToastMessage(context, "賬戶和郵箱不匹配,今日你還有5次機會");
							forgetpwd.setSendnum(1);
							finalDb.save(forgetpwd);
						}
					} else if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "發送成功");
						finalDb = FinalDb.create(context, "child"); // 创建数据库
						Forgetpwd_table forgetpwd = new Forgetpwd_table();
						forgetpwd.setUsernum(ed_forgetpwd.getText().toString()
								.trim());
						forgetpwd.setIs_sucessful(1);
						forgetpwd.setForget_time(newtime);
						forgetpwd.setSendnum(1);
						finalDb.save(forgetpwd);
					}
				}
				if (key.equals(MyConstant.GET_INTERNET_TIME)) {
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "未连接服务器....", 0).show();
						return;
					}
					newtime = result.get(0).toString();
					finalDb = FinalDb.create(context, "child"); // 创建数据库
					List<Forgetpwd_table> forgetpwd_list = finalDb
							.findAllByWhere(Forgetpwd_table.class, "usernum='"
									+ ed_forgetpwd.getText().toString().trim()
									+ "' and is_sucessful='1'"
									+ " and sendnum='1' and forget_time='"
									+ newtime + "'");
					forget_list = finalDb
							.findAllByWhere(Forgetpwd_table.class, "usernum='"
									+ ed_forgetpwd.getText().toString().trim()
									+ "' and is_sucessful='0'"
									+ " and forget_time='"
									+ newtime + "'");
					for (int i = 0; i < forget_list.size(); i++) {
						System.out.println(forget_list.get(i).toString()+"-");
					}
					if (forgetpwd_list.size() == 0&&forget_list.size()==0) {
						start.getServerData(0, MyConstant.USER_FORGETPWD,
								ed_forgetpwd.getText().toString().trim(),
								ed_forgetpwd_email.getText().toString().trim());
					}else {
						if (forgetpwd_list.size()>0) {
							UIHelper.ToastMessage(context, "已經發過了...");
						}else if (forget_list.size()>0) {
							if (forget_list.get(0).getSendnum()>5) {
								UIHelper.ToastMessage(context, "今日發送次數超過限制");
							}else if (forget_list.get(0).getSendnum()<6) {
								start.getServerData(0, MyConstant.USER_FORGETPWD,
										ed_forgetpwd.getText().toString().trim(),
										ed_forgetpwd_email.getText().toString().trim());
							} 
						}
					}
				}
				if (key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(context, "網絡異常");
				}
			}
		}
	};

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		TextView title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText("找回密碼");
		ed_forgetpwd = (EditText) findViewById(R.id.ed_forgetpwd);
		ed_forgetpwd_email = (EditText) findViewById(R.id.ed_forgetpwd_email);
		send_email = (Button) findViewById(R.id.send_email);
		send_email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	// 返回键按钮
	public void activity_back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}
}
