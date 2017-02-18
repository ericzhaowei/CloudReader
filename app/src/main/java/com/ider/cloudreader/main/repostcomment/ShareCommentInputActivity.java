package com.ider.cloudreader.main.repostcomment;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ider.cloudreader.R;
import com.ider.cloudreader.common.RegularExpression;
import com.ider.cloudreader.views.EditorTopBar;

public class ShareCommentInputActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "ShareCommentInputActivity";

    public static final String STATUS_ID_KEY = "status_id";
    public static final String TYPE_KEY = "type";
    public static final int TYPE_COMMENT = 0;
    public static final int TYPE_SHARE = 1;

    public static final int COMMENT_REQUEST_CODE = 100;
    public static final int REPOST_REQUEST_CODE = 101;
    public static final int RESULT_OK = 1000;

    public int type;
    private String statusId;
    private EditorTopBar topBar;
    private ImageView composePic, composeAt, composeGif, composeEmoji, composeMore;
    private EditText editText;
    private TextView vSend;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_comment_editor_layout);
        statusId = getIntent().getStringExtra(STATUS_ID_KEY);

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
                if(type == TYPE_COMMENT) {
                    if (editable.length() > 0) {
                        vSend.setEnabled(true);
                    } else {
                        vSend.setEnabled(false);
                    }
                }
            }
        });

        setupTopBar();
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    editText.setSelection(editText.getText().length());
                }
            });

        }
    }

    public void setupTopBar() {
        topBar = (EditorTopBar) findViewById(R.id.share_comment_editor_topbar);
        Intent intent = getIntent();
        type = intent.getIntExtra(TYPE_KEY, -1);
        String title;
        if(type == TYPE_COMMENT) {
            title = getString(R.string.editor_type_comment);
            editText.setHint(R.string.editor_hint_comment);
        } else if(type == TYPE_SHARE) {
            title = getString(R.string.editor_type_share);
            editText.setHint(R.string.editor_hint_repost);
            vSend.setEnabled(true);
        } else {
            this.finish();
            return;
        }
        topBar.setTitle(title);

        topBar.setListener(new EditorTopBar.Listener() {
            @Override
            public void onCancel() {
                ShareCommentInputActivity.this.finish();
            }

            @Override
            public void onSend() {
                String text = editText.getText().toString();
                if(type == TYPE_COMMENT) {
                    if (text.length() > 140) {
                        Toast.makeText(ShareCommentInputActivity.this, R.string.comment_content_tolong, Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("text", text);
                        setResult(100, intent);
                        ShareCommentInputActivity.this.finish();
                    }
                } else if (type == TYPE_SHARE) {
                    Intent intent = new Intent();
                    intent.putExtra("text", text);
                    intent.putExtra("statusId", statusId);
                    setResult(RESULT_OK, intent);
                    ShareCommentInputActivity.this.finish();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.compose_at:
                Intent intent = new Intent(ShareCommentInputActivity.this, ContactsActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if(resultCode == 100) {
                Log.i(TAG, "onActivityResult: ");
                String username = data.getStringExtra("name");
                String id = data.getStringExtra("id");
                editText.append("@" + username + " ");
                String text = editText.getText().toString();
                SpannableString spannableString = RegularExpression.checkText(this, text);
                editText.setText(spannableString);
            }
        }
    }

}