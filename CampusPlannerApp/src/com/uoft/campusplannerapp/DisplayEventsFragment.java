package com.uoft.campusplannerapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class DisplayEventsFragment extends Fragment{

	// Global Variables
	public static final String TAG = "Display Events";
	public Context ctx; 
	private HTTPConsole http_console;
	private DatabaseHandler db;
	private String userEmail;
	private String user; 
	private List<EventClass> my_events;
	private List<FriendClass> my_friends;
	private ListView lv;
	private ArrayAdapter<String> ev_adp;
	private List<String> events_array;
	View rv;

	
	// Public Constructor
	public DisplayEventsFragment() {
		super();
	}
	
	// Create new instance of displayEvents Fragment
	public static DisplayEventsFragment newInstance(Context ctx) {
		DisplayEventsFragment fragment = new DisplayEventsFragment();
		fragment.ctx = ctx;
		return fragment;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		View rootView = inflater.inflate(R.layout.display_event, container,
				false);		
		rv = rootView;
		
		// Create new http console
		http_console = new HTTPConsole(ctx); 
		get_events(rootView);
		
	
		
		return rootView;
	}
	
	public ArrayAdapter<String> getEventsAdapter()
	{
		return this.ev_adp;
	}
	
	
	// Create function to get events
	private void get_events(View rootView)
	{
		// Get user
	    db = new DatabaseHandler(ctx);
		User u = db.getUser();
		db.Close();
		
		// Check if user is valid
		if (u == null) { 
			System.err.println("Error getting user");
		}
		userEmail = u.getEmail();
		// Get events from http console
		my_events = http_console.GetEvents();
		int num_events = my_events.size();
		
		lv = (ListView) rootView.findViewById(R.id.eventlistview);
		
		events_array = new ArrayList<String>();
		
		// Array of event descriptors
		//events = new String[num_events];
		int i = 0;
		for(i = 0; i < num_events; i++)
		{
			EventClass ev = my_events.get(i);
			//String sFromTime = String.valueOf(ev.getFrom_time());
			String sFromTime = String.format("%.2f", ev.getFrom_time());
			String colonFormatedFromTime = sFromTime.replace(".", ":");
			String s = ev.getName() + " in " + ev.getLocation() + " at " + colonFormatedFromTime;
			System.out.println("String output is: " + s);
			events_array.add(s);
		}
			
		ev_adp = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,events_array);
		lv.setAdapter(ev_adp);
		
		// onItemClick only allows final contexts
		final Context final_ctx = ctx;
		
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				System.out.println("Clicked listview button");
				eventDetailDialog(final_ctx, view);
				
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
			{
				System.out.println("Long Clicked listview button");
				
				deleteEventDialog(final_ctx, view);
				return true;
			}
		});
		//System.out.println("Size of events list is: " + num_events);		
	}
	
	private void eventDetailDialog(Context ctx, View v) 
	{
		AlertDialog.Builder eventDialog = new AlertDialog.Builder(ctx);
		TextView tv = (TextView) v;
		String dialogMessage = null;
		final String event = tv.getText().toString();
		
		int i = 0;
		for(i = 0; i < my_events.size(); i++)
		{
			EventClass AllEvents = my_events.get(i);
			//String sFromTime2 = String.valueOf(AllEvents.getFrom_time());
			String sFromTime2 = String.format("%.2f", AllEvents.getFrom_time());
			String colonFormatedFromTime2 = sFromTime2.replace(".", ":");
			String thatEvent = AllEvents.getName() + " in " + AllEvents.getLocation() + " at " + colonFormatedFromTime2;
			if(event.equals(thatEvent))
			{
				// Make call to get invitees
				User u = db.getUser();
				user = u.getEmail();
				my_friends = http_console.GetFriend(user);
				List<EventClass> eventinvitees = http_console.GetInvitees(AllEvents.getId());
				String invitees[] = new String[eventinvitees.size()];
				long iID[] = new long[eventinvitees.size()];
				int j = 0;
				for(j = 0; j < eventinvitees.size(); j++)
				{

					FriendClass fr = db.getFriendFromId(eventinvitees.get(j).getUser());
					invitees[j] = fr.getFirst_name() + " " + fr.getLast_name();
					// Did that person accept?
					long responseCode = eventinvitees.get(j).isResponse();
					//System.out.println("Invitee: " + invitees[j] + "and their response code is:" + responseCode);
					String appendedS;
					if(responseCode == 0)
					{
						appendedS = "";
					}
					else if (responseCode == 1)
					{
						appendedS = "- Accepted ";
						
					}
					else if (responseCode == -1)
					{
						appendedS = "- Declined ";
					}
					else
					{
						System.out.println("ERROR! SHOULD NOT BE HERE!");
						appendedS = "";
					}
					invitees[j] += " " + appendedS;
				}
				//for (EventClass accInvite: eventinvitees)
				//{
				//	for(j = 0; j < eventinvitees.size(); j++)
				//	{
						
				//	}
				//}
				
				//String sFromTime = String.valueOf(AllEvents.getFrom_time());
				String sFromTime = String.format("%.2f", AllEvents.getFrom_time());
				//String sToTime = String.valueOf(AllEvents.getTo_time());
				String sToTime = String.format("%.2f", AllEvents.getTo_time());
				
				String colonFormatedFromTime = sFromTime.replace(".", ":");
				String colonFormatedToTime = sToTime.replace(".", ":");
				
				dialogMessage = "Event Name: " + AllEvents.getName() + " \n" +
								"Event Location: " + AllEvents.getLocation() + " \n" +
								"From: " + AllEvents.getFrom_date() + " @ " + colonFormatedFromTime + " \n" +
								"To: " + AllEvents.getTo_date() + " @ " + colonFormatedToTime + " \n" + 
								"Invitees: ";
				int k = 0;
				for (k = 0; k < eventinvitees.size(); k ++) 
				{
					dialogMessage += invitees[k] + " \n";
				}
								
			}
		}
		
		final Context f_ctx = ctx;
		
		eventDialog.setTitle("Event Details");
		eventDialog.setMessage(dialogMessage);
		eventDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton)
			{
				dialog.cancel();
			}
		});
		eventDialog.setNegativeButton("Add Event to Calendar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton)
			{
				//Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials).setApplicationName("applicationName").build();
				
			}
		});
		eventDialog.show();
	}
	
	private void deleteEventDialog(Context ctx, View v) 
	{
		AlertDialog.Builder eventDialog = new AlertDialog.Builder(ctx);
		TextView tv = (TextView) v;
		final String event = tv.getText().toString();
		
		eventDialog.setTitle("Delete Event?");
		eventDialog.setMessage("Delete event: " + event + "?");
		eventDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				// TO DO:
				// Add code to remove event!
				for(EventClass levents: my_events)
				{
					String sFromTime = String.format("%.2f", levents.getFrom_time());
					String colonFormatedFromTime = sFromTime.replace(".", ":");
					String s = levents.getName() + " in " + levents.getLocation() + " at " + colonFormatedFromTime;
					if(s.equals(event))
					{
						System.out.println("events "+event);
						//my_events.get(indexOf(levents));
						events_array.remove(s);
						// Get id of event select
						long dlt_ID = levents.getId();
						System.out.println("Event id i want to delete is" + dlt_ID);
						boolean deleted = http_console.DeleteEvent(dlt_ID);
						
					}
				}
				ev_adp.notifyDataSetChanged();
			}
		});
		eventDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				dialog.cancel();
			}
		});
		eventDialog.show();
		
	}
	
	// On resume 
	public void onResume() 
	{
		if(rv != null) 
		{
			get_events(rv);
		}
		ev_adp.notifyDataSetChanged();
		super.onResume();
	}
	

	

	
}
