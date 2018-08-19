package com.zsm.foxconn.mypaperless.base;

import android.app.Application;
import android.content.res.Configuration;

public class BaseApplication extends Application {

	private String logonname = null;
	private String password = null;
	private String chinesename = null;
	private String englishname = null;
	private String title = null;
	private String ext = null;
	private String email = null;
	private String userlevel = null;
	private String optionid = null;
	private String master = null;
	private boolean isNightMode;

	public boolean isNightMode() {
		return isNightMode;
	}

	public void setNightMode(boolean isNightMode) {
		this.isNightMode = isNightMode;
	}

	public void setResult(String logonname, String password,
			String chinesename, String englishname, String title, String ext,
			String email, String userlevel, String optionid, String master) {
		this.logonname = logonname;
		this.password = password;
		this.chinesename = chinesename;
		this.englishname = englishname;
		this.title = title;
		this.ext = ext;
		this.email = email;
		this.userlevel = userlevel;
		this.optionid = optionid;
		this.master = master;
	}

	public void setResult(String username, String englishName, String ext,
			String email) {
		this.chinesename = username;
		this.englishname = englishName;
		this.ext = ext;
		this.email = email;
	}

	public void setResult() {
		this.logonname = "";
		this.password = "";
		this.chinesename = "";
		this.englishname = "";
		this.title = "";
		this.ext = "";
		this.email = "";
		this.userlevel = "";
		this.optionid = "";
		this.master = "";
	}

	public String getLogonname() {
		return logonname;
	}

	public String getPassword() {
		return password;
	}

	public String getChinesename() {
		return chinesename;
	}

	public String getEnglishname() {
		return englishname;
	}

	public String getTitle() {
		return title;
	}

	public String getExt() {
		return ext;
	}

	public String getEmail() {
		return email;
	}

	public String getUserlevel() {
		return userlevel;
	}

	public String getOptionid() {
		return optionid;
	}

	public String getMaster() {
		return master;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

}
