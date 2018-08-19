package com.zsm.foxconn.mypaperless.bean;

import java.io.Serializable;

/**
 * 保存全部信息页的数据的一个bean
 * @author F1330297
 *
 */
public class ExamineAllMessageTableBean implements Serializable{

	private String check_report_NO, create_Date, check_seqno, check_release,
			examine_person, check_reject, numberorder, status,whether_check;

	public String getWhether_check() {
		return whether_check;
	}
	
	public void setWhether_check(String whether_check) {
		this.whether_check = whether_check;
	}
	public String getNumberorder() {
		return numberorder;
	}

	public void setNumberorder(String numberorder) {
		this.numberorder = numberorder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCheck_report_NO() {
		return check_report_NO;
	}

	public String getExamine_person() {
		return examine_person;
	}

	public void setExamine_person(String examine_person) {
		this.examine_person = examine_person;
	}

	public void setCheck_report_NO(String check_report_NO) {
		this.check_report_NO = check_report_NO;
	}

	public String getCreate_Date() {
		return create_Date;
	}

	public void setCreate_Date(String create_Date) {
		this.create_Date = create_Date;
	}

	public String getCheck_seqno() {
		return check_seqno;
	}

	public void setCheck_seqno(String check_seqno) {
		this.check_seqno = check_seqno;
	}

	public String getCheck_release() {
		return check_release;
	}

	public void setCheck_release(String check_release) {
		this.check_release = check_release;
	}

	public String getCheck_reject() {
		return check_reject;
	}

	public void setCheck_reject(String check_reject) {
		this.check_reject = check_reject;
	}

	public ExamineAllMessageTableBean(String check_report_NO,
			String create_Date, String examine_person, String check_seqno,
			String check_release, String check_reject, String numberorder) {
		this.examine_person = examine_person;
		this.check_reject = check_reject;
		this.create_Date = create_Date;
		this.check_release = check_release;
		this.check_report_NO = check_report_NO;
		this.check_seqno = check_seqno;
		this.numberorder = numberorder;
	}

	/**
	 * 
	 * @param check_report_NO
	 *            点检编号
	 * @param create_Date
	 *            创建时间
	 * @param examine_person
	 *            审核人
	 * @param status
	 *            审核状态
	 * @param numberorder
	 *            序列
	 */
	public ExamineAllMessageTableBean(String check_report_NO,
			String create_Date, String examine_person, String status,
			String numberorder) {
		this.examine_person = examine_person;
		this.create_Date = create_Date;
		this.check_report_NO = check_report_NO;
		this.numberorder = numberorder;
		this.status = status;
	}

	public ExamineAllMessageTableBean(String check_report_NO,
			String create_Date, String examine_person, String status,
			String numberorder,String whether_check) {
		this.examine_person = examine_person;
		this.create_Date = create_Date;
		this.check_report_NO = check_report_NO;
		this.numberorder = numberorder;
		this.status = status;
		this.whether_check = whether_check;
	}
	
	
}
