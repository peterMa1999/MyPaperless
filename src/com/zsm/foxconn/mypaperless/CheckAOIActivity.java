package com.zsm.foxconn.mypaperless;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.adapter.CheckeAdapter2;
import com.zsm.foxconn.mypaperless.adapter.MyExpandableAdapterT;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.CheckHolder;
import com.zsm.foxconn.mypaperless.bean.ChildModel;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.qr.CaptureActivity;

public class CheckAOIActivity extends BaseActivity implements OnClickListener {

	private UserBean user;
	HttpStart start;
	private Context context = CheckAOIActivity.this;
	private Spinner lineSP;
	private TextView shiftTV, RNOTV;
	private AutoCompleteTextView autoTV;
	private Button selectBtn, submitBtn;
	private String reportName, shift, RNO, mfg, reportNum, ToolNO, mline;
	private List<String> AllLine = new ArrayList<String>();
	private List<String> ToolNos = new ArrayList<String>();
	private int submitLzfsBS = -1;
	ExpandableListView expListView;
	List<String> CompleteLzs = new ArrayList<String>();
	LinkedList<String> groupData = new LinkedList<String>();
	List<LinkedList<ChildModel>> childData = new ArrayList<LinkedList<ChildModel>>();
	List<List<Object>> saveData = null; // 需存儲到數據庫的點檢結果
	MyExpandableAdapterT adapter;
	boolean InputOk = false;// 內容是否全部填完
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
		setContentView(R.layout.activity_check_aoi);
		user = (UserBean) getApplicationContext();
		start = new HttpStart(context, handler);
		start.getServerData(3, MyConstant.GET_SHIFT);
		CheckLogin();
		Intent intent = getIntent();
		reportName = intent.getStringExtra("reportName");
		findViewById();
		setListener();
		mfg = user.getMFG();

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
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		TextView textView = (TextView) findViewById(R.id.bartitle_txt);
		textView.setText(reportName);
		shiftTV = (TextView) findViewById(R.id.tv_shift);
		RNOTV = (TextView) findViewById(R.id.tv_RNO);
		lineSP = (Spinner) findViewById(R.id.sp_line);
		autoTV = (AutoCompleteTextView) findViewById(R.id.auto);
		selectBtn = (Button) findViewById(R.id.select);
		submitBtn = (Button) findViewById(R.id.btn_submit);
		expListView = (ExpandableListView) findViewById(R.id.LTlaptop_list);
		cameraIB = (ImageButton) findViewById(R.id.btn_camera);
		layout_check_aoi = (LinearLayout) findViewById(R.id.layout_check_aoi);
	}

	protected void setListener() {
		// TODO Auto-generated method stub
		selectBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		cameraIB.setOnClickListener(this);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		SimpleDateFormat dat = new SimpleDateFormat("yyyyMMddHHmmss");
		String iDate = dat.format(new Date()).toString();
		String time = iDate.substring(0, 12);

		if (reportName.equalsIgnoreCase("SMT AOI每日點檢保養記錄表")) {
			RNO = time + "AOI" + mfg + shift;
		} else if (reportName.equalsIgnoreCase("離子發生器周點檢表")) {
			RNO = time + "ION" + mfg + shift;
		} else if (reportName.equalsIgnoreCase("條碼打印每日點檢保養記錄表")) {
			RNO = time + "BARCODE" + mfg + shift;
		}
		RNOTV.setText(RNO);
		shiftTV.setText(shift);
		if (reportName.equalsIgnoreCase("SMT AOI每日點檢保養記錄表")) {
			start.getServerData(3, MyConstant.GET_SMTLINE, mfg);
		} else {
			start.getServerData(3, MyConstant.GET_NOTSMTLINE, mfg);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_camera:
			// 打开扫描界面扫描条形码或二维码
			Intent openCameraIntent = new Intent(context, CaptureActivity.class);
			startActivityForResult(openCameraIntent, SCANER_CODE);
			break;
		case R.id.select:
			if (submitLzfsBS == -1) {
				ToolNO = autoTV.getText().toString();
				searchQZ(0, ToolNos.size() - 1, ToolNos);
				if (bl) {
					submitLzfsBS = 1;
					selectBtn.setText("取消");
					autoTV.setEnabled(false);
				} else {
					UIHelper.alertDialog(context, "無此編號");
				}
			} else {
				submitLzfsBS = -1;
				bl = false;
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

						@SuppressWarnings("unchecked")
						@Override
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
										if (reportName.equals("條碼打印每日點檢保養記錄表")) {
											UIHelper.alertDialog(context,
													"請選中需點檢的條碼打印机编号");
										} else if (reportName
												.equals("SFPG工裝板線每日點檢保養記錄表")) {
											UIHelper.alertDialog(context,
													"請選中需點檢的工裝板線編號");
										} else {
											UIHelper.alertDialog(context,
													"請選中需點檢的離子發生器编号");
										}
									}
								}

							}
						}
					});

			alert.setButton(DialogInterface.BUTTON_POSITIVE, "否，立即交由當前線長審核！",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
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
		if (CompleteLzs.indexOf(ToolNO) == -1) {
			if (i==0) {
				popsindows();
			}else {
				UIHelper.alertDialog(context, "已經點檢過了...");
			}
		} else {
			UIHelper.alertDialog(context, "此编号已經點檢");
		}

	}

	String Check_Result = "";

	private boolean checkInputOk() {
		if (Check_Result.length() > 0) {
			Check_Result = "";
		}
		saveData = new ArrayList<List<Object>>(); // 需存儲到數據庫的數據
		childData = adapter.getChildData();
		List<ChildModel> result1 = null;
		ChildModel model;
		for (int i = 0; i < childData.size(); i++) {
			result1 = childData.get(i);
			for (int j = 0; j < result1.size(); j++) {
				model = result1.get(j);
				if ((model.getChildContent().length() == 0 || model
						.getChildContent().toString().isEmpty())
						|| (model.getChildIcar().length() == 0 || model
								.getChildIcar().toString().isEmpty())) {
					UIHelper.alertDialog(context, model.getChildTitle()
							+ "選項不能為空");
					return false;
				} else {
					int flag = 0;

					Check_Result += RNO + "~" + flag + "~" + reportNum + "~"
							+ model.getProId() + "~" + model.getChildContent()
							+ "~" + model.getChildResult() + "~"
							+ model.getChildIcar() + "~" + ToolNO + "~a66abb5684c45962d887564f08346e8d";

				}
			}
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

	boolean bl = false;

	public void searchQZ(int x, int y, List<String> ToolNos) {// x,y標記位置
		if (bl == false) {
			if (y - x != 1 && y - x != 2) {
				searchQZ(x, (y - x) / 2 + x, ToolNos);
				searchQZ((y - x) / 2 + x + 1, y, ToolNos);
			}
			if (y - x == 1
					&& (ToolNos.get(x).equals(ToolNO) || ToolNos.get(y).equals(
							ToolNO))) {
				bl = true;
			}
			if (y - x == 2
					&& (ToolNos.get(x).equals(ToolNO)
							|| ToolNos.get(x + 1).equals(ToolNO) || ToolNos
							.get(y).equals(ToolNO))) {
				bl = true;
				return;
			}
		}
		return;
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
					}
				} else if (key.equals(MyConstant.GET_SMTLINE)) {
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
					}
				} else if (key.equals(MyConstant.GET_REPORTNUM)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						reportNum = result.get(1).toString();
						start.getServerData(3, MyConstant.GET_REPORTTOOLNO,
								mfg, reportNum);
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
						start.getServerData(3, MyConstant.GET_CHECKREPORTTITLE,
								reportNum, user.getSite(), mfg, user.getSBU());
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "网络异常....", 0).show();
					}
				} else if (key.equals(MyConstant.GET_CHECKREPORTTITLE)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						// 获取groupData
						for (int i = 1; i < result.size(); i += 4) {
							if (!groupData.contains(result.get(i).toString())) {
								groupData.add(result.get(i).toString());
							}
						}

						// child:(title,proid,checkflag)
						List<String> child = new ArrayList<String>();
						for (int j = 0; j < groupData.size(); j++) {
							String ss = "";
							for (int k = 1; k < result.size(); k += 4) {
								if (groupData.get(j).toString()
										.equals(result.get(k).toString())) {
									ss += result.get(k + 1).toString() + "%"
											+ result.get(k + 2).toString()
											+ "%"
											+ result.get(k + 3).toString()
											+ "%";
								}
							}
							System.out.println(ss);
							child.add(ss);
						}
						// 获取二级条目数据
						for (int x = 0; x < child.size(); x++) {
							LinkedList<ChildModel> childList = new LinkedList<ChildModel>();
							String[] singChild = child.get(x).toString()
									.split("%");
							for (int b = 0; b < singChild.length; b += 3) {
								ChildModel model = new ChildModel();
								model.setChildTitle(singChild[b]);
								model.setProId(singChild[b + 2]);
								model.setChildInputFlag(singChild[b + 1]);
								model.setChildContent("");
								model.setChildResult("0");
								childList.add(model);
							}
							childData.add(childList);
						}
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "此报表點檢項目还未配置,請聯繫組長進行配置!", 0)
								.show();
					}
					adapter = new MyExpandableAdapterT(context, groupData,
							childData);
					expListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else if (key.equals(MyConstant.GET_CHECKTOOLNO)) {
					result = new ArrayList<String>();
					if (CompleteLzs.size() > 0) {
						CompleteLzs.clear();
					}
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i++) {
							CompleteLzs.add(result.get(i).toString());
						}
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
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "點檢異常", 0).show();
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
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "點檢異常", 0).show();
					}
				} else if (key.equals(MyConstant.SAVE_CHECKRESULT)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					String totalDate = "";
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						String a[] = { RNO,
								"NULL",
								"NULL",
								"0",
								"NULL",
								"NULL",
								"0", "NULL", "NULL", "0",
								user.getLogonName() };
						for (int i = 0; i < a.length; i++) {
							totalDate += a[i] + MyConstant.GET_FLAG3;
						}
						start.getServerData(0, MyConstant.SAVE_CHECK_BASE_INFO,
								totalDate);
					} else {
						UIHelper.alertDialog(context, "點檢失敗");
					}
				}else if (key.equals(MyConstant.SAVE_CHECK_BASE_INFO)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						// Toast.makeText(context, "提交PD点检基本信息成功", 0).show();
						i = 1;
						UIHelper.alertDialog(context, "點檢成功");
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "提交PD点检基本信息失败", 0).show();
					}
				}else if (key.equals(MyConstant.SAVE_CHECK_PERSON)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						start.getServerData(0, MyConstant.SAVE_TOOLCONTENT, RNO,
								ToolNO, reportNum);
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "提交審核人失敗", 0).show();
					}
				}else if (key.equals(MyConstant.GET_PD_CHECK_NAME)) //
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
					if (i==0) {
						start.getServerData(0, MyConstant.SAVE_CHECK_PERSON, RNO,
								personNameString, user.getLogonName().toString()
								.trim());
					}else {
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
