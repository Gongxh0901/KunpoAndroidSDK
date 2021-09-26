package com.kunpo.kunposdk.request;

import android.app.Activity;
import android.os.Build;
import android.util.ArrayMap;

import com.kunpo.kunposdk.KunpoKit;
import com.kunpo.kunposdk.listener.RequestListener;
import com.kunpo.kunposdk.manager.DataManager;
import com.kunpo.kunposdk.network.KunpoHttpClient;
import com.kunpo.kunposdk.network.core.KunpoRequest;
import com.kunpo.kunposdk.network.core.KunpoRequestCallBack;
import com.kunpo.kunposdk.network.core.KunpoResponse;
import com.kunpo.kunposdk.utils.Constant;
import com.kunpo.kunposdk.utils.ContextUtils;
import com.kunpo.kunposdk.utils.DeviceUtils;
import com.kunpo.kunposdk.utils.ErrorInfo;
import com.kunpo.kunposdk.utils.JsonUtils;
import com.kunpo.kunposdk.utils.KunpoLog;
import com.kunpo.kunposdk.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

abstract class RequestBase {
    private static final String TAG = "Request/RequestBase";
    protected Map<String, Object> _requestParams;
    protected Map<String, String> _requestHeaders;
    protected RequestListener _requestListener;
    RequestBase() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            _requestParams = new ArrayMap<>();
        } else {
            _requestParams = new HashMap<>();
        }
        _requestHeaders = new TreeMap<>();
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
        KunpoLog.d(TAG, "开始请求 url:" + url);
        _requestHeaders.put("KP-Sign", Utils.generateSign(_requestHeaders, _requestParams));
        KunpoRequest request = new KunpoRequest(method, _requestHeaders, _requestParams);
        KunpoHttpClient httpClient = KunpoHttpClient.builder().url(url).request(request);
        httpClient.execute(new KunpoRequestCallBack() {
            @Override
            public void success(KunpoResponse response) {
                KunpoLog.i(TAG,  "请求结果 code:" + response.getCode() + " body:" + response.getBody());
                onSuccess(response.getCode(), response.getBody());
            }

            @Override
            public void error(KunpoResponse response) {
                if (null == response) {
                    KunpoLog.i(TAG,  "请求失败response:null");
                    onFail(-999, "response is null");
                } else {
                    KunpoLog.i(TAG,  "请求失败 code:" + response.getCode() + " msg:" + response.getMessage());
                    onFail(response.getCode(), response.getMessage());
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
        common_params.put("channel_id", String.valueOf(DataManager.getInstance().getChannelID().ordinal()));

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
    protected final void generateHeader(Map<String, String> headerMap) {
        _requestHeaders.clear();
        _requestHeaders.put("Content-Type", "application/json");
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
    protected void onFail(int code, String msg) {
        KunpoLog.d(TAG, "onFail:" + code + ":" + msg);
        if (_requestListener != null) {
            _requestListener.onFail(new ErrorInfo(String.valueOf(code), msg));
        }
    }

    /** 成功 */
    protected void onSuccess(int status, String response) {
        Map<String, Object> responseMap = JsonUtils.jsonStringToMap(response);
        Activity activity = DataManager.getInstance().getActivity();
        if (null == responseMap) {
            ContextUtils.showDialog(activity, Constant.ERROR_CODE_PARSE, Constant.ERROR_MESSAGE_PARSE);
            return;
        }
        int code = (int) responseMap.get("code");
        if (code == 0) {
            Map<String, Object> retMap = null;
            try {
                Object data = responseMap.get("data");
                if (data instanceof List) {
                    retMap = new HashMap<>();
                    retMap.put("data", data);
                } else if (data instanceof Map) {
                    retMap = (Map<String, Object>) data;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != _requestListener) {
                if (retMap != null) {
                    _requestListener.onSuccess(retMap);
                } else {
                    _requestListener.onSuccess(new HashMap<>());
                }
            }
        } else {
            String code_string = String.valueOf(code);
            String msg = String.valueOf(responseMap.get("msg"));
            if (null != _requestListener) {
                _requestListener.onFail(new ErrorInfo(code_string,  msg));
            }
            ContextUtils.showDialog(activity, code_string, msg);
        }
    }
}
