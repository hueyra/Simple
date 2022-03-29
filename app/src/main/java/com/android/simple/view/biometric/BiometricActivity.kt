package com.android.simple.view.biometric

import android.content.Intent
import android.os.Bundle
import com.android.simple.databinding.ActivityBiometricBinding
import com.github.hueyra.base.util.onClick
import com.github.hueyra.base.view.BaseBindingActivity

/**
 * Created by zhujun
 * Date : 2022-03-29
 * Desc : _
 */
class BiometricActivity : BaseBindingActivity<ActivityBiometricBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSimpleTitleView(mBinding.stvTitle)
    }

    override fun initBinding() {
        mBinding.apply {
            btnFace.onClick {
                startActivity(Intent(this@BiometricActivity, AuthFaceIDActivity::class.java))
            }
            btnTouch.onClick {
                startActivity(Intent(this@BiometricActivity, AuthTouchIDActivity::class.java))
            }
            btnGesture.onClick {
                startActivity(Intent(this@BiometricActivity, PatternLockActivity::class.java))
            }
        }
    }
}