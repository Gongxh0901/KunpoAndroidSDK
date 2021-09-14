package com.kunpo.kunposdk.network.core;

import java.util.Map;

public class KunpoRequest {
    private String _requestMethod = "GET";
    private Map<String, String> _headers = null;
    private Map<String, Object> _params = null;

    public KunpoRequest(String requestMethod, Map<String, String> headers, Map<String, Object> params) {
        this._requestMethod = requestMethod;
        this._headers = headers;
        this._params = params;
    }
    public void setParams(Map<String, Object> params) {
        this._params = params;
    }
    public Map<String, Object> getParams() {
        return _params;
    }

    public void setRequestMethod(String requestMethod) {
        this._requestMethod = requestMethod;
    }
    public String getRequestMethod() {
        return _requestMethod;
    }

    public void setHeaders(Map<String, String> headers) {
        this._headers = headers;
    }
    public Map<String, String> getHeaders() {
        return _headers;
    }
}
