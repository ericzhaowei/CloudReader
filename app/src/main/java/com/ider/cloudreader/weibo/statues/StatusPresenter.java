package com.ider.cloudreader.weibo.statues;

import android.content.Context;
import android.util.Log;

import com.ider.cloudreader.main.IStatusView;
import com.ider.cloudreader.weibo.user.AccessTokenKeeper;
import com.ider.cloudreader.weibo.user.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/7.
 */

public class StatusPresenter {

    private static final String TAG = "StatusPresenter";

    private Context context;

    private Oauth2AccessToken mAccessToken;
    private StatusesAPI mStatuesApi;

    private IStatusView iStatusView;
    private boolean loading;
    private boolean append; // statusList追加或者是更新

    public StatusPresenter(Context context, IStatusView iStatusView) {
        this.context = context;
        this.iStatusView = iStatusView;

    }

    /**
     * 刷新微博数据
     *
     * @param accessToken 为null时，读取本地accessToken
     */
    public void refreshStatues(Oauth2AccessToken accessToken, boolean append, long maxId) {
        loading = true;
        if (accessToken != null && accessToken.isSessionValid()) {
            this.mAccessToken = accessToken;
        } else {
            mAccessToken = AccessTokenKeeper.readAccessToken(context);
        }
        mStatuesApi = new StatusesAPI(context, Constants.APP_KEY, mAccessToken);
        this.append = append;
        mStatuesApi.friendsTimeline(0L, maxId, 10, 1, false, 0, false, mListener);
    }


    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            Log.i(TAG, "onComplete: " + response);
            StatusList statuses = StatusList.parse(response);
            ArrayList<Status> statusList = statuses.statusList;
            if(statusList != null) {
                iStatusView.displayLatestStatus(statusList, append);
            } else {
                iStatusView.loadNoMore();
            }
            loading = false;
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.i(TAG, "onWeiboException: " + e.getMessage());
            loading = false;
            iStatusView.loadError();
        }
    };

    public boolean isLoading() {
        return loading;
    }

}
