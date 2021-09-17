package com.kunpo.kunposdk;

import android.app.Activity;

import com.kunpo.kunposdk.listener.LoginListener;
import com.kunpo.kunposdk.manager.ViewManager;
import com.kunpo.kunposdk.utils.KunpoLog;
import com.kunpo.kunposdk.view.DialogType;
import com.kunpo.kunposdk.view.LoginView;

import java.util.Stack;

/**
 * 登录相关接口
 */
public class LoginKit {
    private final String TAG = "LoginKit";
    private Activity _activity;
    private LoginListener _listener;
    LoginKit(Activity activity) {
        _activity = activity;
    }

    public LoginListener getListener() {
        return _listener;
    }

    /** 手机号登录 */
    public void login(LoginListener listener) {
        if (null == listener) {
            KunpoLog.e(TAG, " login > LoginListener 不能为空 ");
            return;
        }
        _listener = listener;
        ViewManager.getInstance().createDialog(DialogType.LoginView);
    }

    /** 退出登录 */
    public void logout() {

    }
}