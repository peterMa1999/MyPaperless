package com.zsm.foxconn.mypaperless.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * 二级条目模型
 * 
 * @author ZSM
 * 
 *         2016-1-12
 */

@Table(name="childmodel")
public class ChildModel {
	@Id(column="Id")
	private int id;
	private String childTitle;//二级条目
	private String proId;//二级条目Id
	private String childInputFlag;
	private String childContent;//填写内容
	private String childResult;//是/否
	private String childIcar;
	private String Logonname;	
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

	public String getLogonname() {
		return Logonname;
	}
	public void setLogonname(String logonname) {
		Logonname = logonname;
	}
	public String getReport_num() {
		return report_num;
	}
	public void setReport_num(String report_num) {
		this.report_num = report_num;
	}
	
	
	public String getChildInputFlag() {
		return childInputFlag;
	}
	public void setChildInputFlag(String childInputFlag) {
		this.childInputFlag = childInputFlag;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	public String getChildTitle() {
		return childTitle;
	}
	public void setChildTitle(String childTitle) {
		this.childTitle = childTitle;
	}
	public String getChildContent() {
		return childContent;
	}
	public void setChildContent(String childContent) {
		this.childContent = childContent;
	}
	public String getChildResult() {
		return childResult;
	}
	public void setChildResult(String childResult) {
		this.childResult = childResult;
	}
	public String getChildIcar() {
		return childIcar;
	}
	public void setChildIcar(String childIcar) {
		this.childIcar = childIcar;
	}
	
	
	@Override
	public String toString() {
		return "ChildModel [childTitle=" + childTitle + ", proId=" + proId
				+ ", childInputFlag=" + childInputFlag + ", childContent="
				+ childContent + ", childResult=" + childResult
				+ ", childIcar=" + childIcar + ", Logonname=" + Logonname
				+ ", report_num=" + report_num + "]";
	}

}
