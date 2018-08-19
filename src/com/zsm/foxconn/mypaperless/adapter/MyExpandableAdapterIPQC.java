package com.zsm.foxconn.mypaperless.adapter;

import java.util.LinkedList;
import java.util.List;

import com.zsm.foxconn.mypaperless.Check_Project_ipqc;
import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.CheckViewHolder;
import com.zsm.foxconn.mypaperless.bean.ChildModelIPQC;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
public class MyExpandableAdapterIPQC extends BaseExpandableListAdapter {
	private LinkedList<String> groupData = null;
	private List<LinkedList<ChildModelIPQC>> childData = null;
	private Context context;
	private HttpStart start;
	private String reportid, wo = "";
	private UserBean userBean;
	private int group, child;
	Handler handler = null;
	private int groupInde = 0, childInde = 0;
	private static final int CODE = 0;

	public final class ViewHolder {
		public TextView mChildTitle = null;
		public EditText mContent = null;
		public RadioGroup mRadio = null;
		public EditText mIcar = null;
	}

	public final class ViewHolderGroup {
		public TextView mGroupTitle;
		public TextView mGroupNum;
	}

	public MyExpandableAdapterIPQC(Context context,
			LinkedList<String> groupData,
			List<LinkedList<ChildModelIPQC>> childData, UserBean userBean,
			String reportid, Handler handler) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.groupData = groupData;
		this.childData = childData;
		this.userBean = userBean;
		this.reportid = reportid;
		this.handler = handler;
	}

	public void getwo(String wo) {
		this.wo = wo;
	}

	public MyExpandableAdapterIPQC(List<LinkedList<ChildModelIPQC>> childData) {
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
	private int[][] indexs = null;

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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.main_listview_group_item, null);
			groupHolder = new ViewHolderGroup();
			groupHolder.mGroupTitle = (TextView) convertView
					.findViewById(R.id.groupTitle);
			groupHolder.mGroupNum = (TextView) convertView
					.findViewById(R.id.groupNum);
			groupHolder.mGroupNum.setVisibility(View.VISIBLE);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (ViewHolderGroup) convertView.getTag();
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
		final CheckViewHolder childHolder;
		indexs = new int[getGroupCount()][getChildrenCount(groupPosition)];
		if (convertView == null) {
			childHolder = new CheckViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.main_listview_child_item, null);
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
			ChildModelIPQC model = new ChildModelIPQC();
			model = childData.get(groupPosition).get(childPosition);
			childHolder.mChildTitle.setText(model.getChildTitle());
			childHolder.mContent.setText(model.getChildContent());
			childHolder.mContent.setTag(childPosition);
			childHolder.mChildTitle.setTag(groupPosition);
			childHolder.mIcar.setText(model.getChildIcar());
			int position = (Integer) childHolder.mContent.getTag();
			int groupIndex = (Integer) childHolder.mChildTitle.getTag();
