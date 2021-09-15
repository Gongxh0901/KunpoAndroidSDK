package com.kunpo.kunposdk;

import android.app.Activity;
import android.app.Application;

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

    public static KunpoKit getInstance() {
        if (_instance == null) {
            _instance = new KunpoKit();
        }
        return _instance;
    }

    public void init(Activity activity) {
        _activity = activity;
        ViewManager.getInstance().init(activity);
    }

    public void init(Application application) {
        _application = application;
        ViewManager.getInstance().init(application);
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
