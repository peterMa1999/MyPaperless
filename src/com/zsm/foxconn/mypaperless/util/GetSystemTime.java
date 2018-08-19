package com.zsm.foxconn.mypaperless.util;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GetSystemTime 
{
	/**
	 * 
	 * @param 时间分割符号,例如:"/","-";
	 * @return 年月日时分秒;
	 */
	public static String GetTimeYMDhms(String a) 
	{
		String minute,hour,sec,Year,Month,Day,time,b;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String iDate = df.format(new Date()).toString();
		Year=iDate.substring(0,4);
		Month=iDate.substring(4,6);
		Day=iDate.substring(6,8);
		hour=iDate.substring(8,10);
		minute=iDate.substring(10,12);
		sec=iDate.substring(12,14);
		b=a;
		time=Year+b+Month+b+Day+b+hour+b+minute+b+sec;
		return time;
	}
	/**
	 * 
	 * @param 时间分割符号,例如:"/","-";
	 * @return 年月日;
	 */
	public static String GetTimeYMD(String a) 
	{
		String Year,Month,Day,time,b;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String iDate = df.format(new Date()).toString();
		Year=iDate.substring(0,4);
		Month=iDate.substring(4,6);
		Day=iDate.substring(6,8);
		b=a;
		time=Year+b+Month+b+Day;
		return time;
	}
	/**
	 * 
	 * @param 时间分割符号,例如:"/","-";
	 * @return 时分秒;
	 */
	public static String GetTimehms(String a) 
	{
		String minute,hour,sec,time,b;
		SimpleDateFormat df = new SimpleDateFormat("HHmmss");
		String iDate = df.format(new Date()).toString();
		hour=iDate.substring(0,2);
		minute=iDate.substring(2,4);
		sec=iDate.substring(4,6);
		b=a;
		time=hour+b+minute+b+sec;
		return time;
	}
	/**
	 * 
	 * @param 时间分割符号,例如:"/","-";
	 * @return 时分;
	 */
	public static String GetTimehm(String a) 
	{
		String minute,hour,sec,time,b;
		SimpleDateFormat df = new SimpleDateFormat("HHmmss");
		String iDate = df.format(new Date()).toString();
		hour=iDate.substring(0,2);
		minute=iDate.substring(2,4);
		b=a;
		time=hour+b+minute;
		return time;
	}


	
}
