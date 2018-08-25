package com.slgunz.root.sialia.di;

import com.slgunz.root.sialia.SialiaApplication;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        ActivitiesBindingModule.class
})
public interface AppComponent extends AndroidComponent<SialiaApplication> {
}
