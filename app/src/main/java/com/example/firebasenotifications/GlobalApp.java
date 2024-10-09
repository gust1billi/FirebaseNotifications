package com.example.firebasenotifications;

import android.app.Application;

import timber.log.Timber;

public class GlobalApp extends Application {
    private static final String TAG = "Global App";

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    private String access_token;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant( new Timber.DebugTree( ) );
    }
}
