package com.pattern.test.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by frog on 7/16/16.
 */
public class ImageLodaer {
    //图片缓存
    ImageCache mImageCache = new ImageCache();
    //线程池，线程数量为CPU数量
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ImageLodaer() {
    }


    public void displayImage(final String url, final ImageView imageView) {
        Bitmap bitmap = mImageCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        imageView.setTag(url);
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
