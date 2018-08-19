package com.zsm.foxconn.mypaperless;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class IPQC_CrossBU_Issue extends FragmentActivity
{
	private IssueRightFragment rightFragment=null;
	private FragmentTransaction  transaction=null;
	private IssueLeftFragment leftFragment=null;
	private TextView crossbu_issue_tv,smart_guart_tv;
	private FragmentManager fragmentManager=null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ipqc_cross_issue);
		fragmentManager=getSupportFragmentManager();
		findViewById();
		initView();
		selectFramgment(0);
	}
	protected void findViewById() 
	{
		crossbu_issue_tv=(TextView) findViewById(R.id.crossbu_issue_tv);
		smart_guart_tv=(TextView) findViewById(R.id.smart_guart_tv);
	}
	protected void initView() 
	{
		crossbu_issue_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectFramgment(0);
			}
		});
		smart_guart_tv.setOnClickListener(new OnClickListener() {
			
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
		        	crossbu_issue_tv.setBackground(getResources().getDrawable(R.drawable.shape_login_change2));  
					crossbu_issue_tv.setTextSize(16);
					crossbu_issue_tv.setTextColor(Color.parseColor("#1495D1"));
		            if (rightFragment == null) {  
		            	rightFragment = new IssueRightFragment();  
		                transaction.add(R.id.abnoemal_Fram, rightFragment);  
		            } else {  
		                transaction.show(rightFragment);  
		            }  
		            break;  
		        case 1:  
		        	smart_guart_tv.setBackground(getResources().getDrawable(R.drawable.shape_login_change2));  
		        	smart_guart_tv.setTextSize(16);
		        	smart_guart_tv.setTextColor(Color.parseColor("#1495D1"));
		        	if (leftFragment == null) {  
		        		leftFragment = new IssueLeftFragment();  
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
		crossbu_issue_tv.setBackground(getResources().getDrawable(R.drawable.shape_login));  
		crossbu_issue_tv.setTextSize(12);
		crossbu_issue_tv.setTextColor(Color.parseColor("#CBCBCB"));
		
		smart_guart_tv.setBackground(getResources().getDrawable(R.drawable.shape_login));  
		smart_guart_tv.setTextSize(12);
		smart_guart_tv.setTextColor(Color.parseColor("#CBCBCB"));
    }
	public void back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// intent.putExtra("bUname", bUname);
			// intent.putExtra("issection", issection);
			// startActivity(intent);
			overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
