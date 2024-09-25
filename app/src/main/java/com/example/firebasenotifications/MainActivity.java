package com.example.firebasenotifications;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN ACTIVITY";

    Button token;
    TextView hello;
    EditText tokenCopy;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                if ( isGranted ){
                    Timber.tag(TAG).e("Granted");
                } else Timber.tag(TAG).e("Denied");
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initProjectTest();

        token = findViewById( R.id.tokenBtn );
        hello = findViewById( R.id.hello );
        tokenCopy = findViewById( R.id.edit_text_id );

        token.setOnClickListener( view -> firebaseMessagingToken() );
        askNotificationPermission();
    }

    private void firebaseMessagingToken() {
        String failure = "Firebase Messaging GET TOKEN FAILED. \n Fetching FCM REGIS TOKEN FAILURE";
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if ( task.isSuccessful() ){
                        String token = task.getResult();
                        String msg = TAG + "\nFCM REGIS TOKEN = " + token;
                        Timber.tag( TAG ).e( msg );
                        hello.setText( token );
                        tokenCopy.setText( token );
                    } else Timber.tag( TAG ).e( failure );
                });

    }

    private void initProjectTest() {
        Utils.showToast( MainActivity.this, "HELLO WORLD!" );
//        Timber.tag(TAG).e("bruh");
        Timber.i( "Custom Logs");
    }

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
                Timber.tag( TAG ).e( "Permission already granted" );
            } else if ( shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) ) {
                Utils.showToast( MainActivity.this, "Please allow notifications" );
                Timber.tag( TAG ).e( "Permission not granted" );
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}