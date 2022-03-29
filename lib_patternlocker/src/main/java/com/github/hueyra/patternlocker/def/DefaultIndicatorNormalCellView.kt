package com.github.hueyra.patternlocker.def

import android.graphics.Canvas
import android.graphics.Paint
import com.github.hueyra.patternlocker.bean.CellBean
import com.github.hueyra.patternlocker.bean.DefaultStyleDecorator
import com.github.hueyra.patternlocker.inte.INormalCellView

open class DefaultIndicatorNormalCellView(private val styleDecorator: DefaultStyleDecorator) :
    INormalCellView {

    private val paint: Paint by lazy {
        DefaultConfig.createPaint()
    }

    init {
        this.paint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas, cellBean: CellBean) {
        val saveCount = canvas.save()

        //outer circle
        this.paint.color = this.styleDecorator.normalColor
        canvas.drawCircle(cellBean.centerX, cellBean.centerY, cellBean.radius, this.paint)

        canvas.restoreToCount(saveCount)
    }
}