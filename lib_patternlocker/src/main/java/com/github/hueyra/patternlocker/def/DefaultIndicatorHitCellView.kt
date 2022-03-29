package com.github.hueyra.patternlocker.def

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import com.github.hueyra.patternlocker.bean.CellBean
import com.github.hueyra.patternlocker.bean.DefaultStyleDecorator
import com.github.hueyra.patternlocker.inte.IHitCellView

open class DefaultIndicatorHitCellView(private val styleDecorator: DefaultStyleDecorator) :
    IHitCellView {

    private val paint: Paint by lazy {
        DefaultConfig.createPaint()
    }

    init {
        this.paint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas, cellBean: CellBean, isError: Boolean) {
        val saveCount = canvas.save()

        this.paint.color = this.getColor(isError)
        canvas.drawCircle(cellBean.centerX, cellBean.centerY, cellBean.radius, this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean): Int {
        return if (isError) this.styleDecorator.errorColor else this.styleDecorator.hitColor
    }
}