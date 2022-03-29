package com.github.hueyra.base.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.hueyra.base.R;

/**
 * Created by zhujun
 * Date : 2021-08-12
 * Desc : 自定义对话框
 */
public abstract class CustomDialog {

    protected AlertDialog alertDialog;
    protected OnDialogPositiveClickListener mOnDialogPositiveClickListener;
    protected OnDialogNegativeClickListener mOnDialogNegativeClickListener;
    protected OnDialogPositiveClickWithTagListener mOnDialogPositiveClickWithTagListener;
    protected OnDialogNegativeClickWithTagListener mOnDialogNegativeClickWithTagListener;
    protected String mTag;

    public CustomDialog(Context context) {
        this(context, true);
    }

    public CustomDialog(Context context, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialog);
        View view = LayoutInflater.from(context).inflate(getCustomLayoutId(), null);
        initView(view);
        builder.setView(view);
        builder.setCancelable(cancelable);
        alertDialog = builder.create();
    }

    protected abstract int getCustomLayoutId();

    protected abstract void initView(View view);

    public void show() {
        try {
            alertDialog.show();
        } catch (Exception e) {
            //
        }
    }

    public void hide() {
        try {
            alertDialog.hide();
        } catch (Exception e) {
            //
        }
    }

    public void dismiss() {
        try {
            alertDialog.dismiss();
            alertDialog.cancel();
        } catch (Exception e) {
            //
        }
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public boolean isShowing() {
        return alertDialog.isShowing();
    }

    public void setOnDialogNegativeClickListener(OnDialogNegativeClickListener listener) {
        mOnDialogNegativeClickListener = listener;
    }

    public void setOnDialogPositiveClickListener(OnDialogPositiveClickListener listener) {
        mOnDialogPositiveClickListener = listener;
    }

    public void setOnDialogNegativeClickWithTagListener(OnDialogNegativeClickWithTagListener listener) {
        mOnDialogNegativeClickWithTagListener = listener;
    }

    public void setOnDialogPositiveClickWithTagListener(OnDialogPositiveClickWithTagListener listener) {
        mOnDialogPositiveClickWithTagListener = listener;
    }

    public interface OnDialogPositiveClickListener {
        void onCustomDialogPositiveClick();
    }

    public interface OnDialogNegativeClickListener {
        void onCustomDialogNegativeClick();
    }

    public interface OnDialogPositiveClickWithTagListener {
        void onCustomDialogPositiveClickWithTag(String tag);
    }

    public interface OnDialogNegativeClickWithTagListener {
        void onCustomDialogNegativeClickWithTag(String tag);
    }
}

