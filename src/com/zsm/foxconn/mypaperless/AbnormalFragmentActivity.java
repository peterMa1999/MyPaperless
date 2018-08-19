package com.zsm.foxconn.mypaperless;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class AbnormalFragmentActivity extends FragmentActivity
{
	private AbnormalRightFragment rightFragment=null;
	private FragmentTransaction  transaction=null;
	private AbnormalLeftFragment leftFragment=null;
	private TextView mreplay,mdeal;
	private FragmentManager fragmentManager=null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.abnormal_fragment_activity);
		fragmentManager=getSupportFragmentManager();
		findViewById();
		initView();
		selectFramgment(0);
	}
	protected void findViewById() 
	{
		mreplay=(TextView) findViewById(R.id.replay_tv);
		mdeal=(TextView) findViewById(R.id.deal_tv);
	}
	protected void initView() 
	{
		mreplay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectFramgment(0);
			}
		});
		mdeal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectFramgment(1);
			}
		});
	}
	@SuppressLint("NewApi")
	public void selectFramgment(int index) 
	{
		clearSelection();
		FragmentTransaction transaction= fragmentManager.beginTransaction(); 
		hideFragments(transaction);
	        switch (index) 
	        {  
		        case 0:  
		        	mreplay.setBackground(getResources().getDrawable(R.drawable.shape_login_change2));  
					mreplay.setTextSize(16);
					mreplay.setTextColor(Color.parseColor("#1495D1"));
		            if (rightFragment == null) {  
		            	rightFragment = new AbnormalRightFragment();  
		                transaction.add(R.id.abnoemal_Fram, rightFragment);  
		            } else {  
		                transaction.show(rightFragment);  
		            }  
		            break;  
		        case 1:  
		        	mdeal.setBackground(getResources().getDrawable(R.drawable.shape_login_change2));  
					mdeal.setTextSize(16);
					mdeal.setTextColor(Color.parseColor("#1495D1"));
		        	if (leftFragment == null) {  
		        		leftFragment = new AbnormalLeftFragment();  
		                transaction.add(R.id.abnoemal_Fram, leftFragment);  
		            } else {  
		                transaction.show(leftFragment);  
		            } 
		            break;  
		        default:  
		            break;  
	        }
	    transaction.commit();  
	}
	private void hideFragments(FragmentTransaction transaction)
    {  
        if (rightFragment != null) 
        {  
            transaction.hide(rightFragment);  
        }if (leftFragment != null) 
        {  
            transaction.hide(leftFragment);  
        }  
    }
	@SuppressLint("NewApi")
	private void clearSelection() 
	{  
		mreplay.setBackground(getResources().getDrawable(R.drawable.shape_login));  
		mreplay.setTextSize(12);
		mreplay.setTextColor(Color.parseColor("#CBCBCB"));
		
		mdeal.setBackground(getResources().getDrawable(R.drawable.shape_login));  
		mdeal.setTextSize(12);
		mdeal.setTextColor(Color.parseColor("#CBCBCB"));
    }
	public void back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}
}
