package load.ing.carefree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ImageButton emergency_call = findViewById(R.id.emergency_call);
        TextView emergency_text = findViewById(R.id.emergency_text);
        ImageButton back_btn = findViewById(R.id.back__btn);
        emergency_call.setBackground(null);
        back_btn.setBackground(null);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent back = new Intent(List.this, MainActivity.class);
             startActivity(back);
            }
        });


        emergency_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phone_intent = new Intent(Intent.ACTION_DIAL);
                phone_intent.setData(Uri.parse("tel:" + "166"));
                startActivity(phone_intent);
            }
        });

        emergency_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phone_intent = new Intent(Intent.ACTION_DIAL);
                phone_intent.setData(Uri.parse("tel:" + "166"));
                startActivity(phone_intent);
            }
        });



    }
}