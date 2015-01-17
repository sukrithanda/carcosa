package com.uoft.campusplannerapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import org.apache.http.message.BasicNameValuePair;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class HTTPConsole {
	private final String LOGIN_PAGE = "http://104.236.85.199:8080/login";
	private final String SIGNUP_PAGE = "http://104.236.85.199:8080/sigup";
	private final String GET_FRIEND = "http://104.236.85.199:8080/get_friend";
	private final String ADD_FRIEND = "http://104.236.85.199:8080/add_friend";
	private final String LOCATE_FRIEND = "http://104.236.85.199:8080/locate_fried";
	private final String REMOVE_FRIEND = "http://104.236.85.199:8080/remove_friend";
	private final String REMOVE_USER = "http://104.236.85.199:8080/remove_user";
	private final String SEND_REGID = "http://104.236.85.199:8080/send_regid";
	private final String SEND_MESSAGE = "http://104.236.85.199:8080/send_message";
	private final String SEND_PRIVATE = "http://104.236.85.199:8080/send_message";
	private final String HELLO_WORLD = "http://104.236.85.199:8080/hello-world";
	
	public String HelloWorld(){
		return SendGetRequest(HELLO_WORLD);
	}
	
	public Boolean LoginRequest(String email, String password, String reg_id){
		String URL = LOGIN_PAGE + "?email=" + email + "&password=" + password + "&regid=" + reg_id;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
//		try {
//			JSONObject obj = new JSONObject(result);
//			int status = Integer.parseInt(obj.getString("status"));
//			if (status == 0){
//				
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
	}
	
	public Boolean SignupRequest(String email, String password, String first_name, String last_name){
		String URL = SIGNUP_PAGE + "?email=" + email + "&password=" + password + "&first_name=" + 
						first_name + "&last_name" + last_name;
		if (SendGetRequest(URL).equals("Invalid Request")){
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
	
	public List<Integer> LocateFriend(String friend_email) {
		String URL = LOCATE_FRIEND + "?friend_email=" + friend_email;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return null;
		} else {
			return null;
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
		String URL = REMOVE_FRIEND + "?user_email=" + user_email;
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
		String URL = SEND_REGID + "?user_email=" + user_email + "&private_key=" + private_key;
		if (SendGetRequest(URL).equals("Invalid Request")){
			return false;
		} else {
			return true;
		}
	}
	
	private Boolean SendPostRequest(String URL, List<NameValuePair> nameValuePairs) {
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
            	boolean sts = false; 
        		HttpClient client = new DefaultHttpClient();
        	    HttpGet request = new HttpGet(URL[0]);
        	    HttpResponse response;
        	    String result = null;
        	    try {
        	        response = client.execute(request);         
        	        HttpEntity entity = response.getEntity();

        	        if (entity != null) {

        	            // A Simple JSON Response Read
        	            InputStream instream = entity.getContent();
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
        }.execute(null, null, null);
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
