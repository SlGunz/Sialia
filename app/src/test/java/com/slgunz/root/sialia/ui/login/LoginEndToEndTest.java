package com.slgunz.root.sialia.ui.login;

import android.content.Context;
import android.content.Intent;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.User;
import com.slgunz.root.sialia.ui.TestSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;
import io.reactivex.Single;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginEndToEndTest {

    private LoginPresenter presenter;

    @Mock
    private LoginActivity mView;

    @Mock
    private ApplicationDataManager appManager;

    @Mock
    private Intent intent;

    @Mock
    private Context mContext;

    public static final String CALLBACK_URL = "callback_url";

    @Before
    public void initialization(){
        MockitoAnnotations.initMocks(this);
        presenter = new LoginPresenter(appManager, mView, new TestSchedulerProvider());
    }

    @Test
    public void launchApplicationAndLoginFirstTime() {
        User user = new User();
        when(appManager.openAuthenticatePage(CALLBACK_URL)).thenReturn(Single.just(""));
        when(appManager.verifyCredentials()).thenReturn(Single.just(user));
        when(appManager.retrieveAccessToken(intent)).thenReturn(Completable.complete());

        // User click "sign in with twitter" button
        presenter.openAuthenticatePage(CALLBACK_URL);
        verify(mView).setWaitingIndicator(true);
        // data manager generates query to twitter service
        verify(appManager).openAuthenticatePage(CALLBACK_URL);
        // application launches a browser for request a permission
        verify(mView).openBrowser("");

        // Obtain result from "onNewIntent" event for retrieving "access token"
        presenter.obtainVerifier(CALLBACK_URL, mContext, intent);
        verify(mView).setWaitingIndicator(false);
        // sends a query for getting an access token
        verify(appManager).retrieveAccessToken(intent);
        //getting and saving data
        verify(appManager).saveReceivedOAuthData(mContext);
        // send verifying request
        verify(appManager).verifyCredentials();
        // set account profile data
        verify(appManager).setAccountProfile(user);
        // open HomeActivity
        verify(mView).openHomeScreen();
    }

    @Test
    public void launchApplicationAndLoginWithObtainedAccessToken(){
        User user = new User();
        when(appManager.verifyCredentials()).thenReturn(Single.just(user));

        // return true if has saved data
        presenter.hasSavedOAuthData(mContext);
        // check validation of "access token", when application is launching
        presenter.verifyAccountCredentials();

        // disabled "sign in" button while awaiting validation
        verify(mView).enableLoginButton(false);
        // send verifying request
        verify(appManager).verifyCredentials();
        // set account profile data
        verify(appManager).setAccountProfile(user);
        // enable button
        verify(mView).enableLoginButton(true);
        verify(mView).openHomeScreen();
    }
}
