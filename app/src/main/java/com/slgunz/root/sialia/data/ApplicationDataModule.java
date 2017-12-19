package com.slgunz.root.sialia.data;

import com.slgunz.root.sialia.data.source.local.SialiaLocalDataSource;
import com.slgunz.root.sialia.data.source.local.SialiaLocalDataSourceImpl;
import com.slgunz.root.sialia.data.source.remote.AuthorizationRemoteDataSource;
import com.slgunz.root.sialia.data.source.remote.AuthorizationRemoteDataSourceImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class ApplicationDataModule {
    @Singleton
    @Binds
    abstract SialiaLocalDataSource provideTweetsLocalDataSource(SialiaLocalDataSourceImpl dataSource);

    @Singleton
    @Binds
    abstract AuthorizationRemoteDataSource provideAuthorizationRemoteDataSource(AuthorizationRemoteDataSourceImpl dataSource);
}
