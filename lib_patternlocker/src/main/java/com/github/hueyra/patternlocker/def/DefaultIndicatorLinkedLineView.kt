package com.github.hueyra.patternlocker.def

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.annotation.ColorInt
import com.github.hueyra.patternlocker.bean.CellBean
import com.github.hueyra.patternlocker.bean.DefaultStyleDecorator
import com.github.hueyra.patternlocker.inte.IIndicatorLinkedLineView

open class DefaultIndicatorLinkedLineView(private val styleDecorator: DefaultStyleDecorator) :
    IIndicatorLinkedLineView {

    private val paint: Paint by lazy {
        DefaultConfig.createPaint()
    }

    init {
        this.paint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas, hitIndexList: List<Int>, cellBeanList: List<CellBean>, isError: Boolean) {
        if (hitIndexList.isEmpty() || cellBeanList.isEmpty()) {
            return
        }

        val saveCount = canvas.save()
        val path = Path()
        var first = true

        hitIndexList.forEach {
            if (0 <= it && it < cellBeanList.size) {
                val c = cellBeanList[it]
                if (first) {
                    path.moveTo(c.centerX, c.centerY)
                    first = false
                } else {
                    path.lineTo(c.centerX, c.centerY)
                }
            }
        }

        this.paint.color = this.getColor(isError)
        this.paint.strokeWidth = this.styleDecorator.lineWidth
        canvas.drawPath(path, this.paint)
        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean): Int {
        return if (isError) this.styleDecorator.errorColor else this.styleDecorator.hitColor
    }
}
