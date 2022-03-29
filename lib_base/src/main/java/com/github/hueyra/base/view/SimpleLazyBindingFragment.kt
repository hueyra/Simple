package com.github.hueyra.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.github.hueyra.base.data.IAPIResponse
import com.github.hueyra.base.util.convertToApiResponse
import com.github.hueyra.base.util.inflateBindingWithGeneric
import com.github.hueyra.base.widget.SimpleTitleView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by zhujun.
 * Date: 7/5/21
 * Info: 需配合viewpager.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
 */
abstract class SimpleLazyBindingFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val mBinding: VB get() = _binding!!

    private var isLoaded = false

    override fun onResume() {
        super.onResume()
        //增加了Fragment是否可见的判断
        if (!isLoaded && !isHidden) {
            lazyLoadData()
            isLoaded = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBindingWithGeneric(layoutInflater, container, false)
        initView()
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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

    protected abstract fun initView()

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