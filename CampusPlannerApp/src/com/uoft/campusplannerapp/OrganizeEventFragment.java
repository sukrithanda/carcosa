package com.uoft.campusplannerapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import com.uoft.campusplannerapp.DatePickerFragment;


public class OrganizeEventFragment extends Fragment implements DateInterface{
	public static final String TAG = "organizeEvent";   
	private HTTPConsole http_console;
	public Context ctx; 
	private AutoCompleteTextView actv;
	private List<FriendClass> my_friends;
	private DatabaseHandler db;
	private String user;
	View rv;
    
    List<String> pickedFriends = new ArrayList<String>();
    String room = null;
	
	// Constructor of organizeEvent
	public OrganizeEventFragment() {
		super();
	}
	
	public static OrganizeEventFragment newInstance(Context ctx) {
		OrganizeEventFragment fragment = new OrganizeEventFragment();
		fragment.ctx = ctx;
		return fragment;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.event_organizer, container,
				false);
		rv = rootView;
		//Button addfriendbutton = (Button) rootView.findViewById(R.id.imageButton1);
		
		http_console = new HTTPConsole(ctx); 
		actv = (AutoCompleteTextView) rootView.findViewById(R.id.editText2);
		
	    db = new DatabaseHandler(ctx);
		User u = db.getUser();
		db.Close();
		if (u == null) { 
			System.err.println("Error getting user");
		}
		user = u.getEmail();
		my_friends = http_console.GetFriend(user);
		int num_friends = my_friends.size();
		
