package load.ing.carefree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {


    double haversine(double lat1, double lon1, double lat2, double lon2){

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

        Double [] pharmacy_lats =  {37.630238109521486, 37.62819300280989, 37.6307632419298, 37.6287162088105, 37.61314798821025, 37.615934428378075,37.92436740210267}; // last 2 cords in athens
        Double [] pharmacy_longs = {26.106339097445733,26.158847526239356,26.178307223731224,26.18643483803518,26.294174906418764,26.295176613093464, 23.724148422567776};// last 2 cords in athens


        Button Signup = findViewById(R.id.signup);
        ScrollView scrollView = findViewById(R.id.scrollView);
        LinearLayout linearLayout = findViewById(R.id.linear);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
        } else {
            Toast permission_denied = Toast.makeText(getApplicationContext(), "Location permission not granted try again", Toast.LENGTH_LONG);
        }

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                int i;
                                double lat = location.getLatitude();
                                double lon = location.getLongitude();
                                double [] distance = {0,0,0,0,0,0,0};
                                //double min = 99999999;

                                for(i = 0; i <= 6;i++) {distance[i] = haversine(lat, lon, pharmacy_lats[i], pharmacy_longs[i]);}

                                for(i = 0;i<=6;i++){
                                    int finalI = i;
                                    int n = distance.length;
                                    Arrays.sort(distance);

                                    TextView textView = new TextView(MainActivity.this);
                                    textView.setText("Pharmacy "+ (finalI+1)+" (" +new DecimalFormat("##.##").format(distance[i]) + " km)");
                                    linearLayout.addView(textView);

                                    ImageButton button = new ImageButton(MainActivity.this);
                                    button.setImageResource(R.drawable.icons8_map_marker_48);
                                    button.setBackground(null);

                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+Double.toString(pharmacy_lats[finalI]) +","+Double.toString(pharmacy_longs[finalI]));
                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                            mapIntent.setPackage("com.google.android.apps.maps");
                                            startActivity(mapIntent);
                                        }
                                    });
                                    linearLayout.addView(button);
                                    linearLayout.setGravity(Gravity.RIGHT);
                                }


                            }
                            else{ Toast permission_denied = Toast.makeText(getApplicationContext(), "Failed to load location", Toast.LENGTH_LONG);}
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

