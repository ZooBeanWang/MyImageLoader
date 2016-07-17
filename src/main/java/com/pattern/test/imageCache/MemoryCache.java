package com.pattern.test.imageCache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.pattern.test.utils.ImageLoaderUitls;

/**
 * Created by frog on 7/16/16.
 */
public class MemoryCache implements IImageCache {
    private LruCache<String, Bitmap> mImageCache;

    public MemoryCache() {
        initImageCache();
    }

    /**
     * 使用最大可用内存得1/8作为缓存
     */
    private void initImageCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mImageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap get(String url, int reqWidth, int reqHeight) {
        return mImageCache.get(ImageLoaderUitls.hashKeyFromUrl(url));
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        mImageCache.put(ImageLoaderUitls.hashKeyFromUrl(url), bitmap);
    }
}
