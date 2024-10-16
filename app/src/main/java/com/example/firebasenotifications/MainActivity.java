package com.example.firebasenotifications;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Console;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN ACTIVITY";
    GlobalApp app = new GlobalApp();

    Button token, GSignIn;
    TextView hello;
    EditText tokenCopy;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
//            reload();
            Timber.tag( TAG ).e( "RELOAD ");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == R.id.hello ){
            Timber.tag( TAG ).e( "Hello" );
        } else if ( item.getItemId() == R.id.signIn ){
            Timber.tag( TAG ).e( "Sign In ");
            Intent i = new Intent( MainActivity.this, SignInActivity.class );
            startActivity( i );
        }
        return super.onOptionsItemSelected(item);
    }

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

        token = findViewById( R.id.tokenBtn );
        hello = findViewById( R.id.hello );
        tokenCopy = findViewById( R.id.edit_text_id );

        token.setOnClickListener( view -> firebaseMessagingToken() );
        askNotificationPermission();
        firebaseSubscribeToTopic("weather");
        firebaseSubscribeToTopic("ESports");
        firebaseSubscribeToTopic("Programming");
        firebaseSubscribeToTopic("Manga");

        GSignIn = findViewById( R.id.signInByGoogle );
        GSignIn.setOnClickListener( view -> {
            Intent i = new Intent( MainActivity.this, SignInActivity.class );
            startActivity( i );
        });

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void firebaseSubscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic( topic )
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed ";
                    if ( !task.isSuccessful() ){
                        msg = "Subscription Task Failed";
                    }
                    Timber.tag( TAG ).d( msg );
                    Utils.showToast( MainActivity.this, msg );
                });
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
                        app.setAccess_token( token );
                        Timber.tag( TAG ).e( "Token from Global: %s", token );
                        tokenCopy.setText( token );
                    } else Timber.tag( TAG ).e( failure );
                });
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