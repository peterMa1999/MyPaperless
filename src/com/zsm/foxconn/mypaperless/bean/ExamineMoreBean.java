package com.zsm.foxconn.mypaperless.bean;
/**
 * 保存详细信息页数据的一个bean
 * @author F1330297
 *
 */
public class ExamineMoreBean {
	private String nodeNB, checkNB, producePerson, produceTime, checkLabel,
			business, floor, lineDiffent, listNB, matter_NB, reportSum, faceNB,
			diffError, labelReasons,check_order;
	
	
	public String getCheck_order() {
		return check_order;
	}

	public void setCheck_order(String check_order) {
		this.check_order = check_order;
	}

	private String state,statusnext;

	public String getStatusnext() {
		return statusnext;
	}

	public void setStatusnext(String statusnext) {
		this.statusnext = statusnext;
	}

	public String getNodeNB() {
		return nodeNB;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setNodeNB(String nodeNB) {
		this.nodeNB = nodeNB;
	}

	// public String getCheck_seqno() {
	// return check_seqno;
	// }
	//
	// public void setCheck_seqno(String check_seqno) {
	// this.check_seqno = check_seqno;
	// }
	//
	// public String getCheck_release() {
	// return check_release;
	// }
	//
	// public void setCheck_release(String check_release) {
	// this.check_release = check_release;
	// }
	//
	// public String getCheck_reject() {
	// return check_reject;
	// }
	//
	// public void setCheck_reject(String check_reject) {
	// this.check_reject = check_reject;
	// }

	public String getCheckNB() {
		return checkNB;
	}

	public void setCheckNB(String checkNB) {
		this.checkNB = checkNB;
	}

	public String getProducePerson() {
		return producePerson;
	}

	public void setProducePerson(String producePerson) {
		this.producePerson = producePerson;
	}

	public String getProduceTime() {
		return produceTime;
	}

	public void setProduceTime(String produceTime) {
		this.produceTime = produceTime;
	}

	public String getCheckLabel() {
		return checkLabel;
	}

	public void setCheckLabel(String checkLabel) {
		this.checkLabel = checkLabel;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getLineDiffent() {
		return lineDiffent;
	}

	public void setLineDiffent(String lineDiffent) {
		this.lineDiffent = lineDiffent;
	}

	public String getListNB() {
		return listNB;
	}

	public void setListNB(String listNB) {
		this.listNB = listNB;
	}

	public String getMatter_NB() {
		return matter_NB;
	}

	public void setMatter_NB(String matter_NB) {
		this.matter_NB = matter_NB;
	}

	public String getReportSum() {
		return reportSum;
	}

	public void setReportSum(String reportSum) {
		this.reportSum = reportSum;
	}

	public String getFaceNB() {
		return faceNB;
	}

	public void setFaceNB(String faceNB) {
		this.faceNB = faceNB;
	}

	public String getDiffError() {
		return diffError;
	}

	public void setDiffError(String diffError) {
		this.diffError = diffError;
	}

	public String getLabelReasons() {
		return labelReasons;
	}

	public void setLabelReasons(String labelReasons) {
		this.labelReasons = labelReasons;
	}

	public ExamineMoreBean(String nodeNB, String checkNB, String producePerson,
			String produceTime, String checkLabel, String state,
			String business, String floor, String lineDiffent, String listNB,
			String matter_NB, String reportSum, String faceNB,
			String diffError, String labelReasons) {

		this.business = business;
		this.checkLabel = checkLabel;
		this.checkNB = checkNB;
		this.diffError = diffError;
		this.faceNB = faceNB;
		this.floor = floor;
		this.labelReasons = labelReasons;
		this.lineDiffent = lineDiffent;
		this.listNB = listNB;
		this.matter_NB = matter_NB;
		this.nodeNB = nodeNB;
		this.producePerson = producePerson;
		this.produceTime = produceTime;
		this.reportSum = reportSum;
		// this.check_seqno = check_seqno;
		// this.check_release = check_release;
		// this.check_reject = check_reject;

	}

	/**
	 * 
	 * @param business
	 *            事业处
	 * @param floor
	 *            楼层
	 * @param listNB
	 *            工单
	 * @param lineDiffent
	 *            线别
	 * @param matter_NB
	 *            工单对应料号
	 * @param reportSum
	 *            工单数量
	 * @param diffError
	 *            误差
	 * @param faceNB
	 *            面别
	 * @param labelReasons
	 *            标注理由
	 * @param checkLabel
	 *            点检标注
	 * @param produceTime
	 *            创建时间
	 * @param statusnext
	 *            签核状态》》为下一页判断页面UI
	 */
	public ExamineMoreBean(String business, String floor, String listNB,
			String lineDiffent, String matter_NB, String reportSum,
			String diffError, String faceNB, String labelReasons,
			String checkLabel, String produceTime,String statusnext) {

		this.business = business;
		this.diffError = diffError;
		this.faceNB = faceNB;
		this.floor = floor;
		this.labelReasons = labelReasons;
		this.lineDiffent = lineDiffent;
		this.listNB = listNB;
		this.matter_NB = matter_NB;
		this.reportSum = reportSum;
		this.checkLabel = checkLabel;
		this.produceTime = produceTime;
		this.statusnext=statusnext;
	}

	/**
	 * 
	 * @param nodeNB
	 *            节数
	 * @param checkNB
	 *            点检编号
	 * @param producePerson
	 *            创建人
	 * @param state
	 *            审核状态
	 */
	public ExamineMoreBean(String nodeNB, String checkNB, String producePerson,
			String state,String check_order) {

		this.checkNB = checkNB;
		this.nodeNB = nodeNB;
		this.producePerson = producePerson;
		this.state = state;
		this.check_order = check_order;
		// this.check_seqno = check_seqno;
		// this.check_release = check_release;
		// this.check_reject = check_reject;
	}
}
