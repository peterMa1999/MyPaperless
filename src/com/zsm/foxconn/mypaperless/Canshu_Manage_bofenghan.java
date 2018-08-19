package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.Bofenghan_Canshu_bean;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.refreshlistview.OnRefreshListener;
import com.zsm.foxconn.mypaperless.refreshlistview.RefreshListView;
import com.zsm.qr.CaptureActivity;


/**
 * 
 * @author MPG
 *	
 * 2016-11-23 上午11:34:25
 * Canshu_Manage_bofenghan 波峰焊參數
 */
public class Canshu_Manage_bofenghan extends BaseActivity{

	private Intent intent;
	private String bUname, isaccess, reportid, reportName, section, issection,selecttype = "1";;
	private UserBean user;
	private RefreshListView examineListView;
	private ImageView imageview_add, imageview_xiaoxi;
	private TextView tv_canshu_mynews,canshu_pop_jizhong;
	private EditText bfh_canshu_jizhong;
	LinearLayout layout_canshu_seach;
	private int datamore = 50;// datamore每次多刷新加载的数据
	private HttpStart httpStart;
	private Context context = Canshu_Manage_bofenghan.this;
	private MyAdapter adapter;
	private List<Bofenghan_Canshu_bean> listexaminall = new ArrayList<Bofenghan_Canshu_bean>();
	private final int SCANER_CODE = 1;
	private Button bfh_canshu_scanQR,canshu_bfh_all_imagebt;
	private String[] linestr = null, floorstr = null;
	private String floor, line;
	private Spinner sp_bfh_line, sp_bfh_floor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.canshu_manage_bofenghan);
		user = (UserBean) getApplicationContext();
		CheckLogin();
		intent = getIntent();
		bUname = intent.getStringExtra("bUname");
		isaccess = intent.getStringExtra("isaccess");
		reportid = intent.getStringExtra("reportid");
		reportName = intent.getStringExtra("reportName");
		section = intent.getStringExtra("section");
		issection = intent.getStringExtra("issection");
		findViewById();
		initView();
		httpStart = new HttpStart(context, handler);
		httpStart.getServerData(3, MyConstant.BFH_GET_ALL_CANSHU,
				selecttype, datamore + "",user.getMFG());
		examineListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				intent = new Intent(context, Detailed_canshu_bofenghan.class);
				intent.putExtra("parameternum", listexaminall.get(arg2-1).getParameterNum());		//id
				intent.putExtra("section", listexaminall.get(arg2-1).getProductName());
				intent.putExtra("operaction", "0");		
				intent.putExtra("section", section);
				startActivity(intent);
			}
		});
		examineListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				SystemClock.sleep(500);
				datamore = 90;
				adapter.getRefreshData();
			}

			@Override
			public void onLoadMoring() {
				SystemClock.sleep(500);
				datamore += 30;
				adapter.getRefreshData();
			}
		});
		SystemClock.sleep(500);
		httpStart.getServerData(0, MyConstant.GET_FLOOR_IPQC,
				user.getLogonName(), section, user.getMFG(),user.getBU());
	}
	
	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		if (user.getLogonName() == null || user.getLogonName().length() == 0) {
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
		examineListView = (RefreshListView) findViewById(R.id.bofenghan_info_listView);
		imageview_add = (ImageView) findViewById(R.id.bfh_imageview_add);
		imageview_xiaoxi = (ImageView) findViewById(R.id.bfh_imageview_xiaoxi);
		tv_canshu_mynews = (TextView) findViewById(R.id.bfh_tv_canshu_mynews);
		bfh_canshu_jizhong = (EditText) findViewById(R.id.bfh_canshu_jizhong);
		layout_canshu_seach = (LinearLayout) findViewById(R.id.bfh_layout_canshu_seach);
		canshu_pop_jizhong = (TextView) findViewById(R.id.bfh_canshu_pop_jizhong);
		bfh_canshu_scanQR = (Button) findViewById(R.id.bfh_canshu_scanQR);
		sp_bfh_floor = (Spinner) findViewById(R.id.sp_bfh_floor);
		sp_bfh_line = (Spinner) findViewById(R.id.sp_bfh_line);
		canshu_bfh_all_imagebt = (Button) findViewById(R.id.canshu_bfh_all_imagebt);
		
		adapter = new MyAdapter();
		examineListView.setAdapter(adapter);
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		bfh_canshu_jizhong.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
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
				if (s.toString().length() > 0) {
					layout_canshu_seach.setVisibility(View.VISIBLE);
					canshu_pop_jizhong.setText("搜機種" + "“" + s.toString() + "”");
				} else {
					layout_canshu_seach.setVisibility(View.GONE);
				}
			}
		});
		
		canshu_pop_jizhong.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selecttype = "2";
				httpStart.getServerData(3, MyConstant.BFH_GET_ALL_CANSHU,
						selecttype, datamore + "",user.getMFG(),bfh_canshu_jizhong.getText().toString());
			}
		});
		sp_bfh_floor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				floor = arg0.getItemAtPosition(arg2).toString();
				httpStart.getServerData(3, MyConstant.GET_CHECK_LINE,
						user.getMFG(), section, floor,user.getSBU().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		sp_bfh_line.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				line = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		canshu_bfh_all_imagebt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selecttype = "4";
				httpStart.getServerData(3, MyConstant.BFH_GET_ALL_CANSHU,selecttype,
						datamore + "", user.getMFG(),floor, line);
			}
		});
	}
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = new ArrayList<String>();
				result = msg.getData().getStringArrayList(key);
				if (key.equalsIgnoreCase(MyConstant.BFH_GET_ALL_CANSHU)) {
					if (listexaminall.size() > 0) {
						listexaminall.clear();
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)){
						for (int i = 1; i < result.size() - 1; i += 7) {
							Bofenghan_Canshu_bean cs_bean = new Bofenghan_Canshu_bean(result.get(i).trim(),
									result.get(i + 1).trim(), result.get(i+2)
											.trim(), result.get(i + 3)
											.trim(), result.get(i + 4)
											.trim(),result.get(i + 5)
											.trim(),result.get(i + 6)
											.trim());
							listexaminall.add(cs_bean);
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無數據");
						adapter.notifyDataSetChanged();
						examineListView.onRefreshFinish();
						return;
					}
					
					adapter.notifyDataSetChanged();
					examineListView.onRefreshFinish();
//					httpStart.getServerData(0, MyConstant.GET_CHECK_NEWS,
//							"ALONE", reportid, user.getLogonName());
				}
				
				if (key.equalsIgnoreCase(MyConstant.GET_FLOOR_IPQC)) {
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						floorstr = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							floorstr[i - 1] = result.get(i).toString();
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無樓層信息");
						return;
					}
					sp_bfh_floor.setAdapter(new ArrayAdapter(Canshu_Manage_bofenghan.this,
							android.R.layout.simple_dropdown_item_1line,
							floorstr));
					floor = sp_bfh_floor.getSelectedItem().toString();
					httpStart.getServerData(3, MyConstant.GET_CHECK_LINE,
							user.getMFG(), section, floor,user.getSBU().toString());
				}
				
				if (key.equalsIgnoreCase(MyConstant.GET_CHECK_LINE)) {
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						linestr = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							linestr[i - 1] = result.get(i).toString();
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
//						linestr = new String[result.size()];
//						linestr[0] = "無";
						UIHelper.ToastMessage(context, "暫無線別信息");
						return;
					}
					sp_bfh_line.setAdapter(new ArrayAdapter(Canshu_Manage_bofenghan.this,
							android.R.layout.simple_dropdown_item_1line,
							linestr));
					line = sp_bfh_line.getSelectedItem().toString();
				}
			}
		}
	};
	
	// 适配器，显示查询的信息
		class MyAdapter extends BaseAdapter {

			public void getRefreshData() {
				if (selecttype.equals("1")) {
					httpStart.getServerData(3, MyConstant.BFH_GET_ALL_CANSHU,
							selecttype, datamore + "",user.getMFG());
				}else if (selecttype.equals("4")) {
					httpStart.getServerData(3, MyConstant.BFH_GET_ALL_CANSHU,selecttype,
							datamore + "", user.getMFG(),floor, line);
				}else {
					httpStart.getServerData(3, MyConstant.BFH_GET_ALL_CANSHU,
							selecttype, datamore + "",user.getMFG(),bfh_canshu_jizhong.getText().toString());
				}
			}

			public int getCount() {
				return listexaminall.size();
			}

			public Object getItem(int position) {
				return listexaminall.get(position);
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				View item = convertView != null ? convertView : View.inflate(
						getApplicationContext(),
						R.layout.bfh_info_listview_item, null);
				Bofenghan_Canshu_bean listex = listexaminall.get(position);

				final TextView info_nb_tv = (TextView) item
						.findViewById(R.id.bfh_info_nb_tv);
				final TextView info_jizhong_numbertv = (TextView) item
						.findViewById(R.id.bfh_jizhong_numbertv);
				final TextView info_floor_tv = (TextView) item
						.findViewById(R.id.bfh_floor_tv);
				final TextView bfh_jizhongname_tv = (TextView) item
						.findViewById(R.id.bfh_jizhongname_tv);
				final TextView bfh_line_tv = (TextView) item
						.findViewById(R.id.bfh_line_tv);
				
				info_nb_tv.setText(position+1+"");
				info_jizhong_numbertv.setText(listex.getProductName());
				info_floor_tv.setText(listex.getBuilding());
				bfh_jizhongname_tv.setText(listex.getModelName());
				bfh_line_tv.setText(listex.getLine());
				return item;
			}

		}
		
		public void add(View v) {
			switch (v.getId()) {
			case R.id.bfh_imageview_add:
				intent = new Intent(context, Detailed_canshu_bofenghan.class);
				intent.putExtra("operaction", "1");			//添加動作
				intent.putExtra("section", section);
				startActivity(intent);
				break;
			case R.id.bfh_canshu_scanQR:
				// 打开扫描界面扫描条形码或二维码
				Intent openCameraIntent = new Intent(context, CaptureActivity.class);
				startActivityForResult(openCameraIntent, SCANER_CODE);
				break;

			default:
				break;
			}
		}
		
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			// 处理扫描结果（在界面上显示）
			if (resultCode == RESULT_OK) {
				if (requestCode == SCANER_CODE) {
					Bundle bundle = data.getExtras();
					String scanResult = bundle.getString("result");
					bfh_canshu_jizhong.setText(scanResult);
				}
			}
		}
		
		@Override
		protected void onRestart() {
			// TODO Auto-generated method stub
			super.onRestart();
		}
}
