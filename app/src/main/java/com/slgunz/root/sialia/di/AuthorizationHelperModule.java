package com.slgunz.root.sialia.di;

import com.slgunz.root.sialia.util.oauth.AuthorizationHelper;
import com.slgunz.root.sialia.util.oauth.AuthorizationHelperImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class AuthorizationHelperModule {

    @Binds
    abstract AuthorizationHelper provideAuthorizationRemoteDataSource(AuthorizationHelperImpl dataSource);
}
