package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.tsz.afinal.FinalDb;

import com.zsm.foxconn.mypaperless.adapter.BaseViewHolder;
import com.zsm.foxconn.mypaperless.adapter.MyGridAdapter;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.database.APP_yindao_page;
import com.zsm.foxconn.mypaperless.help.MarqueeText;
import com.zsm.foxconn.mypaperless.help.MyGridView;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.help.ViewPagerBarnner;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.GuideUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 審核管理
 */
public class FragmentMain1 extends Fragment {
	private View view;
	private Intent intent;
	private MyGridView gridview;// 宫格
	private List<Bitmap> url;
	private ViewPagerBarnner viewPagerBarnner;// 轮播
	private MarqueeText test;
	private UserBean userBean;
	private ImageButton exitImageButton, switchers;
	private TextView bartitle_txt;
	String isaccess = "1";
	private HttpStart httpStart;
	String string;
	String n = "", m = "",p= "", q="";
	int waitNum = 0,rejectNum = 0,excetion_num = 0;
	private FinalDb finalDb = null;
	private GuideUtil guideUtil = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.examine_grid, container, false);
		userBean = (UserBean) getActivity().getApplicationContext();
		httpStart = new HttpStart(getActivity(), handler);
		exitImageButton = (ImageButton) view.findViewById(R.id.bt_img_exit);
		exitImageButton.setVisibility(view.GONE);
		bartitle_txt = (TextView) view.findViewById(R.id.bartitle_txt);
		bartitle_txt.setText("審核管理");
		initViewPager();
		initView();
		finalDb = FinalDb.create(getActivity(), "child"); //创建数据库
		List<APP_yindao_page> list_app = finalDb.findAllByWhere(APP_yindao_page.class, "pagename='FragmentMain1' and pagename_id='0'");
		if (list_app.size()==0){
			guideUtil = GuideUtil.getInstance();
			guideUtil.initGuide(getActivity(), R.drawable.fragmentmain1_20161223,"FragmentMain1", 0);
		}
		test = (MarqueeText) view.findViewById(R.id.test);
		test.startScroll();
		return view;
	}

	// 可见执行
	public void onStart() {
		// 查询滚动文字
		// start.getServerData(0, MyConstant.QUERIES_SCROLLING_TEXT);
		super.onStart();
	}

	// 宫格
	private void initView() {
		gridview = (MyGridView) view.findViewById(R.id.gridview);
		gridview.setAdapter(new MyGridAdapter(getActivity(), n, m, p,q));
		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				TextView tv = BaseViewHolder.get(arg1, R.id.tv_item);
				String title = tv.getText().toString();
				if (title.length() > 0 && title != null) {
					// 宫格跳转
					switch (arg2) {
					// 報表歷史查詢
					case 0:
						Intent intent = new Intent(getActivity(),
								Examine_table_messageActivity.class);
						intent.putExtra("linearid", "1");
						startActivity(intent);
						break;
					// 待審核列表
					case 1:
						Intent intent2 = new Intent(getActivity(),
								Examine_table_messageActivity.class);
						intent2.putExtra("linearid", "2");
						startActivity(intent2);
						break;
					// 已審核列表
					case 2:
						Intent intent3 = new Intent(getActivity(),
								Examine_table_messageActivity.class);
						intent3.putExtra("linearid", "3");
						startActivity(intent3);
						break;
					// 拒簽列表
					case 3:
						Intent intent4 = new Intent(getActivity(),
								Examine_table_messageActivity.class);
						intent4.putExtra("linearid", "4");
						startActivity(intent4);
						break;
					// 點檢修改
					case 4:
						Intent intent1 = new Intent(getActivity(),
								ModifyCheckActivity.class);
						intent1.putExtra("waitNum",waitNum+"");
						intent1.putExtra("rejectNum",rejectNum+"");
						startActivity(intent1);
						break;
					// 點檢狀況查詢
					case 5:
						Intent intent6 = null;
//						if (userBean.getTeam().equalsIgnoreCase("IPQC")) {
//							intent6 = new Intent(getActivity(),
//									Choose_section.class);
//							intent6.putExtra("issection", "0");
//							intent6.putExtra("isaccess", isaccess);
//							intent6.putExtra("bUname", userBean.getBU());
//							startActivity(intent6);
//						}else {
							intent6 = new Intent(getActivity(),CheckSitActivity.class);
							startActivity(intent6);
//						}
						break;
					// 提前維護線體
					case 6:
						if (userBean.getTeam().equalsIgnoreCase("IPQC")) {
						Intent intent7 = new Intent(getActivity(),
								Choose_section.class);
						intent7.putExtra("issection", "1");
						intent7.putExtra("isaccess", isaccess);
						intent7.putExtra("bUname", userBean.getBU());
						startActivity(intent7);
						}else {
						startActivity(new Intent(getActivity(), Tiqianweihu_Byline.class));
						}
						break;
					// 報表配置
					case 7:
						if (userBean.getUserLevel().equals("2")) {
							Intent intentcft = new Intent(getActivity(),
									Choose_section.class);
							intentcft.putExtra("issection", "2");
							intentcft.putExtra("isaccess", isaccess);
							intentcft.putExtra("bUname", userBean.getBU());
							startActivity(intentcft);
						} else {
							UIHelper.ToastMessage(getActivity(), "您暫無權限");
						}
						break;
					// 新增報表
					case 8:
						// if (userBean.getUserLevel().equals("2")) {
						// Intent intentcft = new Intent(getActivity(),
						// Add_report.class);
						// startActivity(intentcft);
						// } else {
						// UIHelper.ToastMessage(getActivity(), "您暫無權限");
						// }

						UIHelper.ToastMessage(getActivity(),
								"暫不開放,如需添加報表請聯繫周雨林-187825");
						break;
					// 樓層管理
					case 9:
						Intent intent10 = new Intent(getActivity(),
								Choose_section.class);
						intent10.putExtra("issection", "3");
						intent10.putExtra("isaccess", "2");
						intent10.putExtra("bUname", userBean.getBU());
						startActivity(intent10);
						break;

					// 參數管理
					case 10:
						Intent intent11 = new Intent(getActivity(),
								Choose_section.class);
						intent11.putExtra("issection", "4");
						intent11.putExtra("isaccess", "2");
						intent11.putExtra("bUname", userBean.getBU());
						startActivity(intent11);
						// UIHelper.ToastMessage(getActivity(), "待開發中...");
						break;

					// 圖表統計
					case 11:
						Intent intent12 = new Intent(getActivity(),
								ChartListActivity.class);
						startActivity(intent12);
						break;
					// 異常管理
					case 12:
						Intent intent13 = new Intent(getActivity(),
								AbnormalFragmentActivity.class);
						startActivity(intent13);
						break;
						
					//補點檢	
					case 13:
						if (userBean.getUserLevel().equalsIgnoreCase("2")
								&& !userBean.getTeam().equalsIgnoreCase("IPQC")) {
//							Intent intent14 = new Intent(getActivity(),
//									Check_UpActivity.class);
//							TextView texttv = (TextView) gridview.getAdapter()
//									.getView(arg2, arg1, gridview)
//									.findViewById(R.id.tv_item);
//							intent14.putExtra("TitleName", texttv.getText()
//									.toString());
//							intent14.putExtra("weihu", "0");
//							startActivity(intent14);
							UIHelper.ToastMessage(getActivity(), "暫無權限");
						}else if (userBean.getLogonName().equalsIgnoreCase("admin")||
								userBean.getUserLevel().equalsIgnoreCase("3")) {
							Intent intent14 = new Intent(getActivity(),
									Check_UpActivity.class);
							TextView texttv = (TextView) gridview.getAdapter()
									.getView(arg2, arg1, gridview)
									.findViewById(R.id.tv_item);
							intent14.putExtra("TitleName", texttv.getText()
									.toString());
							intent14.putExtra("weihu", "0");
							startActivity(intent14);
						} else {
							UIHelper.ToastMessage(getActivity(), "暫無權限");
						}
						break;
					//IPQC CrossBU Issue
					case 14:
						if (userBean.getTeam().equalsIgnoreCase("IPQC")){
							Intent intent15 = new Intent(getActivity(),
									IPQC_CrossBU_Issue.class);
							startActivity(intent15);
						}else {
							UIHelper.ToastMessage(getActivity(), "您無權限");
						}						
						break;
					default:
						break;
					}
				}
			}
		});
	}

	// 轮播
	private void initViewPager() {
		// viewPagerBarnner = (ViewPagerBarnner)
		// view.findViewById(R.id.viewPager);
		// url = new ArrayList<Bitmap>();
		// url.add(BitmapFactory.decodeResource(getResources(), R.drawable.a));
		// url.add(BitmapFactory.decodeResource(getResources(), R.drawable.b));
		// url.add(BitmapFactory.decodeResource(getResources(), R.drawable.c));
		// url.add(BitmapFactory.decodeResource(getResources(), R.drawable.d));
		//
		// viewPagerBarnner.addImageUrls(url);
		// viewPagerBarnner.setViewPagerClick(new ViewPagerClick() {
		//
		// public void viewPagerOnClick(View view) {
		// Toast.makeText(getActivity(),
		// "点击了第" + view.getTag().toString() + "个图片",
		// Toast.LENGTH_SHORT).show();
		// }
		// });
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				result = new ArrayList<String>();
				result = msg.getData().getStringArrayList(key);
				if (key.equalsIgnoreCase(MyConstant.GET_CHECK_NEWS)) {
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						int a = 0, b = 0;
						try {
							a = Integer.parseInt(result.get(1).toString());
							b = Integer.parseInt(result.get(2).toString());
							waitNum = Integer.parseInt(result.get(3).toString());
							rejectNum = Integer.parseInt(result.get(4).toString());
							excetion_num = Integer.parseInt(result.get(5).toString());
							
							if (a > 99) {
								n = "99+";
							} else if (a == 0) {
								n = "";
							} else {
								n = a + "";
							}
							if (b > 99) {
								m = "99+";
							} else if (b == 0) {
								m = "";
							} else {
								m = b + "";
							}
							
							if ((waitNum+rejectNum) > 99) {
								p = "99+";
							}else if (waitNum+rejectNum==0) {
								p="";
							} else {
								p =(waitNum+rejectNum)+"";
							}
							
							if (excetion_num>99) {
								q = "99+";
							}else if (excetion_num==0) {
								q = "";
							}else {
								q = excetion_num+"";
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						if (a != 0 || b != 0||waitNum+rejectNum!=0||excetion_num!=0) {
							gridview.setAdapter(new MyGridAdapter(
									getActivity(), n, m,p,q));
						}
					}
					if (result.get(0).toString()
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						return;
					}
				}
				if (key.equals(
						MyConstant.GET_FLAG_UNUNITED)) {
					Toast.makeText(getActivity(), "未连接服务器....", 0).show();
					return;
				}
			}
		}
	};
	
	@Override
	public void onResume() {
		super.onResume();
		httpStart.getServerData(0, MyConstant.GET_CHECK_NEWS, "ALL",
				userBean.getLogonName());
	};
}
