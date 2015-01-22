package com.uoft.campusplannerapp;

import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.uoft.campusplannerapp.HTTPConsole;
import com.uoft.campusplannerapp.CreateAlert;
import com.uoft.campusplannerapp.FriendClass;
import com.uoft.campusplannerapp.MovingImage;

public class FriendActivity extends Activity {
	private HTTPConsole http_console;
	private CreateAlert alert;
	private List<FriendClass> my_friends;
	private String user; 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    http_console = new HTTPConsole(this);
	    setContentView(R.layout.friend_layout);
	    alert = new CreateAlert(this);
	    get_friends();
	}
	
	private void get_friends(){
		SharedPreferences pref = getSharedPreferences("User", Context.MODE_PRIVATE);
	    user = pref.getString("user", "none");
	    my_friends = http_console.GetFriend(user);
	    if (my_friends != null){
		    LinearLayout sv = (LinearLayout)findViewById(R.id.friendlistlinear);
		    int i = 0; 
		    final Context f_ctx = this;
		    for (i = 0; i < my_friends.size(); i++){
		    	FriendClass fr = my_friends.get(i);
		    	Button btn = new Button(this);
		    	btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		    	btn.setText(fr.getFirst_name() + " " + fr.getLast_name());
		    	final int id = btn.getId();
		    	btn.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View v) {
		            	createMessageOrLocate(f_ctx, v);
		            }
		        });
			    sv.addView(btn);
		    }
	    }
	}
	
	private void createMessageOrLocate(Context ctx, View v){
		AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
		Button btn = (Button) v;
		String name = btn.getText().toString();
		String names[] = name.split(Pattern.quote(" "));
		String email = "none";
		int i = 0; 
		for (i = 0; i < my_friends.size(); i++){
			FriendClass friend = my_friends.get(i);
			if (friend.getFirst_name().equals(names[0]) && friend.getLast_name().equals(names[1])){
				email = friend.getEmail();
				break;
			}
		}
		final String f_email = email;
		alert.setTitle(name);
		alert.setMessage("");


		// Set an EditText view to get user input 
		final EditText input = new EditText(ctx);
		alert.setView(input);
		final Context f_ctx = ctx;
		alert.setPositiveButton("Message", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			try {
				String msg = input.getText().toString();
				String mod_msg = msg.replaceAll(" ", "%20");
				
				http_console.SendMessage(user, f_email, mod_msg);
			} catch (Exception e) {
				
			}
		  }
		});

		alert.setNegativeButton("Locate", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
				http_console.LocateFriend(user, f_email);
				setContentView(new MovingImage(f_ctx));
		  }
		});

		alert.show();
		
	}
	
	public boolean add_friend(View view){
		alert.create_input_alert_for_add_friend("Add Friend", "Enter email", user);
		return true;
	}
	
	private void GetLocAndPlot(View v){
		Button btn = (Button) v;
		String name = btn.getText().toString();
		String names[] = name.split(Pattern.quote(" "));
		String email = "none";
		int i = 0; 
		for (i = 0; i < my_friends.size(); i++){
			FriendClass friend = my_friends.get(i);
			if (friend.getFirst_name().equals(names[0]) && friend.getLast_name().equals(names[1])){
				email = friend.getEmail();
				break;
			}
		}
		http_console.LocateFriend(user, email);
		setContentView(new MovingImage(this));
		
		
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.signout) {
            boolean sts = http_console.Logout();
            if (sts == true){
            	setContentView(R.layout.activity_main);
            } else { 
            	alert.create_alert("Error","Signout Failed. Try again later");
            }
        } else if (id == R.id.friend) {
        	setContentView(R.layout.friend_layout);
        	get_friends();
        }
        return super.onOptionsItemSelected(item);
    }

}
