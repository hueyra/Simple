package com.github.hueyra.base.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.github.hueyra.base.data.IAPIResponse
import com.github.hueyra.base.quickadapter.BaseQuickAdapter
import com.github.hueyra.base.quickadapter.BaseViewHolder
import com.github.hueyra.base.util.convertToApiResponse
import com.github.hueyra.base.util.inflateBindingWithGeneric
import com.github.hueyra.base.widget.SimpleTitleView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by zhujun.
 * Date: 7/5/21
 * Info: 基础的懒片段
 */
abstract class BaseLazyBindingFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val mBinding: VB get() = _binding!!

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
        _binding = inflateBindingWithGeneric(layoutInflater, container, false)
        initView()
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    /**
     * onCreateView 回调此方法，初始化view
     *
     * */
    protected abstract fun initView()

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
            adapter.setEnableLoadMore(newData.size >= pageSizes)
        } else {
            //做一个判断，如果小于APP_PAGE_SIZE，那就不要再加载了
            if (newData.size < pageSizes) adapter.loadMoreEnd() else adapter.loadMoreComplete()
        }
        //添加数据，刷新页面
        rvDataSource.addAll(newData)
        adapter.notifyDataSetChanged()
    }
}