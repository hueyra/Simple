package com.android.simple.view

import android.os.Bundle
import com.android.simple.databinding.ActivityPickerBinding
import com.github.hueyra.base.util.onClick
import com.github.hueyra.base.view.BaseBindingActivity
import com.github.hueyra.picker.text.TextPicker

/**
 * Created by zhujun
 * Date : 2022-03-29
 * Desc : _
 */
class PickerActivity : BaseBindingActivity<ActivityPickerBinding>() {

    private val mTextPicker by lazy { TextPicker(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTextPicker()
    }

    override fun initBinding() {
        mBinding.apply {
            setupSimpleTitleView(stvTitle)

            btnText.onClick {
                mTextPicker.show()
            }

            btnDate.onClick {

            }

            btnDateRange.onClick {

            }

            btnCity.onClick {

            }

            btnCascade.onClick {

            }

            btnMulti.onClick {

            }

        }
    }

    private fun initTextPicker() {
        mTextPicker.setTitle("简单picker")
        val list = mutableListOf<String>()
        for (i in 0..10) {
            list.add("选项$i")
        }

        mTextPicker.setDataSource(list)
        mTextPicker.setOnItemPickedListener {
            showLog("TextPicker ; select -> $it ; data -> ${list[it]}")
        }
    }

    private fun showLog(log: String) {
        mBinding.tvLog.text = log
    }

}