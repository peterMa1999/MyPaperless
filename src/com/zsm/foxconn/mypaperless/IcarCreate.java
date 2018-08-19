//package com.zsm.foxconn.mypaperless;
//
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import com.zsm.foxconn.mypaperless.help.UIHelper;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.view.ViewTreeObserver.OnPreDrawListener;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.HorizontalScrollView;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class IcarCreate extends Activity implements OnItemClickListener {
//	IpqcSampleService sampleservice = new IpqcSampleService();
//	HibJdbcDbconn iCarinfo=new HibJdbcDbconn();
//	PerSon perSon;
//	private int isRead = 0;
//	private static final int TAKE_PHOTO = 0;
//	private static final int CROP_PHOTO = 1;
//	private static final int RESULT_PHOTO = 2;
//	public List<Bitmap> bmp = new ArrayList<Bitmap>();// 保存图片
//	public List<String> drr = new ArrayList<String>();
//	private File imageFile = new File(
//			Environment.getExternalStorageDirectory(), getPhotoName());
//	private static final String[] items = new String[] { "图库", "拍照" };
//	public static final String IMAGE_PATH = "Icar-photo";
//	private static String localTempImageFileName = "";
//	public static final File FILE_SDCARD = Environment
//			.getExternalStorageDirectory();
//	public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
//	public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
//			"images/former");
//	Thread thread;
//	ProgressDialog pd;
//	ArrayAdapter<String> teamAdapter = null;
//	Button icar_a_submit, icar_create_btn_show, icar_create_add1,
//			icar_create_add2, icar_create_add3, Release, Reject;
//	TextView icar_a_create, family, close, dsp1, tvapproval, asp4, bsp6, csp9;
//	Spinner priority, type, site, bu, mfg, sbu, problemtype, asp1, bsp1, dsp2,
//			dsp3, dsp4, dsp5, dsp6, dsp7, dsp8, dsp9, dsp10, dsp11, dsp12,
//			dsp13, dsp14, rejectto, seqno, dsp2_id, dsp3_id, dsp4_id, dsp5_id,
//			dsp6_id, dsp7_id, dsp8_id, dsp9_id, dsp10_id, dsp11_id, dsp12_id,
//			dsp13_id, dsp14_id;
//	EditText ed1, ed2, ed3, ed4, ed5, failed, line, asp2, asp3, bsp3, bsp4,
//			bsp5, csp1, csp2, csp3, csp4, csp5, csp6, csp7, csp8, leader,
//			remark;
//	GridView gridview;
//	String mIcar, mTitle, mPriority, mType, mChar // 必填值
//			, mSite, mBu, mMfg, mProblemType, mFailed;
//	String nPhoto, nSbu, nLine, nfa;
//	String Adata1, Adata2, Adata3, Adata4;
//	String Bdata1, Bdata2, Bdata3, Bdata4, Bdata5, Bdata6;
//	String Cdata1, Cdata2, Cdata3, Cdata4, Cdata5, Cdata6, Cdata7, Cdata8,
//			Cdata9;
//	String Ddata1 = "", Ddata2 = "", Ddata3 = "", Ddata4 = "", Ddata5 = "",
//			Ddata6 = "", Ddata7 = "", Ddata8 = "", Ddata9 = "", Ddata10 = "",
//			Ddata11 = "", Ddata12 = "", Ddata13 = "", Ddata14 = "",
//			Ddata2_2 = "", Ddata3_3 = "", Ddata4_4 = "", Ddata5_5 = "",
//			Ddata6_6 = "", Ddata7_7 = "", Ddata8_8 = "", Ddata9_9 = "",
//			Ddata10_10 = "", Ddata11_11 = "", Ddata12_12 = "", Ddata13_13 = "",
//			Ddata14_14 = "";
//	String Mpath = "";
//	String AllTeam[] = null, AllPriority[] = null, AllType[] = null,
//			AllSite[] = null, AllProblemType[] = null, AllTeamUser[] = null,
//			allApproval[] = null;
//	String[] siteItem = null, buItem = null;
//	String oldMfg = null, oldBu = null, oldSite = null, oldTeam = null,
//			approval = null, logonname = null;
//	String Icar = null, skuno = null, FamilyItem = null;
//	List skunoItem = null, AllSbu = null, AllMfg = null;
//	List<String> result = null;
//	int count1 = 0, count2 = 0, count3 = 0;
//
//	boolean result_ok = false;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.layout_icar_create);
//		pd = ProgressDialog
//				.show(IcarCreate.this,
//						(String) getBaseContext().getResources().getText(
//								R.string.wait),
//						(String) getBaseContext().getResources().getText(
//								R.string.loading), false, false);
//		// newThread();
//		ed1 = (EditText) findViewById(R.id.icar_create_ed1);
//
//		icar_a_create = (TextView) findViewById(R.id.icar_a_create);
//		icar_a_submit = (Button) findViewById(R.id.icar_a_submit);
//		icar_create_add1 = (Button) findViewById(R.id.icar_create_add1);
//		icar_create_add2 = (Button) findViewById(R.id.icar_create_add2);
//		icar_create_add3 = (Button) findViewById(R.id.icar_create_add3);
//
//		// ---step1内容
//		priority = (Spinner) findViewById(R.id.priority);
//		type = (Spinner) findViewById(R.id.type);
//		ed2 = (EditText) findViewById(R.id.icar_create_ed2);
//		ed2.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch (v.getId()) {  
//			    case R.id.icar_create_ed2:  
//			        v.getParent().requestDisallowInterceptTouchEvent(true);  
//			        switch (event.getAction() & MotionEvent.ACTION_MASK) {  
//			        case MotionEvent.ACTION_UP:  
//			            v.getParent().requestDisallowInterceptTouchEvent(false);  
//			            break;  
//			        }  
//			    } 
//				return false;
//			}
//		});
//		ed3 = (EditText) findViewById(R.id.icar_create_ed3);
//		gridview = (GridView) findViewById(R.id.icar_gridView);
//		icar_create_btn_show = (Button) findViewById(R.id.icar_create_btn_show);
//
//		// ---step2---part1内容
//		site = (Spinner) findViewById(R.id.icar_create_spinner1);
//
//		bu = (Spinner) findViewById(R.id.icar_create_spinner2);
//
//		mfg = (Spinner) findViewById(R.id.icar_create_spinner3);
//
//		sbu = (Spinner) findViewById(R.id.icar_create_spinner4);
//
//		problemtype = (Spinner) findViewById(R.id.icar_create_spinner5);
//
//		failed = (EditText) findViewById(R.id.icar_create_ed4);
//
//		line = (EditText) findViewById(R.id.icar_create_ed5);
//
//		// ---step2---part2
//		asp1 = (Spinner) findViewById(R.id.icar_create_spinner6);
//		asp2 = (EditText) findViewById(R.id.icar_create_ed6);
//		asp3 = (EditText) findViewById(R.id.icar_create_ed7);
//		asp4 = (TextView) findViewById(R.id.icar_create_ed8);
//		asp4.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				try {
//					String Failed11 = asp2.getText().toString();
//					String Tested11 = asp3.getText().toString();
//					System.out.println(Failed11 + "= " + Tested11);
//					if ((Failed11.length() == 0 || Failed11 == null)
//							|| (Tested11.length() == 0 || Tested11 == null)) {
//						UIHelper.ToastMessage(IcarCreate.this, "栏位不能为空");
//					} else {
//						String data = CountDPPM(Failed11, Tested11);
//						;
//						asp4.setText(data);
//						Adata4 = asp4.getText().toString();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		// ---step3---part3
//		bsp1 = (Spinner) findViewById(R.id.icar_create_spinner7);
//
//		family = (TextView) findViewById(R.id.icar_create_t18);
//		bsp3 = (EditText) findViewById(R.id.icar_create_ed9);
//		bsp4 = (EditText) findViewById(R.id.icar_create_ed10);
//		bsp5 = (EditText) findViewById(R.id.icar_create_ed11);
//		// bsp6 = (EditText) findViewById(R.id.icar_create_ed12);
//		bsp6 = (TextView) findViewById(R.id.icar_create_ed12);
//		bsp6.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				try {
//					String Product21 = bsp3.getText().toString();
//					String Failed21 = bsp5.getText().toString();
//					System.out.println(Product21 + "= " + Failed21);
//					if ((Product21.length() == 0 || Product21 == null)
//							|| (Failed21.length() == 0 || Failed21 == null)) {
//						UIHelper.ToastMessage(IcarCreate.this, "栏位不能为空");
//					} else {
//						String data = CountDPPM(Product21, Failed21);
//						;
//						bsp6.setText(data);
//						Bdata6 = bsp6.getText().toString();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		// ---step3---part4
//		csp1 = (EditText) findViewById(R.id.icar_create_ed13);
//		csp2 = (EditText) findViewById(R.id.icar_create_ed14);
//		csp3 = (EditText) findViewById(R.id.icar_create_ed15);
//		csp4 = (EditText) findViewById(R.id.icar_create_ed16);
//		csp5 = (EditText) findViewById(R.id.icar_create_ed17);
//		csp6 = (EditText) findViewById(R.id.icar_create_ed18);
//		csp7 = (EditText) findViewById(R.id.icar_create_ed19);
//		csp8 = (EditText) findViewById(R.id.icar_create_ed20);
//		// csp9 = (EditText) findViewById(R.id.icar_create_ed21);
//		csp9 = (TextView) findViewById(R.id.icar_create_ed21);
//		csp9.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				try {
//					String Product31 = csp7.getText().toString();
//					String Failed31 = csp8.getText().toString();
//					System.out.println(Product31 + "= " + Failed31);
//					if ((Product31.length() == 0 || Product31 == null)
//							|| (Failed31.length() == 0 || Failed31 == null)) {
//						UIHelper.ToastMessage(IcarCreate.this, "栏位不能为空");
//					} else {
//						String data = CountDPPM(Product31, Failed31);
//						;
//						csp9.setText(data);
//						Cdata9 = csp9.getText().toString();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//		});
//		// ---step4---创建人
//		dsp1 = (TextView) findViewById(R.id.icar_create_ed22);
//		// oldTeam = perSon.getTeam().toString();
//		// dsp1.setText(oldTeam + " " + perSon.getLogonName().toString());
//
//		// 围堵部门
//		dsp2 = (Spinner) findViewById(R.id.icar_create_spinner8);
//		dsp2_id = (Spinner) findViewById(R.id.icar_create_spinner8_8);
//		Ddata2 = dsp2.getSelectedItem().toString();
//		dsp3 = (Spinner) findViewById(R.id.icar_create_spinner9);
//		dsp3_id = (Spinner) findViewById(R.id.icar_create_spinner9_9);
//
//		dsp6 = (Spinner) findViewById(R.id.icar_create_spinner12);
//		dsp6_id = (Spinner) findViewById(R.id.icar_create_spinner12_12);
//		dsp6.setEnabled(false);
//		dsp6_id.setEnabled(false);
//		dsp9 = (Spinner) findViewById(R.id.icar_create_spinner15);
//		dsp9_id = (Spinner) findViewById(R.id.icar_create_spinner15_15);
//		dsp9.setEnabled(false);
//		dsp9_id.setEnabled(false);
//		dsp12 = (Spinner) findViewById(R.id.icar_create_spinner18);
//		dsp12_id = (Spinner) findViewById(R.id.icar_create_spinner18_18);
//		dsp12.setEnabled(false);
//		dsp12_id.setEnabled(false);
//
//		dsp4 = (Spinner) findViewById(R.id.icar_create_spinner10);
//		dsp4_id = (Spinner) findViewById(R.id.icar_create_spinner10_10);
//		dsp7 = (Spinner) findViewById(R.id.icar_create_spinner13);
//		dsp7_id = (Spinner) findViewById(R.id.icar_create_spinner13_13);
//		dsp7.setEnabled(false);
//		dsp7_id.setEnabled(false);
//		dsp10 = (Spinner) findViewById(R.id.icar_create_spinner16);
//		dsp10_id = (Spinner) findViewById(R.id.icar_create_spinner16_16);
//		dsp10.setEnabled(false);
//		dsp10_id.setEnabled(false);
//		dsp13 = (Spinner) findViewById(R.id.icar_create_spinner19);
//		dsp13_id = (Spinner) findViewById(R.id.icar_create_spinner19_19);
//		dsp13.setEnabled(false);
//		dsp13_id.setEnabled(false);
//
//		dsp5 = (Spinner) findViewById(R.id.icar_create_spinner11);
//		dsp5_id = (Spinner) findViewById(R.id.icar_create_spinner11_11);
//		dsp8 = (Spinner) findViewById(R.id.icar_create_spinner14);
//		dsp8_id = (Spinner) findViewById(R.id.icar_create_spinner14_14);
//		dsp8.setEnabled(false);
//		dsp8_id.setEnabled(false);
//		dsp11 = (Spinner) findViewById(R.id.icar_create_spinner17);
//		dsp11_id = (Spinner) findViewById(R.id.icar_create_spinner17_17);
//		dsp11.setEnabled(false);
//		dsp11_id.setEnabled(false);
//		dsp14 = (Spinner) findViewById(R.id.icar_create_spinner20);
//		dsp14_id = (Spinner) findViewById(R.id.icar_create_spinner20_20);
//		dsp14.setEnabled(false);
//		dsp14_id.setEnabled(false);
//		tvapproval = (TextView) findViewById(R.id.icar_create_t48);
//
//		Release = (Button) findViewById(R.id.CarNo_Release);
//		Reject = (Button) findViewById(R.id.CarNo_Reject);
//		rejectto = (Spinner) findViewById(R.id.icar_create_spinner21);
//		rejectto = (Spinner) findViewById(R.id.icar_create_spinner22);
//		leader = (EditText) findViewById(R.id.icar_create_ed23);
//		remark = (EditText) findViewById(R.id.icar_create_ed24);
//
//		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		gridviewInit();
//		mfg.setOnItemSelectedListener(new select(1));
//		dsp2.setOnItemSelectedListener(new select(2));
//		dsp3.setOnItemSelectedListener(new select(3));
//		dsp4.setOnItemSelectedListener(new select(4));
//		dsp5.setOnItemSelectedListener(new select(5));
//		// dsp6.setOnItemSelectedListener(new select(6));
//
//		// dsp7.setOnItemSelectedListener(new select(7));
//		// dsp8.setOnItemSelectedListener(new select(8));
//		// dsp9.setOnItemSelectedListener(new select(9));
//		// dsp10.setOnItemSelectedListener(new select(10));
//
//		// dsp11.setOnItemSelectedListener(new select(11));
//		// dsp12.setOnItemSelectedListener(new select(12));
//		// dsp13.setOnItemSelectedListener(new select(13));
//		// dsp14.setOnItemSelectedListener(new select(14));
//
//		bsp1.setOnItemSelectedListener(new select(15));
//		sbu.setOnItemSelectedListener(new select(16));
//		icar_a_submit.setOnClickListener(new MyOnClick(0));
//		icar_create_btn_show.setOnClickListener(new MyOnClick(1));
//		icar_create_add1.setOnClickListener(new MyOnClick(2));
//		icar_create_add2.setOnClickListener(new MyOnClick(3));
//		icar_create_add3.setOnClickListener(new MyOnClick(4));
//		
//		/**
//		 * getLogonName
//		 * getMfg
//		 * getBu
//		 * getSite
//		 * getTeam
//		 */
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				Message msg = new Message();
//				perSon = (PerSon) getApplicationContext();
//				if (perSon.getLogonName().toString().equals("")) {
//					UIHelper.ToastMessage(IcarCreate.this,
//							R.string.toast_again_error);
//					pd.dismiss();
//				} else {
//					System.out.println("I am here");
//					isRead = 1;
//					oldMfg = perSon.getMfg().toString();
//					oldBu = perSon.getBu().toString();
//					oldSite = perSon.getSite().toString();
//					logonname = perSon.getLogonName().toString();
//					try {
////						AllMfg = userService.getAllMfg(oldBu);
////						allApproval = userService.getApproval(logonname); // 获取最高审核人
//						AllMfg = sampleservice.getAllMfg(oldBu);
//						allApproval = sampleservice.getApprovals(logonname);
////						Log.i("", "名字="+logonname);
////						for (int i = 0; i < allApproval.length; i++) {
////							Log.i("", "资料="+allApproval[i]);	
////						}
//						
//						System.out.println(AllMfg.size() + ">>>>>>>");
//						if (AllMfg.size() > 0) {
//							msg.what = 1;
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					handler1.sendMessage(msg);
//					pd.dismiss();
//				}
//			}
//		}).start();
//		System.out.println("I am here1=" + isRead);
//		
//		
//		MyApplication.getInstance().addActivity(this);
//	}
//
//	private Handler handler1 = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 1:
//				System.out.println("I am here2=" + isRead);
//				siteItem = new String[] { oldSite };
//				@SuppressWarnings({ "unchecked", "rawtypes" })
//				ArrayAdapter siteAdapter = new ArrayAdapter(IcarCreate.this,
//						android.R.layout.simple_spinner_item, siteItem);
//				site.setAdapter(siteAdapter);
//
//				buItem = new String[] { oldBu };
//				@SuppressWarnings({ "unchecked", "rawtypes" })
//				ArrayAdapter buAdapter = new ArrayAdapter(IcarCreate.this,
//						android.R.layout.simple_spinner_item, buItem);
//				bu.setAdapter(buAdapter);
//
//				// AllMfg = userService.getAllMfg(oldBu);
//				if (AllMfg.get(0).toString().equals("fail")) {
//					Toast.makeText(IcarCreate.this,
//							R.string.toast_network_error, Toast.LENGTH_SHORT)
//							.show();
//				} else if (AllMfg == null) {
//					Toast.makeText(IcarCreate.this, "数据加载失败",
//							Toast.LENGTH_SHORT).show();
//				} else {
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter mfgAdapter = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllMfg);
//					mfg.setAdapter(mfgAdapter);
//					for (int i = 0; i < AllMfg.size(); i++) {
//						if (oldMfg.equals(AllMfg.get(i))) {
//							mfg.setSelection(i);
//							mMfg = oldMfg;
//							break; // 默认值为当前mfg
//						} else {
//							mfg.setSelection(0);
//						}
//					}
//				}
//				/**
//				 * oldMfg--创建icar的用户本身的mfg
//				 */
//				if (allApproval[0].equals("fail")) {
//					UIHelper.ToastMessage(IcarCreate.this,
//							R.string.toast_network_error);
//				} else {
//					approval = allApproval[0] + " | " + allApproval[2];
//					tvapproval.setText(approval);
//				}
//
//				oldTeam = perSon.getTeam().toString();
//				dsp1.setText(oldTeam + " " + perSon.getLogonName().toString());
//				break;
//
//			default:
//				break;
//			}
//		}
//	};
//
//	public String CountDPPM(String failqty, String totalqty) {
//		String dppm = "";
//		Integer data1 = Integer.parseInt(failqty) * 1000000;
//		Integer data2 = Integer.parseInt(totalqty);
//		Integer data = (data1 / data2);
//		dppm = data.toString();
//		return dppm;
//	}
//
//
//	public void getServieData() {
//		new Thread() {
//			public void run() {
//				Looper.prepare();
//				try {
//
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			}
//		}.start();
//
//	}
//
//	public class MyOnClick implements OnClickListener {
//		private int index = 0;
//		Intent intent = null;
//
//		public MyOnClick(int i) {
//			index = i;
//		}
//
//		@Override
//		public void onClick(View v) {
//			switch (index) {
//			case 0:// 提交按钮
//				mTitle = ed1.getText().toString(); // mTitle
//				mPriority = priority.getSelectedItem().toString(); // mPriority
//				mType = type.getSelectedItem().toString(); // mType
//				mChar = ed2.getText().toString(); // mChar
//				mSite = site.getSelectedItem().toString(); // site
//				mBu = bu.getSelectedItem().toString(); // mBu
//				mMfg = mfg.getSelectedItem().toString();
//				mProblemType = problemtype.getSelectedItem().toString(); // mProblemType
//				mFailed = failed.getText().toString(); // mFailed
//				nfa = ed3.getText().toString();
//				nSbu = sbu.getSelectedItem().toString();
//				nLine = line.getText().toString();
//				Adata1 = asp1.getSelectedItem().toString();
//				Adata2 = asp2.getText().toString();
//				Adata3 = asp3.getText().toString();
//				Adata4 = asp4.getText().toString();
//				// ---step3---part3
//				Bdata1 = bsp1.getSelectedItem().toString();
//				Bdata2 = family.getText().toString();
//				Bdata3 = bsp3.getText().toString();
//				Bdata4 = bsp4.getText().toString();
//				Bdata5 = bsp5.getText().toString();
//				Bdata6 = bsp6.getText().toString();
//				// ---step3---part4
//				Cdata1 = csp1.getText().toString();
//				Cdata2 = csp2.getText().toString();
//				Cdata3 = csp3.getText().toString();
//				Cdata4 = csp4.getText().toString();
//				Cdata5 = csp5.getText().toString();
//				Cdata6 = csp6.getText().toString();
//				Cdata7 = csp7.getText().toString();
//				Cdata8 = csp8.getText().toString();
//				Cdata9 = csp9.getText().toString();
//				Ddata1 = dsp1.getText().toString(); // 创建人
//
//				// Ddata2=dsp2.getSelectedItem().toString(); //围堵部门
//				// Ddata2_2=dsp2_id.getSelectedItem().toString();
//
//				if (StringUtils.isEmpty(mTitle)
//						|| StringUtils.isEmpty(mPriority)
//						|| StringUtils.isEmpty(mType)
//						|| StringUtils.isEmpty(mChar)
//						|| StringUtils.isEmpty(mSite)
//						|| StringUtils.isEmpty(mBu)
//						|| StringUtils.isEmpty(mMfg)
//						|| StringUtils.isEmpty(mProblemType)
//						|| StringUtils.isEmpty(mFailed)) {
//					UIHelper.ToastMessage(IcarCreate.this,
//							R.string.toast_must);
//				} else if (StringUtils.isEmpty(Ddata2)
//						|| StringUtils.isEmpty(Ddata3)) {
//					UIHelper.ToastMessage(IcarCreate.this,
//							R.string.toast_must_stifled);
//				} else if (!(StringUtils.isEmpty(Ddata4) || StringUtils
//						.isEmpty(Ddata5))) {
//					if (StringUtils.isEmpty(Ddata3)) {
//						UIHelper.ToastMessage(IcarCreate.this,
//								R.string.toast_must_Investigation1);
//					}
//				} else {
//					pd = ProgressDialog.show(
//							IcarCreate.this,
//							(String) getBaseContext().getResources().getText(
//									R.string.wait), (String) getBaseContext()
//									.getResources().getText(R.string.saving),
//							false, false);
//					if (drr.size() > 0) {
//						for (int i = 0; i < drr.size(); i++) {
//							String path1 = drr
//									.get(i)
//									.toString()
//									.substring(
//											drr.get(i).toString()
//													.lastIndexOf("/") + 1);
//							Mpath += path1;
//							uploadFile(drr.get(i).toString());
//						}
//						UIHelper.ToastMessage(IcarCreate.this, Mpath);
//					}
//					submit();
//				}
//				break;
//			case 1: // 拍照按钮
//				CheckPhoto();
//				break;
//			case 2:
//				Toast.makeText(IcarCreate.this, "暂无此功能", Toast.LENGTH_SHORT)
//						.show();
//				break;
//			case 3:
//				Toast.makeText(IcarCreate.this, "暂无此功能", Toast.LENGTH_SHORT)
//						.show();
//				break;
//			case 4:
//				Toast.makeText(IcarCreate.this, "暂无此功能", Toast.LENGTH_SHORT)
//						.show();
//				break;
//			default:
//				break;
//			}
//		}
//
//		/**
//		 * 創建Icar同時提交
//		 */
//
//	}
//
//	private void MyThread(final int getId, final String item) {
//		final Handler handler = new Handler() {
//			@SuppressLint("NewApi")
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case 1:
//					if (item.equals("")) {
//						@SuppressWarnings("rawtypes")
//						ArrayAdapter sbuAdapter = new ArrayAdapter(
//								IcarCreate.this,
//								android.R.layout.simple_spinner_item);
//						sbu.setAdapter(sbuAdapter);
//						@SuppressWarnings("rawtypes")
//						ArrayAdapter skunoAdapter = new ArrayAdapter(
//								IcarCreate.this,
//								android.R.layout.simple_spinner_item);
//						bsp1.setAdapter(skunoAdapter);
//						family.setText("");
//					} else {
//						System.out
//								.println("why me as  here>>>>>>>>>>>>>>>>>>>>>>");
//
//						AllSbu = sampleservice.getAllSub(item);
//						if (AllSbu.get(0).equals("fail")) {
//							UIHelper.ToastMessage(IcarCreate.this,
//									R.string.toast_network_error);
//						} else {
//							@SuppressWarnings({ "rawtypes", "unchecked" })
//							ArrayAdapter sbuAdapter = new ArrayAdapter(
//									IcarCreate.this,
//									android.R.layout.simple_spinner_item,
//									AllSbu);
//							sbu.setAdapter(sbuAdapter);
//							// sbu.setOnItemSelectedListener(new select(16));
//						}
//						skunoItem = sampleservice.getSkuNo(item);
//						for(int i=0;i<skunoItem.size();i++){
//							Log.i("tagicar2", "msg"+skunoItem.get(i).toString());
//						}
//						if (skunoItem.get(0).equals("fail")
//								|| skunoItem.size() == 0) {
//							UIHelper.ToastMessage(IcarCreate.this,
//									R.string.toast_network_error);
//						} else {
//							@SuppressWarnings({ "rawtypes", "unchecked" })
//							ArrayAdapter skunoAdapter = new ArrayAdapter(
//									IcarCreate.this,
//									android.R.layout.simple_spinner_item,
//									skunoItem);
//							bsp1.setAdapter(skunoAdapter);
//							// bsp1.setOnItemSelectedListener(new select(15));
//						}
//					}
//					break;
//				case 2:
//					dsp2_id.setVisibility(View.VISIBLE);
//					for (int i = 0; i < AllTeamUser.length; i++) {
//						System.out.println("结果："+AllTeamUser[i]);
//					}
//					
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter userAdapter2 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					
//					
//					
//					dsp2_id.setAdapter(userAdapter2);
//					//Ddata2_2 = dsp2_id.getSelectedItem().toString();
//					break;
//				case 3:
//					dsp3_id.setVisibility(View.VISIBLE);
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					
//					ArrayAdapter userAdapter3 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					for(int i=0;i<AllTeamUser.length;i++){
//						Log.i("tagicar", "msg"+AllTeamUser[i].toString());
//					}
//					dsp3_id.setAdapter(userAdapter3);
//				//	Ddata3_3 = dsp3_id.getSelectedItem().toString();
//					break;
//				case 4:
//					dsp4_id.setVisibility(View.VISIBLE);
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter userAdapter4 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					dsp4_id.setAdapter(userAdapter4);
//					//Ddata4_4 = dsp4_id.getSelectedItem().toString();
//					break;
//				case 5:
//					dsp5_id.setVisibility(View.VISIBLE);
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter userAdapter5 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					dsp5_id.setAdapter(userAdapter5);
//				//	Ddata5_5 = dsp5_id.getSelectedItem().toString();
//					break;
//				case 6:
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter userAdapter6 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					dsp6_id.setAdapter(userAdapter6);
//					break;
//				case 7:
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter userAdapter7 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					dsp7_id.setAdapter(userAdapter7);
//					break;
//				case 8:
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter userAdapter8 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					dsp8_id.setAdapter(userAdapter8);
//					break;
//				case 9:
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter userAdapter9 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					dsp9_id.setAdapter(userAdapter9);
//					break;
//				case 10:
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter userAdapter10 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					dsp10_id.setAdapter(userAdapter10);
//					break;
//				case 11:
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter userAdapter11 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					dsp11_id.setAdapter(userAdapter11);
//					break;
//				case 12:
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter userAdapter12 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					dsp12_id.setAdapter(userAdapter12);
//					break;
//				case 13:
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter userAdapter13 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					dsp13_id.setAdapter(userAdapter13);
//					break;
//				case 14:
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					ArrayAdapter userAdapter14 = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, AllTeamUser);
//					dsp14_id.setAdapter(userAdapter14);
//					break;
//				case 15:
//					family.setText(FamilyItem);
//					break;
//				case 16:
//					@SuppressWarnings({ "rawtypes", "unchecked" })
//					ArrayAdapter skunoAdapter = new ArrayAdapter(
//							IcarCreate.this,
//							android.R.layout.simple_spinner_item, skunoItem);
//					bsp1.setAdapter(skunoAdapter);
//					break;
//				case 22:
//					dsp2_id.setVisibility(View.INVISIBLE);
//					Toast.makeText(IcarCreate.this, R.string.no_person,
//							Toast.LENGTH_SHORT).show();
//					break;
//				case 33:
//					dsp3_id.setVisibility(View.INVISIBLE);
//					Toast.makeText(IcarCreate.this, R.string.no_person,
//							Toast.LENGTH_SHORT).show();
//					break;
//				case 44:
//					dsp4_id.setVisibility(View.INVISIBLE);
//					Toast.makeText(IcarCreate.this, R.string.no_person,
//							Toast.LENGTH_SHORT).show();
//					break;
//				case 55:
//					dsp5_id.setVisibility(View.INVISIBLE);
//					Toast.makeText(IcarCreate.this, R.string.no_person,
//							Toast.LENGTH_SHORT).show();
//				case 88:
//					Toast.makeText(IcarCreate.this, R.string.no_person,
//							Toast.LENGTH_SHORT).show();
//					break;
//				default:
//					Toast.makeText(IcarCreate.this, R.string.error,
//							Toast.LENGTH_SHORT).show();
//					break;
//				}
//			}
//		};
//		new Thread() {
//			Message msg = new Message();
//
//			public void run() {
//				try {
//					switch (getId) {
//					case 1:
//						msg.what = 1;
//						break;
//					case 2:
//						if (oldMfg.equals(mMfg)) {
//							Log.i("tagicar1", "msg"+">>>>>"+oldSite+">>>>>"+oldMfg+">>>>>"+mMfg+">>>>>"+item+">>>>>");
//							AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//							
//						} else {
//							AllTeamUser = sampleservice.getTeamUsers(oldSite,mMfg, item);                                                                                                                  
//						}
//						if (AllTeamUser.length == 0) {
//							Ddata3_3 = "";
//							msg.what = 22;
//						} else {
//							msg.what = 2;
//						}
//						break;
//					case 3:
//						if (oldMfg.equals(mMfg)) {
//							AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//						} else {
////							AllTeamUser = userService.getTeamUser(oldSite,mMfg, item);
//							AllTeamUser = sampleservice.getTeamUsers(oldSite,mMfg, item);
//						}
//
//						if (AllTeamUser.length == 0) {
//							msg.what = 33;
//						} else {
//							msg.what = 3;
//						}
//						break;
//					case 4:
//						if (oldMfg.equals(mMfg)) {
////							AllTeamUser = userService.getTeamUser(oldSite,oldMfg, item);
//							AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//						} else {
////							AllTeamUser = userService.getTeamUser(oldSite,mMfg, item);
//							AllTeamUser = sampleservice.getTeamUsers(oldSite,mMfg, item);
//						}
//						if (AllTeamUser.length == 0) {
//							msg.what = 44;
//						} else {
//							msg.what = 4;
//						}
//						break;
//					case 5:
//						if (oldMfg.equals(mMfg)) {
////							AllTeamUser = userService.getTeamUser(oldSite,oldMfg, item);
//							AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//						} else {
////							AllTeamUser = userService.getTeamUser(oldSite,mMfg, item);
//							AllTeamUser = sampleservice.getTeamUsers(oldSite,mMfg, item);
//						}
//						if (AllTeamUser.length == 0) {
//							msg.what = 55;
//						} else {
//							msg.what = 5;
//						}
//						break;
//					case 6:
////						AllTeamUser = userService.getTeamUser(oldSite, oldMfg,item);
//						AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//						if (AllTeamUser.length == 0) {
//							msg.what = 88;
//							dsp6_id.setVisibility(View.INVISIBLE);
//						} else {
//							msg.what = 6;
//							dsp6_id.setVisibility(View.VISIBLE);
//						}
//						break;
//					case 7:
////						AllTeamUser = userService.getTeamUser(oldSite, oldMfg,item);
//						AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//						if (AllTeamUser.length == 0) {
//							msg.what = 88;
//							dsp7_id.setVisibility(View.INVISIBLE);
//						} else {
//							msg.what = 7;
//							dsp7_id.setVisibility(View.VISIBLE);
//						}
//						break;
//					case 8:
////						AllTeamUser = userService.getTeamUser(oldSite, oldMfg,item);
//						AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//						if (AllTeamUser.length == 0) {
//							msg.what = 88;
//							dsp8_id.setVisibility(View.INVISIBLE);
//						} else {
//							msg.what = 8;
//							dsp8_id.setVisibility(View.VISIBLE);
//						}
//						break;
//					case 9:
////						AllTeamUser = userService.getTeamUser(oldSite, oldMfg,item);
//						AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//						if (AllTeamUser.length == 0) {
//							msg.what = 88;
//							dsp9_id.setVisibility(View.INVISIBLE);
//						} else {
//							msg.what = 9;
//							dsp9_id.setVisibility(View.VISIBLE);
//						}
//						break;
//					case 10:
////						AllTeamUser = userService.getTeamUser(oldSite, oldMfg,item);
//						AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//						if (AllTeamUser.length == 0) {
//							msg.what = 88;
//							dsp10_id.setVisibility(View.INVISIBLE);
//						} else {
//							msg.what = 10;
//							dsp10_id.setVisibility(View.VISIBLE);
//						}
//						break;
//					case 11:
////						AllTeamUser = userService.getTeamUser(oldSite, oldMfg,item);
//						AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//						if (AllTeamUser.length == 0) {
//							msg.what = 88;
//							dsp11_id.setVisibility(View.INVISIBLE);
//						} else {
//							msg.what = 11;
//							dsp11_id.setVisibility(View.VISIBLE);
//						}
//						break;
//					case 12:
////						AllTeamUser = userService.getTeamUser(oldSite, oldMfg,item);
//						AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//						if (AllTeamUser.length == 0) {
//							msg.what = 88;
//							dsp12_id.setVisibility(View.INVISIBLE);
//						} else {
//							msg.what = 12;
//							dsp12_id.setVisibility(View.VISIBLE);
//						}
//						break;
//					case 13:
////						AllTeamUser = userService.getTeamUser(oldSite, oldMfg,item);
//						AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//						if (AllTeamUser.length == 0) {
//							msg.what = 88;
//							dsp13_id.setVisibility(View.INVISIBLE);
//						} else {
//							msg.what = 13;
//							dsp13_id.setVisibility(View.VISIBLE);
//						}
//						break;
//					case 14:
////						AllTeamUser = userService.getTeamUser(oldSite, oldMfg,item);
//						AllTeamUser = sampleservice.getTeamUsers(oldSite,oldMfg, item);
//						if (AllTeamUser.length == 0) {
//							msg.what = 88;
//							dsp14_id.setVisibility(View.INVISIBLE);
//						} else {
//							msg.what = 14;
//							dsp14_id.setVisibility(View.VISIBLE);
//						}
//						break;
//					case 15:
//						if (mMfg.equals("MFGV") && nSbu.equals("CHSBU")) {
////							FamilyItem = userService.getFamilyItem(nSbu, item);
//							FamilyItem = sampleservice.getFamily(nSbu, item).get(0).toString();
//						} else {
////							FamilyItem = userService.getFamilyItem(mMfg, item);
//							FamilyItem = sampleservice.getFamily(mMfg, item).get(0).toString();
//						}
//						System.out.println(mMfg + "mMfg  >>>>>>>>>>>>>");
//						if (FamilyItem.length() == 0
//								|| FamilyItem.equals("fail")) {
//							family.setText("");
//						} else {
//							msg.what = 15;
//						}
//						break;
//					case 16:
////						skunoItem = userService.getSkuNoItem(item);
//						skunoItem = sampleservice.getSkuNo(item);
//						if (skunoItem.get(0).equals("fail")) {
//							family.setText("");
//						} else {
//							msg.what = 16;
//						}
//						break;
//					default:
//						Toast.makeText(IcarCreate.this, "数据加载失败",
//								Toast.LENGTH_SHORT).show();
//						break;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					System.out.println(e);
//				}
//				handler.sendMessage(msg);
//			}
//		}.start();
//	}
//
//	public void submit() {
//		// getValue();
//
//		final Handler handler = new Handler() {
//			public void handleMessage(Message msg) {
//				if (msg.what == 0) {
//					pd.dismiss();
//					UIHelper.ToastMessage(IcarCreate.this,
//							getString(R.string.toast_network_error));
//				} else if (msg.what == 2) {
//					pd.dismiss();
//					UIHelper.ToastMessage(IcarCreate.this,
//							getString(R.string.toast_network_Exception));
//				} else if (msg.what == 1) // 提交内容
//				{
//					icar_a_create.setText(mIcar);
//					// status 0:open,1:close
//					Ddata2_2 = dsp2_id.getSelectedItem().toString();
//					String Containmentteam = Ddata2 + "|" + Ddata2_2; // 围堵部门
//					result = new ArrayList<String>();
//					int check = 0;
//					String Investigationteam1 = "", Investigationteam2 = "", Investigationteam3 = "";
//					if (!(Ddata3.toString().equals(""))) {
//						Ddata3_3 = dsp3_id.getSelectedItem().toString();
//						if (Ddata3_3.equals("")) {
//							Investigationteam1 = Ddata3;
//						} else {
//							Investigationteam1 = Ddata3 + "|" + Ddata3_3; // 调查部门1
//						}
//						count1 = 1;
//					}
//					if (!(Ddata4.toString().equals(""))) {
//						Ddata4_4 = dsp4_id.getSelectedItem().toString();
//						if (Ddata4_4.equals("")) {
//							Investigationteam2 = Ddata4; // 调查部门2
//						} else {
//							Investigationteam2 = Ddata4 + "|" + Ddata4_4; // 调查部门2
//						}
//						count2 = 1;
//					}
//					if (!(Ddata5.toString().equals(""))) {
//						Ddata5_5 = dsp5_id.getSelectedItem().toString();
//						if (Ddata5_5.equals("")) {
//							Investigationteam3 = Ddata5;
//						} else {
//							Investigationteam3 = Ddata5 + "|" + Ddata5_5; // 调查部门3
//						}
//						count3 = 1;
//					}
//					String data1 = iCarinfo.setMfiCarStatus(mIcar, mTitle,
//							mType, mPriority, mChar, nfa, Mpath, 0, logonname);
//
//					String data2 = iCarinfo.setMfiCarDetail(mIcar, "1", "1",
//							mSite, mBu, mMfg, nSbu, mProblemType, mFailed,
//							nLine, "", logonname).toString();
//					String data3 = iCarinfo.setMfiCarDetail(mIcar, "2", "1",
//							Adata1, Adata2, Adata3, Adata4, "", "", "", "",
//							logonname).toString();
//					String data4 = iCarinfo.setMfiCarDetail(mIcar, "3", "1",
//							Bdata2, Bdata1, Bdata3, Bdata4, Bdata5, Bdata6, "",
//							"", logonname);
//					String data5 = iCarinfo.setMfiCarDetail(mIcar, "4", "1",
//							Cdata1, Cdata2, Cdata3, Cdata4, Cdata5, Cdata6,
//							Cdata7, Cdata8, logonname).toString();
//
//					String data6 = iCarinfo.setMfiCarAction(mIcar, "01",
//							"1", "0", oldTeam, "1", "", "", "", "", logonname);
//					String data7 = iCarinfo.setMfiCarAction(mIcar, "02",
//							"1", "0", Containmentteam, "1", "", "", "", "",
//							logonname);
//					result.add(data1);
//					result.add(data2);
//					result.add(data3);
//					result.add(data4);
//					result.add(data5);
//					result.add(data6);
//					result.add(data7);
//					if (count1 == 1) {
//						String data8 = iCarinfo.setMfiCarAction(mIcar, "03",
//								"1", "0", Investigationteam1, "1", "", "", "",
//								"", logonname);
//						result.add(data8);
//					}
//					if (count2 == 1) {
//						String data9 = iCarinfo.setMfiCarAction(mIcar, "03",
//								"2", "0", Investigationteam2, "1", "", "", "",
//								"", logonname);
//						result.add(data9);
//					}
//					if (count3 == 1) {
//						String data10 = iCarinfo.setMfiCarAction(mIcar,
//								"03", "3", "0", Investigationteam3, "1", "",
//								"", "", "", logonname);
//						result.add(data10);
//					}
//
//					String data11 = iCarinfo.setMfiCarAction(mIcar, "07",
//							"1", "0", allApproval[0], "1", "", "", "", "",
//							logonname);
//					result.add(data11);
//					String data12 = iCarinfo.setMfiCarActionRelease(mIcar,
//							"01", "1", "0", "0", "", "", logonname);
//					result.add(data12);
//					for (int i = 0; i < result.size(); i++) {
//						System.out.println(result.get(i)
//								+ ">>>>>>>>>>>>>>>++++");
//						if (result.get(i).equals("fail")
//								|| result.get(i).equals("0")) {
//							Toast.makeText(IcarCreate.this,
//									result.get(i) + "数据插入失败",
//									Toast.LENGTH_SHORT).show();
//							check = 1;
//							break;
//						}
//					}
//					if (check == 0) {
//						result_ok = true;
//						pd.dismiss();
//						icar_a_submit.setEnabled(false);
//						UIHelper.ToastMessage(IcarCreate.this, mIcar
//								+ "数据创建成功 \n 邮件已成功发送至您的主管");
//					}
//				}
//
//			}
//		};
//		new Thread() {
//			public void run() {
//				Message msg = new Message();
//				Looper.prepare();
//				try {
//					mIcar = iCarinfo.getIcar();
//					System.out.println();
//					if (mIcar.equals("fail")) {
//						msg.what = 0;
//					} else {
//						msg.what = 1;
//					}
//				} catch (Exception e) {
//					msg.what = 2;
//				}
//				System.out.println("msg=" + msg + ">>>>>>>>>>>>>>>");
//				handler.sendMessage(msg);
//			}
//		}.start();
//
//	}
//
//	public void uploadFile(String sb) {
//		Socket socket = null;
//		DataOutputStream dos = null;
//		FileInputStream fis;
//		try {
//			File file = new File(sb);
//			if (file.length() == 0) {
//				return;
//			} else {
//			//	socket = new Socket("10.167.4.131", 8889);
//				socket = new Socket("10.167.4.199", 8899);
//				dos = new DataOutputStream(socket.getOutputStream());
//				fis = new FileInputStream(file);
//				dos.writeUTF(sb.toString().substring(
//						sb.toString().lastIndexOf("/") + 1));// 截取图片名称
//				dos.flush();
//				int length;
//				byte[] buffer = new byte[8192];
//				while ((length = fis.read(buffer, 0, buffer.length)) > 0) {
//					dos.write(buffer, 0, length);
//					dos.flush();
//				}
//				dos.close();
//				fis.close();
//				socket.close();
//			}
//		} catch (UnknownHostException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//
//	}
//
//	public void gridviewInit() {
//		// 添加适配器
//		GridAdapter adapter = new GridAdapter(this);
//		adapter.setSelectId(0);
//		int size = 0;
//		if (bmp.size() < 6) {
//			size = bmp.size() + 1;
//		} else {
//			size = bmp.size();
//		}
//		GridView gridview = (GridView) findViewById(R.id.icar_gridView);
//		LayoutParams params = gridview.getLayoutParams();
//		final int width = size * (int) (25 * 9.4f);
//		params.width = width;
//		gridview.setLayoutParams(params);
//		gridview.setColumnWidth((int) (25 * 9.4f));
//		gridview.setStretchMode(GridView.NO_STRETCH);
//		gridview.setNumColumns(size);
//		gridview.setAdapter(adapter);
//		gridview.setOnItemClickListener(this);
//
//		final HorizontalScrollView hsvSelectImg = (HorizontalScrollView) findViewById(R.id.icar_horizon);
//		hsvSelectImg.getViewTreeObserver().addOnPreDrawListener(
//				new OnPreDrawListener() {
//					public boolean onPreDraw() {
//						hsvSelectImg.scrollTo(width, 0);
//						hsvSelectImg.getViewTreeObserver()
//								.removeOnPreDrawListener(this);
//						return false;
//					}
//				});
//	}
//
//	/**
//	 * 创建显示图片的适配器
//	 * 
//	 * @author Administrator
//	 * 
//	 */
//	public class GridAdapter extends BaseAdapter {
//		private LayoutInflater inflater;
//		private int selectId = -1;
//		private boolean shape;
//
//		public boolean isShape() {
//			return shape;
//		}
//
//		public void setShape(boolean shape) {
//			this.shape = shape;
//		}
//
//		public class ViewHolder {
//			public ImageView image;
//			public Button btButton;
//		}
//
//		public GridAdapter(Context context) {
//			inflater = LayoutInflater.from(context);
//		}
//
//		@Override
//		public int getCount() {
//			if (bmp.size() < 6) {
//				return bmp.size() + 1;
//			} else {
//				return bmp.size();
//			}
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		public void setSelectId(int position) {
//			selectId = position;
//		}
//
//		public int getSelectId() {
//			return selectId;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			final int id = position;
//			ViewHolder holder = null;
//			if (convertView == null) {
//				holder = new ViewHolder();
//				convertView = inflater.inflate(R.layout.layout_girdview_item,
//						null);
//				holder.image = (ImageView) convertView
//						.findViewById(R.id.item_show_image);
//				holder.btButton = (Button) convertView
//						.findViewById(R.id.item_grida_bt);
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			if (position == bmp.size()) {
//				holder.image.setImageBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.ic_launcher));
//				holder.btButton.setVisibility(View.GONE);
//				if (position == 6) {
//					holder.image.setVisibility(View.GONE);
//				}
//			} else
//				holder.image.setImageBitmap(bmp.get(position));
//			holder.btButton.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					PhotoActivity.bitmap.remove(id);
//					bmp.get(id).recycle();
//					bmp.remove(id);
//					drr.remove(id);
//					gridviewInit();
//				}
//			});
//			return convertView;
//		}
//
//	}
//
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//				.hideSoftInputFromWindow(IcarCreate.this.getCurrentFocus()
//						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//		if (arg2 == bmp.size()) {
//			String sdcardState = Environment.getExternalStorageState();
//			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
//				GridView gridview = (GridView) findViewById(R.id.icar_gridView);
//				// new PopupWindows(IcarCreate01.this, gridview);
//			} else {
//				Toast.makeText(getApplicationContext(), "sdcard not install",
//						Toast.LENGTH_SHORT).show();
//			}
//		} else {
//			Intent intent = new Intent(IcarCreate.this, PhotoActivity.class);
//			intent.putExtra("ID", arg2);
//			startActivity(intent);
//		}
//	}
//
//	public class select implements OnItemSelectedListener {
//		private int index = 0;
//
//		public select(int i) {
//			index = i;
//		}
//
//		boolean isFirst = false;
//
//		@Override
//		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			switch (index) {
//			case 1:
//				if (isFirst) {
//					mMfg = arg0.getItemAtPosition(arg2).toString();
//					Log.i("tagicar3", "msg"+mMfg);
//					MyThread(1, mMfg);
//				}
//				isFirst = true;
//				break;
//			case 2:
//				if (isFirst) {
//					Ddata2 = arg0.getItemAtPosition(arg2).toString();
//					if (!StringUtils.isEmpty(Ddata2)) {
//						MyThread(2, Ddata2);
//					} else {
//						dsp2_id = (Spinner) findViewById(R.id.icar_create_spinner9_9);
//						dsp2_id.setVisibility(View.INVISIBLE);
//					}
//				}
//				isFirst = true;
//				break;
//			case 3:
//				if (isFirst) {
//					Ddata3 = arg0.getItemAtPosition(arg2).toString();
//					if (!Ddata3.equals("")) {
//						if (Ddata3.equals(Ddata4) || Ddata3.equals(Ddata5)) {
//							UIHelper.ToastMessage(IcarCreate.this,
//									R.string.toast_error_same);
//							arg0.setSelection(0);
//						} else {
//							if (!StringUtils.isEmpty(Ddata3)) {
//								MyThread(3, Ddata3);
//							}
//						}
//					} else {
//						dsp3_id = (Spinner) findViewById(R.id.icar_create_spinner9_9);
//						dsp3_id.setVisibility(View.INVISIBLE);
//					}
//				}
//
//				isFirst = true;
//				break;
//			case 4:
//				if (isFirst) {
//					Ddata4 = arg0.getItemAtPosition(arg2).toString();
//					if (!Ddata4.equals("")) {
//						if (Ddata4.equals(Ddata3) || Ddata4.equals(Ddata5)) {
//							UIHelper.ToastMessage(IcarCreate.this,
//									R.string.toast_error_same);
//							arg0.setSelection(0);
//						} else {
//							if (!StringUtils.isEmpty(Ddata4)) {
//								MyThread(4, Ddata4);
//							}
//						}
//					} else {
//						dsp4_id = (Spinner) findViewById(R.id.icar_create_spinner10_10);
//						dsp4_id.setVisibility(View.INVISIBLE);
//					}
//
//				}
//				isFirst = true;
//				break;
//			case 5:
//				if (isFirst) {
//					Ddata5 = arg0.getItemAtPosition(arg2).toString();
//					if (!Ddata5.equals("")) {
//						if (Ddata5.equals(Ddata4) || Ddata5.equals(Ddata3)) {
//							UIHelper.ToastMessage(IcarCreate.this,
//									R.string.toast_error_same);
//							arg0.setSelection(0);
//						} else {
//							if (!StringUtils.isEmpty(Ddata5)) {
//								MyThread(5, Ddata5);
//							}
//						}
//					} else {
//						dsp5_id = (Spinner) findViewById(R.id.icar_create_spinner11_11);
//						dsp5_id.setVisibility(View.INVISIBLE);
//					}
//
//				}
//				isFirst = true;
//				break;
//			case 6:
//				Ddata6 = arg0.getItemAtPosition(arg2).toString();
//
//				// MyThread(6, Ddata6);
//				break;
//			case 7:
//				Ddata7 = arg0.getItemAtPosition(arg2).toString();
//
//				if (Ddata7 != "") {
//					MyThread(7, Ddata7);
//				}
//				break;
//			case 8:
//				Ddata8 = arg0.getItemAtPosition(arg2).toString();
//
//				if (Ddata8 != "") {
//					MyThread(8, Ddata8);
//				}
//				break;
//			case 9:
//				Ddata9 = arg0.getItemAtPosition(arg2).toString();
//
//				if (Ddata9 != "") {
//					MyThread(9, Ddata9);
//				}
//				break;
//			case 10:
//				Ddata10 = arg0.getItemAtPosition(arg2).toString();
//
//				// MyThread(10, Ddata10);
//				break;
//			case 11:
//				Ddata11 = arg0.getItemAtPosition(arg2).toString();
//
//				if (Ddata11 != "") {
//					MyThread(11, Ddata11);
//				}
//				break;
//			case 12:
//				Ddata12 = arg0.getItemAtPosition(arg2).toString();
//
//				if (Ddata12 != "") {
//					MyThread(12, Ddata12);
//				}
//				break;
//			case 13:
//				Ddata13 = arg0.getItemAtPosition(arg2).toString();
//				if (Ddata13 != "") {
//					MyThread(13, Ddata13);
//				}
//				break;
//			case 14:
//				Ddata14 = arg0.getItemAtPosition(arg2).toString();
//				if (Ddata14 != "") {
//					MyThread(14, Ddata14);
//				}
//				break;
//			case 15:
//				if (isFirst) {
//				skuno = arg0.getItemAtPosition(arg2).toString();
//				if (!skuno.equals("Other")) {
//					MyThread(15, skuno);
//				}
//				}
//				isFirst = true;
//				break;
//			case 16:
//				if (isFirst) {
//				// *nSbu = arg0.getItemAtPosition(arg2).toString();
//				nSbu = arg0.getItemAtPosition(arg2).toString();
//				if (nSbu.equals("CHSBU")) {
//					MyThread(16, nSbu);
//				}
//				}
//				isFirst=true;
//				break;
//			default:
//				break;
//			}
//
//		}
//
//		@Override
//		public void onNothingSelected(AdapterView<?> arg0) {
//			// TODO Auto-generated method stub
//
//		}
//	}
//
//	private void CheckPhoto() {
//		new AlertDialog.Builder(IcarCreate.this)
//				.setTitle("选择")
//				.setItems(items, new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						switch (which) {
//						case 0:
//							Intent intent = new Intent();// 调用图库
//							intent.setType("image/*");
//							intent.setAction(Intent.ACTION_PICK);// 调用系统图库
//							startActivityForResult(intent, CROP_PHOTO);
//							break;
//						default:
//							// Intent take = new
//							// Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启用拍照功能
//							// imageUri = Uri.fromFile(imageFile);
//							//
//							// take.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//							// // 传递图片保存路径
//							// startActivityForResult(take, TAKE_PHOTO);
//
//							String status = Environment
//									.getExternalStorageState();
//							if (status.equals(Environment.MEDIA_MOUNTED)) {
//								try {
//									localTempImageFileName = "";
//									// localTempImageFileName =
//									// String.valueOf((new Date()).getTime())
//									// + ".png";
//									localTempImageFileName = getPhotoName()
//											+ ".png";
//									File filePath = FILE_PIC_SCREENSHOT;
//									if (!filePath.exists()) {
//										filePath.mkdirs();
//									}
//									Intent intent1 = new Intent(
//											android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//									File f = new File(filePath,
//											localTempImageFileName);
//									Uri u = Uri.fromFile(f);
//									intent1.putExtra(
//											MediaStore.Images.Media.ORIENTATION,
//											0);
//									intent1.putExtra(MediaStore.EXTRA_OUTPUT, u);
//									startActivityForResult(intent1, TAKE_PHOTO);
//								} catch (ActivityNotFoundException e) {
//									//
//								}
//							}
//							break;
//						}
//					}
//				})
//				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//					}
//				}).show();
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		// super.onActivityResult(requestCode, resultCode, data);
//		switch (requestCode) {
//		case TAKE_PHOTO:
//			// 开启裁剪功能
//			// startPhotoZoom(imageUri);
//
//			if (drr.size() < 6 && resultCode == RESULT_OK) {
//				File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
//				Intent intent = new Intent(this, CropImageActivity.class);
//				intent.putExtra("path", f.getAbsolutePath());
//				startActivityForResult(intent, RESULT_PHOTO);
//				// f.delete();
//			}
//			break;
//		case CROP_PHOTO:
//			// startPhotoZoom(data.getData());
//			if (drr.size() < 6 && resultCode == RESULT_OK && null != data) {
//				if (data != null) {
//					Uri uri = data.getData();
//					if (!TextUtils.isEmpty(uri.getAuthority())) {
//						Cursor cursor = getContentResolver().query(uri,
//								new String[] { MediaStore.Images.Media.DATA },
//								null, null, null);
//						if (null == cursor) {
//							Toast.makeText(
//									IcarCreate.this,
//									(String) getBaseContext()
//											.getResources()
//											.getText(
//													R.string.icar_newrecord_picture_missing),
//									Toast.LENGTH_SHORT).show();
//							return;
//						}
//						cursor.moveToFirst();
//						String path = cursor.getString(cursor
//								.getColumnIndex(MediaStore.Images.Media.DATA));
//						cursor.close();
//						// Log.i(TAG,"path=" + path);
//						Intent intent = new Intent(this,
//								CropImageActivity.class);
//						intent.putExtra("path", path);
//						startActivityForResult(intent, RESULT_PHOTO);
//					} else {
//						// Log.i(TAG,"path=" + uri.getPath());
//						Intent intent = new Intent(this,
//								CropImageActivity.class);
//						intent.putExtra("path", uri.getPath());
//						startActivityForResult(intent, RESULT_PHOTO);
//					}
//				}
//			}
//			break;
//		case RESULT_PHOTO:
//			// if (data != null) {
//			// SentPictoNext(data);
//			// }
//			if (resultCode == RESULT_OK && null != data) {
//				final String path = data.getStringExtra("path");
//				Bitmap bitmap = BitmapFactory.decodeFile(path);
//				drr.add(path);
//				PhotoActivity.bitmap.add(bitmap);
//				bitmap = Bimp.createFramedPhoto(480, 480, bitmap,
//						(int) (20 * 1.6f));
//				bmp.add(bitmap);
//				gridviewInit();
//			}
//			break;
//		default:
//			break;
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//
//	public boolean isNetworkConnected() {
//		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo ni = cm.getActiveNetworkInfo();
//		return ni != null && ni.isConnectedOrConnecting();
//	}
//
//	/**
//	 * 获取当前系统时间
//	 * 
//	 * @return 返回图片名称
//	 */
//	private String getPhotoName() {
//		Date date = new Date(System.currentTimeMillis()); // 获取当前系统的时间
//		SimpleDateFormat dateFormat = new SimpleDateFormat(
//				"'IMG'_yyyyMMddHHmmss");
//		return dateFormat.format(date);
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (result_ok) {
//				Bundle bundle = new Bundle();
//				bundle.putString("Icar", mIcar);
//				System.out.println(">>>>" + mIcar + ">>>>>>");
//				setResult(RESULT_OK, this.getIntent().putExtras(bundle));
//			} else {
//				System.out.println(">>>>" + mIcar);
//			}
//			this.finish();
//			return true;
//		} else {
//			return super.onKeyDown(keyCode, event);
//
//		}
//	}
//
//}
