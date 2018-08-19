package com.zsm.foxconn.mypaperless.adapter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.zsm.foxconn.mypaperless.Configuration_report;
import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.bean.EXpandaChildModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;



/**
 * 二级列表适配器
 * 
 * @author ZSM
 * 
 *         2016-1-12
 *         
 *         [0][0],[0][1],[0][2],
 *         [1][0],[1][1],[1][2]
 *         
 *         
 */
public class PZExpandableAdapter extends BaseExpandableListAdapter{
	private LinkedList<String> groupData=null;
	private List<LinkedList<EXpandaChildModel>> childData = null;
	private Context context;
	public static boolean kecaozuo = false;
	private int ismuban = 0;		//是否隱藏CheckBox 0否1是

	public List<LinkedList<EXpandaChildModel>> data(){
		return childData;
	}
	
	public void allchoose(Boolean bl){
		for (int i = 0; i < groupData.size(); i++) {
			for (int j = 0; j < childData.get(i).size(); j++) {
				childData.get(i).get(j).setIschoosePZ(bl);
			}
		}
	}
	public final class ViewHolder {
		public TextView mChildTitle;
		public CheckBox mchildcheckBox;
	}

	public final class ViewHolderGroup {
		public TextView mGroupTitle;
	}

	public PZExpandableAdapter(Context context, LinkedList<String> groupData,
			List<LinkedList<EXpandaChildModel>> childData) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.groupData = groupData;
		this.childData = childData;
	}
	
	public PZExpandableAdapter(Context context, LinkedList<String> groupData,
			List<LinkedList<EXpandaChildModel>> childData,int ismuban) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.groupData = groupData;
		this.childData = childData;
		this.ismuban = ismuban;
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
//	private int[][] indexs = null;

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
			convertView = LayoutInflater.from(context).inflate(R.layout.main_listview_group_item,null);
			groupHolder = new ViewHolderGroup();
			groupHolder.mGroupTitle = (TextView) convertView
					.findViewById(R.id.groupTitle);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (ViewHolderGroup) convertView.getTag();
		}
		groupHolder.mGroupTitle.setText(groupData.get(groupPosition).toString());
		return convertView;
	}

	
	@Override
	public View getChildView(int groupPosition,int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 final ViewHolder childHolder;
//		 indexs=new int[getGroupCount()][getChildrenCount(groupPosition)];
		if (convertView == null) {
			childHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.main_expandablelistview_child_item,
					null);
			childHolder.mChildTitle = (TextView) convertView
					.findViewById(R.id.tvTitle);
			childHolder.mchildcheckBox = (CheckBox) convertView
					.findViewById(R.id.cbCheckBox);
			if (ismuban==1) {
				childHolder.mchildcheckBox.setVisibility(View.GONE);
			}
			EXpandaChildModel model = new EXpandaChildModel();
			model = childData.get(groupPosition).get(childPosition);
			childHolder.mChildTitle.setText(model.getChildTitle());
			childHolder.mChildTitle.setTag(groupPosition);
//			childHolder.mchildcheckBox.setOnCheckedChangeListener(null);
			childHolder.mchildcheckBox.setTag(childPosition);
			childHolder.mchildcheckBox.setChecked(model.isIschoosePZ());
			
			childHolder.mchildcheckBox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int position = (Integer) childHolder.mchildcheckBox.getTag();
					int groupIndex=(Integer) childHolder.mChildTitle.getTag();
					if (childData.get(groupIndex).get(position).isIschoosePZ()) {
						childData.get(groupIndex).get(position).setIschoosePZ(false);
					}else {
						childData.get(groupIndex).get(position).setIschoosePZ(true);
					}
				}
			});
			
			convertView.setTag(childHolder);
		} else {
			childHolder = (ViewHolder) convertView.getTag();
			childHolder.mChildTitle.setTag(groupPosition);
			childHolder.mchildcheckBox.setTag(childPosition);
		}
		
		Object value = childData.get(groupPosition).get(childPosition).getChildTitle();
		if (value != null) {
			childHolder.mChildTitle.setText(value.toString());
		} 
		
		value = childData.get(groupPosition).get(childPosition).isIschoosePZ();
		if (value != null) {
			childHolder.mchildcheckBox.setChecked((Boolean)value);
			//((RadioButton) childHolder.mRadio.getChildAt(Integer.valueOf(value.toString()))).setChecked(true);
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

}
