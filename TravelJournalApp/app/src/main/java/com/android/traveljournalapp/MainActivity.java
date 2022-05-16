package com.android.traveljournalapp;
import java.io.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Uri photoUri;


    // Using ArrayList to store images data
   // public static ArrayList addedByUserId = new ArrayList<>(Arrays.asList());
    public static ArrayList cityImg = new ArrayList<>(Arrays.asList());
    public static ArrayList cityName = new ArrayList<>(Arrays.asList());
    public static ArrayList cityDesc=new ArrayList<>(Arrays.asList());
    public static ArrayList cityFeedback=new ArrayList<>(Arrays.asList());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //------------recycler viewer ----------------

        // Getting reference of recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // Setting the layout as linear
        // layout for vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // Sending reference and data to Adapter
        Adapter adapter = new Adapter(MainActivity.this, /*addedByUserId,*/ cityImg, cityName, cityDesc, cityFeedback);
        // Setting Adapter to RecyclerView
        recyclerView.setAdapter(adapter);


        //------------bottom nav bar ----------------
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }

    public static void addItem(Uri photoUri,/*String currentUserId*/ String cityNameData, String cityDescriptionData, String cityFeedbackData)
    {
        //addedByUserId.add(currentUserId);
        cityImg.add(photoUri);
        cityName.add(cityNameData);
        cityDesc.add(cityDescriptionData);
        cityFeedback.add(cityFeedbackData);
    }

    public void addCityBtnClicked(View view) {
        startActivity(new Intent(this, TravelItemsActivity.class));
    }



}

