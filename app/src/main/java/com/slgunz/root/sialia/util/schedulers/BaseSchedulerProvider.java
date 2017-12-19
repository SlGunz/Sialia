package com.slgunz.root.sialia.util.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;


/**
 * Created by root on 05.11.2017.
 */

public interface BaseSchedulerProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
