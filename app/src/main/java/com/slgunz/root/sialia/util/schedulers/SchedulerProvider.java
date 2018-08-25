package com.slgunz.root.sialia.util.schedulers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import dagger.Reusable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Reusable
public class SchedulerProvider implements BaseSchedulerProvider {

    @Inject
    public SchedulerProvider(){}

    @Override
    @NonNull
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    @NonNull
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    @NonNull
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}