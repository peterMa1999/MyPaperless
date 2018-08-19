package com.zsm.foxconn.mypaperless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.zsm.foxconn.mypaperless.base.AppManager;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
public class Aduit_Noinspectinfo extends BaseActivity {
	private Context context = Aduit_Noinspectinfo.this;
	private String number, session, pro_id, check_content, check_name,
			check_pro_name,status;
	EditText edt;
	private ListView listviewinfo;
	private String[] listinfo;
	private Boolean islistinfo = false;
	List<Map<String, Object>> result = null;
	HashMap<String, Object> params = new HashMap<String, Object>();
	HttpStart start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aduit_noinspectinfo);
		findViewById();
		start = new HttpStart(context, handler);
		Intent intent = getIntent();
		number = intent.getStringExtra("number");
		session = intent.getStringExtra("session");
		status = intent.getStringExtra("status");
		start.getServerData(3, MyConstant.GET_DETAILS,
				number, session);
		
 
		listviewinfo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (status.equals("0")||status.equals("2")) {

					pro_id = result.get(position).get("pro_id").toString();
					check_content = result.get(position).get("check_content")
							.toString();
					check_name = result.get(position).get("check_name").toString();
					check_pro_name = result.get(position).get("check_pro_name")
							.toString(); 
					Builder builder = new AlertDialog.Builder(context);
				
					
					//自定义对话框
					PopUpDialog newDialog = new PopUpDialog(context, R.style.electric_line);//调用对话框样式
					newDialog.setCanceledOnTouchOutside(true);
					View view = newDialog.getCustomView();//获取customView对应的布局
					builder.setView(view);
					
					TextView text1 = (TextView) view
							.findViewById(R.id.textViewTotal1);
					TextView text2 = (TextView) view
							.findViewById(R.id.textViewTotal2);
					edt = (EditText) view.findViewById(R.id.editTotal);

					text1.setText(check_name);
					text2.setText(check_pro_name);
					edt.setText(check_content);
					
					builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							check_content = edt.getText().toString();

							// TODO Auto-generated method stub
							if(check_content.equals("")||check_content.length()==0){
								Toast.makeText(Aduit_Noinspectinfo.this, "輸入不能為空",0).show();
								return;
							}else{
								AlertDialog alert = new AlertDialog.Builder(Aduit_Noinspectinfo.this).create();
								alert.setTitle("系統提示：");
								alert.setIcon(R.drawable.img8);
								alert.setMessage("是否進行修改");
								alert.setButton(DialogInterface.BUTTON_POSITIVE, "確定", new DialogInterface.OnClickListener() {
									  
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										start.getServerData(3, MyConstant.GET_CONFIRM,
												check_content, number ,session,pro_id);
										//result = sampleservice.upnospectinfo(number,session,pro_id,check_content);
										
									}
								});
								
								alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										
										return;
									}
								});
								alert.show();
							}
						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					builder.create().show();
				
				}
				
			}
		});

	}

	public void showlist() {
		result = new ArrayList<Map<String, Object>>();
		if (islistinfo) {
			if (listinfo[0].toString().equalsIgnoreCase("null")
					|| listinfo[0].toString().equals(null)) {
				UIHelper.ToastMessage(context, "暂无数据");
				return;
			} else {
				for (int i = 0; i < listinfo.length; i += 4) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("check_name", listinfo[i].toString());
					map.put("check_pro_name", listinfo[i + 1].toString());
					map.put("check_content", listinfo[i + 2].toString());
					map.put("pro_id", listinfo[i + 3].toString());
					result.add(map);
				} 
			}
		} 
		SimpleAdapter adapter = new SimpleAdapter(context, result,
				R.layout.aduit_iteminfo, new String[] { "check_name",
						"check_pro_name", "check_content", "pro_id" },
				new int[] { R.id.inspectcheck_name, R.id.inspectcheck_pro_name,
						R.id.inspectcheck_content, R.id.inspectpro_id });
		listviewinfo.setAdapter(adapter);
	}          

	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		listviewinfo = (ListView) findViewById(R.id.nospectinlistview);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) // 键值对
			{
				if (key.equals(MyConstant.GET_DETAILS)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						listinfo = new String[result.size() - 1];
						for (int i = 1; i < result.size(); i++) {
							listinfo[i - 1] = result.get(i).toString();
						}
					}
					if (result.get(0).equals(MyConstant.GET_FLAG_NULL)) {
						listinfo = new String[result.size()];
						listinfo[0] = "null";
					}
					islistinfo = true;
					showlist();
				}
				
				if (key.equals(MyConstant.GET_CONFIRM)) {
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE)) {
						Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
					}
					
					if (result.get(0).equals(MyConstant.GET_FLAG_FALSE)){
						Toast.makeText(context, "修改失敗", Toast.LENGTH_SHORT).show();
						
					}
					start.getServerData(3, MyConstant.GET_DETAILS,
							number, session);
				} 
			}
			
		}
	};

	// 返回键按钮
	public void returnClick(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}

}
