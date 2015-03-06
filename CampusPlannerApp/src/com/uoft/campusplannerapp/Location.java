package com.uoft.campusplannerapp;

import java.util.Random;

public class Location {
	private String bldg ; 
	private int floor ; 
	private float latitude; 
	private float longitude; 
	private float bearing; 
	private float accuracy;
	private long user_id;
	private boolean plot;
	
	public Location(String bldg, int floor, float latitude, float longitude, long user_id){
		this.bldg = bldg;
		this.floor = floor;
		this.latitude = latitude;
		this.longitude = longitude;
		this.user_id = user_id;
		plot = true;
	}
	
	public Location() {
		;// TODO Auto-generated constructor stub
	}

	public void GetLocation(){
		bldg = "BA";
		Random rand = new Random();
		floor = rand.nextInt(8) + 1;
		latitude = rand.nextInt(1000) + 1;
		longitude = rand.nextInt(1000) + 1;
	}
	
	public String getBldg(){
		return bldg; 
	}
	
	public int getFloor(){
		return floor; 
	}
	
	public float getLatitude(){
		return latitude;
	}
	
	public float getLongitude(){
		return longitude;
	}
	
	public void setBldg(String bldg){
		this.bldg = bldg;
	}
	
	public void setFloor(int floor){
		this.floor = floor;
	}
	
	public void setLatitude(float x){
		this.latitude = x;
	}

	public void setLongitude(float y){
		this.longitude = y;
	}

	public boolean isPlot() {
		return plot;
	}

	public void setPlot(boolean plot) {
		this.plot = plot;
	}
	

	public boolean getPlot() {
		return plot;
	}

	public float getBearing() {
		return bearing;
	}

	public void setBearing(float bearing) {
		this.bearing = bearing;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}


}
