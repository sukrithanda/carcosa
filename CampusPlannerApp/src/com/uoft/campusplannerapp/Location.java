package com.uoft.campusplannerapp;

import java.util.Random;

public class Location {
	private String bldg ; 
	private int floor ; 
	private int x; 
	private int y; 
	private String email; 
	private boolean plot;
	
	public void GetLocation(){
		bldg = "BA";
		Random rand = new Random();
		floor = rand.nextInt(8) + 1;
		x = rand.nextInt(1000) + 1;
		y = rand.nextInt(1000) + 1;
	}
	
	public String getBldg(){
		return bldg; 
	}
	
	public int getFloor(){
		return floor; 
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setBldg(String bldg){
		this.bldg = bldg;
	}
	
	public void setFloor(int floor){
		this.floor = floor;
	}
	
	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
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


}
