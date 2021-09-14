package com.kunpo.kunposdk.request;

import com.kunpo.kunposdk.listener.RequestListener;
import com.kunpo.kunposdk.utils.ContextUtils;
import java.util.Map;

public class RequestAutoLogin extends RequestBase {
    protected void generateHeader(Map<String, String> headerMap) {
        super.generateHeader(headerMap);
    }

    /**
     * 自动登录
     * @param openid 当前用户openid
     * @param requestListener
     */
    public void onTaskStart(final String openid, RequestListener requestListener) {
        generateParams(null);
        generateHeader(null);

        _requestListener = requestListener;
        ContextUtils.runOnMainLooper(new Runnable() {
            public void run() {
                post(URLConfig.BASE_URL + "/api/usersys/tokenLogin");
            }
        });
    }
}

