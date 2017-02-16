package com.ider.cloudreader.main;

/**
 * Created by ider-eric on 2017/2/16.
 */

public class LoadStateInterfaceHolder {

    public static interface AdapterInterface {
        public static final int STATE_LOADING = 1000;
        public static final int STATE_ERROR = 1001;
        public static final int STATE_NOMORE = 1002;

        void loadMore();
        void noMoreItem();
        void loadError();
    }

    public static interface HolderInterface {
        void bind();
        void stateNoMore();
        void stateLoading();
        void stateError();
    }
}


