package com.zsm.foxconn.mypaperless.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class ConnSkyWebService {
	SoapSerializationEnvelope envelope;
	String namespace = "http://tempuri.org/";
	String methodName = "";
	String soupacation = "";
	String wsdl = "http://10.167.192.160:8008/AppLoginCount.asmx";
	String result = null;

	public String getInfo(String... strings) {
		methodName = strings[0];
		SoapObject rpc = new SoapObject(namespace, methodName);
		if (methodName.equals("AppUserCount")) {
			rpc.addProperty("LogonName", strings[1]);
			rpc.addProperty("AppName", strings[2]);
			rpc.addProperty("ActionItem", strings[3]);
			rpc.addProperty("Ip", strings[4]);
		}
		Log.i("Strings=======", "strings[1]=" + strings[1] + ",strings[2]="
				+ strings[2] + ",strings[3]=" + strings[3] + ",strings[4]="
				+ strings[4]);
		HttpTransportSE ht = new HttpTransportSE(wsdl);
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		soupacation = namespace + methodName;
		try {

			ht.call(soupacation, envelope);
			ht.debug = true;
			// SoapObject result1=(SoapObject) envelope.getResponse();
			SoapPrimitive result1 = (SoapPrimitive) envelope.getResponse();
			// SoapObject result1 = (SoapObject) envelope.bodyIn; //集合
			// SoapObject detail = (SoapObject)
			// result1.getProperty(methodName+"Result");

			// List<String> re=parseData(detail);
			// for (int i = 0; i < re.size(); i++) {
			// System.out.println(re.get(i).toString());
			// }

			// SoapObject result1=(SoapObject) envelope.getResponse();
			// SoapFault result1=(SoapFault) envelope.getResponse();
			// for (int i = 0; i < result1.getPropertyCount(); i++) {
			// System.out.println("ConnWebService>>>>"+result1.getProperty(i).toString());
			// }
			result = result1.toString().trim();
			Log.i(">>>>>>>result", result);
			// result1.getProperty(0).toString();
			// System.out.println("ConnWebService>>>>"+result1.getProperty(0).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String SingelConnection(String...strings) {
		// TODO Auto-generated method stub
		String wsdl2="http://10.134.44.122:8000/SFCWebService.asmx";
		SoapObject request=new SoapObject(namespace,"GetMessage");
		request.addProperty("strurl", "http://172.18.32.151/webservice/sso/Authorization.asmx");
		request.addProperty("strmethodname", "IsAuthenticated");
		request.addProperty("strargs", "Domain1;"+strings[0]+";"+strings[1]);
		HttpTransportSE ht = new HttpTransportSE(wsdl2);
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		soupacation = namespace + methodName;
		try {

			ht.call(soupacation, envelope);
			ht.debug = true;
//			SoapPrimitive result1 = (SoapPrimitive) envelope.getResponse();
//			SoapObject result1 = (SoapObject)envelope.bodyIn;
			Object result1 = (Object)envelope.getResponse();
			result = result1.toString().trim();
			Log.i(">>>>>>>result", result);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	public String HttpConnWebservice() {
		String path = "http://10.167.192.160:8088/VSOM.asmx/";
		String parameter = null;
		parameter = "GetSn_EventTime?SN=FCH1930J2JA&Event=C                                                                                                                                                                                                                                                                                                                                                           ARTON";
		String lines = null;
		String spec = path + parameter;
		try {
			URL url = new URL(spec);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				while ((lines = br.readLine()) != null) {
					// System.out.println(lines);
					System.out.println(lines.replaceAll("&[lg]t;", ""));
				}
				br.close();
			}
			conn.disconnect();
			return lines;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}
	
	public static List<String> parseData(SoapObject detail) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < detail.getPropertyCount(); i++) {
			// 解析出每个省份
			result.add(detail.getProperty(i).toString().split(",")[0]);
		}
		return result;
	}

	public List<byte[]> JDBCGetByte(String sp_Name) {
		List<byte[]> result = new ArrayList<byte[]>();
		return result;
	}
}
