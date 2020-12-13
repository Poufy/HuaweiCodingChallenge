package com.example.huaweicodingchallenge.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.huaweicodingchallenge.R;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, HuaweiLoginActivity.class));
                finish();
            }
        }, 5000);

    }
}