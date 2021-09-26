package com.kunpo.kunposdk.manager;

import com.kunpo.kunposdk.data.UserInfo;
import com.kunpo.kunposdk.listener.LoginListener;
import com.kunpo.kunposdk.listener.LogoutListener;
import com.kunpo.kunposdk.listener.RequestListener;
import com.kunpo.kunposdk.listener.VerifyCodeListener;
import com.kunpo.kunposdk.request.RequestAutoLogin;
import com.kunpo.kunposdk.request.RequestGetVerifyCode;
import com.kunpo.kunposdk.request.RequestLoginPhonenumber;
import com.kunpo.kunposdk.request.RequestLogout;
import com.kunpo.kunposdk.utils.ErrorInfo;

import java.util.Map;

public class RequestManager {
    private static RequestManager _instance;
    private final static String TAG = "manager/RequestManager";

    public static RequestManager getInstance() {
        if (_instance == null) {
            _instance = new RequestManager();
        }
        return _instance;
    }
    private RequestManager() { }

    /**
     * 手机号登录
     * @param phone_number 手机号
     * @param verify_code 验证码
     * @param {LoginListener} listener 登录回调
     */
    public void loginPhoneNumber(final String phone_number, final String verify_code, final LoginListener listener) {
        new RequestLoginPhonenumber().onTaskStart(phone_number, verify_code, new RequestListener() {
            public void onFail(ErrorInfo errorInfo) {
                listener.onFailure(errorInfo);
            }
            public void onSuccess(Map<String, Object> mapResult) {
                UserInfo userInfo = new UserInfo();
                userInfo.loadMap(mapResult);
                userInfo.isVisitor = false;
                DataManager.getInstance().userInfo = userInfo;
                listener.onSuccess(userInfo);
            }
        });
    }

    /**
     * 自动登录
     * @param openid 用户openid
     * @param {LoginListener} listener
     */
    public void autoLogin(final String openid, final LoginListener listener) {
        new RequestAutoLogin().onTaskStart(openid, new RequestListener() {
            public void onFail(ErrorInfo errorInfo) {
                listener.onFailure(errorInfo);
            }
            public void onSuccess(Map<String, Object> mapResult) {
                UserInfo userInfo = new UserInfo();
                userInfo.loadMap(mapResult);
                DataManager.getInstance().userInfo = userInfo;
                listener.onSuccess(userInfo);
            }
        });
    }

    /**
     * 登出
     * @param openid
     * @param {LogoutListener} listener
     */
    public void logout(final String openid, final LogoutListener listener) {
        new RequestLogout().onTaskStart(openid, new RequestListener() {
            public void onFail(ErrorInfo errorInfo) {
                listener.onFailure(errorInfo);
            }
            public void onSuccess(Map<String, Object> mapResult) {
                listener.onSuccess();
            }
        });
    }

    /**
     * 获取验证码
     * @param phone_number 手机号
     * @param scene 场景 （0 登录 1 注册 2 游客绑定 3 绑定手机 4 修改密码）
     * @param {VerifyCodeListener} listener
     */
    public void getVerifyCode(final String phone_number, final int scene, final VerifyCodeListener listener) {
        new RequestGetVerifyCode().onTaskStart(phone_number, scene, new RequestListener() {
            public void onFail(ErrorInfo errorInfo) {
                if (listener != null) {
                    listener.onFailure(errorInfo);
                }
            }
            public void onSuccess(Map<String, Object> mapResult) {
                listener.onSuccess(mapResult);
            }
        });
    }
}
