package com.zsm.foxconn.mypaperless.http;

import java.util.HashMap;

import com.zsm.foxconn.mypaperless.base.MyConstant;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class HttpStart {
	HashMap<String, Object> params = new HashMap<String, Object>();
	HttpThread http = null;
	private Context context;
	private Handler handler;

	public HttpStart() {
		// TODO Auto-generated constructor stub
	}
	
	public HttpStart(Context context, Handler handler) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
	}

	/**
	 * 开启线程连接服务器
	 * 
	 * @param flag
	 *            0：默认带数据不添加旋转,1:旋转效果;2：旋转效果2
	 * @param strings
	 *            参数：Action开始后带参数
	 */
	public void getServerData(int flag, String... strings) {
		Log.i("client action is =", strings[0]);
		http = new HttpThread(handler, context);
		if (params.size() > 0) {
			params.clear();
		}
		String result = getResult(strings);
		params.put("arg0", result);
		http.doStart(MyConstant.GET_WSDL, MyConstant.GET_METHOD_NAME, params,
				strings[0], flag);
	}
	/**
	 * 自动更新软件...
	 * @param flag
	 * @param strings
	 */
	public void getServerDataUpdate(int flag, String... strings) {
		Log.i("client action is =", strings[0]);
		http = new HttpThread(handler, context);
		if (params.size() > 0) {
			params.clear();
		}
		String result = getResult(strings);
		params.put("arg0", strings[0]);
		params.put("arg1", strings[1]);
		http.doStart(MyConstant.GET_WSDL_UPDATA, MyConstant.GET_METHOD_NAME_UPDATE, params,
				strings[0], flag);
	}
	/**
	 * 返回字符串数据,把数据以%拼接
	 * 
	 * @param strings
	 * @return
	 */
	public static String getResult(String... strings) {
		String result = "";
		for (int i = 0; i < strings.length; i++) {
			result += strings[i] + MyConstant.GET_FLAG;
		}
		return result;
	}

}
