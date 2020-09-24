package com.example.demotouristapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FeatureActivity extends AppCompatActivity {

    private CardView TouristAttraction_;
    private CardView Schedule_;
    private CardView Food_;
    private CardView NeededApp_;
    private String _place = new String();
    private ArrayList<Landmark> ListLandmark = new ArrayList<>();
    private CardView.OnClickListener TouristItemOnClick = new CardView.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(FeatureActivity.this, TouristAttractionActivity.class);
            intent.putExtra("cityname",_place);
            startActivity(intent);
        }
    };

    private  CardView.OnClickListener ScheduleItemOnClick = new CardView.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(ListLandmark.size() != 0) {
                Intent intent = new Intent(FeatureActivity.this, ScheduleActivity.class);
                Gson gson = new Gson();
                String jsonRoute = gson.toJson(ListLandmark);
                intent.putExtra("routes", jsonRoute);
                startActivity(intent);
            }
            else
            {
                Context context = getApplicationContext();
                CharSequence text = "Routes is Null!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    };

    private  CardView.OnClickListener FoodItemOnClick = new CardView.OnClickListener(){

        @Override
        public void onClick(View v) {
           /* Intent intent = new Intent(FeatureActivity.this, FoodActivity.class);
            startActivity(intent);*/
            Context context = getApplicationContext();
            CharSequence text = "Comming soon!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    };

    private  CardView.OnClickListener NeededAppItemOnClick = new CardView.OnClickListener(){

        @Override
        public void onClick(View v) {
           /* Intent intent = new Intent(FeatureActivity.this, NeededAppActivity.class);
            startActivity(intent);*/
            Context context = getApplicationContext();
            CharSequence text = "Comming soon!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);
        loadData();
        initComponent();
    }

    private void initComponent() {
        TouristAttraction_ = findViewById(R.id.TouristAttraction);
        TouristAttraction_.setOnClickListener(TouristItemOnClick);
        Schedule_ = findViewById(R.id.Schedule);
        Schedule_.setOnClickListener(ScheduleItemOnClick);
        Food_ = findViewById(R.id.Food);
        Food_.setOnClickListener(FoodItemOnClick);
        NeededApp_ = findViewById(R.id.NeededApp);
        NeededApp_.setOnClickListener(NeededAppItemOnClick);
    }

    private void loadData() {
        Intent intent = getIntent();
        _place = intent.getStringExtra("city");
        Log.d("testplace","place "+_place);
    }
    public void btn_home_onclick(View view) throws FileNotFoundException {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
