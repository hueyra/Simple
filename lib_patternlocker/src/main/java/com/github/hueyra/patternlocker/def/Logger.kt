package com.github.hueyra.patternlocker.def

import android.util.Log

internal class Logger {
    companion object {
        var enable = false
        fun d(tag: String, msg: String) {
            if (enable) {
                Log.d(tag, msg)
            }
        }
    }
}