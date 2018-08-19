package com.zsm.foxconn.mypaperless.bean;

public class CheckSituation {
	
	private String report_name,floor_name,line_name,create_data,shift_name,createby_name,
	createby_num,report_num,check_num;

	public String getReport_name() {
		return report_name;
	}

	public void setReport_name(String report_name) {
		this.report_name = report_name;
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

	public String getCreate_data() {
		return create_data;
	}

	public void setCreate_data(String create_data) {
		this.create_data = create_data;
	}

	public String getShift_name() {
		return shift_name;
	}

	public void setShift_name(String shift_name) {
		this.shift_name = shift_name;
	}

	public String getCreateby_name() {
		return createby_name;
	}

	public void setCreateby_name(String createby_name) {
		this.createby_name = createby_name;
	}

	public String getCreateby_num() {
		return createby_num;
	}

	public void setCreateby_num(String createby_num) {
		this.createby_num = createby_num;
	}

	public String getReport_num() {
		return report_num;
	}

	public void setReport_num(String report_num) {
		this.report_num = report_num;
	}

	public String getCheck_num() {
		return check_num;
	}

	public void setCheck_num(String check_num) {
		this.check_num = check_num;
	}
	
	public CheckSituation(String report_name,String floor_name,String line_name,String create_data,
			String shift_name,String createby_name,String createby_num,String report_num,String check_num){
		this.report_name = report_name;
		this.floor_name = floor_name;
		this.line_name = line_name;
		this.create_data = create_data;
		this.shift_name = shift_name;
		this.createby_name = createby_name;
		this.createby_num = createby_num;
		this.report_num = report_num;
		this.check_num = check_num;
	}
}
