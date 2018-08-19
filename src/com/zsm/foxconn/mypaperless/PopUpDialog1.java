package com.zsm.foxconn.mypaperless;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
public class PopUpDialog1 extends Dialog{

	Context context;  
	private View customView;  
	public PopUpDialog1(Context context) {  
	super(context);  
	this.context = context;  
	}  
public PopUpDialog1(Context context, int theme){  
	       super(context, theme);  
	        this.context = context;  
	        //获取布局
	        LayoutInflater inflater= LayoutInflater.from(context);  
	       customView = inflater.inflate(R.layout.activity_switch1, null);  
	   }  
	@Override  
	protected void onCreate(Bundle savedInstanceState) {  
	// TODO Auto-generated method stub  
	super.onCreate(savedInstanceState);  
	this.setContentView(customView);  
}  
	@Override  
	public View findViewById(int id) {  
	// TODO Auto-generated method stub  
	return super.findViewById(id);  
	}  
	public View getCustomView() {  
	return customView;  
	}  

}
