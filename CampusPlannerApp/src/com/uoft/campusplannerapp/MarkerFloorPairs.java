package com.uoft.campusplannerapp;

import com.google.android.gms.maps.model.Marker;

public class MarkerFloorPairs {

	    private Marker marker;
	    private int floor;
	    private String id;

	    public MarkerFloorPairs(Marker marker, int floor, String email)
	    {
	        this.marker   = marker;
	        this.floor = floor;
	        this.id = email;
	    }

	    public Marker getMarker()   { return marker; }
	    public int getFloor() { return floor; }
	    public String getID() { return id; }

	    
}
