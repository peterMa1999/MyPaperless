package com.zsm.foxconn.mypaperless.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zsm.foxconn.mypaperless.CheckPdActivity;
import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.bean.CheckHolder;
import com.zsm.foxconn.mypaperless.help.UIHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CheckeAdapter3 extends BaseAdapter {
	private List<String> list = new ArrayList<String>();
	private HashMap<Integer, Boolean> isSelected = null;
	private Context context;
	private LayoutInflater inflater = null;
	List<String> data=new ArrayList<String>(); 
	String issection;
	public CheckeAdapter3(List<String> list, Context context,String issection) 
	{
		this.context = context;
		this.list = list;
		this.issection = issection;
		initDate();
		inflater = LayoutInflater.from(context);
	}
	public void CheckeAdapterintent(List<String> arg) 
	{
		this.data=arg;
	}

	public void initDate() {
		System.out.println("initDate>>>>>>>>>>>>>>>>>>>>>>>>>>");
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < list.size(); i++) {
			isSelected.put(i, false);
			Log.i("ad", isSelected.get(i).toString());
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_lines_item_pd_3,
					null);
			holder = new CheckHolder();
			holder.lines = (TextView) convertView
					.findViewById(R.id.textViewline);
			holder.flagCheckBox = (CheckBox) convertView
					.findViewById(R.id.flagCheckBox);
			holder.check_bt_img=(ImageButton) convertView.findViewById(R.id.check_bt_img);
			convertView.setTag(holder);
		} else {
			holder = (CheckHolder) convertView.getTag();
		}
		holder.lines.setText(list.get(position).toString());
		holder.flagCheckBox.setChecked(getIsSelected().get(position));
		holder.check_bt_img.setFocusable(false);
		final int arg=position;
		try {
			holder.check_bt_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,CheckPdActivity.class);
				intent.putExtra("reportName",data.get(0).toString());
			    intent.putExtra("report_Num",data.get(1).toString());
			    intent.putExtra("selectLine",list.get(arg).toString());
			    intent.putExtra("section",data.get(2).toString());
			    intent.putExtra("flagfloor",data.get(3).toString());
			    intent.putExtra("issection", issection);
			    ((Activity) context).startActivityForResult(intent, 1);
			    ((Activity) context).overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
			}
		});
		} catch (Exception e) {
		}
		
		return convertView;
	}

	public HashMap<Integer, Boolean> getIsSelected() 
	{
		return isSelected;
	}

}
