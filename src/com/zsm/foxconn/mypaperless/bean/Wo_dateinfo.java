package com.zsm.foxconn.mypaperless.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;


@Table(name="Wo_dateinfo")
public class Wo_dateinfo {

	@Id(column="Id")
	private int id;
	private String wo;
	private String jizhong_num;
	private String rev;
	private int piliang;
	private String deviation;
	private String side;
	private String logonname;
	private String report_num;
	private String floor_name;
	private String line_name;
	
	
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWo() {
		return wo;
	}
	public void setWo(String wo) {
		this.wo = wo;
	}
	public String getJizhong_num() {
		return jizhong_num;
	}
	public void setJizhong_num(String jizhong_num) {
		this.jizhong_num = jizhong_num;
	}
	public String getRev() {
		return rev;
	}
	public void setRev(String rev) {
		this.rev = rev;
	}
	public int getPiliang() {
		return piliang;
	}
	public void setPiliang(int piliang) {
		this.piliang = piliang;
	}
	public String getDeviation() {
		return deviation;
	}
	public void setDeviation(String deviation) {
		this.deviation = deviation;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public String getLogonname() {
		return logonname;
	}
	public void setLogonname(String logonname) {
		this.logonname = logonname;
	}
	public String getReport_num() {
		return report_num;
	}
	public void setReport_num(String report_num) {
		this.report_num = report_num;
	}
}
