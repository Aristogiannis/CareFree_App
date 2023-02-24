package load.ing.carefree;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Double [] pharmacy_lats =  {37.630238109521486, 37.62819300280989, 37.6307632419298, 37.6287162088105, 37.61314798821025, 37.615934428378075,37.92436740210267}; // last 2 cords in athens
        Double [] pharmacy_longs = {26.106339097445733,26.158847526239356,26.178307223731224,26.18643483803518,26.294174906418764,26.295176613093464, 23.724148422567776};// last 2 cords in athens
        String [] ratings = {"5/5","5/5","4/5","5/5","5/5","4.8/5", "69/420"};

        Button Signup = findViewById(R.id.signup);
        ScrollView scrollView = findViewById(R.id.scrollView);
        LinearLayout linearLayout = findViewById(R.id.linear);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
        } else {
            Toast.makeText(MainActivity.this, "Location permission not granted try again", Toast.LENGTH_LONG);
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
                                int len = distance.length;


                                for(i = 0; i <= len-1;i++) {distance[i] = haversine(lat, lon, pharmacy_lats[i], pharmacy_longs[i]);}

                                Arrays.sort(distance);
                                reverseArray(distance);

                                for(i = len-1;i >= 0;i--){

                                    int finalI = i;
                                    TextView pharmacy_view = new TextView(MainActivity.this);

                                    pharmacy_view.setTextColor(Color.parseColor("#000000"));
                                    pharmacy_view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                    String pharmacy_string = ("Pharmacy "+ (len - finalI)+" (" +new DecimalFormat("##.##").format(distance[i]) + " km)  " + ratings[finalI] + " ☆");
                                    SpannableString spannable = new SpannableString(pharmacy_string);

                                    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FFD700")), pharmacy_string.length() - ratings[finalI].length()-2, (pharmacy_string).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    pharmacy_view.setText(spannable, TextView.BufferType.SPANNABLE);

                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    layoutParams.gravity = Gravity.LEFT;
                                    layoutParams.topMargin = 100;
                                    pharmacy_view.setLayoutParams(layoutParams);

                                    ImageButton button = new ImageButton(MainActivity.this);
                                    button.setImageResource(R.drawable.icons8_map_marker_48);
                                    button.setBackground(null);
                                    LinearLayout.LayoutParams layoutParams_btn = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );

                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+Double.toString(pharmacy_lats[finalI]) +","+Double.toString(pharmacy_longs[finalI]));
                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                            mapIntent.setPackage("com.google.android.apps.maps");
                                            startActivity(mapIntent);
                                        }
                                    });
                                    layoutParams_btn.gravity = Gravity.RIGHT;
                                    layoutParams_btn.bottomMargin = 85;
                                    button.setLayoutParams(layoutParams_btn);


                                    linearLayout.addView(pharmacy_view);
                                    linearLayout.addView(button);

                                }


                            }
                            else{Toast.makeText(MainActivity.this, "Failed to load location", Toast.LENGTH_LONG);}
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


    public static void reverseArray(double[] arr) {
        int start = 0;
        int end = arr.length - 1;
        while (start < end) {
            double temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }



}