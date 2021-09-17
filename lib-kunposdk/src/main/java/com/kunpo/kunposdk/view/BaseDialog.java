package com.kunpo.kunposdk.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kunpo.kunposdk.utils.ResUtils;

public class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        super(context, android.R.style.Theme_Dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        if (window != null) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
