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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.ExamineDetailBean;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.Dialog_Examine;

/**
 * 詳細點檢信息
 * 
 * @author f1330297
 * 
 */
public class Examine_mes_detailActivity extends BaseActivity {
	ListView detailListView;
	MyAdapter adapter;
	private TextView title, statusTextView;
	Button falsebtn, turebtn;
	private String Status = null, RepostNOstr = null, report_numstr = null,
			NodeNBstr = null, falsecausestr = null, reportName = null,
			checkLabel = null, labelReasons = null, examineperson = null,
			fialcause = null, linearid = null, check_by = null,
			check_order = null, createby_status = null;
	Context context = Examine_mes_detailActivity.this;
	HttpStart start = null;
	List<ExamineDetailBean> listdatail = new ArrayList<ExamineDetailBean>();
	private int k = 0;
	LinearLayout detailLinear, statusLinear, listviewlinear, becausedatelinear,
			becauselinear;
	private boolean future = false;
	UserBean user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.examine_mes_detail);
		title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(getResources().getString(R.string.message_detail_more));
		user = (UserBean) getApplicationContext();
		CheckLogin();
		Intent intent = getIntent();
		RepostNOstr = intent.getStringExtra("RepostNO").toString().trim();// 点检编号
		report_numstr = intent.getStringExtra("report_num").toString().trim();// 报表编号如：mfgv
		NodeNBstr = intent.getStringExtra("NodeNB").toString().trim();// 节次
		Status = intent.getStringExtra("Status").toString().trim();// 签核状态
		examineperson = intent.getStringExtra("ExaminePerson").toString()
				.trim();// 审核人
		reportName = intent.getStringExtra("report_Name").toString().trim();// 报表名
		checkLabel = intent.getStringExtra("checkLabel").toString().trim();// 点检标注
		labelReasons = intent.getStringExtra("labelReasons").toString().trim();// 标注理由
		check_order = intent.getStringExtra("check_order").toString().trim(); // 審核順序
		linearid = intent.getStringExtra("linearid").toString().trim();
		createby_status = intent.getStringExtra("createby_status").toString().trim();  //點檢修改入口
		
		detailLinear = (LinearLayout) findViewById(R.id.if_detaillinear);
		statusLinear = (LinearLayout) findViewById(R.id.status_detaillinear);
		becauselinear = (LinearLayout) findViewById(R.id.because_detaillinear);
		statusTextView = (TextView) findViewById(R.id.examine_statusEndTV);
		TextView person = (TextView) findViewById(R.id.examine_statusperson);

		if (linearid.equals("1")) {
			check_by = intent.getStringExtra("check_by").toString().trim();
			// 根据签核状态判断是否隐藏
			if (Status.equals("0")) {// 待审核
				detailLinear.setVisibility(View.GONE);
				statusLinear.setVisibility(View.VISIBLE);
				statusTextView.setText(getResources().getString(
						R.string.message_detail_to_audit));
				becauselinear.setVisibility(View.GONE);
				person.setText(getResources().getString(
						R.string.message_check_by)
						+ check_by);
			} else if (Status.equals("1")) {// 完毕

				detailLinear.setVisibility(View.GONE);
				statusLinear.setVisibility(View.VISIBLE);
				statusTextView.setText(getResources().getString(
						R.string.message_detail_statusok));
				becauselinear.setVisibility(View.GONE);
				person.setText(getResources().getString(
						R.string.message_check_by)
						+ examineperson);
			} else {// 拒签
				detailLinear.setVisibility(View.GONE);
				statusLinear.setVisibility(View.VISIBLE);
				becauselinear.setVisibility(View.VISIBLE);
				person.setText(getResources().getString(
						R.string.message_check_by)
						+ examineperson);
				statusTextView.setText(getResources().getString(
						R.string.message_detail_statusno));
			}
		} else {
			// 根据签核状态判断是否隐藏
			if (Status.equals("0")) {// 待审核
				if (examineperson.equals(user.getChineseName().toString())) {
					detailLinear.setVisibility(View.VISIBLE);
					statusLinear.setVisibility(View.GONE);
					becauselinear.setVisibility(View.GONE);
					person.setText(examineperson);
				} else {
					detailLinear.setVisibility(View.GONE);
					statusLinear.setVisibility(View.VISIBLE);
					statusTextView.setText(getResources().getString(
							R.string.message_detail_to_audit));
					becauselinear.setVisibility(View.GONE);
					person.setText(getResources().getString(
							R.string.message_check_by)
							+ examineperson);
				}
			} else if (Status.equals("1")) {// 完毕

				detailLinear.setVisibility(View.GONE);
				statusLinear.setVisibility(View.VISIBLE);
				statusTextView.setText(getResources().getString(
						R.string.message_detail_statusok));
				becauselinear.setVisibility(View.GONE);
				person.setText(examineperson);
			} else {// 拒签
				detailLinear.setVisibility(View.GONE);
				statusLinear.setVisibility(View.VISIBLE);
				becauselinear.setVisibility(View.VISIBLE);
				person.setText(examineperson);
				statusTextView.setText(getResources().getString(
						R.string.message_detail_statusno));
			}
		}
		start = new HttpStart(context, handler);
		getData();
		init();

	}

	/**
	 * 驳回原因
	 */
	public void getCause() {
		start.getServerData(3, MyConstant.GET_EXAMINE_MESSAGE_DETAIL_CAUSE,
				RepostNOstr, NodeNBstr, user.getLogonName(), check_order);
	}

	public void setCause() {
		TextView becauseNo = (TextView) findViewById(R.id.examine_becausetx);
		Log.i("<<<<<<<<OOOOOO", fialcause);
		becauseNo.setText(fialcause);
	}

	/**
	 * 获得详细信息
	 */
	public void getData() {

		Log.i(">>>>>RRRRRR", report_numstr + "_" + RepostNOstr + "_"
				+ NodeNBstr);
		start.getServerData(3, MyConstant.GET_EXAMINE_MESSAGE_DETAIL,
				report_numstr, RepostNOstr, NodeNBstr);
	}

	/**
	 * 审核是否通过
	 * 
	 * @param future
	 */
	public void examineData(boolean future) {
		if (future) {
			start.getServerData(3, MyConstant.UPDATE_EXAMINE_TURE_DETAIL,
					RepostNOstr, NodeNBstr, "date", check_order);
		} else {
			Log.i(">>>>detail>>", falsecausestr);
			start.getServerData(3, MyConstant.UPDATE_EXAMINE_FALSE_DETAIL,
					falsecausestr, RepostNOstr, NodeNBstr, "date", check_order);
		}
	}

	/**
	 * 没有数据时显示
	 */
	public void examineDataNull() {
		TextView reportname = (TextView) findViewById(R.id.text_table_detail);
		TextView reportnumber = (TextView) findViewById(R.id.data_table_detail);
		TextView NDcheck = (TextView) findViewById(R.id.ND_check_detail);
		TextView becausedetail = (TextView) findViewById(R.id.because_detail);
		reportname.setText(reportName);
		reportnumber.setText(RepostNOstr);
		NDcheck.setText(NodeNBstr);
		becausedetail.setText(checkLabel + "---" + labelReasons);
	}

	public void init() {
		// 返回主页
		Button homebtn = (Button) findViewById(R.id.btn_submit);
		homebtn.setBackgroundResource(R.drawable.tabbar_home1);
		homebtn.setVisibility(View.GONE);
		homebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
			}
		});
		listviewlinear = (LinearLayout) findViewById(R.id.examine_detail_listlinear);
		becausedatelinear = (LinearLayout) findViewById(R.id.nodata_linear_deatil);
		detailListView = (ListView) findViewById(R.id.examine_detail_lV);
		adapter = new MyAdapter();
		// 驳回
		falsebtn = (Button) findViewById(R.id.false_detailbtn);
		falsebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showWaiterAuthorizationDialog();
			}
		});
		// 通过
		turebtn = (Button) findViewById(R.id.ture_detailbtn);
		turebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(context)
						.setIcon(R.drawable.nt_warn)
						.setTitle(
								getResources().getString(
										R.string.message_detail_orpass))
						.setPositiveButton(
								getResources().getString(
										R.string.message_detail_passT),
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										future = true;
										examineData(future);
									}
								})
						.setNeutralButton(
								getResources().getString(
										R.string.message_detail_passF),
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		});
	}

	/**
	 * 初始化適配器數據
	 */
	private void initAdapter() {
		detailListView.setAdapter(adapter);
		detailListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				/**
				 * 修改記錄說明
				 * 1、顯示了審核按鈕即有權限修改此筆記錄
				 * 2、點檢人在審核人未簽核或駁回的情況下可修改此筆記錄 由ModifyCheckActivity中的fragment傳值標識
				 */
				if (detailLinear.getVisibility()==View.VISIBLE||createby_status.equals("0")
						||createby_status.equals("2")) {
					final Dialog_Examine edDialog = new Dialog_Examine(context,
							listdatail.get(arg2).getNumberstr() + ":"
									+ listdatail.get(arg2).getCheck_name());
					edDialog.setCanceledOnTouchOutside(false);// 点击对话框外不消失
					edDialog.getWindow().setBackgroundDrawableResource(
							android.R.color.transparent);// 去除弹框背景
					final EditText TypeEtd = (EditText) edDialog
							.findViewById(R.id.type_kpi_config);
					TypeEtd.setText(listdatail.get(arg2).getCheck_content());
					edDialog.show();
					final int proid = arg2;
					edDialog.setOKButton("確定", new OnClickListener() {
						@Override
						public void onClick(View v) {
							start.getServerData(3, MyConstant.GET_CONFIRM,
									TypeEtd.getText().toString(), RepostNOstr,
									NodeNBstr, listdatail.get(proid)
											.getProid());
							edDialog.dismiss();
						}
					});
					edDialog.setNOButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							edDialog.dismiss();
						}
					});
				}
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.equals(MyConstant.GET_EXAMINE_MESSAGE_DETAIL)) {
					// 數據遍歷
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (listdatail.size() > 0) {
						listdatail.clear();
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)
							|| result.get(1).toString()
									.equals(MyConstant.GET_FLAG_NULL_DETAIL)) {
						becausedatelinear.setVisibility(View.VISIBLE);
						listviewlinear.setVisibility(View.GONE);
						examineDataNull();
						adapter.notifyDataSetChanged();
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						String ord = null;
						for (int i = 1; i < result.size(); i += 7) {
							k++;
							if (result.get(i + 3).toString().equals("0")) {
								ord = getResources().getString(
										R.string.detail_passyes);
							} else {
								ord = getResources().getString(
										R.string.detail_passno);
							}
							ExamineDetailBean detailbean = new ExamineDetailBean(
									result.get(i + 0).toString(), result.get(
											i + 1).toString(), result
											.get(i + 2).toString(), ord, result
											.get(i + 4).toString(), result.get(
											i + 5).toString(), k + "",
									result.get(i + 6));
							listdatail.add(detailbean);
						}
						Log.i(">>parentlistbean>>>", listdatail.size() + "");
						becausedatelinear.setVisibility(View.GONE);
						listviewlinear.setVisibility(View.VISIBLE);
					}
					initAdapter();
					adapter.notifyDataSetChanged();
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						becausedatelinear.setVisibility(View.VISIBLE);
						listviewlinear.setVisibility(View.GONE);
						Toast.makeText(context, "未连接服务器....", 0).show();
						adapter.notifyDataSetChanged();
					}
					getCause();
					result.clear();
				}

				if (key.equals(MyConstant.GET_EXAMINE_MESSAGE_DETAIL_CAUSE)) {
					// 數據遍歷
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)
							|| result.get(1).toString()
									.equals(MyConstant.GET_FLAG_NULL_DETAIL)) {
						return;
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {

						fialcause = result.get(1).toString();
						Log.i(">>>>causesssss", fialcause);

					}
					adapter.notifyDataSetChanged();
					setCause();
					result.clear();
				}
				if (key.equals(MyConstant.GET_CONFIRM)) {
					// 數據遍歷
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)
							|| result.get(1).toString()
									.equals(MyConstant.GET_FLAG_NULL_DETAIL)) {
						return;
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						Toast.makeText(context, "修改成功！", 0).show();
						getData();
					}
				}
				if (key.equals(MyConstant.UPDATE_EXAMINE_TURE_DETAIL)) {
					// 數據遍歷
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "審核異常，請返回重試");
						return;
					}
					if (result.get(0).toString().equals("CHECK_NO_PASS")) {

						UIHelper.ToastMessage(context, "審核失敗，上一階審核主管"
								+ result.get(3).toString() + "還未審核完");
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "審核完成");
						turebtn.setVisibility(View.GONE);
						falsebtn.setVisibility(View.GONE);
					}
				}
				if (key.equals(MyConstant.UPDATE_EXAMINE_FALSE_DETAIL)) {
					// 數據遍歷
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "審核異常，請返回重試");
						return;
					}
					if (result.get(0).toString().equals("CHECK_NO_PASS")) {

						UIHelper.ToastMessage(context, "審核失敗，上一階審核主管"
								+ result.get(3).toString() + "還未審核完");
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "審核完成");
						turebtn.setVisibility(View.GONE);
						falsebtn.setVisibility(View.GONE);
					}
				}
			}

		};
	};

	class MyAdapter extends BaseAdapter {

		public int getCount() {
			return listdatail.size();
		}

		public Object getItem(int position) {
			return listdatail.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View item = convertView != null ? convertView : View.inflate(
					getApplicationContext(),
					R.layout.examine_mes_detail_listview, null);
			final TextView number = (TextView) item.findViewById(R.id.xulie_tv);
			final TextView checkname = (TextView) item
					.findViewById(R.id.checkname_tv);
			final TextView check_yeild = (TextView) item
					.findViewById(R.id.check_yeild_tv);
			final TextView icarno = (TextView) item
					.findViewById(R.id.icarno_tv);
			final TextView check_result = (TextView) item
					.findViewById(R.id.check_result_tv);
			final TextView check_pro_name = (TextView) item
					.findViewById(R.id.check_pro_name_tv);
			final TextView check_content = (TextView) item
					.findViewById(R.id.check_content_tv);
			number.setText(listdatail.get(position).getNumberstr());
			checkname.setText(listdatail.get(position).getCheck_name());
			check_yeild.setText(listdatail.get(position).getCheck_yeild());
			icarno.setText(listdatail.get(position).getIcarno());
			check_result.setText(listdatail.get(position).getCheck_result());
			check_pro_name
					.setText(listdatail.get(position).getCheck_pro_name());
			check_content.setText(listdatail.get(position).getCheck_content());
			return item;
		}

	}

	/**
	 * 显示对话框>>驳回框 输入原因
	 */
	public void showWaiterAuthorizationDialog() {

		// LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
		// LayoutInflater factory =
		// LayoutInflater.from(Examine_mes_detailActivity.this);
		final LinearLayout textEntryView = (LinearLayout) getLayoutInflater()
				.inflate(R.layout.examine_detail_alertdialog, null);
		// 把activity_login中的控件定义在View中
		// final View textEntryView=
		// factory.inflate(R.layout.examine_detail_alertdialog,
		// null);

		// 将examine_detail_alertdialog中的控件显示在对话框中
		new AlertDialog.Builder(context)
		// 对话框的标题
				.setTitle(
						getResources().getString(
								R.string.message_detail_statusno))
				// 设定显示的View
				.setView(textEntryView)
				// 对话框中的“登陆”按钮的点击事件
				.setPositiveButton(
						getResources().getString(R.string.message_detail_passT),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								// 注意：textEntryView.findViewById很重要，因为上面factory.inflate(R.layout.examine_detail_alertdialog,
								// null)将页面布局赋值给了textEntryView了
								final EditText falsecause = (EditText) textEntryView
										.findViewById(R.id.false_alertdialog_edt);

								falsecausestr = ChangString.change(falsecause
										.getText().toString().trim());
								// 现在为止已经获得了输入框中的内容，接下来就是根据自己的需求来编写代码了
								// 这里做一个简单的测试，假定输入框为空进入其他操作页面
								if (!falsecausestr.equals("")) {
									// 跳转到其他Activity
									examineData(future);
								} else {
									Toast.makeText(context, "输入有误,请重输",
											Toast.LENGTH_SHORT).show();
									return;

								}
							}
						})
				// 对话框的“取消”单击事件
				.setNegativeButton(
						getResources().getString(R.string.message_detail_passF),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Examine_mes_detailActivity.this.finish();
							}
						})
				// 设置dialog是否为模态，false表示模态，true表示非模态
				.setCancelable(false)
				// 对话框的创建、显示
				.create().show();
	}

	// 返回键按钮
	public void activity_back(View v) {
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
		this.finish();
	}

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

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}
}
