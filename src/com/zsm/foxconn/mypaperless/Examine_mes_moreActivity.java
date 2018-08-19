package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.ExamineMoreBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import android.os.Bundle;
import android.os.Handler;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class Examine_mes_moreActivity extends BaseActivity {
	ListView examine_more_list;
	private TextView title;
	ExpandableListView morelistview = null;
	MyAdapter adapter = null;
	List<String> parent = null;
	Map<String, List<String>> map = null;
	List<ExamineMoreBean> childlistbean = new ArrayList<ExamineMoreBean>();
	List<ExamineMoreBean> parentlistbean = new ArrayList<ExamineMoreBean>();
	ExamineMoreBean childbean, parentbean;
	private String RepostNO, report_Num, reportName, linearid, createby_status;// 点检编号
	Context context = Examine_mes_moreActivity.this;
	HttpStart start = null;
	String check_by = "", checkLabel = "", labelReasons = "", NodeNB = "",
			Status = "", ExaminePerson = "",check_order="";
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.examine_mes_more);
		title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(getResources().getString(R.string.message_aboutmore));

		Intent intent = getIntent();
		RepostNO = intent.getStringExtra("RepostNO").toString().trim();
		report_Num = intent.getStringExtra("report_num").toString().trim();// 报表编号
		reportName = intent.getStringExtra("report_Name").toString().trim();// 报表名
		linearid = intent.getStringExtra("linearid").toString().trim();
		createby_status = intent.getStringExtra("createby_status").toString().trim();  //點檢修改入口
		getDataMore();
		init();

	}

	public void init() {
		// 返回主页
		Button homebtn = (Button) findViewById(R.id.btn_submit);
		homebtn.setBackgroundResource(R.drawable.tabbar_home1);
		homebtn.setVisibility(View.GONE);
		homebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
			}
		});
		morelistview = (ExpandableListView) this
				.findViewById(R.id.examine_more_listView);
		// initData();// 整理数据
		adapter = new MyAdapter();
		morelistview.setAdapter(adapter);
		morelistview.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				RepostNO = parentlistbean.get(groupPosition).getCheckNB();
				checkLabel = childlistbean.get(groupPosition).getCheckLabel();
				labelReasons = childlistbean.get(groupPosition)
						.getLabelReasons();
				NodeNB = parentlistbean.get(groupPosition).getNodeNB();
				Status = childlistbean.get(groupPosition).getStatusnext();
				ExaminePerson = parentlistbean.get(groupPosition)
						.getProducePerson();
				check_order = parentlistbean.get(groupPosition).getCheck_order();
				if (linearid.equals("1")) {
					start.getServerData(0,
							MyConstant.GET_CHECK_REPORT_NO_BYCHECK,
							parentlistbean.get(groupPosition).getCheckNB(),
							parentlistbean.get(groupPosition).getNodeNB());
				} else {
					intent = new Intent(context,
							Examine_mes_detailActivity.class);
					intent.putExtra("RepostNO", RepostNO);// 点检编号
					intent.putExtra("report_num", report_Num);// 报表编号
					intent.putExtra("report_Name", reportName);// 报表名
					intent.putExtra("checkLabel", checkLabel);// 点检标注
					intent.putExtra("labelReasons", labelReasons);// 标注理由
					intent.putExtra("NodeNB", NodeNB);// 节数
					intent.putExtra("Status", Status);// 签核状态
					intent.putExtra("ExaminePerson", ExaminePerson);// 审核人
					intent.putExtra("check_order", check_order);	//審核順序
					intent.putExtra("linearid", linearid);
					intent.putExtra("createby_status", createby_status);
					startActivity(intent);
				}
				return true;
			}
		});

	}

	public void getDataMore() {
		// 按点检编号查询所属信息
		start = new HttpStart(context, handler);
		start.getServerData(3, MyConstant.GET_EXAMINE_MESSAGE_MORE, RepostNO);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.equals(MyConstant.GET_EXAMINE_MESSAGE_MORE)) {
					// 數據遍歷
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (childlistbean.size() > 0||parentlistbean.size()>0) {
						childlistbean.clear();
						parentlistbean.clear();
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)
							|| result.get(1).toString()
									.equals(MyConstant.GET_FLAG_NULL_DETAIL)) {
						adapter.notifyDataSetChanged();
						UIHelper.ToastMessage(context, "暫無可查詢的報表信息", 5000);
						adapter.notifyDataSetChanged();
						return;
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						String sataend = null;
						String sataendnext = "0";
						for (int i = 1; i < result.size(); i += 17) {
							// result.get(i + 3).toString().equals("1")&&
							if (result.get(i + 4).toString().equals("1")
									&& result.get(i + 5).toString().equals("0")) {
								sataend = getResources().getString(
										R.string.table_Status_endok);
								sataendnext = "1";
								// 签核通过
							} else if (result.get(i + 4).toString().equals("0")
									&& result.get(i + 5).toString().equals("0")) {
								sataend = getResources().getString(
										R.string.message_will_ok);
								sataendnext = "0";
							} else if (result.get(i + 4).toString().equals("1")
									&& result.get(i + 5).toString().equals("1")) {
								sataend = getResources().getString(
										R.string.message_no_pass);
								sataendnext = "2";
								// 签核驳回
							}
							parentbean = new ExamineMoreBean(result.get(i + 0)
									.toString(), result.get(i + 1).toString(),
									result.get(i + 2).toString(), sataend,result.get(i+3).toString());
							parentlistbean.add(parentbean);
							childbean = new ExamineMoreBean(result.get(i + 6)
									.toString(), result.get(i + 7).toString(),
									result.get(i + 8).toString(), result.get(
											i + 9).toString(), result.get(
											i + 10).toString(), result.get(
											i + 11).toString(), result.get(
											i + 12).toString(), result.get(
											i + 13).toString(), result.get(
											i + 14).toString(), result.get(
											i + 15).toString(), result.get(
											i + 16).toString(), sataendnext);
							childlistbean.add(childbean);
						}
						Log.i(">>parentlistbean>>>", parentlistbean.size() + "");
						Log.i(">>childlistbean>>>", childlistbean.size() + "");

					}
					adapter.notifyDataSetChanged();
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						adapter.notifyDataSetChanged();
						Toast.makeText(context, "未连接服务器....", 0).show();
						adapter.notifyDataSetChanged();
					}
					result.clear();
				}
				if (key.equals(MyConstant.GET_CHECK_REPORT_NO_BYCHECK)) {
					result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						if (check_by.length() > 0) {
							check_by = "";
						}
						for (int i = 1; i < result.size(); i++) {
							check_by += result.get(i).toString()
									+ " ";
						}
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						check_by = "無審核人";
					}
					intent = new Intent(context,
							Examine_mes_detailActivity.class);
					intent.putExtra("RepostNO", RepostNO);// 点检编号
					intent.putExtra("report_num", report_Num);// 报表编号
					intent.putExtra("report_Name", reportName);// 报表名
					intent.putExtra("checkLabel", checkLabel);// 点检标注
					intent.putExtra("labelReasons", labelReasons);// 标注理由
					intent.putExtra("NodeNB", NodeNB);// 节数
					intent.putExtra("Status", Status);// 签核状态
					intent.putExtra("ExaminePerson", ExaminePerson);// 审核人
					intent.putExtra("linearid", linearid);
					intent.putExtra("createby_status", createby_status);
					intent.putExtra("check_order", check_order);	//審核順序
					intent.putExtra("check_by", check_by);
					startActivity(intent);
				}
				if (key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					Toast.makeText(context, "未连接服务器....", 0).show();
				}

			}
		};
	};

	class MyAdapter extends BaseExpandableListAdapter {

		// 得到子item需要关联的数据
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// int key = parentlistbean.get(groupPosition);
			// return (map.get(key).get(childPosition));获取当前父容器item的子item的id位置
			// 获取当前childd
			// item的位置,child只有一个item,故是groupPosition
			return childlistbean.get(groupPosition);
		}

		// 得到子item的ID
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// return childPosition;
			return groupPosition;
		}

		// 设置子item的组件
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// ExpandableActivity.this.parent.get(groupPosition);获取当前父容器item的位置
			String key = Examine_mes_moreActivity.this.childlistbean
					.get(groupPosition) + "";
			ExamineMoreBean infobean = childlistbean.get(groupPosition);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) Examine_mes_moreActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.examine_mes_more_child,
						null);
			}
			ExamineMoreBean parent22 = parentlistbean.get(groupPosition);
			TextView business_tv = (TextView) convertView
					.findViewById(R.id.business_tv);
			TextView floor_tv = (TextView) convertView
					.findViewById(R.id.floor_tv);
			TextView line_nbtv = (TextView) convertView
					.findViewById(R.id.line_nbtv);
			TextView list_nbtv = (TextView) convertView
					.findViewById(R.id.list_nbtv);
			TextView matter_nbtv = (TextView) convertView
					.findViewById(R.id.matter_nbtv);
			TextView report_sumtv = (TextView) convertView
					.findViewById(R.id.report_sumtv);
			TextView face_nbtv = (TextView) convertView
					.findViewById(R.id.face_nbtv);
			TextView diff_error_tv = (TextView) convertView
					.findViewById(R.id.diff_error_tv);
			TextView label_reasonstv = (TextView) convertView
					.findViewById(R.id.label_reasonstv);
			TextView produce_time = (TextView) convertView
					.findViewById(R.id.produce_timetv);
			TextView check_label = (TextView) convertView
					.findViewById(R.id.Check_labeltv);
			business_tv.setText(infobean.getBusiness());
			floor_tv.setText(infobean.getFloor());
			line_nbtv.setText(infobean.getLineDiffent());
			list_nbtv.setText(infobean.getListNB());
			matter_nbtv.setText(infobean.getMatter_NB());
			report_sumtv.setText(infobean.getReportSum());
			face_nbtv.setText(infobean.getFaceNB());
			diff_error_tv.setText(infobean.getDiffError());
			label_reasonstv.setText(infobean.getLabelReasons());

			produce_time.setText(infobean.getProduceTime());
			check_label.setText(infobean.getCheckLabel());
			return convertView;
		}

		// 获取当前父item下的子item的个数
		@Override
		public int getChildrenCount(int groupPosition) {
			int size = parentlistbean.size();
			Log.i(">>size_childitem", "" + size);
			return 1;
		}

		// 获取当前父item的数据
		@Override
		public Object getGroup(int groupPosition) {
			return parentlistbean.get(groupPosition);
		}

		// 获取父item要显示的行数
		@Override
		public int getGroupCount() {
			return parentlistbean.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		// 设置父item组件
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) Examine_mes_moreActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.examine_mes_more_parent, null);
			}

			ExamineMoreBean parentlist = parentlistbean.get(groupPosition);

			TextView node_number = (TextView) convertView
					.findViewById(R.id.node_numbertv);
			TextView check_number = (TextView) convertView
					.findViewById(R.id.Check_numbertv);
			TextView produce_person = (TextView) convertView
					.findViewById(R.id.produce_persontv);
			TextView state = (TextView) convertView.findViewById(R.id.state_tv);
			final ImageView dowButton = (ImageView) convertView
					.findViewById(R.id.image_dowbtn);
			if (isExpanded) {
				dowButton.setBackgroundResource(R.drawable.btn_dow);
			} else {
				dowButton.setBackgroundResource(R.drawable.btn_up);
			}
			node_number.setText(parentlist.getNodeNB());
			check_number.setText(parentlist.getCheckNB());
			produce_person.setText(parentlist.getProducePerson());
			state.setText(parentlist.getState());
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	// 返回键按钮
	public void activity_back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onResume(){
		// TODO Auto-generated method stub
		super.onResume();
		start.getServerData(3, MyConstant.GET_EXAMINE_MESSAGE_MORE, RepostNO);
	}
}
