package com.github.hueyra.patternlocker.bean

import androidx.annotation.ColorInt

data class DefaultStyleDecorator(
    @ColorInt var normalColor: Int,
    @ColorInt var hitColor: Int,
    @ColorInt var hitBorderColor: Int,
    @ColorInt var errorColor: Int,
    @ColorInt var errorBorderColor: Int,
    var lineWidth: Float
)