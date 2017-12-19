package com.slgunz.root.sialia.ui.login;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LoginEndToEndTest {

    private LoginPresenter mPresenter;

    @Mock
    private LoginContract.View mView;

    @Mock
    private Intent intent;

    @Mock
    private Context mContext;

    @Before
    public void initialization(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void launchApplicationAndLoginFirstTime() {
        // User click "sign in with twitter" button
        mPresenter.openAuthenticatePage("callback_url");
        mView.setWaitingIndicator(true);

        // Obtain result from "onNewIntent" event for retrieving "access token"
        mPresenter.obtainVerifier("callback_url", mContext, intent);
        mView.setWaitingIndicator(false);

        // open HomeActivity
        mView.openHomeScreen();
    }

    @Test
    public void launchApplicationAndLoginWithObtainedAccessToken(){
        mPresenter.hasSavedOAuthData(mContext);
        // check validation of "access token", when application is launching
        mPresenter.verifyAccountCredentials();
        // disabled "sign in" button while awaiting validation
        mView.enableLoginButton(false);
        // enable button
        mView.enableLoginButton(true);
        mView.openHomeScreen();
    }
}
