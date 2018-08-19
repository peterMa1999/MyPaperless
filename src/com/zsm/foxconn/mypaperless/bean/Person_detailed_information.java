package com.zsm.foxconn.mypaperless.bean;

import android.app.Application;
import android.content.Context;

public class Person_detailed_information extends Application {
	private String Job_number, Chinese_name, English_name, Duties, Telephone,
			EMail, Executive_director, Industry_title, Division_BU,
			Business_name, BU, BU_again, Keeping_personnel, Recording_time,
			UserLevel;
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

	public String getEnglish_name() {
		return English_name;
	}

	public void setEnglish_name(String english_name) {
		English_name = english_name;
	}

	public String getDuties() {
		return Duties;
	}

	public void setDuties(String duties) {
		Duties = duties;
	}

	public String getTelephone() {
		return Telephone;
	}

	public void setTelephone(String telephone) {
		Telephone = telephone;
	}

	public String getEMail() {
		return EMail;
	}

	public void setEMail(String eMail) {
		EMail = eMail;
	}

	public String getExecutive_director() {
		return Executive_director;
	}

	public void setExecutive_director(String executive_director) {
		Executive_director = executive_director;
	}

	public String getIndustry_title() {
		return Industry_title;
	}

	public void setIndustry_title(String industry_title) {
		Industry_title = industry_title;
	}

	public String getDivision_BU() {
		return Division_BU;
	}

	public void setDivision_BU(String division_BU) {
		Division_BU = division_BU;
	}

	public String getBusiness_name() {
		return Business_name;
	}

	public void setBusiness_name(String business_name) {
		Business_name = business_name;
	}

	public String getBU() {
		return BU;
	}

	public void setBU(String bu) {
		BU = bu;
	}

	public String getBU_again() {
		return BU_again;
	}

	public void setBU_again(String bu_again) {
		BU_again = bu_again;
	}

	public String getKeeping_personnel() {
		return Keeping_personnel;
	}

	public void setKeeping_personnel(String keeping_personnel) {
		Keeping_personnel = keeping_personnel;
	}

	public String getRecording_time() {
		return Recording_time;
	}

	public void setRecording_time(String recording_time) {
		Recording_time = recording_time;
	}

	public String getUserLevel() {
		return UserLevel;
	}

	public void setUserLevel(String userLevel) {
		UserLevel = userLevel;
	}

	public Person_detailed_information(String Job_number, String Chinese_name,
			String English_name, String Duties, String Telephone, String EMail,
			String Executive_director, String Industry_title,
			String Division_BU, String Business_name, String BU,
			String BU_again, String Keeping_personnel, String Recording_time,
			String UserLevel)

	{
		this.Job_number = Job_number;
		this.Chinese_name = Chinese_name;
		this.English_name = English_name;
		this.Duties = Duties;
		this.Telephone = Telephone;
		this.EMail = EMail;
		this.Executive_director = Executive_director;
		this.Industry_title = Industry_title;
		this.Division_BU = Division_BU;
		this.Business_name = Business_name;
		this.BU = BU;
		this.BU_again = BU_again;
		this.Keeping_personnel = Keeping_personnel;
		this.Recording_time = Recording_time;
		this.UserLevel = UserLevel;
	}

	public Person_detailed_information(Context applicationContext) {
		// TODO Auto-generated constructor stub
	}
}
