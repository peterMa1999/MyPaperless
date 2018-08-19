package com.zsm.foxconn.mypaperless.adapter;

//
import java.util.ArrayList;
import java.util.List;

import com.zsm.foxconn.mypaperless.Addreport_GroupListFragment;
import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.R.array;
import com.zsm.foxconn.mypaperless.bean.Line_childItem;
import com.zsm.foxconn.mypaperless.help.UIHelper;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class Addreport_MyAdapter_Linename extends BaseAdapter {
	private Context context = null;
	private LayoutInflater inflater = null;
	private List<Line_childItem> list = null;
	private String itemString = ""; // 记录每个item中textview的值
	List<String> arrays = null;

	public class ViewHolder {
		public Spinner floor;
		public EditText line;
		public ImageView deleteview;
	}

	public Addreport_MyAdapter_Linename(Context context,
			List<Line_childItem> list, List<String> arrays) {
		this.arrays = arrays;
		this.context = context;
		this.list = list;
		Log.i(">>>>>>>>>>>>>>>>>B", list.size()+"");
		this.inflater = LayoutInflater.from(context);
	}

	public List<Line_childItem> get_addreport_list() {
		if (list != null) {
			return list;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position).toString();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private Integer index = -1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater
					.inflate(R.layout.add_report_line_child, null);
			holder.floor = (Spinner) convertView
					.findViewById(R.id.add_report_floor_name_spinner);
			holder.line = (EditText) convertView
					.findViewById(R.id.add_report_line_name_edittext);
			holder.deleteview = (ImageView) convertView
					.findViewById(R.id.add_line_image_delete);
			holder.line.setText(list.get(position).getLinename().toString());
			holder.floor.setSelection(arrays.indexOf(list.get(position).toString()));
			holder.line.setTag(position);
			holder.line.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_UP) {
						index = (Integer) v.getTag();
						System.out.println("焦点在第" + index + "行");
					}
					return false;
				}
			});

			holder.line.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					Log.i(">>>>>>>>>>>>>", s.toString());
					int mIndex = (Integer) holder.line.getTag();
					try {
						if (s != null && !"".equals(s.toString())) {
							if (s.toString().matches(".*-.*")) {
								list.get(mIndex).setLinename("");
								UIHelper.ToastMessage(context, "不能含有'-'字符");
							} else {
//								if (s.toString().contains(list.get(mIndex).getFloorname())) {
									list.get(mIndex).setLinename(s.toString());
//								}else {
//									list.get(mIndex).setLinename(list.get(mIndex).getFloorname()+s.toString());
//								}
							}
						} else {
							Log.i(">>>>>>>>>>>>>>>>>A",mIndex+ "赋值为空");
							list.get(mIndex).setLinename("");
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			});

			holder.floor
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							// TODO Auto-generated method stub
							int mIndex = (Integer) holder.line.getTag();
							try {
								itemString = arg0.getItemAtPosition(arg2)
										.toString();
								list.get(mIndex).setFloorname(itemString);
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});

			holder.deleteview.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int mIndex = (Integer) holder.line.getTag();
					try {
						Addreport_GroupListFragment
								.deleteChild_linename(mIndex);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.line.setTag(position);
		}
		Object value = list.get(position).getLinename().toString();
		if (value != null && !"".equals(value)) {
			holder.line.setText(value.toString());
		} else {
			holder.line.setText("");
		}
		value = list.get(position).getFloorname().toString();
		if (value != null && !"".equals(value)) {
			holder.floor.setSelection(arrays.indexOf(value.toString()));
		}

		holder.line.clearFocus();
		if (index != -1 && index == position) {
			holder.line.requestFocus();
		}
		return convertView;
	}

}
