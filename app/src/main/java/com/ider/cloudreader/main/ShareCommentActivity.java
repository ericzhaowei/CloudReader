package com.ider.cloudreader.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ider.cloudreader.R;
import com.ider.cloudreader.views.EditorTopBar;
import com.ider.cloudreader.weibo.comment.CommentPresenter;

public class ShareCommentActivity extends Activity implements View.OnClickListener, ICommentView{

    private static final String TAG = "ShareCommentActivity";

    public static final String STATUS_ID_KEY = "status_id";
    public static final String TYPE_KEY = "type";
    public static final int TYPE_COMMENT = 0;
    public static final int TYPE_SHARE = 1;
    private String statusId;
    private EditorTopBar topBar;
    private ImageView composePic, composeAt, composeGif, composeEmoji, composeMore;
    private EditText editText;
    private TextView vSend;


    private CommentPresenter commentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentPresenter = new CommentPresenter(this);
        setContentView(R.layout.share_comment_editor_layout);
        statusId = getIntent().getStringExtra(STATUS_ID_KEY);
        Log.i(TAG, "onCreate: " + statusId);
        setupTopBar();

        vSend = (TextView) findViewById(R.id.share_comment_editor_send);
        editText = (EditText) findViewById(R.id.share_comment_edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) {
                    vSend.setEnabled(true);
                } else {
                    vSend.setEnabled(false);
                }
            }
        });
        composePic = (ImageView) findViewById(R.id.compose_picture);
        composeAt = (ImageView) findViewById(R.id.compose_at);
        composeGif = (ImageView) findViewById(R.id.compose_gif);
        composeEmoji = (ImageView) findViewById(R.id.compose_emoji);
        composeMore = (ImageView) findViewById(R.id.compose_more);
        composePic.setOnClickListener(this);
        composeAt.setOnClickListener(this);
        composeGif.setOnClickListener(this);
        composeEmoji.setOnClickListener(this);
        composeMore.setOnClickListener(this);
    }

    public void setupTopBar() {
        topBar = (EditorTopBar) findViewById(R.id.share_comment_editor_topbar);
        Intent intent = getIntent();
        int type = intent.getIntExtra(TYPE_KEY, -1);
        String title;
        if(type == TYPE_COMMENT) {
            title = getString(R.string.editor_type_comment);
        } else if(type == TYPE_SHARE) {
            title = getString(R.string.editor_type_share);
        } else {
            this.finish();
            return;
        }
        topBar.setTitle(title);

        topBar.setListener(new EditorTopBar.Listener() {
            @Override
            public void onCancel() {
                ShareCommentActivity.this.finish();
            }

            @Override
            public void onSend() {
                String text = editText.getText().toString();
                if(text.length() > 140) {
                    Toast.makeText(ShareCommentActivity.this, R.string.comment_content_tolong, Toast.LENGTH_LONG).show();
                } else {
                    commentPresenter.commitComment(text, statusId);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.compose_at:
                Intent intent = new Intent(ShareCommentActivity.this, ContactsActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void commentSuccess(String comment) {

    }

    @Override
    public void commentFailed(String message) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void committing() {

    }
}