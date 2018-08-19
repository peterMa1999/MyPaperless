package com.zsm.foxconn.mypaperless.broadcast;

import java.util.ArrayList;
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

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Timelistenresiger {
	private Context context;
	private HttpStart start;
	private UserBean user;
	private Timer timer;
	private String[] report_no = null;
	private List<Map<String, Object>> alarmshowlist;
	private String cString[] = null;

	public Timelistenresiger(Context context, UserBean user) {
		this.context = context;
		this.user = user;
	}

	public void alter() {
		start = new HttpStart(context, handler);
		start.getServerData(0, MyConstant.GET_CHECK_TIME);
	}

	Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 2) {
				if (cString[0].toString().equalsIgnoreCase(
						MyConstant.GET_FLAG_NULL)) {
				} else {
					boolean flag = false;
					for (int i = 0; i < cString.length; i++) {
						if (cString[i].equalsIgnoreCase(GetSystemTime
								.GetTimehm(":").toString().trim())) {
							if (user.isFlag()) {
								Log.i("toast", "本节次报警已完成");
							} else {
								Log.i("toast", "本节次报警提醒中...");
								start.getServerData(0,
										MyConstant.ALARM_GET_REVNO,
										user.getLogonName());
								user.setFlag(true);
							}
							flag=false;
							break;
						}else {
							flag=true;
						}
					}
					if (flag) {
						user.setFlag(false);
					}

				}
			}
		};
	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.equals(MyConstant.GET_CHECK_TIME)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						cString = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							cString[i - 1] = result.get(i).toString();
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)||result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_UNUNITED)) {
						cString = new String[result.size()];
						cString[0] = MyConstant.GET_FLAG_NULL;
					}
					timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							try {
								
							} catch (Exception e) {
								// TODO: handle exception
							}
							Message message = new Message();
							message.what = 2;
							handler2.sendMessage(message);
						}
					}, 0, 60000);
				}

				if (key.equals(MyConstant.ALARM_GET_REVNO)) {
					result = msg.getData().getStringArrayList(key);
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
					start.getServerData(0, MyConstant.ALARM_GET_ALLLINE,
							user.getLogonName());
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
							System.out.println("-"+str);
						}
						UIHelper.alertDialog(context, "請及時下列線別：" + str);
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						break;
					}
				}
			}
		};
	};

	public void stopthread() {
		timer = new Timer();
		timer.cancel();
	}
}
