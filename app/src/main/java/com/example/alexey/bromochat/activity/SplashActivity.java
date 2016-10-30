package com.example.alexey.bromochat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.alexey.bromochat.PrefsHelper;
import com.example.alexey.bromochat.R;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;


public class SplashActivity extends Activity implements Animation.AnimationListener{

    PrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefsHelper = PrefsHelper.getPrefsHelper(getApplicationContext());

        ImageView animView = (ImageView)findViewById(R.id.animView);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        animView.setAnimation(animation);
        animation.setAnimationListener(this);
    }

    private void checkUsersData(String userEmail, String userPassword) {
        boolean isEmailEntered = !TextUtils.isEmpty(userEmail);
        boolean isPasswordEntered = !TextUtils.isEmpty(userPassword);
        if (isEmailEntered && isPasswordEntered) {
            doAutoLogin(userEmail, userPassword);
        } else {
            startLogin();
        }
    }

    private void doAutoLogin(String userEmail, String userPassword) {

        final QBChatService chatService = QBChatService.getInstance();

        final QBUser user = new QBUser(null, userPassword, userEmail);
        QBAuth.createSession(user, new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                user.setId(qbSession.getUserId());

                chatService.login(user, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        startDialogs();
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.d("LOGIN", e.getMessage());
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("SESSION", e.getMessage());
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //startLogin();
        if(!prefsHelper.isPrefExists(PrefsHelper.PREF_USER_EMAIL) ||
                !prefsHelper.isPrefExists(PrefsHelper.PREF_USER_PASSWORD)) {
            startLogin();
            finish();
        }

        String userEmail = prefsHelper.getPref(PrefsHelper.PREF_USER_EMAIL);
        String userPassword = prefsHelper.getPref(PrefsHelper.PREF_USER_PASSWORD);

        boolean isRememberMe = prefsHelper.getPref(PrefsHelper.PREF_REMEMBER_ME, false);

        if (isRememberMe) {
            checkUsersData(userEmail, userPassword);
        } else {
            startLogin();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void startLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void startDialogs() {
        Intent intent = new Intent(SplashActivity.this, DialogsActivity.class);
        startActivity(intent);
    }
}
