package com.slgunz.root.sialia.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.di.ActivityInjector;
import com.slgunz.root.sialia.ui.home.HomeActivity;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    final public static String CALLBACK_URL = "sialia-twitter-client://sialiatwitterclient";
    @Inject
    LoginContract.Presenter mPresenter;
    // uses for snackbar
    CoordinatorLayout mCoordinatorLayout;

    Button mLoginButton;

    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityInjector.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);

        mCoordinatorLayout = findViewById(R.id.login_coordinator_layout);

        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(
                view -> mPresenter.openAuthenticatePage(CALLBACK_URL)
        );

        mProgressBar = findViewById(R.id.login_progress_bar);

        mPresenter.subscribe(this);
        if (mPresenter.hasSavedOAuthData(LoginActivity.this)) {
            mPresenter.setTokenAndSecret(this);
            mPresenter.verifyAccountCredentials();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPresenter.obtainVerifier(CALLBACK_URL, LoginActivity.this, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void enableProgressBar(boolean active) {
        mProgressBar.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void openHomeScreen() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void enableLoginButton(boolean isEnabled) {
        mLoginButton.setEnabled(isEnabled);
    }

    @Override
    public void openBrowser(String uriString) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
        startActivity(intent);
    }

    @Override
    public void showErrorMessage(Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
