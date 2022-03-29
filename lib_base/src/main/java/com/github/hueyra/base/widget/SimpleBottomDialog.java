package com.github.hueyra.base.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.github.hueyra.base.R;

public class SimpleBottomDialog extends BaseBottomDialog {

    private TextView mSbdTvTitle;
    private TextView mSbdTvConfirm;
    private TextView mSbdTvCancel;
    private View mSbdTvSplit;

    private OnPositiveClickListener mPositiveClickListener;
    private OnNegativeClickListener mNegativeClickListener;

    private boolean mAutoDismissFlag = false;

    public SimpleBottomDialog(Context context) {
        super(context);
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_simple_bottom;
    }

    @Override
    protected void initView(View view) {
        mSbdTvTitle = view.findViewById(R.id.sbd_tv_title);
        mSbdTvConfirm = view.findViewById(R.id.sbd_tv_confirm);
        mSbdTvCancel = view.findViewById(R.id.sbd_tv_cancel);
        mSbdTvSplit = view.findViewById(R.id.sbd_v_split);

        mSbdTvConfirm.setOnClickListener(v -> {
            if (mPositiveClickListener != null) {
                mPositiveClickListener.onPositiveClick();
                if (mAutoDismissFlag) {
                    hide();
                }
            } else {
                hide();
            }
        });

        mSbdTvCancel.setOnClickListener(v -> {
            if (mNegativeClickListener != null) {
                mNegativeClickListener.onNegativeClick();
            } else {
                hide();
            }
        });
    }

    public interface OnPositiveClickListener {
        void onPositiveClick();
    }

    public interface OnNegativeClickListener {
        void onNegativeClick();
    }

    public SimpleBottomDialog setOnPositiveClickListener(OnPositiveClickListener l) {
        mPositiveClickListener = l;
        return this;
    }

    public SimpleBottomDialog setOnNegativeClickListener(OnNegativeClickListener l) {
        mNegativeClickListener = l;
        return this;
    }

    public SimpleBottomDialog setTitle(String title) {
        mSbdTvTitle.setText(title);
        return this;
    }


    public SimpleBottomDialog setPositiveText(String text) {
        mSbdTvConfirm.setText(text);
        return this;
    }

    public SimpleBottomDialog setNegativeText(String text) {
        mSbdTvCancel.setText(text);
        return this;
    }

    public SimpleBottomDialog setPositiveClickedAutoDismiss(boolean flag) {
        mAutoDismissFlag = flag;
        return this;
    }

    public SimpleBottomDialog hideNegativeClicker(){
        mSbdTvSplit.setVisibility(View.GONE);
        mSbdTvCancel.setVisibility(View.GONE);
        return this;
    }
}
