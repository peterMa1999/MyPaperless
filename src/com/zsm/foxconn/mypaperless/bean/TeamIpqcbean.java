package com.zsm.foxconn.mypaperless.bean;

public class TeamIpqcbean {
	private String Floors, Line_style, SITE_NAME, BU, MFG_NAME;

	public String getFloors() {
		return Floors;
	}

	public void setFloors(String floors) {
		Floors = floors;
	}

	public String getLine_style() {
		return Line_style;
	}

	public void setLine_style(String line_style) {
		Line_style = line_style;
	}

	public String getSITE_NAME() {
		return SITE_NAME;
	}

	public void setSITE_NAME(String sITE_NAME) {
		SITE_NAME = sITE_NAME;
	}

	public String getBU() {
		return BU;
	}

	public void setBU(String bU) {
		BU = bU;
	}

	public String getMFG_NAME() {
		return MFG_NAME;
	}

	public void setMFG_NAME(String mFG_NAME) {
		MFG_NAME = mFG_NAME;
	}

	/**
	 * 标识是否可以删除
	 */
	private boolean canRemove = true;

	public boolean isCanRemove() {
		return canRemove;
	}

	public void setCanRemove(boolean canRemove) {
		this.canRemove = canRemove;
	}

	public TeamIpqcbean() {

	}

	public TeamIpqcbean(String Floors, String Line_style, String SITE_NAME,
			String BU, String MFG_NAME, boolean canRemove) {
		this.Floors = Floors;
		this.Line_style = Line_style;
		this.SITE_NAME = SITE_NAME;
		this.BU = BU;
		this.MFG_NAME = MFG_NAME;
		this.canRemove = canRemove;
	}
}
