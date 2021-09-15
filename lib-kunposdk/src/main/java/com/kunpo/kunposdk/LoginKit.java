package com.kunpo.kunposdk;

import android.app.Activity;

import com.kunpo.kunposdk.manager.ViewManager;
import com.kunpo.kunposdk.view.DialogType;
import com.kunpo.kunposdk.view.LoginView;

/**
 * 登录相关接口
 */
public class LoginKit {
    private Activity _activity;
    LoginKit(Activity activity) {
        _activity = activity;
    }

    /** 手机号登录 */
    public void loginPhone() {
        ViewManager.getInstance().createDialog(DialogType.LoginView);
    }

    /** 退出登录 */
    public void logout() {

    }
}