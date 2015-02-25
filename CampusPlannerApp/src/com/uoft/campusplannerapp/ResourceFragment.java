package com.uoft.campusplannerapp;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ResourceFragment extends Fragment{
	Context ctx;
	private HTTPConsole http_console;
	CreateAlert alert;
	public static final String TAG = "resources";
	public static ResourceFragment newInstance(Context ctx) {
		ResourceFragment fragment = new ResourceFragment();
		fragment.ctx = ctx;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		http_console = new HTTPConsole(ctx);
	    alert = new CreateAlert(ctx);
		View rootView = inflater.inflate(R.layout.resource_layout, container,
				false);
		Button button = (Button) rootView.findViewById(R.id.findresourcebtn);
		final View frv = rootView; 
		button.setOnClickListener(new View.OnClickListener()
		{
             @Override
             public void onClick(View v)
             {
            	TextView lText = new TextView(ctx);
    	        lText.setId(0); 
    	        Spinner spinner = (Spinner)frv.findViewById(R.id.resourceSpinner);
    	        String text = spinner.getSelectedItem().toString();
         		List<ResourceClass> rsc = http_console.getResources(text);
         		ListView lv = (ListView) frv.findViewById(R.id.resourcelistview);
         		int num_resources = rsc.size();
     		    String names[] = new String[num_resources];
     		    int i = 0; 
     		    for (i = 0; i < num_resources; i++){
     		    	ResourceClass fr = rsc.get(i);
     		    	names[i] = fr.getResource();
     		    }
     		    ArrayAdapter<String> fr_adp = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,lText.getId(),names);
     		    lv.setAdapter(fr_adp);
             } 
		}); 
	    return rootView;
	}
}
