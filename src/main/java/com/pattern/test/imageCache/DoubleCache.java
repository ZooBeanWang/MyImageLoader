package com.pattern.test.imageCache;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 内存和本地双缓存
 * Created by frog on 7/16/16.
 */
public class DoubleCache implements IImageCache {
    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;


    public DoubleCache(Context context) {
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(context);
    }


    @Override
    public void put(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
        mDiskCache.put(url, bitmap);
    }

    @Override
    public Bitmap get(String url, int reqWidth, int reqHeight) {
        Bitmap bitmap = mMemoryCache.get(url, reqWidth, reqHeight);

        if (bitmap == null) {
            bitmap = mDiskCache.get(url, reqWidth, reqHeight);
        }
        if (bitmap != null) {
            mMemoryCache.put(url, bitmap);
        }

        return bitmap;
    }
}
