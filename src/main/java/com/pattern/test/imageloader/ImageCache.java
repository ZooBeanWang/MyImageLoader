package com.pattern.test.imageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by frog on 7/16/16.
 */
public class ImageCache {
    LruCache<String, Bitmap> mImageCache;

    public ImageCache(LruCache<String, Bitmap> mImageCache) {
        initImageCache();
    }

    /**
     * 使用最大可用内存得1/4作为缓存
     */
    private void initImageCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;
        mImageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public Bitmap get(String url) {
        return mImageCache.get(url);
    }

    public void put(String url, Bitmap bitmap) {
        mImageCache.put(url, bitmap);
    }
}
