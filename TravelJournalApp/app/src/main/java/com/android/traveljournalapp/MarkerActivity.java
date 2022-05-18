package com.android.traveljournalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MarkerActivity extends AppCompatActivity {

    private Marker marker;
    //ArrayList<Marker> markerArrayList;
    GoogleMap map;
    EditText input_lat, input_long, input_title;
    Button addMarkerButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

        input_lat = (EditText) findViewById(R.id.input_marker_lat);
        input_long = (EditText) findViewById(R.id.input_marker_long);
        input_title= (EditText) findViewById(R.id.input_marker_title);

        // also register the submit button with the appropriate id
        addMarkerButton = (Button) findViewById(R.id.addMarkerButton);

        // handle the button with the onClickListener
        addMarkerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        String entered_lat = input_lat.getText().toString();
                        String entered_long = input_long.getText().toString();
                        String entered_title = input_title.getText().toString();

                        if (entered_lat.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Please Enter Latitude", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if (entered_long.isEmpty())
                            {
                                Toast.makeText(getApplicationContext(), "Please Enter Longitude", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if (entered_title.isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Enter title", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    MapsActivity.addMarkerToList(new Marker(Integer.parseInt(entered_lat),Integer.parseInt(entered_long), entered_title));
                                    startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                                }
                            }
                        }
                    }
                });




    }



}