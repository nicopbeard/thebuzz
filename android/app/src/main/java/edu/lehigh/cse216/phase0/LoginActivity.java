package edu.lehigh.cse216.phase0;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.Nullable;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {
    private Button signOut;
    private static final String TAG = "AndroidClarified";
    private static final String CLIENT_ID = "755594120508-dbkfkcfuo13jk4un0onvlob7lgie271h.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "";
    private static final String REDIRECT_URI_ROOT = "";
    private String code;

    private GoogleSignInClient googleSignInClient;
    private SignInButton googleSignInButton;
    private static final int RC_SIGN_IN = 9001;  //Was 9003 before

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
//        MultiDex.install(this);
//        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
//                .get(LoginViewModel.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(CLIENT_ID)
                .requestEmail()
                .build();

        signOut=findViewById(R.id.sign_out);

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        //findViewById(R.id.sign_in_button).setOnClickListener(this);
        googleSignInButton = findViewById(R.id.sign_in_button);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    case R.id.sign_out:
                        signOut();
                        break;
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            if(data != null) {
                Log.d("kta221","onActivityResult: data = NOT NULL");
            } else {
                Log.d("kta221", "onActivityResult: data = NULL");
            }

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            Log.d("kta221", String.valueOf(completedTask.isComplete()));
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            Log.d("kta221", idToken);

            updateUI(account);
        } catch (Exception e) {
            e.printStackTrace();
            updateUI(null);
        }
    }

    //Change UI according to user data.
    public void  updateUI(GoogleSignInAccount account){
        Log.d("kta221", "Made it to updateUI");


        if(account != null ){
            Toast.makeText(this,"Sign in Successfull",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            Log.d("kta221", "GoogleSignInAccount account was passed as null");

            Toast.makeText(this,"Sign in Fail",Toast.LENGTH_LONG).show();
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//    private void signOut() {
//        googleSignInClient.signOut()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // ...
//                    }
//                });
//    }
    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void revokeAccess() {
        googleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
}
