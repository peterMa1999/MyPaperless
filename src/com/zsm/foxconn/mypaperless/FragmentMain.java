package com.zsm.foxconn.mypaperless;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.adapter.CommonAdapter;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.ConnSkyWebService;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.service.TimeServer;

/**
 * 首頁，得到當前用戶的所有BU
 * 
 * @author mgp 2015/12/26
 */
public class FragmentMain extends Fragment {
	private View view;
	private GridView info;
	HttpStart start = null;
	private String titles[] = null;
	private String BUName;
	private ImageView images = null;
	private UserBean userBean;
	private ImageButton exitImageButton;
	private TextView bartitle_txt;
	private Spinner BU_spinner, TEAM_spinner, MFG_spinner, SBU_spinner;
	private Boolean isfirst = false;
	private String BUname, teamname, SBUname, mfgname;
	private List<String> mfgstrs = null;
	String modelstr = "";
	private ImageButton switchers;
	pictureAdapter adapter;
	private CommonAdapter<Pic> myAdapter;
	private List<Pic> data = new ArrayList<Pic>();
	ConnSkyWebService service=new ConnSkyWebService();

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.main, container, false);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		userBean = (UserBean) getActivity().getApplicationContext();
		info = (GridView) view.findViewById(R.id.info);
		images = (ImageView) view.findViewById(R.id.image);
		exitImageButton = (ImageButton) view.findViewById(R.id.bt_img_exit);
		bartitle_txt = (TextView) view.findViewById(R.id.bartitle_txt);
		bartitle_txt.setText("首頁");
		myAdapter = new CommonAdapter<Pic>(getActivity(), data, R.layout.give) {
			@Override
			public void convert(
					com.zsm.foxconn.mypaperless.adapter.ViewHolder holder, Pic t) {
				// TODO Auto-generated method stub
				holder.setText(R.id.title, t.getTitle().toString());
			}
		};
		info.setAdapter(myAdapter); // 為gridview設置數據源
		// adapter = new pictureAdapter(titles,
		// images, getActivity());// 創建對象
		// info.setAdapter(adapter); // 為gridview設置數據源
		if (userBean.getLogonName().equals("admin")) {
			switchers = (ImageButton) view.findViewById(R.id.switchers);
			switchers.setVisibility(view.VISIBLE);
			switchers.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					start.getServerData(3, MyConstant.GET_BIG_CAREER_BU_ADMIN);
					Builder builder = new AlertDialog.Builder(getActivity());
					// 自定义对话框
					PopUpDialog1 newDialog = new PopUpDialog1(getActivity(),
							R.style.electric_line);// 调用对话框样式
					View view = newDialog.getCustomView();// 获取customView对应的布局
					builder.setView(view);
					builder.setCancelable(false);
					BU_spinner = (Spinner) view.findViewById(R.id.BU_spinner);
					TEAM_spinner = (Spinner) view
							.findViewById(R.id.TEAM_spinner);
					MFG_spinner = (Spinner) view.findViewById(R.id.MFG_spinner);
					SBU_spinner = (Spinner) view.findViewById(R.id.SBU_spinner);
					BU_spinner
							.setOnItemSelectedListener(new OnItemSelectedListener() {
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int pos, long id) {
									// 获取製造处
									BUname = parent.getItemAtPosition(pos)
											.toString();
									start.getServerData(0,
											MyConstant.GET_CAREER_MFG_ADMIN,
											BUname);
								}

								public void onNothingSelected(
										AdapterView<?> parent) {
								}
							});
					MFG_spinner
							.setOnItemSelectedListener(new OnItemSelectedListener() {
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int pos, long id) {
									// 获取SBU
									mfgname = parent.getItemAtPosition(pos)
											.toString();
									start.getServerData(0,
											MyConstant.GET_A_SBU_ADMIN, BUname,
											mfgname);
								}

								public void onNothingSelected(
										AdapterView<?> parent) {
								}
							});
					SBU_spinner
							.setOnItemSelectedListener(new OnItemSelectedListener() {
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int pos, long id) {

									// 获取team
									SBUname = parent.getItemAtPosition(pos)
											.toString();
									start.getServerData(0,
											MyConstant.GET_CREATE_TEAM_ADMIN,
											BUname,mfgname,SBUname);

								}

								public void onNothingSelected(
										AdapterView<?> parent) {
								}

							});
					TEAM_spinner
							.setOnItemSelectedListener(new OnItemSelectedListener() {
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int pos, long id) {
									teamname = parent.getItemAtPosition(pos)
											.toString();
								}

								public void onNothingSelected(
										AdapterView<?> parent) {
								}

							});

					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									userBean.setBU(BUname);
									userBean.setTeam(teamname);
									userBean.setMFG(mfgname);
									userBean.setSBU(SBUname);
									start.getServerData(0, MyConstant.GET_BU,
											userBean.getSite(),
											userBean.getBG());
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
					builder.create().show();
				}

				private LayoutInflater getLayoutInflater() {
					// TODO Auto-generated method stub
					return null;
				}
			});
		}

		start = new HttpStart(getActivity(), handler);
		start.getServerData(0, MyConstant.GET_BU, userBean.getSite(),
				userBean.getBG());
		exitImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getActivity())
						.setIcon(R.drawable.nt_warn)
						.setTitle(getResources().getString(R.string.exit))
						.setPositiveButton(
								getResources().getString(R.string.back_yes),
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										userBean.resolve();
										AppManager.getInstance().AppExit(
												getActivity());
										Intent intent = new Intent(
												getActivity(),
												LoginActivity.class);
										startActivity(intent);
										getActivity().stopService(new Intent(getActivity(),TimeServer.class));
										service.getInfo("AppUserCount",userBean.getLogonName(),MyConstant.APP_NAME,"exit",
													UIHelper.getLocalMacAddressFromWifiInfo(getActivity()));
									}
								})
						.setNeutralButton(
								getResources().getString(R.string.back_no),
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		});
		return view;

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = null;
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.equals(MyConstant.GET_BU)) {
					// result = new ArrayList<String>();
					result = msg.getData().getStringArrayList(key);
					// titles = new String[result.size() - 1];

					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_NULL_DETAIL)) {
						UIHelper.ToastMessage(getActivity(), "該廠區暫無BU", 5000);
						return;
					}
					if (result.get(0).toString()
							.equals(MyConstant.GET_FLAG_TRUE)) {
						if (data.size() > 0) {
							data.clear();
						}
						for (int i = 1; i < result.size(); i++) {
							// titles[k] = result.get(i).toString();
							Pic pic = new Pic();
							pic.setTitle(result.get(i).toString());
							data.add(pic);
						}
						info.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								String section = "";
								for (int i = 0; i < data.size(); i++) {
									// for (int i = 0; i <
									// titles[arg2].length(); i++) {
									// char h = titles[arg2].charAt(i);
									char h = data.get(arg2).toString()
											.charAt(i);

									if ((h >= 'A' && h <= 'Z')
											|| (h >= 'a' && h <= 'Z')) {
										section += h;
									} else {
										break;
									}
								}
								BUName = data.get(arg2).getTitle().toString();
								String isaccess = "0";
								// UIHelper.ToastMessage(getActivity(), BUName);
								Intent intent = new Intent(getActivity(),
										Choose_section.class);
								intent.putExtra("issection", "2");
								intent.putExtra("bUname", BUName);
								intent.putExtra("isaccess", isaccess);
								startActivity(intent);
								getActivity().overridePendingTransition(
										R.anim.move_right_in_activity,
										R.anim.move_left_out_activity);
							}
						});
						myAdapter.notifyDataSetChanged();
					}
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(getActivity(), "未连接服务器....", 0).show();
					}
					start.getServerData(0, MyConstant.GET_INTERNET_TIME);
				}

				if (key.equals(MyConstant.GET_INTERNET_TIME)) {
					result = msg.getData().getStringArrayList(key);
					System.out.println("--" + result.get(0).toString());
					if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_UNUNITED)) {
						Toast.makeText(getActivity(), "未连接服务器....", 0).show();
						return;
					}
					Long nowtime = Long.parseLong(result.get(0).toString());
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
					String iDate = df.format(new Date());
					Long time = Long.parseLong(iDate);
					if (!time.equals(nowtime)) {
						AlertDialog alert = new AlertDialog.Builder(
								getActivity()).create();
						alert.setIcon(R.drawable.ic_system);
						alert.setTitle("系統提示:");
						alert.setMessage("當前日期有較大的誤差,請修改");
						alert.setButton(DialogInterface.BUTTON_NEUTRAL, "去設置",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(
												Settings.ACTION_SETTINGS);
										startActivity(intent);
									}
								});
						alert.show();
					}
					
				}

				// 获取事业处BU GET_A_CAREER_BU
				if (key.equals(MyConstant.GET_BIG_CAREER_BU_ADMIN)) {
					result = msg.getData().getStringArrayList(key);

					mfgstrs = new ArrayList<String>();// 实例化mfastr集合
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i++) {
							mfgstrs.add(result.get(i).toString());
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						mfgstrs.add("N/A");
					}
					BU_spinner.setAdapter(new ArrayAdapter(getActivity(),
							android.R.layout.simple_spinner_dropdown_item,
							mfgstrs));
				}
				if (key.equals(MyConstant.GET_CREATE_TEAM_ADMIN)) {
					result = msg.getData().getStringArrayList(key);
					mfgstrs = new ArrayList<String>();
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i++) {
							mfgstrs.add(result.get(i).toString());
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						mfgstrs.add("N/A");
					}
					TEAM_spinner.setAdapter(new ArrayAdapter(getActivity(),
							android.R.layout.simple_spinner_dropdown_item,
							mfgstrs));
				}
				if (key.equals(MyConstant.GET_CAREER_MFG_ADMIN)) {
					result = msg.getData().getStringArrayList(key);
					mfgstrs = new ArrayList<String>();
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i++) {
							mfgstrs.add(result.get(i).toString());
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						mfgstrs.add("N/A");
					}
					MFG_spinner.setAdapter(new ArrayAdapter(getActivity(),
							android.R.layout.simple_spinner_dropdown_item,
							mfgstrs));
				}
				if (key.equals(MyConstant.GET_A_SBU_ADMIN)) {
					result = msg.getData().getStringArrayList(key);
					mfgstrs = new ArrayList<String>();
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) {
						for (int i = 1; i < result.size(); i++) {
							mfgstrs.add(result.get(i).toString());
						}
					}
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_NULL)) {
						mfgstrs.add("N/A");
					}
					SBU_spinner.setAdapter(new ArrayAdapter(getActivity(),
							android.R.layout.simple_spinner_dropdown_item,
							mfgstrs));
				}
				if (key.equals(
						MyConstant.GET_FLAG_UNUNITED)) {
					Toast.makeText(getActivity(), "未连接服务器....", 0).show();
					return;
				}
			}
		};
	};

	class pictureAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private List<Picture> pictures;

		public pictureAdapter(String[] titles, ImageView images, Context context) {
			super();
			pictures = new ArrayList<Picture>();
			inflater = LayoutInflater.from(context);
			Picture picture = null;
			for (int i = 0; i < titles.length; i++) {
				picture = new Picture(titles[i], images);
				pictures.add(picture);
			}

		}

		@Override
		public int getCount() {
			if (null != pictures) {
				return pictures.size();
			} else {
				return 0;
			}

		}

		@Override
		public Object getItem(int arg0) {
			return pictures.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		// 顯示內容
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			ViewHolder viewHolder;
			if (arg1 == null) {
				arg1 = inflater.inflate(R.layout.give, null);
				viewHolder = new ViewHolder();
				viewHolder.title = (TextView) arg1.findViewById(R.id.title);
				viewHolder.image = (ImageView) arg1.findViewById(R.id.image);
				arg1.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) arg1.getTag();
			}
			String title = pictures.get(arg0).getTitle();
			viewHolder.title.setText(title);
			// if (list.indexOf(title) == -1) {
			// viewHolder.image.setImageResource(R.drawable.images2);
			// }
			// arg1.setTag(viewHolder);
			return arg1;
		}
	}

	class ViewHolder {
		public TextView title;
		public ImageView image;
	}

	class Picture {
		public String title;
		private ImageView imageId;

		public Picture() {
			super();
		}

		public int getIageId() {
			// TODO Auto-generated method stub
			return 0;
		}

		public Picture(String title, ImageView images) {
			super();
			this.title = title;
			this.imageId = images;

		}

		public String getTitle() {
			return title;

		}

		public void setTitle(String title) {
			this.title = title;

		}

	}

	class Pic {
		String title;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}
}
