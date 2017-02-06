package com.ider.cloudreader.weibo;

import android.app.Activity;

import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by Eric on 2017-02-04.
 */

public interface ILoginView {

    void showUser(User user);

}
