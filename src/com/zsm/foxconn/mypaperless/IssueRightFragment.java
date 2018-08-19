package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.Issue_bean;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

public class IssueRightFragment extends Fragment {
	private ListView listview;
	HttpStart start;
	UserBean userBean;
	private MyAdapter adapter;
	private View view = null;
	private List<Issue_bean> listexaminall = new ArrayList<Issue_bean>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_right_issue_layout,
				container, false);
		userBean = (UserBean) getActivity().getApplicationContext();
		start = new HttpStart(getActivity(), handler);
		findViewById();
		initView();
		start.getServerData(0, MyConstant.Get_BU_IPQCCrossBUIssue,
				userBean.getMFG());
		return view;
	}

	protected void findViewById() {
		// TODO Auto-generated method stub
		listview = (ListView) view.findViewById(R.id.issueright_listview);
		adapter = new MyAdapter();
		listview.setAdapter(adapter);
	}

	protected void initView() {
		// TODO Auto-generated method stub
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) // 键值对
			{
				result = msg.getData().getStringArrayList(key);
				if (key.equalsIgnoreCase(MyConstant.Get_BU_IPQCCrossBUIssue)) {
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						if (listexaminall.size()>0) {
							listexaminall.clear();
						}
						for (int i = 1; i < result.size(); i+=9) {
							Issue_bean issue = new Issue_bean(result.get(i+1).toString().trim(),
									result.get(i+2).toString().trim(), result.get(i+3).toString().trim(),
									result.get(i+4).toString().trim(), result.get(i+5).toString().trim(),
									result.get(i+6).toString().trim(), result.get(i+7).toString().trim(),
									result.get(i+8).toString().trim());
							listexaminall.add(issue);
						}
						adapter.notifyDataSetChanged();
					}
				}
				if (key.equals(
						MyConstant.GET_FLAG_UNUNITED)) {
					Toast.makeText(getActivity(), "未连接服务器....", 0).show();
					return;
				}
			}
		}
	};

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
					getActivity(), R.layout.issue_listview_item, null);
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
			final TextView issue_infoto_tv = (TextView) item
					.findViewById(R.id.issue_infoto_tv);
			issue_infoto_tv.setVisibility(View.VISIBLE);
			issue_rootcause_tv.setText("發生原因:"+listex.getRootCause());
			issue_location_tv.setText(listex.getLocation());
			issue_failurecode_tv.setText(listex.getFailureCode());
			issue_section_tv.setText("工段:"+listex.getSection());
			issue_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UIHelper.ToastMessage(getActivity(), "發生地:"+listex.getOccurred());
				}
			});
			issue_infoto_tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String RootCause = listex.getRootCause();
					String FailureCode = listex.getFailureCode();
					String Location = listex.getLocation();
					
					Intent intent = new Intent(getActivity(),IPQC_Issue_Info.class);
					intent.putExtra("RootCause", RootCause);
					intent.putExtra("FailureCode", FailureCode);
					intent.putExtra("Location", Location);
					startActivity(intent);
					}
			});
			return item;
		}

	}
}
