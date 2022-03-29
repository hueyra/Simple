package com.github.hueyra.webview

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by zhujun
 * Date : 2022-03-29
 * Desc : _
 */
open class BaseWebActivity : AppCompatActivity() {

    //默认的StatusBar高度，默认给22个DP
    private var mStatusBarHeight = dp2px(22f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT

    }

    open fun resetStatusBarHeight(statusBarView: View?) {
        if (null != statusBarView) {
            val height: Int = getStatusBarHeight()
            //设置一下view的layoutParams
            val params = statusBarView.layoutParams
            params.height = if (height > 0) height else mStatusBarHeight.toInt()
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            statusBarView.layoutParams = params
        }
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = this.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    open fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics
        )
    }

    open fun setStatusBarMode(isDark: Boolean) {
        if (isDark) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}