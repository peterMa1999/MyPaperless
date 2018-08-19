package com.zsm.foxconn.mypaperless.broadcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.zsm.foxconn.mypaperless.AlarmSettingAgain;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class TimeBrocast extends BroadcastReceiver {
	Context context = null;
	AlertDialog alertDialog = null;
	private Intent intent;

	// TimeServiceActivity activity=new TimeServiceActivity();
	@Override
	public void onReceive(Context arg0, Intent intent) {
		this.context = arg0;
		if (intent.getAction().equalsIgnoreCase("fromTimeServer")) {
			Log.i(">>>TimeBrocast>>>", intent.getExtras()
					.getString("broadinfo"));
			String str = intent.getExtras().getString("broadinfo");
			Intent alaramIntent = new Intent(context, AlarmSettingAgain.class);
			alaramIntent.putExtra("broadinfo", str);
			alaramIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(alaramIntent);
		}
		if (intent.getAction().equalsIgnoreCase("fromConnectedServer")) {
			String str = intent.getExtras().getString("subtypename");
			if (str.equals("WIFI")) {
				UIHelper.ToastMessage(context, "當前為WIFI網絡");
			}else if (str.equals("Network Type is null")) {
				UIHelper.ToastMessage(context, "無網絡連接");
			} else {
				UIHelper.ToastMessage(context, "當前為" + str + "網絡不可用");
			}
		}
	}
}
