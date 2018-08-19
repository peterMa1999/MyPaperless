package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.adapter.CommonAdapter;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

/**
 * 個人已選線別
 * 
 * @author mgp 2016/01/06
 */
@SuppressLint("ResourceAsColor")
public class Showchooseline_ipqc extends BaseActivity {
	private Context context = Showchooseline_ipqc.this;
	private UserBean userBean;
	private TextView head_title;
	private HttpStart start;
	private String reportid, section, reportname, check_id, bUname, isaccess,
			check_idname, floor, line ,issection;
	private String CheckLines[] = null; // 存放線別
	private String[] passcheckline = null;
	ArrayList<HashMap<String, Object>> remoteWindowItem = new ArrayList<HashMap<String, Object>>(); // 存放的數據源
	private ListView showchooseline;
	lvButtonAdapter listItemAdapter = new lvButtonAdapter();
	List<Bean> data=new ArrayList<Showchooseline_ipqc.Bean>();
	CommonAdapter<Bean> adapter=null; 
	boolean isfirst = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showchooseline);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();

		// tiemTimelistenresiger = new Timelistenresiger(context,userBean);
		// tiemTimelistenresiger.alter();
		Intent intent = getIntent();
		reportid = intent.getStringExtra("reportid");
		section = intent.getStringExtra("section");
		reportname = intent.getStringExtra("reportname");
		check_id = intent.getStringExtra("check_id");
		bUname = intent.getStringExtra("bUname");
		isaccess = intent.getStringExtra("isaccess");
		check_idname = intent.getStringExtra("check_idname");
		issection = intent.getStringExtra("issection");
		start = new HttpStart(context, handler);
		findViewById();
		start.getServerData(0, MyConstant.GET_SAVE_LINE,
				userBean.getLogonName(), reportid);

	}

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

	class Bean{
		String floor;
		String line;
		
		public String getLine() {
			return line;
		}
		public void setLine(String line) {
			this.line = line;
		}
		boolean isCheck;
		public String getFloor() {
			return floor;
		}
		public void setFloor(String floor) {
			this.floor = floor;
		}
		public boolean getIsCheck() {
			return isCheck;
		}
		public void setIsCheck(boolean isCheck) {
			this.isCheck = isCheck;
		}
	}
	
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		ImageButton head_next = (ImageButton) findViewById(R.id.head_next);
		head_next.setVisibility(View.GONE);
		showchooseline = (ListView) findViewById(R.id.showchooseline);
		adapter=new CommonAdapter<Bean>(context,data,R.layout.showchooseline_item) {
			@Override
			public void convert(com.zsm.foxconn.mypaperless.adapter.ViewHolder holder,
					Bean t) {
				holder.setText(R.id.sclfloor, t.getFloor()).setText(R.id.scllines, t.getLine());
				Button check=holder.getView(R.id.begincheck_bt);
				Button delete=holder.getView(R.id.deleteline_bt);
				if (t.getIsCheck()) {
					delete.setVisibility(View.GONE);
					check.setText("已点检");
					check.setBackgroundColor(R.color.white);
					check.setEnabled(false);
				}else {
					check.setText("开始点检");
//					holder.setButton(R.id.begincheck_bt,0)
					delete.setBackgroundResource(R.drawable.deleteline_click_seletor);
//					holder.setButton(R.id.deleteline_bt, R.drawable.deleteline_click_seletor);
					check.setOnClickListener(new lvButtonListener(
							holder.getPosition(), "begincheck"));
					delete.setOnClickListener(new lvButtonListener(
							holder.getPosition(), "delete"));
				}
			}
		};
		
		head_title = (TextView) findViewById(R.id.head_title);
		head_title.setText("选择线别");
		
		
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {

				// 当前用户本节次勾选的线别
				if (key.equals(MyConstant.GET_SAVE_LINE)) {
					result = msg.getData().getStringArrayList(key);
//					if (remoteWindowItem.size() > 0) {
//						remoteWindowItem.clear();
//					}
					if (data.size() > 0) {
						data.clear();
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "暫無已選擇線別，返回上一頁選擇", 3000);
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						CheckLines = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							CheckLines[i - 1] = result.get(i).toString();
						}
						for (int i = 0; i < CheckLines.length; i += 2) {
//							HashMap<String, Object> map = new HashMap<String, Object>();
//							map.put("Itemfloor", CheckLines[i].toString());
//							map.put("Itemlines", CheckLines[i + 1].toString());
//							map.put("ischeck", "0");
//							remoteWindowItem.add(map);
							Bean bean=new Bean();
							bean.setFloor(CheckLines[i].toString());
							bean.setLine(CheckLines[i+1].toString());
							bean.setIsCheck(false);
							data.add(bean);
						}
					}
					start.getServerData(0, MyConstant.GET_CHECK_LINE_PASS,
							reportid,userBean.getMFG());
				}

				// 删除线别
				if (key.equals(MyConstant.DELETE_CHECK_LINE)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "刪除失敗,請稍後重試", 3000);
						return;
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						start.getServerData(0, MyConstant.GET_SAVE_LINE,
								userBean.getLogonName(), reportid);

					}
				}
				// 本節次已勾選線別中完成點檢的check_report_no
				if (key.equals(MyConstant.GET_CHECK_LINE_PASS)) {
					result = msg.getData().getStringArrayList(key);
					if (reportid.equalsIgnoreCase("IPQCOBA2016001")||reportid.equalsIgnoreCase("FQ3NMD054001A")||
							reportid.equalsIgnoreCase("FQ3NMD054002A")||reportid.equalsIgnoreCase("FQ3NMD054003A")) {
						passcheckline = new String[1];
						passcheckline[0] = "null";
					} else {
						if (result.get(0).toString()
								.equals(MyConstant.GET_FLAG_NULL)) {
							passcheckline = new String[1];
							passcheckline[0] = "null";
						}
						if (result.get(0).toString()
								.equals(MyConstant.GET_FLAG_TRUE)) {
							passcheckline = new String[(result.size() - 1)/2];
							for (int i = 1,j=0; i < result.size(); i+=2,j++) {
								passcheckline[j] = result.get(i).toString();
							}
						}
					}

					for (int i = 0; i < data.size(); i++) {
						for (int j = 0; j < passcheckline.length; j++) {
							if (passcheckline[j].toString().equals(
									data.get(i).getLine()
											.toString()) ) {
								data.get(i).setIsCheck(true);
							}
						}
					}
					
//					for (int i = 0; i < remoteWindowItem.size(); i++) {
//						for (int j = 0; j < passcheckline.length; j++) {
//							if (passcheckline[j].toString().indexOf(
//									remoteWindowItem.get(i).get("Itemlines")
//											.toString()) != -1) {
//								remoteWindowItem.get(i).put("ischeck", "1");
//							}
//						}
//					}
					
//					adapter.notifyDataSetChanged();
					if (isfirst) {
						showchooseline.setAdapter(adapter);
						isfirst=true;
					}else {
						adapter.notifyDataSetChanged();
					}
//					listItemAdapter.notifyDataSetChanged();
				}
				if (key.equals(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(context, "未連接服務器", 3000);
					return;
				}
			}
		}

	};
	private Context mContext = Showchooseline_ipqc.this;

	class lvButtonAdapter extends BaseAdapter {

		// private ArrayList<HashMap<String, Object>> remoteWindowItem = null;
		//
		// public lvButtonAdapter(
		// ArrayList<HashMap<String, Object>> remoteWindowItem) {
		// // TODO Auto-generated constructor stub
		// this.remoteWindowItem = remoteWindowItem;
		// }

		private class buttonViewHolder {
			TextView floorname;
			TextView linesname;
			Button buttoncheck;
			Button buttonDelete;

		}

		@Override
		public int getCount() {
			return remoteWindowItem.size();
		}

		@Override
		public Object getItem(int position) {
			return remoteWindowItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void removeItem(int position) {
			remoteWindowItem.remove(position);
			this.notifyDataSetChanged();
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			buttonViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.showchooseline_item, null);
				holder = new buttonViewHolder();

				holder.floorname = (TextView) convertView
						.findViewById(R.id.sclfloor);
				holder.linesname = (TextView) convertView
						.findViewById(R.id.scllines);
				holder.buttoncheck = (Button) convertView
						.findViewById(R.id.begincheck_bt);
				holder.buttonDelete = (Button) convertView
						.findViewById(R.id.deleteline_bt);
				convertView.setTag(holder);
			} else {
				holder = (buttonViewHolder) convertView.getTag();
			}
			if (remoteWindowItem.get(position).get("ischeck").toString()
					.equals("1")) {
				Log.i(TAG, "-4");
				holder.floorname.setTextColor(R.color.color1);
				holder.linesname.setTextColor(R.color.color1);
				holder.floorname.setText(remoteWindowItem.get(position)
						.get("Itemfloor").toString());
				holder.linesname.setText(remoteWindowItem.get(position)
						.get("Itemlines").toString());
				holder.buttoncheck.setText("已點檢");
				holder.buttoncheck.setBackgroundColor(R.color.color1);
				holder.buttoncheck.setEnabled(false);
				holder.buttonDelete.setEnabled(false);
				holder.buttonDelete
						.setBackgroundResource(R.drawable.toolbar_unfav_icon_res);
			}
			else{
				holder.floorname.setText(remoteWindowItem.get(position)
						.get("Itemfloor").toString());
				holder.linesname.setText(remoteWindowItem.get(position)
						.get("Itemlines").toString());
				holder.buttoncheck.setOnClickListener(new lvButtonListener(
						position, "begincheck")); // 開始點檢
				holder.buttonDelete.setOnClickListener(new lvButtonListener(
						position, "delete")); // 删除标识
			}
			return convertView;
		}

		
	}

	public void HeadBack(View view) {
//		Intent intent = new Intent(this, Choose_line_ipqc.class);
//		intent.putExtra("reportid", reportid);
//		intent.putExtra("section", section);
//		intent.putExtra("reportname", reportname);
//		intent.putExtra("bUname", bUname);
//		intent.putExtra("isaccess", isaccess);
		// tiemTimelistenresiger.stopthread();
//		startActivity(intent);
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
		this.finish();
	}
	class lvButtonListener implements OnClickListener {
		private int position;
		private String flag;

		lvButtonListener(int pos, String mflag) {
			position = pos;
			flag = mflag;
		}

		@Override
		public void onClick(View v) {
			v.getId();
			if (flag.equals("delete")) {
				mDialog(CheckLines[position + position],
						CheckLines[position + position + 1]); // 删除已经选择但未点检过的线别
			}
			if (flag.equals("begincheck")) {
				UIHelper.ToastMessage(context, "開始點檢");
				Intent intent = new Intent(Showchooseline_ipqc.this,
						Check_Project_ipqc.class);
				Bundle bundle = new Bundle();
				bundle.putString("linename", CheckLines[position + position
						+ 1]);
				// bundle.putString("shift", shift);
				bundle.putString("check_id", check_id);
				bundle.putString("section", section);
				bundle.putString("floorName", CheckLines[position
						+ position]);
				bundle.putString("reportname", reportname);
				bundle.putString("reportid", reportid);
				bundle.putString("bUname", bUname);
				bundle.putString("isaccess", isaccess);
				bundle.putString("check_idname", check_idname);
				bundle.putString("issection", issection);
				// bundle.putString("mfg", mfg2.getText().toString());
				// bundle.putString("class2", class2.getText().toString());
				intent.putExtras(bundle);
				// tiemTimelistenresiger.stopthread();
				Showchooseline_ipqc.this.startActivity(intent);
				overridePendingTransition(R.anim.move_right_in_activity,
						R.anim.move_left_out_activity);
//				finish();
			}
		}
	}

	public void mDialog(String floor_name, String line_name) {
		floor = floor_name;
		line = line_name;
		final AlertDialog alertDialog = new AlertDialog.Builder(
				Showchooseline_ipqc.this).create();
		alertDialog.setIcon(R.drawable.appseller_suggestitem_icon_clear);
		alertDialog.setTitle("系統提示:");
		alertDialog.setMessage("您将删除未点检过的线别：" + floor + "-" + line);
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(Showchooseline_ipqc.this, "取消",
								Toast.LENGTH_LONG).show();
						alertDialog.dismiss();
						return;
					}
				});
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "删除",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						start.getServerData(0,
								MyConstant.DELETE_CHECK_LINE,
								userBean.getLogonName(), floor, line,
								check_id, reportid);
					}
				});
		alertDialog.show();

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
//			Intent intent = new Intent(this, Choose_line_ipqc.class);
//			intent.putExtra("reportid", reportid);
//			intent.putExtra("section", section);
//			intent.putExtra("reportname", reportname);
//			intent.putExtra("bUname", bUname);
//			intent.putExtra("isaccess", isaccess);
//			intent.putExtra("issection", issection);
			// tiemTimelistenresiger.stopthread();
//			startActivity(intent);
			overridePendingTransition(R.anim.move_left_in_activity,
					R.anim.move_right_out_activity);
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
		start.getServerData(0, MyConstant.GET_SAVE_LINE,
				userBean.getLogonName(), reportid);
	}
}
