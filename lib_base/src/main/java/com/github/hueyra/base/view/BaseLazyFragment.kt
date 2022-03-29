package com.github.hueyra.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.hueyra.base.data.IAPIResponse
import com.github.hueyra.base.util.convertToApiResponse
import com.github.hueyra.base.widget.SimpleTitleView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by zhujun.
 * Date: 7/5/21
 * Info: 基础的懒片段
 */
abstract class BaseLazyFragment : Fragment() {

    private lateinit var mRootView: View
    private var isVisibleToUser = false
    private var isPrepared = false
    private var isFirst = true

    //--------------------system method callback------------------------//
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isPrepared = true
        prepareData()
    }

    override fun setUserVisibleHint(isVisible: Boolean) {
        super.setUserVisibleHint(isVisible)
        if (userVisibleHint) {
            isVisibleToUser = true
            lazyLoad()
        } else {
            isVisibleToUser = false
            onInvisible()
        }
    }

    override fun onResume() {
        super.onResume()
        if (userVisibleHint) {
            userVisibleHint = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(getLayoutResId(), container, false)
        initView(mRootView)
        return mRootView
    }

    /**
     * 设置SimpleTitleView，自动设置状态栏高度，自动处理点击返回事件
     *
     * @param view SimpleTitleView
     *
     * */
    fun setupSimpleTitleView(view: SimpleTitleView) {
        view.resetStatusBarHeight(getStatusBarHeight())
        view.setOnBackClickListener { activity?.finish() }
    }

    protected open fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = this.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 懒加载
     */
    protected open fun lazyLoad() {
        if (!isPrepared || !isVisibleToUser || !isFirst) {
            return
        }
        lazyLoadData()
        isFirst = false
    }

    protected abstract fun getLayoutResId(): Int

    protected abstract fun initView(view: View)

    /**
     * fragment被设置为不可见时调用
     *
     */
    protected open fun onInvisible() {
    }

    /**
     * 在onActivityCreated中调用的方法，可以用来进行初始化操作。
     *
     */
    protected open fun prepareData() {

    }

    /**
     * 这里获取数据，刷新界面
     *
     */
    protected abstract fun lazyLoadData()

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

    fun String.showToast() {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}