package com.github.hueyra.patternlocker.def

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.util.TypedValue
import androidx.annotation.ColorInt
import com.github.hueyra.patternlocker.bean.CellBean
import com.github.hueyra.patternlocker.bean.DefaultStyleDecorator
import com.github.hueyra.patternlocker.inte.IHitCellView

open class DefaultLockerHitCellView(private val styleDecorator: DefaultStyleDecorator) :
    IHitCellView {

    private val paint: Paint by lazy {
        DefaultConfig.createPaint()
    }

    init {
        this.paint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas, cellBean: CellBean, isError: Boolean) {
        val saveCount = canvas.save()

        // draw outer border
        this.paint.color = this.getColor(isError, true)
        canvas.drawCircle(cellBean.centerX, cellBean.centerY, dp2px(25f), this.paint)

        // draw inner circle
        this.paint.color = this.getColor(isError, false)
        canvas.drawCircle(cellBean.centerX, cellBean.centerY, dp2px(10f), this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean, isBorder: Boolean): Int {
        return if (isBorder) {
            if (isError) this.styleDecorator.errorBorderColor else this.styleDecorator.hitBorderColor
        } else {
            if (isError) this.styleDecorator.errorColor else this.styleDecorator.hitColor
        }

    }

    private fun dp2px(dpNum: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpNum,
            Resources.getSystem().displayMetrics
        )
    }
}
