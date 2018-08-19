package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.UIHelper;
import com.zsm.foxconn.mypaperless.http.HttpStart;
import com.zsm.foxconn.mypaperless.util.TouchImageView;
public class Abnormal_right_itemActivity extends BaseActivity {
	Context context = Abnormal_right_itemActivity.this;
	private ListView gridView;
	HttpStart start;
	UserBean userBean;
	Map<String,Object> map=null;
	private Button msubmit;
	private TextView mconmittime;
	List<String> PhotoName=new ArrayList<String>();
	private AbnormalGradviewAdapter gradviewAdapter=null;
	private TextView detailsTextView;
	private EditText mreplay_et;
	private String ID,Dealresult;
	private SimpleAdapter simpleAdapter=null;
	private Map<String,String> listMap=null;
	private List<Map<String,String>> list=new ArrayList<Map<String,String>>();
	private ListView mlistView;
	private String peppleString="";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abnormal_right_item_layout);
		userBean = (UserBean) getApplicationContext();
		start = new HttpStart(context, handler);
		ID=getIntent().getStringExtra("id");
		findViewById();
		initView();
		start.getServerData(0,MyConstant.GET_ABNORMAL_ITEM,ID);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		detailsTextView=(TextView) findViewById(R.id.details);
		mreplay_et=(EditText) findViewById(R.id.replay_et);
		mreplay_et.setText("\t\t");
		msubmit=(Button) findViewById(R.id.btn_submit);
		gridView=(ListView) findViewById(R.id.photo_listview);
		mconmittime=(TextView) findViewById(R.id.committime_tv);
		gradviewAdapter=new AbnormalGradviewAdapter();
		gridView.setAdapter(gradviewAdapter);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		msubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChoosePop();
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				// TODO Auto-generated method stub
				showAbnormalPop(MyConstant.GET_WSDL_ABNORMALPHOTO+PhotoName.get(arg2));
			}
		});
	}
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> result = new ArrayList<String>();
			Bundle bundle = msg.getData();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) // 键值对
			{
				if (key.equalsIgnoreCase(MyConstant.GET_ABNORMAL_ITEM))
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE))
					{
							map=new HashMap<String, Object>();
//							map.put("from_user",result.get(1));
							map.put("content",result.get(2));
//							map.put("to_user",result.get(3));
							map.put("picture_url",result.get(4));
							map.put("commit_time",result.get(5));
							detailsTextView.setText("\t\t"+map.get("content").toString().trim());
							mconmittime.setText(map.get("commit_time").toString().trim());
							PhotoName=Arrays.asList(map.get("picture_url").toString().trim().split("!"));
							for (String string : PhotoName)
							{
								Log.i("图片路径>>>",MyConstant.GET_WSDL_ABNORMALPHOTO+string);
							}
					}else {
						UIHelper.ToastMessage(context,"獲取失敗");
					}
					gradviewAdapter.notifyDataSetChanged();
				}
				if (key.equalsIgnoreCase(MyConstant.DEAL_ABNORMAL)) 
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0).equals(MyConstant.GET_FLAG_TRUE))
					{
						UIHelper.ToastMessage(context,"提交成功");
					}else {
						UIHelper.ToastMessage(context, "提交失败");
					}
				}
				if (key.equals(MyConstant.GET_PD_CHECK_NAME)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) 
					{
						if (list.size() > 0)
						{
							list.clear();
						}
						for (int i = 1; i < result.size(); i += 2)
						{
							listMap = new HashMap<String, String>();
							listMap.put("workNB",result.get(i));
							listMap.put("Name",result.get(i + 1));
							list.add(listMap);
						}
					} else if (result.get(0).equalsIgnoreCase(
							MyConstant.GET_FLAG_NULL)) 
					{
						Toast.makeText(context, "暫無处理异常主管", 0).show();
					}
					simpleAdapter.notifyDataSetChanged();
				}
				if (key.equals(MyConstant.CHANGE_ABNORMAL_DEAL_PEOPLE)) //
				{
					result = msg.getData().getStringArrayList(key);
					if (result.get(0)
							.equalsIgnoreCase(MyConstant.GET_FLAG_TRUE)) 
					{
						UIHelper.ToastMessage(context,"修改成功");
					} else 
					{
						UIHelper.ToastMessage(context,"修改失败");
					}
				}
			}
		}
	};
	/**
	 * 图片listview;
	 * @param url
	 */
	private  void showAbnormalPop(String url)
	{
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		TouchImageView touchImageView;
		View contentView=LayoutInflater.from(context).inflate(R.layout.popuwindow_abnormal_pictures,null);
		touchImageView=(TouchImageView) contentView.findViewById(R.id.photo_scale);
		backgroundAlpha(0.3f);
		PopupWindow window=new PopupWindow();
		window.setContentView(contentView);
		window.setFocusable(true);
		window.setOutsideTouchable(true);
		window.setAnimationStyle(R.style.POP_Animation_trans);
		window.setWindowLayoutMode(
				(display.getWidth() - (display.getWidth() / 14)),
				(display.getHeight() - (display.getHeight()/4)));
		window.setWindowLayoutMode(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		ColorDrawable cd = new ColorDrawable(0x000000);
		window.setBackgroundDrawable(cd);
		window.showAtLocation(Abnormal_right_itemActivity.this.findViewById(R.id.linear_right_root),Gravity.CENTER, 0,0);
		touchImageView.setImageUrl(url);
		window.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
					backgroundAlpha(1f);
			}
		});
	}
	/**
	 * 选择异常处理人listview;
	 * @param url
	 */
	private  void ChoosePop()
	{
		final TextView mchooseTextView;
		Spinner spinner;
		Button mchange;
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		View contentView=LayoutInflater.from(context).inflate(R.layout.popuwindow_abnormal_choosepeople,null);
		mchooseTextView=(TextView) contentView.findViewById(R.id.tv_choose);
		mlistView=(ListView) contentView.findViewById(R.id.lv_choose);
		spinner=(Spinner) contentView.findViewById(R.id.spinner_choose_depart);
		mchange=(Button) contentView.findViewById(R.id.bt_change);
		simpleAdapter=new SimpleAdapter(context,list,R.layout.abnormal_right_enter_item,
				new String[]{"workNB","Name"},
				new int[]{R.id.tv_workNB,R.id.tv_name});
		mlistView.setAdapter(simpleAdapter);
		backgroundAlpha(0.3f);
		final PopupWindow window=new PopupWindow();
		window.setContentView(contentView);
		window.setFocusable(true);
		window.setOutsideTouchable(true);
		window.setAnimationStyle(R.style.POP_Animation_trans);
		window.setWindowLayoutMode(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		ColorDrawable cd = new ColorDrawable(0x000000);
		window.setBackgroundDrawable(cd);
		window.showAtLocation(Abnormal_right_itemActivity.this.findViewById(R.id.linear_right_root),Gravity.CENTER, 0,0);
		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				peppleString=list.get(arg2).get("workNB").toString().trim();
				mchooseTextView.setText(list.get(arg2).get("workNB")+"/"+list.get(arg2).get("Name"));
			}
		});
		mchange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				start.getServerData(0,MyConstant.CHANGE_ABNORMAL_DEAL_PEOPLE,peppleString,ID);
				window.dismiss();
			}
		});
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String PDteam = arg0.getItemAtPosition(arg2).toString();
				start.getServerData(0, MyConstant.GET_PD_CHECK_NAME,
						PDteam,userBean.getMFG());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		window.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
					backgroundAlpha(1f);
			}
		});
	}
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha;
		getWindow().setAttributes(lp);
	}
	class AbnormalGradviewAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return PhotoName.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return PhotoName.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2)
		{
			// TODO Auto-generated method stub
			viewPhotoHolder holder=null;
			if (convertView==null)
			{
				convertView=LayoutInflater.from(context).inflate(R.layout.abnormal_details_item,null);
				holder=new viewPhotoHolder();
				holder.photoView=(SmartImageView) convertView.findViewById(R.id.photo_ti);
				convertView.setTag(holder);
			}else {
				holder=(viewPhotoHolder) convertView.getTag();
			}
			holder.photoView.setImageUrl(MyConstant.GET_WSDL_ABNORMALPHOTO+PhotoName.get(arg0));
			return convertView;
		}
		private class viewPhotoHolder
		{
			private SmartImageView photoView;
		} 
	}
// 返回键按钮
	public void back(View v) {
		this.finish();
		overridePendingTransition(R.anim.right_pull, R.anim.left_pull);
	}
	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		
	}
	private String getRepalay()
	{
		return mreplay_et.getText().toString().trim();
	}
}
