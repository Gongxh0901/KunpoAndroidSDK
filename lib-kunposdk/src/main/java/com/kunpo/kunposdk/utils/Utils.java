package com.kunpo.kunposdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String TAG = "Utils/Utils";

    public static boolean isNullOrEmpty(String value) {
        return (value == null) || (value.length() == 0);
    }

    /** md5 工具 */
    public static String getMD5(String text) {
        byte[] bytes = text.getBytes();
        StringBuffer tmpSBuffer;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(bytes);
            byte[] bytes2 = md5.digest();
            tmpSBuffer = new StringBuffer();
            for (int i = 0; i < bytes2.length; i++) {
                if ((0xFF & bytes2[i]) < 16) {
                    tmpSBuffer.append("0");
                }
                tmpSBuffer.append(Long.toString(0xFF & bytes2[i], 16));
            }
        } catch (Exception exception) {
            return text;
        }
        return tmpSBuffer.toString();
    }

    public static boolean isEmptyString(String code) {
        return TextUtils.isEmpty(code) || "null".equalsIgnoreCase(code);
    }

    /** 是否手机号格式 */
    public static boolean isPhoneNumber(String value) {
        if (value != null && value.matches("^[1][0-9]{2}[0-9]{8}$")) {
            return true;
        } else {
            return false;
        }
    }

    /** 判断邮箱格式 */
    public static boolean isEmailAddress(String email) {
        String s = null;
        if (!Utils.isEmptyString(email)) {
            s = email.replace(" ", "");
            String str = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
            Pattern p = Pattern.compile(str);
            Matcher m = p.matcher(s);
            return m.matches();
        }
        return false;
    }

    /** 验证码 */
    public static boolean isVerificationCode(String value) {
        if (value != null && value.matches("^[0-9]{4}$")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param value 6 ~ 20位
     * @return
     */
    public static boolean isPassword(String value) {
        if (value != null && value.length() >= 6 && value.length() <= 20) {
            return true;
        } else {
            return false;
        }
    }

    public static String getmetadataid(Context _context) {
        ApplicationInfo info = null;
        try {
            info = _context.getPackageManager().getApplicationInfo(_context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String msg = info.metaData.getString("app_id");
        KunpoLog.d(TAG, " msg == " + msg);
        return msg;
    }

    public static String getmetadatakey(Context _context) {
        ApplicationInfo info = null;
        try {
            info = _context.getPackageManager()
                    .getApplicationInfo(_context.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String msg = info.metaData.getString("app_key");
        KunpoLog.d(TAG, " msg == " + msg);
        return msg;
    }

    public static String getmetadatasecret(Context _context) {
        ApplicationInfo info = null;
        try {
            info = _context.getPackageManager()
                    .getApplicationInfo(_context.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String msg = info.metaData.getString("app_secret");
        KunpoLog.d(TAG, " msg == " + msg);
        return msg;


    }

    //获取当前地区  1 国内；2 国外
    public static int getCurrentLocal() {

        int flag = 1;

        String local = Locale.getDefault().getCountry();
        if (!local.equals("CN")) {
            flag = 2;
        }
        return flag;
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /** 隐藏虚拟键 */
    public static void hideNavigation(Activity context) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = context.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = context.getWindow().getDecorView();
            hideNavigation(decorView);
        }
    }

    /** 隐藏虚拟键 */
    public static void hideNavigation(View view) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            view.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            view.setSystemUiVisibility(uiOptions);
        }
    }

    public static void saveLog(String string) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            //如果文件存在则删除文件
            file = new File(Environment.getExternalStorageDirectory(), "logtxt.txt");
            if(file.exists()){
                String logtxt = getFile("logtxt.txt");
                string = logtxt +"\n"+ string;
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(string.getBytes()); //把需要保存的文件保存到SD卡中
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String getFile(String fileName) {
        try {
            // 创建文件
            File file = new File(Environment.getExternalStorageDirectory(),fileName);
            FileInputStream fis = new FileInputStream(file);
            byte[] b = new byte[1024]; // 创建字节数组 每次缓冲1k
            int len = 0;// 一次读取1024字节大小，没有数据后返回-1.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 一次读取1024个字节，然后往字符输出流中写读取的字节数
            while ((len = fis.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            byte[] data = baos.toByteArray(); // 将读取的字节总数生成字节数组
            baos.close(); // 关闭字节输出流
            fis.close(); // 关闭文件输入流
            return new String(data); // 返回字符串对象
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 时间戳
     * @return long (秒)
     */
    public static long timestamp() {
        return System.currentTimeMillis() / 1000;
    }
}
