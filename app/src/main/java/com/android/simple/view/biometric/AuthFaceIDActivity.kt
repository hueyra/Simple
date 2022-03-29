package com.android.simple.view.biometric

import android.os.Bundle
import android.widget.Toast
import com.android.simple.R
import com.android.simple.databinding.ActivityBiometricAuthBinding
import com.github.hueyra.base.view.BaseBindingActivity
import com.github.hueyra.biometricauth.BiometricAuth
import com.github.hueyra.biometricauth.model.BiometricAuthCallback
import com.github.hueyra.biometricauth.model.BiometricConst
import com.github.hueyra.biometricauth.widget.AuthErrorDialog
import com.github.hueyra.biometricauth.widget.OnAuthActionListener
import com.github.hueyra.biometricauth.widget.SimpleAuthDialog

/**
 * Created by zhujun.
 * Date : 2021/10/19
 * Desc : 授权面容ID页面
 */
class AuthFaceIDActivity : BaseBindingActivity<ActivityBiometricAuthBinding>(),
    BiometricAuthCallback, OnAuthActionListener {

    private val mSimpleAuthDialog by lazy { SimpleAuthDialog(this) }
    private val mAuthErrorDialog by lazy { AuthErrorDialog(this) }

    private var mCurrentAuthTime = 1//当前识别的次数

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSimpleTitleView(mBinding.stvTitle)
        startAuth()
    }

    override fun onDestroy() {
        super.onDestroy()
        BiometricAuth.getInstance().cancelAuth()
    }

    override fun initBinding() {
        mBinding.apply {
            afaRivHead.setImageResource(R.mipmap.ic_launcher_round)
            afaTvName.text = "UserName"
            afaIvAuthImg.setImageResource(com.github.hueyra.biometricauth.R.mipmap.ic_face_id_green)
            afaIvAuthText.text = ("点击授权使用面容识别")
            afaLlAuth.setOnClickListener { startAuth() }
        }
        mSimpleAuthDialog.setOnAuthActionListener(this)
        mAuthErrorDialog.setOnAuthActionListener(this)
    }

    //开始识别认证
    private fun startAuth() {
        //这边主要做一个判断，如果是超过5次，就直接报识别太多次了
        if (mCurrentAuthTime >= 5) {
            mAuthErrorDialog.authErrorTooManyTimes(true)
        } else {
            //设置回调
            BiometricAuth.getInstance().setBiometricAuthCallback(this)
            val check = BiometricAuth.getInstance().canAuthenticate(this)
            //检查一下能不能支持生物识别
            if (check.isSuccess) {
                //如果是支持的，那么用soter看一下能不能用人脸，最后调用认证是通过soter api实现的
                val soterCheck = BiometricAuth.getInstance().isSupportFaceAuth(this)
                if (soterCheck.isSuccess) {
                    //soter也支持就可以认证了
                    if (!BiometricAuth.getInstance().isOpenSoterFaceAuth) {
                        BiometricAuth.getInstance().openSoterFaceAuth(this)
                    } else {
                        BiometricAuth.getInstance().authenticateWithSoterFace(this)
                    }
                } else {
                    Toast.makeText(this, soterCheck.errorMsg, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, check.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onShowLoading(msg: String?) {
        if (!mAuthErrorDialog.isShowing()) {
            mSimpleAuthDialog.loading()
        }
    }

    override fun onHideLoading() {
    }

    override fun onShowAuthDialog(biometricType: Int) {
        if (!mAuthErrorDialog.isShowing()) {
            mSimpleAuthDialog.authWithFaceID()
        }
    }

    override fun onHideAuthDialog(isSuccess: Boolean) {
    }

    override fun onNotifyAuthDialogOnceAgain(biometricType: Int) {
        if (mCurrentAuthTime == 2) {
            //直接提示识别错误对话框
            mSimpleAuthDialog.hide()
            BiometricAuth.getInstance().cancelAuth()
            mAuthErrorDialog.authErrorWithFaceID()
        } else if (mCurrentAuthTime == 3) {
            //直接提示识别错误对话框
            mSimpleAuthDialog.hide()
            BiometricAuth.getInstance().cancelAuth()
            mAuthErrorDialog.authErrorWithFaceID()
        } else if (mCurrentAuthTime >= 4) {
            //结束，不识别
            mSimpleAuthDialog.hide()
            BiometricAuth.getInstance().cancelAuth()
            mAuthErrorDialog.authErrorTooManyTimes()
        }
        mCurrentAuthTime++
    }

    override fun onAuthenticationStart() {
    }

    override fun onAuthenticationSucceeded() {
        saveAuthAndNavi()
        if (mSimpleAuthDialog.isShowing()) {
            mSimpleAuthDialog.authSuccess()
        } else {
            mAuthErrorDialog.authSuccess()
        }
    }

    //识别成功后，直接保存数据，然后跳转页面，注意跳转的逻辑是在authSuccess动画执行完后
    private fun saveAuthAndNavi() {
        //
    }

    override fun onAuthenticationError(biometricType: Int, err: Int, msg: String?) {
        if (err == 10308 || msg?.startsWith("Too many failed") == true) {
            //错误次数过多，传感器被锁定了
            mCurrentAuthTime = 100
            mSimpleAuthDialog.hide()
            mAuthErrorDialog.authErrorTooManyTimes(true)
        }
    }

    override fun onAuthenticationFailed(biometricType: Int) {
    }

    override fun onAuthenticationCancelled(biometricType: Int) {
    }

    override fun onAuthenticationSoterError(err: Int, msg: String) {
        //当签名出错的时候，需要重新生成签名并验证，参考BiometricAuthManager.getSoterAuthResultErrorMsg
        if (msg.contains(BiometricConst.AUTH_KEY_ERROR)) {
            startAuth()
        }
    }

    override fun onOpenSoterAuthSucceeded(biometricType: Int) {
    }

    override fun onOpenSoterAuthFailed(biometricType: Int) {
    }

    override fun onOpenSysAuthSucceeded() {
    }

    override fun onOpenSysAuthFailed() {
    }

    override fun onAuthAgain() {
        startAuth()
        mAuthErrorDialog.authAgain()
    }

    override fun onAuthCancel() {
        BiometricAuth.getInstance().cancelAuth()
    }

    //这个就是AuthSuccessAnimEnd，跳转
    override fun onAuthSuccessAnimEnd() {
        mSimpleAuthDialog.cancel()
        mAuthErrorDialog.cancel()
        finish()
    }
}