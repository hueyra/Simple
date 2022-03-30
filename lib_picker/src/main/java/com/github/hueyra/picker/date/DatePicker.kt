package com.github.hueyra.picker.date

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.bigkoo.pickerview.configure.PickerOptions
import com.github.hueyra.picker.R
import com.github.hueyra.picker.base.BasePicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DatePicker constructor(context: Context) : BasePicker(context) {

    private lateinit var mDsdPvDatePicker: DatePickerView
    private lateinit var mTitle: TextView
    private var mDateFormat: String? = null
    private var mOnDatePickedListener: ((String) -> Unit)? = null

    override fun getPickerLayoutResId(): Int = R.layout.dialog_date_picker

    override fun setTitle(title: String) {
        mTitle.text = title
    }

    override fun initView(view: View) {
        mDsdPvDatePicker = view.findViewById(R.id.dsd_pv_date_picker)
        mTitle = view.findViewById(R.id.dsd_tv_title)
        view.findViewById<View>(R.id.dsd_tv_done).setOnClickListener { v: View? ->
            hide()
            if (mOnDatePickedListener != null) {
                if (!TextUtils.isEmpty(mDateFormat) && mDateFormat == "year") {
                    //这里做一些特殊处理，如果只要展示年份的
                    mOnDatePickedListener!!.invoke(
                        mDsdPvDatePicker.selectDate.substring(0, 4)
                    )
                } else {
                    mOnDatePickedListener!!.invoke(mDsdPvDatePicker.selectDate)
                }
            }
        }
        view.findViewById<View>(R.id.dsd_tv_cancel).setOnClickListener { v: View? -> hide() }
        val options = PickerOptions(PickerOptions.TYPE_PICKER_TIME)
        options.textColorOut = Color.parseColor("#A6A8AB")
        options.textColorCenter = Color.parseColor("#000000")
        options.dividerColor = Color.parseColor("#DDDDDD")
        options.itemsVisibleCount = 5
        options.lineSpacingMultiplier = 3f
        options.cyclic = false
        options.textSizeContent = 16
        options.startYear = 1950
        if (!TextUtils.isEmpty(mDateFormat) && mDateFormat == "today") {
            options.endYear = Calendar.getInstance()[Calendar.YEAR]
        } else {
            options.endYear = Calendar.getInstance()[Calendar.YEAR] + 50
        }
        //
        if (!TextUtils.isEmpty(mDateFormat)) {
            //这里做一些特殊处理，如果只要展示年份的
            if (mDateFormat == "year") {
                options.type = booleanArrayOf(true, false, false, false, false, false)
            }
        }
        mDsdPvDatePicker.setPickerOptions(options)
    }

    fun setOrigSelect(date: String?) {
        if (!TextUtils.isEmpty(date)) {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            try {
                val calendar = Calendar.getInstance()
                calendar.time = Objects.requireNonNull(sdf.parse(date))
                mDsdPvDatePicker.setDate(calendar)
            } catch (e: ParseException) {
                //
            }
        }
    }

    fun setOnDatePickedListener(l: (String) -> Unit) {
        mOnDatePickedListener = l
    }

}