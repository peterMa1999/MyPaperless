package com.zsm.foxconn.mypaperless.adapter;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.TextView;

public class CheckeAdapter extends BaseAdapter {
	private ArrayList<String> list = new ArrayList<String>();
	private HashMap<Integer, Boolean> isSelected = null;
	private Context context;
	private LayoutInflater inflater = null;

	public CheckeAdapter(ArrayList<String> list, Context context) 
	{
		this.context = context;
		this.list = list;
		initDate();
		inflater = LayoutInflater.from(context);
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
			convertView = inflater.inflate(R.layout.listview_lines_item_pd,
					null);
			holder = new CheckHolder();
			holder.lines = (TextView) convertView
					.findViewById(R.id.textViewline);
			holder.flagCheckBox = (CheckBox) convertView
					.findViewById(R.id.flagCheckBox);
			convertView.setTag(holder);
		} else {
			holder = (CheckHolder) convertView.getTag();
		}
		holder.lines.setText(list.get(position).toString());
		holder.flagCheckBox.setChecked(getIsSelected().get(position));
		return convertView;
	}

	public HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

}
