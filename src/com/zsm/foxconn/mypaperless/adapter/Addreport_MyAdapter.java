package com.zsm.foxconn.mypaperless.adapter;

import java.util.List;
import java.util.Map;

import com.zsm.foxconn.mypaperless.Add_report;
import com.zsm.foxconn.mypaperless.Addreport_GroupListFragment;
import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.bean.ChildItem;
import com.zsm.foxconn.mypaperless.bean.Line_childItem;
import com.zsm.foxconn.mypaperless.help.ModifyDialog;

import android.R.string;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Addreport_MyAdapter extends BaseExpandableListAdapter {
	private List<String> parentList,lineparentList;
	private static Map<String, List<ChildItem>> map=null;
	private static List<Line_childItem> add_map=null;
	private Context context;
	private EditText edit_modify;
	private ModifyDialog dialog;
	private Spinner inputtype_spinner;
	private String flag=null;

	// 构造函数
public Addreport_MyAdapter(Context context, List<String> parentList,Map<String,List<ChildItem>> map) 
	{
		this.context = context;
		this.parentList = parentList;
		this.map = map;
	}

public Addreport_MyAdapter(Context context, List<Line_childItem> add_map) 
{
	this.context = context;
	this.add_map = add_map;
}

	// 获取分组数
	@Override
	public int getGroupCount() {
		return parentList.size();
	}

	// 获取当前组的子项数
	@Override
	public int getChildrenCount(int groupPosition)
	{
		String groupName = parentList.get(groupPosition);
		int childCount = map.get(groupName).size();
		return childCount;
	}

	// 获取当前组对象
	@Override
	public Object getGroup(int groupPosition) {
		String groupName = parentList.get(groupPosition);
		return groupName;
	}

	// 获取当前子项对象
	@Override
	public Object getChild(int groupPosition, int childPosition) 
	{
		String groupName = parentList.get(groupPosition);
		String chidlName =map.get(groupName).get(childPosition).toString();
		return chidlName;
	}

	// 获取组ID
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 获取子项ID
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	// 组视图初始化
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		final int groupPos = groupPosition;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_parent, null);
		}

		ImageView image = (ImageView) convertView
				.findViewById(R.id.image_parent);
		ImageView image_add = (ImageView) convertView
				.findViewById(R.id.image_add);
		ImageView image_delete = (ImageView) convertView
				.findViewById(R.id.image_delete);

		if (isExpanded) {
			 image.setImageResource(R.drawable.ic_indown);
		} else {
			image.setImageResource(R.drawable.ic_right);
		}

		image_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				alertAddDialog(Add_report.fragment.getActivity(), "新增子项",
						groupPos);
			}
		});
		image_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Addreport_GroupListFragment.deleteGroup(groupPos);
			}
		});

		TextView parentText = (TextView) convertView
				.findViewById(R.id.text_parent);
		parentText.setText(parentList.get(groupPosition));
		return convertView;
	}

	// 子项视图初始化
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final int groupPos = groupPosition;
		final int childPos = childPosition;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_child, null);
		}
		TextView childText = (TextView) convertView
				.findViewById(R.id.text_child);
		String level[] = context.getResources().getStringArray(R.array.input_type);//资源文件
		Spinner inputtype_spinner = (Spinner) convertView.findViewById(R.id.field_item_spinner_content);
		inputtype_spinner.setAdapter(new ArrayAdapter<String>(context,R.layout.simple_newspinner_item,level));
		ImageView image_delete = (ImageView) convertView
				.findViewById(R.id.image_delete);
		final String parentName = parentList.get(groupPosition).toString();
		final String childName = map.get(parentName).get(childPosition).getChildText().toString();
		childText.setText(childName);
		flag="0";
		image_delete.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Addreport_GroupListFragment.deleteChild(groupPos, childPos);
				
			}
		});
		inputtype_spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				 flag=arg2+"";
				 map.get(parentName).get(childPosition).setItem(childName, flag);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		map.get(parentName).get(childPosition).setItem(childName, flag);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	// 弹新增子项对话框
	public void alertAddDialog(Context context, String title, int currentGroup)
	{
		final int group = currentGroup;

		dialog = new ModifyDialog(context, title, null);
		edit_modify = dialog.getEditText();
		dialog.setOnClickCommitListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Addreport_GroupListFragment.addChild(group, edit_modify.getText()
						.toString(),flag);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	public static Map<String, List<ChildItem>> newMap() 
	{
		return map;
	}
}