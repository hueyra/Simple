package com.github.hueyra.base.widget

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.github.hueyra.base.R
import com.github.hueyra.base.listener.SimpleTextWatcher
import com.github.hueyra.base.util.Utils

/**
 * Created by zhujun
 * Date : 2021-11-26
 * Desc : _
 */
class SimpleSearchTitleView : LinearLayout {

    private var mVStatusBar: View? = null
    private var mIvBack: ImageView? = null
    private var mEtInput: EditText? = null
    private var mIvClear: ImageView? = null
    private var mTvConfirm: TextView? = null
    private var m22pxHeight = 0

    private var mBackClickListener: (() -> Unit)? = null
    private var mSearchClickListener: ((String) -> Unit)? = null

    private var mInputConfirm = false //防止重复回调的参数

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    init {
        m22pxHeight = Utils.dp2px(22f)
        LayoutInflater.from(context).inflate(R.layout.layout_simple_search_title, this)
        mVStatusBar = findViewById(R.id.v_status_bar)
        mIvBack = findViewById(R.id.iv_back)
        mEtInput = findViewById(R.id.et_input)
        mIvClear = findViewById(R.id.iv_clear)
        mTvConfirm = findViewById(R.id.tv_confirm)

        mEtInput?.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s.toString())) {
                    mIvClear?.visibility = VISIBLE
                } else {
                    mIvClear?.visibility = GONE
                }
            }
        })

        mIvClear?.setOnClickListener { mEtInput?.text?.clear() }

        mIvBack?.setOnClickListener {
            mBackClickListener?.invoke()
        }

        mTvConfirm?.setOnClickListener {
            mSearchClickListener?.invoke(mEtInput?.text.toString())
        }

        mEtInput?.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || (event != null && KeyEvent.KEYCODE_ENTER == event.keyCode)
            ) {
                if (!mInputConfirm) {
                    //防止重复回调
                    mInputConfirm = true
                    mSearchClickListener?.invoke(mEtInput?.text.toString())
                    mEtInput?.postDelayed({
                        mInputConfirm = false
                    }, 500)
                }
                true
            } else {
                false
            }
        }

    }

    fun setOnBackClickListener(l: (() -> Unit)) {
        mBackClickListener = l
    }

    fun setOnSearchClickListener(l: ((String) -> Unit)) {
        mSearchClickListener = l
    }

    fun requestEditFocus() {
        mEtInput?.requestFocus()
    }

    fun resetStatusBarHeight(height: Int) {
        if (mVStatusBar?.visibility == VISIBLE) {
            val params = mVStatusBar?.layoutParams as ViewGroup.LayoutParams
            params.height = if (height > 0) height else m22pxHeight
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            mVStatusBar?.layoutParams = params
        }
    }

}