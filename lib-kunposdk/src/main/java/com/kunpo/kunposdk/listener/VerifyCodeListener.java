package com.kunpo.kunposdk.listener;

import com.kunpo.kunposdk.utils.ErrorInfo;

import java.util.Map;

public interface VerifyCodeListener {
    void onFailure(ErrorInfo errorInfo);
    void onSuccess(Map<String, Object> mapResult);
}
