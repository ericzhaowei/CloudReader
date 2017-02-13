package com.ider.cloudreader.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ider.cloudreader.R;
import com.ider.cloudreader.weibo.user.UserKeeper;

/**
 * Created by Eric on 2017-02-09.
 */

public class EditorTopBar extends ConstraintLayout {
    private TextView vSubtitle;
    private TextView vTitle;
    private TextView vCancel;
    private TextView vSend;

    private Listener listener;

    public interface Listener {
        void onCancel();
        void onSend();
    }


    public EditorTopBar(Context context) {
        this(context, null);
    }

    public EditorTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.editor_page_topbar, this);
        setBackgroundColor(0xffffffff);
        setElevation(6);

        vTitle = (TextView) findViewById(R.id.share_comment_editor_title);
        vCancel = (TextView) findViewById(R.id.editor_topbar_cancel);
        vSend = (TextView) findViewById(R.id.share_comment_editor_send);

        vSubtitle = (TextView) findViewById(R.id.share_comment_editor_subtitle);
        vSubtitle.setText(UserKeeper.readUser(context).name);

        vCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onCancel();
                }
            }
        });
        vSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onSend();
                }
            }
        });

    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setTitle(String title) {
        vTitle.setText(title);
    }


}
