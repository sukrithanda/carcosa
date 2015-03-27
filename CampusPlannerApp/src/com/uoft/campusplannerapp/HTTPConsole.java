package com.uoft.campusplannerapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.uoft.campusplannerapp.FriendClass;
import com.uoft.campusplannerapp.DatabaseHandler;

public class HTTPConsole {
	private final String GET_PERMISSION = "http://104.236.85.199:8080/user/getPermission";
	private final String HIDE_LOCATION = "http://104.236.85.199:8080/user/hideLocation";
	private final String SHOW_LOCATION = "http://104.236.85.199:8080/user/showLocation";
	private final String LOGIN_PAGE = "http://104.236.85.199:8080/user/login";
	private final String SIGNUP_PAGE = "http://104.236.85.199:8080/user/signup";
	private final String GET_FRIEND = "http://104.236.85.199:8080/friend/getFriends";
	private final String ADD_FRIEND = "http://104.236.85.199:8080/friend/addFriend";
	private final String LOCATE_FRIEND = "http://104.236.85.199:8080/location/locateFriend";
	private final String REMOVE_FRIEND = "http://104.236.85.199:8080/remove_friend";
	private final String REMOVE_USER = "http://104.236.85.199:8080/remove_user";
	private final String SEND_REGID = "http://104.236.85.199:8080/send_regid";
	private final String SEND_MESSAGE = "http://104.236.85.199:8080/message/sendMessage";
	private final String SEND_PRIVATE = "http://104.236.85.199:8080/send_private";
	private final String HELLO_WORLD = "http://104.236.85.199:8080/hello-world";
	private final String SEND_LOCATION = "http://104.236.85.199:8080/location/putLocation";
	private final String ADD_LOCATION = "http://104.236.85.199:8080/location/addLocation";
	private final String SIGNOUT = "http://104.236.85.199:8080/user/logout";
	private final String CREATE_EVENT = "http://104.236.85.199:8080/event/create";
	private final String GET_EVENTS = "http://104.236.85.199:8080/event/getEvents";
	private final String SET_RESPONSE = "http://104.236.85.199:8080/event/setResponse";
	private final String DELETE_EVENT = "http://104.236.85.199:8080/event/deleteEvent";
	private final String GET_INVITEES = "http://104.236.85.199:8080/event/getInvitees";
	private final String GET_RESOURCES = "http://104.236.85.199:9090/findResources";
	private final String GET_PATH = "http://104.236.85.199:9090/path";
	
	static HashMap<String,String> resourceHash = new HashMap<String,String>();
	
	static {
		resourceHash.put("Undergraduate Lab", "Lab");
		resourceHash.put("Graduate Lab", "GradLab");
		resourceHash.put("Lecture Room", "LectureRoom");
		resourceHash.put("Meeting Room", "MeetingRoom");
		resourceHash.put("Library", "Library");
		resourceHash.put("Male", "WashroomMale");
		resourceHash.put("Female", "WashroomFemale");
		resourceHash.put("Family", "Washroom");
		resourceHash.put("Study Area", "StudyArea");
		resourceHash.put("Tutorial Rooms", "TutorialRoom");
		resourceHash.put("Graduate Students Study Area", "GradStudentRoom");
		resourceHash.put("Elevator", "Elevator");
		resourceHash.put("Stairs", "Stairs");
		resourceHash.put("Cafeteria", "Cafeteria");
		resourceHash.put("Office", "Office");
		resourceHash.put("Prayer Room", "PrayerRoom");
		resourceHash.put("Exit", "Exit");
	}

	private Context ctx;
	
	public HTTPConsole(Context ctx){
		this.ctx = ctx;
	}
	
	public void SetLoc(String message){
		Log.i("Sidd", message);
		String[] params = message.split(Pattern.quote("&"));
		String friend = params[0].split(Pattern.quote("="))[1];
		String bldg = params[1].split(Pattern.quote("="))[1];
		String floor = params[2].split(Pattern.quote("="))[1];
		String x = params[3].split(Pattern.quote("="))[1];
		String y = params[4].split(Pattern.quote("="))[1];
		Log.i("Sidd", friend+bldg+floor+x+y);
		Location loc = new Location();
		loc.setBldg(bldg);
		loc.setFloor(Integer.parseInt(floor));
		loc.setLatitude(Float.parseFloat(x));
		loc.setLongitude(Float.parseFloat(y));
		//loc.setUser(friend);
		// get id from friend and set it
		DatabaseHandler db = new DatabaseHandler(ctx);
		FriendClass fr = db.getFriendFromEmail(friend);
		if(fr!=null){
			loc.setUser_id(fr.getUser_id());
			db.addLocation(loc);
		}
		db.Close();
		
	}

