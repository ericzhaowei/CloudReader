package com.ider.cloudreader.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ider.cloudreader.R;

/**
 * Created by ider-eric on 2017/2/18.
 */

public class ImageActivity extends Activity {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);
        image = (ImageView) findViewById(R.id.image_activity_image);

        String url = getIntent().getStringExtra("image");
        Glide.with(this).load(url).into(image);

    }



}
