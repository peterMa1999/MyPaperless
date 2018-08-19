package com.zsm.foxconn.mypaperless.base;

/**
 * 常量类 保存各类ACTION
 * 
 * @author ZSM
 * 
 */
public class MyConstant {

	// <-----------DEFAULT----------->
	// 1秒=1000毫秒(ms)
	// 1秒=1,000,000 微秒(μs)
	// 1秒=1,000,000,000 纳秒(ns)
	public static final String SD_PATH = "/mnt/sdcard/BeaconAppStore/";
	/**
	 * 当前apk
	 */
	public static String GET_localAPK = "MyPaperless.apk";
	public static String APP_NAME = "MyPaperless";
	/**
	 * 检查版本更新
	 */
	/**
	 * 获取数据方法
	 */
	public static final String GET_METHOD_NAME_UPDATE = "ConntionBeacon";
	public static final String GET_CHECK_VERSION = "GET_CHECK_VERSION";
	/**
	 * 下载地址
	 */
	public static String GET_localUrl = "";

	/**
	 * 当前版本信息
	 */
	public static int GET_localVersion;

	/**
	 * 服务器版本信息
	 */
	public static int GET_serverVersion;

	// /**
	// * 连接更新时的服务器地址
	// */
	// public static final String GET_WSDL_UPDATA =
	// "http://10.149.5.84:8080/BeaconServer/BeaconServicePort";
	/**
	 * 连接更新时的服务器地址
	 */
	public static final String GET_WSDL_UPDATA = "http://10.167.4.199:8080/BeaconServer/BeaconServicePort";

	/*
	 * --------------------------------------------------------------------------
	 * ------------------------------
	 */

	/**
	 * 连接服务器地址
	 */
	// public static final String GET_WSDL
	// ="http://10.149.6.91:8080/MyPaperless/MyPaperlessServicePort";
//	public static final String GET_WSDL = "http://10.167.6.150:8080/MyPaperless/MyPaperlessServicePort";
//	public static final String GET_WSDL = "http://10.134.44.121:8888/MyPaperless/MyPaperlessServicePort";		
//	public static final String GET_WSDL = "http://10.167.4.131:8888/MyPaperless/MyPaperlessServicePort";   
//	public static final String GET_WSDL = "http://10.134.44.122:8888/MyPaperless/MyPaperlessServicePort";
//	public static final String GET_WSDL = "http://10.167.4.199:8080/MyPaperless/MyPaperlessServicePort"; 
	 public static final String GET_WSDL
	 ="http://10.149.5.84:8080/MyPaperless/MyPaperlessServicePort";
//	 public static final String GET_WSDL
	// ="http://10.149.3.238:8080/MyPaperless/MyPaperlessServicePort";
	// public static final String
	// GET_WSDL="http://10.149.2.223:8080/MyPaperless/MyPaperlessServicePort";
	// public static final String
	// GET_WSDL="http://10.149.5.252:8080/MyPaperless/MyPaperlessServicePort";
	// public static final String
	// GET_WSDL="http://10.149.4.8:8080/MyPaperless/MyPaperlessServicePort";
	
//	public static final String GET_WSDL_PICTURE = "http://10.167.6.150:8080/MyPaperless/";
//	public static final String GET_WSDL_PICTURE = "http://10.167.4.199:8080/MyPaperless/";
//	public static final String GET_WSDL_PICTURE = "http://10.149.5.84:8080/MyPaperless/";
	public static final String GET_WSDL_PICTURE = "http://10.134.44.121:8888/MyPaperless/";
	public static final String GET_WSDL_ABNORMALPHOTO = "http://10.134.44.121:8888/MyPaperless/image/";
//	public static final String GET_WSDL_PICTURE = "http://10.134.44.122:8888/Uploaddome/";
	
	/**
	 * 服务器命名空间
	 */
	public static final String GET_NAME_SPACE = "http://service/";

	/**
	 * 字符串分割符
	 */
	public static final String GET_FLAG = "%%%";

	public static final String GET_FLAG1 = "&&";
	/**
	 * 总线别字符串分割符----"//"
	 */
	public static final String GET_FLAG2 = "//";
	/**
	 * 总基本数据字符串分割符----"~"
	 */
	public static final String GET_FLAG3 = "~";
	/**
	 * 数据返回验证,NULL返回数据为空
	 */
	public static final String GET_FLAG_NULL = "NULL";

	/**
	 * 数据返回验证,TRUE有返回数据
	 */
	public static final String GET_FLAG_TRUE = "TRUE";
	/**
	 * 数据返回验证,false密码错误
	 */
	public static final String GET_FLAG_FALSE = "FALSE";
	
