package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.Choose_report.Picture;
import com.zsm.foxconn.mypaperless.adapter.PZExpandableAdapter;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.EXpandaChildModel;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

/**
 * 報表配置
 * 
 * @author mgp 2016/01/15
 */
public class Configuration_report extends BaseActivity implements
		OnClickListener {
	private Context context = Configuration_report.this;
	HttpStart start = null;
	private TextView head_title, add;
	private CheckBox all;
	private String isaccess, bUname, reportid,sbu,section,issection;
	private UserBean userBean;
	private ExpandableListView configguration_edlist;
	private LinkedList<String> groupData = new LinkedList<String>();
	private List<LinkedList<EXpandaChildModel>> childData = new ArrayList<LinkedList<EXpandaChildModel>>();
	public static boolean kecaozuo,isfirst = false;
	private String[] passpz = null,sbustr = null;
	private PZExpandableAdapter pzAdapter;
	String modelstr = "";		//存放勾選的Pro_ID
	private int checknum;		//勾選數目
	private Spinner choosesbu_sp;
	private LinearLayout ll_bottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configuration_report);
		AppManager.getInstance().addActivity(Configuration_report.this);
		Intent intent = getIntent();
		bUname = intent.getStringExtra("bUname");
		isaccess = intent.getStringExtra("isaccess");
		reportid = intent.getStringExtra("reportid");
		section = intent.getStringExtra("section");
		issection = intent.getStringExtra("issection");
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		start = new HttpStart(context, handler);
		start.getServerData(0, MyConstant.GET_PEIZHI_PRO_ID,
				reportid, userBean.getMFG(), userBean.getSBU(),userBean.getSite(),userBean.getBU());
		findViewById();
		initView();
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
		head_title = (TextView) findViewById(R.id.head_title);
		head_title.setText("配置報表");
		all = (CheckBox) findViewById(R.id.tv_all);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
		ll_bottom.setVisibility(View.VISIBLE);
		all.setOnClickListener(this);
		add = (TextView) findViewById(R.id.submitbt);
		add.setOnClickListener(this);
		configguration_edlist = (ExpandableListView) findViewById(R.id.configguration_edlist);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		pzAdapter = new PZExpandableAdapter(context, groupData, childData);
		configguration_edlist.setAdapter(pzAdapter);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.equals(MyConstant.GET_CHECK_NAME_PEIZHI)) {
					result = msg.getData().getStringArrayList(key);
					// 數據遍歷
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 3) {
							if (!groupData.contains(result.get(i + 1)
									.toString())) {
								groupData.add(result.get(i + 1).toString());
							}
						}

						List<String> child = new ArrayList<String>();
						for (int j = 0; j < groupData.size(); j++) {
							String ss = "";
							for (int k = 1; k < result.size(); k += 3) {
								if (groupData.get(j).toString()
										.equals(result.get(k + 1).toString())) {
									ss += result.get(k).toString() + MyConstant.GET_FLAG1
											+ result.get(k + 2).toString()
											+ MyConstant.GET_FLAG1;
								}
							}
							child.add(ss);
						}
						if (passpz[0].toString().equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
							for (int k2 = 0; k2 < child.size(); k2++) {
								LinkedList<EXpandaChildModel> childList = new LinkedList<EXpandaChildModel>();
								String[] ss = child.get(k2).toString().split(MyConstant.GET_FLAG1);
								for (int b = 0; b < ss.length; b += 2) {
									int m = 0;
									EXpandaChildModel eXpandaChildModel = new EXpandaChildModel();
									eXpandaChildModel.setChildpro_id(ss[b]);
									eXpandaChildModel.setChildTitle(ss[b + 1]);
									eXpandaChildModel.setIschoosePZ(false);
									childList.add(eXpandaChildModel);
								}
								childData.add(childList);
							}
						}else {
							for (int k2 = 0; k2 < child.size(); k2++) {
								LinkedList<EXpandaChildModel> childList = new LinkedList<EXpandaChildModel>();
								String[] ss = child.get(k2).toString().split(MyConstant.GET_FLAG1);
								for (int b = 0; b < ss.length; b += 2) {
									int m = 0;
									EXpandaChildModel eXpandaChildModel = new EXpandaChildModel();
									eXpandaChildModel.setChildpro_id(ss[b]);
									eXpandaChildModel.setChildTitle(ss[b + 1]);
									 for (int k = 0; k < passpz.length; k++) {
										if (ss[b].toString().equals(passpz[k].toString())) {
											eXpandaChildModel.setIschoosePZ(true);
											m = 1;
										}
									 }
									 if (m==0) {
										 eXpandaChildModel.setIschoosePZ(false);
									}
									childList.add(eXpandaChildModel);
								}
								childData.add(childList);
							}
						}
						
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無數據");
						return;
					}
					pzAdapter.notifyDataSetChanged();
				}
				if (key.equals(MyConstant.SAVE_PEIZHI_CHECK_NAME)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "配置成功");
						Intent intent = new Intent(context, Choose_report.class);
						intent.putExtra("isaccess", isaccess);
						intent.putExtra("bUname", bUname);
						intent.putExtra("section", section);
						intent.putExtra("issection", issection);
						startActivity(intent);
						finish();
						AppManager.killTopActivity();
					}
					
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						modelstr = "";
						UIHelper.ToastMessage(context, "提交異常");
						return;
					}
					}
				
				if (key.equals(MyConstant.GET_PEIZHI_PRO_ID)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						passpz = new String[result.size()-1];
						for (int i = 1; i < result.size(); i++) {
							passpz[i-1] = result.get(i).toString();
						}
					}
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						passpz = new String[result.size()];
						passpz[0] = MyConstant.GET_FLAG_NULL;
					}
					
					start.getServerData(3, MyConstant.GET_CHECK_NAME_PEIZHI,
							userBean.getSite(), userBean.getBU(), reportid);
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
								Configuration_report.this,
								android.R.layout.simple_dropdown_item_1line,
								sbustr));
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
		case R.id.tv_all:
			kecaozuo = true;
			if (all.getText().toString().trim().equals("全选"))
			{
				pzAdapter.allchoose(true);
				pzAdapter.notifyDataSetChanged();
				all.setText("全不选");
			} else {
				pzAdapter.allchoose(false);
				pzAdapter.notifyDataSetChanged();
				all.setText("全选");
			}
			kecaozuo = false;
			break;
		case R.id.submitbt:
			childData = pzAdapter.data();
			checknum = 0;
			for (int i = 0; i < groupData.size(); i++) {
				for (int k = 0; k < childData.get(i).size(); k++) {
					EXpandaChildModel model = new EXpandaChildModel();
					model=childData.get(i).get(k);
					if (model.isIschoosePZ()) {
						modelstr = modelstr + model.getChildpro_id()+MyConstant.GET_FLAG2;
						checknum = checknum + 1;
					}
				}
			}
			if (modelstr.length()==0||checknum==0) {
				UIHelper.ToastMessage(context, "無勾選項");
				return;
			}
			start.getServerData(0, MyConstant.GET_PEIZHI_SBU,userBean.getBU(),userBean.getMFG());
			LayoutInflater inflater =getLayoutInflater();
            View layout = inflater.inflate(R.layout.alertdialog_item, null);
            AlertDialog alert = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).create();
            alert.setView(layout);
            choosesbu_sp = (Spinner)layout.findViewById(R.id.choosesbu_spinner);
            alert.setCanceledOnTouchOutside(true);
            alert.setCancelable(true);
			alert.setIcon(R.drawable.ic_system);
			alert.setTitle("系統提示:");
			alert.setMessage("已選擇"+checknum+"條點檢項,"+"確定提交信息?");
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
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "確定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					start.getServerData(3, MyConstant.SAVE_PEIZHI_CHECK_NAME,modelstr,reportid,userBean.getMFG(),sbu,userBean.getSite(),userBean.getBU());
				}
			});
			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
			});
			alert.show();
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
		overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
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
			overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
