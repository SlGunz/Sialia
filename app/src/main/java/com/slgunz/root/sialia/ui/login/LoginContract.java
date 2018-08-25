package com.slgunz.root.sialia.ui.login;

import android.content.Context;
import android.content.Intent;

import com.slgunz.root.sialia.ui.base.BaseContract;
import com.slgunz.root.sialia.ui.base.BaseFragment;


public interface LoginContract {
    interface View {

        void enableProgressBar(boolean active);

        void openHomeScreen();

        void enableLoginButton(boolean isEnabled);

        void openBrowser(String uriString);

        void showErrorMessage(Throwable error);
    }

    interface Presenter  {

        void openAuthenticatePage(String callbackUrl);

        void obtainVerifier(String callbackUrl, Context context, Intent verifier);

        void verifyAccountCredentials();

        void setTokenAndSecret(Context context);

        boolean hasSavedOAuthData(Context context);

        void subscribe(View view);

        void unsubscribe();
    }
}
