package com.github.hueyra.base.adapter

import android.content.Context
import com.github.hueyra.base.adapter.base.ViewHolder

/**
 * Created by zhujun.
 * Date : 2021/11/15
 * Desc : 简单的列表adapter，复写至CommonAdapter
 */
class SimpleCommAdapter<T>(
    context: Context?,
    layoutId: Int,
    list: List<T>,
    var converter: ((ViewHolder, T, Int) -> Unit)
) : CommonAdapter<T>(context, layoutId, list) {

    override fun convert(holder: ViewHolder, item: T, position: Int) {
        converter.invoke(holder, item, position)
    }
}