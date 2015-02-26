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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.AsyncTask;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.widget.Spinner;
import android.widget.DatePicker;

import com.uoft.campusplannerapp.HTTPConsole;
import com.uoft.campusplannerapp.CreateAlert;;

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
    
    private Spinner spinner2;
    private List<FriendClass> my_friends;
    private String user; 
    
	private Fragment mVisible;
	private SupportMapFragment mMapFragment;
	private Fragment mFriendsFragment;
	private Fragment mOrganizeEventFragment;
	private Fragment mSetUpOfficeHoursFragment;
	private Fragment mSignoutFragment;
	private GoogleMap map;
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
    
	private Calendar dateTime = Calendar.getInstance();
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd yyyy");
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
	
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

        alert = new CreateAlert(this);
        db = new DatabaseHandler(this);
        User usero = db.getUser();
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
				//(DrawerLayout)findViewById(R.id.drawer_layout));

		setUpFragments();
		mVisible = mMapFragment;
		mTitle = getString(R.string.title_map);
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
					mTitle = getString(R.string.title_friends);
					break;
					
				case 2:
					showFragment(mOrganizeEventFragment);
					//addItemOnSpinner2();
					mTitle = getString(R.string.title_OrganizeEvent);
					break;
				case 3:
					showFragment(mSetUpOfficeHoursFragment);
					//addItemOnSpinner2();
					mTitle = getString(R.string.title_SetupOfficeHours);
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
				mTitle = getString(R.string.title_OrganizeEvent);
				break;
			case 4:
				mTitle = getString(R.string.title_SetupOfficeHours);
				break;
			case 5:
				mTitle = getString(R.string.title_signout);
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

    		//list.add(fr.getFirst_name() + " " + fr.getLast_name());
//    		list.add("Tanvi Mehta");
//    		list.add("Siddharth Zaveri");
//    		list.add("Sukrit Handa");

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
	
	public boolean ask_for_location(View view){
		EditText email = (EditText)findViewById(R.id.friend);
		try{
			String friend_email = email.getText().toString();
			User usero = db.getUser();
			String user = usero.getEmail();
			if (user == null) {
				create_alert(this,"You are not signed in");
			}
			String Loc = http_console.LocateFriend(user, friend_email);

			create_alert(this,"Friend is at " + Loc );

			//Log.i("Sidd","Print location now " + Loc);
	      //  setContentView(new MovingImage(this));

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
			View rootView = inflater.inflate(R.layout.setup_officehours, container,
					false);
			
			return rootView;
		}
    }
    
    
    /**
	 * A placeholder fragment containing a simple view.
	 */
	public static class FriendsFragment extends Fragment {
		public static final String TAG = "friends";
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static FriendsFragment newInstance(String friendName) {
			FriendsFragment fragment = new FriendsFragment();
			Bundle args = new Bundle();
			args.putString(ARG_SECTION_NUMBER, friendName);
			fragment.setArguments(args);
			return fragment;
		}

		public FriendsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);
			textView.setText(getArguments().getString(
					ARG_SECTION_NUMBER));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static  class MFragment extends SupportMapFragment {
		public static final String TAG = "map";
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */

		public MFragment() {
			super();
		}

		@Override
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			View rootView = inflater.inflate(R.layout.fragment_map, container,
					false);
			
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
		}
	}
	
	private void showFragment(Fragment fragmentIn) {
        if (fragmentIn == null) return;

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        if (mVisible != null) ft.hide(mVisible);

        ft.show(fragmentIn).commit();
        mVisible = fragmentIn;
    }

	private void setUpFragments() {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // If the activity is killed while in BG, it's possible that the
        // fragment still remains in the FragmentManager, so, we don't need to
        // add it again.
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mMapFragment == null) {
            mMapFragment = MFragment.newInstance();
            ft.add(R.id.container, mMapFragment, MFragment.TAG);
        }
        else{
        	map= mMapFragment.getMap();
        	map.setIndoorEnabled(false);
           ft.replace(R.id.container, mMapFragment, MFragment.TAG);
        }
        ft.show(mMapFragment);

        mFriendsFragment = (FriendsFragment) getSupportFragmentManager().findFragmentByTag(FriendsFragment.TAG);
        if (mFriendsFragment == null) {
            mFriendsFragment = FriendsFragment.newInstance("Gary");
            ft.add(R.id.container, mFriendsFragment, FriendsFragment.TAG);
        }
        ft.hide(mFriendsFragment);

        mOrganizeEventFragment = (OrganizeEventFragment) getSupportFragmentManager().findFragmentByTag(FriendsFragment.TAG);
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
 
        ft.commit();
        
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    /*    int id = item.getItemId();
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
        } else if (id == R.id.meeting) {
        	menu_button(null);
        } 
        return super.onOptionsItemSelected(item);*/
    }


  
}
