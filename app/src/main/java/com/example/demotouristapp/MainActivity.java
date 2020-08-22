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
        loadData();
        initComponents();
    }
    private void loadData(){
        _landmarks = new ArrayList<>();
      /*  Landmark lndmk1 = new Landmark("Bến Nhà Rồng","Nơi Bác Hồ ra đi tìm đường cứu nước năm 1911",R.drawable.logo_ben_nha_rong, new LatLng(10.768313, 106.706793));
        Landmark lndmk2 = new Landmark("Chợ Bến Thành", "Địa danh nổi tiếng qua các thời kì của Sài Gòn",R.drawable.logo_cho_ben_thanh, new LatLng(10.772535,106.698034));
        Landmark lndmk3 = new Landmark("Nhà thờ Đức Bà","Công trình kiến trúc độc đáo, nét đặc trưng của Sài Gòn",R.drawable.logo_nha_tho_duc_ba, new LatLng(10.779742,106.699188));
        Landmark lndmk4 = new Landmark("Hồ Con Rùa","Hồ Con Rùa nhưng không có rùa",R.drawable.logo_ho_con_rua, new LatLng(10.7826608,106.695915));
        _landmarks.add(lndmk1);
        _landmarks.add(lndmk2);
        _landmarks.add(lndmk3);
        _landmarks.add(lndmk4);*/
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
            JSONObject _hue = _inline.getJSONObject("Hue");
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
            Log.d("test_hue", "ex");
            e.printStackTrace();
        }

    }
    private void initComponents() {
        _gridview = findViewById(R.id.gridview_places);
        _adapter = new GridViewArrayAdapter(this,R.layout.gridview_item, _landmarks);
        _gridview.setAdapter(_adapter);
        _gridview.setOnItemClickListener(_itemOnclick);
    }
}