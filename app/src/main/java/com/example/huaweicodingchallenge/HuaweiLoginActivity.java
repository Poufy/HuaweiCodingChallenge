package com.example.huaweicodingchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

public class HuaweiLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huawei_login);
    }


    private void requestHuaweiAuth() {
        HuaweiIdAuthParams authParams = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setIdToken().createParams();

        HuaweiIdAuthService service = HuaweiIdAuthManager.getService(HuaweiLoginActivity.this, authParams);

        startActivityForResult(service.getSignInIntent(), 8888);
    }
}