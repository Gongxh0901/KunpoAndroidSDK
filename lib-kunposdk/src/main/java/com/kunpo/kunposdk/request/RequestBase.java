package com.kunpo.kunposdk.request;

import android.os.Build;
import android.util.ArrayMap;

import com.kunpo.kunposdk.listener.RequestListener;
import com.kunpo.kunposdk.manager.DataManager;
import com.kunpo.kunposdk.network.KunpoHttpClient;
import com.kunpo.kunposdk.network.core.KunpoRequest;
import com.kunpo.kunposdk.network.core.KunpoRequestCallBack;
import com.kunpo.kunposdk.network.core.KunpoResponse;
import com.kunpo.kunposdk.utils.Constant;
import com.kunpo.kunposdk.utils.DeviceUtils;
import com.kunpo.kunposdk.utils.ErrorInfo;
import com.kunpo.kunposdk.utils.JsonUtils;
import com.kunpo.kunposdk.utils.KunpoLog;
import com.kunpo.kunposdk.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class RequestBase {
    private static final String TAG = "Request/RequestBase";
    protected Map<String, Object> _requestParams;
    protected Map<String, String> _requestHeaders;
    protected RequestListener _requestListener;
    RequestBase() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            _requestHeaders = new ArrayMap<>();
            _requestParams = new ArrayMap<>();
        } else {
            _requestHeaders = new HashMap<>();
            _requestParams = new HashMap<>();
        }
    }

    protected void get(String url) {
        request("GET", url);
    }

    protected void post(String url) {
        request("POST", url);
    }

    /**
     * 网络请求
     * @param method
     * @param url
     */
    private void request(String method, final String url) {
        KunpoLog.d(TAG, "开始请求" + _requestParams.get("callback_url"));
        KunpoRequest request = new KunpoRequest(method, _requestHeaders, _requestParams);
//         SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
//         Date curDate = new Date(System.currentTimeMillis());
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // http://yapi.lanfeitech.com/mock/103/
//                    InetAddress x = java.net.InetAddress.getByName("ucenter.kunpogames.com");
//                    String ipAddress = x.getHostAddress(); // 得到字符串形式的ip地址
//                    Log.d(TAG, "ip:" + ipAddress);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.d(TAG, "域名解析出错"+e.getMessage());
//                }
//            }
//        }).start();

        KunpoLog.i(TAG, "url:" + url + " headers:" + _requestHeaders.toString() + " params:" + _requestParams.toString());
        KunpoHttpClient httpClient = KunpoHttpClient.builder().url(url).request(request);
        httpClient.execute(new KunpoRequestCallBack() {
            @Override
            public void success(KunpoResponse response) {
                KunpoLog.i(TAG,  "请求结果===" + response.getCode() + ": " + response.getBody());
                onSuccess(response.getCode(), response.getBody());
            }

            @Override
            public void error(KunpoResponse response) {
                if (null == response) {
                    onFail(0, "");
                } else {
                    KunpoLog.i(TAG, "code:" + response.getCode() + " responseBody:" + response.getBody());
                    onFail(response.getCode(), response.getBody());
                }
            }
        });
    }

    /**
     * 登录发送的数据
     * @param paramsMap
     */
    protected void generateLoginParams(Map<String, Object> paramsMap) {
        _requestParams.clear();
        if (paramsMap != null) {
            _requestParams.putAll(paramsMap);
        }
        Map<String, Object> common_params;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            common_params = new ArrayMap<>();
        } else {
            common_params = new HashMap<>();
        }
        common_params.put("app_version", DeviceUtils.getVersionCode());
        common_params.put("carrier", DeviceUtils.getCarrierType());
        common_params.put("device_id", DeviceUtils.getDeviceId());
        common_params.put("device_model", DeviceUtils.getSystemModel());
        common_params.put("manufacturer", DeviceUtils.getDeviceManufacturer());
        common_params.put("network_type", DeviceUtils.getNetName());
        common_params.put("os", DeviceUtils.getOS());
        common_params.put("os_version", DeviceUtils.getOSVersionName());
        common_params.put("screen_height", DeviceUtils.getWidthPixels());
        common_params.put("screen_width", DeviceUtils.getHeightPixels());
        common_params.put("channel_id", DataManager.getInstance().getChannelID());

        _requestParams.put("common_params", common_params);
    }

    /**
     * 发送的数据
     * @param paramsMap
     */
    protected void generateParams(Map<String, Object> paramsMap) {
        _requestParams.clear();
        if (paramsMap != null) {
            _requestParams.putAll(paramsMap);
        }
    }

    /**
     * header数据
     * @param headerMap
     */
    protected void generateHeader(Map<String, String> headerMap) {
        _requestHeaders.clear();
        _requestHeaders.put("Content-Type", "application/json");
        _requestHeaders.put("KP-Sign", DataManager.getInstance().getSign());
        _requestHeaders.put("KP-Appid", DataManager.getInstance().getAppID());
        if (DataManager.getInstance().userInfo != null) {
            _requestHeaders.put("KP-Token", DataManager.getInstance().userInfo.token);
        }
        _requestHeaders.put("KP-Timestamp", String.valueOf(Utils.timestamp()));
        if (headerMap != null) {
            _requestHeaders.putAll(headerMap);
        }
    }

    /** 失败 */
    protected void onFail(int status, String response) {
        KunpoLog.d(TAG, "onFail:" + status + ":" + response);
        if (_requestListener != null) {
            _requestListener.onFail(new ErrorInfo(Constant.ERROR_CODE_NET, Constant.ERROR_MESSAGE_NET));
        }
    }

    /** 成功 */
    protected void onSuccess(int status, String response) {
        KunpoLog.d(TAG, "onSuccess status:" + status + " response:" + response);
        if (_requestListener != null) {
            Map<String, Object> responseMap = JsonUtils.jsonStringToMap(response);
            if (responseMap != null) {
                String result;
                result = (String) responseMap.get("result");
                if (result != null && result.equalsIgnoreCase("ok")) {
                    Map<String, Object> retMap = null;
                    try {
                        if ((responseMap.get("data")) instanceof List) {
                            retMap = new HashMap<>();
                            retMap.put("data", (List) responseMap.get("data"));
                        } else {
                            retMap = (Map<String, Object>) responseMap.get("data");
                            if (null == retMap) {
                                retMap = new HashMap<>();
                            }
                        }
                    } catch (Exception e) {
                        retMap = new HashMap<>();
                    }
                    if (retMap != null) {
                        _requestListener.onSuccess(retMap);
                    }
                } else {
                    String message = (String) responseMap.get("message");
                    _requestListener.onFail(new ErrorInfo(result, message));
                }
            } else {
                _requestListener.onFail(new ErrorInfo(Constant.ERROR_CODE_PARSE, Constant.ERROR_MESSAGE_PARSE));
            }
        }
    }
}
