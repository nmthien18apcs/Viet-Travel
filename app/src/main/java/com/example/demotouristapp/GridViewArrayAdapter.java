package com.example.demotouristapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GridViewArrayAdapter extends ArrayAdapter<Landmark> {
    private Context _context;
    private int _layoutID;
    private ArrayList<Landmark> _landmarks;

    public GridViewArrayAdapter(@NonNull Context context, int resource, @NonNull List<Landmark> objects) {
        super(context, resource, objects);
        _context = context;
        _layoutID = resource;
        _landmarks = (ArrayList<Landmark>) objects;
    }

    @Override
    public int getCount() {
        return _landmarks.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater inflater = LayoutInflater.from(_context);
            convertView = inflater.inflate(_layoutID,null, false);
        }
        ImageView imageView = convertView.findViewById(R.id.imageview_logo);
        TextView textView = convertView.findViewById(R.id.textview_title);
        Landmark lndmk= _landmarks.get(position);
        Bitmap bmp = BitmapFactory.decodeResource(_context.getResources(), lndmk.getLogoID());
        bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/12, bmp.getHeight()/12, false);
        imageView.setImageBitmap(bmp);
        textView.setText(lndmk.getName());
        return convertView;
    }
}
