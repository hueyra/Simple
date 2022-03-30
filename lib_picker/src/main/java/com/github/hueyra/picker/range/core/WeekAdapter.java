package com.github.hueyra.picker.range.core;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeekAdapter extends BaseAdapter {
    private static final String[] DATA = new String[]{
            "日", "一", "二", "三", "四", "五", "六"
    };
    private ColorScheme colorScheme = new ColorScheme();

    public void setColorScheme(ColorScheme colorScheme) {
        if (colorScheme == null) {
            colorScheme = new ColorScheme();
        }
        this.colorScheme = colorScheme;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return DATA.length;
    }

    @Override
    public Object getItem(int position) {
        return DATA[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(15);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        int padding = (int) (parent.getResources().getDisplayMetrics().density * 10);
        textView.setPadding(0, padding, 0, padding);
        textView.setText(DATA[position]);
        textView.setBackgroundColor(colorScheme.weekBackgroundColor());
        textView.setTextColor(colorScheme.weekTextColor());
        return textView;
    }

}
