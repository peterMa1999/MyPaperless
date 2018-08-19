package com.zsm.foxconn.mypaperless.bean;

/**
 * 二级条目模型
 * 
 * @author ZSM
 * 
 *         2016-1-12
 */
public class EXpandaChildModel {
	private String childTitle;
	private String childpro_id;
	private String childgrouptitle;
	private boolean ischoosePZ;
	
	
	
	public boolean isIschoosePZ() {
		return ischoosePZ;
	}
	public void setIschoosePZ(boolean ischoosePZ) {
		this.ischoosePZ = ischoosePZ;
	}
	public String getChildgrouptitle() {
		return childgrouptitle;
	}
	public void setChildgrouptitle(String childgrouptitle) {
		this.childgrouptitle = childgrouptitle;
	}
	public String getChildpro_id() {
		return childpro_id;
	}
	public void setChildpro_id(String childpro_id) {
		this.childpro_id = childpro_id;
	}
	public String getChildTitle() {
		return childTitle;
	}
	public void setChildTitle(String childTitle) {
		this.childTitle = childTitle;
	}
	
}
