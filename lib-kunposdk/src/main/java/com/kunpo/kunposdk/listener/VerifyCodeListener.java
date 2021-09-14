package com.kunpo.kunposdk.listener;

import com.kunpo.kunposdk.utils.ErrorInfo;

public interface VerifyCodeListener {
    void onFailure(ErrorInfo errorInfo);
    void onSuccess(String code);
}
