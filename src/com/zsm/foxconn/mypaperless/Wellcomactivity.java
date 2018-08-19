package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.util.RemberCode;

/**
 * @author 
 * mgp 2016/03/05  引导页
 */
public class Wellcomactivity extends BaseActivity{
	private ViewPager viewPager;
	private ArrayList<View> list;
	private ViewGroup main;
	private Context context = Wellcomactivity .this;
	RemberCode code = new RemberCode();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater=getLayoutInflater();		
		list=new ArrayList<View>();
		list.add(inflater.inflate(R.layout.page_one, null));
		list.add(inflater.inflate(R.layout.page_two, null));
		list.add(inflater.inflate(R.layout.page_three, null));
		main=(ViewGroup) inflater.inflate(R.layout.viewpager_main, null);
		viewPager=(ViewPager)main.findViewById(R.id.viewPager);	
		this.setContentView(main);
		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(new MyListener());
		
		  
		boolean firstload = code.get_wellcom_page(context);
	     if (!firstload){                       
	                Intent intent = new Intent();
	                intent.setClass(this,LoginActivity.class);           
	                startActivityForResult(intent,0);                   
	            }
	}
	public void viewPager(View view){
		code.set_wellcom_page(context);
    	Intent intent = new Intent();
        intent.setClass(Wellcomactivity.this, LoginActivity.class); 
        startActivityForResult(intent, 0);
	}
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container,int position, Object object){
			((ViewPager)container).removeView(list.get(position));			
		}

		@Override
		public void finishUpdate(ViewGroup container){
			super.finishUpdate(container);
		}

		@Override
		public int getItemPosition(Object object)
		{
			return super.getItemPosition(object);
		}

		@Override
		public CharSequence getPageTitle(int position){
			return super.getPageTitle(position);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position){
			((ViewPager)container).addView(list.get(position));
			return list.get(position);
		}
	}
	
	class MyListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0){
			
		}

		@Override
		public void onPageScrolled(int arg0,float arg1, int arg2){
			
		}

		@Override
		public void onPageSelected(int arg0)
		{
			if (arg0 == 2) {
				
			}
		}
	}

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		
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
