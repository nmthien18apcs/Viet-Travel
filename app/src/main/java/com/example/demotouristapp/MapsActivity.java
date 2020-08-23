package com.example.demotouristapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;  
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String mcity = new String();
    private Landmark mLandmark;
    private Marker mMarker;
    private TextToSpeech mText2Speech;
    private boolean mIsText2SpeechReady = true;
    private String mURL;
    private String mURL2;
    private String mURL1 = new String("?alternatives=false&geometries=geojson&steps=true&access_token=pk.eyJ1Ijoibm10aGllbjMxMDEyMDAwIiwiYSI6ImNrZTA0Nnp4YjJyeWUyem55ZzF3eHNobTMifQ.q4wLYq20JBSf1OmRJlnDvQ");
    private Location location;
    private String mLocation_start;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    private ArrayList Location = new ArrayList();
    Polyline mpolyline;
    boolean isInAsynctask = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        loadData();
        initcomponent1();
    }

    private void initcomponent1() {
            mText2Speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    mIsText2SpeechReady = true;
                }
            });
    }

    private void loadData() {
        Intent intent = getIntent();
        mcity = intent.getStringExtra("city");
        mLandmark = new Landmark(intent.getStringExtra("name"), intent.getStringExtra("description"),
                intent.getIntExtra("logoid", 0),
                new LatLng(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("lng", 0)));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap= googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }  else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    0);
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (mIsText2SpeechReady)
                {
                    mText2Speech.speak(mLandmark.getDescription(),
                            TextToSpeech.QUEUE_FLUSH, null, null);
                    Toast.makeText(getApplicationContext(),
                            mLandmark.getDescription(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                return false;
            }
        });
        displayMarker();
    }

    private void displayMarker() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), mLandmark.getLogoID());
        bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/12, bmp.getHeight()/12, false);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bmp);
        mMarker = mMap.addMarker(new MarkerOptions().position(mLandmark.getLatlng()).title(mLandmark.getName()).snippet(mLandmark.getDescription()).icon(bitmapDescriptor));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15),2000,null);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(mLandmark.getLatlng()).zoom(15).bearing(90).tilt(30).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
    public  boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    private void drawdirection(ArrayList arrayList) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(arrayList);
        polylineOptions.width(15);
        polylineOptions.color(Color.rgb(0,153,255));
        polylineOptions.geodesic(true);
        Polyline polyline = mMap.addPolyline(polylineOptions);
        mpolyline = polyline;
    }
    public void btn_direct_onclick(View view) {
            if(mpolyline!=null)
                mpolyline.remove();
            client = LocationServices.getFusedLocationProviderClient(this);
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
            }  else {
                ActivityCompat.requestPermissions(this, new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION },
                        0);
            }
            Task<Location> task =client.getLastLocation();
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    location= task.getResult();
                    mURL="https://api.mapbox.com/directions/v5/mapbox/cycling/";
                    mURL+=String.valueOf(location.getLongitude())+"%2C"+String.valueOf(location.getLatitude())+"%3B"+String.valueOf(mLandmark.getLatlng().longitude)+"%2C"+
                            String.valueOf(mLandmark.getLatlng().latitude)+mURL1;
                    Log.d("getLocation",mURL);
                    MyDirection myDirection = new MyDirection();
                    myDirection.execute();
                }
            });
    }


    public void btn_add_onclick (View view) {
        Context context = getApplicationContext();
        CharSequence text = "This place has been added to the route!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        Intent intent = new Intent();
        intent.putExtra("name",mLandmark.getName());
        intent.putExtra("description",mLandmark.getDescription());
        intent.putExtra("logoid",mLandmark.getLogoID());
        intent.putExtra("lat",mLandmark.getLatlng().latitude);
        intent.putExtra("lng",mLandmark.getLatlng().longitude);
        intent.putExtra("cityname",mcity);
        setResult(RESULT_OK,intent);
        Log.d("test1","123");
        finish();
    }

    private class MyDirection extends AsyncTask<Void, Void, ArrayList>{
        @Override
        protected void onPreExecute() {
            Location.clear();
            findViewById(R.id.btn_direct).setEnabled(false);
            super.onPreExecute();
            Log.d("testOnpre","true");
        }

        @Override
        protected ArrayList doInBackground(Void... voids) {
            String inline="";
            URL url = null;
            Log.d("testdoIN","true");
            isInAsynctask = true;
            if (isNetworkConnected())
            {
                try{
                    url = new URL(mURL);
                    Log.d("getLocation_ondoIn",mURL);
                    HttpURLConnection cnt = null;
                    cnt = (HttpURLConnection) url.openConnection();
                    cnt.setRequestMethod("GET");
                    cnt.connect();
                    Scanner sc = null;
                    sc = new Scanner(url.openStream());
                    while (sc.hasNext())
                        inline+=sc.nextLine();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            try{
                JSONObject jsoninline = new JSONObject(inline);
                JSONArray jsonArray_routes = jsoninline.getJSONArray("routes");
                JSONArray jsonArray_legs = jsonArray_routes.getJSONObject(0).getJSONArray("legs");
                JSONArray jsonArray_steps = jsonArray_legs.getJSONObject(0).getJSONArray("steps");
                for(int i = 0;i<jsonArray_steps.length();i++){
                    JSONArray jsonArray_intersections = jsonArray_steps.getJSONObject(i).getJSONArray("intersections");
                    JSONArray jsonArray_location = jsonArray_intersections.getJSONObject(0).getJSONArray("location");
                    String lng = jsonArray_location.getString(0);
                     String lat = jsonArray_location.getString(1);
                    Location.add(new LatLng(Double.valueOf(lat), Double.valueOf(lng)));
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return Location;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
            Log.d("testOnPos","true");
            drawdirection(arrayList);
            findViewById(R.id.btn_direct).setEnabled(true);
        }

    }
}