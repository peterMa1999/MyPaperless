package com.zsm.foxconn.mypaperless;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.Create_floorQR.MyAdapter.ViewHolder_QR;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.Picture;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.qr.EncodingHandler;

public class Create_floorQR extends BaseActivity implements OnClickListener {
	private Context context = Create_floorQR.this;
	private Intent intent;
	private String section, issection, bUname, isaccess, reportnum, reportName,sbu;
	private UserBean userBean;
	private List<Map<String, Object>> list;
	private static List<Map<String, Object>> yixuan_list = new ArrayList<Map<String, Object>>();
	private String[] datalist;
	private Boolean isdatalist = false;
	private ListView qr_listview_floor_info;
	private LinearLayout layout_create_qr,layout_xuanze_sbu;
	private ImageView imageview_add;
	private Button create_bt;
	private CheckBox quanxuan_cb;
	private HttpStart httpStart;
	private MyAdapter myadpter;
	private boolean isfirst = false;
	private Spinner choosereport_sp,choosesbu_sp;
	private TextView alertdialog_tv;
	private String[] reportstr = null,sbustr = null;
	private ArrayList<Picture> dataspinn = new ArrayList<Picture>();
	public static final File FILE_SDCARD = Environment
			.getExternalStorageDirectory(); // 检测SD卡
	public static final String IMAGE_PATH = "Mypaperless-photo"; // 文件夾名稱
	public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
	public static final File FILE_PIC_MYQRCODE = new File(FILE_LOCAL,
			"images/myQRcode"); // 照片路徑
	private static String QRCONDENAME = "";
	private static Map<Integer,Object> isselected = new HashMap<Integer, Object>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_floorqr);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		findViewById();
		httpStart = new HttpStart(context, handler);
		intent = getIntent();
		section = intent.getStringExtra("section");
		issection = intent.getStringExtra("issection");
		bUname = intent.getStringExtra("bUname");
		isaccess = intent.getStringExtra("isaccess");
		httpStart.getServerData(3, MyConstant.GET_MANAGE_FLOOR,
				userBean.getMFG(), section,userBean.getSBU().toString());
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = msg.getData().getStringArrayList(key);
				if (key.equalsIgnoreCase(MyConstant.GET_MANAGE_FLOOR)) {
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						datalist = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							datalist[i - 1] = result.get(i);
						}
					}
					if (result.get(0).equals(MyConstant.GET_FLAG_NULL)) {
						datalist = new String[result.size()];
						datalist[0] = "null";
					}
					isdatalist = true;
					Showdatalist();
				}

				if (key.equals(MyConstant.GET_CHECK_ALL_REPORT_NAME)) {
					result = msg.getData().getStringArrayList(key);
					if (dataspinn.size() > 0) {
						dataspinn.clear();
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL_DETAIL)
							|| result.get(0).toString()
									.equals(MyConstant.GET_FLAG_NULL)) {
						Toast.makeText(context, "暫無可查詢的報表", 5000).show();
						return;
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 3) {
							Picture spin = new Picture(
									result.get(i).toString(), result.get(i + 1)
											.toString(), result.get(i + 2)
											.toString());
							// 如果有权限就添加进去
							if (!(result.get(i + 2).toString().equals("0"))) {
								dataspinn.add(spin);
							}
						}
					}
					reportstr = new String[dataspinn.size()];
					if (dataspinn.size() == 0) {
						UIHelper.ToastMessage(context, "暫無數據");
						return;
					} else {
						for (int i = 0; i < dataspinn.size(); i++) {
							reportstr[i] = dataspinn.get(i).getReportName();
						}
						ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
								context,
								android.R.layout.simple_dropdown_item_1line,
								reportstr);
						choosereport_sp.setAdapter(arrayAdapter);
						reportnum = dataspinn.get(0).getReportId();// 默认第一个报表编号
						reportName = dataspinn.get(0).getReportName();// 默认第一个报表名
					}
					
					httpStart.getServerData(0, MyConstant.GET_PEIZHI_SBU,userBean.getBU(),userBean.getMFG());
				}
				
				if (key.equals(MyConstant.GET_PEIZHI_SBU)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						sbustr = new String[result.size()-1];
						for (int i = 1; i < result.size(); i++) {
							sbustr[i-1] = result.get(i).toString();
						}
					}
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						sbustr = new String[result.size()];
						sbustr[0] = MyConstant.GET_FLAG_NULL;
					}
					choosesbu_sp.setAdapter(new ArrayAdapter(
							Create_floorQR.this,
							android.R.layout.simple_dropdown_item_1line,
							sbustr));
					sbu = choosesbu_sp.getItemAtPosition(0).toString();// 默认第一个报表名
					}
				
				if (key.equalsIgnoreCase(MyConstant.UPLOAD_PICTURE_INFO)) {
					if (result.get(0).equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "圖片信息上傳異常");
					}
				}
				if (key.equalsIgnoreCase(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(context, "未連接服務器");
				}
			}
		}
	};

	public void Showdatalist() {
		list = new ArrayList<Map<String, Object>>();// 集合
		if (isdatalist) {
			if (datalist[0].toString().equalsIgnoreCase("null")
					|| datalist[0].toString().equals(null)) {
				UIHelper.ToastMessage(context, "暂无数据");
				return;
			} else {
				// 遍历
				for (int i = 0; i < datalist.length - 1; i += 3) {

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("floor_name", datalist[i].toString());
					map.put("line_name", datalist[i + 1].toString());
					map.put("sbu_name", datalist[i + 2].toString());
					map.put("ischeck", false);
					list.add(map);
				}
			}
		}
		// 适配器
		myadpter = new MyAdapter(
				context,
				list,
				R.layout.create_floorqr_item,
				new String[] { "sbu_name", "floor_name", "line_name", "ischeck" },
				new int[] { R.id.tv_sbu, R.id.tv_floor, R.id.tv_line });
		qr_listview_floor_info.setAdapter(myadpter);
		qr_listview_floor_info
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						ViewHolder_QR holder = (ViewHolder_QR) arg1.getTag();
						holder.qr_cb.toggle();// 在每次获取点击的item时改变checkbox的状态
						if (holder.qr_cb.isChecked() == true) {
							isselected.put(arg2,true);
							yixuan_list.add(list.get(arg2));
							Log.i("TAG", "--" + list.toString());
							Log.i("TAG", "---" + yixuan_list.toString());
						} else {
							yixuan_list.remove(list.get(arg2));
							isselected.put(arg2,false);
						}
					}
				});

	}

	public static class MyAdapter extends BaseAdapter {

		private Context context = null;
		private LayoutInflater inflater = null;
		private List<Map<String, Object>> list = null;

		public class ViewHolder_QR {
			public TextView qr_sbu_name = null;
			public TextView qr_floor_name = null;
			public TextView qr_line_name = null;
			public CheckBox qr_cb = null;
		}

		public MyAdapter(Context context, List<Map<String, Object>> list,
				int resource, String[] from, int[] to) {
			this.context = context;
			this.list = list;
			inflater = LayoutInflater.from(context);
			for (int i = 0; i < list.size(); i++) {
				isselected.put(i, false);
			}
		}

		public void allchoose(boolean bl) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("ischeck", bl);
				isselected.put(i, bl);
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder_QR holder = null;
			if (view == null) {
				holder = new ViewHolder_QR();
				view = inflater.inflate(R.layout.create_floorqr_item, null);
				holder.qr_sbu_name = (TextView) view
						.findViewById(R.id.qr_tv_sbu);
				holder.qr_floor_name = (TextView) view
						.findViewById(R.id.qr_tv_floor);
				holder.qr_line_name = (TextView) view
						.findViewById(R.id.qr_tv_line);
				holder.qr_cb = (CheckBox) view
						.findViewById(R.id.checkbox_floor_item);
				view.setTag(holder);
			} else {
				holder = (ViewHolder_QR) view.getTag();
			}
			holder.qr_sbu_name.setText(list.get(position).get("sbu_name")
					.toString());
			holder.qr_floor_name.setText(list.get(position).get("floor_name")
					.toString());
			holder.qr_line_name.setText(list.get(position).get("line_name")
					.toString());
			holder.qr_cb
					.setChecked((Boolean) list.get(position).get("ischeck"));
			 if((Boolean) isselected.get(position)==true){
				 holder.qr_cb.setChecked(true);
			 }else{
				 holder.qr_cb.setChecked(false);
			 }
			return view;
		}
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		if (userBean.getLogonName() == null
				|| userBean.getLogonName().length() == 0) {
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

	// 返回键按钮
	public void activity_back(View v) {
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// intent.putExtra("bUname", bUname);
			// intent.putExtra("issection", issection);
			// startActivity(intent);
			overridePendingTransition(R.anim.move_left_in_activity,
					R.anim.move_right_out_activity);
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		if (yixuan_list.size() > 0) {
			yixuan_list.clear();
		}
		TextView title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(R.string.creat_qr);
		imageview_add = (ImageView) findViewById(R.id.imageview_add);
		imageview_add.setVisibility(View.GONE);
		create_bt = (Button) findViewById(R.id.create_bt);
		qr_listview_floor_info = (ListView) findViewById(R.id.qr_listview_floor_info);
		layout_create_qr = (LinearLayout) findViewById(R.id.layout_create_qr);
		quanxuan_cb = (CheckBox) findViewById(R.id.quanxuan_cb);
		create_bt.setOnClickListener(this);
		quanxuan_cb.setOnClickListener(this);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.create_bt:
			if (yixuan_list.size() == 0) {
				UIHelper.ToastMessage(context, "未選擇" + "");
			} else {
				Log.i("TAG", "-" + yixuan_list.toString());
				httpStart.getServerData(3,
						MyConstant.GET_CHECK_ALL_REPORT_NAME, userBean.getMFG()
								.toString(), userBean.getTeam().toString(),
						userBean.getSite().toString(), userBean.getBU(),
						section, userBean.getBU().toString());
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.alertdialog_item, null);
				final AlertDialog alert = new AlertDialog.Builder(this,
						AlertDialog.THEME_HOLO_LIGHT).create();
				alert.setView(layout);
				layout_xuanze_sbu = (LinearLayout) layout.findViewById(R.id.layout_xuanze_sbu);
				layout_xuanze_sbu.setVisibility(View.VISIBLE);
				choosesbu_sp = (Spinner) layout.findViewById(R.id.choosesbu_sp);
				choosereport_sp = (Spinner) layout
						.findViewById(R.id.choosesbu_spinner);
				alertdialog_tv = (TextView) layout
						.findViewById(R.id.alertdialog_tv);
				alertdialog_tv.setText("選擇報表:");
				alert.setCanceledOnTouchOutside(true);
				alert.setCancelable(true);
				alert.setIcon(R.drawable.ic_system);
				alert.setTitle("系統提示:");
				alert.setMessage("已選擇"+yixuan_list.size()+"條，請選擇生成二維碼的報表");
				choosereport_sp
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								if (isfirst) {
									isfirst = true;
								} else {
									reportnum = dataspinn.get(arg2)
											.getReportId();
									reportName = dataspinn.get(arg2)
											.getReportName();
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});
				choosesbu_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (isfirst) {
							isfirst = true;
						}else {
							sbu = arg0.getItemAtPosition(arg2).toString();
						}
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								UIHelper.ToastMessage(context, "" + reportnum
										+ "-" + reportName);
								if (reportnum.equals(null)
										|| reportName.equals(null)
										|| reportnum.length() == 0
										|| reportName.length() == 0) {
									alert.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
									return;
								} else {

									try {
										File filePath = FILE_PIC_MYQRCODE;
										if (!filePath.exists()) {
											filePath.mkdirs(); // 如果文件夹不存在,则重新创建
										}
										for (int i = 0; i < yixuan_list.size(); i++) {
											SimpleDateFormat df = new SimpleDateFormat(
													"yyyyMMddhhmmssSSS");
											String iDate = df
													.format(new Date())
													.toString();
											Bitmap mBitmap = BitmapFactory.decodeResource(
													context.getResources(),
													R.drawable.logo);
											Bitmap qrCodeBitmap = EncodingHandler
													.createQRCode(iDate + i,
															280,280,mBitmap);
											QRCONDENAME = reportName
													+ "-"
													+ yixuan_list.get(i)
															.get("floor_name")
															.toString()
													+ "-"
													+ yixuan_list.get(i)
															.get("line_name")
															.toString()
													+ "-"
													+ sbu
													+ ".png";
											File f = new File(filePath,
													QRCONDENAME);
											FileOutputStream fOut = null;
											try {
												fOut = new FileOutputStream(f);
											} catch (FileNotFoundException e) {
												e.printStackTrace();
											}
											qrCodeBitmap.compress(
													Bitmap.CompressFormat.PNG,
													100, fOut);
											try {
												fOut.flush();
											} catch (IOException e) {
												e.printStackTrace();
											}
											try {
												fOut.close();
											} catch (IOException e) {
												e.printStackTrace();
											}
											// UIHelper.ToastMessage(context,
											// "正在上傳第" + i + "張圖片");
//											UploadFileTask uploadFileTask = new UploadFileTask(
//													Create_floorQR.this);
//											uploadFileTask.execute(f
//													.getAbsolutePath());
											httpStart
													.getServerData(
															3,
															MyConstant.UPLOAD_PICTURE_INFO,
															userBean.getSite(),
															userBean.getBU(),
															userBean.getMFG(),
															sbu,
															yixuan_list
																	.get(i)
																	.get("floor_name")
																	.toString(),
																	yixuan_list
																	.get(i)
																	.get("line_name")
																	.toString(),
															yixuan_list
															.get(i)
															.get("line_name")
															.toString(),
															reportnum,
															iDate + i,
															QRCONDENAME,
															userBean.getLogonName()
																	.toString());
										}
										UIHelper.ToastMessage(
												context,
												"二维码已生成至Mypaperless-photo/images/myQRcode中",
												5000);
										yixuan_list.clear();
										quanxuan_cb.setChecked(false);
										myadpter.allchoose(false);
										myadpter.notifyDataSetChanged();
									} catch (Exception e) {
										Log.e("generate QRCode Error",
												e.toString());
									}

								}
							}
						});
				alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								alert.dismiss();
								return;
							}
						});
				alert.show();
			}
			break;
		case R.id.quanxuan_cb:
			if (quanxuan_cb.isChecked() == true) {
				myadpter.allchoose(true);
				yixuan_list.clear();
				yixuan_list.addAll(list);
				myadpter.notifyDataSetChanged();
			} else {
				myadpter.allchoose(false);
				yixuan_list.clear();
				myadpter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}

}