	/**
	 * 密碼規則 長度為8-16位，必須含有大小寫字母、特殊字符、數字
	 */
	public static final String GET_PASSWORD_EXCEPTION = "密碼不符合規則";
	
	/**
	 * 数据返回验证,EXCEPTION获取数据出现异常
	 */
	public static final String GET_FLAG_EXCEPTION = "EXCEPTION";
	/**
	 * 未连接服务器
	 */
	public static final String GET_FLAG_UNUNITED = "GET_FLAG_UNUNITED";

	public static final String GET_FLAG_UNUNITED_DETAIL = "未连接服务器";

	/**
	 * 数据返回验证,NULL返回数据为空说明
	 */
	public static final String GET_FLAG_NULL_DETAIL = "服务器返回数据为空";

	/**
	 * 账号与密码不匹配
	 */
	public static final String GET_FLAG_NOT_MATCH = "账号与密码不匹配";

	/**
	 * 数据返回验证,EXCEPTION获取数据出现异常说明
	 */
	public static final String GET_FLAG_EXCEPTION_DETAIL = "服务器返回数据出现异常";

	/**
	 * 获取数据方法
	 */
	public static final String GET_METHOD_NAME = "ConntionService";

	/**
	 * 数据返回验证,数据更新失败
	 */
	public static final String GET_FLAG_UPDATE_FALSE = "数据更新失败";

	/**
	 * 数据返回验证,数据更新成功
	 */
	public static final String GET_FLAG_UPDATE_TRUE = "数据更新成功";

	// <-----------ACTION----------->
	/**
	 * 按工单查询
	 */
	public static final String GET_WORK_ORDER = "GET_WORK_ORDER";
	/**
	 * 按日期查询
	 */
	public static final String GET_DATE1 = "GET_DATE1";
	/**
	 * 确定按钮
	 */
	public static final String GET_CONFIRM = "GET_CONFIRM";
	/**
	 * 
	 * 该用户点检日期
	 */
	public static final String GET_DATE = "GET_DATE";
	/**
	 * 
	 * 选择修改点检项
	 */
	public static final String GET_SELECT_MODIFY = "GET_SELECT_MODIFY";

	/**
	 * 该用户点检未审核的列表
	 */
	public static final String GET_NO_AUDIT = "GET_NO_AUDIT";
	
	/**
	 * 駁回報表
	 */
	public static final String GET_CHECK_REJECT = "GET_CHECK_REJECT";
	/**
	 * 该用户未审核列表的详细信息 Check_Report_No&check_id
	 */
	public static final String GET_DETAILS = "GET_DETAILS";

	/**
	 * 获取楼层ipqc mfg&section
	 */
	public static final String GET_CHECK_FLOOR = "GET_CHECK_FLOOR";

	/**
	 * 获取班别
	 */
	public static final String GET_SHIFT = "GET_SHIFT";

	/**
	 * 获取非SMT线别
	 */
	public static final String GET_NOTSMTLINE = "GET_NOTSMTLINE";

	/**
	 * 获取報表編號
	 */
	public static final String GET_REPORTNUM = "GET_REPORTNUM";

	/**
	 * 获取起子編號
	 */
	public static final String GET_REPORTTOOLNO = "GET_REPORTTOOLNO";

	/**
	 * 保存工具點檢信息
	 */
	public static final String SAVE_TOOLCONTENT = "SAVE_TOOLCONTENT";

	/**
	 * 保存點檢報表
	 */
	public static final String SAVE_CHECKREPORT = "SAVE_CHECKREPORT";

	/**
	 * 獲取點檢過得工具編號
	 */
	public static final String GET_CHECKTOOLNO = "GET_CHECKTOOLNO";

	/**
	 * 保存點檢結果
	 */
	public static final String SAVE_CHECKRESULT = "SAVE_CHECKRESULT";

	/**
	 * 獲取烙鐵/風槍編號
	 */
	public static final String GET_TOOLNO = "GET_TOOLNO";

	/**
	 * 獲取二級點檢項目
	 */
	public static final String GET_PRONAME = "GET_PRONAME";

	/**
	 * 获取SMT线别
	 */
	public static final String GET_SMTLINE = "GET_SMTLINE";

	/**
	 * 獲取一級點檢項目
	 */
	public static final String GET_ONEPRONAME = "GET_ONEPRONAME";

	/**
	 * 獲取一二級點檢條目
	 */
	public static final String GET_CHECKREPORTTITLE = "GET_CHECKREPORTTITLE";

