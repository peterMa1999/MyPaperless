package com.zsm.foxconn.mypaperless.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

	private SparseArray<View> mView;

	private int mPosition;

	private View mConvertView;

	public ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {

		this.mPosition = position;
		this.mView = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);

	}

	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}

	public int getPosition() {
		return mPosition;
	}

	/**
	 * 通过viewId获取控件
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mView.get(viewId);

		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mView.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	
	public ViewHolder setText(int viewId, int text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	public ViewHolder setImageResource(int viewId, int resId) {
		ImageView view = getView(viewId);
		view.setImageResource(resId);
		return this;
	}
	
	public ViewHolder setButton(int viewId, int resId) {
		Button view = getView(viewId);
		if (resId!=0) {
			view.setBackgroundResource(resId);
		}
		return this;
	}

	public ViewHolder setImageBitamp(int viewId, Bitmap bitmap) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bitmap);
		return this;
	}

	public ViewHolder setImageBitamp(int viewId, String url) {
		ImageView view = getView(viewId);
		// Imageloader.getInstance().loading();
		return this;
	}

}
