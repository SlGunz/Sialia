package com.slgunz.root.sialia.di;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.slgunz.root.sialia.SialiaApplication;

import java.util.NoSuchElementException;

public class ActivityInjector  {
    public static  <T extends Activity> void inject(T activity) {
        component(activity).inject(activity);
    }

    @SuppressWarnings("unchecked")
    private static  <T extends Activity> AndroidComponent<T> component(T activity) {
        AndroidComponent.Builder<T> componentnBuilder = (AndroidComponent.Builder<T>)
                ((SialiaApplication) activity.getApplication())
                        .componentBuilder(activity.getClass());
        if (componentnBuilder != null) {
            return componentnBuilder.build();
        }
        throw new NoSuchElementException();
    }
}
