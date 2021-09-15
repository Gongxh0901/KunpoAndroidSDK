package com.kunpo.kunposdk.manager;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.kunpo.kunposdk.view.DialogType;
import com.kunpo.kunposdk.view.LoginPhoneView;
import com.kunpo.kunposdk.view.LoginView;

import java.util.Stack;

public class ViewManager {
    private static ViewManager _instance;

    private Activity _activity;
    private Application _application;
    private Stack<Dialog> _stack;
    public static ViewManager getInstance() {
        if (_instance == null) {
            _instance = new ViewManager();
        }
        return _instance;
    }

    public void init(Activity activity) {
        _activity = activity;
    }

    public void init(Application application) {
        _application = application;
    }

    private ViewManager() {
        _stack = new Stack<>();
    }

    public void closeTopDialog() {
        if (_stack.empty()) {
            return;
        }
        Dialog dialog = _stack.pop();
        dialog.dismiss();
        showTopDialog();
    }

    public void createDialog(DialogType dialogType) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Dialog view = null;
                if (dialogType == DialogType.LoginView) {
                    view = new LoginView(_activity);
                } else if (dialogType == DialogType.LoginPhoneView) {
                    view = new LoginPhoneView(_activity);
                }
                addDialog(view);
                view.show();
            }
        });
    }

    private void showTopDialog() {
        if (_stack.empty()) {
            return;
        }
        Dialog dialog = _stack.peek();
        dialog.show();
    }

    private void hideTopDialog() {
        if (_stack.empty()) {
            return;
        }
        Dialog dialog = _stack.peek();
        dialog.hide();
    }

    private void addDialog(Dialog dialog) {
        hideTopDialog();
        _stack.push(dialog);
    }
}
