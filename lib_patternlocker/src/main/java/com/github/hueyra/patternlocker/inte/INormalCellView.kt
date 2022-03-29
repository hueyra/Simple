package com.github.hueyra.patternlocker.inte

import android.graphics.Canvas
import com.github.hueyra.patternlocker.bean.CellBean

interface INormalCellView {
    /**
     * 绘制正常情况下（即未设置的）每个图案的样式
     *
     * @param canvas
     * @param cellBean the target cell view
     */
    fun draw(canvas: Canvas, cellBean: CellBean)
}