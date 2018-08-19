package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.ChangString;

public class Person_Modify_delete extends BaseActivity {
	Intent intent;
	Context context = Person_Modify_delete.this;
	private String string = null;
	HttpStart start = null;
	UserBean userBean;
	private EditText job_number, chinese_name, english_name, duties, telephone,
			eMail, executive_director, bu, bu_again, keeping_personnel,
			recording_time;
	private String Job_number, Chinese_name, English_name, Telephone, EMail,
			Industry_title, Section, Business_name, Division_BU, BU,
			UserLevels, dutiesstr;

	String string1, string2, string3, string4, string5, strlogonname;
	Spinner spinner1, spinner2, spinner3, spinner4, spinner5;

	private List<String> mfgstr = null;

	int k = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_modify_delete);
		TextView title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(R.string.Person_detailed_information);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		start = new HttpStart(context, handler);

		job_number = (EditText) findViewById(R.id.a1);
		chinese_name = (EditText) findViewById(R.id.a2);
		english_name = (EditText) findViewById(R.id.a3);
		duties = (EditText) findViewById(R.id.a4);
		telephone = (EditText) findViewById(R.id.a5);
		eMail = (EditText) findViewById(R.id.a6);
		executive_director = (EditText) findViewById(R.id.a7);
		bu = (EditText) findViewById(R.id.a8);

		// 部门
		spinner1 = (Spinner) findViewById(R.id.a9);
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				Industry_title = parent.getItemAtPosition(pos).toString();
				if (k == 1) {
					start.getServerData(0, MyConstant.ACQUISITION_STATION,
							Division_BU);
				}

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 製造处名称
		spinner2 = (Spinner) findViewById(R.id.a10);
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				Business_name = parent.getItemAtPosition(pos).toString();
				// 获取事业处BU
				if (k == 1) {
					start.getServerData(0, MyConstant.GET_A_SBU_ADMIN, userBean
							.getBU().toString(), Business_name);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 製造处BU
		spinner3 = (Spinner) findViewById(R.id.a11);
		spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				Division_BU = parent.getItemAtPosition(pos).toString();
				if (k == 1) {
					start.getServerData(0, MyConstant.GET_CREATE_TEAM_ADMIN,
							userBean.getBU().toString(), Business_name,
							Division_BU);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 工站（结束）
		spinner4 = (Spinner) findViewById(R.id.a12);
		spinner4.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				Section = parent.getItemAtPosition(pos).toString();
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		bu_again = (EditText) findViewById(R.id.a13);
		keeping_personnel = (EditText) findViewById(R.id.a14);
		recording_time = (EditText) findViewById(R.id.a15);

		spinner5 = (Spinner) findViewById(R.id.a16);
		spinner5.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String[] languages = getResources().getStringArray(
						R.array.quanxian);
				UserLevels = languages[pos];
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// 得到传递的数据
		intent = getIntent();
		string = intent.getStringExtra("name");
		start.getServerData(3, MyConstant.GET_EMPLOYEE_INFOMATION, string);

	}

	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
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
						string1 = (result.get(i + 7));
						string3 = (result.get(i + 8));
						string2 = (result.get(i + 9));
						bu.setText(result.get(i + 10));
						string4 = (result.get(i + 11));
						bu_again.setText(result.get(i + 12));
						keeping_personnel.setText(result.get(i + 13));
						recording_time.setText(result.get(i + 14));
						string5 = result.get(i + 15);
					}
					start.getServerData(0, MyConstant.GET_CAREER_MFG_ADMIN,
							userBean.getBU());

				}
				// 获取事业处
				if (key.equals(MyConstant.GET_CAREER_MFG_ADMIN)) {
					result = msg.getData().getStringArrayList(key);
					mfgstr = new ArrayList<String>();
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i++) {
							mfgstr.add(result.get(i).toString());
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						mfgstr.add("N/A");
					}
					spinner2.setAdapter(new ArrayAdapter(context,
							android.R.layout.simple_spinner_dropdown_item,
							mfgstr));
					for (int i = 0; i < mfgstr.size(); i++) {
						if (mfgstr.get(i).equals(string2)) {
							spinner2.setSelection(i, true);
						}
					}
					Business_name = spinner2.getSelectedItem().toString();
					start.getServerData(0, MyConstant.GET_A_SBU_ADMIN, userBean
							.getBU().toString(), Business_name);
				}
				// 获取事业处BU
				if (key.equals(MyConstant.GET_A_SBU_ADMIN)) {
					result = msg.getData().getStringArrayList(key);
					mfgstr = new ArrayList<String>();
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i++) {
							mfgstr.add(result.get(i).toString());
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						mfgstr.add("N/A");
					}
					spinner3.setAdapter(new ArrayAdapter(context,
							android.R.layout.simple_spinner_dropdown_item,
							mfgstr));
					for (int i = 0; i < mfgstr.size(); i++) {
						if (mfgstr.get(i).equals(string3)) {
							spinner3.setSelection(i, true);
						}
					}
					Division_BU = spinner3.getSelectedItem().toString();
					start.getServerData(0, MyConstant.GET_CREATE_TEAM_ADMIN,
							userBean.getBU().toString(), Business_name,
							Division_BU);
				}
				if (key.equals(MyConstant.GET_CREATE_TEAM_ADMIN)) {
					result = msg.getData().getStringArrayList(key);
					mfgstr = new ArrayList<String>();
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i++) {
							mfgstr.add(result.get(i).toString());
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						mfgstr.add("N/A");
					}
					spinner1.setAdapter(new ArrayAdapter(context,
							android.R.layout.simple_spinner_dropdown_item,
							mfgstr));
					for (int i = 0; i < mfgstr.size(); i++) {
						if (mfgstr.get(i).equals(string1)) {
							spinner1.setSelection(i, true);
						}
					}
					Industry_title = spinner1.getSelectedItem().toString();
					start.getServerData(0, MyConstant.ACQUISITION_STATION,
							Division_BU);
				}

				// 获取工站
				if (key.equals(MyConstant.ACQUISITION_STATION)) {
					result = msg.getData().getStringArrayList(key);
					mfgstr = new ArrayList<String>();
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i++) {
							mfgstr.add(result.get(i).toString());
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						mfgstr.add("N/A");
					}

					spinner4.setAdapter(new ArrayAdapter(context,
							android.R.layout.simple_spinner_dropdown_item,
							mfgstr));
					for (int i = 0; i < mfgstr.size(); i++) {
						if (mfgstr.get(i).equals(string4)) {
							spinner4.setSelection(i, true);
						}
					}
					Section = spinner4.getSelectedItem().toString();
					k = 1;
				}
			}
		};
	};

	// 提交按钮
	public void tijiao1(View view) {
		int i = 1;
		Job_number = job_number.getText().toString().trim();
		Chinese_name = chinese_name.getText().toString().trim();
		Chinese_name = ChangString.change(Chinese_name);
		English_name = english_name.getText().toString().trim();
		Telephone = telephone.getText().toString().trim();
		EMail = eMail.getText().toString().trim();
		BU = bu.getText().toString().trim();
		dutiesstr = ChangString.change(duties.getText().toString().trim());
		new AlertDialog.Builder(Person_Modify_delete.this)
				.setIcon(R.drawable.nt_warn)
				.setTitle(getResources().getString(R.string.Person_SUBMIT))
				.setPositiveButton(
						getResources().getString(R.string.Person_Determine),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (Integer.parseInt(UserLevels) >= Integer
										.parseInt(userBean.getUserLevel())) {
									Toast.makeText(getApplicationContext(),
											"不允许添加权限", Toast.LENGTH_SHORT)
											.show();
								} else if (Job_number.isEmpty()
										|| Chinese_name.isEmpty()) {
									Toast.makeText(getApplicationContext(),
											"工号或中文名不能为空", Toast.LENGTH_SHORT)
											.show();
								} else {
									boolean a = checkNameChese(chinese_name
											.getText().toString().trim());
									if (a == true) {
										getall();
										Toast.makeText(getApplicationContext(),
												"提交成功", Toast.LENGTH_SHORT)
												.show();
										intent = new Intent(
												Person_Modify_delete.this,
												Person_Information.class);
										startActivity(intent);
										Person_Modify_delete.this.finish();
									} else {
										Toast.makeText(getApplicationContext(),
												"中文名请输入中文", Toast.LENGTH_SHORT)
												.show();
									}
								}
							}
						})
				.setNeutralButton(
						getResources().getString(R.string.Person_Cancel),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
	}

	// 提交修改
	private void getall() {
		String No = "NULL";
		start.getServerData(3, MyConstant.MODIFY_EMPLOYEE_INFORMATION,
				Job_number, No, Chinese_name, English_name, dutiesstr,
				Telephone, EMail, No, Industry_title, Section, Division_BU,
				Business_name, BU, No, No, No, UserLevels);
	}

	// 删除按钮
	public void dele(View view) {
		strlogonname = job_number.getText().toString().trim();
		if (strlogonname.equalsIgnoreCase(userBean.getLogonName().toString())) {
			UIHelper.ToastMessage(context, "不能刪除自己");
			return;
		}
		new AlertDialog.Builder(Person_Modify_delete.this)
				.setIcon(R.drawable.nt_warn)
				.setTitle(getResources().getString(R.string.Person_DELETE))
				.setPositiveButton(
						getResources().getString(R.string.Person_Determine),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								start.getServerData(3,
										MyConstant.DELETE_EMPLOYEE_INFORMATION,
										strlogonname);
								Toast.makeText(getApplicationContext(), "删除成功",
										Toast.LENGTH_SHORT).show();
								intent = new Intent(Person_Modify_delete.this,
										Person_Information.class);
								startActivity(intent);
								Person_Modify_delete.this.finish();
							}
						})
				.setNeutralButton(
						getResources().getString(R.string.Person_Cancel),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
	}

	// 判定输入汉字
	public boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	// 检测String是否全是中文
	public boolean checkNameChese(String name) {
		boolean res = true;
		char[] cTemp = name.toCharArray();
		for (int i = 0; i < name.length(); i++) {
			if (!isChinese(cTemp[i])) {
				res = false;
				break;
			}
		}
		return res;
	}

	// 返回键按钮
	public void activity_back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
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