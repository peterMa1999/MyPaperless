package com.zsm.foxconn.mypaperless.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.help.ChangString;
import com.zsm.foxconn.mypaperless.http.HttpStart;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateManger 
{
	/**
	 * 当前apk
	 */
	// 应用程序Context
	private Context mContext;
	//旧版本号
	private String PastVersion;
	//新版本号
	private String NowVersion;
	// 提示消息
	private String updateMsg = "有最新的软件包，请下载！";
	// 下载安装包的网络路径
	private String apkUrl = MyConstant.GET_localUrl + MyConstant.GET_localAPK;
	private Dialog noticeDialog;// 提示有软件更新的对话框
	private Dialog downloadDialog;// 下载对话框
	private static final String savePath = MyConstant.SD_PATH;// 保存apk的文件夹
	private static final String saveFileName = savePath
			+ MyConstant.GET_localAPK;
	// 进度条与通知UI刷新的handler和msg常量
	private ProgressBar mProgress;
	private TextView mProg,path;
	
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private int progress;// 当前进度
	private Thread downLoadThread; // 下载线程
	private boolean interceptFlag = false;// 用户取消下载
	HttpStart start = null;
	
	// 通知处理刷新界面的handler
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProg.setText(progress+"%");
				break;
			case DOWN_OVER:
				if (downloadDialog!=null) {
					downloadDialog.cancel();
				}
//				installApk();
				Intent i = new Intent();
				 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				 i.setAction(android.content.Intent.ACTION_VIEW);
				 i.setDataAndType(Uri.parse("file://" + saveFileName),
						 "application/vnd.android.package-archive");
//						i.setDataAndType(Uri.fromFile(new File(saveFileName)),
//						"application/vnd.android.package-archive");// File.toString()会返回路径信息
				mContext.startActivity(i);
				break;
			}
			super.handleMessage(msg);
		}
	};

	public UpdateManger(Context context, String localVersion,
			String mNowVersion, String urlapk) {
		this.mContext = context;
		this.PastVersion=localVersion;
		this.NowVersion=mNowVersion;
		this.apkUrl=urlapk+MyConstant.GET_localAPK;
	}
	
	public UpdateManger(Context context, String urlapk) {
		this.mContext = context;
		this.apkUrl=urlapk+MyConstant.GET_localAPK;
	}

	// 显示更新程序对话框，供主程序调用
	public void checkUpdateInfo(String updatemsg) {
		if (updatemsg!=null) {
			updateMsg=updatemsg;
		}
		showNoticeDialog();
	}

	private void showNoticeDialog() {
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
				mContext);// Builder，可以通过此builder设置改变AleartDialog的默认的主题样式及属性相关信息
		builder.setTitle("软件版本更新");
		builder.setMessage(updateMsg);
		builder.setPositiveButton("下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();// 当取消对话框后进行操作一定的代码？取消对话框
				showDownloadDialog();
				PhoneInfo phoneinfo = new PhoneInfo();
				TelephonyManager tm = phoneinfo.getTelephonyManager(mContext);
				//唯一的设备ID
				String deviceid = tm.getDeviceId();
				//手機運營商
				String simoperatorname = ChangString.change(tm.getSimOperatorName());
				String phonebrand = phoneinfo.getPhoneBrand();
				String phonemodel = phoneinfo.getPhoneModel();
				int appvercode = phoneinfo.getVerCode(mContext);
				int[] metrics = phoneinfo.getMetrics(mContext);
				String metric = metrics[0]+"*"+metrics[1];
				if (simoperatorname.length()==0) {
					simoperatorname = "NA";
				}
				start = new HttpStart(mContext, mHandler);
				start.getServerData(0, MyConstant.SAVE_DOWNLOAD_PHONE,deviceid,phonebrand,phonemodel,appvercode+"",metric,simoperatorname);
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	public void showDownloadDialog() 
	{
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
				mContext);
		builder.setTitle("软件版本更新");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progressbar, null);
		mProgress = (ProgressBar) v.findViewById(R.id.current_progress);
		mProg= (TextView) v.findViewById(R.id.current_progress_info);
		path= (TextView) v.findViewById(R.id.path);
		path.setText("保存路径于:"+MyConstant.SD_PATH);
		builder.setView(v);// 设置对话框的内容为一个View
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();
		downloadApk();
	}

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	protected void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
//		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				i.setDataAndType(Uri.fromFile(new File(saveFileName)),
				"application/vnd.android.package-archive");// File.toString()会返回路径信息
		mContext.startActivity(i);
		
		 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		 i.setAction(android.content.Intent.ACTION_VIEW);
		 i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				 "application/vnd.android.package-archive");
		 mContext.startActivity(i);
		
		
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			URL url;
			try {
				url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream ins = conn.getInputStream();
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream outStream = new FileOutputStream(ApkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = ins.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 下载进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					outStream.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消停止下载
				outStream.close();
				ins.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}