package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

public class AduitActivity extends BaseActivity {
	Context context = AduitActivity.this;
	List<Map<String, Object>> list;// 集合
	private String number, session, station;
	private ListView listview;
	private String[] datalist;
	private Boolean isdatalist = false;
	private ImageButton search;
	private String str = "0";
	private PopupWindow window;
	HttpStart start;
	UserBean userBean;
	HashMap<String, Object> params = new HashMap<String, Object>();// 是一种把键对象和值对象进行关联的容器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aduitactivity);
		search = (ImageButton) findViewById(R.id.search);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		start = new HttpStart(context, handler);
		findViewById();
		initView();

		search = (ImageButton) findViewById(R.id.search);

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopwindow();
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				number = list.get(position).get("number").toString();
				session = list.get(position).get("session").toString();

				Intent intent = new Intent(context, Aduit_Noinspectinfo.class);
				intent.putExtra("number", number);
				intent.putExtra("session", session);
				startActivity(intent);
			}
		});
	}

	public void Showdatalist() {
		list = new ArrayList<Map<String, Object>>();// 集合
		if (isdatalist) {
			if (datalist[0].toString().equalsIgnoreCase("null")
					|| datalist[0].toString().equals(null)) {
				UIHelper.ToastMessage(context, "暂无数据");
				return;
			} else {
				// 遍历
				for (int i = 0; i < datalist.length - 1; i += 3) {

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("number", datalist[i].toString());
					map.put("session", datalist[i + 1].toString());
					map.put("creat_date", datalist[i + 2].toString());
					list.add(map);
				}
			}
		}
		// 适配器
		SimpleAdapter seachadapter = new SimpleAdapter(context, list,
				R.layout.aduit_item, new String[] { "number", "session",
						"creat_date" }, new int[] { R.id.listreportno,
						R.id.listcheckid, R.id.listdata });
		listview.setAdapter(seachadapter);

	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		if (userBean.getLogonName() == null
				|| userBean.getLogonName().length() == 0) {
			android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
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
		listview = (ListView) findViewById(R.id.list1);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		start.getServerData(3, MyConstant.GET_NO_AUDIT, userBean.getLogonName());
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) // 键值对
			{
				if (key.equalsIgnoreCase(MyConstant.GET_NO_AUDIT)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						if (str.equals("2")) {
							int count = 0;
							for (int i = 1; i < result.size(); i += 3) {
								if (result.get(i).toString().contains(station)) {
									count++;
								}
							}
							System.out.println(count + ">>" + count * 3);
							datalist = new String[count * 3];
							for (int i = 1, k = 0; i < result.size(); i += 3) {
								if (result.get(i).toString().contains(station)) {
									datalist[k] = result.get(i);
									datalist[k + 1] = result.get(i + 1);
									datalist[k + 2] = result.get(i + 2);
									k += 3;
								}
							}
						} else {
							datalist = new String[result.size() - 1];
							for (int i = 1; i < result.size(); i++) {
								datalist[i - 1] = result.get(i);
							}
						}
					}
					if (result.get(0).equals(MyConstant.GET_FLAG_NULL)) {
						datalist = new String[result.size()];
						datalist[0] = "null";
					}

					isdatalist = true;
					Showdatalist();

				}
				if (key.equalsIgnoreCase(MyConstant.GET_DATE1)) {
					result = msg.getData().getStringArrayList(key);
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

				if (key.equalsIgnoreCase(MyConstant.GET_CHECK_REJECT)) {
					result = msg.getData().getStringArrayList(key);
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
			}
		}
	};

	// 返回键按钮
	public void back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}

	// 弹窗
	public void showPopwindow() {
		str = "0";
		// 显示页面的布局
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.aduit_dialog, null);

		window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		window.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		window.setAnimationStyle(R.style.AppBaseTheme);

		window.showAtLocation(search, Gravity.BOTTOM, 0, 0);
		// 获取layout布局里面的控件
		TextView textView01 = (TextView) view.findViewById(R.id.textview01);
		TextView textView02 = (TextView) view.findViewById(R.id.textview02);
		TextView textView03 = (TextView) view.findViewById(R.id.textview03);

		final Calendar c = Calendar.getInstance();// 时间日期类
		textView01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialog dialog = new DatePickerDialog(context,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								c.set(year, monthOfYear, dayOfMonth);
								String date = (String) DateFormat.format(
										"yyy-MM-dd", c);
								UIHelper.ToastMessage(context, date);
								start.getServerData(3, MyConstant.GET_DATE1,
										userBean.getLogonName(), date);
								window.dismiss();
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH));
				dialog.show();
			}
		});
		textView02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				start.getServerData(3, MyConstant.GET_CHECK_REJECT,
						userBean.getLogonName());
				window.dismiss();
			}
		});

		textView03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				start.getServerData(3, MyConstant.GET_NO_AUDIT,
						userBean.getLogonName());
				window.dismiss();
			}
		});
		window.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				Log.i(TAG, "窗口消失....");
			}
		});

	}
}
