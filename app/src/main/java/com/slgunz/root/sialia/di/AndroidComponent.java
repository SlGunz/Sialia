package com.slgunz.root.sialia.di;

public interface AndroidComponent<T> {
    void inject(T instance);

    interface Builder<U> {
        AndroidComponent<U> build();
    }
}
