package com.navigation.smartcane;

/**
 * Created by hp pc on 05-12-2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import khushboo.rohit.osmnavi.R;

public class ImageActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Button txtButton = (Button) this.findViewById(R.id.button1);
        txtButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageActivity.this,text_recognition.class);
                startActivity(intent);
            }
        });
        Button imgbtn = (Button) this.findViewById(R.id.button);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageActivity.this,image_recognition.class);
                startActivity(intent);

            }
        });
    }
}
