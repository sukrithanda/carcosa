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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

    CreateAlert alert;
    DatabaseHandler db;
    User u;
    
    public Spinner spinner2;
    public List<FriendClass> my_friends;
    public Fragment mVisible;
	public SupportMapFragment mMapFragment;
	public Fragment mFriendsFragment;
	public Fragment mOrganizeEventFragment;
	public Fragment mSetUpOfficeHoursFragment;
	public Fragment mPrivacyFragment;
	public Fragment mResourceFragment;

	public GoogleMap map;
	
	//LOCALIZER CODE - START
	
	   // Google Map
   // private GoogleMap googleMap;
    public CurrentLocationProvider mylocation;
    public CurrentLocationProvider mylocation1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(this);
        http_console = new HTTPConsole(this);

        alert = new CreateAlert(this);
        User usero = db.getUser();
        u = usero;
        db.Close();
    	pref = this.getSharedPreferences("User",MODE_PRIVATE);
        @SuppressWarnings("unused")
		String temp = get_reg_id();
    	
     
  
    	if (usero == null) {
            setContentView(R.layout.activity_main);
    	} else {
    		setContentView(R.layout.map_main);  

    		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
    		DrawerLayout m = (DrawerLayout)findViewById(R.id.drawer_layout);
    
    		// Set up the drawer.
    		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,m);

    		setUpFragments();
			showFragment(mMapFragment);
    		//mTitle = getString(R.string.title_map);
    		
    		//LOCALIZATION CODE - START
    		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	
    		
    		localizationcore = new LocalizationCore();
    		
    		try {
    			wifiManager = (WifiManager) getBaseContext()
    					.getSystemService(Context.WIFI_SERVICE);
    			//wifiManager.setWifiEnabled(false);
    		} catch (Exception e) {

    		}

    		// Register Broadcast Receiver
    		receiver=new WiFiScanReceiver();
    	
    		
    		params_sim[0]=1;
    		params_sim[1]=1;
    		loadPref();
    		
    		if(autostart_enable)
    			start_process();
    		
    		//THREADS to receive and send locations

            new ClientReciever().start();
        	new ClientSender().start();
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
				case 4:
					showFragment(mSetUpOfficeHoursFragment);
					//addItemOnSpinner2();
					mTitle = getString(R.string.title_SetupOfficeHours);
					break;
				case 5:
					showFragment(mPrivacyFragment);
					mPrivacyFragment.onResume();
					mTitle = getString(R.string.title_privacySettings);
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
			case 5:
				mTitle = getString(R.string.title_SetupOfficeHours);
				break;
			case 6:
				mTitle = getString(R.string.title_privacySettings);
				break;
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
	}
	
	public void updateTimeButton (String time)
	{
		Button timeButton = (Button) findViewById(R.id.timepickButton);
		timeButton.setText(time);
	}
	
	public void updateDateButton2 (String date)
	{
		Button dateButton = (Button) findViewById(R.id.datepickButton2);
		dateButton.setText(date);
	}
	
	public void updateTimeButton2 (String time)
	{
		Button timeButton = (Button) findViewById(R.id.timepickButton2);
		timeButton.setText(time);
	}
		
	public void showTimePickerDialog(View v) {
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "timePicker");
	}
    
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	
	public void showTimePickerDialog2(View v) {
	    DialogFragment newFragment = new TimePickerFragment2();
	    newFragment.show(getSupportFragmentManager(), "timePicker");
	}
    
	public void showDatePickerDialog2(View v) {
	    DialogFragment newFragment = new DatePickerFragment2();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
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
	        setContentView(R.layout.map_main);
	      
	        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
					.findFragmentById(R.id.navigation_drawer);
	    	DrawerLayout m = (DrawerLayout)findViewById(R.id.drawer_layout);
	    
			// Set up the drawer.
			mNavigationDrawerFragment.setUp(R.id.navigation_drawer,m);

			setUpFragments();
			mVisible = mMapFragment;
			mTitle = getString(R.string.title_map);
	        
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
            boolean sts = http_console.Logout();
            if (sts == true){
            	db.deleteDatabses();
            	setContentView(R.layout.activity_main);
            } else { 
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
          else{
        	  return super.onOptionsItemSelected(item);
          }
    }

    
    public static class OrganizeEventFragment extends Fragment {
    	public static final String TAG = "organizeEvent";    
    	
    	// Constructor of organizeEvent
    	public OrganizeEventFragment() {
    		super();
    	}
    	
		public static OrganizeEventFragment newInstance() {
			OrganizeEventFragment fragment = new OrganizeEventFragment();
			return fragment;
		}
    	
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			View rootView = inflater.inflate(R.layout.event_organizer, container,
					false);
			
			return rootView;
		}
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
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

        if (mVisible != null) {
        	ft.hide(mVisible);
        }

        ft.show(fragmentIn).commit();
        mVisible = fragmentIn;
    }
	
	public void showFriend(int floor, float latitude, float longitude, final String name, final String email) {
	 
  	  LatLng pinLocation = new LatLng(latitude, longitude);

	
        	      Marker storeMarker = map.addMarker(new MarkerOptions()
        	      .position(pinLocation)
        	      .title(name)
        	      .snippet(email));
      
           
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
                
        	//map= mMapFragment.getMapAsync(new OnMapReadyCallback());
        	//initilizeMap(map);
            
            ft.add(R.id.container, mMapFragment, MFragment.TAG);
        }
        else{
        	
        	  mMapFragment.getMapAsync(new OnMapReadyCallback() {
                  @Override
                  public void onMapReady(GoogleMap googleMap) {
                      map = googleMap;
                      Load_Maps();
                      initilizeMap();
                  }
              });
        	//map= mMapFragment.getMapAsync(this);
        	//initilizeMap(map);
           ft.replace(R.id.container, mMapFragment, MFragment.TAG);
        }
        ft.hide(mMapFragment);

        mFriendsFragment = (FriendsFragment) getSupportFragmentManager().findFragmentByTag(FriendsFragment.TAG);
        if (mFriendsFragment == null) {
            mFriendsFragment = FriendsFragment.newInstance(this, MainActivity.this, mMapFragment);
            ft.add(R.id.container, mFriendsFragment, FriendsFragment.TAG);
        }
        ft.hide(mFriendsFragment);
        
        mResourceFragment = (ResourceFragment) getSupportFragmentManager().findFragmentByTag(ResourceFragment.TAG);
        if (mResourceFragment == null) {
        	mResourceFragment = ResourceFragment.newInstance(this);
            ft.add(R.id.container, mResourceFragment, ResourceFragment.TAG);
        }
        ft.hide(mResourceFragment);

        mOrganizeEventFragment = (OrganizeEventFragment) getSupportFragmentManager().findFragmentByTag(OrganizeEventFragment.TAG);
        if (mOrganizeEventFragment == null)
        {
        	mOrganizeEventFragment = OrganizeEventFragment.newInstance();
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
 
        ft.commit();

    }
	
	 public void initilizeMap() {
	      //  if (googleMap == null) {
	        //    googleMap = ((MapFragment) getFragmentManager().findFragmentById(
	       //             R.id.map)).getMap();
         // check if map is created successfully or not
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
	  	                new LatLng(43.659652988335878, -79.397276867154886)).zoom(20).build();
	  		  
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
		wifiManager.startScan();

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
	
	localizationcore.initialize();
	start=true;
	Load_RadioMap();
	}
}

public void stop_process(){
	if (start){

		unregisterReceiver(receiver);
		
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

		SetMyLocation(geolocation_sim[0],geolocation_sim[1],(int)geolocation_sim[2],accuracy_sim[0],bearing);
		
		
		
		if(start)
			wifiManager.startScan();
		
	  }
}

public void SetMyLocation(float latitude,float longitude,int floor,float accuracy,float bearing){
	// latitude and longitude
	//Log.d("FLOOR",Integer.toString(floor)+Integer.toString(load_floor));
	if (floor != load_floor){
		
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

  mylocation.push_location(latitude, longitude, 0f, accuracy, bearing);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 //super.onActivityResult(requestCode, resultCode, data);
 
 /*
  * To make it simple, always re-load Preference setting.
  */
 
 loadPref();
}

  
}
