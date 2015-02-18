package com.uoft.campusplannerapp;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.LocationSource;

public class CurrentLocationProvider implements LocationSource,LocationListener{
	
    public OnLocationChangedListener listener;
    private LocationManager locationManager;
    public Location mlocation=null;
    public CurrentLocationProvider(Context context)
    {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    public void activate(OnLocationChangedListener listener)
    {
        this.listener = listener;
        try {
        locationManager.addTestProvider("Test", false, false, false, false, false, false, false, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
        }catch (Exception IllegalArgumentException){
        
        
        }
        locationManager.setTestProviderEnabled("Test", true);
        mlocation = new Location("Test");
     //   locationManager.
      //  mlocation.
    }

    public void push_location(float lat,float longit,float alt,float accuracy,float bearing){
	  	mlocation.setAccuracy(accuracy);
	  	mlocation.setAltitude(alt);
        mlocation.setLatitude(lat);
        mlocation.setLongitude(longit);
       // mlocation.setBearing(bearing);
    	listener.onLocationChanged(mlocation);
    	
    }
    
    public void push_bearing(float bearing){
        mlocation.setBearing(bearing);
    	listener.onLocationChanged(mlocation);
    }
    @Override
    public void deactivate()
    {
        locationManager.removeUpdates(this);
    }

    public void onLocationChanged(Location location)
    {	
        if(listener != null)
        {
            
        }
    }

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}