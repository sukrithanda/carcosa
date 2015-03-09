package com.wifilocalizer;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;



public class MainActivity extends Activity{
	
    // Google Map
    private GoogleMap googleMap;
    private CurrentLocationProvider mylocation;
    private GroundOverlay groundOverlay;
    float bearing;
    
    
	
	BitmapDescriptor floor1;
	BitmapDescriptor floor2;
	BitmapDescriptor floor3;
	BitmapDescriptor floor4;
	BitmapDescriptor floor5;
	BitmapDescriptor floor6;
	BitmapDescriptor floor7;
	BitmapDescriptor floor8;
	private int load_floor=1;
		
	
	LocalizationCore localizationcore;
	


    private static final int MAX_WIFI_APs = 200;
	private int counter_wifi=0;
    private int[] wifi_rss_buff= new int[MAX_WIFI_APs];
    private String[] wifi_mac_buff= new String[MAX_WIFI_APs];
    
    
	// Wifi scanner part
    int counter=1;
	WiFiScanReceiver receiver;
	WifiManager wifiManager = null;

	
	
    
	private float[] gyro_sim = new float[3];
	private float[] accel_sim = new float[3];
	private float[] magnet_sim = new float[3];
	private float[] gravity_sim = new float[3];
	private float[] linearaccel_sim = new float[3];
	private float[] rotationvector_sim = new float[3];
	private float[] gamerotation_sim = new float[3];
	private float[] orientation_sim = new float[3];
	private float[] rotationmatrix_sim = new float[9];
	private float pressure_sim,gps_status_sim,wifi_status_sim,ble_status_sim;
	private float[] gps_sim = new float[6];
	private float[] wifi_rss_sim = new float[200];
	private byte[]	wifi_mac_sim = new byte[3400];
	private float[] ble_rss_sim = new float[100];
	private byte[]	ble_mac_sim = new byte[1700];
	private float[] ble_coordinates_sim = new float[300];
	private float[] ble_tx_powers_sim = new float[100];
	private float[] params_sim = new float[10];
	
	int[] location_sim=new int[3];
	float[] geolocation_sim=new float[3];
	float[] accuracy_sim = new float[1]; 
	float[] speed_sim=new float[3];
	float[] steps_sim = new float[1]; 
	
	
	boolean autostart_enable=true;
    private boolean start=false;
    
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		
		Load_Maps();
		
        try {
            // Loading map
          initilizeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
        }

        
		localizationcore = new LocalizationCore();
	    
	    // Initialize Wifi
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
		
	}

	

	
	public void start_process(){
		if (!start){

	        try {
	            // Loading map
	          initilizeMap();
	 
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
    
    
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    }
    
    
    
	 /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
  		  googleMap.setMyLocationEnabled(true);
  		  googleMap.setIndoorEnabled(false);
  		 
  		  
  		  googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
  		  CameraPosition cameraPosition = new CameraPosition.Builder().target(
  	                new LatLng(43.659652988335878, -79.397276867154886)).zoom(20).build();
  		  
  		   googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  		   
  		  groundOverlay=googleMap.addGroundOverlay(new GroundOverlayOptions().image(floor1).transparency(0.01f).anchor(0.5f, 0.5f)
  		        .position(new LatLng(43.659652988335878, -79.397276867154886), 100f, 121f).bearing(-17.89f));

  		
  		  mylocation=new CurrentLocationProvider(this);
  		  googleMap.setLocationSource(mylocation);

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
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
	public boolean onPrepareOptionsMenu (Menu menu) {
		if (start){
	        menu.getItem(0).setEnabled(false);
	        menu.getItem(1).setEnabled(true);
	    }else{
	    	menu.getItem(0).setEnabled(true);
	        menu.getItem(1).setEnabled(false);
	    }
	    return true;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_start:
	            start_process();
	        	return true;
	        case R.id.menu_stop:
	            stop_process();
	            return true;
	        case R.id.action_settings:
	        	 Intent i = new Intent(getApplicationContext(), PrefsActivity.class);
               startActivityForResult(i, 0); 
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // TODO Auto-generated method stub
	  //super.onActivityResult(requestCode, resultCode, data);
	  
	  /*
	   * To make it simple, always re-load Preference setting.
	   */
	  
	  loadPref();
	 }
	    
	 private void loadPref(){
	  SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	  
	  /* Algorithms */
	  params_sim[0]=(float)Integer.valueOf(mySharedPreferences.getString("wifi_localization_algorithm", "1"));
	  params_sim[1]=1;
	  

	  /* Startup */
	  autostart_enable = mySharedPreferences.getBoolean("autostart_enable", true);
			  

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
	

	
	
}




