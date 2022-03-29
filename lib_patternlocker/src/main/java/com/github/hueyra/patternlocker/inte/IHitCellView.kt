package  com.github.hueyra.patternlocker.inte

import android.graphics.Canvas
import com.github.hueyra.patternlocker.bean.CellBean

interface IHitCellView {
    /**
     * 绘制已设置的每个图案的样式
     *
     * @param canvas
     * @param cellBean
     * @param isError
     */
    fun draw(canvas: Canvas, cellBean: CellBean, isError: Boolean)
}
