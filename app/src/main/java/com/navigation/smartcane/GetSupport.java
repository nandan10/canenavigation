package com.navigation.smartcane;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import khushboo.rohit.osmnavi.R;

public class GetSupport extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_support);

      // return null;
    }

//    @Override
//    public void onBackPressed() {
//        Intent intentMain = new Intent(this, MainActivity.class);
//        startActivity(intentMain);
//        finish();
//    }

    public void onClick11(View view) {

        // call Login Activity
        Toast.makeText(getApplicationContext(), "You Clicked Register", Toast.LENGTH_LONG).show();
        Intent intentRegister = new Intent(getBaseContext(), RegisterActivity.class);
//                intentNA.putExtra("Type", NAV_TYPE_LOAD_ROUTE);
        startActivity(intentRegister);

    }

    public void onClick12(View view) {

        // Stay at the current activity.
        Toast.makeText(getApplicationContext(), "You Clicked Report Issue", Toast.LENGTH_LONG).show();
        Intent intentReport = new Intent(getBaseContext(), ReportIssue.class);
//                intentNA.putExtra("Type", NAV_TYPE_LOAD_ROUTE);
        startActivity(intentReport);
    }


    public void goBackPressed(View view) {
        onBackPressed();
    }
}