package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.zsm.foxconn.mypaperless.adapter.Addreport_MyAdapter;
import com.zsm.foxconn.mypaperless.adapter.Addreport_MyAdapter_Linename;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.ChildItem;
import com.zsm.foxconn.mypaperless.bean.Line_childItem;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.GenerateQRCodeActivity;
import com.zsm.foxconn.mypaperless.help.ModifyDialog;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Addreport_GroupListFragment extends Fragment {
	private View view;
	private EditText RNO_edit, RNO_name;
	private AutoCompleteTextView rate_edit, check_section;
	private ExpandableListView expandableListView;
	private ListView add_listview_linename;
	public static Addreport_MyAdapter adapter;
	private static Addreport_MyAdapter_Linename listview_adapter = null;
	public static List<String> parentList, lineparentList;
	public static Map<String, List<ChildItem>> map;
	private Spinner check_department;
	private RelativeLayout add_report_line_mfgvii;
	/**
	 * 保存線別
	 */
	public static List<Line_childItem> map_linename = new ArrayList<Line_childItem>();
	
	private ModifyDialog dialog;
	private EditText edit_modify;
	HttpStart start = null;
	private LinearLayout image_add_linear;
	private Button image_add_linename;
	private UserBean userBean;
	private TextView head_title;
	private int currentGroup, currentChild;
	// public static SharedPreferences sp;
	public static Editor editor;
	public static String dataMap, dataParentList, addlinestr = "";
	private Intent intent;
	private ImageButton head_next, head_back;
	private GenerateQRCodeActivity genrateqrcode = new GenerateQRCodeActivity();
	private String reportnum = ""; // 報表ID
	private boolean isfirst = true;
	List<String> arrays = new ArrayList<String>();
	private String Team;
	String a = "",b = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		userBean = (UserBean) getActivity().getApplicationContext();
		view = inflater.inflate(R.layout.fragment_group_list, container, false);
		start = new HttpStart(getActivity(), handler);
		String[] array = getResources().getStringArray(R.array.floorname);
		for (int i = 0; i < array.length; i++) {
			arrays.add(array[i]);
		}
		RNO_edit = (EditText) view.findViewById(R.id.RNO_edit);
		RNO_name = (EditText) view.findViewById(R.id.RNO_name);
		rate_edit = (AutoCompleteTextView) view.findViewById(R.id.rate_edit);
		add_report_line_mfgvii = (RelativeLayout) view
				.findViewById(R.id.add_report_line_mfgvii);
		add_listview_linename = (ListView) view
				.findViewById(R.id.add_listview_linename);
		image_add_linename = (Button) view
				.findViewById(R.id.image_add_linename);
		check_section = (AutoCompleteTextView) view
				.findViewById(R.id.check_section);
		check_department = (Spinner) view
				.findViewById(R.id.check_department);
		String[] array_check_yeild = getResources().getStringArray(
				R.array.check_yeild);
		ArrayAdapter<String> adapt_rate = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_dropdown_item_1line,
				array_check_yeild);
		rate_edit.setAdapter(adapt_rate);
		String[] array_section = getResources().getStringArray(R.array.section);
		ArrayAdapter<String> adapt_section = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_dropdown_item_1line,
				array_section);
		check_section.setAdapter(adapt_section);
		start.getServerData(0,
				MyConstant.GET_MFG_TEAM, userBean.getMFG().toString());
		head_title = (TextView) view.findViewById(R.id.head_title);
		head_title.setText("新增報表");
		
		check_section.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.toString().equalsIgnoreCase("SMT")||s.toString().equalsIgnoreCase("ASSY")||s.toString().equalsIgnoreCase("PTH")||
					s.toString().equalsIgnoreCase("PACK")){
						add_report_line_mfgvii.setVisibility(View.GONE);
					}else {
						add_report_line_mfgvii.setVisibility(View.VISIBLE);
					}
				
				}
		});
		check_department.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (!isfirst) {
					isfirst = true;
				} else {
					Team = arg0.getItemAtPosition(arg2).toString();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		head_back = (ImageButton) view.findViewById(R.id.head_back);
		head_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
						.create();
				alertDialog.setIcon(R.drawable.ic_system);
				alertDialog.setTitle("系統提示:");
				alertDialog.setMessage("是否退出編輯?");
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								intent = new Intent(getActivity(),
										MainActivity.class);
								startActivity(intent);
								getActivity().overridePendingTransition(
										R.anim.move_left_in_activity,
										R.anim.move_right_out_activity);
								getActivity().finish();
							}
						});
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								return;
							}
						});
				alertDialog.show();

			}
		});

		head_next = (ImageButton) view.findViewById(R.id.head_next);
		head_next.setImageResource(R.drawable.add_submit_click);
		head_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
						.create();
				alertDialog.setIcon(R.drawable.ic_system);
				alertDialog.setTitle("系統提示:");
				alertDialog.setMessage("請務必使用繁體字,确认提交?");
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								if (RNO_edit.getText().toString().length() == 0
										|| RNO_name.getText().toString()
												.length() == 0
										|| rate_edit.getText().toString()
												.length() == 0
										|| check_section.getText().toString()
												.length() == 0
										|| Team.length() == 0) {
									UIHelper.ToastMessage(getActivity(), "不能為空");
									return;
								}else if (RNO_name.length()>18) {
									UIHelper.ToastMessage(getActivity(), "報表名字不能超過18位");
									return;
								} else {
									if (add_report_line_mfgvii.getVisibility()==View.VISIBLE) {
										if (map_linename.size() == 0) {
											UIHelper.ToastMessage(
													getActivity(),
													"請添加點檢樓層及點檢物");
											return;
										} else {
											boolean bl = true;
											map_linename = listview_adapter
													.get_addreport_list();
											System.out.println(map_linename
													.size() + "<<<<<<");
											for (int i = 0; i < map_linename
													.size(); i++) {
												if (map_linename.get(i)
														.getFloorname()
														.equals("")
														|| map_linename.get(i)
																.getLinename()
																.equals("")) {
													bl = false;
													UIHelper.ToastMessage(
															getActivity(),
															"輸入不能為空或者不能含有特殊字符");
													return;
												}
											}
											if (bl) {
												if (addlinestr.length() > 0) {
													addlinestr = "";
												}
												for (int j = 0; j < map_linename
														.size(); j++) {
													addlinestr += map_linename
															.get(j)
															.getFloorname()
															.toString()
															+ "&&"
															+ map_linename
																	.get(j)
																	.getLinename()
															+ "&&";
													System.out
															.println(addlinestr
																	+ "<<<<<<");
												}
											}
										}
									}
									map = adapter.newMap();
									a = getResult(RNO_edit.getText()
											.toString().trim(), RNO_name
											.getText().toString().trim(),
											rate_edit.getText().toString()
													.trim(), check_section
													.getText().toString()
													.trim(), Team, userBean.getMFG(),
											userBean.getSBU(),
											userBean.getSite(),
											userBean.getBU());
									
									for (int i = 0; i < parentList.size(); i++) {
										for (int j = 0; j < map.get(
												parentList.get(i).toString())
												.size(); j++) {
											if (parentList.get(i).toString()
													.length() == 0) {
												UIHelper.ToastMessage(
														getActivity(),
														"大項中未添加小項");
												return;
											}
											ChildItem item = new ChildItem();
											item = map.get(
													parentList.get(i)
															.toString()).get(j);
											b += getResult2(parentList.get(i)
													.toString(), item
													.getChildText(), item
													.getChildFlag())
													+ "-";
										}
									}
									start.getServerData(0,
											MyConstant.RNO_ISEXIT, RNO_edit.getText()
											.toString());
								}

							}
						});
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								return;
							}
						});
				alertDialog.show();
			}
		});
