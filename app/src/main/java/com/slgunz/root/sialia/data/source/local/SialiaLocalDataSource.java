package com.slgunz.root.sialia.data.source.local;

import android.content.Context;


public interface SialiaLocalDataSource {
    void setOAuthUserKey(Context context, String userKey);

    void setOAuthUserSecret(Context context, String userSecret);

    String getOAuthUserKey(Context context);

    String getOAuthUserSecret(Context context);

    boolean hasUserKey(Context context);

    boolean hasUserSecret(Context context);
}
