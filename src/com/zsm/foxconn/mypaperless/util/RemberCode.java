package com.zsm.foxconn.mypaperless.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class RemberCode 
{
	public static boolean saveUserInfor(Context context,String number,String password) 
	{
		SharedPreferences sp=context.getSharedPreferences("RemberCode",context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putString("username",number);
		editor.putString("pwd",password);
		editor.commit();
		return true;
	}
	
	public static boolean saveusername(Context context,String username){
		SharedPreferences sp=context.getSharedPreferences("Remberusername",context.MODE_PRIVATE);
		Editor editor=sp.edit();
		if (!sp.getAll().containsKey(username)) {
			editor.putString(username, username);
			editor.commit();
		}
		System.out.println(">>>"+sp.getString(username, null));
		return true;
	}
	
	public static String[] getusername(Context context){
		SharedPreferences sp=context.getSharedPreferences("Remberusername",context.MODE_PRIVATE);
		Editor editor=sp.edit();
		System.out.println(">>>>"+sp.getAll()+"-"+sp.getAll().values().toString());
		return sp.getAll().keySet().toArray(new String[0]);
	}
	
	public static Map<String,String> getUserInfo(Context context) 
	{
		SharedPreferences sp=context.getSharedPreferences("RemberCode",context.MODE_PRIVATE);
		String number=sp.getString("username",null);
		String password=sp.getString("pwd",null);
		 Map<String,String> userMap=new HashMap<String, String>();
		userMap.put("number",number);
		userMap.put("password",password);
		return userMap;	
	}
	
	public static boolean saveCheck_notification(Context context,boolean flag) 
	{
		SharedPreferences sp=context.getSharedPreferences("Check_version",context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putBoolean("flag",flag);
		editor.commit();
		return true;
	}
	public static boolean getCheck_notification(Context context) 
	{
		SharedPreferences sp=context.getSharedPreferences("Check_version",context.MODE_PRIVATE);
		boolean flag=sp.getBoolean("flag",true);
		return flag;	
	}
	
	public static boolean clean_userinfo(Context context) 
	{
		SharedPreferences sp=context.getSharedPreferences("RemberCode",context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.remove("username");
		editor.remove("pwd");
		editor.commit();
		return true;	
	}
	
	public static boolean get_wellcom_page(Context context){
		SharedPreferences sharepreferences=context.getSharedPreferences("check", context.MODE_PRIVATE);
		Editor editor=sharepreferences.edit();
	    boolean fristload=sharepreferences.getBoolean("fristload", true);
	    return fristload;
	}
	
	public static boolean set_wellcom_page(Context context){
		SharedPreferences sharepreferences=context.getSharedPreferences("check", context.MODE_PRIVATE);
		Editor editor=sharepreferences.edit();
		editor.putBoolean("fristload", false);
		editor.commit();
	    return true;
	}
	
	/**
	 * 是否第一次操作批量審核
	 * @param context
	 * @return
	 */
	public static boolean get_isfirstcheck(Context context){
		SharedPreferences sharepreferences=context.getSharedPreferences("version_operation", context.MODE_PRIVATE);
		Editor editor=sharepreferences.edit();
	    boolean fristload=sharepreferences.getBoolean("fristoperation", true);
	    return fristload;
	}
	
	/**
	 * 表示第一次操作待審核已完成
	 * @param context
	 * @return
	 */
	public static boolean set_isfirstcheck(Context context){
		SharedPreferences sharepreferences=context.getSharedPreferences("version_operation", context.MODE_PRIVATE);
		Editor editor=sharepreferences.edit();
		editor.putBoolean("fristoperation", false);
		editor.commit();
	    return true;
	}
	
	/**
	 * 是否第一次操作人員信息
	 * @param context
	 * @return
	 */
	public static boolean get_isfirst_addperson(Context context){
		SharedPreferences sharepreferences=context.getSharedPreferences("version_operation", context.MODE_PRIVATE);
		Editor editor=sharepreferences.edit();
	    boolean fristload=sharepreferences.getBoolean("fristaddperson", true);
	    return fristload;
	}
	
	
	/**
	 * 表示第一次操作人員已完成
	 * @param context
	 * @return
	 */
	public static boolean set_isfirst_addperson(Context context){
		SharedPreferences sharepreferences=context.getSharedPreferences("version_operation", context.MODE_PRIVATE);
		Editor editor=sharepreferences.edit();
		editor.putBoolean("fristaddperson", false);
		editor.commit();
	    return true;
	}
}
