package com.github.hueyra.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.hueyra.base.data.IAPIResponse
import com.github.hueyra.base.util.convertToApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by zhujun.
 * Date: 2021/07/05
 * Info: 基础的ViewModel
 */
open class BaseViewModel : ViewModel() {

    /**
     * 加载状态
     */
    val loadingLD = MutableLiveData<Boolean>()

    /**
     * 通用网络请求异常
     */
    val errorLD = MutableLiveData<String>()

    /**
     * 使用viewModelScope开启一个协程，并且处理错误
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
    ) = viewModelScope.launch(Dispatchers.Main) {
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
     * 使用viewModelScope在IO线程中开启一个协程，并且处理错误
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
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        } finally {
            complete()
        }
    }

    /**
     * 使用viewModelScope在IO线程中开启一个HTTP协程，在错误回调中做了自动处理
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
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            block()
        } catch (e: Throwable) {
            //这里把Throwable转化成默认的ApiResponse，方便后续业务处理
            error(e.convertToApiResponse())
        } finally {
            complete()
        }
    }

    fun launchFlow(
        req: suspend () -> Unit
    ) = flow {
        val rep = req()
        emit(rep)
    }

}