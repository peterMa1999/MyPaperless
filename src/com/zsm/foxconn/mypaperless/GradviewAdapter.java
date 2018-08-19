package com.zsm.foxconn.mypaperless;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.util.PhotoUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GradviewAdapter extends BaseAdapter 
{
	private Context context;
	private List<Bitmap> bitmaps=new ArrayList<Bitmap>();
	public GradviewAdapter(Context context,List<Bitmap> list) 
	{
		this.context=context;
		bitmaps=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bitmaps.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return bitmaps.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		viewPhotoHolder holder=null;
		if (convertView==null)
		{
			//item_griview_echeckativity
			convertView=LayoutInflater.from(context).inflate(R.layout.item_griview_echeckativity,null);
			holder=new viewPhotoHolder();
			holder.photoView=(ImageView) convertView.findViewById(R.id.photo_img);
			convertView.setTag(holder);
		}else {
			holder=(viewPhotoHolder) convertView.getTag();
		}
		holder.photoView.setImageBitmap(PhotoUtils.getThumbnailbyBitmap(context,PhotoUtils.toRoundCorner(bitmaps.get(position),5),R.dimen.img_w_120,R.dimen.img_w_120));
		return convertView;
	}
	public class viewPhotoHolder{
		private ImageView photoView;
	} 
}
