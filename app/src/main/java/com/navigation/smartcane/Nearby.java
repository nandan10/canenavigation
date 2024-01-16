package com.navigation.smartcane;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.utils.HttpConnection;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import khushboo.rohit.osmnavi.R;

public class Nearby extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int REQ_CODE_SAVE_LANDMARK = 1;
    private static final int REQ_CODE_SAVE_ROUTE = 2;
    private static final int REQ_CODE_SPEECH_INPUT = 3;
    private static final int REQ_CODE_LOAD_ROUTE = 4;
    public static final int NAV_TYPE_LOAD_ROUTE = 2;
    public static final int NAV_TYPE_NAVIGATE = 0;
    public static final int NAV_TYPE_PREPLAN = 1;
    private static final String API_KEY = "AIzaSyBsq96XJwY6_aCw6uB-00R0EfPIJ0CWs0k";
    private static final String OSM_EDITING_URL = "https://api.openstreetmap.org/api/0.6//map?bbox=";
    private static final int RESULT_PICK_CONTACT = 1;
    private static final int REQ_CODE_PICK_CONTACT = 5;
    private String TAG = MainActivity.class.getSimpleName();
    private LinearLayout myLayout = null;
    MyItemizedOverlay myItemizedOverlay = null;

    TextToSpeech tts;
    Compass compass;
    SQLiteDatabase db;
    MyApp app;
    Handler h = new Handler();
    Handler appHandler = new Handler();
    int delay = 100, delayCheck = 100 ; //milliseconds
    boolean faceHeadDirection = false;
    boolean part1 = false, part2 = false, part3 = false, part4 = false;
    long time_diff = 60 * 1000;
    Button button, save_button;
    String navigatingDistance, routeName;
    int osmNumInstructions, osmNextInstruction, prefetch_nextInstruction, Instruction_progressTracker;
    int prev_id = 0;
    long tracetime = 0;
    int trackdataSerial = 0;
    int currentLandmark = -1, xingCnt = 0;
    String xingStr = "After ";
    double headingDirection = 0;
    boolean orientationCheck = true;
    boolean searchStatus;
    Place destinationLatLng;
    double currentDegree = 0;
    ArrayList<GeoPoint> landmarks;
    ArrayList<GeoPoint> saved_landmarks;
    ArrayList<GeoPoint> tags;
    ArrayList<GeoPoint> poi_tags;
    ArrayList<String> instructions;
    ArrayList<String> saved_instructions;
    ArrayList<String> tagInstructions;
    ArrayList<Integer> tagCheck;
    ArrayList<Integer> landmarkCheck;
    ArrayList<Integer> saved_landmarks_check;
    ArrayList<Integer> tagLandmark;
    ArrayList<String> poi_tagInstructions;
    ArrayList<String> crossings;
    ArrayList<Integer> crossingsLandmark;
    ArrayList<Long> timestamps;
    ArrayList<Long> tst;
    Long dir_timestamp;
    int i = 100;
    int instid=0;
    int navType, selectedRoute;
    String destinationAddress;
    double final_lat = 0;
    double final_long = 0;
    double init_lat = 0;
    double init_long = 0;
    int x=0,y=0,z=0,w=0;
    long starttime = 0;
    long turn_timestamp;
    boolean isPreplanning = false, isSpeaking = false, isConnected=false, isNavigating =false, isLocChanged = false;
    double current_lat, current_long, previous_bearing;
    GeoPoint previous_landmark;
    Road road;
