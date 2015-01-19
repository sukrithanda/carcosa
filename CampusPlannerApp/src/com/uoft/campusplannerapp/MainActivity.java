package com.uoft.campusplannerapp;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;

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
    
    private static void create_alert(Context ctx, String msg) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
    	builder.setMessage(msg)
    	       .setCancelable(false)
    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                //do things
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http_console = new HTTPConsole(this);
    	pref = this.getSharedPreferences("User",MODE_PRIVATE);
    	String user = pref.getString("user", null);
        @SuppressWarnings("unused")
		String temp = get_reg_id();
    	if (user == null) {
            setContentView(R.layout.activity_main);
    	} else {
    		//setContentView(R.layout.locate_friend_or_message);
	        setContentView(new MovingImage(this));

    	}
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
			create_alert(this,"All fields are required");
			return false;
		}
		
		/* Encrypt the Password */
		String encrypt_pw = encrypt_password(s_password);
		/* Send password and user name to server for verification */
		boolean verification = http_console.LoginRequest(s_username, encrypt_pw, regid);
		if (verification == false) {
			create_alert(this , "Username or PW invalid");
		}else {
			Editor edit = pref.edit();
			edit.putString("user", s_username);
			edit.commit();
			create_alert(this, "LogIn Successful");
	        //setContentView(R.layout.locate_friend_or_message);
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
			create_alert(this,"All fields are required");
			return false;
		}
		
		/* validate password */
		boolean a = ValidatePassword(s_password);
		boolean b = ValidateEmail(s_utmail);
		if (a == false) {
			/* If validation failed */
			create_alert(this, "Invalid PW. PW must contain atleast 1 upper case, 1 lower case and 1 number " +
					"and must be atleast 8 characters");
			
		} else if (b == false) {
			/* If validation failed */
			create_alert(this, "Invalid Email. You must use your UofT email to sign up");
		} else {
			/* Encrypt the Password */
			String encrypted_pw = encrypt_password(s_password); 
			
			boolean verification = http_console.SignupRequest(s_utmail,encrypted_pw,s_first_name,s_last_name);
			if (verification == false){
				create_alert(this, "Invalid Email. Try with a UofT email");
			} else {
				create_alert(this, "Validation email sent. Click the link in your email to compelete setup");
			}
		}
		return true;
	}
	
	public boolean ask_for_location(View view){
		EditText email = (EditText)findViewById(R.id.friend);
		try{
			String friend_email = email.getText().toString();
			String user = pref.getString("user", null);
			if (user == null) {
				create_alert(this,"You are not signed in");
			}
			String Loc = http_console.LocateFriend(user, friend_email);
			create_alert(this,"Friend is at " + Loc );
			return true;
		} catch (Exception e) {
			create_alert(this,"Please enter friends email");
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
        String oldRegId = pref.getString("regid",null);
        String oldVersionId = pref.getString("version", null);
        version = getApplicationVersion();
        if ((oldRegId == null) || !(oldVersionId.equals(version))) {
        	register_gcm();
        } else {
        	regid = oldRegId; 
        }
        return regid;
        
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

                	SharedPreferences.Editor edit = pref.edit();
                	edit.putString("regid", regid);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
