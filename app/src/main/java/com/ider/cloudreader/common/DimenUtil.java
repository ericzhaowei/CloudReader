package com.ider.cloudreader.common;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by ider-eric on 2017/2/9.
 */

public class DimenUtil {
    public static int dp2px(Context context, int dp) {
        Resources res = context.getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, displayMetrics);
    }
}
