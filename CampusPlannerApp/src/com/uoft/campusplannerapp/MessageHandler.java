package com.uoft.campusplannerapp;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.uoft.campusplannerapp.HTTPConsole;

public class MessageHandler extends IntentService {

	String mes, msg;
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
        	http.SendLocation(email, "BA", "3", "100", "100");
        } else if (title.equals("putLocation")) {
        	String message = extras.getString("message");
        	http.SetLoc(message);
        	Log.i("com.uoft.campusplannerapp", message);
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
