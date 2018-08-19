package com.zsm.foxconn.mypaperless.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.tsz.afinal.FinalDb;

import com.zsm.foxconn.mypaperless.Check_Project_ipqc;
import com.zsm.foxconn.mypaperless.Choose_report;
import com.zsm.foxconn.mypaperless.LoginActivity;
import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.Showchooseline_ipqc;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.ChildModelIPQC;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.bean.Wo_dateinfo;
import com.zsm.foxconn.mypaperless.help.AdialogActivity.MyAdapter.ViewHolder;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 
 * @author MGP
 *2016/01/24
 */
public class AdialogActivity{
	private HttpStart starts;
	private UserBean userBean = new UserBean();
	private List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
	private MyAdapter myAdapter;
	private int m;
	private boolean isFrist = false;
	View textEntryView;
	TextView select;
	Context context;
	private static Spinner  spPartment;
	 String checkposition[][] = null;  //選擇的審核人存在此處 
	ListView lvPerson;
	int layout,submitnum;
	int point;// 區分頁面標誌
	String teamid, checkid, mfgString,datestr;
	private String team[] = null, checknameid[] = null ,checknamenum[] = null;
	private String reportid,check_id,section,reportname,bUname,isaccess,issection,check_idname,linename,floorname;    //傳遞intent所需參數
	private String namestr = "";   //審核人拼接成字符串
	private FinalDb finalDb = null;
	
	public AdialogActivity(int layout, Context context, String datestr ,int submitnum) {
		this.context=context;
		this.datestr = datestr;
		this.layout = layout;
		this.point = 0;
		this.submitnum = submitnum;
	}

