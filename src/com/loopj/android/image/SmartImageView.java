package com.loopj.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class SmartImageView extends ImageView {
    private static final int LOADING_THREADS = 4;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(LOADING_THREADS);

    private SmartImageTask currentTask;


    public SmartImageView(Context context) {
        super(context);
    }

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    // Helpers to set image by URL
    public void setImageUrl(String url) {
        setImage(new WebImage(url));
    }

    public void setImageUrl(String url, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(url), completeListener);
    }

    public void setImageUrl(String url, final Integer fallbackResource) {
        setImage(new WebImage(url), fallbackResource);
    }

    public void setImageUrl(String url, final Integer fallbackResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(url), fallbackResource, completeListener);
    }

    public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new WebImage(url), fallbackResource, loadingResource);
    }

    public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(new WebImage(url), fallbackResource, loadingResource, completeListener);
    }


    // Helpers to set image by contact address book id
    public void setImageContact(long contactId) {
        setImage(new ContactImage(contactId));
    }

    public void setImageContact(long contactId, final Integer fallbackResource) {
        setImage(new ContactImage(contactId), fallbackResource);
    }

    public void setImageContact(long contactId, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new ContactImage(contactId), fallbackResource, fallbackResource);
    }


    // Set image using SmartImage object
    public void setImage(final SmartImage image) {
        setImage(image, null, null, null);
    }

    public void setImage(final SmartImage image, final SmartImageTask.OnCompleteListener completeListener) {
        setImage(image, null, null, completeListener);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource) {
        setImage(image, fallbackResource, fallbackResource, null);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource, SmartImageTask.OnCompleteListener completeListener) {
        setImage(image, fallbackResource, fallbackResource, completeListener);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource) {
        setImage(image, fallbackResource, loadingResource, null);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource, final SmartImageTask.OnCompleteListener completeListener) {
    	// 设置下载的时候显示的图片
        if(loadingResource != null){
            setImageResource(loadingResource);
        }

        // 停止正在为该SmartImageView下载网络图片的线程
        if(currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }

        // 创建一个新的线程
        currentTask = new SmartImageTask(getContext(), image); // SmartImageTask实现Runnable方法
        currentTask.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if(bitmap != null) {
                    setImageBitmap(bitmap);
                } else {
                    // 如果下载失败,责显示预设的加载失败时显示的图片
                    if(fallbackResource != null) {
                        setImageResource(fallbackResource);
                    }
                }

                // 设置图片下载完成的回调回调
                if(completeListener != null){
                    completeListener.onComplete(bitmap);
                }
            }
        });

        // 将该下载图片的线程添加到线程池中并执行
        threadPool.execute(currentTask);
    }

    public static void cancelAllTasks() {
        threadPool.shutdownNow();
        threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
    }
}