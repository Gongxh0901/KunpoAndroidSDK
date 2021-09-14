package com.kunpo.kunpoandroidsdk;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kunpo.kunposdk.KunpoKit;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化KunpoKit
        KunpoKit.getInstance().init(this);

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
                KunpoKit.getInstance().getLoginKit().loginPhone();
            }
        });
    }
}