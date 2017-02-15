package com.ider.cloudreader.weibo.comment;

import android.content.Context;
import android.util.Log;

import com.ider.cloudreader.common.JsonUtil;
import com.ider.cloudreader.main.repostcomment.ICommentView;
import com.ider.cloudreader.weibo.user.AccessTokenKeeper;
import com.ider.cloudreader.weibo.user.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.models.Comment;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/14.
 */

public class CommentPresenter {

    private static final String TAG = "CommentPresenter";
    
    private ICommentView commentView;
    private Context context;
    private static final String COMMIT_COMMENT_URL = "https://api.weibo.com/2/comments/create.json";
    private static final String REQUEST_COMMENTS_URL = "https://api.weibo.com/2/comments/show.json";

    public CommentPresenter(ICommentView commentView) {
        this.commentView = commentView;
        this.context = commentView.getContext();
    }

    public void commitComment(String comment, String statusId) {

        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context);
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);

        parameters.put("access_token", accessToken.getToken());
        parameters.put("comment", comment);
        parameters.put("id", statusId);

        new AsyncWeiboRunner(context).requestAsync(COMMIT_COMMENT_URL, parameters, "POST", commitListener);
    }

    public void getComments(String statusId) {
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context);
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
        parameters.put("access_token", accessToken.getToken());
        parameters.put("id", statusId);
        new AsyncWeiboRunner(context).requestAsync(REQUEST_COMMENTS_URL, parameters, "GET", requestListener);
    }

    RequestListener commitListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            Log.i(TAG, "onComplete: " + s);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.i(TAG, "onWeiboException: " + e.getMessage());
        }
    };

    RequestListener requestListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            Log.i(TAG, "onComplete: " + s);
            ArrayList<Comment> comments = JsonUtil.parseComments(s);
            commentView.requestSuccess(comments);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            commentView.requestFailed(e.getMessage());
        }
    };



}