	/**
	 * 獲取非SMT未點檢的線別
	 */
	public static final String GET_UNCHECKEDLINE = "GET_UNCHECKEDLINE";

	/**
	 * 獲取SMT未點檢的線別
	 */
	public static final String GET_SMTUNCHECKEDLINE = "GET_SMTUNCHECKEDLINE";

	/**
	 * 獲取customer與mfg_model
	 */
	public static final String GET_MODEL = "GET_MODEL";

	/**
	 * 獲取最大最小扭力值
	 */
	public static final String GET_MAXMINTORSION = "GET_MAXMINTORSION";

	/**
	 * 登录 1、账号 2、密码
	 */
	public static final String GET_LOGIN = "GET_LOGIN";

	/**
	 * SITE,BG
	 */
	public static final String GET_BU = "GET_BU";
	/**
	 * ADMIN获取大BU
	 */
	public static final String GET_BIG_CAREER_BU_ADMIN = "GET_BIG_CAREER_BU_ADMIN";
	/**
	 * ADMIN获取部门
	 */
	public static final String GET_CREATE_TEAM_ADMIN = "GET_CREATE_TEAM_ADMIN";

	/**
	 * ADMIN获取事业处
	 */
	public static final String GET_CAREER_MFG_ADMIN = "GET_CAREER_MFG_ADMIN";

	/**
	 * ADMIN获取SBU
	 */
	public static final String GET_A_SBU_ADMIN = "GET_A_SBU_ADMIN";

	/**
	 * 获取当前节次、班别、报警时间
	 */
	public static final String GET_ALERT_TIME = "GET_ALERT_TIME";

	/**
	 * 注册 1、根据表IPQCEuser个人所有信息
	 */
	public static final String GET_REGISTER = "GET_REGISTER";

	/**
	 * 获取TEAM中所有审核人员
	 */
	public static final String GET_CHECK_NAME = "GET_CHECK_NAME";

	/**
	 * 获取ICAR中所有审核人员
	 */
	public static final String GET_ICAR_CHECK_NAME = "GET_CHECK_NAME";

	/**
	 * 获取所有点检报表 1、厂区&Team&Mfg
	 */
	public static final String GET_CHECK_ALL_REPORT_NAME = "GET_CHECK_ALL_REPORT_NAME";
	
	/**
	 * 可查詢點檢記錄的報表
	 */
	public static final String GET_MFG_ALL_REPORT_NAME = "GET_MFG_ALL_REPORT_NAME";
	
	/**
	 * 獲取報表工站
	 */
	public static final String GET_CHECK_REPORT_SECTION = "GET_CHECK_REPORT_SECTION";
	
	/**
	 * 获取楼层ipqc mfg&section
	 */
	public static final String GET_FLOOR_IPQC = "GET_FLOOR_IPQC";

	/**
	 * 獲取報表name IPQC report_num
	 */
	public static final String GET_REPORT_NAME = "GET_REPORT_NAME";

	/**
	 * 获取对应楼层所有线别IPQC mfg_name&section_name&floor_name
	 */
	public static final String GET_CHECK_LINE = "GET_CHECK_LINE";

	/**
	 * 获取本节次所有已经勾选过的线别IPQC mfg_name&check_date&check_id&report_num
	 */
	public static final String GET_CHECK_LINE_ALREADY = "GET_CHECK_LINE_ALREADY";

	/**
	 * 获取本节个人勾选过的线别IPQC check_by&report_Num
	 */
	public static final String GET_SAVE_LINE = "GET_SAVE_LINE";

	/**
	 * 获取本节未完成点检的线别IPQC
	 */
	public static final String GET_CHECK_LINE_UNFINISHED = "GET_CHECK_LINE_UNFINISHED";

	/**
	 * 获取该节次线别中已被点检的线别IPQC report_Num
	 */
	public static final String GET_CHECK_LINE_PASS = "GET_CHECK_LINE_PASS";

	/**
	 * 获取IPQC工单数据IPQC mfg_name&sbu&wo
	 */
	public static final String GET_WO_DATA_IPQC = "GET_WO_DATA_IPQC";

	/**
	 * 获取工单数据GET_B/T IPQC mfg_name&wo&floor&line
	 */
	public static final String GET_WO_DATA_GETBT = "GET_WO_DATA_GETBT";

	/**
	 * 獲取對應報表的所有點檢項IPQC site&bu&report_num
	 */
	public static final String GET_CHECK_NAME_PEIZHI = "GET_CHECK_NAME_PEIZHI";

	/**
	 * 獲取配置表中已配置的點檢Pro_ID IPQC report_num,mfg_name,sbu_name,site_name,bu
	 */
	public static final String GET_PEIZHI_PRO_ID = "GET_PEIZHI_PRO_ID";

