package com.uoft.campusplannerapp;

import java.util.Calendar;
import android.support.v4.app.DialogFragment;
import android.app.Activity;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	
	DateInterface i;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
	 @Override
	 	public void onAttach(Activity activity) {
		 	super.onAttach(activity);
		 	try
		 	{
		 		i = (DateInterface) activity;
		 	}
		 	catch (ClassCastException e)
		 	{
		 		// To Be implemented
		 		System.out.println("Error in attaching activity");
		 	}
		 
	 	}
	 
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
    	String shour = Integer.toString(hourOfDay);
    	String sminute = Integer.toString(minute);
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append(shour);
    	sb.append(".");
    	sb.append(sminute);
    	
    	String time = sb.toString();
    	
    	i.updateTimeButton(time);
	}
	
}
