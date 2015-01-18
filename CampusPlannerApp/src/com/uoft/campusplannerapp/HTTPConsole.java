package com.uoft.campusplannerapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class HTTPConsole {
	private final String LOGIN_PAGE = "http://104.236.85.199:8080/user/login";
	private final String SIGNUP_PAGE = "http://104.236.85.199:8080/user/signup";
	private final String GET_FRIEND = "http://104.236.85.199:8080/get_friend";
	private final String ADD_FRIEND = "http://104.236.85.199:8080/add_friend";
	private final String LOCATE_FRIEND = "http://104.236.85.199:8080/location/locateFriend";
	private final String REMOVE_FRIEND = "http://104.236.85.199:8080/remove_friend";
	private final String REMOVE_USER = "http://104.236.85.199:8080/remove_user";
	private final String SEND_REGID = "http://104.236.85.199:8080/send_regid";
	private final String SEND_MESSAGE = "http://104.236.85.199:8080/send_message";
	private final String SEND_PRIVATE = "http://104.236.85.199:8080/send_message";
	private final String HELLO_WORLD = "http://104.236.85.199:8080/hello-world";
	private final String SEND_LOCATION = "http://104.236.85.199:8080/location/putLocation";
	// userEmail , building, floor, x,y

	private Context ctx;
	
	public HTTPConsole(Context ctx){
		this.ctx = ctx;
	}
	
	public void SetLoc(String message){
		SharedPreferences pref = ctx.getSharedPreferences("Locations",Context.MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString("result", message);
		edit.commit();
	}
	
	private String PollAndGetLoc(){
		SharedPreferences pref = ctx.getSharedPreferences("Locations",Context.MODE_PRIVATE);
		String result = pref.getString("result", null);
		while (result == null){
			try {
				Thread.sleep(1000);
				Log.i("Sidd", "Slept");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("Sidd", "Polling and Polling");
			result = pref.getString("result", null);
		}
		return result;
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
				String user = obj.getString("email");
				String first_name = obj.getString("first_name");
				String last_name = obj.getString("last_name");
				String token = obj.getString("token");
				Editor edit = ctx.getSharedPreferences("User", Context.MODE_PRIVATE).edit();
				edit.putString("user", user);
				edit.putString("first_name", first_name);
				edit.putString("last_name", last_name);
				edit.putString("token", token);
				edit.commit();
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
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
	
	public List<String> GetFriend(String email){
		String URL = GET_FRIEND + "?email=" + email;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return null;
		} else {
			return null;
		}
	}
	
	public boolean AddFriend(String user_email, String friend_email) {
		String URL = ADD_FRIEND + "?email=" + user_email + "&friend_email=" + friend_email ;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	
	public String LocateFriend(String user_email,String friend_email) {
		String URL = LOCATE_FRIEND + "?userEmail=" + user_email + "&friendEmail=" + friend_email;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return null;
		} else {
			//return null;
			return PollAndGetLoc();
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
		String URL = SEND_MESSAGE + "?user_email=" + user_email + "&friend_email=" + friend_email + 
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
	
	
	@SuppressWarnings("unused")
	private Boolean SendPostRequest(String URL, final List<NameValuePair> nameValuePairs) {
		AsyncTask<String, Void, Boolean> a = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... URL) {

        		HttpClient httpclient = new DefaultHttpClient();
        	    HttpPost httppost = new HttpPost(URL[0]);
        	
        	    try {
        	        // Add your data
//        	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//        	        nameValuePairs.add(new BasicNameValuePair("data1", data1));
//        	        nameValuePairs.add(new BasicNameValuePair("data2", data2));
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
        	        // TODO Auto-generated catch block
        	    } catch (IOException e) {
        	        // TODO Auto-generated catch block
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
        	        // TODO Auto-generated catch block
        	        e1.printStackTrace();
        	    } catch (IOException e1) {
        	        // TODO Auto-generated catch block
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
