package com.github.hueyra.base.data;

import android.text.TextUtils;

/**
 * Created by zhujun.
 * Date : 2021/08/19
 * Desc : Api相应参数
 */
public class IAPIResponse {

    private boolean success;
    private String code;
    private String message;
    private String errmsg4log;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrmsg4log() {
        if (TextUtils.isEmpty(errmsg4log)) {
            return message;
        }
        return errmsg4log;
    }

    public void setErrmsg4log(String errmsg4log) {
        this.errmsg4log = errmsg4log;
    }

    public String getErrToastMsg() {
        return message + " [" + code + "]";
    }

}
