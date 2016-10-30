package com.example.alexey.bromochat;


import android.content.Intent;

import com.example.alexey.bromochat.activity.SplashActivity;
import com.quickblox.core.QBSettings;

public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();

        QBSettings.getInstance().init(this, Consts.QB_APP_ID, Consts.QB_AUTH_KEY, Consts.QB_AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Consts.QB_ACCOUNT_KEY);

        Intent intent = new Intent(Application.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
