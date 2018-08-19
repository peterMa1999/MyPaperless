package com.zsm.foxconn.mypaperless;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.tsz.afinal.FinalDb;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.adapter.CheckeAdapter_eCheck;
import com.zsm.foxconn.mypaperless.adapter.ChoosepersonAdapter;
import com.zsm.foxconn.mypaperless.adapter.MyExpandableAdapterT;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.CheckHolder;
import com.zsm.foxconn.mypaperless.bean.ChildModel;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.help.UploadFileTask;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.GetSystemTime;
import com.zsm.qr.CaptureActivity;

public class ECheckActivity extends BaseActivity implements OnClickListener {

	private UserBean user;
	HttpStart start;
	private Context context = ECheckActivity.this;
	private TextView RNOTV, lineTV, floorTV;
	private Button submitBtn;
	private ImageButton imageButton_UP;
	private String reportName, shift, RNO, reportNum, ToolNO, mline, floor,
			section;
	ExpandableListView expListView;
	List<String> CompleteLzs = new ArrayList<String>();
	LinkedList<String> groupData = new LinkedList<String>();
	List<LinkedList<ChildModel>> childData = new ArrayList<LinkedList<ChildModel>>();
	List<List<Object>> saveData = null; // 需存儲到數據庫的點檢結果
	MyExpandableAdapterT adapter;
	boolean InputOk = false;// 內容是否全部填完
	private CheckeAdapter_eCheck adapterPerson = null;
	private ChoosepersonAdapter chooseaAdapter = null;
	private List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> list_choosemap = new ArrayList<Map<String, String>>();
	private Map<String, String> choosemap = null;
	private LinearLayout layout_e_check;
	private CheckHolder holder = new CheckHolder();
	List<String> liststr2 = new ArrayList<String>();
	List<String> list3 = new ArrayList<String>();
	String personNameString = null;
	int i = 0;
	private boolean checkFlag = true, tijiao_way_flag = true;
	private String site, mfg, sbu;
	private final int SCANER_CODE = 1, SCANER_CODE_TWO = 2;
	private LinearLayout layout_user_check, layout_qr_check, tijiao_way_layout;
	private Spinner spinner_floor, spinner_line, way_spinner;
	private String is_input_order, is_usercheck, time, PDteam;
	private String[] linestr = null, floorstr = null;
	private PopupWindow popuWindow;
	private View contentView;
	private FinalDb finalDb = null;
	private String tijiao_way = "NULL";
	private boolean isShow = false;
	private boolean isAbnor = false;
	private GradviewAdapter gradviewAdapter = null;
	private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
	private List<String> photoaddress = new ArrayList<String>();
	private List<String> photoName = new ArrayList<String>();
	private File file = null;
	private String fileName = null;
	private String pro_id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_e_check);
		user = (UserBean) getApplicationContext();
		start = new HttpStart(context, handler);
		CheckLogin();
		Intent intent = getIntent();
		reportName = intent.getStringExtra("reportname");
		reportNum = intent.getStringExtra("reportid");
		Log.i(">>>reportName>>>", reportName);
		site = intent.getStringExtra("site");
		mfg = intent.getStringExtra("mfg");
		sbu = intent.getStringExtra("sbu");
		is_input_order = intent.getStringExtra("is_input_order");
		is_usercheck = intent.getStringExtra("is_usercheck");
		if (is_usercheck.equals("0")) { // 掃碼點檢
			floor = intent.getStringExtra("floorName");
			mline = intent.getStringExtra("linename");
		} else {
			section = intent.getStringExtra("section");
		}
		start.getServerData(3, MyConstant.GET_SHIFT);
		findViewById();
		setListener();
		spinner_floor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				floor = arg0.getItemAtPosition(arg2).toString();
				start.getServerData(3, MyConstant.GET_CHECK_LINE,
						user.getMFG(), section, floor, user.getSBU().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		spinner_line.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				mline = arg0.getItemAtPosition(arg2).toString();
				getSQLlite_checkdata();
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
		layout_qr_check = (LinearLayout) findViewById(R.id.layout_qr_check);
		layout_user_check = (LinearLayout) findViewById(R.id.layout_user_check);
		spinner_floor = (Spinner) findViewById(R.id.spinner_floor);
		spinner_line = (Spinner) findViewById(R.id.spinner_line);
		if (is_usercheck.equals("0")) {
			layout_qr_check.setVisibility(View.VISIBLE);
			lineTV = (TextView) findViewById(R.id.tv_line);
			floorTV = (TextView) findViewById(R.id.tv_floor);
			lineTV.setOnClickListener(this);
			floorTV.setOnClickListener(this);
		} else {
			if (is_input_order.equals("1")) {
				layout_user_check.setVisibility(View.VISIBLE);
			} else if (is_input_order.equals("2")) {
				floor = "NULL";
				mline = "NULL";
			}
		}
		tijiao_way_layout = (LinearLayout) findViewById(R.id.tijiao_way_layout);
		way_spinner = (Spinner) findViewById(R.id.way_spinner);
		RNOTV = (TextView) findViewById(R.id.tv_RNO);
		submitBtn = (Button) findViewById(R.id.btn_submit);
		expListView = (ExpandableListView) findViewById(R.id.LTlaptop_list);
		layout_e_check = (LinearLayout) findViewById(R.id.layout_e_check);
		imageButton_UP = (ImageButton) findViewById(R.id.imageButton_UP);
		imageButton_UP.setOnClickListener(this);
	}

	protected void setListener() {
		// TODO Auto-generated method stub
		submitBtn.setOnClickListener(this);
		way_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 1) {
					tijiao_way = "不點檢";
					tijiao_way_flag = false; // 提交標示
				} else {
					tijiao_way = "NULL";
					tijiao_way_flag = true;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		SimpleDateFormat dat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String iDate = dat.format(new Date()).toString();
		time = iDate.substring(0, 17);
		RNO = time + mfg + shift;
		if (is_usercheck.equals("0")) {
			lineTV.setText(mline);
			floorTV.setText(floor);
		}
		RNO = RNO.replaceAll(" ", "");
		RNOTV.setText(RNO);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit:
			try {
				if (tijiao_way_flag) {
					if (checkInputOk()) {
						if (mline.length() == 0) {
							UIHelper.alertDialog(context, "線別不能為空");
						} else {
							checkLINE();
						}
					}
				} else {
					if (mline.length() == 0) {
						UIHelper.alertDialog(context, "線別不能為空");
					} else {
						checkLINE();
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
				UIHelper.ToastMessage(context, "提交失敗");
				return;
			}
			break;
		case R.id.tv_line:
			initPopuWindow(v, lineTV.getText().toString());
			break;
		case R.id.tv_floor:
			initPopuWindow(v, floorTV.getText().toString());
			break;
		case R.id.imageButton_UP:
			if (tijiao_way_layout.getVisibility() == View.GONE) {
				tijiao_way_layout.setVisibility(View.VISIBLE);
				imageButton_UP.setImageResource(R.drawable.ic_down2);
			} else {
				tijiao_way_layout.setVisibility(View.GONE);
				imageButton_UP.setImageResource(R.drawable.ic_up2);
			}
			break;
		default:
			break;
		}

	}

	private void initPopuWindow(View parent, String str) {
		if (popuWindow == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(this);
			contentView = mLayoutInflater.inflate(R.layout.popuwindow, null);
			popuWindow = new PopupWindow(contentView,
					WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.WRAP_CONTENT);
		}

		ColorDrawable cd = new ColorDrawable(0x000000);
		popuWindow.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.4f;
		getWindow().setAttributes(lp);

		popuWindow.setOutsideTouchable(true);
		popuWindow.setFocusable(true);
		popuWindow.setAnimationStyle(R.style.AppBaseTheme);
		popuWindow.showAtLocation((View) parent.getParent(), Gravity.CENTER
				| Gravity.CENTER_HORIZONTAL, 0, 0);

		popuWindow.update();
		TextView brokemate = (TextView) contentView
				.findViewById(R.id.brokemate);
		brokemate.setText(str);
		popuWindow.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
	}

	/**
	 * 判斷當前點檢編號是否點檢過
	 */
	private void checkLINE() {
		// TODO Auto-generated method stub
		if (CompleteLzs.indexOf(ToolNO) == -1) {
			if (i == 0) {
				if (!tijiao_way_flag) {
					AlertDialog alertDialog = new AlertDialog.Builder(context)
							.create();
					alertDialog.setIcon(R.drawable.ic_system);
					alertDialog.setTitle("系統提示:");
					alertDialog.setMessage("不點檢方式，點檢項目內容將不被提交");
					alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
							"確定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									popsindows();
								}
							});
					alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
							"取消", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									return;
								}
							});
					alertDialog.show();
				} else {
					popsindows();
				}
			} else {
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
							+ model.getChildIcar() + "~" + ToolNO
							+ "~a66abb5684c45962d887564f08346e8d";
				}
			}
		}
		return true;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x8090:
				isShow = true;
				pro_id = msg.obj.toString();
				popsindows();
				break;

			default:
				break;
			}
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
					start.getServerData(3, MyConstant.GET_MY_MASTER,
							user.getLogonName());
				} else if (key.equals(MyConstant.GET_MY_MASTER)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("ch_logonname", result.get(1).toString());
						map.put("ch_name", result.get(2).toString());
						map.put("ch_team", result.get(3).toString());
						list_choosemap.add(map);
						liststr2.add(result.get(1).toString());
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
					}
					start.getServerData(3, MyConstant.GET_CHECKREPORTTITLE,
							reportNum, site, mfg, sbu);
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
								model.setProId(singChild[b + 1]);
								model.setChildInputFlag(singChild[b + 2]);
								model.setChildContent("");
								model.setChildResult("0");
								childList.add(model);
							}
							childData.add(childList);
						}
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "此报表點檢項目还未配置,請聯繫課長進行配置!", 0)
								.show();
					}
					adapter = new MyExpandableAdapterT(handler, context,
							groupData, childData);
					expListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					if (is_input_order.equals("1") && !is_usercheck.equals("0")) {
						start.getServerData(0, MyConstant.GET_FLOOR_IPQC,
								user.getLogonName(), section, user.getMFG(),
								user.getBU());
					} else {
						if (childData.size() != 0) {
							getSQLlite_checkdata();
						}
					}
				} else if (key.equals(MyConstant.SAVE_CHECKREPORT)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						if (!tijiao_way_flag) {
							String totalDate = "";
							String a[] = { RNO, "NULL", "NULL", "0", "NULL",
									"NULL", "0", tijiao_way, "NULL", "0",
									user.getLogonName() };
							for (int i = 0; i < a.length; i++) {
								totalDate += a[i] + MyConstant.GET_FLAG3;
							}
							start.getServerData(0,
									MyConstant.SAVE_CHECK_BASE_INFO, totalDate);
						} else {
							Check_Result = ChangString.change(Check_Result);
							start.getServerData(0, MyConstant.SAVE_CHECKRESULT,
									Check_Result);
						}

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
						String a[] = { RNO, "NULL", "NULL", "0", "NULL",
								"NULL", "0", tijiao_way, "NULL", "0",
								user.getLogonName() };
						for (int i = 0; i < a.length; i++) {
							totalDate += a[i] + MyConstant.GET_FLAG3;
						}
						start.getServerData(0, MyConstant.SAVE_CHECK_BASE_INFO,
								totalDate);
					} else {
						UIHelper.alertDialog(context, "點檢失敗");
					}
				} else if (key.equals(MyConstant.SAVE_CHECK_BASE_INFO)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						// Toast.makeText(context, "提交PD点检基本信息成功", 0).show();
						UIHelper.ToastMessage(context, "點檢成功", 6000);
						i = 1;
						finalDb = FinalDb.create(context, "child");
						finalDb.deleteByWhere(ChildModel.class, "Logonname='"
								+ user.getLogonName() + "' and report_num='"
								+ reportNum + "' and floor_name='" + floor
								+ "' and line_name='" + mline + "'");
						ECheckActivity.this.finish();
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "提交PD点检基本信息失败", 0).show();
					}
				} else if (key.equals(MyConstant.SAVE_CHECK_PERSON)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						start.getServerData(0, MyConstant.SAVE_CHECKREPORT,
								RNO, 0 + "", reportNum, mfg, floor, mline,
								shift, user.getLogonName());
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "提交審核人失敗", 0).show();
					}
				} else if (key.equals(MyConstant.GET_PD_CHECK_NAME)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (list2.size() > 0) {
						list2.clear();
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
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
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						adapterPerson.notifyDataSetChanged();
						Toast.makeText(context, "暫無審核主管", 0).show();
					}
				} else if (key.equalsIgnoreCase(MyConstant.GET_FLOOR_IPQC)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						floorstr = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							floorstr[i - 1] = result.get(i).toString();
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無樓層信息");
						return;
					}
					spinner_floor.setAdapter(new ArrayAdapter(
							ECheckActivity.this,
							android.R.layout.simple_dropdown_item_1line,
							floorstr));
					floor = spinner_floor.getSelectedItem().toString();
					start.getServerData(3, MyConstant.GET_CHECK_LINE, user
							.getMFG(), section, floor, user.getSBU().toString());
				} else if (key.equalsIgnoreCase(MyConstant.GET_CHECK_LINE)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						linestr = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							linestr[i - 1] = result.get(i).toString();
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無線體或設備標號信息");
						return;
					}
					spinner_line.setAdapter(new ArrayAdapter(
							ECheckActivity.this,
							android.R.layout.simple_dropdown_item_1line,
							linestr));
					mline = spinner_line.getSelectedItem().toString();
					if (childData.size() != 0) {
						getSQLlite_checkdata();
					}
				} else if (key.equalsIgnoreCase(MyConstant.SUBMIT_ABNORMAL)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "提交异常成功");
					} else {
						UIHelper.ToastMessage(context, "提交异常失败");
					}
					isShow = false;
					isAbnor = false;
					bitmaps.clear();
					photoaddress.clear();
					photoName.clear();
					liststr2.clear();
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
				R.layout.activity_echeck_popwindow, null);
		final CheckBox mabnormal = (CheckBox) view
				.findViewById(R.id.abnormal_cbox);
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
		ListView chperson_listview = (ListView) view
				.findViewById(R.id.listView_alchoose_person);
		if (isShow) {
			mabnormal.setVisibility(view.VISIBLE);
			OK.setVisibility(View.INVISIBLE);
		} else {
			mabnormal.setVisibility(view.INVISIBLE);
			OK.setVisibility(View.VISIBLE);
		}
		mabnormal.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				isAbnor = isChecked;
			}
		});
		spinner_check_name
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						PDteam = arg0.getItemAtPosition(arg2).toString();
						start.getServerData(0, MyConstant.GET_PD_CHECK_NAME,
								PDteam, user.getMFG());
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		// -------------------------------------------------------//list_choosemap被选中的人;
		adapterPerson = new CheckeAdapter_eCheck(list2, context);
		personListView.setAdapter(adapterPerson);
		chooseaAdapter = new ChoosepersonAdapter(list_choosemap, context,
				liststr2);
		chperson_listview.setAdapter(chooseaAdapter);

		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		final PopupWindow window = new PopupWindow(view,
				(display.getWidth() - (display.getWidth() / 15)),
				(display.getHeight() - (display.getHeight() / 5)));

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		window.setFocusable(true); // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		// 在底部显示
		window.showAtLocation(
				ECheckActivity.this.findViewById(R.id.btn_submit),
				Gravity.BOTTOM, 0, 0);
		window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		// window.update();
		backgroundAlpha(0.3f);
		window.setAnimationStyle(R.style.POP_Animation_trans);
		window.showAtLocation(layout_e_check, Gravity.CENTER, 0, 0);
		personListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				holder = (CheckHolder) arg1.getTag();
				holder.flagCheckBox.isEnabled();
				// if (holder.flagCheckBox.isChecked()) {
				int m = 0;
				// adapterPerson.getIsSelected().put(arg2,
				// holder.flagCheckBox.isChecked());
				for (int i = 0; i < liststr2.size(); i++) {
					if (liststr2.get(i).equals(
							list2.get(arg2).get("LogonName").toString())) {
						m = 1;
					}
				}
				if (m == 0) {
					liststr2.add(list2.get(arg2).get("LogonName").toString());
					choosemap = new HashMap<String, String>();
					choosemap.put("ch_logonname",
							list2.get(arg2).get("LogonName").toString());
					choosemap.put("ch_name", list2.get(arg2).get("ChineseName")
							.toString());
					choosemap.put("ch_team", PDteam);
					list_choosemap.add(choosemap);
					chooseaAdapter.notifyDataSetChanged();
				}
			}
		});
		closePopwindow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// window.setOnDismissListener(new poponDismissListener());
				window.dismiss();
				if (isAbnor) {
					if (list_choosemap.size() > 0) {
						AbnormalPopwindow();
					}
				}
				isShow = false;
				// for (Map<String, String> list : list_choosemap)
				// {
				// Toast.makeText(context,list.get("ch_name").toString().trim(),Toast.LENGTH_LONG).show();
				// }
			}
		});
		OK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String allLines = "";
				liststr2 = chooseaAdapter.get_allperson();
				if (liststr2.size() > 0) {

					for (int i = 0; i < liststr2.size(); i++) {
						allLines += liststr2.get(i).toString().trim()
								+ MyConstant.GET_FLAG2;
					}
					personNameString = allLines;
					if (checkFlag) {
						start.getServerData(0, MyConstant.SAVE_CHECK_PERSON,
								RNO, personNameString, user.getLogonName()
										.toString().trim());
						// --------------------------------------勾选生成异常提交popwindow-----------------------------
						if (mabnormal.isChecked()) {
							AbnormalPopwindow();
						}
						checkFlag = false;
						window.dismiss();
					} else {
						Toast.makeText(context, "请不要重复提交", 0).show();
					}

				} else {
					personNameString = allLines;
					Toast.makeText(context, "您还未选择审核人，请选择...", 1000).show();
				}
			}
		});

		window.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
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

	public void QR() {
		Intent openCameraIntent = new Intent(ECheckActivity.this,
				CaptureActivity.class);
		startActivityForResult(openCameraIntent, SCANER_CODE_TWO);
	}

	public void QR1() {
		Intent openCameraIntent = new Intent(ECheckActivity.this,
				CaptureActivity.class);
		startActivityForResult(openCameraIntent, SCANER_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			if (requestCode == SCANER_CODE_TWO) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				String[] str = adapter.getLocation().split("%");
				childData.get(Integer.parseInt(str[0].toString()))
						.get(Integer.parseInt(str[1].toString()))
						.setChildContent(scanResult);
				adapter.notifyDataSetChanged();
			} else if (requestCode == SCANER_CODE) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				String[] str = adapter.getLocation().split("%");
				childData.get(Integer.parseInt(str[0].toString()))
						.get(Integer.parseInt(str[1].toString()))
						.setChildIcar(scanResult);
				adapter.notifyDataSetChanged();
			} else if (requestCode == 3) {
				Uri originalUri = data.getData(); // 获得图片的uri
				Log.i("originalUri>>>>>>>", originalUri.getPath());
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(originalUri, proj, null, null,
						null);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				photoaddress.add(cursor.getString(column_index));
				int a = cursor.getString(column_index).lastIndexOf("/");
				photoName.add(cursor.getString(column_index).substring(a + 1));
				photoName.add(GetSystemTime.GetTimeYMDhms("_") + ".jpg");
				try {
					Bitmap bm = MediaStore.Images.Media.getBitmap(
							getContentResolver(), originalUri);
					bitmaps.add(bm);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gradviewAdapter.notifyDataSetChanged();
			} else if (requestCode == 4) {
				Bitmap b = BitmapFactory.decodeFile(file.getPath());
				bitmaps.add(b);
				photoaddress.add(file.getAbsolutePath());
				photoName.add(fileName);
				gradviewAdapter.notifyDataSetChanged();
			}
		}
	}

	public void getSQLlite_checkdata() {
		try {
			finalDb = FinalDb.create(context, "child");
			List<ChildModel> child = finalDb.findAllByWhere(ChildModel.class,
					"Logonname='" + user.getLogonName() + "' and report_num='"
							+ reportNum + "' and floor_name='" + floor
							+ "' and line_name='" + mline + "'", "proId");
			for (int i = 0; i < child.size(); i++) {
				System.out.println("-" + child.get(i).toString());
			}
			if (child.size() != 0 && childData.size() != 0) {
				for (int j = 0; j < childData.size(); j++) {
					for (int j2 = 0; j2 < childData.get(j).size(); j2++) {
						for (int j3 = 0; j3 < child.size(); j3++) {
							if (childData.get(j).get(j2).getProId()
									.equals(child.get(j3).getProId())
									&& child.get(j3).getFloor_name().trim()
											.equalsIgnoreCase(floor)
									&& child.get(j3).getLine_name().trim()
											.equalsIgnoreCase(mline)) {
								childData
										.get(j)
										.get(j2)
										.setChildContent(
												child.get(j3).getChildContent());
								childData
										.get(j)
										.get(j2)
										.setChildIcar(
												child.get(j3).getChildIcar());
								childData
										.get(j)
										.get(j2)
										.setChildResult(
												child.get(j3).getChildResult());
							}
						}
					}
				}
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void saveinputcontect() {
		try {
			finalDb = FinalDb.create(context, "child"); // 创建数据库
			childData = adapter.getChildData();
			boolean delete = true;
			for (int i = 0; i < childData.size(); i++) {
				for (int j = 0; j < childData.get(i).size(); j++) {
					if (childData.get(i).get(j).getChildContent().length() != 0) {
						if (delete) {
							finalDb.deleteByWhere(ChildModel.class,
									"Logonname='" + user.getLogonName()
											+ "' and report_num='" + reportNum
											+ "' and floor_name='" + floor
											+ "' and line_name='" + mline + "'");
							delete = false;
						}
						ChildModel childmodel = new ChildModel();
						childmodel.setChildTitle(childData.get(i).get(j)
								.getChildTitle());
						childmodel.setChildInputFlag(childData.get(i).get(j)
								.getChildInputFlag());
						childmodel.setLogonname(user.getLogonName());
						childmodel.setReport_num(reportNum);
						childmodel.setProId(childData.get(i).get(j).getProId());
						childmodel.setChildResult(childData.get(i).get(j)
								.getChildResult());
						childmodel.setChildIcar(childData.get(i).get(j)
								.getChildIcar());
						childmodel.setChildContent(childData.get(i).get(j)
								.getChildContent());
						childmodel.setFloor_name(floor);
						childmodel.setLine_name(mline);
						finalDb.save(childmodel);
					}
				}
			}
			overridePendingTransition(R.anim.move_left_in_activity,
					R.anim.move_right_out_activity);
			finish();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// 返回键按钮
	public void activity_back(View v) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setIcon(R.drawable.ic_system);
		alertDialog.setTitle("系統提示:");
		alertDialog.setMessage("是否退出編輯?");
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						saveinputcontect();
					}
				});
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});
		alertDialog.show();
	}

	// 回退
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog alertDialog = new AlertDialog.Builder(context).create();
			alertDialog.setIcon(R.drawable.ic_system);
			alertDialog.setTitle("系統提示:");
			alertDialog.setMessage("是否退出編輯?");
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							saveinputcontect();
						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							return;
						}
					});
			alertDialog.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void AbnormalPopwindow() {
		final EditText editText;
		GridView gridView;
		ImageView addimageView, mtakephoto;
		Button msubmit;
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		contentView = mLayoutInflater.inflate(R.layout.popuwindow_abnormal,
				null);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		final PopupWindow popuWindow = new PopupWindow(contentView,
				(display.getWidth() - (display.getWidth() / 14)),
				(display.getHeight() - (display.getHeight() / 3)));

		addimageView = (ImageView) contentView.findViewById(R.id.addphoto);
		msubmit = (Button) contentView.findViewById(R.id.submit_bt);
		mtakephoto = (ImageView) contentView.findViewById(R.id.takephoto_bt);
		editText = (EditText) contentView.findViewById(R.id.details);
		gridView = (GridView) contentView.findViewById(R.id.abnormal_wall);
		gradviewAdapter = new GradviewAdapter(context, bitmaps);
		gridView.setAdapter(gradviewAdapter);
		ColorDrawable cd = new ColorDrawable(0x000000);
		popuWindow.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		backgroundAlpha(0.3f);
		popuWindow.setOutsideTouchable(true);
		popuWindow.setFocusable(true);
		popuWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_FROM_FOCUSABLE);
		popuWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		popuWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		popuWindow.showAtLocation(
				ECheckActivity.this.findViewById(R.id.center_layout),
				Gravity.CENTER, 0, 0);

		addimageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, 3);
			}
		});
		mtakephoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				file = new File(Environment.getExternalStorageDirectory()
						+ "/MyAbnormalPhotos");
				if (!file.exists()) {
					file.mkdirs();
				}
				fileName = GetSystemTime.GetTimeYMDhms("_") + ".jpg";
				file = new File(file, fileName);
				Uri takephotoUri = Uri.fromFile(file);
				intent2.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, takephotoUri);
				startActivityForResult(intent2, 4);
			}
		});
		msubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String whoString = "";
				String totalPhotoName = "";
				for (int i = 0; i < list_choosemap.size(); i++) {
					whoString = whoString
							+ list_choosemap.get(i).get("ch_logonname") + "!";
				}
				if (photoName.size() > 0) {
					for (int i = 0; i < photoName.size(); i++) {
						totalPhotoName = totalPhotoName + photoName.get(i)
								+ "!";
					}
				} else {
					totalPhotoName = "null";
				}
				start.getServerData(0, MyConstant.SUBMIT_ABNORMAL,
						user.getSite(), user.getBU(), user.getMFG(), mline,
						floor, user.getTeam(), reportNum, RNO, "0",
						user.getLogonName(), editText.getText().toString()
								.trim(), whoString, totalPhotoName, pro_id);
				for (int i = 0; i < photoaddress.size(); i++) {
					UploadFileTask uploadFileTask = new UploadFileTask(
							ECheckActivity.this);
					uploadFileTask.execute(photoaddress.get(i),
							photoName.get(i));
				}
				Log.i("上传数据", editText.getText().toString().trim() + "<>"
						+ whoString + "<>" + totalPhotoName);
				popuWindow.dismiss();
			}
		});
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				bitmaps.remove(arg2);
				photoaddress.remove(arg2);
				photoName.remove(arg2);
				gradviewAdapter.notifyDataSetChanged();
				return true;
			}
		});
		popuWindow.setOnDismissListener(new OnDismissListener() {
			// 在dismiss中恢复透明度
			public void onDismiss() {
				backgroundAlpha(1f);
			}
		});
	}
}
