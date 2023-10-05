package com.navigation.smartcane;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import khushboo.rohit.osmnavi.R;

/**
 * Created by rohit on 19/1/17.
 */

public class ShowDb extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_db);
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv2.setText(getIntent().getStringExtra("db"));
    }
}
