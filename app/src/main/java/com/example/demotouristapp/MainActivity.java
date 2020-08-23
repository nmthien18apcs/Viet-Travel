package com.example.demotouristapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GridView _gridView;
    private GridViewCityAdapter _adapter;
    private ArrayList<City> _city;

    private GridView.OnItemClickListener _gridViewItemOnClick = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, FeatureActivity.class);
            City getCity= _city.get(position);
            intent.putExtra("city",getCity.get_name());
            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        initComponents();
    }
    private void loadData(){
        _city= new ArrayList<>();
        City city1 = new City("Hue");
        City city2 = new City("SaiGon");
        City city3 = new City("HaNoi");
        _city.add(city1);
        _city.add(city2);
        _city.add(city3);
    }

    private void initComponents() {
        _gridView = findViewById(R.id.gridview_city);
        _adapter = new GridViewCityAdapter(this, R.layout.gridview_city_item,_city, _city);
        _gridView.setAdapter(_adapter);
        _gridView.setOnItemClickListener(_gridViewItemOnClick);
    }
}