	/**
	 * 獲取對應的點檢項目IPQC Report_Num,MFG_Name,SBU_Name,SITE_Name,BU,Check_Department
	 */
	public static final String GET_CHECK_NAME_IPQC = "GET_CHECK_NAME_IPQC";

	/**
	 * 獲取提交點檢的審核人IPQC team&mfg
	 */
	public static final String GET_SIGNOFF_NAME = "GET_SIGNOFF_NAME";

	/**
	 * 獲取該主管所在處的SBU IPQC MFG
	 */
	public static final String GET_PEIZHI_SBU = "GET_PEIZHI_SBU";

	/**
	 * 获取PD楼层
	 */
	public static final String GET_FLOOR_PD = "GET_FLOOR_PD";

	/**
	 * 获取PD线别
	 */
	public static final String GET_CHECK_LINE_PD = "GET_CHECK_LINE_PD";
	/**
	 * 添加(勾选)PD线别
	 */
	public static final String ADD_SELECT_LINE_PD = "ADD_SELECT_LINE_PD";
	/**
	 * 保存PD点检结果;
	 */
	public static final String SAVE_CHECK_RESULT = "SAVE_CHECK_RESULT";
	/**
	 * 审核PD点检报表;
	 */
	public static final String SAVE_REPORT_CHECK = "SAVE_REPORT_CHECK";
	/**
	 * 获取PD点检审核人;
	 */
	public static final String GET_PD_CHECK_NAME = "GET_PD_CHECK_NAME";
	/**
	 * 保存PD点检报表;
	 */
	public static final String SAVE_CHECK_REPORT = "SAVE_CHECK_REPORT";
	/**
	 * 保存PD点检线基信息;
	 */
	public static final String SAVE_CHECK_BASE_INFO = "SAVE_CHECK_BASE_INFO";
	/**
	 * 获取添加(勾选)待点检PD线别
	 */
	public static final String CHECKED_LINE_PD = "CHECKED_LINE_PD";
	/**
	 * 获取PD线别点检报表一二级条目;
	 */
	public static final String GET_CHECK_REPORT_TITLE = "GET_CHECK_REPORT_TITLE";
	/**
	 * 删除已添加(勾选)待点检PD线别
	 */

	public static final String DELETE_LINE_PD = "DELETE_LINE_PD";
	/**
	 * 提交PD点检人;
	 */
	public static final String SAVE_CHECK_PERSON = "SAVE_CHECK_PERSON";
	/**
	 * 获取PD线别点检基本信息;
	 */
	public static final String CHECKED_BASEINFO_PD = "CHECKED_BASEINFO_PD";

	/**
	 * 获取工单数据
	 */
	public static final String GET_WO_DATA = "GET_WO_DATA";

	/**
	 * 获取起子編號(离子发生器&电动起子)
	 */
	public static final String GET_TOOL_NO = "GET_TOOL_NO";

	/**
	 * 获取起子編號(风枪)
	 */
	public static final String GET_TOOL_NO_LT = "GET_TOOL_NO_LT";

	/**
	 * 获取起子編號(风枪)
	 */
	public static final String GET_ALLPART_DATA = "GET_ALLPART_DATA";
	/**
	 * 获取所有的审核信息
	 */
	public static final String GET_ALL_EXAMINE_MESSAGE = "GET_ALL_EXAMINE_MESSAGE";
	/**
	 * 按条件获取所有的审核信息
	 */
	public static final String GET_ALL_EXAMINE_MESSAGE_TYPE = "GET_ALL_EXAMINE_MESSAGE_TYPE";
	/**
	 * 获取详细的信息列表
	 */
	public static final String GET_EXAMINE_MESSAGE_MORE = "GET_EXAMINE_MESSAGE_MORE";
	/**
	 * 获取详细点检信息
	 */
	public static final String GET_EXAMINE_MESSAGE_DETAIL = "GET_EXAMINE_MESSAGE_DETAIL";
	/**
	 * 获取审核结果
	 */
	public static final String GET_EXAMINE_STATUS = "GET_EXAMINE_STATUS";
	/**
	 * 详细点检信息通过
	 */
	public static final String UPDATE_EXAMINE_TURE_DETAIL = "UPDATE_EXAMINE_TURE_DETAIL";
	/**
	 * 详细点检信息驳回
	 */
	public static final String UPDATE_EXAMINE_FALSE_DETAIL = "UPDATE_EXAMINE_FALSE_DETAIL";
	/**
	 * 详细点检信息驳回原因
	 */
	public static final String GET_EXAMINE_MESSAGE_DETAIL_CAUSE = "GET_EXAMINE_MESSAGE_DETAIL_CAUSE";
	/**
	 * 获取员工信息
	 */
	public static final String GET_EMPLOYEE_INFOMATION = "GET_EMPLOYEE_INFOMATION";
	/**
	 * 获取所有员工信息
	 */
	public static final String GET_ALL_EMPLOYEE_INFORMATION = "GET_ALL_EMPLOYEE_INFORMATION";

