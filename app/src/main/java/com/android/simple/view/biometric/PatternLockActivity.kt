package com.android.simple.view.biometric

import android.os.Bundle
import android.view.View
import com.android.simple.R
import com.android.simple.databinding.ActivityPatternLockBinding
import com.github.hueyra.base.util.isEmptyy
import com.github.hueyra.base.util.onClick
import com.github.hueyra.base.view.BaseBindingActivity
import com.github.hueyra.biometricauth.widget.shakeShake
import com.github.hueyra.patternlocker.inte.OnPatternChangeListener
import com.github.hueyra.patternlocker.view.PatternLockerView

/**
 * Created by zhujun.
 * Date : 2021/10/19
 * Desc : 设置手势密码页面
 */
class PatternLockActivity : BaseBindingActivity<ActivityPatternLockBinding>() {

    companion object {
        private const val PATTERN_NOTICE = "请设置您的手势密码"
        private const val PATTERN_AGAIN_NOTICE = "请再次绘制您的手势密码"
        private const val PATTERN_TWICE_NOT_SAME_NOTICE = "和上次设置手势不同，请重新设置"//两次不一致提示
        private const val PATTERN_TOO_SIMPLE_NOTICE = "请至少连接4个点"//太简单了
    }

    private var mPatternAgainErrorNum = 0
    private var mCurrentPatternPassword = ""
    private val mStringBuffer = StringBuffer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSimpleTitleView(mBinding.stvTitle)
    }

    override fun initBinding() {
        mBinding.apply {
            tvReset.onClick {
                tvReset.visibility = View.GONE
                tvReset.text = PATTERN_NOTICE
                mCurrentPatternPassword = ""
                mPatternAgainErrorNum = 0
            }
            //
            ivHead.setImageResource(R.mipmap.ic_launcher)
            tvName.text = "UserName"
            tvHint.text = PATTERN_NOTICE
        }
        mBinding.plvLock.setOnPatternChangedListener(object : OnPatternChangeListener {
            override fun onStart(view: PatternLockerView) {
            }

            override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {
            }

            override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
                if (mCurrentPatternPassword.isEmptyy()) {
                    mPatternAgainErrorNum = 0
                    if (hitIndexList.size < 4) {
                        //如果链接的点小于4个，提示错误
                        notifyError(PATTERN_TOO_SIMPLE_NOTICE, PATTERN_NOTICE)
                    } else {
                        mStringBuffer.setLength(0)
                        for (i in hitIndexList.indices) {
                            mStringBuffer.append(hitIndexList[i])
                        }
                        mCurrentPatternPassword = mStringBuffer.toString()
                        //第一次设置成功
                        postDelay({
                            mBinding.tvHint.text = PATTERN_AGAIN_NOTICE
                        }, 600)
                        postDelay({
                            mBinding.plvLock.clearHitState()
                        }, 800)
                    }
                } else {
                    mStringBuffer.setLength(0)
                    for (i in hitIndexList.indices) {
                        mStringBuffer.append(hitIndexList[i])
                    }
                    val thisPwd = mStringBuffer.toString()
                    if (mCurrentPatternPassword == thisPwd) {
                        //如果两个一样的
                        setPwdSuccess()
                    } else {
                        mPatternAgainErrorNum++
                        notifyError(PATTERN_TWICE_NOT_SAME_NOTICE, PATTERN_AGAIN_NOTICE)
                        if (mPatternAgainErrorNum > 5) {
                            mBinding.tvReset.visibility = View.VISIBLE
                        }
                    }
                }
            }

            override fun onClear(view: PatternLockerView) {
            }

        })
    }

    private fun setPwdSuccess() {
        mBinding.tvHint.setTextColor(0xFF333333.toInt())
        mBinding.tvHint.text = "设置成功"
        mBinding.plvLock.isEnabled = false
        //"手势密码设置成功".showToast()
        //保存密码 跳转
        finish()
    }

    //提醒用户出错了
    private fun notifyError(errHint: String, newHint: String) {
        //设置错误的文案和颜色并且shake,然后再改成新的提示
        mBinding.tvHint.text = errHint
        mBinding.tvHint.setTextColor(0xFFFA74833.toInt())
        mBinding.tvHint.shakeShake {
            mBinding.tvHint.translationX = 0f
            postDelay({
                mBinding.tvHint.setTextColor(0xFF333333.toInt())
                mBinding.tvHint.text = newHint
                mBinding.plvLock.clearHitState()
            }, 600)
        }
    }


}