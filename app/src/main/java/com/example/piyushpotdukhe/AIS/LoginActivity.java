package com.example.piyushpotdukhe.AIS;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.piyushpotdukhe.AIS.StoxInfo.GetStocksInfo;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.piyushpotdukhe.AIS.com.stox.userinfo.AuthenticatedUserInfo.getAuthUserObject;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static FirebaseUser mUser;

    private static final String PATH_TOS = "";
    private static final int RC_SIGN_IN = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                    getAuthUserObject().setIsLoggedIn(true);
                    getAuthUserObject().setUserName(mUser.getDisplayName());
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid()
                            + "name=" + mUser.getDisplayName());
                } else {
                    // User is signed out
                    getAuthUserObject().setIsLoggedIn(false);
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        if (isUserAlreadyLoggedIn()) {
            launchStocksInfoActivity();
        }
    }

     public void onClickLoginButton(View view) {
        Snackbar.make(view, "logging in ...", Snackbar.LENGTH_LONG)/*
                .setAction("Action", null)*/.show();

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setTosUrl(PATH_TOS)
                .build(), RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
//                getAuthUserObject().setIsLoggedIn(true);
                loginUser();
            }
            if(resultCode == RESULT_CANCELED){
                displayMessage("Sign-in: FAILED");
            }
            return;
        }
        displayMessage("Sign-in: UNKNOWN RESPONSE");
    }

    private boolean isUserAlreadyLoggedIn(){
        return (getAuthUserObject().getIsLoggedIn());
    }

    private void loginUser(){
        displayMessage("Sign-in: SUCCESS");
        launchStocksInfoActivity();
    }

    private void launchStocksInfoActivity() {
        Intent afterLoginSuccess = new Intent(LoginActivity.this, GetStocksInfo.class);
        finish();
        startActivity(afterLoginSuccess);
    }

    private void displayMessage(String message){
        Snackbar.make(getWindow().getDecorView().getRootView(),
                message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
