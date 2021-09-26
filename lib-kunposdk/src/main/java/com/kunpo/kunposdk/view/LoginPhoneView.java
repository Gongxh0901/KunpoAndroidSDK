package com.kunpo.kunposdk.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.kunpo.kunposdk.KunpoKit;
import com.kunpo.kunposdk.data.UserInfo;
import com.kunpo.kunposdk.listener.LoginListener;
import com.kunpo.kunposdk.listener.VerifyCodeListener;
import com.kunpo.kunposdk.manager.DataManager;
import com.kunpo.kunposdk.manager.RequestManager;
import com.kunpo.kunposdk.manager.ViewManager;
import com.kunpo.kunposdk.utils.ContextUtils;
import com.kunpo.kunposdk.utils.ErrorInfo;
import com.kunpo.kunposdk.utils.KunpoLog;
import com.kunpo.kunposdk.utils.Utils;
import com.kunpo.lib_kunposdk.R;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 手机号 验证码登录界面
 */
public class LoginPhoneView extends BaseDialog {
    private static final String TAG = "ui/LoginPhoneView";

    private Button _btn_back;
    private Button _btn_verify;
    private Button _btn_login;
    private EditText _input_verify;
    private EditText _input_phone;
    private Timer _timer;
    private Long _countdown = 0L;
    public LoginPhoneView(Context context) {
        super(context);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.fragment_login_phone);

        // 返回按钮
        _btn_back = (Button)findViewById(R.id.button_back);
        _btn_verify = (Button)findViewById(R.id.button_get_verify_code);
        _btn_login = (Button)findViewById(R.id.button_login);
        _input_phone = (EditText) findViewById(R.id.input_phone);
        _input_verify = (EditText) findViewById(R.id.input_verify_code);

        _initButtonClick();
        _initInputPhone();

        _tryStartTimer();
    }

    private void _initButtonClick() {

        final Context context = this.getContext();

        _btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ViewManager.getInstance().closeTopDialog();
            }
        });

        _btn_verify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KunpoLog.d(TAG, "获取验证码");
                String phoneNumber = _input_phone.getText().toString();
                if (!Utils.isPhoneNumber(phoneNumber)) {
                    ContextUtils.showToast(context, "请输入正确的手机号", Gravity.CENTER);
                    return;
                }
                // 手机号正确
                KunpoLog.d(TAG, "手机号:" + phoneNumber);
                RequestManager.getInstance().getVerifyCode(phoneNumber, 1, new VerifyCodeListener() {
                    public void onFailure(ErrorInfo errorInfo) {
                        // 获取验证码失败
                        ContextUtils.showToast(context, "验证码获取失败", Gravity.CENTER);
                    }
                    public void onSuccess(Map<String, Object> mapResult) {
                        _countdown = Long.valueOf(String.valueOf(mapResult.get("countdown")));
                        DataManager.getInstance().runingData.verify_keepto = Utils.timestamp() + _countdown;
                        _tryStartTimer();
                    }
                });
            }
        });

        _btn_login.setEnabled(Utils.isPhoneNumber(_input_phone.getText().toString())); // 设置按钮是否可点击
        _btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KunpoLog.d(TAG, "登录");
                if ("".equals(_input_verify.getText().toString())) {
                    ContextUtils.showToast(context, "请输入验证码", Gravity.CENTER);
                    return;
                }
                String phoneNumber = _input_phone.getText().toString();
                if (!Utils.isPhoneNumber(phoneNumber)) {
                    ContextUtils.showToast(context, "请输入正确的手机号", Gravity.CENTER);
                    return;
                }
                ContextUtils.showProgressDialog(context, "登录中...");
                RequestManager.getInstance().loginPhoneNumber(phoneNumber, _input_verify.getText().toString(), new LoginListener() {
                    public void onFailure(ErrorInfo errorInfo) {
                        KunpoLog.d(TAG, "登录失败:" + errorInfo.toJsonString());
                        ContextUtils.cancelProgressDialog(context);
                        ContextUtils.showToast(context, "登录失败:" + errorInfo.toJsonString());
                        KunpoKit.getInstance().getLoginKit().getListener().onFailure(errorInfo);
                    }
                    public void onSuccess(UserInfo userInfo) {
                        ContextUtils.cancelProgressDialog(context);
                        ContextUtils.showToast(context, "登录成功");

                        ViewManager.getInstance().closeAllDialog(); // 关闭所有dialog
                        KunpoKit.getInstance().getLoginKit().getListener().onSuccess(userInfo);
                    }
                });
            }
        });
    }

    private void _initInputPhone() {
        _input_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable text) {
                _btn_login.setEnabled(Utils.isPhoneNumber(text.toString())); // 设置按钮是否可点击
            }
        });
    }

    /**
     * 尝试开启定时器，刷新获取验证码按钮
     */
    private void _tryStartTimer() {
        _countdown = DataManager.getInstance().runingData.verify_keepto - Utils.timestamp();
        if (_timer != null || _countdown <= 0) {
            return;
        }
        _btn_verify.setEnabled(false);
        _timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                _countdown--;
                if (_countdown <= 0) {
                    _timer.cancel();
                    _timer = null;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            _btn_verify.setEnabled(true);
                            _btn_verify.setText(R.string.get_verify_code);
                        }
                    });
                } else {
                    // 切到主线程，修改按钮标题文本
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            _btn_verify.setText(String.valueOf(_countdown));
                        }
                    });
                }
            }
        };
        _timer.schedule(timerTask, 0, 1000); //立刻执行，间隔1秒循环执行
    }

    protected void onStop() {
        super.onStop();
        if (null != _timer) {
            _timer.cancel();
            _timer = null;
        }
    }
}
