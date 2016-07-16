package com.pattern.test.ImageCache;

import android.graphics.Bitmap;

/**
 * 内存和本地双缓存
 * Created by frog on 7/16/16.
 */
public class DoubleCache implements IImageCache {
    private MemoryCache mMemoryCache = new MemoryCache();
    private DiskCache mDiskCache = new DiskCache();

    @Override
    public void put(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
        mDiskCache.put(url, bitmap);
    }

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);

        if (bitmap == null) {
            bitmap = mDiskCache.get(url);
        }
        if (bitmap != null) {
            mMemoryCache.put(url, bitmap);
        }

        return bitmap;
    }
}
