package com.pattern.test.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.pattern.test.imageCache.IImageCache;
import com.pattern.test.imageCache.MemoryCache;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by frog on 7/16/16.
 */
public class ImageLodaer {
    //图片缓存
    IImageCache mImageCache = new MemoryCache();
    //线程池，线程数量为CPU数量
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final String TAG = ImageLodaer.class.getSimpleName();

    /**
     * @param context
     * @param imageCache 可以为null 默认MemoryCache
     */
    public ImageLodaer(Context context, IImageCache imageCache) {
        if (imageCache != null) {
            mImageCache = imageCache;
        }

    }


    public void displayImage(String url, ImageView imageView) {
        displayImage(url, imageView, 0, 0);
    }

    /**
     * 展示图片
     *
     * @param url
     * @param imageView
     */
    public void displayImage(String url, ImageView imageView, int reqWidth, int reqHeight) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Bitmap bitmap = mImageCache.get(url, reqWidth, reqHeight);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        submitLoadRequest(url, imageView);
    }

    /**
     * 从网络获取图片
     *
     * @param url
     * @param imageView
     */
    private void submitLoadRequest(final String url, final ImageView imageView) {
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    imageView.setImageBitmap(bitmap);
                }
                mImageCache.put(url, bitmap);
            }
        });
    }

    private Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }


}
