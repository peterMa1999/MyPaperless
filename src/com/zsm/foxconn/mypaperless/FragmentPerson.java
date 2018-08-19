package com.zsm.foxconn.mypaperless;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.foxconn.mypaperless.base.MyConstant;
import com.zsm.foxconn.mypaperless.bean.UserBean;
import com.zsm.foxconn.mypaperless.help.Bimp;
import com.zsm.foxconn.mypaperless.help.CropImageActivity;
import com.zsm.foxconn.mypaperless.help.GenerateQRCodeActivity;
import com.zsm.foxconn.mypaperless.help.UploadFileTask;

public class FragmentPerson extends Fragment implements OnClickListener {

	Context context;
	Intent intent;
	UserBean userBean;
	private View view;
	private TextView job_number, Full_name;
	private LinearLayout Person_stick, Personnel_information, Person_Feedback,
			Person_Setting_Center;
	private ImageButton exitImageButton;
	private TextView bartitle_txt;
	private ImageView Head_portrait;
	private Button photo_camera, photo_cancel, photo_gallery;
	private Dialog dialog;
	private static final int Shear_picture = 0; // 剪切圖片
	private static final int Start_Photo_Gallery = 1;
	private static final int Start_CAMERA = 2;
	public static final File FILE_SDCARD = Environment
			.getExternalStorageDirectory(); // 检测SD卡
	public static final String IMAGE_PATH = "Mypaperless-photo"; // 文件夾名稱
	public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
	public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
			"images/myself"); // 照片路徑
	private static String localTempImageFileName = "";
	private Handler pic_hdl;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.person, container, false);
		userBean = (UserBean) getActivity().getApplicationContext();
		job_number = (TextView) view.findViewById(R.id.text_job_number);
		Full_name = (TextView) view.findViewById(R.id.text_Full_name);
		exitImageButton = (ImageButton) view.findViewById(R.id.bt_img_exit);
		exitImageButton.setVisibility(view.GONE);
		bartitle_txt = (TextView) view.findViewById(R.id.bartitle_txt);
		Head_portrait = (ImageView) view.findViewById(R.id.Head_portrait);
		bartitle_txt.setText("個人中心");
		job_number.setText(userBean.getLogonName());
		Full_name.setText(userBean.getChineseName());
		Person_stick = (LinearLayout) view.findViewById(R.id.Person_stick);
		Personnel_information = (LinearLayout) view
				.findViewById(R.id.Personnel_information);
		Person_Feedback = (LinearLayout) view
				.findViewById(R.id.Person_Feedback);
		Person_Setting_Center = (LinearLayout) view
				.findViewById(R.id.Person_Setting_Center);
		try {
			Bitmap bit = null;
			bit = BitmapFactory.decodeFile(FILE_PIC_SCREENSHOT + "/"
					+ userBean.getLogonName() + ".png");
			if (bit.equals(null)) {
				pic_hdl = new PicHandler();
				Thread t = new LoadPicThread();
				t.start();
			}
			Bitmap bitmap = Bimp.createFramedPhoto(480, 480, bit,
					(int) (20 * 1.6f));
			Head_portrait.setImageBitmap(bitmap);
		} catch (Exception e) {
			// TODO: handle exception
			pic_hdl = new PicHandler();
			Thread t = new LoadPicThread();
			t.start();
		}

		Head_portrait.setOnClickListener(this);
		Person_stick.setOnClickListener(this);
		Personnel_information.setOnClickListener(this);
		Person_Feedback.setOnClickListener(this);
		Person_Setting_Center.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Head_portrait:
			View view = getActivity().getLayoutInflater().inflate(
					R.layout.photo_choose_dialog, null);
			dialog = new Dialog(getActivity(),
					R.style.transparentFrameWindowStyle);
			dialog.setContentView(view, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			Window window = dialog.getWindow();
			photo_camera = (Button) dialog.findViewById(R.id.photo_camera);
			photo_cancel = (Button) dialog.findViewById(R.id.photo_cancel);
			photo_gallery = (Button) dialog.findViewById(R.id.photo_gallery);
			photo_camera.setOnClickListener(this);
			photo_cancel.setOnClickListener(this);
			photo_gallery.setOnClickListener(this);
			// 设置显示动画
			window.setWindowAnimations(R.style.main_menu_animstyle);
			WindowManager.LayoutParams wl = window.getAttributes();
			wl.x = 0;
			wl.y = getActivity().getWindowManager().getDefaultDisplay()
					.getHeight();
			// 以下这两句是为了保证按钮可以水平满屏
			wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
			wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

			// 设置显示位置
			dialog.onWindowAttributesChanged(wl);
			// 设置点击外围解散
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
			break;
		case R.id.Person_stick:
			intent = new Intent(getActivity(), Person_My_message.class);
			startActivity(intent);
			break;
		case R.id.Personnel_information:
			intent = new Intent(getActivity(), Person_Information.class);
			startActivity(intent);
			break;
		case R.id.Person_Feedback:
			intent = new Intent(getActivity(), Person_Feedback.class);
			startActivity(intent);
			break;
		case R.id.Person_Setting_Center:
			intent = new Intent(getActivity(), Person_Setting_Center.class);
			startActivity(intent);
			break;
		case R.id.photo_camera:
			localTempImageFileName = userBean.getLogonName()+"原图" + ".png";
			File filePath = FILE_PIC_SCREENSHOT;
			if (!filePath.exists()) {
				filePath.mkdirs(); // 如果文件夹不存在,则重新创建
			}
			Intent intent1 = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

			File f = new File(filePath, localTempImageFileName);
			Uri u = Uri.fromFile(f);
			intent1.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			intent1.putExtra(MediaStore.EXTRA_OUTPUT, u);
			startActivityForResult(intent1, Start_CAMERA);
			dialog.cancel();
			break;
		case R.id.photo_cancel:
			dialog.cancel();
			break;
		case R.id.photo_gallery:
			Intent intent = new Intent();// 调用图库
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_PICK);// 调用系统图库
			startActivityForResult(intent, Start_Photo_Gallery);
			dialog.cancel();
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Start_Photo_Gallery:
			if (resultCode == getActivity().RESULT_OK && null != data) {
				if (data != null) {
					Uri uri = data.getData();
					if (!TextUtils.isEmpty(uri.getAuthority())) {
						Cursor cursor = getActivity()
								.getContentResolver()
								.query(uri,
										new String[] { MediaStore.Images.Media.DATA },
										null, null, null);
						if (null == cursor) {
							Toast.makeText(getActivity(), "照片不存在",
									Toast.LENGTH_SHORT).show();
							return;
						}
						cursor.moveToFirst();
						String path = cursor.getString(cursor
								.getColumnIndex(MediaStore.Images.Media.DATA));
						cursor.close();
						Intent intent = new Intent(getActivity(),
								CropImageActivity.class);
						intent.putExtra("path", path);
						startActivityForResult(intent, Shear_picture);
					} else {
						// Log.i(TAG,"path=" + uri.getPath());
						Intent intent = new Intent(getActivity(),
								CropImageActivity.class);
						intent.putExtra("path", uri.getPath());
						startActivityForResult(intent, Shear_picture);
					}
				}
			}
			break;
		case Start_CAMERA:
			File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
			Intent intent = new Intent(getActivity(), CropImageActivity.class);
			intent.putExtra("path", f.getAbsolutePath());
			startActivityForResult(intent, Shear_picture);
			break;
		case Shear_picture:
			if (resultCode == getActivity().RESULT_OK && null != data) {
				final String path = data.getStringExtra("path");
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				bitmap = Bimp.createFramedPhoto(480, 480, bitmap,
						(int) (20 * 1.6f));
//				GenerateQRCodeActivity.uploadFile(path);
				UploadFileTask uploadFileTask=new UploadFileTask(getActivity());
				uploadFileTask.execute(path);
				Head_portrait.setImageBitmap(bitmap);
			}else {
				return;
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	class LoadPicThread extends Thread {
		@Override
		public void run() {
			try {
				//訪問服務器獲取當前用戶的頭像
				Bitmap img = getUrlImage(MyConstant.GET_WSDL_PICTURE + "image/"
						+ userBean.getLogonName() + ".png");
				if (img.equals(null)) {
					return;
				}
				Message msg = pic_hdl.obtainMessage();
				msg.what = 0;
				msg.obj = img;
				pic_hdl.sendMessage(msg);		
			} catch (Exception e) {
				// TODO: handle exception
				onResume();
				return;
			}
		}
	}

	class PicHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// String s = (String)msg.obj;
			// ptv.setText(s);
			Bitmap myimg = (Bitmap) msg.obj;
			localTempImageFileName = userBean.getLogonName() + ".png";
			File filePath = FILE_PIC_SCREENSHOT;		
			if (!filePath.exists()) {
				filePath.mkdirs();   
			}
			File f = new File(filePath, localTempImageFileName);	//將圖片存到本地,方便下一次頭像獲取
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(f);
			} catch (Exception e) {
				// TODO: handle exception
			}
			myimg.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			try {
				fOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Bitmap bitmap = Bimp.createFramedPhoto(480, 480, myimg,
					(int) (20 * 1.6f));
			Head_portrait.setImageBitmap(bitmap);
		}
	}

	// 加载图片
	public Bitmap getUrlImage(String url) {
		DataInputStream dis;
		Bitmap img = null;
		try {
			URL picurl = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) picurl
					.openConnection();
			conn.setConnectTimeout(6000);// 设置超时
			conn.setDoInput(true);
			conn.setUseCaches(true);// 缓存
			conn.connect();
			// 接收客户端文件
			dis = new DataInputStream(conn.getInputStream());		//获得图片的数据流
			img = BitmapFactory.decodeStream(dis);
			System.out.println("-" + img.getHeight());
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

}
