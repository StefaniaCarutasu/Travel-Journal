package com.android.traveljournalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class StartActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ImageView sunset_photo=(ImageView)findViewById(R.id.sunset_photo);
        sunset_photo.setImageResource(R.drawable.sunset_image);
    }

    public void goToLoginButtonClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
