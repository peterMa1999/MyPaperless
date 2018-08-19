package com.zsm.foxconn.mypaperless.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.zsm.foxconn.mypaperless.DefaultDailog;
import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.base.MyConstant;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 线程类
 * 
 * @author zsm 2015/7/25
 */
public class HttpThread extends Thread {

	private Handler handler = null;

	String wsdl = null;

	String dataName = "";

	String methodName = null;

	HashMap<String, Object> params = null;

	// ProgressDialog pd=null;

	Context context;

	DefaultDailog pd = null;

	// 构造函数
	public HttpThread(Handler handler, Context context) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.context = context;
	}

	// 启动线程
	// public void doStart(String wsdl,String methodName,HashMap<String,Object>
	// params,String dataName) {
	// this.wsdl=wsdl;
	// this.methodName=methodName;
	// this.params=params;
	// this.dataName=dataName;
	// pd=ProgressDialog.show(context,"提示","正在请求数据请稍后...", true);

	// pd=new ProgressDialog(context);
	// pd.setMessage("服务请求中...");
	// pd.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.dialog_style_xml_color));
	// pd.setIndeterminate(true);
	// pd.show();
	// pd=DefaultDailog.show(context, "服务请求中...",true, null);
	// pd=DefaultDailog.show(context, "服务请求中...",R.drawable.dialog_2,true,
	// null);
	// this.start();
	// }

	/**
	 * 
	 * @param wsdl
	 * @param methodName
	 * @param params
	 * @param dataName
	 * @param flag
	 */
	public void doStart(String wsdl, String methodName,
			HashMap<String, Object> params, String dataName, int flag) {
		this.wsdl = wsdl;
		this.methodName = methodName;
		this.params = params;
		this.dataName = dataName;
		switch (flag) {
		case 0:
			Log.i("toast", "正在请求数据请稍后...");
			break;
		case 1:
			pd = DefaultDailog.show(context, "服务请求中...", R.drawable.dialog_2,
					true, null);
			break;
		case 2:
			pd = DefaultDailog.show(context, "服务请求中...",
					R.drawable.dialog_style_xml_color, true, null);
			break;
		case 3:
			pd = DefaultDailog.show(context, "服务请求中...",
					R.anim.loadmore_animation, true, null);
			break;
		default:
//			 pd=ProgressDialog.show(context,"提示","正在请求数据请稍后...", true);
			break;
		}
		this.start();
	}

	/**
	 * 线程运行
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Log.i("Toast", "------>Start");
		super.run();

		ArrayList<String> list = null;
		try {
			// Thread.sleep(1000);
			// web service请求
			long start = System.currentTimeMillis(); // 获取开始时间
			SoapObject result = (SoapObject) CallWebService();
			long end = System.currentTimeMillis(); // 获取结束时间
			// System.out.println("程序运行时间： " + (end - start) + "ms");
			Log.i("conntion server in time=", (end - start) + "ms");
			// 构造数据
			Log.i("Toast------>", result.getPropertyCount() + "");
			if (result != null && result.getPropertyCount() > 0) {
				list = new ArrayList<String>();
				for (int i = 0; i < result.getPropertyCount(); i++) {
					// SoapPrimitive value=(SoapPrimitive)
					// result.getProperty(i);
					list.add(result.getProperty(i).toString());
					// list.add(value.toString());
				}
				Log.i("Toast------>", "As Here Is Action------------->1"
						+ dataName);
				Message msg = handler.obtainMessage();
				Bundle bundle = new Bundle();
				bundle.putStringArrayList(dataName, list);
				msg.setData(bundle);
				handler.sendMessage(msg);
				// 取消进度对话框
				if (pd != null) {
					pd.dismiss();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", "------------------->" + context);
			list = new ArrayList<String>();
			list.add(MyConstant.GET_FLAG_UNUNITED);
			list.add(MyConstant.GET_FLAG_UNUNITED_DETAIL);
			Message msg = handler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putStringArrayList(dataName, list);
			msg.setData(bundle);
			handler.sendMessage(msg);
			if (pd != null) {
				pd.dismiss();
			}
		} finally {

		}
	}

	protected Object CallWebService() {
		String SOAP_ACTION = MyConstant.GET_NAME_SPACE + methodName;
		// 创建SoapObject实例
		SoapObject request = new SoapObject(MyConstant.GET_NAME_SPACE,
				methodName);
		// 生成调用webservice 方法的soap请求消息
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		// envelope.bodyOut=
		// envelope.dotNet=true;
		// 发送请求
		envelope.bodyOut = request;
		envelope.setOutputSoapObject(request);
		// 请求参数-动态
		if (params != null && !params.isEmpty()) {
			for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Entry) it.next();
				request.addProperty(e.getKey().toString(), e.getValue());
				Log.i("sql------>", "" + e.getValue());
			}
		}
		HttpTransportSE http = new HttpTransportSE(wsdl);
		SoapObject result = null;
		try {
			// web service 请求
			http.call(SOAP_ACTION, envelope);
			http.debug = true;
			// 得到返回结果
			result = (SoapObject) envelope.bodyIn;
			Log.i("result--------->",
					result + ">>>" + result.getPropertyCount());
			// getResponse()
		} catch (Exception e) {
			e.printStackTrace();
			pd.dismiss();
		}
		return result;
	}

}
