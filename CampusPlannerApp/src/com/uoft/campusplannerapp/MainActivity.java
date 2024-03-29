package com.uoft.campusplannerapp;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.GroundOverlay;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.uoft.campusplannerapp.CurrentLocationProvider;
import com.uoft.campusplannerapp.LocalizationCore;
import com.uoft.campusplannerapp.PrefsActivity;
import com.uoft.campusplannerapp.R;
//import com.wifilocalizer.R;

import android.net.wifi.ScanResult;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.WindowManager;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity  implements NavigationDrawerFragment.NavigationDrawerCallbacks, DateInterface {
	
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
    boolean session = true;

    CreateAlert alert;
    DatabaseHandler db;
    User u;
    int hardcoded = 0; 
    float hard_lat = 0; 
    float hard_longi = 0; 
    int hard_floor = 0; 
    
    ClientReciever listener;
    
    public Spinner spinner2;
    public List<FriendClass> my_friends;
    public Fragment mVisible;
	public SupportMapFragment mMapFragment;
	public Fragment mFriendsFragment;
	public Fragment mOrganizeEventFragment;
	public Fragment mSetUpOfficeHoursFragment;
	public Fragment mPrivacyFragment;
	public Fragment mResourceFragment;
	public Fragment mDisplayEventsFragment;

	public static GoogleMap map;
	private boolean reload_needed = false;
	
    ArrayList<MarkerFloorPairs> markers = new ArrayList<MarkerFloorPairs>();
    ArrayList<PolyLineFloorPairs> pathlines = new ArrayList<PolyLineFloorPairs>();



	
	//LOCALIZER CODE - START

    public CurrentLocationProvider mylocation;

    public GroundOverlay groundOverlay;
    float bearing;
    
	BitmapDescriptor floor1;
	BitmapDescriptor floor2;
	BitmapDescriptor floor3;
	BitmapDescriptor floor4;
	BitmapDescriptor floor5;
	BitmapDescriptor floor6;
	BitmapDescriptor floor7;
	BitmapDescriptor floor8;
	public int load_floor=1;
		
	LocalizationCore localizationcore;
	
    public static final int MAX_WIFI_APs = 200;
	public int counter_wifi=0;
    public int[] wifi_rss_buff= new int[MAX_WIFI_APs];
    public String[] wifi_mac_buff= new String[MAX_WIFI_APs];
    
    
	// Wifi scanner part
    int counter=1;
	WiFiScanReceiver receiver;
	WifiManager wifiManager = null;

	public float[] gyro_sim = new float[3];
	public float[] accel_sim = new float[3];
	public float[] magnet_sim = new float[3];
	public float[] gravity_sim = new float[3];
	public float[] linearaccel_sim = new float[3];
	public float[] rotationvector_sim = new float[3];
	public float[] gamerotation_sim = new float[3];
	public float[] orientation_sim = new float[3];
	public float[] rotationmatrix_sim = new float[9];
	public float pressure_sim,gps_status_sim,wifi_status_sim,ble_status_sim;
	public float[] gps_sim = new float[6];
	public float[] wifi_rss_sim = new float[200];
	public byte[]	wifi_mac_sim = new byte[3400];
	public float[] ble_rss_sim = new float[100];
	public byte[]	ble_mac_sim = new byte[1700];
	public float[] ble_coordinates_sim = new float[300];
	public float[] ble_tx_powers_sim = new float[100];
	public float[] params_sim = new float[10];
	
	int[] location_sim=new int[3];
	float[] geolocation_sim=new float[3];
	float[] accuracy_sim = new float[1]; 
	float[] speed_sim=new float[3];
	float[] steps_sim = new float[1]; 
	
	
	boolean autostart_enable=true;
    public boolean start=false;
    // LOCALIZATION CODE - END
	
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	public NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	public CharSequence mTitle;
    
	public Calendar dateTime = Calendar.getInstance();
	public SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd yyyy");
	public SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
	//THREADS to receive and send locations

	
	

	
    public static void create_alert(Context ctx, String msg) {
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
    
    private void setUpMapAfterLogin() {

        User usero = db.getUser();
        u = usero;
        List<FriendClass> friend_list = new ArrayList<FriendClass>();
        if (u != null)
        	friend_list = http_console.GetFriend(u.getEmail());
        db.Close(); 

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
			.findFragmentById(R.id.navigation_drawer);
		DrawerLayout m = (DrawerLayout)findViewById(R.id.drawer_layout);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,m);

	
		//mTitle = getString(R.string.title_map);
		
		//LOCALIZATION CODE - START
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	
		
		localizationcore = new LocalizationCore();
		
		try {
			wifiManager = (WifiManager) getBaseContext()
					.getSystemService(Context.WIFI_SERVICE);
			//wifiManager.setWifiEnabled(false);
		} catch (Exception e) {

		}

		// Register Broadcast .wReceiver
		receiver=new WiFiScanReceiver();
	
		
		params_sim[0]=1;
		params_sim[1]=1;
		loadPref();
	   // listener = new ClientReciever(db, u, http_console, friend_list);
		if(autostart_enable){
			start_process();
		//	listener.start();
			showFragment(mMapFragment);
		}
		else{
			setUpFragments();
			showFragment(mMapFragment);
		}

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(this);
        http_console = new HTTPConsole(this);
        session = true;
        alert = new CreateAlert(this);

        /* First of all verify if we can connect to server */ 
        String server_sts = http_console.HelloWorld();
        if (server_sts.equals("Invalid Request")) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Error")
        		   .setMessage("Can't connect to server right now\n" +
        		   		"Check your network connection and try again")
        	       .setCancelable(false)
        	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	                finish();
        	                System.exit(0);
        	           }
        	       });
        	AlertDialog alert = builder.create();
        	alert.show();
        	return;
        }
        User usero = db.getUser();
        u = usero;
        List<FriendClass> friend_list = new ArrayList<FriendClass>();
        if (u != null)
        	friend_list = http_console.GetFriend(u.getEmail());
        db.Close();
    	pref = this.getSharedPreferences("User",MODE_PRIVATE);
        @SuppressWarnings("unused")
		String temp = get_reg_id();
    	
     
  
    	if (usero == null) {
            setContentView(R.layout.activity_main);
    	} else {
        	setContentView(R.layout.map_main); 
    		setUpMapAfterLogin();
    	}
    	
    }
    
	@Override
    protected void onResume() {
        super.onResume();
    }
	
	
	// Set different Fragments Here
		@Override
		public void onNavigationDrawerItemSelected(int position) {
			// update the main content by replacing fragments

			switch(position)
			{
				case 0:
					showFragment(mMapFragment);
					 markers.clear();
					 pathlines.clear();
					mTitle = getString(R.string.title_map);
					break;
				case 1:
					showFragment(mFriendsFragment);
					mFriendsFragment.onResume();
					mTitle = getString(R.string.title_friends);
					break;
				case 2:
					showFragment(mResourceFragment);
					mTitle = getString(R.string.title_resources);
					break;
					
				case 3:
					showFragment(mOrganizeEventFragment);
					//addItemOnSpinner2();
					mTitle = getString(R.string.title_OrganizeEvent);
					break;
//				case 4:
//					showFragment(mSetUpOfficeHoursFragment);
//					//addItemOnSpinner2();
//					mTitle = getString(R.string.title_SetupOfficeHours);
//					break;
				case 4:
					showFragment(mPrivacyFragment);
					mPrivacyFragment.onResume();
					mTitle = getString(R.string.title_privacySettings);
					break;
				case 5:
					showFragment(mDisplayEventsFragment);
					mTitle = getString(R.string.title_displayEvents);
					break;
			}
		}
	

		public void onSectionAttached(int number) {
			switch (number) {
			case 1:
				mTitle = getString(R.string.title_map);
				break;
			case 2:
				mTitle = getString(R.string.title_friends);
				break;

			case 3:
				mTitle = getString(R.string.title_resources);
				break;
			case 4:
				mTitle = getString(R.string.title_OrganizeEvent);
				break;
//			case 5:
//				mTitle = getString(R.string.title_SetupOfficeHours);
//				break;
			case 5:
				mTitle = getString(R.string.title_privacySettings);
				break;
			case 6:
				mTitle = getString(R.string.title_displayEvents);
			}
		}	
		
		public void restoreActionBar() {
			ActionBar actionBar = getSupportActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle(mTitle);
		}
		
	
	
	
	public void updateDateButton(String date)
	{
		System.out.println("Date in updateDateButton is: "+date);
		Button dateButton = (Button) findViewById(R.id.datepickButton);
		dateButton.setText(date);
		SharedPreferences pref = getSharedPreferences("Event", MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString("from_date", date);
		edit.commit();
	}
	
	public void updateTimeButton (String time)
	{
		Button timeButton = (Button) findViewById(R.id.timepickButton);
		timeButton.setText(time);
		SharedPreferences pref = getSharedPreferences("Event", MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString("from_time", time);
		edit.commit();
	}
	
	public void updateDateButton2 (String date)
	{
		Button dateButton = (Button) findViewById(R.id.datepickButton2);
		dateButton.setText(date);
		SharedPreferences pref = getSharedPreferences("Event", MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString("to_date", date);
		edit.commit();
	}
	
	public void updateTimeButton2 (String time)
	{
		Button timeButton = (Button) findViewById(R.id.timepickButton2);
		timeButton.setText(time);
		SharedPreferences pref = getSharedPreferences("Event", MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString("to_time", time);
		edit.commit();
	}

    public void addItemOnSpinner2() {
    	User usero = db.getUser();
    	if (usero == null) {
    		alert.create_alert("Error", "You need to Sign in first");
    		String user = "tanvim.mehta@utoronto.ca";
        	my_friends = http_console.GetFriend(user);
    		
    	} else {
    		my_friends = http_console.GetFriend(usero.getEmail());
    	}
	    List<String> list = new ArrayList<String>();
    	//spinner2 = (Spinner) findViewById(R.id.spinner2);
    	int i;
    	for (i = 0; i < my_friends.size(); i++)
    	{
    		FriendClass fr = my_friends.get(i);  		
    		list.add(fr.getFirst_name() + " " + fr.getLast_name());
    	}

    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner2.setAdapter(dataAdapter);
    }
    
    public boolean login_button(View view){
		setContentView(R.layout.login_page);
       // setContentView(R.layout.map_layout);

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
			try {
			((Activity) this).setContentView(R.layout.map_main);
			} catch (Exception e) {
				alert.create_alert("One", "First try failed");
				try {
					alert.create_alert("Two", "Second try failed");
					this.setContentView(R.layout.map_main);
				} catch (Exception f) {
					
				}
			}
			setUpMapAfterLogin();
	        /*
	        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
					.findFragmentById(R.id.navigation_drawer);
	    	DrawerLayout m = (DrawerLayout)findViewById(R.id.drawer_layout);
	    
			// Set up the drawer.
			mNavigationDrawerFragment.setUp(R.id.navigation_drawer,m);

			setUpFragments();
			mVisible = mMapFragment;
			mTitle = getString(R.string.title_map);
	        */
	       // setContentView(new MovingImage(this));

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

	public boolean ValidateEmail(String s_utmail) {
		if (s_utmail.contains("utoronto.ca")  || s_utmail.contains("toronto.edu"))
			return true;
		else 
			return false;
	}

	public boolean ValidatePassword(String s_password) {
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
	
	@SuppressLint("TrulyRandom") public void CreateKeys() {
		try {
			gen = KeyPairGenerator.getInstance("RSA");
	        gen.initialize(1024);
	        key = gen.genKeyPair();
	        publicKey = key.getPublic();
	        privateKey = key.getPrivate();
	        String privateKeyString = privateKey.toString();
	        http_console.SendPrivate("None",privateKeyString);
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

    public String getApplicationVersion() {
    	try {
            PackageInfo packageInfo = getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
  
    }
    
    public String get_reg_id() {
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
    
    public void register_gcm() {
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
    	if(mNavigationDrawerFragment != null && !mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
    	int id = item.getItemId();
         if (id == R.id.signout) {
        	//listener.setRunning(false);
            boolean sts = http_console.Logout();
            if (sts == true){
            	session = false;
            	db.deleteDatabses();
            	setContentView(R.layout.activity_main);
            	finish();
            	System.exit(0);	
            } else { 
            	// restart client if signout failed
            	//listener.setRunning(true);
            	//listener.start();
            	alert.create_alert("Error","Signout Failed. Try again later");
            }
            return true;
        } else if (id == R.id.menu_start){
        	  start_process();
	        	return true;
        }
          else if (id == R.id.menu_stop){
        	   stop_process();
	            return true;
        }
          else if (id == R.id.action_settings){
        	  Intent i = new Intent(getApplicationContext(), PrefsActivity.class);
              startActivityForResult(i, 0); 
	        	return true;
        }
          else if (id == R.id.menu_up){
        	  int floor = load_floor;
        	   if(floor < 8){
        		   stop_process();
        		   showFragment(mMapFragment, ++floor);
        	   }
	        	return true;
        }
          else if (id == R.id.menu_down){
        	  int floor = load_floor;
        	  if(floor > 1){
       		   stop_process();
       		   showFragment(mMapFragment, --floor);
       	   	  }
	        	return true;
        }
          else{
        	  return super.onOptionsItemSelected(item);
          }
    }

    
    public static class SetUpOfficeHoursFragment extends Fragment {
    	public static final String TAG = "Office Hours";
    	
    	public SetUpOfficeHoursFragment() {
    		super();
    	}
    	public static SetUpOfficeHoursFragment newInstance() {
    		SetUpOfficeHoursFragment fragment = new SetUpOfficeHoursFragment();
    		return fragment;
    	}
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			View rootView = inflater.inflate(R.layout.setup_officehours, container, false);
			
			return rootView;
		}
    }
    
    

	
	
	public void showFragment(Fragment fragmentIn) {
        if ( fragmentIn == null /*|| fragmentIn == mVisible*/) return;

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        if (fragmentIn == mMapFragment && map!=null){
        	map.setMyLocationEnabled(true);
        	start_process();
        }
        if (mVisible != null) {
        	ft.hide(mVisible);
        }

        ft.show(fragmentIn).commit();
        mVisible = fragmentIn;
    }
	
	public void showFragment(Fragment fragmentIn, int floor) {
		System.out.println("DEBUG - SHOWING FRAGMENT WITH FLOOR");

        if ( fragmentIn == null /*|| fragmentIn == mVisible*/) return;
		System.out.println("DEBUG - CHANGING FLOOR: " + floor);
       changefloor(floor);
		System.out.println("DEBUG - FLOOR CHANGED");

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        if (mVisible != null) {
        	ft.hide(mVisible);
        }

        ft.show(fragmentIn).commit();
        mVisible = fragmentIn;
    }
	
	public void showFriend(int floor, float latitude, float longitude, final String name, final String email) {
		System.out.println(" DEBUG - IN SHOW FRIEND");
		stop_process();
		System.out.println("DEBUG - PROCESS STOPPED");
		System.out.println("DEBUG - DRAWING MAKER");
		 markers.clear();
		 pathlines.clear();
		LatLng pinLocation = new LatLng(latitude, longitude);
        	      Marker storeMarker = map.addMarker(new MarkerOptions()
        	      .position(pinLocation)
        	      .title(name)
        	      .snippet(email));   
        	      
        	      
        	      MarkerFloorPairs j = new MarkerFloorPairs(storeMarker, floor, email);
   
        	      
        	      Iterator<MarkerFloorPairs> iterator = markers.iterator();
        			while (iterator.hasNext()) {
        				if(iterator.next().getID().equals(email)){
        					System.out.println("DEBUG - PREVIOUS MARKER FOUND");
        					iterator.remove();
        					System.out.println("DEBUG - REMOVED MARKER FOUND");

        				}
        			}
					System.out.println("DEBUG - ADDING NE MARKER TO LIST");
        		  markers.add(j);
        		  mMapFragment.isResumed();
        		  showFragment(mMapFragment, floor);
        		  
    }
	
	public void showpath(List<ResourceClass> m) {
		
		 int i;
		 stop_process();
		 
		 markers.clear();
		 pathlines.clear();
	      int size = m.size();
			System.out.println("DEBUG - IN SHOW PATH");
		ArrayList<Location> points = new ArrayList<Location>();
		//ArrayList<Integer> fl = new ArrayList<Integer>();

		//grab all the points and draw a marker at the end
		double oldlat = 43.659652988335878;
		double oldlong = -79.397276867154886;
				
		float difflat;
		float difflong;
		LatLng endLocation = null;
		
		for (i = 0; i < size; i++) {
      	  Location l = m.get(i).getLoc();
      	  String g = m.get(i).getResource();
      	  int pointfloor = m.get(i).getLoc().getFloor();
      	  
      	 
      	 if(pointfloor == 1){
      		difflat = (float) (43.6596355 - oldlat);
    		difflong = (float)(oldlong - -79.397400);
      	  }
      	 else if(pointfloor == 2){
      		difflat = (float) (-0.00004);
     		difflong = (float)( 0.00015);
       	  }
      	 else{
       		difflat = (float) (43.659658 - oldlat);
     		difflong = (float)( oldlong - -79.397435);
       	  }
      	  
		  LatLng pinLocation = new LatLng(l.getLatitude() + difflat, l.getLongitude() + difflong);
      	  	if (m.get(i).getType().equals("Corridor") || m.get(i).getType().equals("Elevator") || m.get(i).getType().equals("Stairs")  ){
    		  	points.add(l);

      		  		if ( m.get(i).getLoc().getFloor() != m.get(i+1).getLoc().getFloor() && (m.get(i).getType().equals("Elevator") || m.get(i).getType().equals("Stairs"))  ){
      		  			String title = "";
      		  			String mode = "";
      		  			if (m.get(i).getLoc().getFloor() < m.get(i+1).getLoc().getFloor()) {
      		  				title = "Please go Up";
      		  			} else {
      		  				title  = "Please go down";
      		  			}
      		  			mode = m.get(i).getType();
      	  	    		 Marker storeMarker = map.addMarker(new MarkerOptions()
      	  	      		.position(pinLocation)
      	  	      		.title(title)
      	  	      		.snippet(mode));  
      	  	    		 MarkerFloorPairs x =  new MarkerFloorPairs(storeMarker, m.get(i).getLoc().getFloor() , "NO EMAIL");
      	  	    		 markers.add(x);
      		  		}

      	  	}
      	  	if (i == size-1){
    		  	points.add(l);

    		 endLocation = new LatLng(l.getLatitude() + difflat, l.getLongitude() + difflong);

  	    	 Marker storeMarker = map.addMarker(new MarkerOptions()
  	      	.position(endLocation)
  	      	.title(g)
  	      	.snippet("End Destination"));  
  	    	 
  	    	MarkerFloorPairs x =  new MarkerFloorPairs(storeMarker, m.get(i).getLoc().getFloor() , "NO EMAIL");
	  	    	 markers.add(x);
      	  }
      	  
         }
		
		 LatLng currentloc = new LatLng(geolocation_sim[0],geolocation_sim[1]);
		 Marker storeMarker = map.addMarker(new MarkerOptions()
	      	.position(currentloc)
	      	.title("Starting Location")
	      	.snippet("User is here")); 
		 MarkerFloorPairs x =  new MarkerFloorPairs(storeMarker, m.get(0).getLoc().getFloor() , "NO EMAIL");
	    	 markers.add(x);
		 
		
		//draw the lines in between the markers!
		int p;
		if (points.size() > 1){
			 int pointfloor = points.get(0).getFloor();

		  	 if(pointfloor == 1){
		  		difflat = (float) (43.6596355 - oldlat);
	    		difflong = (float)(oldlong - -79.397400);
		      	  }
		      	 else if(pointfloor == 2){
		      		difflat = (float) (-0.00004);
		     		difflong = (float)( 0.00015);
		       	  }
		      	 else{
		       		difflat = (float) (43.659658 - oldlat);
		     		difflong = (float)( oldlong - -79.397435);
		       	  }
   		  LatLng o = new LatLng(points.get(0).getLatitude() + difflat, points.get(0).getLongitude() + difflong);

	     	  Polyline line = map.addPolyline((new PolylineOptions())
					.add(currentloc, o).width(10).color(Color.BLUE)
					.geodesic(true));
	     	  
	     	 PolyLineFloorPairs b = new PolyLineFloorPairs(line,points.get(0).getFloor());
	     	  pathlines.add(b);
	     	  
	     	  
	     	  
			for (p = 0; p < points.size() - 1; p++){
		      	  pointfloor = points.get(p).getFloor();

			  	 if(pointfloor == 1){
			  		difflat = (float) (43.6596355 - oldlat);
		    		difflong = (float)(oldlong - -79.397400);
			      	  }
			      	 else if(pointfloor == 2){
			      		difflat = (float) (-0.00004);
			     		difflong = (float)( 0.00015);
			       	  }
			      	 else{
			       		difflat = (float) (43.659658 - oldlat);
			     		difflong = (float)( oldlong - -79.397435);
			       	  }
				
				if(points.get(p).getFloor() == points.get(p+1).getFloor()){
		   		  LatLng plusone = new LatLng(points.get(p).getLatitude() + difflat, points.get(p).getLongitude() + difflong);
		   		  LatLng o1 = new LatLng(points.get(p+1).getLatitude() + difflat, points.get(p+1).getLongitude() + difflong);

		   		  line = map.addPolyline((new PolylineOptions())
						.add(o1, plusone).width(10).color(Color.BLUE)
						.geodesic(true));
				
				 b = new PolyLineFloorPairs(line, points.get(p).getFloor() );
		     	  pathlines.add(b);
				}
		      	 
			}
		}else{
			 int pointfloor = points.get(0).getFloor();

		  	 if(pointfloor == 1){
		  		difflat = (float) (43.6596355 - oldlat);
	    		difflong = (float)(oldlong - -79.397400);
		      	  }
		      	 else if(pointfloor == 2){
		       		difflat = (float) (-0.00004);
		     		difflong = (float)( 0.00015);
		       	  }
		      	 else{
		       		difflat = (float) (43.659658 - oldlat);
		     		difflong = (float)( oldlong - -79.397435);
		       	  }
	   		  LatLng plusone = new LatLng(points.get(0).getLatitude() + difflat, points.get(0).getLongitude() + difflong);

			 Polyline line = map.addPolyline((new PolylineOptions())
						.add(plusone, endLocation) .width(10).color(Color.BLUE)
						.geodesic(true));
			 PolyLineFloorPairs b = new PolyLineFloorPairs(line, points.get(points.size() - 1).getFloor()  );
	     	  pathlines.add(b);
		}
		System.out.println("DEBUG - DONE DRAWING MARKERS");

	    
  	    mMapFragment.isResumed();
		hidemarkers(m.get(0).getLoc().getFloor() );
		showFragment(mMapFragment, m.get(0).getLoc().getFloor() );
		System.out.println("DEBUG - MAP SHOULD OPEN");

        		  
    }


	public void setUpFragments() {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // If the activity is killed while in BG, it's possible that the
        // fragment still remains in the FragmentManager, so, we don't need to
        // add it again.
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mMapFragment == null) {
            mMapFragment = MFragment.newInstance();
            
            mMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    Load_Maps();
                    initilizeMap();
                }
            });
                       
            ft.add(R.id.container, mMapFragment, MFragment.TAG);
        }
        else{
        	
        	  mMapFragment.getMapAsync(new OnMapReadyCallback() {
                  @Override
                  public void onMapReady(GoogleMap googleMap) {
                      map = googleMap;
                      //Load_Maps();
                      //initilizeMap();
                  }
              });
           ft.replace(R.id.container, mMapFragment, MFragment.TAG);
        }
        ft.hide(mMapFragment);

        mFriendsFragment = (FriendsFragment) getSupportFragmentManager().findFragmentByTag(FriendsFragment.TAG);
        if (mFriendsFragment == null) {
        	System.out.println("GetFragments");
            mFriendsFragment = FriendsFragment.newInstance(this, MainActivity.this, mMapFragment);
            ft.add(R.id.container, mFriendsFragment, FriendsFragment.TAG);
        }
        ft.hide(mFriendsFragment);
        
        mResourceFragment = (ResourceFragment) getSupportFragmentManager().findFragmentByTag(ResourceFragment.TAG);
        if (mResourceFragment == null) {
        	mResourceFragment = ResourceFragment.newInstance(this, MainActivity.this);
            ft.add(R.id.container, mResourceFragment, ResourceFragment.TAG);
        }
        ft.hide(mResourceFragment);

        mOrganizeEventFragment = (OrganizeEventFragment) getSupportFragmentManager().findFragmentByTag(OrganizeEventFragment.TAG);
        if (mOrganizeEventFragment == null)
        {
        	mOrganizeEventFragment = OrganizeEventFragment.newInstance(this);
        	ft.add(R.id.container, mOrganizeEventFragment, OrganizeEventFragment.TAG);
        }
        ft.hide(mOrganizeEventFragment);

        
        mSetUpOfficeHoursFragment = (SetUpOfficeHoursFragment) getSupportFragmentManager().findFragmentByTag(SetUpOfficeHoursFragment.TAG);
        if (mSetUpOfficeHoursFragment == null)
        {
        	mSetUpOfficeHoursFragment = SetUpOfficeHoursFragment.newInstance();
        	ft.add(R.id.container, mSetUpOfficeHoursFragment, SetUpOfficeHoursFragment.TAG);
        }
        ft.hide(mSetUpOfficeHoursFragment);
        
        mPrivacyFragment = (PrivacyFragment) getSupportFragmentManager().findFragmentByTag(PrivacyFragment.TAG);
        if (mPrivacyFragment == null)
        {
        	mPrivacyFragment = PrivacyFragment.newInstance(this);
        	ft.add(R.id.container, mPrivacyFragment, PrivacyFragment.TAG);
        }
        ft.hide(mPrivacyFragment);
        
        mDisplayEventsFragment = (DisplayEventsFragment) getSupportFragmentManager().findFragmentByTag(DisplayEventsFragment.TAG);
        if (mDisplayEventsFragment == null) 
        {
        	mDisplayEventsFragment = DisplayEventsFragment.newInstance(this);
        	ft.add(R.id.container, mDisplayEventsFragment, DisplayEventsFragment.TAG);
        }
        ft.hide(mDisplayEventsFragment);
        
        ((OrganizeEventFragment)mOrganizeEventFragment).AddDE((DisplayEventsFragment)mDisplayEventsFragment);
 
        ft.commit();

    }
	
	 public void initilizeMap() {
	      
		 if (map == null) {
             Toast.makeText(getApplicationContext(),
                     "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                     .show();
             return;
         }
	 
	  		  map.setMyLocationEnabled(true);
	  		  map.setIndoorEnabled(false);
	  		 
	  		  
	  		  map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	  		  CameraPosition cameraPosition = new CameraPosition.Builder().target(
	  	                new LatLng(43.659652988335878, -79.397276867154886)).zoom(19).build();
	  		  
	  		   map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	  		   
	  		 int floorint = (int)geolocation_sim[2];
	  		 BitmapDescriptor floor;
	  		switch (floorint) {
			case 1:
				floor = floor1;
				break;
			case 2:
				floor = floor2;				
				break;
			case 3:
				floor = floor3;
				break;
			case 4:
				floor = floor4;
				break;
			case 5:
				floor = floor5;
				break;
			case 6:
				floor = floor6;
				break;
			case 7:
				floor = floor7;
				break;
			case 8:
				floor = floor8;
				break;
			default:
				floor = floor1;
				break;
	  		}  
	  		
	  		 groundOverlay=map.addGroundOverlay(new GroundOverlayOptions().image(floor).transparency(0.01f).anchor(0.5f, 0.5f)
	  		        .position(new LatLng(43.659652988335878, -79.397276867154886), 100f, 121f).bearing(-17.89f));

	  	
	  		  mylocation=new CurrentLocationProvider(this);
	  		  map.setLocationSource(mylocation);
	  	
	    }
	
	//LOCALIZATION CODE - FUNCTIONS START
public void Load_Maps(){
		
		final	String	foldername="WiFi-Localizer/Maps";
		String FileName;
		File file;
		File root = Environment.getExternalStorageDirectory();
		File appDirectory = new File(root.toString()+"/"+foldername);
		
	    FileName = "bahen1.png";
		file = new File(appDirectory.getAbsolutePath()+ "/"+FileName);
		if(file.exists())      
			floor1 = BitmapDescriptorFactory.fromPath(appDirectory.getAbsolutePath()+ "/"+FileName);	
		else
			floor1 = BitmapDescriptorFactory.fromResource(R.drawable.bahen1);
		
		
		FileName = "bahen2.png";
		file = new File(appDirectory.getAbsolutePath()+ "/"+FileName);
		if(file.exists())      
			floor2 = BitmapDescriptorFactory.fromPath(appDirectory.getAbsolutePath()+ "/"+FileName);	
		else
			floor2 = BitmapDescriptorFactory.fromResource(R.drawable.bahen2);
		
		
		FileName = "bahen3.png";
		file = new File(appDirectory.getAbsolutePath()+ "/"+FileName);
		if(file.exists())      
			floor3 = BitmapDescriptorFactory.fromPath(appDirectory.getAbsolutePath()+ "/"+FileName);	
		else
			floor3 = BitmapDescriptorFactory.fromResource(R.drawable.bahen3);
		
		
		FileName = "bahen4.png";
		file = new File(appDirectory.getAbsolutePath()+ "/"+FileName);
		if(file.exists())      
			floor4 = BitmapDescriptorFactory.fromPath(appDirectory.getAbsolutePath()+ "/"+FileName);	
		else
			floor4 = BitmapDescriptorFactory.fromResource(R.drawable.bahen4);
		
		
		FileName = "bahen5.png";
		file = new File(appDirectory.getAbsolutePath()+ "/"+FileName);
		if(file.exists())      
			floor5 = BitmapDescriptorFactory.fromPath(appDirectory.getAbsolutePath()+ "/"+FileName);	
		else
			floor5 = BitmapDescriptorFactory.fromResource(R.drawable.bahen5);
		
		
		FileName = "bahen6.png";
		file = new File(appDirectory.getAbsolutePath()+ "/"+FileName);
		if(file.exists())      
			floor6 = BitmapDescriptorFactory.fromPath(appDirectory.getAbsolutePath()+ "/"+FileName);	
		else
			floor6 = BitmapDescriptorFactory.fromResource(R.drawable.bahen6);
		
		
		FileName = "bahen7.png";
		file = new File(appDirectory.getAbsolutePath()+ "/"+FileName);
		if(file.exists())      
			floor7 = BitmapDescriptorFactory.fromPath(appDirectory.getAbsolutePath()+ "/"+FileName);	
		else
			floor7 = BitmapDescriptorFactory.fromResource(R.drawable.bahen7);
		
		
		FileName = "bahen8.png";
		file = new File(appDirectory.getAbsolutePath()+ "/"+FileName);
		if(file.exists())      
			floor8 = BitmapDescriptorFactory.fromPath(appDirectory.getAbsolutePath()+ "/"+FileName);	
		else
			floor8 = BitmapDescriptorFactory.fromResource(R.drawable.bahen8);  	
		
		
	}

public void Load_RadioMap(){
	
	int i;
	final	String	foldername="WiFi-Localizer/RadioMaps";
	String FileName="radiomap.bin";
	File root = Environment.getExternalStorageDirectory();
	File appDirectory = new File(root.toString()+"/"+foldername);
	File file=new File(appDirectory.getAbsolutePath(), "/"+FileName);
	FileInputStream fin = null;
	
	
	try {
		fin = new FileInputStream(file);

		int unique_ID;
		short n_floors;
		float A[] = new float[4];
		float B[] = new float[2];
		float Center[] = new float[2];
		int n_points;
		int n_AP;
		
		byte building_name[] = new byte[64];
		byte unique_ID_arr[]=new byte[4];
		byte n_floors_arr[]=new byte[2];
		byte A_arr[] = new byte[16];
		byte B_arr[] = new byte[8];
		byte Center_arr[] = new byte[8];
		byte n_points_arr[]=new byte[4];
		byte n_AP_arr[]=new byte[4];
		byte temp_arr[] = new byte[4];
		
		fin.read(building_name, 0, 64);
		fin.read(unique_ID_arr,0,4);
		fin.read(n_floors_arr,0,2);
		fin.read(A_arr,0,16);
		fin.read(B_arr,0,8);
		fin.read(Center_arr,0,8);
		fin.read(n_points_arr,0,4);
		fin.read(n_AP_arr,0,4);

		unique_ID=ByteBuffer.wrap(unique_ID_arr).order(ByteOrder.LITTLE_ENDIAN).getInt();
		n_floors=ByteBuffer.wrap(n_floors_arr).order(ByteOrder.LITTLE_ENDIAN).getShort();
		n_points=ByteBuffer.wrap(n_points_arr).order(ByteOrder.LITTLE_ENDIAN).getInt();
		n_AP=ByteBuffer.wrap(n_AP_arr).order(ByteOrder.LITTLE_ENDIAN).getInt();
		
		for (i=0;i<4;i++){
			temp_arr[0]=A_arr[i*4];
			temp_arr[1]=A_arr[i*4+1];
			temp_arr[2]=A_arr[i*4+2];
			temp_arr[3]=A_arr[i*4+3];
			A[i] = ByteBuffer.wrap(temp_arr).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		}
		
		for (i=0;i<2;i++){
			temp_arr[0]=B_arr[i*4];
			temp_arr[1]=B_arr[i*4+1];
			temp_arr[2]=B_arr[i*4+2];
			temp_arr[3]=B_arr[i*4+3];
			B[i] = ByteBuffer.wrap(temp_arr).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		}
		
		for (i=0;i<2;i++){
			temp_arr[0]=Center_arr[i*4];
			temp_arr[1]=Center_arr[i*4+1];
			temp_arr[2]=Center_arr[i*4+2];
			temp_arr[3]=Center_arr[i*4+3];
			Center[i] = ByteBuffer.wrap(temp_arr).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		}

		byte pointList_arr[] = new byte[n_points*12];
		byte mean_rss_arr[] = new byte[n_AP*n_points*4];
		byte AP_List[] = new byte[n_AP*17];

		
		float pointList[] = new float[n_points*3];
		float mean_rss[] = new float[n_AP*n_points];
		
		fin.read(pointList_arr, 0, n_points*12);
		fin.read(mean_rss_arr, 0, n_AP*n_points*4);
		fin.read(AP_List, 0, n_AP*17);
		
		String building_name_str = new String(building_name);
		
		for (i=0;i<(int)n_points*3;i++){
			temp_arr[0]=pointList_arr[i*4];
			temp_arr[1]=pointList_arr[i*4+1];
			temp_arr[2]=pointList_arr[i*4+2];
			temp_arr[3]=pointList_arr[i*4+3];
			pointList[i] = ByteBuffer.wrap(temp_arr).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		}
		
		for (i=0;i<(int)n_points*(int)n_AP;i++){
			temp_arr[0]=mean_rss_arr[i*4];
			temp_arr[1]=mean_rss_arr[i*4+1];
			temp_arr[2]=mean_rss_arr[i*4+2];
			temp_arr[3]=mean_rss_arr[i*4+3];
			mean_rss[i] = ByteBuffer.wrap(temp_arr).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		}
		
		localizationcore.InjectRadioMap(AP_List,mean_rss,pointList,n_points,n_AP,A,B);
		Toast.makeText(getApplicationContext(),building_name_str, Toast.LENGTH_LONG).show();
		Toast.makeText(getApplicationContext(),"Unique ID: "+String.valueOf(unique_ID), Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(),"Number of floors: "+String.valueOf(n_floors), Toast.LENGTH_SHORT).show();
		
	}
	catch (FileNotFoundException e) {
		//System.out.println("File not found" + e);
	}
	catch (IOException ioe) {
		//System.out.println("Exception while reading file " + ioe);
	}
	
	
	finally {
		// close the streams using close method
		try {
			if (fin != null) {
				fin.close();
			}
		}
		catch (IOException ioe) {
			//System.out.println("Error while closing stream: " + ioe);
		}
	}
}

public void loadPref(){
	  SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	  
	  /* Algorithms */
	  params_sim[0]=(float)Integer.valueOf(mySharedPreferences.getString("wifi_localization_algorithm", "1"));
	  params_sim[1]=1;
	  

	  /* Startup */
	  autostart_enable = mySharedPreferences.getBoolean("autostart_enable", true);
	  
	  String hardcoded_string = mySharedPreferences.getString("preset_location", "0");
	  if (hardcoded_string.equals("0")) {
		  hardcoded = 0;
		  hard_lat = (float) 0.0;
		  hard_longi = (float) 0.0;
		  hard_floor = 0; 
	  } else {
		  String values[] = hardcoded_string.split(":");
		  hardcoded = 1; 
		  float lat = Float.parseFloat(values[0]); 
		  float longi = Float.parseFloat(values[1]); 
		  hard_floor = Integer.parseInt(values[2]);
		  hard_lat = translateLatitude(lat, hard_floor);
		  hard_longi = translateLongitude(longi, hard_floor);
		  User cu = db.getUser();
		  cu = db.getUser();
		  if (cu != null) {   
			  Location loc = new Location();
			  loc.setBldg("Ba");
			  loc.setFloor(hard_floor);
			  loc.setLatitude(hard_lat);
			  loc.setLongitude(hard_longi);
			  loc.setPlot(false);
			  loc.setAccuracy(0);
			  loc.setBearing(0);
			  loc.setUser_id(cu.getUserId());
			  db.addLocation(loc);
		  }
	  }
			  
}

public void start_process(){
	if (!start){

        try {
            // Loading map
          setUpFragments();
 
        } catch (Exception e) {
            e.printStackTrace();
        }


		registerReceiver(receiver, new IntentFilter(
		WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		boolean wifi = false; 
		if (wifiManager != null)
			wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		wifi = wifiManager.startScan();
		System.out.println("DEBUG: GETTING WIFI SIGNALS:" + wifi);
	counter=1;

	
	// initialize variables
	int ii;
	
	
	for (ii=0;ii<3;ii++){
		gyro_sim[ii]=-1000;
		accel_sim[ii]=-1000; 
		magnet_sim[ii]=-1000;
		gravity_sim[ii]=-1000;
		linearaccel_sim[ii]=-1000;
		rotationvector_sim[ii]=-1000;
		gamerotation_sim[ii]=-1000;
		orientation_sim[ii]=-1000;
	}
	for (ii=0;ii<9;ii++){
		rotationmatrix_sim[ii]=-1000;
	}
	pressure_sim=1000;		
	if (localizationcore == null) 
		localizationcore = new LocalizationCore();
	localizationcore.initialize();
	start=true;
	Load_RadioMap();
	//receiver.onReceive(this, null);

	if (map != null){
		map.setMyLocationEnabled(true);
	changefloor((int)geolocation_sim[2]);
	}
}
}

public void stop_process(){
	if (start && (map != null)){
		System.out.println("DEBUG - STOPPING LOCATION TRACKING");
		unregisterReceiver(receiver);
		map.setMyLocationEnabled(false);
		start=false;
		
	}
}

@Override
public void onStop() {
	super.onStop();
	//wifiManager.setWifiEnabled(true);
	stop_process();
}

@Override
protected void onPause() {
    super.onPause();
   // wifiManager.setWifiEnabled(true);
   stop_process();
}

// Handling wifi samples
public class WiFiScanReceiver extends BroadcastReceiver {
	
	@Override
	  public void onReceive(Context c, Intent intent) {
		
		
		List<ScanResult> results = wifiManager.getScanResults();
		
		if (results != null){

		
		for (counter_wifi=0;counter_wifi<MAX_WIFI_APs;counter_wifi++){
			wifi_rss_buff[counter_wifi]=-200;
			wifi_mac_buff[counter_wifi]="00:00:00:00:00:00";
		}
		counter_wifi=0;
		
	    for (ScanResult result : results) {
	    	
	    	String name = result.BSSID;
			int level = result.level;
			
			wifi_rss_buff[counter_wifi]=level;
			wifi_mac_buff[counter_wifi]=name;
			counter_wifi++;
				
	    } 		    
    	
	    counter++;
	    
		}
		
		
		for (int i=0;i<MAX_WIFI_APs;i++){
			wifi_rss_sim[i] = (float)wifi_rss_buff[i];
			
		}
		
		byte[] mac_byte= new byte[17];
		for (int i=0;i<(MAX_WIFI_APs);i++){
			mac_byte = wifi_mac_buff[i].getBytes();
			for (int j=0;j<17;j++){
				wifi_mac_sim[i*17+j]=mac_byte[j];
			}
		}
		
		
		wifi_status_sim=(float) 1.0;
		
		localizationcore.RunModel(gyro_sim,accel_sim,magnet_sim,gravity_sim,linearaccel_sim,rotationvector_sim,gamerotation_sim,
				orientation_sim,rotationmatrix_sim,pressure_sim,gps_sim,gps_status_sim,wifi_rss_sim,wifi_mac_sim,wifi_status_sim,
				ble_rss_sim,ble_mac_sim,ble_coordinates_sim,ble_tx_powers_sim,ble_status_sim,params_sim,location_sim,geolocation_sim,accuracy_sim,speed_sim,steps_sim);

		if (hardcoded == 0) {
			SetMyLocation(geolocation_sim[0],geolocation_sim[1],(int)geolocation_sim[2],accuracy_sim[0],bearing);
		} else if (hardcoded == 1) {
			SetMyLocation(hard_lat,hard_longi,hard_floor,0,0);
		}
		//send location to server
		
		
		
		if(start)
			wifiManager.startScan();
		
	  }
}

public void SetMyLocation(float latitude,float longitude,int floor,float accuracy,float bearing){
	// latitude and longitude
	//Log.d("FLOOR",Integer.toString(floor)+Integer.toString(load_floor));
	/*if (groundOverlay == null) {
		//alert.create_alert("Error", "Can't connect to internet right now\n. Try again later");
		return;
	}*/
	if ((floor != load_floor) && (groundOverlay != null)){
	switch(floor){
	case 1:
		hidemarkers(floor);	
		groundOverlay.setImage(floor1);
		break;
	case 2:
		hidemarkers(floor);	
		groundOverlay.setImage(floor2);
		break;
	case 3:
		hidemarkers(floor);	
		groundOverlay.setImage(floor3);
		break;
	case 4:
		hidemarkers(floor);	
		groundOverlay.setImage(floor4);
		break;
	case 5:
		hidemarkers(floor);	
		groundOverlay.setImage(floor5);
		break;
	case 6:
		hidemarkers(floor);	
		groundOverlay.setImage(floor6);
		break;
	case 7:
		hidemarkers(floor);	
		groundOverlay.setImage(floor7);
		break;
	case 8:
		hidemarkers(floor);	
		groundOverlay.setImage(floor8);
		break;
	default:
		return;
	}
	}
	load_floor=floor;

	if (mylocation != null)
		mylocation.push_location(latitude, longitude, 0f, accuracy, bearing);

  if (session == false) {
	  return;
  }
  User cu = db.getUser();
  if (cu != null) {   
	  Location loc = new Location();
	  loc.setBldg("Ba");
	  loc.setFloor(floor);
	  loc.setLatitude(latitude);
	  loc.setLongitude(longitude);
	  loc.setPlot(false);
	  loc.setAccuracy(accuracy);
	  loc.setBearing(bearing);
	  loc.setUser_id(cu.getUserId());
	  db.addLocation(loc);
  }
  http_console.AddLocation("BA", Integer.toString(floor), Float.toString(latitude), Float.toString(longitude), Float.toString(accuracy), Float.toString(bearing));
}


public void changefloor(int floor){
	// latitude and longitude
	Log.d("FLOOR",Integer.toString(floor)+Integer.toString(load_floor));
	if (floor != load_floor){
		
		hidemarkers(floor);
		switch(floor){
		case 1:
			groundOverlay.setImage(floor1);
			break;
		case 2:
			groundOverlay.setImage(floor2);
			break;
		case 3:
			groundOverlay.setImage(floor3);
			break;
		case 4:
			groundOverlay.setImage(floor4);
			break;
		case 5:
			groundOverlay.setImage(floor5);
			break;
		case 6:
			groundOverlay.setImage(floor6);
			break;
		case 7:
			groundOverlay.setImage(floor7);
			break;
		case 8:
			groundOverlay.setImage(floor8);
			break;
		default:
			return;
		}
	}
	load_floor=floor;
}

public void hidemarkers(int floor){
	for (int i = 0; i < markers.size(); i++) {
		if (markers.get(i).getFloor() == floor){
			markers.get(i).getMarker().setVisible(true);
		}
		else{
			markers.get(i).getMarker().setVisible(false);
		}
	}
	
	for (int j = 0; j < pathlines.size(); j++) {
		if (pathlines.get(j).getFloor() == floor){
			pathlines.get(j).getLine().setVisible(true);
		}
		else{
			pathlines.get(j).getLine().setVisible(false);
		}
	}
	
}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 //super.onActivityResult(requestCode, resultCode, data);
 
 /*
  * To make it simple, always re-load Preference setting.
  */
 
 loadPref();
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	if ((keyCode == KeyEvent.KEYCODE_BACK)) {
		moveTaskToBack(true);
	} 
	return super.onKeyDown(keyCode, event);
}

private float translateLatitude(float oldLat, int floor) {
	float difflat; 
	double oldlat = 43.659652988335878;
	if(floor == 1){
  		difflat = (float) (43.6596355 - oldlat);
  	  }
  	 else if(floor == 2){
  		difflat = (float) (-0.00004);
   	  }
  	 else{
   		difflat = (float) (43.659658 - oldlat);
  	 }
	return oldLat + difflat;
}

private float translateLongitude(float oldLongi, int floor) {
	float difflong; 
	double oldlong = -79.397276867154886;
	if(floor == 1){
		difflong = (float)(oldlong - -79.397400);
  	  }
  	 else if(floor == 2){
 		difflong = (float)( 0.00015);
   	  }
  	 else{
 		difflong = (float)( oldlong - -79.397435);
  	 }
	return oldLongi + difflong;
}


  
}
