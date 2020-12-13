package com.example.huaweicodingchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

public class HuaweiLoginActivity extends AppCompatActivity {
    private static final int  REQUEST_SIGN_IN_LOGIN_CODE = 3000; // Login by code
    private static final int  REQUEST_SIGN_IN_LOGIN = 3001; // Normal Login
    private HuaweiIdAuthService mAuthManager;
    private HuaweiIdAuthParams mAuthParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huawei_login);

        Button signInButton = (Button) findViewById(R.id.challenge_silent_signin);
        addOnClickListener(signInButton);

        mAuthParam = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();

        mAuthManager = HuaweiIdAuthManager.getService(HuaweiLoginActivity.this, mAuthParam);

    }

    private void addOnClickListener(Button signInButton) {
        signInButton.setOnClickListener(v -> HuaweiLoginActivity.this.silentSignIn());
    }

    private void silentSignIn() {
        Task<AuthHuaweiId> task = mAuthManager.silentSignIn();
        task.addOnSuccessListener(authHuaweiId -> {
            Toast.makeText(HuaweiLoginActivity.this, "Silent SignIn success", Toast.LENGTH_SHORT).show();
            Intent toUserActivityIntent = new Intent(HuaweiLoginActivity.this, UserActivity.class);
            passParametersToIntent(toUserActivityIntent, authHuaweiId);
            startActivity(toUserActivityIntent);
        });
        task.addOnFailureListener(e -> {
            //if Failed use getSignInIntent
            if (e instanceof ApiException) {
                ApiException apiException = (ApiException) e;
                signIn();
            }
        });
    }

    private void passParametersToIntent(Intent toUserActivityIntent, AuthHuaweiId authHuaweiId) {
        toUserActivityIntent.putExtra("displayName", authHuaweiId.getDisplayName());
    }

    private void signIn() {
        startActivityForResult(mAuthManager.getSignInIntent(), REQUEST_SIGN_IN_LOGIN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_IN_LOGIN) {
            //login success
            //get user message by parseAuthResultFromIntent
            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()) {
                AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
                Toast.makeText(this, huaweiAccount.getDisplayName() + " signIn success ", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "AccessToken: " + huaweiAccount.getAccessToken(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "In onactivityResult", Toast.LENGTH_LONG).show();

//                Intent toUserActivityIntent = new Intent(HuaweiLoginActivity.this, UserActivity.class);
//                toUserActivityIntent.putExtra("account", authHuaweiId);
//
//                startActivity(toUserActivityIntent);
            } else {
                Toast.makeText(this, "signIn failed: " + ((ApiException) authHuaweiIdTask.getException()).getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}