//		if (add_report_line_mfgvii.getVisibility()==View.VISIBLE) {
			// add_initData();
			addChild_linename(arrays.get(0).toString(),"");
			listview_adapter = new Addreport_MyAdapter_Linename(getActivity(),
					map_linename, arrays);
			add_listview_linename.setAdapter(listview_adapter);
			image_add_linename.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					addChild_linename(arrays.get(0).toString(), "");
					map_linename=listview_adapter.get_addreport_list();
					listview_adapter.notifyDataSetChanged();
					for (int k = 0; k < map_linename.size(); k++) {
						System.out.println(">>>>"+map_linename.get(k).getFloorname().toString()+"\t"+map_linename.get(k).getLinename().toString());
					}
				}
			});
//		}

		expandableListView = (ExpandableListView) view
				.findViewById(R.id.expandablelistview);
		image_add_linear = (LinearLayout) view.findViewById(R.id.image_add_linear);
		image_add_linear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertAddDialog(getActivity(), "新增点检大标题");
			}
		});

		initData();

		adapter = new Addreport_MyAdapter(
				getActivity().getApplicationContext(), parentList, map);
		expandableListView.setAdapter(adapter);

		// 设置子项点击事件
		MyOnClickListener myListener = new MyOnClickListener();
		expandableListView.setOnChildClickListener(myListener);

		// 设置长按点击事件
		MyOnLongClickListener myLongListener = new MyOnLongClickListener();
		expandableListView.setOnItemLongClickListener(myLongListener);

		return view;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> result = null;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) // 键值对
			{
				/*
				 * 添加报表;
				 */
				if (key.equals(MyConstant.ADD_REPORT)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						if (add_report_line_mfgvii.getVisibility()==View.VISIBLE
								&& addlinestr.length() != 0) {
							reportnum = result.get(result.size() - 1);
							start.getServerData(3,
									MyConstant.ADD_REPORT_FLOORLINE,
									addlinestr, userBean.getMFG(), userBean
											.getSBU(), check_section.getText()
											.toString());
						} else {
							Toast.makeText(getActivity(), "报表添加成功", 1000)
									.show();
							Intent intent = new Intent(getActivity(),
									MainActivity.class);
							startActivity(intent);
							getActivity().finish();
						}

					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						a = "";
						b = "";
						Toast.makeText(getActivity(), "报表添加失败", 1000).show();
					}
				}

				if (key.equals(MyConstant.ADD_REPORT_FLOORLINE)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						Toast.makeText(getActivity(), "报表添加成功", 1000).show();
						lineparentList = new ArrayList<String>();
						for (int i = 0; i < map_linename.size(); i++) {
							lineparentList.add(map_linename.get(i)
									.getFloorname().toString());
							lineparentList.add(map_linename.get(i)
									.getLinename().toString());
						}
						genrateqrcode.createqrcode(lineparentList, reportnum,
								getActivity());
						UIHelper.ToastMessage(getActivity(),
								"二维码已生成至Mypaperless/images/myQRcode中",5000);
						Intent intent = new Intent(getActivity(),
								MainActivity.class);
						startActivity(intent);
						getActivity().finish();
					}
				}
				
				if (key.equals(MyConstant.GET_MFG_TEAM)) {
					result = msg.getData().getStringArrayList(key);
					String[] team_str = null;
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						team_str = new String[result.size()-1];
						for (int i = 1; i < result.size(); i++) {
							team_str[i - 1] = result.get(i).toString();
						}
					}
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						team_str = new String[1];
						team_str[0] = userBean.getTeam().toString();
						
					}
					
					check_department.setAdapter(new ArrayAdapter(
							getActivity(),
							android.R.layout.simple_dropdown_item_1line,
							team_str));
					Team = check_department.getSelectedItem().toString();
				}
				if (key.equals(MyConstant.RNO_ISEXIT)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(getActivity(), "報表編號已存在,添加失敗");
						a = "";
						b = "";
						return;
					} 
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) {
						start.getServerData(0,
								MyConstant.ADD_REPORT, a, b);
					}
				}
			}
		}
	};

	public void initData() {
		map = new HashMap<String, List<ChildItem>>();
		parentList = new ArrayList<String>();
		if (dataMap == null || dataParentList == null) {
			parentList = new ArrayList<String>();
			parentList.add("推板機(長按修改此大項)");

			List<ChildItem> childList = new ArrayList<ChildItem>();
			ChildItem item1 = new ChildItem();
			item1.setItem("推板是否平穩無卡板及擋板現象", "");
			ChildItem item2 = new ChildItem();
			item2.setItem("推板方向與SOP一致", "");
			childList.add(item1);
			childList.add(item2);
			map.put("推板機(長按修改此大項)", childList);
		} else {
			try {
				// 初始化parentList
				JSONArray jsonArray = new JSONArray(dataParentList);
				for (int i = 0; i < jsonArray.length(); i++) {
					parentList.add(jsonArray.get(i).toString());
				}

				// 初始化map
				JSONObject jsonObject = new JSONObject(dataMap);
				for (int i = 0; i < jsonObject.length(); i++) {
					String key = jsonObject.getString(parentList.get(i));
					JSONArray array = new JSONArray(key);
					List<ChildItem> childList = new ArrayList<ChildItem>();
					for (int j = 0; j < array.length(); j++) {
						ChildItem item = new ChildItem();
						item.setItem(array.get(j).toString(), "");
						childList.add(item);
					}
					map.put(parentList.get(i), childList);
				}

				Log.d("eric", "①：" + map + "②：" + parentList);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("eric", "String转Map或List出错" + e);
			}
		}
		Log.e("eric", dataMap + "!&&!" + dataParentList);
	}

	// 自定义点击监听事件
	public class MyOnClickListener implements
			ExpandableListView.OnChildClickListener {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			String str = "choose" + groupPosition + "-" + childPosition;
			Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	// 自定义长按监听事件
	public class MyOnLongClickListener implements
			AdapterView.OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// 长按子项
			if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
				long packedPos = ((ExpandableListView) parent)
						.getExpandableListPosition(position);
				int groupPosition = ExpandableListView
						.getPackedPositionGroup(packedPos);
				int childPosition = ExpandableListView
						.getPackedPositionChild(packedPos);

				currentGroup = groupPosition;
				currentChild = childPosition;

				String str = (String) adapter.getChild(groupPosition,
						childPosition);
				alertModifyDialog("修改此项名称", str);
				Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
				return true;
				// 长按组
			} else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
				long packedPos = ((ExpandableListView) parent)
						.getExpandableListPosition(position);
				int groupPosition = ExpandableListView
						.getPackedPositionGroup(packedPos);
				int childPosition = ExpandableListView
						.getPackedPositionChild(packedPos);

				currentGroup = groupPosition;
				currentChild = childPosition;

				String group = parentList.get(groupPosition);
				alertModifyDialog("修改组名称", group);

				String str = (String) adapter.getGroup(groupPosition);
				Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	}

	// 新增组
	public static void addGroup(String newGroupName) {
		parentList.add(newGroupName);
		List<ChildItem> list = new ArrayList<ChildItem>();
		map.put(newGroupName, list);
		adapter.notifyDataSetChanged();
	}

	// 新增子项到指定组
	public static void addChild(int groupPosition, String newChildName,
			String flag) {
		String groupName = parentList.get(groupPosition);
		List<ChildItem> lists = map.get(groupName);
		ChildItem item1 = new ChildItem();
		item1.setItem(newChildName, flag);
		lists.add(item1);
		adapter.notifyDataSetChanged();
	}

	// 新增子项（點檢物）
	public void addChild_linename(String floorname, String linename) {
		Line_childItem item1 = new Line_childItem();
		item1.setchilditem(floorname, linename);
		map_linename.add(item1);
	}

	// 删除指定组
	public static void deleteGroup(int groupPos) {
		String groupName = parentList.get(groupPos);
		map.remove(groupName);
		parentList.remove(groupPos);
		adapter.notifyDataSetChanged();
	}

	// 删除指定子项
	public static void deleteChild(int groupPos, int childPos) {
		String groupName = parentList.get(groupPos);
		List<ChildItem> list = map.get(groupName);
		list.remove(childPos);
		adapter.notifyDataSetChanged();
	}

	// 删除指定子项（點檢物）
	public static void deleteChild_linename(int childPos) {
		try {
			Log.i(">>>", childPos + ">>" + map_linename.size());
			// if (map_linename.size()>0) {
			// map_linename.clear();
			// }
			map_linename = listview_adapter.get_addreport_list();
			 map_linename.remove(childPos);
			listview_adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// 修改该项名称
	public void modifyName(int groupPosition, int childPosition,
			String modifyName) {
		if (childPosition < 0) {
			// 修改组名称
			String groupName = parentList.get(groupPosition);
			if (!groupName.equals(modifyName)) {
				map.put(modifyName, map.get(groupName));
				map.remove(groupName);
				parentList.set(groupPosition, modifyName);
			}
		}
		// } else {
		// // 修改子项名称
		// String group = parentList.get(groupPosition);
		// List<ChildItem> list = map.get(group);
		// list.set(childPosition, object)
		// map.put(group, list);
		// }
		adapter.notifyDataSetChanged();
	}

	// 弹修改对话框
	public void alertModifyDialog(String title, String name) {
		dialog = new ModifyDialog(getActivity(), title, name);
		edit_modify = dialog.getEditText();
		dialog.setOnClickCommitListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edit_modify.getText().toString().length() == 0) {
					Toast.makeText(getActivity(), "無輸入內容", Toast.LENGTH_SHORT);
				} else {
					modifyName(currentGroup, currentChild, edit_modify
							.getText().toString());
					dialog.dismiss();
				}
			}
		});
		dialog.show();
	}

	// 弹新增组对话框
	public void alertAddDialog(Context context, String title) {
		dialog = new ModifyDialog(context, title, null);
		edit_modify = dialog.getEditText();
		dialog.setOnClickCommitListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edit_modify.getText().toString().length() == 0) {
					Toast.makeText(getActivity(), "無輸入內容", Toast.LENGTH_SHORT)
							.show();
				} else {
					addGroup(edit_modify.getText().toString());
					dialog.dismiss();
				}
			}
		});
		dialog.show();
	}

	public static String getResult(String... strings)// 用“#”RNO_edit,RNO_name,rate_edit,check_section,check_department
	{
		String result = "";
		for (int i = 0; i < strings.length; i++) {
			result += strings[i] + "#";
		}
		return result;
	}

	public static String getResult2(String... strings)// 用“~”拼接大项加一条小项;
	{
		String result = "";
		for (int i = 0; i < strings.length; i++) {
			result += strings[i] + MyConstant.GET_FLAG3;
		}
		return result;
	}

}