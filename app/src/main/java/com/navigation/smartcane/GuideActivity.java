
package com.navigation.smartcane;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import khushboo.rohit.osmnavi.R;

public class GuideActivity extends AppCompatActivity {

   // private int mCounter = 0;
    //Button btn;
   // TextView txv;
    //private WebView webView;
    //private String doc;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);
        // Intent intentNavi = new Intent(new NavigationActivity(), getAssets().getClass());
//                intentNA.putExtra("Type", NAV_TYPE_LOAD_ROUTE);

        WebView webview = findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/guide.html");

    }







    //TextView select = findViewById(R.id.select);

    // getting data from intent.
    //  String name = getIntent().getStringExtra("key");

    // setting data to our text view.
    //  select.setText(name);

      /*  btn = (Button) findViewById(R.id.bt);
        txv = (TextView) findViewById(R.id.tx);

    }

            public void count(View view) {
                mCounter++;
                txv.setText(Integer.toString(mCounter));

            }
    public void count1(View view) {
        mCounter--;
        txv.setText(Integer.toString(mCounter));

    }


    }

//    @Override
//    public void onBackPressed() {
//        Intent intentMain = new Intent(this, MainActivity.class);
//        startActivity(intentMain);
//        finish();
//    }



*/

    public void goBackPressed(View view) {
        onBackPressed();
    }
}



