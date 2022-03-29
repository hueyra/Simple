package com.github.hueyra.base.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.hueyra.base.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * Created by zhujun.
 * Date: 2021/07/05
 * Info: 基础的底部弹出对话框
 */
public abstract class BaseBottomDialog {

    protected Context mContext;

    //DialogPlus
    protected DialogPlus mDialogProxy;

    public BaseBottomDialog(Context context) {
        mContext = context;
        initData();
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(getDialogRes(), null);
        mDialogProxy = DialogPlus.newDialog(mContext)
                .setContentHolder(new ViewHolder(view))
                .setContentBackgroundResource(R.color.color_dialog_plus_bg)
                .create();
        initView(view);
    }

    /**
     * 获取对话框的layoutResId
     */
    protected abstract int getDialogRes();

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 初始化view
     *
     * @param view 对话框的View
     */
    protected abstract void initView(View view);

    public void show() {
        mDialogProxy.show();
    }

    public void hide() {
        mDialogProxy.dismiss();
    }
}
