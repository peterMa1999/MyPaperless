//package com.zsm.foxconn.mypaperless;
//
//import java.util.ArrayList;
//import java.util.Set;
//
//import com.zsm.foxconn.mypaperless.base.MyConstant;
//import com.zsm.foxconn.mypaperless.bean.UserBean;
//import com.zsm.foxconn.mypaperless.help.UIHelper;
//import com.zsm.foxconn.mypaperless.http.HttpStart;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class FragmentExamine extends Fragment implements OnClickListener {
//	private View view;
//	private LinearLayout linear_Examine11,linear_Examine10,item_all9, item_all8, item_all6, item_all7,
//			linear_Examine5, Examine_all, Examine_not, Examine_ok,
//			Examine_notpass;
//	private UserBean userBean;
//	private ImageButton exitImageButton,switchers;
//	private TextView bartitle_txt,tv_canshu_mynews;
//	String isaccess = "1";
//	private HttpStart httpStart;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		userBean = (UserBean) getActivity().getApplicationContext();
//		httpStart = new HttpStart(getActivity(), handler);
//		view = inflater.inflate(R.layout.examine, container, false);
//		httpStart.getServerData(0, MyConstant.GET_CHECK_NEWS,
//				"ALL",userBean.getLogonName());
//		tv_canshu_mynews = (TextView) view.findViewById(R.id.tv_canshu_mynews);
//		exitImageButton = (ImageButton) view.findViewById(R.id.bt_img_exit);
//		if (userBean.getUserLevel().equals("2")) {
//			switchers = (ImageButton) view.findViewById(R.id.switchers);
//			switchers.setVisibility(View.VISIBLE);
//			switchers.setImageResource(R.drawable.erweima_click_seletor);
//			switchers.setOnClickListener(this);
//		}
//		exitImageButton.setVisibility(view.GONE);
//		bartitle_txt = (TextView) view.findViewById(R.id.bartitle_txt);
//		bartitle_txt.setText("審核管理");
//		Examine_all = (LinearLayout) view.findViewById(R.id.linear_Examine1);
//		Examine_all.setOnClickListener(this);
//		Examine_not = (LinearLayout) view.findViewById(R.id.linear_Examine2);
//		Examine_not.setOnClickListener(this);
//		Examine_ok = (LinearLayout) view.findViewById(R.id.linear_Examine3);
//		Examine_ok.setOnClickListener(this);
//		Examine_notpass = (LinearLayout) view
//				.findViewById(R.id.linear_Examine4);
//		Examine_notpass.setOnClickListener(this);
//		linear_Examine5 = (LinearLayout) view
//				.findViewById(R.id.linear_Examine5);
//		linear_Examine5.setOnClickListener(this);
//		item_all6 = (LinearLayout) view.findViewById(R.id.linear_Examine6);
//		item_all7 = (LinearLayout) view.findViewById(R.id.linear_Examine7);
//		item_all6.setOnClickListener(this);
//		item_all7.setOnClickListener(this);
//		item_all8 = (LinearLayout) view.findViewById(R.id.linear_Examine8);
//		item_all8.setOnClickListener(this);
//		item_all9 = (LinearLayout) view.findViewById(R.id.linear_Examine9);
//		item_all9.setOnClickListener(this);
//		linear_Examine10 = (LinearLayout) view.findViewById(R.id.linear_Examine10);
//		linear_Examine10.setOnClickListener(this);
//		linear_Examine11 = (LinearLayout) view.findViewById(R.id.linear_Examine11);
//		linear_Examine11.setOnClickListener(this);
//		return view;
//	}
//	
//	Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			ArrayList<String> result;
//			Bundle bundle = msg.getData();
//			Set<String> keySet = bundle.keySet();
//			for (String key : keySet) {
//				result = new ArrayList<String>();
//				result = msg.getData().getStringArrayList(key);
//				if (key.equalsIgnoreCase(MyConstant.GET_CHECK_NEWS)) {
//					if (result.get(0).toString()
//							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
//						int m = 0;
//						for (int i = 1; i < result.size(); i++) {
//							m += Integer.parseInt(result.get(i).toString());
//						}
//						if (m!=0) {
//							tv_canshu_mynews.setVisibility(View.VISIBLE);
//							tv_canshu_mynews.setText(""+m);
//						}
//					}
//					if (result.get(0).toString()
//							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
//						tv_canshu_mynews.setVisibility(View.GONE);
//						return;
//					}
//				}
//					}
//				}
//		};
//	
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		//報表歷史查詢
//		case R.id.linear_Examine1:
//			Intent intent = new Intent(getActivity(),
//					Examine_table_messageActivity.class);
//			intent.putExtra("linearid", "1");
//			startActivity(intent);
//			break;
//		//待審核列表
//		case R.id.linear_Examine2:
//			Intent intent2 = new Intent(getActivity(),
//					Examine_table_messageActivity.class);
//			intent2.putExtra("linearid", "2");
//			startActivity(intent2);
//			break;
//		//已審核列表
//		case R.id.linear_Examine3:
//			Intent intent3 = new Intent(getActivity(),
//					Examine_table_messageActivity.class);
//			intent3.putExtra("linearid", "3");
//			startActivity(intent3);
//			break;
//		//拒簽列表
//		case R.id.linear_Examine4:
//			Intent intent4 = new Intent(getActivity(),
//					Examine_table_messageActivity.class);
//			intent4.putExtra("linearid", "4");
//			startActivity(intent4);
//			break;
//		//點檢修改
//		case R.id.linear_Examine5:
//			String str = "0";
//			Intent intent1 = new Intent(getActivity(), AduitActivity.class);
//			// intent1.putExtra("str",str);
//			startActivity(intent1);
//			break;
//		//未點檢查詢
//		case R.id.linear_Examine6:
//				Intent intent6 = new Intent(getActivity(),
//						Choose_section.class);
//				intent6.putExtra("issection", "0");
//				intent6.putExtra("isaccess", isaccess);
//				intent6.putExtra("bUname", userBean.getBU());
//				startActivity(intent6);
//			break;
//		//提前維護線體
//		case R.id.linear_Examine7:
//				Intent intent7 = new Intent(getActivity(),
//						Choose_section.class);
//				intent7.putExtra("issection", "1");
//				intent7.putExtra("isaccess", isaccess);
//				intent7.putExtra("bUname", userBean.getBU());
//				startActivity(intent7);
//			break;
//		//報表配置
//		case R.id.linear_Examine8:
//			if (userBean.getUserLevel().equals("2")) {
//				Intent intentcft = new Intent(getActivity(),
//						Choose_section.class);
//				intentcft.putExtra("issection", "2");
//				intentcft.putExtra("isaccess", isaccess);
//				intentcft.putExtra("bUname", userBean.getBU());
//				startActivity(intentcft);
//			} else {
//				UIHelper.ToastMessage(getActivity(), "您暫無權限");
//			}
//			break;
//		//新增報表
//		case R.id.linear_Examine9:
////			if (userBean.getUserLevel().equals("2")) {
////				Intent intentcft = new Intent(getActivity(), Add_report.class);
////				startActivity(intentcft);
////			} else {
////				UIHelper.ToastMessage(getActivity(), "您暫無權限");
////			}
//			
//			UIHelper.ToastMessage(getActivity(), "暫不開放,如需添加報表請聯繫周雨林-187825");
//			break;
//		//樓層管理
//		case R.id.linear_Examine10:
//				Intent intent10 = new Intent(getActivity(), Choose_section.class);
//				intent10.putExtra("issection", "3");
//				intent10.putExtra("isaccess", "2");
//				intent10.putExtra("bUname", userBean.getBU());
//				startActivity(intent10);
//			break;
//		
//		//二維碼中心
//		case R.id.switchers:
////			Intent qr_intent = new Intent(getActivity(),MyQR_CODE.class);
////			startActivity(qr_intent);
//			UIHelper.ToastMessage(getActivity(), "二維碼中心待開發中...");
//			break;
//			
//		//參數管理
//		case R.id.linear_Examine11:
//			Intent intent11 = new Intent(getActivity(),Choose_section.class);
//			intent11.putExtra("issection", "4");
//			intent11.putExtra("isaccess", "2");
//			intent11.putExtra("bUname", userBean.getBU());
//			startActivity(intent11);
////			UIHelper.ToastMessage(getActivity(), "待開發中...");
//			break;
//		default:
//			break;
//		}
//	}
//
//}
