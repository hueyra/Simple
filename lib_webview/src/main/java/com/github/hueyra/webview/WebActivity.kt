package com.github.hueyra.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Created by zhujun
 * Date : 2022-03-29
 * Desc : Tencent X5 WebView
 */
class WebActivity : BaseWebActivity() {

    private val mWaVStatus: View by lazy { findViewById(R.id.wa_v_status) }
    private val mWaIvClose: ImageView by lazy { findViewById(R.id.wa_iv_close) }
    private val mWaTvTitle: TextView by lazy { findViewById(R.id.wa_tv_title) }
    private val mWaVSplitLine: View by lazy { findViewById(R.id.wa_v_split) }
    private val mWebView: WebView by lazy { findViewById(R.id.wa_wv_web) }
    private val mWaPbProgress: ProgressBar by lazy { findViewById(R.id.wa_pb_progress) }
    private val mWaClBottom: ConstraintLayout by lazy { findViewById(R.id.wa_cl_bottom) }
    private val mWaIvBack: ImageView by lazy { findViewById(R.id.wa_iv_back) }
    private val mWaIvForward: ImageView by lazy { findViewById(R.id.wa_iv_forward) }

    private val mTranslationY = dp2px(60f)
    private var mIsLoadingSuccess = false
    private var mCurrentPageCanBackOrForward = false//当前页面，是否可以返回或者前进
    private var mCurrentIsDoingAnim = false//当前页面，是否正在进行滑动动画
    private val mWebViewScrollY = 30//mWaClBottomView显示隐藏最小滑动距离

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        setStatusBarMode(true)
        resetStatusBarHeight(mWaVStatus)
        mWaIvClose.setOnClickListener { finish() }
        //
        mWaClBottom.translationY = mTranslationY
        val url = intent.getStringExtra("url")
        if (!TextUtils.isEmpty(url)) {
            mWaIvBack.setOnClickListener {
                if (mWebView.canGoBack()) {
                    mWebView.goBack()
                }
            }
            mWaIvForward.setOnClickListener {
                if (mWebView.canGoForward()) {
                    mWebView.goForward()
                }
            }

            mWebView.postDelayed({
                initWebView(url!!)
            }, 100)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack()
            } else {
                finish()
            }
        }
        return true
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun initWebView(url: String) {
        // 启用支持javascript
        val settings: WebSettings = mWebView.settings
        settings.javaScriptEnabled = true
        settings.allowFileAccess = true
        settings.databaseEnabled = true
        settings.domStorageEnabled = true
        settings.setGeolocationEnabled(true)
        // //设置加载进来的页面自适应手机屏幕
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        mWebView.addJavascriptInterface(this, "nativeMethod")
        mWebView.setOnScrollChangeListener { _: View?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            //titleView的分割线
            mWaVSplitLine.visibility = if (scrollY > 10) View.VISIBLE else View.INVISIBLE
            //如果此页面可以返回或前进
            if (mCurrentPageCanBackOrForward) {
                //处理mWaClBottom显示隐藏
                if (scrollY - oldScrollY > mWebViewScrollY) {
                    //向下滑动
                    if (mWaClBottom.translationY == 0f && !mCurrentIsDoingAnim) {
                        mCurrentIsDoingAnim = true
                        mWaClBottom.postDelayed({
                            mCurrentIsDoingAnim = false
                        }, 300)
                        //"向下滑动".printLog()
                        mWaClBottom.animate().translationY(mTranslationY).setDuration(300).start()
                    }
                } else if (scrollY - oldScrollY < -mWebViewScrollY) {
                    //向上滑动
                    if (mWaClBottom.translationY == mTranslationY && !mCurrentIsDoingAnim) {
                        mCurrentIsDoingAnim = true
                        mWaClBottom.postDelayed({
                            mCurrentIsDoingAnim = false
                        }, 300)
                        //"向上滑动".printErr()
                        mWaClBottom.animate().translationY(0f).setDuration(300).start()
                    }
                }
            }
        }
        // webView加载url
        mWebView.loadUrl(url)
        // 覆盖webview默认使用第三方或系统默认浏览器打开网页得行为，是网页用webview打开
        mWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // 返回值是true得时候控制去webview打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url)
                return true
            }

            override fun onPageStarted(webView: WebView, s: String?, bitmap: Bitmap?) {
                super.onPageStarted(webView, s, bitmap)
                mIsLoadingSuccess = false
                mWaPbProgress.progress = 0
                mWaPbProgress.visibility = View.VISIBLE
            }

            override fun onPageFinished(webView: WebView, s: String?) {
                super.onPageFinished(webView, s)
                mCurrentPageCanBackOrForward = false
                if (webView.canGoBack()) {
                    mCurrentPageCanBackOrForward = true
                    mWaIvBack.isEnabled = true
                    mWaIvBack.setImageResource(R.mipmap.ic_web_back)
                } else {
                    mWaIvBack.isEnabled = false
                    mWaIvBack.setImageResource(R.mipmap.ic_web_back_un)
                }
                if (webView.canGoForward()) {
                    mCurrentPageCanBackOrForward = true
                    mWaIvForward.isEnabled = true
                    mWaIvForward.setImageResource(R.mipmap.ic_web_forward)
                } else {
                    mWaIvForward.isEnabled = false
                    mWaIvForward.setImageResource(R.mipmap.ic_web_forward_un)
                }
                if (mCurrentPageCanBackOrForward) {
                    mWaClBottom.translationY = 0f
                }
            }
        }
        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(webView: WebView, s: String?) {
                super.onReceivedTitle(webView, s)
                if (!TextUtils.isEmpty(s)) {
                    mWaTvTitle.text = s
                }
            }

            override fun onProgressChanged(web: WebView, progress: Int) {
                // 判断 如果没有加载成功，那就设置进度
                if (!mIsLoadingSuccess) {
                    mWaPbProgress.progress = progress
                }

                // 判断加载成功
                if (progress == 100) {
                    // 设置100%进度
                    mIsLoadingSuccess = true
                    mWaPbProgress.progress = progress
                    // 推迟隐藏
                    mWaPbProgress.postDelayed({
                        mWaPbProgress.visibility = View.GONE
                    }, 500)
                }
                super.onProgressChanged(web, progress)
            }
        }
    }

}