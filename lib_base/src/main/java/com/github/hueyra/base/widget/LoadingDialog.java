package com.github.hueyra.base.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.hueyra.base.R;

public class LoadingDialog {

    protected AlertDialog alertDialog;
    private TextView tvLoading;

    public LoadingDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogBgNull);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        tvLoading = view.findViewById(R.id.tv_load_dialog);
        tvLoading.setText("处理中...");
        builder.setView(view);
        alertDialog = builder.create();
    }

    public void setCancelable(boolean b) {
        try {
            if (alertDialog != null) {
                alertDialog.setCancelable(b);
            }
        } catch (Exception w) {
            //
        }
    }

    public void setMessage(String msg) {
        try {
            if (tvLoading != null) {
                tvLoading.setText(msg);
            }
        } catch (Exception w) {
            //
        }
    }

    public void show() {
        if (alertDialog != null) {
            alertDialog.show();
        }
    }

    public void hide() {
        if (alertDialog != null) {
            alertDialog.hide();
        }
    }

    public void cancel() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog.cancel();
        }
    }


}
