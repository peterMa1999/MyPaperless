package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.adapter.CommonAdapter;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.Qr_check_situation;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.qr.CaptureActivity;

/**
 * 獲取該用戶所在BU的所有點檢報表
 * 
 * @author mgp 2015/12/26
 */
public class Choose_report extends BaseActivity {
	private Context context = Choose_report.this;
	HttpStart start = null;
	private TextView head_title;
	private GridView info;
	private String reporttitles[] = null;
	private String reportflag, reportid, reportName, bUname, Flag, check_id,
			check_idname;
	private List<Picture> data = new ArrayList<Picture>();
	private CommonAdapter<Picture> adapter;
	private UserBean user;
	String isaccess;
	private String section = "", rpt = "", issection = "";
	private Intent intent = null;
	// private Timelistenresiger tiemTimelistenresiger;
	private final int SCANER_CODE = 1;
	private String scan[] = null;
	private String floorName, reportNum, lineName, site, mfg, sbu,
			is_input_order;
	private ImageButton head_next;
	private String[] passcheckline = null;
	private String whether_check, check_name;
	private PopupWindow popuWindow;
	private View contentView;
	private List<Qr_check_situation> list_qrcheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_report);
		user = (UserBean) getApplicationContext();
		CheckLogin();
		// tiemTimelistenresiger = new Timelistenresiger(context,user);
		// tiemTimelistenresiger.alter();
		head_title = (TextView) findViewById(R.id.head_title);
		head_title.setText("點檢報表");
		Intent intent = getIntent();
		bUname = intent.getStringExtra("bUname");
		isaccess = intent.getStringExtra("isaccess");
		section = intent.getStringExtra("section");
		issection = intent.getStringExtra("issection");
		head_next = (ImageButton) findViewById(R.id.head_next);
		head_title = (TextView) findViewById(R.id.head_title);
		if (isaccess.equals("1")) {
			head_title.setText("選擇配置報表");
			head_next.setVisibility(View.GONE);
		} else if (isaccess.equals("0")) {
			if (user.getBU().toString().equalsIgnoreCase(bUname)) {
				head_next.setVisibility(View.VISIBLE);
			} else {
				head_next.setVisibility(View.GONE);
			}
			head_title.setText("點檢報表");
		} else {
			head_title.setText("點檢報表");
			head_next.setVisibility(View.GONE);
		}
		start = new HttpStart(context, handler);

		findViewById();
		initView();
		try {
			start.getServerData(3, MyConstant.GET_CHECK_ALL_REPORT_NAME, user
					.getMFG().toString(), user.getTeam().toString(), user
					.getSite().toString(), user.getBU(), section, bUname);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		if (user.getLogonName() == null || user.getLogonName().length() == 0) {
			android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// tiemTimelistenresiger.stopthread();
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
		head_next.setImageResource(R.drawable.erweima_click_seletor);
		head_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openCameraIntent = new Intent(context,
						CaptureActivity.class);
				startActivityForResult(openCameraIntent, SCANER_CODE);
				UIHelper.ToastMessage(context, "掃碼點檢");
			}
		});
		info = (GridView) findViewById(R.id.report_info);
		adapter = new CommonAdapter<Choose_report.Picture>(context, data,
				R.layout.reportgive) {
			@Override
			public void convert(
					com.zsm.foxconn.mypaperless.adapter.ViewHolder holder,
					Picture t) {
				// TODO Auto-generated method stub
				if (t.getReportflag().equals("0")) {
					holder.setImageResource(R.id.reportimage,
							R.drawable.im_check_dark);
					holder.getConvertView().setVisibility(View.VISIBLE);
				} else {
					holder.setImageResource(R.id.reportimage,
							R.drawable.im_check_bright);
					holder.getConvertView().setVisibility(View.VISIBLE);
				}
				holder.setText(R.id.reporttitle, t.getReportName());
			}
		};
		info.setAdapter(adapter);

		info.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				reportid = data.get(arg2).getReportId();
				reportflag = data.get(arg2).getReportflag();
				reportName = data.get(arg2).getReportName();
				if (reportflag.equals("0")) {
					intent = new Intent(context,
							Muban_Configuration_report.class);
					intent.putExtra("reportid", reportid);
					intent.putExtra("bUname", bUname);
					intent.putExtra("isaccess", isaccess);
					intent.putExtra("reportName", reportName);
					intent.putExtra("section", section);
					intent.putExtra("issection", issection);
					startActivity(intent);
					return;
				} else {
					if (issection.equals("4")) {
						if (reportid.equalsIgnoreCase("FM3NCD034003A")) {
							if (user.getTeam().equalsIgnoreCase("Process")
									|| user.getTeam().equalsIgnoreCase("ME")) {
								intent = new Intent(context,
										Canshu_Manage.class);
								intent.putExtra("reportid", reportid);
								intent.putExtra("bUname", bUname);
								intent.putExtra("isaccess", isaccess);
								intent.putExtra("reportName", reportName);
								intent.putExtra("section", section);
								intent.putExtra("issection", issection);
								startActivity(intent);
							} else {
								UIHelper.ToastMessage(context, "您暫無權限");
							}
						} else if (reportid.equalsIgnoreCase("FM3NCD034003B")) {
							if (user.getTeam().equalsIgnoreCase("Process")) {
								intent = new Intent(context,
										Canshu_Manage.class);
								intent.putExtra("reportid", reportid);
								intent.putExtra("bUname", bUname);
								intent.putExtra("isaccess", isaccess);
								intent.putExtra("reportName", reportName);
								intent.putExtra("section", section);
								intent.putExtra("issection", issection);
								startActivity(intent);
							} else {
								UIHelper.ToastMessage(context, "您暫無權限");
							}
						}else if (reportid.equalsIgnoreCase("SOP-NN-2005157")) {
							if (!user.getUserLevel().equals("0")) {
								intent = new Intent(context,
										Canshu_Manage_bofenghan.class);
								intent.putExtra("reportid", reportid);
								intent.putExtra("bUname", bUname);
								intent.putExtra("isaccess", isaccess);
								intent.putExtra("reportName", reportName);
								intent.putExtra("section", section);
								intent.putExtra("issection", issection);
								startActivity(intent);
							} else {
								UIHelper.ToastMessage(context, "您暫無權限");
							}
						} else {
							UIHelper.ToastMessage(context, "此報表暫無需管理參數");
						}
						return;
					} else if(issection.equals("3")){
						start.getServerData(3,
								MyConstant.GET_CHECK_REPORT_SECTION_ANME,
								reportid);
						
					}else {
							if (isaccess.equals("1")) {
								intent = new Intent(context,
										Configuration_report.class);
								start.getServerData(3,
										MyConstant.GET_CHECK_REPORT_SECTION_ANME,
										reportid);
							} else {
								// intent = new Intent(context,
								// Choose_line_ipqc.class);
								Intent openCameraIntent = new Intent(context,
										CaptureActivity.class);
								startActivityForResult(openCameraIntent,
										SCANER_CODE);
								return;
							}
					}

				}
			}
		});
		info.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				reportid = data.get(arg2).getReportId();
				reportflag = data.get(arg2).getReportflag();
				Flag = user.getUserLevel().toString();
				if (reportflag.equals("0")
						|| (Flag.equals("0") || Flag.equals("1"))) {
					UIHelper.ToastMessage(context, "您暫無此權限");
				} else {
					AlertDialog alertDialog = new AlertDialog.Builder(context)
							.create();
					alertDialog.setIcon(R.drawable.ic_system);
					alertDialog.setTitle("警告:");
					alertDialog.setMessage("删除此表單,是否繼續?");
					alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
							"確定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									AlertDialog alertDialog = new AlertDialog.Builder(
											context).create();
									alertDialog.setTitle("警告:");
									alertDialog.setMessage("將刪除此表單的全部點檢項目?");
									alertDialog
											.setButton(
													DialogInterface.BUTTON_NEGATIVE,
													"確定",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {

															start.getServerData(
																	0,
																	MyConstant.DELETE_REPORT,
																	reportid,
																	user.getMFG(),
																	user.getSite(),
																	user.getBU());

														}
													});
									alertDialog
											.setButton(
													DialogInterface.BUTTON_POSITIVE,
													"取消",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															return;
														}
													});
									alertDialog.show();
								}
							});
					alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
							"取消", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							});
					alertDialog.show();
				}
				return true;
			}
		});

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.equals(MyConstant.GET_CHECK_ALL_REPORT_NAME)) {
					// 數據遍歷
					result = msg.getData().getStringArrayList(key);
					if (data.size() > 0) {
						data.clear();
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL_DETAIL)) {
						UIHelper.ToastMessage(context, "暫無需要點檢的報表", 5000);
						return;
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 3) {
							Picture pc = new Picture();
							pc.setReportId(result.get(i).toString());
							pc.setReportName(result.get(i + 1).toString());
							pc.setReportflag(result.get(i + 2).toString());
							data.add(pc);
						}
					}
					adapter.notifyDataSetChanged();

				}
				if (key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					Toast.makeText(context, "未连接服务器....", 0).show();
				}
				if (key.equals(MyConstant.GET_CHECK_REPORT_SECTION_ANME)) {
					// 數據遍歷
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						if (!result.get(1).toString()
								.equalsIgnoreCase(user.getBU())) {
							section = result.get(1).toString();
						}
						if (issection.equals("2")) {
							intent.putExtra("bUname", bUname);
							intent.putExtra("isaccess", isaccess);
							intent.putExtra("issection", issection);
							intent.putExtra("section", section);
							intent.putExtra("reportid", reportid);
						}else if (issection.equals("3")) {
							is_input_order = result.get(2).toString();
							intent = new Intent(context, Floor_manage.class);
							intent.putExtra("bUname", bUname);
							intent.putExtra("isaccess", isaccess);
							intent.putExtra("issection", issection);
							intent.putExtra("section", section);
						}
						startActivity(intent);
						overridePendingTransition(
								R.anim.move_right_in_activity,
								R.anim.move_left_out_activity);
					}
				}
				if (key.equals(MyConstant.GET_ALERT_TIME)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						check_id = result.get(1).toString();
						check_idname = result.get(4).toString();
					}
					intent = new Intent(context, Showchooseline_ipqc.class);
					intent.putExtra("reportid", reportNum);
					intent.putExtra("section", section);
					intent.putExtra("reportname", reportName);
					intent.putExtra("check_id", check_id);
					intent.putExtra("bUname", bUname);
					intent.putExtra("isaccess", isaccess);
					intent.putExtra("check_idname", check_idname);
					intent.putExtra("issection", issection);
					startActivity(intent);
				}

				if (key.equals(MyConstant.GET_QRCODEID)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						if (result.size() > 14) {
							list_qrcheck = new ArrayList<Qr_check_situation>();
							for (int i = 1; i < result.size(); i += 12) {
								Qr_check_situation check_situation;
								String str = "";
								str = result.get(i + 2).toString();
								if (result.get(i + 11).equals(
										"Has been checked")) {
									check_situation = new Qr_check_situation(
											result.get(i).toString().trim(),
											str, result.get(i + 3).toString()
													.trim(), result.get(i + 4)
													.toString().trim(), result
													.get(i + 5).toString()
													.trim(), result.get(i + 6)
													.toString().trim(), result
													.get(i + 7).toString()
													.trim(), result.get(i + 8)
													.toString().trim(), result
													.get(i + 9).toString()
													.trim(), result.get(i + 11)
													.toString().trim(), result
													.get(i + 12).toString()
													.trim());
									i = i + 1;
								} else {
									check_situation = new Qr_check_situation(
											result.get(i).toString().trim(),
											str, result.get(i + 3).toString()
													.trim(), result.get(i + 4)
													.toString().trim(), result
													.get(i + 5).toString()
													.trim(), result.get(i + 6)
													.toString().trim(), result
													.get(i + 7).toString()
													.trim(), result.get(i + 8)
													.toString().trim(), result
													.get(i + 9).toString()
													.trim(), result.get(i + 11)
													.toString().trim(), "");
								}
								list_qrcheck.add(check_situation);
							}
							initPopuWindow(list_qrcheck);
						} else {
							floorName = result.get(1).toString();
							lineName = result.get(3).toString();
							reportNum = result.get(4).toString();
							site = result.get(5).toString();
							mfg = result.get(6).toString();
							sbu = result.get(7).toString();
							rpt = result.get(8).toString();
							section = result.get(9).toString();
							is_input_order = result.get(10).toString();
							whether_check = result.get(12).toString();
							if (user.getSite().trim().toString()
									.equalsIgnoreCase(site)
									&& user.getMFG().trim().toString()
											.equalsIgnoreCase(mfg)
									&& (user.getSBU().trim().toString()
											.equalsIgnoreCase(sbu) || sbu
											.equalsIgnoreCase("all"))) {
								if (whether_check.equals("Has been checked")) { // 表示當前時間段內已經被點檢
									check_name = result.get(13).toString();
									UIHelper.ToastMessage(context, "當前時間段內已被"
											+ check_name + "點檢");
									return;
								} else {
									if (is_input_order.equals("1")
											|| is_input_order.equals("2")) {
										intent = new Intent(context,
												ECheckActivity.class);
										intent.putExtra("site", site);
										intent.putExtra("mfg", mfg);
										intent.putExtra("sbu", sbu);
										intent.putExtra("floorName", floorName);
										intent.putExtra("linename", lineName);
										intent.putExtra("is_input_order",
												is_input_order);
										intent.putExtra("is_usercheck", "0"); // 掃碼標示
										intent.putExtra("reportid", reportNum);
										intent.putExtra("reportname", rpt);
										intent.putExtra("reportName", rpt);
										startActivity(intent);
									} else {
										if (user.getTeam().toString()
												.equalsIgnoreCase("IPQC")) {
											// start.getServerData(
											// 0,
											// MyConstant.GET_CHECK_LINE_PASS,
											// reportNum, user.getMFG());
											start.getServerData(
													0,
													MyConstant.INSERT_SELECTED_LINE,
													user.getMFG(), reportNum,
													floorName, lineName,
													user.getLogonName());
										} else {
											intent = new Intent(context,
													CheckPdActivity.class);
											intent.putExtra("reportName", rpt);
											intent.putExtra("report_Num",
													reportNum);
											intent.putExtra("selectLine",
													lineName);
											intent.putExtra("section", section);
											intent.putExtra("flagfloor",
													floorName);
											intent.putExtra("issection",
													issection);
											startActivity(intent);
										}

									}
								}
							} else {
								UIHelper.ToastMessage(context, "您無權限點檢此報表");
							}

						}
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "二維碼信息錯誤");
						return;
					}
				}

				if (key.equals(MyConstant.INSERT_SELECTED_LINE)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString().equals("TO BE SELECTED")) {
						if (result.get(1).equalsIgnoreCase(
								user.getLogonName().toString().trim())) {
							start.getServerData(0, MyConstant.GET_ALERT_TIME);
						} else {
							UIHelper.ToastMessage(context, "此" + lineName
									+ "線體已經被" + result.get(2) + "勾選");
						}
					}
					if (result.get(0).toString().equals("ADD SUCCESS")) {
						start.getServerData(0, MyConstant.GET_ALERT_TIME);
					}
					if (result.get(0).toString().equals("ADD FAIL")) {
						UIHelper.ToastMessage(context, "添加線別異常");
					}

				}

				if (key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					Toast.makeText(context, "未连接服务器....", 0).show();
				}
				if (key.equals(MyConstant.DELETE_REPORT)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						Toast.makeText(context, "删除报表成功", 0).show();
						try {
							start.getServerData(3,
									MyConstant.GET_CHECK_ALL_REPORT_NAME, user
											.getMFG().toString(), user
											.getTeam().toString(), user
											.getSite().toString(), bUname,
									section);
						} catch (Exception e) {
							// TODO: handle exception
						}
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "删除报表失败...", 0).show();
					}
				}
			}
		};
	};

	private void initPopuWindow(final List<Qr_check_situation> list) {
		String[] arr1 = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr1[i] = list.get(i).getRpt() + "(" + list.get(i).getReportNum()
					+ ")-" + list.get(i).getMfg();
		}
		if (popuWindow == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(this);
			contentView = mLayoutInflater.inflate(
					R.layout.popuwindow_chooserpt, null);
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
		popuWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		popuWindow.update();
		ListView listview_chooserpt = (ListView) contentView
				.findViewById(R.id.listview_chooserpt);
		ArrayAdapter<String> adpter = new ArrayAdapter<String>(this,
				R.layout.popuwindow_chooserpt_item, arr1);
		listview_chooserpt.setAdapter(adpter);
		listview_chooserpt.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				floorName = list.get(arg2).getFloorName();
				lineName = list.get(arg2).getLineName();
				reportNum = list.get(arg2).getReportNum();
				site = list.get(arg2).getSite();
				mfg = list.get(arg2).getMfg();
				sbu = list.get(arg2).getSbu();
				rpt = list.get(arg2).getRpt();
				section = list.get(arg2).getSection();
				is_input_order = list.get(arg2).getIs_input_order();
				whether_check = list.get(arg2).getWhether_check();
				if (user.getSite().trim().toString().equalsIgnoreCase(site)
						&& user.getMFG().trim().toString()
								.equalsIgnoreCase(mfg)
						&& (user.getSBU().trim().toString()
								.equalsIgnoreCase(sbu) || sbu
								.equalsIgnoreCase("all"))) {
					if (whether_check.equals("Has been checked")) { // 表示當前時間段內已經被點檢
						check_name = list.get(arg2).getCheck_name();
						UIHelper.ToastMessage(context, "當前時間段內已被" + check_name
								+ "點檢");
						return;
					} else {
						if (is_input_order.equals("1")
								|| is_input_order.equals("2")) {
							intent = new Intent(context, ECheckActivity.class);
							intent.putExtra("site", site);
							intent.putExtra("mfg", mfg);
							intent.putExtra("sbu", sbu);
							intent.putExtra("floorName", floorName);
							intent.putExtra("linename", lineName);
							intent.putExtra("is_input_order", is_input_order);
							intent.putExtra("is_usercheck", "0"); // 掃碼標示
							intent.putExtra("reportid", reportNum);
							intent.putExtra("reportname", rpt);
							intent.putExtra("reportName", rpt);
							startActivity(intent);
						} else {
							if (user.getTeam().toString().equalsIgnoreCase("IPQC")) {
								// start.getServerData(0,
								// MyConstant.GET_CHECK_LINE_PASS,
								// reportNum,user.getMFG());
								start.getServerData(0,
										MyConstant.INSERT_SELECTED_LINE,
										user.getMFG(), reportNum, floorName,
										lineName, user.getLogonName());
							} else {
								intent = new Intent(context,
										CheckPdActivity.class);
								intent.putExtra("reportName", rpt);
								intent.putExtra("report_Num", reportNum);
								intent.putExtra("selectLine", lineName);
								intent.putExtra("section", section);
								intent.putExtra("flagfloor", floorName);
								intent.putExtra("issection", issection);
								startActivity(intent);
							}

						}
					}
					popuWindow.dismiss();
				} else {
					UIHelper.ToastMessage(context, "您無權限點檢此報表");
				}
			}
		});
		popuWindow.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
	}

	class Picture {
		public String reportId;
		public String reportName;
		private String reportflag;

		public String getReportId() {
			return reportId;
		}

		public String getReportflag() {
			return reportflag;
		}

		public void setReportflag(String reportflag) {
			this.reportflag = reportflag;
		}

		public void setReportId(String reportId) {
			this.reportId = reportId;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

	}

	public void HeadBack(View view) {
		// Intent intent = new Intent(this, Choose_section.class);
		// tiemTimelistenresiger.stopthread();
		// intent.putExtra("bUname", bUname);
		// intent.putExtra("isaccess", isaccess);
		// intent.putExtra("issection", issection);
		// startActivity(intent);
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// Intent intent = new Intent(this, Choose_section.class);
			// tiemTimelistenresiger.stopthread();
			// intent.putExtra("bUname", bUname);
			// intent.putExtra("isaccess", isaccess);
			// intent.putExtra("issection", issection);
			// issection = intent.getStringExtra("issection");
			// startActivity(intent);
			overridePendingTransition(R.anim.move_left_in_activity,
					R.anim.move_right_out_activity);
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			if (requestCode == SCANER_CODE) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result").trim();
				// scan = scanResult.split("-");
				// start.getServerData(3, MyConstant.GET_MFGVII_QRCODE_ID,
				// scan[0].toString());
				start.getServerData(3, MyConstant.GET_QRCODEID, scanResult);
			}
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// start.getServerData(3, MyConstant.GET_CHECK_ALL_REPORT_NAME, user
		// .getMFG().toString(), user.getTeam().toString(), user
		// .getSite().toString(), user.getBU(), section, bUname);
	}
}
