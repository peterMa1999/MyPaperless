package com.foxconn.selecttime;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.net.ParseException;

public class Get_Time {
	/**
	 * 獲得相隔日期的相隔周數
	 * 
	 * @param startDate
	 *            開始時間日期
	 * @param endDate
	 *            結束時間日期
	 * @return
	 */
	public static Object countTwoDayWeek(String startDate, String endDate) {
		Date start = null;
		Date end = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); // 设置时间格式
		try {
			start = sdf.parse(startDate);
			end = sdf.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		long time1 = cal.getTimeInMillis();
		cal.setTime(end);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		Double days = Double.parseDouble(String.valueOf(between_days));
		if ((days / 7) > 0 && (days / 7) <= 1) {
			// 不满一周的按一周算
			return 1;
		} else if (days / 7 > 1) {
			int day = days.intValue();
			if (day % 7 > 0) {
				return day / 7 + 1;
			} else {
				return day / 7;
			}
		} else if ((days / 7) == 0) {
			return 0;
		} else {
			// 负数返还null
			return null;
		}

	}

	/**
	 * 按年月獲得當前月的日期
	 * 
	 * @param time
	 *            年月：2016/06/19或2016-06-19
	 * @param day
	 *            天
	 * @return
	 */
	public static String getNowMonthDate(String time, String day) {
		String month = "";
		String[] monthstr = time.split("-");
		String[] monthstr2 = time.split("/");
		if (monthstr.length > 1) {

			int yue = Integer.parseInt(monthstr[1].toString().trim());
			int year = Integer.parseInt(monthstr[0].toString().trim());

			if (yue < 10) {
				month = year + "-0" + yue + "-" + day;
			} else {
				month = year + "-" + yue + "-" + day;
			}
		}
		if (monthstr2.length > 1) {
			int yue = Integer.parseInt(monthstr2[1].toString().trim());
			int year = Integer.parseInt(monthstr2[0].toString().trim());

			if (yue < 10) {
				month = year + "/0" + yue + "/" + day;
			} else {
				month = year + "/" + yue + "/" + day;
			}
		}

		return month;
	}

	/**
	 * 將時間轉化為Float大小
	 * 
	 * @param initStartTime
	 *            ：2016/06/19或2016-06-19
	 * @return
	 */
	public static Float getDateFloat(String initStartTime) {
		String[] StartTime = initStartTime.split("/");
		String[] StartTime2 = initStartTime.split("-");

		Float timeStart = 0f;// 時間大小
		if (StartTime.length > 1) {
			String timeStart_s = "";
			for (int i = 0; i < StartTime.length; i++) {
				timeStart_s += StartTime[i].toString().trim();

			}
			timeStart = Float.parseFloat(timeStart_s.toString().trim());
		}
		if (StartTime2.length > 1) {
			String timeStart_s = "";
			for (int i = 0; i < StartTime2.length; i++) {
				timeStart_s += StartTime2[i].toString().trim();

			}
			timeStart = Float.parseFloat(timeStart_s.toString().trim());
		}

		return timeStart;
	}

	/**
	 * 獲取當前時間 年月日時分秒
	 * 
	 * @param 时间分割符号
	 *            ,例如:"/","-";
	 * @return 年月日时分秒;
	 */
	public static String GetTimeYMDhms(String a) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy" + a + "MM" + a + "dd"+ " HH:mm:ss");
		String iDate = df.format(new Date()).toString();
		return iDate;
	}

	/**
	 * 字符串轉時間
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}
}
