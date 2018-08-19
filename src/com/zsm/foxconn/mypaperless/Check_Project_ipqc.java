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
import java.util.StringTokenizer;

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
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.adapter.CheckeAdapter_eCheck;
import com.zsm.foxconn.mypaperless.adapter.ChoosepersonAdapter;
import com.zsm.foxconn.mypaperless.adapter.MyExpandableAdapterIPQC;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.CheckHolder;
import com.zsm.foxconn.mypaperless.bean.ChildModelIPQC;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.bean.Wo_dateinfo;
import com.zsm.foxconn.mypaperless.help.AdialogActivity;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.help.UploadFileTask;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.GetSystemTime;
import com.zsm.qr.CaptureActivity;

/**
 * @author mgp ipqc點檢模塊 2016/01/09
 */
public class Check_Project_ipqc extends BaseActivity implements OnClickListener {
	private Context context = Check_Project_ipqc.this;
	ImageButton imageButtonUP, up2;
	private ImageButton checkp_submit_bt;
	private final int SCANER_CODE = 1, SCANER_CODE_TWO = 2;
	LinearLayout wlayout, exmalplelayout, exmalple_list, layout_report_name;
	TextView ipqccheckedtv, checkp_rno_id, checkp_line_id, checkp_checkid_id,
			checkp_model_id, checkp_piliang_id, exlist_tv, textview_wo_lot;
	EditText check_wo_id, checkp_bt_id, checkp_deviation_id, checkp_rev_id;
	Spinner beizhuSpiner, questionSpiner;
	private LinkedList<String> groupData = new LinkedList<String>(); // 一級條目數據源
	private List<LinkedList<ChildModelIPQC>> childData = new ArrayList<LinkedList<ChildModelIPQC>>(); // 二級條目數據源
	private String reportid, section, reportname, check_id, bUname, linename,
			floorName, check_idname, isaccess, issection; // 接受上個頁面的參數
	ExpandableListView expListView_check_ipqc;
	private Button scanQR;
	private String time, shiftName, reportNo, remark, reasonString; // 時間,節次名,報表ID,備註，停線原因
	private String wo = "", checkp_rev, checkp_piliang, checkp_deviation,
			checkp_model, checkp_bt;
	private String Check_Result = ""; // 存放輸入的字符串數據
	private String check_alldata = ""; // 全部需要傳遞的數據
	private MyExpandableAdapterIPQC myAdapter;
	private boolean issubmit = false, woisnull = false, isnull = false;
	int hour;
	UserBean userBean;
	HttpStart starts;
	private String k;
	private List<List<String>> wodata = new ArrayList<List<String>>();
	private static final int CODE = 0;
	String Icar = null;
	private SimpleDateFormat df;
	private FinalDb finalDb = null;
	//新增------------------------------------------------------------
	PopupWindow window =  null,popuWindow = null;
	private View contentView;
	private boolean isShow = false;
	private boolean isAbnor = false;
	private GradviewAdapter gradviewAdapter = null;
	private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
	private List<String> photoaddress = new ArrayList<String>();
	private List<String> photoName = new ArrayList<String>();
	private File file = null;
	private String fileName = null,PDteam = null;
	private String pro_id = "";
	private List<Map<String, String>> list_choosemap = new ArrayList<Map<String, String>>();
	private CheckeAdapter_eCheck adapterPerson = null;
	private ChoosepersonAdapter chooseaAdapter = null;
	List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
	List<String> liststr2 = new ArrayList<String>();
	LinearLayout root_ipqc_layout = null;
	private CheckHolder holder = new CheckHolder();
	private Map<String, String> choosemap = null;
	String personNameString = null;
	boolean checkFlag = true;
	List<String> list3 = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkproject_ipqc);
		userBean = (UserBean) getApplicationContext();
		starts = new HttpStart(context, handler);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		reportid = bundle.getString("reportid");
		section = bundle.getString("section");
		reportname = bundle.getString("reportname");
		check_id = bundle.getString("check_id");
		bUname = bundle.getString("bUname");
		isaccess = bundle.getString("isaccess");
		linename = bundle.getString("linename");
		floorName = bundle.getString("floorName");
		check_idname = bundle.getString("check_idname");
		issection = bundle.getString("issection");
		findViewById();
		CheckLogin();
		df = new SimpleDateFormat("yyyyMMddHH");
		String iDate = df.format(new Date()).toString();
		time = iDate.substring(0, 8);
		hour = Integer.parseInt(iDate.substring(8, 10));
		remark = beizhuSpiner.getItemAtPosition(0).toString(); // 獲取備註為空的值
		questionSpiner.setEnabled(false); // 停線原因默認不打開
		reasonString = "null"; // 停線原因默認提交為null
		starts.getServerData(0, MyConstant.GET_CHECK_NAME_IPQC, reportid,
				userBean.getMFG(), userBean.getSBU(), userBean.getSite(),
				userBean.getBU(), userBean.getTeam());
		initView();
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
		if (reportid.equalsIgnoreCase("IPQCOBA2016001")) {
			textview_wo_lot = (TextView) findViewById(R.id.textview_wo_lot);
			textview_wo_lot.setText("LOT:");
		}
		root_ipqc_layout = (LinearLayout) findViewById(R.id.root_ipqc_layout);
		checkp_rno_id = (TextView) findViewById(R.id.checkp_rno_id);
		checkp_line_id = (TextView) findViewById(R.id.checkp_line_id);
		checkp_checkid_id = (TextView) findViewById(R.id.checkp_checkid_id);
		checkp_model_id = (TextView) findViewById(R.id.checkp_model_id);
		checkp_rev_id = (EditText) findViewById(R.id.checkp_rev_id);
		checkp_piliang_id = (TextView) findViewById(R.id.checkp_piliang_id);
		check_wo_id = (EditText) findViewById(R.id.check_wo_id);
		checkp_bt_id = (EditText) findViewById(R.id.checkp_bt_id);
		checkp_deviation_id = (EditText) findViewById(R.id.checkp_deviation_id);
		beizhuSpiner = (Spinner) findViewById(R.id.beizhuSpiner);
		wlayout = (LinearLayout) findViewById(R.id.wlayout);
		layout_report_name = (LinearLayout) findViewById(R.id.layout_report_name);
		scanQR = (Button) findViewById(R.id.scanQR);
		exmalple_list = (LinearLayout) findViewById(R.id.exmalple_list);
		exlist_tv = (TextView) findViewById(R.id.exlist_tv);
		questionSpiner = (Spinner) findViewById(R.id.questionSpiner);
		imageButtonUP = (ImageButton) findViewById(R.id.imageButtonUP);
		up2 = (ImageButton) findViewById(R.id.up2);
		ipqccheckedtv = (TextView) findViewById(R.id.ipqccheckedtv);
		expListView_check_ipqc = (ExpandableListView) findViewById(R.id.expListView_check_ipqc);
		checkp_submit_bt = (ImageButton) findViewById(R.id.checkp_submit_bt);
		ipqccheckedtv.setText(reportname);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		int h = Integer.valueOf(check_id);
		if (h < 10) {
			check_id = "0" + h;
		}
		if (h < 6) {
			shiftName = "D";
		} else {
			shiftName = "N";
		}
		if (shiftName == "N" && hour < 8)
			time = (Integer.parseInt(time) - 1) + "";

		if (reportid.equalsIgnoreCase("IPQCOBA2016001")||reportid.equalsIgnoreCase("FQ3NMD054001A")||
				reportid.equalsIgnoreCase("FQ3NMD054002A")||reportid.equalsIgnoreCase("FQ3NMD054003A")) {
			df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String iDate = df.format(new Date()).toString();
			time = iDate.substring(0, 17);
			reportNo = time + userBean.getMFG().toString() + shiftName; // 點檢編號
		} else {
			reportNo = time + section + linename + shiftName; // 點檢編號
		}
		checkp_rno_id.setText(reportNo);
		checkp_line_id.setText(floorName + "/" + linename); // 顯示線別

		checkp_checkid_id.setText(check_idname); // 中文節次顯示
		imageButtonUP.setOnClickListener(this);
		up2.setOnClickListener(this);
		checkp_submit_bt.setOnClickListener(this);
		beizhuSpiner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				remark = beizhuSpiner.getItemAtPosition(arg2).toString(); // 獲取備註為空的值
				if (remark.equals("停線")) {
					questionSpiner.setEnabled(true);
				} else {
					questionSpiner.setEnabled(false);
					questionSpiner.setSelection(0);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

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
		questionSpiner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				reasonString = questionSpiner.getItemAtPosition(arg2)
						.toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		if (isnull) {
			check_wo_id.setEnabled(false);
			check_wo_id
					.setBackgroundResource(R.drawable.bg_edittext_bottom_text_color_nor);
		}
		TextWatcher textWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				wo = check_wo_id.getText().toString();
				if (wo.length() == 12) {
					beizhuSpiner.setEnabled(false);
					questionSpiner.setEnabled(false);
					try {
						myAdapter.getwo(wo);
						starts.getServerData(0, MyConstant.GET_WO_DATA_IPQC,
								userBean.getMFG(), wo, reportid);
					} catch (Exception e) {
						// TODO: handle exception
						UIHelper.ToastMessage(context, "輸入異常");
						return;
					}
					imageButtonUP.setImageResource(R.drawable.ic_down2);
					wlayout.setVisibility(View.VISIBLE);
				} else {
					beizhuSpiner.setEnabled(true);
					checkp_rev_id.setText("");
					checkp_piliang_id.setText("");
					checkp_deviation_id.setText("");
					checkp_model_id.setText("");
					checkp_bt_id.setText("");
					woisnull = false;
				}
			}
		};

		check_wo_id.addTextChangedListener(textWatcher);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x8090:
				isShow = true;
				pro_id = msg.obj.toString();
				popsindows();
				break;

			default:
				break;
			}
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				// 獲取ipqc工單數據
				if (key.equals(MyConstant.GET_WO_DATA_IPQC)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						if (reportid.equalsIgnoreCase("IPQCOBA2016001")||reportid.equalsIgnoreCase("FQ3NMD054001A")||
								reportid.equalsIgnoreCase("FQ3NMD054002A")||reportid.equalsIgnoreCase("FQ3NMD054003A")) {
							checkp_rev_id.setText("NO DATA");
							checkp_model_id.setText(result.get(1).toString());
							String piliang = result.get(2).toString();
							StringTokenizer st = new StringTokenizer(piliang,
									".");
							int pl = Integer.parseInt(st.nextToken());
							checkp_piliang_id.setText(Integer.toString(pl));
							checkp_deviation_id.setText("NO DATA");
							k = result.get(3).toString();
						} else {
							if (result.size() == 5) {
								checkp_model_id.setText(result.get(1)
										.toString());
								checkp_rev_id.setText(result.get(2).toString());
								checkp_piliang_id.setText(result.get(3)
										.toString());
								checkp_deviation_id.setText("NO DATA");
								k = result.get(4).toString();
							} else {
								checkp_rev_id.setText(result.get(1).toString());
								checkp_piliang_id.setText(result.get(2)
										.toString());
								checkp_deviation_id.setText(result.get(3)
										.toString());
								checkp_model_id.setText(result.get(4)
										.toString());
								k = result.get(5).toString();
							}
						}
						woisnull = true;
						starts.getServerData(0, MyConstant.GET_WO_DATA_GETBT,
								userBean.getMFG(), wo, floorName, linename);
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						AlertDialog alert = new AlertDialog.Builder(context)
								.create();
						alert.setIcon(R.drawable.ic_system);
						alert.setTitle("系統提示:");
						if (reportid.equalsIgnoreCase("IPQCOBA2016001")) {
							alert.setMessage("輸入的LOTNO號有誤");
						} else {
							alert.setMessage("輸入的WO工單號有誤");
						}
						alert.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										return;
									}
								});
						alert.show();
					}
				}
				// 根據工單獲取BT列數據
				if (key.equals(MyConstant.GET_WO_DATA_GETBT)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						checkp_bt_id.setText(result.get(1).toString());
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						checkp_bt_id.setText("NO DATA");
					}
					if (!isnull) {
						String str = "";
						for (int i = 0; i < childData.size(); i++) {
							for (int j = 0; j < childData.get(i).size(); j++) {
								String inputtypestr = childData.get(i).get(j)
										.getInputtype();
								if (inputtypestr.trim().equals("2")) {
									str += childData.get(i).get(j).getProId()
											+ MyConstant.GET_FLAG3;
								}
							}
						}
						if (str.length() != 0) {
							String pro_id = reportid + MyConstant.GET_FLAG2
									+ wo + MyConstant.GET_FLAG2
									+ userBean.getMFG() + MyConstant.GET_FLAG2
									+ str + MyConstant.GET_FLAG2 + k
									+ MyConstant.GET_FLAG2
									+ checkp_model_id.getText().toString()
									+ MyConstant.GET_FLAG2 + checkp_bt_id.getText().toString().trim()
									+ MyConstant.GET_FLAG2 + floorName
									+ MyConstant.GET_FLAG2 + linename;
							starts.getServerData(3,
									MyConstant.GET_PRO_ID_DATA_OFWO, pro_id);
						}
					}

				}
				// 獲取當前用戶的點檢項目
				if (key.equals(MyConstant.GET_CHECK_NAME_IPQC)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 4) {
							if (!groupData.contains(result.get(i).toString())) {
								groupData.add(result.get(i).toString());
							}
						}

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

						for (int k2 = 0; k2 < child.size(); k2++) {
							LinkedList<ChildModelIPQC> childList = new LinkedList<ChildModelIPQC>();
							String[] ss = child.get(k2).toString().split("%");
							for (int b = 0; b < ss.length; b += 3) {
								if (reportid.equalsIgnoreCase("IPQCOBA2016001")||reportid.equalsIgnoreCase("FQ3NMD054001A")||
										reportid.equalsIgnoreCase("FQ3NMD054002A")||reportid.equalsIgnoreCase("FQ3NMD054003A")) {
									ChildModelIPQC childModel = new ChildModelIPQC();
									childModel.setChildTitle(ss[b]);
									childModel.setProId(ss[b + 1]);
									childModel.setInputtype(ss[b + 2]);
									if (childModel.getProId().equalsIgnoreCase(
											"16")) {
										childModel.setChildContent("");
									} else {
										childModel.setChildContent("OK");
									}
									childModel.setChildResult("0");
									childList.add(childModel);
								} else {
									ChildModelIPQC childModel = new ChildModelIPQC();
									childModel.setChildTitle(ss[b]);
									childModel.setProId(ss[b + 1]);
									childModel.setInputtype(ss[b + 2]);
									childModel.setChildContent("");
									childModel.setChildResult("0");
									childList.add(childModel);
								}
							}
							childData.add(childList);
						}
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "此报表點檢項目还未配置,請聯繫組長進行配置!");
						isnull = true;
					} else {
						myAdapter = new MyExpandableAdapterIPQC(context,
								groupData, childData, userBean, reportid,
								handler);
						expListView_check_ipqc.setAdapter(myAdapter);
						getSQLlite_checkdata();
					}
				}
				// 通過工單帶出下端數據
				if (key.equals(MyConstant.GET_PRO_ID_DATA_OFWO)) {
					result = msg.getData().getStringArrayList(key);
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
									for (int k2 = 0; k2 < wodata.get(k).size() - 1; k2++) {
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
					myAdapter.notifyDataSetChanged();
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
					if (result.get(0).toString().equalsIgnoreCase("TR_SN_NULL")) {
						return;
					}
					if (result.get(0).toString().equalsIgnoreCase("P_SN_TRUE")) {
						return;
					}
					if (result.get(0).toString().equalsIgnoreCase("P_SN_NULL")) {
						UIHelper.ToastMessage(context, "輸入的SN有誤", 2000);
						return;
					}
					String[] id = myAdapter.getCoordinate().split("%");
					childData.get(Integer.valueOf(id[0]))
							.get(Integer.valueOf(id[1]))
							.setChildContent(data_ofinput);
					// myAdapter = new MyExpandableAdapterIPQC(childData);
					myAdapter.notifyDataSetChanged();
				}

				if (key.equals(MyConstant.GET_WO_OF_SN)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "無工單號");
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						check_wo_id.setText(result.get(1).toString().trim());
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
				if (key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(context, "未連接服務器", 3000);
					return;
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
			}
		}
	};

	public void returnClick(View view) {
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imageButtonUP:
			if (wlayout.getVisibility() == View.GONE) {
				imageButtonUP.setImageResource(R.drawable.ic_down2);
				wlayout.setVisibility(View.VISIBLE);
				layout_report_name.setVisibility(View.VISIBLE);
			} else {
				imageButtonUP.setImageResource(R.drawable.ic_up2);
				wlayout.setVisibility(View.GONE);
				layout_report_name.setVisibility(View.GONE);
			}
			break;
		case R.id.up2:
			if (exmalple_list.getVisibility() == View.GONE) {
				up2.setImageResource(R.drawable.ic_down2);
				exmalple_list.setVisibility(View.VISIBLE);
				exlist_tv.setText("收起列表");
			} else {
				up2.setImageResource(R.drawable.ic_up2);
				exmalple_list.setVisibility(View.GONE);
				exlist_tv.setText("展開列表");
			}

			break;

		case R.id.checkp_submit_bt:
			if (remark.length() == 0 || remark.equals("null")) {
				if (userBean.getMFG().equalsIgnoreCase("mfgvii")
						&& userBean.getTeam().equalsIgnoreCase("IE")) {
					wo = "NULL";
					checkp_rev = "NULL";
					checkp_piliang = "0";
					checkp_deviation = "NULL";
					checkp_model = "NULL";
					checkp_bt = "NULL";
					check_id = "0";
					woisnull = true;
				} else {
					wo = check_wo_id.getText().toString();
					checkp_rev = checkp_rev_id.getText().toString();
					checkp_piliang = checkp_piliang_id.getText().toString();
					checkp_deviation = checkp_deviation_id.getText().toString();
					checkp_model = checkp_model_id.getText().toString();
					checkp_bt = checkp_bt_id.getText().toString();

				}
				getalldata();
				if (woisnull) {
					if (issubmit) {
						check_alldata = reportNo + "&&" + wo + "&&" + linename
								+ "&&" + floorName + "&&" + check_id + "&&"
								+ reportid + "&&" + checkp_rev + "&&"
								+ checkp_piliang + "&&" + checkp_deviation
								+ "&&" + checkp_model + "&&" + checkp_bt + "&&"
								+ shiftName + "&&";
						System.out.println(check_alldata);
						AdialogActivity SH = new AdialogActivity(
								R.layout.checkproject_ipqc,
								Check_Project_ipqc.this, check_alldata
										+ Check_Result + "&&", 0);
						SH.intentvalue(reportid, check_id, section, reportname,
								bUname, isaccess, check_idname, linename,
								floorName, issection);
						SH.onCreate(userBean); // 開始選擇審核人
					}
				} else {
					Check_Result = "";
					check_alldata = "";
					return;
				}
			} else {
				AlertDialog alertDialog = new AlertDialog.Builder(context)
						.create();
				alertDialog.setIcon(R.drawable.ic_system);
				alertDialog.setTitle("系統提示:");
				alertDialog.setMessage("是否以" + remark + "方式進行提交?");
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								wo = check_wo_id.getText().toString();
								if (wo.length() == 0 || wo.equals(null)) {
									wo = "NULL";
									checkp_rev = "NULL";
									checkp_piliang = "NULL";
									checkp_deviation = "NULL";
									checkp_model = "NULL";
									checkp_bt = "NULL";
								}
								if (reasonString.length() == 0) {
									reasonString = "NULL";
								}
								check_alldata = reportNo + "&&" + wo + "&&"
										+ linename + "&&" + floorName + "&&"
										+ check_id + "&&" + reportid + "&&"
										+ checkp_rev + "&&" + checkp_piliang
										+ "&&" + checkp_deviation + "&&"
										+ checkp_model + "&&" + checkp_bt
										+ "&&" + shiftName + "&&";
								AdialogActivity SH = new AdialogActivity(
										R.layout.checkproject_ipqc,
										Check_Project_ipqc.this, check_alldata
												+ remark + "#" + reasonString
												+ "#&&", 1);
								SH.intentvalue(reportid, check_id, section,
										reportname, bUname, isaccess,
										check_idname, linename, floorName,
										issection);
								SH.onCreate(userBean); // 開始選擇審核人

							}
						});
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								return;
							}
						});
				alertDialog.show();

			}

			break;
		default:
			break;
		}
	}

	private void getalldata() {
		// TODO Auto-generated method stub
		int k = 0;
		try {
			childData = myAdapter.getChildData();
			groupData = myAdapter.getgroupData();
		} catch (Exception e) {
			// TODO: handle exception
			UIHelper.ToastMessage(context, "提交异常");
			return;
		}
		LinkedList<ChildModelIPQC> modelist = null;
		ChildModelIPQC model = new ChildModelIPQC();
		if (k == 0) {
			for (int i = 0; i < childData.size(); i++) {
				modelist = childData.get(i);
				for (int j = 0; j < modelist.size(); j++) {
					try {

						model = modelist.get(j);
						if ((model.getChildContent().length() == 0 || model
								.getChildContent().toString().isEmpty())
								|| (model.getChildIcar().length() == 0 || model
										.getChildIcar().toString().isEmpty())) {
							UIHelper.ToastMessage(context,
									groupData.get(i).toString() + " 大项中的 "
											+ model.getChildTitle() + "選項不能為空");
							k = 1;
							return;
						}
					} catch (Exception e) {
						// TODO: handle exception
						UIHelper.ToastMessage(context, groupData.get(i)
								.toString()
								+ " 大项中的 "
								+ model.getChildTitle()
								+ "選項不能為空");
						k = 1;
						return;
					}
				}
			}
		}
		if (k == 0) {
			if (Check_Result.length() > 0) {
				Check_Result = "";
			}
			for (int i = 0; i < childData.size(); i++) {
				modelist = childData.get(i);
				for (int j = 0; j < modelist.size(); j++) {
					model = modelist.get(j);
					System.out.println(model.getProId() + " = "
							+ model.getChildContent() + "=  "
							+ model.getChildResult() + "  ="
							+ model.getChildIcar() + "= "
							+ model.getInputtype());
					int flag = 0;
					Check_Result += check_id + "~" + reportid + "~"
							+ model.getProId() + "~" + model.getChildContent()
							+ "~" + model.getChildResult() + "~"
							+ model.getChildIcar() + "~#";
					System.out.println(Check_Result);
					issubmit = true;
				}
			}
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
						starts.getServerData(0, MyConstant.GET_PD_CHECK_NAME,
								PDteam, userBean.getMFG());
						Log.e(">>>><<<<","GET_PD_CHECK_NAME");
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
				window.showAtLocation(root_ipqc_layout, Gravity.BOTTOM, 0, 0);
				window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
				window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				
				// window.update();
				backgroundAlpha(0.3f);
				window.setAnimationStyle(R.style.POP_Animation_trans);
				
				window.showAtLocation(root_ipqc_layout, Gravity.CENTER, 0, 0);
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
//								starts.getServerData(0, MyConstant.SAVE_CHECK_PERSON,
//										RNO_textView.getText().toString(),
//										personNameString, userBean.getLogonName()
//										.toString().trim());
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
	public void QR() {
		Intent openCameraIntent = new Intent(Check_Project_ipqc.this,
				CaptureActivity.class);
		startActivityForResult(openCameraIntent, SCANER_CODE_TWO);
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
					starts.getServerData(0, MyConstant.GET_WO_OF_SN, section,
							scanResult, userBean.getMFG());
				} else {
					check_wo_id.setText(scanResult);
				}
			}
			if (requestCode == SCANER_CODE_TWO) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				String[] str = myAdapter.getLocation().split("%");
				childData.get(Integer.parseInt(str[0].toString()))
						.get(Integer.parseInt(str[1].toString()))
						.setChildContent(scanResult);
				myAdapter.notifyDataSetChanged();
			}
		}
		if (resultCode == CODE) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				Icar = bundle.getString("Icar");
				// expListAdapter.setIcar(Icar,Rgroup,Rchild);
				childData = myAdapter.getChildData();
				String[] str = myAdapter.getLocation().split("%");
				childData.get(Integer.parseInt(str[0].toString()))
						.get(Integer.parseInt(str[1].toString()))
						.setChildIcar(Icar);
				myAdapter.notifyDataSetChanged();
				// System.out.println("返回的Icar值="+Icar);
				Toast.makeText(Check_Project_ipqc.this, "返回的Icar值=" + Icar,
						Toast.LENGTH_LONG).show();
			} else {
				childData = myAdapter.getChildData();
				String[] str = myAdapter.getLocation().split("%");
				childData.get(Integer.parseInt(str[0].toString()))
						.get(Integer.parseInt(str[1].toString()))
						.setChildResult("0");
				myAdapter.notifyDataSetChanged();
			}

		}else if (requestCode == 3) {
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

	public void saveinputcontect() {
		try {
			finalDb = FinalDb.create(context, "child"); // 创建数据库
			childData = myAdapter.getChildData();
			boolean delete = true;
			for (int i = 0; i < childData.size(); i++) {
				for (int j = 0; j < childData.get(i).size(); j++) {
					if (childData.get(i).get(j).getChildContent().length() != 0) {
						if (delete) {
							finalDb.deleteByWhere(ChildModelIPQC.class,
									"Logonname='" + userBean.getLogonName()
											+ "' and report_num='" + reportid
											+ "' and floor_name='" + floorName
											+ "' and line_name='" + linename
											+ "'");
							delete = false;
						}
						ChildModelIPQC childmodel = new ChildModelIPQC();
						childmodel.setChildTitle(childData.get(i).get(j)
								.getChildTitle());
						childmodel.setInputtype(childData.get(i).get(j)
								.getInputtype());
						childmodel.setLogonname(userBean.getLogonName());
						childmodel.setReport_num(reportid);
						childmodel.setProId(childData.get(i).get(j).getProId());
						childmodel.setChildResult(childData.get(i).get(j)
								.getChildResult());
						childmodel.setChildIcar(childData.get(i).get(j)
								.getChildIcar());
						childmodel.setChildContent(childData.get(i).get(j)
								.getChildContent());
						childmodel.setFloor_name(floorName);
						childmodel.setLine_name(linename);
						finalDb.save(childmodel);
					}
				}
			}
			if (check_wo_id.getText().length() == 12) {
				finalDb.deleteByWhere(Wo_dateinfo.class, "Logonname='"
						+ userBean.getLogonName() + "' and report_num='"
						+ reportid + "' and floor_name='" + floorName
						+ "' and line_name='" + linename + "'");
				Wo_dateinfo wo_info = new Wo_dateinfo();
				wo_info.setLogonname(userBean.getLogonName());
				wo_info.setReport_num(reportid);
				wo_info.setFloor_name(floorName);
				wo_info.setLine_name(linename);
				wo_info.setWo(check_wo_id.getText().toString().trim());
				wo_info.setJizhong_num(checkp_model_id.getText().toString()
						.trim());
				wo_info.setRev(checkp_rev_id.getText().toString().trim());
				wo_info.setPiliang(Integer.parseInt(checkp_piliang_id.getText()
						.toString().trim()));
				wo_info.setSide(checkp_bt_id.getText().toString().trim());
				wo_info.setDeviation(checkp_deviation_id.getText().toString()
						.trim());
				finalDb.save(wo_info);
			}else {
				finalDb.deleteByWhere(Wo_dateinfo.class, "Logonname='"
						+ userBean.getLogonName() + "' and report_num='"
						+ reportid + "' and floor_name='" + floorName
						+ "' and line_name='" + linename + "'");
			}
			overridePendingTransition(R.anim.move_left_in_activity,
					R.anim.move_right_out_activity);
			finish();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 獲取上次點檢未提交的輸入數據
	 */
	public void getSQLlite_checkdata() {
		finalDb = FinalDb.create(context, "child");
		List<ChildModelIPQC> child = finalDb.findAllByWhere(
				ChildModelIPQC.class, "Logonname='" + userBean.getLogonName()
						+ "' and report_num='" + reportid
						+ "' and floor_name='" + floorName
						+ "' and line_name='" + linename + "'", "proId");
		List<Wo_dateinfo> childwo = finalDb.findAllByWhere(Wo_dateinfo.class,
				"Logonname='" + userBean.getLogonName() + "' and report_num='"
						+ reportid + "' and floor_name='" + floorName
						+ "' and line_name='" + linename + "'");
		if (childwo.size() != 0) {
			checkp_model_id.setText(childwo.get(0).getJizhong_num());
			checkp_rev_id.setText(childwo.get(0).getRev());
			checkp_piliang_id.setText(childwo.get(0).getPiliang() + "");
			checkp_deviation_id.setText(childwo.get(0).getDeviation());
			checkp_bt_id.setText(childwo.get(0).getSide());
			check_wo_id.setText(childwo.get(0).getWo());
		}
		if (child.size() != 0 && childData.size() != 0) {
			for (int j = 0; j < childData.size(); j++) {
				for (int j2 = 0; j2 < childData.get(j).size(); j2++) {
					for (int j3 = 0; j3 < child.size(); j3++) {
						if (childData.get(j).get(j2).getProId()
								.equals(child.get(j3).getProId())
								&& child.get(j3).getFloor_name().trim()
										.equalsIgnoreCase(floorName)
								&& child.get(j3).getLine_name().trim()
										.equalsIgnoreCase(linename)) {
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
			myAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("tag", "停止Activity");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("tag", "暫停Activity");
	}
	//新增代码----------------------------------------------------------------------------;
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
				Check_Project_ipqc.this.findViewById(R.id.root_ipqc_layout),
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
				starts.getServerData(0, MyConstant.SUBMIT_ABNORMAL,
						userBean.getSite(), userBean.getBU(), userBean.getMFG(), linename,
						floorName, userBean.getTeam(), reportid, reportNo, "0",
						userBean.getLogonName(), editText.getText().toString()
								.trim(), whoString, totalPhotoName, pro_id);
				for (int i = 0; i < photoaddress.size(); i++) {
					UploadFileTask uploadFileTask = new UploadFileTask(
							Check_Project_ipqc.this);
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
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha;
		getWindow().setAttributes(lp);
	}
}
