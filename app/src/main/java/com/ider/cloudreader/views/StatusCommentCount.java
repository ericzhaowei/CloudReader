package com.ider.cloudreader.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ider.cloudreader.R;

/**
 * Created by ider-eric on 2017/2/8.
 */

public class StatusCommentCount extends LinearLayout{

    public StatusCommentCount(Context context) {
        this(context, null);
    }

    public StatusCommentCount(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.status_item_type_count, this);

    }
}
