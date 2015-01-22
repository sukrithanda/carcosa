package com.uoft.campusplannerapp;

import java.util.Random;

public class LocateDevice {
	private String bldg ; 
	private int floor ; 
	private int x; 
	private int y; 
	
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

}
