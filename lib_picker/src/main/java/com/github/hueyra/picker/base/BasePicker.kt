package com.github.hueyra.picker.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.hueyra.picker.R
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder

/**
 * Created by zhujun
 * Date : 2022-03-29
 * Desc : _
 */
abstract class BasePicker constructor(private val context: Context) : IPickerView {

    private var mDialogProxy: DialogPlus? = null

    init {
        val view: View = LayoutInflater.from(context).inflate(getPickerLayoutResId(), null)
        mDialogProxy = DialogPlus.newDialog(context)
            .setContentHolder(ViewHolder(view))
            .setContentBackgroundResource(R.color.color_dialog_plus_bg)
            .create()
        initView(view)
    }

    abstract fun getPickerLayoutResId(): Int

    abstract fun initView(view: View)

    override fun show() {
        mDialogProxy?.show()
    }

    override fun hide() {
        mDialogProxy?.dismiss()
    }

}