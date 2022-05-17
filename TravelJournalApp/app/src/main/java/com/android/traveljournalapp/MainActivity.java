package com.android.traveljournalapp;
import java.io.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.SortedMap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;


public class MainActivity extends AppCompatActivity {

   /* RecyclerView recyclerView;
    Uri photoUri;

    // Using ArrayList to store images data
   // public static ArrayList addedByUserId = new ArrayList<>(Arrays.asList());
    public static ArrayList cityImg=new ArrayList<>(Arrays.asList());
    public static ArrayList cityName=new ArrayList<>(Arrays.asList());
    public static ArrayList cityDesc=new ArrayList<>(Arrays.asList());
    public static ArrayList cityFeedback=new ArrayList<>(Arrays.asList());
    */

    private RecyclerView itemRV;
    private Adapter itemAdapter;
    public static ArrayList<ItemModal> itemModalArrayList=new ArrayList<ItemModal>();
   // itemModalArrayList = new ArrayList<ItemModal>();

    private FirebaseUser currentUser;

    private FirebaseAuth mAuth;

    private String userID;

    private FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;

    private static boolean hasChanged = true;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // itemModalArrayList.clear();

        //createArrayLists();
        itemRV=findViewById(R.id.recyclerView);
        // buildRecyclerView();


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

        // Lista locatii vizitate

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        userID = currentUser.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users_Locations/" + userID);

        if (hasChanged) {
            itemModalArrayList.clear();
            retrieveFromFirebase();
            hasChanged = !hasChanged;
        }

        buildRecyclerView(itemModalArrayList);

    }


    // calling on create option menu
    // layout to inflate our menu file.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch);

        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<ItemModal> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (ItemModal item : itemModalArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getCityName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Matches your request", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            itemAdapter.filterList(filteredlist);
        }
    }
    private void buildRecyclerView(ArrayList<ItemModal> itemModalArrayList) {
        // initializing our adapter class.
        itemAdapter = new Adapter(itemModalArrayList, MainActivity.this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        // setting layout manager
        // to our recycler view.
        itemRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        itemRV.setAdapter(itemAdapter);

    }

    private static void retrieveFromFirebase() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                itemModalArrayList.add(snapshot.getValue(ItemModal.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                System.out.println("Removed");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ItemModal itemModalArrayList = snapshot.getValue(ItemModal.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public static void addToList(String name, String desc, String feedback)
    {
        retrieveFromFirebase();
        ItemModal newLocation = new ItemModal(name, desc, feedback);
        itemModalArrayList.add(newLocation);

        // obiectul e ItemModal

        addToFirebase(itemModalArrayList);
    }

    private static void addToFirebase(ArrayList<ItemModal> itemModal) {

        FirebaseDatabase.getInstance().getReference("Users_Locations")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(itemModal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("We good");
                    hasChanged = !hasChanged;
                }
            }
        });

        if (hasChanged) {
            itemModalArrayList.clear();
            retrieveFromFirebase();
            hasChanged = !hasChanged;
        }

    }

    public void addCityBtnClicked(View view) {
        startActivity(new Intent(this, TravelItemsActivity.class));
    }




}

