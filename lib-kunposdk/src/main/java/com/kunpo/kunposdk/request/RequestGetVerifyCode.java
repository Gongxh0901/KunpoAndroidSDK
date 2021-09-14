package com.kunpo.kunposdk.request;

import android.util.ArrayMap;

import com.kunpo.kunposdk.listener.RequestListener;
import com.kunpo.kunposdk.utils.ContextUtils;

import java.util.HashMap;
import java.util.Map;

/** 登录请求 */
public class RequestGetVerifyCode extends RequestBase {
    protected void generateHeader(Map<String, String> headerMap) {
        super.generateHeader(headerMap);
    }

    /**
     * 获取验证码
     * @param phone_number 手机号
     * @param scene 场景验证码（0 登录 1 注册 2 游客绑定 3 绑定手机 4 修改密码）
     * @param requestListener
     */
    public void onTaskStart(final String phone_number, final int scene, RequestListener requestListener) {
        Map<String, Object> mapParams;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            mapParams = new ArrayMap<>();
        } else {
            mapParams = new HashMap<>();
        }
        mapParams.put(RequsetKeys.PHONE_NUMBER, phone_number);
        mapParams.put(RequsetKeys.SCENE, scene);

        generateParams(mapParams);
        generateHeader(null);

        _requestListener = requestListener;
        ContextUtils.runOnMainLooper(new Runnable() {
            public void run() {
                post(URLConfig.BASE_URL + "/api/usersys/getVerifyCode");
            }
        });
    }
}
