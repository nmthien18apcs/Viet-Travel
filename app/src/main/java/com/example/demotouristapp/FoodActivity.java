package com.example.demotouristapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.SortedMap;

public class FoodActivity extends AppCompatActivity {
    private GridView _gridview;
    private ArrayList<Landmark> _landmarks;
    private ArrayList<Landmark> _restaurant = new ArrayList<>();
    private String _cityname="";
    private GridViewArrayAdapter  _adapter;
    private GridView.OnItemClickListener _itemOnclick = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(FoodActivity.this, MapsActivity.class);
            Landmark lndmk = _landmarks.get(position);
            intent.putExtra("name",lndmk.getName());
            intent.putExtra("description",lndmk.getDescription());
            intent.putExtra("logoid",lndmk.getLogoID());
            intent.putExtra("lat",lndmk.getLatlng().latitude);
            intent.putExtra("lng",lndmk.getLatlng().longitude);
            intent.putExtra("city",_cityname);
            startActivityForResult(intent,123);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_food);
        loadData();
        initComponents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 123)
        {
            Landmark lndmk;
            lndmk = new Landmark(intent.getStringExtra("name"), intent.getStringExtra("description"),
                    intent.getIntExtra("logoid", 0),
                    new LatLng(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("lng", 0)));
            _restaurant.add(lndmk);
        }
    }

    private void loadData(){
        _landmarks = new ArrayList<>();
        Intent intent = getIntent();
        Log.d("testload","a "+_cityname);
        _cityname = intent.getStringExtra("cityname");
        Log.d("testload","a "+_cityname);
        String inline="";
        try {
            InputStream is = getAssets().open("restaurant.txt");
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
            Log.d("testcityname",_cityname);
            JSONArray _place = _hue.getJSONArray("Restaurant");
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
        _gridview = findViewById(R.id.gridview_restaurant);
        _adapter = new GridViewArrayAdapter(this,R.layout.gridview_item, _landmarks);
        _gridview.setAdapter(_adapter);
        _gridview.setOnItemClickListener(_itemOnclick);
    }

    public void btn_back_onclick(View view) {
        Log.d("aaa",String.valueOf(_restaurant.size()));
        Gson gson = new Gson();
        String jsonCars = gson.toJson(_restaurant);
        Intent intent = new Intent(FoodActivity.this, FeatureActivity.class);
        intent.putExtra("routes", jsonCars);
        intent.putExtra("city", _cityname);
        Log.d("testroute", jsonCars);
        startActivity(intent);
        String textToSave = jsonCars;
        try {
            FileOutputStream fileOutputStream = openFileOutput("routes.txt", MODE_PRIVATE);
            fileOutputStream.write(textToSave.getBytes());
            fileOutputStream.close();
            Log.e("save:", "Done");
        } catch (FileNotFoundException e) {
            Log.e("Save: ", "cant find routes.txt");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Save: ", "cant save routes.txt");
        }
    }
}