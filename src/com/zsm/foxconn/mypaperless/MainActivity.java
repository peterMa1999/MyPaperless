package com.zsm.foxconn.mypaperless;

import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.ConnSkyWebService;
import com.zsm.foxconn.mypaperless.service.TimeServer;

import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	Context context = MainActivity.this;
	private RadioGroup myTabRg;
	private long ExitTime;
	UserBean userBean=new UserBean();
	FragmentPerson person;
	FragmentMain main;
//	FragmentExamine examine;
	FragmentMain1 examine;
	ConnSkyWebService service=new ConnSkyWebService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		userBean = (UserBean) getApplicationContext();
		AppManager.getInstance().addActivity(MainActivity.this);
		init();
	}

	private void init(){
		main = new FragmentMain();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, main).commit();
		myTabRg = (RadioGroup) findViewById(R.id.tab_menu);
		myTabRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId){
				case R.id.main:
					main = new FragmentMain();
					getSupportFragmentManager().beginTransaction()
							.replace(R.id.main_fragment, main).commit();
					break;
				case R.id.examine:
//					examine = new FragmentExamine();
					examine = new FragmentMain1();
					getSupportFragmentManager().beginTransaction()
							.replace(R.id.main_fragment, examine).commit();
					break;
				case R.id.Person:
					person = new FragmentPerson();
					getSupportFragmentManager().beginTransaction()
							.replace(R.id.main_fragment, person).commit();
					break;
				default:
					break;
				}

			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-ExitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();                                
	            ExitTime = System.currentTimeMillis();
	        } 
	        else 
	        {
	          AppManager.getInstance().AppExit(context);
	          service.getInfo("AppUserCount",userBean.getLogonName(),MyConstant.APP_NAME,"exit",
						UIHelper.getLocalMacAddressFromWifiInfo(context));
	          stopService(new Intent(context,TimeServer.class));
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	    } 
	
}
