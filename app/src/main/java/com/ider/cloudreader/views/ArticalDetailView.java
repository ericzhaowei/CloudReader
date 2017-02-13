package com.ider.cloudreader.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ider.cloudreader.R;

/**
 * Created by ider-eric on 2017/2/8.
 */

public class ArticalDetailView extends LinearLayout{

    private boolean animEnable;
    private ImageView iconView;
    private TextView countView;


    public ArticalDetailView(Context context) {
        this(context, null);
    }

    public ArticalDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(isInEditMode()) return;
        LayoutInflater.from(context).inflate(R.layout.artical_detail_layout, this);
        iconView = (ImageView) findViewById(R.id.status_count_type_icon);
        countView = (TextView) findViewById(R.id.status_count_type_text);
        if(attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StatusCommentContentView);
            Drawable iconRes = array.getDrawable(R.styleable.StatusCommentContentView_status_type_count_image);
            animEnable = array.getBoolean(R.styleable.StatusCommentContentView_status_type_count_imageAnim, false);
            iconView.setImageDrawable(iconRes);
            array.recycle();
        }
    }

    public void setCount(int count) {
        countView.setText(String.valueOf(count));
    }

}
