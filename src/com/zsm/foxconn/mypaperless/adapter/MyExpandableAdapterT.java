package com.zsm.foxconn.mypaperless.adapter;

import java.util.LinkedList;
import java.util.List;

import com.zsm.foxconn.mypaperless.Check_Project_ipqc;
import com.zsm.foxconn.mypaperless.ECheckActivity;
import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.bean.ChildModel;
import com.zsm.foxconn.mypaperless.help.UIHelper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 二级列表适配器
 * 
 * @author ZSM
 * 
 *         2016-1-12
 * 
 *         [0][0],[0][1],[0][2], [1][0],[1][1],[1][2]
 * 
 * 
 */
public class MyExpandableAdapterT extends BaseExpandableListAdapter {
	private final static String TAG = "MyExpandableAdapterT";
	private LinkedList<String> groupData = null;
	private List<LinkedList<ChildModel>> childData = null;
	private Context context;
	private int groupInde = 0, childInde = 0;
	int flag = 0;
	private Handler handler;

	public final class ViewHolder {
		public TextView mChildTitle;
		public EditText mContent;
		public RadioGroup mRadio;
		public EditText mIcar;
		public ImageView qr_bt;
	}

	public final class ViewHolderGroup {
		public ImageView mGroupimage;
		public TextView mGroupTitle;
		public TextView mGroupNum;
	}

