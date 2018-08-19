package com.loopj.android.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WebImage implements SmartImage {
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 10000;
    private int _displaywidth =1080;
	private int _displayheight =1920;
	private int _displaypixels = _displaywidth * _displayheight;
    private static WebImageCache webImageCache;

    private String url;

    public WebImage(String url) {
        this.url = url;
    }

    public Bitmap getBitmap(Context context) {
        // Don't leak context
        if(webImageCache == null) {
            webImageCache = new WebImageCache(context);
        }

        // Try getting bitmap from cache first
        Bitmap bitmap = null;
        if(url != null) {
            bitmap = webImageCache.get(url);
            if(bitmap == null) {
                bitmap = getBitmapFromUrl(url);
                if(bitmap != null){
                    webImageCache.put(url, bitmap);
                }
            }
        }

        return bitmap;
    }

    private Bitmap getBitmapFromUrl(String url) {
        Bitmap bitmap = null;

        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            BitmapFactory.Options opts = new BitmapFactory.Options();
    		InputStream stream = new URL(url).openStream();
    		byte[] bytes = getBytes(stream);
    		// 下面三行时处理图片溢出的开始（如果不需要处理溢出直接opts.inSampleSize=1）
    		opts.inJustDecodeBounds = true;
    		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
    		opts.inSampleSize = computeSampleSize(opts, -1, _displaypixels);
    		// end
//    		opts.inSampleSize =1;
    		opts.inJustDecodeBounds = false;
    		bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    /**
	 * 处理图片bitmap size exceeds VM budget(Out of Memory 内存溢出)
	 * 
	 * @param opts
	 * @param i
	 * @param displaypixels
	 * @return
	 */
	private int computeSampleSize(BitmapFactory.Options opts,
			int minSideLength, int maxNumofPixels) {
		// TODO Auto-generated method stub
		int initalSize = computeInitialSampleSize(opts, minSideLength,
				maxNumofPixels);
		int roundedSize;
		if (initalSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initalSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initalSize + 7) / 8 * 8;
		}
		return roundedSize;
	}
    public int computeInitialSampleSize(BitmapFactory.Options opts,
			int minSideLength, int maxNumofPixels) {
		double w = opts.outWidth;
		double h = opts.outHeight;
		int lowerBound = (maxNumofPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumofPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumofPixels == -1) && (minSideLength == -1)) 
		{
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
    
    /**
	 * 数据流转成btye[]数组
	 * 
	 * @param stream
	 * @return
	 */
	private byte[] getBytes(InputStream stream) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[2048];
		int len = 0;
		try {
			while ((len = stream.read(b, 0, 2048)) != -1) {
				baos.write(b, 0, len);
				baos.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	/**
	 * 获取设备的分辨率大小
	 * 
	 * @param width
	 */
	public void setDisplayWidth(int width) {
		_displaywidth = width;
	}

	public int getDisplayWidth() {
		return _displaywidth;
	}

	public void setDisplayHeight(int height) {
		_displayheight = height;
	}

	public int getDisplayHeight() {
		return _displayheight;
	}

	public int getDisplayPixels() {
		return _displaypixels;
	}
    public static void removeFromCache(String url) {
        if(webImageCache != null) {
            webImageCache.remove(url);
        }
    }
}
