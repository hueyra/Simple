package com.android.simple.entity

import com.github.hueyra.picker.cascade.core.ICascadePickerData

/**
 * Created by zhujun
 * Date : 2022-04-14
 * Desc : _
 */
data class TestCascadeEntity(
    var id: String,
    var pid: String,
    var name: String
) : ICascadePickerData {

    override fun getPickerValue(): String {
        return name
    }

    override fun getPickerParentID(): String {
        return pid
    }

    override var isCascadePicked: Boolean = false

    override fun getPickerID(): String {
        return id
    }
}