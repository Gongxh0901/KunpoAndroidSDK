package com.kunpo.kunposdk;

import android.app.Activity;
import android.app.Application;

import com.kunpo.kunposdk.data.KunpoParams;
import com.kunpo.kunposdk.manager.DataManager;
import com.kunpo.kunposdk.manager.ViewManager;
import com.kunpo.kunposdk.utils.KunpoLog;

public class KunpoKit {
    private final String TAG = "KunpoKit";
    private static KunpoKit _instance;

    private Activity _activity;
    private Application _application;

    /** 对外接口的实例 */
    private LoginKit _loginKit;
    private RealNameKit _realNameKit;
    private KunpoParams _params;
    public static KunpoKit getInstance() {
        if (_instance == null) {
            _instance = new KunpoKit();
        }
        return _instance;
    }

    public void setParams(KunpoParams params) {
        _params = params;
    }

    public void init(Activity activity) {
        try {
            if (null == _params) {
                throw new Exception("请先使用 setParams 设置参数后再初始化");
            }
            _activity = activity;
            ViewManager.getInstance().init(activity);
            DataManager.getInstance().onCreate(activity);
        } catch (Exception e) {
            KunpoLog.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    public void init(Application application) {
        try {
            if (null == _params) {
                throw new Exception("请先使用 setParams 设置参数后再初始化");
            }
            _application = application;
            ViewManager.getInstance().init(application);
            DataManager.getInstance().onCreate(application);
        } catch (Exception e) {
            KunpoLog.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    public LoginKit getLoginKit() {
        if (_loginKit == null) {
            _loginKit = new LoginKit(_activity);
        }
        return _loginKit;
    }

    public RealNameKit getRealNameKit() {
        if (_realNameKit == null) {
            _realNameKit = new RealNameKit(_activity);
        }
        return _realNameKit;
    }

    public KunpoParams getKunpoParams() {
        return _params;
    }

    protected void onStart() {
        KunpoLog.d(TAG, "onStart");
    }

    protected void onRestart() {
        KunpoLog.d(TAG, "onRestart");
    }

    protected void onStop() {
        KunpoLog.d(TAG, "onStop");
    }

    protected void onResume() {
        KunpoLog.d(TAG, "onResume");
    }

    protected void onDestroy() {
        KunpoLog.d(TAG, "onDestroy");
        ViewManager.getInstance().onDestroy();
    }
}
