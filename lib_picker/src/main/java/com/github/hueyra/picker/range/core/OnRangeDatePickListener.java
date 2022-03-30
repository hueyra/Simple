package com.github.hueyra.picker.range.core;

import androidx.annotation.NonNull;

import java.util.Date;

public interface OnRangeDatePickListener {

    void onRangeDatePicked(@NonNull Date startDate, @NonNull Date endDate);

}
