package com.zsm.foxconn.mypaperless.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.help.UIHelper;

public class ChoosepersonAdapter extends BaseAdapter{
	private List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	private Context context;
	private List<String> liststr = new ArrayList<String>(); 
	private LayoutInflater inflater = null;
	private Map<String, String> map = null;
	private String str_logonname = null;
	
	public ChoosepersonAdapter(List<Map<String,String>> list,Context context,List<String> liststr){
		this.list = list;
		this.context = context;
		this.liststr = liststr;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChCheckHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.alchoose_person_item,
					null);
			holder = new ChCheckHolder();
			holder.tv_chperson_no=(TextView) convertView
			.findViewById(R.id.tv_chperson_no);
			holder.tv_chperson_name = (TextView) convertView
					.findViewById(R.id.tv_chperson_name);
			holder.tv_chperson_team = (TextView) convertView
					.findViewById(R.id.tv_chperson_team);
			holder.image_bt_shangyi = (ImageButton) convertView
					.findViewById(R.id.image_bt_shangyi);
			holder.image_bt_xiayi = (ImageButton) convertView
					.findViewById(R.id.image_bt_xiayi);
			holder.image_bt_delete = (ImageButton) convertView
					.findViewById(R.id.image_bt_delete);
			convertView.setTag(holder);
		} else {
			holder = (ChCheckHolder) convertView.getTag();
		}
		holder.tv_chperson_no.setText(position+1+"");
		holder.tv_chperson_name.setText(list.get(position).get("ch_name").toString()+
				"("+list.get(position).get("ch_logonname").toString()+")");
		holder.tv_chperson_team.setText(list.get(position).get("ch_team").toString());
		if (list.size()==1) {
			holder.image_bt_shangyi.setVisibility(View.INVISIBLE);
			holder.image_bt_xiayi.setVisibility(View.INVISIBLE);
		}else if (position==0) {
			holder.image_bt_shangyi.setVisibility(View.INVISIBLE);
		}else if (position==list.size()-1) {
			holder.image_bt_xiayi.setVisibility(View.INVISIBLE);
		}else {
			holder.image_bt_shangyi.setVisibility(View.VISIBLE);
			holder.image_bt_xiayi.setVisibility(View.VISIBLE);
		}
		holder.image_bt_shangyi.setOnClickListener(new lvButtonListener(
				position, "ch_shangyi")); // 向上移動
		holder.image_bt_xiayi.setOnClickListener(new lvButtonListener(
				position, "ch_xiayi")); // 向下移動
		holder.image_bt_delete.setOnClickListener(new lvButtonListener(
				position, "ch_delete")); // 移除
		return convertView;
	}

	class lvButtonListener implements OnClickListener{
		
		private int flag;
		private String caozuo;
		
		lvButtonListener(int pos, String mflag) {
			flag = pos;
			caozuo = mflag;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			v.getId();
			if (caozuo.equals("ch_shangyi")) {
				UIHelper.ToastMessage(context, "上移一位");
				map = new HashMap<String, String>();
				map = list.get(flag);
				str_logonname = liststr.get(flag);
				liststr.remove(flag);
				liststr.add(flag-1, str_logonname);
				list.remove(flag);
				list.add(flag-1, map);
				notifyDataSetChanged();
			}
			if (caozuo.equals("ch_xiayi")) {
				UIHelper.ToastMessage(context, "下移一位");
				map = new HashMap<String, String>();
				map = list.get(flag);
				str_logonname = liststr.get(flag);
				liststr.remove(flag);
				liststr.add(flag+1, str_logonname);
				list.remove(flag);
				list.add(flag+1, map);
				notifyDataSetChanged();
			}
			if (caozuo.equals("ch_delete")) {
				list.remove(flag);
				liststr.remove(flag);
				notifyDataSetChanged();
			}
		}
	} 
	
	public class ChCheckHolder{
		private TextView tv_chperson_no = null;
		private TextView tv_chperson_name = null;
		private TextView tv_chperson_team = null;
		private ImageButton image_bt_shangyi = null;
		private ImageButton image_bt_xiayi = null;
		private ImageButton image_bt_delete = null;
		
	}
	
	public List<String> get_allperson(){
		return liststr;
	}
}
