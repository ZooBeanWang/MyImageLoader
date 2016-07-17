package com.pattern.test.imageCache;

import android.graphics.Bitmap;

/**
 * Created by frog on 7/16/16.
 */
public interface IImageCache {
    void put(String url, Bitmap bitmap);

    Bitmap get(String url, int reqWidth, int reqHeight);
}
