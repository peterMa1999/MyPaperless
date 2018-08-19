package com.zsm.foxconn.mypaperless;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.help.UIHelper;

/**
 * 
 * Copyright (c) 2016, CNSBG SFC IT
 * 
 * @Description: 統計圖列表
 * 
 * @author TSY
 * 
 * @date 2016-10-5 下午4:01:22
 */
public class ChartListActivity extends BaseActivity {
	private ListView listView;
	private List<String> dataList;
	Intent intent = null;
	private Context context = ChartListActivity.this;
	private int[] data = { R.string.moistureproof_chart,
			R.string.environment_chart };
	private ImageView homebtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chart_list);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		TextView textView = (TextView) findViewById(R.id.bartitle_txt);
		textView.setText("統計圖列表");
		homebtn = (ImageView) findViewById(R.id.imageview_add);
		homebtn.setBackgroundResource(R.drawable.tongjitu_click_seletor);
		
		listView = (ListView) findViewById(R.id.lv_chart);
		listView.setAdapter(new MyAdapter());
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					intent = new Intent(context,
							MoistureproofChartActivity.class);
					intent.putExtra("reportName",
							getResources().getString(data[arg2]));
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(context,
							EnvironmentChartActivity.class);
					intent.putExtra("reportName",
							getResources().getString(data[arg2]));
					startActivity(intent);
					break;
				}
			}
		});
		homebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UIHelper.ToastMessage(context, "報表使用統計");
				intent = new Intent(context,
						AnotherBarActivity.class);
				startActivity(intent);
			}
		});
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.chart_list_item, parent, false);
			}
			TextView nameTV = (TextView) convertView
					.findViewById(R.id.tv_chart_list);
			nameTV.setText(data[position]);
			return convertView;
		}

	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub

	}

	// 返回键按钮
	public void activity_back(View v) {
			this.finish();
			overridePendingTransition(R.anim.move_left_in_activity,
					R.anim.move_right_out_activity);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
