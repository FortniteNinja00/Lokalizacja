package com.example.bozedajmisily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    TextView longitude, latitude;
    Button lokalizacja;
    private final static int REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        lokalizacja = findViewById(R.id.button);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        lokalizacja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PobierzLokalizacje();
            }
        });
    }

    private void PobierzLokalizacje() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {

                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            latitude.setText("Szerokość: " + addresses.get(0).getLatitude());
                            longitude.setText("Długość : " + addresses.get(0).getLongitude());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });

        }else {

                ZapytanieoZgodze();

        }
    }

    private void ZapytanieoZgodze() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    if(requestCode == REQUEST_CODE){

        if(grantResults.length> 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED){
            PobierzLokalizacje();
        }
        else {
            Toast.makeText(this, "Wymagana zgoda do użycia lokalizacji", Toast.LENGTH_SHORT).show();
        }
    }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}