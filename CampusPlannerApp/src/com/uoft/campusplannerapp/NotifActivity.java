package com.uoft.campusplannerapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotifActivity extends Activity {
	
	HTTPConsole http_console;
	String event_id;
	boolean type_bool = false; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		http_console = new HTTPConsole(this);
		Intent intent = getIntent();
		String type = intent.getStringExtra("Type");
		String event = intent.getStringExtra("Event");
		String creator = intent.getStringExtra("Creator");
		event_id = intent.getStringExtra("Event_id");
		String nid = intent.getStringExtra("notification");
		int notif = Integer.parseInt(nid);
		System.out.println("type = " + type);
		System.out.println("Event = " + event);
		System.out.println("Creator = " + creator);
		System.out.println("event_id = " + event_id);
		System.out.println("nid = " + nid);
		NotificationManager mNotifyMgr = 
	             (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    mNotifyMgr.cancel(notif); 
	    setContentView(R.layout.notif_activity);
	    if (type.equals("eventCancelled")){
	    	TextView title = (TextView) findViewById(R.id.notifTitle);
	    	title.setText("Event Cancelled");
	    	TextView message = (TextView) findViewById(R.id.notifMessage);
	    	message.setText(creator + " has cancelled the event " + event + "\n." +
	    			"Do you want to remove this from your calendar ");
	    	Button positiveButton = (Button) findViewById(R.id.positiveButton);
	    	positiveButton.setText("Yes Remove it");
	    	Button negativeButton = (Button) findViewById(R.id.negativeButton);
	    	negativeButton.setText("No Keep it");
	    	
	    } else if (type.equals("userNotAttending")){
	    	type_bool = true;
	    	TextView title = (TextView) findViewById(R.id.notifTitle);
	    	title.setText("User Not Attending");
	    	TextView message = (TextView) findViewById(R.id.notifMessage);
	    	message.setText(creator + " is not attending your event \" " + event + "\" \n." +
	    			"Do you want keep the event or cancel it ");
	    	Button positiveButton = (Button) findViewById(R.id.positiveButton);
	    	positiveButton.setText("Yes Keep it");
	    	Button negativeButton = (Button) findViewById(R.id.negativeButton);
	    	negativeButton.setText("No Remove it");
	    	
	    }
	}
	public boolean positiveButton(View view){
		if (type_bool == true) {
			long eid = Long.parseLong(event_id);
			http_console.DeleteEvent(eid);
			finish();
		} else {
			// Remove from calendar
			finish();
		}
		return true;
	}
	public boolean negativeButton(View view){
		if (type_bool == true) {
			finish();
		} else {
			finish();
		}
		return true;
	}

}
