package com.android.traveljournalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

// facebook login
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputUsername;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private CredentialsClient mCredentialsClient;
/*
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;*/
    private static final String TAG = "FacebookAuthentication";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputUsername = (EditText) findViewById(R.id.username);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Get Firebase instances
        mAuth = FirebaseAuth.getInstance();
        System.out.println("onCreate");

        // if it's already logged in, redirect to signed in screen
        if (mAuth.getCurrentUser() != null) {
            System.out.println("E DEJA BOSS");
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        }

        initializeGoogleSignIn();
    }

    private void initializeGoogleSignIn() {
        // Configure Google Sign in
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.google_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

    }

    private void googleSignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            System.out.println("requestCode == 100");

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            String displayName = task.getResult().getDisplayName();
            String email = task.getResult().getEmail();


            mAuth.createUserWithEmailAndPassword(email, email)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {
                                User user = new User(displayName, email);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG).show();

                                            // redirect to login
                                        }

                                        else {
                                            Toast.makeText(RegisterActivity.this, "Registration failed, please try again", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
                                finish();
                            }
                            else {
                                // Toast.makeText(RegisterActivity.this, "Registration failed, please try again", Toast.LENGTH_LONG).show();
                                // Log.e("MyTag", task.getException().toString());

                                mAuth.signInWithEmailAndPassword(email, email)
                                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                progressBar.setVisibility(View.GONE);
                                                if (task.isSuccessful()) {
                                                    // the login was a success and we redirect to the main activity
                                                    startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
                                                    finish();
                                                }
                                                else {
                                                    // there was an error
                                                    // first we check if the problem might be the length of the password
                                                    if (email.length() < 6) {
                                                        inputPassword.setError(getString(R.string.minimum_password));
                                                    }
                                                    else {
                                                        Toast.makeText(RegisterActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    private void HomeActivity() {
        finish();
        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));

    }

    private void goToProfile() {
        finish();
        startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
    }


    /*
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    } */

    public void onRegisterClicked(View view) {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        final String username = inputUsername.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Please enter a username!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter an email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter a password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "The password must be at least 6 characters long!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            User user = new User(username, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG).show();

                                        // redirect to login
                                    }

                                    else {
                                        Toast.makeText(RegisterActivity.this, "Registration failed, please try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Registration failed, please try again", Toast.LENGTH_LONG).show();
                            Log.e("MyTag", task.getException().toString());
                        }
                    }
                });
    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}