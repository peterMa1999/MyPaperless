package com.zsm.foxconn.mypaperless.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public abstract class BaseActivity extends Activity {

	public static final String TAG = BaseActivity.class.getSimpleName();
	protected Handler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		AppManager.getInstance().addActivity(this);
		// if (!ImageLoader.getInstance().isInited()) {
		// ImageLoaderConfig.initImageLoader(this, Constants.BASE_IMAGE_CACHE);
		// }
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		Log.i("tag", "父類暫停Activity");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
//		Log.i("tag", "父類重新啟動Activity");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// AseoZdpAseo.init(this, AseoZdpAseo.INSERT_TYPE);
		// AseoZdpAseo.init(this, AseoZdpAseo.SCREEN_TYPE);
//		Log.i("tag", "父類重新啟動Activity");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		Log.i("tag", "父類啟動Activity");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		Log.i("tag", "父類停止Activity");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		Log.i("tag", "父類銷毀Activity");
	}
	
	/**
	 * 检查账号是否登录
	 */
	protected abstract void CheckLogin();

	/**
	 * 绑定控件id
	 */
	protected abstract void findViewById();

	/**
	 * 初始化控件
	 */
	protected abstract void initView();

	/**
	 * 通过类名启动Activity
	 * 
	 * @param pClass
	 */
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	/**
	 * 通过类名启动Activity，并且含有Bundle数据
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 通过Action启动Activity
	 * 
	 * @param pAction
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	/**
	 * 通过Action启动Activity，并且含有Bundle数据
	 * 
	 * @param pAction
	 * @param pBundle
	 */
	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

}
