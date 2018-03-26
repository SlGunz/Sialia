package com.slgunz.root.sialia.di;

import android.app.Application;

import com.slgunz.root.sialia.util.oauth.AuthorizationHelperModule;
import com.slgunz.root.sialia.data.service.PollServiceModule;
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
        AuthorizationHelperModule.class,
        SchedulerProviderModule.class,
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        PollServiceModule.class,
        ActivityBindingModule.class
})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    @Override
    void inject(DaggerApplication instance);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }


}
