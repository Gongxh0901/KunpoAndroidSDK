package com.kunpo.kunposdk.listener;

import com.kunpo.kunposdk.data.UserInfo;
import com.kunpo.kunposdk.utils.ErrorInfo;

public interface LoginListener {
	void onFailure(ErrorInfo errorInfo);
	void onSuccess(UserInfo userInfo);
}
