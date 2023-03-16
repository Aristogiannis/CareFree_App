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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);




        Double [] pharmacy_lats =  {37.630238109521486, 37.62819300280989, 37.6307632419298, 37.6287162088105, 37.61314798821025, 37.615934428378075,37.92436740210267}; // last 2 cords in athens
        Double [] pharmacy_longs = {26.106339097445733,26.158847526239356,26.178307223731224,26.18643483803518,26.294174906418764,26.295176613093464, 23.724148422567776};// last 2 cords in athens
        String [] ratings = {"5/5","5/5","4/5","5/5","5/5","4.8/5", "4/5"};
        String [] tel_numbers = {"2275071271","2275032985","2275031394","4","2275023990","2275022212","7"};

        ImageButton account = findViewById(R.id.account);
        ScrollView scrollView = findViewById(R.id.scrollView);
        LinearLayout linearLayout = findViewById(R.id.linear);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
        } else {
            Toast.makeText(MainActivity.this, "Location permission not granted try again", Toast.LENGTH_LONG);
        }
            account.setBackground(null);
            account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent acc_page = new Intent(MainActivity.this, List.class);
                    startActivity(acc_page);
                }
            });

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                int i;
                                double lat = location.getLatitude();
                                double lon = location.getLongitude();
                                double [] distance = new double[pharmacy_lats.length];
                                int len = distance.length;

                                //TODO: AD COMMENTS ASAP OR COMMIT SUICIDE ON GITHUB
                                for(i = 0; i <= len-1;i++) {distance[i] = haversine(lat, lon, pharmacy_lats[i], pharmacy_longs[i]);}

                                Arrays.sort(distance);
                                reverseArray(distance);

                                for(i = len-1; i >= 0; i--){

                                    int finalI = i;
                                    TextView pharmacy_view = new TextView(MainActivity.this);
                                    TextView no_num = new TextView(MainActivity.this);

                                    no_num.setTextColor(Color.parseColor("#000000"));
                                    no_num.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                    no_num.setText("Telephone Number is not available");

                                    pharmacy_view.setTextColor(Color.parseColor("#000000"));
                                    pharmacy_view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                                    String pharmacy_string = ("Pharmacy "+ (len - finalI)+" (" +new DecimalFormat("##.##").format(distance[i]) + " km)  " + ratings[finalI] + " ★");
                                    SpannableString spannable = new SpannableString(pharmacy_string);
                                    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FFD700")), pharmacy_string.length() - ratings[finalI].length()-2, (pharmacy_string).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    pharmacy_view.setText(spannable, TextView.BufferType.SPANNABLE);


                                    ImageButton call_button = new ImageButton(MainActivity.this);

                                    call_button.setImageResource(R.drawable.icons8_phone_48);
                                    call_button.setBackground(null);
                                    call_button.setScaleX((float) 1);
                                    call_button.setScaleY((float) 1);

                                    ImageButton directions_button = new ImageButton(MainActivity.this);

                                    directions_button.setImageResource(R.drawable.icons8_map_marker_48);
                                    directions_button.setScaleX((float) 0.9);
                                    directions_button.setScaleY((float) 0.9);
                                    directions_button.setBackground(null);

                                    call_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent phone_intent = new Intent(Intent.ACTION_DIAL);
                                            phone_intent.setData(Uri.parse("tel:" + tel_numbers[len - finalI]));
                                            startActivity(phone_intent);
                                        }
                                    });
                                    directions_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+Double.toString(pharmacy_lats[finalI]) +","+Double.toString(pharmacy_longs[finalI]));
                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                            mapIntent.setPackage("com.google.android.apps.maps");
                                            startActivity(mapIntent);
                                        }
                                    });

                                    LinearLayout row = new LinearLayout(MainActivity.this);
                                    LinearLayout back = new LinearLayout(MainActivity.this);
                                    back.setOrientation(LinearLayout.VERTICAL);
                                    back.setBackgroundResource(R.drawable.backshape);

                                    row.setOrientation(LinearLayout.HORIZONTAL);

                                    LinearLayout row2 = new LinearLayout(MainActivity.this);
                                    row.setOrientation(LinearLayout.VERTICAL);

                                    /*** Layout parameters for pharmacy view ***/
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    layoutParams.weight = -1;
                                    layoutParams.gravity = Gravity.LEFT;
                                    layoutParams.setMargins(10,0,0,35);


                                    /** Layout parameters for get directions directions_button ***/
                                    LinearLayout.LayoutParams layoutParams_btn = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    layoutParams_btn.gravity = Gravity.LEFT;
                                    layoutParams_btn.setMargins(0,50,0,0);
                                    layoutParams_btn.weight =-1;

                                    /** Layout parameters for rows ***/
                                    LinearLayout.LayoutParams row_params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );

                                    row_params.gravity = Gravity.LEFT;
                                    row_params.setMargins(45,50,0,0);

                                    /** Layout parameters for rows ***/
                                    LinearLayout.LayoutParams back_params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT);
                                    back_params.setMargins(30,50,30,0);


                                    pharmacy_view.setLayoutParams(layoutParams);
                                    directions_button.setLayoutParams(layoutParams_btn);
                                    row.setLayoutParams(row_params);
                                    back.setLayoutParams(back_params);
                                    row.addView(pharmacy_view);
                                    row2.addView(directions_button);


                                    if (tel_numbers[finalI].length() < 9){
                                        row2.addView(no_num);
                                    }
                                    else{
                                        row2.addView(call_button);
                                    }

                                    back.addView(row);
                                    back.addView(row2);
                                    linearLayout.addView(back);
                                }


                            }
                            //else{Toast.makeText(MainActivity.this, "Failed to load location", Toast.LENGTH_LONG);}
                        }
                    });
    }

    public static void reverseArray(double[] arr){
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
