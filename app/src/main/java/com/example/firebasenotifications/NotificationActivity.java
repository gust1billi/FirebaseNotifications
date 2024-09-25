package com.example.firebasenotifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import timber.log.Timber;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NOTIFICATION ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Timber.tag( TAG ).e( "%s: Here", TAG );
    }
}