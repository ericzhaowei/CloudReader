package com.ider.cloudreader.weibo.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by Eric on 2017-02-04.
 * 该类定义了微博授权时所需要的参数
 */

public class AccessTokenKeeper {

    private static final String PREFERENCE_NAME = "weibo_preference";

    private static final String KEY_UID = "uid";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_TIME = "expires_time";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";


    public static void writeAccessToken(Context context, Oauth2AccessToken accessToken) {
        if (context == null || accessToken == null) {
            return;
        }
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_UID, accessToken.getUid());
        editor.putString(KEY_ACCESS_TOKEN, accessToken.getToken());
        editor.putString(KEY_REFRESH_TOKEN, accessToken.getRefreshToken());
        editor.putLong(KEY_EXPIRES_TIME, accessToken.getExpiresTime());
        editor.apply();
    }


    public static Oauth2AccessToken readAccessToken(Context context) {
        if(context == null) {
            return null;
        }
        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_APPEND);
        token.setUid(preferences.getString(KEY_UID, ""));
        token.setToken(preferences.getString(KEY_ACCESS_TOKEN, ""));
        token.setRefreshToken(preferences.getString(KEY_REFRESH_TOKEN, ""));
        token.setExpiresTime(preferences.getLong(KEY_EXPIRES_TIME, 0));
        return token;

    }


    public static void clear(Context context) {
        if(context == null) {
            return;
        }
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

}
