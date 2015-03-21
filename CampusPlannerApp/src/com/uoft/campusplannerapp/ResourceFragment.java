package com.uoft.campusplannerapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ResourceFragment extends Fragment{
	Context ctx;
	private HTTPConsole http_console;
	CreateAlert alert;
	MainActivity x;
	public static final String TAG = "resources";
	public static ResourceFragment newInstance(Context ctx, MainActivity x) {
		ResourceFragment fragment = new ResourceFragment();
		fragment.ctx = ctx;
		fragment.x = x;
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
         		int num_resources = 0; 
         		if (rsc != null)
         			num_resources = rsc.size();
     		    String names[] = new String[num_resources];
     		    int i = 0; 
     		    for (i = 0; i < num_resources; i++){
     		    	ResourceClass fr = rsc.get(i);
     		    	names[i] = fr.getResource();
     		    }
     		    ArrayAdapter<String> fr_adp = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,lText.getId(),names);
     		    lv.setAdapter(fr_adp);

     		    lv.setOnItemClickListener(new OnItemClickListener() {
     	           public void onItemClick(AdapterView<?> parent, View view,
     	               int position, long id) {
     	               String room = parent.getItemAtPosition(position).toString();
     	            //  new httpcall().execute(room);

     	              List<ResourceClass> path = http_console.getPath(room);
     	              //new httpcall().execute(room);
     	               ArrayList<MarkerFloorPairs> route_markers = new ArrayList<MarkerFloorPairs>();
     	             // try { Thread.sleep(5000); }
     	             // catch (InterruptedException e) { e.printStackTrace(); }
     	               String path_s = "";
     	               int i = 0; 
     	               int size = 0; 
     	               if (path != null) {
     	            	   size = path.size();

     	               }
     	               for (i = 0; i < size; i++) {
     	            	   if (i!=0) {
     	            		   path_s += "\n";
     	            	   }
     	            	
     	            	   
     	            	   path_s += path.get(i).getResource();
     	        	   
     	            	  
     	               }
	     	         

     	              if (path !=null)
    	            	  x.showpath(path);

     	           }
     	         });
     		    
     		    		

             } 
		}); 
	    return rootView;
	
	}
}