package com.example.demotouristapp;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ShortestPath {
    private Double bestDist;
    private ArrayList<Landmark> carsList;
    private ArrayList<Landmark> bestRoute;

    public ShortestPath(ArrayList<Landmark> tmp) {
        this.carsList = tmp;
        bestDist = 1e18;
    }
    
    public float distance (LatLng src, LatLng dsc)
    {
        Double lat_a = src.latitude, lng_a = src.longitude;
        Double lat_b = dsc.latitude, lng_b = dsc.longitude;

        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }

    public void generateShortestPath(int lhs, int rhs) {
        if (lhs == rhs)
        {
            Double cur = 0.0;
            for (int i = 1; i < carsList.size(); ++i) {
                cur += distance(carsList.get(i - 1).getLatlng(), carsList.get(i).getLatlng());
            }
            if (cur < bestDist) {
                cur = bestDist;
                bestRoute = carsList;
            }
            return;
        }
        for (int i = lhs; i <= rhs; ++i) {
            Collections.swap(carsList, lhs, i);
            generateShortestPath(lhs + 1, rhs);
            Collections.swap(carsList, lhs, i);
        }
    }
    public ArrayList<Landmark> findShortestPath() {
        generateShortestPath(0, carsList.size() - 1);
        return bestRoute;
    }
}
