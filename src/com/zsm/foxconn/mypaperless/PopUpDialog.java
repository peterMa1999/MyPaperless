package com.zsm.foxconn.mypaperless;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
public class PopUpDialog extends Dialog{

	Context context;  
	private View customView;  
	public PopUpDialog(Context context) {  
	super(context);  
	this.context = context;  
	}  
public PopUpDialog(Context context, int theme){  
	       super(context, theme);  
	        this.context = context;  
	        //获取布局
	        LayoutInflater inflater= LayoutInflater.from(context);  
	       customView = inflater.inflate(R.layout.audit_dialog, null);  
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
