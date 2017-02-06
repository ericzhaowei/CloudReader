package com.ider.cloudreader.navigation;


import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by ider-eric on 2017/1/13.
 */

public class NavHeader implements NavItem {

    User user;

    NavHeader() {

    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
