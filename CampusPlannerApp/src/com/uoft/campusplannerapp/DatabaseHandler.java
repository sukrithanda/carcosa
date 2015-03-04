package com.uoft.campusplannerapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.uoft.campusplannerapp.User;
import com.uoft.campusplannerapp.FriendClass;
import com.uoft.campusplannerapp.Location;


public class DatabaseHandler extends SQLiteOpenHelper {
	
	private static final String DB = "uplan";
	private static final int VERSION = 1;
	
	public DatabaseHandler(Context context) {
		super(context, DB, null, VERSION);
		this.onCreate(getWritableDatabase());
	}
	
	public void Close(){
		close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String user = "CREATE TABLE IF NOT EXISTS user" + 
				"( user_id INTEGER PRIMARY KEY, " + 
				"first_name TEXT, " + 
				"last_name TEXT, " + 
				"email TEXT, " + 
				"token TEXT, " + 
				"reg_id TEXT, " + 
				"password TEXT)";
		String friends = "CREATE TABLE IF NOT EXISTS friends" + 
				"( friend_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				"first_name TEXT, " + 
				"last_name TEXT, " + 
				"email TEXT )";
		String locations = "CREATE TABLE IF NOT EXISTS locations" + 
				"( location_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				"email TEXT, " +
				"bldg TEXT, " + 
				"floor INTEGER, " + 
				"x INTEGER, " + 
				"y INTEGER, " + 
				"plotted INTEGER)" ;
		try {
			db.execSQL(user);
			db.execSQL(friends);
			db.execSQL(locations);
			Log.i("Sidd","Tables Created");
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("Sidd",e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS user");
		db.execSQL("DROP TABLE IF EXISTS friends");
		db.execSQL("DROP TABLE IF EXISTS locations");
		Log.i("Sidd","Tables Deleted");
		
	}
	
	public void deleteDatabses(){
		SQLiteDatabase db = this.getWritableDatabase();
		try {
		db.execSQL("DROP TABLE IF EXISTS user");
		db.execSQL("DROP TABLE IF EXISTS friends");
		db.execSQL("DROP TABLE IF EXISTS locations");
		} catch (Exception e) {
			Log.i("Sidd",e.getMessage());
		}
		db.close();
	}
	
	public void deleteLocations(){
		Log.i("Sidd","deleting locations");
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS locations");
		db.close();
	}
	
	public void deleteUser(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS user");
		db.close();
	}
	
	public void deleteFriends(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS friends");
		db.close();
	}
	
	
	public boolean addUser(User current){
		Log.i("UPlan","Add User to the database");
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * FROM user";
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			Log.i("UPlan", "User already exists");
			return false; 
		}
		ContentValues values = new ContentValues();
		values.put("user_id", current.getUserId());
		values.put("first_name", current.getFirstName());
		values.put("last_name", current.getLastName());
		values.put("email", current.getEmail());
		values.put("reg_id", current.getRegId());
		values.put("token", current.getToken());
		values.put("password", current.getPassword());
		db.insert("user", null, values);
		db.close();
		return true;
	}
	
	public User getUser(){
		Log.i("UPlan","Get Current User");
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * FROM user";
		Cursor cursor = db.rawQuery(query, null);
		User user = null;
		if (cursor.moveToFirst()){
			int user_id = Integer.parseInt(cursor.getString(0));
			String first_name = cursor.getString(1);
			String last_name = cursor.getString(2);
			String email = cursor.getString(3);
			String reg_id = cursor.getString(4);
			String token = cursor.getString(5);
			String password = cursor.getString(6);
			user = new User(user_id, first_name, last_name, email, token, reg_id, password);
			db.close();
			return user;
		}
		db.close();
		return null;
	}
	
	public List<FriendClass> getFriends(){
		Log.i("UPlan","Get All Friends");
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * FROM friends";
		Cursor cursor = db.rawQuery(query, null);
		List<FriendClass> fl = new ArrayList<FriendClass>();
		if (cursor.moveToFirst()){
			do {
				FriendClass friend = new FriendClass();
				friend.setFirst_name(cursor.getString(1));
				friend.setLast_name(cursor.getString(2));
				friend.setEmail(cursor.getString(3));
				fl.add(friend);
			} while (cursor.moveToNext());
		}
		db.close();
		return fl;
	}
	
	public boolean addFriendList(List<FriendClass> fl){
		Log.i("UPlan","Add list of friends");
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		for (FriendClass f : fl){
			values.put("first_name", f.getFirst_name());
			values.put("last_name", f.getLast_name());
			values.put("email",f.getEmail());
			db.insert("friends", null, values);
		}
		db.close();
		return true;	
	}
	
	public boolean addFriend(FriendClass friend) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("first_name", friend.getFirst_name());
		values.put("last_name", friend.getLast_name());
		values.put("email",friend.getEmail());
		db.insert("friends", null, values);
		db.close();
		return true;
	}
	
	public List<Location> getLocations(){
		Log.i("UPlan","Get All Friends");
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * FROM locations";
		Cursor cursor = db.rawQuery(query, null);
		List<Location> locations = new ArrayList<Location>();
		if (cursor.moveToFirst()){
			do {
				Location loc = new Location();
				try {
					loc.setEmail(cursor.getString(1));
					loc.setBldg(cursor.getString(2));
					loc.setFloor(Integer.parseInt(cursor.getString(3)));
					loc.setX(Integer.parseInt(cursor.getString(4)));
					loc.setY(Integer.parseInt(cursor.getString(5)));
					int plot = Integer.parseInt(cursor.getString(6));
					loc.setPlot(plot==1?true:false);
					locations.add(loc);
				} catch (Exception e){
					e.printStackTrace();
				}
			} while (cursor.moveToNext());
		}
		db.close();
		return null;
	}
	
	public boolean addLocation(Location loc){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("email", loc.getEmail());
		values.put("bldg", loc.getBldg());
		values.put("floor", "" + loc.getFloor());
		values.put("x", "" + loc.getX());
		values.put("y", "" + loc.getY());
		values.put("plotted", "" + 0);
		db.insert("locations", null, values);
		db.close();
		Log.i("Sidd", "Set Email, bldg, floor, x, y, 0"+ loc.getEmail() + loc.getBldg() + loc.getFloor() +  
				loc.getX() + loc.getY());
		return true;
	}
	
	public boolean updatePlotted(Location loc){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("email", loc.getEmail());
		values.put("bldg", loc.getBldg());
		values.put("floor", "" + loc.getFloor());
		values.put("x", "" + loc.getX());
		values.put("y", "" + loc.getY());
		values.put("plotted", "" + 1);
		int i = db.update("locations", values, "email=?", new String[]{loc.getEmail()});
		db.close();
		return i==1?true:false;
	}

}
