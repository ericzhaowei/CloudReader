package com.ider.cloudreader.main.repostcomment;

import android.content.Context;

import com.sina.weibo.sdk.openapi.models.Comment;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/14.
 */

public interface ICommentView {

    void committing();
    void commentSuccess(String comment);
    void commentFailed(String message);
    Context getContext();

    void requestSuccess(ArrayList<Comment> comments);
    void requestFailed(String message);

}
