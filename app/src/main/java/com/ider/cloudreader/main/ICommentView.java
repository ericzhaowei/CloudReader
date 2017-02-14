package com.ider.cloudreader.main;

import android.content.Context;

/**
 * Created by ider-eric on 2017/2/14.
 */

public interface ICommentView {

    void committing();
    void commentSuccess(String comment);
    void commentFailed(String message);
    Context getContext();

}
