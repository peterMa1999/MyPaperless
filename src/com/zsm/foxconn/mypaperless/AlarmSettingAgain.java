package com.zsm.foxconn.mypaperless;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.broadcast.TimeBrocast;

public class AlarmSettingAgain extends BaseActivity {

	private MediaPlayer mp = new MediaPlayer();
	private Vibrator vibrator;
	private PowerManager.WakeLock mWakelock;
	private Context context = AlarmSettingAgain.this;
	String str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // hide title
		Intent intent = getIntent();
		str = intent.getStringExtra("broadinfo");
		Log.i("tag", str);
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		startMedia();
		startVibrator();
		createDialog();
	}

	/**
	 * 
	 */
	private void createDialog() {
		AlertDialog dialog = new AlertDialog.Builder(context)
				.setTitle("以下線體請及時點檢:")
				.setMessage(str)
				.setPositiveButton("我知道了",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mp.stop();
				                vibrator.cancel();
								finish();
							}
						}).create();
		dialog.getWindow()
				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 唤醒屏幕
		acquireWakeLock();
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseWakeLock();
	}

	/**
	 * 开始播放铃声
	 */
	private void startMedia() {
		try {
			mp.setDataSource(this,
					RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
			mp.prepare();
			mp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 震动
	 */
	private void startVibrator() {
		/**
		 * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
		 * 
		 */
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 500, 1000, 500, 1000 }; // 停止 开启 停止 开启
		vibrator.vibrate(pattern, 0);
	}

	/**
	 * 唤醒屏幕
	 */
	private void acquireWakeLock() {
		if (mWakelock == null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
					| PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass()
					.getCanonicalName());
			mWakelock.acquire();
		}
	}

	/**
	 * 释放锁屏
	 */
	private void releaseWakeLock() {
		if (mWakelock != null && mWakelock.isHeld()) {
			mWakelock.release();
			mWakelock = null;
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			mp.stop();
            vibrator.cancel();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
