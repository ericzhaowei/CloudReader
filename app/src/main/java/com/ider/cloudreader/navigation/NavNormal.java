package com.ider.cloudreader.navigation;


import android.view.View;

/**
 * Created by ider-eric on 2017/1/13.
 */

class NavNormal implements NavItem {

    private int icon;
    private int title;
    /* 此变量标记一个位于最右边的子view,可能是textview或者是switchview */
    private Object obj;


    NavNormal(int iconRes, int titleRes) {
        this.icon = iconRes;
        this.title = titleRes;
    }

    NavNormal(int iconRes, int titleRes, Object obj) {
        this.icon = iconRes;
        this.title = titleRes;
        this.obj = obj;
    }

    int getIconRes() {
        return this.icon;
    }

    int getTitleRes() {
        return this.title;
    }

    Object getObj() {
        return this.obj;
    }



}
