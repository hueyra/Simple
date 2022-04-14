package com.github.hueyra.picker.cascade.core

/**
 * Created by zhujun.
 * Date : 2021/09/13
 * Desc : __
 */
interface ICascadePickerData {

    var isCascadePicked: Boolean
        get() = false
        set(value) {}

    fun getPickerValue(): String

    fun getPickerParentID(): String

    fun getPickerID(): String

}