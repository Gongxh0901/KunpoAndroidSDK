package com.kunpo.kunposdk.data;

import android.util.ArrayMap;

import com.kunpo.kunposdk.utils.JsonUtils;
import com.kunpo.kunposdk.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/** 用户信息 */
public class UserInfo {
    public String openid = ""; // openid
    public String uid = ""; // 客户端展示的用户ID（邀请码）
    public String token = ""; // token（请求接口需要携带此token）
    /**
     * 性别
     * 0:男 1:女 2:未知
     */
    public int sex = 2;
    /**
     * 认证状态
     * 0:认证成功 1:认证中 2:未认证
     */
    public int status = 2;
    public String nick_name = ""; // 昵称
    public String mail = ""; // 邮箱
    public String avatar = ""; // 头像
    public String phone_number = ""; // 手机号
    public int expire_time = -1; //token过期时间（秒）

    public int single_max_pay = -1; // 单次最大可充值金额（单位：分；-1表示无限制）
    public int surplus_max_pay = -1; // 剩余可充值金额（单位：分；-1表示无限制）
    public int played_time = 0; // 已经玩了多长时间（单位：秒）
    public int surplus_play_time = -1; // 剩余游玩时间（单位：秒；-1表示无限制）
    public String refuse_msg = ""; // 防沉迷限制原因
    /**
     * 防沉迷限制状态
     * 0：正常，不限制
     * 1：未认证被限制
     * 2：已认证但未在开放时间
     * 3：已认证已达到游玩时间上限
     */
    public int refuse_code = 0;

    /** token是否过期 */
    public boolean isTokenExpire() {
        return Utils.timestamp() > expire_time;
    }
    /** 显示名 */
    public String getDisplayName() {
        if (!Utils.isNullOrEmpty(nick_name)) {
            return nick_name;
        }
        if (!Utils.isNullOrEmpty(phone_number)) {
            return phone_number;
        }
        if (!Utils.isNullOrEmpty(mail)) {
            return mail;
        }
        return "Guest";
    }

    public UserInfo loadMap(Map<String, Object> map) {
        openid = (String) map.get("openid");
        token = (String) map.get("token");

        if (map.containsKey("uid")) {
            uid = (String) map.get("uid");
        }

        if (map.containsKey("sex")) {
            sex = (int) map.get("sex");
        }

        if (map.containsKey("status")) {
            status = (int) map.get("status");
        }

        if (map.containsKey("nick_name")) {
            nick_name = (String) map.get("nick_name");
        }

        if (map.containsKey("mail")) {
            mail = (String) map.get("mail");
        }

        if (map.containsKey("avatar")) {
            avatar = (String) map.get("avatar");
        }

        if (map.containsKey("phone_number")) {
            phone_number = (String) map.get("phone_number");
        }

        if (map.containsKey("expire_time")) {
            expire_time = (int) map.get("expire_time");
        }

        if (map.containsKey("single_max_pay")) {
            single_max_pay = (int) map.get("single_max_pay");
        }

        if (map.containsKey("surplus_max_pay")) {
            surplus_max_pay = (int) map.get("surplus_max_pay");
        }

        if (map.containsKey("played_time")) {
            played_time = (int) map.get("played_time");
        }

        if (map.containsKey("surplus_play_time")) {
            surplus_play_time = (int) map.get("surplus_play_time");
        }

        if (map.containsKey("refuse_msg")) {
            refuse_msg = (String) map.get("refuse_msg");
        }

        if (map.containsKey("refuse_code")) {
            refuse_code = (int) map.get("refuse_code");
        }
        return this;
    }

    public String toString() {
        return  "openid:" + openid +
                " uid:" + uid +
                " token:" + token +
                " nick_name:" + nick_name +
                " phone_number" + phone_number +
                " status:" + status +
                " expire_time:" + expire_time +
                " refuse_code:" + refuse_code +
                " refuse_msg:" + refuse_msg;
    }

    public String toJsonString() {
        Map<String, Object> map;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            map = new ArrayMap<>();
        } else {
            map = new HashMap<>();
        }

        map.put("openid", openid);
        map.put("uid", uid);
        map.put("token", token);
        map.put("nick_name", nick_name);
        map.put("phone_number", phone_number);
        map.put("status", status);
        map.put("expire_time", expire_time);
        map.put("refuse_code", refuse_code);
        map.put("refuse_msg", refuse_msg);
        return JsonUtils.mapToJsonString(map);
    }
}
