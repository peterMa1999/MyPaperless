package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.Set;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

/**
 * 
 * @author mgp 2016/07/12
 *	添加樓層
 */
public class Add_floor extends BaseActivity{
	private ImageView imageview_add;
	private TextView title,tv_section;
	private Spinner spinnerfloor,sbu_spinner;
	private UserBean userBean;
	private HttpStart httpStart;
	private Context context = Add_floor.this;
	private String[] sbustr = null,floorstr = null;
	private String floor_name,line_name,sbu_name,section,operating,sbu_name_old,floor_name_old,line_name_old;
	private Intent intent;
	private Button bt_Submit;
	private EditText ed_line_name;
	private boolean isnull = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_floor);
		userBean = (UserBean) getApplicationContext();
		intent = getIntent();
		section = intent.getStringExtra("section");
		operating = intent.getStringExtra("operating");
		httpStart = new HttpStart(context, handler);
		CheckLogin();
		findViewById();
		httpStart.getServerData(0,MyConstant.GET_ALL_FLOOR_BU,userBean.getBU(),userBean.getSite(),userBean.getBG());
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
					startActivity(new Intent(Add_floor.this, LoginActivity.class));
				}
			};
			Builder builder = new Builder(Add_floor.this);
			builder.setTitle("您还未登陆，请先登录");
			builder.setPositiveButton("确定", listener);
			builder.create().show();
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = msg.getData().getStringArrayList(key);
				if (key.equalsIgnoreCase(MyConstant.GET_ALL_FLOOR_BU)) {
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						floorstr = new String[result.size()-1];
						for (int i = 1; i < result.size(); i++) {
							floorstr[i-1] = result.get(i).toString();
						}
						isnull = true;
					}
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						floorstr = new String[result.size()];
						floorstr[0] = MyConstant.GET_FLAG_NULL;
						
					}
					httpStart.getServerData(0,MyConstant.GET_PEIZHI_SBU,userBean.getBU(),userBean.getMFG());
					spinnerfloor.setAdapter(new ArrayAdapter(
							Add_floor.this,
							android.R.layout.simple_dropdown_item_1line,
							floorstr));
					for (int i = 0; i < floorstr.length; i++) {
						if (floorstr[i].equals(floor_name_old)) {
							spinnerfloor.setSelection(i, true);
						}
					}
					floor_name = spinnerfloor.getSelectedItem().toString();
					
				}
				if (key.equalsIgnoreCase(MyConstant.GET_PEIZHI_SBU)) {
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
					sbu_spinner.setAdapter(new ArrayAdapter(
							Add_floor.this,
							android.R.layout.simple_dropdown_item_1line,
							sbustr));
					for (int i = 0; i < sbustr.length; i++) {
						if (sbustr[i].equals(sbu_name_old)) {
							sbu_spinner.setSelection(i, true);
						}
					}
					sbu_name = sbu_spinner.getSelectedItem().toString();
					
				}
				if (key.equalsIgnoreCase(MyConstant.INSERT_INTO_CHECK_LINE)) {
					if (result.get(0).equalsIgnoreCase("Line_Exit")) {
						UIHelper.ToastMessage(context, "線別或點檢物已存在");
						return;
					}
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "添加成功");
					}
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "添加失敗");
					}
				}
				if (key.equalsIgnoreCase(MyConstant.UPDATE_FLOOR_LINE)) {
					if (result.get(0).equalsIgnoreCase("Line_Exit")) {
						UIHelper.ToastMessage(context, "線別或點檢物已存在");
						return;
					}
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "修改成功");
					}
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "修改失敗");
					}
				}
				if (key.equalsIgnoreCase(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(context, "未連接服務器");
				}
			}
		}
	};
	
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		imageview_add = (ImageView) findViewById(R.id.imageview_add);
		imageview_add.setVisibility(View.GONE);
		title = (TextView) findViewById(R.id.bartitle_txt);
		if (operating.equals("1")) {
			title.setText(getResources().getString(R.string.update_floor));
			sbu_name_old = intent.getStringExtra("sbu_name");
			floor_name_old = intent.getStringExtra("floor_name");
			line_name_old = intent.getStringExtra("line_name");
		}else {
			title.setText(getResources().getString(R.string.add_floor));
		}
		tv_section = (TextView) findViewById(R.id.tv_section);
		tv_section.setText(section);
		bt_Submit = (Button) findViewById(R.id.bt_Submit);
		ed_line_name = (EditText) findViewById(R.id.ed_line_name);
		if (operating.equals("1")) {
			ed_line_name.setText(line_name_old);
		}
		spinnerfloor = (Spinner) findViewById(R.id.spinnerfloor);
		sbu_spinner = (Spinner) findViewById(R.id.sbu_spinner);
		spinnerfloor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				floor_name = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		sbu_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sbu_name = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		bt_Submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				line_name = ChangString.change(ed_line_name.getText().toString());
				if (line_name.length()==0||floor_name.length()==0) {
					UIHelper.ToastMessage(context, "空的內容");
				}else if (line_name.length()>10) {
					UIHelper.ToastMessage(context, "字符長度不能大於10");
				}else{
					if (operating.equals("1")) {
						httpStart.getServerData(0,MyConstant.UPDATE_FLOOR_LINE,userBean.getMFG(),sbu_name,floor_name,line_name,section,sbu_name_old,floor_name_old,line_name_old);
					}else if(operating.equals("0")){
						httpStart.getServerData(0,MyConstant.INSERT_INTO_CHECK_LINE,userBean.getMFG(),sbu_name,floor_name,line_name,section);
					}
				}
				
			}
		});
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
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
				overridePendingTransition(R.anim.move_left_in_activity,
						R.anim.move_right_out_activity);
				this.finish();
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}

}
