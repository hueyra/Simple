package com.github.hueyra.base.adapter.listener;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zhujun.
 * Date : 2021/08/18
 * Desc : __
 */
public class SimpleOnItemClickListener implements OnItemClickListener {

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
}
