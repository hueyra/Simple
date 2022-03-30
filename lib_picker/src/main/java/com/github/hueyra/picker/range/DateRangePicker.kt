package com.github.hueyra.picker.range

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.hueyra.picker.R
import com.github.hueyra.picker.base.IPickerView
import com.github.hueyra.picker.range.core.CalendarAdapter
import com.github.hueyra.picker.range.core.OnDateSelectedListener
import com.github.hueyra.picker.range.core.RangeCalendarView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * Created by zhujun
 * Date : 2022-03-30
 * Desc : _
 */
class DateRangePicker constructor(private val activity: AppCompatActivity) :
    BottomSheetDialogFragment(),
    IPickerView, OnDateSelectedListener {

    private var mTitleText: String? = null
    private var mTitleView: TextView? = null
    private var mRangeCalendarView: RangeCalendarView? = null
    private lateinit var mCalendarAdapter: CalendarAdapter

    @SuppressLint("SimpleDateFormat")
    private val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    private var mSelectStartDate: Date? = null
    private var mSelectEndDate: Date? = null

    private var mOnDateRangePickedListener: ((String, String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheet)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(true);//设置点击外部可消失
        setWhiteNavigationBar(dialog)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_date_range_picker, container, false)
        mTitleView = view.findViewById(R.id.tv_title)
        mRangeCalendarView = view.findViewById(R.id.cv_calendar)
        if (mTitleText != null) {
            mTitleView!!.text = mTitleText
        }
        initData()
        return view
    }

    private fun initData() {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val minDate = mSimpleDateFormat.parse("2021-01-01")
        val maxDate = mSimpleDateFormat.parse("${year + 1}-12-31")
        //起 2021-01-01
        mCalendarAdapter = mRangeCalendarView!!.adapter
        mCalendarAdapter.setOnCalendarSelectedListener(this)
        mCalendarAdapter.notify(false)
        mCalendarAdapter.single(false)
        mCalendarAdapter.valid(minDate, maxDate)
        if (mSelectStartDate != null && mSelectEndDate != null) {
            mCalendarAdapter.select(mSelectStartDate, mSelectEndDate)
        }
        mCalendarAdapter.range(minDate, maxDate)
        mCalendarAdapter.refresh()
        if (mSelectStartDate == null) {
            mSelectStartDate = Calendar.getInstance().time
        }
        scrollToSelectedPosition()
    }

    private fun scrollToSelectedPosition() {
        mRangeCalendarView!!.post(Runnable {
            var position: Int = mCalendarAdapter.getDatePosition(mSelectStartDate)
            position = max(position, 0)
            position = min(position, mCalendarAdapter.itemCount - 1)
            mRangeCalendarView!!.layoutManager.scrollToPositionWithOffset(position, 0)
        })
    }

    override fun show() {
        show(activity.supportFragmentManager, "")
    }

    override fun hide() {
        dismiss()
    }

    override fun setTitle(title: String) {
        if (mTitleView != null) {
            mTitleView!!.text = title
        } else {
            mTitleText = title
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setWhiteNavigationBar(@NonNull dialog: Dialog) {
        val window = dialog.window
        if (window != null) {
            val metrics = DisplayMetrics()
            window.windowManager.defaultDisplay.getMetrics(metrics)

            val dimDrawable = GradientDrawable()

            val navigationBarDrawable = GradientDrawable()
            navigationBarDrawable.shape = GradientDrawable.RECTANGLE
            navigationBarDrawable.setColor(Color.WHITE)

            val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)

            val windowBackground = LayerDrawable(layers)
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }

    override fun onSingleSelected(date: Date) {

    }

    override fun onRangeSelected(start: Date, end: Date) {
        mOnDateRangePickedListener?.invoke(
            mSimpleDateFormat.format(start),
            mSimpleDateFormat.format(end)
        )
        mRangeCalendarView?.postDelayed({
            hide()
        }, 200)
    }


    fun setOnDateRangePickedListener(l: (String, String) -> Unit) {
        mOnDateRangePickedListener = l
    }
}