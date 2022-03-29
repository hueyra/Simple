package com.github.hueyra.base.quickadapter.listener;

import android.view.View;

import com.github.hueyra.base.quickadapter.BaseQuickAdapter;

public abstract class OnItemClickListener extends SimpleClickListener {
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        onSimpleItemClick(adapter, view, position);
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    public abstract void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position);
}
