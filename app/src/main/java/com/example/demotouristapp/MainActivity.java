package com.example.demotouristapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GridView _gridview;
    private ArrayList<Landmark> _landmarks;
    private ArrayList<Landmark> _routes = new ArrayList<>();
    private String _cityname="Hue";
    private GridViewArrayAdapter  _adapter;
    private GridView.OnItemClickListener _itemOnclick = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            Landmark lndmk = _landmarks.get(position);
            intent.putExtra("name",lndmk.getName());
            intent.putExtra("description",lndmk.getDescription());
            intent.putExtra("logoid",lndmk.getLogoID());
            intent.putExtra("lat",lndmk.getLatlng().latitude);
            intent.putExtra("lng",lndmk.getLatlng().longitude);
            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData(_cityname);
        initComponents();
    }

    private void loadDatafromMaps() {
        Intent intent = getIntent();
        Landmark lndmk;
        lndmk = new Landmark(intent.getStringExtra("name"), intent.getStringExtra("description"),
                intent.getIntExtra("logoid", 0),
                new LatLng(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("lng", 0)));
        Log.d("testgetlndmk","landmark "+ lndmk.getName());
        Log.d("testgetlndmk","landmark "+ lndmk.getDescription());
        Log.d("testgetlndmk","landmark "+ String.valueOf(lndmk.getLogoID()));
        Log.d("testgetlndmk","landmark "+ String.valueOf(lndmk.getLatlng().latitude));
        Log.d("testgetlndmk","landmark "+ String.valueOf(lndmk.getLatlng().longitude));
        _routes.add(lndmk);
    }


    private void loadData(String _cityname){
        _landmarks = new ArrayList<>();
        String inline="";
        try {
            InputStream is = getAssets().open("place.txt");
            int size = is.available();
            byte [] buffer = new byte[size];
            is.read(buffer);
            is.close();
            inline = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
//            Log.d("test_hue",inline);
            JSONObject _inline = new JSONObject(inline);
            JSONObject _hue = _inline.getJSONObject(_cityname);
            JSONArray _place = _hue.getJSONArray("Place");
            for(int i=0;i<_place.length();i++) {
                String _name = _place.getJSONObject(i).getString("name");
                String _imageurl = _place.getJSONObject(i).getString("imageUrl");
                String _description = _place.getJSONObject(i).getString("description");
                JSONArray _location = _place.getJSONObject(i).getJSONArray("location");
                Double lat = _location.getDouble(0);
                Double lng = _location.getDouble(1);
                int resID = getResources().getIdentifier(_imageurl , "drawable", getPackageName());
                Landmark lndmk0 = new Landmark(_name, _description,resID, new LatLng(lat,lng));
                _landmarks.add(lndmk0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void initComponents() {
        _gridview = findViewById(R.id.gridview_places);
        _adapter = new GridViewArrayAdapter(this,R.layout.gridview_item, _landmarks);
        _gridview.setAdapter(_adapter);
        _gridview.setOnItemClickListener(_itemOnclick);
        loadDatafromMaps();
    }

    public void btn_route_click(View view) {
        Intent intent = new Intent(MainActivity.this, RouteActivity.class);
        intent.putExtra("routes",_routes);
        startActivity(intent);
    }
}