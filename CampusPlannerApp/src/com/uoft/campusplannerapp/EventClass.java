package com.uoft.campusplannerapp;

public class EventClass {
	private long id; 
	private long user; 
	private boolean creator; 
	private boolean response; 
	private float from_time; 
	private float to_time; 
	private String location;
	private String name; 
	private String from_date; 
	private String to_date;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public float getFrom_time() {
		return from_time;
	}
	public void setFrom_time(float from_time) {
		this.from_time = from_time;
	}
	public boolean isResponse() {
		return response;
	}
	public void setResponse(boolean response) {
		this.response = response;
	}
	public boolean isCreator() {
		return creator;
	}
	public void setCreator(boolean creator) {
		this.creator = creator;
	}
	public long getUser() {
		return user;
	}
	public void setUser(long user) {
		this.user = user;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public float getTo_time() {
		return to_time;
	}
	public void setTo_time(float to_time) {
		this.to_time = to_time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFrom_date() {
		return from_date;
	}
	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}
	public String getTo_date() {
		return to_date;
	}
	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}
}
