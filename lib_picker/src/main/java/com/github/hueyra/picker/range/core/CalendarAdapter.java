package com.github.hueyra.picker.range.core;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.VH> implements OnDateClickListener {
    private boolean notify = true;
    private ColorScheme colorScheme = new ColorScheme();
    private final List<Date> dates = new ArrayList<>();
    private final Interval<Date> valid = new Interval<>();
    private final Interval<Date> select = new Interval<>();
    private final Interval<String> selectNote = new Interval<>();
    private boolean singleMode = false;
    private Date lastClickDate = null;
    private OnDateSelectedListener onDateSelectedListener;

    public CalendarAdapter notify(boolean notify) {
        this.notify = notify;
        return this;
    }

    public CalendarAdapter colorScheme(ColorScheme colorScheme) {
        if (colorScheme == null) {
            colorScheme = new ColorScheme();
        }
        this.colorScheme = colorScheme;
        return this;
    }

    public CalendarAdapter single(boolean value) {
        singleMode = value;
        if (notify) {
            refresh();
        }
        return this;
    }

    public CalendarAdapter valid(Date from, Date to) {
        valid.left(from);
        valid.right(to);
        if (notify) {
            refresh();
        }
        return this;
    }

    /**
     * 选择区间提示语
     *
     * @param noteFrom 开始日期提示语
     * @param noteTo   结束日期提示语
     */
    public CalendarAdapter intervalNotes(String noteFrom, String noteTo) {
        selectNote.left(noteFrom);
        selectNote.right(noteTo);
        if (notify) {
            refresh();
        }
        return this;
    }

    /**
     * 设置选择范围
     *
     * @param fromInMillis 开始日期
     * @param toInMillis   结束日期
     */
    public CalendarAdapter select(long fromInMillis, long toInMillis) {
        return select(new Date(fromInMillis), new Date(toInMillis));
    }

    /**
     * 设置选择范围
     *
     * @param from 开始日期
     * @param to   结束日期
     */
    public CalendarAdapter select(Date from, Date to) {
        select.left(from);
        select.right(to);
        if (notify) {
            refresh();
        }
        return this;
    }

    /**
     * 设置选择日期范围
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    public CalendarAdapter range(Date startDate, Date endDate) {
        return range(startDate, endDate, true);
    }

    public CalendarAdapter range(Date startDate, Date endDate, boolean clear) {
        List<Date> dates = DateUtils.fillDates(startDate, endDate);
        return range(dates, clear);
    }

    public CalendarAdapter range(List<Date> list, boolean clear) {
        if (clear) {
            dates.clear();
        }
        if (null != list && list.size() > 0) {
            dates.addAll(list);
        }
        if (notify) {
            refresh();
        }
        return this;
    }

    /**
     * @deprecated 使用 {@link #notify(boolean)} 及 {@link #range(Date, Date, boolean)} 代替
     */
    @Deprecated
    public CalendarAdapter setRange(Date startDate, Date endDate, boolean clear, boolean notify) {
        List<Date> dates = DateUtils.fillDates(startDate, endDate);
        this.notify = notify;
        return range(dates, clear);
    }

    /**
     * @deprecated 使用 {@link #notify(boolean)} 及 {@link #range(List, boolean)} 代替
     */
    @Deprecated
    public CalendarAdapter setRange(List<Date> list, boolean clear, boolean notify) {
        this.notify = notify;
        return range(list, clear);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh() {
        notifyDataSetChanged();
    }

    public void setOnCalendarSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = new LinearLayout(parent.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        //
        TextView titleView = new TextView(parent.getContext());
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(16);
        int padding = (int) (parent.getResources().getDisplayMetrics().density * 10);
        titleView.setPadding(padding, padding, padding, padding);
        linearLayout.addView(titleView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        //
        View line = new View(parent.getContext());
        line.setBackgroundColor(0xFFF1F4F9);
        line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        linearLayout.addView(line);
        //
        MonthView monthView = new MonthView(parent.getContext());
        linearLayout.addView(monthView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return new VH(linearLayout, titleView, monthView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.titleView.setBackgroundColor(colorScheme.monthTitleBackgroundColor());
        holder.titleView.setTextColor(colorScheme.monthTitleTextColor());
        holder.titleView.setText(TimeUtils.dateText(getDateValue(position).getTime(), TimeUtils.YY_M_CN));
        holder.monthView.setOnDayInMonthClickListener(this);
        holder.monthView.setValue(MonthEntity.obtain(valid, select)
                .date(dates.get(position))
                .singleMode(singleMode)
                .note(selectNote), colorScheme);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public final int getDatePosition(Date date) {
        int size = dates.size();
        if (size <= 1) {
            return 0;
        }
        long time = date.getTime();
        if (time <= dates.get(0).getTime()) {
            return 0;
        }
        int lastPosition = size - 1;
        if (time >= dates.get(lastPosition).getTime()) {
            return lastPosition;
        }
        for (int i = 0; i <= lastPosition; i++) {
            Calendar minDate = DateUtils.calendar(dates.get(i).getTime());
            minDate.set(Calendar.DAY_OF_MONTH, 1);
            minDate.set(Calendar.HOUR_OF_DAY, 0);
            minDate.set(Calendar.MINUTE, 0);
            minDate.set(Calendar.SECOND, 0);
            Calendar maxDate = DateUtils.calendar(dates.get(i).getTime());
            maxDate.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(maxDate.getTime()));
            maxDate.set(Calendar.HOUR_OF_DAY, 23);
            maxDate.set(Calendar.MINUTE, 59);
            maxDate.set(Calendar.SECOND, 59);
            if (time >= minDate.getTime().getTime() && time <= maxDate.getTime().getTime()) {
                return i;
            }
        }
        return -1;
    }

    public Date getDateValue(int position) {
        if (position >= 0 && position < dates.size()) {
            return dates.get(position);
        }
        return new Date(0);
    }

    @Override
    public void onCalendarDayClick(Date date) {
        if (null == date) {
            return;
        }
        if (null == lastClickDate || singleMode) {
            lastClickDate = date;
            select(date, date).refresh();
            if (null != onDateSelectedListener) {
                onDateSelectedListener.onSingleSelected(date);
            }
            return;
        }
        if (lastClickDate.getTime() >= date.getTime()) {
            lastClickDate = date;
            select(date, date).refresh();
            if (null != onDateSelectedListener) {
                onDateSelectedListener.onSingleSelected(date);
            }
        } else {
            select(lastClickDate, date).refresh();
            if (null != onDateSelectedListener) {
                onDateSelectedListener.onRangeSelected(lastClickDate, date);
            }
            lastClickDate = null;
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView titleView;
        MonthView monthView;

        VH(View itemView, TextView titleView, MonthView monthView) {
            super(itemView);
            this.titleView = titleView;
            this.monthView = monthView;
        }

    }

}