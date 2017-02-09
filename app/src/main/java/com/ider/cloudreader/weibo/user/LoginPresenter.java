package com.ider.cloudreader.weibo.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ider.cloudreader.main.ILoginView;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.openapi.RefreshTokenApi;
import com.sina.weibo.sdk.openapi.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric on 2017-02-04.
 */

public class LoginPresenter {

    private static final String TAG = "LoginPresenter";

    private Activity activity;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;


    public LoginPresenter(Activity activity) {
        this.activity = activity;
        // 微博授权类对象，保存应用信息
        AuthInfo mAuthInfo = new AuthInfo(activity, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        this.mSsoHandler = new SsoHandler(activity, mAuthInfo);
    }

    // 检查本地保存的AccessToken
    public void checkExistsAccessToken() {

        mAccessToken = AccessTokenKeeper.readAccessToken(activity);
        if(!mAccessToken.getUid().isEmpty() && mAccessToken.getExpiresTime() > System.currentTimeMillis()) {
            if(!checkExistUser()) {
                UserApi.getInstance(activity.getApplicationContext()).getUser(userListener);
            }

            // 剩余一天之内刷新期限
            if(mAccessToken.getExpiresTime() - System.currentTimeMillis() < 86400000) {
                refreshToken();
            }
        } else if(!mAccessToken.getUid().isEmpty() && mAccessToken.getExpiresTime() < System.currentTimeMillis()) {
            UserKeeper.removeUser(activity);
        }
    }

    // 检测本地保存user信息
    private boolean checkExistUser() {
        User user = UserKeeper.readUser(activity);
        if(!user.name.isEmpty()) {
            ((ILoginView) activity).showUser(user, mAccessToken);
            return true;
        }
        return false;
    }

    public void login() {
        this.mSsoHandler.authorize(new AuthListener());
    }


    public void onAuthorizeCallback(int requestCode, int resultCode, Intent data) {
        if(mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle bundle) {
            if(activity instanceof ILoginView) {
                Toast.makeText(activity, "登陆成功", Toast.LENGTH_LONG).show();

                /** 封装了“access_token”，“expires_in”，“refresh_token”，并提供了他们的管理功能 */
                mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                if(mAccessToken.isSessionValid()) {
                    Log.i(TAG, "onComplete: uid = " + mAccessToken.getUid());
                    AccessTokenKeeper.writeAccessToken(activity, mAccessToken);
                    UserApi.getInstance(activity.getApplicationContext()).getUser(userListener);

                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if(activity instanceof ILoginView) {

            }
        }

        @Override
        public void onCancel() {
            if(activity instanceof ILoginView) {

            }
        }
    }

    private RequestListener userListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            Log.i(TAG, "onComplete: " + response);
            User user = User.parse(response);
            if (user != null) {
                UserKeeper.writeUser(activity, user);
                ((ILoginView) activity).showUser(user, mAccessToken);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.i(TAG, "onWeiboException: " + e.getMessage());
        }
    };

    public void refreshToken() {
        RefreshTokenApi.create(activity).refreshToken(Constants.APP_KEY, mAccessToken.getRefreshToken(), new RequestListener() {
            @Override
            public void onComplete(String response) {
                Log.i(TAG, "onComplete: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String accessToken = jsonObject.getString("access_token");
                    long expiresTime = jsonObject.getLong("expires_in") + System.currentTimeMillis();
                    String refreshToken = jsonObject.getString("refresh_token");
                    String uid = jsonObject.getString("uid");
                    mAccessToken.setUid(uid);
                    mAccessToken.setToken(accessToken);
                    mAccessToken.setRefreshToken(refreshToken);
                    mAccessToken.setExpiresTime(expiresTime);
                    AccessTokenKeeper.writeAccessToken(activity, mAccessToken);
                    UserApi.getInstance(activity.getApplicationContext()).getUser(userListener);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onWeiboException(WeiboException e) {

            }
        });
    }


}
