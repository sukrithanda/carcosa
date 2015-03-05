package com.uoft.campusplannerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

public class ResponseActivity extends Activity{
	HTTPConsole http_console = null;
	String event_id = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		event_id = intent.getStringExtra("EventId");
		setContentView(R.layout.response_activity);
		TextView tv = (TextView) findViewById(R.id.eventId);
		tv.setText(event_id);
		http_console = new HTTPConsole(this);
	}
	
	public boolean yesButton(View view){
		long eid = Long.parseLong(event_id);
		http_console.SetResponse(eid, true);
		return true;
	}
	public boolean noButton(View view){
		long eid = Long.parseLong(event_id);
		http_console.SetResponse(eid, false);
		return true;
	}

}
