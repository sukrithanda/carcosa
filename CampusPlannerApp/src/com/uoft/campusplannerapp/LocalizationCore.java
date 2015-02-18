package com.uoft.campusplannerapp;

public class LocalizationCore {
	
	  static {
	    System.loadLibrary("LocalizationCore");
	  }
	  
	  private native void step(float gyro[],float accel[],float magnet[],float gravity[],float linearaccel[]
			  ,float rotationvector[],float gamerotation[],float orientation[],float rotationmatrix[],
			  float pressure,float gps[],float gps_status,float wifi_rss[],byte wifi_mac[],float wifi_status,
			  float ble_rss[],byte ble_mac[],float ble_coordinates[],float ble_tx_powers[],float ble_status,float params[]);
	  
	  public native void InjectRadioMap(byte AP_List[],float mean_rss[],float pointList[],float n_points,float n_ap, float A[], float B[]);
	  
	  public native void initialize();	  
	  private native int[] getlocation();
	  private native float[] getgeolocation();
	  private native float[] getspeed();
	  private native float getaccuracy();
	  private native float getsteps();
	  
	  public void RunModel(float gyro[],float accel[],float magnet[],float gravity[],float linearaccel[]
			  ,float rotationvector[],float gamerotation[],float orientation[],float rotationmatrix[],
			  float pressure,float gps[],float gps_status,float wifi_rss[],byte wifi_mac[],float wifi_status,
			  float ble_rss[],byte ble_mac[],float ble_coordinates[],float ble_tx_powers[],float ble_status,float params[],
			  int location[],float geolocation[],float accuracy[],float speed[],float steps[]){
		  
		  int[] result1=new int[3];
		  float[] result2=new float[3];

		  int i;
		  step(gyro,accel,magnet,gravity,linearaccel,rotationvector,gamerotation,orientation,rotationmatrix,pressure,
				  gps,gps_status,wifi_rss,wifi_mac,wifi_status,ble_rss,ble_mac,ble_coordinates,ble_tx_powers,ble_status,params);
		  
		  result1=getlocation();
		  for (i=0;i<3;i++)
			  location[i]=result1[i];
		  
		  result2=getgeolocation();
		  for (i=0;i<3;i++)
			  geolocation[i]=result2[i];
		  
		  result2[0]=getaccuracy();
		  accuracy[0]=result2[0];
		  
		  result2=getspeed();
		  for (i=0;i<3;i++)
			  speed[i]=result2[i];
		  
		  result2[0]=getsteps();
		  steps[0]=result2[0];
	  }
	}