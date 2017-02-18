package com.ider.cloudreader.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ider.cloudreader.R;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by ider-eric on 2017/2/18.
 */

public class ImageActivity extends Activity {
    private static final String TAG = "ImageActivity";
    private PhotoView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);
        image = (PhotoView) findViewById(R.id.image_activity_image);

        String url = getIntent().getStringExtra("image");
        Glide.with(this).load(url).into(image);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}
