package com.kunpo.kunposdk.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.kunpo.kunposdk.data.UserInfo;
import com.kunpo.kunposdk.utils.DeviceUtils;
import com.kunpo.kunposdk.utils.ResUtils;

public class DataManager {
    private static DataManager _instance;
    private Application _application;
    private Activity _activity;
    public UserInfo userInfo; // 用户信息
    private String _appID;
    private String _sign;
    private String _channelID = "0";
    private String _deviceID;

    public static DataManager getInstance() {
        if (_instance == null) {
            _instance = new DataManager();
        }
        return _instance;
    }
    private DataManager() { }

    /**
     * @param application
     */
    public void onCreate(Application application) {
        _application = application;
        _deviceID = DeviceUtils.getDeviceId();
        Bundle bundle = ResUtils.getApplicationMetaData(application);
        _appID = bundle.getString("KUNPO_APP_ID");
    }

    /**
     * @param activity
     */
    public void onCreate(Activity activity) {
        _activity = activity;
    }

    public Application getApplication() {
        return _application;
    }

    public Activity getActivity() {
        return _activity;
    }

    public String getAppID() {
        return _appID;
    }

    public String getDeviceID() {
        return _deviceID;
    }

    public void setChannelID(String channel) {
        _channelID = channel;
    }

    public String getChannelID() {
        return _channelID;
    }

    public void setSign(String sign) {
        _sign = sign;
    }

    public String getSign() {
        return _sign;
    }
}
