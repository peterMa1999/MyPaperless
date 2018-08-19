package com.zsm.foxconn.mypaperless.bean;

public class CheckSit {
	
	private String report_num,report_name,
	mfg_name,floor_name,equipment,line_name,qr_image_info,bu,yeild_name,
	duty_by,check_flag,duty_chinesename,section;

	public String getReport_num() {
		return report_num;
	}

	public void setReport_num(String report_num) {
		this.report_num = report_num;
	}

	public String getReport_name() {
		return report_name;
	}

	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	public String getMfg_name() {
		return mfg_name;
	}

	public void setMfg_name(String mfg_name) {
		this.mfg_name = mfg_name;
	}

	public String getFloor_name() {
		return floor_name;
	}

	public void setFloor_name(String floor_name) {
		this.floor_name = floor_name;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getLine_name() {
		return line_name;
	}

	public void setLine_name(String line_name) {
		this.line_name = line_name;
	}

	public String getQr_image_info() {
		return qr_image_info;
	}

	public void setQr_image_info(String qr_image_info) {
		this.qr_image_info = qr_image_info;
	}

	public String getBu() {
		return bu;
	}

	public void setBu(String bu) {
		this.bu = bu;
	}

	public String getYeild_name() {
		return yeild_name;
	}

	public void setYeild_name(String yeild_name) {
		this.yeild_name = yeild_name;
	}

	public String getDuty_by() {
		return duty_by;
	}

	public void setDuty_by(String duty_by) {
		this.duty_by = duty_by;
	}

	public String getCheck_flag() {
		return check_flag;
	}

	public void setCheck_flag(String check_flag) {
		this.check_flag = check_flag;
	}

	public String getDuty_chinesename() {
		return duty_chinesename;
	}

	public void setDuty_chinesename(String duty_chinesename) {
		this.duty_chinesename = duty_chinesename;
	}
	
	public CheckSit(String report_num,String report_name,
			String mfg_name,String floor_name,String equipment,String line_name,
			String qr_image_info,String bu,String yeild_name,
			String duty_by,String section,String check_flag,String duty_chinesename){
		this.report_num = report_num;
		this.report_name = report_name;
		this.mfg_name = mfg_name;
		this.floor_name = floor_name;
		this.equipment = equipment;
		this.line_name = line_name;
		this.qr_image_info = qr_image_info;
		this.bu = bu;
		this.yeild_name = yeild_name;
		this.duty_by = duty_by;
		this.check_flag = check_flag;
		this.duty_chinesename = duty_chinesename;
		this.section = section;
	}
}
