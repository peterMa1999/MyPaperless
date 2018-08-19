package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MyCanshu_news extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {
	private ViewPager pager;
	private List<Fragment> fragments;
	private RadioGroup radioGroup;
	private ImageView imageview_add;
	private TextView bartitle_txt;

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
		bartitle_txt.setText("參數消息");
		findViewById(R.id.main_a).setOnClickListener(this);
		findViewById(R.id.main_b).setOnClickListener(this);
		findViewById(R.id.main_c).setOnClickListener(this);

		fragments = new ArrayList<Fragment>();
		fragments.add(new MyCanshu_Fragment_onging());
		fragments.add(new MyCanshu_Fragment_finish());
		fragments.add(new MyCanshu_Fragment_MyCheck());

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
