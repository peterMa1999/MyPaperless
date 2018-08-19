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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

/**
 * 
 * @author MPG
 *	
 * 2016/07/12 下午5:21:34
 * Floor_manage 樓層管理
 */
public class Floor_manage extends BaseActivity {
	private Context context = Floor_manage.this;
	private Intent intent;
	private String section, issection, bUname, isaccess;
	private UserBean userBean;
	private HttpStart httpStart;
	private List<Map<String, Object>> list;
	private String[] datalist;
	private Boolean isdatalist = false;
	private ListView listview_floor_info;
	private String line_name, floor_name, sbu_name;
	private ImageView imageview_add;
	private Button bt_create_qr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.floor_manage);
		userBean = (UserBean) getApplicationContext();
		intent = getIntent();
		section = intent.getStringExtra("section");
		issection = intent.getStringExtra("issection");
		bUname = intent.getStringExtra("bUname");
		isaccess = intent.getStringExtra("isaccess");
		httpStart = new HttpStart(context, handler);
		CheckLogin();
		findViewById();
		httpStart.getServerData(3, MyConstant.GET_MANAGE_FLOOR,
				userBean.getMFG(), section,userBean.getSBU());
		bt_create_qr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context,Create_floorQR.class);
				 intent.putExtra("bUname", bUname);
				 intent.putExtra("issection", issection);
				 intent.putExtra("isaccess", isaccess);
				 intent.putExtra("section", section);
				 startActivity(intent);
			}
		});
		listview_floor_info.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				sbu_name = list.get(position).get("sbu_name").toString();
				floor_name = list.get(position).get("floor_name").toString();
				line_name = list.get(position).get("line_name").toString();
				if (userBean.getUserLevel().equals("2")) {
					final AlertDialog alertDialog = new AlertDialog.Builder(context)
							.create();
					alertDialog.setIcon(R.drawable.ic_system);
					alertDialog.setTitle("系統提示:");
					alertDialog.setMessage("當前選擇的為:" + sbu_name + "-"
							+ floor_name + "-" + line_name);
					alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
							"修改", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									intent = new Intent(context,Add_floor.class);
									intent.putExtra("sbu_name", sbu_name);
									intent.putExtra("floor_name", floor_name);
									intent.putExtra("line_name", line_name);
									intent.putExtra("section", section);
									intent.putExtra("operating", "1");		//修改-操作標示
									startActivity(intent);
								}
							});
					alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "刪除",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									final AlertDialog alert = new AlertDialog.Builder(context)
									.create();
									alert.setMessage("是否繼續刪除");
									alert.setButton(DialogInterface.BUTTON_POSITIVE,
									"確定", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											httpStart.getServerData(0,MyConstant.DELETE_FlOOR_LINE,userBean.getMFG(),sbu_name,floor_name,line_name,section);
											
										}
									});
									alert.setButton(DialogInterface.BUTTON_NEGATIVE,
											"取消", new DialogInterface.OnClickListener() {

												@Override
												public void onClick(DialogInterface dialog,
														int which) {
													// TODO Auto-generated method stub
													alert.dismiss();
												}
											});
									alert.show();
						}
					});
					alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
							"取消", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									alertDialog.dismiss();
									return;
								}
							});
					alertDialog.show();
				} else {
					return;
				}

			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = msg.getData().getStringArrayList(key);
				if (key.equalsIgnoreCase(MyConstant.GET_MANAGE_FLOOR)) {
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						datalist = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							datalist[i - 1] = result.get(i);
						}
					}
					if (result.get(0).equals(MyConstant.GET_FLAG_NULL)) {
						datalist = new String[result.size()];
						datalist[0] = "null";
					}
					isdatalist = true;
					Showdatalist();
				}
				
				if (key.equalsIgnoreCase(MyConstant.DELETE_FlOOR_LINE)) {
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						UIHelper.ToastMessage(context, "刪除成功");
						httpStart.getServerData(3, MyConstant.GET_MANAGE_FLOOR,
								userBean.getMFG(), section,userBean.getSBU());
					}
					if (result.get(0).equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "刪除失敗");
					}
				}
				
				if (key.equalsIgnoreCase(MyConstant.GET_FLAG_UNUNITED)) {
					UIHelper.ToastMessage(context, "未連接服務器");
				}
			}
		}
	};

	public void Showdatalist() {
		list = new ArrayList<Map<String, Object>>();// 集合
		if (isdatalist) {
			if (datalist[0].toString().equalsIgnoreCase("null")
					|| datalist[0].toString().equals(null)) {
				UIHelper.ToastMessage(context, "暂无数据");
				bt_create_qr.setVisibility(View.GONE);
				return;
			} else {
				// 遍历
				for (int i = 0; i < datalist.length - 1; i += 3) {

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("floor_name", datalist[i].toString());
					map.put("line_name", datalist[i + 1].toString());
					map.put("sbu_name", datalist[i + 2].toString());
					list.add(map);
				}
			}
		}
		// 适配器
		SimpleAdapter seachadapter = new SimpleAdapter(context, list,
				R.layout.floor_manage_item, new String[] { "sbu_name",
						"floor_name", "line_name" }, new int[] { R.id.tv_sbu,
						R.id.tv_floor, R.id.tv_line });
		listview_floor_info.setAdapter(seachadapter);

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

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		TextView title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(section);
		bt_create_qr = (Button) findViewById(R.id.bt_create_qr);
		listview_floor_info = (ListView) findViewById(R.id.listview_floor_info);
		imageview_add = (ImageView) findViewById(R.id.imageview_add);
		if (userBean.getUserLevel().equals("2")) {
			imageview_add.setVisibility(View.VISIBLE);
			bt_create_qr.setVisibility(View.VISIBLE);
		} else {
			imageview_add.setVisibility(View.GONE);
			bt_create_qr.setVisibility(View.GONE);
		}
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}
	
	// 增加按钮
	public void add(View view) {
		intent = new Intent(context, Add_floor.class);
		intent.putExtra("section", section);
		intent.putExtra("operating", "0");		//操作標示
		startActivity(intent);
	}

	// 返回键按钮
	public void activity_back(View v) {
		// intent.putExtra("bUname", bUname);
		// intent.putExtra("issection", issection);
		// startActivity(intent);
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// intent.putExtra("bUname", bUname);
			// intent.putExtra("issection", issection);
			// startActivity(intent);
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
		httpStart.getServerData(3, MyConstant.GET_MANAGE_FLOOR,
				userBean.getMFG(), section,userBean.getSBU());
	}
}
