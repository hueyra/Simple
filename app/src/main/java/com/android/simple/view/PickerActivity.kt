package com.android.simple.view

import android.os.Bundle
import com.android.simple.databinding.ActivityPickerBinding
import com.android.simple.entity.TestCascadeEntity
import com.github.hueyra.base.util.onClick
import com.github.hueyra.base.view.BaseBindingActivity
import com.github.hueyra.picker.cascade.CascadePicker
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
    private val mCascadePicker by lazy { CascadePicker(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTextPicker()
        initDatePicker()
        initCascadePicker()
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
                mCascadePicker.show()
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

    private fun initCascadePicker() {

        mCascadePicker.setTitle("请选择")

        val list = mutableListOf<List<TestCascadeEntity>>()

        val entity1 = TestCascadeEntity("1", "0", "张一")
        val entity2 = TestCascadeEntity("2", "0", "李二")
        val entity3 = TestCascadeEntity("3", "0", "王三")
        val entity4 = TestCascadeEntity("4", "0", "陈四")
        val entity5 = TestCascadeEntity("5", "0", "祝五")

        val list1 = mutableListOf<TestCascadeEntity>()
        list1.add(entity1)
        list1.add(entity2)
        list1.add(entity3)
        list1.add(entity4)
        list1.add(entity5)
        list.add(list1)
        //
        val entity11 = TestCascadeEntity("11", "1", "张一一")
        val entity12 = TestCascadeEntity("12", "1", "张一二")
        val entity13 = TestCascadeEntity("13", "1", "张一三")
        val entity14 = TestCascadeEntity("14", "1", "张一四")
        val list2 = mutableListOf<TestCascadeEntity>()
        list2.add(entity11)
        list2.add(entity12)
        list2.add(entity13)
        list2.add(entity14)
        list.add(list2)

//
        val entity21 = TestCascadeEntity("21", "2", "李二1")
        val entity22 = TestCascadeEntity("22", "2", "李二2")
        val entity23 = TestCascadeEntity("23", "2", "李二3")
        val entity24 = TestCascadeEntity("24", "2", "李二4")
        val list3 = mutableListOf<TestCascadeEntity>()
        list3.add(entity21)
        list3.add(entity22)
        list3.add(entity23)
        //list3.add(entity24)
        list.add(list3)

        mCascadePicker.setPickerDataSource(list, "0")
        mCascadePicker.setOnItemPickedListener {
            showLog("CascadePicker ; select -> $it")
        }
    }

    private fun showLog(log: String) {
        mBinding.tvLog.text = log
    }

}