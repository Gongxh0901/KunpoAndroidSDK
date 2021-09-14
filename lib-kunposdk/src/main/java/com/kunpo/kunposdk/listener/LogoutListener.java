package com.kunpo.kunposdk.listener;

import com.kunpo.kunposdk.utils.ErrorInfo;

public interface LogoutListener {
	void onFailure(ErrorInfo errorInfo);
	void onSuccess();
}
