package com.zsm.foxconn.mypaperless.bean;
/**
 * 保存所获取的有权限报表信息的数据的一个bean
 * @author F1330297
 *
 */
public class Picture {
	public String reportId;
	public String reportName;
	private String reportflag;

	public String getReportId() {
		return reportId;
	}

	public String getReportflag() {
		return reportflag;
	}

	public void setReportflag(String reportflag) {
		this.reportflag = reportflag;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Picture(String reportId, String reportName, String reportflag) {
		this.reportName = reportName;
		this.reportId = reportId;
		this.reportflag = reportflag;
	}
}
