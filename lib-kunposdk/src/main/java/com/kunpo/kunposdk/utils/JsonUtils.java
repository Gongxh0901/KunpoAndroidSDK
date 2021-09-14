package com.kunpo.kunposdk.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {
    public static String mapToJsonString(Map<String, Object> jsonMap) {
        try {
            if (jsonMap != null) {
                JSONObject jsonObj = new JSONObject();
                for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                    jsonObj.put(entry.getKey(), entry.getValue());
                }
                return jsonObj.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String listToJsonString(List<Object> jsonList) {
        try {
            if (jsonList != null) {
                JSONArray jsonArr = new JSONArray();
                for (int i = 0; i < jsonList.size(); i++) {
                    jsonArr.put(jsonList.get(i));
                }
                return jsonArr.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> jsonStringToMap(String jsonString) {
        try {
            if (!Utils.isNullOrEmpty(jsonString)) {
                JSONObject jsonObj = new JSONObject(jsonString);
                return jsonObjectToMap(jsonObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> jsonObjectToMap(JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                Map<String, Object> retMap = new HashMap<>();
                Iterator<String> it = jsonObject.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    Object value = jsonObject.get(key);
                    if (value instanceof JSONObject) {
                        Map<String, Object> objMap = jsonObjectToMap((JSONObject) value);
                        retMap.put(key, objMap);
                    } else if (value instanceof JSONArray) {
                        List<Object> objList = jsonArrayToList((JSONArray) value);
                        retMap.put(key, objList);
                    } else {
                        retMap.put(key, value);
                    }
                }
                return retMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Object> jsonArrayToList(JSONArray jsonArray) {
        try {
            if (jsonArray != null) {
                List<Object> retList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object value = jsonArray.get(i);
                    if (value instanceof JSONObject) {
                        Map<String, Object> objMap = jsonObjectToMap((JSONObject) value);
                        retList.add(objMap);
                    } else if (value instanceof JSONArray) {
                        List<Object> objList = jsonArrayToList((JSONArray) value);
                        retList.add(objList);
                    } else {
                        retList.add(value);
                    }
                }
                return retList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
