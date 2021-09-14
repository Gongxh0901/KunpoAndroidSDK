package com.kunpo.kunposdk.utils;
import android.util.Log;

public class KunpoLog {
    private static final String TAG = "KunpoLog";
    public static void d(String var1, String var2) {
        if (Constant.DEBUG) {
            Log.d(TAG, var1 + ": " + var2);
        }
    }

    public static void e(String var1, String var2) {
        if (Constant.DEBUG) {
            Log.e(TAG, var1 + ": " + var2);
        }
    }

    public static void i(String var1, String var2) {
        if (Constant.DEBUG) {
            Log.i(TAG, var1 + ": " + var2);
        }
    }

    public static void w(String var1, String var2) {
        if (Constant.DEBUG) {
            Log.w(TAG, var1 + ": " + var2);
        }
    }
}
