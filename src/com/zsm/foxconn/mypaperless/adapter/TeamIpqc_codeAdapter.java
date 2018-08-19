package com.zsm.foxconn.mypaperless.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.bean.TeamIpqc_codebean;

public class TeamIpqc_codeAdapter extends BaseAdapter {

	/**
	 * 上下文对象
	 */
	private Context context = null;

	/**
	 * 数据集合
	 */
	private List<TeamIpqc_codebean> datas = null;

	/**
	 * CheckBox 是否选择的存储集合,key 是 position , value 是该position是否选中
	 */
	private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();

	public TeamIpqc_codeAdapter(Context context, List<TeamIpqc_codebean> datas) {
		this.datas = datas;
		this.context = context;

		// 初始化,默认都没有选中
		configCheckMap(false);
		notifyDataSetChanged();
	}

	/**
	 * 首先,默认情况下,所有项目都是没有选中的.这里进行初始化
	 */
	public void configCheckMap(boolean bool) {

		for (int i = 0; i < datas.size(); i++) {
			isCheckMap.put(i, bool);
		}
	}

	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	public Object getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewGroup layout = null;

		/**
		 * 进行ListView 的优化
		 */
		if (convertView == null) {
			layout = (ViewGroup) LayoutInflater.from(context).inflate(
					R.layout.listview_code, parent, false);
		} else {
			layout = (ViewGroup) convertView;
		}

		TeamIpqc_codebean bean = datas.get(position);

		/*
		 * 获得该item 是否允许删除
		 */
		boolean canRemove = bean.isCanRemove();

		/*
		 * 设置每一个item的文本
		 */
		TextView tvTitle0 = (TextView) layout.findViewById(R.id.LoCeng);
		TextView tvTitle1 = (TextView) layout.findViewById(R.id.Report_name);
		TextView tvTitle2 = (TextView) layout.findViewById(R.id.Line_level);
		TextView tvTitle3 = (TextView) layout.findViewById(R.id.Control_no);
		tvTitle0.setText("楼层:" + bean.getFlooR_NAME());
		tvTitle1.setText(bean.getReport_name());
		tvTitle2.setText("线别:" + bean.getLine_level());
		tvTitle3.setText(bean.getControl_no());
		// "设备编号:" +

		/*
		 * 获得单选按钮
		 */
		CheckBox cbCheck = (CheckBox) layout.findViewById(R.id.codeCheckBox);

		/*
		 * 设置单选按钮的选中
		 */
		cbCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				/*
				 * 将选择项加载到map里面寄存
				 */
				isCheckMap.put(position, isChecked);
			}
		});

		if (!canRemove) {
			// 隐藏单选按钮,因为是不可删除的
			cbCheck.setVisibility(View.INVISIBLE);
			cbCheck.setChecked(false);
		} else {
			cbCheck.setVisibility(View.VISIBLE);

			if (isCheckMap.get(position) == null) {
				isCheckMap.put(position, false);
			}

			cbCheck.setChecked(isCheckMap.get(position));

			ViewHolder2 holder = new ViewHolder2();

			holder.cbCheck = cbCheck;
			holder.tvTitle0 = tvTitle0;
			holder.tvTitle1 = tvTitle1;
			holder.tvTitle2 = tvTitle2;
			holder.tvTitle3 = tvTitle3;

			/**
			 * 将数据保存到tag
			 */
			layout.setTag(holder);
		}
		return layout;
	}

	/**
	 * 增加一项的时候
	 */
	public void add(TeamIpqc_codebean bean) {
		this.datas.add(0, bean);

		// 让所有项目都为不选择
		configCheckMap(false);
	}

	// 移除一个项目的时候
	public void remove(int position) {
		System.out.println("position=" + position);
		this.datas.remove(position);
	}

	public Map<Integer, Boolean> getCheckMap() {
		return this.isCheckMap;
	}

	public static class ViewHolder2 {
		public TextView tvTitle0 = null;
		public TextView tvTitle1 = null;
		public TextView tvTitle2 = null;
		public TextView tvTitle3 = null;
		public CheckBox cbCheck = null;
		public Object data = null;
	}

	public List<TeamIpqc_codebean> getDatas() {
		return datas;
	}
}
