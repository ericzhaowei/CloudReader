package com.ider.cloudreader.common;

import android.util.Log;

/**
 * Created by ider-eric on 2017/2/14.
 */

public class LogUtil {
    //log4k问题
    public static void log(String tag, String str) {
        int index = 0; // 当前位置
        int max = 1000;// 需要截取的最大长度,别用4000
        String sub;    // 进行截取操作的string
        while (index < str.length()) {
            if (str.length() < max) { // 如果长度比最大长度小
                max = str.length();   // 最大长度设为length,全部截取完成.
                sub = str.substring(index, max);
            } else {
                sub = str.substring(index, max);
            }
            Log.i(tag, sub);         // 进行输出
            index = max;
            max += 1000;
        }
    }
}
