package com.zsm.foxconn.mypaperless.help;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.zsm.foxconn.mypaperless.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ImageShower extends Activity {
	File file;
	ImageView image_shower;
	public static final File FILE_SDCARD = Environment
			.getExternalStorageDirectory(); // 检测SD卡
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageshower);
		image_shower = (ImageView) findViewById(R.id.image_shower);
		
		image_shower.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ImageShower.this);
                builder.setItems(new String[]{getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	image_shower.setDrawingCacheEnabled(true);
                        Bitmap imageBitmap = image_shower.getDrawingCache();
                        if (imageBitmap != null) {
                            String photoname = "download.png";
                            file = new File(FILE_SDCARD,"Mypaperless-photo/images");
                            if (!file.exists()) {
                    			file.mkdirs(); // 如果文件夹不存在,则重新创建
                    		}
                    		File f = new File(file, photoname); // 將圖片存到本地,方便下一次頭像獲取
                    		FileOutputStream fOut = null;
                    		try {
                    			fOut = new FileOutputStream(f);

                    		} catch (Exception e) {
                    			// TODO: handle exception
                    		}
                    		imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
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
                    		UIHelper.ToastMessage(ImageShower.this, "保存至Mypaperless-photo/images");
                        }
                    }
                });
                builder.show();

                return true;
            }

        });
		//加載圖片
//		final ImageLoadingDialog dialog = new ImageLoadingDialog(this);
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.show();
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				dialog.dismiss();
//			}
//		}, 1000 * 2);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}

}
