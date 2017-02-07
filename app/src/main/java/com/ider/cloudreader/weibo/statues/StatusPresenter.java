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
    public StatusPresenter(Context context, IStatusView iStatusView) {
        this.context = context;
        this.iStatusView = iStatusView;
        mAccessToken = AccessTokenKeeper.readAccessToken(context);
        mStatuesApi = new StatusesAPI(context, Constants.APP_KEY, mAccessToken);
    }

    public void requestLatestStatues() {
        mStatuesApi.friendsTimeline(0L, 0L, 10, 1, false, 0, false, mListener);
    }

    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            Log.i(TAG, "onComplete: \n" + response);
            StatusList statuses = StatusList.parse(response);
            ArrayList<Status> statusList = statuses.statusList;
            iStatusView.displayLatestStatus(statusList);
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    };

}
