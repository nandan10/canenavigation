package com.navigation.smartcane;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import khushboo.rohit.osmnavi.R;

public class TrainingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        WebView webview = findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/doc.html");
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation3);

        // Set Home selected
        // bottomNavigationView.setSelectedItemId(R.id.home1);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {
                    case R.id.settings3:
                        // startActivity(new Intent(getApplicationContext(),SearchPOI.class));
                        overridePendingTransition(0, 0);
                        Toast.makeText(getApplicationContext(), "You Clicked Settings", Toast.LENGTH_LONG).show();
                        Intent intentProfiles = new Intent(getBaseContext(), SettingsActivity.class);
//                intentNA.putExtra("Type", NAV_TYPE_LOAD_ROUTE);
                        startActivity(intentProfiles);
                        return true;
                    case R.id.home1:
                        overridePendingTransition(0, 0);
                        Toast.makeText(getApplicationContext(), "You Clicked Home", Toast.LENGTH_LONG).show();
                        Intent intentMainActivity = new Intent(getBaseContext(), MainActivity.class);
//                intentNA.putExtra("Type", NAV_TYPE_LOAD_ROUTE);
                        startActivity(intentMainActivity);
                        return true;
                    case R.id.call:
                        overridePendingTransition(0, 0);
                        Toast.makeText(getApplicationContext(), "You Clicked Emergency Call", Toast.LENGTH_LONG).show();
                        Intent intentEmergencyCall = new Intent(getBaseContext(), EmergencyCall.class);
//                intentNA.putExtra("Type", NAV_TYPE_LOAD_ROUTE);
                        startActivity(intentEmergencyCall);
                        return true;
                }
                return false;
            }
        });

    }


    public void goBackPressed(View view) {
        onBackPressed();
    }
}
