package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.adapter.CheckeAdapter3;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.CheckHolder;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.broadcast.Timelistenresiger;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.http.HttpThread;
public class StartCheckedActivity extends BaseActivity
{
	Context context=StartCheckedActivity.this;
	private UserBean userBean;
	private ImageButton deteleImageButton;
	HttpThread http = null;
	ArrayList<String> liststr=new ArrayList<String>();
	HttpStart start;
	CheckHolder holder = null;
	HashMap<Integer,Boolean> isSelected=null;
	private CheckeAdapter3 mCheckeAdapter=null;
	ListView listView;
	private String reportId=null;
	private String reportName=null;
	private String section=null,issection = null; 
	private String flagfloor=null;
	List<String> list=new ArrayList<String>();
	List<String> data=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_startchecked);
		userBean = (UserBean) getApplicationContext();
		CheckLogin();
		start=new HttpStart(context, handler);
		Intent intent2=getIntent();
		try {
			reportId=intent2.getStringExtra("reportId").toString();
			reportName=intent2.getStringExtra("reportName").toString();
			section=intent2.getStringExtra("section").toString();
			flagfloor=intent2.getStringExtra("flagfloor").toString();
			issection = intent2.getStringExtra("issection");
		} catch (Exception e) {
			// TODO: handle exception
			UIHelper.ToastMessage(context, "無數據");
			return;
		}
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
				 * 得到所选PD线别;
				 */
				if (key.equals(MyConstant.CHECKED_LINE_PD)) //
				{
					result=msg.getData().getStringArrayList(key);
					if (list.size()>0) 
					{
						list.clear();
					}
					if(result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) 
					{
						for (int i =1; i < result.size(); i++) 
						{
							list.add(result.get(i));
						}
					}else if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) 
					{
						Toast.makeText(context, "當前無任何所勾選線別...",0).show();
					}
					mCheckeAdapter.notifyDataSetChanged();
					mCheckeAdapter.initDate();
				}
				/*
				 * 删除线别;
				 */
				if (key.equals(MyConstant.DELETE_LINE_PD)) //
				{
					result=msg.getData().getStringArrayList(key);
					if(result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) 
					{
						Toast.makeText(context, "刪除成功...",0).show();
						if (liststr.size()>0) 
						{
							liststr.clear();
						}
						start.getServerData(0,MyConstant.CHECKED_LINE_PD,userBean.getLogonName().toString().trim(),reportId);
					}else if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) 
					{
						Toast.makeText(context, "刪除失敗...",0).show();
					}
				}
				}
			}
		};
	@Override
	protected void CheckLogin() {
		userBean = (UserBean) getApplicationContext();
		if (userBean.getLogonName() == null) {
			UIHelper.ToastMessage(context,
					"登陆异常,请从新登陆...");
			openActivity(LoginActivity.class);
			AppManager.getInstance().AppExit(context);
		}
	}
	@Override
	protected void findViewById() 
	{
		listView=(ListView) findViewById(R.id.CheckedLines);
		deteleImageButton=(ImageButton) findViewById(R.id.head_next);
		deteleImageButton.setImageResource(R.drawable.deletelines);
	}
	@Override
	protected void initView() 
	{
		
		mCheckeAdapter=new CheckeAdapter3(list, context, issection);
		data.add(reportName);
		data.add(reportId);
		data.add(section);
		data.add(flagfloor);
		mCheckeAdapter.CheckeAdapterintent(data);
		listView.setAdapter(mCheckeAdapter);
		start.getServerData(0,MyConstant.CHECKED_LINE_PD,userBean.getLogonName().toString().trim(),reportId);
		listView.setOnItemClickListener(new OnItemClickListener() 
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
				}else 
				{
					mCheckeAdapter.getIsSelected().remove(arg2);
					liststr.remove(list.get(arg2).toString());
				}
				
			}
		});
	}
	public void onActivityResult(int requestCode,int resultCode,Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==1)
		{
			start.getServerData(0,
					MyConstant.CHECKED_LINE_PD,
					userBean.getLogonName().toString().trim(),
					data.getExtras().getString("report_Num"));
		}
	}
	public void HeadNext(View view)
	{
				if (liststr.size()>0) 
				{
					new AlertDialog.Builder(context)
					.setIcon(R.drawable.nt_warn)
					.setTitle("確定刪除？")
					.setPositiveButton(
							getResources().getString(R.string.back_yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) 
								{
									if (liststr.size()>0) 
									{
										String allLines="";
										for (int i = 0; i < liststr.size(); i++) 
										{
											allLines+=liststr.get(i).toString().trim()+ MyConstant.GET_FLAG2;
										}
										 start.getServerData(0,MyConstant.DELETE_LINE_PD,
										    		userBean.getLogonName().toString().trim(),allLines.toString().trim(),reportId);
									}
								}
							})
					.setNeutralButton(
							getResources().getString(R.string.back_no),
							new DialogInterface.OnClickListener() 
							{
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
				}else {
					Toast.makeText(context,"請選擇您要刪除的線別...",0).show();
				}
	}
	public void HeadBack(View view)
	{
		AppManager.getInstance().killActivity(this);
		 overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
	}
}
	

		
