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
import java.util.regex.Pattern;

import net.tsz.afinal.FinalDb;
import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.adapter.CheckeAdapter_eCheck;
import com.zsm.foxconn.mypaperless.adapter.ChoosepersonAdapter;
import com.zsm.foxconn.mypaperless.adapter.MyExpandableAdapter;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.CheckHolder;
import com.zsm.foxconn.mypaperless.bean.ChildModel;
import com.zsm.foxconn.mypaperless.bean.ChildModelIPQC;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.bean.Wo_dateinfo;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.help.UploadFileTask;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.GetSystemTime;
import com.zsm.qr.CaptureActivity;

public class CheckPdActivity extends BaseActivity {
	private ImageButton imageButtonUP, imageButtonUP2, submitbutton;
	private Button scanQR;
	private Spinner beizhuSpinner, questionSpinner;
	private Context context = CheckPdActivity.this;
	private CheckHolder holder = new CheckHolder();
	private LinearLayout wlayout, exmalplelayout, root_checkPD;
	private EditText edit_wo, Devation_editText, sample_editText;
	private TextView RNO_textView, lines_textView, MODEL_textView,
			REV_textView, mutil_textView, head_title, tv_bt_sample;
	private UserBean userBean;
	private RadioGroup radioGroup1;
	private CheckeAdapter_eCheck adapterPerson = null;
	private ChoosepersonAdapter chooseaAdapter = null;
	private String reportName, shift, RNO, reportNum, ToolNO, mline, floor;
	HttpStart start = null;
	List<String> list = new ArrayList<String>();
	List<String> list3 = new ArrayList<String>();
	List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
	List<String> liststr2 = new ArrayList<String>();
	ExpandableListView expListView;
	LinkedList<String> groupData = new LinkedList<String>();
	LinkedList<String> groupData2 = new LinkedList<String>();
	private List<LinkedList<ChildModel>> childData = new ArrayList<LinkedList<ChildModel>>();
	MyExpandableAdapter adapter;
	private final int SCANER_CODE = 1, SCANER_CODE_TWO = 2;
	private String[] passpz = null;
	private String report_Num = null;
	private boolean isShow = false;
	private boolean isAbnor = false;
	private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
	private List<String> photoaddress = new ArrayList<String>();
	private List<String> photoName = new ArrayList<String>();
	private GradviewAdapter gradviewAdapter = null;
	// private String reportName = null;
	private String selectLine = null;
	private String flagfloor = null;
	List<List<Object>> saveData = null;
	private String section = null;
	private String issection = null, PDteam = null;
	private String[] singChild;
	String Check_Result = "";
	private String beizhuString, questionString, radioString;
	String personNameString = null;
	private boolean isOk = false, isnull = true, checkFlag = true;
	private boolean submitType = false;// 正常点检提交为TRUE,提交备注原因为false
	private List<List<String>> wodata = new ArrayList<List<String>>();
	private FinalDb finalDb = null;
	private String smt_kaihuan = "0"; // SMT開換線標示
	private Map<String, String> choosemap = null;
	private List<Map<String, String>> list_choosemap = new ArrayList<Map<String, String>>();
	private View contentView;
	private File file = null;
	private String fileName = null;
	private String pro_id = "";
	PopupWindow window =  null,popuWindow = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_check_pd);
		userBean = (UserBean) getApplicationContext();
		start = new HttpStart(context, handler);
		Intent intent = getIntent();
		report_Num = intent.getStringExtra("report_Num").toString().trim();
		reportName = intent.getStringExtra("reportName").toString().trim();
		selectLine = intent.getStringExtra("selectLine").toString().trim();
		flagfloor = intent.getStringExtra("flagfloor").toString();
		section = intent.getStringExtra("section").toString();
		issection = intent.getStringExtra("issection");
		CheckLogin();
		findViewById();
		start.getServerData(3, MyConstant.GET_CHECK_REPORT_TITLE, report_Num,
				userBean.getSite().toString().trim(), userBean.getMFG()
						.toString().trim(), userBean.getSBU().toString().trim());
		initView();

	}

	@Override
	protected void findViewById() {
		imageButtonUP = (ImageButton) findViewById(R.id.up);
		imageButtonUP2 = (ImageButton) findViewById(R.id.up2);
		wlayout = (LinearLayout) findViewById(R.id.wlayout);
		root_checkPD = (LinearLayout) findViewById(R.id.root_checkPD);
		exmalplelayout = (LinearLayout) findViewById(R.id.exmalple_list);
		RNO_textView = (TextView) findViewById(R.id.RNO_textView);
		edit_wo = (EditText) findViewById(R.id.edit_wo);
		lines_textView = (TextView) findViewById(R.id.lines_textView);
		REV_textView = (TextView) findViewById(R.id.REV_textView);
		mutil_textView = (TextView) findViewById(R.id.mutil_textView);
		MODEL_textView = (TextView) findViewById(R.id.MODEL_textView);
		Devation_editText = (EditText) findViewById(R.id.Devation_editText);
		beizhuSpinner = (Spinner) findViewById(R.id.beizhuSpiner);
		questionSpinner = (Spinner) findViewById(R.id.questionSpiner);
		submitbutton = (ImageButton) findViewById(R.id.head_next);
		radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		scanQR = (Button) findViewById(R.id.scanQR);
		expListView = (ExpandableListView) findViewById(R.id.laptop_list);
		submitbutton.setImageResource(R.drawable.submit_click_seletor3);
		edit_wo.addTextChangedListener(textWatcher);
		tv_bt_sample = (TextView) findViewById(R.id.tv_bt_sample);
		sample_editText = (EditText) findViewById(R.id.sample_editText);
		if (report_Num.equalsIgnoreCase("FM3NCD034003A")
				|| report_Num.equalsIgnoreCase("FM3NCD034003B")) {
			tv_bt_sample.setText("面別:");
			sample_editText.setText("");
		}
		head_title = (TextView) findViewById(R.id.head_title);
		head_title.setText("點檢");
	}

	@Override
	protected void initView() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String iDate = df.format(new Date()).toString();
		String time = iDate.substring(0, 17);
		RNO_textView.setText(time.toString().trim() + section);
		adapter = new MyExpandableAdapter(context, groupData, childData,
				userBean, report_Num, handler);
		expListView.setAdapter(adapter);
		lines_textView.setText(flagfloor + "/" + selectLine.toString().trim());
		imageButtonUP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (wlayout.getVisibility() == View.GONE) {
					imageButtonUP.setImageResource(R.drawable.ic_down2);
					wlayout.setVisibility(View.VISIBLE);
				} else {
					imageButtonUP.setImageResource(R.drawable.ic_up2);
					wlayout.setVisibility(View.GONE);
				}

			}
		});
		imageButtonUP2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (exmalplelayout.getVisibility() == View.VISIBLE) {
					imageButtonUP2.setImageResource(R.drawable.ic_up2);
					exmalplelayout.setVisibility(View.GONE);
				} else {
					imageButtonUP2.setImageResource(R.drawable.ic_down2);
					exmalplelayout.setVisibility(View.VISIBLE);
				}
			}
		});
		beizhuString = beizhuSpinner.getItemAtPosition(0).toString().trim();
		beizhuSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == 0) {
					isOk = false;
				} else if (arg2 == 1) {
					isOk = true;
					questionSpinner
							.setBackgroundResource(R.drawable.nt_wangdianditu_cbx_bg);
					questionSpinner.setClickable(true);

				} else {
					isOk = true;
					questionString = questionSpinner.getItemAtPosition(0)
							.toString().trim();
					questionSpinner.setSelection(0);
					questionSpinner
							.setBackgroundResource(R.drawable.nt_wangdianditu_cbx_bg_unclic);
					questionSpinner.setClickable(false);
				}
				beizhuString = arg0.getItemAtPosition(arg2).toString();
				if (report_Num.equalsIgnoreCase("FD3NMD219001A")) {
					smt_kaihuan = arg2 + "";
					if (arg2 == 4 || arg2 == 5) {
						if (edit_wo.getText().toString().length() == 12) {
							start.getServerData(0,
									MyConstant.CHECKED_BASEINFO_PD, edit_wo
											.getText().toString().trim(),
									userBean.getMFG().toString(), report_Num);
						}
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		questionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				questionString = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		scanQR.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开扫描界面扫描条形码或二维码
				Intent openCameraIntent = new Intent(context,
						CaptureActivity.class);
				startActivityForResult(openCameraIntent, SCANER_CODE);
			}
		});

		// expListView.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		// CheckViewHolder mHolder = (CheckViewHolder) arg1.getTag();
		// // mHolder.qr_bt.findViewById(R.id.qr_bt);
		// mHolder.qr_bt.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// // 打开扫描界面扫描条形码或二维码
		// Intent openCameraIntent = new Intent(context,
		// CaptureActivity.class);
		// startActivityForResult(openCameraIntent, SCANER_CODE);
		// }
		// });
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
	}

	public void QR() {
		Intent openCameraIntent = new Intent(CheckPdActivity.this,
				CaptureActivity.class);
		startActivityForResult(openCameraIntent, SCANER_CODE_TWO);
	}

	private boolean checkInputOk() {
		saveData = new ArrayList<List<Object>>(); // 需存儲到數據庫的數據
		childData = adapter.getChildData();
		List<ChildModel> result1 = null;
		ChildModel model;
		Check_Result = "";
		if (Check_Result.length() > 0) {
			Check_Result = "";
		}
		for (int i = 0; i < childData.size(); i++) {
			result1 = childData.get(i);
			for (int j = 0; j < result1.size(); j++) {
				model = result1.get(j);
				if ((model.getChildContent().length() == 0 || model
						.getChildContent().toString().isEmpty())) {
					UIHelper.alertDialog(context, model.getChildTitle()
							+ "選項不能為空");
					return false;
				} else if ((report_Num.equalsIgnoreCase("FM3NCD034003A") || report_Num
						.equalsIgnoreCase("FM3NCD034003B"))
						&& (model.getChildIcar().length() == 0 || model
								.getChildIcar().toString().isEmpty())) {
					UIHelper.alertDialog(context, model.getChildTitle()
							+ "參數實際值不能為空");
					return false;
				}else {
					if ((report_Num.equalsIgnoreCase("FM3NCD034003A") || report_Num
							.equalsIgnoreCase("FM3NCD034003B"))
							&& (model.getChildIcar().length() != 0 && !model
									.getChildIcar().toString().isEmpty())) {
						int x = model.getChildContent().trim().length();
						Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
						 try {
							 if (model.getChildContent().trim().contains("+/-")) {
								 	int m = model.getChildContent().trim().indexOf("+");
								 	int n = model.getChildContent().trim().indexOf("-");
									float a = Float.valueOf(model.getChildContent().trim().substring(0, m));
									float b;
									if (model.getChildContent().trim().substring(n+1, x).contains(";")) {
										b = Float.valueOf(model.getChildContent().trim().substring(n+1, x-1));	
									}else {
										b = Float.valueOf(model.getChildContent().trim().substring(n+1, x));
									}
									float c = Float.valueOf(model.getChildIcar().toString().trim());
									if (a-b>c||c>a+b) {
										UIHelper.alertDialog(context, model.getChildTitle()
												+ "實際參數不在設定範圍內，請修正");
										return false;
									}
								}else if (model.getChildContent().trim().contains("~")) {
								 	int w = model.getChildContent().trim().indexOf("~");
									float a = Float.valueOf(model.getChildContent().trim().substring(0, w));
									float b;
									if (model.getChildContent().trim().substring(w+1, x).contains(";")) {
										b = Float.valueOf(model.getChildContent().trim().substring(w+1, x-1));	
									}else {
										b = Float.valueOf(model.getChildContent().trim().substring(w+1, x));
									}
									float c = Float.valueOf(model.getChildIcar().toString().trim());
									if (a>c||c>b) {
										UIHelper.alertDialog(context, model.getChildTitle()
												+ "實際參數不在設定範圍內，請修正");
										return false;
									}
								}else if (model.getChildContent().trim().contains("or")) {
									boolean vsor = false;
									String[] str = model.getChildContent().trim().split("or");
									for (int k = 0; k < str.length; k++) {
										if (str[k].contains(";")) {
											str[k] = str[k].replace(";", "");
										}
										if (str[k].equalsIgnoreCase(model.getChildIcar().toString().trim())) {
											vsor = true;
										}
									}
									if (!vsor) {
										UIHelper.alertDialog(context, model.getChildTitle()
												+ "實際參數不在設定範圍內，請修正");
										return false;
									}
								}else if (pattern.matcher(model.getChildContent().trim().substring(0, x-1)).matches()){
									float a = Float.valueOf(model.getChildContent().trim().substring(0, x-1));
									float c = Float.valueOf(model.getChildIcar().toString().trim());
									if (a!=c) {
										UIHelper.alertDialog(context, model.getChildTitle()
												+ "實際參數與設定參數不匹配，請修正");
										return false;
									}
								}
						} catch (Exception e) {
							// TODO: handle exception
							UIHelper.alertDialog(context, model.getChildTitle()
									+ "參數格式錯誤或實際參數不是數字，請修正");
							return false;
						}
					}
					Check_Result += RNO_textView.getText().toString()
							+ MyConstant.GET_FLAG1 + report_Num
							+ MyConstant.GET_FLAG1 + model.getProId()
							+ MyConstant.GET_FLAG1 + model.getChildContent()
							+ MyConstant.GET_FLAG1 + model.getChildResult()
							+ MyConstant.GET_FLAG1 + "N/A"
							+ MyConstant.GET_FLAG1 + "N/A"
							+ "&&a66abb5684c45962d887564f08346e8d";
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
				if (key.equals(MyConstant.CHECKED_BASEINFO_PD)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						if (list.size() > 0) {
							list.clear();
						}
						for (int i = 1; i < result.size(); i++) {
							list.add(result.get(i));
						}
						REV_textView.setText(list.get(0));
						mutil_textView.setText(list.get(1));
						Devation_editText.setText(list.get(2));
						MODEL_textView.setText(list.get(3));
						isOk = true;
						if (report_Num.equalsIgnoreCase("FM3NCD034003A")
								|| report_Num.equalsIgnoreCase("FM3NCD034003B")
								|| report_Num.equalsIgnoreCase("FD3NMD219001A")) {
							start.getServerData(0,
									MyConstant.GET_WO_DATA_GETBT, userBean
											.getMFG(), edit_wo.getText()
											.toString(), flagfloor, selectLine);
						} else {
							if (isnull) {
								String str = "";
								for (int i = 0; i < childData.size(); i++) {
									for (int j = 0; j < childData.get(i).size(); j++) {
										String inputtypestr = childData.get(i)
												.get(j).getChildInputFlag()
												.trim();
										if (inputtypestr.equals("2")) {
											str += childData.get(i).get(j)
													.getProId()
													+ MyConstant.GET_FLAG3;
										}
									}
								}
								if (str.length() != 0) {
									String pro_id = report_Num
											+ MyConstant.GET_FLAG2
											+ edit_wo.getText().toString()
											+ MyConstant.GET_FLAG2
											+ userBean.getMFG()
											+ MyConstant.GET_FLAG2
											+ flagfloor
											+ MyConstant.GET_FLAG2
											+ selectLine
											+ MyConstant.GET_FLAG2
											+ MODEL_textView.getText()
													.toString()
											+ MyConstant.GET_FLAG2 + "NA"
											+ MyConstant.GET_FLAG2 + str
											+ MyConstant.GET_FLAG2;
									start.getServerData(3,
											MyConstant.GET_PRO_ID_DATA_OFWO,
											pro_id);
								}
							}
						}

					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "无此工单或工单输入有误...", 0).show();
					}

				}
				// 根據工單獲取BT列數據
				if (key.equals(MyConstant.GET_WO_DATA_GETBT)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						sample_editText.setText(result.get(1).toString());
						String str = "";
						for (int i = 0; i < childData.size(); i++) {
							for (int j = 0; j < childData.get(i).size(); j++) {
								String inputtypestr = childData.get(i).get(j)
										.getChildInputFlag();
								if (inputtypestr.equals("2")) {
									str += childData.get(i).get(j).getProId()
											+ MyConstant.GET_FLAG3;
								}
							}
						}
						if (str.length() != 0) {
							String pro_id = "";
							if (report_Num.equalsIgnoreCase("FD3NMD219001A")) {
								pro_id = report_Num + MyConstant.GET_FLAG2
										+ edit_wo.getText().toString()
										+ MyConstant.GET_FLAG2
										+ userBean.getMFG()
										+ MyConstant.GET_FLAG2 + flagfloor
										+ MyConstant.GET_FLAG2 + selectLine
										+ MyConstant.GET_FLAG2
										+ MODEL_textView.getText().toString()
										+ MyConstant.GET_FLAG2
										+ sample_editText.getText().toString()
										+ MyConstant.GET_FLAG2 + str
										+ MyConstant.GET_FLAG2 + smt_kaihuan
										+ MyConstant.GET_FLAG2;
							} else {
								pro_id = report_Num + MyConstant.GET_FLAG2
										+ edit_wo.getText().toString()
										+ MyConstant.GET_FLAG2
										+ userBean.getMFG()
										+ MyConstant.GET_FLAG2 + flagfloor
										+ MyConstant.GET_FLAG2 + selectLine
										+ MyConstant.GET_FLAG2
										+ MODEL_textView.getText().toString()
										+ MyConstant.GET_FLAG2
										+ sample_editText.getText().toString()
										+ MyConstant.GET_FLAG2 + str
										+ MyConstant.GET_FLAG2;
							}
							start.getServerData(3,
									MyConstant.GET_PRO_ID_DATA_OFWO, pro_id);
						}
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						sample_editText.setText("NO DATA");
						UIHelper.ToastMessage(context, "該工單不是在線工單", 15000);
					}
				}
				// 通過工單帶出下端數據
				if (key.equals(MyConstant.GET_PRO_ID_DATA_OFWO)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equalsIgnoreCase("CANCHU_ISEXIT")) {
						UIHelper.ToastMessage(context, "暫無詳細參數", 15000);
					} else {
						List<String> list = new ArrayList<String>();
						for (int i = 1; i < result.size(); i++) {
							if (result.get(i).toString()
									.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)
									|| result
											.get(i)
											.toString()
											.equalsIgnoreCase(
													MyConstant.GET_FLAG_NULL)) {
								wodata.add(list);
								list = new ArrayList<String>();
								continue;
							} else {
								list.add(result.get(i).toString());
							}
							if (i == result.size() - 1) {
								wodata.add(list);
							}
						}
						for (int j = 0; j < childData.size(); j++) {
							for (int j2 = 0; j2 < childData.get(j).size(); j2++) {
								for (int k = 0; k < wodata.size(); k++) {
									if (childData
											.get(j)
											.get(j2)
											.getProId()
											.equalsIgnoreCase(
													wodata.get(k)
															.get(wodata.get(k)
																	.size() - 1))) {
										String content = "";
										for (int k2 = 0; k2 < wodata.get(k)
												.size() - 1; k2++) {
											content += wodata.get(k).get(k2)
													.toString()
													+ ";";
										}
										childData.get(j).get(j2)
												.setChildContent(content);
									}
								}
							}
						}
						// myAdapter = new MyExpandableAdapterIPQC(childData);
						adapter.notifyDataSetChanged();
					}

				}
				// 根據關鍵字帶數據
				if (key.equals(MyConstant.GET_DATA_OF_INPUTTYPE)) {
					result = msg.getData().getStringArrayList(key);
					String data_ofinput = "";
					if (data_ofinput.length() > 0) {
						data_ofinput = "";
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						data_ofinput = result.get(1).toString();
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i++) {
							data_ofinput += result.get(i).toString() + ";";
						}
					}

					String[] id = adapter.getLocation().split("%");
					childData.get(Integer.valueOf(id[0]))
							.get(Integer.valueOf(id[1]))
							.setChildContent(data_ofinput);
					// myAdapter = new MyExpandableAdapterIPQC(childData);
					adapter.notifyDataSetChanged();
				}
				if (key.equals(MyConstant.GET_CHECK_REPORT_TITLE)) //
				{
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
								model.setChildIcar("N/A");
								childList.add(model);
							}
							childData.add(childList);
						}
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "此报表點檢項目还未配置,請聯繫組長進行配置!", 0)
								.show();
						isnull = false;
						return;
					}
					adapter.notifyDataSetChanged();
					getSQLlite_checkdata();
					start.getServerData(3, MyConstant.GET_MY_MASTER,
							userBean.getLogonName());
				}
				if (key.equals(MyConstant.GET_MY_MASTER)) {
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
				}
				if (key.equals(MyConstant.SAVE_CHECK_BASE_INFO)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						// Toast.makeText(context, "提交PD点检基本信息成功", 0).show();
						start.getServerData(0, MyConstant.SAVE_CHECK_REPORT,
								RNO_textView.getText().toString(), report_Num,
								userBean.getMFG(), flagfloor, selectLine,
								userBean.getLogonName());
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "提交PD点检基本信息失败", 0).show();
					}
				}
				if (key.equals(MyConstant.SAVE_CHECK_RESULT)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						Toast.makeText(context, "点检成功", 5000).show();
						deleteLine();
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "点检失败...", 0).show();
					}
				}
				if (key.equals(MyConstant.SAVE_CHECK_REPORT)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						// Toast.makeText(context, "保存PD点检报表成功", 0).show();
						if (submitType) {
							String str = ChangString.change(Check_Result);
							start.getServerData(0,
									MyConstant.SAVE_CHECK_RESULT, str);
						} else {
							deleteLine();
						}
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "保存PD点检报表失败", 0).show();
					}
				}
				if (key.equals(MyConstant.GET_PD_CHECK_NAME)) {
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
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "获取审核人失败...", 0).show();
					}
				}
				if (key.equals(MyConstant.SAVE_CHECK_PERSON)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						// Toast.makeText(context,"提交审核人成功",1000).show();
						for (int i = 0; i < radioGroup1.getChildCount(); i++) {
							RadioButton radioButton = (RadioButton) radioGroup1
									.getChildAt(i);
							if (radioButton.isChecked()) {
								switch (i + 1) {
								case 1:
									radioString = "1";
									break;
								case 2:
									radioString = "2";
									break;
								case 3:
									radioString = "3";
									break;
								case 4:
									radioString = "4";
									break;
								default:
									break;
								}
							}
						}
						String totalDate = "";
						if (submitType) {
							String a[] = { RNO_textView.getText().toString(),
									edit_wo.getText().toString(),
									MODEL_textView.getText().toString(),
									mutil_textView.getText().toString(),
									REV_textView.getText().toString(),
									Devation_editText.getText().toString(),
									"1", beizhuString, "N/A", radioString,
									userBean.getLogonName(),
									sample_editText.getText().toString() };

							for (int i = 0; i < a.length; i++) {
								totalDate += a[i] + MyConstant.GET_FLAG3;
							}
						} else {
							String b[] = { RNO_textView.getText().toString(),
									"N/A", "N/A", "0", "N/A", "N/A", "0",
									beizhuString, questionString, "0",
									userBean.getLogonName() };
							for (int i = 0; i < b.length; i++) {
								totalDate += b[i] + MyConstant.GET_FLAG3;
							}
						}
						// 保存PD点检基本信息;
						start.getServerData(0, MyConstant.SAVE_CHECK_BASE_INFO,
								totalDate);
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "提交审核人失败", 1000).show();
					}
				}

				if (key.equals(MyConstant.GET_WO_OF_SN)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "無工單號");
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						edit_wo.setText(result.get(1).toString());
					}
				}
				if (key.equalsIgnoreCase(MyConstant.SUBMIT_ABNORMAL)) {
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
					list_choosemap.clear();
					liststr2.clear();
				}
			}
		}
	};

	public void HeadBack(View view) {
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			if (requestCode == SCANER_CODE) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				if (scanResult.length() == 11) {
					start.getServerData(0, MyConstant.GET_WO_OF_SN, section,
							scanResult, userBean.getMFG());
				} else {
					edit_wo.setText(scanResult);
				}
			}
			if (requestCode == SCANER_CODE_TWO) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				String[] str = adapter.getLocation().split("%");
				childData.get(Integer.parseInt(str[0].toString()))
						.get(Integer.parseInt(str[1].toString()))
						.setChildContent(scanResult);
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

	private TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			HttpStart start2 = new HttpStart(context, handler);
			if (edit_wo.getText().toString().length() > 0) {
				imageButtonUP.setImageResource(R.drawable.ic_down2);
				wlayout.setVisibility(View.VISIBLE);
			}
			if (edit_wo.getText().toString().length() == 12) {
				start2.getServerData(0, MyConstant.CHECKED_BASEINFO_PD, edit_wo
						.getText().toString().trim(), userBean.getMFG()
						.toString(), report_Num);
				beizhuString = beizhuSpinner.getItemAtPosition(0).toString()
						.trim();
				questionString = questionSpinner.getItemAtPosition(0)
						.toString().trim();
				if (!report_Num.equalsIgnoreCase("FD3NMD219001A")) {
					beizhuSpinner.setSelection(0);
					questionSpinner.setSelection(0);
					beizhuSpinner.setClickable(false);
					beizhuSpinner
							.setBackgroundResource(R.drawable.nt_wangdianditu_cbx_bg_unclic);
					questionSpinner.setClickable(false);
					isOk = false;
					questionSpinner
							.setBackgroundResource(R.drawable.nt_wangdianditu_cbx_bg_unclic);
				}
			} else {
				REV_textView.setText("");
				mutil_textView.setText("");
				Devation_editText.setText("");
				MODEL_textView.setText("");
				sample_editText.setText("");
				isOk = false;
				beizhuSpinner.setClickable(true);
				beizhuSpinner
						.setBackgroundResource(R.drawable.nt_wangdianditu_cbx_bg);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

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

	// 选择点检人;
	private void popsindows() {
			if (window!= null) {
				if (window.isShowing()) {
					window.dismiss();
					return;
				}
			}
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
			mabnormal.setVisibility(view.INVISIBLE);
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
							PDteam, userBean.getMFG());
				}
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			adapterPerson = new CheckeAdapter_eCheck(list2, context);
			personListView.setAdapter(adapterPerson);
			chooseaAdapter = new ChoosepersonAdapter(list_choosemap, context,
					liststr2);
			chperson_listview.setAdapter(chooseaAdapter);
			
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			window = new PopupWindow(view,
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
			window.showAtLocation(root_checkPD, Gravity.BOTTOM, 0, 0);
			window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			
			// window.update();
			backgroundAlpha(0.3f);
			window.setAnimationStyle(R.style.POP_Animation_trans);
			
			window.showAtLocation(root_checkPD, Gravity.CENTER, 0, 0);
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
									RNO_textView.getText().toString(),
									personNameString, userBean.getLogonName()
									.toString().trim());
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

	public void HeadNext(View view) {
		if (!isOk) {
			UIHelper.ToastMessage(context, "請輸入工單或者工站異常情況");
			return;
		} else {
			if (edit_wo.length() == 12) {
				if (checkInputOk()) {
					submitType = true;
					popsindows();
				}
			} else {
				submitType = false;
				popsindows();
			}
		}
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		userBean = (UserBean) getApplicationContext();
		if (userBean.getLogonName() == null) {
			UIHelper.ToastMessage(context, "登陆异常,请重新登陆...");
			openActivity(LoginActivity.class);
			AppManager.getInstance().AppExit(context);
		}
	}

	public void deleteLine() {

		start.getServerData(0, MyConstant.DELETE_LINE_PD, userBean
				.getLogonName().toString().trim(),
				selectLine.toString().trim(), report_Num);
		finalDb = FinalDb.create(context, "child"); // 创建数据库
		finalDb.deleteByWhere(Wo_dateinfo.class,
				"Logonname='" + userBean.getLogonName() + "' and report_num='"
						+ report_Num + "' and floor_name='" + flagfloor
						+ "' and line_name='" + selectLine + "'");
		finalDb.deleteByWhere(ChildModelIPQC.class,
				"Logonname='" + userBean.getLogonName() + "' and report_num='"
						+ report_Num + "' and floor_name='" + flagfloor
						+ "' and line_name='" + selectLine + "'");
		Intent intent = new Intent();
		intent.putExtra("report_Num", report_Num);
		setResult(1, intent);
		AppManager.getInstance().killActivity(this);
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
							finalDb.deleteByWhere(ChildModelIPQC.class,
									"Logonname='" + userBean.getLogonName()
											+ "' and report_num='" + report_Num
											+ "' and floor_name='" + flagfloor
											+ "' and line_name='" + selectLine
											+ "'");
							delete = false;
						}
						ChildModelIPQC childmodel = new ChildModelIPQC();
						childmodel.setChildTitle(childData.get(i).get(j)
								.getChildTitle());
						childmodel.setLogonname(userBean.getLogonName());
						childmodel.setReport_num(report_Num);
						childmodel.setProId(childData.get(i).get(j).getProId());
						childmodel.setChildResult(childData.get(i).get(j)
								.getChildResult());
						childmodel.setChildIcar(childData.get(i).get(j)
								.getChildIcar());
						childmodel.setChildContent(childData.get(i).get(j)
								.getChildContent());
						childmodel.setFloor_name(flagfloor);
						childmodel.setLine_name(selectLine);
						finalDb.save(childmodel);
					}
				}
			}
			if (edit_wo.getText().length() == 12) {
				finalDb.deleteByWhere(Wo_dateinfo.class, "Logonname='"
						+ userBean.getLogonName() + "' and report_num='"
						+ report_Num + "' and floor_name='" + flagfloor
						+ "' and line_name='" + selectLine + "'");
				Wo_dateinfo wo_info = new Wo_dateinfo();
				wo_info.setLogonname(userBean.getLogonName());
				wo_info.setReport_num(report_Num);
				wo_info.setFloor_name(flagfloor);
				wo_info.setLine_name(selectLine);
				wo_info.setWo(edit_wo.getText().toString().trim());
				wo_info.setJizhong_num(MODEL_textView.getText().toString()
						.trim());
				wo_info.setRev(REV_textView.getText().toString().trim());
				wo_info.setPiliang(Integer.parseInt(mutil_textView.getText()
						.toString().trim()));
				wo_info.setSide(sample_editText.getText().toString().trim());
				wo_info.setDeviation(Devation_editText.getText().toString()
						.trim());
				finalDb.save(wo_info);
			} else {
				finalDb.deleteByWhere(Wo_dateinfo.class, "Logonname='"
						+ userBean.getLogonName() + "' and report_num='"
						+ report_Num + "' and floor_name='" + flagfloor
						+ "' and line_name='" + selectLine + "'");
			}
			overridePendingTransition(R.anim.move_left_in_activity,
					R.anim.move_right_out_activity);
			finish();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void getSQLlite_checkdata() {
		finalDb = FinalDb.create(context, "child");
		List<ChildModelIPQC> child = finalDb.findAllByWhere(
				ChildModelIPQC.class, "Logonname='" + userBean.getLogonName()
						+ "' and report_num='" + report_Num
						+ "' and floor_name='" + flagfloor
						+ "' and line_name='" + selectLine + "'", "proId");
		List<Wo_dateinfo> childwo = finalDb.findAllByWhere(Wo_dateinfo.class,
				"Logonname='" + userBean.getLogonName() + "' and report_num='"
						+ report_Num + "' and floor_name='" + flagfloor
						+ "' and line_name='" + selectLine + "'");
		if (childwo.size() != 0) {
			MODEL_textView.setText(childwo.get(0).getJizhong_num());
			REV_textView.setText(childwo.get(0).getRev());
			mutil_textView.setText(childwo.get(0).getPiliang() + "");
			Devation_editText.setText(childwo.get(0).getDeviation());
			sample_editText.setText(childwo.get(0).getSide());
			edit_wo.setText(childwo.get(0).getWo());
		}
		if (child.size() != 0 && childData.size() != 0) {
			for (int j = 0; j < childData.size(); j++) {
				for (int j2 = 0; j2 < childData.get(j).size(); j2++) {
					for (int j3 = 0; j3 < child.size(); j3++) {
						if (childData.get(j).get(j2).getProId()
								.equals(child.get(j3).getProId())
								&& child.get(j3).getFloor_name().trim()
										.equalsIgnoreCase(flagfloor)
								&& child.get(j3).getLine_name().trim()
										.equalsIgnoreCase(selectLine)) {
							childData
									.get(j)
									.get(j2)
									.setChildContent(
											child.get(j3).getChildContent());
							childData.get(j).get(j2)
									.setChildIcar(child.get(j3).getChildIcar());
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
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
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
		popuWindow = new PopupWindow(contentView,
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
		
		popuWindow.showAtLocation(root_checkPD, Gravity.CENTER, 0, 0);

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
						userBean.getSite(), userBean.getBU(),
						userBean.getMFG(), mline, floor, userBean.getTeam(),
						reportNum, RNO, "0", userBean.getLogonName(), editText
								.getText().toString().trim(), whoString,
						totalPhotoName, pro_id);
				for (int i = 0; i < photoaddress.size(); i++) {
					UploadFileTask uploadFileTask = new UploadFileTask(
							CheckPdActivity.this);
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
	}
}
