package com.slgunz.root.sialia.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.di.ActivityScoped;
import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;

import java.security.InvalidParameterException;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;

@ActivityScoped
public class LoginPresenter implements LoginContract.Presenter {

    @NonNull
    private ApplicationDataManager mSialiaDataManager;
    @NonNull
    private LoginContract.View mLoginView;
    @NonNull
    private BaseSchedulerProvider mScheduler;

    private CompositeDisposable mDisposables = new CompositeDisposable();

    @Inject
    public LoginPresenter(@NonNull ApplicationDataManager sialiaDataManager,
                          @NonNull LoginActivity loginActivity,
                          @NonNull BaseSchedulerProvider schedulerProvider) {
        mSialiaDataManager = checkNotNull(sialiaDataManager, "Login Application DataManager cannot be null");
        mLoginView = checkNotNull(loginActivity, "Login Fragment cannot be null");
        mScheduler = checkNotNull(schedulerProvider, "Login SchedulerProvider cannot be null");
    }

    @Override
    public void openAuthenticatePage(String callback_url) {
        mLoginView.setWaitingIndicator(true);
        Disposable disposable = mSialiaDataManager.openAuthenticatePage(callback_url)
                .observeOn(mScheduler.ui())
                .subscribeOn(mScheduler.io())
                .subscribe(
                        // onSuccess
                        s -> {
                            mLoginView.openBrowser(s);
                        },
                        // onError
                        this::showErrorMessage
                );
        mDisposables.add(disposable);
    }

    private void showErrorMessage(Throwable message) {
        mLoginView.showErrorMessage(message);
    }

    @Override
    public void obtainVerifier(String callback_url, Context context, Intent verifier) {
        mLoginView.setWaitingIndicator(false);

        if (mSialiaDataManager.isNotCorrectVerifier(callback_url, verifier)) {
            showErrorMessage(new InvalidParameterException("returned verifier isn't correct"));
            return;
        }

        Disposable disposable = mSialiaDataManager.obtainVerifier(verifier)
                .observeOn(mScheduler.ui())
                .subscribeOn(mScheduler.io())
                .subscribe(
                        // onComplete
                        () -> {
                            String token = mSialiaDataManager.getToken();
                            String tokenSecret = mSialiaDataManager.getTokenSecret();

                            mSialiaDataManager.setOAuthUserKey(context, token);
                            mSialiaDataManager.setOAuthUserSecret(context, tokenSecret);

                            verifyAccountCredentials();
                        },
                        // onError
                        this::showErrorMessage
                );

        mDisposables.add(disposable);
    }

    @Override
    public void verifyAccountCredentials() {

        mLoginView.enableLoginButton(false);

        Disposable disposable = mSialiaDataManager.verifyCredentials()
                .observeOn(mScheduler.ui())
                .subscribeOn(mScheduler.io())
                .subscribe(
                        // onSuccess
                        user -> {
                            mSialiaDataManager.setAccountProfile(user);
                            mLoginView.enableLoginButton(true);
                            mLoginView.openHomeScreen();
                        },
                        // onError
                        throwable -> {
                            mLoginView.enableLoginButton(true);
                            showErrorMessage(throwable);
                        }
                );
        mDisposables.add(disposable);
    }

    @Override
    public void setTokenAndSecret(Context context) {
        mSialiaDataManager.setTokenAndSecret(context);
    }

    @Override
    public boolean hasSavedOAuthData(Context context) {
        return mSialiaDataManager.hasTokenAndSecret(context);
    }

    @Override
    public void subscribe(LoginContract.View view) {}

    @Override
    public void unsubscribe() {
        mDisposables.dispose();
    }
}
