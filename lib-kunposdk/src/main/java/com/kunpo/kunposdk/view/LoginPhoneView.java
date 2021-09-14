package com.kunpo.kunposdk.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.kunpo.kunposdk.utils.KunpoLog;
import com.kunpo.lib_kunposdk.R;

/**
 * 手机号 验证码登录界面
 */
public class LoginPhoneView extends BaseDialog {
    private static final String TAG = "ui/LoginView";

    public static void openLoginView(Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                LoginPhoneView loginView = new LoginPhoneView(context);
                loginView.show();
            }
        });
    }

    private LoginPhoneView(Context context) {
        super(context);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.fragment_login_phone);

        Button buttonBack = (Button)findViewById(R.id.button_back);
//        Button buttonQQ = (Button)findViewById(R.id.button_qq);
//        Button buttonWeixin = (Button)findViewById(R.id.button_weixin);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KunpoLog.d(TAG, "返回上一个界面");
            }
        });

//        //QQ登录
//        buttonQQ.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                KunpoLog.d(TAG, "QQ登录");
//            }
//        });
//        //微信登录
//        buttonWeixin.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                KunpoLog.d(TAG, "微信登录");
//            }
//        });

//        tv_title = (TextView) findViewById("tv_real_title");
//        tv_tip = (TextView) findViewById("tv_real_tip");
//
//        et_name = (EditText) findViewById("et_name");
//        et_name.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String name = et_name.getText().toString();
//                String reg = "[^\u4E00-\u9FA5]";
//                String valid = Pattern.compile(reg).matcher(name).replaceAll("").trim();
//                if (!TextUtils.equals(name, valid)) {
//                    et_name.setText(valid);
//                    et_name.setSelection(valid.length());
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }
}
