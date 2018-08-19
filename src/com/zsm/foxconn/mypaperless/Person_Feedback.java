package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.Person_HFeedback;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.ChangString;

public class Person_Feedback extends BaseActivity {
	UserBean userBean;
	TextView job_number, Full_name;
	EditText editText;
	HttpStart start = null;
	private Button button;
	String wenzi;
	ListView listView;
	private Main main;
	private List<Person_HFeedback> list = new ArrayList<Person_HFeedback>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_feedback);
		TextView title = (TextView) findViewById(R.id.bartitle_txt);
		title.setText(R.string.Person_Feedback);

		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		start = new HttpStart(Person_Feedback.this, handler);
		getFeedback();
		job_number = (TextView) findViewById(R.id.we1);
		Full_name = (TextView) findViewById(R.id.we2);
		editText = (EditText) findViewById(R.id.EditText01);

		job_number.setText(userBean.getLogonName());
		Full_name.setText(userBean.getChineseName());

		button = (Button) findViewById(R.id.Button01);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				wenzi = editText.getText().toString().trim();
				wenzi = ChangString.change(wenzi);
				if (wenzi.isEmpty()) {
					Toast.makeText(getApplicationContext(), "空的内容",
							Toast.LENGTH_SHORT).show();
				} else {
					getall();
				}
			}
		});
		listView = (ListView) findViewById(R.id.listView22);
		main = new Main();
		listView.setAdapter(main);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 内部点击
			}
		});
	}

	// 意见反馈
	private void getall() {
		start.getServerData(3, MyConstant.Feedback, userBean.getLogonName(),
				userBean.getChineseName(), userBean.getExt(),
				userBean.getEmail(), userBean.getSite(), userBean.getBG(),
				wenzi);
	}

	// 历史反馈
	private void getFeedback() {
		String string = userBean.getBG();
		start.getServerData(0, MyConstant.Historical_feedback, string);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.equals(MyConstant.Feedback)) {
					Toast.makeText(getApplicationContext(), "反馈成功,请等待处理",
							Toast.LENGTH_LONG).show();
					editText.setText("");
					getFeedback();
				}
				if (key.equals(MyConstant.Historical_feedback)) {
					result = msg.getData().getStringArrayList(key);
					if (list.size() > 0) {
						list.clear();
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i += 3) {
							Person_HFeedback all_staff = new Person_HFeedback(
									result.get(i + 0).toString(), result.get(
											i + 1).toString(), result
											.get(i + 2).toString());
							list.add(all_staff);
						}
					}
					// 刷新数据
					main.notifyDataSetChanged();
				}
			}
		}
	};

	class Main extends BaseAdapter {

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView != null ? convertView : View.inflate(
					getApplicationContext(), R.layout.person_hfeedback, null);
			final TextView textView1, textView2, textView3;
			textView1 = (TextView) view.findViewById(R.id.shijian);
			textView2 = (TextView) view.findViewById(R.id.neirong);
			textView3 = (TextView) view.findViewById(R.id.xingming);

			// 显示数据
			Person_HFeedback check = list.get(position);
			textView1.setText(check.getLasteditdt());
			textView2.setText(check.getFeedback());
			textView3.setText(check.getChineseName());
			return view;
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
		if (userBean.getLogonName() == null
				|| userBean.getLogonName().length() == 0) {
			android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					AppManager.getInstance().killAllActivity();
					startActivity(new Intent(Person_Feedback.this,
							LoginActivity.class));
				}
			};
			Builder builder = new Builder(Person_Feedback.this);
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
