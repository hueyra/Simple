package com.github.hueyra.picker.range.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface DayStatus {
    //正常
    int NORMAL = 0;
    //不可用
    int INVALID = 1;
    //范围内
    int RANGE = 2;
    //左边界
    int BOUND_L = 3;
    //单选
    int BOUND_M = 4;
    //右边界
    int BOUND_R = 5;
    //强调
    int STRESS = 6;
}