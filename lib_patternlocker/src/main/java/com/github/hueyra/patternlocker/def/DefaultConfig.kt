package com.github.hueyra.patternlocker.def

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import androidx.annotation.ColorInt

internal object DefaultConfig {
    private const val DEFAULT_NORMAL_COLOR = "#BCC3D5"
    private const val DEFAULT_HIT_COLOR = "#3DB77E"
    private const val DEFAULT_HIT_BORDER_COLOR = "#333DB77E"
    private const val DEFAULT_ERROR_COLOR = "#FA7483"
    private const val DEFAULT_ERROR_BORDER_COLOR = "#26FA7483"
    private const val DEFAULT_FILL_COLOR = "#FFFFFF"
    private const val DEFAULT_LINE_WIDTH = 3

    const val defaultFreezeDuration = 1000//ms
    const val defaultEnableAutoClean = false
    const val defaultEnableHapticFeedback = false
    const val defaultEnableSkip = false
    const val defaultEnableLogger = false

    @ColorInt
    val defaultNormalColor: Int = Color.parseColor(DEFAULT_NORMAL_COLOR)

    @ColorInt
    val defaultHitColor: Int = Color.parseColor(DEFAULT_HIT_COLOR)

    @ColorInt
    val defaultErrorColor: Int = Color.parseColor(DEFAULT_ERROR_COLOR)

    @ColorInt
    val defaultHitBorderColor: Int = Color.parseColor(DEFAULT_HIT_BORDER_COLOR)

    @ColorInt
    val defaultErrorBorderColor: Int = Color.parseColor(DEFAULT_ERROR_BORDER_COLOR)

    @ColorInt
    val defaultFillColor: Int = Color.parseColor(DEFAULT_FILL_COLOR)

    fun getDefaultLineWidth(resources: Resources): Float {
        return convertDpToPx(DEFAULT_LINE_WIDTH.toFloat(), resources)
    }

    fun createPaint(): Paint {
        val paint = Paint()
        paint.isDither = true
        paint.isAntiAlias = true
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        return paint
    }

    private fun convertDpToPx(dpValue: Float, resources: Resources): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.displayMetrics)
    }
}