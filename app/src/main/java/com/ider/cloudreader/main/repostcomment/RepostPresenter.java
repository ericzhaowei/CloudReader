package com.ider.cloudreader.main.repostcomment;

import android.content.Context;
import android.util.Log;

import com.ider.cloudreader.weibo.user.AccessTokenKeeper;
import com.ider.cloudreader.weibo.user.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * Created by ider-eric on 2017/2/18.
 */

public class RepostPresenter {

    private static final String TAG = "RepostPresenter";
    private IRepostView repostView;
    private Context context;
    private static final String REPOST_URL = "https://api.weibo.com/2/statuses/repost.json";

    public RepostPresenter(IRepostView repostView) {
        this.repostView = repostView;
        this.context = repostView.getContext();
    }

    public void repostStatus(String statusId, String text) {
        repostView.reposting();
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(repostView.getContext());
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
        parameters.put("access_token", accessToken.getToken());
        Log.i(TAG, "repostStatus: statusid = " + statusId);
        parameters.put("id", statusId);
        if(text != null) {
            parameters.put("status", text);
        }
        new AsyncWeiboRunner(context).requestAsync(REPOST_URL, parameters, "POST", mListener);
    }

    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            repostView.repostSuccess(s);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            repostView.repostFailed(e.getMessage());
        }
    };

}
