package com.kunpo.kunposdk.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.kunpo.kunposdk.KunpoKit;
import com.kunpo.kunposdk.data.ChannelType;
import com.kunpo.kunposdk.data.RuningData;
import com.kunpo.kunposdk.data.UserInfo;
import com.kunpo.kunposdk.utils.DeviceUtils;
import com.kunpo.kunposdk.utils.ResUtils;

public class DataManager {
    public UserInfo userInfo; // 用户信息
    public RuningData runingData = new RuningData(); // 运行时数据

    private static DataManager _instance;
    private Application _application;
    private Activity _activity;
    private String _appID;
    private ChannelType _channelID = ChannelType.Android;
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
        return KunpoKit.getInstance().getKunpoParams().appid();
    }

    public String getAppSecret() {
        return KunpoKit.getInstance().getKunpoParams().appSecret();
    }

    public String getDeviceID() {
        return _deviceID;
    }

    public void setChannelID(ChannelType channel) {
        _channelID = channel;
    }

    public ChannelType getChannelID() {
        return _channelID;
    }
}
