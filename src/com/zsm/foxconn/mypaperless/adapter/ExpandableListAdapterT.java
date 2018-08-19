package com.zsm.foxconn.mypaperless.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.zsm.foxconn.mypaperless.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class ExpandableListAdapterT extends BaseExpandableListAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<String> m_lStation = new ArrayList<String>();// 一級標題數據源
	private List<List<List<String>>> m_All = new ArrayList<List<List<String>>>();// 二級標題數據源
	private Context context;
	String color[] = null; // 顏色數值數組

	public ExpandableListAdapterT(Context context, List<String> station, // 構造函數
			List<List<List<String>>> childTile, String[] color, Context fi) {
		this.mContext = context;
		this.m_lStation = station;
		this.m_All = childTile;
		this.context = fi;
		this.color = color;
		// color=user.getColor();

	}

	// 二級標題控件類
	public final class ViewHolder {
		public EditText m_Ed1;
		public TextView m_Tv1;
		public RadioGroup m_Rb;
		public EditText m_Ed2;

	}

	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setData(List<String> station, List<List<List<String>>> childMap) {
		this.m_lStation = station;
		this.m_All = childMap;
	}

	Vector vv = new Vector(); // 添加二級標題中Edtext1地址
	Vector vv2 = new Vector(); // 添加二級標題中Edtext2地址

	// 顯示二級標題方法
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final ViewHolder mHolder;

		final int group = groupPosition;
		final int child = childPosition;

		mHolder = new ViewHolder();
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.child_item, null);
		// 提取二級標題中的控件
		mHolder.m_Ed1 = (EditText) convertView.findViewById(R.id.childContent);
		mHolder.m_Ed1.requestFocusFromTouch();
		mHolder.m_Tv1 = (TextView) convertView.findViewById(R.id.childTitle);
		mHolder.m_Rb = (RadioGroup) convertView.findViewById(R.id.childRadio);
		mHolder.m_Ed2 = (EditText) convertView.findViewById(R.id.childIcar);
		mHolder.m_Ed2.setHint("comment");
		convertView.setTag(mHolder);

		final String station = this.m_lStation.get(group).toString()
				+ Integer.toString(child);// 提取一級標題

		// 為二級標題裏面的內容賦值賦值與
		mHolder.m_Ed1.setText(m_All.get(group).get(1).get(childPosition)
				.toString());
		mHolder.m_Tv1.setText(m_All.get(group).get(0).get(childPosition)
				.toString());
		mHolder.m_Ed2.setText(m_All.get(group).get(3).get(childPosition)
				.toString());

		if (m_All.get(group).get(2).get(childPosition).toString().equals("0")) { // 為
			((RadioButton) mHolder.m_Rb.getChildAt(0)).setChecked(true);
		} else {
			((RadioButton) mHolder.m_Rb.getChildAt(1)).setChecked(true);
		}

		// 為單選按鈕添加改變選擇監聽事件
		mHolder.m_Rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group1, int checkedId) {
				// TODO Auto-generated method stub
				for (int i = 0; i < mHolder.m_Rb.getChildCount(); i++) {
					RadioButton ra = (RadioButton) mHolder.m_Rb.getChildAt(i);
					if (ra.isChecked()) {
						if (i == 1) {
							AlertDialog alert = new AlertDialog.Builder(context)
									.create();
							alert.setIcon(R.drawable.img8);
							alert.setTitle("系統提示：");
							alert.setMessage("您確定此項異常?");

							alert.setButton(DialogInterface.BUTTON_NEGATIVE,
									"確定",
									new DialogInterface.OnClickListener() {

										// 為二級標題中單選按鈕變動
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											m_All.get(group).get(2)
													.set(childPosition, "" + 1);
											((RadioButton) mHolder.m_Rb
													.getChildAt(1))
													.setChecked(true);

										}

									});

							alert.setButton(DialogInterface.BUTTON_POSITIVE,
									"取消",
									new DialogInterface.OnClickListener() {
										// 為二級標題中單選按鈕變動并改變第二個文本框賦值（EdText2）
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											((RadioButton) mHolder.m_Rb
													.getChildAt(0))
													.setChecked(true);
											m_All.get(group).get(2)
													.set(childPosition, "" + 0);
										}
									});
							alert.show();
						} else {
							((RadioButton) mHolder.m_Rb.getChildAt(0))
									.setChecked(true);
							m_All.get(group).get(2).set(childPosition, "" + 0);

						}
					}

				}
			}

		});

		// 文本框輸入監聽事件對象
		TextWatcher textWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = mHolder.m_Ed1.getText().toString();

				if (text != null && text.length() > 0) {
					mHolder.m_Ed1.setBackgroundResource(R.color.baise);
				}
				for (int i = 0; i < Ed.size(); i++) {
					EditText e1 = mHolder.m_Ed1;
					point po = (point) Ed.get(i);
					if (po.edString.equals(e1)) {
						m_All.get(po.group).get(1).set(po.child, text);// 賦值
						break;
					}
				}

				String text1 = mHolder.m_Ed2.getText().toString();

				for (int i = 0; i < Ed2.size(); i++) {
					EditText e2 = mHolder.m_Ed2;
					point po = (point) Ed2.get(i);
					if (po.edString.equals(e2)) {
						m_All.get(po.group).get(3).set(po.child, text1);// 賦值
						break;
					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		};

		if (vv.indexOf(mHolder.m_Ed1) == -1) {
			mHolder.m_Ed1.addTextChangedListener(textWatcher); // 為文本框添加輸入監聽事件
			for (int i = 0; i < Ed.size(); i++) { // 結束vv，Ed中多餘的元素
				point po = (point) Ed.get(i);
				if (po.station.equals(station)) {
					vv.remove(vv.indexOf(po.edString));
					Ed.remove(i);
					break;
				}
			}
			vv.add(mHolder.m_Ed1);
			point po = new point();
			po.edString = mHolder.m_Ed1;
			po.group = group;
			po.child = child;
			po.station = station;

			int colorCount = count(group, child);

			switch (Integer.valueOf(color[colorCount])) {
			case 0:
				if (mHolder.m_Ed1 == null
						|| (mHolder.m_Ed1.getText().toString()).length() == 0) {
					mHolder.m_Ed1.setBackgroundResource(R.color.white);
				} else {
					mHolder.m_Ed1.setBackgroundResource(R.color.baise);
				}
				break;
			case 1:
				mHolder.m_Ed1.setBackgroundResource(R.color.grap);
				mHolder.m_Ed1.setEnabled(false);
				m_All.get(group).get(1).set(child, " ");// 灰色框標記
				break;

			default:
				mHolder.m_Ed1.setBackgroundResource(R.color.baise);
				break;
			}
			Ed.add(po);
		}

		if (vv2.indexOf(mHolder.m_Ed2) == -1) {
			mHolder.m_Ed2.addTextChangedListener(textWatcher);
			for (int i = 0; i < Ed2.size(); i++) { // 結束vv2，Ed2中多餘的元素
				point po = (point) Ed2.get(i);
				if (po.station.equals(station)) {
					vv2.remove(vv2.indexOf(po.edString));
					Ed2.remove(i);
					break;
				}

			}
			vv2.add(mHolder.m_Ed2);
			point po = new point();
			po.edString = mHolder.m_Ed2;
			po.group = group;
			po.child = child;
			po.station = station;
			mHolder.m_Ed2.setBackgroundResource(R.color.baise);
			Ed2.add(po);
		}
		return convertView;
	}

	// 根據文本框座標提取文本框顏色相應的位置
	public int count(int group, int child) {
		int u = 0;
		for (int i = 0; i < group; i++) {
			u += m_All.get(i).get(0).size();
		}
		u += child;
		return u;
	}

	Vector Ed = new Vector(); // 二級標題中EdText1（第一個文本框）地址與座標綁定的的集合
	Vector Ed2 = new Vector(); // 二級標題中EdText2（第二個文本框）地址與座標綁定的的集合

	// Vector Ed22=new Vector(); //IcarNo 存放二級標題中EdText2的值的集合

	// 將文本框地址與座標相綁定
	public class point {
		EditText edString;// 控件地址
		int group; // 一級標題位置
		int child; // 二級標題位置
		String station;
	}

	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return this.m_All.get(groupPosition).get(0).size();

	}

	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this.m_lStation.get(groupPosition).toString();
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.m_lStation.size();
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 顯示一級標題的方法
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View view = convertView;

		if (view == null) {
			LayoutInflater inflater_new = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater_new.inflate(R.layout.group, null);
		}
		TextView text_titel = (TextView) view.findViewById(R.id.groupTitle);
		text_titel.setText(getGroup(groupPosition).toString());
		
		return view;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<List<List<String>>> getAll() {
		return m_All;
	}

	public void setAll(List<List<List<String>>> m_All) {
		this.m_All = m_All;
	}
}
