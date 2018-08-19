package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zsm.foxconn.mypaperless.base.BaseActivity;

public class Choose_section extends BaseActivity implements OnClickListener{
	private Context context = Choose_section.this;
	private TextView head_title;
	private ImageButton assy_imagebt,other_imagebt,pack_imagebt,pth_imagebt,smt_imagebt,whs_imagebt
	,kitting_imagebt,re_imagebt,te_imagebt,se_imagebt;
	String isaccess,bUname,issection;
	List<String> data = new ArrayList<String>();
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_section);
		findViewById();
		intent = getIntent();
		issection = intent.getStringExtra("issection");
		bUname = intent.getStringExtra("bUname");
		isaccess = intent.getStringExtra("isaccess");
		head_title = (TextView) findViewById(R.id.head_title);
		head_title.setText("選擇報表類型");
		
	}
	
	public void HeadBack(View view) {
//		Intent intent = new Intent(this,MainActivity.class);
//		tiemTimelistenresiger.stopthread();
//		startActivity(intent);
		overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
		this.finish();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
//	    	Intent intent = new Intent(this,MainActivity.class);
//	    	tiemTimelistenresiger.stopthread();
//			startActivity(intent);
			overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
			this.finish();
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	    }
	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		ImageButton head_next = (ImageButton) findViewById(R.id.head_next);
		head_next.setVisibility(View.GONE);
		smt_imagebt = (ImageButton) findViewById(R.id.smt_imagebt);
		assy_imagebt = (ImageButton) findViewById(R.id.assy_imagebt);
		other_imagebt = (ImageButton) findViewById(R.id.other_imagebt);
		pack_imagebt = (ImageButton) findViewById(R.id.pack_imagebt);
		pth_imagebt = (ImageButton) findViewById(R.id.pth_imagebt);
		whs_imagebt = (ImageButton) findViewById(R.id.whs_imagebt);
		kitting_imagebt = (ImageButton) findViewById(R.id.kitting_imagebt);
		re_imagebt =  (ImageButton) findViewById(R.id.re_imagebt);
		te_imagebt =  (ImageButton) findViewById(R.id.te_imagebt);
		se_imagebt = (ImageButton) findViewById(R.id.se_imagebt);
		smt_imagebt.setOnClickListener(this);
		assy_imagebt.setOnClickListener(this);
		other_imagebt.setOnClickListener(this);
		pack_imagebt.setOnClickListener(this);
		pth_imagebt.setOnClickListener(this);
		whs_imagebt.setOnClickListener(this);
		kitting_imagebt.setOnClickListener(this);
		re_imagebt.setOnClickListener(this);
		te_imagebt.setOnClickListener(this);
		se_imagebt.setOnClickListener(this);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.smt_imagebt:
			if (issection.equals("0")) {
				intent = new Intent(context,Examine_all_itemActivity.class);
			}else if (issection.equals("1")) {
				intent = new Intent(context,Examine_all_itemActivity_tiqian.class);
			}else if(issection.equals("2")||issection.equals("4")){
				intent = new Intent(context,
						Choose_report.class);
				intent.putExtra("bUname", bUname);
				intent.putExtra("isaccess", isaccess);
			}else if (issection.equals("3")) {
				intent = new Intent(context,
						Floor_manage.class);
				intent.putExtra("isaccess", isaccess);
			}
			intent.putExtra("issection", issection);
			intent.putExtra("section", "SMT");
			startActivity(intent);
			((Activity) context).overridePendingTransition(
					R.anim.move_right_in_activity,
					R.anim.move_left_out_activity);
			break;
		case R.id.assy_imagebt:
			if (issection.equals("0")) {
				intent = new Intent(context,Examine_all_itemActivity.class);
			}else if (issection.equals("1")) {
				intent = new Intent(context,Examine_all_itemActivity_tiqian.class);
			}else if(issection.equals("2")||issection.equals("4")){
				intent = new Intent(context,
						Choose_report.class);
				intent.putExtra("bUname", bUname);
				intent.putExtra("isaccess", isaccess);
			}else if (issection.equals("3")) {
				intent = new Intent(context,
						Floor_manage.class);
				intent.putExtra("isaccess", isaccess);
			}
			intent.putExtra("issection", issection);
			intent.putExtra("section", "ASSY");
			startActivity(intent);
			this.overridePendingTransition(
					R.anim.move_right_in_activity,
					R.anim.move_left_out_activity);
			break;
		case R.id.other_imagebt:
			if (issection.equals("0")) {
				intent = new Intent(context,Examine_all_itemActivity.class);
			}else if (issection.equals("1")) {
				intent = new Intent(context,Examine_all_itemActivity_tiqian.class);
			}else if(issection.equals("2")||issection.equals("3")||issection.equals("4")){
				intent = new Intent(context,
						Choose_report.class);
				intent.putExtra("bUname", bUname);
				intent.putExtra("isaccess", isaccess);
			}
			intent.putExtra("issection", issection);
			intent.putExtra("section", "OTHER");
			startActivity(intent);
			this.overridePendingTransition(
					R.anim.move_right_in_activity,
					R.anim.move_left_out_activity);
			break;
		case R.id.pack_imagebt:
			if (issection.equals("0")) {
				intent = new Intent(context,Examine_all_itemActivity.class);
			}else if (issection.equals("1")) {
				intent = new Intent(context,Examine_all_itemActivity_tiqian.class);
			}else if(issection.equals("2")||issection.equals("4")){
				intent = new Intent(context,
						Choose_report.class);
				intent.putExtra("bUname", bUname);
				intent.putExtra("isaccess", isaccess);
			}else if (issection.equals("3")) {
				intent = new Intent(context,
						Floor_manage.class);
				intent.putExtra("isaccess", isaccess);
			}
			intent.putExtra("issection", issection);
			intent.putExtra("section", "PACK");
			startActivity(intent);
			this.overridePendingTransition(
					R.anim.move_right_in_activity,
					R.anim.move_left_out_activity);
			break;
		case R.id.pth_imagebt:
			if (issection.equals("0")) {
				intent = new Intent(context,Examine_all_itemActivity.class);
			}else if (issection.equals("1")) {
				intent = new Intent(context,Examine_all_itemActivity_tiqian.class);
			}else if(issection.equals("2")||issection.equals("4")){
				intent = new Intent(context,
						Choose_report.class);
				intent.putExtra("bUname", bUname);
				intent.putExtra("isaccess", isaccess);
			}
			else if (issection.equals("3")) {
				intent = new Intent(context,
						Floor_manage.class);
				intent.putExtra("isaccess", isaccess);
			}
			intent.putExtra("issection", issection);
			intent.putExtra("section", "PTH");
			startActivity(intent);
			this.overridePendingTransition(
					R.anim.move_right_in_activity,
					R.anim.move_left_out_activity);
			break;
		case R.id.whs_imagebt:
			if (issection.equals("0")) {
				intent = new Intent(context,Examine_all_itemActivity.class);
			}else if (issection.equals("1")) {
				intent = new Intent(context,Examine_all_itemActivity_tiqian.class);
			}else if(issection.equals("2")||issection.equals("4")){
				intent = new Intent(context,
						Choose_report.class);
				intent.putExtra("bUname", bUname);
				intent.putExtra("isaccess", isaccess);
			}
			else if (issection.equals("3")) {
				intent = new Intent(context,
						Floor_manage.class);
				intent.putExtra("isaccess", isaccess);
			}
			intent.putExtra("issection", issection);
			intent.putExtra("section", "WHS");
			startActivity(intent);
			this.overridePendingTransition(
					R.anim.move_right_in_activity,
					R.anim.move_left_out_activity);
			break;
		case R.id.kitting_imagebt:
			if (issection.equals("0")) {
				intent = new Intent(context,Examine_all_itemActivity.class);
			}else if (issection.equals("1")) {
				intent = new Intent(context,Examine_all_itemActivity_tiqian.class);
			}else if(issection.equals("2")||issection.equals("4")){
				intent = new Intent(context,
						Choose_report.class);
				intent.putExtra("bUname", bUname);
				intent.putExtra("isaccess", isaccess);
			}
			else if (issection.equals("3")) {
				intent = new Intent(context,
						Floor_manage.class);
				intent.putExtra("isaccess", isaccess);
			}
			intent.putExtra("issection", issection);
			intent.putExtra("section", "KITTING");
			startActivity(intent);
			this.overridePendingTransition(
					R.anim.move_right_in_activity,
					R.anim.move_left_out_activity);
			break;
			
		case R.id.re_imagebt:
			if (issection.equals("0")) {
				intent = new Intent(context,Examine_all_itemActivity.class);
			}else if (issection.equals("1")) {
				intent = new Intent(context,Examine_all_itemActivity_tiqian.class);
			}else if(issection.equals("2")||issection.equals("4")){
				intent = new Intent(context,
						Choose_report.class);
				intent.putExtra("bUname", bUname);
				intent.putExtra("isaccess", isaccess);
			}
			else if (issection.equals("3")) {
				intent = new Intent(context,
						Floor_manage.class);
				intent.putExtra("isaccess", isaccess);
			}
			intent.putExtra("issection", issection);
			intent.putExtra("section", "RE");
			startActivity(intent);
			this.overridePendingTransition(
					R.anim.move_right_in_activity,
					R.anim.move_left_out_activity);
			break;
		case R.id.te_imagebt:
			if (issection.equals("0")) {
				intent = new Intent(context,Examine_all_itemActivity.class);
			}else if (issection.equals("1")) {
				intent = new Intent(context,Examine_all_itemActivity_tiqian.class);
			}else if(issection.equals("2")||issection.equals("4")){
				intent = new Intent(context,
						Choose_report.class);
				intent.putExtra("bUname", bUname);
				intent.putExtra("isaccess", isaccess);
			}
			else if (issection.equals("3")) {
				intent = new Intent(context,
						Floor_manage.class);
				intent.putExtra("isaccess", isaccess);
			}
			intent.putExtra("issection", issection);
			intent.putExtra("section", "TE");
			startActivity(intent);
			this.overridePendingTransition(
					R.anim.move_right_in_activity,
					R.anim.move_left_out_activity);
			break;
		case R.id.se_imagebt:
			if (issection.equals("0")) {
				intent = new Intent(context,Examine_all_itemActivity.class);
			}else if (issection.equals("1")) {
				intent = new Intent(context,Examine_all_itemActivity_tiqian.class);
			}else if(issection.equals("2")||issection.equals("4")){
				intent = new Intent(context,
						Choose_report.class);
				intent.putExtra("bUname", bUname);
				intent.putExtra("isaccess", isaccess);
			}
			else if (issection.equals("3")) {
				intent = new Intent(context,
						Floor_manage.class);
				intent.putExtra("isaccess", isaccess);
			}
			intent.putExtra("issection", issection);
			intent.putExtra("section", "SE");
			startActivity(intent);
			this.overridePendingTransition(
					R.anim.move_right_in_activity,
					R.anim.move_left_out_activity);
			break;
		default:
			break;
		}
	}

}
