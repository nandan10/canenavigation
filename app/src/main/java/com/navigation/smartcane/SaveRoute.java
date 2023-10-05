package com.navigation.smartcane;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import khushboo.rohit.osmnavi.R;

/**
 * Created by rohit on 19/1/17.
 */

public class SaveRoute extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saveroute);
    }

    public void onSave(View view) {
        EditText routeNameView = (EditText)findViewById(R.id.editText2);
        String routeName = routeNameView.getText().toString();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("name", routeName);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
    public void goBackPressed(View view) {
        onBackPressed();
    }
}
