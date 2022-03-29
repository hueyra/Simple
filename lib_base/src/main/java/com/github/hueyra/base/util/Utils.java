package com.github.hueyra.base.util;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by zhujun.
 * Date : 2021/08/16
 * Desc : __
 */
public class Utils {

    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