	public MyExpandableAdapterT(Handler handler,Context context, LinkedList<String> groupData,
			List<LinkedList<ChildModel>> childData) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.groupData = groupData;
		this.childData = childData;
		this.handler=handler;
	}
	
	public MyExpandableAdapterT(Context context, LinkedList<String> groupData,
			List<LinkedList<ChildModel>> childData) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.groupData = groupData;
		this.childData = childData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	private Integer index = -1;
	private Integer mindex = -1;
	private int[][] indexs = null;
	private int[][] mindexs = null;

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childData.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groupData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolderGroup groupHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.group,
					null);
			groupHolder = new ViewHolderGroup();
			groupHolder.mGroupimage = (ImageView) convertView
					.findViewById(R.id.groupimg);
			groupHolder.mGroupTitle = (TextView) convertView
					.findViewById(R.id.groupTitle);
			groupHolder.mGroupNum = (TextView) convertView
					.findViewById(R.id.groupNum);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (ViewHolderGroup) convertView.getTag();
		}
		groupHolder.mGroupimage.setImageResource(R.drawable.ic_indown);
		if (!isExpanded) {
			groupHolder.mGroupimage.setImageResource(R.drawable.ic_right);
		}
		groupHolder.mGroupTitle
				.setText(groupData.get(groupPosition).toString());
		groupHolder.mGroupNum.setText(getChildrenCount(groupPosition) + "");
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder childHolder;
		indexs = new int[getGroupCount()][getChildrenCount(groupPosition)];
		mindexs = new int[getGroupCount()][getChildrenCount(groupPosition)];
		if (convertView == null) {
			childHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.child_item, null);
			childHolder.mChildTitle = (TextView) convertView
					.findViewById(R.id.childTitle);
			childHolder.mContent = (EditText) convertView
					.findViewById(R.id.childContent);
			childHolder.mRadio = (RadioGroup) convertView
					.findViewById(R.id.childRadio);
			childHolder.mIcar = (EditText) convertView
					.findViewById(R.id.childIcar);
			childHolder.mIcar.setHint("  comment  ");
			childHolder.qr_bt = (ImageView) convertView
					.findViewById(R.id.qr_bt);
			ChildModel model = new ChildModel();
			model = childData.get(groupPosition).get(childPosition);
			childHolder.mChildTitle.setText(model.getChildTitle());
			childHolder.mContent.setText(model.getChildContent());
			childHolder.mContent.setTag(childPosition);
			childHolder.mChildTitle.setTag(groupPosition);

			childHolder.mIcar.setText(model.getChildIcar());

			((RadioButton) childHolder.mRadio.getChildAt(0)).setChecked(true);
			childHolder.mContent.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_UP) {
						index = (Integer) v.getTag();
						int groupIndex = (Integer) childHolder.mChildTitle
								.getTag();
						int childIndex = (Integer) childHolder.mContent
								.getTag();
						indexs[groupIndex][childIndex] = index;
						if (childData.get(groupIndex).get(childIndex)
								.getChildInputFlag().equals("1")) {
							UIHelper.ToastMessage(context, "此灰编辑框输入内容无效");
						}
					}
					return false;
				}
			});

			childHolder.mIcar.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					int position = (Integer) childHolder.mContent.getTag();
					int groupIndex = (Integer) childHolder.mChildTitle.getTag();
					if (s != null && !"".equals(s.toString())) {
						childData.get(groupIndex).get(position)
								.setChildIcar(s.toString());
					} else {
						childData.get(groupIndex).get(position)
								.setChildIcar("N/A");
					}

				}
			});
			childHolder.mContent.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					int position = (Integer) childHolder.mContent.getTag();
					int groupIndex = (Integer) childHolder.mChildTitle.getTag();
					if (s != null && !"".equals(s.toString())) {
						try {
							if (childData.get(groupIndex).get(position)
									.getChildInputFlag().equals("1")) {
								childData.get(groupIndex).get(position)
										.setChildContent("OK");
							} else {
								childData.get(groupIndex).get(position)
										.setChildContent(s.toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						childData.get(groupIndex).get(position)
								.setChildContent("");
					}
				}
			});
			childHolder.mIcar.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					flag = 1;
					Log.i(">>>>>>>>>flag", "flag==" + flag);
				}
			});
			childHolder.mContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					flag = 0;
					Log.i(">>>>>>>>>flag", "flag==" + flag);
				}
			});
			childHolder.qr_bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					childInde = (Integer) childHolder.mContent.getTag();
					groupInde = (Integer) childHolder.mChildTitle.getTag();
					Log.i(">>>>>>>>>", groupInde + "==" + childInde);
					if (flag == 1) {
						((ECheckActivity) context).QR1();
					} else {
						((ECheckActivity) context).QR();
					}
				}
			});
			childHolder.mRadio
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							// TODO Auto-generated method stub
							int position = (Integer) childHolder.mContent
									.getTag();
							int groupIndex = (Integer) childHolder.mChildTitle
									.getTag();
							RadioButton rb = (RadioButton) childHolder.mRadio
									.findViewById(checkedId);
							if (checkedId == R.id.rb_ok) {
								childData.get(groupIndex).get(position)
										.setChildResult("0");
							} else if (checkedId == R.id.rb_no) {
								childData.get(groupIndex).get(position)
										.setChildResult("1");
								Message message = handler.obtainMessage();
								message.what = 0x8090;
								message.obj = childData.get(groupIndex)
										.get(position).getProId();
								handler.sendMessage(message);
							}
						}
					});

			convertView.setTag(childHolder);
		} else {
			childHolder = (ViewHolder) convertView.getTag();
			childHolder.mContent.setTag(childPosition);
			childHolder.mChildTitle.setTag(groupPosition);
		}
		Object value = childData.get(groupPosition).get(childPosition)
				.getChildContent();
		if (value != null && !"".equals(value)) {
			childHolder.mContent.setText(value.toString());
		} else {
			childHolder.mContent.setText("");
		}
		value = childData.get(groupPosition).get(childPosition).getChildIcar();
		if (value != null && !"".equals(value)) {
			childHolder.mIcar.setText(value.toString());
		} else {
			childHolder.mIcar.setText("");
		}

		value = childData.get(groupPosition).get(childPosition).getChildTitle();
		if (value != null) {
			childHolder.mChildTitle.setText(value.toString());
		}

		value = childData.get(groupPosition).get(childPosition)
				.getChildResult();
		if (value != null) {
			((RadioButton) childHolder.mRadio.getChildAt(Integer.valueOf(value
					.toString()))).setChecked(true);
		}

		// 點檢標示,0表示手寫,1表示不可寫,2表示從系統帶出,3表示輸入關鍵字
		switch (Integer.valueOf(childData.get(groupPosition).get(childPosition)
				.getChildInputFlag().trim())) {
		case 0:
			childHolder.mContent
					.setBackgroundResource(R.drawable.bg_edittext_color15);
			break;
		case 1:
			childHolder.mContent
					.setBackgroundResource(R.drawable.bg_edittext_bottom_text_color_nor);
			childData.get(groupPosition).get(childPosition)
					.setChildContent("N/A");
			break;
		case 2:
			childHolder.mContent
					.setBackgroundResource(R.drawable.bg_edittext_blue2);
			break;
		case 3:
			childHolder.mContent
					.setBackgroundResource(R.drawable.bg_edittext_blue3);
			break;
		case 4:
			childHolder.mContent
					.setBackgroundResource(R.drawable.bg_edittext_blue3);
			break;
		default:
			break;
		}

		childHolder.mContent.clearFocus();
		if (indexs != null && index == childPosition) {
			childHolder.mContent.requestFocus();
			childHolder.mIcar.requestFocus();
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	public List<LinkedList<ChildModel>> getChildData() {
		if (childData != null) {
			return childData;
		}
		return null;
	}

	public String getLocation() {
		return groupInde + "%" + childInde;
	}

}
