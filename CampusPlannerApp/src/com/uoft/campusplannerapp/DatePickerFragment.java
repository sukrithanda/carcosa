package com.uoft.campusplannerapp;

import java.util.Calendar;

import com.google.android.gms.maps.SupportMapFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	DateInterface i;
	OrganizeEventFragment ctx;
	public static DatePickerFragment newInstance(OrganizeEventFragment ctx) {
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.ctx = ctx;
		return fragment;
	}
	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the current date as the default date in the picker
	        final Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR);
	        int month = c.get(Calendar.MONTH);
	        int day = c.get(Calendar.DAY_OF_MONTH);

	        // Create a new instance of DatePickerDialog and return it
	        return new DatePickerDialog(getActivity(), this, year, month, day);
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
	    public void onDateSet(DatePicker view, int year, int month, int day) {
	    	
	    	String syear = Integer.toString(year);
	    	String smonth = Integer.toString(month+1);
	    	String sday = Integer.toString(day);
	    	
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(smonth);
	    	sb.append("/");
	    	sb.append(sday);
	    	sb.append("/");
	    	sb.append(syear);
	    	String date = sb.toString();
	    	
	    	System.out.println(date);
	    	
	    	//i.updateDateButton(date);
	    	ctx.updateDateButton(date);
	    }
	    

}