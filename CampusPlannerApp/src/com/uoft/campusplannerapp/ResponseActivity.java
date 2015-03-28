package com.uoft.campusplannerapp;

import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ResponseActivity extends Activity{
	HTTPConsole http_console = null;
	String event_id = null;
	DatabaseHandler db = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		event_id = intent.getStringExtra("EventId");
		String creator = intent.getStringExtra("Creator");
		String nid = intent.getStringExtra("notification");
		int notif = Integer.parseInt(nid);
	    NotificationManager mNotifyMgr = 
	             (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    mNotifyMgr.cancel(notif); 
		setContentView(R.layout.response_activity);
		http_console = new HTTPConsole(this);
		db = new DatabaseHandler(this);
		User current = db.getUser();
		List<FriendClass> friends = db.getFriends();
		if (friends == null) {
			friends = http_console.GetFriend(current.getEmail());
		}
		List<EventClass> event_invitees = http_console.GetInvitees(Long.parseLong(event_id));
		if (event_invitees == null) 
			return;
	    String names[] = new String[event_invitees.size()];
	    
		int i = 0; 
		for (i = 0; i < event_invitees.size(); i++) {
			names[i] = "";
		}
		for (i = 0; i < event_invitees.size(); i++) {
			if (i == 0){
				TextView tv = (TextView) findViewById(R.id.eventName);
				tv.setText(event_invitees.get(i).getName());
				
				tv = (TextView) findViewById(R.id.creatorName);
				tv.setText(creator);
				
				tv = (TextView) findViewById(R.id.TimeAct);
				String sFromTime = String.format("%.2f", event_invitees.get(i).getFrom_time());
				String colonFormatedFromTime = sFromTime.replace(".", ":");
				String sToTime = String.format("%.2f", event_invitees.get(i).getTo_time());
				String colonFormatedToTime = sToTime.replace(".", ":");
				String time = "" + colonFormatedFromTime + " " + event_invitees.get(i).getFrom_date() + 
						" - " + 
				colonFormatedToTime + " " + event_invitees.get(i).getTo_date();
				tv.setText(time);
				
				tv = (TextView) findViewById(R.id.WhereAct);
				tv.setText(event_invitees.get(i).getLocation());
			}
			
			FriendClass fr = db.getFriendFromId(event_invitees.get(i).getUser());
			if (fr == null) {
				if (current.getUserId() == event_invitees.get(i).getUser()) {
					names[i] = "You";
				} else {
					names[i] = "Unknown User";
				}
			}
			else {
				names[i] = fr.getFirst_name() + " " + fr.getLast_name();
			}

		}
	    TextView lText = new TextView(this);
        lText.setId(0); 
		ListView lv = (ListView) findViewById(R.id.inviteeScroll);
	    ArrayAdapter<String> fr_adp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lText.getId(),names);
	    lv.setAdapter(fr_adp);
	}
	
	public boolean yesButton(View view){
		long eid = Long.parseLong(event_id);
		http_console.SetResponse(eid, true);
		finish();
		return true;
	}
	public boolean noButton(View view){
		long eid = Long.parseLong(event_id);
		http_console.SetResponse(eid, false);
		finish();
		return true;
	}

}
