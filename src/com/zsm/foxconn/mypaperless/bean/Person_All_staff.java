package com.zsm.foxconn.mypaperless.bean;

public class Person_All_staff {
	private String Job_number, Chinese_name, Duties, Executive_director;

	public String getJob_number() {
		return Job_number;
	}

	public void setJob_number(String job_number) {
		Job_number = job_number;
	}

	public String getChinese_name() {
		return Chinese_name;
	}

	public void setChinese_name(String chinese_name) {
		Chinese_name = chinese_name;
	}

	public String getDuties() {
		return Duties;
	}

	public void setDuties(String duties) {
		Duties = duties;
	}

	public String getExecutive_director() {
		return Executive_director;
	}

	public void setExecutive_director(String executive_director) {
		Executive_director = executive_director;
	}

	public Person_All_staff(String Job_number, String Chinese_name,
			String Duties, String Executive_director) {
		this.Job_number = Job_number;
		this.Chinese_name = Chinese_name;
		this.Duties = Duties;
		this.Executive_director = Executive_director;
	}
}
