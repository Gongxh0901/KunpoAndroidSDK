package com.kunpo.kunposdk.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.kunpo.lib_kunposdk.R;

/**
 * Created by KimCh.
 */
public class ContextUtils {
    /**
     *
     */
    private static ProgressDialog _globalProgressDialog;

    /**
     * @param runnable
     */
    public static void runOnMainLooper(Runnable runnable) {

        new Handler(Looper.getMainLooper()).post(runnable);
    }

    /**
     * 打开网址
     *
     * @param url
     */
    public static void openUrl(Context context, String url) {
        if (Utils.isNullOrEmpty(url)) {
            return;
        }
        if (context != null) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            context.startActivity(intent);
        }
    }

    /**
     * 消息提示框
     * @param title
     * @param message
     */
    public static void showDialog(final Context context, final String title, final String message) {
        showDialog(context, title, message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 消息提示框
     * @param title
     * @param message
     * @param clickListener
     */
    public static void showDialog(final Context context, final String title, final String message, final DialogInterface.OnClickListener clickListener) {
        runOnMainLooper(new Runnable() {
            public void run() {
                if (null!=context){
                    AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
                    localBuilder.setTitle(title);
                    localBuilder.setMessage(message);
                    localBuilder.setCancelable(false);
                    localBuilder.setPositiveButton(R.string.gotit, clickListener);
                    localBuilder.show();
                }

            }
        });
    }

    /**
     * 显示toast提示
     * @param context
     * @param text 文本
     */
    public static void showToast(final Context context, final String text) {
        showToast(context, text, Gravity.BOTTOM);
    }

    /**
     * 显示toast提示
     * @param context
     * @param text
     * @param {Gravity.CENTER} gravity 位置
     */
    public static void showToast(final Context context, final String text, int gravity) {
        showToast(context, text, gravity, 0, 0);
    }

    public static void showToast(final Context context, final String text, int gravity, int offsetx, int offsety) {
        runOnMainLooper(new Runnable() {
            public void run() {

                if (null!=context){
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.setGravity(gravity, offsetx, offsety);
                    toast.show();
                }
            }
        });
    }

    /**
     * 显示toast提示
     * @param context
     * @param text 文本
     */
    public static void showLongToast(final Context context, final String text) {
        showToast(context, text, Gravity.BOTTOM);
    }

    /**
     * 显示toast提示
     * @param context
     * @param text
     * @param {Gravity.CENTER} gravity 位置
     */
    public static void showLongToast(final Context context, final String text, int gravity) {
        showToast(context, text, gravity, 0, 0);
    }

    public static void showLongToast(final Context context, final String text, int gravity, int offsetx, int offsety) {
        runOnMainLooper(new Runnable() {
            public void run() {

                if (null!=context){
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                    toast.setGravity(gravity, offsetx, offsety);
                    toast.show();
                }
            }
        });
    }

    /**
     * 取消进度条
     *
     * @param context
     */
    public static void cancelProgressDialog(final Context context) {
        if (_globalProgressDialog != null) {
            runOnMainLooper(new Runnable() {
                public void run() {
                    //增加非null判断
                    if (null!=_globalProgressDialog){
                        _globalProgressDialog.cancel();
                        _globalProgressDialog.hide();
                        _globalProgressDialog = null;
                    }
                }
            });
        }
    }

    /**
     * 显示进度条
     *
     * @param context
     * @param message
     */
    public static void showProgressDialog(final Context context, final String message) {
        cancelProgressDialog(context);
        runOnMainLooper(new Runnable() {
            public void run() {
                if (null!=context){
                    _globalProgressDialog = new ProgressDialog(context);
                    _globalProgressDialog.setMessage(message);
                    _globalProgressDialog.setCanceledOnTouchOutside(false);
                    _globalProgressDialog.setCancelable(false);
                    _globalProgressDialog.show();
                }
            }
        });
    }
}
