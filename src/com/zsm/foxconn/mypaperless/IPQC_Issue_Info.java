package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.Issue_bean;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

public class IPQC_Issue_Info extends BaseActivity{
	
	private ListView issue_info_listview;
	private MyAdapter adapter;
	private List<Issue_bean> listexaminall = new ArrayList<Issue_bean>();
	private Context context = IPQC_Issue_Info.this;
	private HttpStart start;
	UserBean userBean;
	private String RootCause,FailureCode,Location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ipqc_issue_info);
		userBean = (UserBean) getApplicationContext();
		Intent intent = getIntent();
		RootCause = intent.getStringExtra("RootCause");
		FailureCode = intent.getStringExtra("FailureCode");
		Location = intent.getStringExtra("Location");
		findViewById();
		start = new HttpStart(context, handler);
		start.getServerData(0, MyConstant.GET_IPQCCrossBUIssue,RootCause,FailureCode,Location
				);
	}
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) // 键值对
			{
				result = msg.getData().getStringArrayList(key);
				if (key.equalsIgnoreCase(MyConstant.GET_IPQCCrossBUIssue)) {
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						if (listexaminall.size()>0) {
							listexaminall.clear();
						}
						for (int i = 1; i < result.size(); i+=9) {
							Issue_bean issue = new Issue_bean(result.get(i).toString(),
									result.get(i+6).toString(),result.get(i+7).toString(),
									result.get(i+8).toString());
							listexaminall.add(issue);
						}
						adapter.notifyDataSetChanged();
					}else if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無數據");
					}
				}
				if (key.equalsIgnoreCase(MyConstant.update_BU_IPQCCrossBUIssue)) {
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "更改成功");
					}else {
						UIHelper.ToastMessage(context, "更改失敗");
					}
				}
				
				if (key.equals(
						MyConstant.GET_FLAG_UNUNITED)) {
					Toast.makeText(context, "未连接服务器....", 0).show();
					return;
				}
			}
		}
	};
	
	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		issue_info_listview = (ListView) findViewById(R.id.issue_info_listview);
		adapter = new MyAdapter();
		issue_info_listview.setAdapter(adapter);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	// 适配器，显示查询的信息
	class MyAdapter extends BaseAdapter {

			public int getCount() {
				return listexaminall.size();
			}

			public Object getItem(int position) {
				return listexaminall.get(position);
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				View item = convertView != null ? convertView : View.inflate(
						context, R.layout.issue_listview_item, null);
				final Issue_bean listex = listexaminall.get(position);

				final TextView issue_rootcause_tv = (TextView) item
						.findViewById(R.id.issue_rootcause_tv);
				final TextView issue_location_tv = (TextView) item
						.findViewById(R.id.issue_location_tv);
				final TextView issue_failurecode_tv = (TextView) item
						.findViewById(R.id.issue_failurecode_tv);
				final TextView issue_section_tv = (TextView) item
						.findViewById(R.id.issue_section_tv);
				final LinearLayout issue_layout = (LinearLayout) item
						.findViewById(R.id.issue_layout);
				final Switch issue_infoto_switch = (Switch) item
						.findViewById(R.id.issue_infoto_switch);
				issue_infoto_switch.setVisibility(View.VISIBLE);
				issue_rootcause_tv.setText("BU:"+listex.getBU_NAME());
				issue_failurecode_tv.setText("修改日期："+listex.getCreate_Date().substring(0, 16));
				issue_section_tv.setText("By："+listex.getLastEditBy());
				if (listex.getIssue_Flag().equals("0")) {
					issue_location_tv.setText("異常開啟");
					issue_infoto_switch.setChecked(true);
				}else {
					issue_location_tv.setText("異常關閉");
					issue_location_tv.setTextColor(getResources().getColor(
						R.color.color1));
					issue_infoto_switch.setChecked(false);
				}
				
				issue_infoto_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
							if (listex.getBU_NAME().equalsIgnoreCase(userBean.getMFG())) {
								if (isChecked) {
									start.getServerData(0, MyConstant.update_BU_IPQCCrossBUIssue,"open",userBean.getLogonName(),userBean.getMFG(),RootCause,FailureCode,Location
											);
									UIHelper.ToastMessage(context, "打開異常");
								}else {
									start.getServerData(0, MyConstant.update_BU_IPQCCrossBUIssue,"close",userBean.getLogonName(),userBean.getMFG(),RootCause,FailureCode,Location
											);
									UIHelper.ToastMessage(context, "關閉異常");
								}
							}else {
								issue_infoto_switch.setEnabled(false);
								UIHelper.ToastMessage(context, "更改無效，您無權限更改其他BU狀態",5000);
							}
					}
				});
				return item;
			}

		}
		
		public void back(View v) {
			this.finish();
			overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
		}
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				// intent.putExtra("bUname", bUname);
				// intent.putExtra("issection", issection);
				// startActivity(intent);
				overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
				this.finish();
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
}