	    String names[] = new String[num_friends];
	    int i = 0; 
	    for (i = 0; i < num_friends; i++){
	    	FriendClass fr = my_friends.get(i);
	    	names[i] = fr.getFirst_name() + " " + fr.getLast_name();
	    }
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,names);
	    
	    actv.setAdapter(adapter);
	    
	    /*Setup On clicks */ 
	    //setupListView(rootView);
	    setupLocationTypeOnClick(rootView);
	    setupAutoBtnOnClick(rootView);
	    setupSubmitButtonOnClick(rootView);
	    setupRoomOnClick(rootView);
	    setupDate1OnClick(rootView);
	    setupDate2OnClick(rootView);
	    setupTime1OnClick(rootView);
	    setupTime2OnClick(rootView);
	    setupClearBtnOnClick(rootView);
	    setupLocationFirstSpinnerOnClick(rootView);


		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	private void setupLocationFirstSpinnerOnClick(View rootView){
		final View frv = rootView;
		Spinner locType = (Spinner) rootView.findViewById(R.id.spinner3);
	    locType.setOnItemSelectedListener(new OnItemSelectedListener() {
	        @Override
	        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	        	String type = parentView.getItemAtPosition(position).toString();
				String roomList[] = {};
				if (type.equals("Lab")) {
					roomList = getResources().getStringArray(R.array.resourceTypeLab);
				} else if (type.equals("Room")) {
					roomList = getResources().getStringArray(R.array.resourceTypeRoom);
					
				} else if (type.equals("Washroom")) {
					roomList = getResources().getStringArray(R.array.resourceTypeWashroom);
					
				} else if (type.equals("Studying")) {
					roomList = getResources().getStringArray(R.array.resourceTypeStudyArea);
					
				} else if (type.equals("Elevator/Stairs")) {
					roomList = getResources().getStringArray(R.array.resourceTypeElevatorStairs);
					
				} else if (type.equals("Other")) {
					roomList = getResources().getStringArray(R.array.resourceTypeOther);
				}

	            Spinner locList = (Spinner) frv.findViewById(R.id.spinner5);
	            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,roomList);
	            locList.setAdapter(adapter);
	        }

	        @Override
	        public void onNothingSelected(AdapterView<?> parentView) {
	            // your code here
	        }

	    });
	}
	
	private void setupLocationTypeOnClick(View rootView){
		final View frv = rootView;
	    
	    Spinner locType = (Spinner) rootView.findViewById(R.id.spinner5);
	    locType.setOnItemSelectedListener(new OnItemSelectedListener() {
	        @Override
	        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	            String type = parentView.getItemAtPosition(position).toString();
	            List<ResourceClass> rsc = http_console.getResources(type);
	            int size = 0;
	            if (rsc != null) {
	            	size = rsc.size();
	            }
	            String rooms[] = new String[size];
	            int j = 0; 
	            for (j = 0; j < size; j++) {
	            	rooms[j] = rsc.get(j).getResource();
	            }
	            Spinner locList = (Spinner) frv.findViewById(R.id.spinner4);
	            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,rooms);
	            locList.setAdapter(adapter);
	        }

	        @Override
	        public void onNothingSelected(AdapterView<?> parentView) {
	            // your code here
	        }

	    });
	}
	
	private void setupAutoBtnOnClick(View rootView) {
		final View frv = rootView;
		ImageButton autoBtn = (ImageButton) frv.findViewById(R.id.imageButton1);

		autoBtn.setOnClickListener(new View.OnClickListener()
		{
             @Override
             public void onClick(View v)
             {
            	AutoCompleteTextView actv = (AutoCompleteTextView) frv.findViewById(R.id.editText2);
 				pickedFriends.add(actv.getText().toString());

 				//ListView lv = (ListView) v.findViewById(R.id.listView1);
 				//ArrayAdapter<String> Adpter = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, pickedFriends);
 				//lv.setAdapter(Adpter);
 				//updateListView(frv);

 				actv.setText("");
 				actv.setHint("Invite Friends");

 				System.out.println("Add" + actv.getText().toString() + " mlkdasmd");
 				return ;
             } 
		}); 
	    
	}
	
	private void updateListView(View rootView) {
		System.out.println("Updated list view");
		for (String s: pickedFriends)
		{
			System.out.println("Friends are: " + s);
		}
		ListView lv = (ListView) rootView.findViewById(R.id.friendListView1);
		ArrayAdapter<String> Adpter = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, pickedFriends);
		lv.setAdapter(Adpter);
		
	}
	
	private void setupClearBtnOnClick(View rootView) {
		final View frv = rootView;
		Button autoBtn = (Button) frv.findViewById(R.id.clearButton);
		autoBtn.setOnClickListener(new View.OnClickListener()
		{
             @Override
             public void onClick(View v)
             {
            	Clear();
             } 
		}); 
	    
	}
	
	private void setupSubmitButtonOnClick(View rootView) {
		Button btn = (Button) rootView.findViewById(R.id.submitButton);
	    CreateAlert alerts = new CreateAlert(ctx);
	    final CreateAlert alert = alerts;
	    final View frvbtn = rootView;
	    btn.setOnClickListener(new View.OnClickListener()
		{
             @Override
             public void onClick(View v)
             {
            	 try {
 					String from_date = ((Button) frvbtn.findViewById(R.id.datepickButton)).getText().toString();
 					String to_date = ((Button) frvbtn.findViewById(R.id.datepickButton2)).getText().toString();
 					String from_time = ((Button) frvbtn.findViewById(R.id.timepickButton)).getText().toString();
 					String to_time = ((Button) frvbtn.findViewById(R.id.timepickButton2)).getText().toString();
 					String event_name = ((EditText) frvbtn.findViewById(R.id.editText1)).getText().toString();
 					String friendsStr = "";
 					for(int i = 0; i < pickedFriends.size(); i++) {
 						if (i != 0) {
 							friendsStr += ";";
 						}
 						System.out.println(pickedFriends.get(i));
 						FriendClass fr = db.getFriendFromName(pickedFriends.get(i));
 						if (fr != null) {
 							System.out.println("Added");
 							friendsStr+= fr.getEmail();
 						}
 					}
 					Spinner spinner4 = (Spinner) frvbtn.findViewById(R.id.spinner4); 
 					room = spinner4.getSelectedItem().toString();

			        SharedPreferences pref = ctx.getSharedPreferences("Event",ctx.MODE_PRIVATE);
			       // String from_date = pref.getString("from_date", "None");
			       // String to_date = pref.getString("to_date", "None");
			       // String from_time = pref.getString("from_time", "None");
			       // String to_time = pref.getString("to_time", "None");
 					System.out.println(from_date);
 					System.out.println(to_date);
 					System.out.println(from_time);
 					System.out.println(to_time);
 					System.out.println(event_name);
 					System.out.println(friendsStr);
 					System.out.println(room);
 					http_console.CreateEventRequest(friendsStr, from_time, to_time, room, 
 							event_name, from_date, to_date);
 					Clear();
 					
 				} catch (Exception e) {
 					e.printStackTrace();
 					alert.create_alert("Error", "You need to input all fields");
 				}
             } 
		}); 
	}
	
	private void Clear() {
        SharedPreferences pref = ctx.getSharedPreferences("Event",ctx.MODE_PRIVATE);
		pref.edit().remove("from_date").commit();
		pref.edit().remove("to_date").commit();
		pref.edit().remove("from_time").commit();
		pref.edit().remove("to_time").commit();
		((Button) rv.findViewById(R.id.datepickButton)).setText("Select a date");
		((Button) rv.findViewById(R.id.datepickButton2)).setText("Select a date");
		((Button) rv.findViewById(R.id.timepickButton)).setText("Select a time");
		((Button) rv.findViewById(R.id.timepickButton2)).setText("Select a time");
		pickedFriends.clear();
		((EditText) rv.findViewById(R.id.editText1)).setText("");
		((AutoCompleteTextView) rv.findViewById(R.id.editText2)).setText("");
	}
	
	private void setupRoomOnClick(View rootView) {
	    Spinner locList = (Spinner) rootView.findViewById(R.id.spinner4);
	    locList.setOnItemSelectedListener(new OnItemSelectedListener() {
	        @Override
	        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	            String room = parentView.getItemAtPosition(position).toString();
	            SharedPreferences pref = ctx.getSharedPreferences("Event",ctx.MODE_PRIVATE);
	            Editor edit = pref.edit();
	            edit.putString("room", room);
	            edit.commit();
	            
	        }

	        @Override
	        public void onNothingSelected(AdapterView<?> parentView) {
	            // your code here
	        }

	    });
	    
	    
	}

	private void setupDate1OnClick(View rootView) {
		final View frv = rootView;
		Button btn = (Button) frv.findViewById(R.id.datepickButton);
		btn.setOnClickListener(new View.OnClickListener()
		{
            @Override
            public void onClick(View v)
            {
            	DatePickerFragment newFragment = DatePickerFragment.newInstance(OrganizeEventFragment.this);
        	    newFragment.show(getChildFragmentManager(), "datePicker");
            } 
		}); 
	}
	
	private void setupDate2OnClick(View rootView) {
		final View frv = rootView;
		Button btn = (Button) frv.findViewById(R.id.datepickButton2);
		btn.setOnClickListener(new View.OnClickListener()
		{
            @Override
            public void onClick(View v)
            {
            	DatePickerFragment2 newFragment = DatePickerFragment2.newInstance(OrganizeEventFragment.this);
        	    newFragment.show(getChildFragmentManager(), "datePicker");
            } 
		}); 
	}

	private void setupTime1OnClick(View rootView) {
		final View frv = rootView;
		Button btn = (Button) frv.findViewById(R.id.timepickButton);
		btn.setOnClickListener(new View.OnClickListener()
		{
            @Override
            public void onClick(View v)
            {
            	TimePickerFragment newFragment = TimePickerFragment.newInstance(OrganizeEventFragment.this);
        	    newFragment.show(getChildFragmentManager(), "timePicker");
            } 
		}); 
	}

	private void setupTime2OnClick(View rootView) {
		final View frv = rootView;
		Button btn = (Button) frv.findViewById(R.id.timepickButton2);
		btn.setOnClickListener(new View.OnClickListener()
		{
            @Override
            public void onClick(View v)
            {
            	TimePickerFragment2 newFragment = TimePickerFragment2.newInstance(OrganizeEventFragment.this);
        	    newFragment.show(getChildFragmentManager(), "timePicker2");
            } 
		}); 
	}
	



	@Override
	public void updateDateButton(String date)
	{
		System.out.println("Date in updateDateButton is: "+date);
		Button dateButton = (Button) rv.findViewById(R.id.datepickButton);
		dateButton.setText(date);
	}

	@Override
	public void updateTimeButton (String time)
	{
		Button timeButton = (Button) rv.findViewById(R.id.timepickButton);
		timeButton.setText(time);
	}

	@Override
	public void updateDateButton2 (String date)
	{
		Button dateButton = (Button) rv.findViewById(R.id.datepickButton2);
		dateButton.setText(date);
	}

	@Override
	public void updateTimeButton2 (String time)
	{
		Button timeButton = (Button) rv.findViewById(R.id.timepickButton2);
		timeButton.setText(time);
	}
}