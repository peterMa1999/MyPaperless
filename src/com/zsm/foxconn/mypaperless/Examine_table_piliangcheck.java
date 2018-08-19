package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.Examine_table_piliangcheck.MyAdapter.ViewHolder_QR;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.ExamineAllMessageTableBean;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

public class Examine_table_piliangcheck extends BaseActivity implements OnClickListener{
	private Context context = Examine_table_piliangcheck.this;
	private UserBean userBean;
	private HttpStart httpStart;
	private Intent intent;
	private ListView listview_piliang;
	private TextView bartitle_txt;
	private ImageView imageview_add;
	private Button check_pass_bt;
	private CheckBox quanxuan_cb;
	List<ExamineAllMessageTableBean> piliang_listdata = new ArrayList<ExamineAllMessageTableBean>();
	private List<Map<String, Object>> list;
	private static List<Map<String, Object>> yixuan_list = new ArrayList<Map<String, Object>>();
	private static Map<Integer, Object> isselected = new HashMap<Integer, Object>();
	private String[] datalist;
	private MyAdapter myadpter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examine_table_piliangcheck);
		userBean = (UserBean) getApplicationContext();
		piliang_listdata = (List<ExamineAllMessageTableBean>) getIntent()
				.getSerializableExtra("piliang_listdata");
		CheckLogin();
		findViewById();
		httpStart = new HttpStart(context, handler);

	}
	
	public void Showdatalist() {
		list = new ArrayList<Map<String, Object>>();// 集合
		datalist = new String[piliang_listdata.size()*3];
		for (int i = 0,j=0; i < piliang_listdata.size(); i++,j+=3) {
			datalist[j] = i+1+"";
			datalist[j+1] = piliang_listdata.get(i).getCheck_report_NO();
//			datalist[j+2] = piliang_listdata.get(i).getCreate_Date();
			datalist[j+2] = piliang_listdata.get(i).getExamine_person();
		}
			if (datalist[0].toString().equalsIgnoreCase("null")
					|| datalist[0].toString().equals(null)) {
				UIHelper.ToastMessage(context, "暂无数据");
				return;
			} else {
				// 遍历
				for (int i = 0; i < datalist.length - 1; i += 3) {

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("piliang_check_no", datalist[i].toString());
					map.put("check_report_num", datalist[i+1].toString());
					map.put("check_repor_date", datalist[i + 2].toString());
					map.put("ischeck", false);
					list.add(map);
				}
			}
		// 适配器
		myadpter = new MyAdapter(
				context,
				list,
				R.layout.create_floorqr_item,
				new String[] {"piliang_check_no ","check_report_num", "check_repor_date", "ischeck" },
				new int[] {R.id.piliang_check_no, R.id.piliang_check_reportnum, R.id.tv_floor, R.id.tv_line });
		listview_piliang.setAdapter(myadpter);
		listview_piliang
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						ViewHolder_QR holder = (ViewHolder_QR) arg1.getTag();
						holder.piliang_check_cb.toggle();// 在每次获取点击的item时改变checkbox的状态
						if (holder.piliang_check_cb.isChecked() == true) {
							isselected.put(arg2,true);
							yixuan_list.add(list.get(arg2));
							check_pass_bt.setText("確定審核("+yixuan_list.size()+")");
						} else {
							yixuan_list.remove(list.get(arg2));
							isselected.put(arg2,false);
							if (yixuan_list.size()==0) {
								check_pass_bt.setText("確定審核");	
							}else {
								check_pass_bt.setText("確定審核("+yixuan_list.size()+")");
							}
						}
					}
				});

	}

	public static class MyAdapter extends BaseAdapter {

		private Context context = null;
		private LayoutInflater inflater = null;
		private List<Map<String, Object>> list = null;

		public class ViewHolder_QR {
			public TextView piliang_check_no = null;
			public TextView piliang_check_reportnum = null;
			public TextView piliang_check_date = null;
			public CheckBox piliang_check_cb = null;
		}

		public MyAdapter(Context context, List<Map<String, Object>> list,
				int resource, String[] from, int[] to) {
			this.context = context;
			this.list = list;
			inflater = LayoutInflater.from(context);
			for (int i = 0; i < list.size(); i++) {
				isselected.put(i, false);
			}
		}

		public void allchoose(boolean bl) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("ischeck", bl);
				isselected.put(i, bl);
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder_QR holder = null;
			if (view == null) {
				holder = new ViewHolder_QR();
				view = inflater.inflate(R.layout.piliang_check_item, null);
				holder.piliang_check_no = (TextView) view
						.findViewById(R.id.piliang_check_no);
				holder.piliang_check_reportnum = (TextView) view
						.findViewById(R.id.piliang_check_reportnum);
				holder.piliang_check_date = (TextView) view
						.findViewById(R.id.piliang_check_date);
				holder.piliang_check_cb = (CheckBox) view
						.findViewById(R.id.piliang_check_cb);
				view.setTag(holder);
			} else {
				holder = (ViewHolder_QR) view.getTag();
			}
			holder.piliang_check_no.setText(list.get(position)
					.get("piliang_check_no").toString());
			holder.piliang_check_reportnum.setText(list.get(position)
					.get("check_report_num").toString());
			holder.piliang_check_date.setText(list.get(position)
					.get("check_repor_date").toString());
			holder.piliang_check_cb.setChecked((Boolean) list.get(position)
					.get("ischeck"));
			if ((Boolean) isselected.get(position) == true) {
				holder.piliang_check_cb.setChecked(true);
			} else {
				holder.piliang_check_cb.setChecked(false);
			}
			return view;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = msg.getData().getStringArrayList(key);
				if (key.equals(MyConstant.PILIANG_CHECK_PASS))  {
					if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "簽核成功，請返回刷新查看");
					}else if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "簽核異常");
					}else if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_UNUNITED)) {
						UIHelper.ToastMessage(context, "未连接服务器");
					}
				}
			}
		}
	};

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		if (userBean.getLogonName() == null
				|| userBean.getLogonName().length() == 0) {
			android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// tiemTimelistenresiger.stopthread();
					AppManager.getInstance().killAllActivity();
					startActivity(new Intent(context, LoginActivity.class));
				}
			};
			Builder builder = new Builder(context);
			builder.setTitle("您还未登陆，请先登录");
			builder.setPositiveButton("确定", listener);
			builder.create().show();
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		listview_piliang = (ListView) findViewById(R.id.listview_piliang);
		bartitle_txt = (TextView) findViewById(R.id.bartitle_txt);
		bartitle_txt.setText("批量審核");
		imageview_add = (ImageView) findViewById(R.id.imageview_add);
		imageview_add.setVisibility(View.GONE);
		quanxuan_cb = (CheckBox) findViewById(R.id.pl_quanxuan_cb);
		check_pass_bt = (Button) findViewById(R.id.check_pass_bt);
		quanxuan_cb.setOnClickListener(this);
		check_pass_bt.setOnClickListener(this);
		Showdatalist();
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	// 返回键按钮
	public void activity_back(View v) {
		this.finish();
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.check_pass_bt:
			if (yixuan_list.size()>0) {
				AlertDialog alertDialog = new AlertDialog.Builder(context).create();
				alertDialog.setIcon(R.drawable.ic_system);
				alertDialog.setTitle("系統提示:");
				alertDialog.setMessage("是否全部審核通過?");
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "確定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								String str = "";
								for (int i = 0; i < yixuan_list.size(); i++) {
										str += yixuan_list.get(i).get("check_report_num").toString()+MyConstant.GET_FLAG1;	
								}
								httpStart.getServerData(3,
										MyConstant.PILIANG_CHECK_PASS, userBean.getLogonName().toString(),
										str);
							}
						});
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								return;
							}
						});
				alertDialog.show();
			}else {
				UIHelper.ToastMessage(context, "未選擇");
			}
			break;
			
		case R.id.pl_quanxuan_cb:
			if (quanxuan_cb.isChecked() == true) {
				myadpter.allchoose(true);
				yixuan_list.clear();
				yixuan_list.addAll(list);
				myadpter.notifyDataSetChanged();
				check_pass_bt.setText("確定審核("+yixuan_list.size()+")");
			} else {
				myadpter.allchoose(false);
				yixuan_list.clear();
				myadpter.notifyDataSetChanged();
				check_pass_bt.setText("確定審核");
			}
			break;
		default:
			break;
		}
	}
}
