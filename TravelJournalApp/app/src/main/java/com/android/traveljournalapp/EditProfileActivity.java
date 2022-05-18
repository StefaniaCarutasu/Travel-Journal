package com.android.traveljournalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditProfileActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText bioEditText;

    private Button submit;
    private Button cancel;
    private Button selectPicture;

    private FirebaseUser currentUser;

    private FirebaseAuth mAuth;

    private String userID;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String oldEmail;
    private String oldUsername;
    private String oldBio;

    private Uri imageUri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private ImageView profilePicture;
    private Button takePicture;

    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        checkPermission(Manifest.permission.CAMERA, RequestPermissionCode);

        usernameEditText = (EditText) findViewById(R.id.username);
        bioEditText = (EditText) findViewById(R.id.bio);
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
        selectPicture = (Button) findViewById(R.id.import_profile_picture);
        takePicture = (Button) findViewById(R.id.take_profile_picture);

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
                    uploadImage();
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

        selectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImage();
            }
        });


    }

    private void uploadImage() {

        String fileName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures/"+fileName);


        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        profilePicture.setImageURI(null);
                        Toast.makeText(EditProfileActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(EditProfileActivity.this,"Failed to Upload",Toast.LENGTH_SHORT).show();


            }
        });

    }

    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(EditProfileActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }

    public void takeImage(){

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 101);
            //zuploadImage();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){

            imageUri = data.getData();
            profilePicture.setImageURI(imageUri);

        }

        if (requestCode == 101 && data != null && data.getData() != null){

            Bitmap bitmap=(Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(bitmap, Bitmap.CompressFormat.JPEG, 80 );
            profilePicture.setImageURI(imageUri);

        }
    }

    public Uri getImageUri(Bitmap src, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), src, "title", null);
        return Uri.parse(path);
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