	private void InitializeLoc(){
		DatabaseHandler db = new DatabaseHandler(ctx);
		db.deleteLocations();
		db.Close();
	}
	public String HelloWorld(){
		return SendGetRequest(HELLO_WORLD);
	}
	
	public Boolean LoginRequest(String email, String password, String reg_id){
		String URL = LOGIN_PAGE + "?email=" + email + "&password=" + password + "&regid=" + reg_id;
		String result = SendGetRequest(URL);
		if (result.equals("Invalid Request")){
			return false;
		} else {
			try {
				JSONObject obj = new JSONObject(result);
				String user_id = obj.getString("user_id");
				Integer i_user_id = Integer.parseInt(user_id);
				String user = obj.getString("email");
				String first_name = obj.getString("firstName");
				String last_name = obj.getString("lastName");
				String token = obj.getString("token");
				User usero = new User(i_user_id,first_name,last_name,user,token,reg_id, password);
				DatabaseHandler db = new DatabaseHandler(ctx);
				db.addUser(usero);
				db.Close();
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
				return true;
			}
		}

	}
	
	public Boolean SignupRequest(String email, String password, String first_name, String last_name){
		String URL = SIGNUP_PAGE + "?first=" + first_name + "&last=" + last_name + "&email=" + 
						email + "&password=" + password;
		String ans = SendGetRequest(URL);
		if (ans.equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	
	public List<FriendClass> GetFriend(String email){
		String URL = GET_FRIEND + "?userEmail=" + email;
		String friends = SendGetRequest(URL);
		return getFriendFromString(friends); 
	}
	
	public boolean AddFriend(String user_email, String friend_email) {
		String URL = ADD_FRIEND + "?userEmail=" + user_email + "&friendEmail=" + friend_email ;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	
	public Location LocateFriend(String user_email,String friend_email) {
		String URL = LOCATE_FRIEND + "?userEmail=" + user_email + "&friendEmail=" + friend_email;
		InitializeLoc();
		String result = SendGetRequest(URL);
		return GetLocationFromString(result);
	}
	
	public boolean RemoveFriend(String user_email, String friend_email) {
		String URL = REMOVE_FRIEND + "?user_email=" + friend_email + "&friend_email=" + friend_email;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	
	public boolean RemoveUser(String user_email) {
		String URL = REMOVE_USER + "?user_email=" + user_email;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	

	public boolean SendRegId(String user_email, String reg_id) {
		String URL = SEND_REGID + "?user_email=" + user_email + "&reg_id=" + reg_id;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	

	public boolean SendMessage(String user_email, String friend_email, String message) {
		String URL = SEND_MESSAGE + "?userEmail=" + user_email + "&friendEmail=" + friend_email + 
				"&message=" + message;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	

	public boolean SendPrivate(String user_email, String private_key) {
		String URL = SEND_PRIVATE + "?user_email=" + user_email + "&private_key=" + private_key;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	
	public boolean SendLocation(String email, String building, String floor, String x, String y) {
		String URL = SEND_LOCATION + "?friendEmail=" + email + "&building=" + building + "&floor=" + floor + 
				"&x=" + x+ "&y=" + y ;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	
	
	public boolean AddLocation(String building, String floor, String x, String y, String accuracy, 
			String bearing) {

		DatabaseHandler db = new DatabaseHandler(ctx);
		User user = db.getUser();
		db.Close();
		String URL = ADD_LOCATION + "?email=" + user.getEmail() + "&floor=" + floor + "&building=" + building + 
				"&x=" + x+ "&y=" + y ;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	
	public boolean Logout(){
		DatabaseHandler db = new DatabaseHandler(ctx);
		User user = db.getUser();
		String URL = SIGNOUT + "?email=" + user.getEmail() + "&password=" + user.getPassword();
		String ans = SendGetRequest(URL);
		if (ans.equals("Invalid Request")){
			db.Close();
			return false;
		} else {
			db.deleteUser();
			db.Close();
			return true;
		}
		
	}
	
	public List<ResourceClass> getResources(String looseType) {
		String type = resourceHash.get(looseType);
		DatabaseHandler db = new DatabaseHandler(ctx);
		User user = db.getUser();
		Location loc = db.getLocationFromId(user.getUserId());
		double lat = 43.659779,  longi = -79.397339;
		String URL = GET_RESOURCES + "/" + type + "/" + lat + "/"+ longi + "/" + 4;
//		String URL = GET_RESOURCES + "/" + type + "/" + loc.getLatitude() + "/"+ loc.getLongitude() + "/" 
//				+ loc.getFloor();
		System.out.println(URL);
		String ans = SendGetRequest(URL);
		return GetResourcesFromString(ans);
	}
	
	public List<ResourceClass> getPath(String destination) {
		DatabaseHandler db = new DatabaseHandler(ctx);
		User user = db.getUser();
		System.out.println("DEBUG - IN HTTPCONSOLE GET PATH");

		Location loc = db.getLocationFromId(user.getUserId());
		double lat = 43.659779,  longi = -79.397339;
		String URL = GET_PATH + "/" + lat + "/"+ longi + "/" + 4 + "/"+ destination;
//		String URL = GET_PATH + "/" +  loc.getLatitude() + "/"+ loc.getLongitude() + "/" + loc.getFloor() 
//				+ "/" + destination;
		System.out.println(URL);
		System.out.println("DEBUG - SENDING GET REQUEST");

		String ans = SendGetRequest(URL);
		System.out.println("DEBUG - REQUEST RETURNED");

		return GetResourcesFromString(ans);
	}
	
	public boolean HideLocation() {
		DatabaseHandler db = new DatabaseHandler(ctx);
		User user = db.getUser();
		db.Close();
		String URL = HIDE_LOCATION + "?email=" + user.getEmail() + "&password=" + user.getPassword();
		String ans = SendGetRequest(URL);
		if (ans.equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	
	public boolean ShowLocation() {
		DatabaseHandler db = new DatabaseHandler(ctx);
		User user = db.getUser();
		db.Close();
		String URL = SHOW_LOCATION + "?email=" + user.getEmail() + "&password=" + user.getPassword();
		String ans = SendGetRequest(URL);
		if (ans.equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	 
	/* Returns "Invalid Request" if there was some error
	 *         "Hide" if location is hidden
	 *         "Show" if location is shown
	 */
	public String GetPermission() {
		DatabaseHandler db = new DatabaseHandler(ctx);
		User user = db.getUser();
		db.Close();
		String URL = GET_PERMISSION + "?email=" + user.getEmail() + "&password=" + user.getPassword();
		String ans = SendGetRequest(URL);
		String anss[] = ans.split("\n");
		return anss[0];
	}
	
	/* EVENT REQUESTS START*/
	// time must be string in format hh.mm
	public boolean CreateEventRequest(/*List<FriendClass> friends*/String friendsStr, String from_time, String to_time, 
			String location, String name, String from_date, String to_date ){
		DatabaseHandler db = new DatabaseHandler(ctx);
		User user = db.getUser();
		db.Close();
		String email = user.getEmail();
		/*String friendsStr = "";
		int i = 0; 
		for (i = 0; i < friends.size(); i++) {
			if (i != 0){
				friendsStr += ";";
			}
			friendsStr += friends.get(i);
		}*/
		String URL = CREATE_EVENT + "?friends=" + friendsStr + "&email=" + email +"&from_date=" + from_date +
				"&to_date=" + to_date +  "&from_time=" + from_time +
				"&to_time=" + to_time + "&location=" + location + "&name=" + name.replaceAll(" ", "%20");
		String ans = SendGetRequest(URL);
		if (ans.equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	
	public List<EventClass> GetEvents(){
		DatabaseHandler db = new DatabaseHandler(ctx);
		User user = db.getUser();
		db.Close();
		String URL = GET_EVENTS + "?email=" + user.getEmail();
		String ans = SendGetRequest(URL);
		return GetEventsFromString(ans);
		
		
	}

	public List<EventClass> GetInvitees(long event_id) {
		String URL = GET_INVITEES + "?event_id=" + event_id;
		String friends = SendGetRequest(URL);
		return GetEventsFromString(friends);
	}
	
	public boolean SetResponse(long event_id, boolean response) {
		DatabaseHandler db = new DatabaseHandler(ctx);
		User user = db.getUser();
		db.Close();
		String responseStr = "";
		if (response) {
			responseStr = "true";
		} else {
			responseStr = "false";
		}
		String URL = SET_RESPONSE + "?event_id=" + event_id + "&email=" + user.getEmail() + "&response=" + responseStr;
		String ans = SendGetRequest(URL);
		if (ans.equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	
	public boolean DeleteEvent(long event_id) {
		DatabaseHandler db = new DatabaseHandler(ctx);
		User user = db.getUser();
		db.Close();
		String URL = DELETE_EVENT + "?event_id=" + event_id + "&user=" + user.getUserId();
		String ans = SendGetRequest(URL);
		if (ans.equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}

	/* EVENT REQUESTS END*/
	
	@SuppressWarnings("unused")
	private Boolean SendPostRequest(String URL, final List<NameValuePair> nameValuePairs) {
		AsyncTask<String, Void, Boolean> a = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... URL) {

        		HttpClient httpclient = new DefaultHttpClient();
        	    HttpPost httppost = new HttpPost(URL[0]);
        	
        	    try {
        	        // Add your data
        	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        	
        	        // Execute HTTP Post Request
        	        HttpResponse response = httpclient.execute(httppost);
        	        int status = response.getStatusLine().getStatusCode();
        	        if (status == 0){
        	        	return true;
        	        } else {
        	        	return false;
        	        }
        	        
        	        
        	    } catch (ClientProtocolException e) {
        	    } catch (IOException e) {
        	    }
        		return false;

            }

            @Override
            protected void onPostExecute(Boolean msg) {
            	return;
            }
        }.execute(null, null, null);
        try {
			return a.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String SendGetRequest(String URL) {
		AsyncTask<String, Void, String> a = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... URL) {
        		HttpClient client = new DefaultHttpClient();
        	    HttpGet request = new HttpGet(URL[0]);
        	    HttpResponse response;
        	    String result = "Invalid Request";
        	    try {
        	        response = client.execute(request);
        	        Log.e("com.uoft.campusplannerapp", "Hi "+response.getStatusLine().toString());
        	        HttpEntity entity = response.getEntity();
        	        
        	        if (entity != null) {

        	            // A Simple JSON Response Read
        	            InputStream instream = entity.getContent();
        	            Log.e("com.uoft.campusplannerapp", "Hi "+response.getStatusLine().toString());
        	            System.out.println(response.getStatusLine().toString());
        	            if (response.getStatusLine().getStatusCode() == 200) {
            	            result = convertStreamToString(instream);
        	            } else {
        	            	result = "Invalid Request";
        	            }
        	            // now you have the string representation of the HTML request
        	            System.out.println("RESPONSE: " + result);
        	            instream.close();

        	        }
        	    } catch (ClientProtocolException e1) {
        	        e1.printStackTrace();
        	    } catch (IOException e1) {
        	        e1.printStackTrace();
        	    } catch (Exception all) {
        	    	all.printStackTrace();
        	    	
        	    }
        	    return result;

            }

            @Override
            protected void onPostExecute(String msg) {
            	return;
            }
        }.execute(URL);
        try {
			return a.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "Invalid Request";
		} catch (ExecutionException e) {
			e.printStackTrace();
			return "Invalid Request";
		}
}
	
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}

	private List<FriendClass> getFriendFromString(String friends) {

		List<FriendClass> my_friends = new ArrayList<FriendClass>();
		if (friends.equals("Invalid Request")){
			return null;
		} else {
	    	String jsonString = "{\"friends\":" + friends.split(Pattern.quote("\n"))[0] + "}";
			Log.i("Sidd", jsonString);
			try {
				JSONObject jsnobject = new JSONObject(jsonString);
				JSONArray jsonArray = jsnobject.getJSONArray("friends");
				if (null == jsonArray) {
					return null;
				}
			    for (int i = 0; i < jsonArray.length(); i++) {
			        JSONObject explrObject = jsonArray.getJSONObject(i);
			        FriendClass temp = new FriendClass();
			        temp.setFirst_name(explrObject.getString("firstName"));
			        temp.setLast_name(explrObject.getString("lastName"));
			        temp.setEmail(explrObject.getString("email"));
			        temp.setUser_id(Long.parseLong(explrObject.getString("user_id")));
			        my_friends.add(temp);
			    }
			    DatabaseHandler db = new DatabaseHandler(ctx);
				db.addFriendList(my_friends);
				db.Close();
				return my_friends;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	private List<EventClass> GetEventsFromString(String ans) {
		List<EventClass> events = new ArrayList<EventClass>();
		
		if (ans.equals("Invalid Request")){
			return null;
		} else {
	    	String jsonString = "{\"events\":" + ans.split(Pattern.quote("\n"))[0] + "}";
			Log.i("Sidd", jsonString);
			try {
				JSONObject jsnobject = new JSONObject(jsonString);
				JSONArray jsonArray = jsnobject.getJSONArray("events");
			    for (int i = 0; i < jsonArray.length(); i++) {
			        JSONObject explrObject = jsonArray.getJSONObject(i);
			        EventClass temp = new EventClass();
			        
			        String creator = explrObject.getString("creator");
			        if (creator.equals("true")) {
			        	temp.setCreator(true);
			        } else {
			        	temp.setCreator(false);
			        }
			        
			        long response = explrObject.getLong("response");
			        temp.setResponse(response);
			        
			        temp.setFrom_time(Float.parseFloat(explrObject.getString("from_time")));
			        temp.setTo_time(Float.parseFloat(explrObject.getString("to_time")));
			        temp.setFrom_date(explrObject.getString("from_date"));
			        temp.setTo_date(explrObject.getString("to_date"));

			        temp.setId(Long.parseLong(explrObject.getString("event_id")));
			        temp.setUser(Long.parseLong(explrObject.getString("user")));
			        temp.setLocation(explrObject.getString("location"));
			        temp.setName(explrObject.getString("name").replaceAll("%20"," "));
			        
			        events.add(temp);
			    }
				return events;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	private Location GetLocationFromString(String result) {
		if (result == null) 
			return null;
		Location loc = new Location();
		if (result.equals("Invalid Request")){
			return null;
		} else {
			try {
				JSONObject obj = new JSONObject(result);
				loc.setUser_id(obj.getInt("user_id"));
				loc.setLatitude(Float.parseFloat(obj.getString("x_coordinate")));
				loc.setLongitude(Float.parseFloat(obj.getString("y_coordinate")));
				loc.setBldg(obj.getString("building"));
				loc.setFloor(Integer.parseInt(obj.getString("floor")));
				return loc;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		
	}
	

	
	private List<ResourceClass> GetResourcesFromString(String ans) {
		List<ResourceClass> resources = new ArrayList<ResourceClass>();
		
		if (ans.equals("Invalid Request")){
			return null;
		} else {
			if (ans.substring(ans.length() - 1).equals(",")) {
				ans = ans.substring(0, ans.length() - 1);
			}
			String withoutResource = ans.replaceAll("Resource", "");
	    	String jsonString = "{\"resources\":[" + withoutResource.split(Pattern.quote("\n"))[0] + "]}";
			Log.i("Sidd", jsonString);
			try {
				JSONObject jsnobject = new JSONObject(jsonString);
				JSONArray jsonArray = jsnobject.getJSONArray("resources");
			    for (int i = 0; i < jsonArray.length(); i++) {
			        JSONObject explrObject = jsonArray.getJSONObject(i);
			        ResourceClass temp = new ResourceClass();
			        Location loctemp = new Location();
			        loctemp.setFloor(Integer.parseInt(explrObject.getString("floor")));
			        loctemp.setLatitude(Float.parseFloat(explrObject.getString("entrance_lat")));
			        loctemp.setLongitude(Float.parseFloat(explrObject.getString("entrance_long")));
			        loctemp.setBldg(explrObject.getString("building"));
			        loctemp.setBearing(0);
			        loctemp.setAccuracy(0);
			        loctemp.setUser_id(-1);
			        loctemp.setPlot(false);
			        
			        temp.setLoc(loctemp);
			        temp.setResource(explrObject.getString("id"));
			        temp.setType(explrObject.getString("type"));
			        temp.setDescription(explrObject.getString("description"));
			        resources.add(temp);
			    }
				return resources;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	

}
