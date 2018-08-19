package com.zsm.foxconn.mypaperless.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
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

import com.zsm.foxconn.mypaperless.CheckPdActivity;
import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.CheckViewHolder;
import com.zsm.foxconn.mypaperless.bean.ChildModel;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

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
public class MyExpandableAdapter extends BaseExpandableListAdapter {
	private final static String TAG = "MyExpandableAdapter";
	private LinkedList<String> groupData = null;
	private List<LinkedList<ChildModel>> childData = null;
	private Context context;
	private final int SCANER_CODE = 1;
	private int groupIndex = 0, childIndex = 0;
	private HttpStart start;
	Handler handler = null;
	private UserBean userBean;
	private String reportid;

	public final class ViewHolderGroup {
		public ImageView mGroupimage;
		public TextView mGroupTitle;
		public TextView mGroupNum;
	}

	public MyExpandableAdapter(Context context, LinkedList<String> groupData,
			List<LinkedList<ChildModel>> childData, UserBean userBean,
			String reportid, Handler handler) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.groupData = groupData;
		this.childData = childData;
		this.userBean = userBean;
		this.reportid = reportid;
		this.handler = handler;
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
	private int index_now = 0;
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
	public View getChildView(final int groupPosition, final int childPosition,
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
			childHolder.qr_bt = (ImageView) convertView
					.findViewById(R.id.qr_bt);
			ChildModel model = new ChildModel();
			model = childData.get(groupPosition).get(childPosition);
			childHolder.mChildTitle.setText(model.getChildTitle());
			childHolder.mContent.setText(model.getChildContent());
			childHolder.mIcar.setText(model.getChildIcar());

			childHolder.mContent.setTag(childPosition);
			childHolder.mChildTitle.setTag(groupPosition);
			childHolder.mIcar.setText(model.getChildIcar());
			((RadioButton) childHolder.mRadio.getChildAt(0)).setChecked(true);
			// childData
			// .get(groupPosition)
			// .get(childPosition)
			// .setChildContent(
			// childData
			// .get(groupPosition)
			// .get(childPosition)
			// .getChildContent()
			// .replace(";", ";\n")
			// .substring(
			// 0,
			// childData.get(groupPosition)
			// .get(childPosition)
			// .getChildContent().length()+1));

			childHolder.mContent.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					try {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							index = (Integer) v.getTag();
							int groupIndex = (Integer) childHolder.mChildTitle
									.getTag();
							int childIndex = (Integer) childHolder.mContent
									.getTag();
							indexs[groupIndex][childIndex] = index;
							if (childData.get(groupIndex).get(childIndex)
									.getChildInputFlag().equals("1")) {
								UIHelper.ToastMessage(context, "灰色编辑框输入内容无效");
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
						Log.i(TAG, "-EXCEPTION-" + e);
						e.printStackTrace();
					}
					return false;
				}
			});

			childHolder.mContent.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						index_now = childPosition;
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
						if (childData.get(groupIndex).get(position)
								.getChildInputFlag().equals("6")) {
							String childcontent = childData.get(groupIndex)
									.get(position).getChildContent();
							if (s.toString().length() == childcontent.length()) {
								if (childcontent.equalsIgnoreCase(s.toString())) {
									childData.get(groupIndex).get(position)
											.setChildResult("0");
									UIHelper.ToastMessage(context, "與二維碼帶出信息一致");
								} else {
									childData.get(groupIndex).get(position)
											.setChildResult("1");
									UIHelper.ToastMessage(context,
											"與二維碼帶出信息不一致");
								}
							} else {
								childData.get(groupIndex).get(position)
										.setChildResult("1");
							}
						} else if (childData.get(groupIndex).get(position)
								.getChildInputFlag().equals("7")) {
							String childcontent = childData.get(groupIndex)
									.get(position).getChildContent();
							if (s.toString().length() == childcontent.length()) {
								if (childcontent.equalsIgnoreCase(s.toString())) {
									childData.get(groupIndex).get(position)
											.setChildResult("0");
									UIHelper.ToastMessage(context, "與二維碼帶出信息一致");
								} else {
									childData.get(groupIndex).get(position)
											.setChildResult("1");
									UIHelper.ToastMessage(context,
											"與二維碼帶出信息不一致");
								}
							} else {
								childData.get(groupIndex).get(position)
										.setChildResult("1");
							}
						}
					} else {
						childData.get(groupIndex).get(position)
								.setChildIcar("N/A");
					}

				}
			});

			// 點檢標示,0表示手寫,1表示不可寫,2表示從系統帶出,3表示輸入關鍵字

			TextWatcher textWatcher = new TextWatcher() {

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

						if (childData.get(groupIndex).get(position)
								.getChildInputFlag().equals("3")) {
							childHolder.mContent.setEnabled(true);
							childHolder.mContent
									.setTransformationMethod(HideReturnsTransformationMethod
											.getInstance());
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
										reportid, userBean.getMFG(), pro_id,
										s.toString());
							} else {
								childHolder.mContent
										.setBackgroundResource(R.color.blue3);
								childData.get(groupIndex).get(position)
										.setChildContent(s.toString());
							}
						} else if (childData.get(groupIndex).get(position)
								.getChildInputFlag().equals("6")) {

							// 禁止手机软键盘
							// childHolder.mContent.setInputType(InputType.TYPE_NULL);
							childHolder.mContent.setEnabled(false);
							// 设置EditText文本为隐藏的
							childHolder.mContent
									.setTransformationMethod(android.text.method.PasswordTransformationMethod
											.getInstance());
							// childHolder.mContent.setInputType(InputType.TYPE_CLASS_TEXT
							// | InputType.TYPE_TEXT_VARIATION_PASSWORD);
						} else if (childData.get(groupIndex).get(position)
								.getChildInputFlag().equals("7")) {

							// 禁止手机软键盘
							// childHolder.mContent.setInputType(InputType.TYPE_NULL);
							childHolder.mContent.setEnabled(false);
							// 设置EditText文本为隐藏的
							childHolder.mContent
									.setTransformationMethod(android.text.method.PasswordTransformationMethod
											.getInstance());
							// childHolder.mContent.setInputType(InputType.TYPE_CLASS_TEXT
							// | InputType.TYPE_TEXT_VARIATION_PASSWORD);
						} else if (childData.get(groupIndex).get(position)
								.getChildInputFlag().equals("1")) {
							childHolder.mContent.setEnabled(true);
							childHolder.mContent
									.setTransformationMethod(HideReturnsTransformationMethod
											.getInstance());
							childHolder.mContent
									.setTransformationMethod(HideReturnsTransformationMethod
											.getInstance());
							childData.get(groupIndex).get(position)
									.setChildContent("OK");
						} else {
							childHolder.mContent.setEnabled(true);
							childHolder.mContent
									.setTransformationMethod(HideReturnsTransformationMethod
											.getInstance());
							childData.get(groupIndex).get(position)
									.setChildContent(s.toString());
						}
					} else {
						childData.get(groupIndex).get(position)
								.setChildContent("");
					}
				}
			};
			childHolder.mContent.addTextChangedListener(textWatcher);
			childHolder.qr_bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 打开扫描界面扫描条形码或二维码
					// Intent openCameraIntent = new Intent(context,
					// CaptureActivity.class);
					// context.startActivityForResult(openCameraIntent,
					// SCANER_CODE);
					// context.startActivity(openCameraIntent);
					// CheckPdActivity.QR();
					// groupIndex=groupPosition;
					// childIndex=childPosition;

					childIndex = (Integer) childHolder.mContent.getTag();
					groupIndex = (Integer) childHolder.mChildTitle.getTag();
					Log.i(">>>>>>>>>", groupIndex + "==" + childIndex);
					((CheckPdActivity) context).QR();
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
			childHolder = (CheckViewHolder) convertView.getTag();
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
		value = childData.get(groupPosition).get(childPosition).getChildTitle();
		if (value != null) {
			childHolder.mChildTitle.setText(value.toString());
		}
		value = childData.get(groupPosition).get(childPosition).getChildIcar();
		if (value != null && !"".equals(value)) {
			childHolder.mIcar.setText(value.toString());
		} else {
			childHolder.mIcar.setText("N/A");
		}
		value = childData.get(groupPosition).get(childPosition)
				.getChildResult();
		if (value != null) {
			((RadioButton) childHolder.mRadio.getChildAt(Integer.valueOf(value
					.toString()))).setChecked(true);
		}
		value = childData.get(groupPosition).get(childPosition)
				.getChildInputFlag();
		if (value != null) {
			// 點檢標示,0表示手寫,1表示不可寫,2表示從系統帶出,3表示輸入關鍵字
			switch (Integer.valueOf(value.toString().trim())) {
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
				childHolder.mContent.setHint("請輸入關鍵字");
				break;
			case 4:
				childHolder.mContent
						.setBackgroundResource(R.drawable.bg_edittext_blue3);
				childHolder.mContent.setHint("");
				break;
			case 5:
				childHolder.mContent
						.setBackgroundResource(R.drawable.bg_edittext_blue3);
				childHolder.mContent.setHint("請輸入SN");
				break;
			case 6:
				childHolder.mContent
						.setBackgroundResource(R.drawable.bg_edittext_blue3);
				childHolder.mIcar.setHint("手動輸入");
				break;
			case 7:
				childHolder.mContent
						.setBackgroundResource(R.drawable.bg_edittext_blue3);
				break;
			default:
				break;
			}
		}

		childHolder.mContent.clearFocus();
		if (indexs != null && index == childPosition) {
			// 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
			childHolder.mContent.requestFocus();
			childHolder.mContent.setSelection(childHolder.mContent.getText()
					.length());
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

	/**
	 * 獲取當前編輯ID
	 * 
	 * @param group
	 * @param child
	 */
	public void setCoordinate(int group, int child) {
		this.groupIndex = group;
		this.childIndex = child;

	}

	public String getLocation() {
		return groupIndex + "%" + childIndex;
	}
}
