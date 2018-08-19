package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.widget.AbsListView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore.Action;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.adapter.CheckeAdapter;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.CheckHolder;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.GetSystemTime;
/**
 * 
 * @author F1330308
 *
 */
public class Choose_line_PD extends BaseActivity
{
	private UserBean userBean;
	private TextView sectionTextView,head_title;
	private Spinner spinnerfloor;
	private ImageButton submit_bt;
	int count=0;
	CheckHolder holder=new CheckHolder();
	private CheckeAdapter mCheckeAdapter=null; 
	HttpStart start=null;
	String[] mdata=null;
	ArrayList<String> list=new ArrayList<String>();
	ArrayList<String> liststr=new ArrayList<String>();
	ArrayAdapter<String> arrayAdapter=null;
	private Context context=Choose_line_PD.this;
	private String flagfloor;
	private ListView lines_lV;
	private boolean isFirst=false;
	private String reportId=null;
	private String reportName=null;
	String section = "",issection="";
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_line);
		userBean = (UserBean) getApplicationContext();
		start=new HttpStart(context, handler);
		Intent intent2=getIntent();
		reportId=intent2.getStringExtra("ReportId").toString();
		reportName=intent2.getStringExtra("reportName").toString();
		section = intent2.getStringExtra("section").toString();
		issection = intent2.getStringExtra("issection");
		CheckLogin();
		findViewById();
		initView();
	}
	private Handler handler=new Handler()
	{
		public void handleMessage(Message msg){
			ArrayList<String> result=null;
			Bundle bundle=msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) //键值对
			{
				/*
				 * 获取楼层;
				 */
				if (key.equals(MyConstant.GET_FLOOR_PD)) //
				{
					result=msg.getData().getStringArrayList(key);
					if(result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) 
					{
						mdata=new String[result.size()-1];
						for (int i =1; i < result.size(); i++) 
						{
							mdata[i-1]=result.get(i);
						}
						for (int j = 0; j < mdata.length; j++) {
							System.out.println(">>>>>>"+mdata[j]);
						}
						spinnerfloor.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,mdata));
					}else if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) 
					{
						Toast.makeText(context, "无楼层信息",0).show();
					}
				}
				/*
				 * 获取PD线别;
				 */
				if (key.equals(MyConstant.GET_CHECK_LINE_PD)) //
				{
					result=msg.getData().getStringArrayList(key);
					if(result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) 
					{
						if (list.size()>0) 
						{
							list.clear();
						}
						for (int i =1; i < result.size(); i++) 
						{
							list.add(result.get(i));
						}
					mCheckeAdapter.notifyDataSetChanged();
					mCheckeAdapter.initDate();
					}else if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) 
					{
						Toast.makeText(context,"此楼层无线别可选...",0).show();
					}
				}
				/*
				 * 添加PD线别;
				 */
				if (key.equals(MyConstant.ADD_SELECT_LINE_PD)) 
				{
					result=msg.getData().getStringArrayList(key);
					if(result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) 
					{
						liststr.clear();
						Toast.makeText(context,"添加成功",0).show();
						start.getServerData(0, MyConstant.GET_CHECK_LINE_PD,userBean.getLogonName(),section,flagfloor);
					}else if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) 
					{
						Toast.makeText(context,"添加失敗",0).show();
					}
					Intent intent = new Intent(context,StartCheckedActivity.class);
					intent.putExtra("reportName",reportName);
				    intent.putExtra("reportId",reportId);
				    intent.putExtra("section",section);
				    intent.putExtra("flagfloor",flagfloor);
				    intent.putExtra("issection", issection);
					startActivity(intent);
					overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
				}
				}
			}
		};
	@Override
	protected void CheckLogin() 
	{
		userBean = (UserBean) getApplicationContext();
		if (userBean.getLogonName() == null) {
			UIHelper.ToastMessage(context,
					"登陆异常,请重新登陆...");
			openActivity(LoginActivity.class);
			AppManager.getInstance().AppExit(context);
		}
	}
	@Override
	protected void findViewById()
	{
		sectionTextView=(TextView) findViewById(R.id.textsection);
		spinnerfloor=(Spinner) findViewById(R.id.spinnerfloor);
		lines_lV=(ListView) findViewById(R.id.listViewLines);
		submit_bt=(ImageButton) findViewById(R.id.head_next);
		submit_bt.setImageResource(R.drawable.submit_click_seletor2);
		head_title = (TextView) findViewById(R.id.head_title);
		head_title.setText("選擇線別");
	}
	@Override
	protected void initView() 
	{
		mCheckeAdapter=new CheckeAdapter(list, context);
		lines_lV.setAdapter(mCheckeAdapter);
		sectionTextView.setText(reportName);
		
		start.getServerData(0, MyConstant.GET_FLOOR_PD,userBean.getLogonName(),section);
		spinnerfloor.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				flagfloor=arg0.getItemAtPosition(arg2).toString();
				start.getServerData(0, MyConstant.GET_CHECK_LINE_PD,userBean.getLogonName(),section,flagfloor);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		lines_lV.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				holder = (CheckHolder) arg1.getTag(); 
				holder.flagCheckBox.toggle();
				if (holder.flagCheckBox.isChecked()) 
				{
					mCheckeAdapter.getIsSelected().put(arg2,holder.flagCheckBox.isChecked());
					liststr.add(list.get(arg2).toString());	
				}else {
					mCheckeAdapter.getIsSelected().remove(arg2);
					liststr.remove(list.get(arg2).toString());
				}
				
			}
		});
	}
	
	public void HeadNext(View view) 
	{
		try {
			if (liststr.size()>0) 
			{
				String allLines="";
				for (int i = 0; i < liststr.size(); i++) 
				{
					allLines+=liststr.get(i).toString().trim()+ MyConstant.GET_FLAG2;
				}
				start.getServerData(0, MyConstant.ADD_SELECT_LINE_PD,
						userBean.getMFG().toString().trim(),
						reportId,
						flagfloor,allLines,"0",userBean.getLogonName().toString()
						.trim());
			}else 
			{
				Intent intent = new Intent(context,StartCheckedActivity.class);
				intent.putExtra("reportName",reportName);
			    intent.putExtra("reportId",reportId);
			    intent.putExtra("section",section);
			    intent.putExtra("flagfloor",flagfloor);
			    intent.putExtra("issection", issection);
				startActivity(intent);
				 overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
			}
		} catch (Exception e) {
			// TODO: handle exception
			UIHelper.ToastMessage(context, "添加異常");
			return;
		}
		
	}
	public void HeadBack(View view) {
		AppManager.getInstance().killActivity(this);
		 overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
	}

}