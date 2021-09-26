package com.kunpo.kunposdk.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.kunpo.kunposdk.manager.ViewManager;
import com.kunpo.kunposdk.utils.KunpoLog;
import com.kunpo.lib_kunposdk.R;

/**
 * 登录初始界面
 */
public class LoginView extends BaseDialog {
    private static final String TAG = "ui/LoginView";
    public LoginView(Context context) {
        super(context);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.fragment_login);

        Button buttonLoginPhone = (Button)findViewById(R.id.button_login);
        Button buttonQQ = (Button)findViewById(R.id.button_qq);
        Button buttonWeixin = (Button)findViewById(R.id.button_weixin);

        buttonLoginPhone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KunpoLog.d(TAG, "手机号登录");
                ViewManager.getInstance().createDialog(DialogType.LoginPhoneView);
            }
        });

        //QQ登录
        buttonQQ.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KunpoLog.d(TAG, "QQ登录");
            }
        });
        //微信登录
        buttonWeixin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KunpoLog.d(TAG, "微信登录");
            }
        });
    }
}