	public void intentvalue(String reportid,String check_id,String section,String reportname,String bUname,String isaccess,String check_idname,String linename,String floorname,String issection){
		this.reportid = reportid;
		this.check_id = check_id;
		this.section = section;
		this.reportname = reportname;
		this.bUname = bUname;
		this.isaccess = isaccess;
		this.check_idname = check_idname;
		this.linename = linename;
		this.floorname = floorname;
		this.issection = issection;
	}
	// public void onCreate() {
	public void onCreate(final UserBean userBean) {
		this.userBean = userBean;
		LayoutInflater factory;
		starts = new HttpStart(context, handler);
		team = new String[2];
		team[0] = userBean.getTeam().toString();
		team[1] = "QE";
		starts.getServerData(0, MyConstant.GET_SIGNOFF_NAME,userBean.getTeam(),userBean.getMFG());
//		starts.getServerData(0, MyConstant.GET_LOGIN,username,password,"no_yu_login");
		factory = LayoutInflater.from(context);

		// 把activity_login中的控件定義在View中
		textEntryView = factory.inflate(R.layout.adialog_addsignoffuser, null);
		spPartment = (Spinner) textEntryView.findViewById(R.id.partment);
		lvPerson = (ListView) textEntryView.findViewById(R.id.person);
		ArrayAdapter adapter1 = new ArrayAdapter(context,
				android.R.layout.simple_spinner_dropdown_item, team);
		spPartment.setAdapter(adapter1);
		spPartment.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				if (!isFrist) {
					isFrist = true;
				} else {
				teamid = parent.getItemAtPosition(position).toString();
				starts.getServerData(0, MyConstant.GET_SIGNOFF_NAME,teamid,userBean.getMFG());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		showWaiterAuthorizationDialog();
	}
	
	private void Adapter2() {
		checkposition = new String[2][];
		
		//当第二次执行之后就清空list值
		if (list.size()>0) {
			list.clear();
		}
		if (checknameid[0].toString().equalsIgnoreCase("null")|| checknameid.length == 0) {
			if (!myAdapter.equals(null)) {
				myAdapter.notifyDataSetChanged();
			}
			UIHelper.ToastMessage(context, "暫無數據");
			return;
		}else {
			list = new ArrayList<HashMap<String,Object>>();
			for (int j=0; j < checknameid.length;j++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("checkusername", checknameid[j].toString());
				map.put("checkstatus", false);
				list.add(map);
			}
			myAdapter = new MyAdapter(context, list, R.layout.listview_floor_item, new String[]{"checkusername","checkstatus"}, new int[]{R.id.textViewline,R.id.checkline},checknamenum);
			lvPerson.setAdapter(myAdapter);
			lvPerson.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					ViewHolder holder = (ViewHolder) arg1.getTag();
					holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态
					myAdapter.isSelected.put(arg2, holder.cb.isChecked()); // 同时修改map的值保存状态
					if (holder.cb.isChecked() == true) {
						checkposition[(int) spPartment.getSelectedItemId()][arg2] = checknamenum[arg2];		//加入數組
						
					} else {
						checkposition[(int) spPartment.getSelectedItemId()][arg2] = null;				//選擇狀態為false時為null
					}
				}
			});
			
		}
			
	}
	
	//listview的适配器
	public class MyAdapter extends BaseAdapter {

		public  HashMap<Integer, Boolean> isSelected;
		private Context context = null;
		private List<HashMap<String, Object>> list = null;
		private String keyString[] = null;
		private String itemString = null; // 记录每个item中textview的值
		private int idValue[] = null;// id值

		public class ViewHolder {
			public TextView tv = null;
			public CheckBox cb = null;
		}

		public MyAdapter(Context context, List<HashMap<String, Object>> list2,
				int resource, String[] from, int[] to,String[]checknamenum) {
			this.context = context;
			this.list = list2;
			init();
		}

		// 初始化 设置所有checkbox都为未选择
		public void init() {
			isSelected = new HashMap<Integer, Boolean>();
			if (checkposition[(int) spPartment.getSelectedItemId()] == null) {
				for (int i = 0; i < list.size(); i++) {
					isSelected.put(i, false);
				}
				checkposition[(int) spPartment.getSelectedItemId()] = new String[list
						.size()];
			} else {
				for (int j = 0; j < checkposition[(int) spPartment
						.getSelectedItemId()].length; j++) {
					if (checkposition[(int) spPartment.getSelectedItemId()][j] != null) {
						isSelected.put(j, true);
					} else {
						isSelected.put(j, false);
					}
				}
			}
			
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(context).inflate(R.layout.listview_floor_item, null);
				holder.tv = (TextView) view.findViewById(R.id.textViewline);
				holder.cb = (CheckBox) view.findViewById(R.id.checkline);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.tv.setText(list.get(position).get("checkusername").toString());
			holder.cb.setChecked((Boolean) list.get(position).get("checkstatus"));
			return view;
		}
	}
	
	long lastClick;

	
		
	public void showWaiterAuthorizationDialog() {
		Builder builder, builder2;

		m = 0;
		builder = new AlertDialog.Builder(context);
		
		AlertDialog alert = builder
				// 對話框的標題
				.setTitle("请选择提交审核人：")
				.setView(textEntryView)
				.setPositiveButton("确认提交",
						new DialogInterface.OnClickListener() {
					
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								for (int i = 0; i < checkposition.length; i++) {
									if (checkposition[i] != null) {
										for (int j = 0; j < checkposition[i].length; j++) {
											if (checkposition[i][j] != null) {
												Log.i("tag", "msg"+""
														+ checkposition[i][j].toString());
												//listline.add(checkposition[i][j].toString());
												namestr +=  checkposition[i][j].toString()+"~";
												continue;
											} else {
												continue;
											}
										}
									}
								}
								
								if (m==0) {
									if (namestr.length()!=0) {
										String str = ChangString.change(datestr);
										if (submitnum==0) {  //0正常點檢提交
											starts.getServerData(1, MyConstant.INSERT_DATA,"0"+"&&"+str+namestr+"&&"+userBean.getLogonName()+"&&"+userBean.getMFG()+"&&");
										}
										if (submitnum==1) {  //1為停線提交
											starts.getServerData(1, MyConstant.INSERT_DATA,"1"+"&&"+str+namestr+"&&"+userBean.getLogonName()+"&&"+userBean.getMFG()+"&&");
										}
									} else {
										UIHelper.ToastMessage(context, "請選擇審核人");
										m = 0;
										return;
									}
								}else {
									UIHelper.ToastMessage(context, "親~本次點檢已經提交", 5000);
									m = 0;
								}
							}
						})

				// 对话框的“退出”单击事件
				.setNegativeButton("退出", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// AdialogActivity.this.finish();
					}
				})
				// 设置dialog是否为模态，false表示模态，true表示非模态
				.setCancelable(false).create();

		alert.show();
	}

	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
					if (key.equalsIgnoreCase(MyConstant.GET_SIGNOFF_NAME)) {
						
						result = msg.getData().getStringArrayList(key);
						if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
							checknameid = new String[(result.size()-1)/2];
							checknamenum = new String[(result.size()-1)/2];
							for (int i = 0,j=1; j < result.size(); i++,j+=2) {
								checknameid[i] =result.get(j).toString()+","+result.get(j+1);
								checknamenum[i] = result.get(j).toString();
							}
						}
						if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
							checknameid = new String[result.size()];
							checknamenum = new String[result.size()];
								checknameid[0] = "null";
								checknamenum[0] = "null";
						}
						
						Adapter2();
					}
					if (key.equalsIgnoreCase(MyConstant.GET_LOGIN)) {
						team = new String[2];
						result = msg.getData().getStringArrayList(key);
						if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
								userBean.setLogonName(result.get(1).toString().toString());
								userBean.setChineseName(result.get(3).toString().toString());	
								userBean.setEnglishName(result.get(4).toString().toString());	
								userBean.setTitle(result.get(5).toString().toString());	
								userBean.setExt(result.get(6).toString().toString());	
								userBean.setEmail(result.get(7).toString().toString());	
								userBean.setUserLevel(result.get(8).toString().toString());	
								userBean.setMaster(result.get(9).toString().toString());	
								userBean.setTeam(result.get(10).toString().toString());	
								userBean.setSection(result.get(11).toString().toString());	
								userBean.setSBU(result.get(12).toString().toString());	
								userBean.setMFG(result.get(13).toString().toString());
								userBean.setBU(result.get(14).toString().toString());	
								userBean.setSite(result.get(15).toString().toString());	
								userBean.setBG(result.get(16).toString().toString());	
								team[0] = userBean.getTeam().toString();
								team[1] = "QE";
								starts.getServerData(0, MyConstant.GET_SIGNOFF_NAME,userBean.getTeam(),userBean.getMFG());
						}
						if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
							team[0] = "null";
							UIHelper.ToastMessage(context, "暫無數據");
						}
						ArrayAdapter adapter1 = new ArrayAdapter(context,
								android.R.layout.simple_spinner_dropdown_item, team);
						spPartment.setAdapter(adapter1);
					}
					
					if (key.equalsIgnoreCase(MyConstant.INSERT_DATA)) {
						result = msg.getData().getStringArrayList(key);
						if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
							m = 1;
							if (reportid.equalsIgnoreCase("IPQCOBA2016001")||reportid.equalsIgnoreCase("FQ3NMD054001A")||
									reportid.equalsIgnoreCase("FQ3NMD054002A")||reportid.equalsIgnoreCase("FQ3NMD054003A")) {
								starts.getServerData(0,MyConstant.OBA_delete_line,reportid,linename,check_id,userBean.getLogonName());
							}else{
//								starts.getServerData(0,MyConstant.REMIND_CHECK,reportname,namestr);
								UIHelper.ToastMessage(context, "點檢成功",5000);
									Intent intent = new Intent(context,Showchooseline_ipqc.class);
									intent.putExtra("reportid", reportid);
									intent.putExtra("check_id", check_id);
									intent.putExtra("section", section);
									intent.putExtra("reportname", reportname);
									intent.putExtra("bUname", bUname);
									intent.putExtra("isaccess", isaccess);
									intent.putExtra("check_idname", check_idname);
									intent.putExtra("issection", issection);
									context.startActivity(intent);
									AppManager.killActivity(Check_Project_ipqc.class);
								Adapter2();
							}
							finalDb = FinalDb.create(context, "child"); // 创建数据库
							finalDb.deleteByWhere(Wo_dateinfo.class, "Logonname='"
									+ userBean.getLogonName() + "' and report_num='"
									+ reportid + "' and floor_name='" + floorname
									+ "' and line_name='" + linename + "'");
							finalDb.deleteByWhere(ChildModelIPQC.class,
									"Logonname='" + userBean.getLogonName()
											+ "' and report_num='" + reportid
											+ "' and floor_name='" + floorname
											+ "' and line_name='" + linename
											+ "'");
						}
						if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
							datestr = "";
							UIHelper.ToastMessage(context, "點檢失敗");
							Adapter2();
						}
						
					}
					
					if (key.equalsIgnoreCase(MyConstant.OBA_delete_line)) {
						result = msg.getData().getStringArrayList(key);
						if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
							}
						if (result.get(0).equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
							
						}
						UIHelper.ToastMessage(context, "點檢成功",5000);
						starts.getServerData(0,MyConstant.REMIND_CHECK,reportname,namestr);
						Intent intent = new Intent(context,Showchooseline_ipqc.class);
						intent.putExtra("reportid", reportid);
						intent.putExtra("check_id", check_id);
						intent.putExtra("section", section);
						intent.putExtra("reportname", reportname);
						intent.putExtra("bUname", bUname);
						intent.putExtra("isaccess", isaccess);
						intent.putExtra("check_idname", check_idname);
						intent.putExtra("issection", issection);
						context.startActivity(intent);
						AppManager.killActivity(Check_Project_ipqc.class);
						Adapter2();
						}
					}
					}
			};
			
}
