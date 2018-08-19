package com.zsm.foxconn.mypaperless.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.R.id;
import com.zsm.foxconn.mypaperless.R.layout;
import com.zsm.foxconn.mypaperless.bean.CheckHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

public class CheckeAdapter_eCheck extends BaseAdapter {
	private List<Map<String,String>>  list = new ArrayList<Map<String,String>>();
	private Context context;
	private LayoutInflater inflater = null;

	public CheckeAdapter_eCheck(List<Map<String,String>> list, Context context) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
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
			convertView = inflater.inflate(R.layout.listview_item_echeck,
					null);
			holder = new CheckHolder();
			holder.textViewperson=(TextView) convertView
			.findViewById(R.id.e_textViewperson);
			holder.lines = (TextView) convertView
					.findViewById(R.id.e_textViewline);
			holder.flagCheckBox = (CheckBox) convertView
					.findViewById(R.id.e_flagadd);
			convertView.setTag(holder);
		} else {
			holder = (CheckHolder) convertView.getTag();
		}
		holder.lines.setText(list.get(position).get("LogonName").toString());
		holder.textViewperson.setText(list.get(position).get("ChineseName").toString());
		return convertView;
	}

}