	/**
	 * 模糊查询员工信息
	 */
	public static final String GET_SELECT_EMPLOYEE_INFORMATION = "GET_SELECT_EMPLOYEE_INFORMATION";

	/**
	 * 添加员工信息
	 */
	public static final String ADD_EMPLOYEE_INFORMATION = "ADD_EMPLOYEE_INFORMATION";
	/**
	 * 删除员工信息
	 */
	public static final String DELETE_EMPLOYEE_INFORMATION = "DELETE_EMPLOYEE_INFORMATION";
	/**
	 * 修改员工信息
	 */
	public static final String MODIFY_EMPLOYEE_INFORMATION = "MODIFY_EMPLOYEE_INFORMATION";
	/**
	 * 修改密码
	 */
	public static final String MOEDIFY_PASSWORD = "MOEDIFY_PASSWORD";
	/**
	 * 获取事业处
	 */
	public static final String GET_CAREER_OFFICE = "GET_CAREER_OFFICE";
	/**
	 * 获取事业处BU
	 */
	public static final String GET_A_CAREER_BU = "GET_A_CAREER_BU";
	/**
	 * 获取工站
	 */
	public static final String ACQUISITION_STATION = "ACQUISITION_STATION";
	/**
	 * 意见反馈
	 */
	public static final String Feedback = "Feedback";

	/**
	 * 历史反馈
	 */
	public static final String Historical_feedback = "Historical_feedback";
	/**
	 * 验证用户
	 */
	public static final String VERIFY_USER = "VERIFY_USER";

	/**
	 * 警報信息：獲得當前用戶所有選擇的線別
	 * 
	 * @check_by
	 */
	public static final String ALARM_GET_ALLLINE = "ALARM_GET_ALLLINE";

	/**
	 * 警報信息：獲得當前用戶已完成點檢的點檢編號
	 * 
	 * @check_by
	 */
	public static final String ALARM_GET_REVNO = "ALARM_GET_REVNO";

	/**
	 * 获取报表報警IPQC
	 */
	public static final String GET_CHECK_TIME = "GET_CHECK_TIME";
	
	/**
	 * 获取报表報警
	 */
	public static final String GET_ALARM_TIME_YEILD = "GET_ALARM_TIME_YEILD";
	
	/**
	 * 得到IPQC\PD未及时点检的线别
	 */
	public static final String GET_NO_CHECK_LINE = "GET_NO_CHECK_LINE";
	/**
	 * 將未及時點檢和提前維護線體的原因信息插入三張表
	 */
	public static final String GET_INSERT_REPORT = "GET_INSERT_REPORT";

	/**
	 * 將未及時點檢和提前維護線體的原因信息插入三張表
	 */
	public static final String GET_INSERT_BASEINFO = "GET_INSERT_BASEINFO";

	/**
	 * 將未及時點檢和提前維護線體的原因信息插入三張表
	 */
	public static final String GET_INSERT_REPORTCHECK = "GET_INSERT_REPORTCHECK";

	/**
	 * 獲取需要帶數據的Pro_id
	 */
	public static final String GET_PRO_ID_DATA_OFWO = "GET_PRO_ID_DATA_OFWO";

	/**
	 * 根據關鍵字帶數據
	 */
	public static final String GET_DATA_OF_INPUTTYPE = "GET_DATA_OF_INPUTTYPE";

	// ---------------------ACTION>>INSERT>>UPDATE>>DELETE---------------------------------
	/**
	 * 保存选中的线别IPQC mfg_name,report_num,floor,line,username,
	 */
	public static final String INSERT_SELECTED_LINE = "INSERT_SELECTED_LINE";

	/**
	 * 删除本节本人勾选过的线别IPQC check_By&line_name&Check_ID
	 */
	public static final String DELETE_CHECK_LINE = "DELETE_CHECK_LINE";

	/**
	 * 配置之前刪除對應的配置IPQC Peport_Num,MFG_Name,SBU_Name,SITE_Name,BU
	 */
	public static final String DELETE_PEIZHI_MFG_TABLE = "DELETE_PEIZHI_MFG_TABLE";

