package com.zsm.foxconn.mypaperless.database;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="user_forgetpwd")
public class Forgetpwd_table {
	@Id(column="Id")
	private int id;
	private String usernum;
	private String forget_time;
	private int is_sucessful;
	private int sendnum;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsernum() {
		return usernum;
	}
	public void setUsernum(String usernum) {
		this.usernum = usernum;
	}
	public String getForget_time() {
		return forget_time;
	}
	public void setForget_time(String forget_time) {
		this.forget_time = forget_time;
	}
	public int getIs_sucessful() {
		return is_sucessful;
	}
	public void setIs_sucessful(int is_sucessful) {
		this.is_sucessful = is_sucessful;
	}
	public int getSendnum() {
		return sendnum;
	}
	public void setSendnum(int sendnum) {
		this.sendnum = sendnum;
	}
	
}
