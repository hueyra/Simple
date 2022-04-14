package com.github.hueyra.picker.cascade.core

/**
 * Created by zhujun.
 * Date : 2021/09/13
 * Desc : __
 */
interface ICascadePickerData {

    var isCascadePicked: Boolean

    fun getPickerID(): String

    fun getPickerParentID(): String

    fun getPickerValue(): String

}