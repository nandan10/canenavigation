package com.navigation.smartcane;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import khushboo.rohit.osmnavi.R;

public class Location<MyApp> extends AppCompatActivity {

    private static Context context;
    // initializing
    // FusedLocationProviderClient
    // object
    FusedLocationProviderClient mFusedLocationClient;
    MyApp app;
    double currentLat, currentLong;
    // Initializing other items
    // from layout file
    TextView latitudeTextView, longitTextView, Location;
    int PERMISSION_ID = 44;
    // private EditText editLocation = null;

    TextToSpeech tts;
    private LocationResult locationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);

        latitudeTextView = findViewById(R.id.latTextView);
        longitTextView = findViewById(R.id.lonTextView);
        Location = findViewById(R.id.TextLocation);
        app = (MyApp) this.getApplicationContext();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
      /*  tts = new TextToSpeech(NavigationActivity1.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
//                    int result=tts.setLanguage(Locale.US);
//                    if(result==TextToSpeech.LANG_MISSING_DATA ||
//                            result==TextToSpeech.LANG_NOT_SUPPORTED){
//                        Log.e("error", "This Language is not supported");
//                    }

                        tts.speak("Your Location", TextToSpeech.QUEUE_ADD, null);


            }}
        });*/


        // method to get the location
        getLastLocation();
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {

                    @Override

                    public void onComplete(@NonNull Task<android.location.Location> task) {
                        android.location.Location location = task.getResult();
                        String cityName = null;
                        Geocoder gcd = new Geocoder(getBaseContext(),
                                Locale.getDefault());
                        List<Address> addresses;
                        try {
                            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude()
                                    , 1);
                            if (addresses.size() > 0)
                                System.out.println(addresses.get(0).getAddressLine(0) + addresses.get(0).getLocality() + addresses.get(0).getAdminArea());
                            cityName = addresses.get(0).getAddressLine(0);
                            // latitudeTextView.setText(location.getLatitude() + "");
                            // longitTextView.setText(location.getLongitude() + "");
                            // Location.setText(cityName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //    String s =location.getLongitude()+location.getLatitude() +
                        //        "My Currrent City is: "+ cityName;
                        if (location == null) {
                            requestNewLocationData();
                        } else {

                            latitudeTextView.setText(location.getLatitude() + "");
                            longitTextView.setText(location.getLongitude() + "");
                            Location.setText(cityName);
                            String finalCityName = cityName;
                        /*    tts = new TextToSpeech(Location.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    // TODO Auto-generated method stub
                                    if (status == TextToSpeech.SUCCESS) {
//                    int result=tts.setLanguage(Locale.US);
//                    if(result==TextToSpeech.LANG_MISSING_DATA ||
//                            result==TextToSpeech.LANG_NOT_SUPPORTED){
//                        Log.e("error", "This Language is not supported");
//                    }
                                        if (finalCityName == null) {
                                            tts.speak("Could Not find location info", TextToSpeech.QUEUE_ADD, null);
                                        } else {

                                            // latitudeTextView.setText(location.getLatitude() + "");
                                            //  longitTextView.setText(location.getLongitude() + "");
                                            // Location.setText(cityName);
                                            tts.speak("You are at " + finalCityName, TextToSpeech.QUEUE_ADD, null);

                                        }
                                        // tts.speak("Your Location", TextToSpeech.QUEUE_ADD, null);


                                    }
                                }
                            });*/

                        /*    if (cityName == null){
                                tts.speak("Could Not find location info", TextToSpeech.QUEUE_ADD, null);}
                            else {

                               // latitudeTextView.setText(location.getLatitude() + "");
                               //  longitTextView.setText(location.getLongitude() + "");
                                // Location.setText(cityName);
                                tts.speak("You are at " + cityName, TextToSpeech.QUEUE_ADD, null);

                            }*/
                        }
                    }

                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    // private Context context;
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            android.location.Location mLastLocation = locationResult.getLastLocation();
            //  latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            //longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
            //  private EditText editLocation = null;
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()
                        , 6);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getAddressLine(0) + addresses.get(0).getLocality() + addresses.get(0).getAdminArea());


                cityName = addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = mLastLocation.getLongitude() + mLastLocation.getLatitude() +
                    "My Currrent City is: " + cityName;
            // Location.setText(s);
            // longitTextView.setText("Address: " + cityName + "");

              /*  Address obj = getAddress(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                String fin_add = getNameFromAddress(obj, 4);
           // TextToSpeech tts = null;
            if (fin_add == null)
                    tts.speak("Could Not find location info", TextToSpeech.QUEUE_ADD, null);
                else
                    tts.speak("You are at " + fin_add, TextToSpeech.QUEUE_ADD, null);

                System.out.println(fin_add);
            }};
            public String getNameFromAddress (Address obj,int ex){
                String add = "";
                String fin_add = "";
                for (int i = 0; i <= obj.getMaxAddressLineIndex(); i++) {
                    add = add + obj.getAddressLine(i);
                    add = add + ", ";
                }
                int delimitCnt = 0;
                for (int j = 0; j < add.length(); j++) {
                    if (add.charAt(j) == ',')
                        delimitCnt++;
                    if (delimitCnt == ex)
                        break;
                    fin_add = fin_add + add.charAt(j);
                }
                return fin_add;
            }

            public Address getAddress ( double lat, double lng){
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                Address obj = null;
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                    obj = addresses.get(1);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return obj;*/
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    public void shareLoc(View view) {

        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {

                    @Override

                    public void onComplete(@NonNull Task<android.location.Location> task) {
                        android.location.Location location = task.getResult();
                        String cityName = null;
                        Geocoder gcd = new Geocoder(getBaseContext(),
                                Locale.getDefault());
                        List<Address> addresses;
                        try {
                            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude()
                                    , 1);
                            if (addresses.size() > 0)
                                System.out.println(addresses.get(0).getAddressLine(0) + addresses.get(0).getLocality() + addresses.get(0).getAdminArea());
                            cityName = addresses.get(0).getAddressLine(0);
                            // latitudeTextView.setText(location.getLatitude() + "");
                            // longitTextView.setText(location.getLongitude() + "");
                            // Location.setText(cityName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //    String s =location.getLongitude()+location.getLatitude() +



                        /*    if (cityName == null){
                                tts.speak("Could Not find location info", TextToSpeech.QUEUE_ADD, null);}
                            else {

                               // latitudeTextView.setText(location.getLatitude() + "");
                               //  longitTextView.setText(location.getLongitude() + "");
                                // Location.setText(cityName);
                                tts.speak("You are at " + cityName, TextToSpeech.QUEUE_ADD, null);

                            }*/


                            // vibe.vibrate(200);
    /*   Location mLastLocation = locationResult.getLastLocation();
       //  latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
       //longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
       //  private EditText editLocation = null;
       String cityName;
       Geocoder gcd = new Geocoder(getBaseContext(),
               Locale.getDefault());
       List<Address>  addresses;
       try {
           addresses = gcd.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()
                   , 6);
           if (addresses.size() > 0)
               System.out.println(addresses.get(0).getAddressLine(0)+addresses.get(0).getLocality()+addresses.get(0).getAdminArea());


           cityName=addresses.get(0).getAddressLine(0);*/
                            String shareBodyText;

                            shareBodyText = "Hi! I am " + " in " + cityName.toUpperCase();

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            String shareSubText = "share your location";
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);

                            startActivity(Intent.createChooser(shareIntent, "Share With"));


                        }

                });
            }

        }
    }
    public void goBackPressed(View view) {
        onBackPressed();
    }


}


