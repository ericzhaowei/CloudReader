package com.ider.cloudreader.toolbar;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;

import com.ider.cloudreader.R;

/**
 * Created by ider-eric on 2017/1/12.
 */

public class MainTabHolder extends LinearLayout {

    private static final String TAG = "MainTabHolder";

    private int currentSelectPosition = 0;
    private LinearLayout root;

    private OnTabItemClickListener listener;

    public MainTabHolder(Context context) {
        this(context, null);
    }

    public MainTabHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.main_tabholder, this);
        root = (LinearLayout) findViewById(R.id.tabholder);
        initOutlineProvider();
        for(int i = 0; i < root.getChildCount(); i++) {
            final int position = i;
            root.getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelectPosition(position);
                    if(listener != null) {
                        listener.onTabItemClicke(position);
                    }
                }
            });
        }
    }


    public void setSelectPosition(int position) {

        root.getChildAt(currentSelectPosition).setSelected(false);
        root.getChildAt(position).setSelected(true);
        currentSelectPosition = position;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        root.getChildAt(0).setSelected(true);

    }

    public void setTabClickListener(OnTabItemClickListener listener) {
        this.listener = listener;
    }


    public void initOutlineProvider() {
        root.getChildAt(0).setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, 50, 50, 30);
            }
        });
    }
}
