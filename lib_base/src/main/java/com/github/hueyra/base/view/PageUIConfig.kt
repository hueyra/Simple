package com.github.hueyra.base.view

/**
 * Created by zhujun.
 * Date: 7/5/21
 * Info: 页面activity的基础配置项
 */
class PageUIConfig {

    companion object {
        const val DEF_BG_COLOR: Int = 0xFFF3F4F9.toInt()
    }

    /**
     * 当前页面的layoutResId
     * */
    var layoutResId: Int

    /**
     * 当前页面的状态栏模式，Light or Dark
     * */
    var textMode: StatusBarMode?

    /**
     * 当前页面的状态栏viewId
     * */
    var statusBarViewId: Int

    /**
     * 当前页面的状态栏背景颜色
     * */
    var statusBarColor: Int

    /**
     * 当前页面的背景颜色
     * */
    var windowBgColor: Int

    constructor() {
        this.layoutResId = 0
        this.textMode = StatusBarMode.DARK
        this.statusBarViewId = 0
        this.statusBarColor = 0
        this.windowBgColor = DEF_BG_COLOR
    }

    constructor(textMode: StatusBarMode, windowBgColor: Int) {
        this.layoutResId = 0
        this.textMode = textMode
        this.statusBarViewId = 0
        this.statusBarColor = 0
        this.windowBgColor = windowBgColor
    }

    constructor(layoutResId: Int) {
        this.layoutResId = layoutResId
        this.textMode = StatusBarMode.DARK
        this.statusBarViewId = 0
        this.statusBarColor = 0
        this.windowBgColor = DEF_BG_COLOR
    }

    constructor(layoutResId: Int, textMode: StatusBarMode?) {
        this.layoutResId = layoutResId
        this.textMode = textMode
        this.statusBarViewId = 0
        this.statusBarColor = 0
        this.windowBgColor = DEF_BG_COLOR
    }

    constructor(layoutResId: Int, windowBgColor: Int, textMode: StatusBarMode?) {
        this.layoutResId = layoutResId
        this.textMode = textMode
        this.windowBgColor = windowBgColor
        this.statusBarViewId = 0
        this.statusBarColor = 0
    }

    constructor(layoutResId: Int, textMode: StatusBarMode?, statusBarViewId: Int) {
        this.layoutResId = layoutResId
        this.textMode = textMode
        this.statusBarViewId = statusBarViewId
        this.statusBarColor = 0
        this.windowBgColor = DEF_BG_COLOR
    }

    constructor(
        layoutResId: Int,
        textMode: StatusBarMode?,
        statusBarViewId: Int,
        statusBarColor: Int
    ) {
        this.layoutResId = layoutResId
        this.textMode = textMode
        this.statusBarViewId = statusBarViewId
        this.statusBarColor = statusBarColor
        this.windowBgColor = DEF_BG_COLOR
    }


    /**
     * 当前状态栏的模式，Light or Dark
     * */
    enum class StatusBarMode {
        /**
         * Light模式，状态栏是白色的字
         * */
        LIGHT,

        /**
         * Dark模式，状态栏是黑色的字
         * */
        DARK
    }
}