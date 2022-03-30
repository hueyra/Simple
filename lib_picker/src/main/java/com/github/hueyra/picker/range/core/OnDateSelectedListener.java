package com.github.hueyra.picker.range.core;

import androidx.annotation.NonNull;

import java.util.Date;

public interface OnDateSelectedListener {

    void onSingleSelected(@NonNull Date date);

    void onRangeSelected(@NonNull Date start, @NonNull Date end);

}
