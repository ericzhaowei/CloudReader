package com.ider.cloudreader.weibo.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.EGLDisplay;

import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by ider-eric on 2017/2/6.
 */

public class UserKeeper {
    private static final String PREFERENCE_NAME = "weibo_user_preference";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_PROFILE_URL = "profile_url";
    private static final String KEY_STATUES_COUNT = "statues_count";
    private static final String KEY_FRIENDS_COUNT = "friends_count";
    private static final String KEY_FANS_COUNT = "fans_count";
    private static final String KEY_UID = "uid";

    public static void writeUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_NAME, user.name);
        editor.putString(KEY_PROFILE_URL, user.avatar_large);
        editor.putInt(KEY_STATUES_COUNT, user.statuses_count);
        editor.putInt(KEY_FRIENDS_COUNT, user.friends_count);
        editor.putInt(KEY_FANS_COUNT, user.followers_count);
        editor.putString(KEY_UID, user.id);
        editor.apply();
    }

    public static User readUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_APPEND);
        User user = new User();
        user.name = preferences.getString(KEY_NAME, "");
        user.avatar_large = preferences.getString(KEY_PROFILE_URL, "");
        user.statuses_count = preferences.getInt(KEY_STATUES_COUNT, 0);
        user.friends_count = preferences.getInt(KEY_FRIENDS_COUNT, 0);
        user.followers_count = preferences.getInt(KEY_FANS_COUNT, 0);
        user.id = preferences.getString(KEY_UID, "");
        return user;

    }

    public static void removeUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
