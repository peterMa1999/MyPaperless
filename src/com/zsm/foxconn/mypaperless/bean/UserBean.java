package com.zsm.foxconn.mypaperless.bean;

import java.io.Serializable;

import android.R.string;
import android.app.Application;

public class UserBean extends Application implements Serializable
{
		
		public boolean flag=false;
		public boolean isNightMode;
		
		
	
		public boolean isNightMode() {
			return isNightMode;
		}

		public void setNightMode(boolean isNightMode) {
			this.isNightMode = isNightMode;
		}

		public boolean isFlag() {
			return flag;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}
		private String  LogonName=null;
		private String PassWord,
		ChineseName,EnglishName,Title,Ext,
		Email,UserLevel,Master,Team,Section,
		SBU,MFG,BU,Site,
		BG;

		public String getLogonName() {
			return LogonName;
		}

		public void setLogonName(String logonName) {
			LogonName = logonName;
		}

		public String getPassWord() {
			return PassWord;
		}

		public void setPassWord(String passWord) {
			PassWord = passWord;
		}

		public String getChineseName() {
			return ChineseName;
		}

		public void setChineseName(String chineseName) {
			ChineseName = chineseName;
		}

		public String getEnglishName() {
			return EnglishName;
		}

		public void setEnglishName(String englishName) {
			EnglishName = englishName;
		}

		public String getTitle() {
			return Title;
		}

		public void setTitle(String title) {
			Title = title;
		}

		public String getExt() {
			return Ext;
		}

		public void setExt(String ext) {
			Ext = ext;
		}

		public String getEmail() {
			return Email;
		}

		public void setEmail(String email) {
			Email = email;
		}

		public String getUserLevel() {
			return UserLevel;
		}

		public void setUserLevel(String userLevel) {
			UserLevel = userLevel;
		}

		public String getMaster() {
			return Master;
		}

		public void setMaster(String master) {
			Master = master;
		}

		public String getTeam() {
			return Team;
		}

		public void setTeam(String team) {
			Team = team;
		}

		public String getSection() {
			return Section;
		}

		public void setSection(String section) {
			Section = section;
		}

		public String getSBU() {
			return SBU;
		}

		public void setSBU(String sBU) {
			SBU = sBU;
		}

		public String getMFG() {
			return MFG;
		}

		public void setMFG(String mFG) {
			MFG = mFG;
		}

		public String getBU() {
			return BU;
		}

		public void setBU(String bU) {
			BU = bU;
		}

		public String getSite() {
			return Site;
		}

		public void setSite(String site) {
			Site = site;
		}


		public String getBG() {
			return BG;
		}

		public void setBG(String bG) {
			BG = bG;
		}
		public void insert(String LogonName,String PassWord,
				String ChineseName,String EnglishName,String Title,String Ext,
				String Email,String UserLevel,String Master,String Team,String Section,
				String SBU,String MFG,String BU,String Site,
				String BG) 
		{
			this.LogonName=LogonName;
			this.PassWord=PassWord;
			this.ChineseName=ChineseName;
			this.EnglishName=EnglishName;
			this.Title=Title;
			this.Ext=Ext;
			this.Email=Email;
			this.UserLevel=UserLevel;
			this.Master=Master;
			this.Team=Team;
			this.Section=Section;
			this.SBU=SBU;
			this.MFG=MFG;
			this.BU=BU;
			this.Site=Site;
			this.BG=BG;
		}
		public  void resolve() 
		{
			this.LogonName="";
			this.PassWord="";
			this.ChineseName="";
			this.EnglishName="";
			this.Title="";
			this.Ext="";
			this.Email="";
			this.UserLevel="";
			this.Master="";
			this.Team="";
			this.Section="";
			this.SBU="";
			this.MFG="";
			this.BU="";
			this.Site="";
			this.BG="";
		}
}
