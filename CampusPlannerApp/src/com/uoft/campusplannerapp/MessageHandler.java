package com.uoft.campusplannerapp;

import java.util.regex.Pattern;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.uoft.campusplannerapp.HTTPConsole;
import com.uoft.campusplannerapp.Location;
import com.uoft.campusplannerapp.R;

public class MessageHandler extends IntentService {

	String mes, msg;
    @SuppressWarnings("unused")
	private Handler handler;
    HTTPConsole http ; 
	public MessageHandler() {
		super("GcmMessageHandler");
	}
   
	public MessageHandler(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
        http = new HTTPConsole(this);
    }
	@Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        Log.e("com.uoft.campusplannerapp", messageType);
////       showToast();
        Log.i("GCM", "Received : (" +messageType+")  "+extras.getString("title"));
        String title = extras.getString("title");
        if (title.equals("getLocation")) {
        	String email = extras.getString("message");
        	Log.i("com.uoft.campusplannerapp", "Location requested by" + email);
        	Location loc = new Location();
        	loc.GetLocation();
        	http.SendLocation(email, loc.getBldg(), "" + loc.getFloor(), "" + loc.getLatitude(), "" + loc.getLongitude());
        } else if (title.equals("putLocation")) {
        	String message = extras.getString("message");
        	http.SetLoc(message);
        	Log.i("com.uoft.campusplannerapp", message);
        } else if (title.equals("message")) {
        	String from_message = extras.getString("message");
        	String from = from_message.split(Pattern.quote(";"))[0];
        	String message = from_message.split(Pattern.quote(";"))[1];
			String mod_msg = message.replaceAll("%20", " ");
        	Log.i("Sidd", from_message + "^^^^^" + message + "^^^^^" + mod_msg);
        	Uri notisnd = Uri.parse("" + R.raw.fallbackring);
        	NotificationCompat.Builder mBuilder =
        		    new NotificationCompat.Builder(this)
        		    .setSmallIcon(R.drawable.ic_launcher)
        		    .setContentTitle(from)
        		    .setSound(notisnd)
        		    .setContentText(mod_msg);
        	int mNotificationId = 001;
        	
        	// Attach Chat activity as On Click
            // Gets an instance of the NotificationManager service
    	     NotificationManager mNotifyMgr = 
    	             (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	     // Builds the notification and issues it.
    	     mNotifyMgr.notify(mNotificationId, mBuilder.build());
        } else if (title.equals("getResponse")) {
        	String message = extras.getString("message");
        	String event = message.split(Pattern.quote(";"))[0];
        	String creator = message.split(Pattern.quote(";"))[1];
        	String event_id = message.split(Pattern.quote(";"))[2];
			String mod_msg = event.replaceAll("%20", " ");
        	Log.i("Sidd", message + "^^^^^" + message + "^^^^^" + mod_msg);
        	Uri notisnd = Uri.parse("" + R.raw.fallbackring);
        	
        	Intent resultIntent = new Intent(this, ResponseActivity.class);
        	resultIntent.putExtra("EventId", event_id);
        	// Because clicking the notification opens a new ("special") activity, there's
        	// no need to create an artificial back stack.
        	PendingIntent resultPendingIntent =
        	    PendingIntent.getActivity(
        	    this,
        	    0,
        	    resultIntent,
        	    PendingIntent.FLAG_UPDATE_CURRENT
        	);
        	
        	NotificationCompat.Builder mBuilder =
        		    new NotificationCompat.Builder(this)
        		    .setSmallIcon(R.drawable.ic_launcher)
        		    .setContentTitle("Event Invite")
        		    .setSound(notisnd)
        		    .setContentIntent(resultPendingIntent)
        		    .setContentText(mod_msg);
        	int mNotificationId = 001;
        	
        	// Attach Chat activity as On Click
            // Gets an instance of the NotificationManager service
    	     NotificationManager mNotifyMgr = 
    	             (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	     // Builds the notification and issues it.
    	     mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
        

        ServerBroadcastReceiver.completeWakefulIntent(intent);

    }

//    public void showToast(){
//        handler.post(new Runnable() {
//            public void run() {
//                Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
//            }
//         });
//
//    }

}
