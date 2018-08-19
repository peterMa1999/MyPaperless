package com.zsm.foxconn.mypaperless.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context context;

	protected LayoutInflater mInflater;

	protected List<T> datas;

	private int layoutId;

	public CommonAdapter(Context context, List<T> datas, int layoutId) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.datas = datas;
		this.layoutId = layoutId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(context, convertView, parent,
				layoutId, position);
		convert(holder, getItem(position));
		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder holder, T t);

}
