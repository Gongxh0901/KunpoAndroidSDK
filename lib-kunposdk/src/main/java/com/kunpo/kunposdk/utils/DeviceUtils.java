package com.kunpo.kunposdk.utils;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import androidx.core.app.ActivityCompat;
import java.util.UUID;
import com.kunpo.kunposdk.manager.DataManager;

public class DeviceUtils {
    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_2G = 2;
    public static final int NETWORK_3G = 3;
    public static final int NETWORK_4G = 4;
    public static final int NETWORK_5G = 5;
    public static final int NETWORK_MOBILE = 6;

    /**
     * 获取手机号
     * @return
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        ) {
            // 如果当前没有权限，动态申请权限
            String[] PERMISSIONS_PHONENUMBER = {
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_SMS
            };
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                DataManager.getInstance().getActivity().requestPermissions(PERMISSIONS_PHONENUMBER, Constant.PERMISSIONS_PHONENUMBER);
            }
            return tm.getLine1Number();
        }
        String pNumber = tm.getLine1Number();//获取本机号码
        return pNumber;
    }

    /**
     * 获取运营商类型 ex.移动,联通,电信
     * @return
     */
    public static String getCarrierType() {
        String mobileType = "未知";
        try {
            Application application = DataManager.getInstance().getApplication();
            TelephonyManager iPhoneManager = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
            String iNumeric = iPhoneManager.getSimOperator();

            if (iNumeric.length() > 0) {
                if (iNumeric.equals("46000") || iNumeric.equals("46002") || iNumeric.equals("46007") || iNumeric.equals("46020")) {
                    mobileType = "中国移动";
                } else if (iNumeric.equals("46001") || iNumeric.equals("46006")) {
                    mobileType = "中国联通";
                } else if (iNumeric.equals("46003") || iNumeric.equals("46005") || iNumeric.equals("46011")) {
                    mobileType = "中国电信";
                }
            }
        } catch (Exception exception) { }
        return mobileType;
    }

    /**
     * 网络类型
     * @return NetType
     */
    public static int getNetworkType() {
        // 获取系统的网络服务
        Application application = DataManager.getInstance().getApplication();
        ConnectivityManager connectivityManager = ((ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE));
        // 如果当前没有网络
        if (null == connectivityManager) {
            return NETWORK_NONE;
        }
        // 获取当前网络类型，如果为空，返回无网络
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            return NETWORK_NONE;
        }
        // 判断连接的是不是wifi
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                return NETWORK_WIFI;
            }
        }
        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            String strSubTypeName = networkInfo.getSubtypeName();
            int networkType = networkInfo.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return NETWORK_2G;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    return NETWORK_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    return NETWORK_4G;
                default:
                    if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                        return NETWORK_3G;
                    } else {
                        return NETWORK_MOBILE;
                    }
            }
        }
        return NETWORK_NONE;
    }

    public static String getNetName() {
        int netType = getNetworkType();
        switch (netType) {
            case NETWORK_WIFI:
                return "WIFI";
            case NETWORK_5G:
                return "5G";
            case NETWORK_4G:
                return "4G";
            case NETWORK_3G:
                return "3G";
            case NETWORK_2G:
                return "2G";
            case NETWORK_MOBILE:
                return "MONILE";
        }
        return "UNKNOWN";
    }

    /** 版本号 */
    public static String getVersionCode() {
        String localVersion = "0.0.1";
        try {
            Application application = DataManager.getInstance().getApplication();
            PackageManager packageManager = application.getApplicationContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(application.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /** build 号 */
    public static int getBuildCode() {
        int localVersion = 0;
        try {
            Application application = DataManager.getInstance().getApplication();
            PackageManager packageManager = application.getApplicationContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(application.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 屏幕宽度
     * @return widthPixels
     */
    public static int getWidthPixels() {
        try {
            Application application = DataManager.getInstance().getApplication();
            WindowManager wm = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metric = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metric);
            return metric.widthPixels;
        } catch (Exception e) {}
        return 0;
    }

    /**
     * 屏幕高度
     * @return widthPixels
     */
    public static int getHeightPixels() {
        try {
            Application application = DataManager.getInstance().getApplication();
            WindowManager wm = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metric = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metric);
            return metric.heightPixels;
        } catch (Exception e) {}
        return 0;
    }

    /**
     * 获取当前手机系统版本号
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取设备品牌
     * @return  设备品牌
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取设备制造商
     * @return  设备制造商
     */
    public static String getDeviceManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取操作系统
     * @return  操作系统
     */
    public static String getOS() {
        return "Android";
    }

    /**
     * 获取操作系统版本 eg: Android 4.1.5
     * @return
     */
    public static String getOSVersionName() {
        return "Android " + android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取设备ID
     * @return 设备码（不是真正的 deviceId）
     */
    public static String getDeviceId() {
        return getUniquePsuedoID();
    }

    private static String getUniquePsuedoID() {
        String serial;
        String szDevIDShort = "123" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 + Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 + Build.USER.length() % 10; //13 位
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            return "";
        }
    }
}
