package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.zsm.foxconn.mypaperless.adapter.CommonAdapter;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.Picture;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 獲取該用戶所在team的所有點檢報表
 * 
 * @author mgp 2015/12/26
 */
public class Examine_all_itemActivity extends BaseActivity {
	private Context context = Examine_all_itemActivity.this;
	HttpStart start = null;
	private TextView head_title;
	private GridView info;
	private String reporttitles[] = null;
	private String reportflag, reportid, reportName;
	private List<Picture> data = new ArrayList<Picture>();
	private CommonAdapter<Picture> adapter;
	private UserBean user;
	private String workStation,section;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_report);
		AppManager.getInstance().addActivity(Examine_all_itemActivity.this);
		user = (UserBean) getApplicationContext();
		CheckLogin();
		Intent intent = getIntent();
		section = intent.getStringExtra("section");
		head_title = (TextView) findViewById(R.id.head_title);
		head_title.setText("未及時點檢報表");
		start = new HttpStart(context, handler);
		initView();
		start.getServerData(3, MyConstant.GET_CHECK_ALL_REPORT_NAME, user
				.getMFG().toString(), user.getTeam().toString(), user.getSite()
				.toString(),user.getBU().toString(),section,user.getBU().toString());
		findViewById();
	}

	/**
	 * 检查账号是否登录
	 */
	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		// 检查账号是否登入
		if (user.getLogonName() == null || user.getLogonName().length() == 0) {
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
		info = (GridView) findViewById(R.id.report_info);
		adapter = new CommonAdapter<Picture>(context, data, R.layout.reportgive) {// 万能继承器适配器
			@Override
			public void convert(
					com.zsm.foxconn.mypaperless.adapter.ViewHolder holder,
					Picture t) {
				// TODO Auto-generated method stub
				if (t.getReportflag().equals("0")) {
					holder.setImageResource(R.id.reportimage,
							R.drawable.im_check_dark);
				} else {
					holder.setImageResource(R.id.reportimage,
							R.drawable.im_check_bright);
				}
				holder.setText(R.id.reporttitle, t.getReportName());
			}
		};
		info.setAdapter(adapter);

		info.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				reportid = data.get(arg2).getReportId();
				reportflag = data.get(arg2).getReportflag();
				reportName = data.get(arg2).getReportName();
				if (reportflag.equals("0")) {
					UIHelper.ToastMessage(context, "您暫無此權限");
					return;
				} else {
					start.getServerData(0, MyConstant.GET_CHECK_REPORT_SECTION, reportid);
				}
			}
		});

	}

	/**
	 * 初始化控件
	 */
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
				if (key.equals(MyConstant.GET_CHECK_ALL_REPORT_NAME)) {
					// 數據遍歷
					result = msg.getData().getStringArrayList(key);
					if (data.size() > 0) {
						data.clear();
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL_DETAIL)) {
						UIHelper.ToastMessage(context, "暫無需要點檢的報表", 5000);
						return;
					}

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 3) {
							Picture pc = new Picture(result.get(i).toString(),
									result.get(i + 1).toString(), result.get(
											i + 2).toString());
							data.add(pc);
						}
					}
					adapter.notifyDataSetChanged();
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(context, "未连接服务器....", 0).show();
					}
				}
				
				
				if (key.equals(MyConstant.GET_CHECK_REPORT_SECTION)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL)) {
						UIHelper.ToastMessage(context, "該報表無需維護", 5000);
						return;
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						workStation = result.get(1).toString();
						Intent intent = null;
						intent = new Intent(context,
								Examine_no_check_inquiryActivity.class);
						intent.putExtra("workStation", workStation);
						intent.putExtra("reportName", reportName);
						intent.putExtra("reportNum", reportid);
						startActivity(intent);
					}
				}
			}
		};
	};

	// 返回键按钮
	public void HeadBack(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}

}
