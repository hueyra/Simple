package com.github.hueyra.picker.range.core;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hueyra.picker.R;


@SuppressWarnings("unused")
public class RangeCalendarView extends LinearLayout {
    private final CalendarAdapter calendarAdapter = new CalendarAdapter();
    private final WeekAdapter weekAdapter = new WeekAdapter();
    private final GridView weekView;
    private final RecyclerView bodyView;

    public RangeCalendarView(Context context) {
        this(context, null);
    }

    public RangeCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        inflate(context, R.layout.layout_calendar_body, this);
        weekView = findViewById(R.id.calendar_body_week);
        weekView.setNumColumns(weekAdapter.getCount());
        weekView.setAdapter(weekAdapter);
        weekView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        bodyView = findViewById(R.id.calendar_body_content);
        bodyView.setLayoutManager(new LinearLayoutManager(context));
        bodyView.setAdapter(calendarAdapter);
    }

    public void setColorScheme(ColorScheme colorScheme) {
        weekAdapter.setColorScheme(colorScheme);
        calendarAdapter.colorScheme(colorScheme);
    }

    public final GridView getWeekView() {
        return weekView;
    }

    public final RecyclerView getBodyView() {
        return bodyView;
    }

    public final LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) bodyView.getLayoutManager();
    }

    public final CalendarAdapter getAdapter() {
        return calendarAdapter;
    }

}