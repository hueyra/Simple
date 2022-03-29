package com.github.hueyra.webview

import android.content.Context

/**
 * Created by zhujun
 * Date : 2022-03-29
 * Desc : _
 */
object X5SdkInitializer {

    fun init(context: Context) {
        //初始化SDK环境，在App启动后尽可能早地调用初始化接口，进行内核预加载：
//        QbSdk.initX5Environment(context, object : QbSdk.PreInitCallback {
//            override fun onCoreInitFinished() {
//            }
//
//            override fun onViewInitFinished(p0: Boolean) {
//            }
//
//        })
//        //TBS内核首次使用和加载时，ART虚拟机会将Dex文件转为Oat，该过程由系统底层触发且耗时较长，很容易引起anr问题，解决方法是使用TBS的 ”dex2oat优化方案“。
//        val map = HashMap<String, Any>()
//        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
//        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
//        QbSdk.initTbsSettings(map)
    }

}