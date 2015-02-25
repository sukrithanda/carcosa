package com.uoft.campusplannerapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
	private final String SIGNOUT = "http://104.236.85.199:8080/user/logout";

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
		loc.setX(Integer.parseInt(x));
		loc.setY(Integer.parseInt(y));
		loc.setEmail(friend);
		
		DatabaseHandler db = new DatabaseHandler(ctx);
		db.addLocation(loc);
		
		
	}

	private void InitializeLoc(){
		DatabaseHandler db = new DatabaseHandler(ctx);
		db.deleteLocations();
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
		List<FriendClass> my_friends = new ArrayList<FriendClass>();
		String friends = SendGetRequest(URL);
		if (friends.equals("Invalid Request")){
			return null;
		} else {
	    	String jsonString = "{\"friends\":" + friends.split(Pattern.quote("\n"))[0] + "}";
			Log.i("Sidd", jsonString);
			try {
				JSONObject jsnobject = new JSONObject(jsonString);
				JSONArray jsonArray = jsnobject.getJSONArray("friends");
			    for (int i = 0; i < jsonArray.length(); i++) {
			        JSONObject explrObject = jsonArray.getJSONObject(i);
			        FriendClass temp = new FriendClass();
			        temp.setFirst_name(explrObject.getString("firstName"));
			        temp.setLast_name(explrObject.getString("lastName"));
			        temp.setEmail(explrObject.getString("email"));
			        my_friends.add(temp);
			    }
			    DatabaseHandler db = new DatabaseHandler(ctx);
				db.addFriendList(my_friends);
				return my_friends;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public boolean AddFriend(String user_email, String friend_email) {
		String URL = ADD_FRIEND + "?userEmail=" + user_email + "&friendEmail=" + friend_email ;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	
	public String LocateFriend(String user_email,String friend_email) {
		String URL = LOCATE_FRIEND + "?userEmail=" + user_email + "&friendEmail=" + friend_email;
		InitializeLoc();
		String result = SendGetRequest(URL);
		if (result.equals("Invalid Request")){
			return null;
		} else {
			return result;
		}
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
	
	public boolean Logout(){
		DatabaseHandler db = new DatabaseHandler(ctx);
		User user = db.getUser();
		String URL = SIGNOUT + "?email=" + user.getEmail() + "&password=" + user.getPassword();
		String ans = SendGetRequest(URL);
		if (ans.equals("Invalid Request")){
			return false;
		} else {
			db.deleteUser();
			return true;
		}
		
	}
	

	public List<ResourceClass> getResources(String type) {
		List<ResourceClass> rsc = new ArrayList<ResourceClass>();
		ResourceClass tmp ;
		if (type.equals("Labs")) {
			tmp = new ResourceClass();
			tmp.setResource("BA2025");
			rsc.add(tmp);
			tmp = new ResourceClass();
			tmp.setResource("BA3025");
			rsc.add(tmp);
			tmp = new ResourceClass();
			tmp.setResource("BA4025");
			rsc.add(tmp);
			tmp = new ResourceClass();
			tmp.setResource("BA5025");
			rsc.add(tmp);
		} else if (type.equals("Tutorial Rooms")) {
			tmp = new ResourceClass();
			tmp.setResource("BA2125");
			rsc.add(tmp);
			tmp = new ResourceClass();
			tmp.setResource("BA2135");
			rsc.add(tmp);
			tmp = new ResourceClass();
			tmp.setResource("BA2145");
			rsc.add(tmp);
			tmp = new ResourceClass();
			tmp.setResource("BA21555");
			rsc.add(tmp);
			
		} else if (type.equals("Libraries")){
			tmp = new ResourceClass();
			tmp.setResource("BA1015");
			rsc.add(tmp);
			tmp = new ResourceClass();
			tmp.setResource("BA3175");
			rsc.add(tmp);
		} else if (type.equals("Washrooms")) {
			tmp = new ResourceClass();
			tmp.setResource("BA2225");
			rsc.add(tmp);
			tmp = new ResourceClass();
			tmp.setResource("BA8885");
			rsc.add(tmp);
			
		} else if (type.equals("Cafeteria")) {
			tmp = new ResourceClass();
			tmp.setResource("BA1115");
			rsc.add(tmp);
		}
		return rsc;
	}
	
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
        	    String result = null;
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

}
