package com.github.hueyra.base.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.github.hueyra.base.data.IAPIResponse
import com.github.hueyra.base.quickadapter.BaseQuickAdapter
import com.github.hueyra.base.quickadapter.BaseViewHolder
import com.github.hueyra.base.util.convertToApiResponse
import com.github.hueyra.base.util.dp
import com.github.hueyra.base.util.printLog
import com.github.hueyra.base.widget.LoadingDialog
import com.github.hueyra.base.widget.SimpleTitleView
import com.github.hueyra.base.widget.SimpleSearchTitleView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by zhujun.
 * Date: 7/5/21
 * Info: 基础的activity封装类
 */
abstract class BaseActivity : AppCompatActivity() {

    //默认的StatusBar高度，默认给22个DP
    private var mStatusBarHeight = 22.dp()

    //是否自动处理StatusBar
    private var mAutoInitStatusBar = true

    //默认的loading对话框
    private val mLoadingDialog: LoadingDialog by lazy { LoadingDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 如果是需要处理StatusBar，那么处理一下
        if (mAutoInitStatusBar) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            //读取一下此页面基础配置项是否为空
            getActivityBaseConfig()?.let {
                if (it.windowBgColor != 0) {
                    //设置一个页面的底色，这边有个默认的值
                    window.decorView.setBackgroundColor(it.windowBgColor)
                }
                setContentView(it.layoutResId)
                val statusView: View
                if (it.statusBarViewId > 0) {
                    //找到这个statusBarView ,然后设置颜色高度
                    statusView = findViewById(it.statusBarViewId)
                    if (it.statusBarColor > 0) {
                        statusView.setBackgroundColor(it.statusBarColor)
                    } else {
                        if (null != it.textMode && it.textMode == PageUIConfig.StatusBarMode.DARK) {
                            statusView.setBackgroundColor(Color.WHITE)
                        }
                    }
                    // 调整 刘海 高度
                    resetStatusBarHeight(statusView)
                }
                // 设置状态栏 字体颜色 和 背景
                if (null != it.textMode && it.textMode == PageUIConfig.StatusBarMode.DARK) {
                    // 设置状态栏为白底黑字
                    setStatusBarMode(PageUIConfig.StatusBarMode.DARK)
                }
            }
        }
    }

    override fun onDestroy() {
        mLoadingDialog.cancel()
        super.onDestroy()
    }

    /**
     * 获取此页面的基础配置 ,包括 LayoutResId / Background Color / statusBar Mode（light or dark）
     *
     * @return BaseConfig 此页面的基础配置
     *
     * */
    abstract fun getActivityBaseConfig(): PageUIConfig?

    /**
     * 此方法需在super.onCreated()之前调用，调用之后，在初始化页面的时候不再进行页面基础设置
     *
     * */
    fun setActivityBaseConfigUnable() {
        mAutoInitStatusBar = false
    }

    fun String.showToast() {
        Toast.makeText(applicationContext, this, Toast.LENGTH_SHORT).show()
    }

    suspend fun String.showToastSuspend() {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@BaseActivity, this@showToastSuspend, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 使用ViewModelProvider获取一个ViewModel实例对象
     *
     * @param vm ViewModel Class
     *
     * @return ViewModel 实例对象
     * */
    fun <T : ViewModel?> initViewModel(vm: Class<T>): T {
        return ViewModelProvider(this).get(vm)
    }

    /**
     * 使用ViewModelProvider获取一个ViewModel实例对象
     *
     * @param vm ViewModel Class
     *
     * @return ViewModel 实例对象
     * */
    fun <T : ViewModel?> viewModelProviderOf(vm: Class<T>): T {
        return ViewModelProvider(this).get(vm)
    }

    /**
     * 使用ViewModelProvider获取一个ViewModel实例对象
     *
     * @param vm ViewModel Class
     *
     * @return ViewModel 实例对象
     * */
    inline fun <reified T : ViewModel> viewModelProviderOf(): T {
        return ViewModelProvider(this).get(T::class.java)
    }

    /**
     * 初始化LiveData ,包括 error_livedata and loading_livedata
     *
     * @param error error_livedata , 用于打印日志
     * @param loading loading_livedata , 用于展示loading
     *
     * */
    fun initLiveData(error: LiveData<String>, loading: LiveData<Boolean>) {
        error.observe(this, Observer {
            ("errorLD -> $it").printLog()
        })

        loading.observe(this, Observer {
            if (!isDestroyed) {
                if (it) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        })

    }

    /**
     * 初始化LiveData loading_livedata
     *
     * @param loading loading_livedata , 用于展示loading
     *
     * */
    fun initLiveData(loading: LiveData<Boolean>) {
        loading.observe(this, Observer {
            if (!isDestroyed) {
                if (it) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        })
    }

    /**
     * 设置SimpleSearchTitleView，自动设置状态栏高度，自动处理点击返回事件
     *
     * @param view SimpleSearchTitleView
     *
     * */
    fun setupSimpleSearchTitleView(view: SimpleSearchTitleView) {
        view.resetStatusBarHeight(getStatusBarHeight())
        view.setOnBackClickListener { finish() }
    }

    /**
     * 设置SimpleTitleView，自动设置状态栏高度，自动处理点击返回事件
     *
     * @param view SimpleTitleView
     *
     * */
    fun setupSimpleTitleView(view: SimpleTitleView) {
        view.resetStatusBarHeight(getStatusBarHeight())
        view.setOnBackClickListener { finish() }
    }

    /**
     * 设置SimpleTitleView的右侧事件
     *
     * @param view SimpleTitleView
     * @param text Action的名称
     * @param listener 点击事件
     *
     * */
    fun setTitleViewAction(view: SimpleTitleView, text: String, listener: View.OnClickListener) {
        view.setActionText(text)
        view.setOnActionClickListener(listener)
    }

    /**
     * 重置一下StatusBar的高度
     *
     * @param statusBarView 状态栏的View
     *
     * */
    protected open fun resetStatusBarHeight(statusBarView: View?) {
        if (null != statusBarView) {
            val height: Int = getStatusBarHeight()
            //设置一下view的layoutParams
            val params = statusBarView.layoutParams
            params.height = if (height > 0) height else mStatusBarHeight.toInt()
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            statusBarView.layoutParams = params
        }
    }

    /**
     * 获取系统状态栏高度，需要调用AndroidAPI获取
     *
     * @return Int 状态栏高度
     *
     * */
    protected open fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = this.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 动态的设置状态的模式，是LIGHT 还是 DARK
     *
     * @return mode 状态栏模式
     *
     * */
    protected open fun setStatusBarMode(mode: PageUIConfig.StatusBarMode) {
        if (mode === PageUIConfig.StatusBarMode.LIGHT) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    /**
     * 显示loading加载框
     *
     * */
    fun showLoading() {
        mLoadingDialog.show()
    }

    /**
     * 显示loading加载框
     *
     * @param msg 对话框的文案
     *
     * */
    fun showLoading(msg: String) {
        mLoadingDialog.setMessage(msg)
        mLoadingDialog.show()
    }

    /**
     * 隐藏loading加载框
     *
     * */
    fun hideLoading() {
        mLoadingDialog.hide()
    }

    /**
     * 使用decorView的handler延迟post一个消息
     *
     * @param action 消息事件
     * @param delayMillis 延迟的时间，单位ms
     *
     * */
    fun postDelay(action: Runnable, delayMillis: Long) {
        window.decorView.postDelayed(action, delayMillis)
    }

    /**
     * suspend显示loading加载框，协程中使用
     *
     * */
    suspend fun showLoadingSuspend() {
        withContext(Dispatchers.Main) {
            mLoadingDialog.show()
        }
    }

    /**
     * suspend显示loading加载框，协程中使用
     *
     * @param msg 对话框的文案
     *
     * */
    suspend fun showLoadingSuspend(msg: String) {
        withContext(Dispatchers.Main) {
            mLoadingDialog.setMessage(msg)
            mLoadingDialog.show()
        }
    }

    /**
     * suspend隐藏loading加载框，协程中使用
     *
     * */
    suspend fun hideLoadingSuspend() {
        withContext(Dispatchers.Main) {
            mLoadingDialog.hide()
        }
    }

    /**
     * 快速设置新数据，主要是加载调用此方法
     *
     * @param pageIndex 当前列表的页码
     * @param pageSizes 当前列表每页数量
     * @param newData 从接口获取的新数据列表
     * @param rvDataSource 当前RecyclerView的数据源
     * @param adapter 当前的adapter
     *
     * */
    @SuppressLint("NotifyDataSetChanged")
    fun <T, K : BaseViewHolder> quickSetNewData(
        pageIndex: Int,
        pageSizes: Int,
        newData: List<T>,
        rvDataSource: MutableList<T>,
        adapter: BaseQuickAdapter<T, K>
    ) {
        //如果新的数据不为空
        if (pageIndex == 1) {
            //第一页，需要清空原本的数据源
            rvDataSource.clear()
            //设置新数据
            adapter.setNewData(rvDataSource)
            //做一个判断，如果小于APP_PAGE_SIZE，那就不要再加载了
            if (newData.size < pageSizes) {
                adapter.setEnableLoadMore(false)
            } else {
                adapter.setEnableLoadMore(true)
            }
        } else {
            //做一个判断，如果小于APP_PAGE_SIZE，那就不要再加载了
            if (newData.size < pageSizes) {
                adapter.loadMoreEnd()
            } else {
                adapter.loadMoreComplete()
            }
        }
        //添加数据，刷新页面
        rvDataSource.addAll(newData)
        adapter.notifyDataSetChanged()
    }

    /**
     * 使用lifecycleScope开启一个协程，并且处理错误
     *
     * @param block 协程主体的suspend函数
     * @param error 协程错误处理的suspend函数
     * @param complete 协程finally的suspend函数
     *
     * */
    fun launch(
        block: suspend () -> Unit,
        error: suspend (Throwable) -> Unit,
        complete: suspend () -> Unit
    ) = lifecycleScope.launch(Dispatchers.Main) {
        try {
            withContext(Dispatchers.IO) {
                block()
            }
        } catch (e: Throwable) {
            error(e)
        } finally {
            complete()
        }
    }

    /**
     * 使用lifecycleScope在IO线程中开启一个协程，并且处理错误
     *
     * @param block 协程主体的suspend函数
     * @param error 协程错误处理的suspend函数
     * @param complete 协程finally的suspend函数
     *
     * */
    fun launchWithIO(
        block: suspend () -> Unit,
        error: suspend (Throwable) -> Unit,
        complete: suspend () -> Unit
    ) = lifecycleScope.launch(Dispatchers.IO) {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        } finally {
            complete()
        }
    }

    /**
     * 使用lifecycleScope在IO线程中开启一个HTTP协程，在错误回调中做了自动处理
     *
     * @param block 协程主体的suspend函数
     * @param error 协程错误处理的suspend函数
     * @param complete 协程finally的suspend函数
     *
     * */
    fun launchHttp(
        block: suspend () -> Unit,
        error: suspend (IAPIResponse) -> Unit,
        complete: suspend () -> Unit
    ) = lifecycleScope.launch(Dispatchers.IO) {
        try {
            block()
        } catch (e: Throwable) {
            //这里把Throwable转化成默认的ApiResponse，方便后续业务处理
            error(e.convertToApiResponse())
        } finally {
            complete()
        }
    }
}