//			childData
//			.get(groupPosition)
//			.get(childPosition)
//			.setChildContent(
//					childData
//							.get(groupPosition)
//							.get(childPosition)
//							.getChildContent()
//							.replace(";", ";\n")
//							.substring(
//									0,
//									childData.get(groupPosition)
//											.get(childPosition)
//											.getChildContent().length()));
			if (childData.get(groupIndex).get(position).getChildResult()
					.equals("1")) {
				childData.get(groupIndex).get(position).setChildContent("N/A");
			}

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
						// indexs[groupIndex][childIndex]=index;
						if (childData.get(groupIndex).get(childIndex)
								.getInputtype().equals("1")) {
							UIHelper.ToastMessage(context, "此灰编辑框输入内容无效");
							childData.get(groupIndex).get(childIndex)
									.setChildIcar("OK");
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
									.getInputtype().equals("1")) {
								childData.get(groupIndex).get(position)
										.setChildContent("OK");
							}
							if (childData.get(groupIndex).get(position)
									.getInputtype().equals("3")) {
								if (s.toString().length() == 11
										|| s.toString().length() == 12) {
									String pro_id = childData.get(groupIndex)
											.get(position).getProId();
									childHolder.mContent
											.setBackgroundResource(R.color.white);
									start = new HttpStart(context, handler);
									childHolder.mContent
											.setBackgroundResource(R.color.blue3);
									childData.get(groupIndex).get(position)
											.setChildContent(s.toString());
									setCoordinate(groupIndex, position);
									start.getServerData(0,
											MyConstant.GET_DATA_OF_INPUTTYPE,
											reportid, userBean.getMFG(),
											pro_id, s.toString());
								} else {
									childHolder.mContent
											.setBackgroundResource(R.color.blue3);
									childData.get(groupIndex).get(position)
											.setChildContent(s.toString());
								}
							}
							if (childData.get(groupIndex).get(position)
									.getInputtype().equals("4")) {
								if (s.toString().length() == 12
										|| s.toString().length() == 19) {
									String pro_id = childData.get(groupIndex)
											.get(position).getProId();
									childHolder.mContent
											.setBackgroundResource(R.color.white);
									start = new HttpStart(context, handler);
									childHolder.mContent
											.setBackgroundResource(R.color.blue3);
									childData.get(groupIndex).get(position)
											.setChildContent(s.toString());
									setCoordinate(groupIndex, position);
									start.getServerData(0,
											MyConstant.GET_DATA_OF_INPUTTYPE,
											reportid, userBean.getMFG(),
											pro_id, s.toString());
								} else {
									childHolder.mContent
											.setBackgroundResource(R.color.blue3);
									childData.get(groupIndex).get(position)
											.setChildContent(s.toString());
								}
							}
							if (childData.get(groupIndex).get(position)
									.getInputtype().equals("5")) {
								if (s.toString().length() == 11) {
									if (wo.length() != 0) {
										String pro_id = childData
												.get(groupIndex).get(position)
												.getProId();
										childHolder.mContent
												.setBackgroundResource(R.color.white);
										start = new HttpStart(context, handler);
										childHolder.mContent
												.setBackgroundResource(R.color.blue3);
										childData.get(groupIndex).get(position)
												.setChildContent(s.toString());
										setCoordinate(groupIndex, position);
										start.getServerData(
												0,
												MyConstant.GET_DATA_OF_INPUTTYPE,
												reportid, userBean.getMFG(),
												pro_id, s.toString(), wo);
									} else {
										UIHelper.ToastMessage(context, "請輸入工單");
										childData.get(groupIndex).get(position)
												.setChildContent(s.toString());
									}
								} else {
									childHolder.mContent
											.setBackgroundResource(R.color.blue3);
									childData.get(groupIndex).get(position)
											.setChildContent(s.toString());
								}
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
			childHolder.qr_bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					childInde = (Integer) childHolder.mContent.getTag();
					groupInde = (Integer) childHolder.mChildTitle.getTag();
					Log.i(">>>>>>>>>", groupInde + "==" + childInde);
					((Check_Project_ipqc) context).QR();
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
//								if (userBean.getTeam().toString()
//												.equalsIgnoreCase("IPQC")) {
//									AlertDialog alertDialog = new AlertDialog.Builder(
//											context).create();
//									alertDialog.setIcon(R.drawable.ic_system);
//									alertDialog.setTitle("系統提示:");
//									alertDialog.setMessage("是否創建ICAR?");
//									alertDialog
//											.setButton(
//													DialogInterface.BUTTON_POSITIVE,
//													"確定",
//													new DialogInterface.OnClickListener() {
//
//														@Override
//														public void onClick(
//																DialogInterface dialog,
//																int which) {
//															UIHelper.ToastMessage(context, "接口待開發...");
////															Intent intent = new Intent(context, IcarCreate.class);
////															((Activity) context).startActivityForResult(intent, CODE);	
//														}
//													});
//									alertDialog
//											.setButton(
//													DialogInterface.BUTTON_NEGATIVE,
//													"取消",
//													new DialogInterface.OnClickListener() {
//
//														@Override
//														public void onClick(
//																DialogInterface dialog,
//																int which) {
//															// TODO
//															// Auto-generated
//															// method stub
//														}
//													});
//									alertDialog.show();
//								}
							}
						}
					});

			convertView.setTag(childHolder);
		} else {
			childHolder = (CheckViewHolder) convertView.getTag();
			childHolder.mContent.setTag(childPosition);
			childHolder.mChildTitle.setTag(groupPosition);
		}
		// 點檢標示,0表示手寫,1表示單選,2表示從系統帶出,3表示輸入關鍵字,4表示輸入TR_SN帶出對應數據,5表示驗證輸入P_SN的工單的SN是否一致
		switch (Integer.valueOf(childData.get(groupPosition).get(childPosition)
				.getInputtype().trim())) {
		case 0:
			childHolder.mContent
					.setBackgroundResource(R.drawable.bg_edittext_color15);
			childHolder.mContent.setHint("");
			break;
		case 1:
			childHolder.mContent
					.setBackgroundResource(R.drawable.bg_edittext_bottom_text_color_nor);
			childData.get(groupPosition).get(childPosition)
					.setChildContent("OK");
			break;
		case 2:
			childHolder.mContent
					.setBackgroundResource(R.drawable.bg_edittext_blue2);
			childHolder.mContent.setHint("");
			break;
		case 3:
			childHolder.mContent
					.setBackgroundResource(R.drawable.bg_edittext_blue3);
			childHolder.mContent.setHint("請輸入關鍵字符");
			break;
		case 4:
			childHolder.mContent
					.setBackgroundResource(R.drawable.bg_edittext_blue3);
			childHolder.mContent.setHint("請輸入TR_SN");
			break;
		case 5:
			childHolder.mContent
					.setBackgroundResource(R.drawable.bg_edittext_blue3);
			childHolder.mContent.setHint("請輸入SN");
			break;
		default:
			break;
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

		childHolder.mContent.clearFocus();
		if (indexs != null && index == childPosition) {
			childHolder.mContent.requestFocus();
		}
		return convertView;
	}

	/**
	 * 獲取當前編輯ID
	 * 
	 * @param group
	 * @param child
	 */
	public void setCoordinate(int group, int child) {
		this.group = group;
		this.child = child;

	}

	public String getCoordinate() {
		return group + "%" + child;
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

	public List<LinkedList<ChildModelIPQC>> getChildData() {
		if (childData != null) {
			return childData;
		}
		return null;
	}

	public LinkedList<String> getgroupData() {
		if (groupData != null) {
			return groupData;
		}
		return null;

	}

	public String getLocation() {
		return groupInde + "%" + childInde;
	}
}
