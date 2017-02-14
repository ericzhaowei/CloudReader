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
    private int type;
    private static final int TYPE_COMMENT = 1;
    private static final int TYPE_SHARE = 2;
    private static final int TYPE_LIKE = 3;
    private String detailText;


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
            int type = array.getInt(R.styleable.StatusCommentContentView_status_type, 1);
            setType(type);
            animEnable = array.getBoolean(R.styleable.StatusCommentContentView_status_type_count_imageAnim, false);
            array.recycle();
        }
    }

    public void setType(int type) {
        switch (type) {
            case TYPE_COMMENT:
                iconView.setImageResource(R.drawable.artical_detail_icon_comment);
                detailText = getResources().getString(R.string.status_comment);
                break;
            case TYPE_SHARE:
                iconView.setImageResource(R.drawable.artical_detail_icon_repost);
                detailText = getResources().getString(R.string.status_repost);
                break;
            case TYPE_LIKE:
                iconView.setImageResource(R.drawable.artical_detail_icon_like);
                detailText = getResources().getString(R.string.status_like);
                break;
        }
    }

    public void setCount(int count) {
        if(count == 0) {
            countView.setText(detailText);
        } else {
            countView.setText(String.valueOf(count));
        }

    }

}
