package com.zsm.foxconn.mypaperless.adapter;

import java.util.List;

import com.zsm.foxconn.mypaperless.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 宫格Gridview的Adapter
 */
public class MyGridAdapter extends BaseAdapter {
	private Context mContext;
	private String num = "",num_check="",audit="",excetion_num="";

	public int[] img_text = { R.string.text_examine1,
			R.string.text_examine2, R.string.text_examine3,
			R.string.text_examine4, R.string.text_examine5,
			R.string.text_examine6, R.string.text_examine7,
			R.string.text_examine8, R.string.text_examine9,R.string.text_examine10,
			R.string.text_examine11,R.string.text_examine12,R.string.text_examine13, R.string.text_examine14
			, R.string.text_examine15};
	public int[] imgs = { R.drawable.examine_all_72, R.drawable.examine_will_72, R.drawable.examine_ok_72,
			R.drawable.examine_no_72, R.drawable.examine_update_72, R.drawable.examinefg_72,
			R.drawable.examinetgr_72, R.drawable.examine_zhi_72, R.drawable.examine_jia_72,
			R.drawable.examine_floor_72,R.drawable.examine_jia_72,R.drawable.examine_chart_72,R.drawable.examine_will_72
			,R.drawable.examine_update_72,R.drawable.examine_update_72 };

	public MyGridAdapter(Context mContext,String num,String num_check,String audit,String excetion_num) {
		super();
		this.mContext = mContext;
		this.num = num;
		this.num_check =  num_check;
		this.audit=audit;
		this.excetion_num = excetion_num;
	}

	public int getCount() {

		return img_text.length;
	}

	public Object getItem(int position) {

		return position;
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
		TextView tv_canshu_mynews = BaseViewHolder.get(convertView, R.id.tv_canshu_mynews);
		iv.setBackgroundResource(imgs[position]);

		tv.setText(img_text[position]);
		if (img_text[position]==img_text[10]&&num.length()!=0) {
			tv_canshu_mynews.setVisibility(View.VISIBLE);
			tv_canshu_mynews.setText(num);
		}
		if (img_text[position]==img_text[1]&&num_check.length()!=0) {
			tv_canshu_mynews.setVisibility(View.VISIBLE);
			tv_canshu_mynews.setText(num_check);
		}
		if (img_text[position]==img_text[4]&&audit.length()!=0) {
			tv_canshu_mynews.setVisibility(View.VISIBLE);
			tv_canshu_mynews.setText(audit);
		}
		
		if (img_text[position]==img_text[12]&&excetion_num.length()!=0) {
			tv_canshu_mynews.setVisibility(View.VISIBLE);
			tv_canshu_mynews.setText(excetion_num);
		}
		return convertView;
	}

}

