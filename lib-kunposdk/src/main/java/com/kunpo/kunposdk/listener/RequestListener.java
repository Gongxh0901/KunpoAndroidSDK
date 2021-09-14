package com.kunpo.kunposdk.listener;

import com.kunpo.kunposdk.utils.ErrorInfo;
import java.util.Map;

public interface RequestListener {
    void onFail(ErrorInfo errorInfo);
    void onSuccess(Map<String, Object> mapResult);
}
