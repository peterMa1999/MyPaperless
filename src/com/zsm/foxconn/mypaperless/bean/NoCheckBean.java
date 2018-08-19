package com.zsm.foxconn.mypaperless.bean;

public class NoCheckBean {
	private String mfg_name,section_name,floor_name,line_name,daynight,dates,numbers;

	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	public String getDaynight() {
		return daynight;
	}

	public void setDaynight(String daynight) {
		this.daynight = daynight;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public String getMfg_name() {
		return mfg_name;
	}

	public void setMfg_name(String mfg_name) {
		this.mfg_name = mfg_name;
	}

	public String getSection_name() {
		return section_name;
	}

	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}

	public String getFloor_name() {
		return floor_name;
	}

	public void setFloor_name(String floor_name) {
		this.floor_name = floor_name;
	}

	public String getLine_name() {
		return line_name;
	}

	public void setLine_name(String line_name) {
		this.line_name = line_name;
	}
	public  NoCheckBean(String mfg_name,String section_name,String floor_name,String line_name,String daynight,String dates,String numbers) {
		this.mfg_name=mfg_name;
		this.section_name=section_name;
		this.floor_name=floor_name;
		this.line_name=line_name;
		this.daynight=daynight;
		this.dates=dates;
		this.numbers=numbers;
	}
}
