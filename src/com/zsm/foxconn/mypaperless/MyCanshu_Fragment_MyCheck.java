package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.zsm.foxconn.mypaperless.MyCanshu_Fragment_onging.MyAdapter;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.Canshu_bean;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.refreshlistview.OnRefreshListener;
import com.zsm.foxconn.mypaperless.refreshlistview.RefreshListView;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author mgp
 * 參數 我的審核
 */
public class MyCanshu_Fragment_MyCheck extends Fragment {
	private HttpStart httpStart;
	private Intent intent;
	private String report_num,report_Name,section;
	private View mView;
	private RefreshListView examineListView;
	private MyAdapter adapter;
	private int style = 3, datamore = 20;
	private List<Canshu_bean> listexaminall = new ArrayList<Canshu_bean>();
	private UserBean user;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.mycanshu_frament_mycheck, container,
				false);
		user = (UserBean) getActivity().getApplicationContext();
		CheckLogin();
		intent = getActivity().getIntent();
		report_Name = intent.getStringExtra("report_Name");
		report_num = intent.getStringExtra("report_num");
		section = intent.getStringExtra("section");
		httpStart = new HttpStart(getActivity(), handler);
		httpStart.getServerData(0, MyConstant.CANSHU_ONGING,report_num,"2",datamore + "",user.getLogonName().toString());
		examineListView = (RefreshListView) mView.findViewById(R.id.table_info_listView);
		adapter = new MyAdapter();
		examineListView.setAdapter(adapter);
		examineListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				intent = new Intent(getActivity(), Detailed_canshu.class);
				intent.putExtra("ischeck", "1");
				intent.putExtra("ischeck_ss", "2");
				intent.putExtra("parameternum", listexaminall.get(arg2 - 1)
						.getParameternum());
				intent.putExtra("report_num", report_num);// 报表编号
				intent.putExtra("report_Name", report_Name);// 报表名
				intent.putExtra("section", section);
				intent.putExtra("check_st", listexaminall.get(arg2 - 1)
						.getCheck_by());
				intent.putExtra("canshu_check", listexaminall.get(arg2 - 1)
						.getUpdatetype());
				intent.putExtra("create_date", listexaminall.get(arg2 - 1)
						.getCreate_date());
				startActivity(intent);
			}
		});
		examineListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				SystemClock.sleep(500);
				style = 10;
				datamore = 90;
				adapter.getRefreshData();
			}

			@Override
			public void onLoadMoring() {
				SystemClock.sleep(500);

				style = 10;
				datamore += 30;
				adapter.getRefreshData();
			}
		});
		return mView;
	}
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			int j = 0;// 排的序列
			for (String key : keySet) {
				result = new ArrayList<String>();
				result = msg.getData().getStringArrayList(key);
				if (key.equalsIgnoreCase(MyConstant.CANSHU_ONGING)) {
					if (listexaminall.size() > 0) {
						listexaminall.clear();
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size() - 1; i += 6) {
								++j;
								String updatetp = "";
								String check_statec = "";
								if (result.get(i+2).toString().equals("0")) {
									updatetp = "添加";
								}else if (result.get(i+2).toString().equals("1")) {
									updatetp = "修改";
								}else if (result.get(i+2).toString().equals("2")) {
									updatetp = "刪除";
								}
								if (result.get(i+5).toString().equals("0")) {
									check_statec = "待簽核";
								}else if (result.get(i+5).toString().equals("1")) {
									check_statec = "通過";
								}else if(result.get(i+5).toString().equals("2")){
									check_statec = "拒簽";
								}
								Canshu_bean csbean = new Canshu_bean(j+"", result.get(i).toString(),
										result.get(i+1).toString(), updatetp,
										result.get(i+3).toString(), result.get(i+4).toString(),
										check_statec);
								listexaminall.add(csbean);
							
						}
						if (j<datamore) {
							UIHelper.ToastMessage(getActivity(), "沒有更多了");
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(getActivity(), "暫無數據");
						adapter.notifyDataSetChanged();
						examineListView.onRefreshFinish();
						return;
					}
					adapter.notifyDataSetChanged();
					examineListView.onRefreshFinish();
				}
				
				
				if (key.equalsIgnoreCase(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(getActivity(), "未連接服務器");
				}
			}
		}
	};
	
	
	// 适配器，显示查询的信息
	class MyAdapter extends BaseAdapter {

		public void getRefreshData() {
			httpStart.getServerData(0, MyConstant.CANSHU_ONGING,report_num,"2",datamore + "",user.getLogonName().toString());
		}

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
					getActivity().getApplicationContext(),
					R.layout.canshu_mycheck_item, null);
			Canshu_bean listex = listexaminall.get(position);
			String sataend = null;
			final TextView mycheck_nb_tv = (TextView) item
					.findViewById(R.id.mycheck_nb_tv);
			final TextView mycheck_jizhong_numbertv = (TextView) item
					.findViewById(R.id.mycheck_jizhong_numbertv);
			final TextView mycheck_updatetype_tv = (TextView) item
					.findViewById(R.id.mycheck_updatetype_tv);
			final TextView mycheck_create_by = (TextView) item
					.findViewById(R.id.mycheck_create_by);
			final TextView mycheck_datatime_tv = (TextView) item
					.findViewById(R.id.mycheck_datatime_tv);
			final TextView mycheck_check_state = (TextView) item
					.findViewById(R.id.mycheck_check_state);
			mycheck_nb_tv.setText(listex.getNumberorder());
			mycheck_jizhong_numbertv.setText(listex.getJizhong_num());
			mycheck_updatetype_tv.setText(listex.getUpdatetype());
			mycheck_create_by.setText(listex.getCreate_by());
			mycheck_datatime_tv.setText(listex.getCreate_date().substring(0, 10));
			mycheck_check_state.setText(listex.getCheck_by());
			if (listex.getUpdatetype().toString().equals("刪除")) {
				mycheck_updatetype_tv.setTextColor(getActivity().getResources().getColor(
						R.color.color1));
			}
			if (listex.getUpdatetype().toString().equals("添加")) {
				mycheck_updatetype_tv.setTextColor(getActivity().getResources().getColor(
						R.color.blue));
			}
			if (listex.getCheck_by().equals("拒簽")) {
				mycheck_check_state.setTextColor(getActivity().getResources().getColor(
						R.color.color1));
			}
			if (listex.getCheck_by().equals("待簽核")) {
				mycheck_check_state.setTextColor(getActivity().getResources().getColor(
						R.color.color4));
			}
			if (listex.getCheck_by().equals("通過")) {
				mycheck_check_state.setTextColor(getActivity().getResources().getColor(
						R.color.back));
			}
			return item;
		}

	}
	
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		if (user.getLogonName() == null || user.getLogonName().length() == 0) {
			android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// tiemTimelistenresiger.stopthread();
					AppManager.getInstance().killAllActivity();
					startActivity(new Intent(getActivity(), LoginActivity.class));
				}
			};
			Builder builder = new Builder(getActivity());
			builder.setTitle("您还未登陆，请先登录");
			builder.setPositiveButton("确定", listener);
			builder.create().show();
		}
	}
}
