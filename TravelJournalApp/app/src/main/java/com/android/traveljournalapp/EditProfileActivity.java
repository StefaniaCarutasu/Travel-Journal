package com.android.traveljournalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText bioEditText;

    private Button submit;
    private Button cancel;

    private FirebaseUser currentUser;

    private FirebaseAuth mAuth;

    private String userID;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String oldEmail;
    private String oldUsername;
    private String oldBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);




        usernameEditText = (EditText) findViewById(R.id.username);
        bioEditText = (EditText) findViewById(R.id.bio);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        userID = currentUser.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users/" + userID);

        retrieveFromFirebase();

        usernameEditText.setText(oldUsername);

        bioEditText.setText(oldBio);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameEditText.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please a username", Toast.LENGTH_SHORT).show();
                }
                else {
                    User user = new User(usernameEditText.getText().toString(), oldEmail, bioEditText.getText().toString());
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Edit successful", Toast.LENGTH_LONG).show();

                                // redirect to login
                            }

                            else {
                                Toast.makeText(EditProfileActivity.this, "Edit failed, please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                finish();
            }
        });


    }
    private void retrieveFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userData = dataSnapshot.getValue(User.class);
                oldEmail = userData.email;
                oldUsername = userData.username;
                oldBio = userData.bio;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

}