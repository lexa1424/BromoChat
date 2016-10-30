package com.example.alexey.bromochat.activity;

import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alexey.bromochat.ChatHelper;
import com.example.alexey.bromochat.PrefsHelper;
import com.example.alexey.bromochat.R;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    CheckBox rememberMeCheckBox;
    ProgressBar progressBar;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        boolean isRememberMe = PrefsHelper.getPrefsHelper(getApplicationContext()).getPref(PrefsHelper.PREF_REMEMBER_ME, true);
        rememberMeCheckBox.setChecked(isRememberMe);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    loginOnClickListener();
                    return true;
                }
                return false;
            }
        });
    }

    private void loginOnClickListener() {

        //DialogFragment dialogFragment = new DialogFragment();
        //ProgressDialog.show(this, "Authorization", "Loading");
        //progressBar.setVisibility(ProgressBar.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);

        String userEmail = emailEditText.getText().toString();
        String userPassword = passwordEditText.getText().toString();

        PrefsHelper.getPrefsHelper(getApplicationContext()).savePref(PrefsHelper.PREF_IMPORT_INITIALIZED, true);
        final QBUser user = new QBUser(null, userPassword, userEmail);
        ChatHelper.getInstance().login(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                PrefsHelper.getPrefsHelper(getApplicationContext()).saveQbUser(user);
                DialogsActivity.start(LoginActivity.this);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    private void initUI() {
        emailEditText = (EditText)findViewById(R.id.email_textview);
        passwordEditText = (EditText)findViewById(R.id.password_edittext);
        rememberMeCheckBox = (CheckBox)findViewById(R.id.remember_me_checkbox);
        progressBar = (ProgressBar)findViewById(R.id.loading_pb);
        linearLayout = (LinearLayout)findViewById(R.id.loading_pb_layout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
