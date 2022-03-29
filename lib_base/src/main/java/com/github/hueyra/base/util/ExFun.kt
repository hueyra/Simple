package com.github.hueyra.base.util

import android.content.res.Resources
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.viewbinding.BuildConfig
import com.github.hueyra.base.data.IAPIResponse

/**
 * String的扩展函数，用来日志打印
 *
 * 仅当BuildConfig.DEBUG为真的时候才会打印，并且打印的是debug级别的
 *
 * */
fun String.printLog() {
    if (BuildConfig.DEBUG) {
        Log.d("ly_app", this)
    }
}

/**
 * String的扩展函数，用来日志打印
 *
 * 仅当BuildConfig.DEBUG为真的时候才会打印，并且打印的是error级别的
 *
 * */
fun String.printErr() {
    if (BuildConfig.DEBUG) {
        Log.e("ly_app", this)
    }
}

/**
 * String的扩展函数，任意类toString
 *
 * */
fun Any?.toString(): String {
    if (this == null) return "null"
    // 空检测之后，“this”会自动转换为非空类型，所以下面的 toString()
    // 解析为 Any 类的成员函数
    return toString()
}

/**
 * Int的扩展函数，可以将一个整数dp转化成px
 *
 * */
fun Int.dp(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    )
}

/***
 * 任意一个View，添加一个点击事件，而且是防止快速点击的，延迟是600毫秒
 *
 */
fun View.onClick(listener: (view: View) -> Unit) {
    val minTime = 600L
    var lastTime = 0L
    this.setOnClickListener {
        val tmpTime = System.currentTimeMillis()
        if (tmpTime - lastTime > minTime) {
            lastTime = tmpTime
            listener.invoke(this)
        }
    }
}

/***
 * 任意一个View，双击事件，最小时长是600毫秒
 *
 */
fun View.onDoubleClick(listener: (view: View) -> Unit) {
    val minTime = 400L  //双击最小间隔事件
    val minSplitTime = 1000L  //间隔超过1秒，判定这次失效
    var lastTime = 0L
    var lastSplitTime = 0L
    this.setOnClickListener {
        val tmpTime = System.currentTimeMillis()
        if (lastTime == 0L) {
            lastTime = tmpTime
        }
        val splitTime = tmpTime - lastTime
        //"tmpTime - lastTime -> $splitTime ; lastTime -> $lastTime ;  minTime -> $minTime".printErr()
        if (splitTime in 1..minTime || (splitTime == 0L && lastSplitTime > minSplitTime)) {
            lastTime = 0L
            listener.invoke(this)
        }
        if (splitTime > minSplitTime) {
            lastTime = tmpTime
        }
        lastSplitTime = splitTime
    }
}

/***
 * 将一个Throwable快速转化成错误的ApiResponse
 *
 * 用于ViewModel中错误抓取，并且反馈给前端页面处理 ; 主要是HTTP_404, HTTP_500
 *
 */
fun Throwable.convertToApiResponse(): IAPIResponse {
    // failed to connect to 404
    // End of input at ?
    var errcode = "COMM_ERR"
    //"Throwable.msg -> ${this.message}".printLog()
    if (!TextUtils.isEmpty(this.message)) {
        when {
            this.message!!.startsWith("failed to connect to") -> {
                errcode = "COMM_ERR_404"
            }
            this.message!!.startsWith("End of input at") -> {
                errcode = "COMM_ERR_000"
            }
            this.message!!.contains("HTTP 500") -> {
                errcode = "COMM_ERR_500"
            }
            this.message!!.contains("HTTP 404") -> {
                errcode = "COMM_ERR_404"
            }
        }
    }
    val apiResponse = IAPIResponse()
    apiResponse.isSuccess = false
    apiResponse.code = errcode
    apiResponse.message = "请求错误"
    apiResponse.errmsg4log = this.message
    return apiResponse
}