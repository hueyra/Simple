package com.github.hueyra.base.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.github.hueyra.base.R;
import com.github.hueyra.base.adapter.CommonAdapter;
import com.github.hueyra.base.adapter.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujun.
 * Date: 2021/07/05
 * Info: 基础的底部弹出对话框
 */
public class BottomItemsDialog extends BaseBottomDialog {

    private String mTag;
    private boolean mAutoDismissFlag;
    private List<String> mStringLists;

    private TextView mBidTvTitle;
    private View mBidVTitleSplit;
    private RecyclerView mBidLvContent;
    private TextView mBidBtnCancel;

    private MyAdapter mMyAdapter;

    private OnBottomItemClickListener mBottomItemClickListener;
    private OnBottomCancelClickListener mBottomCancelClickListener;

    public BottomItemsDialog(Context context) {
        super(context);
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_bottom_items;
    }

    @Override
    protected void initData() {
        super.initData();
        mAutoDismissFlag = false;
        mStringLists = new ArrayList<>();
        mTag = "";
    }

    @Override
    protected void initView(View view) {
        mBidTvTitle = view.findViewById(R.id.bid_tv_title);
        mBidVTitleSplit = view.findViewById(R.id.bid_v_title_split);
        mBidLvContent = view.findViewById(R.id.bid_rv_content);
        mBidBtnCancel = view.findViewById(R.id.bid_btn_cancel);
        mBidBtnCancel.setText("取消");
        mBidBtnCancel.setOnClickListener(v -> {
            if (mBottomCancelClickListener != null) {
                mBottomCancelClickListener.onCancelClick();
            } else {
                hide();
            }
        });
    }

    public BottomItemsDialog setTitle(String titleText) {
        mBidTvTitle.setText(titleText);
        mBidTvTitle.setVisibility(View.VISIBLE);
        mBidVTitleSplit.setVisibility(View.VISIBLE);
        return this;
    }

    public BottomItemsDialog setCancelText(String text) {
        mBidBtnCancel.setText(text);
        return this;
    }

    public BottomItemsDialog setItems(List<String> items) {
        return setItems(items, "");
    }

    public BottomItemsDialog setItemsTag(String tag) {
        mTag = tag;
        return this;
    }

    public BottomItemsDialog setItems(List<String> items, String tag) {
        mTag = tag;
        mStringLists.clear();
        mStringLists.addAll(items);
        mMyAdapter = new MyAdapter(mContext, R.layout.dialog_bottom_items_text, mStringLists);
        mBidLvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mBidLvContent.setAdapter(mMyAdapter);
        return this;
    }

    public BottomItemsDialog setOnBottomItemClickListener(OnBottomItemClickListener l) {
        mBottomItemClickListener = l;
        return this;
    }

    public BottomItemsDialog setOnBottomCancelClickListener(OnBottomCancelClickListener l) {
        mBottomCancelClickListener = l;
        return this;
    }

    public BottomItemsDialog setItemClickedAutoDismiss(boolean flag) {
        mAutoDismissFlag = flag;
        return this;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void show() {
        if (mBidTvTitle.getVisibility() == View.VISIBLE) {
            mMyAdapter.showTopCorner = false;
            mMyAdapter.notifyDataSetChanged();
        }
        mDialogProxy.show();
    }

    public interface OnBottomItemClickListener {
        void onItemClick(int position, String tag);
    }

    public interface OnBottomCancelClickListener {
        void onCancelClick();
    }

    class MyAdapter extends CommonAdapter<String> {

        private boolean showTopCorner;

        MyAdapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
            showTopCorner = true;
        }

        @Override
        protected void convert(ViewHolder viewHolder, String item, int position) {
            viewHolder.setVisible(R.id.bidi_v_items_split, position < mDatas.size() - 1);
            TextView textView = viewHolder.getView(R.id.bidi_tv_items_text);
            textView.setText(item);
            textView.setOnClickListener(v -> {
                mBottomItemClickListener.onItemClick(position, mTag);
                if (mAutoDismissFlag) {
                    hide();
                }
            });
            if (showTopCorner && position == 0) {
                textView.setBackgroundResource(R.drawable.bg_ripple_with_top_corner);
            } else {
                textView.setBackgroundResource(R.drawable.bg_ripple_normal);
            }
        }

    }
}