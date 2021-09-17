package com.kunpo.kunposdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class ResUtils {
//    /**
//     * R.anim
//     * @param context
//     * @param name
//     * @return
//     */
//    public static int getAnim(Context context, String name) {
//        return context.getResources().getIdentifier(name, "anim", context.getPackageName());
//    }
//
//    /**
//     * R.attr
//     * @param context
//     * @param name
//     * @return
//     */
//    public static int getAttr(Context context, String name) {
//        return context.getResources().getIdentifier(name, "attr", context.getPackageName());
//    }
//
//    /**
//     * R.layout
//     * @param context
//     * @param name
//     * @return
//     */
//    public static int getLayout(Context context, String name) {
//        return context.getResources().getIdentifier(name, "layout", context.getPackageName());
//    }
//
//    /**
//     * R.string
//     * @param context
//     * @param name
//     * @return
//     */
//    public static int getString(Context context, String name) {
//        return context.getResources().getIdentifier(name, "string", context.getPackageName());
//    }
//
//    /**
//     * R.drawable
//     * @param context
//     * @param name
//     * @return
//     */
//    public static int getDrawable(Context context, String name) {
//        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
//    }
//
//    /**
//     * R.style
//     * @param context
//     * @param name
//     * @return
//     */
//    public static int getStyle(Context context, String name) {
//        return context.getResources().getIdentifier(name, "style", context.getPackageName());
//    }
//
//    /**
//     * R.styleable
//     * @param context
//     * @param name
//     * @return
//     */
//    public static int getStyleable(Context context, String name) {
//        return context.getResources().getIdentifier(name, "styleable", context.getPackageName());
//    }
//
//    /**
//     * R.id
//     * @param context
//     * @param name
//     * @return
//     */
//    public static int getId(Context context, String name) {
//        return context.getResources().getIdentifier(name, "id", context.getPackageName());
//    }
//
//    /**
//     * R.color
//     * @param context
//     * @param name
//     * @return
//     */
//    public static int getColor(Context context, String name) {
//        return context.getResources().getIdentifier(name, "color", context.getPackageName());
//    }

    /**
     * 获取 activity meta-data
     * @param context
     * @return
     */
    public static Bundle getActivityMetaData(Activity context) {
        try {
            ActivityInfo info = context.getPackageManager()
                    .getActivityInfo(context.getComponentName(), PackageManager.GET_META_DATA);
            return info.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 application meta-data
     * @param context
     * @return
     */
    public static Bundle getApplicationMetaData(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
