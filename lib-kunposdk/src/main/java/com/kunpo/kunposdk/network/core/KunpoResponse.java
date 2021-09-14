package com.kunpo.kunposdk.network.core;

public class KunpoResponse {
    private int _code;
    private String _message;
    private String _body;

    public KunpoResponse(int code, String message, String body) {
        this._code = code;
        this._message = message;
        this._body = body;
    }

    public void setCode(int code) {
        this._code = code;
    }
    public int getCode() {
        return _code;
    }

    public void setMessage(String message) {
        this._message = message;
    }
    public String getMessage() {
        return _message;
    }

    public void setBody(String body) {
        this._body = body;
    }
    public String getBody() {
        return _body;
    }
}
