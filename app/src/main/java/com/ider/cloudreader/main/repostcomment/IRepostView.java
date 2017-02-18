package com.ider.cloudreader.main.repostcomment;

import android.content.Context;

/**
 * Created by ider-eric on 2017/2/18.
 */

public interface IRepostView {

    void reposting();
    void repostSuccess(String s);
    void repostFailed(String message);
    Context getContext();


}
