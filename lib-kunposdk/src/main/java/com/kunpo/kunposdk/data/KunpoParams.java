package com.kunpo.kunposdk.data;

import com.kunpo.kunposdk.manager.DataManager;
import com.kunpo.kunposdk.utils.Constant;
import com.kunpo.kunposdk.utils.ContextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class KunpoParams {
    private String _appid;
    private String _app_secret;

    /** set 方法 */
    public KunpoParams appid(String appid) {
        _appid = appid;
        return this;
    }

    public KunpoParams appSecret(String secret) {
        _app_secret = secret;
        return this;
    }

    public KunpoParams channel(ChannelType channelType) {
        DataManager.getInstance().setChannelID(channelType);
        return this;
    }

    public KunpoParams debug(boolean debug) {
        Constant.DEBUG = debug;
        return this;
    }

    /** get 方法 */
    public String appid() {
        return _appid;
    }

    public String appSecret() {
        return _app_secret;
    }

    public boolean debug() {
        return Constant.DEBUG;
    }

    public String toString() {
        return "appid:" + _appid + " appSecret:" + _app_secret;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appid", _appid);
            jsonObject.put("appSecret", _app_secret);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
