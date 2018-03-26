package com.slgunz.root.sialia.ui.login;

import android.content.Context;
import android.content.Intent;

import com.slgunz.root.sialia.ui.base.BaseContract;


public interface LoginContract {
    interface View {

        void setWaitingIndicator(boolean active);

        void openHomeScreen();

        void enableLoginButton(boolean isEnabled);

        void openBrowser(String uriString);

        void showErrorMessage(Throwable error);
    }

    interface Presenter extends BaseContract.Presenter {

        void openAuthenticatePage(String callback_url);

        void obtainVerifier(String callback_url, Context context, Intent verifier);

        void verifyAccountCredentials();

        void setTokenAndSecret(Context context);

        boolean hasSavedOAuthData(Context context);
    }
}
