package com.zsm.foxconn.mypaperless.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.GetSystemTime;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

public class TimeServer extends Service {
//	private Context context = TimeServer.this;
	String timeString[] = null;
	boolean threadDisable = true;
	UserBean userBean;
	Intent intent;
	HttpStart httpStart;
	private String[] report_no = null;
	private List<Map<String, Object>> alarmshowlist;
	private String cString[] = null;
	String connectype;
	// 实时监听网络状态改变
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// String action = intent.getAction();
			// if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
			// {
			Timer timer = new Timer();
			timer.schedule(new QunXTask(TimeServer.this), new Date());
			// }
		}
	};

	// public interface GetConnectState {
	// public void GetState(boolean isConnected); //
	// 网络状态改变之后，通过此接口的实例通知当前网络的状态，此接口在Activity中注入实例对象
	// }

	// private GetConnectState onGetConnectState;
	//
	// public void setOnGetConnectState(GetConnectState onGetConnectState) {
	// this.onGetConnectState = onGetConnectState;
	// }

	private Binder binder = new MyBinder();
	private boolean isContected = true;

	@Override
	public void onCreate() {
		super.onCreate();
		// 注册广播
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); // 添加接收网络连接状态改变的Action
		registerReceiver(mReceiver, mFilter);
		httpStart = new HttpStart(this, handler);
		Log.i(">>TimeServer>>", "服务创建");
		// Log.i(">>TimeServer>>", userBean.getLogonName().toString());
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (threadDisable) {
					try {
						if (timeString != null) {
							for (int i = 0; i < timeString.length; i++) {
								if (timeString[i].toString().equalsIgnoreCase(
										GetSystemTime.GetTimehm(":"))) {
									httpStart.getServerData(0,
											MyConstant.ALARM_GET_REVNO,
											userBean.getLogonName());
								}
							}
						}
						Thread.sleep(60000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}).start();

	}

	class QunXTask extends TimerTask {
		private Context context;

		public QunXTask(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			connectype = GetNetworkType(context);
			if (connectype.length()==0) {
				connectype = "Network Type is null";
			}
			intent = new Intent();
			intent.setAction("fromConnectedServer");
			intent.putExtra("subtypename", connectype);
			sendBroadcast(intent);
//			isWifiConnected(context);
//			isNetworkConnected(context);
//			if (isWifiConnected(context) || isNetworkConnected(context)) {
//				isContected = true;
//			} else {
//				isContected = false;
//			}
			// if (onGetConnectState != null) {
			// onGetConnectState.GetState(isContected); // 通知网络状态改变
			// Log.i("mylog", "通知网络状态改变:" + isContected);
			// }
		}



	}
	
	public static String GetNetworkType(Context context)
	{
	    String strNetworkType = "";
	    ConnectivityManager connectivityManager = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();
	    		
//	    NetworkInfo networkInfo = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE).getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected())
	    {
	        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
	        {
	            strNetworkType = "WIFI";
	        }
	        else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
	        {
	            String _strSubTypeName = networkInfo.getSubtypeName();
	            
	            // TD-SCDMA   networkType is 17
	            int networkType = networkInfo.getSubtype();
	            switch (networkType) {
	                case TelephonyManager.NETWORK_TYPE_GPRS:
	                case TelephonyManager.NETWORK_TYPE_EDGE:
	                case TelephonyManager.NETWORK_TYPE_CDMA:
	                case TelephonyManager.NETWORK_TYPE_1xRTT:
	                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
	                    strNetworkType = "2G";
	                    break;
	                case TelephonyManager.NETWORK_TYPE_UMTS:
	                case TelephonyManager.NETWORK_TYPE_EVDO_0:
	                case TelephonyManager.NETWORK_TYPE_EVDO_A:
	                case TelephonyManager.NETWORK_TYPE_HSDPA:
	                case TelephonyManager.NETWORK_TYPE_HSUPA:
	                case TelephonyManager.NETWORK_TYPE_HSPA:
	                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
	                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
	                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
	                    strNetworkType = "3G";
	                    break;
	                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
	                    strNetworkType = "4G";
	                    break;
	                default:
	                    // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
	                    if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) 
	                    {
	                        strNetworkType = "3G";
	                    }
	                    else
	                    {
	                        strNetworkType = _strSubTypeName;
	                    }
	                    
	                    break;
	             }
	             
	        }
	    }
	    Log.e("cocos2d-x", "Network Type : " + strNetworkType);
	    return strNetworkType;
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> result = null;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = new ArrayList<String>();
				result = msg.getData().getStringArrayList(key);
				if (key.equals(MyConstant.GET_ALARM_TIME_YEILD)) {
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 0; i < result.size(); i++) {
							if (result.get(i).toString()
									.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
								result.remove(i);
							}

						}
						if (result.size() > 0) {
							timeString = new String[result.size()];
							for (int j = 0; j < result.size(); j++) {
								timeString[j] = result.get(j).toString();
							}
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
//						stopService(new Intent(TimeServer.this,
//								TimeServer.class));
					}
				}

				if (key.equals(MyConstant.ALARM_GET_REVNO)) {
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						report_no = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							report_no[i - 1] = result.get(i).toString();
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						report_no = new String[result.size()];
						report_no[0] = MyConstant.GET_FLAG_NULL;
					}
					httpStart.getServerData(0, MyConstant.ALARM_GET_ALLLINE,
							userBean.getLogonName());
				}

				if (key.equals(MyConstant.ALARM_GET_ALLLINE)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						alarmshowlist = new ArrayList<Map<String, Object>>();
						ArrayList<String> list = new ArrayList<String>();
						for (int i = 1; i < result.size(); i += 4) {
							list.add(result.get(i).toString());
							list.add(result.get(i + 1).toString());
							list.add(result.get(i + 3).toString());
						}
						if (report_no[0].toString().equals(
								MyConstant.GET_FLAG_NULL)) {
							for (int k = 0; k < list.size(); k += 3) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("report_name", list.get(k).toString());
								map.put("nocheck_line", list.get(k + 2)
										.toString());
								alarmshowlist.add(map);
							}
						} else {
							for (int j2 = 0; j2 < report_no.length; j2++) {
								for (int k = 0; k < list.size(); k += 3) {
									if (!report_no[j2].toString().contains(
											list.get(k + 2).toString())) {
										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("report_name", list.get(k)
												.toString());
										map.put("nocheck_line", list.get(k + 2)
												.toString());
										alarmshowlist.add(map);
									}
								}
							}
						}
						ArrayList<String> strreportname = new ArrayList<String>();
						for (int k = 0; k < alarmshowlist.size(); k++) {
							String reportname = alarmshowlist.get(k)
									.get("report_name").toString();
							if (!strreportname.contains(reportname)) {
								strreportname.add(reportname);
							}
						}
						ArrayList<String> strlinename = new ArrayList<String>();
						for (int k = 0; k < strreportname.size(); k++) {
							String linestr = "";
							for (int k2 = 0; k2 < alarmshowlist.size(); k2++) {
								String reportname = alarmshowlist.get(k2)
										.get("report_name").toString();
								if (strreportname.get(k).toString()
										.equals(reportname)) {
									String linename = alarmshowlist.get(k2)
											.get("nocheck_line").toString();
									linestr += linename + "/";
								}
							}
							strlinename.add(linestr);
						}
						String str = "";
						for (int l = 0; l < strreportname.size(); l++) {
							str += strreportname.get(l).toString() + "-"
									+ strlinename.get(l).toString() + "  ";
							System.out.println("-" + str);
						}
						intent = new Intent();
						intent.setAction("fromTimeServer");
						intent.putExtra("broadinfo", str);
						sendBroadcast(intent);

					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {

						return;
					}
				}
			}
		}
	};

	public class MyBinder extends Binder {
		public TimeServer getService() {
			return TimeServer.this;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		threadDisable = false;
		unregisterReceiver(mReceiver); // 删除广播
		Log.i(">>TimeServer>>", "服务销毁");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		userBean = (UserBean) intent.getSerializableExtra("userBean");
		Log.i(">>Time", userBean.getLogonName().toString());
		httpStart.getServerData(0, MyConstant.GET_ALARM_TIME_YEILD, userBean
				.getMFG().toString(), userBean.getBU().toString(), userBean
				.getSite().toString());
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub

		return null;
	}

}