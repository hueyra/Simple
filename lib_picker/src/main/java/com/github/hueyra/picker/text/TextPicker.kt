package com.github.hueyra.picker.text

import android.content.Context
import android.view.View
import android.widget.TextView
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.contrarywind.view.WheelView
import com.github.hueyra.picker.R
import com.github.hueyra.picker.base.BasePicker

/**
 * Created by zhujun
 * Date : 2022-03-29
 * Desc : _
 */
class TextPicker constructor(context: Context) : BasePicker(context) {

    private lateinit var mWheelView: WheelView
    private lateinit var mTitleText: TextView
    private var mCurrentDataSourceSize = 0
    private var mOnItemPickedListener: ((Int) -> Unit)? = null

    override fun getPickerLayoutResId(): Int = R.layout.dialog_text_picker

    override fun initView(view: View) {
        mWheelView = view.findViewById(R.id.tpd_wv_content)
        mTitleText = view.findViewById(R.id.tpd_tv_title)

        view.findViewById<View>(R.id.tpd_tv_cancel).setOnClickListener { hide() }
        view.findViewById<View>(R.id.tpd_tv_done).setOnClickListener {
            hide()
            mOnItemPickedListener?.invoke(mWheelView.currentItem)
        }
        //
        //
        mWheelView.setCyclic(false)
        mWheelView.setItemsVisibleCount(5)
        mWheelView.setDividerColor(-0x222223)
        mWheelView.setTextColorOut(-0x595755)
        mWheelView.setTextColorCenter(-0x1000000)
        mWheelView.setLineSpacingMultiplier(3f)
        mWheelView.setTextSize(16f)
    }

    override fun setTitle(title: String) {
        mTitleText.text = title
    }

    fun setDataSource(list: List<String>) {
        mCurrentDataSourceSize = list.size
        mWheelView.adapter = ArrayWheelAdapter(list)
        mWheelView.currentItem = 0
    }

    fun isDataSourceEmpty(): Boolean {
        return mCurrentDataSourceSize == 0
    }

    fun removeAll() {
        mCurrentDataSourceSize = 0
        mWheelView.adapter = ArrayWheelAdapter(ArrayList<Any>())
    }

    fun setOnItemPickedListener(l: (Int) -> Unit) {
        mOnItemPickedListener = l
    }

}