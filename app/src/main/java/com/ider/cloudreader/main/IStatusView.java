package com.ider.cloudreader.main;

import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/7.
 */

public interface IStatusView {
    void displayLatestStatus(ArrayList<Status> statusList, boolean append);
    void loadError();
    void loadNoMore();
}
