package  com.github.hueyra.patternlocker.def

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.util.TypedValue
import com.github.hueyra.patternlocker.bean.CellBean
import com.github.hueyra.patternlocker.bean.DefaultStyleDecorator
import com.github.hueyra.patternlocker.inte.INormalCellView

open class DefaultLockerNormalCellView(private val styleDecorator: DefaultStyleDecorator) :
    INormalCellView {
    private val paint: Paint by lazy {
        DefaultConfig.createPaint()
    }

    init {
        this.paint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas, cellBean: CellBean) {
        val saveCount = canvas.save()

        // draw outer circle
        this.paint.color = this.styleDecorator.normalColor
        //canvas.drawCircle(cellBean.centerX, cellBean.centerY, cellBean.radius, this.paint)
        canvas.drawCircle(cellBean.centerX, cellBean.centerY, dp2px(10f), this.paint)

        canvas.restoreToCount(saveCount)
    }

    private fun dp2px(dpNum: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpNum,
            Resources.getSystem().displayMetrics
        )
    }
}
