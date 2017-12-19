package com.slgunz.root.sialia.di;

import android.app.Application;

import com.slgunz.root.sialia.data.ApplicationDataModule;
import com.slgunz.root.sialia.data.source.remote.NetworkModule;
import com.slgunz.root.sialia.util.schedulers.SchedulerProviderModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        NetworkModule.class,
        ApplicationDataModule.class,
        SchedulerProviderModule.class,
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        ActivityBindingModule.class
})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

//    SialiaDataManagerImpl getSialiaDataManager();

//    void inject(SialiaApplication application);

    @Override
    void inject(DaggerApplication instance);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }


}