	/**
	 * 點檢配置IPQC Report_Num,Pro_ID,MFG_Name,SBU_Name,SITE_Name,BU
	 */
	public static final String SAVE_PEIZHI_CHECK_NAME = "SAVE_PEIZHI_CHECK_NAME";

	/**
	 * 插入數據IPQC
	 */
	public static final String INSERT_DATA = "INSERT_DATA";

	/**
	 * 插入數據至baseinfo表IPQC
	 * 
	 * @check_report_no,@checkid,@checkwo,@checksku,@checkwoqty,@checkversion,@checkside,@checkdeviation,@checkflag,@checkremark,@checkremarkreason,@checktype,@createby
	 */
	public static final String INSERT_DATA_BASEINFO = "INSERT_DATA_BASEINFO";

	/**
	 * 插入數據至CHECKREPORT表IPQC
	 * 
	 * @check_report_no,@checkid,@reportNum,@mfgname,@floorname,@linename,
	 * @shiftname,@username
	 */
	public static final String INSERT_DATA_CHECKREPORT = "INSERT_DATA_CHECKREPORT";

	/**
	 * 插入數據至CHECKRESULT表IPQC
	 * 
	 * @check_report_no,@checkid,@reportNum,@proid,@checkcontent,@checkresult,@icarno,@toolno
	 */
	public static final String INSERT_DATA_CHECKRESULT = "INSERT_DATA_CHECKRESULT";

	/**
	 * 插入數據至REPORTCHECK表IPQC
	 * 
	 * @check_report_no,@checkid,@checkseqno,@remark,@checkby,@createby
	 */
	public static final String INSERT_DATA_REPORTCHECK = "INSERT_DATA_REPORTCHECK";

	/**
	 * OBA報表無節次限制,所以點檢完之後刪除用戶選線表中的線別 reportid,linename,check_id,check_by
	 */
	public static final String OBA_delete_line = "OBA_delete_line";

	/**
	 * 添加报表
	 */
	public static final String ADD_REPORT = "ADD_REPORT";

	/**
	 * 添加报表时增加该报表的点检处（物）
	 */
	public static final String ADD_REPORT_FLOORLINE = "ADD_REPORT_FLOORLINE";
	/**
	 * 獲取報表工站
	 */
	public static final String GET_CHECK_REPORT_SECTION_ANME = "GET_CHECK_REPORT_SECTION_ANME";

	/**
	 * 查询报表是否存在
	 */
	public static final String GET_MFGVII_QRCODE_ID = "GET_MFGVII_QRCODE_ID";

	/**
	 * 删除报表
	 */
	public static final String DELETE_REPORT = "DELETE_REPORT";

	/**
	 * MFGVII IE存入已點檢線別
	 * mfg_name,report_num,floor,line,report_num,username,report_num
	 */
	public static final String MFGVII_IE_INSERT_LINENAME = "MFGVII_IE_INSERT_LINENAME";

	/**
	 * 獲取七處IE部門本時間端已選擇線別 MFG_NAME,REPORT_NUM
	 */
	public static final String GET_FMGVII_IE_CHECKED_LINE = "GET_FMGVII_IE_CHECKED_LINE";

	/**
	 * 發送郵件提醒簽核
	 */
	public static final String REMIND_CHECK = "REMIND_CHECK";
	
	/**
	 * 判斷報表編號是否存在
	 */
	public static final String RNO_ISEXIT = "RNO_ISEXIT";
	
	/**
	 * 获取网络时间
	 */
	public static final String GET_INTERNET_TIME = "GET_INTERNET_TIME";
	
	/**
	 * 模板報表配置
	 */
	public static final String MUBAN_PEIZHI = "MUBAN_PEIZHI";
	
	/**
	 * 當前MFG的所有TEAM
	 */
	public static final String GET_MFG_TEAM = "GET_MFG_TEAM";
	
	/**
	 * 獲取報表的審核部門
	 */
	public static final String GET_REPORT_TEAM = "GET_REPORT_TEAM";
	
	/**
	 * 獲取詳細點檢信息的所有審核人
	 */
	public static final String GET_CHECK_REPORT_NO_BYCHECK = "GET_CHECK_REPORT_NO_BYCHECK";
	
	/**
	 * 樓層管理獲得MFG樓層線別
	 */
	public static final String GET_MANAGE_FLOOR = "GET_MANAGE_FLOOR";
	
	/**
	 * 添加樓層
	 */
	public static final String INSERT_INTO_CHECK_LINE = "INSERT_INTO_CHECK_LINE";
	
	/**
	 * 修改樓層信息
	 */
	public static final String UPDATE_FLOOR_LINE = "UPDATE_FLOOR_LINE";
	
