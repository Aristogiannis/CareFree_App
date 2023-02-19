package load.ing.carefree;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {


    static double haversine(double lat1, double lon1, double lat2, double lon2){

         double dLat = Math.toRadians(lat2 - lat1);
         double dLon = Math.toRadians(lon2 - lon1);
         lat1 = Math.toRadians(lat1);
         lat2 = Math.toRadians(lat2);
         double a = Math.pow(Math.sin(dLat / 2), 2) +
         Math.pow(Math.sin(dLon / 2), 2) *
         Math.cos(lat1) *
         Math.cos(lat2);
         double rad = 6371.0072;
         double c = 2 * Math.atan(Math.sqrt(a));
         return rad * c;
    }


    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Double [] lats = {37.630238109521486, 37.62819300280989};
        Double [] longs = {26.106339097445733, 26.158847526239356};
        Button Signup = findViewById(R.id.signup);
        ImageButton get_directions = findViewById(R.id.get_directions);
        TextView location_view = findViewById(R.id.location);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
        } else {
            Toast permission_denied = Toast.makeText(getApplicationContext(), "Location permission not granted try again", Toast.LENGTH_LONG);
        }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                double lat = location.getLatitude();
                                double lon = location.getLongitude();

                               Double first = haversine(lat, lon,  lats[0], longs[0]);
                               Double second= haversine(lat, lon,  lats[1], longs[1]);
                               if (first < second){
                                  location_view.setText(Double.toString(first) + " first");
                                }
                                else {location_view.setText(Double.toString(second));}



                            }
                        }
                    });

        get_directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=37.92073233218703, 23.711842658876936");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, List.class);
                startActivity(intent);
            }
        });
    }
}