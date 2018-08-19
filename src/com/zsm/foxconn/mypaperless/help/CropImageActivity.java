package com.zsm.foxconn.mypaperless.help;

import com.zsm.foxconn.mypaperless.R;
import com.zsm.foxconn.mypaperless.base.BaseActivity;
import com.zsm.foxconn.mypaperless.bean.UserBean;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
public class CropImageActivity extends BaseActivity implements OnClickListener{
	
	private CropImageView mImageView;
	private Bitmap mBitmap;
	private UserBean userBean;
	
	private CropImage mCrop;
	
	private Button mSave;
	private Button mCancel,rotateLeft,rotateRight;
	private String mPath = "CropImageActivity";
	private String TAG = "";
	public int screenWidth = 0;
	public int screenHeight = 0;
	
	private ProgressBar mProgressBar;
	
	public static final int SHOW_PROGRESS = 2000;

	public static final int REMOVE_PROGRESS = 2001;
	
	private Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SHOW_PROGRESS:
				mProgressBar.setVisibility(View.VISIBLE);
				break;
			case REMOVE_PROGRESS:
				mHandler.removeMessages(SHOW_PROGRESS);
				mProgressBar.setVisibility(View.INVISIBLE);
				break;
			}
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_icar_modify_image);
        findViewById();
    }
    @Override
    protected void onStop(){
    	super.onStop();
    	if(mBitmap!=null){
    		mBitmap=null;
    	}
    }
    private void getWindowWH(){
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth=dm.widthPixels;
		screenHeight=dm.heightPixels;
	}
    private void resetImageView(Bitmap b){
    	 mImageView.clear();
    	 mImageView.setImageBitmap(b);
         mImageView.setImageBitmapResetBase(b, true);
         mCrop = new CropImage(this, mImageView,mHandler);
         mCrop.crop(b);
    }
    
    public void onClick(View v)
    {
    	switch (v.getId())
    	{
    	case R.id.gl_modify_avatar_cancel:
    		mCrop.cropCancel();
    		Intent intent1 = new Intent();
    		setResult(RESULT_CANCELED, intent1);
    		finish();
    		break;
    	case R.id.gl_modify_avatar_save:
    		String path = mCrop.saveToLocal(mCrop.cropAndSave(),userBean.getLogonName());
    		Intent intent = new Intent();
    		intent.putExtra("path", path);
    		setResult(RESULT_OK, intent);
    		finish();
    		break;
    	case R.id.gl_modify_avatar_rotate_left:
    		mCrop.startRotate(270.f);
    		break;
    	case R.id.gl_modify_avatar_rotate_right:
    		mCrop.startRotate(90.f);
    		break;
    		
    	}
    }
    protected void addProgressbar() {
		mProgressBar = new ProgressBar(this);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		addContentView(mProgressBar, params);
		mProgressBar.setVisibility(View.INVISIBLE);
	}
    
    public Bitmap createBitmap(String path,int w,int h){
    	try{
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int destWidth = 0;
			int destHeight = 0;
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inSampleSize = (int) ratio + 1;
			newOpts.inJustDecodeBounds = false;
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    }
	@Override
	protected void CheckLogin() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		userBean = (UserBean) getApplicationContext();
		getWindowWH();
    	mPath = getIntent().getStringExtra("path");
        mImageView = (CropImageView) findViewById(R.id.gl_modify_avatar_image);
        mSave = (Button) this.findViewById(R.id.gl_modify_avatar_save);
        mCancel = (Button) this.findViewById(R.id.gl_modify_avatar_cancel);
        rotateLeft = (Button) this.findViewById(R.id.gl_modify_avatar_rotate_left);
        rotateRight = (Button) this.findViewById(R.id.gl_modify_avatar_rotate_right);
        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        rotateLeft.setOnClickListener(this);
        rotateRight.setOnClickListener(this);
        try{
            mBitmap = createBitmap(mPath,screenWidth,screenHeight);
            if(mBitmap==null){
    			finish();
            }else{
            	resetImageView(mBitmap);
            }
        }catch (Exception e) {
			finish();
		}
        addProgressbar();  
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}
    
}