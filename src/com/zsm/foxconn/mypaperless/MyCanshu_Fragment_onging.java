package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.Canshu_bean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.refreshlistview.OnRefreshListener;
import com.zsm.foxconn.mypaperless.refreshlistview.RefreshListView;

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
 *	參數正在審核中列表
 */
public class MyCanshu_Fragment_onging extends Fragment {
	private HttpStart httpStart;
	private Intent intent;
	private String report_num,report_Name,section;
	private View mView;
	private RefreshListView examineListView;
	private MyAdapter adapter;
	private int style = 3, datamore = 20;
	private List<Canshu_bean> listexaminall = new ArrayList<Canshu_bean>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.mycanshu_frament_onging, container,
				false);
		intent = getActivity().getIntent();
		report_Name = intent.getStringExtra("report_Name");
		report_num = intent.getStringExtra("report_num");
		section = intent.getStringExtra("section");
		httpStart = new HttpStart(getActivity(), handler);
		httpStart.getServerData(0, MyConstant.CANSHU_ONGING,report_num,"1",datamore + "");
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
				intent.putExtra("ischeck_ss", "0");
				intent.putExtra("parameternum", listexaminall.get(arg2 - 1)
						.getParameternum());
				intent.putExtra("report_num", report_num);// 报表编号
				intent.putExtra("report_Name", report_Name);// 报表名
				intent.putExtra("section", section);
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
						for (int i = 1; i < result.size() - 1; i += 8) {
							if (result.get(i+5).toString().equals("0")) {
								++j;
								String updatetp = "";
								if (result.get(i+2).toString().equals("0")) {
									updatetp = "添加";
								}else if (result.get(i+2).toString().equals("1")) {
									updatetp = "修改";
								}else if (result.get(i+2).toString().equals("2")) {
									updatetp = "刪除";
								}
								Canshu_bean csbean = new Canshu_bean(j+"", result.get(i).toString(),
										result.get(i+1).toString(), updatetp,
										result.get(i+3).toString(), result.get(i+4).toString(),
										result.get(i+6).toString());
								listexaminall.add(csbean);
							}
							
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
			httpStart.getServerData(0, MyConstant.CANSHU_ONGING,report_num,"1",datamore + "");
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
					R.layout.canshu_onging_item, null);
			Canshu_bean listex = listexaminall.get(position);
			String sataend = null;
			final TextView onging_nb_tv = (TextView) item
					.findViewById(R.id.onging_nb_tv);
			final TextView onging_jizhong_numbertv = (TextView) item
					.findViewById(R.id.onging_jizhong_numbertv);
			final TextView onging_updatetype_tv = (TextView) item
					.findViewById(R.id.onging_updatetype_tv);
			final TextView onging_create_by = (TextView) item
					.findViewById(R.id.onging_create_by);
			final TextView onging_datatime_tv = (TextView) item
					.findViewById(R.id.onging_datatime_tv);
			final TextView onging_check_by = (TextView) item
					.findViewById(R.id.onging_check_by);
			onging_nb_tv.setText(listex.getNumberorder());
			onging_jizhong_numbertv.setText(listex.getJizhong_num());
			onging_updatetype_tv.setText(listex.getUpdatetype());
			onging_create_by.setText(listex.getCreate_by());
			onging_datatime_tv.setText(listex.getCreate_date());
			onging_check_by.setText(listex.getCheck_by());
			if (listex.getUpdatetype().toString().equals("刪除")) {
				onging_updatetype_tv.setTextColor(getActivity().getResources().getColor(
						R.color.color1));
			}
			if (listex.getUpdatetype().toString().equals("添加")) {
				onging_updatetype_tv.setTextColor(getActivity().getResources().getColor(
						R.color.blue));
			}
			return item;
		}

	}
}
