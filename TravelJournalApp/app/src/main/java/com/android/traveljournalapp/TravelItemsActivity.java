package com.android.traveljournalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TravelItemsActivity extends AppCompatActivity {

    EditText input_city_name, input_city_description, input_city_feedback;
    Button submitButton;
    Uri selectedImageUri;
    // One Button
    Button BSelectImage;

    // One Preview Image
    ImageView IVPreviewImage;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    RecyclerView recyclerView;
    private FirebaseUser currentUser;
    static String currentUserId;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_items);

        /*currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUserId = currentUser.getUid();*/

        // register editText with instance
        input_city_name = (EditText) findViewById(R.id.input_city_name);
        input_city_description = (EditText) findViewById(R.id.input_city_description);
        input_city_feedback = (EditText) findViewById(R.id.input_city_feedback);

        // also register the submit button with the appropriate id
        submitButton = (Button) findViewById(R.id.submitButton);

        // handle the button with the onClickListener
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        String entered_city_name = input_city_name.getText().toString();
                        String entered_city_description = input_city_description.getText().toString();
                        String entered_city_feedback = input_city_feedback.getText().toString();

                        if (entered_city_name.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Please Enter City Name", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if (entered_city_description.isEmpty())
                            {
                                Toast.makeText(getApplicationContext(), "Please Enter City Description", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if (entered_city_feedback.isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Enter City Feedback", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    MainActivity.addToList(entered_city_name,entered_city_description,entered_city_feedback);

                                }
                            }
                        }
                    }
                });


        // register the UI widgets with their appropriate IDs
//        BSelectImage = findViewById(R.id.BSelectImage);
//        IVPreviewImage = findViewById(R.id.IVPreviewImage);
//
//        // handle the Choose Image button to trigger
//        // the image chooser function
//        BSelectImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageChooser();
//            }
//        });
    }
    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    IVPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

    public void goToListBtnClicked(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }


}


