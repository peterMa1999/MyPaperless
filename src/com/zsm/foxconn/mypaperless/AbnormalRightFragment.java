package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

public class AbnormalRightFragment extends Fragment {
	private ListView listview;
	HttpStart start;
	UserBean userBean;
	Map<String,Object> map=null;
	List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	private SimpleAdapter myAdapter=null;
	private View view=null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_right_abnormal_layout, container, false);
		userBean = (UserBean) getActivity().getApplicationContext();
		start = new HttpStart(getActivity(), handler);
		findViewById();
		initView();
		start.getServerData(0,MyConstant.GET_DEAL_ABNORMAL_RESULT,userBean.getLogonName());
		return view;
	}
	protected void findViewById() 
	{
		// TODO Auto-generated method stub
		listview=(ListView) view.findViewById(R.id.ab_listview);
		myAdapter=new SimpleAdapter(
				getActivity(),
				list,
				R.layout.abnormal_right_item,
				new String[]{"ReportNum","dealresult","content","commit_time"},
				new int[]{R.id.Report_tv,R.id.result_tv,R.id.listdata,R.id.commit_time_tv});
		listview.setAdapter(myAdapter);
	}

	protected void initView() {
		// TODO Auto-generated method stub
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (list.get(arg2).get("dealresult").toString().trim().equalsIgnoreCase("驳回"))
				{
					Intent intent = new Intent(getActivity(),Abnormal_right_itemActivity.class);
					intent.putExtra("id", list.get(arg2).get("id").toString().trim());
					startActivity(intent);
				}else if (list.get(arg2).get("dealresult").toString().trim().equalsIgnoreCase("待处理"))
				{
					Intent intent=new Intent(getActivity(),Abnormal_left_itemActivity.class);
					intent.putExtra("id",list.get(arg2).get("id").toString().trim());
					startActivity(intent);
				}
				
			}
		});
//		listview.setOnLongClickListener(new OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) 
//			{
//				// TODO Auto-generated method stub
//				
//				return true;
//			}
//		});
	}
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) // 键值对
			{
				if (key.equalsIgnoreCase(MyConstant.GET_DEAL_ABNORMAL_RESULT))
				{
					result = msg.getData().getStringArrayList(key);
					if (list.size()>0)
					{
						list.clear();
					}
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE))
					{
						for (int i =1; i <result.size(); i+=6) 
						{
							map=new HashMap<String, Object>();
							map.put("id",result.get(i));
							map.put("ReportNum",result.get(i+1));
							map.put("content",result.get(i+3));
							map.put("commit_time",result.get(i+4));
							if (result.get(i+5).equalsIgnoreCase("1"))
							{
								map.put("dealresult","完成");
							}else if (result.get(i+5).equalsIgnoreCase("0"))
							{
								map.put("dealresult","待处理");
							}else if (result.get(i+5).equalsIgnoreCase("2"))
							{
								map.put("dealresult","驳回");
							}
							list.add(map);
						}
					}else {
						UIHelper.ToastMessage(getActivity(),"獲取失敗或者您暫無異常");
					}
					myAdapter.notifyDataSetChanged();
				}
			}
		}
	};
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		start.getServerData(0,MyConstant.GET_DEAL_ABNORMAL_RESULT,userBean.getLogonName());
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (hidden) {
			start.getServerData(0,MyConstant.GET_DEAL_ABNORMAL_RESULT,userBean.getLogonName());
		}
	}
}
