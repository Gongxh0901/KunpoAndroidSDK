package com.kunpo.kunposdk;

import android.app.Activity;

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
        LoginView.openLoginView(_activity);
    }

    /** 退出登录 */
    public void logout() {

    }
}
