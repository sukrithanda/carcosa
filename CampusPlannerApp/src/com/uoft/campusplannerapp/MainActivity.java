package com.uoft.campusplannerapp;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.os.AsyncTask;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.DatePicker;

import com.uoft.campusplannerapp.HTTPConsole;
import com.uoft.campusplannerapp.CreateAlert;;

public class MainActivity extends ActionBarActivity {
	
	GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "124761461971";
    SharedPreferences pref;
    String version;
    HTTPConsole http_console;
	KeyPairGenerator gen;
    KeyPair key;
    PublicKey publicKey;
    PrivateKey privateKey;
    CreateAlert alert;
    DatabaseHandler db;
    
    private Spinner spinner2;
    private List<FriendClass> my_friends;
    private String user; 

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http_console = new HTTPConsole(this);
        alert = new CreateAlert(this);
        db = new DatabaseHandler(this);
        User usero = db.getUser();
    	pref = this.getSharedPreferences("User",MODE_PRIVATE);
        @SuppressWarnings("unused")
		String temp = get_reg_id();
    	if (usero == null) {
            setContentView(R.layout.activity_main);
    	} else {
	        setContentView(new MovingImage(this));

    	}
    }
    
	public void showTimePickerDialog(View v) {
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "timePicker");
	}
    
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	
    public boolean menu_button(View view){
    	setContentView(R.layout.event_organizer);  	
    	addItemOnSpinner2();
    	return true;  	
    }
    
    public void addItemOnSpinner2() {
		SharedPreferences pref = getSharedPreferences("User", Context.MODE_PRIVATE);
	    user = pref.getString("user", "none");
	    List<String> list = new ArrayList<String>();
    	spinner2 = (Spinner) findViewById(R.id.spinner2);
    	my_friends = http_console.GetFriend(user);
    	int i;
    	//for (i = 0; i < my_friends.size(); i++)
    	//{
    		//FriendClass fr = my_friends.get(0);  			
    	//}

    		//list.add(fr.getFirst_name() + " " + fr.getLast_name());
    		list.add("Tanvi Mehta");
    		list.add("Siddharth Zaveri");
    		list.add("Sukrit Handa");

    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner2.setAdapter(dataAdapter);
    }
    
    public boolean login_button(View view){
		setContentView(R.layout.login_page);
		return true;
	}
	
	public boolean grab_login_data(View view){
		EditText username = (EditText)findViewById(R.id.username_id);
		EditText password = (EditText)findViewById(R.id.password_id);
		String s_username;
		String s_password;
		try {
			s_username = username.getText().toString();
			s_password = password.getText().toString();
		} catch (Exception e) {
			alert.create_alert("Error","All fields are required");
			return false;
		}
		
		/* Encrypt the Password */
		String encrypt_pw = encrypt_password(s_password);
		/* Send password and user name to server for verification */
		boolean verification = http_console.LoginRequest(s_username, encrypt_pw, regid);
		if (verification == false) {
			alert.create_alert("Error" , "Username or PW invalid");
		}else {
	        setContentView(new MovingImage(this));

		}
		return true;
	}
	
	public boolean signup_button(View view){
		setContentView(R.layout.signup_page);
		return true;
	}
	
	public boolean grab_signup_data(View view){
		EditText first_name = (EditText)findViewById(R.id.first_name);
		EditText last_name = (EditText)findViewById(R.id.last_name);
		EditText utmail = (EditText)findViewById(R.id.utmail);
		EditText password = (EditText)findViewById(R.id.n_password_id);
		String s_first_name, s_last_name, s_utmail, s_password;
		try {
			s_first_name = first_name.getText().toString();
			s_last_name = last_name.getText().toString();
			s_utmail = utmail.getText().toString();
			s_password = password.getText().toString();
		} catch (Exception e) {
			alert.create_alert("Error","All fields are required");
			return false;
		}
		
		/* validate password */
		boolean a = ValidatePassword(s_password);
		boolean b = ValidateEmail(s_utmail);
		if (a == false) {
			/* If validation failed */
			alert.create_alert("Error", "Invalid PW. PW must contain atleast 1 upper case, 1 lower case and 1 number " +
					"and must be atleast 8 characters");
			
		} else if (b == false) {
			/* If validation failed */
			alert.create_alert("Error", "Invalid Email. You must use your UofT email to sign up");
		} else {
			/* Encrypt the Password */
			String encrypted_pw = encrypt_password(s_password); 
			
			boolean verification = http_console.SignupRequest(s_utmail,encrypted_pw,s_first_name,s_last_name);
			if (verification == false){
				alert.create_alert("Error", "Invalid Email. Try with a UofT email");
			} else {
				alert.create_alert("Error", "Validation email sent. Click the link in your email to compelete setup");
			}
		}
		return true;
	}
	
	public boolean ask_for_location(View view){
		EditText email = (EditText)findViewById(R.id.friend);
		try{
			String friend_email = email.getText().toString();
			User usero = db.getUser();
			String user = usero.getEmail();
			if (user == null) {
				alert.create_alert("Error","You are not signed in");
			}
			String Loc = http_console.LocateFriend(user, friend_email);
			Log.i("Sidd","Print location now " + Loc);
	        setContentView(new MovingImage(this));
			return true;
		} catch (Exception e) {
			alert.create_alert("Error","Please enter friends email");
			return false;
		}
	}

	private boolean ValidateEmail(String s_utmail) {
		if (s_utmail.contains("utoronto.ca")  || s_utmail.contains("toronto.edu"))
			return true;
		else 
			return false;
	}

	private boolean ValidatePassword(String s_password) {
		// TODO Auto-generated method stub
		if (!s_password.matches(".*\\d+.*")){
			return false;
		}
		if (s_password.equals(s_password.toLowerCase(Locale.getDefault())) 
				|| s_password.equals(s_password.toUpperCase(Locale.getDefault()))){
			return false;
		}
		if (s_password.length() < 8){
			return false;
		}
		return true;
	}
	
	public void CreateKeys() {
		try {
			gen = KeyPairGenerator.getInstance("RSA");
	        gen.initialize(1024);
	        key = gen.genKeyPair();
	        publicKey = key.getPublic();
	        privateKey = key.getPrivate();
	        String privateKeyString = privateKey.toString();
	        boolean status = http_console.SendPrivate("None",privateKeyString);
		} catch (NoSuchAlgorithmException n) {
			
		}
	}
	
	public String encrypt_password(String password) {
		return password;
//		try {
//			Cipher c = Cipher.getInstance("RSA");
//	        c.init(Cipher.ENCRYPT_MODE, publicKey);
//	        byte[] encrypted = c.doFinal(password.getBytes());
//	        String sEncrypted = new String(encrypted);
//	        return sEncrypted;
//		} catch (NoSuchPaddingException n) {
//			return null;
//		} catch (InvalidKeyException n) {
//			return null;
//		} catch (IllegalBlockSizeException n) {
//			return null;
//		} catch (NoSuchAlgorithmException n) {
//			return null;
//		} catch (BadPaddingException n) {
//			return null;
//		}	
	}

    private String getApplicationVersion() {
    	try {
            PackageInfo packageInfo = getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
  
    }
    
    private String get_reg_id() {
        String oldVersionId = pref.getString("version", null);
    	User user = db.getUser();
    	if (user == null || oldVersionId == null){
    		register_gcm();
    		return regid;
    	} else {
	    	String oldRegId = user.getRegId();
	    	String 
	        version = getApplicationVersion();
	        if ((oldRegId == null) || !(oldVersionId.equals(version))) {
	        	register_gcm();
	        } else {
	        	regid = oldRegId; 
	        }
	        return regid;
    	}
        
    }
    
    private void register_gcm() {
    	new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);

                	Editor edit = pref.edit();
                	edit.putString("version", version);
                	msg = "RegId is" + regid + "version is" + version;
                	edit.commit();
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM",  msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.signout) {
            boolean sts = http_console.Logout();
            if (sts == true){
            	db.deleteDatabses();
            	setContentView(R.layout.activity_main);
            } else { 
            	alert.create_alert("Error","Signout Failed. Try again later");
            }
        } else if (id == R.id.friend) {
        	Intent i = new Intent(getApplicationContext(), FriendActivity.class);
        	startActivity(i);
        } 
        return super.onOptionsItemSelected(item);
    }
}
