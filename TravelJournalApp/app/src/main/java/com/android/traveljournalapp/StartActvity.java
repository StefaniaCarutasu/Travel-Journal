package com.android.traveljournalapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class StartActvity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ImageView sunset_photo=(ImageView)findViewById(R.id.sunset_photo);
        sunset_photo.setImageResource(R.drawable.sunset_image);

        ImageView plane=(ImageView) findViewById(R.id.plane);
        plane.setImageResource(R.drawable.plane);

        ObjectAnimator AnimatePlane = ObjectAnimator.ofFloat(plane,"translationX",1300f);
        AnimatePlane.setDuration(4000);

        AnimatePlane.start();

        AnimatePlane.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                plane.setX(0f);
                    AnimatePlane.start();
                }

        });

        mAuth = FirebaseAuth.getInstance();

        // if it's already logged in, redirect to signed in screen
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        }
    }


    public void goToLoginButtonClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
