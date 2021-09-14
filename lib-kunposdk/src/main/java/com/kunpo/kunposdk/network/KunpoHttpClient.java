package com.kunpo.kunposdk.network;

import com.kunpo.kunposdk.network.core.KunpoHttpProxy;
import com.kunpo.kunposdk.network.core.KunpoRequest;
import com.kunpo.kunposdk.network.core.KunpoRequestCallBack;
import com.kunpo.kunposdk.network.core.KunpoResponse;

/** 网络请求暴露接口 */
public class KunpoHttpClient {
    private String _url;
    private KunpoRequest _request;
    private KunpoHttpProxy _proxy;

    private KunpoHttpClient() {
        _proxy = KunpoHttpProxy.getInstance();
    }

    public static KunpoHttpClient builder() {
        return new KunpoHttpClient();
    }

    /** 设置url */
    public KunpoHttpClient url(String url) {
        this._url = url;
        return this;
    }

    /** 设置request */
    public KunpoHttpClient request(KunpoRequest request) {
        this._request = request;
        return this;
    }

    /** 同步请求 */
    public KunpoResponse execute() {
        return _proxy.execute(_url, _request);
    }

    /** 异步请求 */
    public void execute(KunpoRequestCallBack callBack) {
        _proxy.execute(_url, _request, callBack);
    }
}
