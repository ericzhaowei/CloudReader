package com.ider.cloudreader.common;

import java.text.DecimalFormat;

/**
 * Created by ider-eric on 2017/2/18.
 */

public class CountFormatter {
    public static String formatCount(int count) {
        if(count / 10000 < 1) {
            return String.valueOf(count);
        } else if(count / 10000 < 10){
            float fCount = count / 10000f;
            DecimalFormat format = new DecimalFormat("#.#");
            return format.format(fCount) + "万";
        } else {
            return count / 10000 + "万";
        }
    }
}
