package com.zsm.foxconn.mypaperless.database;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * 
 * @author MPG
 *	
 * 2016-12-23 下午3:22:30
 * APP_yindao_page 操作引導頁標示
 */
@Table(name="APP_yindao_page") 
public class APP_yindao_page {
	
	@Id(column="Id")
	private int id;
	private String pagename;
	private int pagename_id;
	private boolean iscooled;
	
	
	public boolean isIscooled() {
		return iscooled;
	}
	public void setIscooled(boolean iscooled) {
		this.iscooled = iscooled;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPagename() {
		return pagename;
	}
	public void setPagename(String pagename) {
		this.pagename = pagename;
	}
	public int getPagename_id() {
		return pagename_id;
	}
	public void setPagename_id(int pagename_id) {
		this.pagename_id = pagename_id;
	}
	
	
}
