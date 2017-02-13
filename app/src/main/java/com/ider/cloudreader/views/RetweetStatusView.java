package com.ider.cloudreader.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ider.cloudreader.R;
import com.ider.cloudreader.common.RegularExpression;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/9.
 */

public class RetweetStatusView extends ConstraintLayout {

    private TextView content;
    private ImageGridView imageGrid;
    private Status status;

    public RetweetStatusView(Context context) {
        this(context, null);
    }

    public RetweetStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(isInEditMode()) return;
        LayoutInflater.from(context).inflate(R.layout.retweeted_status_layout, this);
        content = (TextView) findViewById(R.id.retweet_status_content);
        imageGrid = (ImageGridView) findViewById(R.id.retweet_status_image_grid);
        content.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setStatus(Status status) {
        this.status = status;
        if(status == null) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            setContent(status);
            setPics(status.pic_urls);
        }

    }

    private void setContent(Status status) {
        String user = status.user.name;
        String text = status.text;
        String statusContent = String.format("@%s:%s", user, text);
        SpannableString spannableString = RegularExpression.checkText(getContext(), statusContent);
        content.setText(spannableString);
    }

    private void setPics(ArrayList<String> picList) {
        imageGrid.setPicUrls(picList);
    }


}
