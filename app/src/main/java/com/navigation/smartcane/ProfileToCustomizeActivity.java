/*package com.navigation.smartcane;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import khushboo.rohit.osmnavi.R;

public class ProfileToCustomizeActivity extends AppCompatActivity{
    private Button customizeProfile1Button;
    private Button customizeProfile2Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_to_customize);
        initButtons();
    }

//    @Override
//    public void onBackPressed() {
//        Intent intentProfilesActivity = new Intent(this, ProfilesActivity.class);
//        startActivity(intentProfilesActivity);
//        finish();
//    }

    private void initButtons() {
        this.customizeProfile1Button = findViewById(R.id.btnCustomizeProfile1);
        this.customizeProfile1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfileCustomize = new Intent(getBaseContext(), ProfileCustomizeActivity.class);
                intentProfileCustomize.putExtra("CustomProfileNumber", 1);
                startActivity(intentProfileCustomize);
            }
        });

        this.customizeProfile2Button = findViewById(R.id.btnCustomizeProfile2);
        this.customizeProfile2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfileCustomize = new Intent(getBaseContext(), ProfileCustomizeActivity.class);
                intentProfileCustomize.putExtra("CustomProfileNumber", 2);
                startActivity(intentProfileCustomize);
            }
        });
    }
    public void goBackPressed(View view) {
        onBackPressed();
    }
}*/
