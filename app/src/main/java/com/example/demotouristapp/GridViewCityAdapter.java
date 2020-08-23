package com.example.demotouristapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GridViewCityAdapter extends ArrayAdapter<City>  {
    private  Context _context;
    private  int _layoutID;
    private ArrayList<City> _city;

    public GridViewCityAdapter(@NonNull Context context, int resource, @NonNull ArrayList<City> objects, ArrayList<City> city) {
        super(context, resource, objects);
        _context= context;
        _layoutID= resource;
        _city = city;
    }

    @Override
    public int getCount() {
        return _city.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(_context);
            convertView= layoutInflater.inflate(_layoutID,null,false);
        }

        TextView textView = convertView.findViewById(R.id.city_title);
        City getCity = _city.get(position);
        String tmp= getCity.get_name();
        if(getCity.get_name()=="SaiGon")
            tmp = "Sai Gon";
        else if(getCity.get_name()=="HaNoi")
            tmp = "Ha Noi";
        textView.setText(tmp);

        return convertView;
    }
}