TextView Nearby;
   // Button button;

    OverpassAPIProvider2 overpassProvider;
    int pathLength;
    GeoPoint endPoint,startPoint;
    OSRMRoadManager roadManager;
    Location mLastLocation;
    ImageView imageMsg;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        MediaButtonIntentReceiver r = new MediaButtonIntentReceiver();
        //  filter.setPriority(10000); //this line sets receiver priority
        // registerReceiver(r, filter);
        StrictMode.ThreadPolicy policy = null;
        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.nearby_activity);
      //  button = findViewById(R.id.NEARBY);
       // Nearby = findViewById(R.id.txtNearby1);

        landmarks = new ArrayList<GeoPoint>();
        instructions = new ArrayList<String>();
        saved_landmarks = new ArrayList<GeoPoint>();
        saved_landmarks_check = new ArrayList<Integer>();
        saved_instructions = new ArrayList<String>();
        tags = new ArrayList<GeoPoint>();
        tagInstructions = new ArrayList<String>();
        tagCheck = new ArrayList<Integer>();
        landmarkCheck = new ArrayList<Integer>();
        tagLandmark = new ArrayList<Integer>();
        poi_tags = new ArrayList<GeoPoint>();
        poi_tagInstructions = new ArrayList<String>();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            // compass.start();
        }


        buildGoogleApiClient();

        String apiKey = getString(R.string.places_api_key);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        class MyRunnable implements Runnable {
            @Override
            public void run() {

                try {



                    runOnUiThread(new Runnable() {


                        public void run() {

                           nearby();

                        }

                    });


                } catch (Exception e) {
                    Log.i("notifyUpdateTask exception", "run: " + e);
                }


            }}
        ScheduledThreadPoolExecutor exec2 = new ScheduledThreadPoolExecutor(1);

        //exec2.scheduleAtFixedRate(new MyRunnable(), 0, 1000, MILLISECONDS);
        Future<?> f1 = exec2.scheduleWithFixedDelay(new MyRunnable(), 0, 10, TimeUnit.SECONDS);

        Runnable cancelTask = new Runnable() {
            public void run() {
                f1.cancel(true);

            }
        };

       exec2.schedule(cancelTask, 20, TimeUnit.SECONDS);

    }








   public void setTts(){
        tts = new TextToSpeech(Nearby.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    isSpeaking = true;
                    tts.speak(" Please wait while we search for a suitable route.", TextToSpeech.QUEUE_ADD, null);
                    Toast.makeText(getApplicationContext()," Please wait while we search for a suitable route.", Toast.LENGTH_LONG).show();
                }
                else
                    Log.e("error", "Initialization Failed!");
            }
        });
    }


    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }



        @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(500); // Update location twice every second

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            current_lat = mLastLocation.getLatitude();
            current_long = mLastLocation.getLongitude();
            Log.i("current_lat_lng","Fetching Current LatLong  in navigation " + current_lat + ";" + current_long);
        }
        isConnected = true;
        Log.i("current_lat_lng","Connected!");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    @Override
    public void onLocationChanged(Location location) {
        isLocChanged =true;
        current_lat = location.getLatitude();
        current_long = location.getLongitude();
        //Log.i("current_lat_lng","Fetching Current LatLong  in navigation/changed " + current_lat + ";" + current_long);

    }




   public void nearby(){
        //promptSpeechInput();          Uncomment if inputting POIs by voice command
        searchPOIfromName1("name");
    }


    public  void searchPOIfromName1(String POI_type) {
        double lat_float = current_lat;
        double long_float = current_long;
        float[] results = new float[3];
        BoundingBox bb = new BoundingBox(lat_float + 0.0003, long_float + 0.0003, lat_float - 0.0003, long_float - 0.0003);
        System.out.println("Starting to find the POI : " + POI_type);

        overpassProvider = new OverpassAPIProvider2();
        String urlforpoirequest = overpassProvider.urlForPOISearch("\"" + POI_type + "\"", bb, 20, 50);
        ArrayList<POI> namePOI = overpassProvider.getPOIsFromUrl(urlforpoirequest);
      /*  if (namePOI.size() == 0) {
           // tts.speak("No " + POI_type + " nearby", TextToSpeech.QUEUE_ADD, null);
            Toast.makeText(getApplicationContext(), "No " + POI_type + " nearby", Toast.LENGTH_LONG).show();
            System.out.println("overpass returning nothing");
        }*/
        if(poi_tags == null && poi_tagInstructions == null){}

        if (namePOI != null) {
            System.out.println("Size is: " + namePOI.size());
            int ptr = 0;
            while (namePOI != null && ptr < namePOI.size()){
                Log.i("name", "name is : " + namePOI.get(ptr).mType);
                String name = namePOI.get(ptr).mType;
                if ((name != null) && (name.length() > 3)) {
                    poi_tags.add(namePOI.get(ptr).mLocation);
                    System.out.println("For POI Added: " + namePOI.get(ptr).mLocation + " " + name + " " + namePOI.get(ptr).mType);

                    name = name.replaceAll("_", " ");
//                    name = name.replaceAll("residential", "residential area");
//                    name = name.replaceAll("commercial", "commercial area");
//                    name = name.replaceAll("yes", " ");
                    String temp = " " + name + " nearby";
                    poi_tagInstructions.add(temp);
                }
                ptr++;
            }
            for (int i = 0; i < poi_tags.size(); i++) {
                if(i<3){
                    Location.distanceBetween(lat_float, long_float, poi_tags.get(i).getLatitude(), poi_tags.get(i).getLongitude(), results);
                  // tts.speak(poi_tagInstructions.get(i) + " at a distance of " + ((int) results[0]) + " metres.", TextToSpeech.QUEUE_ADD, null);
                    //Nearby.setText(poi_tagInstructions.get(i) + " at a distance of " + ((int) results[0]) + " metres.");
                   // Nearby.append(poi_tagInstructions.get(i));
                    Toast.makeText(getApplicationContext(), poi_tagInstructions.get(i) + " at a distance of " + ((int) results[0]) + " metres.", Toast.LENGTH_LONG).show();
                }

            }  // Onstop(POI_type);
        }
    }


public void Onstop(String POI_type){
  /*  double lat_float = current_lat;
    double long_float = current_long;
    float[] results = new float[3];
    BoundingBox bb = new BoundingBox(lat_float + 0.0003, long_float + 0.0003, lat_float - 0.0003, long_float - 0.0003);
    //System.out.println("Starting to find the POI : " + POI_type);

    overpassProvider = new OverpassAPIProvider2();
    String urlforpoirequest = overpassProvider.urlForPOISearch("\"" + POI_type + "\"", bb, 20, 50);
    ArrayList<POI> namePOI = overpassProvider.getPOIsFromUrl(urlforpoirequest);
    namePOI= null;*/
    poi_tags= null;
    poi_tagInstructions= null;




}}
