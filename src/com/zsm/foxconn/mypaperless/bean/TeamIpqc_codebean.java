package com.zsm.foxconn.mypaperless.bean;

public class TeamIpqc_codebean {
	private String Report_name, Report_number, Line_level, Control_no,
			SITE_NAME, BU, MFG_NAME, FlooR_NAME, QR_IMAGE_INFO, is_input_order;

	public String getQR_IMAGE_INFO() {
		return QR_IMAGE_INFO;
	}

	public String getIs_input_order() {
		return is_input_order;
	}

	public void setIs_input_order(String is_input_order) {
		this.is_input_order = is_input_order;
	}

	public void setQR_IMAGE_INFO(String qR_IMAGE_INFO) {
		QR_IMAGE_INFO = qR_IMAGE_INFO;
	}

	public String getReport_name() {
		return Report_name;
	}

	public void setReport_name(String report_name) {
		Report_name = report_name;
	}

	public String getReport_number() {
		return Report_number;
	}

	public void setReport_number(String report_number) {
		Report_number = report_number;
	}

	public String getLine_level() {
		return Line_level;
	}

	public void setLine_level(String line_level) {
		Line_level = line_level;
	}

	public String getControl_no() {
		return Control_no;
	}

	public void setControl_no(String control_no) {
		Control_no = control_no;
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

	public String getFlooR_NAME() {
		return FlooR_NAME;
	}

	public void setFlooR_NAME(String flooR_NAME) {
		FlooR_NAME = flooR_NAME;
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

	public TeamIpqc_codebean() {

	}

	public TeamIpqc_codebean(String Report_name, String Report_number,
			String Line_level, String Control_no, String SITE_NAME, String BU,
			String MFG_NAME, String FlooR_NAME, String QR_IMAGE_INFO,
			boolean canRemove) {
		this.Report_name = Report_name;
		this.Report_number = Report_number;
		this.Line_level = Line_level;
		this.Control_no = Control_no;
		this.SITE_NAME = SITE_NAME;
		this.BU = BU;
		this.MFG_NAME = MFG_NAME;
		this.FlooR_NAME = FlooR_NAME;
		this.QR_IMAGE_INFO = QR_IMAGE_INFO;
		this.canRemove = canRemove;
	}
}
