package com.kunpo.kunposdk.network.core;

import android.util.Log;
import android.util.LruCache;

import com.kunpo.kunposdk.utils.KunpoLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 缓存类 */
public class KunpoCache {
    private static final String TAG = "NetWork/KunpoCache";
    private LruCache<String, KunpoResponse> _caches;
    public KunpoCache(int size) {
        _caches = new LruCache<String, KunpoResponse>(size) {
            @Override
            protected int sizeOf(String key, KunpoResponse value) {
                return 1;
            }
        };
    }

    /** 加入缓存 */
    public void put(String url, KunpoRequest request, KunpoResponse response) {
        String featureString = getFeatureString(url, request);
        _caches.put(featureString, response);
    }

    /** 获取缓存 */
    public KunpoResponse get(String url, KunpoRequest request) {
        String featureString = getFeatureString(url, request);
        KunpoLog.d(TAG, "featureString: --" + featureString + "--");
        return _caches.get(featureString);
    }

    /** 移除缓存 */
    public void remove(String url, KunpoRequest request) {
        String featureString = getFeatureString(url, request);
        _caches.remove(featureString);
    }

    /** 提取request特征码 */
    private String getFeatureString(String url, KunpoRequest request) {
        String featureString = url;

        //对请求头进行重整排序
        Map<String, String> headers = request.getHeaders();
        Set<String> headersKeySet = headers.keySet();

        List<String> headersKeyList = new ArrayList<>();
        for (String headerKey : headersKeySet) {
            headersKeyList.add(headerKey);
        }
        Collections.sort(headersKeyList);

        //特征码加入请求头
        featureString = featureString + ":";
        for (String headerKey : headersKeyList) {
            featureString = featureString + headerKey + "=" + headers.get(headerKey) + ":";
        }

        //对请求参数进行重整排序
        Map<String, Object> params = request.getParams();
        Set<String> paramKeySet = params.keySet();
        List<String> paramKeyList = new ArrayList<>();

        for (String paramKey : paramKeySet) {
            paramKeyList.add(paramKey);
        }
        Collections.sort(paramKeyList);

        //特征码加入请求参数
        for (Object paramKey : paramKeyList) {
            featureString = featureString + paramKey + "=" + params.get(paramKey) + ":";
        }

        //特征码加入请求方法
        featureString = featureString + request.getRequestMethod();
        return featureString;
    }
}
