package com.uoft.campusplannerapp;
import com.google.android.gms.maps.model.Polyline;

public class PolyLineFloorPairs {

	    private Polyline line;
	    private int floor;

	    public PolyLineFloorPairs(Polyline line, int floor)
	    {
	        this.line   = line;
	        this.floor = floor;
	    }

	    public Polyline getLine()   { return line; }
	    public int getFloor() { return floor; }

	    
}
