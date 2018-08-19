package com.zsm.foxconn.mypaperless.bean;

public class Canshu_bean {
	private String numberorder, jizhong_num, floor_name, line_name,
			create_date;
	private String parameternum,updatetype,create_by,check_state,check_by,check_date;

	public String getParameternum() {
		return parameternum;
	}

	public void setParameternum(String parameternum) {
		this.parameternum = parameternum;
	}

	public String getUpdatetype() {
		return updatetype;
	}

	public void setUpdatetype(String updatetype) {
		this.updatetype = updatetype;
	}

	public String getCreate_by() {
		return create_by;
	}

	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}

	public String getCheck_state() {
		return check_state;
	}

	public void setCheck_state(String check_state) {
		this.check_state = check_state;
	}

	public String getCheck_by() {
		return check_by;
	}

	public void setCheck_by(String check_by) {
		this.check_by = check_by;
	}

	public String getCheck_date() {
		return check_date;
	}

	public void setCheck_date(String check_date) {
		this.check_date = check_date;
	}

	public String getNumberorder() {
		return numberorder;
	}

	public void setNumberorder(String numberorder) {
		this.numberorder = numberorder;
	}

	public String getJizhong_num() {
		return jizhong_num;
	}

	public void setJizhong_num(String jizhong_num) {
		this.jizhong_num = jizhong_num;
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

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public Canshu_bean(String numberorder, String jizhong_num,
			String floor_name, String line_name, String create_date) {
		this.numberorder = numberorder;
		this.jizhong_num = jizhong_num;
		this.floor_name = floor_name;
		this.line_name = line_name;
		this.create_date = create_date;
	}
	
	
	public Canshu_bean(String numberorder,String parameternum,String jizhong_num,String updatetype,String create_by,
			String create_date,String check_by){
		this.numberorder = numberorder;
		this.parameternum = parameternum; 
		this.jizhong_num = jizhong_num;
		this.updatetype = updatetype;
		this.create_by = create_by;
		this.create_date = create_date;
		this.check_by = check_by;
	}
	
	public Canshu_bean(String numberorder,String parameternum,String jizhong_num,String create_by,
			String create_date,String check_state,String check_by,String check_date){
		this.numberorder = numberorder;
		this.parameternum = parameternum; 
		this.jizhong_num = jizhong_num;
		this.check_state = check_state;
		this.create_by = create_by;
		this.create_date = create_date;
		this.check_by = check_by;
		this.check_date = check_date;
	}
	
	public Canshu_bean(String numberorder,String parameternum,String jizhong_num,String check_state,
			String check_by,String check_date){
		this.numberorder = numberorder;
		this.parameternum = parameternum;
		this.jizhong_num = jizhong_num;
		this.check_state = check_state;
		this.check_by = check_by;
		this.check_date = check_date;
	}

}
