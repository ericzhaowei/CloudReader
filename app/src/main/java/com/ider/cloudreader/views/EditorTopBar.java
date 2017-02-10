package com.ider.cloudreader.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.ider.cloudreader.R;

/**
 * Created by Eric on 2017-02-09.
 */

public class EditorTopBar extends ConstraintLayout {
    public EditorTopBar(Context context) {
        this(context, null);
    }

    public EditorTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.editor_page_topbar, this);
        setBackgroundColor(0xffffffff);
        setElevation(6);
    }
}
