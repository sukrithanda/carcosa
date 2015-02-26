package com.uoft.campusplannerapp;

import java.util.Random;

public class Location {
	private String bldg ; 
	private int floor ; 
	private float latitude; 
	private float longitude; 
	private float bearing; 
	private float accuracy;
	private String email; 
	private boolean plot;
	
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
	
	public float getX(){
		return latitude;
	}
	
	public float getY(){
		return longitude;
	}
	
	public void setBldg(String bldg){
		this.bldg = bldg;
	}
	
	public void setFloor(int floor){
		this.floor = floor;
	}
	
	public void setX(int x){
		this.latitude = x;
	}

	public void setY(int y){
		this.longitude = y;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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


}
