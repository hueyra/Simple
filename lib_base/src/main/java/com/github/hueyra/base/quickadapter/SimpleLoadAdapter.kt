package com.jnzx.basecore.quickadapter

import com.github.hueyra.base.quickadapter.BaseQuickAdapter
import com.github.hueyra.base.quickadapter.BaseViewHolder

/**
 * Created by zhujun
 * Date : 2021-11-20
 * Desc : 简单的可加载列表adapter，复写至BaseQuickAdapter
 */
class SimpleLoadAdapter<T>(
    layoutId: Int, list: List<T>,
    var converter: ((BaseViewHolder, T, Int) -> Unit)
) : BaseQuickAdapter<T, BaseViewHolder>(layoutId, list) {

    override fun convert(holder: BaseViewHolder, item: T, position: Int) {
        converter.invoke(holder, item, position)
    }

}