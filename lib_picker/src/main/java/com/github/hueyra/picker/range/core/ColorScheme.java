package com.github.hueyra.picker.range.core;

import android.graphics.Color;

import androidx.annotation.ColorInt;

import java.io.Serializable;

public class ColorScheme implements Serializable {
    private int weekTextColor = 0xFF343434;
    private int weekBackgroundColor = Color.TRANSPARENT;
    private int monthTitleTextColor = 0xFF3DB77F;
    private int monthTitleBackgroundColor = Color.TRANSPARENT;
    private int monthBackgroundColor = Color.TRANSPARENT;
    private int monthDividerColor = Color.TRANSPARENT;
    private int dayNormalTextColor = 0xFF333333;
    private int dayInvalidTextColor = 0xFFCCCCCC;
    private int dayStressTextColor = 0xFF999999;
    private int daySelectTextColor = 0xFFFFFFFF;
    private int dayNormalBackgroundColor = Color.TRANSPARENT;
    private int dayInvalidBackgroundColor = Color.TRANSPARENT;
    private int daySelectBackgroundColor = 0xFF3CB780;
    private int daySelectRangeBackgroundColor = 0x333CB780;

    public ColorScheme weekTextColor(@ColorInt int color) {
        this.weekTextColor = color;
        return this;
    }

    @ColorInt
    public int weekTextColor() {
        return weekTextColor;
    }

    public ColorScheme weekBackgroundColor(@ColorInt int color) {
        this.weekBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int weekBackgroundColor() {
        return weekBackgroundColor;
    }

    public ColorScheme monthTitleTextColor(@ColorInt int color) {
        this.monthTitleTextColor = color;
        return this;
    }

    @ColorInt
    public int monthTitleTextColor() {
        return monthTitleTextColor;
    }

    public ColorScheme monthTitleBackgroundColor(@ColorInt int color) {
        this.monthTitleBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int monthTitleBackgroundColor() {
        return monthTitleBackgroundColor;
    }

    public ColorScheme monthBackgroundColor(@ColorInt int color) {
        this.monthBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int monthBackgroundColor() {
        return monthBackgroundColor;
    }

    public ColorScheme monthDividerColor(@ColorInt int color) {
        this.monthDividerColor = color;
        return this;
    }

    @ColorInt
    public int monthDividerColor() {
        return monthDividerColor;
    }

    public ColorScheme dayNormalTextColor(@ColorInt int color) {
        this.dayNormalTextColor = color;
        return this;
    }

    @ColorInt
    public int dayNormalTextColor() {
        return dayNormalTextColor;
    }

    public ColorScheme dayInvalidTextColor(@ColorInt int color) {
        this.dayInvalidTextColor = color;
        return this;
    }

    @ColorInt
    public int dayInvalidTextColor() {
        return dayInvalidTextColor;
    }

    public ColorScheme dayStressTextColor(@ColorInt int color) {
        this.dayStressTextColor = color;
        return this;
    }

    @ColorInt
    public int dayStressTextColor() {
        return dayStressTextColor;
    }

    public ColorScheme daySelectTextColor(@ColorInt int color) {
        this.daySelectTextColor = color;
        return this;
    }

    @ColorInt
    public int daySelectTextColor() {
        return daySelectTextColor;
    }

    public ColorScheme dayNormalBackgroundColor(@ColorInt int color) {
        this.dayNormalBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int dayNormalBackgroundColor() {
        return dayNormalBackgroundColor;
    }

    public ColorScheme dayInvalidBackgroundColor(@ColorInt int color) {
        this.dayInvalidBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int dayInvalidBackgroundColor() {
        return dayInvalidBackgroundColor;
    }

    public ColorScheme daySelectBackgroundColor(@ColorInt int color) {
        this.daySelectBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int daySelectBackgroundColor() {
        return daySelectBackgroundColor;
    }

    public ColorScheme daySelectRangeBackgroundColor(@ColorInt int color) {
        this.daySelectRangeBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int daySelectRangeBackgroundColor() {
        return daySelectRangeBackgroundColor;
    }

}
