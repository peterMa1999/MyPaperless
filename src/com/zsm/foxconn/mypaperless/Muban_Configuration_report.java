package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.adapter.PZExpandableAdapter;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.EXpandaChildModel;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

/**
 * 模板报表
 * 
 * @author mgp 2016/01/15
 */
public class Muban_Configuration_report extends BaseActivity implements
		OnClickListener {
	private Context context = Muban_Configuration_report.this;
	HttpStart start = null;
	private TextView head_title, add;
	private CheckBox all;
	private LinearLayout ll_bottom;
	private String isaccess, bUname, reportid, sbu, reportName, section,
			issection;
	private UserBean userBean;
	private ExpandableListView configguration_edlist;
	private LinkedList<String> groupData = new LinkedList<String>();
	private List<LinkedList<EXpandaChildModel>> childData = new ArrayList<LinkedList<EXpandaChildModel>>();
	public static boolean kecaozuo, isfirst = false;
	private PZExpandableAdapter pzAdapter;
	String modelstr = ""; // 存放Pro_ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configuration_report);
		AppManager.getInstance().addActivity(Muban_Configuration_report.this);
		Intent intent = getIntent();
		bUname = intent.getStringExtra("bUname");
		isaccess = intent.getStringExtra("isaccess");
		reportid = intent.getStringExtra("reportid");
		reportName = intent.getStringExtra("reportName");
		section = intent.getStringExtra("section");
		issection = intent.getStringExtra("issection");
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		start = new HttpStart(context, handler);
		start.getServerData(3, MyConstant.GET_CHECK_NAME_PEIZHI,
				userBean.getSite(), bUname, reportid);
		findViewById();
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
		head_title = (TextView) findViewById(R.id.head_title);
		head_title.setText(reportName);
		all = (CheckBox) findViewById(R.id.tv_all);
		all.setVisibility(View.GONE);
		add = (TextView) findViewById(R.id.submitbt);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
		add.setOnClickListener(this);
		configguration_edlist = (ExpandableListView) findViewById(R.id.configguration_edlist);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		pzAdapter = new PZExpandableAdapter(context, groupData, childData, 1);
		configguration_edlist.setAdapter(pzAdapter);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = msg.getData().getStringArrayList(key);
				if (key.equals(MyConstant.GET_CHECK_NAME_PEIZHI)) {
					// 數據遍歷
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 3) {
							if (!groupData.contains(result.get(i + 1)
									.toString())) {
								groupData.add(result.get(i + 1).toString());
							}
							modelstr += result.get(i) + MyConstant.GET_FLAG3;
						}

						List<String> child = new ArrayList<String>();
						for (int j = 0; j < groupData.size(); j++) {
							String ss = "";
							for (int k = 1; k < result.size(); k += 3) {
								if (groupData.get(j).toString()
										.equals(result.get(k + 1).toString())) {
									ss += result.get(k).toString() + MyConstant.GET_FLAG
											+ result.get(k + 2).toString()
											+ MyConstant.GET_FLAG;
								}
							}
							child.add(ss);
						}
						for (int k2 = 0; k2 < child.size(); k2++) {
							LinkedList<EXpandaChildModel> childList = new LinkedList<EXpandaChildModel>();
							String[] ss = child.get(k2).toString().split(MyConstant.GET_FLAG);
							for (int b = 0; b < ss.length; b += 2) {
								EXpandaChildModel eXpandaChildModel = new EXpandaChildModel();
								eXpandaChildModel.setChildpro_id(ss[b]);
								eXpandaChildModel.setChildTitle(ss[b + 1]);
								eXpandaChildModel.setIschoosePZ(false);
								childList.add(eXpandaChildModel);
							}
							childData.add(childList);
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無數據");
						return;
					}
					pzAdapter.notifyDataSetChanged();
					start.getServerData(0, MyConstant.GET_REPORT_TEAM, reportid);
				}
				if (key.equals(MyConstant.MUBAN_PEIZHI)) {
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "添加成功");
					}
					if (result.get(0).toString().equalsIgnoreCase("MUBAN EXIT")) {
						UIHelper.ToastMessage(context, "模板報表已添加");
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "模板報表添加異常");
					}
				}
				if (key.equals(MyConstant.GET_REPORT_TEAM)) {
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						String team = result.get(1).toString();
						if (team.equalsIgnoreCase(userBean.getTeam())) {
							if (userBean.getUserLevel().toString()
									.equalsIgnoreCase("2")
									&& isaccess.equals("0")
									&& bUname.equalsIgnoreCase(userBean.getBU()
											.toString())) {
								ll_bottom.setVisibility(View.VISIBLE);
								add.setText("將此報表模板添加為我處使用");
							} else {
								ll_bottom.setVisibility(View.GONE);
							}
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						ll_bottom.setVisibility(View.GONE);
					}

				}
				if (key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					Toast.makeText(context, "未连接服务器....", 0).show();
				}
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submitbt:
			final AlertDialog alertDialog = new AlertDialog.Builder(context)
					.create();
			alertDialog.setTitle("系統提示:");
			alertDialog.setMessage("確定將此報表模板添加至" + userBean.getMFG() + "-"
					+ userBean.getSBU() + "-" + userBean.getTeam() + "處");
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(context, "取消", Toast.LENGTH_LONG)
									.show();
							alertDialog.dismiss();
							return;
						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							start.getServerData(0, MyConstant.MUBAN_PEIZHI,
									modelstr, reportid, userBean.getMFG(),
									userBean.getSite(), userBean.getBU());
						}
					});
			alertDialog.show();
			break;
		default:
			break;
		}
	}

	public void returnClick(View view) {
//		Intent intent = new Intent(this, Choose_report.class);
//		intent.putExtra("isaccess", isaccess);
//		intent.putExtra("bUname", bUname);
//		intent.putExtra("section", section);
//		intent.putExtra("issection", issection);
//		startActivity(intent);
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
//			Intent intent = new Intent(this, Choose_report.class);
//			intent.putExtra("isaccess", isaccess);
//			intent.putExtra("bUname", bUname);
//			intent.putExtra("section", section);
//			intent.putExtra("issection", issection);
//			startActivity(intent);
			overridePendingTransition(R.anim.move_left_in_activity,
					R.anim.move_right_out_activity);
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
