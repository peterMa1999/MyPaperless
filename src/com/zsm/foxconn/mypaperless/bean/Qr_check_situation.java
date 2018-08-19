package com.zsm.foxconn.mypaperless.bean;

public class Qr_check_situation {
	private String floorName, reportNum, lineName, site, mfg, sbu,
	is_input_order,whether_check,check_name,section,rpt;

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getReportNum() {
		return reportNum;
	}

	public void setReportNum(String reportNum) {
		this.reportNum = reportNum;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getMfg() {
		return mfg;
	}

	public void setMfg(String mfg) {
		this.mfg = mfg;
	}

	public String getSbu() {
		return sbu;
	}

	public void setSbu(String sbu) {
		this.sbu = sbu;
	}

	public String getIs_input_order() {
		return is_input_order;
	}

	public void setIs_input_order(String is_input_order) {
		this.is_input_order = is_input_order;
	}

	public String getWhether_check() {
		return whether_check;
	}

	public void setWhether_check(String whether_check) {
		this.whether_check = whether_check;
	}

	public String getCheck_name() {
		return check_name;
	}

	public void setCheck_name(String check_name) {
		this.check_name = check_name;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getRpt() {
		return rpt;
	}

	public void setRpt(String rpt) {
		this.rpt = rpt;
	}
	
	public Qr_check_situation(String floorName,String lineName,String reportNum,String site,
			String mfg,String sbu,String rpt,String section,
			String is_input_order,String whether_check,String check_name){
		this.floorName = floorName;
		this.reportNum = reportNum;
		this.lineName = lineName;
		this.site = site;
		this.mfg = mfg;
		this.sbu = sbu;
		this.is_input_order = is_input_order;
		this.whether_check = whether_check;
		this.check_name = check_name;
		this.section = section;
		this.rpt = rpt;
	}
}
