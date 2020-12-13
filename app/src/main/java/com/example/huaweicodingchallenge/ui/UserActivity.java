package com.example.huaweicodingchallenge.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.huaweicodingchallenge.R;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

import domain.AuthService;

public class UserActivity extends AppCompatActivity {
    private HuaweiIdAuthService mAuthManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);



        TextView userDisplayName = (TextView)findViewById(R.id.userDispalyName);
        Button signOutBtn = (Button) findViewById(R.id.challenge_signout);
        addButtonListener(signOutBtn);

        AuthService.setContext(this);
        mAuthManager = AuthService.getInstance().mAuthManager;

        Bundle parametersBundle = getIntent().getExtras();
        userDisplayName.setText(parametersBundle.get("displayName").toString());
    }


    private void addButtonListener(Button signOutBtn){
        signOutBtn.setOnClickListener(v -> UserActivity.this.signOut());
    }

    private void signOut() {
        if (mAuthManager != null) {
            Task<Void> signOutTask = mAuthManager.signOut();
            signOutTask.addOnSuccessListener(aVoid ->
                    finish())
                    .addOnFailureListener(e -> Toast.makeText(UserActivity.this, "Logout Failure", Toast.LENGTH_SHORT).show());
        }
    }
}