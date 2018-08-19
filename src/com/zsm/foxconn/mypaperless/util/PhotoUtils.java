package com.zsm.foxconn.mypaperless.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;

public class PhotoUtils {
	 /**
	  * 获得缩略图(bitmap);
	  * @param mContext
	  * @param beforeBitmap
	  * @return
	  */
	 public static Bitmap getThumbnailbyBitmap(Context mContext,Bitmap beforeBitmap,int width,int height)
	 {
	   int w = mContext.getResources()
	       .getDimensionPixelOffset(width);
	   int h = mContext.getResources().getDimensionPixelSize(height);
	   Bitmap afterBitmap = ThumbnailUtils.extractThumbnail(beforeBitmap, w, h);
	   return afterBitmap;
	 }
	 /**
		 * 图片变圆角
		 * @param bitmap
		 * @param pixels
		 * @return
		 */
		public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) 
		{ 

			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),Config.ARGB_8888); 
			Canvas canvas = new Canvas(output); 
			final int color = 0xff424242; 
			final Paint paint = new Paint(); 
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); 
			final RectF rectF = new RectF(rect); 
			final float roundPx = pixels; 

			paint.setAntiAlias(true); 
			canvas.drawARGB(0, 0, 0, 0); 
			paint.setColor(color); 
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
			canvas.drawBitmap(bitmap, rect, rect, paint); 
			return output; 
		}
}
