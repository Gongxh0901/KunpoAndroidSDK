package com.kunpo.kunposdk.request;

import android.util.ArrayMap;
import com.kunpo.kunposdk.listener.RequestListener;
import com.kunpo.kunposdk.utils.ContextUtils;

import java.util.HashMap;
import java.util.Map;

/** 登录请求 */
public class RequestLoginPhonenumber extends RequestBase {
    /** 用户登录地址 */

    /**
     * 手机号登录
     * @param phone_number 手机号
     * @param verify_code 验证码
     * @param requestListener
     */
    public void onTaskStart(final String phone_number, final String verify_code, RequestListener requestListener) {
        Map<String, Object> mapParams;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            mapParams = new ArrayMap<>();
        } else {
            mapParams = new HashMap<>();
        }
        mapParams.put(RequsetKeys.PHONE_NUMBER, phone_number);
        mapParams.put(RequsetKeys.VERIFY_CODE, verify_code);

        generateLoginParams(mapParams);
        generateHeader(null);

        _requestListener = requestListener;
        ContextUtils.runOnMainLooper(new Runnable() {
            public void run() {
                post(URLConfig.BASE_URL + "/api/usersys/phoneLogin");
            }
        });
    }
//    /**
//     * @param requestListener
//     */
//    public void logout(RequestListener requestListener) {
//        generateData(null);
//        generateHeader(null);
//
//        _requestListener = requestListener;
//        ContextUtils.runOnMainLooper(new Runnable() {
//            public void run() {
//                post(ConstantsKey.BASE_URL + URL_LOGOUT);
//            }
//        });
//    }
}
