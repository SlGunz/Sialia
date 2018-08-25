package com.slgunz.root.sialia.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.slgunz.root.sialia.data.ApplicationDataManager;
import com.slgunz.root.sialia.data.model.Tweet;
import com.slgunz.root.sialia.di.scopes.ActivityScoped;
import com.slgunz.root.sialia.ui.base.BaseContract;
import com.slgunz.root.sialia.ui.base.BaseFragment;
import com.slgunz.root.sialia.util.schedulers.BaseSchedulerProvider;

import java.security.InvalidParameterException;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@ActivityScoped
public class LoginPresenter implements LoginContract.Presenter {

    @NonNull
    private ApplicationDataManager mDataManager;
    @NonNull
    private LoginContract.View mLoginView;
    @NonNull
    private BaseSchedulerProvider mScheduler;

    private CompositeDisposable mDisposables;

    @Inject
    public LoginPresenter(@NonNull ApplicationDataManager dataManager,
                          @NonNull BaseSchedulerProvider scheduler) {
        mDataManager = dataManager;
        mScheduler = scheduler;
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void openAuthenticatePage(String callbackUrl) {
        mLoginView.enableProgressBar(true);
        Disposable disposable = mDataManager.openAuthenticatePage(callbackUrl)
                .observeOn(mScheduler.ui())
                .subscribeOn(mScheduler.io())
                .subscribe(
                        // onSuccess
                        s -> {
                            mLoginView.openBrowser(s);
                        },
                        // onError
                        throwable -> {
                            showErrorMessage(throwable);
                            mLoginView.enableProgressBar(false);
                        }
                );
        mDisposables.add(disposable);
    }

    private void showErrorMessage(Throwable message) {
        mLoginView.showErrorMessage(message);
    }

    @Override
    public void obtainVerifier(String callbackUrl, Context context, Intent verifier) {
        mLoginView.enableProgressBar(false);

        if (mDataManager.isNotCorrectVerifier(callbackUrl, verifier)) {
            showErrorMessage(new InvalidParameterException("returned verifier isn't correct"));
            return;
        }

        Disposable disposable = mDataManager.retrieveAccessToken(verifier)
                .observeOn(mScheduler.ui())
                .subscribeOn(mScheduler.io())
                .subscribe(
                        // onComplete
                        () -> {
                            mDataManager.saveReceivedOAuthData(context);
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

        Disposable disposable = mDataManager.verifyCredentials()
                .observeOn(mScheduler.ui())
                .subscribeOn(mScheduler.io())
                .subscribe(
                        // onSuccess
                        user -> {
                            mDataManager.setAccountProfile(user);
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
        mDataManager.setTokenAndSecret(context);
    }

    @Override
    public boolean hasSavedOAuthData(Context context) {
        return mDataManager.hasTokenAndSecret(context);
    }

    @Override
    public void subscribe(LoginContract.View view) {
        mLoginView = view;
    }

    @Override
    public void unsubscribe() {
        mDisposables.dispose();
    }

}
