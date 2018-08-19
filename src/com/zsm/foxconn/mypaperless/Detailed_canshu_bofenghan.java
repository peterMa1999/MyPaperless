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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

public class Detailed_canshu_bofenghan extends BaseActivity {

	private TextView bfh_tv_bu, bfh_tv_buiding,bfh_tv_line, bfh_tv_modelname,
			bfh_tv_productname, bfh_tv_botpreheati, bfh_tv_botpreheatii,
			bfh_tv_botpreheatiii, bfh_tv_botpreheativ, bfh_tv_toppreheati,
			bfh_tv_toppreheatii, bfh_tv_toppreheatiii, bfh_tv_toppreheativ,
			bfh_tv_chainspeedafter, bfh_tv_chainspeed, bfh_tv_tintemperature,
			bfh_tv_tinbathheight, bfh_tv_turbulentwave, bfh_tv_advectionwave,
			bfh_tv_tinbartype, bfh_tv_fluxtype, bfh_tv_fluxflow,
			bfh_tv_nozzleairpressure, bfh_tv_totalairpressure,
			bfh_tv_tankairpressure, bfh_tv_fluxproportion, tv_last_editdate,
			tv_bfh_update_person, bfh_bartitle_txt,
			tv_last_updatetype;
	private Button bt_bfh_detailed_update, bt_bfh_detailed_delete,
			bt_bfh_detailed_submit;
	private HttpStart httpStart = null;
	private Intent intent;
	private String parameternum, floo_name,line_name, th_floor, operaction, section, th_line;
	private Context context = Detailed_canshu_bofenghan.this;
	private String[] floorstr = null,Lines = null;
	private Spinner sp_de_floor = null,sp_bfh_line =  null;
	private UserBean userBean;
	private ScrollView layout_bfh_operaction, layout_bfh_detailed;
	private EditText bfh_ed_modelname, bfh_ed_productname, bfh_ed_botpreheati,
			bfh_ed_botpreheatii, bfh_ed_botpreheatiii, bfh_ed_botpreheativ,
			bfh_ed_toppreheati, bfh_ed_toppreheatii, bfh_ed_toppreheatiii,
			bfh_ed_toppreheativ, bfh_ed_chainspeedafter, bfh_ed_chainspeed,
			bfh_ed_tintemperature, bfh_ed_tinbathheight, bfh_ed_turbulentwave,
			bfh_ed_advectionwave, bfh_ed_tinbartype, bfh_ed_fluxtype,
			bfh_ed_fluxflow, bfh_ed_nozzleairpressure, bfh_ed_totalairpressure,
			bfh_ed_tankairpressure, bfh_ed_fluxproportion, bfh_ed_beizhu;
	private List<String> list = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_canshu_bofenghan);
		httpStart = new HttpStart(context, handler);
		userBean = (UserBean) getApplicationContext();
		intent = getIntent();
		CheckLogin();
		section = intent.getStringExtra("section");
		operaction = intent.getStringExtra("operaction"); // 動作
		findViewById();
		if (operaction.equals("1")) { // 添加動作
			httpStart.getServerData(0, MyConstant.GET_FLOOR_IPQC,
					userBean.getLogonName(), section, userBean.getMFG(),
					userBean.getBU());
		} else {
			parameternum = intent.getStringExtra("parameternum");
			httpStart.getServerData(0, MyConstant.GET_BFH_DETAILED_CANSHU,
					parameternum);
		}
		initView();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = new ArrayList<String>();
				result = msg.getData().getStringArrayList(key);
				if (key.equalsIgnoreCase(MyConstant.GET_BFH_DETAILED_CANSHU)) {
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						bfh_tv_bu.setText(result.get(2).trim());
						bfh_tv_buiding.setText(result.get(3).trim());
						bfh_tv_line.setText(result.get(31).trim());
						bfh_tv_modelname.setText(result.get(4).trim());
						bfh_tv_productname.setText(result.get(5).trim());
						bfh_tv_botpreheati.setText(result.get(6).trim());
						bfh_tv_botpreheatii.setText(result.get(7).trim());
						bfh_tv_botpreheatiii.setText(result.get(8).trim());
						bfh_tv_botpreheativ.setText(result.get(9).trim());
						bfh_tv_toppreheati.setText(result.get(10).trim());
						bfh_tv_toppreheatii.setText(result.get(11).trim());
						bfh_tv_toppreheatiii.setText(result.get(12).trim());
						bfh_tv_toppreheativ.setText(result.get(13).trim());
						bfh_tv_chainspeedafter.setText(result.get(14).trim());
						bfh_tv_chainspeed.setText(result.get(15).trim());
						bfh_tv_tintemperature.setText(result.get(16).trim());
						bfh_tv_tinbathheight.setText(result.get(17).trim());
						bfh_tv_turbulentwave.setText(result.get(18).trim());
						bfh_tv_advectionwave.setText(result.get(19).trim());
						bfh_tv_tinbartype.setText(result.get(20).trim());
						bfh_tv_fluxtype.setText(result.get(21).trim());
						bfh_tv_fluxflow.setText(result.get(22).trim());
						bfh_tv_nozzleairpressure.setText(result.get(23).trim());
						bfh_tv_totalairpressure.setText(result.get(24).trim());
						bfh_tv_tankairpressure.setText(result.get(25).trim());
						bfh_tv_fluxproportion.setText(result.get(26).trim());
						tv_last_editdate.setText(result.get(27).trim());
						tv_last_updatetype.setText(result.get(28).trim());
						tv_bfh_update_person.setText(result.get(30).trim());
						floo_name = result.get(3).trim();
						line_name = result.get(31).trim();
						if (list.size() > 0) {
							list.clear();
						}
						list.addAll(result);
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						bt_bfh_detailed_update.setVisibility(View.GONE);
						bt_bfh_detailed_delete.setVisibility(View.GONE);
						UIHelper.ToastMessage(context, "無詳細參數");
					}
				}

				if (key.equalsIgnoreCase(MyConstant.GET_FLOOR_IPQC)) {
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
					sp_de_floor.setAdapter(new ArrayAdapter(context,
							android.R.layout.simple_dropdown_item_1line,
							floorstr));
					for (int i = 0; i < floorstr.length; i++) {
						if (floorstr[i].equals(floo_name)) {
							sp_de_floor.setSelection(i, true);
						}
					}
					th_floor = sp_de_floor.getSelectedItem().toString();
					httpStart.getServerData(0, MyConstant.GET_CHECK_LINE,
							userBean.getMFG(), section, th_floor,userBean.getSBU().toString());
				}

				//獲取對應樓層的線別
				if (key.equals(MyConstant.GET_CHECK_LINE)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						Lines = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							Lines[i - 1] = result.get(i).toString();
						}
					}
					
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無線別信息");
						return;
					}
					sp_bfh_line.setAdapter(new ArrayAdapter(context,
							android.R.layout.simple_dropdown_item_1line,
							Lines));
					for (int i = 0; i < Lines.length; i++) {
						if (Lines[i].equals(line_name)) {
							sp_bfh_line.setSelection(i, true);
						}
					}
					th_line = sp_bfh_line.getSelectedItem().toString();
				}
				
				if (key.equalsIgnoreCase(MyConstant.OPERACTION_BFH_CANSHU)) {
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "操作成功");
						bt_bfh_detailed_submit.setVisibility(View.GONE);
						bt_bfh_detailed_delete.setVisibility(View.GONE);
						bt_bfh_detailed_update.setVisibility(View.GONE);
					}else if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "操作失敗");
					}else {
						UIHelper.ToastMessage(context, "操作異常");
					}
				}

				if (key.equalsIgnoreCase(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(context, "未連接服務器");
				}

			}
		}
	};

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

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		layout_bfh_operaction = (ScrollView) findViewById(R.id.layout_bfh_operaction);
		layout_bfh_detailed = (ScrollView) findViewById(R.id.layout_bfh_detailed);
		bfh_tv_bu = (TextView) findViewById(R.id.bfh_tv_bu);
		bfh_tv_buiding = (TextView) findViewById(R.id.bfh_tv_buiding);
		bfh_tv_line = (TextView) findViewById(R.id.bfh_tv_line);
		bfh_tv_modelname = (TextView) findViewById(R.id.bfh_tv_modelname);
		bfh_tv_productname = (TextView) findViewById(R.id.bfh_tv_productname);
		bfh_tv_botpreheati = (TextView) findViewById(R.id.bfh_tv_botpreheati);
		bfh_tv_botpreheatii = (TextView) findViewById(R.id.bfh_tv_botpreheatii);
		bfh_tv_botpreheatiii = (TextView) findViewById(R.id.bfh_tv_botpreheatiii);
		bfh_tv_botpreheativ = (TextView) findViewById(R.id.bfh_tv_botpreheativ);
		bfh_tv_toppreheati = (TextView) findViewById(R.id.bfh_tv_toppreheati);
		bfh_tv_toppreheatii = (TextView) findViewById(R.id.bfh_tv_toppreheatii);
		bfh_tv_toppreheatiii = (TextView) findViewById(R.id.bfh_tv_toppreheatiii);
		bfh_tv_toppreheativ = (TextView) findViewById(R.id.bfh_tv_toppreheativ);
		bfh_tv_chainspeedafter = (TextView) findViewById(R.id.bfh_tv_chainspeedafter);
		bfh_tv_chainspeed = (TextView) findViewById(R.id.bfh_tv_chainspeed);
		bfh_tv_tintemperature = (TextView) findViewById(R.id.bfh_tv_tintemperature);
		bfh_tv_tinbathheight = (TextView) findViewById(R.id.bfh_tv_tinbathheight);
		bfh_tv_turbulentwave = (TextView) findViewById(R.id.bfh_tv_turbulentwave);
		bfh_tv_advectionwave = (TextView) findViewById(R.id.bfh_tv_advectionwave);
		bfh_tv_tinbartype = (TextView) findViewById(R.id.bfh_tv_tinbartype);
		bfh_tv_fluxtype = (TextView) findViewById(R.id.bfh_tv_fluxtype);
		bfh_tv_fluxflow = (TextView) findViewById(R.id.bfh_tv_fluxflow);
		bfh_tv_nozzleairpressure = (TextView) findViewById(R.id.bfh_tv_nozzleairpressure);
		bfh_tv_totalairpressure = (TextView) findViewById(R.id.bfh_tv_totalairpressure);
		bfh_tv_tankairpressure = (TextView) findViewById(R.id.bfh_tv_tankairpressure);
		bfh_tv_fluxproportion = (TextView) findViewById(R.id.bfh_tv_fluxproportion);
		tv_last_editdate = (TextView) findViewById(R.id.tv_last_editdate);
		tv_bfh_update_person = (TextView) findViewById(R.id.tv_bfh_update_person);
		bfh_bartitle_txt = (TextView) findViewById(R.id.bfh_bartitle_txt);
		tv_last_updatetype = (TextView) findViewById(R.id.tv_last_updatetype);
		bt_bfh_detailed_update = (Button) findViewById(R.id.bt_bfh_detailed_update);
		bt_bfh_detailed_delete = (Button) findViewById(R.id.bt_bfh_detailed_delete);
		bfh_bartitle_txt.setText("參數詳情");

		sp_de_floor = (Spinner) findViewById(R.id.sp_bfh_floor);
		sp_bfh_line =(Spinner) findViewById(R.id.sp_bfh_line);
		bfh_ed_modelname = (EditText) findViewById(R.id.bfh_ed_modelname);
		bfh_ed_productname = (EditText) findViewById(R.id.bfh_ed_productname);
		bfh_ed_botpreheati = (EditText) findViewById(R.id.bfh_ed_botpreheati);
		bfh_ed_botpreheatii = (EditText) findViewById(R.id.bfh_ed_botpreheatii);
		bfh_ed_botpreheatiii = (EditText) findViewById(R.id.bfh_ed_botpreheatiii);
		bfh_ed_botpreheativ = (EditText) findViewById(R.id.bfh_ed_botpreheativ);
		bfh_ed_toppreheati = (EditText) findViewById(R.id.bfh_ed_toppreheati);
		bfh_ed_toppreheatii = (EditText) findViewById(R.id.bfh_ed_toppreheatii);
		bfh_ed_toppreheatiii = (EditText) findViewById(R.id.bfh_ed_toppreheatiii);
		bfh_ed_toppreheativ = (EditText) findViewById(R.id.bfh_ed_toppreheativ);
		bfh_ed_chainspeedafter = (EditText) findViewById(R.id.bfh_ed_chainspeedafter);
		bfh_ed_chainspeed = (EditText) findViewById(R.id.bfh_ed_chainspeed);
		bfh_ed_tintemperature = (EditText) findViewById(R.id.bfh_ed_tintemperature);
		bfh_ed_tinbathheight = (EditText) findViewById(R.id.bfh_ed_tinbathheight);
		bfh_ed_turbulentwave = (EditText) findViewById(R.id.bfh_ed_turbulentwave);
		bfh_ed_advectionwave = (EditText) findViewById(R.id.bfh_ed_advectionwave);
		bfh_ed_tinbartype = (EditText) findViewById(R.id.bfh_ed_tinbartype);
		bfh_ed_fluxtype = (EditText) findViewById(R.id.bfh_ed_fluxtype);
		bfh_ed_fluxflow = (EditText) findViewById(R.id.bfh_ed_fluxflow);
		bfh_ed_nozzleairpressure = (EditText) findViewById(R.id.bfh_ed_nozzleairpressure);
		bfh_ed_totalairpressure = (EditText) findViewById(R.id.bfh_ed_totalairpressure);
		bfh_ed_tankairpressure = (EditText) findViewById(R.id.bfh_ed_tankairpressure);
		bfh_ed_fluxproportion = (EditText) findViewById(R.id.bfh_ed_fluxproportion);
		bfh_ed_beizhu = (EditText) findViewById(R.id.bfh_ed_beizhu);
		bt_bfh_detailed_submit = (Button) findViewById(R.id.bt_bfh_detailed_submit);
		if (operaction.equals("1")) {
			layout_bfh_operaction.setVisibility(View.VISIBLE);
			layout_bfh_detailed.setVisibility(View.GONE);
		} else {
			layout_bfh_operaction.setVisibility(View.GONE);
			layout_bfh_detailed.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		sp_de_floor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				th_floor = arg0.getItemAtPosition(arg2).toString();
				httpStart.getServerData(3, MyConstant.GET_CHECK_LINE,
						userBean.getMFG(), section, th_floor,userBean.getSBU());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		sp_bfh_line.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				th_line = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void add(View v) {
		switch (v.getId()) {
		case R.id.bfh_textview_update: // 修改記錄
			intent = new Intent(context, Update_recording.class);
			intent.putExtra("parameternum", parameternum);
			UIHelper.ToastMessage(context, "待開發中...");
			// startActivity(intent);
			break;

		// 修改參數
		case R.id.bt_bfh_detailed_update:
			layout_bfh_operaction.setVisibility(View.VISIBLE);
			layout_bfh_detailed.setVisibility(View.GONE);
			httpStart.getServerData(0, MyConstant.GET_FLOOR_IPQC,
					userBean.getLogonName(), section, userBean.getMFG(),
					userBean.getBU());
			bfh_ed_modelname.setText(list.get(4).trim());
			bfh_ed_productname.setText(list.get(5).trim());
			bfh_ed_botpreheati.setText(list.get(6).trim());
			bfh_ed_botpreheatii.setText(list.get(7).trim());
			bfh_ed_botpreheatiii.setText(list.get(8).trim());
			bfh_ed_botpreheativ.setText(list.get(9).trim());
			bfh_ed_toppreheati.setText(list.get(10).trim());
			bfh_ed_toppreheatii.setText(list.get(11).trim());
			bfh_ed_toppreheatiii.setText(list.get(12).trim());
			bfh_ed_toppreheativ.setText(list.get(13).trim());
			bfh_ed_chainspeedafter.setText(list.get(14).trim());
			bfh_ed_chainspeed.setText(list.get(15).trim());
			bfh_ed_tintemperature.setText(list.get(16).trim());
			bfh_ed_tinbathheight.setText(list.get(17).trim());
			bfh_ed_turbulentwave.setText(list.get(18).trim());
			bfh_ed_advectionwave.setText(list.get(19).trim());
			bfh_ed_tinbartype.setText(list.get(20).trim());
			bfh_ed_fluxtype.setText(list.get(21).trim());
			bfh_ed_fluxflow.setText(list.get(22).trim());
			bfh_ed_nozzleairpressure.setText(list.get(23).trim());
			bfh_ed_totalairpressure.setText(list.get(24).trim());
			bfh_ed_tankairpressure.setText(list.get(25).trim());
			bfh_ed_fluxproportion.setText(list.get(26).trim());
			break;
		// 提交參數
		case R.id.bt_bfh_detailed_submit:
			if (bfh_ed_modelname.getText().length() == 0
					|| bfh_ed_productname.getText().length() == 0
					|| bfh_ed_botpreheati.getText().length() == 0
					|| bfh_ed_botpreheatii.getText().length() == 0
					|| bfh_ed_botpreheatiii.getText().length() == 0
					|| bfh_ed_botpreheativ.getText().length() == 0
					|| bfh_ed_toppreheati.getText().length() == 0
					|| bfh_ed_toppreheatii.getText().length() == 0
					|| bfh_ed_toppreheatiii.getText().length() == 0
					|| bfh_ed_toppreheativ.getText().length() == 0
					|| bfh_ed_chainspeedafter.getText().length() == 0
					|| bfh_ed_chainspeed.getText().length() == 0
					|| bfh_ed_tintemperature.getText().length() == 0
					|| bfh_ed_tinbathheight.getText().length() == 0
					|| bfh_ed_turbulentwave.getText().length() == 0
					|| bfh_ed_advectionwave.getText().length() == 0
					|| bfh_ed_tinbartype.getText().length() == 0
					|| bfh_ed_fluxtype.getText().length() == 0
					|| bfh_ed_fluxflow.getText().length() == 0
					|| bfh_ed_nozzleairpressure.getText().length() == 0
					|| bfh_ed_totalairpressure.getText().length() == 0
					|| bfh_ed_tankairpressure.getText().length() == 0
					|| bfh_ed_fluxproportion.getText().length() == 0
					|| bfh_ed_beizhu.getText().length() == 0) {
				UIHelper.ToastMessage(context, "輸入不能為空");
			} else {
				AlertDialog alert = new AlertDialog.Builder(context).create();
				alert.setIcon(R.drawable.ic_system);
				alert.setTitle("系統提示:");
				alert.setMessage("是否提交參數?");
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								if (operaction.equals("1")) {
									httpStart.getServerData(0,
											MyConstant.OPERACTION_BFH_CANSHU,
											"ADD_WS_PARAMETER", userBean
													.getMFG(), userBean
													.getSBU(), th_floor,th_line,
											bfh_ed_modelname.getText()
													.toString().trim(),
											bfh_ed_productname.getText()
													.toString().trim(),
											bfh_ed_botpreheati.getText()
													.toString().trim(),
											bfh_ed_botpreheatii.getText()
													.toString().trim(),
											bfh_ed_botpreheatiii.getText()
													.toString().trim(),
											bfh_ed_botpreheativ.getText()
													.toString().trim(),
											bfh_ed_toppreheati.getText()
													.toString().trim(),
											bfh_ed_toppreheatii.getText()
													.toString().trim(),
											bfh_ed_toppreheatiii.getText().toString()
													.trim(),
											bfh_ed_toppreheativ.getText()
													.toString().trim(),
											bfh_ed_chainspeedafter.getText()
													.toString().trim(),
											bfh_ed_chainspeed.getText()
													.toString().trim(),
											bfh_ed_tintemperature.getText()
													.toString().trim(),
											bfh_ed_tinbathheight.getText()
													.toString().trim(),
											bfh_ed_turbulentwave.getText()
													.toString().trim(),
											bfh_ed_advectionwave.getText()
													.toString().trim(),
											bfh_ed_tinbartype.getText()
													.toString().trim(),
											bfh_ed_fluxtype.getText()
													.toString().trim(),
											bfh_ed_fluxflow.getText()
													.toString().trim(),
											bfh_ed_nozzleairpressure.getText()
													.toString().trim(),
											bfh_ed_totalairpressure.getText()
													.toString().trim(),
											bfh_ed_tankairpressure.getText()
													.toString().trim(),
											bfh_ed_fluxproportion.getText()
													.toString().trim(),
											userBean.getLogonName(),
											ChangString.change(bfh_ed_beizhu.getText().toString()));
								} else {
									httpStart.getServerData(0,
											MyConstant.OPERACTION_BFH_CANSHU,
											"UPDATE_WS_PARAMETER",
											parameternum, userBean.getMFG(),
											userBean.getSBU(), th_floor,th_line,
											bfh_ed_modelname.getText()
													.toString().trim(),
											bfh_ed_productname.getText()
													.toString().trim(),
											bfh_ed_botpreheati.getText()
													.toString().trim(),
											bfh_ed_botpreheatii.getText()
													.toString().trim(),
											bfh_ed_botpreheatiii.getText()
													.toString().trim(),
											bfh_ed_botpreheativ.getText()
													.toString().trim(),
											bfh_ed_toppreheati.getText()
													.toString().trim(),
											bfh_ed_toppreheatii.getText()
													.toString().trim(),
											bfh_ed_toppreheatiii.getText().toString()
													.trim(),
											bfh_ed_toppreheativ.getText()
													.toString().trim(),
											bfh_ed_chainspeedafter.getText()
													.toString().trim(),
											bfh_ed_chainspeed.getText()
													.toString().trim(),
											bfh_ed_tintemperature.getText()
													.toString().trim(),
											bfh_ed_tinbathheight.getText()
													.toString().trim(),
											bfh_ed_turbulentwave.getText()
													.toString().trim(),
											bfh_ed_advectionwave.getText()
													.toString().trim(),
											bfh_ed_tinbartype.getText()
													.toString().trim(),
											bfh_ed_fluxtype.getText()
													.toString().trim(),
											bfh_ed_fluxflow.getText()
													.toString().trim(),
											bfh_ed_nozzleairpressure.getText()
													.toString().trim(),
											bfh_ed_totalairpressure.getText()
													.toString().trim(),
											bfh_ed_tankairpressure.getText()
													.toString().trim(),
											bfh_ed_fluxproportion.getText()
													.toString().trim(),
											userBean.getLogonName(),
											ChangString.change(bfh_ed_beizhu.getText().toString()));
								}
							}
						});
				alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
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

			break;
		// 刪除參數
		case R.id.bt_bfh_detailed_delete:
			AlertDialog alertDialog = new AlertDialog.Builder(context).create();
			alertDialog.setIcon(R.drawable.ic_system);
			alertDialog.setTitle("系統提示:");
			alertDialog.setMessage("是否刪除參數?");
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							httpStart.getServerData(0,
									MyConstant.OPERACTION_BFH_CANSHU,
									"DELETE_WS_PARAMETER", parameternum);
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

			break;
		default:
			break;
		}
	}

	// 返回键按钮
	public void activity_back(View v) {
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
		this.finish();
	}

}
