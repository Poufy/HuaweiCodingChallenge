package com.example.huaweicodingchallenge.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huaweicodingchallenge.R;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int LAUNCH_SCREEN_DELAY = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView padLockLogo = findViewById(R.id.padLockLogo);
        TextView logoText = findViewById(R.id.logoText);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, HuaweiLoginActivity.class));
                finish();
            }
        }, LAUNCH_SCREEN_DELAY);

        padLockLogo.animate().translationY(270).setInterpolator(new DecelerateInterpolator()).setDuration(1500);
        logoText.animate().translationY(-270).setInterpolator(new DecelerateInterpolator()).setDuration(1500);

    }
}