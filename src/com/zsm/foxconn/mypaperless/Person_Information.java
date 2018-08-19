package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.tsz.afinal.FinalDb;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.Person_All_staff;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.database.APP_yindao_page;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.GuideUtil;
import com.zsm.foxconn.mypaperless.util.Loading_dialogo;
import com.zsm.foxconn.mypaperless.util.RemberCode;

public class Person_Information extends BaseActivity{
	UserBean userBean;
	EditText editText;
	private String Team, MFG;
	HttpStart start = null;
	Context context = Person_Information.this;
	Loading_dialogo dialogo = null;
	Intent intent;
	private MyAdapter adapter;
	private ListView listView;
	private List<Person_All_staff> listCard = new ArrayList<Person_All_staff>();
	private FinalDb finalDb = null;
	private GuideUtil guideUtil = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_information);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		Team = userBean.getTeam();
		MFG = userBean.getMFG();
		start = new HttpStart(context, handler);
		getDataAll();
		TextView title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(R.string.Person_Staff_information_list);
		editText = (EditText) findViewById(R.id.query);
		listView = (ListView) findViewById(R.id.listView111);
		
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 判断登录账号等级
				String string = (userBean.getUserLevel());
				if (string.equals("1")||string.equals("2")) {
					Toast.makeText(getApplicationContext(), "修改信息",
							Toast.LENGTH_SHORT).show();
					intent = new Intent(Person_Information.this,
							Person_Modify_delete.class);
					intent.putExtra("name", listCard.get(arg2).getJob_number());
					startActivity(intent);
				} else {
					intent = new Intent(Person_Information.this,
							Person_View_information.class);
					intent.putExtra("name", listCard.get(arg2).getJob_number());
					startActivity(intent);
				}
			}
		});
		
		finalDb = FinalDb.create(context, "child"); //创建数据库
		List<APP_yindao_page> list_app = finalDb.findAllByWhere(APP_yindao_page.class, "pagename='Person_Information' and pagename_id='0'");
		if (list_app.size()==0){
			guideUtil = GuideUtil.getInstance();
			guideUtil.initGuide(this, R.drawable.add_guide,"Person_Information", 0);
		}
	}

	// 使用工号事业处获取信息
	private void getDataAll() {
		start.getServerData(3, MyConstant.GET_ALL_EMPLOYEE_INFORMATION, Team,
				MFG);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = null;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.equals(MyConstant.GET_ALL_EMPLOYEE_INFORMATION)
						|| key.equals(MyConstant.GET_SELECT_EMPLOYEE_INFORMATION)) {
					result = msg.getData().getStringArrayList(key);
					if (listCard.size() > 0) {
						listCard.clear();
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1,K=0; i < result.size(); i += 4,K++) {
							Person_All_staff all_staff = new Person_All_staff(
									result.get(i + 0).toString(), result.get(
											i + 1).toString(), result
											.get(i + 2).toString(), result.get(
											i + 3).toString());
							listCard.add(all_staff);
						}
					} else {
						UIHelper.ToastMessage(context, "查无此人");
					}
					// 刷新数据
					adapter.notifyDataSetChanged();
				}
			}
		};
	};

	// listview显示
	class MyAdapter extends BaseAdapter {

		public int getCount() {
			Log.i("adapt>>>>", ">>>>>" + listCard.size());
			return listCard.size();
		}

		public Object getItem(int position) {
			return listCard.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View item = convertView != null ? convertView : View.inflate(
					getApplicationContext(), R.layout.person_listview, null);
			final TextView job_number, chinese_name, duties, executive_director;
			job_number = (TextView) item.findViewById(R.id.gonghao);
			chinese_name = (TextView) item.findViewById(R.id.xingming);
			duties = (TextView) item.findViewById(R.id.zhiwu);
			executive_director = (TextView) item.findViewById(R.id.Master);

			// 显示数据
			Person_All_staff check = listCard.get(position);
			job_number.setText(check.getJob_number());
			chinese_name.setText(check.getChinese_name());
			duties.setText(check.getDuties());
			executive_director.setText(check.getExecutive_director());
			return item;
		}
	}

	// 搜索按钮
	public void search(View view) {
		
		String string = editText.getText().toString().trim();
		if (string.length() > 0 || !string.equals("")) {
			start.getServerData(3, MyConstant.GET_SELECT_EMPLOYEE_INFORMATION,
					Team, MFG, string);

		} else {
			Toast.makeText(getApplicationContext(), "请输入查询条件",
					Toast.LENGTH_SHORT).show();
		}
	}

	// 增加按钮
	public void add(View view) {
		String string = (userBean.getUserLevel());
		if (string.equals("1")||string.equals("2")) {
			Intent intent = new Intent(getApplicationContext(),
					Person_Add_personnel.class);
			Toast.makeText(getApplicationContext(), "添加员工", Toast.LENGTH_SHORT)
					.show();
			startActivity(intent);
			Person_Information.this.finish();
		} else {
			Toast.makeText(getApplicationContext(), "没有权限", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// 返回键按钮
	public void activity_back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		if (userBean.getLogonName() == null || userBean.getLogonName().length() == 0) {
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
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}
}
