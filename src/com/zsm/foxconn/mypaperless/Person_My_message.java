package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.ChangString;

public class Person_My_message extends BaseActivity{
	UserBean userBean;
	Context context = Person_My_message.this;
	private EditText job_number, chinese_name, english_name, duties, telephone,
			eMail, executive_director, bu, bu_again, keeping_personnel,
			recording_time;
	HttpStart start = null;
	private List<String> mfgstr = null;

	private String logonName, Job_number, PassWord, Chinese_name, English_name,
			Telephone, EMail, Industry_title, Business_name, Division_BU, BU,
			Section;
	String string1, string2, string3, string4;
	Spinner spinner1, spinner2, spinner3, spinner4;
	int k = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_my_message);
		TextView title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(R.string.Person_detailed_information);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		start = new HttpStart(context, handler);
		getDataAll();

		job_number = (EditText) findViewById(R.id.ed1);
		chinese_name = (EditText) findViewById(R.id.ed2);
		english_name = (EditText) findViewById(R.id.ed3);
		duties = (EditText) findViewById(R.id.ed4);
		telephone = (EditText) findViewById(R.id.ed5);
		eMail = (EditText) findViewById(R.id.ed6);
		executive_director = (EditText) findViewById(R.id.ed7);
		bu = (EditText) findViewById(R.id.ed8);

		// 部门
		spinner1 = (Spinner) findViewById(R.id.ed9);
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				
				Industry_title = parent.getItemAtPosition(pos).toString();
				if (k==1) {
					start.getServerData(0, MyConstant.ACQUISITION_STATION,
							Division_BU);
				}
				
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 製造处名称
		spinner2 = (Spinner) findViewById(R.id.ed10);
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				Business_name = parent.getItemAtPosition(pos).toString();
				// 获取事业处BU
				if (k==1) {
					start.getServerData(0,
							MyConstant.GET_A_SBU_ADMIN, userBean.getBU().toString(),
							Business_name);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 製造处BU
		spinner3 = (Spinner) findViewById(R.id.ed11);
		spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				Division_BU = parent.getItemAtPosition(pos).toString();
				if (k==1) {
					start.getServerData(0,
							MyConstant.GET_CREATE_TEAM_ADMIN,
							userBean.getBU().toString(),Business_name,Division_BU);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 工站（结束）
		spinner4 = (Spinner) findViewById(R.id.ed12);
		spinner4.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				Section = parent.getItemAtPosition(pos).toString();
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		bu_again = (EditText) findViewById(R.id.ed13);
		keeping_personnel = (EditText) findViewById(R.id.ed14);
		recording_time = (EditText) findViewById(R.id.ed15);
	}

	// 使用登录工号查询数据
	private void getDataAll() {
		logonName = userBean.getLogonName();
		start.getServerData(0, MyConstant.GET_EMPLOYEE_INFOMATION, logonName);
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
					}
					start.getServerData(0,
							MyConstant.GET_CAREER_MFG_ADMIN,
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
						start.getServerData(0,
								MyConstant.GET_A_SBU_ADMIN, userBean.getBU().toString(),
								Business_name);
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
						start.getServerData(0,
								MyConstant.GET_CREATE_TEAM_ADMIN,
								userBean.getBU().toString(),Business_name,Division_BU);
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
						if (result.get(0).equalsIgnoreCase(
								MyConstant.GET_FLAG_TRUE)) {
							for (int i = 1; i < result.size(); i++) {
								mfgstr.add(result.get(i).toString());
							}
						}
						if (result.get(0).equalsIgnoreCase(
								MyConstant.GET_FLAG_NULL)) {
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
				if (key.equals(MyConstant.MODIFY_EMPLOYEE_INFORMATION)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						userBean.setChineseName(Chinese_name);
						userBean.setEnglishName(English_name);
						userBean.setExt(Telephone);
						userBean.setEmail(EMail);
						userBean.setTeam(Industry_title);
						userBean.setSection(Section);
						userBean.setSBU(Division_BU);
						userBean.setMFG(Business_name);
						userBean.setBU(BU);
					}
					
				}
			}
		};
	};

	// 修改提交
	public void tijiao(View view) {
		Job_number = job_number.getText().toString().trim();
		PassWord = job_number.getText().toString().trim();
		Chinese_name = chinese_name.getText().toString().trim();
		Chinese_name = ChangString.change(Chinese_name);
		English_name = english_name.getText().toString().trim();
		Telephone = telephone.getText().toString().trim();
		EMail = eMail.getText().toString().trim();
		BU = bu.getText().toString().trim();
		new AlertDialog.Builder(Person_My_message.this)
				.setIcon(R.drawable.nt_warn)
				.setTitle("系統提示:")
				.setMessage(getResources().getString(R.string.Person_SUBMIT))
				.setPositiveButton(
						getResources().getString(R.string.Person_Determine),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (Job_number.isEmpty()
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

	// 修改信息
	private void getall() {
		String string = userBean.getLogonName();// 不上传的数据位置
		start.getServerData(3, MyConstant.MODIFY_EMPLOYEE_INFORMATION,
				Job_number, PassWord, Chinese_name, English_name, duties.getText().toString(),
				Telephone, EMail, string,Industry_title, Section,
				Division_BU, Business_name, BU, string, string, string,userBean.getUserLevel());
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
