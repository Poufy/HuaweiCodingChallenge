package com.example.huaweicodingchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class HuaweiLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int  REQUEST_SIGN_IN_LOGIN_CODE = 3000; // Login by code
    private static final int  REQUEST_SIGN_IN_LOGIN = 3001; // Normal Login
    private HuaweiIdAuthService mAuthManager;
    private HuaweiIdAuthParams mAuthParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huawei_login);
        findViewById(R.id.challenge_silent_signin).setOnClickListener(this);
        findViewById(R.id.challenge_signout).setOnClickListener(this);
        findViewById(R.id.challenge_signIn).setOnClickListener(this);
    }

    private void signIn() {
        mAuthParam = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();
        Log.e("manager", "setting up manager");
        mAuthManager = HuaweiIdAuthManager.getService(HuaweiLoginActivity.this, mAuthParam);
        startActivityForResult(mAuthManager.getSignInIntent(), REQUEST_SIGN_IN_LOGIN);
    }

    private void signOut() {
        if (null != mAuthManager) {
            Task<Void> signOutTask = mAuthManager.signOut();
            signOutTask.addOnSuccessListener(aVoid -> Toast.makeText(HuaweiLoginActivity.this, "SignOut Success", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(HuaweiLoginActivity.this, "signOut fail", Toast.LENGTH_SHORT).show());
        }
    }

    private void silentSignIn() {
        Task<AuthHuaweiId> task = mAuthManager.silentSignIn();
        task.addOnSuccessListener(new OnSuccessListener<AuthHuaweiId>() {
            @Override
            public void onSuccess(AuthHuaweiId authHuaweiId) {
                Toast.makeText(HuaweiLoginActivity.this, "Silent SignIn success", Toast.LENGTH_SHORT).show();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                //if Failed use getSignInIntent
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    signIn();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.challenge_silent_signin:
                silentSignIn();
                break;
            case R.id.challenge_signIn:
                signIn();
                break;
            case R.id.challenge_signout:
                signOut();
                break;
        }
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
            } else {
                Toast.makeText(this, "signIn failed: " + ((ApiException) authHuaweiIdTask.getException()).getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_SIGN_IN_LOGIN_CODE) {
            //login success
            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()) {
                AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
                Toast.makeText(this, "signIn get code success.", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "ServerAuthCode: " + huaweiAccount.getAuthorizationCode(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "signIn get code failed: " + ((ApiException) authHuaweiIdTask.getException()).getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}