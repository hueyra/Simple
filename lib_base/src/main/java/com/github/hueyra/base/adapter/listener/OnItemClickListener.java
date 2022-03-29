package com.github.hueyra.base.adapter.listener;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zhujun.
 * Date : 2021/08/18
 * Desc : Adapter的OnItemClickListener
 */
public interface OnItemClickListener {

    void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

    boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
}
