package com.example.demotouristapp;

import com.google.android.gms.maps.model.LatLng;

public class Landmark {
    private String _name;
    private String _description;
    private int _logoID;
    private LatLng _latlng;

    public LatLng getLatlng() {
        return _latlng;
    }

    public void setLatlng(LatLng latlng) {
        this._latlng = latlng;
    }

    public Landmark(String name, String description, int logoID, LatLng latlng) {
        this._name = name;
        this._description = description;
        this._logoID = logoID;
        this._latlng = latlng;
    }
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public int getLogoID() {
        return _logoID;
    }

    public void setLogoID(int logoID) {
        this._logoID = logoID;
    }
}
