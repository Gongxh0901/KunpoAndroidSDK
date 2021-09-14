package com.kunpo.kunposdk.utils;

/** 错误信息类 */
public class ErrorInfo {
    private static final String KEY_ERROR = "err";
    private static final String KEY_MESSAGE = "msg";
    private static final String KEY_REASON = "reason";

    private String _err = null;
    private String _msg = null;
    private String _reason = null;

    public ErrorInfo(String code, String message) {
        _err = code;
        _msg = message;
        _reason = "";
    }

    public ErrorInfo(String code, String message, String reason) {
        _err = code;
        _msg = message;
        _reason = reason;
    }

    public String getError() {
        return _err;
    }
    public String getMessage() {
        return _msg;
    }
    public String getReason() {
        return _reason;
    }
    public String toString() {
        return KEY_ERROR + ":" + _err + "," + KEY_MESSAGE + ":" + _msg + "," + KEY_REASON + ":" + _reason;
    }

    public String toJsonString() {
        return "{\"" + KEY_ERROR + "\":\"" + _err + "\",\"" + KEY_MESSAGE + "\":\"" + _msg + "\",\"" + KEY_REASON + "\":\"" + _reason + "\"}";
    }
}
