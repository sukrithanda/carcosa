package com.uoft.campusplannerapp;

public class User {
	private String firstName; 
	private String lastName; 
	private String email; 
	private String token; 
	private String regId;
	private int userId; 
	private String password;
	
	public User(int user_id, String firstName, String lastName, String email, String token, 
			String regId, String password){
		this.firstName = firstName; 
		this.lastName = lastName;
		this.email = email;
		this.token = token;
		this.regId = regId; 
		this.userId = user_id;
		this.password = password;
	}
	public int getUserId(){
		return userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public String getToken() {
		return token;
	}
	public String getRegId() {
		return regId;
	}
	public String getPassword() {
		return password;
	}
}
