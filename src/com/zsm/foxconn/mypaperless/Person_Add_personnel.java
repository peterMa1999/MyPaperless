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
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class Person_Add_personnel extends BaseActivity {
	Button button;
	UserBean userBean;
	Context context = Person_Add_personnel.this;
	HttpStart start = null;
	private EditText job_number, chinese_name, english_name, telephone, eMail,
			executive_director;

	private String Job_number, Chinese_name, English_name, Duties, Telephone,
			EMail, UserLevel, Executive_director, Industry_title, Section,
			Division_BU, Business_name, BU, BU_again;

	private List<String> mfgstr = null;

	Spinner spinner2, spinner3, spinner4, spinner1;
	private boolean isfirst = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_add_personnel);
		TextView title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(R.string.Person_Add_personnel);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		start = new HttpStart(context, handler);

		job_number = (EditText) findViewById(R.id.te1);
		chinese_name = (EditText) findViewById(R.id.te3);
		english_name = (EditText) findViewById(R.id.te4);
		Spinner spinner8 = (Spinner) findViewById(R.id.te5);
		spinner8.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String[] languages = getResources().getStringArray(
						R.array.Duties);
				Duties = languages[pos];
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		telephone = (EditText) findViewById(R.id.te6);
		eMail = (EditText) findViewById(R.id.te7);
		eMail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

		spinner1 = (Spinner) findViewById(R.id.te8);
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String[] languages = getResources().getStringArray(
						R.array.UserLevel);
				UserLevel = languages[pos];
				if (UserLevel.equals("普通用户")) {
					UserLevel = "0";
				} else if (UserLevel.equals("管理员")) {
					UserLevel = "1";
				} else {
					UserLevel = "2";
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		executive_director = (EditText) findViewById(R.id.te9);
		executive_director.setText(userBean.getLogonName());
		EditText bu_ed = (EditText) findViewById(R.id.te14);
		BU = userBean.getBU();
		bu_ed.setText(BU);
		Spinner spinner7 = (Spinner) findViewById(R.id.te15);
		spinner7.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String[] languages = getResources().getStringArray(
						R.array.BU_again);
				BU_again = languages[pos];
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 获取事业处
		start.getServerData(0, MyConstant.GET_CAREER_MFG_ADMIN, BU);
		// 事业处名称
		spinner2 = (Spinner) findViewById(R.id.te12);
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				Business_name = parent.getItemAtPosition(pos).toString();
				// 获取SBU
				start.getServerData(0, MyConstant.GET_A_SBU_ADMIN, BU,
						Business_name);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 获取SBU
		spinner3 = (Spinner) findViewById(R.id.te13);
		spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// 获取team
				Division_BU = parent.getItemAtPosition(pos).toString();
				start.getServerData(0, MyConstant.GET_CREATE_TEAM_ADMIN, BU,
						Business_name, Division_BU);

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 部门
		spinner1 = (Spinner) findViewById(R.id.te10);
		Industry_title = spinner1.getItemAtPosition(0).toString();
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				Industry_title = parent.getItemAtPosition(pos).toString();
				// 获取工站
				start.getServerData(0, MyConstant.ACQUISITION_STATION,
						Division_BU);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 工站（结束）
		spinner4 = (Spinner) findViewById(R.id.te11);
		spinner4.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (!isfirst) {
					isfirst = true;
				} else {
					Section = parent.getItemAtPosition(pos).toString();
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});


		button = (Button) findViewById(R.id.Submit);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Job_number = job_number.getText().toString().trim();
				Chinese_name = chinese_name.getText().toString().trim();
				Chinese_name = ChangString.change(Chinese_name);
				new AlertDialog.Builder(Person_Add_personnel.this)
						.setIcon(R.drawable.nt_warn)
						.setTitle(getResources().getString(R.string.Person_ADD))
						.setPositiveButton(
								getResources().getString(
										R.string.Person_Determine),
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										if (Job_number.isEmpty()
												|| Chinese_name.isEmpty()) {
											Toast.makeText(
													getApplicationContext(),
													"工号或中文名不能为空",
													Toast.LENGTH_SHORT).show();
										} else {
											boolean a = checkNameChese(chinese_name
													.getText().toString()
													.trim());
											if (a == true) {
												// 验证用户
												start.getServerData(3,
														MyConstant.VERIFY_USER,
														Job_number);
											} else {
												Toast.makeText(
														getApplicationContext(),
														"中文名请输入中文",
														Toast.LENGTH_SHORT)
														.show();
											}
										}
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

			}
		});
	}

	// 添加员工
	private void getDataAll() {

		English_name = english_name.getText().toString().trim();
		Telephone = telephone.getText().toString().trim();
		EMail = eMail.getText().toString().trim();
		Executive_director = executive_director.getText().toString().trim();

		start.getServerData(3, MyConstant.ADD_EMPLOYEE_INFORMATION, Job_number,
				Job_number, Chinese_name, English_name, Duties, Telephone,
				EMail, UserLevel, Executive_director, Industry_title, Section,
				Division_BU, Business_name, BU, BU_again,
				userBean.getLogonName(), "CNSBG");
		Intent intent = new Intent(Person_Add_personnel.this,
				Person_Information.class);
		startActivity(intent);
		Person_Add_personnel.this.finish();
	}

	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {

			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();

			for (String key : keySet) {
				if (key.equals(MyConstant.ADD_EMPLOYEE_INFORMATION)) {
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
				}

				// 验证用户
				if (key.equals(MyConstant.VERIFY_USER)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						if (result.get(1).toString().equals(Job_number)) {
							Toast.makeText(getApplicationContext(),
									"此用户已经添加,添加失败", Toast.LENGTH_SHORT).show();
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						getDataAll();
						Toast.makeText(getApplicationContext(), "添加成功",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	};

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
