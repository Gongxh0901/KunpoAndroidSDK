package com.kunpo.kunposdk.request;

import android.util.ArrayMap;

import com.kunpo.kunposdk.listener.RequestListener;
import com.kunpo.kunposdk.utils.ContextUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestLogout extends RequestBase {
    protected void generateHeader(Map<String, String> headerMap) {
        super.generateHeader(headerMap);
    }

    /**
     * 登出
     * @param openid 当前用户openid
     * @param requestListener
     */
    public void onTaskStart(final String openid, RequestListener requestListener) {
        Map<String, Object> mapParams;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            mapParams = new ArrayMap<>();
        } else {
            mapParams = new HashMap<>();
        }
        mapParams.put(RequsetKeys.OPENID, openid);

        generateParams(mapParams);
        generateHeader(null);

        _requestListener = requestListener;
        ContextUtils.runOnMainLooper(new Runnable() {
            public void run() {
                post(URLConfig.BASE_URL + "/api/usersys/signOut");
            }
        });
    }
}
