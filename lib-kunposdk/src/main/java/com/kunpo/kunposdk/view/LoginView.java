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
 * 登录初始界面
 */
public class LoginView extends BaseDialog {
    private static final String TAG = "ui/LoginView";

    public static void openLoginView(Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                LoginView loginView = new LoginView(context);
                loginView.show();
            }
        });
    }

    private LoginView(Context context) {
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
