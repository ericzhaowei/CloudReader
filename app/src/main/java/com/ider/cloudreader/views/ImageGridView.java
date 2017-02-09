package com.ider.cloudreader.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ider.cloudreader.R;
import com.ider.cloudreader.common.DimenUtil;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/9.
 */

public class ImageGridView extends GridLayout {

    private static final String TAG = "ImageGridView";

    private ArrayList<String> picUrls;
    private int picWidth, picHeight;

    public ImageGridView(Context context) {
        this(context, null);
    }

    public ImageGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColumnCount(3);
        setRowCount(3);
        picWidth = DimenUtil.dp2px(context, context.getResources().getDimensionPixelSize(R.dimen.status_item_pic_width));
        picHeight = DimenUtil.dp2px(context, context.getResources().getDimensionPixelSize(R.dimen.status_item_pic_height));
    }

    public void setPicUrls(ArrayList<String> picUrls) {
        if(picUrls == null) {
            setVisibility(View.GONE);
            return;
        }

        removeAllViews();
        setVisibility(View.VISIBLE);
        this.picUrls = picUrls;
        for (String picUrl : picUrls) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LayoutParams lp = new LayoutParams();
            lp.width = picWidth;
            lp.height = picHeight;
            lp.setMargins(6, 6, 6, 6);
            imageView.setLayoutParams(lp);
            addView(imageView);
            Log.i(TAG, "setPicUrls: " + picUrl);
            Glide.with(getContext()).load(picUrl).into(imageView);

        }
    }





}
