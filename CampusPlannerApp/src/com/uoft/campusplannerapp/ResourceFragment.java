package com.uoft.campusplannerapp;

import java.util.ArrayList;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
		System.out.println("DEBUG: RESOURCE ONCREATE");

		http_console = new HTTPConsole(ctx);
	    alert = new CreateAlert(ctx);
		View rootView = inflater.inflate(R.layout.resource_layout, container,
				false);
		Button button = (Button) rootView.findViewById(R.id.findresourcebtn);
		final View frv = rootView; 
		Spinner main = (Spinner) rootView.findViewById(R.id.resourceSpinnerMain);
		main.setOnItemSelectedListener(new OnItemSelectedListener() {

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

	            Spinner locList = (Spinner) frv.findViewById(R.id.resourceSpinner);
	            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,roomList);
	            locList.setAdapter(adapter);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
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
         		if (rsc == null) {
         			alert.create_alert("Error", "No " + text + " found near you");
         		}
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
     		    final List<ResourceClass> f_rsc = rsc;
     		    final Context f_ctx = ctx;
     		    ArrayAdapter<String> fr_adp = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,lText.getId(),names);
     		    lv.setAdapter(fr_adp);
     		    lv.setOnItemClickListener(new OnItemClickListener() {
     	           public void onItemClick(AdapterView<?> parent, View view,
     	               int position, long id) {
     	               String room = parent.getItemAtPosition(position).toString();
     	              
     	               ResourceClass cur = null;
     	               int i;
     	               for (i = 0; i < f_rsc.size(); i++) {
     	            	   cur = f_rsc.get(i);
     	     			   if (room.equals(cur.getResource())) {
     	     				   break;
     	     			   }
     	               }
     	               if (null == cur) {
     	            	   return;
     	               }
     	               List<ResourceClass> path = http_console.getPath(cur.getResource());
     	               for (i = 0; i < 8; i ++){
     	              	   x.hidemarkers(i);
     	               }
     	               if (path !=null) {
     	            	   System.out.println(path.get(0).getResource());
     	               }
     	               locateOrRoute(f_ctx,cur,path);
     	            
     	           }
     	         });
             } 
		}); 
		
		
	    return rootView;
	}
	
	private void locateOrRoute(Context ctx,ResourceClass cur, List<ResourceClass> path){
		int i = 0; 
		AlertDialog.Builder custon_alert = new AlertDialog.Builder(ctx);
		custon_alert.setTitle(cur.getResource());
		custon_alert.setMessage(cur.getDescription());
		final ResourceClass f_cur = cur;
		final List<ResourceClass> f_path = path;
		custon_alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			System.out.println(f_cur.getResource());
//			x.showFriend(f_cur.getLoc().getFloor(), f_cur.getLoc().getLatitude(), f_cur.getLoc().getLongitude(),
//					f_cur.getResource(), "None");
			return;
		  }
		});

		custon_alert.setPositiveButton("Route", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
			  if (f_path == null) {
				  alert.create_alert("Error", "No Path found");
			  } else {
				  x.showpath(f_path);
			  }
		  }
		});

		custon_alert.show();
		
	}
}
