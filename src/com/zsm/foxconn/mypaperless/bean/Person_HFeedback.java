package com.zsm.foxconn.mypaperless.bean;

public class Person_HFeedback {
	private String ChineseName,Lasteditdt, Feedback;

	public String getChineseName() {
		return ChineseName;
	}

	public void setChineseName(String chineseName) {
		ChineseName = chineseName;
	}
	
	public String getLasteditdt() {
		return Lasteditdt;
	}

	public void setLasteditdt(String lasteditdt) {
		Lasteditdt = lasteditdt;
	}

	public String getFeedback() {
		return Feedback;
	}

	public void setFeedback(String feedback) {
		Feedback = feedback;
	}

	public Person_HFeedback(String ChineseName,String Lasteditdt, String Feedback) {
		this.ChineseName=ChineseName;
		this.Lasteditdt = Lasteditdt;
		this.Feedback = Feedback;
	}
}
