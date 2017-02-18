package com.ider.cloudreader.main;

/**
 * Created by ider-eric on 2017/2/16.
 */

public class LoadStateInterfaceHolder {

    public static interface AdapterInterface {
        int STATE_LOADING = 1000;
        int STATE_ERROR = 1001;
        int STATE_NOMORE = 1002;

        void setLoadingState(int loadingState);
    }

    public static interface HolderInterface {
        void bind();
        void stateNoMore();
        void stateLoading();
        void stateError();
    }
}


