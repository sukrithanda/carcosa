package com.uoft.campusplannerapp;

import java.util.List;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.uoft.campusplannerapp.HTTPConsole;
import com.uoft.campusplannerapp.CreateAlert;
import com.uoft.campusplannerapp.FriendClass;
import com.uoft.campusplannerapp.DatabaseHandler;
import com.uoft.campusplannerapp.User;
import com.uoft.campusplannerapp.R;

public class FriendsFragment extends Fragment {
	private HTTPConsole http_console;
	private CreateAlert alert;
	private List<FriendClass> my_friends;
	private String user; 
	private DatabaseHandler db;
	/** Called when the activity is first created. */
	public Context ctx;
	public static final String TAG = "friends";
	LayoutInflater inftr; 
	ViewGroup ctr;
	View rv = null;
	
	public static FriendsFragment newInstance(Context ctx) {
		FriendsFragment fragment = new FriendsFragment();
		fragment.ctx = ctx;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.friend_layout, container,
				false);
		rv = rootView;
	    //super.onCreate(savedInstanceState);
	    http_console = new HTTPConsole(ctx);
	    alert = new CreateAlert(ctx);
	    get_friends(rootView);
	    Button button = (Button) rootView.findViewById(R.id.addfriendbtn);
		button.setOnClickListener(new View.OnClickListener()
		{
             @Override
             public void onClick(View v)
             {
         		alert.create_input_alert_for_add_friend("Add Friend", "Enter email", user);
             } 
		}); 
	    return rootView;
	}
	
	private void get_friends(View rootView){
	    db = new DatabaseHandler(ctx);
		User u = db.getUser();
		db.Close();
		if (u == null) { 
			return;
		}
		user = u.getEmail();
	    my_friends = http_console.GetFriend(user);
	    if (my_friends != null){
		    TextView lText = new TextView(ctx);
	        lText.setId(0); 
		    ListView lv = (ListView) rv.findViewById(R.id.friendlistview);
		    int num_friends = my_friends.size();
		    String names[] = new String[num_friends];
		    int i = 0; 
		    for (i = 0; i < num_friends; i++){
		    	FriendClass fr = my_friends.get(i);
		    	names[i] = fr.getFirst_name() + " " + fr.getLast_name();
		    }
		    ArrayAdapter<String> fr_adp = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,lText.getId(),names);
		    lv.setAdapter(fr_adp);
		    final Context f_ctx = ctx;
		    
	        lv.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view,
	              int position, long id) {
	              createMessageOrLocate(f_ctx,view);
	          }
	        });
	    }
	}
	
	private void createMessageOrLocate(Context ctx, View v){
		AlertDialog.Builder custon_alert = new AlertDialog.Builder(ctx);
		TextView txt = (TextView) v;
		String name = txt.getText().toString();
		String names[] = name.split(Pattern.quote(" "));
		String email = "none";
		String fn = "none";
		int i = 0; 
		for (i = 0; i < my_friends.size(); i++){
			FriendClass friend = my_friends.get(i);
			if (friend.getFirst_name().equals(names[0]) && friend.getLast_name().equals(names[1])){
				email = friend.getEmail();
				fn = friend.getFirst_name();
				break;
			}
		}
		final String f_first_name = fn;
		final String f_email = email;
		custon_alert.setTitle(name);
		custon_alert.setMessage("");

		// Set an EditText view to get user input 
		final EditText input = new EditText(ctx);
		custon_alert.setView(input);
		custon_alert.setPositiveButton("Message", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			try {
				String msg = input.getText().toString();
				String mod_msg = msg.replaceAll(" ", "%20");
				
				http_console.SendMessage(user, f_email, mod_msg);
			} catch (Exception e) {
				
			}
		  }
		});

		custon_alert.setNegativeButton("Locate", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
				String result = http_console.LocateFriend(user, f_email);
				if (result.equals("Failed")){
					alert.create_alert("Error", f_first_name + " doesnt wish to share location right now");
				}
				//View rootView = inftr.inflate(R.layout.fragment_map, ctr, false);
		  }
		});

		custon_alert.show();
		
	}
	
	@Override
    public void onResume() {
		if (rv != null)
        	get_friends(rv);
		super.onResume();
    }
}
