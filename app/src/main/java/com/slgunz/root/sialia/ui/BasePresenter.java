package com.slgunz.root.sialia.ui;

/**
 * Created by root on 12.12.2017.
 */

public interface BasePresenter<T> {

    void subscribe(T view);

    void unsubscribe();
}
