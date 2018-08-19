package com.zsm.foxconn.mypaperless.bean;

public class Issue_bean {
	
	private String BU_NAME,Occurred,RootCause,FailureCode,Location,Section,Issue_Flag,Create_Date,LastEditBy;

	
	
	public String getBU_NAME() {
		return BU_NAME;
	}

	public void setBU_NAME(String bU_NAME) {
		BU_NAME = bU_NAME;
	}

	public String getOccurred() {
		return Occurred;
	}

	public void setOccurred(String occurred) {
		Occurred = occurred;
	}

	public String getRootCause() {
		return RootCause;
	}

	public void setRootCause(String rootCause) {
		RootCause = rootCause;
	}

	public String getFailureCode() {
		return FailureCode;
	}

	public void setFailureCode(String failureCode) {
		FailureCode = failureCode;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getSection() {
		return Section;
	}

	public void setSection(String section) {
		Section = section;
	}

	public String getIssue_Flag() {
		return Issue_Flag;
	}

	public void setIssue_Flag(String issue_Flag) {
		Issue_Flag = issue_Flag;
	}

	public String getCreate_Date() {
		return Create_Date;
	}

	public void setCreate_Date(String create_Date) {
		Create_Date = create_Date;
	}

	public String getLastEditBy() {
		return LastEditBy;
	}

	public void setLastEditBy(String lastEditBy) {
		LastEditBy = lastEditBy;
	}

	public Issue_bean(String occurred, String rootCause, String failureCode,
			String location, String section) {
		super();
		Occurred = occurred;
		RootCause = rootCause;
		FailureCode = failureCode;
		Location = location;
		Section = section;
	}

	public Issue_bean(String occurred, String rootCause, String failureCode,
			String location, String section, String issue_Flag,
			String create_Date, String lastEditBy) {
		super();
		Occurred = occurred;
		RootCause = rootCause;
		FailureCode = failureCode;
		Location = location;
		Section = section;
		Issue_Flag = issue_Flag;
		Create_Date = create_Date;
		LastEditBy = lastEditBy;
	}

	public Issue_bean(String bU_NAME, String issue_Flag, String create_Date,
			String lastEditBy) {
		super();
		BU_NAME = bU_NAME;
		Issue_Flag = issue_Flag;
		Create_Date = create_Date;
		LastEditBy = lastEditBy;
	}
	
	
}