	/**
	 * 刪除樓層信息
	 */
	public static final String DELETE_FlOOR_LINE = "DELETE_FlOOR_LINE";
	
	/**
	 * 查詢用戶所在BU的所有樓層
	 */
	public static final String GET_ALL_FLOOR_BU = "GET_ALL_FLOOR_BU";
	
	/**
	 * 將上傳圖片的信息加入數據庫
	 */
	public static final String UPLOAD_PICTURE_INFO = "UPLOAD_PICTURE_INFO";
	
	/**
	 *獲取參數表數據 
	 */
	public static final String GET_ALL_CANSHU = "GET_ALL_CANSHU";
	
	/**
	 *獲取波峰焊參數表數據 
	 */
	public static final String BFH_GET_ALL_CANSHU = "BFH_GET_ALL_CANSHU";
	
	/**
	 * 獲取波峰焊詳細參數
	 */
	public static final String GET_BFH_DETAILED_CANSHU = "GET_BFH_DETAILED_CANSHU";
	
	/**
	 * 波峰焊參數操作
	 */
	public static final String OPERACTION_BFH_CANSHU = "OPERACTION_BFH_CANSHU";
	
	/**
	 * 獲取詳細參數信息
	 */
	public static final String GET_CANSHU_INFO = "GET_CANSHU_INFO";
	
	/**
	 * 提交刪除參數
	 */
	public static final String DELETE_CANSHU_INFO = "DELETE_CANSHU_INFO"; 
	
	/**
	 * 提交添加參數
	 */
	public static final String ADD_CANSHU_INFO = "ADD_CANSHU_INFO";
	
	/**
	 * 提交修改參數
	 */
	public static final String UPDATE_CANSHU_INFO = "UPDATE_CANSHU_INFO";
	
	/**
	 * 獲取所有參數驗證
	 */
	public static final String CANSHU_ONGING = "CANSHU_ONGING";
	
	/**
	 * 簽核同意
	 */
	public static final String AGREEN_CHECK = "AGREEN_CHECK";
	
	/**
	 * 簽核駁回
	 */
	public static final String Refuse_CHECK = "Refuse_CHECK";
	
	/**
	 * 獲取審核表中我的消息條數
	 */
	public static final String GET_CHECK_NEWS = "GET_CHECK_NEWS";
	
	/**
	 * 掃碼獲取二維碼的信息
	 */
	public static final String GET_QRCODEID = "GET_QRCODEID";
	
	
	/**
	 * 獲取當前MFG樓層
	 */
	public static final String GET_MFG_FLOOR = "GET_MFG_FLOOR";
	
	/**
	 * 獲取當前MFG線體或設備
	 */
	public static final String GET_MFG_LINE = "GET_MFG_LINE";
	
	/**
	 * 通過SN帶出工單號
	 */
	public static final String GET_WO_OF_SN = "GET_WO_OF_SN";
	
	/**
	 * 獲得當前用戶的上級主管
	 */
	public static final String GET_MY_MASTER = "GET_MY_MASTER";
	/**
	 * 獲取溫濕度圖表數據
	 */
	public static final String GET_CHART_DATA = "GET_CHART_DATA";

	/**
	 * 獲取环境溫濕度圖表數據
	 */
	public static final String GET_ENVIRONMENT_CHART_DATA = "GET_ENVIRONMENT_CHART_DATA";
	
	/**
	 * 批量審核
	 */
	public static final String PILIANG_CHECK_PASS = "PILIANG_CHECK_PASS";
	
	/**
	 * 查看報表使用情況
	 */
	public static final String GET_REPORT_USAGE = "GET_REPORT_USAGE";
	
	/**
	 * BU報表擁有總數
	 */
	public static final String GET_BU_REPORT_NUM = "GET_BU_REPORT_NUM";
	
	/**
	 * 獲取有圖表統計有記錄的樓層
	 */
	public static final String GET_MFG_FLOOR_TUBIAO = "GET_MFG_FLOOR_TUBIAO";
	
	/**
	 * 獲取有圖表統計有記錄的樓層具體處
	 */
	public static final String GET_MFG_LINE_TUBIAO = "GET_MFG_LINE_TUBIAO";
	
	/**
	 * 獲取MFG基本的詳細點檢狀況
	 */
	public static final String GET_DETAILED_CHECK_LIST = "GET_DETAILED_CHECK_LIST";
	
	/**
	 * 找回密碼
	 */
	public static String USER_FORGETPWD = "USER_FORGETPWD";
	
