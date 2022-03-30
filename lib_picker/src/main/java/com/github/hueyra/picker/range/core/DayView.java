package com.github.hueyra.picker.range.core;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.github.hueyra.picker.R;

public final class DayView extends LinearLayout {
    private TextView tvDesc;
    private TextView tvDay;
    private DayEntity entity;

    public DayView(@NonNull Context context) {
        super(context);
        initialize(context);
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        //setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tvDay = new TextView(context);
        tvDay.setGravity(Gravity.CENTER);
        tvDay.setTextSize(15);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(40f));
        lp.topMargin = dp2px(10f);
        lp.bottomMargin = dp2px(10f);
        tvDay.setLayoutParams(lp);
        addView(tvDay);

//        tvDesc = new TextView(context);
//        tvDesc.setGravity(Gravity.CENTER);
//        tvDesc.setTextSize(12);
//        addView(tvDesc, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setValue(DayEntity entity, ColorScheme scheme) {
        if (null != getValue()) {
            getValue().recycle();
        }
        this.entity = entity;
        //内容
        tvDay.setText(entity.value());
        setTextStatusColor(tvDay, entity.valueStatus(), scheme);
        //描述
        //tvDesc.setText(entity.desc());
        //setTextStatusColor(tvDesc, entity.descStatus(), scheme);
        //背景
        setBackgroundStatus(entity, scheme);
    }

    public DayEntity getValue() {
        return entity;
    }

    private void setTextStatusColor(TextView tv, @DayStatus int status, ColorScheme scheme) {
        switch (status) {
            //正常
            case DayStatus.NORMAL:
                tv.setTextColor(scheme.dayNormalTextColor());
                break;
            //不可用
            case DayStatus.INVALID:
                tv.setTextColor(scheme.dayInvalidTextColor());
                break;
            //强调
            case DayStatus.STRESS:
                tv.setTextColor(scheme.dayStressTextColor());
                break;
            //范围内、左边界
            case DayStatus.RANGE:
            case DayStatus.BOUND_L:
            case DayStatus.BOUND_M:
            case DayStatus.BOUND_R:
                //tv.setTextColor(scheme.daySelectTextColor());
                break;
            default:
                break;
        }
    }

    private void setBackgroundStatus(DayEntity entity, ColorScheme scheme) {
        switch (entity.status()) {
            //正常、强调
            case DayStatus.NORMAL:
            case DayStatus.STRESS:
                tvDay.setBackgroundColor(scheme.dayNormalBackgroundColor());
                setEnabled(true);
                break;
            //不可用
            case DayStatus.INVALID:
                tvDay.setTextColor(scheme.dayInvalidTextColor());
                tvDay.setBackgroundColor(scheme.dayInvalidBackgroundColor());
                setEnabled(false);
                break;
            //范围内
            case DayStatus.RANGE:
                tvDay.setBackgroundColor(scheme.daySelectRangeBackgroundColor());
                setEnabled(true);
                break;
            //左边界、单选、右边界
            case DayStatus.BOUND_L:
                tvDay.setTextColor(scheme.daySelectTextColor());
                tvDay.setBackgroundResource(R.drawable.bg_day_select_left);
                break;
            case DayStatus.BOUND_M:
                tvDay.setTextColor(scheme.daySelectTextColor());
                tvDay.setBackgroundResource(R.drawable.bg_day_select_single);
                break;
            case DayStatus.BOUND_R:
                tvDay.setTextColor(scheme.daySelectTextColor());
                tvDay.setBackgroundResource(R.drawable.bg_day_select_right);
                break;
            default:
                break;
        }
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

}