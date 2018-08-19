package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;

import com.zsm.foxconn.mypaperless.PassAuditModifyFragment.CallBackValue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ModifyCheckActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener ,CallBackValue{
	private ViewPager pager;
	private List<Fragment> fragments;
	private RadioGroup radioGroup;
	private RadioButton waitAuditRB,passAuditRB,rejectAuditRB;
	private ImageView imageview_add;
	private TextView bartitle_txt;

	private TextView waitNum,passNum,rejectNum;
	private String passStr;

	//要获取的值  就是这个参数的值  
	@Override
    public void SendMessageValue(String strValue) {  
        // TODO Auto-generated method stub  
		passStr=strValue;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mycanshu_news);
		radioGroup = (RadioGroup) findViewById(R.id.main_navi_radiogroup);
		pager = (ViewPager) findViewById(R.id.packpage_vPager);
		imageview_add = (ImageView) findViewById(R.id.imageview_add);
		imageview_add.setVisibility(View.GONE);
		bartitle_txt = (TextView) findViewById(R.id.bartitle_txt);
		bartitle_txt.setText("點檢修改");

		waitAuditRB=(RadioButton)findViewById(R.id.main_a);
		passAuditRB=(RadioButton)findViewById(R.id.main_b);
		rejectAuditRB=(RadioButton)findViewById(R.id.main_c);
		waitAuditRB.setOnClickListener(this);
		passAuditRB.setOnClickListener(this);
		rejectAuditRB.setOnClickListener(this);
		waitAuditRB.setText("待審核");
		passAuditRB.setText("通過");
		rejectAuditRB.setText("駁回");
		
		waitNum=(TextView)findViewById(R.id.tv_wait_num);
		passNum=(TextView)findViewById(R.id.tv_pass_num);
		rejectNum=(TextView)findViewById(R.id.tv_reject_num);
		
		Intent intent=getIntent();
		String waitStr=intent.getStringExtra("waitNum");
		String rejectStr=intent.getStringExtra("rejectNum");
		if(waitStr!=null&&!waitStr.equals("")&&Integer.parseInt(waitStr)!=0){
			waitNum.setVisibility(View.VISIBLE);
			waitNum.setText(waitStr);
		}
		if(rejectStr!=null&&!rejectStr.equals("")&&Integer.parseInt(rejectStr)!=0){
			rejectNum.setVisibility(View.VISIBLE);
			rejectNum.setText(rejectStr);
		}
		if(passStr!=null&&!passStr.equals("")&&Integer.parseInt(passStr)!=0){
			passNum.setVisibility(View.VISIBLE);
			passNum.setText(passStr);
		}

		fragments = new ArrayList<Fragment>();
		fragments.add(new WaitAuditModifyFragment());
		fragments.add(new PassAuditModifyFragment());
		fragments.add(new RejectAuditModifyFragment());

		pager.setAdapter(new FragmentPagerAdapter(this
				.getSupportFragmentManager()) {

			public int getCount() {
				return fragments.size();
			}

			public Fragment getItem(int arg0) {
				return fragments.get(arg0);
			}
		});

		// 添加页面切换事件的监听器
		pager.setOnPageChangeListener(this);
		radioGroup.check(R.id.main_a);
	}

	public void finish() {
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
		view.removeAllViews();
		super.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_a:
			pager.setCurrentItem(0, true);
			break;
		case R.id.main_b:
			pager.setCurrentItem(1, true);
			break;
		case R.id.main_c:
			pager.setCurrentItem(2, true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			radioGroup.check(R.id.main_a);
			break;
		case 1:
			radioGroup.check(R.id.main_b);
			break;
		case 2:
			radioGroup.check(R.id.main_c);
			break;
		default:
			break;
		}
	}

	// 返回键按钮
	public void activity_back(View v) {
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
		this.finish();
	}

}
