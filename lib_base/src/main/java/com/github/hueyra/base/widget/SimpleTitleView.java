package com.github.hueyra.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.hueyra.base.BaseConfig;
import com.github.hueyra.base.R;
import com.github.hueyra.base.util.Utils;

/**
 * Created by zhujun.
 * Date: 7/5/21
 * Info: __
 */
public class SimpleTitleView extends LinearLayout {

    private TextView mTitle;
    private View mStatusBar;
    private View mSplitLine;
    private ImageView mFinish;
    private TextView mAction;
    private ImageView mActionIcon;

    private int m22pxHeight;

    public SimpleTitleView(Context context) {
        this(context, null);
    }

    public SimpleTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        m22pxHeight = Utils.dp2px(22f);
        LayoutInflater.from(context).inflate(R.layout.layout_simple_title, this, true);
        setBackgroundColor(BaseConfig.INSTANCE.getSTATUS_BAR_BG_COLOR());
        mTitle = findViewById(R.id.stv_tv_title);
        mStatusBar = findViewById(R.id.stv_v_status);
        mAction = findViewById(R.id.stv_tv_action);
        mActionIcon = findViewById(R.id.stv_iv_action);
        mFinish = findViewById(R.id.stv_iv_back);
        mSplitLine = findViewById(R.id.stv_v_split);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SimpleTitleView);
        String title = array.getString(R.styleable.SimpleTitleView_stv_title);
        int finishResId = array.getResourceId(R.styleable.SimpleTitleView_stv_finish_icon, 0);
        if (finishResId != 0) {
            mFinish.setImageResource(finishResId);
        }
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }
        String action = array.getString(R.styleable.SimpleTitleView_stv_action);
        if (!TextUtils.isEmpty(action)) {
            mAction.setVisibility(VISIBLE);
            mAction.setText(action);
        } else {
            //有了文案，图标就不处理了
            int icon = array.getResourceId(R.styleable.SimpleTitleView_stv_action_icon, 0);
            if (icon != 0) {
                mActionIcon.setVisibility(VISIBLE);
                mActionIcon.setImageResource(icon);
            }
        }
        boolean show_finish = array.getBoolean(R.styleable.SimpleTitleView_stv_show_finish, true);
        mFinish.setVisibility(show_finish ? VISIBLE : GONE);
        boolean show_split_line = array.getBoolean(R.styleable.SimpleTitleView_stv_show_split_line, true);
        mSplitLine.setVisibility(show_split_line ? VISIBLE : INVISIBLE);
        boolean show_status = array.getBoolean(R.styleable.SimpleTitleView_stv_show_status_bar, false);
        mStatusBar.setVisibility(show_status ? VISIBLE : GONE);
        array.recycle();
    }

    public SimpleTitleView setTitleText(String title) {
        mTitle.setText(title);
        return this;
    }

    public String getTitleText() {
        return mTitle.getText().toString();
    }

    public SimpleTitleView setActionText(String action) {
        mActionIcon.setVisibility(GONE);
        mAction.setVisibility(VISIBLE);
        mAction.setText(action);
        return this;
    }

    public SimpleTitleView setActionIcon(int resId) {
        mAction.setVisibility(GONE);
        mActionIcon.setVisibility(VISIBLE);
        mActionIcon.setImageResource(resId);
        return this;
    }

    public SimpleTitleView setActionUnVisible() {
        mAction.setVisibility(GONE);
        mActionIcon.setVisibility(GONE);
        return this;
    }

    public SimpleTitleView setSplitLineVisible(boolean visible) {
        if (visible) {
            if (mSplitLine.getVisibility() != View.VISIBLE) {
                mSplitLine.setVisibility(View.VISIBLE);
            }
        } else {
            if (mSplitLine.getVisibility() == View.VISIBLE) {
                mSplitLine.setVisibility(View.INVISIBLE);
            }
        }
        return this;
    }

    public SimpleTitleView setOnBackClickListener(OnClickListener listener) {
        if (mFinish.getVisibility() == VISIBLE) {
            mFinish.setOnClickListener(listener);
        }
        return this;
    }

    public SimpleTitleView setOnActionClickListener(OnClickListener listener) {
        mAction.setOnClickListener(listener);
        mActionIcon.setOnClickListener(listener);
        return this;
    }

    public SimpleTitleView setActionVisible(boolean visible) {
        mAction.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public SimpleTitleView setActionIconVisible(boolean visible) {
        mActionIcon.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public SimpleTitleView resetStatusBarHeight(int height) {
        if (mStatusBar.getVisibility() == VISIBLE) {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) mStatusBar.getLayoutParams();
            params.height = height > 0 ? height : m22pxHeight;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mStatusBar.setLayoutParams(params);
        }
        return this;
    }


}
