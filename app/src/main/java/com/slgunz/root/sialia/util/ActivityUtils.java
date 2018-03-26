package com.slgunz.root.sialia.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.Button;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActivityUtils {
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId){
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void changeButtonDrawable(Button button, Drawable drawable){
        Drawable[] drawables = button.getCompoundDrawables();

        for (int i = 0; i < drawables.length; i++){
            if(drawables[i] == null) continue;
            drawables[i] = DrawableCompat.wrap(drawable);
            button.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
            break;
        }
    }
}