	/**
	 * 上传异常;
	 */
	public static final String SUBMIT_ABNORMAL = "SUBMIT_ABNORMAL";
	/**
	 * 獲取异常列表;
	 */
	public static final String GET_ABNORMAL = "GET_ABNORMAL";
	/**
	 * 獲取異常詳細;
	 */
	public static final String GET_ABNORMAL_ITEM = "GET_ABNORMAL_ITEM";
	/**
	 * 处理异常;
	 */
	public static final String DEAL_ABNORMAL = "DEAL_ABNORMAL";
	/**
	 * 带出处理完之后的结果;
	 */
	public static final String GET_DEAL_ABNORMAL_RESULT = "GET_DEAL_ABNORMAL_RESULT";
	/**
	 * 更改异常接收人;
	 */
	public static final String CHANGE_ABNORMAL_DEAL_PEOPLE = "CHANGE_ABNORMAL_DEAL_PEOPLE";
	
	/**
	 * 通过報表
	 */
	public static final String GET_CHECK_PASS = "GET_CHECK_PASS";
	
	/**
	 * 獲取樓層
	 */
	public static final String Access_floors = "Access_floors";
	/**
	 * 獲取線體
	 */
	public static final String Gets_the_line_style = "Gets_the_line_style";
	/**
	 * 獲取編號
	 */
	public static final String Gets_a_number = "Gets_a_number";
	/**
	 * 二維碼編號維護
	 */
	public static final String Two_dimensional_code_number_maintenance = "Two_dimensional_code_number_maintenance";
	/**
	 * 二维码详细查询
	 */
	public static final String QR_details_query = "QR_details_query";
	/**
	 * 维护语句
	 */
	public static final String Line_maintenance = "Line_maintenance";
	
	
	/**
	 * 4小時時間段维护语句
	 */
	public static final String Line_fourhours_maintenance = "Line_fourhours_maintenance";
	
	/**
	 * 2小時時間段维护语句
	 */
	public static final String Line_twohours_maintenance = "Line_twohours_maintenance";
	
	/**
	 * 根据二维码信息获取报表编号信息(補點檢)
	 */
	public static final String GET_QRCODEID_CHECK_UP = "GET_QRCODEID_CHECK_UP";
	/**
	 * 獲取已經點檢的時間段(補點檢)
	 */
	public static final String GET_QRCODEID_CHECK_UP_TIME = "GET_QRCODEID_CHECK_UP_TIME";
	/**
	 * 保存PD点检线基信息(補點檢)
	 */
	public static final String SAVE_CHECK_BASE_INFO_CHECK_UP = "SAVE_CHECK_BASE_INFO_CHECK_UP";
	/**
	 * 保存點檢報表(補點檢)
	 */
	public static final String SAVE_CHECKREPORT_CHECK_UP = "SAVE_CHECKREPORT_CHECK_UP";
	/**
	 * 提交PD点检人(補點檢)
	 */
	public static final String SAVE_CHECK_PERSON_CHECK_UP = "SAVE_CHECK_PERSON_CHECK_UP";
	/**
	 * 保存PD点检報表(補點檢)
	 */
	public static final String SAVE_CHECK_REPORT_CHECK_UP = "SAVE_CHECK_REPORT_CHECK_UP";
	
	
	/**
	 * 點檢狀況查詢、責任人
	 */
	public static final String GET_CHECK_SITUATION = "GET_CHECK_SITUATION";
	
	/**
	 * 下載設備信息
	 */
	public static final String SAVE_DOWNLOAD_PHONE = "SAVE_DOWNLOAD_PHONE";
	
	/**
	 * IPQC提前整天維護
	 */
	public static final String IPQC_TIQIAN_BYDAY = "IPQC_TIQIAN_BYDAY";
	
	/**
	 * GetIPQCCrossBUIssue
	 */
	public static final String GetIPQCCrossBUIssue = "GetIPQCCrossBUIssue";
	
	/**
	 * 保存ISSUE信息SAVE_IPQCCrossBUIssue
	 */
	public static final String SAVE_IPQCCrossBUIssue = "SAVE_IPQCCrossBUIssue";
	
	/**
	 * 獲取當前所在BU的TOP ISSUE信息
	 */
	public static final String Get_BU_IPQCCrossBUIssue = "Get_BU_IPQCCrossBUIssue";
	
	/**
	 * update ISSUE狀態信息
	 */
	public static final String update_BU_IPQCCrossBUIssue = "update_BU_IPQCCrossBUIssue";
	
	/**
	 * 查詢121數據庫是否有IPQCCrossBUIssue信息
	 */
	public static final String GET_IPQCCrossBUIssue = "GET_IPQCCrossBUIssue";
}
