package com.android.simple.view

import android.os.Bundle
import com.android.simple.databinding.ActivityPickerBinding
import com.github.hueyra.base.util.onClick
import com.github.hueyra.base.view.BaseBindingActivity
import com.github.hueyra.picker.date.DatePicker
import com.github.hueyra.picker.range.DateRangePicker
import com.github.hueyra.picker.text.TextPicker

/**
 * Created by zhujun
 * Date : 2022-03-29
 * Desc : _
 */
class PickerActivity : BaseBindingActivity<ActivityPickerBinding>() {

    private val mTextPicker by lazy { TextPicker(this) }
    private val mDatePicker by lazy { DatePicker(this) }
    private val mDateRangePicker by lazy { DateRangePicker(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTextPicker()
        initDatePicker()
        initDateRangePicker()
    }

    override fun initBinding() {
        mBinding.apply {
            setupSimpleTitleView(stvTitle)

            btnText.onClick {
                mTextPicker.show()
            }

            btnDate.onClick {
                mDatePicker.show()
            }

            btnDateRange.onClick {
                mDateRangePicker.show()
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
        mTextPicker.setTitle("请选择")
        val list = mutableListOf<String>()
        for (i in 0..10) {
            list.add("选项$i")
        }
        mTextPicker.setDataSource(list)
        mTextPicker.setOnItemPickedListener {
            showLog("TextPicker ; select -> $it ; data -> ${list[it]}")
        }
    }

    private fun initDatePicker() {
        mDatePicker.setTitle("请选择")
        mDatePicker.setOnDatePickedListener {
            showLog("DatePicker ; select -> $it")
        }
    }

    private fun initDateRangePicker() {
        mDateRangePicker.setTitle("请选择")
        mDateRangePicker.setOnDateRangePickedListener { start, end ->
            showLog("DatePicker ; select -> $start ~ $end")
        }
    }

    private fun showLog(log: String) {
        mBinding.tvLog.text = log
    }

}