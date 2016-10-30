package com.example.alexey.bromochat;

import android.content.Intent;
import android.os.Bundle;

import com.example.alexey.bromochat.activity.DialogsActivity;
import com.example.alexey.bromochat.activity.LoginActivity;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;


public class ChatHelper {

    private static final int CHAT_SOCKET_TIMEOUT = 0;

    private static ChatHelper instance;

    private QBChatService qbChatService;

    public static synchronized ChatHelper getInstance() {
        if (instance == null) {
            QBChatService.setDebugEnabled(true);
            QBChatService.setConfigurationBuilder(buildChatConfigs());
            instance = new ChatHelper();
        }
        return instance;
    }

    private ChatHelper() {
        qbChatService = QBChatService.getInstance();
        qbChatService.setUseStreamManagement(true);
    }

    private static QBChatService.ConfigurationBuilder buildChatConfigs(){
        QBChatService.ConfigurationBuilder configurationBuilder = new QBChatService.ConfigurationBuilder();
        configurationBuilder.setKeepAlive(true)
                .setSocketTimeout(CHAT_SOCKET_TIMEOUT)
                .setUseTls(true);

        return configurationBuilder;
    }

    public void login(final QBUser user, final QBEntityCallback<Void> callback) {
        // Create REST API session on QuickBlox
        QBAuth.createSession(user, new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle args) {
                user.setId(session.getUserId());
                QBChatService.getInstance().login(user, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        callback.onSuccess(null, null);
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

}
