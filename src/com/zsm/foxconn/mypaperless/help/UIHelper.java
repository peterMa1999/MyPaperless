package com.zsm.foxconn.mypaperless.help;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.base.MyConstant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class UIHelper {

	/**
	 * 公用方法，提示消息
	 * 
	 * @param cont
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * 公用方法，提示消息
	 * 
	 * @param cont
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg, String path) {
		Toast toast = null;
		toast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 公用方法，提示消息
	 * 
	 * @param cont
	 * @param msg
	 */
	public static void ToastMessage(Context cont, int msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 公用方法，提示消息
	 * 
	 * @param cont
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg, int time) {
		Toast.makeText(cont, msg, time).show();
	}

	/**
	 * 公用方法，提示消息
	 * 
	 * @param cont
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg, boolean flag) {
		Toast toast = new Toast(cont);
		toast = Toast.makeText(cont, msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(cont);
		if (flag) {
			// imageCodeProject.setImageResource(R.drawable.f001);
		} else {
			// imageCodeProject.setImageResource(R.drawable.f006);
		}
		toastView.addView(imageCodeProject, 0);
		toast.show();
	}

	/**
	 * 返回字符串数据,把数据以%拼接
	 * 
	 * @param strings
	 * @return
	 */
	public static String getResult(String... strings) {
		String result = "";
		for (int i = 0; i < strings.length; i++) {
			result += strings[i] + MyConstant.GET_FLAG;
		}
		return result;
	}

	/**
	 * 服务器返回数据提示
	 * 
	 * @param context
	 * @param parameter
	 * @return
	 */
	public static boolean getResultFlag(Context context, String... parameter) {
		for (int i = 0; i < parameter.length; i++) {
			Log.i("parameter=", parameter[i]);
		}
		boolean result = false;
		if (parameter[0].equals(MyConstant.GET_FLAG_NULL)
				|| parameter[0].equals(MyConstant.GET_FLAG_EXCEPTION)
				|| parameter[0].equals(MyConstant.GET_FLAG_UNUNITED)) {
			Toast.makeText(context, parameter[1], 1000).show();
		} else if (parameter[0].equals(MyConstant.GET_FLAG_TRUE)) {
			result = true;
		}
		return result;
	}

	/**
	 * 设置监控
	 * 
	 * @param webView
	 * @param path
	 */
	public static void setWebView(WebView webView, String path) {
		webView.getSettings().setJavaScriptEnabled(true);// 設置javaScript可用
		webView.setWebChromeClient(new WebChromeClient());// 處理javaScript對話框
		webView.setWebViewClient(new WebViewClient());// 處理各種請求和通知事件,如果不使用該句代碼,將使用內置瀏覽器訪問網頁
		webView.loadUrl(path);
	}

	/**
	 * 获取当前日期时间
	 * 
	 * @return
	 */
	public static String getDayTime(String flag) {
		SimpleDateFormat df = null;
		if (flag.equals("hhmmss")) {
			df = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS");
		} else {
			df = new SimpleDateFormat("yyyyMMdd");
		}
		String date = df.format(new Date());
		return date;
	}
	
	/**
	 * 弹出系统提示框
	 * 
	 * @param context
	 * @param Title
	 */
	public static void alertDialog(Context context,String Title){
		final AlertDialog alert=new AlertDialog.Builder(context).create();
		alert.setIcon(R.drawable.nt_warn);
		alert.setTitle("温馨提示：");
		alert.setMessage(Title);
		alert.setButton(DialogInterface.BUTTON_POSITIVE, "確定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alert.dismiss();
			}
		});
		alert.show();
	}

	/**
	 * 弹出系统提示框
	 * 
	 * @param context
	 * @param Title
	 */
	public static void alertDialogEng(Context context,String Title){
		AlertDialog alert=new AlertDialog.Builder(context).create();
		alert.setIcon(R.drawable.nt_warn);
		alert.setTitle("System info：");
		alert.setMessage(Title);
		alert.show();
	}
	
	/**
	 * Id增加1
	 * 
	 * @param content
	 * @return
	 */
	public static String ContentId(String content) { // 20150711A0001
		String result = null;
		String x = content.substring(0, 9);// 20150711A
		String y = content.substring(9); // 0001
		String h = null;
		Integer i = Integer.valueOf(y) + 1;

		if (i < 10) {
			h = "000" + i;
		} else if (i < 100) {
			h = "00" + i;
		} else if (i < 1000) {
			h = "0" + i;
		} else {
			h = "" + i;
		}
		result = x + h;
		return result;
	}
	
	public static String getLocalMacAddressFromWifiInfo(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
        WifiInfo info = wifi.getConnectionInfo();  
        return info.getMacAddress(); 
    }
}
