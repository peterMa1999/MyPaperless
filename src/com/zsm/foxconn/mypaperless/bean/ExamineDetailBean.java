package com.zsm.foxconn.mypaperless.bean;
/**
 * 保存点检信息数据的一个bean
 * @author F1330297
 *
 */
public class ExamineDetailBean {
	private String check_name, check_yeild, check_result, icarno,
			check_pro_name, check_content,numberstr,proid;


	public String getProid() {
		return proid;
	}

	public void setProid(String proid) {
		this.proid = proid;
	}

	public String getNumberstr() {
		return numberstr;
	}

	public void setNumberstr(String numberstr) {
		this.numberstr = numberstr;
	}

	public String getCheck_name() {
		return check_name;
	}

	public void setCheck_name(String check_name) {
		this.check_name = check_name;
	}

	public String getCheck_yeild() {
		return check_yeild;
	}

	public void setCheck_yeild(String check_yeild) {
		this.check_yeild = check_yeild;
	}

	public String getCheck_result() {
		return check_result;
	}

	public void setCheck_result(String check_result) {
		this.check_result = check_result;
	}

	public String getIcarno() {
		return icarno;
	}

	public void setIcarno(String icarno) {
		this.icarno = icarno;
	}

	public String getCheck_pro_name() {
		return check_pro_name;
	}

	public void setCheck_pro_name(String check_pro_name) {
		this.check_pro_name = check_pro_name;
	}

	public String getCheck_content() {
		return check_content;
	}

	public void setCheck_content(String check_content) {
		this.check_content = check_content;
	}

	/**
	 * 
	 * @param check_name
	 *            点检项目
	 * @param check_yeild
	 *            点检频率
	 * @param icarno
	 *            icarNO编号
	 * @param check_result
	 *            是否点检
	 * @param check_pro_name
	 *            点检细项
	 * @param check_content
	 *            点检记录内容
	 */
	public ExamineDetailBean(String check_name, String check_yeild,
			String icarno, String check_result, String check_pro_name,
			String check_content,String numberstr,String proid) {
		this.check_name = check_name;
		this.check_content = check_content;
		this.check_pro_name = check_pro_name;
		this.check_result = check_result;
		this.icarno = icarno;
		this.check_yeild = check_yeild;
		this.numberstr=numberstr;
		this.proid=proid;
	}

}
