package com.github.xiaofeidev.round.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * @author 黎曼
 * @date 2020/7/5
 */
public class SizeUtils {
    public static float dp2px(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }

    public static int px2dp(final float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
