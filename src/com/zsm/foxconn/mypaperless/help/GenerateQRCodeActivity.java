package com.zsm.foxconn.mypaperless.help;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.google.zxing.WriterException;
import com.zsm.foxconn.mypaperless.Create_floorQR;
import com.zsm.foxconn.mypaperless.R;
import com.zsm.qr.EncodingHandler;

public class GenerateQRCodeActivity{
	/**用户输入的字符串*/
	private String qrStrEditText;

	/**生成的二维码图片*/
	private Bitmap qrImgImageView;
	public static final File FILE_SDCARD = Environment
			.getExternalStorageDirectory(); // 检测SD卡
	public static final String IMAGE_PATH = "Mypaperless"; // 文件夾名稱
	public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
	public static final File FILE_PIC_MYQRCODE = new File(FILE_LOCAL,
			"images/myQRcode"); // 照片路徑
	private static String QRCONDENAME = "";
	
	public void createqrcode(List<String> liststr,String reportnum,Activity context){
		try {
			if (!liststr.equals("")) {
				
				File filePath = FILE_PIC_MYQRCODE;
				for (int i = 0; i < liststr.size(); i+=2) {
					Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(),
							R.drawable.logo_bmp);
					Bitmap qrCodeBitmap = EncodingHandler.createQRCode(
							reportnum+"-"+liststr.get(i)+"-"+liststr.get(i+1),280,280,mBitmap);
					QRCONDENAME = reportnum+"-"+liststr.get(i)+"-"+liststr.get(i+1)+".png";
					if (!filePath.exists()) {
						filePath.mkdirs(); // 如果文件夹不存在,则重新创建
					}
					File f = new File(filePath, QRCONDENAME);
					System.out.println("--"+f.getAbsolutePath());
					FileOutputStream fOut = null;
				    try {
				                fOut = new FileOutputStream(f);
				        } catch (FileNotFoundException e) {
				                e.printStackTrace();
				        }
				        qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
				        try{
				                fOut.flush();
				        } catch (IOException e) {
				                e.printStackTrace();
				        }
				        try{
				                fOut.close();
				        } catch (IOException e) {
				                e.printStackTrace();
				        }
				        
				        UploadFileTask uploadFileTask = new UploadFileTask(context
				        		);
						uploadFileTask.execute(f
								.getAbsolutePath());
				}
				
			} else {
				return;
			}

		} catch (Exception e) {
			Log.e("generate QRCode Error",e.toString());
		}
	}
	
	
	public static void uploadFile(String sb) {
		System.out.println(sb);
		Socket socket = null;
		DataOutputStream dos = null;
		FileInputStream fis;
		try {
			File file = new File(sb);
			if (file.length() == 0) {
				return;
			} else {
				socket = new Socket("10.167.6.150", 8081);
				dos = new DataOutputStream(socket.getOutputStream());
				fis = new FileInputStream(file);
				dos.writeUTF(sb.toString().substring(
						sb.toString().lastIndexOf("/") + 1));// 截取图片名称
				dos.flush();
				int length;
				byte[] buffer = new byte[8192];
				while ((length = fis.read(buffer, 0, buffer.length)) > 0) {
					dos.write(buffer, 0, length);
					dos.flush();
				}
				dos.close();
				fis.close();
				socket.close();
			}
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}