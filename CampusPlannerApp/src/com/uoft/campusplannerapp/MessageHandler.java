package com.uoft.campusplannerapp;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class MessageHandler extends IntentService {

	String mes, msg;
    private Handler handler;
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
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("location");
        if (mes.equals("None")) {
        	msg = extras.getString("message");
        	// display message
        } else {
        	// get location
        }
//       showToast();
        Log.i("GCM", "Received : (" +messageType+")  "+extras.getString("title"));

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
