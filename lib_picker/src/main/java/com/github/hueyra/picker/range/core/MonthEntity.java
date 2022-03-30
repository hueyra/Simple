package com.github.hueyra.picker.range.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonthEntity implements Serializable {
    public static final int WEEK_DAYS = 7;
    public static final int MAX_HORIZONTAL_LINES = 6;
    public static final int MAX_DAYS_OF_MONTH = 31;
    public static final String STR_TODAY = "今天";
    private final static List<MonthEntity> pools = new ArrayList<>();
    private Date date;
    private Interval<Date> valid;
    private Interval<Date> select;
    private Interval<String> note;
    private boolean singleMode = false;

    public static MonthEntity obtain(Interval<Date> valid, Interval<Date> select) {
        MonthEntity entity = pools.size() == 0 ? new MonthEntity() : pools.remove(0);
        entity.valid = valid;
        entity.select = select;
        return entity;
    }

    private MonthEntity() {
        super();
    }

    public Date date() {
        return date;
    }

    public MonthEntity date(Date date) {
        this.date = date;
        return this;
    }

    public Interval<Date> valid() {
        return valid;
    }

    public MonthEntity valid(Interval<Date> valid) {
        this.valid = valid;
        return this;
    }

    public Interval<Date> select() {
        return select;
    }

    public MonthEntity select(Interval<Date> select) {
        this.select = select;
        return this;
    }

    public MonthEntity singleMode(boolean single) {
        this.singleMode = single;
        return this;
    }

    public boolean singleMode() {
        return this.singleMode;
    }

    public Interval<String> note() {
        return note;
    }

    public MonthEntity note(Interval<String> note) {
        this.note = note;
        return this;
    }

    public void recycle() {
        if (!pools.contains(this)) {
            date = null;
            valid = null;
            select = null;
            note = null;
            pools.add(this);
        }
    }
}