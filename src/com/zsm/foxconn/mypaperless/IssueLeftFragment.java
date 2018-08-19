package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.zsm.foxconn.mypaperless.IssueRightFragment.MyAdapter;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.Canshu_bean;
import com.zsm.foxconn.mypaperless.bean.Issue_bean;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class IssueLeftFragment extends Fragment {
	private ListView listview;
	HttpStart start;
	UserBean userBean;
	private MyAdapter adapter;
	private View view = null;
	private List<Issue_bean> listexaminall = new ArrayList<Issue_bean>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_left_issue_layout, container,
				false);
		userBean = (UserBean) getActivity().getApplicationContext();
		start = new HttpStart(getActivity(), handler);
		findViewById();
		initView();
		start.getServerData(0, MyConstant.GetIPQCCrossBUIssue,
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
				if (key.equalsIgnoreCase(MyConstant.GetIPQCCrossBUIssue)) {
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						if (listexaminall.size()>0) {
							listexaminall.clear();
						}
						for (int i = 1; i < result.size(); i+=6) {
							Issue_bean issue = new Issue_bean(result.get(i+1).toString().trim(),
									result.get(i+2).toString().trim(), result.get(i+3).toString().trim(),
									result.get(i+4).toString().trim(), result.get(i+5).toString().trim());
							listexaminall.add(issue);
						}
						adapter.notifyDataSetChanged();
					}
				}
				
				if (key.equalsIgnoreCase(MyConstant.SAVE_IPQCCrossBUIssue)) {
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(getActivity(), "添加成功");
					}else if (result.get(0).equalsIgnoreCase("Issue is exit")) {
						UIHelper.ToastMessage(getActivity(), "已經添加過了~");
					}else {
						UIHelper.ToastMessage(getActivity(), "添加失敗，請重試");
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
			final ImageButton issue_add_imagebt = (ImageButton) item
					.findViewById(R.id.issue_add_imagebt);
			issue_add_imagebt.setVisibility(View.VISIBLE);
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
			issue_add_imagebt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
					alertDialog.setIcon(R.drawable.ic_system);
					alertDialog.setTitle("系統提示:");
					alertDialog.setMessage("是否添加到CrossBU Issue?");
					alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									String Occurred = listex.getOccurred();
									String RootCause = listex.getRootCause();
									String FailureCode = listex.getFailureCode();
									String Location = listex.getLocation();
									String Section = listex.getSection();
									start.getServerData(0, MyConstant.SAVE_IPQCCrossBUIssue,
											Occurred,RootCause,FailureCode,Location,Section,userBean.getLogonName());
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
				}
			});
			
			return item;
		}

	}
}
