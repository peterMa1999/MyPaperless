package com.zsm.foxconn.mypaperless.bean;

public class Line_childItem {
	String floorname;
	String linename;
	
	public void setchilditem(String floorname,String linename){
		this.floorname = floorname;
		this.linename = linename;
	}
	
	public void setFloorname(String floorname) {
		this.floorname = floorname;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	public String getFloorname() {
		return floorname;
	}
	public String getLinename() {
		return linename;
	}
	
	
}
