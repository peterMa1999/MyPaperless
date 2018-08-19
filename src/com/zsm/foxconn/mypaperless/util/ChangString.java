package com.zsm.foxconn.mypaperless.util;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import taobe.tec.jcc.JChineseConvertor;

public class ChangString {
	
	/**
	 * 
	 * @param 简体转成繁体 
	 * @return
	 */
    public static String change(String changeText) 
    {  
        try {  
            JChineseConvertor jChineseConvertor = JChineseConvertor  
                    .getInstance();  
            changeText = jChineseConvertor.s2t(changeText);  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return changeText;  
    }  
  
    /**
	 * 
	 * @param 繁体转成简体 
	 * @return
	 */
    public static String change1(String changeText) {  
        try {  
            JChineseConvertor jChineseConvertor = JChineseConvertor  
                    .getInstance();  
            changeText = jChineseConvertor.t2s(changeText);  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return changeText;  
    }  
    /** 含有标题、内容、图标、两个按钮的对话框 **/
	public static AlertDialog showAlertDialog(Context context,String title, String message,
			int icon, 
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener,
			String NeutralText,
			DialogInterface.OnClickListener onNeutralClickListener)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message).setIcon(icon)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.setNeutralButton(NeutralText, onNeutralClickListener)
				.show();
		return alertDialog;
	} 

}
