package com.github.hueyra.picker.range.core;

public class NumInterval extends Interval<Integer> {

    public NumInterval() {
        left(-1);
        lBound(-1);
        right(-1);
        rBound(-1);
    }

    public boolean contain(int index) {
        return index >= left() && index <= right();
    }
}