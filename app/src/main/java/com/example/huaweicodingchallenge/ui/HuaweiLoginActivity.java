package com.example.huaweicodingchallenge.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.huaweicodingchallenge.R;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

import domain.AuthService;

public class HuaweiLoginActivity extends AppCompatActivity {
    private static final int REQUEST_SIGN_IN_LOGIN = 3001; // Normal Login
    private HuaweiIdAuthService mAuthManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huawei_login);

        // Here we defined our button as a framelayout composed of 3 elements
//        LinearLayout signInButton = (LinearLayout) findViewById(R.id.challenge_silent_signin);
        ConstraintLayout signInButton = (ConstraintLayout) findViewById(R.id.challenge_silent_signin);
        // Adding on click listeners to the framelayout
        addOnClickListener(signInButton);

        // Setting the context for the AuthService singleton class
        AuthService.setContext(this);

        // Retrieving the Authservice instance object from the singleton
        mAuthManager = AuthService.getInstance().mAuthManager;

    }

    private void addOnClickListener(ConstraintLayout signInButton) {
        signInButton.setOnClickListener(v -> HuaweiLoginActivity.this.silentSignIn());
    }


    private void silentSignIn() {
        Task<AuthHuaweiId> task = mAuthManager.silentSignIn();
        task.addOnSuccessListener(authHuaweiId -> {
            Toast.makeText(HuaweiLoginActivity.this, "Sign in Success!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, huaweiAccount.getDisplayName() + " Sign in Success!", Toast.LENGTH_SHORT).show();
                Intent toUserActivityIntent = new Intent(HuaweiLoginActivity.this, UserActivity.class);
                passParametersToIntent(toUserActivityIntent, huaweiAccount);
                startActivity(toUserActivityIntent);
            } else {
                Toast.makeText(this, "signIn failed: " + ((ApiException) authHuaweiIdTask.getException()).getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}