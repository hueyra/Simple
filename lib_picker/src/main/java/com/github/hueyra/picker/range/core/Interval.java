package com.github.hueyra.picker.range.core;

public class Interval<T> {
    private int lBound;
    private T left;
    private int rBound;
    private T right;

    public int lBound() {
        return lBound;
    }

    public void lBound(int lBound) {
        this.lBound = lBound;
    }

    public T left() {
        return left;
    }

    public Interval<T> left(T left) {
        this.left = left;
        return this;
    }

    public int rBound() {
        return rBound;
    }

    public void rBound(int rBound) {
        this.rBound = rBound;
    }

    public T right() {
        return right;
    }

    public Interval<T> right(T right) {
        this.right = right;
        return this;
    }

    public boolean bothNoNull() {
        return null != left() && null != right();
    }
}
