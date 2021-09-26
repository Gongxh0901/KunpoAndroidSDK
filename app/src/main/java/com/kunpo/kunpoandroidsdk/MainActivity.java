package com.kunpo.kunpoandroidsdk;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kunpo.kunposdk.KunpoKit;
import com.kunpo.kunposdk.data.ChannelType;
import com.kunpo.kunposdk.data.KunpoParams;
import com.kunpo.kunposdk.data.UserInfo;
import com.kunpo.kunposdk.listener.LoginListener;
import com.kunpo.kunposdk.manager.DataManager;
import com.kunpo.kunposdk.manager.ViewManager;
import com.kunpo.kunposdk.utils.ErrorInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends Activity {
    private final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KunpoParams params = new KunpoParams()
                .appid("ENwSFU")
                .appSecret("doW0gD")
                .channel(ChannelType.Android)
                .debug(true);

        KunpoKit.getInstance().setParams(params);
        // 初始化KunpoKit
        KunpoKit.getInstance().init(this);
        KunpoKit.getInstance().init(getApplication());

        initLayout();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initLayout() {
        Button buttonLoginPhone = (Button)findViewById(R.id.button_login);
        buttonLoginPhone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("点击", "1111");
                KunpoKit.getInstance().getLoginKit().login(new LoginListener() {
                    @Override
                    public void onFailure(ErrorInfo errorInfo) {
                        Log.d(TAG, "登录失败" + errorInfo.toJsonString());
                    }

                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        Log.d(TAG, "登录成功" + userInfo.toJsonString());
                    }
                });
            }
        });
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}