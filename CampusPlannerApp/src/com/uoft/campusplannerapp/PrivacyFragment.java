package com.uoft.campusplannerapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

public class PrivacyFragment extends Fragment {
	Context ctx;
	private HTTPConsole http_console;
	CreateAlert alert;
	public static final String TAG = "privacy";
	View rv;
	public static PrivacyFragment newInstance(Context ctx) {
		PrivacyFragment fragment = new PrivacyFragment();
		fragment.ctx = ctx;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		http_console = new HTTPConsole(ctx);
	    alert = new CreateAlert(ctx);
		View rootView = inflater.inflate(R.layout.privacy_layout, container,
				false);
		Button button = (Button) rootView.findViewById(R.id.locationButton);
		rv = rootView;
		updateState(rv);
		button.setOnClickListener(new View.OnClickListener()
		{
             @Override
             public void onClick(View v)
             {
            	// Is the toggle on?
            	ToggleButton tv = (ToggleButton) v;
				boolean on = tv.isChecked();
				
				if (on) {
				    boolean sts = http_console.ShowLocation();
				    if (sts == false) {
				    	alert.create_alert("Error", "Couldnt enable location due to server error");
				    	tv.setChecked(false);
				    }
				} else {
				    boolean sts = http_console.HideLocation();
				    if (sts == false) {
				    	alert.create_alert("Error", "Couldnt disable location due to server error");
				    	tv.setChecked(true);
				    }
				}
             } 
		}); 
	    return rootView;
	}
	
	private void updateState(View v){
		String state = http_console.GetPermission();
		ToggleButton tv = (ToggleButton) v.findViewById(R.id.locationButton);
		if (state.equals("Invalid Request")) {
			alert.create_alert("Error", "Cant read current state due to server error");
		} else if (state.equals("Hide")) {
			tv.setChecked(false);
			//tv.drawableStateChanged();
		} else if (state.equals("Show")) {
			tv.setChecked(true);
		}
	}
	
	
	@Override
    public void onResume() {
		super.onResume();
		System.out.println("Entered resume");
		updateState(rv);
    }

}
