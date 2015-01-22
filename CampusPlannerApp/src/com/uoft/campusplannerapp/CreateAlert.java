package com.uoft.campusplannerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import com.uoft.campusplannerapp.HTTPConsole;

public class CreateAlert {
	Context ctx; 
	String user;
	public CreateAlert(Context ctxt){
		ctx = ctxt;
	}
	public void create_alert(String title, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title)
    		   .setMessage(message)
    	       .setCancelable(false)
    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                //do things
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
	}
	
	public void create_input_alert_for_add_friend(String title, String message, String email){
		AlertDialog.Builder alert = new AlertDialog.Builder(ctx);

		alert.setTitle(title);
		alert.setMessage(message);
		
		user = email;

		// Set an EditText view to get user input 
		final EditText input = new EditText(ctx);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			try {
				String email = input.getText().toString();
				HTTPConsole http_console = new HTTPConsole(ctx);
				http_console.AddFriend(user, email);
			} catch (Exception e) {
				
			}
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}

}
