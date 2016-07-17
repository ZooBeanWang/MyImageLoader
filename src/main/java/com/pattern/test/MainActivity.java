package com.pattern.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.pattern.test.imageCache.DoubleCache;
import com.pattern.test.imageloader.ImageLodaer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img = (ImageView) findViewById(R.id.iv_test);
        ImageLodaer imageLodaer = new ImageLodaer(this, new DoubleCache(this));
        imageLodaer.displayImage("http://pic.mmfile.net/2013/06/201306214839flhqf1wzcfi.jpg", img);
    }
}
