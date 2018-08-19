package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.Choose_line_ipqc.MyAdapter.ViewHolder;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.broadcast.Timelistenresiger;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

/**
 * 选取线别
 * 
 * @author mgp 2015/12/26
 */
public class Choose_line_ipqc extends BaseActivity {
	private String reportid, section, reportname, floorname, check_by,
			check_id, check_idname,bUname,isaccess,issection ;
	private String check_data, alarmtime;
	private HttpStart start;
	private Context context = Choose_line_ipqc.this;
	private String Floors[] = null; // 楼层
	private String Lines[] = null; // 线别
	static String position[][] = null;// 樓層線別
	private String checklines[] = null; // 该节次所有已经选好的数据库中的线别
	private TextView reportnametv, head_title, check_idnametv;
	private static Spinner spinnerfloor;
	private ListView listViewFilter;
	private ImageButton submitbt;
	private UserBean userBean;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>(); 	//所有线别
	ArrayList<String> listStr = null; // 选择的线别列表
	private String[] mychecklines = null;  //该节次个人已选的数据库中的线别
	private MyAdapter adapter;
	private boolean isFrist = false;
	private String Shift; // 班别
//	private Timelistenresiger tiemTimelistenresiger;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_line_ipqc);
		userBean = (UserBean) getApplicationContext();
		head_title = (TextView) findViewById(R.id.head_title);
		head_title.setText("选择线别");
//		tiemTimelistenresiger = new Timelistenresiger(context,userBean);
//		tiemTimelistenresiger.alter();
		Intent intent = getIntent();
		reportid = intent.getStringExtra("reportid");
		section = intent.getStringExtra("section");
		reportname = intent.getStringExtra("reportname");
		bUname = intent.getStringExtra("bUname");
		isaccess = intent.getStringExtra("isaccess");
		issection = intent.getStringExtra("issection");
		start = new HttpStart(context, handler);
		findViewById();
		initView();
		CheckLogin();
		start.getServerData(0, MyConstant.GET_ALERT_TIME);
		reportnametv.setText(reportname);
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		if (userBean.getLogonName() == null || userBean.getLogonName().length() == 0) {
			android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					tiemTimelistenresiger.stopthread();
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
		reportnametv = (TextView) findViewById(R.id.reportnametv);
		spinnerfloor = (Spinner) findViewById(R.id.spinnerfloor);
		listViewFilter = (ListView) findViewById(R.id.listViewFilter);
		check_idnametv = (TextView) findViewById(R.id.check_idnametv);
		submitbt = (ImageButton) findViewById(R.id.submitbt);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

		spinnerfloor.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (!isFrist) {
					isFrist = true;
				} else {
					floorname = arg0.getSelectedItem().toString();
					Log.i("tag", "msg" + floorname);
					start.getServerData(0, MyConstant.GET_CHECK_LINE,
							userBean.getMFG(), section, floorname,userBean.getSBU().toString());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		submitbt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// TODO Auto-generated method stub
					// start.getServerData(0, MyConstant.GET_CHECK_TIME);
					Log.i("tag", "msg" + "已选线>>>"+ position.length);
					
						ArrayList listline = new ArrayList();
						String liststr = "";
						String spiltflag="/";
						for (int i = 0; i < position.length; i++) {
							if (position[i] != null) {
								for (int j = 0; j < position[i].length; j++) {
									if (position[i][j] != null) {
										Log.i("tag", "msg" + "已选线别列表>>>"
												+Floors[i].toString()+ position[i][j].toString());
										listline.add(Floors[i].toString());
										listline.add(position[i][j].toString());
										continue;
									} else {
										continue;
									}
								}
							}
						}
						if (listline.size()!=0) {
							Log.i("tag", "msg" + ">>>"
									+listline.size());
							for (int i = 0; i < listline.size(); i++) {
								liststr = liststr+listline.get(i)+spiltflag;
							}
							start.getServerData(0,MyConstant.INSERT_SELECTED_LINE,userBean.getMFG(),reportid,liststr,userBean.getLogonName());
							Intent intent = new Intent(context,
									Showchooseline_ipqc.class);
							intent.putExtra("reportid", reportid);
							intent.putExtra("section", section);
							intent.putExtra("check_id", check_id);
							intent.putExtra("reportname", reportname);
							intent.putExtra("bUname", bUname);
							intent.putExtra("isaccess", isaccess);
							intent.putExtra("check_idname", check_idname);
							intent.putExtra("issection", issection);
//							tiemTimelistenresiger.stopthread();
							startActivity(intent);
							overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
						}else {
							start.getServerData(0, MyConstant.GET_SAVE_LINE,
									userBean.getLogonName(), reportid);
						}
				} catch (Exception e) {
					// TODO: handle exception
					UIHelper.ToastMessage(context, "添加異常");
					return;
				}
				
			}
		});

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				
				//獲取樓層信息
				if (key.equals(MyConstant.GET_FLOOR_IPQC)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無樓層信息", 3000);
						return;
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						position = new String[result.size()][];
						Floors = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							Floors[i - 1] = result.get(i).toString();
						}
						spinnerfloor.setAdapter(new ArrayAdapter(
								Choose_line_ipqc.this,
								android.R.layout.simple_dropdown_item_1line,
								Floors));
						floorname = Floors[0];
						start.getServerData(0, MyConstant.GET_CHECK_LINE,
								userBean.getMFG(), section, floorname,userBean.getSBU().toString());

					}
				}

				//獲取對應樓層的線別
				if (key.equals(MyConstant.GET_CHECK_LINE)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						Lines = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							Lines[i - 1] = result.get(i).toString();
						}
						showCheckBoxListView();
					}
				}
				//獲取相關time
				if (key.equals(MyConstant.GET_ALERT_TIME)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						Log.i("tagtime", "msg" + result.get(1).toString());
						check_id = result.get(1).toString();
						alarmtime = result.get(2).toString();
						Shift = result.get(3).toString();
						check_idname = result.get(4).toString();
						check_idnametv.setText(check_idname);
					}
					start.getServerData(0, MyConstant.GET_CHECK_LINE_ALREADY,
							userBean.getMFG(), reportid);
				}
				//獲取本節次所有已勾選的線別
				if (key.equals(MyConstant.GET_CHECK_LINE_ALREADY)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						checklines = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							checklines[i - 1] = result.get(i).toString();
						}
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						checklines = new String[result.size()];
						UIHelper.ToastMessage(context, "无已选择列表");
						checklines[0] = "null";
					}
					
					start.getServerData(0, MyConstant.GET_FLOOR_IPQC,
							userBean.getLogonName(), section ,userBean.getMFG(),userBean.getBU());
				}
				//添加線別至數據庫
				if (key.equals(MyConstant.INSERT_SELECTED_LINE)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "添加成功", 5000);
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "添加失败", 5000);
					}
				}
				//獲取本節次當前用戶已勾選的線別
				if (key.equals(MyConstant.GET_SAVE_LINE)) {
					result = msg.getData().getStringArrayList(key);
					mychecklines = new String[result.size()-1];
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i++) {
							mychecklines[i-1] = result.get(i).toString();
						}
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						mychecklines[0] = "null";
					}
					
					if (mychecklines[0].equals("null")) {
						UIHelper.ToastMessage(context, "請勾選線別!");
						return;
					} else {
						Intent intent = new Intent(context,
								Showchooseline_ipqc.class);
						intent.putExtra("reportid", reportid);
						intent.putExtra("section", section);
						intent.putExtra("reportname", reportname);
						intent.putExtra("check_id", check_id);
						intent.putExtra("bUname", bUname);
						intent.putExtra("isaccess", isaccess);
						intent.putExtra("check_idname", check_idname);
						intent.putExtra("issection", issection);
//						tiemTimelistenresiger.stopthread();
						startActivity(intent);
						overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
					}
				}
				if (key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(context, "未連接服務器", 3000);
					return;
				}
			}
		}

	};

	public void showCheckBoxListView() {
		if (list.size() > 0) {
			list.clear();
		}
		String str = "可点检";
		
		//本節次無已勾選的線別遍歷方式
		if (checklines[0].toString().equalsIgnoreCase("null")
				|| checklines.length == 0) {
			for (int i = 0; i < Lines.length; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>(); // 創建一個map集合
				Log.i("tagline", "msg" + Lines[i].toString());
				map.put("item_tv", Lines[i].toString());
				map.put("checkby_name", str);
				map.put("item_cb", false);
				list.add(map); // 添加至List集合
			}
		} else {
			
			//本節次存在已勾選的線別 則遍歷為已勾選帶出對應的用戶名
			for (int i = 0; i < Lines.length; i++) {
				int k = 0;
				HashMap<String, Object> map = new HashMap<String, Object>(); // 創建一個map集合
				for (int j = 0; j < checklines.length; j += 2) {
					if (checklines[j].equals(Lines[i])) {
						map.put("item_tv", Lines[i]); // list顯示線別
						map.put("checkby_name", checklines[j + 1]); // 显示点检人
						map.put("item_cb", true); // 默認狀態為true
						list.add(map); // 添加至List集合
						k = 1;
						break;
					}
				}
				if (k != 1) {
					map.put("item_tv", Lines[i]);
					map.put("checkby_name", str);
					map.put("item_cb", false);
					list.add(map); // 添加至List集合
					k=0;
				}
			}
		}
		listStr = new ArrayList<String>(); // 初始化
		adapter = new MyAdapter(this, list, R.layout.listview_floor_item,
				new String[] { // 數據源+list佈局
				"item_tv", "checkby_name", "item_cb" }, // 數據對象
				new int[] { R.id.textViewline, R.id.tvcreateperson,
						R.id.checkline }); // 佈局對象
		listViewFilter.setAdapter(adapter); // 添加適配器
		listViewFilter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				ViewHolder holder = (ViewHolder) view.getTag();
				holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态
				MyAdapter.isSelected.put(arg2, holder.cb.isChecked()); // 同时修改map的值保存状态

				if (holder.cb.isChecked() == true) {
					 
					if (!checklines[0].toString().equalsIgnoreCase("null")){
						for (int i = 0; i < checklines.length; i++) {
							if (checklines[i].toString().equals(Lines[arg2])) {
								UIHelper.ToastMessage(context, "本节次该线别已经被选择,添加無效",
										30000);
								return;
							}
						}
					}
					listStr.add(Lines[arg2]);
					position[(int) spinnerfloor.getSelectedItemId()][arg2] = Lines[arg2];		//加入數組

				} else {

					listStr.remove(Lines[arg2]);
					position[(int) spinnerfloor.getSelectedItemId()][arg2] = null;				//選擇狀態為false時為null
				}
			}
		});
	}

	public static class MyAdapter extends BaseAdapter {

		public static HashMap<Integer, Boolean> isSelected;
		private Context context = null;
		private LayoutInflater inflater = null;
		private List<HashMap<String, Object>> list = null;

		public class ViewHolder {
			public TextView tv = null;
			public CheckBox cb = null;
			public TextView check_bytv;
		}

		public MyAdapter(Context context, List<HashMap<String, Object>> list,
				int resource, String[] from, int[] to) {
			this.context = context;
			this.list = list;
			inflater = LayoutInflater.from(context);
			init();
		}

		// 初始化 设置所有checkbox都为未选择
		public void init() {
			isSelected = new HashMap<Integer, Boolean>();
			if (position[(int) spinnerfloor.getSelectedItemId()] == null) {
				for (int i = 0; i < list.size(); i++) {
					isSelected.put(i, false);
				}
				position[(int) spinnerfloor.getSelectedItemId()] = new String[list
						.size()];
			} else {
				for (int j = 0; j < position[(int) spinnerfloor
						.getSelectedItemId()].length; j++) {
					if (position[(int) spinnerfloor.getSelectedItemId()][j] != null) {
						isSelected.put(j, true);
					} else {
						isSelected.put(j, false);
					}
				}
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
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = inflater.inflate(R.layout.listview_floor_item, null);
				holder.tv = (TextView) view.findViewById(R.id.textViewline);
				holder.check_bytv = (TextView) view
						.findViewById(R.id.tvcreateperson);
				holder.cb = (CheckBox) view.findViewById(R.id.checkline);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.tv.setText(list.get(position).get("item_tv").toString());
			holder.check_bytv.setText(list.get(position).get("checkby_name")
					.toString());
			holder.cb.setChecked((Boolean) list.get(position).get("item_cb"));
			// HashMap<String, Object> map = list.get(position);

			// if (map != null) {
			// itemString = (String) map.get(keyString[0]);
			// holder.tv.setText(itemString);
			// }
			// holder.cb.setChecked(isSelected.get(position));
			return view;
		}
	}

	public void returnClick(View view) {
//		Intent intent = new Intent(this,Choose_report.class);
//		intent.putExtra("bUname", bUname);
//		intent.putExtra("isaccess", isaccess);
//		intent.putExtra("section", section);
//		intent.putExtra("issection", issection);
//		tiemTimelistenresiger.stopthread();
//		startActivity(intent);
		overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
		this.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
//	    	Intent intent = new Intent(this,Choose_report.class);
//			intent.putExtra("bUname", bUname);
//			intent.putExtra("isaccess", isaccess); 
//			intent.putExtra("section", section);
//			intent.putExtra("issection", issection);
//			tiemTimelistenresiger.stopthread();
//			startActivity(intent);
			overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
			this.finish();
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	    }
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("tag", "重新啟動Activity");
		start.getServerData(0, MyConstant.GET_ALERT_TIME);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("tag", "恢復Activity");
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("tag", "停止Activity");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("tag", "暫停Activity");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("tag", "銷毀Activity");
	}
}
