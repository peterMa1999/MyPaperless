package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * Copyright (c) 2016, CNSBG SFC IT
 *
 * @Description: 點檢修改-駁回 
 *
 * @author TSY   
 *
 * @date 2016-11-10 上午11:20:38
 */
public class RejectAuditModifyFragment extends Fragment {
	private HttpStart httpStart;
	private String  number, session, station,check_report_no,report_num;
	private View mView;
	private String[] datalist;
	private RefreshListView examineListView;
	private MyAdapter adapter;
	private int style = 3, datamore = 20;
	private UserBean user;
	private Boolean isdatalist = false;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();// 集合
	private String str = "0";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.mycanshu_frament_mycheck, container,
				false);
		user = (UserBean) getActivity().getApplicationContext();
		CheckLogin();
		httpStart = new HttpStart(getActivity(), handler);
		httpStart.getServerData(0, MyConstant.GET_CHECK_REJECT, user.getLogonName()
				.toString());
		examineListView = (RefreshListView) mView
				.findViewById(R.id.table_info_listView);
		adapter = new MyAdapter();
		examineListView.setAdapter(adapter);
		examineListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				session = list.get(position-1).get("session").toString();
				check_report_no = list.get(position-1).get("check_report_no").toString();
				number = list.get(position-1).get("number").toString();
				report_num = list.get(position-1).get("report_num").toString();
				Intent intent = new Intent(getActivity(),
						Examine_mes_moreActivity.class);
				intent.putExtra("report_num", report_num);
				intent.putExtra("RepostNO", check_report_no);
				intent.putExtra("report_Name", number);
				intent.putExtra("linearid", "1");
				intent.putExtra("createby_status", "2");
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

	public void Showdatalist() {
		if (list.size() > 0) {
			list.clear();
		}
		if (isdatalist) {
			if (datalist[0].toString().equalsIgnoreCase("null")
					|| datalist[0].toString().equals(null)) {
				UIHelper.ToastMessage(getActivity(), "暂无数据");
				return;
			} else {
				// 遍历
				for (int i = 0; i < datalist.length - 1; i += 7) {

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("number", datalist[i].toString());
					map.put("session", datalist[i + 1].toString());
					map.put("floor", datalist[i + 2].toString());
					map.put("creat_date", datalist[i + 3].toString().substring(0, 16));
					map.put("line", datalist[i + 4].toString());
					map.put("check_report_no", datalist[i + 5].toString());
					map.put("report_num", datalist[i + 6].toString());
					list.add(map);
				}
			}
		}
		adapter.notifyDataSetChanged();

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
				if (key.equalsIgnoreCase(MyConstant.GET_CHECK_REJECT)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						if (str.equals("2")) {
							int count = 0;
							for (int i = 1; i < result.size(); i += 7) {
								if (result.get(i).toString().contains(station)) {
									count++;
								}
							}
							datalist = new String[count * 3];
							for (int i = 1, k = 0; i < result.size(); i += 7) {
								if (result.get(i).toString().contains(station)) {
									datalist[k] = result.get(i);
									datalist[k + 1] = result.get(i + 1);
									datalist[k + 2] = result.get(i + 2);
									datalist[k + 3] = result.get(i + 3);
									datalist[k + 4] = result.get(i + 4);
									datalist[k + 5] = result.get(i + 5);
									datalist[k + 6] = result.get(i + 6);
									k += 7;
								}
							}
						} else {
							datalist = new String[result.size() - 1];
							for (int i = 1; i < result.size(); i++) {
								datalist[i - 1] = result.get(i);
							}
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(getActivity(), "暫無數據");
						adapter.notifyDataSetChanged();
						examineListView.onRefreshFinish();
						return;
					}
					if (result.get(0).equals(MyConstant.GET_FLAG_NULL)) {
						datalist = new String[result.size()];
						datalist[0] = "null";
					}
					adapter.notifyDataSetChanged();
					examineListView.onRefreshFinish();

					isdatalist = true;
					Showdatalist();

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
			httpStart.getServerData(0, MyConstant.GET_CHECK_REJECT, user
					.getLogonName().toString());
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			View item = convertView != null ? convertView : View.inflate(
					getActivity().getApplicationContext(), R.layout.aduit_item,
					null);

			TextView textView = (TextView) item.findViewById(R.id.listreportno);
			TextView textView1 = (TextView) item.findViewById(R.id.listcheckid);
			TextView textView2 = (TextView) item.findViewById(R.id.listdata);
			TextView textView3 = (TextView) item.findViewById(R.id.list_line);

			textView.setText(list.get(position).get("number").toString());
			textView1.setText(list.get(position).get("floor").toString());
			textView2.setText(list.get(position).get("creat_date").toString());
			textView3.setText(list.get(position).get("line").toString());
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
