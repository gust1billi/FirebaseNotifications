package com.example.firebasenotifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import timber.log.Timber;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NOTIFICATION ACTIVITY";

    TextView intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Timber.tag( TAG ).e( "%s: Here", TAG );
        String topic = getIntent().getStringExtra("topic");
        intro = findViewById( R.id.intro_call_by_notif );

        intro.append( "\n" + topic );
    }
}