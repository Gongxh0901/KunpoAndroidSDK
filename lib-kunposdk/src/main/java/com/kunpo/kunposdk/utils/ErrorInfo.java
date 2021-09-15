package com.kunpo.kunposdk.utils;

/** 错误信息类 */
public class ErrorInfo {
    private static final String KEY_CODE = "code";
    private static final String KEY_MESSAGE = "msg";
    private static final String KEY_REASON = "reason";

    private String _code = null;
    private String _msg = null;
    private String _reason = null;

    public ErrorInfo(String code, String message) {
        _code = code;
        _msg = message;
        _reason = "";
    }

    public ErrorInfo(String code, String message, String reason) {
        _code = code;
        _msg = message;
        _reason = reason;
    }

    public String getErrorCode() {
        return _code;
    }
    public String getMessage() {
        return _msg;
    }
    public String getReason() {
        return _reason;
    }
    public String toString() {
        return KEY_CODE + ":" + _code + "," + KEY_MESSAGE + ":" + _msg + "," + KEY_REASON + ":" + _reason;
    }

    public String toJsonString() {
        return "{\"" + KEY_CODE + "\":\"" + _code + "\",\"" + KEY_MESSAGE + "\":\"" + _msg + "\",\"" + KEY_REASON + "\":\"" + _reason + "\"}";
    }
}
